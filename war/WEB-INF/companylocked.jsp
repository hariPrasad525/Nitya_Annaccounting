<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> Company locked
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<meta http-equiv="refresh" content="5">
<link rel="shortcut icon" href="/images/favicon.ico" />
<% String version = application.getInitParameter("version"); %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />
<%
	Integer reasontype =(Integer) request.getAttribute("reasonType");
	String reason ="of internal problem";
	if(reasontype ==1){
		reason = "Your Company data encryption processing in background.";
	}
	
%>

</head>
	<body>
		<div id="commanContainer">
		   <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
		   <div id="company-locked" class="form-box">
		      <p>Your company has been locked because <%= reason%>...Please wait a while.</p>	
		   </div>
		   <div class="form-bottom-options">
		     <a   href="/main/companies" style="padding-right:10px"><i18n:i18n msg='companieslist'/></a>
		     <a   href="/main/logout" ><i18n:i18n msg='logout'/></a>
		   </div>
		</div>
		
		
	     <!-- Footer Section-->
		
		<div id="mainFooter"  >
	    <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span> |
	       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
	       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
	       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
	    </div>
	</div>
		
		<div id="appVersions">
<!-- 	    <div> -->
<!-- 	       <span>Access Anna Accounting from </span> -->
<!-- 	       <a target="_blank" href="https://market.android.com/details?id=com.vimukti.accounter"> Android </a> | -->
<!-- 	       <a target="_blank" href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> Windows Phone </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> iPhone </a> | -->
<!-- 		   <a target="_blank" href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> Black Berry </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> iPad </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> Mac OS </a> -->
<!-- 		   </div> -->
	</div>
		 
	<%@ include file="./scripts.jsp" %>
		</body>
</html>