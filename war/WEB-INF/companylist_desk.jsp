<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
  <head>
  <title><i18n:i18n msg='companieslist'/>| Anna Accounting
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		<%@ include file="./feedback_desk.jsp" %>
	<% 
	Boolean ipad = (Boolean)request.getAttribute( "ipad" );
	%>
	<% if(ipad != null && ipad){%>
	<link type="text/css" href="../css/ipadlogin.css?version=<%= version%>" rel="stylesheet" />
	<% }else{%>
	<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
		<% }%>
		<%	boolean isConListRTL=(Boolean) request.getAttribute("isRTL");	%>
		<%	Boolean enableEncryption=(Boolean) request.getAttribute("enableEncryption");	%>
		<%	enableEncryption=enableEncryption==null?false:enableEncryption;	%>
		<%	Boolean isPaid=(Boolean) request.getAttribute("isPaid");	%>
		<%	Boolean canEncrypt=(Boolean) request.getAttribute("encrypt");	%>
		<%	String userEmail=(String) request.getAttribute("emailId");	%>
		<%	Boolean canCreate=(Boolean) request.getAttribute("canCreate");	%>
		<%	isPaid=isPaid==null?false:isPaid;	%>
		<%	canEncrypt=canEncrypt==null?false:canEncrypt;	%>
		<%	canCreate=canCreate==null?false:canCreate;	%>
		<%	enableEncryption=enableEncryption&&isPaid&&canEncrypt;	%>
		
	<script type="text/javascript">
		window.onload=function(){
		document.body.style.direction=(<%= isConListRTL %>)?"rtl":"ltr";
		};
		
		$(document).ready(function() {
		var isPaid=${isPaid};
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
			var el = document.createElement('div');
			el.setAttribute('ongesturestart', 'return;');
			if(typeof el.ongesturestart == "function"){
				return true;
			}else {
				return false;
			}
		}
		
	</script>
  </head>
  <body>
        	<% if(ipad != null && ipad){%>
  						<h3><i18n:i18n msg='companies'/></h3>
			<% }%>
  <table id="commanContainer" class="companies-list-page">
  <tr>
  <td>
	<div>
		<img style="float:left" src="/images/Accounter_logo_title.png" class="accounterLogo" alt="loading" />
	</div>
	<div style="float:right">
	 <%@ include file="./locale.jsp" %>
	</div>
    <div class="company_lists" style="clear:both">
        <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
       <div class="form-box">
        <c:if test="<%= canCreate%>">
      	<div> <a onClick=createCompany() href="#" class="create_new_company"><i18n:i18n msg='createNewCompany'/></a></div>
      	</c:if>
      	<ul><li>
	    <c:if test="${companeyList != null}">
		   <c:forEach var="company" items="${companeyList}">
			   <c:set var='url' value="/main/companies?companyId=${company.id}"/>
			   <div class="companies-list"><a OnClick="goto(${company.id});" href="#">${company.preferences.tradingName} - ${company.registeredAddress.countryOrRegion} </a></div>
		   </c:forEach> 
	    </c:if>
	    </li>
	   </ul>
	  </div>
    </div>
    <table class="form-bottom-options">
    <tr>
    <td>
      <a id="logoutlink" href="/main/logout"><i18n:i18n msg='logout'/></a>
      </td>
	      <c:choose>
				<c:when test="<%= isPaid %>">
				<td>
			       <a target="_blank" href="/main/licenseInfo"><i18n:i18n msg='manageLicense'/></a>
				</td>
				</c:when>
		   </c:choose>
      
	      <c:if test="<%= enableEncryption %>">
	      <td>
	    	 <a href="/main/encryption"><i18n:i18n msg='encryption'/></a>
	      </td>
	      </c:if>
	       <td>
		      <a href="/main/deleteAccount"><i18n:i18n msg='deleteAccount'/></a>
			</td>
	     </tr>
    </table>
      </td>
   </tr>
</table>
   
   <!-- Footer Section-->
   

   <div id="appVersions"/>
	   <div id="mainFooter"  >
   <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span>
   </div>
</div>
  </body>
  <%@ include file="./scripts_desk.jsp" %>
</html>