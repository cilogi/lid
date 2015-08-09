/*global jQuery */
(function($) {
    'use strict';

    var user;

    $(document).ready(function() {
        $.ajax({
            url: "/user/status",
            method: "post",
            dataType: "json",
            success: function(data) {
                user = data.user;
                if (user) {
                    $("#loginStatus").show().html("<p>You are logged in as " + user + "</p>");
                    $("#loggedIn").show();
                    $("#loggedOut").hide();
                } else {
                    $("#loginStatus").show().html("<p>You are not logged in</p>");
                    $("#loggedIn").hide();
                    $("#loggedOut").show();
                }
            },
            error: function(jqXHR, status, error) {
                console.log("weird error: " + status);
            }
        });
    });
}(jQuery));