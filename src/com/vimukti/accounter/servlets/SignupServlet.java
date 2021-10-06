package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.Global;

public class SignupServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/signup.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String header2 = req.getHeader("User-Agent");
		boolean contains = header2.contains("iPad");
		if (contains) {
			req.setAttribute("ipad", contains);
		}

		dispatch(req, resp, view);

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		boolean response = isBotTryingToLogin("https://www.google.com/recaptcha/api/siteverify", "app.annaccounting.com", "secret=6LdZG5IUAAAAAFeKvI-MQ3BIfxJOOVsLr2B3_5X9&response="+req.getParameter("google-key").trim());
		
		// Take userName from request
		String emailId = req.getParameter("emailId").trim();
		// String confirmEmailId = req.getParameter("confirmemailId").trim();
		String firstName = req.getParameter("firstName").trim();
		String lastName = req.getParameter("lastName").trim();
		String password = req.getParameter("password").trim();
		// String phoneNumber = req.getParameter("phoneNumber").trim();
		String country = req.getParameter("country").trim();
		boolean isSubscribedToNewsLetter = true;
		/*
		 * if (req.getParameter("newsletter") != null &&
		 * req.getParameter("newsletter").equals("on")) isSubscribedToNewsLetter
		 * = true;
		 */

		boolean isAgreed = true;
		// if (req.getParameter("agree") != null
		// && req.getParameter("agree").equals("on"))
		// isAgreed = true;
		// if (!isAgreed) {
		// dispatchMessage(Global.get().messages().pleaseacceptTermsofuse(),
		// req, resp, view);
		// }

		if (!isValidInputs(NAME, firstName, lastName, country)
				|| !isValidInputs(MAIL_ID, emailId)) {
			dispatchMessage(Global.get().messages().incorrectEmailOrPassWord(),
					req, resp, view);
			return;
		}
		
		if(!response) {
			req.setAttribute("errormessage", Global.get().messages().errorMsg("Something wrong with your signup, please reload the page and try again."));
			dispatch(req, resp, view);
			return;
		}
		
		
		// if (!emailId.equals(confirmEmailId)) {
		// dispatchMessage(Global.get().messages()
		// .emailIdAndConfirmEmaildMustBeSame(), req, resp, view);
		// return;
		// }
		emailId = emailId.toLowerCase();
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(emailId
				+ Client.PASSWORD_HASH_STRING + password));
		HttpSession session = req.getSession(true);
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = hibernateSession.beginTransaction();
			// Have to check UserExistence
			if (getClient(emailId) != null) {
				// If Exists then send to login password with username
				// TODO::: in login.jsp check for email id in the request if it
				// is available set this email id in the email id field of login
				// page
				// redirectExternal(req, resp, LOGIN_URL);
				Client client = getClient(emailId);
				if (client.isDeleted()) {
					//client.setActive(true);
					client.setUsers(new HashSet<User>());
					client.setEmailId(emailId);
					client.setFirstName(firstName);
					client.setLastName(lastName);
					client.setFullName(Global.get().messages()
							.fullName(firstName, lastName));
					client.setPassword(passwordWithHash);
					client.setPasswordRecoveryKey(EU.encryptPassword(password));
					client.setPhoneNo("");
					client.setCountry(country);
					client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);
					ClientSubscription clientSubscription = new ClientSubscription();
					clientSubscription.setCreatedDate(new Date());
					clientSubscription.setLastModified(new Date());
					clientSubscription.setSubscription(Subscription
							.getInstance(Subscription.PREMIUM_USER));
					clientSubscription
							.setDurationType(ClientSubscription.UNLIMITED_USERS);
					saveEntry(clientSubscription);

					client.setClientSubscription(clientSubscription);
					client.setDeleted(false);
					client.setActive(true);
					saveEntry(client);
					//session.setAttribute(EMAIL_ID, emailId);
					
					sendActivationEmail(createActivation(emailId), client); // Send to SignUp Success
					String message = "?message=" + ACT_FROM_SIGNUP;
					
//					String paramDest = (String) session
//							.getAttribute(PARAM_DESTINATION);
//					if (paramDest != null) {
//						redirectExternal(req, resp, paramDest);
//						session.removeAttribute(paramDest);
//					} else {
						//redirectExternal(req, resp, COMPANIES_URL);
						redirectExternal(req, resp, ACTIVATION_URL + message);
//					}
					transaction.commit();

					// SEND WELCOME MAIL TO SIGNUP USER
					sendWelComeMail(firstName, emailId);
				} else {
					req.setAttribute("errormessage", Global.get().messages()
							.alreadyRegisteredWithAccounter()
							+ " <a href=\"/main/login\">"
							+ Global.get().messages().here()
							+ "</a> "
							+ Global.get().messages().toLogin());
					dispatch(req, resp, view);
					return;

				}
			} else {
				// else

				// Create Client and Save
				Client client = new Client();
				//client.setActive(true);
				client.setUsers(new HashSet<User>());
				client.setEmailId(emailId);
				client.setFirstName(firstName);
				client.setLastName(lastName);
				client.setFullName(firstName + " " + lastName);
				client.setPassword(passwordWithHash);
				client.setPasswordRecoveryKey(EU.encryptPassword(password));
				client.setCountry(country);
				client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);

				// clientSubscription.setCreatedDate(new Date(System
				// .currentTimeMillis()));
				// Set<String> members = new HashSet<String>();
				// members.add(emailId);
				// clientSubscription.setMembers(members);
				// Subscription subscription = new Subscription();
				// subscription.setName("");
				// Set<String> features = new HashSet<String>();
				// subscription.setFeatures(features);
				// clientSubscription.setSubscription(subscription);
				client.setCreatedDate(new FinanceDate());
				ClientSubscription clientSubscription = new ClientSubscription();
				clientSubscription.setCreatedDate(new Date());
				clientSubscription.setLastModified(new Date());
				clientSubscription
						.setPremiumType(ClientSubscription.UNLIMITED_USERS);
				// if (!ServerConfiguration.isDesktopApp()) {
				// clientSubscription.setSubscription(Subscription
				// .getInstance(Subscription.FREE_CLIENT));
				// } else {
				clientSubscription.setSubscription(Subscription
						.getInstance(Subscription.PREMIUM_USER));
				// }
				saveEntry(clientSubscription);
				client.setClientSubscription(clientSubscription);

				client.setDeleted(false);
				saveEntry(client);
				//session.setAttribute(EMAIL_ID, emailId);

				// SEND WELCOME MAIL TO SIGNUP USER
				sendWelComeMail(firstName, emailId);
				
				sendActivationEmail(createActivation(emailId), client); // Send to SignUp Success
				String message = "?message=" + ACT_FROM_SIGNUP;

				// Email to that user.
				/*
				 * sendActivationEmail(token, client); // Send to SignUp Success
				 * View String message = "?message=" + ACT_FROM_SIGNUP;
				 * 
				 * redirectExternal(req, resp, ACTIVATION_URL + message);
				 */
//				String paramDest = (String) session
//						.getAttribute(PARAM_DESTINATION);
//				if (paramDest != null) {
//					redirectExternal(req, resp, paramDest);
//					session.removeAttribute(paramDest);
//				} else {
					//redirectExternal(req, resp, COMPANIES_URL);
					redirectExternal(req, resp, ACTIVATION_URL + message);
//				}
				transaction.commit();
				try {
					NewLoginServlet.createD2(req, emailId, password);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		}

		return;
	}

}
