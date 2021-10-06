/**
 * 
 */
package com.nitya.accounter.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.main.ServerConfiguration;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ServerMaintainanceFilter implements Filter {

	private static final String MAINTANACE_VIEW = "/WEB-INF/maintananceInfo.jsp";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		String requestURI = req.getRequestURI();
		if (requestURI.endsWith(".js") ||requestURI.endsWith(".css") || requestURI.endsWith(".jpg")
				|| requestURI.endsWith(".png")) {
			arg2.doFilter(arg0, arg1);
			return;
		}

		if (!(req.getServletPath().endsWith("/maintanance")|| req.getServletPath().endsWith("/maintanaceinform"))
				&& ServerConfiguration.isUnderMaintainance()) {
			req.getRequestDispatcher(MAINTANACE_VIEW).forward(req,
					(HttpServletResponse) arg1);
		} else {
			arg2.doFilter(arg0, arg1);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
