/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.common.util.Base64DecoderException;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserPermissions;
import com.vimukti.accounter.encryption.Encrypter;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.CompanyAndFeatures;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationService;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.CountryPreferences;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.server.managers.CompanyManager;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterCompanyInitializationServiceImpl extends
		RemoteServiceServlet implements IAccounterCompanyInitializationService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void onAfterRequestDeserialized(RPCRequest rpcRequest) {
		Method method = rpcRequest.getMethod();
		log(method.getDeclaringClass().getSimpleName() + "." + method.getName());
		super.onAfterRequestDeserialized(rpcRequest);
	}

	protected final void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			if (isValidSession(request)) {
				Session session = HibernateUtil.openSession();
				try {
					if (CheckUserExistanceAndsetAccounterThreadLocal(request)) {
						super.service(request, response);
						Long serverCompanyID = (Long) request.getSession()
								.getAttribute(BaseServlet.COMPANY_ID);
						if (serverCompanyID != null) {
							new FinanceTool()
									.putChangesInCometStream(serverCompanyID);
						}
					} else {
						response.sendError(HttpServletResponse.SC_FORBIDDEN,
								"Could Not Complete the Request!");
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {
					// EU.removeCipher();
					session.close();
				}
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,
						"Could Not Complete the Request!");
			}
		} catch (Exception e) {

			e.printStackTrace();

			// log.error("Failed to Process Request", e);

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Could Not Complete the Request!");

		}
	}

	@Override
	public boolean initalizeCompany(ClientCompanyPreferences preferences,
			String password, String passwordHint,
			ArrayList<TemplateAccount> accounts) throws AccounterException {
		try {
			Client client = getClient(getUserEmail());
			byte[] d2 = getD2();
			Company company = intializeCompany(preferences, accounts, client,
					password, passwordHint, d2, getThreadLocalRequest()
							.getSession().getId());
			getThreadLocalRequest().getSession().setAttribute(
					BaseServlet.COMPANY_ID, company.getId());
			getThreadLocalRequest().getSession().removeAttribute(
					BaseServlet.CREATE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}
		return true;
	}

	public static Company intializeCompany(
			ClientCompanyPreferences preferences,
			List<TemplateAccount> accounts, Client client, String password,
			String passwordHint, byte[] d2, String sessionId)
			throws AccounterException {

		// if (!client.getClientSubscription().getSubscription().isPaidUser()) {
		// List<Company> companies = client.getCompanies();
		// if (companies.size() > 0) {
		// throw new AccounterException();
		// }
		// }
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			byte[] companySecret = null;
			byte[] userSecret = null;
			byte[] encryptedPass = null;
			EU.removeCipher();
			byte[] passworedRecoveryKey = null;
			if (password != null) {
				try {
					byte[] s3 = EU.generateSymetric();
					byte[] csk = EU.generatePBS(password);
					companySecret = EU.encrypt(s3, csk);
					userSecret = EU.encrypt(s3,
							EU.decrypt(d2, EU.getKey(sessionId)));

					EU.createCipher(userSecret, d2, sessionId);

					String string = SecureUtils.createID(16);
					byte[] prk = EU.generatePBS(string);
					encryptedPass = EU.encrypt(csk, prk);
					Encrypter.sendCompanyPasswordRecoveryKey(
							client.getEmailId(), string);
					passworedRecoveryKey = EU.encryptPassword(password);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Company company = new Company();
			company.setTradingName(preferences.getTradingName());
			company.setConfigured(false);
			company.setCreatedDate(new Date());
			company.setEncryptedPassword(encryptedPass);
			company.setPasswordRecoveryKey(passworedRecoveryKey);
			company.setSecretKey(companySecret);
			company.setVersion(Company.CURRENT_VERSION);

			User user = new User(getUser(client));
			user.setActive(true);
			user.setClient(client);
			user.setCompany(company);

			user.setSecretKey(userSecret);

			session.save(user);
			company.setCreatedBy(user);
			client.getUsers().add(user);
			session.saveOrUpdate(client);

			AccounterThreadLocal.set(user);
			// EU.initEncryption(company, client.getPassword());
			company.getUsers().add(user);
			company.setCompanyEmail(user.getClient().getEmailId());

			company.setConfigured(true);

			CompanyPreferences serverCompanyPreferences = company
					.getPreferences();

			serverCompanyPreferences = new ServerConvertUtil().toServerObject(
					serverCompanyPreferences, preferences, session);

			Currency primaryCurrency = serverCompanyPreferences
					.getPrimaryCurrency();
			primaryCurrency.setCompany(company);
			session.save(primaryCurrency);

			session.saveOrUpdate(company);

			// Updating CompanyPreferences

			company.getRegisteredAddress().setCountryOrRegion(
					client.getCountry());
			company.setRegisteredAddress(serverCompanyPreferences
					.getTradingAddress());
			// Initializing Accounts
			company.setPreferences(serverCompanyPreferences);
			company.initialize(accounts);
			company.setPasswordHInt(passwordHint);

			session.saveOrUpdate(company);

			// Creating Accounts Receivables and Payables for Primary Currency
			primaryCurrency.setAccountsReceivable(company
					.getAccountsReceivableAccount());
			primaryCurrency.setAccountsPayable(company
					.getAccountsPayableAccount());
			session.saveOrUpdate(primaryCurrency);

			transaction.commit();

			// UsersMailSendar.sendMailToDefaultUser(user,
			// company.getTradingName());
			return company;
		} catch (AccounterException e) {
			e.printStackTrace();
			transaction.rollback();
		}
		return null;
	}

	@Override
	public CompanyAndFeatures getCompany() throws AccounterException {
		Long companyID = (Long) getThreadLocalRequest().getSession()
				.getAttribute(BaseServlet.COMPANY_ID);
		Client client = getClient(getUserEmail());
		CompanyAndFeatures companyAndFeatures = new CompanyAndFeatures();
		if (companyID == null) {

			companyAndFeatures.setClientCompany(null);

			ArrayList<String> list = new ArrayList<String>(getClient(
					getUserEmail()).getClientSubscription().getSubscription()
					.getFeatures());
			if (!client.getClientSubscription().isPaidUser()) {
				list.remove(Features.ENCRYPTION);
			}
			companyAndFeatures.setFeatures(list);

			return companyAndFeatures;
		} else {

			FinanceTool tool = new FinanceTool();
			Company company = tool.getCompany(companyID);
			ClientCompany clientCompany = tool.getCompanyManager()
					.getClientCompany(getUserEmail(), companyID);
			if(company.getPreferences().isPayrollOnly()) {
			   clientCompany.setPayHeads(tool.getPayrollManager().getPayheadsList(0, -1, clientCompany.getID()));
			   clientCompany.setEmployees(tool.getPayrollManager().getEmployees(true, 0, -1, clientCompany.getID()));
			}

			CometSession cometSession = CometServlet
					.getCometSession(getThreadLocalRequest().getSession());
			CometManager.initStream(getThreadLocalRequest().getSession()
					.getId(), companyID, clientCompany.getLoggedInUser()
					.getEmail(), cometSession);

			companyAndFeatures.setClientCompany(clientCompany);

			ArrayList<String> list = new ArrayList<String>(company
					.getCreatedBy().getClient().getClientSubscription()
					.getSubscription().getFeatures());
			if (!client.getClientSubscription().isPaidUser()) {
				list.remove(Features.ENCRYPTION);
			}
			companyAndFeatures.setFeatures(list);

			return companyAndFeatures;

		}
	}

	protected String getUserEmail() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				BaseServlet.EMAIL_ID);
	}

	protected byte[] getD2() {
		String d2 = (String) getThreadLocalRequest().getSession().getAttribute(
				BaseServlet.SECRET_KEY_COOKIE);
		if (d2 == null) {
			return null;
		}
		try {
			return Base64.decode(d2);
		} catch (Base64DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(BaseServlet.EMAIL_ID, emailId);
		Client client = (Client) namedQuery.uniqueResult();
		// session.close();
		return client;
	}

	public ClientUser getUser() {
		return getUser(getClient(getUserEmail()));
	}

	private static ClientUser getUser(Client client) {
		User user = client.toUser();
		// user.setFullName(user.getFirstName() + " " + user.getLastName());
		user.setUserRole(RolePermissions.ADMIN);
		user.setAdmin(true);
		user.setCanDoUserManagement(true);
		UserPermissions permissions = new UserPermissions();
		permissions.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		permissions.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
		permissions.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		permissions.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		permissions.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		permissions.setTypeOfViewReports(RolePermissions.TYPE_YES);
		permissions.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		permissions.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);
		user.setPermissions(permissions);
		return user.getClientUser();
	}

	public boolean isValidSession(HttpServletRequest request) {
		return request.getSession().getAttribute(BaseServlet.EMAIL_ID) == null ? false
				: true;
	}

	private boolean CheckUserExistanceAndsetAccounterThreadLocal(
			HttpServletRequest request) {
		Session session = HibernateUtil.getCurrentSession();
		Long serverCompanyID = (Long) request.getSession().getAttribute(
				BaseServlet.COMPANY_ID);
		if (serverCompanyID == null) {
			String create = (String) request.getSession().getAttribute(
					BaseServlet.CREATE);
			if (create != null && create.equals("true")) {
				return true;
			}
			return false;
		}
		if (islockedCompany(serverCompanyID)) {
			return false;
		}
		EU.removeCipher();
		String userEmail = (String) request.getSession().getAttribute(
				BaseServlet.EMAIL_ID);
		User user = BaseServlet.getUser(userEmail, serverCompanyID);
		if (user != null && user.getSecretKey() != null) {
			try {
				EU.createCipher(user.getSecretKey(), getD2(request), request
						.getSession().getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (getCompanySecretFromDB(serverCompanyID) != null) {
			if (!EU.hasChiper()) {
				return false;
			}
		}

		Company company = (Company) session.get(Company.class, serverCompanyID);
		if (company == null || company.isLocked()) {
			return false;
		}
		user = company.getUserByUserEmail(userEmail);
		if (user == null) {
			return false;
		}
		AccounterThreadLocal.set(user);
		return true;
	}

	public byte[] getCompanySecretFromDB(Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getCompanySecret");
		namedQuery.setParameter("companyId", companyId);
		byte[] secret = (byte[]) namedQuery.uniqueResult();
		return secret;
	}

	private boolean islockedCompany(Long companyID) {
		if (companyID == null) {
			return false;
		}
		Session session = HibernateUtil.getCurrentSession();
		Object res = session.getNamedQuery("isCompanyLocked")
				.setLong("companyId", companyID).uniqueResult();
		return res == null ? false : (Boolean) res;
	}

	public byte[] getD2(HttpServletRequest request)
			throws Base64DecoderException {
		String d2 = (String) request.getSession().getAttribute(
				BaseServlet.SECRET_KEY_COOKIE);
		if (d2 == null) {
			return null;
		}
		return Base64.decode(d2);
	}

	@Override
	public ArrayList<AccountsTemplate> getAccountsTemplate()
			throws AccounterException {
		AccountsTemplateManager manager = new AccountsTemplateManager();
		try {
			return manager.loadAccounts(ServerLocal.get());
		} catch (Exception e) {
			throw new AccounterException(e);
		}
	}

	@Override
	public String getCountry() {
		Client client = getClient(getUserEmail());
		return client.getCountry();
	}

	@Override
	public boolean isCompanyNameExists(String companyName)
			throws AccounterException {
		if (companyName == null) {
			return true;
		}
		companyName = companyName.trim().toLowerCase();
		Session hibernateSession = HibernateUtil.getCurrentSession();
		String email = (String) getThreadLocalRequest().getSession()
				.getAttribute(BaseServlet.EMAIL_ID);
		Number clientId = (Number) hibernateSession
				.getNamedQuery("getClientByCompany")
				.setParameter("clientEmail", email)
				.setParameter("companyName", companyName).uniqueResult();
		return clientId != null;
	}

	@Override
	public CountryPreferences getCountryPreferences(String countryName) {
		return new CompanyManager().getCountryPreferences(countryName, "");
	}

	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		moduleBaseURL = moduleBaseURL.replace("ms-wwa://", "http://");
		moduleBaseURL = moduleBaseURL.replace("ms-appx://", "http://");
		return super.doGetSerializationPolicy(request, moduleBaseURL,
				strongName);
	}
}
