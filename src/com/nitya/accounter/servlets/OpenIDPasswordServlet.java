package com.nitya.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.utils.HexUtil;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.utils.Security;
import com.nitya.accounter.web.client.Global;

public class OpenIDPasswordServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8974718380835687273L;
	private static final String VIEW = "/WEB-INF/openidpassword.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			dispatch(req, resp, VIEW);
		} else {
			resp.sendRedirect(LOGIN_URL);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			String emailId = (String) session.getAttribute(EMAIL_ID);
			String password = req.getParameter("password");
			Client client = getClient(emailId);
			if (client != null) {
				String passwordHash = HexUtil.bytesToHex(Security
						.makeHash(emailId + password.trim()));
				String passwordWord = HexUtil.bytesToHex(Security
						.makeHash(emailId + Client.PASSWORD_HASH_STRING
								+ password.trim()));
				if (client.getPassword().equals(passwordHash)) {
					client.setPassword(passwordWord);
					client.setPasswordRecoveryKey(EU.encryptPassword(password
							.trim()));
					HibernateUtil.getCurrentSession().saveOrUpdate(client);
				}
				if (!client.getPassword().equals(passwordWord)) {
					req.setAttribute("errormessage", Global.get().messages()
							.youHaveEnteredWrongPassword());
					dispatch(req, resp, VIEW);
					return;
				}
				try {
					NewLoginServlet.createD2(req, emailId, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				redirectExternal(req, resp, LOGIN_URL);
				return;
			}
		}
		resp.sendRedirect(LOGIN_URL);
	}

}
