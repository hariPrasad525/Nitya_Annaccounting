package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.services.SubscriptionTool;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class SubDeleteUserConformServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/deleteUserConform.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doDelete(req, resp, false);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doDelete(req, resp, true);
	}

	private void doDelete(HttpServletRequest req, HttpServletResponse resp,
			boolean isConfirmed) throws ServletException, IOException {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		HttpSession session2 = req.getSession();
		if (session2 == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		Client client = getClient(emailId);
		if (client == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		String string = null;
		if (isConfirmed) {
			string = (String) req.getSession().getAttribute("userMailds");
			if (string == null) {
				redirectExternal(req, resp, "/main/subscriptionmanagement");
			}
		} else {
			string = req.getParameter("userMailds");
		}
		ClientSubscription clientSubscription = client.getClientSubscription();
		if (clientSubscription.getPremiumType() == 0) {
			req.setAttribute("error", "Your not premium user");
			redirectExternal(req, resp, "/main/subscriptionmanagement");
			return;
		}
		Set<String> oldMembers = clientSubscription.getMembers();
		List<String> existedUsers = getExistedUsers(oldMembers);
		Set<String> members = getMembers(string);
		members.add(emailId);

		if (!checkTotalMembers(members, client.getClientSubscription()
				.getPremiumType())) {
			req.setAttribute("error", "No of users should be limit");
			redirectExternal(req, resp, "/main/subscriptionmanagement");
			return;
		}
		clientSubscription.setMembers(members);
		clientSubscription.setLastModified(new Date());
		saveEntry(clientSubscription);

		try {
			Set<String> mergeUsers = mergeUsers(client, oldMembers,
					existedUsers, members);
			if (!mergeUsers.isEmpty()) {
				if (isConfirmed) {
					for (String s : mergeUsers) {
						SubscriptionTool.deleteUser(client, s);
					}
				} else {
					req.getSession().setAttribute("userMailds", string);
					showSubscriptionManagementDetails(mergeUsers, req, resp);
					return;
				}
			}
			transaction.commit();
		} catch (AccounterException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		resp.sendRedirect(COMPANIES_URL);
	}

	private Set<String> mergeUsers(Client client, Set<String> oldMembers,
			List<String> existedUsers, Set<String> members)
			throws AccounterException {
		Set<String> deletedUsers = new HashSet<String>();
		for (String o : oldMembers) {
			if (!members.contains(o) && existedUsers.contains(o)) {
				deletedUsers.add(o);
			}
		}
		return deletedUsers;
	}

	private boolean checkTotalMembers(Set<String> members, int type) {
		switch (type) {
		case ClientSubscription.ONE_USER:
		case ClientSubscription.TRIAL_USER:
			return members.size() <= 1;
		case ClientSubscription.TWO_USERS:
			return members.size() <= 2;
		case ClientSubscription.FIVE_USERS:
			return members.size() <= 5;
		default:
			return true;
		}
	}

	private Set<String> getMembers(String string) {
		Set<String> emailIds = new HashSet<String>();
		String[] stringArray = string.split("\r\n");
		for (String string2 : stringArray) {
			if (!string2.isEmpty()) {
				emailIds.add(string2);
			}
		}
		return emailIds;
	}

	private void showSubscriptionManagementDetails(Set<String> members,
			HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		req.setAttribute("userIdsList", members);
		dispatch(req, resp, view);
	}

	@SuppressWarnings("unchecked")
	private List<String> getExistedUsers(Set<String> members) {
		if (members.isEmpty()) {
			return new ArrayList<String>();
		}
		return HibernateUtil.getCurrentSession()
				.getNamedQuery("get.created.users")
				.setParameterList("users", members).list();
	}
}
