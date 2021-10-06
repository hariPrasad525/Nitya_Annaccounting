package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.encryption.Encrypter;
import com.vimukti.accounter.utils.HibernateUtil;

public class DecryptCompanyServlet extends HttpServlet {
	private static final String VIEW = "/WEB-INF/decryptcompany.jsp";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp);
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher(VIEW).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String companyName = req.getParameter("companyName");
		String password = req.getParameter("password");
		Session session = HibernateUtil.openSession();
		try {
			Object[] obj = getCompanyIdAndSecret(companyName);// companyId,companySecret,createdById
			if (obj == null) {
				req.setAttribute("message", " wrong company name '"
						+ companyName + "'");
				dispatch(req, resp);
				return;
			}
			long companyId = (Long) obj[0];
			byte[] secret = (byte[]) obj[1];
			if (secret == null) {
				req.setAttribute("message", "Company '" + companyName
						+ "' is not yet encrypted.");
				dispatch(req, resp);
				return;
			}
			Company company = (Company) session.get(Company.class, companyId);
			company.setLocked(true);
			Transaction beginTransaction = session.beginTransaction();
			session.saveOrUpdate(company);
			new Encrypter(companyId, secret, password, (Long) obj[2]).start();
			beginTransaction.commit();
			req.setAttribute("message", companyName + " is decrypting...");
		} catch (Exception e) {
			req.setAttribute("message", companyName
					+ " decryption is failed try again.");
		} finally {
			if (session.isOpen()) {
				session.close();
			}
			dispatch(req, resp);
		}
	}

	private Object[] getCompanyIdAndSecret(String companyName) {
		Session session = HibernateUtil.getCurrentSession();
		List<Object[]> res = session
				.getNamedQuery("get.companyId.secret.createdUserId")
				.setString("companyName", companyName).list();
		return res.size() == 0 ? null : res.get(0);
	}
}
