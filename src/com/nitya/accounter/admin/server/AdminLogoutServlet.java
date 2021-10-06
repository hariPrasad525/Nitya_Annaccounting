package com.nitya.accounter.admin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.servlets.BaseServlet;

public class AdminLogoutServlet extends BaseServlet {

	public static final String ADMIN_LOGIN_VIEW = "/WEB-INF/AdminLogin.jsp";

	/**
	 * 
	 */
	private static final long serialVersionUID = -9092599962984086055L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String userid = (String) req.getSession().getAttribute(EMAIL_ID);
			if (userid != null) {
				req.getSession().setAttribute(EMAIL_ID, null);
				req.getSession().invalidate();
				redirectExternal(req, resp, "/adminlogin");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);

	}
}
