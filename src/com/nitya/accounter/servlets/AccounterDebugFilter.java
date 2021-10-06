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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.nitya.accounter.main.ServerConfiguration;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterDebugFilter implements Filter {

	private static final String DEBUG_URL = "gwt.codesvr=127.0.0.1:9997";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		if (!ServerConfiguration.isDebugMode) {
			chain.doFilter(req, resp);
			return;
		}
		HttpServletResponse httpResponse = (HttpServletResponse) resp;
		HttpServletResponseWrapper wrappedResp = new HttpServletResponseWrapper(
				httpResponse) {

			@Override
			public void setHeader(String name, String value) {
				if (name.equals("Location") && !value.endsWith(DEBUG_URL)) {
					value = value + DEBUG_URL;
				}
				super.setHeader(name, value);
			}

			@Override
			public void sendRedirect(String location) throws IOException {
				if(!location.contains(DEBUG_URL)) {
					location += location.contains("?")?'&':'?';					
					location += DEBUG_URL;
				}
				super.sendRedirect(location);
			}

		};

		chain.doFilter(req, wrappedResp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
