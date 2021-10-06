package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;

/**
 * @author Prasanna Kumar G
 * 
 */

@SuppressWarnings("serial")
public class DeleteCompanyServlet extends BaseServlet {

	private static final String deleteCompanyView = "/WEB-INF/deleteCompany.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}

		String companyId = req.getParameter(COMPANY_ID);
		if (companyId == null || companyId.isEmpty()
				|| getLong(companyId) == null) {
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}

		Session hibernateSession = HibernateUtil.getCurrentSession();

		Query query = hibernateSession
				.getNamedQuery("user.by.emailid.companyId")
				.setParameter("emailID", emailID)
				.setParameter("companyId", Long.parseLong(companyId));
		User user = (User) query.uniqueResult();
		if (user == null) {
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}

		String parameter = req.getParameter("isCancel");

		if (parameter != null && parameter.equalsIgnoreCase("true")) {
			req.getSession().removeAttribute(COMPANY_ID);
			redirectExternal(req, resp, COMPANIES_URL);
			return;
		}

		setOptions(req, companyId, emailID);
		dispatch(req, resp, deleteCompanyView);
	}

	private boolean isOpenIdUser(Client client) {
		if (client.getPassword() == null) {
			return true;
		}
		return false;
	}

	private void setOptions(HttpServletRequest req, String companyId,
			String emailID) {
		boolean canDeleteFromSingle = true, canDeleteFromAll = true;
		Session hibernateSession = HibernateUtil.getCurrentSession();
		User user = null;
		try {
			Company company = (Company) hibernateSession.get(Company.class,
					Long.parseLong(companyId));
			user = (User) hibernateSession.getNamedQuery("user.by.emailid")
					.setParameter("emailID", emailID)
					.setParameter("company", company).uniqueResult();

			Query query = hibernateSession.getNamedQuery("get.Admin.Users")
					.setParameter("company", company);
			List<User> adminUsers = query.list();
			if (adminUsers.size() < 2) {
				for (User u : adminUsers) {
					if (u.getID() == user.getID()) {
						canDeleteFromSingle = false;
						break;
					}
				}
			}

			if (user != null && !user.isAdmin()) {
				canDeleteFromAll = false;
			}
		} finally {
		}
		req.setAttribute("isOpenIdUser", isOpenIdUser(user.getClient()));
		req.setAttribute("canDeleteFromSingle", canDeleteFromSingle);
		req.setAttribute("canDeleteFromAll", canDeleteFromAll);
		req.getSession().setAttribute(COMPANY_ID, Long.parseLong(companyId));

	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteComapny(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String email = (String) req.getSession().getAttribute(EMAIL_ID);
		Client client = getClient(email);
		String message = "";

		String companyID = String.valueOf(req.getSession().getAttribute(
				COMPANY_ID));
		String delete = req.getParameter("delete");
		String password = req.getParameter("userPassword");
		if (!isOpenIdUser(client)) {
			if (password == null || password.isEmpty()) {
				req.setAttribute("message", "Password shoudn't empty");
				setOptions(req, companyID, email);
				dispatch(req, resp, deleteCompanyView);
				return;

			}

			String passwordWord = HexUtil.bytesToHex(Security.makeHash(email
					+ Client.PASSWORD_HASH_STRING + password.trim()));
			String passwordHash2 = HexUtil.bytesToHex(Security.makeHash(email
					+ password.trim()));
			if (!passwordWord.equals(client.getPassword())
					&& !passwordHash2.equals(client.getPassword())) {
				req.setAttribute("message", "Password is incorrect");
				setOptions(req, companyID, email);
				dispatch(req, resp, deleteCompanyView);
				return;
			}
		}
		if (delete == null) {
			req.setAttribute("message", "Please select the option.");
			setOptions(req, companyID, email);
			dispatch(req, resp, deleteCompanyView);
			return;
		}

		boolean deleteAllUsers = delete.equals("deleteAllUsers");
		HttpSession httpSession = req.getSession();
		deleteCompany(httpSession, deleteAllUsers);

		if (message.isEmpty()) {
			message = "Success";
		}
		redirectExternal(req, resp, COMPANY_STATUS_URL);
	}

	private void deleteCompany(HttpSession httpSession, boolean isDeleteAllUsers) {
		String companyID = String.valueOf(httpSession.getAttribute(COMPANY_ID));
		String email = (String) httpSession.getAttribute(EMAIL_ID);
		Client client = getClient(email);
		Session hibernateSession = HibernateUtil.getCurrentSession();
		Company company = (Company) hibernateSession.get(Company.class,
				Long.parseLong(companyID));

		httpSession.setAttribute(COMPANY_DELETION_STATUS, COMPANY_DELETING);

		if (company == null || company.isDeleted()) {
			httpSession.setAttribute(COMPANY_DELETION_STATUS, "Fail");
			httpSession.setAttribute("DeletionFailureMessage",
					"Company has been deleted.");
		} else {

			boolean canDeleteFromSingle = true, canDeleteFromAll = true;

			User user = (User) hibernateSession
					.getNamedQuery("user.by.emailid")
					.setParameter("emailID", email)
					.setParameter("company", company).uniqueResult();

			Query query = hibernateSession.getNamedQuery("get.Admin.Users")
					.setParameter("company", company);
			List<User> adminUsers = query.list();
			if (adminUsers.size() < 2) {
				for (User u : adminUsers) {
					if (u.getID() == user.getID()) {
						canDeleteFromSingle = false;
						break;
					}
				}
			}

			if (user != null && !user.isAdmin()) {
				canDeleteFromAll = false;
			}

			Company serverCompany = null;
			for (User usr : client.getUsers()) {
				if (usr.getCompany().getID() == company.getID()) {
					serverCompany = usr.getCompany();
				}
			}

			if (serverCompany == null) {
				httpSession.setAttribute(COMPANY_DELETION_STATUS, "Fail");
				httpSession.setAttribute("DeletionFailureMessage",
						"Company Doesnot Exist");
			} else if (!canDeleteFromSingle && !canDeleteFromAll) {
				httpSession.setAttribute(COMPANY_DELETION_STATUS, "Fail");
				httpSession.setAttribute("DeletionFailureMessage",
						"You Don't have Permissions to Delete this Company");
			} else {

				Transaction beginTransaction = hibernateSession
						.beginTransaction();
				try {
					company.setTradingName(company.getTradingName()
							+ "- DELETED");
					company.setDeleted(true);
					hibernateSession.saveOrUpdate(company);
					beginTransaction.commit();
					httpSession
							.setAttribute(COMPANY_DELETION_STATUS, "Success");
				} catch (Exception e) {
					httpSession.setAttribute(COMPANY_DELETION_STATUS, "Fail");
					httpSession.setAttribute("DeletionFailureMessage",
							"Internal Error Occured");
				}

				Thread deleteCompanyThread = getDeleteCompanyThread(
						httpSession, serverCompany, user, isDeleteAllUsers,
						canDeleteFromAll, canDeleteFromSingle);
				deleteCompanyThread.start();
			}
		}
	}

	private Thread getDeleteCompanyThread(final HttpSession httpSession,
			final Company company, final User user,
			final boolean isDeleteAllUsers, final boolean canDeleteFromAll,
			final boolean canDeleteFromSingle) {

		final Locale locale = ServerLocal.get();

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				log("Deleting Company.");
				ServerLocal.set(locale);
				AccounterThreadLocal.set(user);
				Session session = HibernateUtil.openSession();
				Transaction transaction = session.beginTransaction();
				try {

					if (canDeleteFromAll
							&& (isDeleteAllUsers || company
									.getNonDeletedUsers().size() == 1)) {

						CallableStatement call = session.connection()
								.prepareCall("{ ? = call delete_company(?) }");
						call.registerOutParameter(1, Types.BOOLEAN);
						call.setLong(2, company.getId());
						call.execute();
						boolean isDeleted = call.getBoolean(1);
						if (!isDeleted) {
							throw new Exception();
						}

					} else if (canDeleteFromSingle) {
						// Commit problem. Two times commiting changes if we
						// call finance tool to delete user.
						user.setDeleted(true);
						session.saveOrUpdate(user);
						Activity activity = new Activity(user.getCompany(),
								user.getCompany().getCreatedBy(),
								ActivityType.DELETE, user);
						session.save(activity);
					}
					transaction.commit();
					log("Company Deleted Successfully.");
				} catch (Exception e) {
					e.printStackTrace();
					if (transaction != null) {
						transaction.rollback();
					}
				} finally {
					session.close();
				}
			}
		});

		return thread;

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailID = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailID == null
				|| req.getSession().getAttribute(COMPANY_ID) == null) {
			req.setAttribute("message",
					"Company deletion failed because of invalide session.");
			dispatch(req, resp, deleteCompanyView);
			return;
		}

		deleteComapny(req, resp);
	}
}
