<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> -->
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
 	<head>
 	<title>Registration</title>
	<script src="../jscripts/jquery-1.7.min.js" type="text/javascript"></script>
	<script src="../jscripts/jquery.validate.js" type="text/javascript"></script>
  	<script  type="text/javascript" >

	$(document).ready(function() {	   
	
		$('#submitButton').click(function() {
			$("#accounterForm").validate({
		rules: {
			applicationName: "required",
			description: "required",
			integrationUrl:"required",
			applicationType:"required",
			applicationUse:"required",			
			developerEmailId: {
				required: true,
				email: true
			}			
		},
		messages: {
			applicationName: "<i18n:i18n msg='pleaseenteryourapplicationname'/>",
			description: "<i18n:i18n msg='pleaseenterdescription'/>",
			integrationUrl: "<i18n:i18n msg='pleaseenterIntegrationUrl'/>",
			applicationType: "<i18n:i18n msg='pleaseenterApplicationType'/>",
			applicationUse: "<i18n:i18n msg='pleaseenterApplicationUse'/>",			
			developerEmailId: "<i18n:i18n msg='pleaseenterDeveloperEmailId'/>"			
		}
	});
			 <!--$.blockUI({ message: $('#hiddenLoaderDiv'), css: {height: '80px', width: '300px'} });--> 
			<!--document.getElementById("hiddenLoaderDiv").style.display="block";-->
    		<!--document.getElementById("signup").style.display="none";-->
		});
		
		$('#select-box').keydown(function(event) {
			if (event.keyCode == '9') {
			     event.preventDefault();
			  $('#checkbox').focus();
			}	
			});
		 
	});
</script>

   
  <link rel="stylesheet" href="../css/ss.css" type="text/css"></link>
  </head>
  

 <div id="commanContainer" class="api_registration">
  <body>
<div id="accounterlogofield" class="new_logo_field">
<a class="accounterLogoimage" href="/site/home"></a>
</div>

<c:if test="${errormessage!=null}">
	<div id="login_error" class="common-box">
		<span>${errormessage}</span>
	</div>
  </c:if>
   <c:if test="${successmessage==null}">
  <form id="accounterForm" name="accounterForm" method="post" action="/apiregistration">
  
  <div class="accounterform">
   <div class="simple-get-started">
<h4 align="center">Application Information:</h4>
</div>
  <div class="check_label" style="clear:both">


<label><i18n:i18n msg='applicationName'/></label><br />
<input id="mid-box1" type="text" name="applicationName" value="${param.applicationName}" tabindex="1">
</div>
<div>

<div class="check_label">

<label><i18n:i18n msg='description'/></label><br />
<input id="mid-box2" type="text" name="description" value="${param.description}" tabindex="1">
</div>
<div class="check_label">



<label><i18n:i18n msg='integrationUrl'/></label><br />
<input id="mid-box3" type="text" name="integrationUrl" value="${param.integrationUrl}" tabindex="1">
</div>
<div>

<div class="check_label">
<label><i18n:i18n msg='applicationType'/></label><br />
<select id="select-box" tabindex="8" name="applicationType" value="${param.applicationType}">
<option value="Web Application">Web Application</option>
											<option value="Desktop Application">Desktop Application</option>
											<option value="Mobile Application">Mobile Application</option>
</select>											
</div>
<div class="check_label">

<label><i18n:i18n msg='applicationUse'/></label><br />
<input id="mid-box4" type="text" name="applicationUse" value="${param.applicationUse}" tabindex="1">
</div>


<div class="check_label">
<label><i18n:i18n msg='contactInformation'/></label><br />
<input id="mid-box5" type="text" name="contactInformation" tabindex="1">
</div>


<div class="check_label">
<label><i18n:i18n msg='developerEmailId'/></label><br />
<input id="mid-box6" type="text" name="developerEmailId" value="${param.developerEmailId}" tabindex="1">
</div>
<div class="check_label">
<label>Contact Number</label>

<label><i18n:i18n msg='contact'/></label><br />
<input id="mid-box7" type="text" name="contact" value="${param.contact}" tabindex="1">
</div>
<div class="signup-submit">
<input id="submitButton" class="allviews-common-button" type="submit" tabindex="11" value="Add Application" name="submitButton">
</div>
</div>
</form>
</c:if>
</div>
</body>
</html>
