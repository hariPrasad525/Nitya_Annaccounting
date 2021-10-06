package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.utils.HibernateUtil;

public class DecryptPasswordServlet extends HttpServlet {
	private static final String VIEW = "/WEB-INF/decryptpassword.jsp";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp);
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher(VIEW).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String companyName = req.getParameter("companyName");
		String emailId = req.getParameter("emailId");
		String password = req.getParameter("password");
		Session session = HibernateUtil.openSession();
		try {

			String msg = decryptPassword(companyName, password, true);
			if (!msg.isEmpty()) {
				msg += "\n";
			}
			msg += decryptPassword(emailId, password, false);
			if (msg.isEmpty()) {
				msg = "Enter Company name or client emailId";
			}
			req.setAttribute("message", msg);
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("message", "Exception:" + e.getMessage());
		} finally {
			if (session.isOpen()) {
				session.close();
			}
			dispatch(req, resp);
		}
	}

	private String decryptPassword(String companyName, String password,
			boolean isCompany) {
		String message = "";
		if (companyName != null && !companyName.isEmpty()) {
			Object[] obj = isCompany ? getCompanyPasswordKey(companyName)
					: getClientPasswordKey(companyName);
			if (obj == null) {
				message = (isCompany ? "Company" : "Client") + " '"
						+ companyName + "' does not exist";
			} else {
				byte[] companyPasswordKey = (byte[]) obj[0];
				if (companyPasswordKey == null) {
					message = (isCompany ? "Company" : "Client") + " '"
							+ companyName + "' password key is null";
				} else {
					String pass = EU.decryptPassword(companyPasswordKey,
							password);
					if (pass == null) {
						return "wrong password";
					}
					message = "password of "
							+ (isCompany ? "Company" : "Client") + " '"
							+ companyName + "' is '" + pass + "'";
				}
			}
		}
		return message;
	}

	private Object[] getClientPasswordKey(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		List<Object[]> list = session.getNamedQuery("get.client.passwordkey")
				.setString("emailId", emailId).list();
		return list.isEmpty() ? null : list.get(0);
	}

	private Object[] getCompanyPasswordKey(String companyName) {
		Session session = HibernateUtil.getCurrentSession();
		List<Object[]> list = session.getNamedQuery("get.company.passwordkey")
				.setString("companyName", companyName).list();
		return list.isEmpty() ? null : list.get(0);
	}
}
