<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><i18n:i18n msg='serverMaintanace'/>| Anna Accounting
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="/css/ss.css?version=<%= version%>" rel="stylesheet" />
</head>
<body>
    <div id="commanContainer">
		<img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="Anna Accounting logo" />
		<c:if test="${message != null}">
		   <div class="common-box create-company-message">${message}</div>
		</c:if>
		<form class="accounterform" id = "maintananceForm" method="post" action="/main/maintanance">
			<div class="text_box_margin">
				<label><i18n:i18n msg='pleaseenteradminpassword'/> :</label>
				<input type = "password"  id = "adminPassword"  name ="password" />
			</div>
			<c:choose>
			    <c:when test='${CheckedValue == "true"}'>
			       <input type="checkbox" name="option1" value="CheckedValue" checked="" /><label>Server under maintainace</label> <br>
			    </c:when>
			    <c:otherwise>
			       <input type="checkbox" name="option1" /><label><i18n:i18n msg='serverundermaintainace'/></label> <br />
			    </c:otherwise>
		    </c:choose>
			<div class="OkButton">
			    <input type = "submit" class="allviews-common-button" value = "<i18n:i18n msg='submit'/>" id = "submitButton"  />
			</div>
		</form>
	</div>
	
	<%@ include file="./scripts.jsp" %>
</body>
</html>