package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientSubscription;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.ServerConvertUtil;
import com.nitya.accounter.core.Subscription;
import com.nitya.accounter.core.User;
import com.nitya.accounter.core.UserPermissions;
import com.nitya.accounter.main.ServerLocal;
import com.nitya.accounter.utils.HexUtil;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.utils.SecureUtils;
import com.nitya.accounter.utils.Security;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccountsTemplate;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.TemplateAccount;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.client.uibinder.setup.SetupWizard;
import com.nitya.accounter.web.server.AccountsTemplateManager;

public class SignupServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/signup.jsp";
	List<AccountsTemplate> allAccounts = new ArrayList<AccountsTemplate>();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String header2 = req.getHeader("User-Agent");
		boolean contains = header2.contains("iPad");
		if (contains) {
			req.setAttribute("ipad", contains);
		}

		dispatch(req, resp, view);

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		boolean response  = true;
//		boolean response = isBotTryingToLogin("https://www.google.com/recaptcha/api/siteverify", "app.annaccounting.com", "secret=6LdZG5IUAAAAAFeKvI-MQ3BIfxJOOVsLr2B3_5X9&response="+req.getParameter("google-key").trim());
		resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Credentials","true");
        resp.setHeader("Access-Control-Max-Age", "86400");
		resp.setContentType("application/json");
		// Take userName from request
		String emailId = req.getParameter("emailId").trim();
		// String confirmEmailId = req.getParameter("confirmemailId").trim();
		String firstName = req.getParameter("firstName").trim();
		String lastName = req.getParameter("lastName").trim();
		String password = req.getParameter("password").trim();
		// String phoneNumber = req.getParameter("phoneNumber").trim();
		String country = req.getParameter("country").trim();
		boolean isSubscribedToNewsLetter = true;
		/*
		 * if (req.getParameter("newsletter") != null &&
		 * req.getParameter("newsletter").equals("on")) isSubscribedToNewsLetter
		 * = true;
		 */

		boolean isAgreed = true;
		// if (req.getParameter("agree") != null
		// && req.getParameter("agree").equals("on"))
		// isAgreed = true;
		// if (!isAgreed) {
		// dispatchMessage(Global.get().messages().pleaseacceptTermsofuse(),
		// req, resp, view);
		// }

		if (!isValidInputs(NAME, firstName, lastName, country)
				|| !isValidInputs(MAIL_ID, emailId)) {
			dispatchMessage(Global.get().messages().incorrectEmailOrPassWord(),
					req, resp, view);
			return;
		}
		
		if(!response) {
			req.setAttribute("errormessage", Global.get().messages().errorMsg("Something wrong with your signup, please reload the page and try again."));
			dispatch(req, resp, view);
			return;
		}
		
		
		// if (!emailId.equals(confirmEmailId)) {
		// dispatchMessage(Global.get().messages()
		// .emailIdAndConfirmEmaildMustBeSame(), req, resp, view);
		// return;
		// }
		emailId = emailId.toLowerCase();
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(emailId
				+ Client.PASSWORD_HASH_STRING + password));
		HttpSession session = req.getSession(true);
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = hibernateSession.beginTransaction();
			// Have to check UserExistence
			if (getClient(emailId) != null) {
				// If Exists then send to login password with username
				// TODO::: in login.jsp check for email id in the request if it
				// is available set this email id in the email id field of login
				// page
				// redirectExternal(req, resp, LOGIN_URL);
				Client client = getClient(emailId);
				if (client.isDeleted()) {
					//client.setActive(true);
					client.setUsers(new HashSet<User>());
					client.setEmailId(emailId);
					client.setFirstName(firstName);
					client.setLastName(lastName);
					client.setFullName(Global.get().messages()
							.fullName(firstName, lastName));
					client.setPassword(passwordWithHash);
					client.setPasswordRecoveryKey(EU.encryptPassword(password));
					client.setPhoneNo("");
					client.setCountry(country);
					client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);
					ClientSubscription clientSubscription = new ClientSubscription();
					clientSubscription.setCreatedDate(new Date());
					clientSubscription.setLastModified(new Date());
					clientSubscription.setSubscription(Subscription
							.getInstance(Subscription.PREMIUM_USER));
					clientSubscription
							.setDurationType(ClientSubscription.UNLIMITED_USERS);
					saveEntry(clientSubscription);

					client.setClientSubscription(clientSubscription);
					client.setDeleted(false);
					client.setActive(true);
					saveEntry(client);
					
					// Saving the JNP companies Values
//					if(req.getParameter("companyUid") != null)
//					{
//						
//						Company companyDetails = new  Company();
//						companyDetails.setConfigured(true);
//						companyDetails.setCompanyEmail(req.getParameter("companyEmail").trim());
//						companyDetails.setEncryptionKey(SecureUtils.createID(16));
//						companyDetails.setContactSupport(false);
//						companyDetails.setLocked(false);
//						companyDetails.setDeleted(false);
//						companyDetails.setBookKeeping(false);
//						companyDetails.setTradingName(req.getParameter("companyName")+" "+ req.getParameter("companyAlias"));
//						Address clientAddress = new Address();
//						clientAddress.setAddress1(req.getParameter("companyAddress"));
//						String test = req.getParameter("companyAddress");
//						companyDetails.setRegisteredAddress(clientAddress);
//						companyDetails.setTradingAddress(clientAddress);
//						companyDetails.setTimezone("UTC-7:00 America/Denver");
//						User user = new User();
//						user.setActive(true);
//						user.setClient(client);
//						user.setAdmin(true);
//						user.setCanDoUserManagement(true);
//						user.setDeleted(false);
//						companyDetails.setCreatedBy(user);
//						CompanyPreferences companyPref = new CompanyPreferences();
//						companyPref.setTradingName(req.getParameter("companyName"));
//						companyPref.setPhone(req.getParameter("companyPhone"));
//						companyPref.setFax(req.getParameter("companyFax"));
//						companyPref.setCity("Alabama");
//						companyPref.setIndustryType(33);
//						companyPref.setWebSite(req.getParameter("companyUrl"));
//						FinanceDate date = new FinanceDate(20211001);
//						companyPref.setStartOfFiscalYear(date);
//						companyPref.setStartDate(date);
//						companyPref.setDepreciationStartDate(date);
//						date = new FinanceDate(20220930);
//						companyPref.setEndOfFiscalYear(date);
//						companyDetails.setPreferences(companyPref);
//						saveEntry(companyPref);
//					}
					client.getID();
					//session.setAttribute(EMAIL_ID, emailId);
					
					sendActivationEmail(createActivation(emailId), client); // Send to SignUp Success
					String message = "?message=" + ACT_FROM_SIGNUP;
					
//					String paramDest = (String) session
//							.getAttribute(PARAM_DESTINATION);
//					if (paramDest != null) {
//						redirectExternal(req, resp, paramDest);
//						session.removeAttribute(paramDest);
//					} else {
						//redirectExternal(req, resp, COMPANIES_URL);
						redirectExternal(req, resp, ACTIVATION_URL + message);
//					}
					transaction.commit();

					// SEND WELCOME MAIL TO SIGNUP USER
					sendWelComeMail(firstName, emailId);
				} else {
					req.setAttribute("errormessage", Global.get().messages()
							.alreadyRegisteredWithAccounter()
							+ " <a href=\"/main/login\">"
							+ Global.get().messages().here()
							+ "</a> "
							+ Global.get().messages().toLogin());
					dispatch(req, resp, view);
					return;

				}
			} else {
				// else

				// Create Client and Save
				Client client = new Client();
				//client.setActive(true);
				client.setUsers(new HashSet<User>());
				client.setEmailId(emailId);
				client.setFirstName(firstName);
				client.setLastName(lastName);
				client.setFullName(firstName + " " + lastName);
				client.setPassword(passwordWithHash);
				client.setPasswordRecoveryKey(EU.encryptPassword(password));
				client.setCountry(country);
				client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);

				// clientSubscription.setCreatedDate(new Date(System
				// .currentTimeMillis()));
				// Set<String> members = new HashSet<String>();
				// members.add(emailId);
				// clientSubscription.setMembers(members);
				// Subscription subscription = new Subscription();
				// subscription.setName("");
				// Set<String> features = new HashSet<String>();
				// subscription.setFeatures(features);
				// clientSubscription.setSubscription(subscription);
				client.setCreatedDate(new FinanceDate());
				ClientSubscription clientSubscription = new ClientSubscription();
				clientSubscription.setCreatedDate(new Date());
				clientSubscription.setLastModified(new Date());
				clientSubscription
						.setPremiumType(ClientSubscription.UNLIMITED_USERS);
				// if (!ServerConfiguration.isDesktopApp()) {
				// clientSubscription.setSubscription(Subscription
				// .getInstance(Subscription.FREE_CLIENT));
				// } else {
				clientSubscription.setSubscription(Subscription
						.getInstance(Subscription.PREMIUM_USER));
				// }
				saveEntry(clientSubscription);
				client.setClientSubscription(clientSubscription);

				client.setDeleted(false);
				saveEntry(client);
				
				// JnP companies creation
				if(req.getParameter("companyId") != null)
				{
					
					ClientCompanyPreferences preferences = getPreferences(req,resp);
					ArrayList<TemplateAccount> accounts = new ArrayList<TemplateAccount>();
					AccountsTemplateManager manager = new AccountsTemplateManager();
					try {
						allAccounts = manager.loadAccounts(ServerLocal.get());
					} catch (Exception e) {
						e.printStackTrace();
					}			
					
					AccountsTemplate accountsTemplate = allAccounts.get(preferences.getIndustryType()-1);
					accounts.addAll(accountsTemplate.getAccounts());
					
					Company company = new Company();
					company.setTradingName(preferences.getTradingName());
					company.setConfigured(false);
					company.setCreatedDate(new Date());
					company.setVersion(Company.CURRENT_VERSION);
					company.setConfigured(true);
					
					CompanyPreferences serverCompanyPreferences = company
							.getPreferences();
					
					serverCompanyPreferences = new ServerConvertUtil().toServerObject(
							serverCompanyPreferences, preferences, hibernateSession);	
					User user = new User();
					user.setUserRole(RolePermissions.ADMIN);
					user.setAdmin(true);
					user.setCanDoUserManagement(true);
					user.setActive(true);
					user.setClient(client);
					user.setCompany(company);
					UserPermissions userPermissions = new UserPermissions();
					userPermissions.setTypeOfBankReconcilation(1);
					userPermissions.setTypeOfInvoicesBills(1);
					userPermissions.setTypeOfPayBillsPayments(1);
					userPermissions.setTypeOfCompanySettingsLockDates(1);
					userPermissions.setTypeOfViewReports(1);
					userPermissions.setTypeOfManageAccounts(1);
					userPermissions.setTypeOfInventoryWarehouse(1);
					userPermissions.setTypeOfSaveasDrafts(1);
					userPermissions.setAllowUsersToAttendance(0);
					userPermissions.setInvoicesAndPayments(0);
					user.setPermissions(userPermissions);
					saveEntry(user);
					
					company.setCreatedBy(user);
					client.getUsers().add(user);
					saveEntry(client);
					
					company.getUsers().add(user);
					company.setCompanyEmail(user.getClient().getEmailId());
					company.setConfigured(true);
					company.setEncryptionKey(SecureUtils.createID(16));
					Currency primaryCurrency = serverCompanyPreferences
							.getPrimaryCurrency();
					primaryCurrency.setCompany(company);
					hibernateSession.save(primaryCurrency);
					Address clientAddress = new Address();
					clientAddress.setAddress1(req.getParameter("companyAddress"));
					clientAddress.setCountryOrRegion(req.getParameter("country"));
					company.setRegisteredAddress(clientAddress);
					company.setTradingAddress(clientAddress);
					hibernateSession.save(company);

					// Updating CompanyPreferences
					
					company.getRegisteredAddress().setCountryOrRegion(
							client.getCountry());
					company.setRegisteredAddress(serverCompanyPreferences
							.getTradingAddress());
					// Initializing Accounts
					company.setPreferences(serverCompanyPreferences);
					company.setCreatedBy(user);
//					company.initialize(accounts);
//					company.setPasswordHInt(passwordHint);
					
					hibernateSession.save(company);

					// Creating Accounts Receivables and Payables for Primary Currency
					primaryCurrency.setAccountsReceivable(company
							.getAccountsReceivableAccount());
					primaryCurrency.setAccountsPayable(company
							.getAccountsPayableAccount());
					hibernateSession.saveOrUpdate(primaryCurrency);
					
					
					
//					Company companyDetails = new  Company();
//					companyDetails.setConfigured(true);
//					companyDetails.setCompanyEmail(req.getParameter("companyEmail").trim());
//					companyDetails.setEncryptionKey(SecureUtils.createID(16));
//					companyDetails.setContactSupport(false);
//					companyDetails.setLocked(false);
//					companyDetails.setDeleted(false);
//					companyDetails.setBookKeeping(false);
//					companyDetails.setTradingName(req.getParameter("companyName")+" "+ req.getParameter("companyAlias"));
//					Address clientAddress = new Address();
//					clientAddress.setAddress1(req.getParameter("companyAddress"));
//					companyDetails.setRegisteredAddress(clientAddress);
//					companyDetails.setTradingAddress(clientAddress);
//					companyDetails.setTimezone("UTC-7:00 America/Denver");
//					CompanyPreferences companyPref = new CompanyPreferences();
//					companyPref.setTradingName(req.getParameter("companyName"));
//					companyPref.setPhone(req.getParameter("companyPhone"));
//					companyPref.setFax(req.getParameter("companyFax"));
//					companyPref.setTradingAddress(clientAddress);
//					companyPref.setCity("Alabama");
//					companyPref.setIndustryType(33);
//					companyPref.setWebSite(req.getParameter("companyUrl"));
//					FinanceDate date = new FinanceDate(20211001);
//					companyPref.setStartOfFiscalYear(date);
//					companyPref.setStartDate(date);
//					companyPref.setDepreciationStartDate(date);
//					date = new FinanceDate(20220930);
//					companyPref.setEndOfFiscalYear(date);
//					companyDetails.setPreferences(companyPref);
//					
//					user.setUserRole(RolePermissions.ADMIN);
//					user.setAdmin(true);
//					user.setCanDoUserManagement(true);
//					user.setActive(true);
//					user.setClient(client);
//					user.setCompany(companyDetails);
//					UserPermissions userPermissions = new UserPermissions();
//					userPermissions.setTypeOfBankReconcilation(1);
//					userPermissions.setTypeOfInvoicesBills(1);
//					userPermissions.setTypeOfPayBillsPayments(1);
//					userPermissions.setTypeOfCompanySettingsLockDates(1);
//					userPermissions.setTypeOfViewReports(1);
//					userPermissions.setTypeOfManageAccounts(1);
//					userPermissions.setTypeOfInventoryWarehouse(1);
//					userPermissions.setTypeOfSaveasDrafts(1);
//					userPermissions.setAllowUsersToAttendance(0);
//					userPermissions.setInvoicesAndPayments(0);
//					user.setPermissions(userPermissions);
//					saveEntry(user);
//					user.setDeleted(false);
//					companyDetails.setCreatedBy(user);
//					saveEntry(companyDetails);
				}
				//session.setAttribute(EMAIL_ID, emailId);

				// SEND WELCOME MAIL TO SIGNUP USER
				sendWelComeMail(firstName, emailId);
				
				sendActivationEmail(createActivation(emailId), client); // Send to SignUp Success
				String message = "?message=" + ACT_FROM_SIGNUP;

				// Email to that user.
				/*
				 * sendActivationEmail(token, client); // Send to SignUp Success
				 * View String message = "?message=" + ACT_FROM_SIGNUP;
				 * 
				 * redirectExternal(req, resp, ACTIVATION_URL + message);
				 */
//				String paramDest = (String) session
//						.getAttribute(PARAM_DESTINATION);
//				if (paramDest != null) {
//					redirectExternal(req, resp, paramDest);
//					session.removeAttribute(paramDest);
//				} else {
					//redirectExternal(req, resp, COMPANIES_URL);
				if(req.getParameter("loginType") != null) {
//					resp.setHeader("Access-Control-Allow-Origin", "*");
//			        resp.setHeader("Access-Control-Allow-Methods", "POST");
//			        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
//			        resp.setHeader("Access-Control-Max-Age", "86400");
					client.setActive(true);
					saveEntry(client);
					Query query = hibernateSession
							.getNamedQuery("delete.activation.by.emailId");
					query.setParameter("emailId", emailId
							.trim());
					int updatedRows = query.executeUpdate();
					log("No of updated rows = " + updatedRows);
					}
				else
					{redirectExternal(req, resp, ACTIVATION_URL + message);
				}
				transaction.commit();
				try {
					NewLoginServlet.createD2(req, emailId, password);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		}

		return;
	}

	private ClientUser getUser(Client client) {
		User user = client.toUser();
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

	private ClientCompanyPreferences getPreferences(HttpServletRequest req, HttpServletResponse resp) {
		ClientCompanyPreferences preferences = new ClientCompanyPreferences();
	
		preferences.setCity("Alabama");
		preferences.setDecimalCharacte(".");
		preferences.setDefaultTaxCode(0);
		preferences.setEinNumber("0");
		preferences.setEndOfFiscalYear(20220931);
		preferences.setFax(req.getParameter("companyFax"));
		preferences.setFiscalYearFirstMonth(9);
		preferences.setHave1099contractors(false);
		preferences.setHaveEpmloyees(false);
		preferences.setHaveW_2Employees(false);
		preferences.setIndustryType(33);
		preferences.setBeginingorTodaysdate(false);
		preferences.setPayrollOnly(false);
		preferences.setShowLegalName(true);
		preferences.setLegalName(req.getParameter("companyName"));
		preferences.setLocationTrackingId(0);
		preferences.setLogSpaceUsed(0.0);
		preferences.setOrganizationType(0);
		preferences.setPhone(req.getParameter("companyPhone"));
		preferences.setPreventPostingBeforeDate(0);
		ClientCurrency clientCurrency = new ClientCurrency();
		clientCurrency.setName("United States Dollar");
		clientCurrency.setSymbol("$");
		clientCurrency.setFormalName("USD");
		clientCurrency.setID(0);
		preferences.setPrimaryCurrency(clientCurrency);
		preferences.setReferCustomers(0);
		preferences.setReferVendors(0);
		preferences.setRoundingAccount(0);
		preferences.setStateEmpNo("0");
		preferences.setTaxId("");
		preferences.setTimezone("UTC-7:00 America/Denver");
		preferences.setTrackEmployeeExpenses(false);
		preferences.setTrackFinanceDate(0);
		ClientAddress clientAddress = new ClientAddress();
		clientAddress.setAddress1(req.getParameter("companyAddress"));
		preferences.setTradingAddress(clientAddress);
		preferences.setTradingName(req.getParameter("companyName"));
		preferences.setVersion(0);
		preferences.setWebSite(req.getParameter("companyUrl"));
		preferences.setCompanyEmail(req.getParameter("companyEmail"));
		preferences.setStartOfFiscalYear(20211001);
//		preferences.setPreferencesFlag(57871255527);
		preferences.setDepreciationStartDate(20220901);
		return preferences;
	}

}
