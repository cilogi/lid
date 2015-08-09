// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        LogoutServlet.java  (09/08/15)
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


package com.cilogi.lid.servlet;

import com.cilogi.lid.cookie.CookieHandler;
import com.cilogi.lid.cookie.CookieInfo;
import com.cilogi.lid.cookie.Site;
import com.cilogi.lid.guice.annotations.DefaultRedirect;
import com.cilogi.lid.user.LidUser;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class LogoutServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(LogoutServlet.class);

    private static final long serialVersionUID = -5286590258162363047L;

    private final String defaultRedirect;

    @Inject
    public LogoutServlet(@DefaultRedirect String defaultRedirect) {
        this.defaultRedirect = defaultRedirect;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String redirectUrl = stringParameter("redirect", request, defaultRedirect);
        try {
            LidUser.setCurrentUser(null);
            CookieHandler handler = new CookieHandler();
            CookieInfo info = handler.removeCookie(request, response);
            if (info != null && info.getSite() == Site.google) {
                logoutGoogle(redirectUrl, response);
            }
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            LOG.warn("Error logging out: " + e.getMessage());
            response.sendRedirect(redirectUrl);
        }
    }

    static void logoutGoogle(String redirectUrl, HttpServletResponse response) throws IOException {
        UserService service = UserServiceFactory.getUserService();
        User user = service.getCurrentUser();
        if (user != null) {
            String logoutUrl = service.createLogoutURL(redirectUrl);
            response.sendRedirect(logoutUrl);
        }
    }

}
