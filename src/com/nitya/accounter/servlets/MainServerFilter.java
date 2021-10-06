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
import com.nitya.accounter.setup.server.DatabaseManager;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MainServerFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		String requestURI = request.getRequestURI();
		if (requestURI.endsWith(".js") || requestURI.endsWith(".css")
				|| requestURI.endsWith(".jpg") || requestURI.endsWith(".png")
				|| requestURI.endsWith(".html")) {
			arg2.doFilter(arg0, arg1);
			return;
		}
		if (ServerConfiguration.isDesktopApp()
				&& !ServerConfiguration.isSetupCompleted()
				&& !requestURI.contains("/desk/startup")
				&& !requestURI.contains("/setup")) {
			if (!ServerConfiguration.isStartUpCompleted()) {
				response.sendRedirect("/desk/startup");
			} else if (!DatabaseManager.isDBConfigured()
					|| !ServerConfiguration.isSetupCompleted()) {
				response.sendRedirect("/desk/startupcomplete");
			} else {
				arg2.doFilter(arg0, arg1);
			}
		} else {
			arg2.doFilter(arg0, arg1);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	protected String buildMainServerURL(String url) {
		StringBuilder mainServerURL = new StringBuilder("http://");
		mainServerURL.append(ServerConfiguration.getMainServerDomain());
		mainServerURL.append(':');
		mainServerURL.append(ServerConfiguration.getMainServerPort());
		mainServerURL.append(url);
		return mainServerURL.toString();
	}
}
