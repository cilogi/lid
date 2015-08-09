// Copyright (c) 2012 Tim Niblett. All Rights Reserved.
//
// File:        Secrets.java  (23/10/12)
// Author:      tim
//
// Copyright in the whole and every part of this source file belongs to
// Tim Niblett (the Author) and may not be used, sold, licenced, 
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


package com.cilogi.lid.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Secrets {
    static final Logger LOG = LoggerFactory.getLogger(Secrets.class);

    private static final Properties props = new Properties();

    static {
        try {
            try (InputStream is = Secrets.class.getResourceAsStream("/secret.properties")) {
                props.load(is);
            }
        } catch (IOException e) {
            LOG.error("Can't load secrets", e);
        }
    }

    private Secrets() {}

    public static String get(String name) {
        return props.getProperty(name);
    }
}
