<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>  <i18n:i18n msg='deleteMyAccount'/>
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<link rel="shortcut icon" href="/images/favicon.ico" />

<%@ include file="./feedback.jsp" %>
<% 
	Boolean ipad = (Boolean)request.getAttribute( "ipad" );
	%>
	<% if(ipad != null && ipad){%>
	<link type="text/css" href="../css/ipadlogin.css?version=<%= version%>" rel="stylesheet" />
	<% }else{%>
	<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<% }%>
<script type="text/javascript">
<%	boolean isDeleAccRTL=(Boolean) request.getAttribute("isRTL");	%>
window.onload=function(){
document.body.style.direction=(<%= isDeleAccRTL %>)?"rtl":"ltr";
};
</script>
</head>
<body>
<div id="commanContainer">	
	<div>
		<img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="loading" />
	</div>
	
	<div>
		<form class="accounterform" id="deleteform" name="deleteform" method="post" action="/main/deleteAccount">
		<div class="company-heading" style="text-align:center"><h3><i18n:i18n msg='deleteAccount'/></h3></div>
		<div class="delete_company"><div>
			<h4><i18n:i18n msg='deleteaccountmsg'/></h4><br \>
		</div>
		<span><i18n:i18n msg='whyareyouleavingAccounter'/></span><br \>
		<div class="delete-account-options">
			<input id="tooslow" type="checkbox" name="tooslow"  /> <i18n:i18n msg='tooSlow'/><br />
			<input id="takelong" type="checkbox" name="takelong"  /><i18n:i18n msg='takeslongtimetolearn'/> <br />
			<input id="mydata" type="checkbox" name="mydata"  /><i18n:i18n msg='cantgetmydatain'/> <br />
			<input id="features" type="checkbox" name="features"  /><i18n:i18n msg='doesnthavethefeaturesIneed'/> <br />
			<input id="wentoutof" type="checkbox" name="wentoutof"  /><i18n:i18n msg='wentoutofbusiness'/> <br />
			<input id="personalfinance" type="checkbox" name="personalfinance"  /><i18n:i18n msg='imLookingforpersonalfinancesoftware'/> <br />
			<input id="nobusinessyet" type="checkbox" name="nobusinesssyet"  /><i18n:i18n msg='idonothaveabusinessyet'/> 
		</div>
			<br \>
		    <h4><i18n:i18n msg='otherReasons'/> :</h4>
			<textarea class="delete-account-description" name="content"></textarea></div>
			<div>
	  			  <div class="signup-submit">
	      			<input type="button" class="allviews-common-button" value="<i18n:i18n msg='backtoaccount'/>" onclick="parent.location='/main/companies'" />
	  			 </div>
	  			 <div class="signup-submit">
	      			<input type="submit" class="allviews-common-button" value="<i18n:i18n msg='continuetofront'/>" />
	  			 </div>
			</div>
		</form>
	</div>
  <%@ include file="./scripts.jsp" %>
 </div>
</body>
</html>