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
                    $("#loggedIn").show().html("<p>You are logged in as " + user + "</p>");
                    $("#loggedOut").hide();
                } else {
                    $("#loggedIn").show().html("<p>You are not logged in</p>");
                    $("#loggedOut").show();
                }
            },
            error: function(jqXHR, status, error) {
                console.log("weird error: " + status);
            }
        });

        $("#loginByEmail").on("click", function(e) {
            e.preventDefault();
            var emailAddress = $("#email").val().trim();
            $.ajax({
                url: "/login/email",
                method: "post",
                dataType: "json",
                data: {email: emailAddress},
                success: function(data) {
                    alert(data.message);
                },
                error: function(jqXHR, status, error) {
                    var json = jqXHR.responseJSON;
                    if (json) {
                        alert("login failed with message " + json.message);
                    } else {
                        alert("login failed with error " + error);
                    }
                }
            })
        });
    });
}(jQuery));