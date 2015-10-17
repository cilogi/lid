// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        SessionAttributes.java  (09/08/15)
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


package com.cilogi.lid.util.session;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class SessionAttributes {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(SessionAttributes.class);

    private final HttpServletRequest request;

    public SessionAttributes(@NonNull HttpServletRequest request) {
        this.request = request;
    }

    public void put(@NonNull String key, @NonNull String value) {
        HttpSession session = request.getSession(true);
        session.setAttribute(key, value);
    }

    public String get(@NonNull String key, String deflt) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return deflt;
        } else {
            String value = (String)session.getAttribute(key);
            return (value == null) ? deflt : value;
        }
    }

    public String getAndDelete(@NonNull String key, String deflt) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return deflt;
        } else {
            String value = (String)session.getAttribute(key);
            if (value != null) {
                session.removeAttribute(key);
            }
            return (value == null) ? deflt : value;
        }
    }
}
