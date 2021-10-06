<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>

<title>Anna Accounting | Subscription </title>
<link type="text/css" href="../../css/ss.css" rel="stylesheet" />
<%
String info =(String) request.getAttribute("message");
%>
</head>
<body align="center">
	<div id="commanContainer" style ="width: 700px !important">
	   <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
		<% if(info!=null) { %>
			<div id="login_success" class="common-box">
				<span><%= info %></span>
			</div>
		<% } %>
		<form align="center" id="accounterForm"   action="/main/decryption" style="width:650px" class="accounterform" method="post">
			 Company Name
			<input type="text" id= "companyName"  name="companyName" >
			 Enter password
			<input type="password" name="password" >	
			<input type="submit" value="Submit"/>		
	   </form>
	</div>  
</body>
</html>