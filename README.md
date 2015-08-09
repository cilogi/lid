# A lightweight identity solution for Java on App Engine

This code provides a simple identity solution for App Engine on Java.
Its similar to the built-in user service but works with Google, Facebook
and general Email, so that anyone with an Email address can use it.

The Email solution sends an  Email to  users who wants to log in. They
then have to click through a one-time (N minute validity) address which then
sets a cookie.  The cookie persists (unless the user logs out). There is no password,
you have to log in my Email each time.

Google uses the user service to set the cookie.  Facebook uses OAuth.  In all cases
the cookie persists until the user logs out (or the browser deleted it).

This solution is _only_ for authentication.  You'll need to set up servlet filters for
authorization. However, a lot of the time simple authentication/id is all that's wanted.

This solution is very simple and does not hit the datastore in any way so it should be
fast and lightweight for App Engine.

Enjoy.


The License is MIT.