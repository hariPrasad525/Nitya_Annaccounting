<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
<head>
<title><i18n:i18n msg='companieslist' />| Anna Accounting</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp"%>
<%
	Boolean ipad = (Boolean) request.getAttribute("ipad");
%>
<%
	if (ipad != null && ipad) {
%>
<link type="text/css" href="../css/ipadlogin.css?version=<%=version%>"
	rel="stylesheet" />
<%
	} else {
%>
<link type="text/css" href="../css/ss.css?version=<%=version%>"
	rel="stylesheet" />
<%
	}
%>
<%
	boolean isConListRTL = (Boolean) request.getAttribute("isRTL");
%>
<%
	Boolean enableEncryption = (Boolean) request
			.getAttribute("enableEncryption");
%>
<%
	enableEncryption = enableEncryption == null
			? false
			: enableEncryption;
%>
<%
	Boolean isPaid = (Boolean) request.getAttribute("isPaid");
    isPaid = isPaid == null ? false: true;
%>
<%
	Boolean freeTrial = (Boolean) request.getAttribute("freeTrial");
%>
<%
	Boolean canEncrypt = (Boolean) request.getAttribute("encrypt");
%>
<%
	String userEmail = (String) request.getAttribute("emailId");
%>
<%
	Boolean canCreate = (Boolean) request.getAttribute("canCreate");
%>
<%
	isPaid = isPaid == null ? false : isPaid;
%>
<%
	canEncrypt = canEncrypt == null ? false : canEncrypt;
%>
<%
	canCreate = canCreate == null ? false : canCreate;
%>
<%
	enableEncryption = enableEncryption && isPaid && canEncrypt;
%>

<script type="text/javascript">
		window.onload=function(){
		document.body.style.direction=(<%=isConListRTL%>)?"rtl":"ltr";
		};
		
		$(document).ready(function() {
		var isPaid=${isPaid == null ? false: true};
		var userEmail='<%=userEmail%>';
       });
		function goto(comp){
			$(document).ready(function() {
				var params= {
					companyId: comp,
					isTouch: touchDeviceTest()
				};
				document.location = '/main/companies' + '?' + $.param(params);
			});
		};
		function createCompany(){
			$(document).ready(function() {
				var params= {
					isTouch: touchDeviceTest(),
					create:true
				};
				document.location = '/main/companies'+'?'+ $.param(params);
			});
		};
		function touchDeviceTest() {
			return navigator.userAgent.match(/iPad/i)!=null;
		}
		
	</script>
</head>
<body>
	<%
		if (ipad != null && ipad) {
	%>
	<h3>
		<i18n:i18n msg='companies' />
	</h3>
	<%
		}
	%>
	<table id="commanContainer" class="companies-list-page">
		<tr>
			<td>
				<div id="accounter-header">
					<img src="/images/Accounter_logo.png"" class="accounterLogo"
						alt="loading" />
					<%@ include file="./locale.jsp"%>
				</div>

				<table id="companies-page">
<!-- 					<tr> -->
<!-- 						<td id="migration-details"> -->
<!-- 							<div id="migration-content"> -->
<!-- 								<h2>Migrate to Ecgine</h2> -->
<!-- 								</br> -->

<!-- 								<h3>What is Ecgine?</h3> -->
<!-- 								<p>Ecgine is an unified business software suite, with built -->
<!-- 									in Accounting and CRM packages. You can install more of such -->
<!-- 									packages according to your business needs from the App Store.</p> -->

<!-- 								<h3>Why Migrate to Ecgine?</h3> -->
<!-- 								<p>You will get following benefits compared to -->
<!-- 									AccounterLive.com</p> -->
<!-- 								<ol id="migration-instructions"> -->
<!-- 									<li>Multiple business packages like Accounting, CRM etc; -->
<!-- 										you will get as a single software.</li> -->
<!-- 									<li>Customize the software according to your requirement.</li> -->
<!-- 									<li>You can choose to host the server within your -->
<!-- 										organization. This helps you work without internet.</li> -->
<!-- 								</ol> -->
<!-- 								</br> -->
<!-- 								<p> -->
<!-- 									Visit <a href="https://www.ecgine.com">Ecgine</a> to learn more -->
<!-- 									features and pricing. -->
<!-- 								</p> -->
<!-- 								</br> -->
<!-- 								<div id="proceed"> -->
<!-- 									<a class="proceed button-links" href="/main/selectcompany">Proceed</a> -->
<!-- 								</div> -->
<!-- 								<div class="clear"></div> -->
<!-- 							</div> -->

<!-- 						</td> -->
						<td class="company_lists">
							<div style="clear: both" class="company_list_child">
								<c:if test="${message != null}">
									<div class="common-box create-company-message">${message}</div>
								</c:if>
								<div class="form-box">
									<c:if test="<%=canCreate%>">
										<div>
											<a onClick=createCompany() href="#"
												class="create_new_company button-links"><i18n:i18n
													msg='createNewCompany' /></a>
										</div>
									</c:if>
									<ul>
										<li><c:if test="${companeyList != null}">
												<c:forEach var="company" items="${companeyList}">
													<c:set var='url'
														value="/main/companies?companyId=${company.id}" />
													<div class="companies-list company-name-id">
													<img src="/images/company_573865.png" "="" class="accounterLogo" alt="loading" style="
															    width: 20px;
															    height: 20px;
															    top: 17px;
															    position: relative;
															">
														<a OnClick="goto(${company.id});" href="#">${company.preferences.tradingName}
															- ${company.registeredAddress.countryOrRegion} </a>
													</div>
												</c:forEach>
											</c:if></li>
									</ul>
								</div>
							</div>
							<table class="form-bottom-options">
								<tr>
									<%--<c:choose>
							<c:when test="<%=isPaid%>">
								<td><a class="manage-subscription button-links"
									href="/main/subscriptionmanagement"><i18n:i18n
											msg='subscriptionManagement' /></a></td>
							</c:when>
							 	<c:when test="<%=freeTrial%>">
								<td><a target="_blank" class="go-premium button-links"
									href="/content/30-days-premium-trial?emailId=<%=userEmail%>">
										Premium Trial</a></td>
							</c:when>
							<c:otherwise>
								<td><a target="_blank" class="go-premium button-links"
									href="/content/go-premium?emailId=<%=userEmail%>">Go Premium</a></td>
							</c:otherwise> 
						</c:choose>--%>

									<c:if test="<%=enableEncryption%>">
										<td><a  class="logout button-links" href="/main/encryption"><i18n:i18n
													msg='encryption' /></a></td>
									</c:if>

									<td><a class="logout button-links" href="/main/logout"><i18n:i18n
												msg='logout' /></a></td>

									<td><a class="delete-account button-links"
										href="/main/deleteAccount"><i18n:i18n msg='deleteAccount' /></a></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>

			</td>
		</tr>
	</table>

	<!-- Footer Section-->


<!-- 	<div id="appVersions"> -->
<!-- 		<div> -->
<!-- 			<span>Access Anna Accounting from </span> <a target="_blank" -->
<!-- 				href="https://play.google.com/store/apps/details?id=com.vimukti.accounter.android"> -->
<!-- 				Android </a> <a target="_blank" -->
<!-- 				href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> -->
<!-- 				Windows Phone </a> <a target="_blank" -->
<!-- 				href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> -->
<!-- 				iPhone </a> <a target="_blank" -->
<!-- 				href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> -->
<!-- 				Black Berry </a> <a target="_blank" -->
<!-- 				href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> -->
<!-- 				iPad </a> <a target="_blank" -->
<!-- 				href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> -->
<!-- 				Mac OS </a> -->
<!-- 		</div> -->
<!-- 	</div> -->
	<div id="mainFooter">
		<div>
			<span><i18n:i18n msg='atTherateCopy' /></span> | <a target="_blank"
				href="/site/termsandconditions"><i18n:i18n msg='termsConditions' /></a>
			| <a target="_blank" href="/site/privacypolicy"><i18n:i18n
					msg='privacyPolicy' /></a> | <a target="_blank" href="/site/support"><i18n:i18n
					msg='support' /></a>
		</div>
	</div>
	<%@ include file="./scripts.jsp"%>
</body>
</html>