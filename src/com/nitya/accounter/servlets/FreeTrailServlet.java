package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientSubscription;
import com.nitya.accounter.core.Subscription;
import com.nitya.accounter.utils.HibernateUtil;

public class FreeTrailServlet extends BaseServlet {

	/**
	 * 
	 */
	public static String view = "/WEB-INF/thankyou.jsp";
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String parameter = req.getParameter("emailId");
		Client client = getClient(parameter);
		int clientSubscription = client.getClientSubscription()
				.getPremiumType();
		if (clientSubscription == 0) {
			upgradeClient(req, resp, client);

		}
	}

	public void upgradeClient(HttpServletRequest req, HttpServletResponse resp,
			Client client) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			ClientSubscription clientSubscription = client
					.getClientSubscription();
			if (!client.isPremiumTrailDone()) {
				clientSubscription.setLastModified(new Date());
				clientSubscription
						.setPremiumType(ClientSubscription.TRIAL_USER);
				clientSubscription
						.setDurationType(ClientSubscription.MONTHLY_USER);
				clientSubscription.setCreatedDate(new Date());
				clientSubscription.setExpiredDate(getNextMonthDate(1));
				clientSubscription.setSubscription(Subscription
						.getInstance(Subscription.PREMIUM_USER));
				client.setPremiumTrailDone(true);
				client.setSubscriptionType(clientSubscription.getSubscription()
						.getType());
				session.saveOrUpdate(clientSubscription);
				session.saveOrUpdate(client);
				transaction.commit();
				dispatch(req, resp, view);
			}
		} catch (Exception e) {
			transaction.rollback();
		}

	}

	private Date getNextMonthDate(int months) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MONTH, months);
		return instance.getTime();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
