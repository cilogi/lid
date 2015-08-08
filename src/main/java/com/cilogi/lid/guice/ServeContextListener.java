// Copyright (c) 2012 Tim Niblett All Rights Reserved.
//
// File:        ServeContextListener.java  (05-Aug-2012)
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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServeContextListener extends GuiceServletContextListener {
    @SuppressWarnings({"unused"})
    static final Logger LOG = LoggerFactory.getLogger(ServeContextListener.class);

    public ServeContextListener() {}

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                // order is essential here, as the objectify filter must come before others,
                // in order to set up the objectify context
                new BindingModule(), new RouteModule());
    }
}
