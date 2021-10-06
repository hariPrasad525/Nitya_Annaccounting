<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<title>Anna Accounting | Subscription </title>
<link type="text/css" href="../../css/ss.css" rel="stylesheet" />
<%
String info =(String) request.getAttribute("info");
String paypalButtonId =(String) request.getAttribute("paypalButtonId");
Boolean isSandBoxPaypal = (Boolean) request.getAttribute("isSandBoxPaypal");
Boolean freeTrail = (Boolean) request.getAttribute("freeTrail");
String emailId = (String) request.getAttribute("emailId");
 if(emailId ==null){
	 emailId="";
 }
if(isSandBoxPaypal ==null){
	isSandBoxPaypal= true;
}
%>
 
</head>
<body>
<table id="commanContainer">
<tr>
<td>
 <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
<c:if test="${info!=null}"> 
<div id="login_success" class="common-box">
	<span>${info}</span>
</div>
</c:if>
<div class="form-box">


<% if(isSandBoxPaypal){ %> 
<form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="hosted_button_id" value="<%= paypalButtonId%>">
<%}else{%>
<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="hosted_button_id" value="<%= paypalButtonId%>">
<%}%>
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="custom"  value = "<%= emailId %>"   >
<table id="premium-table">
	<tr>
		<td align="left">
			<input type="hidden" name="header" value="Premium Subscription">Premium Subscription
		</td>
	</tr>
	<c:choose>
	<c:when test="<%= freeTrail %>">
		<tr id="trail-button-row">
			<td align="center">
			<a class="trailbutton target="_blank" href="/main/freetrail?emailId=<%= emailId %>"> 30 days Premium Trial</a>	
			</td>
			<td align="center">
			<label for="nocreditcardRwuired">No Credit Card Required</label>
			</td>
		</tr>
		<tr align="center" class="or">
			<td>(or)</td>
		</tr>
	</c:when>
	</c:choose>
<tr id="premium-option-row"><td><input type="hidden" name="on0" value="Subscription options">Subscription options</td></tr><tr id="combo-row"><td><select name="os0">
			<option value="One user monthly">One user monthly : $10.00 USD - monthly</option>
		    <option value="One user yearly"  selected="true">One user yearly : $90.00 USD - yearly</option>
		    <option value="2 users monthly">2 users monthly : $20.00 USD - monthly</option>
		    <option value="2 users yearly">2 users yearly : $180.00 USD - yearly</option>
		    <option value="5 users monthly">5 users monthly : $40.00 USD - monthly</option>
		    <option value="5 users yearly">5 users yearly : $360.00 USD - yearly</option>
		    <option value="Unlimited Users monthly">Unlimited Users monthly : $100.00 USD - monthly</option>
		    <option value="Unlimited Users yearly">Unlimited Users yearly : $1,000.00 USD - yearly</option>
</select> </td></tr>
<tr>
<td align="center">
<input type="hidden" name="currency_code" value="USD">
<% if(isSandBoxPaypal){ %> 
<input type="image" src="https://www.sandbox.paypal.com/en_US/i/btn/btn_subscribeCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
<%}else{%>
<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_subscribeCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
<%}%>
</td>
<tr>
</table>


</form>





</div>
</td>
</tr>
</table>
</body>
</html>