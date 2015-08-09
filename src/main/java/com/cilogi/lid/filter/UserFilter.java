// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        UserFilter.java  (08/08/15)
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


package com.cilogi.lid.filter;

import com.cilogi.lid.cookie.CookieHandler;
import com.cilogi.lid.cookie.CookieInfo;
import com.cilogi.lid.user.LidUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Set the user if we can work it out from the cookie in the request.
 * This relies on AppEngine _not_ allowing threads to be created so setting
 * the user in a <code>ThreadLocal</code> guarantees we can access the user from
 * anywhere in the code later on.
 * <p>Make this filter appear before any that do authorization, this is just
 *  for authentication.</p>
 */
@Singleton
public class UserFilter implements Filter {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(UserFilter.class);

    @Inject
    public UserFilter() {}

    @Override public void init(FilterConfig filterConfig) throws ServletException {}

    @Override public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        CookieHandler handler = new CookieHandler();
        CookieInfo info = handler.getCookie((HttpServletRequest)request);
        LidUser.setCurrentUser((info == null || info.isExpired()) ? null : info.getEmail());
        LidUser.setCurrentSite((info == null) ? null : info.getSite());
        chain.doFilter(request, response);
    }

}
