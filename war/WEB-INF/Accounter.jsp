<%@ page import="java.util.*"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<%@ include file="./init.jsp"%>
<script type="text/javascript" charset="utf-8">
  		var is_ssl = ("https:" == document.location.protocol);
  		var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
  		
  		var AccounterMessages={
  		<%HashMap<String,String> messages=(HashMap<String,String>)request.getAttribute("messages");
  		String user_emailId=(String)request.getSession().getAttribute("emailId");
  		if(messages!=null){
	  		for(String key:messages.keySet()){
	  			String value=messages.get(key);%>'<%=key%>': '<%=value%>',
	  				<%}
  		}%>
  			last: 'end'
  		};
  		var features=[];
  		<%Set<String> features=(Set<String>)request.getAttribute("features");
  		if(features!=null){
  			for(String f:features){%>features.push('<%=f%>');<%}
  		}%>
  		var accounter_locale={
  		<%HashMap<String,String> accounterLocale=(HashMap<String,String>)request.getAttribute("accounterLocale");
  		if(accounterLocale!=null){
	  		for(String key:accounterLocale.keySet()){
	  			String value=accounterLocale.get(key);
	  			if(value.startsWith("['")){%>'<%=key%>': <%=value%>,
	  				<%}else{%>'<%=key%>': '<%=value%>',
	  				<%}
  			}
  		}%>
  			last: 'end'
  		};
		<%boolean isRTL=(Boolean) request.getAttribute("isRTL");%>
		
		var isRTL=<%=isRTL%>;
		
		<%boolean isPaid=(Boolean) request.getAttribute("isPaid");
			boolean freeTrial=(Boolean) request.getAttribute("freeTrial");%>
  		var freeTrial = <%=freeTrial%>;
  		var isPaid= <%=isPaid%>;
	</script>


<!--<script type="text/javascript" src="https://getfirebug.com/firebug-lite.js"></script>-->

<script type="text/javascript">
    var tabsEnabled=["Hr","Finance","Operations","Marketing","Sales","Users","Workflows","Purchases"];
    var helpurl="${helpUrl}";
    
    <%-- $(document).ready(function() {
		var isPaid=${isPaid};
		var freeTrial=${freeTrial};
		var user_emailId='<%=user_emailId%>';
		if(isPaid){
       $('#support').after(' | <a style="padding-left:25px" href="/main/subscriptionmanagement"><i18n:i18n msg='subscriptionManagement'/></a>');
       }
       else{
	       	if(freeTrial){
	       $('#support').after(' | <a target="_blank" href="/main/gopremium?emailId='+user_emailId+'">Go Premium (30 days free trail)</a>');
	       }
	       else{
	       	
	        $('#support').after(' | <a target="_blank" href="/main/gopremium?emailId='+user_emailId+'">Go Premium</a>');
	       }
       }
       
       }); --%>
    </script>
<!--                                                               -->
<!-- Consider inlining CSS to reduce the number of requested files -->
<link type="text/css" rel="stylesheet"
	href="../css/calendar.css?version=<%=version%>" />
<%
	String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
    Boolean isIpad =  (Boolean)request.getSession().getAttribute("IpadApp");
    
   if( isNative){
%>
<link type="text/css" rel="stylesheet"
	href="../css/native.css?version=<%=version%>" />
<%
	}
%>
<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>Anna Accounting</title>

<!--CSS for loading message at application Startup-->
<style type="text/css">
#loading {
	position: absolute;
	left: 42%;
	top: 35%;
	padding: 2px;
	z-index: 20001;
	height: auto;
	border: 1px solid #CCCCCC;
	box-shadow: 0 0 10px #177ea9;
	background: none repeat scroll 0 0 white;
	border-radius: 10px 10px 10px 10px;
}

#loading a {
	color: #225588;
}

#loading .loadingIndicator div {
	color: #177ea9;
	font-size: 20px;
	font-weight: normal;
	padding-left: 10px;
}

#loading .loadingIndicator {
	color: #444444;
	font-family: tahoma, arial, helvetica;
	font-weight: bold;
	height: auto;
	line-height: normal;
	margin: 0;
	padding: 10px;
}

#loadingMsg {
	font: normal 10px arial, tahoma, sans-serif;
}

.login {
	margin: 140px auto;
	background-color: #f2f3f5;
	padding: 10px;
	border: 1px solid #000;
	width: 600px;
}

.clearfix:after {
	content: ".";
	display: block;
	height: 0;
	clear: both;
	visibility: hidden;
}

.fieldset {
	border: 1px solid #000;
	padding: 10px;
	background-color: #e9edf0;
	margin: 5px 0px;
	width: 300px;
}

.fieldset table td {
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
	line-height: 1.5;
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

.loginbox a:hover {
	text-decoration: underline;
}

.hiddenPic {
	display: none;
	float: left;
	margin-top: 25%;
	margin-left: 50%;
}

#import_anchor {
	color: blue;
	font-weight: normal;
	margin-bottom: 0;
	margin-left: 0;
	margin-right: 0;
	margin-top: 0;
	text-decoration: underline;
}

#Import_Account {
	margin-left: 200px;
}

#mainFooter {
	float: left;
	clear: both;
	height: 25px;
	margin-top: 0;
	position: relative;
	width: 100%;
	color: #666666;
	padding-top: 8px;
}

#mainFooter a,#appVersions span {
	text-decoration: none;
	color: #666666;
	font-size: 13px;
	padding: 0 5px;
}

#mainFooter a:HOVER {
	text-decoration: underline;
}

#mainFooter,#mainFooter span {
	text-align: center;
}
</style>
</head>

<!--                                           -->
<!-- The body can have arbitrary html, or      -->
<!-- you can leave the body empty if you want  -->
<!-- to create a completely dynamic UI.        -->
<!--                                           -->
<body>
	<div id="appBar" data-win-control="WinJS.UI.AppBar" data-win-options="">
	</div>
	<div id="hiddenDiv" class="hiddenPic">
		<img src="/images/loader.gif" alt="Loading" title="Loading" />
	</div>

	<!--add loading indicator while the app is being loaded-->
	<div id="loadingWrapper" style="visibility: visible">
		<div id="loading">
			<div class="loadingIndicator">
				<div class="loadingIndicator">
					<%
						if(isTouch != null && isTouch.equals("true")){
					%>
					<img src="/images/image_loading.gif" alt="Main page"> <%
 	} else{
 %>
						<link type="text/css" rel="stylesheet"
							href="/css/web.css?version=<%=version%>" />
						<div>
							<i18n:i18n msg='loadingAccounter' />
						</div> <img src="/images/Main-page-loading.gif" alt="Main page"> <%
 	}
 %>
							<div>
								<i18n:i18n msg='pleasewait' />
								....
							</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">document.getElementById('loadingMsg');</script>
	<!--include the application JS-->
	<script type="text/javascript">document.getElementById('loadingMsg');</script>

	<!--<table class="header" id="mainHeader" style="visibility:hidden">
	   <tr>
	      <td width="25%"><img src="/images/Accounter_logo_title.png" alt="Anna Accounting logo"/></td>
	      <td width="50%"><div class="companyName">${companyName}</div></td>
	      <td width="25%">
	        <ul>
	           <li><img src="/images/User.png" /><a href="">${userName}</a></li>
	           <li><img src="/images/Help.png" /><a href='http://help.accounter.com'>Help</a></li>
	           <li><img src="/images/logout.png" /><a href='/main/logout'>Logout</a></li>
	        </ul>
	      </td>
	   </tr>
	</table>-->
	<div id="mainWindow"></div>
	<script type="text/javascript" charset="utf-8">
			function MacReload(){
				 if(window.macclient){
				 	window.macclient.reloadMenu();
				 }
			}
  		</script>
	<script type="text/javascript" charset="utf-8">
			function ClearMacMenu(){
				 if(window.macclient){
				 	window.macclient.clearMenu();
				 }
			}
  		</script>
  		
	<script type="text/javascript" language="javascript"
		src="/react/dist/lib/react-gwt-anna-accounting.js"></script>
	<script type="text/javascript" language="javascript"
		src="/accounter.client/accounter.client.nocache.js"></script>

<!-- 	<div id="appVersions" style="visibility: hidden"> -->
<!-- 		<div> -->
<!-- 			<span>Access Anna Accounting from </span> -->
<!-- 			<ul> -->
<!-- 				<li><a target="_blank" -->
<!-- 					href="https://play.google.com/store/apps/details?id=com.vimukti.accounter.android"> -->
<!-- 						Android </a></li> -->
<!-- 				<li><a target="_blank" -->
<!-- 					href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> -->
<!-- 						Windows Phone </a></li> -->
<!-- 				<li><a target="_blank" -->
<!-- 					href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> -->
<!-- 						iPhone </a></li> -->
<!-- 				<li><a target="_blank" -->
<!-- 					href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> -->
<!-- 						Black Berry </a></li> -->
<!-- 				<li><a target="_blank" -->
<!-- 					href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> -->
<!-- 						iPad </a></li> -->
<!-- 				<li><a target="_blank" -->
<!-- 					href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> -->
<!-- 						Mac OS </a></li> -->
<!-- 			</ul> -->
<!-- 		</div> -->
<!-- 	</div> -->
	<div id="mainFooter" style="visibility: hidden">
		<div>
			<span>&copy 2019 NITYA SOFTWARE SOLUTIONS, INC.</span> | <a
				target="_blank" href="/site/termsandconditions"> Terms &
				Conditions </a> | <a target="_blank" href="/site/privacypolicy">
				Privacy Policy </a> | <a id="support" target="_blank"
				href="/site/support"> Support </a>
		</div>
		<script type="text/javascript">
	    <%Long subscription=(Long)request.getAttribute("subscription");
			String goPId=(String)request.getAttribute("goPremiumId");%>
			var subscription=<%=subscription%>;
			var goPId="<%=goPId%>";
	    </script>
	</div>
	<!-- OPTIONAL: include this if you want history support -->
	<iframe src="javascript:''" id="__gwt_historyFrame"
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>
	<iframe id="__printingFrame" style="width: 0; height: 0; border: 0">
	</iframe>
	<iframe id="__generatePdfFrame" style="width: 0; height: 0; border: 0">

	</iframe>


	<%@ include file="./scripts.jsp"%>
</body>

<footer> 
	<%@ include file="./wegengage.jsp"%>
</footer>
</html>
