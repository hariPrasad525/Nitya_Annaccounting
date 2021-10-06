package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.EU;

public class SubscribtionComplitionServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5081127451630771127L;
	private String view = "/WEB-INF/thankyou.jsp";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		EU.removeKey(req.getSession().getId());
		req.getSession().removeAttribute(EMAIL_ID);
		req.getSession().removeAttribute(USER_ID);
		req.getSession().invalidate();
		deleteCookie(req, resp);
		dispatch(req, resp, view);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

}
