<head>
  <title>Partner</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>
    <nav class="navbar navbar-default navbar-static-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Auth0 - Java Servlets</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li><a href="/">Home</a></li>
              <% if ((Boolean) request.getAttribute("loggedIn")) { %>
              <li><a href="/user">Profile</a></li>
              <% } else { %>
              <li><a href="/login">Login</a></li>
              <% } %>
          </ul>
          <% if ((Boolean) request.getAttribute("loggedIn")) { %>
            <ul class="nav navbar-nav navbar-right">
              <li><a href="/logout">Logout</a></li>
            </ul>
          <% } %>
        </div>
      </div>
    </nav>
