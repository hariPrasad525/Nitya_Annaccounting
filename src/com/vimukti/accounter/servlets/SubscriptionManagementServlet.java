package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;

public class SubscriptionManagementServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2578211747595543714L;
	private String view = "/WEB-INF/subscriptionmanagement.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			resp.sendRedirect(LOGIN_URL + "?destination="
					+ MANAGE_SUBSCRIPTION_URL);
			return;
		}
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId != null) {
			Client client = getClient(emailId);
			if (client == null) {
				resp.sendRedirect(LOGIN_URL);
				return;
			}
			ClientSubscription clientSubscription = client
					.getClientSubscription();
			if (clientSubscription.getPremiumType() == 0) {
				req.setAttribute("error", "Your not premium user");
			}
			showSubscriptionManagementDetails(client,
					clientSubscription.getMembers(), req, resp);
		} else {
			resp.sendRedirect(LOGIN_URL + "?destination="
					+ MANAGE_SUBSCRIPTION_URL);
			return;
		}
	}

	private void showSubscriptionManagementDetails(Client client,
			Set<String> members, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		members = new HashSet<String>(members);
		members.remove(client.getEmailId());
		req.setAttribute("emailId", client.getEmailId());

		req.setAttribute("premiumType", client.getClientSubscription()
				.getPremiumType());
		req.setAttribute("durationType", client.getClientSubscription()
				.getDurationType());
		req.setAttribute("expiredDate", client.getClientSubscription()
				.getExpiredDateAsString());

		String finalString = "";
		boolean isFirst = true;
		finalString = "[";
		for (String string2 : members) {
			if (string2.isEmpty()) {
				continue;
			}
			if (isFirst) {
				finalString += "'" + string2 + "'";
				isFirst = false;
			} else {
				finalString += "," + "'" + string2 + "'";
			}
		}
		finalString += "]";
		req.setAttribute("userIdsList", finalString);
		dispatch(req, resp, view);
	}
}
