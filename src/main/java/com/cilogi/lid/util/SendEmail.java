package com.cilogi.lid.util;

import com.cilogi.lid.guice.annotations.EmailFromAddress;
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.logging.Logger;

@Singleton
public class SendEmail {
    static final Logger LOG = Logger.getLogger(SendEmail.class.getName());

    private final String fromAddress;

    @Inject
    public SendEmail(@EmailFromAddress String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void send(String toAddress, String title, String htmlMessage) {
        MailService service = MailServiceFactory.getMailService();
        MailService.Message message = new MailService.Message();
        message.setSender(fromAddress);
        message.setTo(toAddress);
        message.setSubject(title);
        message.setHtmlBody(htmlMessage);
        try {
            service.send(message);
            LOG.info("message has been sent from " + fromAddress + " to " + toAddress);
        } catch (IOException e) {
            LOG.warning("Can't send id from " + fromAddress + " to " + toAddress + " about " + title + ": " + e.getMessage());
        }
    }
}