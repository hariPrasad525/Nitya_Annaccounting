<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> Grace Period
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<%
String expireDate = (String)request.getAttribute("expiredDate");
Integer remainigDays =(Integer) request.getAttribute("remainigDays");
java.util.Set<String> users =(java.util.Set<String>) request.getAttribute("users");
Integer premiumType =(Integer) request.getAttribute("premiumType");
String user_Name=(String)request.getAttribute("userName");
%>

<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
</head>
	<body>
		<div id="commanContainer" class="thankyou-form">
		   <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
			  	<p>Subscription Notification</p>
			  	<% if(premiumType==0){ %>
			  		<p>Dear <%= user_Name %>, Your subscription has been expired  on <%= expireDate %>.
			  	<% } else { %>
			  		<p>Dear <%= user_Name %>, Your degraded subscription will be expire on <%= expireDate %>.
			  	<% } %>		    
			  	We will provide <%= remainigDays %> grace period. After this period following users will be delete.
			  	<c:if test="${users!=null}"> 
				  	<c:forEach items="<%= users %>" var="name">  
					<span>${name}</span></br>
					</c:forEach>
				</c:if>
			    </br>
			    Click here to <a href="/main/companies?type=gracePeriod">Continue</a></br>
				If you want to upgrade <a href="/main/subscriptionmanagement">Click here</a>
		</div>
	     <!-- Footer Section-->
	<%@ include file="./scripts.jsp" %>
		</body>
</html>