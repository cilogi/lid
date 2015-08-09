// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        GoogleLoginReturnServlet.java  (09/08/15)
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

import com.cilogi.lid.cookie.CookieHandler;
import com.cilogi.lid.cookie.CookieInfo;
import com.cilogi.lid.guice.annotations.CookieExpireDays;
import com.cilogi.lid.servlet.BaseServlet;
import com.cilogi.lid.cookie.Site;
import com.cilogi.lid.user.LidUser;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Singleton
public class GoogleLoginReturnServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(GoogleLoginReturnServlet.class);

    private static final String DEFAULT_REDIRECT = "/index.html"; // must not require auth
    private static final long serialVersionUID = 4695841156936686023L;

    private final long cookieExpireDays;

    @Inject
    public GoogleLoginReturnServlet(@CookieExpireDays long cookieExpireDays) {
        this.cookieExpireDays = cookieExpireDays;;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = UserServiceFactory.getUserService().getCurrentUser();
        String redirectURL = stringParameter("redirect", request, DEFAULT_REDIRECT);;
        if (user != null) {
            String email = user.getEmail();
            CookieInfo info = new CookieInfo(email)
                    .setSite(Site.google)
                    .expire(cookieExpireDays, TimeUnit.DAYS);
            CookieHandler handler = new CookieHandler();
            handler.setCookie(request, response, info);
            LidUser.setCurrentUser(email);
            response.sendRedirect(redirectURL);
        } else {
            response.sendRedirect(DEFAULT_REDIRECT);
        }
    }
}
