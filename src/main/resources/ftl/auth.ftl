<!DOCTYPE html>
<html lang="en">
<head>
    <title>Lid Authenticated Users</title>
    <link href="/styles/style.css" rel="stylesheet">
</head>

<body>
<h1>Authenticate Users only Page</h1>

<p id="loginStatus">Acquiring Login Status...</p>

<div id="loggedIn">
    <p>You have to be logged in to see this page.</p>
    <p><a href="/logout">Logout</a></p>
</div>

<div id="loggedOut" style="display:none">
    <p>This should not be possible!</p>
</div>
<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="/scripts/script.js"></script>
</body>
</html>