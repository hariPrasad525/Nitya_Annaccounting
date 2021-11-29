package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientSubscription;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.License;
import com.nitya.accounter.core.SupportedUser;
import com.nitya.accounter.core.User;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.services.SubscriptionTool;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.Features;

public class CompaniesServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final String MIGRATION_VIEW = "/WEB-INF/companyMigration.jsp";

	private String companiedListView = "/WEB-INF/companylist.jsp";

	private String companyListViewForApp = "/WEB-INF/companylist_desk.jsp";
	private static final String licenseExpired = "/WEB-INF/licenseExpired.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession httpSession = req.getSession();
		Session session1 = HibernateUtil.getCurrentSession();
		String header2 = req.getHeader("User-Agent");
		boolean contains = header2.contains("iPad");
		if (contains) {
			req.setAttribute("ipad", contains);
		}

		String emailID = (String) httpSession.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}
		
		
		checkForStatus(req);

		String companyID = req.getParameter(COMPANY_ID);
		String emailId = req.getParameter("userMail");
		if(emailId != null)
		{
			emailId = emailId.toLowerCase();
			Company company = new Company();
			List result = session1.getNamedQuery("getCompanyByMailId")
							.setParameter("emailId",  emailId)
							.list();
			Iterator iterator = result.iterator();
			Object[] object = null;
			while(iterator.hasNext())
			{
				object = (Object[]) iterator.next();
				company.setId((long) object[0]);
//				company.setConfigured((boolean) object[0]);
				break;
			}
			
			
			companyID = company.getId()+"";
		}
	
		// for delete account from user profile
		if (companyID == null
				&& httpSession.getAttribute("cancelDeleteAccountcompany") != null) {
			companyID = String.valueOf(httpSession
					.getAttribute("cancelDeleteAccountcompany"));
			httpSession.removeAttribute("cancelDeleteAccountcompany");
		}
		if (companyID != null) {
			openCompany(req, resp, Long.parseLong(companyID));
			return;
		}
		
		String create = req.getParameter(CREATE);
		if (create != null && create.equals("true")) {
			createCompany(req, resp);
			return;
		}

		Session session = HibernateUtil.getCurrentSession();
		try {
			Client client = getClient(emailID);
			if (client == null) {
				redirectExternal(req, resp, LOGIN_URL);
				return;
			}

			String type = req.getParameter("type");
			if (client.getClientSubscription().isInTracePeriod()
					&& (type == null)) {
				if (hasMoreUsers(client.getClientSubscription())) {
					dispatchGracePeriod(req, resp, client);
					return;
				}
			}
			List<Company> list = getCompanyList(client.getUsers());
			List<Object[]> encrypt = getCompaniesToEncrypt(client.getUsers());
			boolean canEncrypt = false;
			if (encrypt.size() > 0) {
				canEncrypt = true;
			}

			
			// boolean freeTrial = client.getClientSubscription().isPaidUser() ?
			// false
			// : !client.isPremiumTrailDone();
			boolean freeTrial = !client.isPremiumTrailDone();

			req.setAttribute("emailId", emailID);
			req.setAttribute("enableEncryption", client.getClientSubscription()
					.isPaidUser()
					&& client.getClientSubscription().getSubscription()
							.getFeatures().contains(Features.ENCRYPTION));
			req.setAttribute("freeTrial", freeTrial);
			int isAttendanceOnly = 0;
			if(client.getUsers().isEmpty()) {
			isAttendanceOnly = 2; //Just like PlaceHolder
			} else {
			isAttendanceOnly = client.getUsers().iterator().next().getPermissions().getAllowUsersToAttendance();
			}

			if (!client.getClientSubscription().isPaidUser()) {
			req.setAttribute("canCreate", (list.size() == 0));
			req.setAttribute("isPaid", false);
			} else {
			req.setAttribute("canCreate", true);
			req.setAttribute("isPaid", true);
			}
			if(isAttendanceOnly == 1) {
			req.setAttribute("canCreate", false);
			//req.setAttribute("isPaid", false);
			}	
			
			
			if (req.getAttribute("message") == null) {
				if (list.isEmpty()
						&& httpSession.getAttribute(COMPANY_CREATION_STATUS) == null) {
					req.setAttribute(
							"message",
							Global.get()
									.messages()
									.youDontHaveAny(
											Global.get().messages().companies()));
				} else {
					req.setAttribute("message", Global.get().messages()
							.clickOnTheCompanyNameToOpen());
				}
			}
			req.setAttribute(ATTR_COMPANY_LIST, list);
			req.setAttribute("encrypt", canEncrypt);
			req.getSession().removeAttribute(COMPANY_ID);
			EU.removeCipher();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String parameter = req.getParameter("message");
		if (parameter != null && parameter.equals("locked")) {
			req.setAttribute("message", Global.get().messages()
					.yourCompanyHasBeenLocked());
		}
		if (ServerConfiguration.isDesktopApp()) {
			checkLicense(req, resp);
		} else {
			dispatch(req, resp, companiedListView);
		}
	}

	private void checkLicense(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession httpSession = req.getSession();
		String emailID = (String) httpSession.getAttribute(EMAIL_ID);
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getLicense");
		List<License> list = query.list();
		if (list.isEmpty()) {
			dispatch(req, resp, licenseExpired);
			return;
		}
		for (License license : list) {
			if (license.isActive() && !license.isValid()) {
				dispatch(req, resp, licenseExpired);
				return;
			}
		}
		dispatch(req, resp, companyListViewForApp);
	}

	private List<Object[]> getCompaniesToEncrypt(Set<User> users) {
		List<Long> userIds = new ArrayList<Long>();
		for (User user : users) {
			if (!user.isDeleted()) {
				userIds.add(user.getID());
			}
		}
		List<Object[]> list = new ArrayList<Object[]>();
		if (!userIds.isEmpty()) {
			Session session = HibernateUtil.getCurrentSession();
			list = session
					.getNamedQuery("get.NonEncrypted.CompanyNames.by.client")
					.setParameterList("userIds", userIds).list();
		}
		return list;
	}

	private List<Company> getCompanyList(Set<User> users) {
		List<Company> list = new ArrayList<Company>();
		List<Long> userIds = new ArrayList<Long>();
		for (User user : users) {
			if (!user.isDeleted()) {
				userIds.add(user.getID());
			}
		}
		List<Object[]> objects = new ArrayList<Object[]>();
		if (!userIds.isEmpty()) {
			Session session = HibernateUtil.getCurrentSession();
			objects = session
					.getNamedQuery(
							"get.CompanyId.Tradingname.and.Country.of.user")
					.setParameterList("userIds", userIds).list();
			addCompanies(list, objects);
		}
		return list;
	}

	private boolean hasMoreUsers(ClientSubscription clientSubscription) {
		int size = clientSubscription.getMembers().size();
		int premiumType = clientSubscription.getPremiumType();
		switch (premiumType) {
		case ClientSubscription.UNLIMITED_USERS:
			return false;
		case ClientSubscription.FIVE_USERS:
			return size > 5;
		case ClientSubscription.TWO_USERS:
			return size > 2;
		default:
			return size > 1;
		}
	}

	private void dispatchGracePeriod(HttpServletRequest req,
			HttpServletResponse resp, Client client) {
		ClientSubscription subscription = client.getClientSubscription();
		req.setAttribute("expiredDate", subscription.getExpiredDateAsString());
		int days = (int) (subscription.getGracePeriodDate().getTime() - (new Date()
				.getTime())) / (24 * 60 * 60 * 1000);
		req.setAttribute("remainigDays", days);
		req.setAttribute("userName", client.getFullName());
		req.setAttribute("premiumType", subscription.getPremiumType());
		Set<String> members = SubscriptionTool.getDeletedMembers(
				subscription.getMembers(), client.getEmailId(),
				subscription.getPremiumType());
		if (members.isEmpty()) {
			members = null;
		}
		req.setAttribute("users", members);

		dispatch(req, resp, "/WEB-INF/graceperiod.jsp");
	}

	private void addCompanies(List<Company> list, List<Object[]> objects) {
		for (Object[] obj : objects) {
			Company com = new Company();
			com.setId((Long) obj[0]);
			com.getPreferences().setTradingName((String) obj[1]);
			com.getRegisteredAddress().setCountryOrRegion((String) obj[2]);

			list.add(com);
		}
	}

	private boolean isSupportUser(String emailId) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Object load = currentSession.get(SupportedUser.class, emailId);
		return load != null;
	}

	private void createCompany(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		addMacAppCookie(req, resp);

		String url = ACCOUNTER_OLD_URL;
		if (ServerConfiguration.isDebugMode) {
			url = ACCOUNTER_URL;
		}

		HttpSession httpSession = req.getSession();
		httpSession.setAttribute(CREATE, "true");
		httpSession.removeAttribute(COMPANY_ID);
		req.getSession()
				.setAttribute(IS_TOUCH, "" + req.getParameter(IS_TOUCH));
		redirectExternal(req, resp, url);
	}

	/**
	 * @param httpSession
	 * 
	 */
	private void checkForStatus(HttpServletRequest req) {
		HttpSession httpSession = req.getSession();

		// Checking CreateCompany Status
		String status = (String) httpSession
				.getAttribute(COMPANY_CREATION_STATUS);
		if (status != null) {
			if (status.equals("Success")) {
				req.setAttribute("message", Global.get().messages()
						.yourCompanyIsCreatetedSuccessfully());
			} else {
				req.setAttribute("message", Global.get().messages()
						.companyCreatedFailed());
			}
			httpSession.removeAttribute(COMPANY_CREATION_STATUS);
		}

		// Checking DeleteCompany Status
		String deleteStatus = (String) httpSession
				.getAttribute(COMPANY_DELETION_STATUS);
		if (deleteStatus != null) {
			if (deleteStatus.equals("Success")) {
				req.setAttribute("message", Global.get().messages()
						.yourCompanyIsDeletedSuccessfully());
			} else {
				Object failureMessage = httpSession
						.getAttribute("DeletionFailureMessage");
				if (failureMessage != null) {
					req.setAttribute("message", Global.get().messages()
							.companyDeletionFailed()
							+ " " + failureMessage);
				} else {
					req.setAttribute("message", Global.get().messages()
							.companyDeletionFailed());
				}
			}
			httpSession.removeAttribute("DeletionFailureMessage");
			httpSession.removeAttribute(COMPANY_DELETION_STATUS);
		}

		String accountDeleteStatus = (String) httpSession
				.getAttribute(ACCOUNT_DELETION_STATUS);
		if (accountDeleteStatus != null) {
			req.setAttribute("message", Global.get().messages()
					.accountDeletionFailed());
			httpSession.removeAttribute("DeletionFailureMessage");
			httpSession.removeAttribute(ACCOUNT_DELETION_STATUS);
		}

	}

	/**
	 * @throws IOException
	 * 
	 */
	private void openCompany(HttpServletRequest req, HttpServletResponse resp,
			long companyID) throws IOException {
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute(COMPANY_ID, companyID);
		httpSession.setAttribute(IS_TOUCH, req.getParameter(IS_TOUCH));
		addMacAppCookie(req, resp);

		Session session = HibernateUtil.getCurrentSession();

		Company company = (Company) session.get(Company.class, companyID);
		if (company != null) {

			// if (!company.isActive()) {
			// dispatch(req, resp, MIGRATION_VIEW);
			// return;
			// }
			String url = ACCOUNTER_URL;
			if (ServerConfiguration.isDebugMode) {
				url = ACCOUNTER_URL;
			}

			redirectExternal(req, resp, url);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	// private void addCompanyCookies(HttpServletResponse resp, long companyID)
	// {
	// Cookie companyCookie = new Cookie(COMPANY_COOKIE,
	// String.valueOf(companyID));
	// companyCookie.setMaxAge(-1);// Two week
	// companyCookie.setPath("/");
	// companyCookie.setDomain(ServerConfiguration.getServerCookieDomain());
	// resp.addCookie(companyCookie);
	// }

	private void addMacAppCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String header = request.getHeader("Nativeapp");
		boolean isNative = (header != null && !header.isEmpty());
		if (isNative) {
			Cookie macAppCookie = new Cookie("Nativeapp", "Mac App");
			macAppCookie.setPath("/");
			response.addCookie(macAppCookie);
		}
	}
}
