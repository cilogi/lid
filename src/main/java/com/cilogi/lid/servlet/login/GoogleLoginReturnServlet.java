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
import com.cilogi.lid.cookie.Site;
import com.cilogi.lid.guice.annotations.CookieExpireDays;
import com.cilogi.lid.guice.annotations.DefaultRedirect;
import com.cilogi.lid.guice.annotations.HandleRedirect;
import com.cilogi.lid.guice.annotations.HttpOnly;
import com.cilogi.lid.servlet.BaseServlet;
import com.cilogi.lid.servlet.handle.IHandleHolder;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@Singleton
public class GoogleLoginReturnServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(GoogleLoginReturnServlet.class);

    private static final long serialVersionUID = 4695841156936686023L;

    private final long cookieExpireDays;
    private final String defaultRedirect;
    private final String handleRedirect;
    private final boolean httpOnly;
    private final ILoginAction loginAction;
    private final IHandleHolder handleHolder;

    @Inject
    public GoogleLoginReturnServlet(@CookieExpireDays long cookieExpireDays,
                                    @DefaultRedirect String defaultRedirect,
                                    @HandleRedirect String handleRedirect,
                                    @HttpOnly boolean httpOnly,
                                    ILoginAction loginAction,
                                    IHandleHolder handleHolder) {
        this.cookieExpireDays = cookieExpireDays;
        this.defaultRedirect = defaultRedirect;
        this.handleRedirect = handleRedirect;
        this.httpOnly = httpOnly;
        this.loginAction = loginAction;
        this.handleHolder = handleHolder;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = UserServiceFactory.getUserService().getCurrentUser();
        String redirectURL = stringParameter("redirect", request, defaultRedirect);
        LOG.info("Google login redirect to " + redirectURL);
        if (user != null) {
            String email = user.getEmail();
            CookieInfo info = new CookieInfo(email)
                    .setName(user.getNickname())
                    .setSite(Site.google)
                    .expire(cookieExpireDays, TimeUnit.DAYS);
            CookieHandler handler = new CookieHandler(httpOnly);
            handler.setCookie(request, response, info);
            LidUser.setInfo(info);
            loginAction.act(info);

            String hr = handleRedirect();
            if (hr != null) {
                String fragment = fragmentOf(redirectURL);
                if (fragment != null) {
                    response.sendRedirect(response.encodeRedirectURL(hr + "?fragment=" + fragment + "&redirect=" + redirectURL));
                } else {
                    response.sendRedirect(response.encodeRedirectURL(hr + "?redirect=" + redirectURL));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL(redirectURL));
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL(defaultRedirect));
        }
    }


    // we want to redirect here if the logged in user doesn't have a
    //
    private String handleRedirect() {
        String userName = LidUser.userID();
        if (userName == null) {
            return null;
        } else {
            String handle = handleHolder.handle(userName);
            return (handle == null) ? handleRedirect : null;
        }
    }

    private String fragmentOf(String uriString) {
        try {
            URI uri = new URI(uriString);
            return uri.getFragment();
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
