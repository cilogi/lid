// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        SavedRedirect.java  (09/08/15)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SavedRedirect {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(SavedRedirect.class);

    private static final String REDIRECT_KEY = "PostLoginRedirect";

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public SavedRedirect(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public String get() {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (String)session.getAttribute(REDIRECT_KEY);
    }

    public void clear() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(REDIRECT_KEY);
        };
    }
}
