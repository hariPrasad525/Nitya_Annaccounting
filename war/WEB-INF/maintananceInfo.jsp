<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> <i18n:i18n msg='underMaintanance'/>| Anna Accounting
</title>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
<link type="text/css" href="/css/ss.css?version=<%= version%>" rel="stylesheet" />
<script  type="text/javascript" >
$(document).ready(function() {
	<%	boolean isMaintanRTL=(Boolean) request.getAttribute("isRTL");	%>
	document.body.style.direction=(<%= isMaintanRTL %>)?"rtl":"ltr";
	$('#notifyMe').click(function(){
		$('#successmsgDiv').addClass("display-none");
		$('#enterEmailDiv').removeClass('display-none');
	})
	$('#submitButton').click(function(){
	
		var email =$('#emailField').val();
		if(	IsValidEmail(email)){
			$.post('/main/maintanaceinform', {email: email},
				function(data) {
				if(data !=null){
					 if(data=="fail"){
						 $('#emailField').val("");
						 $('#successmsgDiv').addClass("display-none");
						 $('#errorMsg').removeClass("display-none");
					}else{
						$('#emailField').val("");
						 $('#errorMsg').addClass("display-none");
						$('#successmsgDiv').removeClass("display-none");
						$('#enterEmailDiv').addClass('display-none');
					 }
				}
					
				}
			)
		}
		else{
			$('#emailField').val("");
			 $('#errorMsg').removeClass("display-none");
		}
	});
	function IsValidEmail(email)
	 	{
	 	var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	 	return filter.test(email);
	 	}
});
</script>
</head>
<body>
  <div id="maintananceContainer">
   <div class="maintanance_subcont">
    <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="Anna Accounting logo" />
    <div class="unavailable_page">
      <div class="maintainence_logo">
        <img src="/images/maintainence_show.jpg" class="accounterLogo" alt= "Maintanance"/> 
      </div>
      <div class="maintainence_message">
	    <h3> <i18n:i18n msg='sorryAccounterisdownformaintenance'/> </h3>
	    <h5> <i18n:i18n msg='weareperformingmaintenance'/> </h5>
	    <span> <i18n:i18n msg='pleasevisitagainlaterFormoreinfovisit'/> </span>
	    <a target="_blank"  href="http://blog.annaccounting.com" style="color:RoyalBlue"> <i18n:i18n msg='blogaccounterlivecom'/> </a>
	    <br/>
	    <a id="notifyMe" style="color:RoyalBlue"> <i18n:i18n msg='notifywhenworking'/> </a>
	    <div id="enterEmailDiv" class= "display-none">
	    <form id="submitForm">
	    <span><i18n:i18n msg='pleaseenteryouremailid'/></span>
	    <input id="emailField"type="text" />
	    <input id= "submitButton" type="button" class="allviews-common-button" value ="<i18n:i18n msg='submit'/>"/></form>
	    </div>
	    <div class ="display-none" id="successmsgDiv"> <i18n:i18n msg='successfullyregisteredyouremail'/> </div>
	    <div class ="display-none" id="errorMsg"> <i18n:i18n msg='pleaseEnterValidEmailId'/> </div>
	</div>
   </div>
   </div>
  </div>
  <%@ include file="./scripts.jsp" %>
</body>
</html>