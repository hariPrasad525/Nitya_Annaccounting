<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> About us | Anna Accounting
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
<script type="text/javascript" charset="utf-8">
	var is_ssl = ("https:" == document.location.protocol);
	var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
	document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
</script>


<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
</head>
<body>
	<div class ="body-container">
		<%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
				<div class="pricing-table" id="about-layout">
					<div style="background-color: #072027 !important;height: 65px;">
					<p class="sites-header">About Us</p>
					</div>
					<div class="adjust-padding" style="margin-top:25px;">
						<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Anna Accounting is one of the products from NITYA SOFTWARE SOLUTIONS, INC. NITYA SOFTWARE SOLUTIONS, INC was incorporated in 2005 with a mission to offer reliable and cost-effective software products and services to the corporate world. We are one of the fastest growing software and development services company with a winning combination of people, process and technology.</p>	
					</div>
					<br>
					<div class="adjust-padding">
						<p><b><u>Our philosophy</u></b><br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Our company follows the philosophy "A new idea every day..." Our aim is to give out all our effort and come out with a new Idea every day. We believe that the implementation of the initiatives will enhance our competitiveness and enable us to deliver continued growth in variable operating situations. <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The objective of adding this new dimension is to help us become more market-oriented and customer-centric, considered a constant imperative in a globally integrated and rapidly changing business environment. We set very high standards for ourselves and undertake periodic reviews to assess our performance. Goal setting and adapting to change are an integral part of our functioning.</p>
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