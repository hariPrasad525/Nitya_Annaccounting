
<%
	String version = application.getInitParameter("version");
	String emailId = (String) request.getSession().getAttribute(
			"emailId");
	String isTouch = (String) request.getAttribute("isTouch");
%>
<script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>

<%
	if (isTouch != null && isTouch.equals("true")) {
%>
<link type="text/css" rel="stylesheet"
	href="../css/iPad.css?version=<%=version%>" />
<%
	} else {
%>
<link type="text/css" rel="stylesheet"
	href="/css/web.css?version=<%=version%>" />
<%
	}
%>


<script type="text/javascript">

	$(document).ready(function() {
	<%boolean isFeedbackRTL = (Boolean) request.getAttribute("isRTL");%>
	window.onload=function(){
	document.body.style.direction=(<%=isFeedbackRTL%>)?"rtl":"ltr";
	}				
	
});
	var emailId="<%=emailId%>";
	if (emailId == "null") {
		emailId = "enter your email here"
	}
	var isTouch = <%=isTouch%> ;
</script>
