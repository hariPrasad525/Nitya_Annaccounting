<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" %>
<html>

  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta content="IE=100" http-equiv="X-UA-Compatible">
    <%@ include file="./feedback.jsp" %>
    <link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
    <title>Anna Accounting Admin</title>
    <script> 
    var isAdmin = true; 
//     var AccounterMessages = {}
    </script> 
   
  </head>

  <body>
	      <div width="25%"><img src="/images/Accounter_logo_title.png" /></div>
	<script type="text/javascript" language="javascript" src="/com.nitya.accounter.admin.Admin/com.nitya.accounter.admin.Admin.nocache.js"></script>
	<span>accounter admin<span><a href='/adminlogout'>Logout</a>
	<%@ include file="./scripts.jsp" %>
 </body>
</html>
