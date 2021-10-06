package com.vimukti.accounter.admin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.admin.core.AdminUser;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HibernateUtil;

public class AdminLoginServlet extends BaseServlet {
	public static final String ADMIN_HOME = "adminhome";
	public static final String ADMIN_LOGIN_VIEW = "/WEB-INF/AdminLogin.jsp";
	/**
	 * 
	 */
	private static final long serialVersionUID = 5166788117640750514L;

	/**
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession();
		if (session != null) {
			String attribute = (String) session.getAttribute(EMAIL_ID);
			if (attribute != null) {
				doAdminLogin(req, resp);
				return;
			}
		}
		dispatch(req, resp, ADMIN_LOGIN_VIEW);
	}

	private void doAdminLogin(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		AdminUser adminUser = getAdminUser(req);
		if (adminUser != null) {
			redirectExternal(req, resp, ADMIN_HOME);
		} else {
			req.setAttribute("errorMsg", "error");
			dispatch(req, resp, ADMIN_LOGIN_VIEW);
		}

	}

	private AdminUser getAdminUser(HttpServletRequest request) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			AdminUser adminUser = null;
			String userEmail = (String) request.getSession().getAttribute(
					EMAIL_ID);
			String userPaswd = (String) request.getSession().getAttribute(
					PASSWORD);
			Query query = session
					.getNamedQuery("get.adminuser.by.emailid.and.password");
			query.setParameter(EMAIL_ID, userEmail);
			query.setParameter(PASSWORD, userPaswd);
			adminUser = (AdminUser) query.uniqueResult();
			return adminUser;
		} catch (Exception e) {
		} finally {
		}
		return null;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String email = req.getParameter("emailId");
		String passwd = req.getParameter("password");
		if (email != null && passwd != null) {
			HttpSession session = req.getSession(true);
			session.setAttribute(EMAIL_ID, email);
			session.setAttribute(PASSWORD, passwd);
			doAdminLogin(req, resp);
		} else {
			req.setAttribute("errorMsg", "Enter values");
			dispatch(req, resp, ADMIN_LOGIN_VIEW);
		}
	}
}
