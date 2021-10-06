package com.nitya.accounter.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.MaintananceInfoUser;
import com.nitya.accounter.utils.HibernateUtil;

public class MaintanaceInfoMailServelet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String view = "/WEB-INF/maintananceInfo.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(view);
		PrintWriter out = resp.getWriter();
		if (!isValidInputs(MAIL_ID, req.getParameter("email"))) {
			resp.setContentType("text/text");
			out.write("fail");
			return;
		}
		String string = (String) req.getParameter("ajax");
		if (string == null || !Boolean.valueOf(string)) {
			if (saveUser(req)) {
				out.write("success");
			} else {
				out.write("success");
			}
			dispatcher.forward(req, resp);
		} else {
			resp.setContentType("text/text");
			if (saveUser(req)) {
				out.write("success");
			} else {
				out.write("fail");
			}
		}

	}

	private boolean saveUser(HttpServletRequest req) {
		String email = req.getParameter("email");
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Transaction transaction = hibernateSession.beginTransaction();
		if (email != null) {
			try {
				MaintananceInfoUser mainInfoUser = new MaintananceInfoUser();
				List<MaintananceInfoUser> usersList = hibernateSession
						.getNamedQuery("getallMaintanaceInfoUsers").list();
				for (MaintananceInfoUser object : usersList) {
					if (object.getUserEmail().equals(email)) {
						return true;
					}
				}
				mainInfoUser.setUserEmail(email);
				saveEntry(mainInfoUser);
				transaction.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (transaction != null) {
					transaction.rollback();
				}
			}
			return true;
		}
		return false;

	}
}
