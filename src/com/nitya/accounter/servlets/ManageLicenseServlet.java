package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.License;
import com.nitya.accounter.core.LicensePurchase;
import com.nitya.accounter.license.LicenseManager;
import com.nitya.accounter.utils.HibernateUtil;

public class ManageLicenseServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String view = "/WEB-INF/LicenseManagement.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(LOGIN_URL + "?destination=" + MANAGE_LICENSE);
			return;
		}

		Client client = getClient(emailId);
		if (client == null) {
			resp.sendRedirect(LOGIN_URL + "?destination=" + MANAGE_LICENSE);
			return;
		}
		String parameter = req.getParameter("gen");
		if (parameter != null) {
			req.setAttribute("gen", parameter);
		}

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getLicense");
		List<License> licenses = (List<License>) query.list();

		req.setAttribute("licenses", licenses);

		req.setAttribute("fullName", client.getFullName());

		if (client.getLicensePurchase() != null) {
			LicensePurchase purchase = client.getLicensePurchase();
			req.setAttribute("purchaseType", purchase.getType());
		}

		dispatch(req, resp, view);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(LOGIN_URL + "?destination=" + MANAGE_LICENSE);
			return;
		}

		Client client = getClient(emailId);
		if (client == null) {
			resp.sendRedirect(LOGIN_URL + "?destination=" + MANAGE_LICENSE);
			return;
		}

		String organisationName = req.getParameter("orgName");
		String serverID = req.getParameter("serverID");
		String licenseTypeStr = req.getParameter("licenseType");
		int licenseType = -1;
		try {
			licenseType = Integer.parseInt(licenseTypeStr);
		} catch (NumberFormatException e) {
		}
		int noOfOusers;
		switch (licenseType) {
		case LicensePurchase.TYPE_TWO_USER:
			noOfOusers = 2;
			break;
		case LicensePurchase.TYPE_FIVE_USER:
			noOfOusers = 5;
			break;
		case LicensePurchase.TYPE_UNLIMITED_USER:
			noOfOusers = -1;

			noOfOusers = 1;
		default:
			noOfOusers = 1;
			break;
		}
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			License license = new License();
			license.setActive(true);
			license.setClient(getClient(emailId));
			license.setServerId(serverID);
			license.setOrganisation(organisationName);
			Calendar instance = Calendar.getInstance();
			if (licenseType == -1) {
				instance.add(Calendar.MONTH, 1);
			} else {
				LicensePurchase licensePurchase = client.getLicensePurchase();
				client.setLicensePurchase(null);
				session.delete(licensePurchase);
				instance.add(Calendar.MONTH, 12);
			}
			license.setExpiresOn(instance.getTime());
			license.setNoOfUsers(noOfOusers);
			license.setPurchasedOn(new Date());

			LicenseManager licenseManager = new LicenseManager();
			license.setLicenseText(licenseManager.generateLicense(license));

			session.saveOrUpdate(license);
			session.saveOrUpdate(client);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}

		doGet(req, resp);

	}
}
