// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        LidUser.java  (08/08/15)
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


package com.cilogi.lid.user;

import com.cilogi.lid.cookie.Site;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class, and the auth filter where its set only work because AppEngine
 * front ends dont allow you to create threads.  I'm not sure how well it would
 * do in a general-purpose setting.  You'd probably need to store the information
 * in the session and make this method session-scoped (only from servlets).
 */
public class LidUser {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(LidUser.class);

    private static final ThreadLocal<LidUser> lu = new ThreadLocal<LidUser>() {
        protected LidUser initialValue() {
            return new LidUser();
        }
    };

    public static String getCurrentUser() {
        return lu.get().getUserName();
    }

    public static Site getCurrentSite() {
        return lu.get().getSite();
    }

    public static void setCurrentUser(String userName) {
        lu.get().setUserName(userName);
    }

    public static void setCurrentSite(Site site) {
        lu.get().setSite(site);
    }

    @Getter @Setter
    private String userName;
    @Getter @Setter
    private Site site;

    private LidUser() {}

}
