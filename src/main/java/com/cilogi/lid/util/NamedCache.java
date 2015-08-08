// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        NamedCache.java  (11/04/15)
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

import com.cilogi.util.cache.IMemcached;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.base.Charsets;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NamedCache implements IMemcached {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(NamedCache.class);

    private final String prefix;
    private final MemcacheService memcache;

    public NamedCache(@NonNull String prefix) {
        this.prefix = prefix;
        memcache = MemcacheServiceFactory.getMemcacheService();
    }

    @Override
    public void put(@NonNull String key, @NonNull byte[] data) {
        memcache.put(prefix + key, data);
    }

    @Override
    public byte[] get(@NonNull String key) {
        return (byte[])memcache.get(prefix + key);
    }

    public String getString(String key) {
        byte[] data = get(key);
        return (data == null) ? null : new String(data, Charsets.UTF_8);
    }

    @Override
    public void close() {
        // no-op
    }
}
