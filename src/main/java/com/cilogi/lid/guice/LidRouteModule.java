// Copyright (c) 2010 Tim Niblett All Rights Reserved.
//
// File:        RouteModule.java  (05-Oct-2010)
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

import com.cilogi.lid.filter.AuthFilter;
import com.cilogi.lid.filter.UserFilter;
import com.cilogi.lid.servlet.LogoutServlet;
import com.cilogi.lid.servlet.StatusServlet;
import com.cilogi.lid.servlet.login.*;
import com.google.inject.servlet.ServletModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class LidRouteModule extends ServletModule {
    @SuppressWarnings({"unused"})
    static final Logger LOG = LoggerFactory.getLogger(LidRouteModule.class.getName());

    private final Properties lidProperties;

    public LidRouteModule(Properties lidProperties) {
        this.lidProperties = lidProperties;
    }

    @Override
    protected void configureServlets() {
        filter("/*").through(UserFilter.class);
        filter("/*").through(AuthFilter.class);


        serve(prop("email.login")).with(EmailLoginServlet.class);
        serve(prop("email.loginReturn")).with(EmailLoginReturn.class);
        serve(prop("google.login")).with(GoogleLoginServlet.class);
        serve(prop("google.loginReturn")).with(GoogleLoginReturnServlet.class);
        serve(prop("facebook.login")).with(FacebookLoginServlet.class);
        serve(prop("logout")).with(LogoutServlet.class);
        serve(prop("user.status")).with(StatusServlet.class);
    }

    private String prop(String s) {
        return prop(s, null);
    }

    private String prop(String s, String deflt) {
        String val = lidProperties.getProperty(s);
        return (val == null) ? deflt : val;
    }
}
