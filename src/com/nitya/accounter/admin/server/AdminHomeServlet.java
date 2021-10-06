package com.nitya.accounter.admin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.servlets.BaseServlet;

public class AdminHomeServlet extends BaseServlet {
	public static final String ADMIN_HOME = "/WEB-INF/AdminHome.jsp";
	/**
	 * 
	 */
	private static final long serialVersionUID = -8914697271736346657L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp, ADMIN_HOME);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
}
