<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <%@ include file="./feedback.jsp" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
 <html>
 <head>
 	 <title>license| Anna Accounting</title>
 </script>
 </head>
 <body id="licenseInfo">
    <h1>License</h1>
    <c:if test="${errorMsg != null}" >
 		<div class="error-mesg-class">
 	       ${errorMsg}
 		</div> 
 	</c:if>
 	<div class="main-class">
 		<table id="licenseContainer" class="license-container-class" >
 			<tr>
 				<td>
					<tr class="under-line">
 						<h2>License Information</h2>
 					</tr>
 					<tr class="under-line">
						<td colspan="2">This page shows your current licensing information.You can use the Update License JIRA running with</td>
					</tr>
					<tr class="under-line">
						<td>Organization</td>
						<td style="bold-ele">${license.organisation}</td>
					</tr>
					<tr class="under-line">
						<td>Date Purchased</td>
						<td style="bold-ele"><fmt:formatDate value="${license.purchasedOn}" pattern="dd, MMM yyyy HH:mm"/></td>
					</tr>
					<tr class="under-line">
						<td align="top">Expired On</td>
						<td style="bold-ele"><fmt:formatDate value="${license.expiresOn}" pattern="dd, MMM yyyy HH:mm"/></td>
					</tr>
					<tr class="under-line">
						<td>Server ID</td>
						<td style="bold-ele">${license.serverId}</td>
					</tr>
					<tr class="under-line">
						<td>User Limit</td>
						<c:choose >
							<c:when test="${license.noOfUsers != -1}">
								 <td style="bold-ele"> ${license.noOfUsers}</td>
							 </c:when>
							 <c:otherwise>
								 <td style="bold-ele">Unlimited</td>
							</c:otherwise>
						</c:choose>

					</tr>
					<tr class="under-line">
						<td><h2>Update License</h2></td>
					</tr>
					<tr >
						<td colspan="2">Copy and paste the license below.You can access your license key on <a href="./MyAccount">My Account</a></td>
					</tr>
					<tr>
					<td align="left">License:</td>
					</tr>
					<tr class="under-line">
						<td colspan="2"  valign="top" align="center" >
						<form action="/main/licenseInfo" method="post">
							<div>
								<textarea name="licensekey" cols=50 rows=5></textarea>
								<div style="width: 400px; text-align: left;"><input type="submit" value="Add"/></div>
							</div>
						</form>
						</td>
					</tr> 
						
				</td>
			</tr>
		</table>
	</div>
</body>
 </html>