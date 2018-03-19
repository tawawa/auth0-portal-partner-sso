<%@ page import="com.auth0.Auth0User" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">

<jsp:include page="header.jsp" flush="true"/>

<script>
     <% Auth0User user = (Auth0User) request.getAttribute("user"); %>
</script>

<section class="text-center">
   <img class="jumbo-thumbnail" src="<%= user.getPicture() %>"/>
   <h4>Welcome, <%= user.getNickname() %></h4>
   <div>
     <button id="check-session">Check Session</button>
   </div>
</section>

<script>
    $('#check-session').on('click', function(e) {
      e.preventDefault();
      checkSession();
    });
    function checkSession() {
      window.location = '/check';
    }
    //- setInterval(function() {
      //- checkSession();
    //- }, 5000);
</script>

