<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> <i18n:i18n msg='resetPassword'/> | Anna Accounting
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<link rel="shortcut icon" href="/images/favicon.ico" />

<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />

<script src="/jscripts/passwordStrength.js" type="text/javascript"></script>
<script type="text/javascript" >

$(document).ready(function() {
		<%	boolean isResetPasswordRTL=(Boolean) request.getAttribute("isRTL");	%>
		<%	String type=(String) request.getAttribute("type");	%>
		if("openid" == "<%= type%>")
			$("#info").append("<div class='openidinfo'><i18n:i18n msg='openidinfo'/></div>");
		document.body.style.direction=(<%= isResetPasswordRTL %>)?"rtl":"ltr";
//	$('#mid-box').attr('autocomplete', 'off');
//	$('#mid-box').password_strength();
 $('#submitButton').click(function() {
		$("#accounterForm").validate({
			rules: {
				newPassword: "required",
				confirmPassword: {
					required: true,
					equalTo: "#mid-box"
				}
			},
			messages: {
				newPassword: "<i18n:i18n msg='pleaseenteryourpassword'/>",
				confirmPassword: {
					required: "<i18n:i18n msg='pleaseconfirmyourpassword'/>",
					equalTo: "<i18n:i18n msg='pleaseenterthesamepasswordasabove'/>"
				}
			}
		});
	});
	
	$('.reset_password').click(function(){
	    $('.indication-box').remove();	
	     $('#reset_hint_box').append("<div class='indication-box'><div class='left-arrow'></div><div class='box-data'><i18n:i18n msg='use3to60characters'/></div></div>");        
	    }).blur(function() {
	        $('.indication-box').remove();
	 });
	
});	
	
	 function CheckPassword(password)
	 {
 	    var strength = new Array();
 	    strength[0] = "Very Weak";
 	    strength[1] = "Weak";
 	    strength[2] = "Medium";
 	    strength[3] = "Strong";
 	    strength[4] = "Very Strong";
 	 
 	    var score = 1;
 	 
 	    if (password.length < 4)
 	        return strength[1];
 	 
 	    if (password.length >= 8)
 	        score++;
 	    if (password.length >= 12)
 	        score++;
 	    if (password.match(/\d+/))
 	        score++;
 	    if (password.match(/[a-z]/) &&
 	        password.match(/[A-Z]/))
 	        score++;
 	    if (password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,�,(,)]/))
 	        score++;
 	 
 	    return strength[score];
 	}
</script>
</head>
	<body>
	<div id="commanContainer">
		   <img class="accounterLogo" src="/images/Accounter_logo_title.png" alt="Anna Accounting logo" />
		   	<c:if test="${errormessage!=null}">
			<div id="login_success" class="common-box">
				<span>${errormessage}</span>
			</div>
  		  </c:if>
		 <form class="accounterform" id="accounterForm" method="post" action="/main/resetpassword">
								<c:if test="${successMessage != null}">
								<span style="color: #008AC3; line-height: 1.5;">
								${successMessage} </span>
								</c:if>
								<c:if test="${errorMessage != null}">
								<span style="color: #CC0000; line-height: 1.5;">
								${errorMessage} </span>
								</c:if>
								<c:if test="${successMessage == null}">
								<c:if test="${errorMessage == null}">
								<div class="mid-login-box1">
							   <div >
							   		 <div id="info"></div>
								     <div><i18n:i18n msg='newPassword'/> : </div>
									 <div>
										<input id="mid-box"  type="password" name="newPassword" onkeyup="CheckPassword(this.value)" tabindex="1" value="" class="reset_password" />								
									 </div>
									 <div id="reset_hint_box">
									 
									 </div>
									 <div><i18n:i18n msg='confirmPassword'/> : </div>
									 <div>
										<input id="mid-box1"  type="password" name="confirmPassword" tabindex="2" value="" />
									</div>
								   <div class="OkButton">
			  						   <input type="submit" tabindex="6" value="<i18n:i18n msg='reset'/>" name="login" class="allviews-common-button" style="width:60px" id="submitButton" />
			 						 </div>
								  </div>
								</c:if>
								</c:if>
								</div>
					</form>
					</div>
	<div class="down-test" id="down"></div>
		
	<%@ include file="./scripts.jsp" %>
		</body>
</html>