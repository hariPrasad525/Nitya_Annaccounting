package com.vimukti.accounter.setup.server;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;

public class AccounterSetupServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!ServerConfiguration.isStartUpCompleted()) {
			resp.sendRedirect("/desk/startup");
			return;
		}
		if (ServerConfiguration.isSetupCompleted()) {
			resp.sendRedirect("/login");
			return;
		}
		String pageNo = req.getParameter("page");
		if (pageNo == null) {
			pageNo = "0";
		}
		req.setAttribute("pageNo", pageNo);
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/WEB-INF/Setup.jsp");
		dispatcher.forward(req, resp);
	}

}
