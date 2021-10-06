package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.UTF8Control;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.util.SupportedBrowsers;

public class OpenCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Log logger = LogFactory
			.getLog(OpenCompanyServlet.class);
	private static final String REDIRECT_PAGE = "/WEB-INF/Redirect.jsp";
	private static final String USER_NAME = "userName";
	private static final String COMPANY_NAME = "companyName";
	private static final String SUBSCRIPTION = "subscription";
	private static final String USER_AGENT = "User-Agent";
	private static final String SUPPORTED_BROWSERS_URL = "/WEB-INF/supportedbrowsers.jsp";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String header = request.getHeader(USER_AGENT);
		if (header != null) {
			if (!SupportedBrowsers.check(header)) {
				dispatch(request, response, SUPPORTED_BROWSERS_URL);
				return;
			}
		}
		String url = request.getRequestURI().toString();
		String isTouch = (String) request.getSession().getAttribute(IS_TOUCH);
		request.setAttribute(IS_TOUCH, isTouch == null ? "false" : isTouch);

		if (url.equals(ACCOUNTER_OLD_URL)) {
			dispatch(request, response, REDIRECT_PAGE);
			return;
		}
		String emailID = (String) request.getSession().getAttribute(EMAIL_ID);

		if (emailID == null) {
			response.sendRedirect(LOGIN_URL);
			return;
		}
		Session session = HibernateUtil.getCurrentSession();
		FinanceTool financeTool = new FinanceTool();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(BaseServlet.EMAIL_ID, emailID);
		Client client = (Client) namedQuery.uniqueResult();
		String language = "";
		try {
			language = getlocale().getISO3Language();
		} catch (MissingResourceException e) {
			language = "eng";
		}
		HashMap<String, String> keyAndValues = financeTool.getKeyAndValues(
				client.getID(), language);

		for (Entry<String, String> entrySet : keyAndValues.entrySet()) {
			String value = entrySet.getValue();
			if (value.contains("'")) {
				value = value.replace("'", "\\'");
				entrySet.setValue(value);
			}
		}
		Long serverCompanyID = (Long) request.getSession().getAttribute(
				COMPANY_ID);
		request.setAttribute("messages", keyAndValues);
		// Load locale aware date time constants, number format constants

		HashMap<String, String> accounterLocale = getLocaleConstants();
		request.setAttribute("accounterLocale", accounterLocale);
			boolean ispaid = client.getClientSubscription().isPaidUser();
			boolean freeTrial = client.getClientSubscription().isPaidUser() ? false
					: !client.isPremiumTrailDone();
		request.setAttribute("isPaid", ispaid);
		request.setAttribute("freeTrial", freeTrial);
		String create = (String) request.getSession().getAttribute(CREATE);
		if (serverCompanyID == null) {
			if (create != null && create.equals("true")) {
				if (ispaid) {
					byte[] d2 = getD2(request);
					if (d2 == null) {
						response.sendRedirect(LOGIN_URL);
						return;
					}
				}
				if (!canCreateCompany(client)) {
					response.sendRedirect(COMPANIES_URL);
					return;
				}
					request.setAttribute("features", client
							.getClientSubscription().getSubscription()
							.getFeatures());
				RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher(getAccountView());
				dispatcher.forward(request, response);
				return;
			} else {
				response.sendRedirect(COMPANIES_URL);
				return;
			}
		}

		// initComet(request.getSession(), serverCompanyID, emailID);
		// we are create comet in getCompany method

		try {
			Transaction transaction = session.beginTransaction();

			int openedCompaniesCount = client.getOpenedCompaniesCount() + 1;
			client.setOpenedCompaniesCount(openedCompaniesCount);
			session.saveOrUpdate(client);

			User user = getUser(emailID, serverCompanyID);
			if (user == null) {
				response.sendRedirect(COMPANIES_URL);
				return;
			}

			user = HibernateUtil.initializeAndUnproxy(user);
			request.setAttribute(EMAIL_ID, user.getClient().getEmailId());
			request.setAttribute(USER_ID, user.getID());
			request.setAttribute(USER_NAME, user.getClient().getFullName());

			AccounterThreadLocal.set(user);
			if (getCompanySecretFromDB(serverCompanyID) != null) {
				try {
					if (user.getSecretKey() == null) {
						request.setAttribute("showResetLink",
								canResetPassword(serverCompanyID));
						Query query = session
								.getNamedQuery("getHint.by.company");
						query.setParameter("companyId", serverCompanyID);
						String hint = (String) query.uniqueResult();
						request.setAttribute("hint", hint);
						dispatch(request, response,
								"/WEB-INF/companypassword.jsp");
						return;
					}
					EU.createCipher(user.getSecretKey(), getD2(request),
							request.getSession().getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Company company = getCompany(request);
			if (company.isLocked()) {
				request.getSession().setAttribute(COMPANY_ID, null);
				response.sendRedirect(COMPANIES_URL + "?message=locked");
				return;
			}
			User createdBy = company.getCreatedBy();
			if (createdBy != null) {
				request.setAttribute("features", createdBy.getClient()
						.getClientSubscription().getSubscription()
						.getFeatures());
			} else {
				request.setAttribute("features", new HashSet<String>());
			}

			request.setAttribute("emailId", emailID);
				request.setAttribute(COMPANY_NAME, company.getDisplayName()
						+ " - " + company.getID());
				Activity activity = new Activity(company, user,
						ActivityType.LOGIN);
			session.save(activity);
			transaction.commit();
			// if (client.getClientSubscription() != null) {
			// request.setAttribute(SUBSCRIPTION, client
			// .getClientSubscription().getId());
			// }
			RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher(getAccountView());
			dispatcher.forward(request, response);
		} finally {
		}
	}

	private boolean canCreateCompany(Client client) {
		return client.getClientSubscription().isPaidUser()
				|| getCompaniesCount(client.getUsers()) == 0;
	}

	private int getCompaniesCount(Set<User> users) {
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
		}
		return objects.size();
	}

	private boolean canResetPassword(Long serverCompanyID) {
		Session session = HibernateUtil.getCurrentSession();
		Boolean support = (Boolean) session
				.getNamedQuery("get.company.contactsupport")
				.setParameter("companyId", serverCompanyID).uniqueResult();
		return support;
	}

	private HashMap<String, String> getLocaleConstants() {
		HashMap<String, String> result = new HashMap<String, String>();

		String files[] = { "DateTimeConstantsImpl", "NumberConstantsImpl" };

		for (String file : files) {
			ResourceBundle dateTimeConstants = ResourceBundle.getBundle(
					"com.vimukti.accounter.web.server.i18n.constants." + file,
					getlocale(), new UTF8Control());
			for (String key : dateTimeConstants.keySet()) {
				String value = dateTimeConstants.getString(key);
				String[] splites = value.split("(?<!\\\\), ");
				if (splites.length == 1) {
					result.put(key, getValue(value));
				} else {
					result.put(key, joinArray(splites));
				}
			}
		}

		return result;
	}

	private String joinArray(String[] value) {
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		if (value.length > 0) {
			for (int x = 0; x < value.length - 1; x++) {
				buffer.append('\'');
				buffer.append(getValue(value[x]));
				buffer.append('\'');
				buffer.append(", ");
			}
			// add last one
			buffer.append('\'');
			buffer.append(getValue(value[value.length - 1]));
			buffer.append('\'');
		}
		buffer.append(']');
		return buffer.toString();
	}

	private String getValue(String string) {
		return string.replaceAll("\'", "\\\\\'");
	}

	private Locale getlocale() {
		return ServerLocal.get();
	}

	// /**
	// * Initialising comet stuff
	// *
	// * @param request
	// * @param identity
	// */
	// private void initComet(HttpSession httpSession, long companyID,
	// String emailID) {
	// // Stream must be created otherwise user will get data
	// // Continuously and browser will struck
	// CometSession cometSession = CometServlet.getCometSession(httpSession);
	// CometManager.initStream(httpSession.getId(), companyID, emailID,
	// cometSession);
	// }

	private String getAccountView() {
		if (ServerConfiguration.isDesktopApp()) {
			return "/WEB-INF/Accounter_desk.jsp";
		} else {
			return "/WEB-INF/Accounter.jsp";
		}
	}
}
