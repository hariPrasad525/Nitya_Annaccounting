package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.google.gdata.util.common.util.Base64;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.developer.api.core.ApiResult;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;

public class ApiLoginServlet extends ApiBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String password = req.getParameter("password");
		if (password == null) {
			sendFail(req, resp, "Password parameter must present");
		}
		String emailId = (String) req.getAttribute("emailId");
		String passwordWord = HexUtil.bytesToHex(Security.makeHash(emailId
				+ Client.PASSWORD_HASH_STRING + password.trim()));
		Client client = getClient(emailId, passwordWord);
		if (client == null) {
			sendFail(req, resp, "Wrong password");
		}
		String encode = null;
		try {
			byte[] d2 = EU.generateD2(password, emailId,
					req.getParameter("ApiKey"));
			encode = Base64.encode(d2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (encode == null) {
			sendFail(req, resp, "Unable to encrypt your password");
		}

		Set<ApiCompany> companyIds = getCompanyList(client.getUsers());
		ApiResult result = new ApiResult();
		ClientDetails details = new ClientDetails(encode, companyIds);

		ClientSubscription subscription = client.getClientSubscription();
		details.setCreatedDate(subscription.getCreatedDate());
		details.setDurationType(subscription.getDurationType());
		details.setExpiredDate(subscription.getExpiredDate());
		details.setFeatures(subscription.getSubscription().getFeatures());
		details.setGracePeriodDate(subscription.getGracePeriodDate());
		details.setLastModified(subscription.getLastModified());
		details.setMembers(subscription.getMembers());
		details.setPremiumType(subscription.getPremiumType());
		details.setType(subscription.getSubscription().getType());

		result.setResult(details);
		result.setStatus(ApiResult.SUCCESS);
		sendData(req, resp, result);
	}

	private Set<ApiCompany> getCompanyList(Set<User> users) {
		Set<ApiCompany> companies = new HashSet<ApiCompany>();
		List<Long> userIds = new ArrayList<Long>();
		for (User user : users) {
			if (!user.isDeleted()) {
				userIds.add(user.getID());
			}
		}
		List<Object[]> objects = new ArrayList<Object[]>();
		if (!userIds.isEmpty()) {
			Session session = HibernateUtil.getCurrentSession();
			objects = session.getNamedQuery("get.ApiCompany.details")
					.setParameterList("userIds", userIds).list();
			for (Object[] obj : objects) {
				ApiCompany company = new ApiCompany();
				company.setId((Long) obj[0]);
				company.setName((String) obj[1]);
				company.setCountryName((String) obj[2]);
				company.setEncrypted((Boolean) obj[3]);
				companies.add(company);
			}
		}
		return companies;
	}

	private Client getClient(String emailId, String password) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery("getclient.from.central.db.using.emailid.and.password");
		query.setParameter("emailId", emailId);
		query.setParameter("password", password);
		Client client = (Client) query.uniqueResult();
		return client;
	}
}