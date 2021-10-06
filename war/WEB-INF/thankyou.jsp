<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> <i18n:i18n msg='subscriptionManagement'/>
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
</head>
	<body>
		<div id="commanContainer" class="thankyou-form">
		    <div id="accounterlogofield" class="new_logo_field">
			    <a class="accounterLogoimage" href="/main/login"></a>
			  </div>
		    <div class="thank-you-data">	
			   <div class="thankyou-header">
				    <p> Thank you</p>
			    </div>
			    <br>
			    <div>
			    <p>
			        Your subscription is successfully updated.
			   		Please Login to your Anna Accounting to use the updated subscription features.
			   		You can manage your subscription by clicking "Manage Subscription" link after login.
			   	</p>
			   	<br>
			   	 <a class="login-thankyou button-links" href="/main/login">Login</a>
			    </br>
			   </div>
		   </div>
		</div>
		
		
	     <!-- Footer Section-->
		
	 
		
		 
	<%@ include file="./scripts.jsp" %>
		</body>
</html>