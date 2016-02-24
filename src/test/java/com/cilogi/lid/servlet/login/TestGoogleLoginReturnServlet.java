// Copyright (c) 2016 Cilogi. All Rights Reserved.
//
// File:        TestGoogleLoginReturnServlet.java  (24/02/16)
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

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TestGoogleLoginReturnServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(TestGoogleLoginReturnServlet.class);


    public TestGoogleLoginReturnServlet() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testFragment() throws URISyntaxException {
        URI uri = new URI("http://localhost:8081/index/html#guide/botanics");
        String fragment = uri.getFragment();
        assertEquals("guide/botanics", fragment);
    }
}