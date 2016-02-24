// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        GoogleLoginServlet.java  (09/08/15)
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


package com.cilogi.lid.servlet.login;

import com.cilogi.lid.filter.AuthFilter;
import com.cilogi.lid.guice.annotations.AuthRedirect;
import com.cilogi.lid.guice.annotations.DefaultRedirect;
import com.cilogi.lid.guice.annotations.HandleRedirect;
import com.cilogi.lid.servlet.BaseServlet;
import com.cilogi.lid.servlet.handle.IHandleHolder;
import com.cilogi.lid.user.LidUser;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.net.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Singleton
public class GoogleLoginServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(GoogleLoginServlet.class);

    private static final long serialVersionUID = -3344803219460057213L;

    private final IHandleHolder handleHolder;
    private final String handleRedirect;
    private final String defaultRedirect;

    @Inject
    public GoogleLoginServlet(IHandleHolder handleHolder,
                              @HandleRedirect String handleRedirect,
                              @DefaultRedirect String defaultRedirect) {
        this.handleHolder = handleHolder;
        this.handleRedirect = handleRedirect;
        this.defaultRedirect = defaultRedirect;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String redirectURL = redirect(request);
        try {
            String url = UserServiceFactory.getUserService().createLoginURL("/login/googleReturn?redirect=" + URLEncoder.encode(redirectURL, "UTF-8"));
            response.sendRedirect(url);
        } catch (Exception e) {
            LOG.warn("Can't login: " + e.getMessage());
            issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can't login: " + e.getMessage(), response);
        }
    }

    private String redirect(HttpServletRequest request) {
        String redirectURL = request.getParameter("redirect");
        if (redirectURL == null) {
            redirectURL = getSession(AuthFilter.authRedirectKey(), request, defaultRedirect);
        }
        return redirectURL;
    }
}
