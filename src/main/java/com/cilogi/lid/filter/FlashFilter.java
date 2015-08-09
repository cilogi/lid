// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        FlashFilter.java  (09/08/15)
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

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Filter for providing a version of FLash scope, but its a little expensive on
 * AppEngine as each call will hit the data-store containing session data
 */
@Singleton
public class FlashFilter implements Filter {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(FlashFilter.class);


    private static final String FLASH_PARAMS = "FLASH_PARAMS";

    @Inject
    public FlashFilter() {}


    @Override public void init(FilterConfig filterConfig) throws ServletException {}

    @Override public void destroy() {}

    @Override
    @SuppressWarnings({"unchecked"})
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            Map<String,Object> params = (Map<String,Object>)session.getAttribute(FLASH_PARAMS);
            if (params != null) {
                for (Map.Entry entry : params.entrySet()) {
                    request.setAttribute(entry.getKey().toString(), entry.getValue());
                }
                session.removeAttribute(FLASH_PARAMS);
            }
        }

        chain.doFilter(request, response);

        session = httpRequest.getSession(false); // session can be created at any time
        if (session != null) {
            Map<String,Object> params = Maps.newHashMap();
            for (Enumeration e = httpRequest.getAttributeNames(); e.hasMoreElements();) {
                String param = (String)e.nextElement();
                if (param.startsWith("flash.")) {
                    Object val = request.getAttribute(param);
                    param = param.substring(6, param.length()); // strip the 'flash.'
                    params.put(param, val);
                }
            }
            if (params.size() > 0) {
                session.setAttribute(FLASH_PARAMS, params);
            }
        }
    }


}
