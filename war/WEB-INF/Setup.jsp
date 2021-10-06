<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" %>
<html>
	
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta content="IE=100" http-equiv="X-UA-Compatible">
    <link type="text/css" href="../css/setup.css" rel="stylesheet" />
    <% String pageNo = (String)request.getAttribute( "pageNo" ); %>
    <title>Anna Accounting Setup</title>   
  </head>

	<script  type="text/javascript" >
		var pageNo=<%= pageNo%>;
	</script>
  <body>
  	<div class="mainPanel">
	  	<table class="title">
		  	<tr>
			  	<td><h2>Anna Accounting Setup</h2></td>
			  	<td><div width="25%"><img src="/images/Accounter_logo_title.png" /></div></td>
			</tr>
		</table>
		<div id="mainBody"></div>
		<script type="text/javascript" language="javascript" src="/accounter.setup/accounter.setup.nocache.js"></script>
	</div>
 </body>
</html>
