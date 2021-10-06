<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
        <meta http-equiv="refresh" content="4"> 
		<link rel="shortcut icon" href="/images/favicon.ico" />
		
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
  </head>
    <body>
	    <div id="commanContainer">
		 <img src="/images/Accounter_logo_title.png" class="accounterLogo" />
		  <c:if test="${successmessage!=null}">
			<div id="login_success" class="common-box">
				<span>${successmessage}</span>
			</div>
  		  </c:if>
     	</div>
     	
     	 
     	<%@ include file="./scripts.jsp" %>
	</body>
</html>