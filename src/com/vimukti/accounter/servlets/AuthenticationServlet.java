package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerLocal;

public class AuthenticationServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("isRTL",
				ServerLocal.get().equals(new Locale("ar", "", "")));
		
		String pwd = (String) request.getParameter("companypsw");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		if (pwd.equals("ltsDf835")) {
			response.getWriter().write("<message id=\"iid\"></message>");
		} else {
			response.getWriter().write(
					"<message id=\"iid\">Invalid Password</message>");
		}
	}
}
