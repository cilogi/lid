# A lightweight identity solution for Java on App Engine

There is a demo on [App Engine](https://cilogi-liddemo.appspot.com)

This code provides a simple identity solution for App Engine on Java.
Its similar to the built-in user service but works with Google, Facebook
and  Email, so that anyone with an Email address can use it.

Google uses the built-in App Engine

The Email solution sends an Email to  users who wants to log in. They
then have to click through a one-time (30 (configurable) minute validity) address which then
sets a cookie.  The cookie persists unless the user logs out. There is no password,
you have to log in by Email each time.

Google uses the user service to set the cookie.  Facebook uses OAuth.  In all cases
the cookie persists until the user logs out (or the browser deleted it).

This solution is _only_ for authentication.  You'll need to set up servlet filters for
authorization. However, a lot of the time simple authentication/id is all that's wanted.
Just to show its possible there ia a very lightweight authorization filter included,
which is shown in the demo.

This solution is  simple and does not hit the datastore in any way so it should be
fast and lightweight for App Engine.

Enjoy.


The License is MIT.