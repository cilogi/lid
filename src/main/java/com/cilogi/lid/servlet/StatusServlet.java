// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        StatusServlet.java  (08/08/15)
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

import com.cilogi.lid.cookie.Site;
import com.cilogi.lid.user.LidUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class StatusServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(StatusServlet.class);
    private static final long serialVersionUID = -7581469173812850564L;

    @Inject
    public StatusServlet() {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = LidUser.getCurrentUser();
        Site site = LidUser.getCurrentSite();
        issueJson(response, HttpServletResponse.SC_OK,
                "user", (user == null) ? "" : user,
                "site", (site == null) ? "" : site.name());
    }

}
