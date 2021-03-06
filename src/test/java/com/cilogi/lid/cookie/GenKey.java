// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        GenKey.java  (08/08/15)
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

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;


public class GenKey {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(GenKey.class);

    public GenKey() {

    }

    public static void main(String[] args) {
        SecureRandom rand = new SecureRandom();
        byte[] data = new byte[16];
        rand.nextBytes(data);
        String s = Base64.encodeBase64String(data);
        System.out.println(s);
    }
}
