<!DOCTYPE html>
<html lang="en" class="user-unknown" manifest="/cache.manifest">
<head>
<#assign title="Cilogi: Mobile Guides for Museums, Galleries and Cultural Heritage Sites, Glasgow, UK."/>
<meta name="description" content="Provider of Mobile Guides for any type of Collection.  Simple to create, free to deliver, with integrated shop." />
<#include "inc/head.ftl">
</head>

<body>

<nav class="navbar navbar-fixed-top" style="border-bottom: 1px solid #337ab7;">
<#include "inc/navbar-internals.ftl">
</nav>

<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron panel">
    <div class="container fluid">
        <h1>Cilogi Mobile Guides</h1>

        <p style="margin-top: 2em;">
            <a href="/signup/signup.html" class="btn btn-primary btn-lg" role="button">Beta Sign Up &raquo;</a>
        </p>
    </div>
</div>

<div class="advert container-fluid light large">
    <!-- Example row of columns -->
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3">
            <h2>Simple</h2>
            <p>Create guides without coding. Edit on your desktop or tablet. Collaborate with colleagues.</p>

            <p><a class="btn btn-default" href="/example.html" role="button">Example &raquo;</a></p>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3">
            <h2>Engaging</h2>
            <p>See what visitors are interested in with integrated analytic tools.
               Work with social media.</p>

            <p><a class="btn btn-default" href="/features.html" role="button">Features &raquo;</a></p>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3">
            <h2>Profitable</h2>
            <p>Sell both physical and digital goods from an integrated web shop.</p>
            <p><a class="btn btn-default" href="/shop.html" role="button">Sales &raquo;</a></p>
        </div>
    </div>


    <hr>

<#include "inc/gafoot.ftl"/>
<#include "inc/copyright.ftl"/>
</div>
<!-- /container -->


<script src="scripts/app/mainConfig.js"></script>
<script src="scripts/require.js" data-main="scripts/app"></script>
</body>
</html>
