<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> <i18n:i18n msg='licenseExpired'/>
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
</head>
	<body>
		<div id="commanContainer" class="thankyou-form">
		   <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
		    <div class="thank-you-data">	
			   <div class="thankyou-header">
				    <p> Sorry, </p>
				     Your license is no longer valid.
			    </div>
			    <div style="text-align: center;">
			    <p>
			   	Generate a new license for Free trail of 30days <a href="http://app.annaccounting.com/main/manageLicense?gen=true" target="_blank">here</a> 
			   	</p>
			   	<div>OR</div>
			   	<p>
			    You can purchase license <a href="http://app.annaccounting.com/main/purchaseLicense" target="_blank">here</a>
			    </p>
			    </br>
			   </div>
		   </div>
		</div>
		
		 
	<%@ include file="./scripts.jsp" %>
		</body>
</html>