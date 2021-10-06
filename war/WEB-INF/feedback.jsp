
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

<!-- <script id="_webengage_script_tag" type="text/javascript">
  window.webengageWidgetInit = window.webengageWidgetInit || function(){
    webengage.init({
      licenseCode:"76aa650"
    }).onReady(function(){
      webengage.render();
      webengage.survey.render({
    	    data : {
    	      "Email-ID" : emailId,
    	    }
    	  });
    });
  };

  (function(d){
    var _we = d.createElement('script');
    _we.type = 'text/javascript';
    _we.async = true;
    _we.src = (d.location.protocol == 'https:' ? "//ssl.widgets.webengage.com" : "//cdn.widgets.webengage.com") +
              "/js/widget/webengage-min-v-3.0.js";
    var _sNode = d.getElementById('_webengage_script_tag');
    _sNode.parentNode.insertBefore(_we, _sNode);
  })(document);
</script> -->
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
