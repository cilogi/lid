// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        DefaultLoginAction.java  (14/08/15)
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

import com.cilogi.lid.cookie.CookieInfo;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultLoginAction implements ILoginAction {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(DefaultLoginAction.class);

    public DefaultLoginAction() {

    }


    @Override
    public void act(@NonNull CookieInfo info) {
        LOG.info("login, id: " + info.getId() + " name: " + info.getName() +
                " expires: " + info.getExpires() + " site: " + info.getSite());
    }
}
