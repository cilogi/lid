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

import com.cilogi.lid.cookie.CookieHandler;
import com.cilogi.lid.cookie.CookieInfo;
import com.cilogi.lid.cookie.Site;
import com.cilogi.lid.guice.annotations.AuthRedirect;
import com.cilogi.lid.guice.annotations.CookieExpireDays;
import com.cilogi.lid.guice.annotations.Development;
import com.cilogi.lid.servlet.BaseServlet;
import com.cilogi.lid.user.LidUser;
import com.cilogi.lid.util.Secrets;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.net.MediaType;
import lombok.NonNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Singleton
public class FacebookLoginServlet extends BaseServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(FacebookLoginServlet.class);

    private static final String DEFAULT_REDIRECT = "/index.html";
    private static final Token NULL_TOKEN = null;
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final long serialVersionUID = -3985426682815913784L;

    private final String apiKey;
    private final String apiSecret;
    private final String host;
    private final long cookieExpireDays;
    private final String authRedirect;

    @Inject
    public FacebookLoginServlet(@Development boolean isDevelopmentServer,
                                @CookieExpireDays long cookieExpireDays,
                                @AuthRedirect String authRedirect) {
        apiKey =    key(isDevelopmentServer, "apiKey");
        apiSecret = key(isDevelopmentServer, "apiSecret");
        host =      key(isDevelopmentServer, "host");
        this.cookieExpireDays = cookieExpireDays;
        this.authRedirect = authRedirect;
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

    /*
     * Step 2: with the code from the original post we get the User's email (and the OAuth token). Then we
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String code = request.getParameter("code");
            String currentUri = request.getRequestURI();
            OAuthInfo info = getUserInfo(code, currentUri);
            if (info.isError()) {
                String message = info.getErrorString();
                issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_BAD_REQUEST,
                        "Couldn't get Facebook permission: " + message, response);
            } else {
                String email = info.getEmail();
                CookieInfo cookieInfo = new CookieInfo(email)
                        .setSite(Site.facebook)
                        .expire(cookieExpireDays, TimeUnit.DAYS);
                CookieHandler handler = new CookieHandler();
                handler.setCookie(request, response, cookieInfo);
                LidUser.setCurrentUser(email);

                String redirectURL = getAndDeleteSession(authRedirect, request, DEFAULT_REDIRECT);
                revokeToken(info.getToken(), request, response, redirectURL);  // not totally sure about this, but it seems safer to revoke token immediately
            }
        } catch (Exception e) {
            issue(MediaType.PLAIN_TEXT_UTF_8, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Something unexpected went wrong: " + e.getMessage(), response);
        }
    }

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

    public OAuthInfo getUserInfo(String code, @NonNull String callBackUrl) {
        Preconditions.checkArgument(callBackUrl.startsWith("/"));

        JSONObject obj = getUserInfoJSON(code, callBackUrl);
        return new OAuthInfo()
                .setErrorString(errorString(obj))
                .setEmail(obj.optString("email"))
                .setToken(obj.optString("access_token"));
    }

    private JSONObject getUserInfoJSON(String code, String callBackUrl) {
        OAuthService service = new ServiceBuilder()
                                      .provider(FacebookApi.class)
                                      .apiKey(apiKey)
                                      .apiSecret(apiSecret)
                                      .callback(host + callBackUrl)
                                      .scope("email")
                                      .build();
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(NULL_TOKEN, verifier);
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        Response response = request.send();
        try {
            JSONObject obj =  new JSONObject(response.getBody());
            obj.put("access_token", accessToken.getToken());
            return obj;
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    private void revokeToken(String token, HttpServletRequest request, HttpServletResponse response,
                            String redirectURL)  throws IOException {
        String redirectHome = host + redirectURL;

        String url = logoutUrl(redirectHome, token);
        response.sendRedirect(response.encodeRedirectURL(url));
    }

    private static String logoutUrl(String redirect, String accessToken) throws IOException {
         String redirectOK = OAuthEncoder.encode(redirect);
         return "https://www.facebook.com/logout.php?next="+redirectOK+"&access_token=" + accessToken;
     }

    private static String key(boolean isDevelopmentServer, String key) {
        return isDevelopmentServer ? Secrets.get("fb.local." + key) : Secrets.get("fb.live." + key);
    }

    private static String errorString(JSONObject obj) {
        if (obj.has("error")) {
            try {
                JSONObject errObj = obj.getJSONObject("error");
                String message = errObj.getString("message");
                return (message == null) ? "unknown JSON error, no message field found" : message;
            } catch (JSONException e) {
                return "Unknown error, JSON won't parse: " + obj.toString();
            }
        } else {
            return null;
        }
    }

}