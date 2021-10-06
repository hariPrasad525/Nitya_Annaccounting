package com.nitya.accounter.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.utils.HibernateUtil;

public class ThirdPartySignupServlet extends BaseServlet {

	private static final String OPENID_SIGNUP_VIEW = "/WEB-INF/OpenIdSignup.jsp";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void loginForUser(String email, String firstname, String lastname,
			HttpServletRequest request, HttpServletResponse response) {
		firstname = firstname == null ? "" : firstname;
		lastname = lastname == null ? "" : lastname;
		request.setAttribute("email", email);
		request.setAttribute("firstname", firstname);
		request.setAttribute("lastname", lastname);
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Client client = (Client) session
					.getNamedQuery("getClient.by.mailId")
					.setString("emailId", email).uniqueResult();
			if (client != null && !client.isDeleted()) {
				// if valid credentials are there we redirect to <dest> param or
				// /companies

				if (!client.isActive()) {
					client.setActive(true);
				} else {
					if (client.isRequirePasswordReset()) {
						client.setRequirePasswordReset(false);
						session.saveOrUpdate(client);
					}

					String destUrl = request.getParameter(PARAM_DESTINATION);
					HttpSession httpSession = request.getSession();
					httpSession.setAttribute(EMAIL_ID, client.getEmailId());
					if (destUrl == null || destUrl.isEmpty()) {
						if (client.getClientSubscription().isPaidUser()) {
							if (client.getPassword() == null) {
								response.sendRedirect("/main/resetpassword?type=openid");
							} else {
								response.sendRedirect("/main/openidpassword");
							}
							return;
						}
						client.setLoginCount(client.getLoginCount() + 1);
						client.setLastLoginTime(System.currentTimeMillis());
						session.saveOrUpdate(client);
						redirectExternal(request, response, COMPANIES_URL);

					} else {
						redirectExternal(request, response, destUrl);
					}

				}
			} else {
				request.setAttribute("message",
						"No account exists with this emailid.please signup");
				dispatch(request, response, OPENID_SIGNUP_VIEW);
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

}
