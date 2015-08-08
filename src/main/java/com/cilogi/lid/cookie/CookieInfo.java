// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        CookieContent.java  (08/08/15)
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * The payload of a cookie.  It contains the email of the registered user
 * and the expiry time of the cookie. No expired cookie should be accepted.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CookieInfo implements Serializable {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(CookieInfo.class);
    private static final long serialVersionUID = 8565488346752323876L;

    // users have to re-login every 3- days, so that va;id cookies can't sit around forever

    private static final long EXPIRY = TimeUnit.MILLISECONDS.convert(30L, TimeUnit.DAYS);

    private static final SecureRandom rand = new SecureRandom();

    private String email;
    private Date expires;
    private String salt;

    private static String salt() {
        byte[] data = new byte[16];
        rand.nextBytes(data);
        return Base64.encodeBase64String(data);
    }

    public static CookieInfo fromJSONString(@NonNull String data) {
        try {
            return new ObjectMapper().readValue(data, CookieInfo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CookieInfo() {}

    public CookieInfo(String email) {
        this(email, new Date(new Date().getTime() + EXPIRY));
    }

    public CookieInfo(@NonNull String email, @NonNull Date expires) {
        this.email = email;
        this.expires = expires;
        this.salt = salt();
    }

    public String toJSONString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
