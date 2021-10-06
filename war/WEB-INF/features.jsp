<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>Features | Anna Accounting
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="../css/vertical-tabs-styles.css?version=<%= version%>" />
<script type="text/javascript" src="/jscripts/vertical-tabs-script.js"></script>

<script type="text/javascript">
    // Load this script when page loads
    $(document).ready(function() {
    <%	boolean isFeaturRTL=(Boolean) request.getAttribute("isRTL");	%>
	document.body.style.direction=(<%= isFeaturRTL %>)?"rtl":"ltr";
    
	$('#blog-bg').click(function(event){
		event.preventDefault();
		$('.menu-bar').find('a').removeClass("header-hover");
		$(this).addClass("header-hover");
		window.open("http://blog.annaccounting.com/","_blank");
	});
	 // Set up a listener so that when anything with a class of 'tab' 
	 // is clicked, this function is run.
	 $('.tabContainer').find('a').click(function (event) {	
	   event.preventDefault();
	  // Remove the 'active' class from the active tab.
	  $('.left-side-options > .tabContainer > li').removeAttr('class');		  
	  // Add the 'active' class to the clicked tab.
	  $(this).parent().addClass('feature_active');
	  
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
					  <a href="/"><img src="/images/Accounter_logo_title.png" alt="Anna Accounting logo" /></a>
				   </div>
			   </div>
			   <div class="menu-bar-login">
			      <div class="menu-bar">
				<ul>
<!-- 					 <li> -->
<!-- 					    <a href="/main/login" id="home-bg" >Home</a> -->
<!-- 					 </li>  -->
					 <li>
					    <a href="/site/features" id="fea-bg" class="header-hover">Features</a>
					 </li> 
					 <li>
						<a href="/site/support" id="sup-bg">Support</a>
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
			 <div class="features_container">
				<div class="left-side-table" id="mid-left">
					<div class="left-side-options">
					  <ul  class="tabContainer">
				<li>
	    <!-- The jQuery generated tabs go here -->

		</li></ul>			
					
					
				</div>
				</div>
				
				<div class="seperator"></div>
				<div class="Anna Accounting-Business-Dashboard-table" id="mid-right">
				<div id="tabContent">
					
                     <div id="contentHolder">
        <!-- The AJAX fetched content goes here -->
              
                   </div>
					
					
					</div>
				</div>
				<div class="shadow-line"></div>
				</div>
			</div>
				<div class="down-test" id="down"></div>
			<%@ include file="./footer.jsp" %>
		</div>
		 
		</div>
		<%@ include file="./scripts.jsp" %>
</body>
</html>