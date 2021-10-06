package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.User;
import com.nitya.accounter.encryption.Encrypter;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.Features;

public class EncryptCompaniesServlet extends BaseServlet {

	private static final String ENCRYPT_VIEW = "/WEB-INF/encryptcompany.jsp";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			String emailId = (String) session.getAttribute(EMAIL_ID);
			Client client = getClient(emailId);
			if (client == null) {
				resp.sendRedirect(LOGIN_URL);
				return;
			}
			if (!client.getClientSubscription().isPaidUser()
					|| !client.getClientSubscription().getSubscription()
							.getFeatures().contains(Features.ENCRYPTION)) {
				resp.sendRedirect(COMPANIES_URL);
				return;
			}
			List<Object[]> list = getNonEncryptedCompanies(client);
			req.setAttribute("companeyList", list);
			dispatch(req, resp, ENCRYPT_VIEW);
		} else {
			resp.sendRedirect(LOGIN_URL);
		}
	}

	private List<Object[]> getNonEncryptedCompanies(Client client) {
		Session currentSession = HibernateUtil.getCurrentSession();
		List<Long> userIds = new ArrayList<Long>();
		for (User user : client.getUsers()) {
			if (!user.isDeleted()) {
				userIds.add(user.getID());
			}
		}
		if (!userIds.isEmpty()) {
			List<Object[]> list = currentSession
					.getNamedQuery("get.NonEncrypted.CompanyNames.by.client")
					.setParameterList("userIds", userIds).list();
			return list;
		}
		return new ArrayList<Object[]>();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			String emailId = (String) session.getAttribute(EMAIL_ID);
			String companyId = req.getParameter("companyname");
			Client client = getClient(emailId);
			if (!client.getClientSubscription().isPaidUser()
					|| !client.getClientSubscription().getSubscription()
							.getFeatures().contains(Features.ENCRYPTION)) {
				resp.sendRedirect(COMPANIES_URL);
				return;
			}
			List<Object[]> nonEncryptedCompanies = getNonEncryptedCompanies(client);
			boolean isContain = false;
			long comId = Long.parseLong(companyId);
			for (Object[] obj : nonEncryptedCompanies) {
				if ((Long) obj[0] == comId) {
					isContain = true;
					break;
				}
			}
			if (!isContain) {
				resp.sendRedirect(COMPANIES_URL);
				return;
			}
			byte[] d2 = getD2(req);
			if (d2 == null) {
				resp.sendRedirect(LOGIN_URL);
				return;
			}

			Session session2 = HibernateUtil.getCurrentSession();
			EU.removeCipher();
			Company company = (Company) session2.get(Company.class,
					Long.parseLong(companyId));
			if (company != null) {
				User user = company.getUserByUserEmail(emailId);
				if (user == null) {
					dispatch(req, resp, ENCRYPT_VIEW);
					return;
				}
				String password = req.getParameter("password");
				String passwordHint = req.getParameter("hint");
				company.setPasswordHInt(passwordHint);
				company.setLocked(true);
				Session currentSession = HibernateUtil.getCurrentSession();
				Transaction beginTransaction = currentSession
						.beginTransaction();
				currentSession.saveOrUpdate(company);
				session.setAttribute(LOCAK_REASON_TYPE,
						LOCK_REASON_TYPE_ENCRYPTION);
				try {
					new Encrypter(company.getId(), password, d2, emailId,
							session.getId()).start();
				} catch (Exception e) {
					session.removeAttribute(LOCAK_REASON_TYPE);
					company.setLocked(false);
				}
				beginTransaction.commit();
			} else {
				dispatch(req, resp, ENCRYPT_VIEW);
				return;
			}
		}
		resp.sendRedirect(LOGIN_URL);
	}
}
