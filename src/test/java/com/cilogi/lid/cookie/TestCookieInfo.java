// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        TestCookieHandler.java  (08/08/15)
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


package com.cilogi.lid.cookie;

import com.cilogi.lid.util.Secrets;
import com.cilogi.lid.util.TEA;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class TestCookieInfo {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(TestCookieInfo.class);


    public TestCookieInfo() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testEncryptDecrypt() {
        CookieInfo info = new CookieInfo("fred@flintstone.org");
        String token = info.toEncryptedString();
        CookieInfo back = CookieInfo.fromEncryptedString(token);
        assertEquals(info, back);
    }

    @Test
    public void testBytes() {
        CookieInfo info = new CookieInfo("fred@flintstone.org");
        String infoString = info.toJSONString();
        byte[] raw = infoString.getBytes(Charsets.UTF_8);
        byte[] key = Base64.decodeBase64(Secrets.get("jose.128"));
        assertEquals(16, key.length);
        TEA tea = new TEA(key);
        byte[] coded = tea.encrypt(raw);
        byte[] back = tea.decrypt(coded);
        assertArrayEquals(back, raw);
    }
}