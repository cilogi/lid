// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        OAuthInfo.java  (09/08/15)
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Accessors(fluent=false,chain=true)
class OAuthInfo {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(OAuthInfo.class);

    private String token;
    private String id;
    private String name;
    private String errorString;

    OAuthInfo() {}

    @JsonIgnore
    public boolean isError() {
        return errorString != null;
    }

}
