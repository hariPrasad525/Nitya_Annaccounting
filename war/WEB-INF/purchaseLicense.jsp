<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@ include file="./scripts.jsp" %>
<html>
  <head>
	  <title><i18n:i18n msg='purchaseLicense'/> | Anna Accounting
	  </title>
	  <link type="text/css" href="../../css/ss.css" rel="stylesheet" />
	  <link type="text/css" href="../../css/setup.css" rel="stylesheet" />
  </head>
  
  <body>
  <div class="license-purchase">
  		<div>
  		<img alt="accounter logo" class="accounterLogo" src="/images/Accounter_logo_title.png">
  		</div>
  		<div class="form-box">
		  	<form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
			<input type="hidden" name="cmd" value="_s-xclick">
			<input type="hidden" name="hosted_button_id" value="ELNAGRJQZ24FA">
			<table>
			<tr><td><input type="hidden" name="on0" value="License Purchase Options">License Purchase Options</td></tr><tr><td><select name="os0">
				<option value="One user yearly">One user yearly: $100.00 USD - yearly</option>
				<option value="2 users yearly">2 users yearly: $200.00 USD - yearly</option>
				<option value="5 users yearly">5 users yearly: $500.00 USD - yearly</option>
				<option value="Unlimited users yearly">Unlimited users yearly: $1,000.00 USD - yearly</option>
			</select> </td></tr>
			</table>
			<input type="hidden" name="currency_code" value="USD">
			<input type="image" src="https://www.sandbox.paypal.com/en_US/i/btn/btn_subscribeCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
			<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
			</form>
	  	</div>
	</div>
  </body>
  
</html>