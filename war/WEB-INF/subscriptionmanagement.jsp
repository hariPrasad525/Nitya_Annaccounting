<%@ page import="java.util.*" %>
<%@ page import="com.vimukti.accounter.core.*" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>

  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
	<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
	<link type="text/css" href="../css/ss.css" rel="stylesheet" />
    <%
		String userEmail =(String) request.getAttribute("emailId");
    	String users =(String) request.getAttribute("userIdsList");
    	String error =(String) request.getAttribute("error");
    	String expDate =(String) request.getAttribute("expiredDate");
    	Integer subscriptionType =(Integer) request.getAttribute("premiumType");
    	Integer durationType =(Integer) request.getAttribute("durationType");
    	Integer durationValue =((subscriptionType*2)-durationType) +1;
    	
  	%>
  	<title>Subscription Management</title>
  </head>
  <body>
  <div id="commanContainer" style="width:420px;  font-size: 13px;">
  <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
	<form id="subscription_complition_form" method="get"  class="form-box"  action="/main/subsdeleteuserconform">
	<table cellspacing="10">
	<tr></tr>
	<tr>
	<p id="error" style="color:red;">
	  <% if(error!=null){ %>
		<%= error %>
	  <%}%>
	 </p> 
	</tr>
	<tr>
	<td> Subscription expire date : </td> 
	<td><%= expDate %></td>
	</tr><tr></tr><tr>
	<tr></tr>
	 <% if(subscriptionType==2||subscriptionType==3){ %>
	<tr>
	<td>Users invited by you : </td> 
	<td id="emailIdsList"></td>
	</tr>
	<%}%>
	<tr>
	<tr>
	<input type="hidden" name="subscriptionType" value="${durationValue}" >
	<td> Subscription Type : </td>
	<td><select  id="subScriptionTypeCombo" disabled>
	<option value="Free user">Free user</option>
	<option value="One User yearly">One user yearly</option>
	<option value="One User monthly">One user monthly</option>
	<option value="Two Users yearly">Two users yearly</option>
    <option value="Two Users monthly">Two users monthly</option>
    <option value="Five Users yearly">Five users yearly</option>
    <option value="Five Users monthly">Five users monthly</option>
    <option value="Unlimited Users yearly">Unlimited users yearly</option>
    <option value="Unlimited Users monthly">Unlimited users monthly</option>
	</select></td>
	</tr>
	<% if(subscriptionType==2||subscriptionType==3){ %>
	<tr>	
	<td> Users Emails : </td>
	<td> <textarea id="mailIdsTextArea"  name="userMailds" onsubmit="";></textarea> </td>
	</tr>
	 <%}%>
	<tr>
	<td></td></tr>
	</table>
	
	
		<div class="subscribtionManagementButton" align="center">
			<% if(subscriptionType==2||subscriptionType==3){ %>
   				<input id="submitButton" type="submit" class="allviews-common-button" name="login" value="Save SubScription"/>
   			<%}%>
   			<a target="_blank" href="/main/subscription/gopremium?emailId=<%= userEmail %>">Upgrade Premium</a>
		</div>
		<a href="/main/companies">Companies List</a>
		<script type="text/javascript">
		$('document').ready(function(){
		   var userEmail='<%=userEmail%>';
		   var users= <%= users%>;
		   var subscriptionType=<%= subscriptionType %>;
           var textAre="";
		   var textDiv="";
		   
			if(users.length>0){
				textAre = users[0];
				textDiv = users[0];
                for(var i=1; i < users.length; i++){
					textAre +='\n'+ users[i];
					textDiv +=', '+ users[i];
				}
				$('#mailIdsTextArea').val(textAre);
				$('#emailIdsList').text(textDiv);
			}
			 
			document.getElementById('subScriptionTypeCombo').options[<%= durationValue %>].selected = true;
			
			
			$('#submitButton').click(function(){
				$('#mailIdsTextArea').val($('#mailIdsTextArea').val().replace(/^\s+|\s+$/, ''));
				var textArray2 =$('#mailIdsTextArea').val().split('\n');
				var textArray=[];
				for(var j=0; j < textArray2.length; j++){
					if(textArray2[j]==""){
						continue;
					}else{
						textArray.push(textArray2[j]);
					}
				}
				if(!validate(textArray)){
					return false;
				}

				$('#error').text("");
				$('#subscription_complition_form').submit();
				return true;
			});	
								
			function validateEmail(elementValue){  
			   var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;  
			   if (emailPattern.test(elementValue)){
			  		return true;
			   }  else{
			 		return false;
			   }
 			} 
 			
 			function validate(textArray){
 				var type =subscriptionType;
 				var maxLimit =-1; 
 				if(type ==1){
 					maxLimit =0;
 				}else if(type ==2){
 					maxLimit =1;
 				}else if(type ==3){
 					maxLimit =4;
 				}
 				
 				for(var i=0; i<textArray.length; i++){
 					if(textArray[i]==""){
 						continue;
 					}
 					if(!validateEmail(textArray[i])){
 						$('#error').text("Wrong emailId '"+textArray[i]+"'");
 						return false;
 					}
 					if(textArray[i]==userEmail){
 						$('#error').text("You can't invite yourself");
 						return false;
 					}
 				}
 				if(maxLimit<0){
 					return true;
 				}
 				
 				if(textArray.length>maxLimit){
 					$('#error').text("maximum number of users exceeded");
 					return false;
 				}
 				return true;
			}
		  
		});
		</script>
	 </form>  
	 </div>
  </body>
</html> 