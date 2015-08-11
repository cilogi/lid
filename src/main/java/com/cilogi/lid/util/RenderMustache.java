// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        RenderMustache.java  (11/08/15)
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


package com.cilogi.lid.util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Render a mustache template stored as a resource
 */
public class RenderMustache {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(RenderMustache.class);

    private RenderMustache() {}

    public static String render(@NonNull String resourceName, @NonNull Map<String,?> map) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        StringReader sr = new StringReader(IOUtils.toString(RenderMustache.class.getResourceAsStream(resourceName)));
        Mustache mustache = mf.compile(sr, "test");
        StringWriter sw = new StringWriter();
        mustache.execute(sw, map);
        return sw.toString();
    }
}
