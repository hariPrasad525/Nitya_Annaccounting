package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HibernateUtil;

public class ActivationServlet extends BaseServlet {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	// private static final String VIEW = "/WEB-INF/resetpassword.jsp";
	private static final String VIEW = "/WEB-INF/activation.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession(true);

		String destUrl = req.getParameter(PARAM_DESTINATION);
		String emailId = (String) session.getAttribute(EMAIL_ID);
		String parameter = req.getParameter("message");
		if (emailId != null && parameter == null) {
			Session hbSession = HibernateUtil.getCurrentSession();
			Client client = (Client) hbSession
					.getNamedQuery("getClient.by.mailId")
					.setParameter(EMAIL_ID, emailId).uniqueResult();
			if (client != null && client.isActive()) {
				if (destUrl == null || destUrl.isEmpty()) {
					redirectExternal(req, resp, COMPANIES_URL);
				} else {
					redirectExternal(req, resp, destUrl);
				}
				return;
			}
		}

		if (parameter != null) {
			if (parameter.equals(ACT_FROM_SIGNUP)) {
				req.setAttribute(
						"successmessage",
						"Thanks for registering with Anna Accounting!<br>To complete the sign up process, please check your email and enter your activation code here to activate your Account.");
				session.setAttribute(ACTIVATION_TYPE, "signup");
			} else if (parameter.equals(ACT_FROM_RESET)) {
				req.setAttribute(
						"successmessage",
						"Reset Password code has been sent to the given emailId. Kindly check your inbox and enter that code below to reset the password.");
				session.setAttribute(ACTIVATION_TYPE, "resetpassword");
			} else if (parameter.equals(ACT_FROM_RESEND)) {
				req.setAttribute(
						"successmessage",
						"Your activation code is sent to your mail id. Please check your email and enter your activation code below to activate your account.");
			} else if (parameter.equals(ACT_FROM_LOGIN)) {
				req.setAttribute("successmessage",
						"Your session is inactive. Please login using your email id & password");
			}

		}
		dispatch(req, resp, VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// get the token
		String token = req.getParameter("code");

		if (token == null) {
			dispatch(req, resp, VIEW);
			return;
		}
		token = token.toLowerCase().trim();

		// get activation record
		try {
			Activation activation = getActivation(token);
			// If it is null
			if (activation == null) {
				// set Error "Token has expired"
				// We check him and if invalid code we show him form to enter
				// valid code.
				req.setAttribute(
						"successmessage",
						"Invalid Activation code. Please click <a href='/main/emailforactivation'>here</a> to get new activation code.");
				req.getRequestDispatcher(VIEW).forward(req, resp);
			} else {
				// If code is valid we create the user and set the session and
				// external redirect him to <dest> param or /login
				HttpSession session = req.getSession(true);
				session.setAttribute(ACTIVATION_TOKEN, token);
				session.setAttribute(EMAIL_ID, activation.getEmailId());

				Session hbSession = HibernateUtil.getCurrentSession();
				Transaction transaction = null;
				try {
					transaction = hbSession.beginTransaction();
					Client client = (Client) hbSession
							.getNamedQuery("getClient.by.mailId")
							.setParameter(EMAIL_ID, activation.getEmailId())
							.uniqueResult();
					client.setActive(true);
					saveEntry(client);

					// delete activation object

					Query query = hbSession
							.getNamedQuery("delete.activation.by.emailId");
					query.setParameter("emailId", activation.getEmailId()
							.trim());
					int updatedRows = query.executeUpdate();
					log("No of updated rows = " + updatedRows);
					transaction.commit();
				} catch (Exception e) {
					e.printStackTrace();
					if (transaction != null) {
						transaction.rollback();
					}
				}
				String activationType = (String) session
						.getAttribute(ACTIVATION_TYPE);
				if (activationType != null
						&& activationType.equals("resetpassword")) {

					session.removeAttribute(ACTIVATION_TYPE);
					redirectExternal(req, resp, RESET_PASSWORD_URL);
					return;
				}
				// redirect To ActivationPage.
				// dispatch(req, resp, VIEW);
				String destUrl = req.getParameter(PARAM_DESTINATION);
				if (destUrl == null || destUrl.isEmpty()) {
					redirectExternal(req, resp, LOGIN_URL);
					return;
				} else {
					redirectExternal(req, resp, destUrl);
					return;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Activation getActivation(String token) {
		Session session = HibernateUtil.getCurrentSession();
		if (session != null) {
			Activation val = (Activation) session
					.getNamedQuery("get.activation.by.token")
					.setString("token", token).uniqueResult();
			return val;

		}
		return null;
	}
}
