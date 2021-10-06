<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
 <c:if test="${message != null}">
			   <div id="login_error" class="common-box">
					<span>${message} </span>
			   </div>
			   </c:if>
<head>
</head><body>
	<%
	Boolean isValid =(Boolean)request.getAttribute("isValid");
	if (!isValid){%>
	<form id="passwordForm" method="post" action="/translate/upload">
 	<br>
		<label><i18n:i18n msg='password'/></label>
 	<input type="password"  name ="password1"  />
 		<div class="loginbutton">
			<input id="logInBtn" type="submit" name="logInBtn" value="submit" tabindex="6"/>
		</div>
 	</br>
 	</form><%
 	}else{%>
 	<form id="uploadForm" method="post" action="/translate/upload?isSubmittingFile=true" enctype="multipart/form-data">
 	<br>
 		<label>Choose a file to upload</label>
 		</br>
		<input type="file" name="datafile" value="dir" size="40">
		<div class="loginbutton">
			<input id="uploadBtn" type="submit" name="uploadBtn" value="submit" tabindex="6"/>
		</div>
 	</form><% }
	%>
 	
	</body>
</html>