<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> -->
<html>
  <head>
  <title>Api Info</title>
  <link rel="stylesheet" href="../css/ss.css" type="text/css"></link>
  </head>  
  <body>
  
  <div id="commanContainer">
  <div id="accounterlogofield" class="new_logo_field">
<a class="accounterLogoimage" href="/site/home"></a>
</div>
  <div id="company-locked" class="form-box">
  	<table align="center">
  	<th>Developer Details</th>
  	</table>
  	<table align="center">
  	<tr><td>Api Key</td><td>:</td><td>${apiKey}</tr>
  	<tr><td>Secret Key</td><td>:</td><td>${secretKey}</tr>
  	<tr><td>Application Name</td><td>:</td><td>${applicationName}</tr>
  	<tr><td>Developer Email Id</td><td>:</td><td>${developerEmailId}</tr>
  	</table>
  	</div>
  	</div>
  	</body>
  	</html>