<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
  <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title> <i18n:i18n msg='activation'/> | Anna Accounting
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
		<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />
		<script  type="text/javascript" >
			$(document).ready(function() {
				<%	boolean iaActivationRTL=(Boolean) request.getAttribute("isRTL");	%>
				document.body.style.direction=(<%= iaActivationRTL %>)?"rtl":"ltr";			
			
				$('#submitButton').click(function() {
					$("#activationForm").validate({
						rules: {
						code: "required",
						},
						messages: {
						code: "<i18n:i18n msg='pleaseentertheactivationcodeyougotinthemail'/>" 
						}
					});
				});
				$('input#actiovationTextbox').keydown(function(e) {
					  if (e.keyCode == '9' || e.which =='9') {
					     e.preventDefault();
					    $('#submitButton').focus();
					   }
					});
				$('#submitButton').keydown(function(e) {
					  if (e.keyCode == '9'|| e.which =='9') {
					     e.preventDefault();
					    $('#emailforactivation').focus();
					   }
					});
			});
			
		</script>
    </head>
    <body>
	    <div id="commanContainer">
		 <img src="/images/Accounter_logo_title.png" class="accounterLogAnna AccountingAccounter logo"/>
		  <c:if test="${successmessage!=null}">
			<div id="login_success" class="common-box">
				<span>${successmessage}</span>
			</div>
  		  </c:if>
  		  
		 <form class="accounterform" id = "activationForm" action="/main/activation" method="post">
		    <div class="reset-header">
			   <h2> <i18n:i18n msg='activationCode'/></h2>
			</div>
			<div>
			  <label><i18n:i18n msg='validActivation'/></label>
			  <input id ="actiovationTextbox" type="text" name="code" />
			</div>
			<div class="reset-button">
			   <input type="submit" tabindex="3" value="<i18n:i18n msg='activate'/>" name="activate" class="allviews-common-button" id="submitButton" />
			</div>
		 </form>
		 <div class="resend-code">
		 	<a id= "emailforactivation" href="/main/emailforactivation"><i18n:i18n msg='resendActivationcode'/></a>
		 </div>
     </div>
     
     <!-- Footer Section-->
     

<!--      <div id="appVersions"> -->
<!-- 	    <div> -->
<!-- 	       <span>Access Anna Accounting from </span> -->
<!-- 	       <a target="_blank" href="https://play.google.com/store/apps/details?id=com.vimukti.accounter.android"> Android </a> | -->
<!-- 	       <a target="_blank" href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> Windows Phone </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> iPhone </a> | -->
<!-- 		   <a target="_blank" href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> Black Berry </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> iPad </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> Mac OS </a> -->
<!-- 		   </div> -->
<!-- 	</div> -->
          <div id="mainFooter"  >
	    <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span> |
	       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
	       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
	       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
	    </div>
	</div>
	<%@ include file="./scripts.jsp" %>
	</body>
</html>