package com.nitya.accounter.servlets;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.LicensePurchase;
import com.nitya.accounter.utils.HibernateUtil;

public class PurchaseLicenseIPNServlet extends PayPalIPNServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void deletePayment(String emailId) {
		Client client = getClient(emailId);
		if (client == null) {
			return;
		}
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			LicensePurchase licensePurchase = client.getLicensePurchase();
			client.setLicensePurchase(null);
			session.delete(licensePurchase);
			session.saveOrUpdate(client);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
	}

	@Override
	protected void createPayment(String emailId, Map<String, String> params) {

		log.info("Upgrading Client, Email - " + emailId + ". Params - "
				+ params);
		String type = params.get("option_selection1");
		log.info("Purchase type:" + type);
		int paymentType = 0;
		Date expiredDate = new Date();
		if (type.equals("One user yearly")) {
			paymentType = LicensePurchase.TYPE_ONE_USER;
			expiredDate = getNextYearDate();
		} else if (type.equals("2 users yearly")) {
			paymentType = LicensePurchase.TYPE_TWO_USER;
			expiredDate = getNextYearDate();
		} else if (type.equals("5 users yearly")) {
			paymentType = LicensePurchase.TYPE_FIVE_USER;
			expiredDate = getNextYearDate();
		} else if (type.equals("Unlimited users yearly")) {
			paymentType = LicensePurchase.TYPE_UNLIMITED_USER;
			expiredDate = getNextYearDate();
		}
		Client client = getClient(emailId);
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			LicensePurchase purchase = client.getLicensePurchase();
			if (purchase == null) {
				purchase = new LicensePurchase();
			}

			purchase.setClient(client);

			purchase.setPurchaseDate(new Date());
			purchase.setExpiredDate(expiredDate);
			String previousSubID = purchase.getPaypalSubscriptionID();
			purchase.setPaypalSubscriptionID(params.get("subscr_id"));

			if (previousSubID != null && !previousSubID.trim().isEmpty()) {
				cancelPreviousSubscription(previousSubID);
			}
			client.setLicensePurchase(purchase);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
	}

	private Date getNextYearDate() {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MONTH, 12);
		return instance.getTime();
	}

}
