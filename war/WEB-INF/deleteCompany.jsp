<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
  <head>
  <title><i18n:i18n msg='deletecompany'/>| Anna Accounting
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
	<script type="text/javascript">
	window.onload=function(){
	<%	boolean isDelComRTL=(Boolean) request.getAttribute("isRTL");	
		boolean isOpenIdUser=(Boolean) request.getAttribute("isOpenIdUser");
	%>
	document.body.style.direction=(<%= isDelComRTL %>)?"rtl":"ltr";
	}
	</script>
  </head>
  <body>
  <div id="commanContainer">
	<div>
		<img src="/images/Accounter_logo_title.png" class="accounterLogo" />
	</div>
    <div >
       
       <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
        
	   <form class="accounterform" action="/main/deletecompany" method="post">
	   <c:if test="${!isOpenIdUser}">
	   <label><i18n:i18n msg='pleaseEntertheUserPassword'/></label>
	   <input id="userPassword"  type="password" name="userPassword"   value="" />
	    </c:if>
	  	 	<c:if test="${canDeleteFromSingle}">
            	<input type="radio" name="delete" value="deleteUser">
            		<i18n:i18n msg='deletecompanyfromaccount'/>
				<p class="delete_message"><i18n:i18n msg='deletecompanyWarningMsg'/></p>
			</c:if>
			<c:if test="${canDeleteFromAll}">
            <br>
            <input type="radio" name="delete" value="deleteAllUsers">
                <i18n:i18n msg='deletecompanyfromallusers'/>
            <p class="delete_message"><i18n:i18n msg='deleteMsg'/></p>
			</c:if>
			<br>
            <br>
            <div class="company_list_buttons">
	            <input type="submit" value="<i18n:i18n msg='delete'/>" class="allviews-common-button">
	            <input type="button" value="<i18n:i18n msg='cancel'/>" class="allviews-common-button" onclick="parent.location='/main/deletecompany?isCancel=true'">
            </div>
        </form>
	    
    </div>
   </div>
    <!-- Footer Section-->
  
<!-- <div id="appVersions"> -->
<!-- 	    <div> -->
<!-- 	       <span>Access Anna Accounting from </span> -->
<!-- 	       <a target="_blank" href="https://market.android.com/details?id=com.vimukti.accounter"> Android </a> | -->
<!-- 	       <a target="_blank" href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> Windows Phone </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> iPhone </a> | -->
<!-- 		   <a target="_blank" href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> Black Berry </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> iPad </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> Mac OS </a> -->
<!-- 		   </div> -->
<!-- 	</div> -->
	 
   <div id="mainFooter"  >
   <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span> |
	       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
	       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
	       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
   </div>
</div>
<%@ include file="./scripts.jsp" %>
  </body>
</html>