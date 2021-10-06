package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.common.util.Base64DecoderException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gwt.json.client.JSONException;
import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.server.FinanceTool;

public class BaseServlet extends HttpServlet {
	public static final String USER_ID = "userID";
	public static final String ACTIVATION_TOKEN = "activationToken";
	public static final String ACTIVATION_TYPE = "resetpassword";
	public static final String OUR_COOKIE = "_accounter_01_infinity_22";

	public static final String COMPANY_COOKIE = "cid";
	public static final String SECRET_KEY_COOKIE = "skc";

	public static final String COMPANY_ID = "companyId";

	public static final String CREATE = "create";

	public static final String EMAIL_ID = "emailId";
	public static final String PASSWORD = "password";
	protected static final String COMPANY_CREATION_STATUS = "comCreationStatus";
	protected static final String COMPANY_DELETION_STATUS = "deleteCompanyStatus";
	protected static final String ACCOUNT_DELETION_STATUS = "accountCompanyStatus";

	protected static final String COMPANY_DELETING = "Deleting";
	protected static final String COMPANY_CREATING = "Creating";
	protected static final String ACCOUNT_DELETING = "AccountDeleting";

	protected static final String PARAM_DESTINATION = "destination";
	protected static final String PARAM_SERVER_COMPANY_ID = "serverCompanyId";
	protected static final String PARAM_COMPANY_TYPE = "companyType";
	protected static final String PARA_COMPANY_NAME = "companyName";
	protected static final String PARAM_FIRST_NAME = "firstName";
	protected static final String PARAM_LAST_NAME = "lastName";
	protected static final String PARAM_COUNTRY = "country";
	protected static final String PARAM_PH_NO = "phNo";

	protected static final String IS_TOUCH = "isTouch";

	protected static final String ATTR_MESSAGE = "message";
	protected static final String ATTR_COMPANY_LIST = "companeyList";

	protected static final String LOGIN_URL = "/main/login";
	protected static final String LOGOUT_URL = "/main/logout";
	protected static final String ACCOUNTER_OLD_URL = "/accounter";
	protected static final String ACCOUNTER_URL = "/company/accounter";
	protected static final String MAINTANANCE_URL = "/main/maintanance";

	protected static final String RESET_PASSWORD_URL = "/main/resetpassword";
	public static final String COMPANIES_URL = "/main/companies";
	protected static final String ACTIVATION_URL = "/main/activation";
	protected static final String CREATE_COMPANY_URL = "/main/createcompany";
	protected static final String DELETE_COMPANY_URL = "/main/deletecompany";
	protected static final String GO_PREMIUM_URL = "/main/subscription/gopremium";
	protected static final String MANAGE_SUBSCRIPTION_URL = "/main/subscriptionmanagement";
	protected static final String MANAGE_LICENSE = "/main/managelicense";
	protected static final String PURCHASE_LICENSE = "/main/purchaseLicense";

	protected String COMPANY_STATUS_URL = "/main/companystatus";

	public static final String ACT_FROM_SIGNUP = "108";
	public static final String ACT_FROM_RESET = "109";
	public static final String ACT_FROM_RESEND = "110";
	public static final String ACT_FROM_LOGIN = "111";
	public static final String IS_SUPPORTED_USER = "isSupportedUser";
	public static final String SUPPORTED_EMAIL_ID = "supportedEmialId";
	public static final String LOCAK_REASON_TYPE = "reasonType";

	public static final Integer LOCK_REASON_TYPE_ENCRYPTION = 1;
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;
	public static final String LOCAL_DATABASE = "accounter";
	public static final int MAIL_ID = 0;
	public static final int NAME = 1;
	public static final int PHONE_NO = 2;
	private static final int ACTIVATION_CODE_SIZE = 10;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log('[' + request.getMethod() + ' ' + request.getServletPath() + ']');
		Session session = HibernateUtil.openSession();
		try {
			String emailId = (String) request.getSession().getAttribute(
					EMAIL_ID);
			Long companyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);
			if (islockedCompany(companyID)) {
				request.getSession().removeAttribute(COMPANY_ID);
				redirectExternal(request, response,
						"/main/companylocked?companyId=" + companyID);
				return;
			}
			if (isDeletedCompany(companyID)) {
				request.getSession().removeAttribute(COMPANY_ID);
				redirectExternal(request, response, LOGIN_URL);
				return;
			}
			
			Client client = getClient(emailId);
			String activationUrl = request.getRequestURI();
			if(client != null && !client.isActive() && !activationUrl.contains(ACTIVATION_URL)) {
				String message = "?message=" + ACT_FROM_SIGNUP;
				redirectExternal(request, response,  ACTIVATION_URL + message);
				return;
			}

			EU.removeCipher();
			byte[] d2 = getD2(request);
			if (emailId != null && d2 != null && companyID != null) {
				User user = getUser(emailId, companyID);
				if (user != null && user.getSecretKey() != null) {
					EU.createCipher(user.getSecretKey(), d2, request
							.getSession().getId());
				}
			}
			request.setAttribute("isRTL",
					ServerLocal.get().equals(new Locale("ar", "", "")));
			super.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Could Not Complete the Request!");
		} finally {
			session.close();
		}
	}

	private boolean isDeletedCompany(Long companyID) {
		if (companyID == null) {
			return false;
		}
		Session session = HibernateUtil.getCurrentSession();
		Object res = session.getNamedQuery("isCompanyDeleted")
				.setLong("companyId", companyID).uniqueResult();
		if (res == null) {
			return false;
		}
		return (Boolean) res;
	}

	public boolean islockedCompany(Long companyID) {
		if (companyID == null) {
			return false;
		}
		Session session = HibernateUtil.getCurrentSession();
		Object res = session.getNamedQuery("isCompanyLocked")
				.setLong("companyId", companyID).uniqueResult();
		if (res == null) {
			return false;
		}
		return (Boolean) res;
	}

	public byte[] getD2(HttpServletRequest request) {
		String d2 = (String) request.getSession().getAttribute(
				SECRET_KEY_COOKIE);
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

	protected Company getCompany(HttpServletRequest req) {
		Long companyID = (Long) req.getSession().getAttribute(COMPANY_ID);
		if (companyID == null) {
			return null;
		}
		Session session = HibernateUtil.getCurrentSession();
		try {
			Company comapny = (Company) session.get(Company.class, companyID);
			if (comapny != null) {
				return comapny;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String getCompanyName(HttpServletRequest req) {
		Company company = getCompany(req);
		if (company != null) {
			return company.getTradingName();
		}
		// Query query = session.getNamedQuery("getServerCompany.by.id")
		// .setParameter("id", Long.valueOf(companyID));
		// if (query.list() != null && !query.list().isEmpty()) {
		// ServerCompany company = (ServerCompany) query.list().get(0);
		// return company.getCompanyName();
		// }
		return null;
	}

	protected FinanceTool getFinanceTool(HttpServletRequest request) {
		return null;
	}

	protected String getCookie(HttpServletRequest request, String ourCookie) {
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

	protected boolean isValidInputs(int inputType, String... values) {
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
	

  public boolean isBotTryingToLogin(String serverUrl,String host,String jsonobject) {

	    StringBuilder sb = new StringBuilder();

	    String http = serverUrl;

	    HttpURLConnection urlConnection = null;
	    try {
	        URL url = new URL(http);
	        urlConnection = (HttpURLConnection) url.openConnection();
	        urlConnection.setDoOutput(true);
	        urlConnection.setRequestMethod("POST");
	        urlConnection.setUseCaches(false);
	        urlConnection.setConnectTimeout(50000);
	        urlConnection.setReadTimeout(50000);
	        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        urlConnection.setRequestProperty("Host", host);
	        urlConnection.connect();
	        //You Can also Create JSONObject here 
	        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
	        out.write(jsonobject);// here i sent the parameter
	        out.close();
	        int HttpResult = urlConnection.getResponseCode();
	        if (HttpResult == HttpURLConnection.HTTP_OK) {
	            BufferedReader br = new BufferedReader(new InputStreamReader(
	                    urlConnection.getInputStream(), "utf-8"));
	            String line = null;
	            while ((line = br.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            br.close();
	            JsonParser parser = new JsonParser();
	            JsonObject object = (JsonObject) parser.parse(sb.toString());//
	            if(object != null) {
	            	if(object.get("success").getAsBoolean() && object.get("score").getAsDouble() >= 0.3 ) {
	            		return true;
	            	}
	            }
	        } else {
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
	        e.printStackTrace();
	    } finally {
	        if (urlConnection != null)
	            urlConnection.disconnect();
	    }
	    return false;
	}


	/**
	 * Dispatches the ServletRequest to The Corresponding View
	 * 
	 * @param req
	 * @param resp
	 * @param page
	 */
	protected void dispatch(HttpServletRequest req, HttpServletResponse resp,
			String page) {
		try {
			RequestDispatcher reqDispatcher = getServletContext()
					.getRequestDispatcher(page);
			reqDispatcher.forward(req, resp);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dispatches the ServletRequest to The Corresponding View with Message
	 * 
	 * @param message
	 * @param req
	 * @param resp
	 * @param page
	 */
	protected void dispatchMessage(String message, HttpServletRequest req,
			HttpServletResponse resp, String page) {
		req.setAttribute("errormessage", message);
		try {
			req.getRequestDispatcher(page).forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void saveEntry(Object object) {
		Session currentSession = HibernateUtil.getCurrentSession();
		currentSession.saveOrUpdate(object);
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(EMAIL_ID, emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}

	protected Client getClientById(long id) {
		Session session = HibernateUtil.getCurrentSession();
		Client client = (Client) session.getNamedQuery("getClient.by.Id")
				.setLong(0, id).uniqueResult();
		// session.close();
		return client;
	}

	/**
	 * Redirect to the External URL {@url}
	 * 
	 * @param req
	 * @param resp
	 * @param string
	 * @throws IOException
	 */
	protected void redirectExternal(HttpServletRequest req,
			HttpServletResponse resp, String url) throws IOException {
		resp.sendRedirect(url);
	}

	protected void sendActivationEmail(String token, Client client) {
		UsersMailSendar.sendActivationMail(token, client);
	}

	/**
	 * Send Welcome Mail to User.
	 * 
	 * @param emailId
	 * 
	 * @param emailId
	 */
	protected void sendWelComeMail(String firstName, String emailId) {
		UsersMailSendar.sendWelComeMail(firstName, emailId);
	}

	protected void sendMailToInvitedUser(Client user, String password,
			String companyName) {
		UsersMailSendar.sendMailToInvitedUser(user, password, companyName);
	}

	/**
	 * Creates new Activation with EmailID
	 * 
	 * @param emailID
	 * @return
	 */
	protected String createActivation(String emailID) {
		String token = SecureUtils.createNumberID(ACTIVATION_CODE_SIZE);
		Activation activation = new Activation();
		activation.setEmailId(emailID);
		activation.setToken(token);
		activation.setSignUpDate(new Date());
		saveEntry(activation);
		return token;
	}

	/**
	 * Deletes the All Activation with this EmailID
	 * 
	 * @param emailId
	 */
	protected void deleteActivationsByEmail(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		session.getNamedQuery("delete.activation.by.emailId")
				.setString("emailId", emailId).executeUpdate();
	}

	// /**
	// * @param serverId
	// */
	// protected void updateServers(Server accounterServer, boolean
	// isAddCompany) {
	// Session session = HibernateUtil.openSession();
	// Transaction transaction = null;
	// try {
	// transaction = session.beginTransaction();
	// Server server = (Server) session.get(Server.class,
	// accounterServer.getId());
	// int companiesCount = server.getCompaniesCount();
	// companiesCount += isAddCompany ? 1 : -1;
	// server.setCompaniesCount(companiesCount);
	// session.saveOrUpdate(server);
	// transaction.commit();
	// } catch (Exception e) {
	// if (transaction != null) {
	// transaction.rollback();
	// }
	// } finally {
	// session.close();
	// }
	// }

	/**
	 * Builds the URL for MainServer
	 * 
	 * @param logoutUrl
	 * @return
	 */
	protected String buildMainServerURL(String url) {
		StringBuilder mainServerURL = new StringBuilder("http://");
		mainServerURL.append(ServerConfiguration.getMainServerDomain());
		// mainServerURL.append(':');
		// mainServerURL.append(ServerConfiguration.getMainServerPort());
		mainServerURL.append(url);
		return mainServerURL.toString();
	}

	/**
	 * Builds the URL for COMPANy
	 * 
	 * @param logoutUrl
	 * @return
	 */
	protected String buildCompanyServerURL(String companyServerAddress,
			String url) {

		StringBuilder mainServerURL = new StringBuilder("http://");
		mainServerURL.append(companyServerAddress);
		// mainServerURL.append(':');
		// mainServerURL.append(ServerConfiguration.getMainServerPort());
		mainServerURL.append(url);
		return mainServerURL.toString();
	}

	/**
	 * Reset the password and send mail to user
	 */
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

	public static User getUser(String emailId, Long serverCompanyID) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session
				.getNamedQuery("getUser.by.mailId.and.companyId");
		namedQuery.setParameter("emailId", emailId).setParameter("companyId",
				serverCompanyID);
		User user = (User) namedQuery.uniqueResult();
		return user;
	}

	public byte[] getCompanySecretFromDB(Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getCompanySecret");
		namedQuery.setParameter("companyId", companyId);
		byte[] secret = (byte[]) namedQuery.uniqueResult();
		return secret;
	}

	public void deleteCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String userKey = getCookie(request, OUR_COOKIE);
		removeCookie(request, response, OUR_COOKIE);

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
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String ourCookie) {
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().endsWith(OUR_COOKIE)) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setDomain(ServerConfiguration.getServerCookieDomain());
				response.addCookie(cookie);
			}
		}
	}

	protected Long getLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
