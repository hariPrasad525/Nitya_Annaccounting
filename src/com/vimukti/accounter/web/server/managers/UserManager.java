package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.OperationContext;

public class UserManager extends Manager {
	public long inviteUser(OperationContext context) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = context.getData();
			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Operation Data Found Null...." + data);
			}
			User user = new User((ClientUser) data);
			user.selfValidate();
			String email = ((ClientUser) data).getEmail();
			Company company = getCompany(context.getCompanyId());
			ClientSubscription subscription = company.getCreatedBy()
					.getClient().getClientSubscription();
			if (subscription.getPremiumType() != ClientSubscription.UNLIMITED_USERS) {
				if (!email.equals("support@annaccounting.com")) {
					Set<String> members = subscription.getMembers();
					if (!members.contains(email)) {
						throw new AccounterException(
								AccounterException.ERROR_PERMISSION_DENIED,
								"You can't invite more users");
					}
				}
			}
			User userByUserEmail = getUserByUserEmail(email, company);
			if (userByUserEmail != null) {
				if (userByUserEmail.isDeleted()) {
					userByUserEmail.setDeleted(false);
					userByUserEmail.setUserRole(user.getUserRole());
					userByUserEmail.setPermissions(user.getPermissions());
					userByUserEmail.setCanDoUserManagement(user
							.isCanDoUserManagement());
					user = userByUserEmail;
				}
			} else {
				company.addUser(user);
			}
			String userID = context.getUserEmail();

			createOrUpdateClient(company, userID, email, user,
					(ClientUser) data);
			User inviteduser = getUserByUserEmail(userID, company);
			Activity inviteuserActivity = new Activity(company, inviteduser,
					ActivityType.ADD, user);

			session.saveOrUpdate(user);
			session.save(inviteuserActivity);
			transaction.commit();
			ClientUser clientObject = new ClientConvertUtil().toClientObject(
					user, ClientUser.class);
			ChangeTracker.put(clientObject.toUserInfo());
			return user.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			transaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL,
						e.getMessage());
			}
		}
	}

	public long updateUser(OperationContext updateContext)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();
		try {
			IAccounterCore data = updateContext.getData();

			ClientUser clientUser = (ClientUser) data;

			User user = (User) session.get(User.class, clientUser.getID());

			String userID = updateContext.getUserEmail();

			Client updateClient = getClient(clientUser.getEmail());
			updateClient.setFirstName(clientUser.getFirstName());
			updateClient.setLastName(clientUser.getLastName());
			updateClient.setFullName(clientUser.getFullName());
			updateClient.setCreatedDate(new FinanceDate());

			Company company = getCompany(updateContext.getCompanyId());
			User user1 = company.getUserByUserEmail(userID);
			new ServerConvertUtil().toServerObject(user,
					(IAccounterCore) clientUser, session);

			canEdit(user, data);
			session.flush();
			session.saveOrUpdate(updateClient);
			session.saveOrUpdate(user);
			Activity userUpdateActivity = new Activity(company, user1,
					ActivityType.EDIT, user);
			session.save(userUpdateActivity);
			hibernateTransaction.commit();
			ChangeTracker.put(clientUser.toUserInfo());
			return user.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			hibernateTransaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}
		}

	}

	private User getUserByUserEmail(String email, Company company) {
		Session session = HibernateUtil.getCurrentSession();
		return (User) session.getNamedQuery("user.by.emailid")
				.setParameter("emailID", email)
				.setParameter("company", company).uniqueResult();

	}

	public PaginationList<ClientActivity> getUsersActivityLog(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			int startIndex, int length, long companyId, long value) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Timestamp startTime = new Timestamp(startDate.getDateAsObject()
				.getTime());
		Timestamp endTime = new Timestamp(endDate.getDateAsObject().getTime());
		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);
		Query query;
		int count;
		if (value == 0) {
			query = session.getNamedQuery("list.Activity");
			query.setEntity("company", company);
			query.setParameter("fromDate", startTime);
			query.setParameter("endDate", endTime);
			query.setFirstResult(startIndex);
			query.setMaxResults(length);
			count = ((BigInteger) session
					.getNamedQuery("getCountOfActivityBetweenDates")
					.setParameter("fromDate", startTime)
					.setParameter("endDate", endTime)
					.setLong("companyId", companyId).uniqueResult()).intValue();
			List<Activity> activites = query.list();
			PaginationList<ClientActivity> clientActivities = new PaginationList<ClientActivity>();
			for (Activity activity : activites) {
				ClientActivity clientActivity;
				try {
					clientActivity = new ClientConvertUtil().toClientObject(
							activity, ClientActivity.class);
					clientActivities.add(clientActivity);
				} catch (AccounterException e) {
					e.printStackTrace();
				}

			}
			clientActivities.setTotalCount(count);
			return clientActivities;

		} else {
			boolean logoutOrLogin = get(ClientActivity.LOGIN_LOGOUT, value);
			boolean preferences = get(ClientActivity.PREFERENCES, value);
			boolean transactions = get(ClientActivity.TRNASACTIONS, value);
			boolean budgets = get(ClientActivity.BUDGETS, value);
			boolean reconciliations = get(ClientActivity.RECONCILIATIONS, value);
			// boolean recurringTransactions = get(
			// ClientActivity.RECURRING_TRNASACTIONS, value);
			query = session.getNamedQuery("getAllUserActivities")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", startTime)
					.setParameter("endDate", endTime)
					.setParameter("logoutOrLogin", logoutOrLogin)
					.setParameter("preferences", preferences)
					.setParameter("transactions", transactions)
					.setParameter("budgets", budgets)
					.setParameter("reconciliations", reconciliations)
					// .setParameter("recurringTransactions",
					// recurringTransactions)
					.setFirstResult(startIndex).setMaxResults(length);
			count = ((Integer) session
					.getNamedQuery("getCountByCustomiseValues")
					.setLong("companyId", companyId)
					.setParameter("fromDate", startTime)
					.setParameter("endDate", endTime)
					.setParameter("fromDate", startTime)
					.setParameter("endDate", endTime)
					.setParameter("logoutOrLogin", logoutOrLogin)
					.setParameter("preferences", preferences)
					.setParameter("transactions", transactions)
					.setParameter("budgets", budgets)
					.setParameter("reconciliations", reconciliations)
					.uniqueResult()).intValue();
			List list = query.list();
			Iterator i = list.iterator();
			PaginationList<ClientActivity> clientActivities = new PaginationList<ClientActivity>();
			while ((i).hasNext()) {
				Object object = i.next();
				if (object == null) {
					continue;
				}
				long activityId = (Long) object;
				Activity activity = (Activity) session.get(Activity.class,
						activityId);
				ClientActivity clientActivity;
				try {
					clientActivity = new ClientConvertUtil().toClientObject(
							activity, ClientActivity.class);
					clientActivities.add(clientActivity);
				} catch (AccounterException e) {
					e.printStackTrace();
				}

			}
			clientActivities.setTotalCount(count);
			return clientActivities;

		}

	}

	private boolean get(long flag, long value) {
		return (value & flag) == flag;

	}

	public ArrayList<ClientUserInfo> getAllUsers(long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List<User> financeUsers = session.getNamedQuery("list.User")
				.setEntity("company", company).list();

		List<ClientUserInfo> clientUsers = new ArrayList<ClientUserInfo>();
		for (User user : financeUsers) {
			if (!user.isDeleted()) {
				ClientUser clientUser = new ClientConvertUtil().toClientObject(
						user, ClientUser.class);
				updateClientUser(clientUser, user.getClient());
				ClientUserInfo userInfo = clientUser.toUserInfo();
				clientUsers.add(userInfo);
			}
		}
		return new ArrayList<ClientUserInfo>(clientUsers);
	}

	public boolean changeMyPassword(String emailId, String oldPassword,
			String newPassword) throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String hasedOldPassword = oldPassword.equals("") ? "" : HexUtil
					.bytesToHex(Security.makeHash(emailId
							+ Client.PASSWORD_HASH_STRING + oldPassword));

			String newHashPassword = HexUtil.bytesToHex(Security
					.makeHash(emailId + Client.PASSWORD_HASH_STRING
							+ newPassword));

			Query query = session.getNamedQuery("getEmailIdFromClient")
					.setParameter("emailId", emailId)
					.setParameter("password", hasedOldPassword);
			String emailID = (String) query.uniqueResult();
			if (emailID == null) {
				hasedOldPassword = oldPassword.equals("") ? "" : HexUtil
						.bytesToHex(Security.makeHash(emailId + oldPassword));
				query = session.getNamedQuery("getEmailIdFromClient")
						.setParameter("emailId", emailId)
						.setParameter("password", hasedOldPassword);
				emailID = (String) query.uniqueResult();
				if (emailID == null) {
					return false;
				}
			}

			query = session.getNamedQuery("updatePasswordForClient");
			query.setParameter("newPassword", newHashPassword);
			query.setParameter("emailId", emailId);
			query.executeUpdate();

			List list = session.getNamedQuery("getUserSecrets")
					.setParameter("emailId", emailId).list();
			Iterator iterator = list.iterator();
			byte[] s1 = EU.generatePBS(oldPassword);
			byte[] s4 = EU.generatePBS(newPassword);
			User user = AccounterThreadLocal.get();
			while (iterator.hasNext()) {
				Object[] next = (Object[]) iterator.next();
				long userId = (Long) next[0];// UserId
				byte[] secret = (byte[]) next[1];// Old Secret
				if (secret == null) {
					continue;
				}
				byte[] s3 = EU.decrypt(secret, s1);
				byte[] us = EU.encrypt(s3, s4);
				session.getNamedQuery("updateUserSecret")
						.setParameter("userId", userId)
						.setParameter("secret", us).executeUpdate();
				if (userId == user.getID()) {
					user.setSecretKey(us);
				}
			}
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		}
		return true;
	}

	public void createAdminUser(ClientUser user, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		User admin = new User(user);
		admin.setActive(true);
		session.save(admin);
		Company company = getCompany(companyId);
		company.getUsersList().add(admin);
		session.saveOrUpdate(this);
		transaction.commit();
	}

	private void createOrUpdateClient(Company company, String senderEmailId,
			String emailId, User user, ClientUser clientUser) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		Client inviter = getClient(senderEmailId);

		Client invitedClient = getClient(emailId);
		boolean userExists = false;
		String randomString = clientUser.getPassword() != null &&  !clientUser.getPassword().isEmpty() ? clientUser.getPassword(): HexUtil.getRandomString();
		if (invitedClient == null) {
			invitedClient = new Client();
			invitedClient.setActive(true);
			Set<User> users = new HashSet<User>();
			user.setClient(invitedClient);
			invitedClient.setDeleted(false);

			user.setCompany(company);
			users.add(user);
			invitedClient.setUsers(users);
			invitedClient.setCountry(inviter.getCountry());
			invitedClient.setEmailId(emailId);
			invitedClient.setFirstName(clientUser.getFirstName());
			invitedClient.setLastName(clientUser.getLastName());
			invitedClient.setFullName(clientUser.getFullName());
			invitedClient.setCreatedDate(new FinanceDate());

			invitedClient.setPassword(HexUtil.bytesToHex(Security
					.makeHash(emailId + Client.PASSWORD_HASH_STRING
							+ randomString)));
			invitedClient.setPasswordRecoveryKey(EU
					.encryptPassword(randomString));
			ClientSubscription clientSubscription = new ClientSubscription();
			clientSubscription.setCreatedDate(new Date());
			clientSubscription.setLastModified(new Date());

			clientSubscription.setSubscription(Subscription
					.getInstance(Subscription.PREMIUM_USER));
			clientSubscription
					.setDurationType(ClientSubscription.UNLIMITED_USERS);
			session.save(clientSubscription);
			invitedClient.setClientSubscription(clientSubscription);

			// invitedClient.setRequirePasswordReset(true);
		} else {
			userExists = true;
			Set<User> users = invitedClient.getUsers();
			boolean flag = false;
			for (User u : users) {
				if (company.getID() == u.getCompany().getID()) {
					flag = true;
				}
			}
			if (!flag) {
				invitedClient.getUsers().add(user);
				user.setClient(invitedClient);
			}
		}
		user.setActive(clientUser.isActive() ? true : userExists);
		//user.setActive(userExists);
		session.setFlushMode(flushMode);
		session.save(invitedClient.getClientSubscription());
		session.save(invitedClient);
		if (userExists) {
			UsersMailSendar.sendMailToOtherCompanyUser(invitedClient,
					company.getTradingName(), inviter);
		} else {
			UsersMailSendar.sendMailToInvitedUser(invitedClient, randomString,
					company.getTradingName());
		}
	}

	public Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(BaseServlet.EMAIL_ID, emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}

	public ArrayList<ClientActivity> getAuditHistory(int objectType,
			long objectID, long activityID, Long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getAuditHistory")
				.setParameter("companyId", companyId)
				.setParameter("objectType", objectType)
				.setParameter("activityID", activityID)
				.setParameter("objectID", objectID);

		Iterator i = query.list().iterator();

		ArrayList<ClientActivity> clientActivities = new ArrayList<ClientActivity>();

		while (i.hasNext()) {
			Object[] object = (Object[]) i.next();
			Activity activity = new Activity();
			activity.setUserName(object[1].toString());
			java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(object[0]
					.toString());
			activity.setTime(ts2);
			activity.setHistory(object[2].toString());

			ClientActivity clientActivity;
			try {
				clientActivity = new ClientConvertUtil().toClientObject(
						activity, ClientActivity.class);
				clientActivities.add(clientActivity);
			} catch (AccounterException e) {
				e.printStackTrace();
			}

		}

		return clientActivities;

	}

	public void deleteClientFromCompany(long serverCompanyId,
			String deletableEmail) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Client deletingClient = getClient(deletableEmail);

			Company company = null;
			company = (Company) session.load(Company.class, serverCompanyId);
			// serverCompany.getClients().remove(deletingClient);
			// session.saveOrUpdate(serverCompany);
			deletingClient.getUsers().remove(company);
			session.saveOrUpdate(deletingClient);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}
	}
}
