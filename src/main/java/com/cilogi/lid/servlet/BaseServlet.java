// Copyright (c) 2011 Tim Niblett All Rights Reserved.
//
// File:        BaseServlet.java  (31-Oct-2011)
// Author:      tim

//
// Copyright in the whole and every part of this source file belongs to
// Tim Niblett (the Author) and may not be used,
// sold, licenced, transferred, copied or reproduced in whole or in
// part in any manner or form or in or on any media to any person
// other than in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


package com.cilogi.lid.servlet;

import com.cilogi.lid.user.LidUser;
import com.cilogi.lid.util.session.SessionAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.base.Preconditions;
import com.google.common.net.MediaType;
import lombok.NonNull;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class BaseServlet extends HttpServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = Logger.getLogger(BaseServlet.class.getName());
    private static final long serialVersionUID = -8267570513322124577L;

    protected BaseServlet() {}

    protected void issue(MediaType mimeType, int returnCode, String output, HttpServletResponse response) throws IOException {
        response.setContentType(mimeType.toString());
        response.setStatus(returnCode);
        response.getWriter().println(output);
    }

    protected void issueJson(HttpServletResponse response, int status, Object... args) throws IOException {
        String jsonString =  new ObjectMapper().writeValueAsString(args2map(args));
        issue(MediaType.JSON_UTF_8, status, jsonString, response);
    }

    protected String stringParameter(@NonNull String name, HttpServletRequest request, String deflt) {
        String s = request.getParameter(name);
        return (s == null) ? deflt : s;
    }

    @SuppressWarnings({"unused"})
    protected String getCurrentUserName() {
        return LidUser.getCurrentUser();
    }

    @SuppressWarnings({"unused"})
    protected void putSession(@NonNull String key, @NonNull String value, @NonNull HttpServletRequest request) {
        new SessionAttributes(request).put(key, value);
    }

    protected String getAndDeleteSession(@NonNull String key, @NonNull HttpServletRequest request, String deflt) {
        return new SessionAttributes(request).getAndDelete(key, deflt);
    }

    static Map<String,Object> args2map(@NonNull Object... args) {
        Preconditions.checkArgument(args.length % 2 == 0, "There must be an even number of arguments");
        Map<String,Object> map = Maps.newLinkedHashMap(); // linked map for testing consistency
        for (int i = 0; i < args.length; i += 2) {
            map.put(args[i].toString(), args[i+1]);
        }
        return map;
    }




}
