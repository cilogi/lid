// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        CookieHandler.java  (08/08/15)
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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class CookieHandler {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(CookieHandler.class);

    private static final String LID_COOKIE_NAME = "Cilogi_LidCookie";

    private static final LoadingCache<String, CookieInfo> cookieCache = CacheBuilder.newBuilder()
        .concurrencyLevel(16)
        .maximumSize(100)
        .expireAfterWrite(5L, TimeUnit.MINUTES)
        .build(new CacheLoader<String, CookieInfo>() {
            public CookieInfo load(String encrypted)  {
                return CookieInfo.fromEncryptedString(encrypted);
            }
        });

    @Getter
    private final boolean httpOnly;

    private CookieHandler() {
        this(true);
    }

    @Inject
    public CookieHandler(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public CookieInfo getCookie(HttpServletRequest request) {
        String value = new HttpCookie(LID_COOKIE_NAME).readValue(request);
        try {
            return (value == null) ? null : cookieCache.get(value);
        } catch (Exception e) {
            LOG.warn("Exception getting cookie from cache: " + e.getMessage());
            return null; // should be safe enough, user won't be able to access anything secure
        }
    }

    public void setCookie(HttpServletRequest request, HttpServletResponse response, CookieInfo info) {
        String cookieValue = info.toEncryptedString();
        long maxAgeMillis = info.getExpires().getTime() - new Date().getTime();
        long maxAge = (maxAgeMillis < 0) ? -1 : maxAgeMillis/1000L;
        HttpCookie cookie = new HttpCookie(LID_COOKIE_NAME)
                .setValue(cookieValue)
                .setMaxAge((int)maxAge)
                .setHttpOnly(httpOnly);
        cookie.saveTo(request, response);
    }

    public CookieInfo removeCookie(HttpServletRequest request, HttpServletResponse response) {
        HttpCookie cookie = new HttpCookie(LID_COOKIE_NAME);
        CookieInfo info = getCookie(request);
        cookie.removeFrom(request, response);
        return info;
    }
}
