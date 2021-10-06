package com.nitya.accounter.developer.api.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gdata.util.common.util.Base64;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.User;
import com.nitya.accounter.developer.api.core.ApiProcessor;
import com.nitya.accounter.utils.HibernateUtil;

public class CreateUserSecretProcessor extends ApiProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String password = req.getParameter("password");
		if (password == null || password.isEmpty()) {
			sendFail("Company password is null or emptry");
			return;
		}
		String companyId = req.getParameter("CompanyId");
		Session session = HibernateUtil.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			Query namedQuery = session.getNamedQuery("getCompanySecret");
			namedQuery.setParameter("companyId", Long.parseLong(companyId));
			byte[] secret = (byte[]) namedQuery.uniqueResult();
			if (secret == null) {
				sendFail("Company is not encrypted");
				return;
			}
			String emailId = (String) req.getAttribute("emailId");
			User user = getUser(emailId, Long.parseLong(companyId));
			if (user == null) {
				sendFail("Wrong company id");
				return;
			}
			String authentication = req.getParameter("authentication");
			byte[] d2 = Base64.decode(authentication);
			byte[] pbs = EU.generatePBS(password);
			byte[] key = null;
			try {
				key = EU.decrypt(secret, pbs);
			} catch (Exception e) {
				sendFail("Wrong company password");
				return;
			}
			byte[] s2 = EU.getKey(req.getParameter("ApiKey"));
			byte[] psk = EU.decrypt(d2, s2);
			byte[] userSecret = EU.encrypt(key, psk);
			user.setSecretKey(userSecret);
			session.saveOrUpdate(user);
			transaction.commit();
			sendResult(true);
		} catch (Exception e) {
			e.printStackTrace();
			sendFail(e.getMessage());
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	private User getUser(String emailId, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getUser.by.mailId.and.companyId")
				.setParameter("emailId", emailId)
				.setParameter("companyId", companyId);
		return (User) query.uniqueResult();
	}
}
