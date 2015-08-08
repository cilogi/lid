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


package com.cilogi.lid.servlet;

import com.cilogi.lid.util.SendLoginEmail;
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

    private final SendLoginEmail sendLoginEmail;

    @Inject
    public EmailLoginServlet(SendLoginEmail sendLoginEmail) {
        this.sendLoginEmail = sendLoginEmail;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        if (email == null) {
            issueJson(response, HttpServletResponse.SC_BAD_REQUEST, "message", "There is no 'email' parameter");
        } else {
            if (!sendLoginEmail.isLegalEmail(email)) {
                issueJson(response, HttpServletResponse.SC_BAD_REQUEST, "message", "Email address " + email + " is invalid");
            } else {
                sendLoginEmail.send(email);
                issueJson(response, HttpServletResponse.SC_OK, "message", "Email sent to " + email);
            }
        }
    }
}
