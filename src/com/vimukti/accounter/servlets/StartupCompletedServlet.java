package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.setup.server.DatabaseManager;

/**
 * It will be used for only Desktop Application
 * 
 * @author Prasanna Kumar G
 * 
 */
public class StartupCompletedServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SETUP_URL = "/desk/setup";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String redirectURL = null;
		if (DatabaseManager.isDBConfigured()) {
			if (ServerConfiguration.isSetupCompleted()) {
				redirectURL = "/login";
			} else {
				String page = ServerConfiguration.getSetupStatus();
				if (page != null) {
					redirectURL = SETUP_URL + "?page=" + page;
				} else {
					redirectURL = SETUP_URL;
				}
			}

		} else {
			redirectURL = SETUP_URL;
		}
		resp.sendRedirect(redirectURL);
	}
}
