// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        EmailLoginServlet.java  (08/08/15)
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

import com.cilogi.lid.guice.annotations.AuthRedirect;
import com.cilogi.lid.servlet.BaseServlet;
import com.cilogi.lid.util.ISendEmail;
import com.cilogi.lid.util.session.SessionAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class EmailLoginServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(EmailLoginServlet.class);
    private static final long serialVersionUID = -7329056757135899836L;

    private final ISendEmail sendLoginEmail;
    private final String authRedirect;

    @Inject
    public EmailLoginServlet(ISendEmail sendLoginEmail, @AuthRedirect String authRedirect) {
        this.sendLoginEmail = sendLoginEmail;
        this.authRedirect = authRedirect;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        String redirect = request.getParameter("redirect");
        String redirectURL = (redirect == null) ? new SessionAttributes(request).getAndDelete(authRedirect, null) : redirect;
        if (redirect != null) {
            putSession(authRedirect, redirect, request);
        }

        if (email == null) {
            issueJson(response, HttpServletResponse.SC_BAD_REQUEST, "message", "There is no 'email' parameter");
        } else {
            if (!sendLoginEmail.isLegalEmail(email)) {
                boolean isEmpty = "".equals(email.trim());
                issueJson(response, HttpServletResponse.SC_BAD_REQUEST, "message",
                        isEmpty ? "Email value is empty" : "Email address " + email + " is invalid");
            } else {
                try {
                    sendLoginEmail.send(email, redirectURL);
                    issueJson(response, HttpServletResponse.SC_OK, "message", "Email sent to " + email + ", please click link in Email to log in");
                } catch (Exception emailEx) {
                    issueJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "message", "Internal error: " + emailEx.getMessage());
                }
            }
        }
    }
}
