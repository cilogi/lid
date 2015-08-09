// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        TestBaseServlet.java  (09/08/15)
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


package com.cilogi.lid.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class TestBaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(TestBaseServlet.class);


    public TestBaseServlet() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testArgs2Map() throws IOException {
        Map<String,Object> map = BaseServlet.args2map("a", 1, "b", 2, "c", "rest");
        String out = new ObjectMapper().writeValueAsString(map);
        assertEquals("{\"a\":1,\"b\":2,\"c\":\"rest\"}", out);
    }

}