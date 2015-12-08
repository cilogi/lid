// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        TokenFilter.java  (02/12/14)
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

import com.cilogi.lid.guice.annotations.ApiHeader;
import com.cilogi.lid.guice.annotations.ApiToken;
import com.cilogi.lid.user.LidUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class TokenFilter implements Filter {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(TokenFilter.class);

    //private ServletContext context;
    private final String apiToken;
    private final String apiHeader;

    @Inject
    public TokenFilter(@ApiToken String apiToken, @ApiHeader String apiHeader) {
        this.apiToken = apiToken;
        this.apiHeader = apiHeader;
    }

    public void init(FilterConfig fConfig) throws ServletException {
        //this.context = fConfig.getServletContext();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String user = LidUser.userID();

        String header = req.getHeader(apiHeader);
        if (user != null || (header != null && header.equals(apiToken))) {
            //((HttpServletRequest) request).getSession(true).setAttribute("currentUserName", "tim.niblett@cilogi.com");
            chain.doFilter(request, response);
        } else {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().println("Permission denied");
        }
    }


    public void destroy() {
        // nothing
    }

}
