package com.nitya.accounter.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.nitya.accounter.core.change.ChangeTracker;

public class ThreadLocalFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		String requestURI = req.getRequestURI();
		if (requestURI.endsWith(".js") || requestURI.endsWith(".css")
				|| requestURI.endsWith(".jpg") || requestURI.endsWith(".png")) {
			// NO NEED TO INITIALIZE CHANGE TRACHER
		} else {
			ChangeTracker.init();
		}
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
