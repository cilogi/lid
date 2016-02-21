// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        HandleFilter.java  (15/10/15)
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


package com.cilogi.lid.servlet.handle;

import com.cilogi.lid.guice.annotations.HandleRedirect;
import com.cilogi.lid.user.LidUser;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class HandleFilter implements Filter {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(HandleFilter.class);

    private static ConcurrentHashMap<String,Boolean> MAP = new ConcurrentHashMap<>();

    private final IHandleHolder handleHolder;
    private final String handleRedirect;

    @Inject
    public HandleFilter(@NonNull IHandleHolder handleHolder, @HandleRedirect String handleRedirect) {
        this.handleHolder = handleHolder;
        this.handleRedirect = handleRedirect;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if(httpRequest.getMethod().equalsIgnoreCase("GET")){
            String lidUser = LidUser.userID();
            if (lidUser == null || handleRedirect == null || httpRequest.getRequestURI().endsWith(handleRedirect)) {
                chain.doFilter(request, response);
            } else {
                if (MAP.get(lidUser) != null) {
                    chain.doFilter(request, response);
                } else {
                    String handle = handleHolder.handle(lidUser);
                    if (handle == null) {
                        HttpServletResponse httpResponse = (HttpServletResponse)response;
                        httpResponse.sendRedirect(httpResponse.encodeRedirectURL(handleRedirect));
                    } else {
                        MAP.put(lidUser, true);
                        chain.doFilter(request, response);
                    }
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
