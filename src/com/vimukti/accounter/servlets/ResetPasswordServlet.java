package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.Global;

public class ResetPasswordServlet extends BaseServlet {

	protected static final Log LOG = LogFactory
			.getLog(ResetPasswordServlet.class);

	private static final long serialVersionUID = 1L;

	String view = "/WEB-INF/resetpassword.jsp";
	String tokenIdCompany;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		checkSession(req, resp);
		String parameter = req.getParameter("type");
		req.setAttribute("type", parameter);
		dispatch(req, resp, view);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.info(req);
		HttpSession httpsession = req.getSession();
		if (httpsession.getAttribute(EMAIL_ID) == null) {
			req.setAttribute(ATTR_MESSAGE, "Session expired.");
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}
		// get activation record from table
		Session hibernateSession = HibernateUtil.getCurrentSession();
		// Activation activation = null;
		// // get token from session
		// String token = (String) httpsession.getAttribute(ACTIVATION_TOKEN);
		try {
			// if (token == null) {
			// String emailId = (String) httpsession.getAttribute(EMAIL_ID);
			// if (emailId != null) {
			// activation = (Activation) hibernateSession
			// .getNamedQuery("get.activation.by.mailId")
			// .setParameter(EMAIL_ID, emailId).uniqueResult();
			// token = activation.getToken();
			// }
			// } else if (token.isEmpty()) {
			// req.setAttribute(ATTR_MESSAGE, "Activation expired.");
			// redirectExternal(req, resp, LOGIN_URL);
			// return;
			// }
			//
			// activation = getActivation(token);
			// // if it is null
			// if (activation == null) {
			// // dispatch withr "Token has expired".
			// req.setAttribute(ATTR_MESSAGE, "Activation expired.");
			// redirectExternal(req, resp, LOGIN_URL);
			// return;
			// }
			// otherwise
			// getPasswords from request
			String password = req.getParameter("newPassword");
			String confirm = req.getParameter("confirmPassword");

			if (password.isEmpty() || confirm.isEmpty()) {
				dispatchMessage(Global.get().messages()
						.youHaveEnteredWrongPassword(), req, resp, view);
				return;
			}
			// compare if not equal send error message
			// otherwise
			if (!password.equals(confirm)) {
				dispatchMessage(Global.get().messages().passwordMismatch(),
						req, resp, view);
				return;
			}

			// getClient record with activation emailId
			String emailId = (String) httpsession.getAttribute(EMAIL_ID);
			Client client = getClient(emailId);

			// update password and set isActive true
			client.setPassword(HexUtil.bytesToHex(Security.makeHash(emailId
					+ Client.PASSWORD_HASH_STRING + password.trim())));
			client.setPasswordRecoveryKey(EU.encryptPassword(password.trim()));
			// set isActive true
			// client.setActive(true);
			// make Require Password Reset False
			client.setRequirePasswordReset(false);
			Transaction transaction = hibernateSession.beginTransaction();
			try {
				// and save Client,
				saveEntry(client);
				Query query = hibernateSession.getNamedQuery("setSecretNull");
				query.setParameter("emailId", emailId);
				query.executeUpdate();
				transaction.commit();
				NewLoginServlet.createD2(req, emailId, password);
			} catch (Exception e) {
				e.printStackTrace();
				transaction.rollback();
			}

			// transaction = hibernateSession.beginTransaction();
			// // delete activation record
			// hibernateSession.getNamedQuery("delete.activation.by.Id")
			// .setLong("id", activation.getID()).executeUpdate();
			// transaction.commit();
			// Send to login page with emailId
			// String activationType = (String) httpsession
			// .getAttribute(ACTIVATION_TYPE);
			// if (activationType != null
			// && activationType.equals("resetpassword")) {
			// httpsession.removeAttribute(ACTIVATION_TYPE);
			// }
			httpsession.setAttribute(EMAIL_ID, emailId);
			// NewLoginServlet.createD2(req, emailId, password);
			redirectExternal(req, resp, LOGIN_URL);
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

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void checkSession(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession httpSession = req.getSession();
		if (httpSession.getAttribute(EMAIL_ID) == null) {
			String destination = req.getParameter(PARAM_DESTINATION);
			if (destination == null) {
				redirectExternal(req, resp, LOGIN_URL);
			} else {
				redirectExternal(req, resp, destination);
			}
		}
	}

}
