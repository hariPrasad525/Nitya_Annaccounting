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
import com.nitya.accounter.web.client.Global;

public class ForgetPasswordServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String view = "/WEB-INF/forgotpassword.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String header2 = req.getHeader("User-Agent");
		boolean contains = header2.contains("iPad");
		if (contains) {
			req.setAttribute("ipad", contains);
		}

		HttpSession session = req.getSession();
		Object attribute = session.getAttribute(EMAIL_ID);
		session.removeAttribute(EMAIL_ID);
		if (attribute == null) {
			attribute = "";
		}
		req.setAttribute(EMAIL_ID, attribute);
		dispatch(req, resp, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param getting from httpservlet request object are emailId and company
	 * name form url.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String header2 = req.getHeader("User-Agent");
		boolean contains = header2.contains("iPad");
		if (contains) {
			req.setAttribute("ipad", contains);
		}

		String emailID = req.getParameter("emailId");
		if (emailID == null) {
			req.setAttribute("errorMessage", Global.get().messages()
					.pleaseEnterValidEmailId());
			dispatch(req, resp, view);
			return;
		}
		emailID = emailID.toLowerCase().trim();

		Session serverSession = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = serverSession.beginTransaction();
			Client client = getClient(emailID);

			if (client == null || client.isDeleted()) {
				req.setAttribute("emailId", emailID);
				req.setAttribute("errorMessage", Global.get().messages()
						.pleaseEnterValidEmailId());
				dispatch(req, resp, view);
				return;
			}

			Activation activation = getActivationByEmailId(emailID);
			String token = null;
			if (activation == null) {
				token = createActivation(emailID);
			} else {
				token = activation.getToken();
			}

			sendForgetPasswordLinkToUser(client, token);

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		}

		// String successMessage =
		// "Reset Password link has been sent to the given emailId, Kindly check your Mail box.";
		// req.setAttribute("successmessage", successMessage);
		String message = "?message=" + ACT_FROM_RESET;
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
