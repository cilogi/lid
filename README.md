# A lightweight identity solution for Java on App Engine

There is a demo on [App Engine](https://cilogi-liddemo.appspot.com)

This code provides a simple identity solution for App Engine on Java.
Its similar to the built-in user service but works with Google,
Facebook and Email, so that anyone with an ID can use it.

Its a library, but functions more as a chunk of Code which you have to
configure.  We'll get to that.

# Overview

There are 3 different methods of getting an ID.  It should
be easy to add other OAuth-based methods.

1. __Google__ User Service, which is specific to App Engine.  If that's
   enough for you its the simplest, and since you're using App Engine
   maybe you're a Google house.  Don't use this Code if Google is OK
   for you.  Google returns an _Email_.
2. __Facebook__, is the 800lb (363kilo) Gorilla in the room.  Most
   people online have an account so  its pretty simple.  Unfortunately
   Facebook, for reasons of user privacy, don't allow you an Email any
   more (you may get one, you may not).  You can reliably get the user
   name and the numeric ID -- we choose the numeric ID for privacy.
3. __Email__, which we use every time the user needs to log in.  The
   reason is to not have to store passwords, and get people to
   remember passwords.  The drawback is that your users will have to
   remember which identity they logged in with last time...

All these solutions work by us storing an encrypted cookie, which
contains the ID and an expiry time.  Its quite difficult to
break the encryption, but we're using [TEA][1] for simplicity, with 16
bytes of random salt, and if you don't trust it then use something
else.

The Email solution sends an Email to users who wants to log in. They
then have to click through a one-time (30 (configurable) minute
validity) address which then sets a cookie.  The cookie persists
unless the user logs out. There is no password, you have to log in by
Email each time.

For all solutions, Google, Facebook and Email the cookie persists for
a configurable amount of time (30 days by default) or until the
browser deleted it, or if the user _logs out_ of the app. The OAuth
token is never stored.

Its a problem that Facebook have moved away from always providing
an Email.  It means that the user needs to remember which
authentication method they used.  Of course they need to remember which
Email address they use in any case and some people may have different
Emails for Google, Facebook and their everyday Email...  Simplest is
probably to tell the user what the ID is (ie Google:<id>,
Facebook:<Id>) explicitly.

## Caveat

This solution is _only_ for authentication.  You'll need to set up
servlet filters for authorization, and you won't get any of the
sophistication of a system like Shiro or Spring Security.  However, a
lot of the time simple authentication/id is all that's wanted.  Just
to show its possible there ia a very lightweight authorization filter
included, which is shown in the demo.

This solution is simple and does not hit the datastore in any way so
it should be fast and lightweight for App Engine.

# Configuration

What you need to do to set your application up for Lightweight
Identity

1. You must use Guice.
    * Set up the `web.xml` file, see the `liddemo` file for an
      example.
    * Extend `LidServeContextListener` with a `getResourceLocation`
      method which specifies where your properties file will go. Ours
      is `/liddemo.properties`.  You need to edit the properties in
      this file with the locations where your servlets will listen.
      There is no general rule here, whatever fits in with your
      scheme.  There are quite a few end-points, and we've documented
      them.
    * While you're exending it make sure to include the base binding
      and route modules as we've done in the example.  Your own
      modules should come later.
    * Set up your secret properties, ours are at
      `/secret.properties`.  The properties are the authorization for
      Facebook and the Base64 coded data for the encrypted cookies.

      The facebook properties are: `fb.local.apiKey`,
      `fb.local.apiSecret`, and `fb.local.host` (usually
      `http://locahost:8080`) and the same with `local` replaced with
      `live` for the external website.  The `apiKey` is the `App ID`
      and the `apiSecret` is the `App Secret`. You will need to create
      an app and set the OAuth Redirect APIs,

      The random data is generated like this for our keys

          SecureRandom rand = new SecureRandom();
          byte[] data = new byte[16];
          rand.nextBytes(data);
          String s = Base64.encodeBase64String(data);
          System.out.println(s);

      There are no doubt better ways.

2.    The Google and Facebook logins work with simple forms being
      posted. The id login is a little different as after the
      initial post the user has to move to another window/app in order
      to access the generated URL.  You can see how we've done this in
      the `script.js` file, which also uses a user status servlet to
      find status by Ajax.  You may prefer to generate the files
      server-side with the user information added. 
      
       
Enjoy. Please use the Github issues for anything that's not clear.

The License is MIT.


[1]: https://en.wikipedia.org/wiki/XXTEA
