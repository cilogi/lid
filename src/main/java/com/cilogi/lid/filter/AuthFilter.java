// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        AuthFilter.java  (09/08/15)
// Author:      tim
//
// Copyright in the whole and every part of this source file belongs to
// Cilogi (the Author) and may not be used, sold, licenced, 
// transferred, copied or reproduced in whole or in part in 
// any manner or form or in or on any media to any person other than 
// in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


package com.cilogi.lid.filter;

import com.cilogi.lid.guice.annotations.AuthRedirect;
import com.cilogi.lid.user.LidUser;
import com.cilogi.lid.util.session.SessionAttributes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Singleton
public class AuthFilter implements Filter {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    private static final String DEFAULT_REDIRECT = "/login.html";


    private final List<Pattern> authorizedPaths;
    private final String authRedirect;

    @Inject
    public AuthFilter(@AuthRedirect String authRedirect) {
        authorizedPaths = Lists.newArrayList();
        this.authRedirect = authRedirect;
    }

    @Override public void init(FilterConfig filterConfig) throws ServletException {
        try {
            List<String> paths = new ObjectMapper().readValue(getClass().getResourceAsStream("/auth.json"), new TypeReference<List<String>>(){});
            for (String path : paths) {
                authorizedPaths.add(Pattern.compile(path));
            }
            LOG.info("there are " + authorizedPaths.size() + " paths");
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override public void destroy() {}

    @Override
    @SuppressWarnings({"unchecked"})
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest sr = (HttpServletRequest)request;
        String uri = sr.getRequestURI();
        String path = uri.startsWith("/") ? uri.substring(1) : uri;
        boolean authRequired = false;
        for (Pattern pattern : authorizedPaths) {
            if (pattern.matcher(path).matches()) {
                authRequired = true;
                break;
            }
        }
        if (authRequired) {
            String user = LidUser.userID();
            if (user == null) {
                new SessionAttributes(sr).put(authRedirect, uri);
                ((HttpServletResponse)response).sendRedirect(DEFAULT_REDIRECT);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }



}
