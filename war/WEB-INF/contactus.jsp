<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> Contact Us | Anna Accounting
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />


<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<script type="text/javascript">
<%	boolean isContRTL=(Boolean) request.getAttribute("isRTL");	%>
window.onload=function(){
document.body.style.direction=(<%= isContRTL %>)?"rtl":"ltr";
};
var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-24502570-1']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();

</script>
</head>
<body>
	<div class ="body-container">
		<%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
				<div class="pricing-table" id="contact-layout">
					<div>
						<p class="sites-header">Contact Us</p>
					</div>
					
 

					<div style="margin:30px;">
						<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NITYA SOFTWARE SOLUTIONS, INC,<br />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CA Office :  3100 Mowry Ave,<br />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Suite 205, Fremont CA,<br />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tel: (510) 790-7020<br />
						</p>	
					</div>
				</div>
			</div>
		<div class="down-test" id="down">
		</div>
		<%@ include file="./footer.jsp" %>
	</div>
	
	 
	<%@ include file="./scripts.jsp" %> 
</body>
</html>