package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class EmailActivateServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String view = "/WEB-INF/emailactivate.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		dispatch(req, resp, view);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();

		String validIP = ServerConfiguration.getValidIP();
		if (!req.getRemoteAddr().equals(validIP)) {
			redirectExternal(req, resp, "/main/emailActivation");
			return;
		}
		String email = req.getParameter("email").trim();

		String emailID = email.toLowerCase();
		Session hbSession = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = hbSession.beginTransaction();
			Client client = (Client) hbSession
					.getNamedQuery("getClient.by.mailId")
					.setParameter(EMAIL_ID, emailID).uniqueResult();
			client.setActive(true);
			saveEntry(client);

			// delete activation object

			Query query = hbSession
					.getNamedQuery("delete.activation.by.emailId");
			query.setParameter("emailId", emailID.trim());
			int updatedRows = query.executeUpdate();
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} 

		redirectExternal(req, resp, "/main/emailActivation");

	}

}
