// Licensed under Apache Version 2
//
// File:        SimpleCookie.java  (08/08/15)
// Author:      tim
//
// Adapted from Apache Shiro
//


package com.cilogi.lid.cookie;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unused"})
@Accessors(fluent = false, chain=true)
public class HttpCookie {
    private static final transient Logger LOG = LoggerFactory.getLogger(HttpCookie.class);

    /**
     * {@code -1}, indicating the cookie should expire when the browser closes.
     */
    public static final int DEFAULT_MAX_AGE = -1;

    /**
     * {@code -1} indicating that no version property should be set on the cookie.
     */
    public static final int DEFAULT_VERSION = -1;

    public static final String DELETED_COOKIE_VALUE = "deleted";

    //These constants are protected on purpose so that the test case can use them
    protected static final String NAME_VALUE_DELIMITER = "=";
    protected static final String ATTRIBUTE_DELIMITER = "; ";
    protected static final long DAY_MILLIS = TimeUnit.MILLISECONDS.convert(1L, TimeUnit.DAYS);
    protected static final String GMT_TIME_ZONE_ID = "GMT";
    protected static final String COOKIE_DATE_FORMAT_STRING = "EEE, dd-MMM-yyyy HH:mm:ss z";
    protected static final String ROOT_PATH = "/";
    protected static final String COOKIE_HEADER_NAME = "Set-Cookie";
    protected static final String PATH_ATTRIBUTE_NAME = "Path";
    protected static final String EXPIRES_ATTRIBUTE_NAME = "Expires";
    protected static final String MAXAGE_ATTRIBUTE_NAME = "Max-Age";
    protected static final String DOMAIN_ATTRIBUTE_NAME = "Domain";
    protected static final String VERSION_ATTRIBUTE_NAME = "Version";
    protected static final String COMMENT_ATTRIBUTE_NAME = "Comment";
    protected static final String SECURE_ATTRIBUTE_NAME = "Secure";
    protected static final String HTTP_ONLY_ATTRIBUTE_NAME = "HttpOnly";


    @Getter
    private String name;
    @Getter @Setter
    private String value;
    @Getter @Setter
    private String comment;
    @Getter @Setter
    private String domain;
    @Getter @Setter
    private String path;
    @Getter
    private int maxAge;
    @Getter
    private int version;
    @Getter @Setter
    private boolean secure;
    @Getter @Setter
    private boolean httpOnly;

    public HttpCookie() {
        this.maxAge = DEFAULT_MAX_AGE;
        this.version = DEFAULT_VERSION;
        this.httpOnly = true; //most of the cookies ever used by Shiro should be as secure as possible.
    }

    public HttpCookie(String name) {
        this();
        this.name = name;
    }

    public HttpCookie setName(@NonNull String name) {
        Preconditions.checkArgument(!"".equals(name.trim()));
        this.name = name;
        return this;
    }

    public HttpCookie setMaxAge(int maxAge) {
        this.maxAge = Math.max(DEFAULT_MAX_AGE, maxAge);
        return this;
    }

    public HttpCookie setVersion(int version) {
        this.version = Math.max(DEFAULT_VERSION, version);
        return this;
    }

    /**
     * Returns the Cookie's calculated path setting.  If the {@link javax.servlet.http.Cookie#getPath() path} is {@code null}, then the
     * {@code request}'s {@link javax.servlet.http.HttpServletRequest#getContextPath() context path}
     * will be returned. If getContextPath() is the empty string or null then the ROOT_PATH constant is returned.
     *
     * @param request the incoming HttpServletRequest
     * @return the path to be used as the path when the cookie is created or removed
     */
    private String calculatePath(HttpServletRequest request) {
        String path = clean(getPath());
        if (!hasText(path)) {
            path = clean(request.getContextPath());
        }

        //fix for http://issues.apache.org/jira/browse/SHIRO-9:
        if (path == null) {
            path = ROOT_PATH;
        }
        LOG.trace("calculated path: {}", path);
        return path;
    }

    public void saveTo(HttpServletRequest request, HttpServletResponse response) {
        addCookieHeader(response, getName(), getValue(), getComment(), getDomain(), calculatePath(request),
                getMaxAge(),  getVersion(), isSecure(), isHttpOnly());
    }

    private void addCookieHeader(HttpServletResponse response, String name, String value, String comment,
                                 String domain, String path, int maxAge, int version,
                                 boolean secure, boolean httpOnly) {

        String headerValue = buildHeaderValue(name, value, comment, domain, path, maxAge, version, secure, httpOnly);
        response.addHeader(COOKIE_HEADER_NAME, headerValue);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Added HttpServletResponse Cookie [{}]", headerValue);
        }
    }

    /*
     * This implementation followed the grammar defined here for convenience:
     * <a href="http://github.com/abarth/http-state/blob/master/notes/2009-11-07-Yui-Naruse.txt">Cookie grammar</a>.
     *
     * @return the 'Set-Cookie' header value for this cookie instance.
     */

    protected String buildHeaderValue(@NonNull String name, String value, String comment,
                                      String domain, String path, int maxAge, int version,
                                      boolean secure, boolean httpOnly) {
        if (!hasText(name)) {
            throw new IllegalStateException("Cookie name cannot be null/empty.");
        }

        StringBuilder sb = new StringBuilder(name).append(NAME_VALUE_DELIMITER);

        if (hasText(value)) {
            sb.append(value);
        }

        appendComment(sb, comment);
        appendDomain(sb, domain);
        appendPath(sb, path);
        appendExpires(sb, maxAge);
        appendVersion(sb, version);
        appendSecure(sb, secure);
        appendHttpOnly(sb, httpOnly);

        return sb.toString();

    }

    private void appendComment(StringBuilder sb, String comment) {
        if (hasText(comment)) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(COMMENT_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(comment);
        }
    }

    private void appendDomain(StringBuilder sb, String domain) {
        if (hasText(domain)) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(DOMAIN_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(domain);
        }
    }

    private void appendPath(StringBuilder sb, String path) {
        if (hasText(path)) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(PATH_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(path);
        }
    }

    private void appendExpires(StringBuilder sb, int maxAge) {
         if (maxAge >= 0) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(MAXAGE_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(maxAge);
            sb.append(ATTRIBUTE_DELIMITER);
            Date expires;
            if (maxAge == 0) {
                //delete the cookie by specifying a time in the past (1 day ago):
                expires = new Date(System.currentTimeMillis() - DAY_MILLIS);
            } else {
                //Value is in seconds.  So take 'now' and add that many seconds, and that's our expiration date:
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, maxAge);
                expires = cal.getTime();
            }
            String formatted = toCookieDate(expires);
            sb.append(EXPIRES_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(formatted);
        }
    }

    private void appendVersion(StringBuilder sb, int version) {
        if (version > DEFAULT_VERSION) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(VERSION_ATTRIBUTE_NAME).append(NAME_VALUE_DELIMITER).append(version);
        }
    }

    private void appendSecure(StringBuilder sb, boolean secure) {
        if (secure) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(SECURE_ATTRIBUTE_NAME); //No value for this attribute
        }
    }

    private void appendHttpOnly(StringBuilder sb, boolean httpOnly) {
        if (httpOnly) {
            sb.append(ATTRIBUTE_DELIMITER);
            sb.append(HTTP_ONLY_ATTRIBUTE_NAME); //No value for this attribute
        }
    }

    private static String toCookieDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone(GMT_TIME_ZONE_ID);
        DateFormat fmt = new SimpleDateFormat(COOKIE_DATE_FORMAT_STRING, Locale.US);
        fmt.setTimeZone(tz);
        return fmt.format(date);
    }

    public void removeFrom(HttpServletRequest request, HttpServletResponse response) {
        String name = getName();
        String value = DELETED_COOKIE_VALUE;
        String comment = null; // comments are irrelevant for deletions
        int maxAge = 0; //always zero for deletion
        boolean httpOnly = false;

        addCookieHeader(response, getName(), value, comment, getDomain(), calculatePath(request),
                maxAge, getVersion(), isSecure(), httpOnly);

        LOG.trace("Removed '{}' cookie by setting maxAge=0", name);
    }

    public String readValue(HttpServletRequest request) {
        Cookie cookie = getCookie(request, getName());
        return (cookie == null) ? null : cookie.getValue();
    }

     private static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private static String clean(String in) {
        String out = (in == null) ? null : in.trim();
        return "".equals(out) ? null : out;
    }

    private static boolean hasText(String str) {
        return str != null && str.trim().length() > 0;
    }

    private static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }
}
