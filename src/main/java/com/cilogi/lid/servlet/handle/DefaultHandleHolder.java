// Copyright (c) 2017 Cilogi. All Rights Reserved.
//
// File:        DefaultHaldleHolder.java
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


package com.cilogi.lid.servlet.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultHandleHolder implements IHandleHolder {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(DefaultHandleHolder.class);

    public DefaultHandleHolder() {

    }

    @Override
    public String handle(String userName) {
        return userName;
    }
}
