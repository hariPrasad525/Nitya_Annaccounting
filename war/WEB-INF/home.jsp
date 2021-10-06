<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>Free Online Accounting Software. Free Support | Anna Accounting
</title>
 <meta http-equiv="refresh" content="0;URL='/main/login'" />    

<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<script type="text/javascript" src="/jscripts/stepcarousel.js"></script>
<script type="text/javascript">
jQuery.noConflict();
jQuery(document).ready(function() {
	
	jQuery('#blog-bg').click(function(event){
		event.preventDefault();
		jQuery('.menu-bar').find('a').removeClass("header-hover");
		jQuery(this).addClass("header-hover");
		window.open("http://blog.annaccounting.com/","_blank");
	});
	
});
			stepcarousel.setup({
				galleryid: 'mygallery', // id of carousel DIV
				beltclass: 'belt', // class of inner "belt" DIV containing all
									// the panel DIVs
				panelclass: 'panel', // class of panel DIVs each holding
										// content
				autostep: {enable:true, moveby:1, pause:3000},
				panelbehavior: {speed:1000, wraparound:true, wrapbehavior:'slide', persist:true},
				defaultbuttons: {enable: true, moveby: 1, leftnav: ['/images/Left-arrow_24x25.png', 127, 279], rightnav: ['/images/Right-arrow_24x25.png', -169, 279]},
				statusvars: ['statusA', 'statusB', 'statusC'], // register 3
																// variables
																// that contain
																// current panel
																// (start),
																// current panel
																// (last), and
																// total panels
				contenttype: ['inline'] // content setting ['inline'] or
										// ['ajax', 'path_to_external_file']
				})
</script>
</head>
	<body>
		<div class ="body-container">
			<div class ="main_body-container">
			<div class ="header" id="top">
			
			<div class="menu-table">
			   <div class="acc_logo_image">
			       <div class="logo-image">
					  <a href="/"><img src="/images/Accounter_logo_title.png" alt="Anna Accounting"/></a>
				   </div>
			   </div>
			   <div class="menu-bar-login">
			      <div class="menu-bar">
				<ul>
<!-- 					 <li> -->
<!-- 					    <a href="/site/home" id="home-bg" class="header-hover">Home</a> -->
<!-- 					 </li>  -->
					 <li>
					    <a href="/site/features" id="fea-bg">Features</a>
					 </li> 
					 <li>
						<a href="/site/support" id="sup-bg">Support</a>
					</li>
<!-- 					 <li> -->
<!-- 					    <a id="blog-bg">Blog</a> -->
<!-- 					 </li> -->
			  </ul>
				</div>
				<div class="remaining-header-part-login">
					<ul>
					   <li><a href="/main/login" id="login-id">Login</a></li>
					</ul>
				</div>
			   </div>
			</div>
			</div>
			<div class="middle-part">
				<div class="upper-part" id="cen">
				<div class="left-middle">
				<div id="header">
				<div class="image-text-button" >
				<h1 align="center" class="account-sys-label">Now Accounting is easy!</h1>
				<p align="center" class="easiest-label">Work on your finances from anywhere, anytime, it's Free</p>
				<ul class="signup-for-free-button">
				   <li><span class="sign-up-left"></span></li>
				   <li><a href="/main/signup" id="but-g" class="sign-up-middle">Sign Up, Its Free</a></li>
				   <li><span class="sign-up-right"></span></li>
				</ul>
					</div>
			</div>  
        	</div>
        	<div class="right-middle">
					<div id="mygallery" class="stepcarousel">
			<div class="belt">
				<div class="panel"><img src="/images/Balancesheet.png" alt="Balance Sheet" /></div>
				<div class="panel"><img src="/images/TrailBlance.png" alt="Trail Balance" /></div>
				<div class="panel"><img src="/images/Enterbillscreenshot.png" alt="Enter Bill" /></div>
				<div class="panel"><img src="/images/Invoice1.png" alt="Invoice" /></div>
				<div class="panel"><img src="/images/ProfitAndLoss.png" alt="Profit And Loss" /></div>
			</div>
		</div>
		<p id="mygallery-paginate" style="width: 502px; text-align:center">
			<img style="cursor: pointer; margin-left: 5px; margin-right: 10px;" data-moveby="1" data-select="/images/Bullet-icon_10x11.png" data-over="/images/Bullet-icon_10x11.png" src="/images/Image-flow-indicator.png" alt=""/>
		</p>
		</div>
		</div>
		<div class="lower-part">
				
					<ol>
						<li><img class="icon1" src="/images/On-offline.png" alt="Online" /><p class="header_p_style"><b>Easy Online Accounting</b></p>
					 	<p>Keep track of your daily financial activities. Track where your money is coming from and going out quickly. You can login from anywhere, anytime.</p>
					  </li>
					  <li><img class="icon2" src="/images/Invoice.png" alt="Invoice" /><p class="header_p_style"><b>Invoicing</b></p>
					 	<p>Customize and send invoices for payments and record bills and expenses.</p>
					  </li>
						<li><img class="icon3" src="/images/Sharing.png" alt="Sharing" /><p class="header_p_style"><b>Easy Sharing</b></p>
					 	<p>Let your accountant, financial adviser or employee have access to your data by sharing or delegating the accounting duties to them. You can set different permission to different users.</p>
					 </li>
				    </ol>
				    <ol>
						<li><img class="icon4" src="/images/Decide.png" alt="Decide" /><p class="header_p_style"><b>Make Quick Decisions</b></p>
					 	<p>With a glance of the dash board you can quickly understand what is working and what is not working with your business? The insightful reports available in the software makes it easy for you to take quick decisions.</p>
					 </li>
						<li><img class="icon5" src="/images/Contacts.png" alt="Contacts" /><p class="header_p_style"><b>Contacts and Inventory</b></p>
					 	<p>Manage and review your customers, suppliers and inventory items details easily.</p>
					  </li>
						<li><img class="icon6" src="/images/Credit-card.png" alt="Credit Card" /><p class="header_p_style"><b>Bank and Credit card transactions</b></p>
					 	<p>Track your bank deposits, fund transfers, credit card charges and refunds. </p>
					  </li>
					  </ol>
			</div>	
			
			<%@ include file="./footer.jsp" %>
		</div>
		
		 
		</div>
		</div>
		<%@ include file="./scripts.jsp" %>
	</body>
</html>
