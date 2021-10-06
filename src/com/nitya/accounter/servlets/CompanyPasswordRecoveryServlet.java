package com.nitya.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.encryption.Encrypter;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.utils.SecureUtils;

public class CompanyPasswordRecoveryServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(
				BaseServlet.EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(BaseServlet.LOGIN_URL);
			return;
		}
		Long companyId = (Long) req.getSession().getAttribute(
				BaseServlet.COMPANY_ID);
		if (companyId == null) {
			resp.sendRedirect(BaseServlet.COMPANIES_URL);
			return;
		}
		dispatch(req, resp);
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/companypasswordrecovery.jsp")
				.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String recoveryKey = req.getParameter("recoveryKey");
		if (recoveryKey == null) {
			req.setAttribute("info", "wrong recovery key");
			dispatch(req, resp);
			return;
		}

		String password = req.getParameter("password");
		if (password == null) {
			req.setAttribute("info", "Enter password");
			dispatch(req, resp);
			return;
		}

		String confirm = req.getParameter("confirm");
		if (!password.equals(confirm)) {
			req.setAttribute("info", "Passwords are not matched.");
			dispatch(req, resp);
			return;
		}
		String emailId = (String) req.getSession().getAttribute(
				BaseServlet.EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(BaseServlet.LOGIN_URL);
			return;
		}
		Long companyId = (Long) req.getSession().getAttribute(
				BaseServlet.COMPANY_ID);
		if (companyId == null) {
			resp.sendRedirect(BaseServlet.COMPANIES_URL);
			return;
		}
		Session openSession = HibernateUtil.getCurrentSession();
		try {

			Company company = (Company) openSession.load(Company.class,
					companyId);
			if (company == null) {
				resp.sendRedirect(BaseServlet.COMPANIES_URL);
				return;
			}
			if (!company.getContactSupport()) {
				req.setAttribute("info", "Contact accounter support team");
				dispatch(req, resp);
				return;
			}
			byte[] prk = EU.generatePBS(recoveryKey);
			byte[] ep = company.getEncryptedPassword();
			try {
				byte[] decrypt = EU.decrypt(ep, prk);
				byte[] secretKey = company.getSecretKey();
				byte[] key = EU.decrypt(secretKey, decrypt);

				byte[] psk = EU.generatePBS(password);
				byte[] newSecretKey = EU.encrypt(key, psk);
				company.setSecretKey(newSecretKey);

				String recocery = SecureUtils.createID(16);
				byte[] rck = EU.generatePBS(recocery);
				byte[] newRecoveyKey = EU.encrypt(psk, rck);
				company.setEncryptedPassword(newRecoveyKey);
				Encrypter.sendCompanyPasswordRecoveryKey(emailId, recocery);
				company.setContactSupport(false);
				Transaction transaction = openSession.beginTransaction();
				openSession.saveOrUpdate(company);
				transaction.commit();
			} catch (Exception e) {
				req.setAttribute("info", "Wrong Recovery Key");
				dispatch(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
