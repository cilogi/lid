// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        MailLoginServlet.java  (08/08/15)
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
import com.cilogi.lid.servlet.BaseServlet;
import com.cilogi.lid.user.LidUser;
import com.google.common.net.MediaType;
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
public class EmailLoginReturn extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(EmailLoginReturn.class);

    private static final long serialVersionUID = 1650249303865008594L;

    private final long cookieExpireDays;
    private final String defaultRedirect;
    private final ILoginAction loginAction;

    @Inject
    public EmailLoginReturn(@CookieExpireDays long cookieExpireDays, @DefaultRedirect String defaultRedirect, ILoginAction loginAction) {
        this.cookieExpireDays = cookieExpireDays;
        this.defaultRedirect = defaultRedirect;
        this.loginAction = loginAction;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null) {
            issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_BAD_REQUEST, "No token", response);
        } else {
            CookieInfo info = null;
            try {
                info = CookieInfo.fromEncryptedString(token);
                if (info.isExpired()) {
                    issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_BAD_REQUEST,
                            "Token has expired, you'll need to ask for a new one", response);
                    return;
                }
            } catch (RuntimeException e) {
                issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_BAD_REQUEST,
                        "Can't parse token: " + e.getMessage(), response);
                return;
            }
            try {
                // create a new cookie so we get a different SALT to go with the different date
                if (info == null || info.getId() == null) {
                    issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "The info is missing or corrupt: " + info, response);
                    return;
                } else {
                    CookieInfo newInfo = new CookieInfo(info.getId())
                            .setName(info.getName())
                            .setSite(Site.email)
                            .expire(cookieExpireDays, TimeUnit.DAYS);
                    CookieHandler handler = new CookieHandler();
                    handler.setCookie(request, response, newInfo);
                    LidUser.setInfo(newInfo);
                    loginAction.act(newInfo);
                    response.sendRedirect(redirectURL(info));
                }
            } catch (Exception e) {
                issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Can't set user: " + e.getMessage(), response);
                return;
            }
        }
    }

    private String redirectURL(CookieInfo info) {
        return (info == null || info.getRedirect() == null) ? defaultRedirect : info.getRedirect();
    }

}
