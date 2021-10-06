package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.nitya.accounter.main.ServerLocal;

public class ServletLocaleFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ServerLocal.set(getLocale((HttpServletRequest) request));
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	private Locale getLocale(HttpServletRequest request) {
		String locale = getCookie(request, "locale");
		if (locale != null) {
			return new Locale(locale);
		}
		return request.getLocale();
	}

	private String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
