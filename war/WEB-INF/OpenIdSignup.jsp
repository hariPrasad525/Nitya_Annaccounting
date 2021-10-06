<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8" %>
 
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->


<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> Sign up & Trial free | Anna Accounting
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />
<script src="/jscripts/passwordStrength.js" type="text/javascript"></script>
<script  type="text/javascript" >

$.validator.addMethod("no_special_characters", function(value, element) {
    return this.optional(element) ||
        value.match(/^[a-zA-Z0-9_]*[a-zA-Z][a-zA-Z0-9_]*$/);
}, "Company ID shouldn't contain special characters");

	$(document).ready(function() {
		<%	boolean isOpenIdSignupRTL=(Boolean) request.getAttribute("isRTL");	%>
		document.body.style.direction=(<%= isOpenIdSignupRTL %>)?"rtl":"ltr";
		$('#submitButton').removeAttr('disabled');
		$('#newsletter').attr('checked', true);
		 $("#mid-box2").focus(function() {
             if ($(this).val())
                 $(this).blur().css('background-color', 'white');
         });
		$('#mid-box4').attr('autocomplete', 'off');
		
		$('#mid-box4').password_strength();
	    var is_opera = navigator.userAgent.toLowerCase().indexOf('opera') > -1;
	    $('#select-box').keydown(function(event){
		    if(event.which == 9){
		       $('#checkbox').focus();
		    } 
	    });
	   
	    $('#mid-box2').click(function(){
	    $('.indication-box').remove();
	        $('#email_id_box').append('<div class="indication-box"><div class="left-arrow"></div><div class="box-data">Enter a valid email address. A mail will be sent to this email to confirm your account and also in case you forgot your password</div></div>');
	    }).blur(function() {
	    	var mailid = jQuery.trim($('#mid-box2').val());
	    	$('#mid-box2').val(mailid);
	        $('.indication-box').remove();
	    });
	   
		$('#submitButton').click(function() {
			$("#accounterForm").validate({
		rules: {
			firstName: "required",
			lastName: "required",
			companyName: {
				required: true,
				no_special_characters: true,
				maxlength: 64
			},
			companyFullName: "required",
			emailId: {
				required: true,
				email: true
			},
			agree: "required"
		},
		messages: {
			firstName: "Please enter your first name",
			lastName: "Please enter your last name",
			companyName: "Please enter company ID",
			companyName: {
				required: "Please enter company ID",
				no_special_characters: "Company ID should contain atleast one letter and no special characters",
				maxlength: "Company ID exceeded the maximum length"
			},
			companyFullName: "Please enter company name",
			emailId: "Please enter a valid email address",
			agree: "Please accept Terms of use"
		}
	});
			 <!--$.blockUI({ message: $('#hiddenLoaderDiv'), css: {height: '80px', width: '300px'} });--> 
			<!--document.getElementById("hiddenLoaderDiv").style.display="block";-->
    		<!--document.getElementById("signup").style.display="none";-->
		});
	});
</script>

<%
   String app = request.getHeader( "Nativeapp" );
	String userEmailId = (String)request.getAttribute("email");
	String userFirstname = (String)request.getAttribute("firstname"); 
	String userLastname = (String)request.getAttribute("lastname"); 
   boolean isNative = ( app != null && !app.equals(""));
   if( isNative ){ %>
   <link type="text/css" rel="stylesheet" href="../css/nativeLogin.css?version=<%= version%>" />
   <% } %>
   
   

</head>
<body>

	<div id="commanContainer" class="openid-signup-container" >	
  <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="Anna Accounting Logo" />	
  <img  style="display:none" src="/images/icons/loading-indicator.gif" alt="Loading" title="Loading" height="50" width="50" />
  
  <c:if test="${errormessage!=null}">
	<div id="login_error" class="common-box">
		<span>${errormessage}</span>
	</div>
  </c:if>
  <div  id="hiddenLoaderDiv" class="hiddenDiv">
		<img src="/images/icons/loading-indicator.gif" alt="Loading" height="50" width="50" />
		<span style="position: absolute; margin: 10px;">Please Wait...</span>
  </div>
  
  
  <c:if test="${successmessage==null}">
  <div id="gmailregister" class="common-box">
   <span>There is no account registered with <strong>"<%= userEmailId %>"</strong>. please complete this form to create your account.</span>
   </div>
    <form class="accounterform" id="accounterForm" method="post" action="/main/openidsignup">
    <div>
	      <span class="mandatory">All fields are mandatory</span>
	   </div>
	   <div style="clear:both" class="check_label">
	     <label>First Name</label><br />
		 <input id="mid-box"  type="text" tabindex="4" name="firstName"  value = "<%= userFirstname %>" />	
	   </div>
	    <div class="check_label">
	     <label>Last Name</label><br />
		 <input id="mid-box1"  type="text" tabindex="5" name="lastName"  value = "<%= userLastname %>"/>	
	   </div>
	   <div class="check_label">
	     <label>Email Address</label><br />
		 <input id="mid-box2"  type="text"  tabindex="6" name="emailId" value = "<%= userEmailId %>"/>	
	   </div>
	   <div class="check_label">
	     <label>Phone Number</label><br />
		 <input id="mid-box6"  type="text" tabindex="9" name="phoneNumber" />
	   </div>
	   <div class="check_label">
	     <label>Country</label><br />
		 <select id="select-box" tabindex="10" name="country">
<option value="United Kingdom">United Kingdom</option>
											<option value="United States">United States</option>
											<option value="India">India</option>
											<option value="">---------------------</option>
											<option value="Afghanistan">Afghanistan</option>
											<option value="Albania">Albania</option>
											<option value="Algeria">Algeria</option>
											<option value="American Samoa">American Samoa</option>
											<option value="Andorra">Andorra</option>
											<option value="Angola">Angola</option>
											<option value="Anguilla">Anguilla</option>
											<option value="Antarctica">Antarctica</option>
											<option value="Antigua and Barbuda">Antigua and Barbuda</option>
											<option value="Argentina">Argentina</option>
											<option value="Armenia">Armenia</option>
											<option value="Aruba">Aruba</option>
											<option value="Austria">Austria</option>
											<option value="Australia">Australia</option>
											<option value="Azerbaijan">Azerbaijan</option>
											<option value="Bahamas, The">Bahamas, The</option>
											<option value="Bahrain">Bahrain</option>
											<option value="Bangladesh">Bangladesh</option>
											<option value="Barbados">Barbados</option>
											<option value="Belarus">Belarus</option>
											<option value="Belgium">Belgium</option>
											<option value="Belize">Belize</option>
											<option value="Benin">Benin</option>
											<option value="Bermuda">Bermuda</option>
											<option value="Bhutan">Bhutan</option>
											<option value="Bolivia">Bolivia</option>
											<option value="Bosnia and Herzegovina">Bosnia and Herzegovina</option>
											<option value="Botswana">Botswana</option>
											<option value="Bouvet Island">Bouvet Island</option>
											<option value="Brazil">Brazil</option>
											<option value="British Indian Ocean Territory">British Indian Ocean Territory</option>
											<option value="Brunei">Brunei</option>
											<option value="Bulgaria">Bulgaria</option>
											<option value="Burkina Faso">Burkina Faso</option>
											<option value="Burundi">Burundi</option>
											<option value="Cambodia">Cambodia</option>
											<option value="Cameroon">Cameroon</option>
											<option value="Canada">Canada</option>
											<option value="Cape Verde">Cape Verde</option>
											<option value="Cayman Islands">Cayman Islands</option>
											<option value="Central African Republic">Central African Republic</option>
											<option value="Chad">Chad</option>
											<option value="Chile">Chile</option>
											<option value="China">China</option>
											<option value="Christmas Island">Christmas Island</option>
											<option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</option>
											<option value="Colombia">Colombia</option>
											<option value="Comoros">Comoros</option>
											<option value="Congo, Democratic Republic of the">Congo, Democratic Republic of the</option>
											<option value="Congo, Republic of the">Congo, Republic of the</option>
											<option value="Cook Islands">Cook Islands</option>
											<option value="Costa Rica">Costa Rica</option>
											<option value="Cote d'Ivoire">Cote d'Ivoire</option>
											<option value="Croatia">Croatia</option>
											<option value="Cuba">Cuba</option>
											<option value="Cyprus">Cyprus</option>
											<option value="Czech Republic">Czech Republic</option>
											<option value="Denmark">Denmark</option>
											<option value="Djibouti">Djibouti</option>
											<option value="Dominica">Dominica</option>
											<option value="Dominican Republic">Dominican Republic</option>
											<option value="Ecuador">Ecuador</option>
											<option value="Egypt">Egypt</option>
											<option value="El Salvador">El Salvador</option>
											<option value="Equatorial Guinea">Equatorial Guinea</option>
											<option value="Eritrea">Eritrea</option>
											<option value="Estonia">Estonia</option>
											<option value="Ethiopia">Ethiopia</option>
											<option value="Falkland Islands (Islas Malvinas)">Falkland Islands (Islas Malvinas)</option>
											<option value="Faroe Islands">Faroe Islands</option>
											<option value="Fiji">Fiji</option>
											<option value="Finland">Finland</option>
											<option value="France">France</option>
											<option value="French Guiana">French Guiana</option>
											<option value="French Polynesia">French Polynesia</option>
											<option value="French Southern and Antarctic Lands">French Southern and Antarctic Lands</option>
											<option value="Gabon">Gabon</option>
											<option value="Gambia, The">Gambia, The</option>
											<option value="Georgia">Georgia</option>
											<option value="Germany">Germany</option>
											<option value="Ghana">Ghana</option>
											<option value="Gibraltar">Gibraltar</option>
											<option value="Greece">Greece</option>
											<option value="Greenland">Greenland</option>
											<option value="Grenada">Grenada</option>
											<option value="Guadeloupe">Guadeloupe</option>
											<option value="Guam">Guam</option>
											<option value="Guatemala">Guatemala</option>
											<option value="Guinea">Guinea</option>
											<option value="Guinea-Bissau">Guinea-Bissau</option>
											<option value="Guyana">Guyana</option>
											<option value="Haiti">Haiti</option>
											<option value="Heard Island and McDonald Islands">Heard Island and McDonald Islands</option>
											<option value="Holy See (Vatican City)">Holy See (Vatican City)</option>
											<option value="Honduras">Honduras</option>
											<option value="Hong Kong">Hong Kong</option>
											<option value="Hungary">Hungary</option>
											<option value="Iceland">Iceland</option>
											<option value="India">India</option>
											<option value="Indonesia">Indonesia</option>
											<option value="Iran">Iran</option>
											<option value="Iraq">Iraq</option>
											<option value="Ireland">Ireland</option>
											<option value="Isle of Man">Isle of Man</option>
											<option value="Israel">Israel</option>
											<option value="Italy">Italy</option>
											<option value="Jamaica">Jamaica</option>
											<option value="Jan Mayen">Jan Mayen</option>
											<option value="Japan">Japan</option>
											<option value="Jordan">Jordan</option>
											<option value="Kazakhstan">Kazakhstan</option>
											<option value="Kenya">Kenya</option>
											<option value="Kiribati">Kiribati</option>
											<option value="Korea, North">Korea, North</option>
											<option value="Korea, South">Korea, South</option>
											<option value="Kuwait">Kuwait</option>
											<option value="Kyrgyzstan">Kyrgyzstan</option>
											<option value="Laos">Laos</option>
											<option value="Latvia">Latvia</option>
											<option value="Lebanon">Lebanon</option>
											<option value="Lesotho">Lesotho</option>
											<option value="Liberia">Liberia</option>
											<option value="Libya">Libya</option>
											<option value="Liechtenstein">Liechtenstein</option>
											<option value="Lithuania">Lithuania</option>
											<option value="Luxembourg">Luxembourg</option>
											<option value="Macau">Macau</option>
											<option value="Macedonia">Macedonia</option>
											<option value="Madagascar">Madagascar</option>
											<option value="Malawi">Malawi</option>
											<option value="Malaysia">Malaysia</option>
											<option value="Maldives">Maldives</option>
											<option value="Mali">Mali</option>
											<option value="Malta">Malta</option>
											<option value="Marshall Islands">Marshall Islands</option>
											<option value="Martinique">Martinique</option>
											<option value="Mauritania">Mauritania</option>
											<option value="Mauritius">Mauritius</option>
											<option value="Mayotte">Mayotte</option>
											<option value="Mexico">Mexico</option>
											<option value="Micronesia, Federated States of">Micronesia, Federated States of</option>
											<option value="Moldova">Moldova</option>
											<option value="Monaco">Monaco</option>
											<option value="Mongolia">Mongolia</option>
											<option value="Montserrat">Montserrat</option>
											<option value="Morocco">Morocco</option>
											<option value="Mozambique">Mozambique</option>
											<option value="Namibia">Namibia</option>
											<option value="Nauru">Nauru</option>
											<option value="Nepal">Nepal</option>
											<option value="Netherlands">Netherlands</option>
											<option value="Netherlands Antilles">Netherlands Antilles</option>
											<option value="New Caledonia">New Caledonia</option>
											<option value="New Zealand">New Zealand</option>
											<option value="Nicaragua">Nicaragua</option>
											<option value="Niger">Niger</option>
											<option value="Nigeria">Nigeria</option>
											<option value="Niue">Niue</option>
											<option value="Norfolk Island">Norfolk Island</option>
											<option value="Northern Mariana Islands">Northern Mariana Islands</option>
											<option value="Norway">Norway</option>
											<option value="Oman">Oman</option>
											<option value="Pakistan">Pakistan</option>
											<option value="Palau">Palau</option>
											<option value="Panama">Panama</option>
											<option value="Papua New Guinea">Papua New Guinea</option>
											<option value="Paraguay">Paraguay</option>
											<option value="Peru">Peru</option>
											<option value="Philippines">Philippines</option>
											<option value="Pitcairn Islands">Pitcairn Islands</option>
											<option value="Poland">Poland</option>
											<option value="Portugal">Portugal</option>
											<option value="Puerto Rico">Puerto Rico</option>
											<option value="Qatar">Qatar</option>
											<option value="Reunion">Reunion</option>
											<option value="Romania">Romania</option>
											<option value="Russia">Russia</option>
											<option value="Rwanda">Rwanda</option>
											<option value="Saint Helena">Saint Helena</option>
											<option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>
											<option value="Saint Lucia">Saint Lucia</option>
											<option value="Saint Pierre and Miquelon">Saint Pierre and Miquelon</option>
											<option value="Saint Vincent and the Grenadines">Saint Vincent and the Grenadines</option>
											<option value="Samoa">Samoa</option>
											<option value="San Marino">San Marino</option>
											<option value="Sao Tome and Principe">Sao Tome and Principe</option>
											<option value="Saudi Arabia">Saudi Arabia</option>
											<option value="Senegal">Senegal</option>
											<option value="Serbia and Montenegro">Serbia and Montenegro</option>
											<option value="Seychelles">Seychelles</option>
											<option value="Sierra Leone">Sierra Leone</option>
											<option value="Singapore">Singapore</option>
											<option value="Slovakia">Slovakia</option>
											<option value="Slovenia">Slovenia</option>
											<option value="Solomon Islands">Solomon Islands</option>
											<option value="Somalia">Somalia</option>
											<option value="South Africa">South Africa</option>
											<option value="South Georgia and the South Sandwich Islands">South Georgia and the South Sandwich Islands</option>
											<option value="Spain">Spain</option>
											<option value="Sri Lanka">Sri Lanka</option>
											<option value="Sudan">Sudan</option>
											<option value="Suriname">Suriname</option>
											<option value="Svalbard">Svalbard</option>
											<option value="Swaziland">Swaziland</option>
											<option value="Sweden">Sweden</option>
											<option value="Switzerland">Switzerland</option>
											<option value="Syria">Syria</option>
											<option value="Taiwan">Taiwan</option>
											<option value="Tajikistan">Tajikistan</option>
											<option value="Tanzania">Tanzania</option>
											<option value="Thailand">Thailand</option>
											<option value="Timor-Leste">Timor-Leste</option>
											<option value="Togo">Togo</option>
											<option value="Tokelau">Tokelau</option>
											<option value="Tonga">Tonga</option>
											<option value="Trinidad and Tobago">Trinidad and Tobago</option>
											<option value="Tunisia">Tunisia</option>
											<option value="Turkey">Turkey</option>
											<option value="Turkmenistan">Turkmenistan</option>
											<option value="Turks and Caicos Islands">Turks and Caicos Islands</option>
											<option value="Tuvalu">Tuvalu</option>
											<option value="Uganda">Uganda</option>
											<option value="Ukraine">Ukraine</option>
											<option value="United Arab Emirates">United Arab Emirates</option>
											<option value="Uruguay">Uruguay</option>
											<option value="Uzbekistan">Uzbekistan</option>
											<option value="Vanuatu">Vanuatu</option>
											<option value="Venezuela">Venezuela</option>
											<option value="Vietnam">Vietnam</option>
											<option value="Virgin Islands">Virgin Islands</option>
											<option value="Wallis and Futuna">Wallis and Futuna</option>
											<option value="Western Sahara">Western Sahara</option>
											<option value="Yemen">Yemen</option>
											<option value="Zambia">Zambia</option>
											<option value="Zimbabwe">Zimbabwe</option>
		 </select>
	   </div>
	   <div>
	     <label style="padding-left:5px;">I have read and I accept the<a href="/site/termsandconditions" target="_blank" tabindex="12">Terms of Use</a></label>
	     <input id="checkbox" type="checkbox" name="agree" tabindex="11" style="float:left" />
		 
	   </div>
	   <div>
	      <input id="newsletter" type="checkbox" name="newsletter" />
		  <label>Yes, Subscribe me to Anna Accounting Newsletter <b>(Optional)</b></label>
	   </div>
	   <div class="signup-submit">
	   <a href="/main/login" class="allviews-common-button" id="openid-cancel">Cancel</a>
	      <input id="submitButton" type="submit" disabled="disabled" class="allviews-common-button" name="getstarted" value="Sign Up" tabindex="13" />
	   </div>
	</form>
  </c:if>
  <div class="form-bottom-options">
  <a href="/main/login" id="forget-link1">I already have an account</a>
  <br />
  </div> 
</div>


<!-- Footer Section-->


<!-- <div id="appVersions"> -->
<!-- 	    <div> -->
<!-- 	       <span>Access Anna Accounting from </span> -->
<!-- 	       <a target="_blank" href="https://market.android.com/details?id=com.vimukti.accounter"> Android </a> | -->
<!-- 	       <a target="_blank" href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> Windows Phone </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> iPhone </a> | -->
<!-- 		   <a target="_blank" href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> Black Berry </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> iPad </a> | -->
<!-- 		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> Mac OS </a> -->
<!-- 		   </div> -->
<!-- </div> -->
<div id="mainFooter"  >
	<div>
	   <span>&copy 2019 NITYA SOFTWARE SOLUTIONS, INC.</span> |
	   <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
	   <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
	   <a target="_blank" href="/site/support"> Support </a>
	</div>
</div>
		<script  type="text/javascript" >
			if(${successmessage!=null}) {
				document.getElementById("right-side-options").style.marginTop = "15px";
				document.getElementById("vertical-line").style.marginTop = "30px";
			}
		</script>
		
 

<%@ include file="./scripts.jsp" %>
<script  type="text/javascript" >
	olark('api.visitor.getDetails', function(details){
		document.forms.accounterForm.country.value=details.country;
	});
</script>
</body>
</html>