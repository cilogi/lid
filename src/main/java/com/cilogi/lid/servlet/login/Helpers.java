// Copyright (c) 2016 Cilogi. All Rights Reserved.
//
// File:        Helpers.java  (24/02/16)
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

import com.cilogi.lid.servlet.handle.IHandleHolder;
import com.cilogi.lid.user.LidUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings({"unused"})
class Helpers {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(Helpers.class);

    private Helpers() {}

    static String computeRedirect(String redirectURL, IHandleHolder holder, String handleRedirect) {
        String hr = handleRedirect(holder, handleRedirect);
        if (hr != null) {
            String fragment = fragmentOf(redirectURL);
            if (fragment != null) {
                return hr + "?fragment=" + fragment + "&redirect=" + redirectURL;
            } else {
                return hr + "?redirect=" + redirectURL;
            }
        } else {
            return redirectURL;
        }
    }

    static String handleRedirect(IHandleHolder handleHolder, String handleRedirect) {
        String userName = LidUser.userID();
        if (userName == null) {
            return null;
        } else {
            String handle = handleHolder.handle(userName);
            return (handle == null) ? handleRedirect : null;
        }
    }

    static String fragmentOf(String uriString) {
        try {
            URI uri = new URI(uriString);
            return uri.getFragment();
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
