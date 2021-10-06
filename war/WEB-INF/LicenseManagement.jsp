<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@ include file="./scripts.jsp" %>
<html>
  <head>
	  <title><i18n:i18n msg='licenseManagement'/> | Anna Accounting
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
		<link type="text/css" href="../css/setup.css?version=<%= version%>" rel="stylesheet" />
  </head>
  
  <script type="text/javascript">
  	var gen = <%= request.getAttribute("gen") %>
   $(document).ready(function() {
  		if(gen){
  			$('#generate').css('visibility', 'visible');
  			$('#licensesList').css('visibility', 'hidden');
  		}else{
  			showList();
  		}
  	});
  	function showList(){
  		$('#generate').css('visibility', 'hidden');
  		$('#licensesList').css('visibility', 'visible');
  	};
  	
  	function select_all(textarea){
  		textarea.select();
  	}
  </script>
  <body>
  <div class="licenseBody">
  	<div class="licenseLogout">
  		<a href="/main/logout">Logout</a>
  	</div>
  	<div class="heading">
  		<h2>Welcome</h2>
  		    <p>Hey ${fullName}, welcome to the Anna Accounting license management portal. You can manage your licensed below.</p>
  	</div>
  	
  	<div id="generate" class="generateLicense">
  		<form action="/main/managelicense" method="post">
	  	<div>
		  	<h4>Server ID</h4>
		  	<input name="serverID" type="text"></input>
	  	</div>
	  	<div>
	  		<h4>Organization Name</h4>
	  		<input name="orgName" type="text"></input>
	  	</div>
	  	<c:if test="${purchaseType!=null}">
	  	<div>
	  		<h4>License Type</h4>
	  		<select name="licenseType">
				<option value="-1">Free License</option>
				<c:choose>
					<c:when test="${purchaseType==1}">
						<option value="${purchaseType}">One user yearly</option>
					</c:when>
					<c:when test="${purchaseType==2}">
						<option value="${purchaseType}">2 users yearly</option>
					</c:when>
					<c:when test="${purchaseType==3}">
						<option value="${purchaseType}">5 users yearly</option>
					</c:when>
					<c:when test="${purchaseType==4}">
						<option value="${purchaseType}">Unlimited users yearly</option>
					</c:when>
		   		</c:choose>
			</select>
	  	</div>
	  	</c:if>
	  	<input type="submit" value="Generate License" />
  		<input type="button" onClick="showList()" value="Cancel" />
  		</form>
  	</div>
  	
  	<div id="licensesList" class="licenseList">
  	<div>
  	<h2>Evaluations</h2>
  	<a href="/main/managelicense?gen=true">New Evalution License</a>
  	<a href="/main/purchaseLicense">Purchase License</a>
  	</div>
  	<table class="licenses">
  		<tr>
  			<td>Server Id</td>
  			<td>Organisation</td>
  			<td>Expires On</td>
  			<td>License</td>
  		</tr>
  		<c:if test="${licenses != null}">
		   <c:forEach var="license" items="${licenses}">
		   	<tr>
		   	<td>
		   		<div>${license.serverId}</div>
			</td>
			<td>
		   		<div>${license.organisation}</div>
			</td>
			<td>
		   		<div><fmt:formatDate value="${license.expiresOn}" pattern="dd, MMM yyyy HH:mm"/></div>
			</td>
			<td>
		   		<div>
		   			<textarea onClick="select_all(this);" class="licenseText" rows='7' cols='85'>${license.licenseText}</textarea>
		   		</div>
			</td>
			</tr>
		   </c:forEach> 
	    </c:if>
  	</table>
  	</div>
  	
  	</div>
  <body>
  
</html>