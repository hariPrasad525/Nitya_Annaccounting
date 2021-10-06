<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
<head>
<title>Migration details</title>
<%
	String version = application.getInitParameter("version");
%>
<link type="text/css" href="../css/ss.css?version=<%=version%>"
	rel="stylesheet" />
<script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#upgrade').click(function() {
			$("#accounterForm").validate({
		rules: {
			company :"required",
			firstName: "required",
			lastName: "required",
			password: {
				required: true,
				minlength: 6
			},
			emailId: {
				required: true,
				email: true
			},
			domain:"required"
		},
		messages: {
			company: "Please select company",
			firstName: "<i18n:i18n msg='pleaseenteryourfirstname'/>",
			lastName: "<i18n:i18n msg='pleaseenteryourlastname'/>",
			password: {
				required: "<i18n:i18n msg='pleaseprovideapassword'/>",
				minlength: "<i18n:i18n msg='yourpasswordmustbeatleast6characterslong'/>"
			},
			emailId: "<i18n:i18n msg='pleaseenteravalidemailaddress'/>",
		    domain: "Please provide domain"
		}
	});
		});
	});
</script>
</head>
<body>
	<form action="/main/selectcompany" method="post" id="accounterForm">
		<div id="company-migrator">
			<h2>Migrate to Ecgine</h2>
			<div class="form-item">
				<label>Company</label><select name="company" tabindex="1" required>
					<c:forEach var="company" items="${companeyList}">
						<option value="${company.id}">${company.preferences.tradingName}-
							${company.registeredAddress.countryOrRegion}</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-item" style="clear: both">
				<label>First Name</label><input type="text" name="firstName"
					tabindex="2" id="mid-box" required/>
			</div>
			<div class="form-item">
				<label>Last Name</label><input type="text" name="lastName"
					tabindex="3" id="mid-box1" required/>
			</div>
			<div class="form-item">
				<label>Email</label><input type="email" name="emailId" tabindex="4"
					id="mid-box2" required/>
			</div>
			<div class="form-item">
				<label>Password</label><input type="password" name="password"
					tabindex="5" id="mid-box4" required/>
			</div>
			<div class="form-item">
				<label>Domain</label><input type="text" name="domain" tabindex="6"
					id="mid-box1" required/>
			</div>
			<div id="buttons-pane">
				<input type="submit" tabindex="7" value="Upgrade"
					class="allviews-common-button" id="upgrade">
			</div>
		</div>
	</form>
</body>
</html>