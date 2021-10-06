package com.nitya.accounter.web.server;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Activation;
import com.nitya.accounter.core.Activity;
import com.nitya.accounter.core.ActivityType;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientSubscription;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.RememberMeKey;
import com.nitya.accounter.core.Subscription;
import com.nitya.accounter.core.SupportedUser;
import com.nitya.accounter.core.User;
import com.nitya.accounter.mail.UsersMailSendar;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.servlets.BaseServlet;
import com.nitya.accounter.utils.HexUtil;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.utils.SecureUtils;
import com.nitya.accounter.utils.Security;
import com.nitya.accounter.web.client.CompanyAndFeatures;
import com.nitya.accounter.web.client.IAccounterWindowsHomeService;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.CompanyDetails;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.SignupDetails;
import com.nitya.accounter.web.client.core.StartupException;
import com.nitya.accounter.web.client.core.SubscriptionDetails;
import com.nitya.accounter.web.client.exception.AccounterException;

public class AccounterWindowsHomeServiceImpl extends
		AccounterWindowsRPCBaseServiceImpl implements
		IAccounterWindowsHomeService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAIL_ID = 0;
	private static final int NAME = 1;
	private static final int PHONE_NO = 2;

	private static final String OUR_COOKIE = "_accounter_01_infinity_22";
	private static final String IS_SUPPORTED_USER = "isSupportedUser";
	private static final String SUPPORTED_EMAIL_ID = "supportedEmialId";
	private static final String EMAIL_ID = "emailId";
	private static final String PASSWORD = "password";
	private static final String ACTIVATION_TOKEN = "activationToken";
	private static final String ACTIVATION_URL = "/main/activation";
	public static final String COMPANY_ID = "companyId";
	public static final String CREATE = "create";

	public AccounterWindowsHomeServiceImpl() {
		super();
	}

	@Override
	public ArrayList<CompanyDetails> login(String email, String password,
			boolean rememberMe) throws StartupException {

		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = null;

		ArrayList<CompanyDetails> list = new ArrayList<CompanyDetails>();
		transaction = session.beginTransaction();
		Client client = null;
		String errorMsg = "Internal error occured.Try again after some time";
		try {
			client = doLogin(email, password, rememberMe);
			if (client != null && !client.isDeleted()) {
				// if valid credentials are there we redirect to <dest> param or
				// /companies

				if (!client.isActive()) {
					// client.setRequirePasswordReset(false);
					// saveEntry(client);
					errorMsg = "Your account is not yet activated. Please enter the activation code below to activate your account or click on Resend activate code to get the activation code again.";
					throw new StartupException(errorMsg);
				} else {

					if (client.isRequirePasswordReset()) {
						client.setRequirePasswordReset(false);
						session.saveOrUpdate(client);
					}

					HttpSession httpSession = getThreadLocalRequest()
							.getSession();
					httpSession.setAttribute(EMAIL_ID, client.getEmailId());
					client.setLoginCount(client.getLoginCount() + 1);
					client.setLastLoginTime(System.currentTimeMillis());
					session.saveOrUpdate(client);
					boolean isSupportedUser = isSupportUser(client.getEmailId());
					httpSession
							.setAttribute(IS_SUPPORTED_USER, isSupportedUser);
					Set<User> users = client.getUsers();
					if (!isSupportedUser && users.isEmpty()) {
						list = new ArrayList<CompanyDetails>();
					} else {
						if (isSupportedUser) {
							// get.CompanyId.Tradingname.and.Country.of.supportUser
							@SuppressWarnings("unchecked")
							List<Object[]> objects = session
									.getNamedQuery(
											"get.CompanyId.Tradingname.and.Country.of.supportUser")
									.list();
							addCompanies(list, objects);
							getThreadLocalRequest().getSession().setAttribute(
									SUPPORTED_EMAIL_ID, email);
						} else {
							List<Long> userIds = new ArrayList<Long>();
							for (User user : users) {
								if (!user.isDeleted()) {
									userIds.add(user.getID());
								}
							}
							@SuppressWarnings("unchecked")
							List<Object[]> objects = session
									.getNamedQuery(
											"get.CompanyId.Tradingname.and.Country.of.user")
									.setParameterList("userIds", userIds)
									.list();
							addCompanies(list, objects);
						}

					}
				}
			} else {
				errorMsg = "The details that you have are incorrect. If you have forgotten your details, please refer to your invitation or contact the person who invited you to Anna Accounting.";
				throw new StartupException(errorMsg);
			}
			transaction.commit();
			return list;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new StartupException(errorMsg);
		} finally {
		}
	}

	@Override
	public boolean signup(SignupDetails details) throws StartupException {
		HttpSession session = getThreadLocalRequest().getSession(true);
		String errorMsg = "Internal error occured";
		if (!details.isAcceptedTerms()) {
			errorMsg = "Please accept Terms of use";
			throw new StartupException(errorMsg);
		}
		if (!isValidInputs(NAME, details.getFirstName(), details.getLastName(),
				details.getCountry())
				|| !isValidInputs(MAIL_ID, details.getEmailId())) {
			errorMsg = "Given Inputs are wrong.";
			throw new StartupException(errorMsg);
		}

		if (details.getCountry() == null || details.getCountry().isEmpty()) {
			errorMsg = "Please select the country";
			throw new StartupException(errorMsg);
		}

		if (!details.getPassword().equals(details.getConfirmPassword())) {
			errorMsg = "Passwords not matched";
			throw new StartupException(errorMsg);
		}

		String emailId = details.getEmailId().toLowerCase();
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(details
				.getEmailId() + details.getPassword()));

		Session hibernateSession = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = hibernateSession.beginTransaction();
			// Have to check UserExistence
			Client client = getClient(emailId);
			if (client != null) {
				if (client.isDeleted()) {
					client.setActive(true);
					client.setUsers(new HashSet<User>());
					client.setEmailId(emailId);
					client.setFirstName(details.getFirstName());
					client.setLastName(details.getLastName());
					client.setCreatedDate(new FinanceDate());
					client.setFullName(details.getFirstName() + " "
							+ details.getLastName());
					client.setPassword(passwordWithHash);
					client.setPasswordRecoveryKey(EU.encryptPassword(details
							.getPassword()));
					client.setPhoneNo(details.getPhoneNum());
					client.setCountry(details.getCountry());
					client.setSubscribedToNewsLetters(details
							.isSubscribeUpdates());
					client.setDeleted(false);
					saveEntry(client);
					session.setAttribute(EMAIL_ID, emailId);

					// SEND WELCOME MAIL TO SIGNUP USER
					UsersMailSendar.sendWelComeMail(details.getFirstName(),
							emailId);
					// Send to SignUp Success View
					transaction.commit();
				} else {
					errorMsg = "This Email ID is already registered with Anna Accounting, try to signup with another Email ID";
					throw new StartupException(errorMsg);
				}
			} else {
				// else

				// Create Client and Save
				client = new Client();
				client.setActive(true);
				client.setUsers(new HashSet<User>());
				client.setEmailId(emailId);
				client.setFirstName(details.getFirstName());
				client.setLastName(details.getLastName());
				client.setCreatedDate(new FinanceDate());
				client.setFullName(details.getFirstName() + " "
						+ details.getLastName());
				client.setPassword(passwordWithHash);
				client.setPasswordRecoveryKey(EU.encryptPassword(details
						.getPassword()));
				client.setPhoneNo(details.getPhoneNum());
				client.setCountry(details.getCountry());
				client.setSubscribedToNewsLetters(details.isSubscribeUpdates());

				ClientSubscription clientSubscription = new ClientSubscription();
				clientSubscription.setCreatedDate(new Date());
				clientSubscription.setSubscription(Subscription
						.getInstance(Subscription.PREMIUM_USER));
				clientSubscription
						.setDurationType(ClientSubscription.UNLIMITED_USERS);
				clientSubscription.setLastModified(new Date());
				saveEntry(clientSubscription);

				client.setClientSubscription(clientSubscription);
				client.setDeleted(false);
				saveEntry(client);
				session.setAttribute(EMAIL_ID, emailId);

				// SEND WELCOME MAIL TO SIGNUP USER
				UsersMailSendar
						.sendWelComeMail(details.getFirstName(), emailId);
				// Send to SignUp Success View

				transaction.commit();
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new StartupException(errorMsg);
		}

		return true;
	}

	@Override
	public boolean activate(String activationCode) throws StartupException {
		String errorMsg = "Internal error occured.Please try after some time";
		if (activationCode == null) {
			errorMsg = "Please enter activation code";
			throw new StartupException(errorMsg);
		}
		activationCode = activationCode.toLowerCase().trim();

		// get activation record
		try {
			Activation activation = getActivation(activationCode);
			// If it is null
			if (activation == null) {
				// set Error "Token has expired"
				// We check him and if invalid code we show him form to enter
				// valid code.
				errorMsg = "Invalid Activation code. Please click <a href='/main/emailforactivation'>here</a> to get new activation code.";
				throw new StartupException(errorMsg);
			} else {
				// If code is valid we create the user and set the session and
				// external redirect him to <dest> param or /login
				HttpSession session = getThreadLocalRequest().getSession(true);
				session.setAttribute(ACTIVATION_TOKEN, activationCode);
				session.setAttribute(EMAIL_ID, activation.getEmailId());

				Session hbSession = HibernateUtil.getCurrentSession();
				Transaction transaction = null;
				try {
					transaction = hbSession.beginTransaction();
					Client client = (Client) hbSession
							.getNamedQuery("getClient.by.mailId")
							.setParameter(EMAIL_ID, activation.getEmailId())
							.uniqueResult();
					client.setActive(true);
					saveEntry(client);

					// delete activation object

					Query query = hbSession
							.getNamedQuery("delete.activation.by.emailId");
					query.setParameter("emailId", activation.getEmailId()
							.trim());
					transaction.commit();
				} catch (Exception e) {
					if (transaction != null) {
						transaction.rollback();
					}
					throw new StartupException(errorMsg);
				}
			}

		} catch (Exception e) {
			throw new StartupException(errorMsg);
		}

		return true;
	}

	@Override
	public boolean forgotPassword(String email) throws StartupException {
		String errorMsg = "Internal error occured.Please try after some time";
		if (email == null) {
			errorMsg = "we couldn't find any user with the given Email ID. please enter a valid email";
			throw new StartupException(errorMsg);
		}
		email = email.toLowerCase().trim();

		Session serverSession = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		transaction = serverSession.beginTransaction();
		Client client = getClient(email);

		if (client == null) {
			errorMsg = "we couldn't find any user with the given Email ID. please enter a valid email.";
			throw new StartupException(errorMsg);
		}

		Activation activation = getActivationByEmailId(email);
		String token = null;
		if (activation == null) {
			token = createActivation(email);
		} else {
			token = activation.getToken();
		}

		sendForgetPasswordLinkToUser(client, token);

		transaction.commit();
		return true;
	}

	@Override
	public boolean deleteCompany(long companyId, boolean fromAllUsers)
			throws StartupException {
		String emailID = (String) getThreadLocalRequest().getSession()
				.getAttribute(EMAIL_ID);
		String errorMsg = "Company deletion failed.Internak error occured";
		if (emailID == null) {
			errorMsg = "Company deletion failed because of invalide session.";
			throw new StartupException(errorMsg);
		}
		try {
			deleteComapny(companyId, fromAllUsers);
		} catch (IOException e) {
			errorMsg = "Company deletion failed.Internal Error occured";
			throw new StartupException(errorMsg);
		} catch (ServletException e) {
			throw new StartupException(errorMsg);
		}
		return true;
	}

	private boolean isValidInputs(int inputType, String... values) {
		for (String value : values) {
			switch (inputType) {
			case MAIL_ID:
				return value
						.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$");

			case NAME:
				// return value.matches("^[a-zA-Z ]+$");
				return true;

			case PHONE_NO:
				return value.matches("^[0-9][0-9-]+$");

			}
		}
		return false;
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(EMAIL_ID, emailId);
		Client client = (Client) namedQuery.uniqueResult();
		// session.close();
		return client;
	}

	protected String createActivation(String emailID) {
		String token = SecureUtils.createNumberID(10);
		Activation activation = new Activation();
		activation.setEmailId(emailID);
		activation.setToken(token);
		activation.setSignUpDate(new Date());
		saveEntry(activation);
		return token;
	}

	protected void saveEntry(Object object) {
		Session currentSession = HibernateUtil.getCurrentSession();
		currentSession.saveOrUpdate(object);
	}

	protected void sendActivationEmail(String token, Client client) {
		UsersMailSendar.sendActivationMail(token, client);
	}

	private Client doLogin(String emailId, String password, boolean isRemember)
			throws NoSuchAlgorithmException {

		Client client = getClient(emailId, password);
		if (client != null && isRemember) {
			// Inserting RememberMeKey
			Session session = HibernateUtil.getCurrentSession();

			byte[] makeHash = Security.makeHash(client.getEmailId()
					+ Security.makeHash(client.getPassword()));
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(makeHash);
			String rememberMeKey = HexUtil.bytesToHex(digest);
			RememberMeKey rememberMe = new RememberMeKey(client.getEmailId(),
					rememberMeKey);
			session.save(rememberMe);
			addUserCookies(getThreadLocalResponse(), rememberMeKey);
		}
		return client;
	}

	protected void addUserCookies(HttpServletResponse resp, String key) {
		Cookie userCookie = new Cookie(OUR_COOKIE, key);
		userCookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		// userCookie.setPath("/");
		// userCookie.setDomain(ServerConfiguration.getServerCookieDomain());
		resp.addCookie(userCookie);
	}

	private Client getClient(String emailId, String password) {
		if (emailId == null || password == null) {
			return null;
		}
		emailId = emailId.trim();
		String passwordWord = HexUtil.bytesToHex(Security.makeHash(emailId
				+ Client.PASSWORD_HASH_STRING + password.trim()));

		Session session = HibernateUtil.getCurrentSession();
		try {
			Client client = null;
			Query query = session
					.getNamedQuery("getclient.from.central.db.using.emailid.and.password");
			query.setParameter(EMAIL_ID, emailId);
			query.setParameter(PASSWORD, passwordWord);
			client = (Client) query.uniqueResult();
			String passwordHash = HexUtil.bytesToHex(Security.makeHash(emailId
					+ password.trim()));
			if (client == null) {
				query = session
						.getNamedQuery("getclient.from.central.db.using.emailid.and.password");
				query.setParameter(EMAIL_ID, emailId);
				query.setParameter(PASSWORD, passwordHash);
				client = (Client) query.uniqueResult();
				if (client != null) {
					client.setPassword(passwordWord);
					client.setPasswordRecoveryKey(EU.encryptPassword(password
							.trim()));
					session.saveOrUpdate(client);
				}
			}
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isSupportUser(String emailId) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Object load = currentSession.get(SupportedUser.class, emailId);
		return load != null;
	}

	private void addCompanies(List<CompanyDetails> list, List<Object[]> objects) {
		for (Object[] obj : objects) {
			CompanyDetails com = new CompanyDetails();
			com.setCompanyId((Long) obj[0]);
			com.setCompanyName((String) obj[1]);
			com.setCountry((String) obj[2]);
			list.add(com);
		}
	}

	private void deleteComapny(final long companyId,
			final boolean deleteAllUsers) throws IOException, ServletException {
		final String email = (String) getThreadLocalRequest().getSession()
				.getAttribute(EMAIL_ID);
		final String errorMsg = "Company deletion failed internal error occured";
		new Thread(new Runnable() {

			@Override
			public void run() {
				Session session = HibernateUtil.openSession();
				Transaction transaction = null;
				try {
					boolean canDeleteFromSingle = true, canDeleteFromAll = true;

					Company company = (Company) session.get(Company.class,
							companyId);
					User user = (User) session.getNamedQuery("user.by.emailid")
							.setParameter("emailID", email)
							.setParameter("company", company).uniqueResult();

					Query query = session.getNamedQuery("get.Admin.Users")
							.setParameter("company", company);
					@SuppressWarnings("unchecked")
					List<User> adminUsers = query.list();
					if (adminUsers.size() < 2) {
						for (User u : adminUsers) {
							if (u.getID() == user.getID()) {
								canDeleteFromSingle = false;
								break;
							}
						}
					}

					if (user != null && !user.isAdmin()) {
						canDeleteFromAll = false;
					}
					AccounterThreadLocal.set(user);

					transaction = session.beginTransaction();

					Client client = getClient(email);
					Company serverCompany = null;
					if (companyId == 0) {
						throw new StartupException(errorMsg);
					}
					for (User usr : client.getUsers()) {
						if (usr.getCompany().getID() == companyId) {
							serverCompany = usr.getCompany();
						}
					}

					if (serverCompany == null) {
						throw new StartupException(errorMsg);
					}

					// boolean isAdmin = s2sService.isAdmin(
					// Long.parseLong(companyID), email);

					if (!canDeleteFromSingle && !canDeleteFromAll) {
						throw new StartupException(
								"You Don't have Permissions to Delete this Company");
					}

					if (canDeleteFromAll
							&& (deleteAllUsers || serverCompany
									.getNonDeletedUsers().size() == 1)) {

						CallableStatement call = session.connection()
								.prepareCall("{ ? = call delete_company(?) }");
						call.registerOutParameter(1, Types.BOOLEAN);
						call.setLong(2, serverCompany.getId());
						call.execute();
						boolean isDeleted = call.getBoolean(1);
						if (!isDeleted) {
							throw new Exception();
						}

					} else if (canDeleteFromSingle) {
						user.setDeleted(true);
						session.saveOrUpdate(user);
						Activity activity = new Activity(user.getCompany(),
								user.getCompany().getCreatedBy(),
								ActivityType.DELETE, user);
						session.save(activity);
						// Deleting Client from ServerCompany
						// client.getUsers().remove(user);
						// session.saveOrUpdate(client);
					}
					transaction.commit();
				} catch (Exception e) {
					if (transaction != null) {
						transaction.rollback();
					}
				} finally {
					session.close();
				}
			}
		}).start();
	}

	private Activation getActivation(String token) {
		Session session = HibernateUtil.getCurrentSession();
		if (session != null) {
			Activation val = (Activation) session
					.getNamedQuery("get.activation.by.token")
					.setString("token", token).uniqueResult();
			return val;

		}
		return null;
	}

	private Activation getActivationByEmailId(String email) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			Query query = session.getNamedQuery("get.activation.by.emailid");
			query.setParameter("emailId", email);
			Activation val = (Activation) query.uniqueResult();
			return val;
		} catch (Exception e) {
		}
		return null;
	}

	protected void sendForgetPasswordLinkToUser(Client client,
			String activationCode) {

		Session session = HibernateUtil.getCurrentSession();
		client.setRequirePasswordReset(true);

		session.save(client);
		StringBuffer link = new StringBuffer("https://");
		link.append(ServerConfiguration.getMainServerDomain());
		link.append(ACTIVATION_URL);
		System.out.println("@@@ ACTIVATION CODE::" + activationCode);
		UsersMailSendar.sendResetPasswordLinkToUser(link.toString(),
				activationCode, client);
	}

	@Override
	public ArrayList<CompanyDetails> getCompanies() throws StartupException {
		ArrayList<CompanyDetails> list = new ArrayList<CompanyDetails>();
		HttpSession httpSession = getThreadLocalRequest().getSession(true);
		Session session = HibernateUtil.getCurrentSession();
		String emailId = (String) httpSession.getAttribute(EMAIL_ID);
		if (emailId == null) {
			throw new StartupException("Please login");
		}
		boolean isSupportedUser = isSupportUser(emailId);
		Client client = getClient(emailId);
		if (client == null) {
			throw new StartupException("Internal error occured");
		}
		Set<User> users = client.getUsers();
		if (!isSupportedUser && users.isEmpty()) {
			list = new ArrayList<CompanyDetails>();
		} else {
			if (isSupportedUser) {
				// get.CompanyId.Tradingname.and.Country.of.supportUser
				@SuppressWarnings("unchecked")
				List<Object[]> objects = session.getNamedQuery(
						"get.CompanyId.Tradingname.and.Country.of.supportUser")
						.list();
				addCompanies(list, objects);
			} else {
				List<Long> userIds = new ArrayList<Long>();
				for (User user : users) {
					if (!user.isDeleted()) {
						userIds.add(user.getID());
					}
				}
				@SuppressWarnings("unchecked")
				List<Object[]> objects = session
						.getNamedQuery(
								"get.CompanyId.Tradingname.and.Country.of.user")
						.setParameterList("userIds", userIds).list();
				addCompanies(list, objects);
			}
		}
		return list;
	}

	@Override
	public boolean logout() throws StartupException {
		HttpServletRequest req = getThreadLocalRequest();
		String userid = (String) req.getSession().getAttribute(EMAIL_ID);
		Boolean isSupportedUser = (Boolean) req.getSession().getAttribute(
				IS_SUPPORTED_USER);
		Long cid = (Long) req.getSession().getAttribute(COMPANY_ID);
		if (userid != null) {
			if (cid != null) {
				try {
					if (!isSupportedUser) {
						updateActivity(userid, cid);
					}

					// Destroy the comet queue so that it wont take memory
					CometManager.destroyStream(req.getSession().getId(), cid,
							userid);
				} catch (Exception e) {
					throw new StartupException("Internal error occured");
				}
			}
			// if is Support user then put his email Id back
			if (isSupportedUser) {
				req.getSession().setAttribute(EMAIL_ID,
						req.getSession().getAttribute(SUPPORTED_EMAIL_ID));
			} else {
				req.getSession().removeAttribute(EMAIL_ID);
			}
		}
		// Support user and OpendCompany
		if (isSupportedUser != null && isSupportedUser && cid != null) {
			req.getSession().removeAttribute(COMPANY_ID);
		} else {
			req.getSession().invalidate();
			deleteCookie(req, getThreadLocalResponse());
		}
		return true;
	}

	private void updateActivity(String userid, Long cid)
			throws StartupException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = (Company) session.get(Company.class, cid);
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = (User) session.getNamedQuery("user.by.emailid")
					.setParameter("emailID", userid)
					.setEntity("company", company).uniqueResult();
			Activity activity = new Activity(user.getCompany(), user,
					ActivityType.LOGOUT);
			session.save(activity);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new StartupException();
		}
	}

	private void deleteCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String userKey = getCookie(request, OUR_COOKIE);
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().endsWith(OUR_COOKIE)) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setDomain(ServerConfiguration.getServerCookieDomain());
				response.addCookie(cookie);
			}
		}

		if (userKey == null || userKey.isEmpty()) {
			return;
		}
		// Deleting RememberMEKEy from Database
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.getNamedQuery("delete.remembermeKeys")
					.setParameter("key", userKey).executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}

	}

	private String getCookie(HttpServletRequest request, String ourCookie) {
		Cookie[] clientCookies = request.getCookies();
		if (clientCookies != null) {
			for (Cookie cookie : clientCookies) {
				if (cookie.getName().equals(ourCookie)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public ArrayList<CompanyDetails> updateClient(String password,
			String confirm) throws StartupException {
		String errorMsg = "Session expired";
		HttpSession httpsession = getThreadLocalRequest().getSession();
		if (httpsession.getAttribute(EMAIL_ID) == null) {
			throw new StartupException(errorMsg);
		}
		// get activation record from table
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Transaction transaction = hibernateSession.beginTransaction();
		try {
			if (password.isEmpty() || confirm.isEmpty()) {
				errorMsg = "Password enter a valid passowrd";
				throw new StartupException(errorMsg);
			}
			// compare if not equal send error message
			// otherwise
			if (!password.equals(confirm)) {
				errorMsg = "Passwords not matched";
				throw new StartupException(errorMsg);
			}

			// getClient record with activation emailId
			String emailId = (String) httpsession.getAttribute(EMAIL_ID);
			Client client = getClient(emailId);

			// update password and set isActive true
			client.setPassword(HexUtil.bytesToHex(Security.makeHash(emailId
					+ password.trim())));
			client.setPasswordRecoveryKey(EU.encryptPassword(password.trim()));
			client.setRequirePasswordReset(false);

			try {
				// and save Client,
				saveEntry(client);
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				throw new StartupException(errorMsg);
			}

			httpsession.setAttribute(EMAIL_ID, emailId);
		} catch (Exception e) {
			transaction.rollback();
			throw new StartupException(errorMsg);
		} finally {

		}

		return getCompanies();
	}

	@Override
	public CompanyAndFeatures getCompany(Long companyId)
			throws AccounterException {

		String loginEmail = (String) getThreadLocalRequest().getSession()
				.getAttribute(EMAIL_ID);
		Client client = getClient(loginEmail);
		CompanyAndFeatures comFeatures = new CompanyAndFeatures();

		HttpSession httpSession = getThreadLocalRequest().getSession();

		if (companyId == null || companyId == 0) {
			comFeatures.setClientCompany(null);

			ArrayList<String> list = new ArrayList<String>(getClient(
					getUserEmail()).getClientSubscription().getSubscription()
					.getFeatures());
			if (!client.getClientSubscription().isPaidUser()) {
				list.remove(Features.ENCRYPTION);
			}
			comFeatures.setFeatures(list);

			httpSession.setAttribute(CREATE, "true");
			httpSession.removeAttribute(COMPANY_ID);

			return comFeatures;
		} else {

			httpSession.setAttribute(COMPANY_ID, companyId);

			FinanceTool tool = new FinanceTool();
			Company company = tool.getCompany(companyId);
			ClientCompany clientCompany = tool.getCompanyManager()
					.getClientCompany(loginEmail, companyId);

			CometSession cometSession = CometServlet
					.getCometSession(getThreadLocalRequest().getSession());
			CometManager.initStream(getThreadLocalRequest().getSession()
					.getId(), companyId, clientCompany.getLoggedInUser()
					.getEmail(), cometSession);

			comFeatures.setClientCompany(clientCompany);
			ArrayList<String> list = new ArrayList<String>(company
					.getCreatedBy().getClient().getClientSubscription()
					.getSubscription().getFeatures());
			if (!client.getClientSubscription().isPaidUser()) {
				list.remove(Features.ENCRYPTION);
			}
			comFeatures.setFeatures(list);

			return comFeatures;

		}
	}

	protected String getUserEmail() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				BaseServlet.EMAIL_ID);
	}

	@Override
	public SubscriptionDetails getSubscriptionDetails(String email)
			throws AccounterException {
		Client client = getClient(email);
		if (client == null) {
			return null;
		}
		ClientSubscription clientSubscription = client.getClientSubscription();
		if (clientSubscription.getPremiumType() == 0) {
			new AccounterException("Your not premium user");
		}

		SubscriptionDetails subDetails = new SubscriptionDetails();

		Set<String> members = clientSubscription.getMembers();
		members.remove(client.getEmailId());
		subDetails.setAllowedEmails(new HashSet<String>(members));

		subDetails.setEmailId(client.getEmailId());
		subDetails.setSubscriptionType(clientSubscription.getPremiumType());
		subDetails.setDurationType(clientSubscription.getDurationType());
		subDetails.setExpiresOn(clientSubscription.getExpiredDateAsString());
		subDetails.setPaidUser(clientSubscription.isPaidUser());
		subDetails.setFreeTrailDone(client.isPremiumTrailDone());

		return subDetails;
	}
}
