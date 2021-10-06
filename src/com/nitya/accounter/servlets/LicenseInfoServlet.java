package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.License;
import com.nitya.accounter.core.Property;
import com.nitya.accounter.license.LicenseManager;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;

public class LicenseInfoServlet extends BaseServlet {
	public static final String PAGE = "/WEB-INF/licenseInfo.jsp";
	AccounterMessages messages = Global.get().messages();
	AccounterMessages2 messages2 = Global.get().messages2();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = (String) req.getSession().getAttribute(EMAIL_ID);
		if (email == null) {
			redirectExternal(req, resp, LOGIN_URL
					+ "?destination=/main/licenseInfo");
			return;
		}
		Client client = getClient(email);
		if (client == null) {
			redirectExternal(req, resp, LOGIN_URL
					+ "?destination=/main/licenseInfo");
			return;
		}

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getActiveLicenseOfClient")
				.setEntity("client", client);
		License license = (License) query.uniqueResult();

		req.setAttribute("license", license);
		dispatch(req, resp, PAGE);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String email = (String) req.getSession().getAttribute(EMAIL_ID);
		if (email == null) {
			redirectExternal(req, resp, LOGIN_URL
					+ "?destination=/main/licenseInfo");
			return;
		}
		Client client = getClient(email);
		if (client == null) {
			redirectExternal(req, resp, LOGIN_URL
					+ "?destination=/main/licenseInfo");
			return;
		}

		String licenseString = (String) req.getParameter("licensekey");
		if (licenseString == null || licenseString.isEmpty()) {
			showError(req, resp, messages.pleaseEnter(messages2.licenseKey()));
			return;
		}
		License license = new LicenseManager().doDecode(licenseString);
		Session session = HibernateUtil.getCurrentSession();
		Property serverIdProp = (Property) session.get(Property.class,
				Property.SERVER_ID);
		if (!license.getServerId().equals(serverIdProp.getValue())) {
			showError(req, resp, messages2.invalidLicense());
			return;
		}

		Transaction transaction = session.beginTransaction();
		try {
			Query query = session.getNamedQuery("getLicense");
			List<License> licenses = query.list();
			for (License l : licenses) {
				l.setActive(false);
				session.saveOrUpdate(l);
			}
			license.setClient(client);
			session.saveOrUpdate(license);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}

		doGet(req, resp);
	}

	private void showError(HttpServletRequest req, HttpServletResponse resp,
			String error) {
		req.setAttribute("errorMsg", error);
		dispatch(req, resp, PAGE);
	}
}
