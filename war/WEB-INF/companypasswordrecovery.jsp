<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> Company Password recovery
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../../../../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />

<script  type="text/javascript" >

$(document).ready(function() {
	$('#submitButton').click(function() {
		$("#accounterForm").validate({
			rules: {
				recoveryKey: {
					required: true,
			},
			password: {
				required: true,
				minlength: 6
			},
			confirm: {
				required: true,
				minlength: 6,
				equalTo: "#mid-box2"
			},
			messages: {
				recoveryKey: "<i18n:i18n msg='shouldNotEmpty'/>",
				password: {
				required: "<i18n:i18n msg='pleaseprovideapassword'/>",
				minlength: "<i18n:i18n msg='yourpasswordmustbeatleast6characterslong'/>"
			},
			confirmPassword: {
				required: "<i18n:i18n msg='pleaseprovideapassword'/>",
				minlength: "<i18n:i18n msg='yourpasswordmustbeatleast6characterslong'/>",
				equalTo: "<i18n:i18n msg='pleaseenterthesamepasswordasabove'/>"
			},
				 
			}
			}
		});
	});
});	
	
</script>
</head>
	<body>
		<div id="commanContainer">
		   <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
		   <div id="forgot-password_error" class="common-box">
		      <p>Company password recovery</p>	
		   </div>
		    <c:if test="${info != null}">
			   <div id="login_error" class="common-box">
					<span>${info} </span>
			   </div>
		   <form class="accounterform" id="accounterForm" method="post" action="/main/company/password/recovery">
		      <div>
			    <label>Recovery key</label>
				 </br>
				<input id="mid-box"  type="text" name="recoveryKey" tabindex="1"  />
			  </div>
			   <div>
			    <label>New Password</label>
				 </br>
				<input id="mid-box1"  type="text" name="password" tabindex="2"  />
			  </div>
			   <div>
			    <label>Confirm Password</label>
				 </br>
				<input id="mid-box2"  type="text" name="confirm" tabindex="3"  />
			  </div>
			  <div id="forgot-login">
			     <input type="submit" tabindex="4" value="<i18n:i18n msg='submit'/>" name="ok" class="allviews-common-button" id="submitButton" />
			  </div>
		   </form>
		   <div class="form-bottom-options">
		      <a href="/main/login" ><i18n:i18n msg='login'/></a>
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
		
<!-- 		<div id="appVersions"> -->
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