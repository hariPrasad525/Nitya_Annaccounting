package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientSubscription;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Subscription;
import com.nitya.accounter.core.User;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;

public class SignupOpenIdServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/OpenIdSignup.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		dispatch(req, resp, view);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Take userName from request
		String emailId = req.getParameter("emailId").trim();
		String firstName = req.getParameter("firstName").trim();
		String lastName = req.getParameter("lastName").trim();
		// String password = req.getParameter("password").trim();
		String phoneNumber = req.getParameter("phoneNumber").trim();
		String country = req.getParameter("country");
		if (country != null) {
			country = country.trim();
		}
		boolean isSubscribedToNewsLetter = false;
		if (req.getParameter("newsletter") != null
				&& req.getParameter("newsletter").equals("on"))
			isSubscribedToNewsLetter = true;

		boolean isAgreed = false;
		if (req.getParameter("agree") != null
				&& req.getParameter("agree").equals("on"))
			isAgreed = true;
		if (!isAgreed) {
			dispatchMessage(Global.get().messages().pleaseacceptTermsofuse(),
					req, resp, view);
		}

		if (!isValidInputs(NAME, firstName, lastName, country)
				|| !isValidInputs(MAIL_ID, emailId)) {
			dispatchMessage(Global.get().messages().incorrectEmailOrPassWord(),
					req, resp, view);
			return;
		}
		emailId = emailId.toLowerCase();
		// String passwordWithHash =
		// HexUtil.bytesToHex(Security.makeHash(emailId
		// + password));

		Session hibernateSession = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = hibernateSession.beginTransaction();
			// Have to check UserExistence
			Client client = getClient(emailId);
			if (client != null) {
				// If Exists then send to login password with username
				// TODO::: in login.jsp check for email id in the request if it
				// is available set this email id in the email id field of login
				// page
				// HttpSession session = req.getSession(true);
				// session.setAttribute(EMAIL_ID, emailId);
				// redirectExternal(req, resp, LOGIN_URL);

				if (client.isDeleted()) {
					client.setActive(true);
					client.setUsers(new HashSet<User>());
					client.setEmailId(emailId);
					client.setFirstName(firstName);
					client.setLastName(lastName);
					client.setFullName(Global.get().messages()
							.fullName(firstName, lastName));
					client.setPhoneNo(phoneNumber);
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
					client.setLoginCount(0);
					client.setDeleted(false);
					client.setPassword(null);
					client.setPasswordRecoveryKey(null);
					client.setCreatedDate(new FinanceDate());
					saveEntry(client);
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
				// Generate Token and create Activation and save. then send
				// String token = createActivation(emailId);

				// Create Client and Save
				client = new Client();
				client.setActive(true);
				client.setUsers(new HashSet<User>());
				client.setEmailId(emailId);
				client.setFirstName(firstName);
				client.setLastName(lastName);
				client.setFullName(Global.get().messages()
						.fullName(firstName, lastName));
				// client.setPassword(passwordWithHash);
				client.setPhoneNo(phoneNumber);
				client.setCountry(country);
				client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);

				ClientSubscription clientSubscription = new ClientSubscription();
				clientSubscription.setCreatedDate(new Date());
				clientSubscription.setSubscription(Subscription
						.getInstance(Subscription.PREMIUM_USER));
				clientSubscription.setLastModified(new Date());
				clientSubscription
						.setPremiumType(ClientSubscription.UNLIMITED_USERS);
				saveEntry(clientSubscription);
				client.setClientSubscription(clientSubscription);

			}
			// Email to that user.
			// sendActivationEmail(token, client);
			// Send to SignUp Success View
			String destUrl = req.getParameter(PARAM_DESTINATION);
			HttpSession httpSession = req.getSession();
			httpSession.setAttribute(EMAIL_ID, client.getEmailId());
			if (destUrl == null || destUrl.isEmpty()) {
				client.setLoginCount(client.getLoginCount() + 1);
				client.setLastLoginTime(System.currentTimeMillis());
				hibernateSession.saveOrUpdate(client);
				redirectExternal(req, resp, COMPANIES_URL);
			} else {
				redirectExternal(req, resp, destUrl);
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		}

		return;
	}
}
