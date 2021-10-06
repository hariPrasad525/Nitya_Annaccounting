<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>  Anna Accounting Under Maintanance
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<link rel="shortcut icon" href="/images/favicon.ico" />

<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
</head>
<body>
  <div id="maintananceContainer">
   <div class="maintanance_subcont">
    <img src="/images/Accounter_logo_title.png" class="accounterLogo" />
    <div class="unavailable_page">
      <div class="maintainence_logo">
        <img src="/images/maintainence_show.jpg" class="accounterLogo" /> 
      </div>
      <div class="maintainence_message">
	    <h3>Sorry! Your Company is Under Migration</h3>
	    <h5>We are performing migration on your company to provide you better service of Anna Accounting.</h5>
	    <span> Please visit again later.For more info, visit </span>
	    <a  href="www.blog.annaccounting.com" style="color:RoyalBlue">blog.annaccounting.com</a>
	    
	</div>
   </div>
   </div>
  </div>
  <%@ include file="./scripts.jsp" %>
</body>
</html>