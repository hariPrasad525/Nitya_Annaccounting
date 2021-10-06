package com.nitya.accounter.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientSubscription;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.core.PortletConfiguration;
import com.nitya.accounter.core.PortletPageConfiguration;
import com.nitya.accounter.core.Subscription;
import com.nitya.accounter.core.User;
import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.main.ServerLocal;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.portlet.PortletFactory;
import com.nitya.accounter.web.client.portlet.PortletPage;

public class SubscriptionTool extends Thread {
	Logger log = Logger.getLogger(SubscriptionTool.class);

	public SubscriptionTool() {
		super("Subsciprion Tool");
	}

	@SuppressWarnings("unchecked")
	public void run() {
		log.info("Started checking of subscriptions");
		ServerLocal.set(Locale.ENGLISH);

		// Get All Clients
		Session session = HibernateUtil.openSession();
		List<Long> clients = null;
		try {
			Query query = session.getNamedQuery("get.all.clients");
			clients = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		// Check All Client Subscriptions
		for (Long clientID : clients) {
			checkSubscription(clientID);
		}
		log.info("Completed checking of subscriptions");
	}

	/**
	 * Checks the Client's Subscription. If Expires Grace Period also, then
	 * Expires the Subscription. Otherwise holds the Subscription Until
	 * GracPeriod
	 * 
	 * Note: We first Expires the Subscription, then Removes all features after
	 * expiration of GracePeriod
	 * 
	 * @param clientID
	 */
	private void checkSubscription(Long clientID) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Client client = (Client) session.get(Client.class, clientID);
			log.info("Client's Subscription EmailID  : " + client.getEmailId());
			ClientSubscription subscription = client.getClientSubscription();
			log.info("Before Subscription Expired  Client Sunscription Details :"
					+ subscription.toString());

			// If Expired GracePeriod also, then Expire Subscription
			if (subscription.isGracePeriodExpired()) {
				doExpireSubscription(client);
				transaction.commit();
				return;
			}

			// If Subscription Expired, Then Make it FREE CLIENT, put on hold
			// untill grace period expires
			if (subscription.isExpired()) {
				Date gracePeriodDate = subscription.getGracePeriodDate();
				if (gracePeriodDate != null) {
					return;
				}
				gracePeriodDate = getGracePeriodDate();
				int premiumType = subscription.getPremiumType();
				subscription.setSubscription(Subscription
						.getInstance(Subscription.FREE_CLIENT));
				client.setSubscriptionType(subscription.getSubscription()
						.getType());
				subscription.setPremiumType(0);
				subscription.setDurationType(0);
				subscription.setLastModified(new Date());
				subscription.setGracePeriodDate(gracePeriodDate);
				session.saveOrUpdate(subscription);
				session.saveOrUpdate(client);
				log.info("After Subscription Expired  Client Sunscription Details :"
						+ subscription.toString());
				// SEND EMAIL
				try {
					UsersMailSendar.sendMailToSubscriptionExpiredUser(client,
							premiumType);
					log.info("Sent Mail To Subscription Expired User  :"
							+ client.getEmailId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			transaction.commit();

		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	public static Date getGracePeriodDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, ServerConfiguration.getGracePeriod());
		return c.getTime();
	}

	/**
	 * Completely Expires the Subscription by deleting all Features.
	 * 
	 * @param client
	 * @throws AccounterException
	 */
	private void doExpireSubscription(Client client) throws Exception {

		Session session = HibernateUtil.getCurrentSession();

		ClientSubscription subscription = client.getClientSubscription();

		int premiumType = subscription.getPremiumType();

		subscription.setLastModified(new Date());
		subscription.setGracePeriodDate(null);
		subscription.setExpiredDate(null);
		Set<String> members = subscription.getMembers();
		Set<String> deletedMembers = getDeletedMembers(members,
				client.getEmailId(), premiumType);
		deleteUsers(deletedMembers, client);
		members.removeAll(deletedMembers);
		subscription.setMembers(members);
		if (subscription.getSubscription().getType() == Subscription.FREE_CLIENT) {
			// Get All Companies Created by this Company
			List<Company> companies = getAllCompaniesByClient(client);
			for (Company company : companies) {
				deleteAllFeatures(company);
				session.saveOrUpdate(company);
			}
		}
		// Update Subscription
		session.saveOrUpdate(subscription);
		log.info("Complted..:" + client.getEmailId());

		try {
			// Send Email to User
			UsersMailSendar.sendMailToSubsGracePeriodExpiredUser(client);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes All Features for this Company
	 * 
	 * @param company
	 */
	private void deleteAllFeatures(Company company) {
		Session session = HibernateUtil.getCurrentSession();

		List<String> defPortlets = getDefaultPortlets();

		CompanyPreferences preferences = company.getPreferences();
		preferences.setInventoryEnabled(false);
		preferences.setEnableMultiCurrency(false);
		preferences.setSalesOrderEnabled(false);
		preferences.setPurchaseOrderEnabled(false);

		preferences.setClassTrackingEnabled(false);// CLASS
		preferences.setLocationTrackingEnabled(false);// LOCATION
		preferences.setProductandSerivesTrackingByCustomerEnabled(false);// BILLABLE_EXPENSE
		preferences.setBillableExpsesEnbldForProductandServices(false);// BILLABLE_EXPENSE
		preferences.setDelayedchargesEnabled(false);// CREDITS_CHARGES

		Set<User> users = company.getUsers();
		for (User user : users) {
			Set<PortletPageConfiguration> portletPages = user.getPortletPages();
			for (PortletPageConfiguration pg : portletPages) {
				if (pg.getPageName().equals(PortletPage.DASHBOARD)) {
					Iterator<PortletConfiguration> pcIterator = pg
							.getPortlets().iterator();
					while (pcIterator.hasNext()) {
						PortletConfiguration pc = pcIterator.next();
						if (!defPortlets.contains(pc.getPortletName())) {
							pcIterator.remove();
						}
					}
					session.saveOrUpdate(pg);
					break;
				}
			}
			session.saveOrUpdate(user);
		}

	}

	private List<String> getDefaultPortlets() {
		List<String> defPortlets = new ArrayList<String>();
		defPortlets.add(PortletFactory.BANKING);
		defPortlets.add(PortletFactory.EXPENSES_CLAIM);
		defPortlets.add(PortletFactory.MONEY_COMING);
		defPortlets.add(PortletFactory.MONEY_GOING);
		return defPortlets;
	}

	@SuppressWarnings("unchecked")
	private List<Company> getAllCompaniesByClient(Client c) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session
				.getNamedQuery("get.client.created.companies");
		((SQLQuery) namedQuery).addEntity(Company.class);
		Query query = namedQuery.setParameter("clientId", c.getID());
		return query.list();
	}

	public static Set<String> getDeletedMembers(Set<String> members,
			String emailId, int premiumType) {
		Set<String> deletedMems = new HashSet<String>();

		List<String> newMembers = new ArrayList<String>(members);
		newMembers.remove(emailId);
		int noOfUsers = 0;
		switch (premiumType) {
		case ClientSubscription.ONE_USER:
			noOfUsers = 1;
			break;
		case ClientSubscription.TWO_USERS:
			noOfUsers = 2;
			break;
		case ClientSubscription.FIVE_USERS:
			noOfUsers = 5;
			break;
		default:
			break;
		}
		noOfUsers = newMembers.size() - noOfUsers + 1;
		for (int i = 0; i < noOfUsers && i < newMembers.size(); i++) {
			deletedMems.add(newMembers.get(i));
		}
		return deletedMems;

	}

	private void deleteUsers(Set<String> members, Client client)
			throws AccounterException {
		for (String s : members) {
			log.info("deleteUser:" + s);
			deleteUser(client, s);
		}
	}

	@SuppressWarnings("unchecked")
	public static void deleteUser(Client client, String emailId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getUserIds.invited.by.client");
		((SQLQuery) query).addEntity(User.class);

		query = query.setParameter("emailId", emailId).setParameter("clientId",
				client.getID());
		List<User> users = query.list();
		for (User user : users) {
			user.setDeleted(true);
			session.saveOrUpdate(user);
		}
	}
}
