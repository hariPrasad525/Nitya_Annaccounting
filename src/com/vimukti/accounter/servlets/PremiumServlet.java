package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;

public class PremiumServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String LIVE_PREMIUM = "/content/go-premium";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = (ServerConfiguration.isInLive() ? LIVE_PREMIUM
				: GO_PREMIUM_URL) + "?emailId=" + req.getParameter("emailId");
		resp.sendRedirect(url);
	}
}
