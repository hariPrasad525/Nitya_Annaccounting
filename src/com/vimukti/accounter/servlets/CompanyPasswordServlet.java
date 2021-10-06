package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;

public class CompanyPasswordServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/companypassword.jsp";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String password = (String) req.getParameter("companyPassword");
		Long companyId = (Long) req.getSession().getAttribute(COMPANY_ID);
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		if (companyId == null) {
			resp.sendRedirect(COMPANIES_URL);
			return;
		}
		byte[] d2 = getD2(req);
		if (d2 == null) {
			req.setAttribute("error", "Please login and logout.");
			setHintForRequest(req, companyId);
			dispatch(req, resp, VIEW);
		}
		if (password != null) {
			byte[] csk = EU.generatePBS(password);
			byte[] comSecret = getCompanySecretFromDB(companyId);
			try {
				byte[] s3 = null;
				try {
					s3 = EU.decrypt(comSecret, csk);
				} catch (Exception e) {
					e.printStackTrace();
					req.setAttribute("error", Global.get().messages()
							.youHaveEnteredWrongPassword());
					setHintForRequest(req, companyId);
					dispatch(req, resp, VIEW);
					return;
				}
				byte[] s2 = EU.getKey(req.getSession().getId());
				byte[] userSecret = EU.encrypt(s3, EU.decrypt(d2, s2));
				User user = getUser(emailId, companyId);
				user.setSecretKey(userSecret);
				Session session = HibernateUtil.getCurrentSession();
				Transaction beginTransaction = session.beginTransaction();
				session.saveOrUpdate(user);
				beginTransaction.commit();
				resp.sendRedirect(ServerConfiguration.isDebugMode ? ACCOUNTER_URL
						: ACCOUNTER_OLD_URL);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				req.setAttribute("error", Global.get().messages()
						.youHaveEnteredWrongPassword());
				setHintForRequest(req, companyId);
				dispatch(req, resp, VIEW);
			}
		} else {
			req.setAttribute(
					"error",
					Global.get()
							.messages()
							.pleaseEnter(
									Global.get().messages().companyPassword()));
			setHintForRequest(req, companyId);
			dispatch(req, resp, VIEW);
		}
	}

	private void setHintForRequest(HttpServletRequest req, Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction beginTransaction = session.beginTransaction();
		Query query = session.getNamedQuery("getHint.by.company");
		query.setParameter("companyId", companyId);
		String hint = (String) query.uniqueResult();
		req.setAttribute("hint", hint);
		beginTransaction.commit();

	}
}
