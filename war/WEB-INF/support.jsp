<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>Get Support | Anna Accounting
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />
<script  type="text/javascript" >

$.validator.addMethod("no_special_characters", function(value, element) {
    return this.optional(element) ||
        value.match(/^[a-zA-Z0-9@_ ]*$/);
}, "shouldn't contain any special characters");
	$(document).ready(function() {
		<%	boolean isSupportRTL=(Boolean) request.getAttribute("isRTL");	%>
		document.body.style.direction=(<%= isSupportRTL %>)?"rtl":"ltr";
	
	
	$('#blog-bg').click(function(event){
		event.preventDefault();
		$('.menu-bar').find('a').removeClass("header-hover");
		$(this).addClass("header-hover");
		window.open("http://blog.annaccounting.com/","_blank");
	});
		$('#submitButton').click(function() {
			$("#contactform").validate({
		rules: {
			name: {
				required: true,
				no_special_characters: true
			},
			subject: {
				required: true,
				no_special_characters: true
			},
			emailId: {
				required: true,
				email: true
			},
			message: {
				required: true,
			}
		},
		messages: {
			name: {
				required: "please enter your name",
				no_special_characters: "name should not contain any special characters"
			},
			emailId: "please enter a valid email",
			subject: {
				required: "please enter subject",
				no_special_characters: "subject should not contain any special characters"
			},
			message: {
				required: "please enter message",
				no_special_characters: "message should not contain any special characters"
			}
		}
	});
		});
	});	
</script>


</head>
<body>
	<div class ="body-container">
			<div class ="main_body-container">
			<div class ="header" id="top">
			
			<div class="menu-table">
			   <div class="acc_logo_image">
			       <div class="logo-image">
					  <a href="/"><img src="/images/Accounter_logo_title.png" alt="Anna Accounting logo"></img></a>
				   </div>
			   </div>
			   <div class="menu-bar-login">
			      <div class="menu-bar">
				<ul>
<!-- 					 <li> -->
<!-- 					    <a href="/site/home" id="home-bg" >Home</a> -->
<!-- 					 </li>  -->
					 <li>
					    <a href="/site/features" id="fea-bg">Features</a>
					 </li> 
					 <li>
						<a href="/site/support" id="sup-bg" class="header-hover">Support</a>
					</li>
<!-- 					 <li> -->
<!-- 					    <a href="http://blog.annaccounting.com/" id="blog-bg" target ="_blank" >Blog</a> -->
<!-- 					 </li> -->
			  </ul>
				</div>
				<div class="remaining-header-part-login">
					<ul>
					   <li><a href="/main/login" id="login-id">Login</a></li>
					</ul>
				</div>
			   </div>
			</div>
			</div>
			<div class="middle-part" id="cen">
			  <div class="support_info_view">
				<div class="pricing-table" id="support-width">
					<div>
					<p class="sites-header">Support</p>
					</div>
					<div id="contact_cont" style="margin-top: 0px;">
				<div id="contact_content">
					<b>Our office address:</b><br />
						<br />
							<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NITYA SOFTWARE SOLUTIONS, INC,<br />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CA Office :  3100 Mowry Ave,<br />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Suite 205, Fremont CA,<br />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tel: (510) 790-7020<br />
				</div>
				<div id="contact_form_cont" class="contact_form_cont">
					<form id="contactform" name="contactform" method="post" action="/main/support">
						<fieldset class="fieldset">
						<c:if test="${message != null}">
								<span style="color: #008AC3; line-height: 1.5;">
								${message} </span>
						</c:if>
						<c:if test="${errormessage != null}">
								<span style="color: red; line-height: 1.5;">
								${errormessage} </span>
						</c:if>
							<table>
							 	<tr>
									 <td style="width : 100px">Name <span class="red">*</span> </td>
									 <td>
										<input id="mid-box"  type="text" tabindex="1" name="name" /> 
					
									 </td>
								</tr>
								<tr>
									 <td style="width : 100px">Email <span class="red">*</span> </td>
									 <td>
										<input id="mid-box1"  type="text" tabindex="2" name="emailId" />
					
									 </td>
								</tr>
								<tr>
									 <td style="width : 100px">Subject <span class="red">*</span> </td>
									 <td>
										<input id="mid-box2"  type="text" tabindex="3" name="subject" />
					
									 </td>
								</tr>
								<tr>
									 <td style="width : 100px; vertical-align: top;">Message <span class="red">*</span> </td>
									 <td>
										<textarea class="support_textarea" id="message" name="message" style="margin-top : 10px;" rows="" cols="" tabindex="4"></textarea>
									 </td>
								</tr>
								<tr>
								   <td></td>
								   <td>
								      <ul class="signup-submit-button support_send">
								         <li><input id="submitButton"  class="signup-submit-mid allviews-common-button" type="submit" value="Send"  tabindex="5" /></li>
								      </ul>
								   </td>
								</tr>
							</table>
							
							</fieldset>
							</form>
					</div>

	</div>
				</div>
				</div>
			</div>
		<div class="down-test" id="down">
		</div>
		<%@ include file="./footer.jsp" %>
	</div>
	
 
 
		</div>
		
		<%@ include file="./scripts.jsp" %>
</body>
</html>