<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
<head>
<title> <i18n:i18n msg='cancelMyAccount'/>
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

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
</head>
<body class="cancel_accountform">
  <div id="commanContainer" >
   <div class="maintanance_subcont">
    <img src="/images/Accounter_logo_title.png" class="accounterLogo" />
    <form class="accounterform" id="cancelform" name="cancelform" method="post" action="/main/cancelform" >
      <h3 style="color:white;text-align:center;margin:10px 0"><i18n:i18n msg='cancelAccount'/></h3>
     <div class="cancel_account" > <h4><i18n:i18n msg='deletepermanentlyMsg'/></h4>
		<div>
			<ul>
				<li>
	    		<c:if test="${companeyList != null}">
		   			<c:forEach var="company" items="${companeyList}">
		   				<c:set var='ids' value="companeyList[${company}].id"/>
			   			<c:set var='url' value="/main/companies?companyId=${company.id}"/>
			   				<div class="companies-list"><a href= '${url}'>${company.preferences.tradingName}</a></div>
		   			</c:forEach>
	    		</c:if>
	    		</li>
	   		</ul>
		</div>	
	  	 <h4><i18n:i18n msg='deletedImmediatelyMsg'/></h4></div>
	    <div>
	   	    <div style="float:left">
	      		<input type="button" class="allviews-common-button" value="<i18n:i18n msg='keepUsingAccounter'/>" onclick="parent.location='/main/companies'"/>
	   		</div>
	   		<div style="float:right">
	      		<input type="submit" class="allviews-common-button" value="<i18n:i18n msg='cancelMyAccount'/>" />
	   		</div>
	   	</div>
	  </form>
   </div>
  </div>
  <%@ include file="./scripts.jsp" %>
</body>
</html>