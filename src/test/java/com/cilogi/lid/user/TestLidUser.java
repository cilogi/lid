// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        TestLidUser.java  (08/08/15)
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

import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class TestLidUser {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(TestLidUser.class);


    public TestLidUser() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testSetAndGet() {
        String fred = "fred@flintstone.org";
        LidUser.setCurrentUser(fred);
        String user = LidUser.getCurrentUser();
        assertEquals(fred, user);
    }
}