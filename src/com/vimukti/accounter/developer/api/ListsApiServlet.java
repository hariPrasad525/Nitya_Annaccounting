package com.vimukti.accounter.developer.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListsApiServlet extends ApiBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3303531810005592521L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getParameter("type");
		if (type == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"type should be present");
			return;
		}
		doProcess(req, resp, type);
	}
}
