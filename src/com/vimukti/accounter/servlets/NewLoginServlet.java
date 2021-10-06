package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gdata.util.common.util.Base64;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.News;
import com.vimukti.accounter.core.RememberMeKey;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.Global;

public class NewLoginServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOGIN_VIEW = "/WEB-INF/login.jsp";
	private static final String ACTIVATION_VIEW = "/WEB-INF/activation.jsp";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String header2 = request.getHeader("User-Agent");
		boolean contains = header2.contains("iPad");
		if (contains) {
			request.setAttribute("ipad", contains);
		}
		
		boolean itIsnotBot = isBotTryingToLogin("https://www.google.com/recaptcha/api/siteverify", "app.annaccounting.com", "secret=6LdZG5IUAAAAAFeKvI-MQ3BIfxJOOVsLr2B3_5X9&response="+request.getParameter("google-key").trim());
        
		if(!itIsnotBot) {
			request.setAttribute("message",Global.get().messages().errorMsg("Something wrong with your login, please reload the page and try again."));
			showLogin(request, response);
		}
		
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Client client = doLogin(request, response);
			if (client != null && !client.isDeleted()) {
				// if valid credentials are there we redirect to <dest> param or
				// /companies

				if (!client.isActive()) {
					// TODO send the ResetPAssword page
					// client.setRequirePasswordReset(false);
					// saveEntry(client);
					request.setAttribute("successmessage", Global.get()
							.messages().accounterNotActivate());
					dispatch(request, response, ACTIVATION_VIEW);

				} else {

					if (client.isRequirePasswordReset()) {
						client.setRequirePasswordReset(false);
						session.saveOrUpdate(client);
					}

					String destUrl = request.getParameter(PARAM_DESTINATION);
					HttpSession httpSession = request.getSession();
					httpSession.setAttribute(EMAIL_ID, client.getEmailId());
					if (destUrl == null || destUrl.isEmpty()) {
						client.setLoginCount(client.getLoginCount() + 1);
						client.setLastLoginTime(System.currentTimeMillis());
						session.saveOrUpdate(client);
						redirectExternal(request, response, COMPANIES_URL);
					} else {
						redirectExternal(request, response, destUrl);
					}

				}
			} else {
				request.setAttribute("message", Global.get().messages()
						.incorrectEmailOrPassWord());
				showLogin(request, response);
				return;
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
		}
	}

	private Client doLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String emailId = request.getParameter(EMAIL_ID);
		String password = request.getParameter(PASSWORD);
		Client client = getClient(emailId, password);

		if (client == null) {
			return null;
		}
		String encode = createD2(request, emailId, password);
		if (request.getParameter("staySignIn") != null
				&& request.getParameter("staySignIn").equals("on")) {
			// Inserting RememberMeKey
			Session session = HibernateUtil.getCurrentSession();

			byte[] makeHash = Security.makeHash(client.getEmailId()
					+ Security.makeHash(client.getPassword()));
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(makeHash);
			String rememberMeKey = HexUtil.bytesToHex(digest);
			RememberMeKey rememberMe = new RememberMeKey(client.getEmailId(),
					rememberMeKey);
			rememberMe.setClientKey(encode);
			rememberMe.setServerKey(EU.getKey(request.getSession().getId()));
			session.save(rememberMe);
			addUserCookies(response, rememberMeKey);
		}
		return client;
	}

	public static String createD2(HttpServletRequest request, String emailId,
			String password) throws Exception {
		byte[] d2 = EU.generateD2(password, emailId, request.getSession()
				.getId());
		String encode = Base64.encode(d2);
		request.getSession().setAttribute(SECRET_KEY_COOKIE, encode);
		return encode;
	}

	protected void addUserCookies(HttpServletResponse resp, String key,
			String name) {
		Cookie userCookie = new Cookie(name, key);
		userCookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		// userCookie.setPath("/");
		// userCookie.setDomain(ServerConfiguration.getServerCookieDomain());
		resp.addCookie(userCookie);
	}

	protected void addUserCookies(HttpServletResponse resp, String key) {
		addUserCookies(resp, key, OUR_COOKIE);
	}

	private Client getClient(String emailId, String password) {
		if (emailId == null || password == null) {
			return null;
		}
		emailId = emailId.trim();
		String passwordWord = HexUtil.bytesToHex(Security.makeHash(emailId.trim()
				+ Client.PASSWORD_HASH_STRING + password.trim()));

		Session session = HibernateUtil.getCurrentSession();
		try {
			Client client = null;
			Query query = session
					.getNamedQuery("getclient.from.central.db.using.emailid.and.password");
			query.setParameter(EMAIL_ID, emailId);
			query.setParameter(PASSWORD, passwordWord);
			client = (Client) query.uniqueResult();
			String passwordHash = HexUtil.bytesToHex(Security.makeHash(emailId.trim()
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

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// We check if the session is already there, if it is, we check if user
		// have to reset his password(by using a flag on the user object)
		HttpSession httpSession = request.getSession(true);

		String header2 = request.getHeader("User-Agent");
		boolean contains = header2.contains("iPad");
		if (contains) {
			request.setAttribute("ipad", contains);
		}

		String header = request.getHeader("Ipadapp");
		if (header != null) {
			if (header.equalsIgnoreCase("1.1")) {
				request.getSession().setAttribute("IpadApp", true);
			}
		}

		String parameter = request.getParameter("message");
		String activationType = (String) httpSession
				.getAttribute(ACTIVATION_TYPE);
		if (activationType != null && activationType.equals("resetpassword")) {
			httpSession.removeAttribute(ACTIVATION_TYPE);
			httpSession.removeAttribute(EMAIL_ID);
			redirectExternal(request, response, LOGIN_URL);
			return;
		}
		String emailID = (String) httpSession.getAttribute(EMAIL_ID);
		if (emailID == null) {
			// if session is not there then we show the form and user fills it
			// which gets submitted to same url
			String userCookie = getCookie(request, OUR_COOKIE);
			if (userCookie == null) {
				showLogin(request, response);
				return;
			}

			Session session = HibernateUtil.getCurrentSession();
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				Query query = session.getNamedQuery("get.remembermeKey");
				query.setParameter("key", userCookie);
				RememberMeKey rememberMeKey = (RememberMeKey) query
						.uniqueResult();

				if (rememberMeKey == null) {
					showLogin(request, response);
					return;
				}
				Client client = getClient(rememberMeKey.getEmailID());
				if (client == null) {
					showLogin(request, response);
					return;
				}
				
				if(!client.isActive()) {
					request.setAttribute("successmessage", Global.get()
							.messages().accounterNotActivate());
					dispatch(request, response, ACTIVATION_VIEW);
					return;
				}
				httpSession.setAttribute(EMAIL_ID, rememberMeKey.getEmailID());
				httpSession.setAttribute(SECRET_KEY_COOKIE,
						rememberMeKey.getClientKey());
				if (rememberMeKey.getServerKey() != null) {
					EU.storeKey(rememberMeKey.getServerKey(),
							httpSession.getId());
				} else {
					// This is happen for old users.(before develop encryption)
					session.delete(rememberMeKey);
					transaction.commit();
					showLogin(request, response);
					return;
				}
				client.setLoginCount(client.getLoginCount() + 1);
				client.setLastLoginTime(System.currentTimeMillis());
				session.saveOrUpdate(client);
				transaction.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (transaction != null) {
					transaction.rollback();
				}
			} finally {
			}
			String destUrl = request.getParameter(PARAM_DESTINATION);
			if (destUrl == null || destUrl.isEmpty()) {
				redirectExternal(request, response, COMPANIES_URL);
			} else {
				redirectExternal(request, response, COMPANIES_URL);
			}

		} else {

			// Get the Client using the mail id
			Session session = HibernateUtil.getCurrentSession();
			try {
				Query query = session.getNamedQuery("getClient.by.mailId");
				query.setParameter(EMAIL_ID, emailID);

				Client client = (Client) query.uniqueResult();
				if (client != null && !client.isDeleted()) {
					if (client.isActive()) {
						if (client.isRequirePasswordReset()) {
							// If session is there and he has to reset the
							// password
							// then do an external redirect to /resetpassword
							// url
							redirectExternal(request, response,
									RESET_PASSWORD_URL);
							return;
						} else {
							// if session is there and no need to reset password
							// then do external redirect to <dest> param or
							// /companies
							String destUrl = request
									.getParameter(PARAM_DESTINATION);
							if (destUrl == null || destUrl.isEmpty()) {
								redirectExternal(request, response,
										COMPANIES_URL);
							} else {
								redirectExternal(request, response, destUrl);
							}

						}
					} else {
						// not activated so resend the activation mail
						// Generate Token and create Activation and save. then
						// send
						// String token = SecureUtils.createID();
						//
						// // get activation object by email id
						//
						// query = session
						// .getNamedQuery("get.activation.by.emailid");
						// query.setParameter(EMAIL_ID, emailID);
						// Activation activation = (Activation) query
						// .uniqueResult();
						// Transaction transaction = session.beginTransaction();
						// // reset the activation code and save it
						// activation.setToken(token);
						// try {
						// saveEntry(activation);
						//
						// // resend activation mail
						// sendActivationEmail(token, client);
						// transaction.commit();
						// } catch (Exception e) {
						// e.printStackTrace();
						// transaction.rollback();
						// }
						String message = "?message=" + ACT_FROM_RESET;
						redirectExternal(request, response, ACTIVATION_URL
								+ message);
						return;
					}
				} else {
					showLogin(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

		}

	}

	private void showLogin(HttpServletRequest request,
			HttpServletResponse response) {
		String news = getNews();
		Object attribute = request.getParameter(PARAM_DESTINATION);
		request.setAttribute(PARAM_DESTINATION, attribute);
		request.setAttribute("news", news);
		HttpSession session = request.getSession();
		session.setAttribute(PARAM_DESTINATION, attribute);
		dispatch(request, response, LOGIN_VIEW);

		return;
	}

	private String getNews() {
		Session session = HibernateUtil.getCurrentSession();
		try {

			@SuppressWarnings("unchecked")
			List<News> list = session.getNamedQuery("getNews").list();

			XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
			xstream.setMode(XStream.NO_REFERENCES);
			xstream.alias("news", News.class);

			return xstream.toXML(list);
		} finally {
		}
	}

}
