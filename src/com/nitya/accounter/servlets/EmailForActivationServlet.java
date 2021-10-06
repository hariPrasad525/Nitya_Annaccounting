package com.nitya.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Activation;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.utils.HibernateUtil;

public class EmailForActivationServlet extends BaseServlet {
	private static final String VIEW = "/WEB-INF/emailforactivation.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp, VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter("emailid");

		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			// Getting activation object using the mail id
			Activation activation = getActivationByEmailId(email);

			String token = null;
			Client client = getClient(email);

			if (activation == null) {
				if (client == null) {
					// send error message
					req.setAttribute(
							"errormessage",
							"Invalid email id, please enter the email id that you used during sign up process.");
					dispatch(req, resp, VIEW);
					return;
				}
				token = createActivation(email);
			} else {
				token = activation.getToken();
			}
			HttpSession httpSession = req.getSession();
			String attribute = (String) httpSession
					.getAttribute(ACTIVATION_TYPE);
			if (attribute != null && attribute.equals("resetpassword")) {
				// Sending ResetPassword Link to User
				sendForgetPasswordLinkToUser(client, token);
			} else {
				// Email to that user.
				sendActivationEmail(token, client);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} 

		// redirect to activation page
		String message = "?message="+ACT_FROM_RESEND;
		redirectExternal(req, resp, ACTIVATION_URL + message);

	}

	private Activation getActivationByEmailId(String email) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			Query query = session.getNamedQuery("get.activation.by.emailid");
			query.setParameter("emailId", email);
			Activation val = (Activation) query.uniqueResult();
			return val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
