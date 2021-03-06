package com.nitya.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Activity;
import com.nitya.accounter.core.ActivityType;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.EU;
import com.nitya.accounter.core.User;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.server.CometManager;

public class LogoutServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3008434821899018651L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userid = (String) req.getSession().getAttribute(EMAIL_ID);
		if(userid == null)
		{
			userid = req.getHeader("emailId");
			
		}
		if(req.getHeader("Sec-Fetch-Site").equals("cross-site"))
		{
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Allow-Methods", "GET");
			resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
			resp.setHeader("Access-Control-Allow-Credentials","true");
			resp.setHeader("Access-Control-Max-Age", "86400");	
		}
		Long cid = (Long) req.getSession().getAttribute(COMPANY_ID);	
		if (userid != null) {
			if (cid != null) {
				try {
					updateActivity(userid, cid);

					// Destroy the comet queue so that it wont take memory
					CometManager.destroyStream(req.getSession().getId(), cid,
							userid);
					EU.removeKey(req.getSession().getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// if is Support user then put his email Id back
			req.getSession().removeAttribute(EMAIL_ID);
			req.getSession().removeAttribute(USER_ID);
		}
		// Support user and OpendCompany
		req.getSession().invalidate();
		deleteCookie(req, resp);
		if(!req.getHeader("Sec-Fetch-Site").equals("cross-site"))
		{
			redirectExternal(req, resp, LOGIN_URL);
		}
		
	}

	/**
	 * @param cid
	 */
	private void updateActivity(String userid, Long cid) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = (Company) session.get(Company.class, cid);
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = (User) session.getNamedQuery("user.by.emailid")
					.setParameter("emailID", userid)
					.setEntity("company", company).uniqueResult();
			Activity activity = new Activity(user.getCompany(), user,
					ActivityType.LOGOUT);
			session.save(activity);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} 
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(req, response);
	}
}
