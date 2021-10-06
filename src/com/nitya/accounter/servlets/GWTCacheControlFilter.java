package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GWTCacheControlFilter implements Filter {

	private final static String[] STATIC_CONTENTS = { ".jpeg", ".jpg", ".gif",
			".png", ".css", ".js", ".html" };

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (requestURI.contains(".nocache.")
				|| requestURI.endsWith("/accounter")) {
			Date now = new Date();

			httpResponse.setDateHeader("Date", now.getTime());
			// one day old
			httpResponse.setDateHeader("Expires", now.getTime() - 86400000L);
			httpResponse.setHeader("Pragma", "no-cache");
			httpResponse.setHeader("Cache-control",
					"no-cache, no-store, max-age=0, must-revalidate");
		} else if (isStaticContentUri(requestURI)) {
			Date now = new Date();
			httpResponse.setDateHeader("Expires", now.getTime() + 31536000000L);
			// httpResponse.setHeader("Cache-control",
			// "max-age=2592000, public");
		}

		filterChain.doFilter(request, response);
	}

	private boolean isStaticContentUri(String uri) {
		uri = uri.toLowerCase();
		for (String contentType : STATIC_CONTENTS) {
			if (uri.endsWith(contentType.toLowerCase()))
				return true;
		}
		return false;
	}
}
