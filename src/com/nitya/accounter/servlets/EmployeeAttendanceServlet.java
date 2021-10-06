package com.nitya.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.EmployeeAttendance;
import com.nitya.accounter.utils.HibernateUtil;

public class EmployeeAttendanceServlet extends BaseServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			resp.sendRedirect("/main/login?dest=/apiregistration");
			return;
		}
		String emailId = (String) session.getAttribute("emailId");
		if (emailId == null) {
			resp.sendRedirect("/main/login?dest=/apiregistration");
			return;
		}
		try {
			Client client = getClient(emailId);
			if (client == null) {
				resp.sendRedirect("/main/login?dest=/apiregistration");
				return;
			}
			EmployeeAttendance developer = getAttendance(client);
			if (developer != null) {
				sendApiInfoPage(developer, req, resp);
				return;
			}
			// Set<ServerCompany> companies = client.getCompanies();
			// List<String> companyList = new ArrayList<String>();
			// for (ServerCompany company : companies) {
			// companyList.add(company.getCompanyName());
			// }
			// req.setAttribute("companyList", companyList);
			req.getRequestDispatcher("/api/attendance.jsp")
					.forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "Session has expired");
			req.getRequestDispatcher("/site/error.jsp").forward(req, resp);
		}
	}

	private void sendApiInfoPage(EmployeeAttendance developer, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("date", developer.getPayrollDate());
		req.setAttribute("milesHours", developer.getMileshours());
		req.setAttribute("foodAllowances", developer.getFoodAllowances());
		req.setAttribute("otherAllowances", developer.getOtherAllowances());
		req.getRequestDispatcher("/api/apiinfo.jsp").forward(req, resp);
	}

	private EmployeeAttendance getAttendance(Client client) {
		Session session = HibernateUtil.getCurrentSession();

		return (EmployeeAttendance) session.getNamedQuery("get.developer.by.client")
				.setParameter("client", client).uniqueResult();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			req.setAttribute("error", "Session has expired");
			req.getRequestDispatcher("/site/error.jsp").forward(req, resp);
		}
		String emailId = (String) session.getAttribute("emailId");
		if (emailId == null) {
			req.setAttribute("error", "Session has expired");
			req.getRequestDispatcher("/site/error.jsp").forward(req, resp);
		}
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = hibernateSession.beginTransaction();
			Client client = getClient(emailId);
			if (client == null) {
				req.setAttribute("error", "Session has expired");
				req.getRequestDispatcher("/site/error.jsp").forward(req, resp);
			}
			if (!client.isActive()) {
				req.setAttribute("error", "User Not Activated");
				req.getRequestDispatcher("/site/error.jsp").forward(req, resp);
			}
			EmployeeAttendance developer = getAttendance(client);
			if (developer != null) {
				sendApiInfoPage(developer, req, resp);
				return;
			}
			String date = req.getParameter("date");
			String milesHours = req.getParameter("milesHours");
			String foodAllowances = req.getParameter("foodAllowances");
			String otherAllowances = req.getParameter("otherAllowances");

			if (!isValidInputs(NAME, date, milesHours,
					foodAllowances, otherAllowances)) {
				// Set<ServerCompany> companies = client.getCompanies();
				// List<String> companyList = new ArrayList<String>();
				// for (ServerCompany company : companies) {
				// companyList.add(company.getCompanyName());
				// }
				// req.setAttribute("companyList", companyList);

				req.setAttribute("error", "Invalid inputs");
				req.getRequestDispatcher("/api/attendance.jsp").forward(req,
						resp);
				return;
			}
			/*
			 * String apiKey = SecureUtils.createID(8); String secretKey =
			 * SecureUtils.createID(16);
			 */

			developer = new EmployeeAttendance();
			developer.setPayrollDate(null);
			developer.setMileshours(milesHours);
			developer.setFoodAllowances(foodAllowances);
			//developer.setClient(client);
			developer.setOtherAllowances(otherAllowances);
			saveEntry(developer);
			sendApiInfoPage(developer, req, resp);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}
	

}
