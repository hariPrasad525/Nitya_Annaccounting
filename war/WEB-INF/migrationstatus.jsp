<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<%
	String version = application.getInitParameter("version");
%>
<link type="text/css" href="../css/ss.css?version=<%=version%>"
	rel="stylesheet" />

</head>
<body>
	<div class="clearfix container" id="signupsuccess">
		<div>
			<div class="content" id="success-content" style="display: none;">
				<h2>Your account setup is successfully completed.</h2>
				<h4>
					<a
						href="https://vimukti.ecgine.com/com.vimukti.ecgine.master/download/ecgine">Download
						Ecgine</a> to login and follow the below steps
				</h4>
				<div id="steps">
					<h4>Login steps:</h4>
					<div class="step">
						<ol>
							<li>Run the downloaded .exe file to install ecgine into your
								system.</li>
							<li>Enter your domain name i.e <c:out value="${domainName}" />
								and click on next.
							</li>
							<li>Enter user email id i.e <c:out value="${email}" /> and
								enter your password and click on login
							</li>
							</li>
						</ol>
					</div>
				</div>
			</div>
			<div id="orgcreationstatus" class="content">
				<h2>Thank you!</h2>
				<div id="statusdiv" value="In Progress">
					We are setting up your account. It will take a few minutes...<img
						alt="Status" src="/images/icons/migration-progress.gif" />
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	var refreshInterval;
	$(document).ready(function() {
		refreshInterval = setInterval(executeQuery, 10000);
	});
	function executeQuery() {
		$.post("/main/migrationstatus", function(data, status) {
			if (data == 3) {
				$('#success-content').show();
				$('#orgcreationstatus').hide();
				clearInterval(refreshInterval);
			}
		});
	}
</script>
</html>