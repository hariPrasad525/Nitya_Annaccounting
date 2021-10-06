package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.services.SubscriptionTool;
import com.vimukti.accounter.utils.HibernateUtil;

public class SubscriptionIPNServlet extends PayPalIPNServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void deletePayment(String emailId) {
		log.info("Removing Client Subscription for" + emailId);
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			Client client = getClient(emailId);
			ClientSubscription clientSubscription = client
					.getClientSubscription();
			clientSubscription.setLastModified(new Date());

			Calendar c = Calendar.getInstance();
			c.setTime(clientSubscription.getExpiredDate());
			c.add(Calendar.DATE, ServerConfiguration.getGracePeriod());

			clientSubscription.setGracePeriodDate(c.getTime());
			session.saveOrUpdate(clientSubscription);
			session.saveOrUpdate(client);
			transaction.commit();
		} catch (Exception e) {
			log.error("Exception while Removing Subscription : ", e);
			transaction.rollback();
		}
		log.info("Removed Client Subscription !!");
	}

	@Override
	protected void createPayment(String emailId, Map<String, String> params) {
		log.info("Upgrading Client, Email - " + emailId + ". Params - "
				+ params);
		String type = params.get("option_selection1");
		log.info("Subscription type:" + type);
		int paymentType = 0;
		int durationType = 4;
		Date expiredDate = new Date();
		if (type.equals("One user monthly")) {
			paymentType = ClientSubscription.ONE_USER;
			durationType = ClientSubscription.MONTHLY_USER;
			expiredDate = getNextMonthDate(1);
		} else if (type.equals("One user yearly")) {
			paymentType = ClientSubscription.ONE_USER;
			durationType = ClientSubscription.YEARLY_USER;
			expiredDate = getNextMonthDate(12);
		} else if (type.equals("2 users monthly")) {
			paymentType = ClientSubscription.TWO_USERS;
			durationType = ClientSubscription.MONTHLY_USER;
			expiredDate = getNextMonthDate(1);
		} else if (type.equals("2 users yearly")) {
			paymentType = ClientSubscription.TWO_USERS;
			durationType = ClientSubscription.YEARLY_USER;
			expiredDate = getNextMonthDate(12);
		} else if (type.equals("5 users monthly")) {
			paymentType = ClientSubscription.FIVE_USERS;
			durationType = ClientSubscription.MONTHLY_USER;
			expiredDate = getNextMonthDate(1);
		} else if (type.equals("5 users yearly")) {
			paymentType = ClientSubscription.FIVE_USERS;
			durationType = ClientSubscription.YEARLY_USER;
			expiredDate = getNextMonthDate(12);
		} else if (type.equals("Unlimited Users monthly")) {
			paymentType = ClientSubscription.UNLIMITED_USERS;
			durationType = ClientSubscription.MONTHLY_USER;
			expiredDate = getNextMonthDate(1);
		} else if (type.equals("Unlimited Users yearly")) {
			paymentType = ClientSubscription.UNLIMITED_USERS;
			durationType = ClientSubscription.YEARLY_USER;
			expiredDate = getNextMonthDate(12);
		}
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Client client = getClient(emailId);
		String previousSubId = null;

		try {
			ClientSubscription clientSubscription = client
					.getClientSubscription();
			clientSubscription.getMembers().add(emailId);
			if (clientSubscription.getPremiumType() != ClientSubscription.TRIAL_USER
					&& clientSubscription.getPremiumType() > paymentType) {
				clientSubscription.setGracePeriodDate(SubscriptionTool
						.getGracePeriodDate());
			} else {
				clientSubscription.setGracePeriodDate(null);
			}
			clientSubscription.setPremiumType(paymentType);
			clientSubscription.setDurationType(durationType);
			clientSubscription.setLastModified(new Date());
			clientSubscription.setExpiredDate(expiredDate);
			clientSubscription.setSubscription(Subscription
					.getInstance(Subscription.PREMIUM_USER));
			String paypalSubscriptionProfileId = clientSubscription
					.getPaypalSubscriptionProfileId();
			if (paypalSubscriptionProfileId != null
					&& !paypalSubscriptionProfileId.trim().isEmpty()) {
				previousSubId = paypalSubscriptionProfileId;
			}
			clientSubscription.setPaypalSubscriptionProfileId(params
					.get("subscr_id"));
			client.setClientSubscription(clientSubscription);
			client.setPremiumTrailDone(true);
			session.saveOrUpdate(client);
			session.saveOrUpdate(clientSubscription);
			transaction.commit();
		} catch (Exception e) {
			log.error("Exception While Upgrading the Client : " + e);
			transaction.rollback();
		}
		log.info("Client Upgraded Successfully !!");
		try {
			UsersMailSendar.sendMailToSubscribedUser(client);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final String previousSubId2 = previousSubId;
		if (previousSubId != null && !previousSubId.trim().isEmpty()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					cancelPreviousSubscription(previousSubId2);

				}
			}).start();
		}
	}

	private Date getNextMonthDate(int months) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MONTH, months);
		return instance.getTime();
	}

}
