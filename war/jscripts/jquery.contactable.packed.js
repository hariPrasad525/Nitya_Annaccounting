/*
 * contactable 1.0 - jQuery Ajax contact form
 *
 * Copyright (c) 2009 Philip Beel (http://www.theodin.co.uk/)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php) 
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * Revision: $Id: jquery.contactable.pack.js 2009-08-24 $
 *
 */

(function($) {
	$.fn.contactable = function(options) {
		var defaults = {
			name : 'Name',
			email : 'Enter your email here  ',
			message : 'Message',
			ajax : true,
			subject : 'A contactable message'
		};
		var options = $.extend(defaults, options);
		return this
				.each(function(options) {
					$(this)
							.html(
									'<div id="contactable"></div><form id="contactForm" method="" action=""><div id="callback"></div><div class="holder"><input type="hidden" id="recipient" name="recipient" value="'
											+ defaults.recipient
											+ '" /><input type="hidden" id="subject" name="subject" value="'
											+ defaults.subject
											+ '" /><p><label for="name">Name <span class="red">  </span></label><br /><input id="name" class="contact" name="name" /></p><p><label for="email">E-Mail <span class="red">  </span></label><br /><input id="email" class="contact" name="email"  value="'
											+ defaults.email
											+ '" /></p><p class= "yourfeedback"><label for="comment">Your Feedback <span class="red"> * </span></label><br /><textarea id="comment" name="comment" class="comment" rows="4" cols="30" ></textarea></p><p><input class="submit" type="submit" value="Send"/></p><p>If you want to suggest some improvement, send a feedback to Accounter Team.</p></div></form>');
					$('div#contactable').toggle(function() {
						$('#overlay').css( {
							display : 'block'
						});
						$(this).animate( {
							"marginRight" : "-=5px"
						}, "fast");
						$('#contactForm').animate( {
							"marginRight" : "-=0px"
						}, "fast");
						$(this).animate( {
							"marginRight" : "+=387px"
						}, "slow");
						$('#contactForm').animate( {
							"marginRight" : "+=390px"
						}, "slow")
					}, function() {
						$('#contactForm').animate( {
							"marginRight" : "-=390px"
						}, "slow");
						$(this).animate( {
							"marginRight" : "-=387px"
						}, "slow").animate( {
							"marginRight" : "+=5px"
						}, "fast");
						$('#overlay').css( {
							display : 'none'
						})
					});
					$("#contactForm").validate(
							{
								rules : {
									name : {
										required : false,
										minlength : 2
									},
									email : {
										required : false,
										email : false
									},
									comment : {
										required : true
									}
								},
								messages : {
									name : "",
									email : "",
									comment : ""
								},
								submitHandler : function() {
									$('#loading').show();
									$.post('/main/support', {
										ajax : true,
										name : $('#name').val(),
										email : $('#email').val(),
										comment : $('#comment').val()
									}, function(data) {
										$('#loading').css( {
											display : 'none'

										});
										if (data == 'success') {
											$('#sucmsg').remove();
											var msgDiv = $('<div id="sucmsg"><p>Thankyou for your Feedback</p>')
											$(".holder").prepend(msgDiv);
											$("#sucmsg").fadeOut(5000);
											$('#name').val("");
											$('#email').val("");
											$('#comment').val("");
										} else {
											$('#failmsg').remove();
											var msgDiv = $('<div id="failmsg"><p>Sorry mail not send,please try again later</p>')
											$(".holder").prepend(msgDiv);
											$("#failmsg").fadeOut(5000);
											$('#name').val("");
											$('#email').val("");
											$('#comment').val("");
										}
									})
							

								}
							})
							
				})
	}
})(jQuery);