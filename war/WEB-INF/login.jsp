<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> <i18n:i18n msg='login'/> | Anna Accounting
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

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

<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />


<%
   String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
   if( isNative ){ %>
   
   <% } %>
   
<script src="https://www.google.com/recaptcha/api.js?render=6LdZG5IUAAAAANewD-r1A7kGFPQeDAHbyQgIWUy7"> </script>
<script  type="text/javascript" >
<%
	String news = (String)request.getAttribute("news");
	String destination =(String) request.getAttribute("destination");
%>
	var news=<%= news%>;
	var isNative=<%= isNative%>;
	
	$(document).ready(function() {
		<%	boolean isLoginRtl=(Boolean) request.getAttribute("isRTL");	%>
		document.body.style.direction=(<%= isLoginRtl %>)?"rtl":"ltr";
		if(isNative){
			$('#accounterlogofield').append('<a target="_blank" class="accounterLogoimage" href="/main/login"></a>');
		}else{
			$('#accounterlogofield').append('<a class="accounterLogoimage" href="/main/login"></a>');
		}
		
		
	$('#submitButton').click(function() {
	 $("#submitButton").addClass("login-focus");
	$("#accounterForm").validate({
		rules: {
			emailId: "required",
			password: "required",
			},
		messages: {
			emailId: "<i18n:i18n msg='pleaseenteryouremailid'/>",
			password: "<i18n:i18n msg='pleaseenterpassword'/>",
			}
		});
	
	
	
	});
	if(news.list.length>0){
		$('#unChangedNewsDiv>h3>a').attr("href" ,news.list[0].url);
		$('#unChangedNewsDiv>h3>a').text(news.list[0].title);
		$('#news_body').text(news.list[0].body);}
		var changingindex =1;
		var setTimeIntervel;
		timerStarted();
			for(var i=0; i <  news.list.length; i++){
				$(news.list[i]).attr("clientid" ,i);
				var listItemDiv = $("<li id="+news.list[i].clientid+"><a target=_blank href='"+news.list[i].url+"'>"+news.list[i].title+"</a></li>");
					$('#feedDivUl').append($(listItemDiv));
			}
			
			$('li').each(function(index) {
					var id=$(this).attr("id");
			$('#'+id).mouseover(function(){
				$(this).addClass("listItemHover");
				changingindex = parseInt(id);
				cahngeContent();
			}).mouseout(function(){
				$(this).removeClass("listItemHover");
				})
			});
			
			$('#feedDiv').mouseover(function(){
				clearInterval(setTimeIntervel);
			}).mouseout(function(){
				timerStarted();
				})
			
			function timerStarted(){
			  setTimeIntervel =	setInterval ( function (){
				  $('li').removeClass("listItemHover");
				  $('#'+changingindex).addClass("listItemHover");
				  if(changingindex <news.list.length)
					changingindex = changingindex+1;	
				  if(news.list.length>1){
				  $('#unChangedNewsDiv>h3>a').attr("href" ,news.list[changingindex-1].url);
					$('#unChangedNewsDiv>h3>a').text(news.list[changingindex-1].title);
					$('#news_body').text(news.list[changingindex-1].body);}
					if(changingindex ==news.list.length){
						changingindex=0;
					}
				}, 3000 );
				
				
			}
			function cahngeContent(){
				 $('#unChangedNewsDiv>h3>a').attr("href" ,news.list[changingindex].url);
					$('#unChangedNewsDiv>h3>a').text(news.list[changingindex].title);
					$('#news_body').text(news.list[changingindex].body);		
					}
		 
	 
	});	
</script>

</head>
	<body>
	 
	     <div id="commanNewContainer" class="loginpage_container">
			  <div id="accounterlogofield" class="new_logo_field">
			    
			  </div>
			  <%@ include file="./locale.jsp" %>
			  <div class="new_login_page">
			  <div id ="feedDiv">
			  <h2><i18n:i18n msg='welcomeToStartup'/></h2>
			  <div id="unChangedNewsDiv" class = "news"><h3>
			  <a target="_blank"></a></h3>
			  <div id='news_body'>
			    ANNA ACCOUNTING ONLINE is web-based application, The data flows from web interface.It provides customized dash board for creating and managing multiple businesses related to 20 different industries,like Financial Accouting Services,IT Software Consulting, Hotel/Motel, Manufacturing Companies, Lawyers or Legal Advisory Services,etc.extensible to suit the user requirement.Simple to use with intuitive navigation. User is resonsble for accracy of data.
			  </div>
			  </div>
			  <ul id="feedDivUl"></ul>
			  </div>
			   
			   <div class="new_login_accounterform">
			  
			   <c:if test="${message != null}">
			   <div id="login_error" class="common-box">
					<span>${message} </span>
			   </div>
			   </c:if>	
			   <% if(ipad != null && ipad){%>
  						<h3>Anna Accounting</h3>
				<% }else{%>
  						<h3><i18n:i18n msg='signIn'/></h3>
				<% }%>
			 
			   <form id="accounterForm" method="post" action="/main/login">
			   		<c:if test="${destination != null}">
			   		<input type="hidden" name="destination" value="${destination}">
			    	</c:if>	
			      <div class="email_field">
				    <label><i18n:i18n msg='email'/></label>
					<br \>
					<input id="mid-box"  type="text" name="emailId" tabindex="1" />
				  </div>
				  <div class="password_field">
				    <label><i18n:i18n msg='password'/></label>
					<br \>
					<input id="mid-box1"  type="password" name="password" tabindex="2" />
					<input id="googlekey" type="hidden" name="google-key" />
				  </div>
				  <div class="rememberMe">
				    <input id="checkbox1" type="checkbox" tabindex="4" name="staySignIn" checked="checked"/> 
					<label><i18n:i18n msg='rememberMe'/></label>
				  </div>
				  <div class="loginbutton">
				     <input id="submitButton" type="submit" class="allviews-common-button" name="login" value="<i18n:i18n msg='login'/>" tabindex="6"/>
				  </div>
			   </form>
			   <div class="form-bottom-options">
			      <a href="/main/forgotpassword" id="forget-link1" tabindex="5"><i18n:i18n msg='lostyourpassword'/></a>
			   </div>
			    <div class="form-bottom-options">
			      <a href="/main/signup" id="signUp-link1" tabindex="6"><i18n:i18n msg='signupAccounter'/></a>
			   </div>
			   
<!-- 			   <form id="openIdForm" method="post" action="/main/openid"> -->
<%-- 			   <span><i18n:i18n msg='signInusing'/> : </span> --%>
<!-- 			   <a href ="/main/openid?openid_identifier=https://www.google.com/accounts/o8/id" class="google_icon" id="openIdLink" tabindex="5"></a> -->
<!-- 			   <a href ="/main/openid?openid_identifier=https://www.yahoo.com" class="yahoo_icon" tabindex="6"></a> -->
<!-- 			   <a href ="/main/openid?openid_identifier=https://openid.aol.com" class="aol_icon" tabindex="6"></a> -->
<!-- 			   <a href ="/main/fbauth"  class="facebook_icon" tabindex="6"></a> -->
<!-- 			   </form> -->
			 </div>
			</div>
			</div>
		
	     <!-- Footer Section-->
		
 
<!-- <div id="appVersions"> -->
<!-- 	    <div> -->
<!-- 	       <span>Access Anna Accounting from </span> -->
<!-- 	       <a target="_blank" href="https://play.google.com/store/apps/details?id=com.vimukti.accounter.android"> Android </a>  -->
<!-- 	       <a target="_blank" href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> Windows Phone </a>  -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> iPhone </a>  -->
<!-- 		   <a target="_blank" href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> Black Berry </a>  -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> iPad </a>  -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> Mac OS </a> -->
<!-- 		   </div> -->
<!-- 	</div> -->
	<div id="mainFooter">
	    <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span> |
	       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
	       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
	       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
	    </div>
	</div>
<%@ include file="./scripts.jsp" %>
<script>
  grecaptcha.ready(function() {
      grecaptcha.execute('6LdZG5IUAAAAANewD-r1A7kGFPQeDAHbyQgIWUy7', {action: 'login'}).then(function(token) {
        document.getElementById("googlekey").value = token;
      });
  });
  </script>
		</body>
</html>