package com.vimukti.accounter.developer.api.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gdata.util.common.util.Base64;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.developer.api.core.ApiProcessor;
import com.vimukti.accounter.encryption.Encrypter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.Features;

public class EncryptCompanyProcessor extends ApiProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.ENCRYPTION);
		String password = req.getParameter("password");
		String hint = req.getParameter("hint");
		if (password == null || password.isEmpty()) {
			sendFail("Company password is null or emptry");
			return;
		}

		String companyId = req.getParameter("CompanyId");
		Session session = HibernateUtil.openSession();
		try {
			String emailId = (String) req.getAttribute("emailId");
			Client client = getClient(emailId);
			if (!client.getClientSubscription().isPaidUser()
					|| !client.getClientSubscription().getSubscription()
							.getFeatures().contains(Features.ENCRYPTION)) {
				sendFail("You don't have permission to encrypt this company");
				return;
			}

			Query namedQuery = session.getNamedQuery("getCompanySecret");
			namedQuery.setParameter("companyId", Long.parseLong(companyId));
			byte[] secret = (byte[]) namedQuery.uniqueResult();
			if (secret != null) {
				sendFail("Company already encrypted");
				return;
			}
			String authentication = req.getParameter("authentication");
			byte[] d2 = Base64.decode(authentication);

			EU.removeCipher();
			Company company = (Company) session.get(Company.class,
					Long.parseLong(companyId));
			if (!company.getCreatedBy().getClient().getEmailId()
					.equals(emailId)) {
				sendFail("Your not a creater of this company");
				return;
			}

			company.setPasswordHInt(hint);
			company.setLocked(true);
			Transaction beginTransaction = session.beginTransaction();
			session.saveOrUpdate(company);
			try {
				new Encrypter(company.getId(), password, d2, emailId,
						req.getParameter("ApiKey")).start();
			} catch (Exception e) {
				company.setLocked(false);
			}
			beginTransaction.commit();
			sendResult(company.isLocked());
		} catch (Exception e) {
			e.printStackTrace();
			sendFail(e.getMessage());
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}
}
