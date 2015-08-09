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
import com.google.common.base.Preconditions;
import com.google.common.net.MediaType;
import lombok.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class BaseServlet extends HttpServlet {
    @SuppressWarnings("unused")
    static final Logger LOG = Logger.getLogger(BaseServlet.class.getName());
    private static final long serialVersionUID = -8267570513322124577L;

    protected BaseServlet() {
    }


    protected void issue(MediaType mimeType, int returnCode, String output, HttpServletResponse response) throws IOException {
        response.setContentType(mimeType.toString());
        response.setStatus(returnCode);
        response.getWriter().println(output);
    }

    protected void issueJson(HttpServletResponse response, int status, Object... args) throws IOException {
        Preconditions.checkArgument(args.length % 2 == 0, "There must be an even number of strings");
        try {
            JSONObject obj = new JSONObject();
            for (int i = 0; i < args.length; i += 2) {
                if (args[i + 1] != null) {
                    obj.put((String) args[i], args[i + 1]);
                }
            }
            issueJson(response, status, obj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void issueJson(HttpServletResponse response, int status, JSONObject obj) throws IOException {
        issue(MediaType.JSON_UTF_8, status, obj.toString(), response);
    }


    protected String stringParameter(@NonNull String name, HttpServletRequest request, String deflt) {
        String s = request.getParameter(name);
        return (s == null) ? deflt : s;
    }

    protected boolean booleanParameter(@NonNull String name, HttpServletRequest request, boolean deflt) {
        String s = request.getParameter(name);
        return (s == null) ? deflt : Boolean.parseBoolean(s);
    }

    protected String getCurrentUserName() {
        return LidUser.getCurrentUser();
    }




}
