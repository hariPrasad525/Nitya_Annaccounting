<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
<head>

    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta content="IE=100" http-equiv="X-UA-Compatible">
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet">
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<script type="text/javascript">
$(document).ready(function() {
	<%	boolean isCreatRTL=(Boolean) request.getAttribute("isRTL");	%>
	document.body.style.direction=(<%= isCreatRTL %>)?"rtl":"ltr";

	$('#submitButton').click(function() {
		$("#activationForm").validate({
			rules: {
			name:{ 
			required :true,
			minlength: 6
			}},
			messages: {
				name :{
				required: "Please enter your Company name" ,
				minlength: "Company name must be at least 6 characters long"
					}
				
			}
		});
	});
	$('#SelectcompanyType').keydown(function(e) {
		  if (e.keyCode == '9' || e.which == '9') {
		     e.preventDefault();
		    $('#submitButton').focus();
		   }
		});
});
</script>
<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>Create Company - Anna Accounting</title>

<script type="text/javascript">
    String.prototype.trim = function() { return this.replace(/^\s*/, "").replace(/\s*$/, ""); };
    	function validation(formObject){
    		var flag=true;
    	
            var Name=document.getElementById("name").value;

    		var search=Name.search(/[A-Z]/);
    		if(Name==null||Name==""){
    			document.getElementById("nameLabel").innerHTML="Enter valid Company Name";
    			flag=false;
    		}
    		var space=Name.trim().split(" ").length;
    		
    		if(space>1){
    			document.getElementById("nameLabel").innerHTML="Company Name must not contain space ";
    			flag=false;
    		}
    		if(search+1>0)
    		{
    		    document.getElementById("nameLabel").innerHTML="Capital letters are not allowed in Company Name";
    			flag=false;
    		}
    		var UsersNos=document.getElementById("nooofusers").value;
    		if(UsersNos==null||UsersNos==""||!UsersNos.match("[0-9]+")){
    			document.getElementById("noOfUserLabel").innerHTML="Enter valid Company Users";
    			flag=false;
    		}
    		var email=document.getElementById("emailid").value;
    		if(email==null||email==""||email.indexOf("@")<1||email.indexOf(".")<1){
    			document.getElementById("emailLabel").innerHTML="Please enter valid email";
    			flag=false;
    		}
    		var pwd=document.getElementById("pwd").value;
    		var cnfrmpwd=document.getElementById("con_pwd").value;
    		if(pwd==null||cnfrmpwd==null||pwd==""||cnfrmpwd==""||pwd!=cnfrmpwd||cnfrmpwd.length<6){
    			document.getElementById("passwordLabel").innerHTML="Enter valid password and same in confirm password with at least 6 character";
    			flag=false;
    		}
    		var companypwd=document.getElementById("companypwd").value;
    		if(companypwd==null || companypwd.length<6){
    			document.getElementById("companyPasswordLabel").innerHTML="Enter valid password ";
    			flag=false;
    		}
    		
    		var user=document.getElementById("userName").value;
    		if(user==null||user==""){
    		     document.getElementById("userNameLabel").innerHTML="Enter User Name";
    		     flag=false;
    		}
    		if(flag){
    			loadingImage();
    			formObject.submit();
    			
    		}
    		return false;
    	}
    	function loadingImage(){
    		document.getElementById("hiddenDiv").style.display="block";
    		document.getElementById("formDiv").style.display="none";
    	}

    	function resetDates(monthComboBoxName,daysComboBoxName){
        	
    		var monthCombobox = document.getElementById(monthComboBoxName);
    		var month = monthCombobox.value;
    		
    		var noOfdays;
    		var date = new Date();
    		var presentYear = date.getFullYear();
    		if(month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11){
    			noOfdays = 31;
    		}else
    		if(month == 3 || month == 5 || month == 8 || month == 10 ){
    			noOfdays = 30;
    		}else
    		if(month == 1 && (presentYear % 4) == 0){
    			noOfdays = 29;
    		}else{
    			noOfdays = 28;
    		}
    		
    		var daysComboBox = document.getElementById(daysComboBoxName);
    		var i;
    		
    		for(i=daysComboBox.options.length-1;i>=0;i--)
    		{
    			daysComboBox.remove(i);
    		}
    		
    		
    		for(i=1;i<=noOfdays;i++){
    			var optn = document.createElement("OPTION");
    			optn.text = i;
    			optn.value = i;
    			daysComboBox.options.add(optn);
    		}
    		
    	}

    	function companyPassword(){
   
    		var companypsw = document.getElementById("companypwd").value;
    	
    		if (window.XMLHttpRequest)
    		  {// code for IE7+, Firefox, Chrome, Opera, Safari
    		  xmlhttp=new XMLHttpRequest();
    		  }
    		else
    		  {// code for IE6, IE5
    		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    		  }
    	
    		xmlhttp.open("GET","companyAuthentication?companypsw="+companypsw,false);
    		
    		xmlhttp.send();
    		xmlDoc=xmlhttp.responseXML; 
    		var message = xmlDoc.getElementsByTagName("message")[0].childNodes[0].nodeValue;
    		document.getElementById("companyPasswordLabel").innerHTML = "<span id=red>"+message+" </span> ";       		
   	}
           		
    </script>
<!--CSS for loading message at application Startup-->
<link rel="shortcut icon" href="/images/favicon.ico" />
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<style type="text/css">
body {
	overflow: hidden
}

#loading {
	border: 1px solid #ccc;
	position: absolute;
	left: 45%;
	top: 40%;
	padding: 2px;
	z-index: 20001;
	height: auto;
}

#loading a {
	color: #225588;
}

#loading .loadingIndicator {
	background: white;
	font: bold 13px tahoma, arial, helvetica;
	padding: 10px;
	margin: 0;
	height: auto;
	color: #444;
}

#loadingMsg {
	font: normal 10px arial, tahoma, sans-serif;
}

html body {
	font-family: Verdana, sans-serif;
	font-size: 13px;
}

html	table,tr,td {
	font-family: Verdana, sans-serif;
	font-size: 13px;
	border-spacing: 0pt;
}

body {
	/*font-size: 13px;
	background: LightSteelBlue none repeat scroll 0 0;*/
}

.login {
	margin: 140px auto;
	background-color: #f2f3f5;
	padding: 10px;
	border: 1px solid #000;
	width: 45%;
}

.image {
	text-align: center;
}

.clearfix:after {
	content: ".";
	display: block;
	height: 0;
	clear: both;
	visibility: hidden;
}

fieldset {
	border: 1px solid #000;
	padding: 10px;
	background-color: #e9edf0;
	margin: 5px 0px;
	width: 300px;
}

fieldset table td {
	float: left;
}

.login img[src="logo.png"] {
	text-align: center;
}

.login img[src="lock.png"] {
	float: left;
}

p {
	text-align: left;
	clear: left;
	line-height: 30px;
	margin: 0px;
}

.loginbox {
	float: right;
}

.loginlabel {
	font-weight: bold;
	color: #c64933;
}

.loginbox a {
	font-weight: bold;
	color: #0000ff;
	margin-left: 20px;
	text-decoration: none;
}

/*#formDiv input[type="text"],#formDiv input[type="password"] {
	border: 1px solid #9cb0ce;
}*/

.loginbox a:hover {
	text-decoration: underline;
}

#admin_info {
	font-size: large;
	padding-left: 10px;
}

#red ,.red{
	color: red;
}

.hiddenPic {
	display: none;
	float: left;
	margin-top: 25%;
	margin-left: 50%;
}

#middlePos {
	float: left;
	margin-top: 20%;
	margin-left: 40%;
}
</style>
</head>
<body>
<div id="commanContainer">	
<img src="/images/Accounter_logo_title.png" class="accounterLogo" />	
<div id="hiddenDiv" class="hiddenPic">
  <img src="/images/icons/loading-indicator.gif" alt="Loading" title="Loading" height="50" width="50">
</div>
<c:if test="${errormessage != null}">
<div id="login_error" class="common-box">
  <b>${errormessage} </b> 
</div> 
</c:if>	

<c:if test="${message==null}">
 <div id="formDiv">
	<form class="accounterform" id = "activationForm" method="post" action="/main/createcompany"
		onsubmit="return validation(this)">
		 <h2 class="company-heading"> Create Company</h2>
	     <div>
		    <label>Name</label>
			<input type="text" name="name" id="name"
				onClick="document.getElementById('nameLabel').innerHTML='';" /></td>
		 </div>
		  <div class="createbutton">
		  <input type="submit" tabindex="6" value="Create" name="create" class="allviews-common-button" style="width:60px" id="submitButton" />
	     </div>
	</form>
	<div class="form-bottom-options">
	<a href="/main/companies" id="forget-link1" tabindex="4"> Companies List </a>
	</div>
	<div class="form-bottom-options">
	<a href="/main/logout" id="forget-link1" tabindex="4"> Logout </a>
	</div>
	</div>
</c:if>
</div>

<!-- Footer Section-->


<!-- <div id="appVersions" > -->
<!-- 	    <div> -->
<!-- 	       <span>Access Anna Accounting from </span> -->
<!-- 			<ul> -->
<!-- 	      <li> <a target="_blank" href="https://market.android.com/details?id=com.vimukti.accounter"> Android </a> </li> -->
<!-- 	      <li><a target="_blank" href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> Windows Phone </a> </li> -->
<!-- 		  <li> <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> iPhone </a></li> -->
<!-- 		  <li> <a target="_blank" href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> Black Berry </a></li> -->
<!-- 		  <li> <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> iPad </a> </li> -->
<!-- 		 <li>  <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> Mac OS </a></li> -->
<!-- 		 </ul> -->
<!-- 		   </div> -->
<!-- 	</div> -->
	<div id="mainFooter"   >
<div>
   <span>&copy 2019 NITYA SOFTWARE SOLUTIONS, INC.</span> |
   <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
   <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
   <a target="_blank" href="/site/support"> Support </a>
</div>
</div>
<%@ include file="./scripts.jsp" %>
 
</body>
</html>
