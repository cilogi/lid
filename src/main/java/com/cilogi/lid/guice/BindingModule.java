// Copyright (c) 2011 Tim Niblett All Rights Reserved.
//
// File:        BindingModule.java  (12-Oct-2011)
// Author:      tim
// $Id$
//
// Copyright in the whole and every part of this source file belongs to
// Tim Niblett (the Author) and may not be used,
// sold, licenced, transferred, copied or reproduced in whole or in
// part in any manner or form or in or on any media to any person
// other than in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


package com.cilogi.lid.guice;


import com.cilogi.lid.guice.annotations.CookieExpireDays;
import com.cilogi.lid.guice.annotations.Development;
import com.google.appengine.api.utils.SystemProperty;
import com.google.apphosting.utils.servlet.SessionCleanupServlet;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BindingModule extends AbstractModule {
    @SuppressWarnings({"unused"})
    static final Logger LOG = LoggerFactory.getLogger(BindingModule.class);


    public BindingModule() {
     }

    @Override
    protected void configure() {
        bind(SessionCleanupServlet.class).in(Scopes.SINGLETON);

        // are we running on the development server or not?
        bind(Boolean.class).annotatedWith(Development.class).toInstance(isDevelopmentServer());

        // default expiry for cookies, the email cookies get the built in 30 minutes default, not settable here
        bind(Long.class).annotatedWith(CookieExpireDays.class).toInstance(30L);

        // the return email address, don't really need for the development server as it doesn't send email
        bindString("returnAddress",
                isDevelopmentServer() ? "http://localhost:8080/mailLogin" : "https://cilogi-lid.appspot.com/mailLogin");
    }

    private void bindString(String key, String value) {
        bind(String.class).annotatedWith(Names.named(key)).toInstance(value);
        //LOG.info("Bound " + key + " to " + value);
    }

    private void bindInt(String key, int value) {
        bind(Integer.class).annotatedWith(Names.named(key)).toInstance(value);
    }

    @SuppressWarnings({"unused"})
    private void bindBoolean(String key, boolean value) {
        bind(Boolean.class).annotatedWith(Names.named(key)).toInstance(value);
    }


    private static boolean isDevelopmentServer() {
        SystemProperty.Environment.Value server = SystemProperty.environment.value();
        return server == SystemProperty.Environment.Value.Development;
    }
}
