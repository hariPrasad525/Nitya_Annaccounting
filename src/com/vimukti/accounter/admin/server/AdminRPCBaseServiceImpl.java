package com.vimukti.accounter.admin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vimukti.accounter.admin.core.AdminUser;
import com.vimukti.accounter.utils.HibernateUtil;

public class AdminRPCBaseServiceImpl extends RemoteServiceServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8758237116889598304L;

	protected static final String EMAIL_ID = "emailId";

	protected static final String PASSWORD = "password";

	public AdminRPCBaseServiceImpl() {
		super();
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Session session = HibernateUtil.openSession();
		try {
			if (isValidSession(request)) {
				setAdminThreadLocal(request);
				super.service(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,
						"Could Not Complete the Request!");
			}
		} catch (Exception e) {

			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Could Not Complete the Request!");
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	private boolean isValidSession(HttpServletRequest request) {
		return request.getSession().getAttribute(EMAIL_ID) == null ? false
				: true;
	}

	protected String getUserEmail() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				EMAIL_ID);
	}

	private void setAdminThreadLocal(HttpServletRequest request) {
		Session session = HibernateUtil.getCurrentSession();
		String userEmail = (String) request.getSession().getAttribute(EMAIL_ID);
		String userPaswd = (String) request.getSession().getAttribute(PASSWORD);
		AdminUser adminuser = null;
		Query query = session
				.getNamedQuery("get.adminuser.by.emailid.and.password");
		query.setParameter(EMAIL_ID, userEmail);
		query.setParameter(PASSWORD, userPaswd);
		adminuser = (AdminUser) query.uniqueResult();
		AdminThreadLocal.set(adminuser);
	}
}
