<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">

<jsp:include page="header.jsp" flush="true"/>

  <div class="container">
    <div class="w3-container">
      <% if ((Boolean) request.getAttribute("loggedIn")) { %>
        <h4>You are logged in!</h4>
      <% } else { %>
        <h4>You are not logged in! Please <a href="<%= request.getAttribute("partnerLoginEndpoint") %>">Log In</a> to continue.</h4>
      <% } %>
    </div>
  </div>
  </body>
</html>
