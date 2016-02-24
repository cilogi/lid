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


import com.cilogi.lid.guice.annotations.*;
import com.cilogi.lid.util.ISendEmail;
import com.cilogi.lid.util.SendLoginEmail;
import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class LidBindingModule extends AbstractModule {
    @SuppressWarnings({"unused"})
    static final Logger LOG = LoggerFactory.getLogger(LidBindingModule.class);

    private final Properties lidProperties;

    public LidBindingModule(Properties lidProperties) {
        this.lidProperties = lidProperties;
     }

    @Override
    protected void configure() {
        bind(Boolean.class).annotatedWith(Development.class).toInstance(isDevelopmentServer());
        bind(Long.class).annotatedWith(CookieExpireDays.class).toInstance(longProp("cookie.expireDays"));
        bind(String.class).annotatedWith(DefaultRedirect.class).toInstance(prop("default.redirect")); // must not require auth
        bind(String.class).annotatedWith(HandleRedirect.class).toInstance(prop("handle.redirect"));

        bind(String.class).annotatedWith(EmailReturnURL.class)
                .toInstance(isDevelopmentServer()
                        ? prop("host.development") + prop("email.loginReturn")
                        : prop("host.production") + prop("email.loginReturn"));
        bind(String.class).annotatedWith(AuthRedirect.class).toInstance(prop("auth.redirect.default"));

        bind(String.class).annotatedWith(EmailFromAddress.class).toInstance(prop("email.from"));
        bind(String.class).annotatedWith(EmailTemplate.class).toInstance(prop("email.template"));
        bind(String.class).annotatedWith(EmailHeading.class).toInstance(prop("email.heading"));

        bind(String.class).annotatedWith(LoginPage.class).toInstance(prop("login.page"));
        bind(String.class).annotatedWith(LogoutPage.class).toInstance(prop("logout"));

        bind(ISendEmail.class).to(SendLoginEmail.class);
        bind(boolean.class).annotatedWith(HttpOnly.class).toInstance(booleanProp("cookie.httpOnly"));
    }

    protected static boolean isDevelopmentServer() {
        SystemProperty.Environment.Value server = SystemProperty.environment.value();
        return server == SystemProperty.Environment.Value.Development;
    }


    protected String prop(String s) {
        return lidProperties.getProperty(s);
    }

    protected long longProp(String s) {
        String val = prop(s);
        return Long.parseLong(val);
    }

    protected boolean booleanProp(String s) {
        String val = prop(s);
        return Boolean.parseBoolean(val);
    }
}
