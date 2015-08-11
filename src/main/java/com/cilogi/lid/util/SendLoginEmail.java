// Copyright (c) 2015 Cilogi. All Rights Reserved.
//
// File:        SendLoginEmail.java  (08/08/15)
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

import com.cilogi.lid.cookie.CookieInfo;
import com.cilogi.lid.guice.annotations.EmailFromAddress;
import com.cilogi.lid.guice.annotations.EmailReturnURL;
import com.cilogi.lid.guice.annotations.EmailTemplate;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Send a login id to a named user
 */
public class SendLoginEmail implements ISendEmail {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(SendLoginEmail.class);

    private final String returnAddress;
    private final SendEmail sendEmail;
    private final String fromAddress;
    private final String emailTemplate;

    @Inject
    public SendLoginEmail(@EmailFromAddress String fromAddress,
                          @EmailReturnURL String returnAddress,
                          @EmailTemplate String emailTemplate) {
        this.fromAddress = fromAddress;
        this.returnAddress = returnAddress;
        this.emailTemplate = emailTemplate;
        this.sendEmail = new SendEmail(fromAddress);
    }

    @Override
    public void send(@NonNull String emailAddress, String redirectURL) throws IOException {
        if (!isLegalEmail(emailAddress)) {
            throw new IllegalArgumentException("The id address " + emailAddress + " isn't valid");
        }
        CookieInfo info = new CookieInfo(emailAddress).setRedirect(redirectURL);
        String token = info.toEncryptedString();
        String fullAddress = returnAddress + "?token=" + token;
        LOG.info("Email from " + fromAddress + " to " + emailAddress + " URL is " + fullAddress);
        String htmlBody = htmlBody(ImmutableMap.of("from", fromAddress, "to", emailAddress, "fullAddress", fullAddress));
        sendEmail.send(emailAddress, "Please go the enclosed address to log in to cilogi-liddemo", htmlBody);
    }


    public boolean isLegalEmail(String emailAddress) {
        if ("".equals(emailAddress.trim())) {
            return false;
        }
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(emailAddress);
    }

    protected String htmlBody(Map<String,String> map) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        StringReader sr = new StringReader(IOUtils.toString(getClass().getResourceAsStream(emailTemplate)));
        Mustache mustache = mf.compile(sr, "test");
        StringWriter sw = new StringWriter();
        mustache.execute(sw, map);
        return sw.toString();
    }
}
