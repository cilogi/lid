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


package com.cilogi.lid.servlet;

import com.cilogi.lid.cookie.CookieHandler;
import com.cilogi.lid.cookie.CookieInfo;
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

@Singleton
public class MailReplyServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(MailReplyServlet.class);

    private static final String REDIRECT_ON_SUCCESS = "/index.html";

    @Inject
    public MailReplyServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null) {
            issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_BAD_REQUEST, "No token", response);
        } else {
            CookieInfo info = null;
            try {
                info = CookieHandler.decode(token);
            } catch (RuntimeException e) {
                issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_BAD_REQUEST,
                        "Can't parse token: " + e.getMessage(), response);
            }
            try {
                CookieHandler handler = new CookieHandler();
                handler.setCookie(request, response, info);
                LidUser.setCurrentUser(info.getEmail());
                response.sendRedirect(REDIRECT_ON_SUCCESS);
            } catch (Exception e) {
                issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Can't set user: " + e.getMessage(), response);
            }
        }
    }

}
