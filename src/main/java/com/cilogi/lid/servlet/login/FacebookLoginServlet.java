// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        FacebookLoginServlet.java  (09/08/15)
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

import com.cilogi.lid.guice.annotations.Development;
import com.cilogi.lid.servlet.BaseServlet;
import com.cilogi.lid.util.Secrets;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import lombok.NonNull;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class FacebookLoginServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(FacebookLoginServlet.class);

    private static final String DEFAULT_REDIRECT = "/index.html";
    private static final Token NULL_TOKEN = null;

    private final String apiKey;
    private final String apiSecret;
    private final String host;

    @Inject
    public FacebookLoginServlet(@Development boolean isDevelopmentServer) {
        apiKey =    key(isDevelopmentServer, "apiKey");
        apiSecret = key(isDevelopmentServer, "apiSecret");
        host =      key(isDevelopmentServer, "host");
    }

    /*
     * Step 1: send off a request to Facebook
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentUri = request.getRequestURI();
        String loginURL = loginURL(currentUri);
        response.sendRedirect(loginURL);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * Get a login URL on Facebook
     * @param redirectURL Must start with a slash (ie be absolute on the server and is where the
     *                    call to Facebook will be redirected
     * @return The login URL on Facebook
     */
    private String loginURL(@NonNull String redirectURL) {
        Preconditions.checkArgument(redirectURL.startsWith("/"));

        OAuthService service = new ServiceBuilder()
                                      .provider(FacebookApi.class)
                                      .apiKey(apiKey)
                                      .apiSecret(apiSecret)
                                      .callback(host + redirectURL)
                                      .scope("email")
                                      .build();
        return service.getAuthorizationUrl(null);
    }

    private static String key(boolean isDevelopmentServer, String key) {
        return isDevelopmentServer ? Secrets.get("fb.local." + key) : Secrets.get("fb.live." + key);
    }

}
