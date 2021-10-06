package com.nitya.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PurchaseLicenseServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String page = "/WEB-INF/purchaseLicense.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(LOGIN_URL + "?destination=" + PURCHASE_LICENSE);
			return;
		}
		page = "/WEB-INF/purchaseLicense.jsp";
		dispatch(req, resp, page);
	}

}
