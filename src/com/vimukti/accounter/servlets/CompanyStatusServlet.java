package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CompanyStatusServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CREATING = "Creating Company Please wait...";
	private static final String DELETING = "Deleting Company Please wait...";
	private static final String DELETING_ACCOUNT = "Deleting Account Please wait...";

	private String REFRESH_VIEW = "/WEB-INF/refresh.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String status = (String) session.getAttribute(COMPANY_CREATION_STATUS);
		if (status != null) {
			if (status.equals(COMPANY_CREATING)) {
				req.setAttribute("successmessage", CREATING);
				req.getRequestDispatcher(REFRESH_VIEW).forward(req, resp);
				return;
			}
		}

		String deleteStatus = (String) session
				.getAttribute(COMPANY_DELETION_STATUS);
		if (deleteStatus != null) {
			if (deleteStatus.equals(COMPANY_DELETING)) {
				req.setAttribute("successmessage", DELETING);
				req.getRequestDispatcher(REFRESH_VIEW).forward(req, resp);
				return;
			}
		}

		String accountDeleteStatus = (String) session
				.getAttribute(ACCOUNT_DELETION_STATUS);
		if (accountDeleteStatus != null) {
			if (accountDeleteStatus.equals(ACCOUNT_DELETING)) {
				req.setAttribute("successmessage", DELETING_ACCOUNT);
				req.getRequestDispatcher(REFRESH_VIEW).forward(req, resp);
				return;
			} else if (accountDeleteStatus.equals("Success")) {
				redirectExternal(req, resp, LOGOUT_URL);
				return;
			}
		}
		redirectExternal(req, resp, COMPANIES_URL);
	}
}
