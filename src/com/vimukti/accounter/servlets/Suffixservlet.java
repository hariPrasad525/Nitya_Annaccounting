package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerLocal;

public class Suffixservlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("isRTL",
				ServerLocal.get().equals(new Locale("ar", "", "")));

		String requestURI = request.getRequestURI();

		if (!(requestURI.contains(".css") || requestURI.contains("."))) {
			request.getRequestDispatcher(
					"/WEB-INF" + request.getPathInfo() + ".jsp").forward(
					request, response);
		} else {
			if (requestURI.contains("site"))
				request.getRequestDispatcher("/sites" + request.getPathInfo())
						.forward(request, response);
		}
	}
}
