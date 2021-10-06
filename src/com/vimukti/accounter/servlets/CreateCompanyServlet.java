package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mortbay.util.UrlEncoded;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserPermissions;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class CreateCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/CreateCompany.jsp";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"max-age=0,no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		HttpSession session = request.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(request, response, LOGIN_URL);
			return;
		}
		doCreateCompany(request, response, emailID);

	}

	private void doCreateCompany(HttpServletRequest request,
			HttpServletResponse response, String emailID) throws IOException {
		String companyName = request.getParameter("name");
		// String companyTypeStr = request.getParameter("companyType");
		if (!isValidCompanyName(companyName)) {
			request.setAttribute("errormessage",
					"Invalid Company Name. Name Should be grater than 5 characters");
			dispatch(request, response, view);
			return;
		}
		// int companyType = getCompanyType(companyTypeStr);
		// if (companyType < 0) {
		// request.setAttribute("errormessage", "Invalid Company Type.");
		// dispatch(request, response, view);
		// return;
		// }
		Company company = null;
		Client client = null;
		Session session = HibernateUtil.getCurrentSession();
		final HttpSession httpSession = request.getSession(true);
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			company = getCompany(companyName);
			if (company != null) {
				request.setAttribute("errormessage",
						"Company exist with the same name.");
				dispatch(request, response, view);
				return;
			}
			client = getClient(emailID);
			if (client == null) {
				request.setAttribute("errormessage",
						"Your session was expired. please login again.");
				dispatch(request, response, view);
				return;
			}

			company = new Company();
			// company.getClients().add(client);
			// company.setActive(true);
			company.setTradingName(companyName);
			// company.setAccountingType(companyType);
			company.setConfigured(false);
			company.setCreatedDate(new Date());
			// company.setServer(getPreferredServer(companyType, companyName,
			// request.getRemoteAddr()));
			session.saveOrUpdate(company);

			User user = new User(getUser(client));
			user.setActive(true);
			user.setClient(client);
			user.setCompany(company);
			session.save(user);

			client.getUsers().add(user);
			session.saveOrUpdate(client);

			AccounterThreadLocal.set(user);
			company.setCreatedBy(user);
			company.getUsers().add(user);
			company.setCompanyEmail(user.getClient().getEmailId());

			session.saveOrUpdate(company);

			// UsersMailSendar.sendMailToDefaultUser(user,
			// company.getTradingName());

			httpSession.setAttribute(COMPANY_CREATION_STATUS, "Success");
			transaction.commit();
			// updateServers(company.getServer(), true);
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			httpSession.setAttribute(COMPANY_CREATION_STATUS, "Fail");
		}
		dispatch(request, response, COMPANIES_URL);
		return;
	}

	// /**
	// *
	// * @return
	// */
	// private Server getPreferredServer(int companyType, String companyName,
	// String sourceAddr) {
	// return ServerAllocationFactory.getServerAllocator().allocateServer(
	// companyType, companyName, sourceAddr);
	// }

	private ClientUser getUser(Client client) {
		User user = client.toUser();
		// user.setFullName(user.getFirstName() + " " + user.getLastName());
		user.setUserRole(RolePermissions.ADMIN);
		user.setAdmin(true);
		user.setCanDoUserManagement(true);
		UserPermissions permissions = new UserPermissions();
		permissions.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		permissions.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
		permissions.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		permissions.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		permissions.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		permissions.setTypeOfViewReports(RolePermissions.TYPE_YES);
		permissions.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		permissions.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);
		user.setPermissions(permissions);
		return user.getClientUser();
	}

	private Company getCompany(String companyName) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getServerCompany.by.name")
				.setParameter("name", companyName);
		return (Company) query.uniqueResult();
	}

	/**
	 * @return
	 */
	private String getUrlString(Company company, String emailID, Client client) {
		StringBuffer buffer = new StringBuffer(
				"http://localhost:8890/initialzeCompany?");

		buffer.append(PARAM_SERVER_COMPANY_ID);
		buffer.append('=');
		buffer.append(new UrlEncoded(String.valueOf(company.getID())).encode());

		buffer.append('&');
		buffer.append(PARA_COMPANY_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded(company.getTradingName()).encode());

		// buffer.append('&');
		// buffer.append(PARAM_COMPANY_TYPE);
		// buffer.append('=');
		// buffer.append(new UrlEncoded(
		// String.valueOf(company.getAccountingType())).encode());

		buffer.append('&');
		buffer.append(EMAIL_ID);
		buffer.append('=');
		buffer.append(new UrlEncoded(emailID).encode());

		buffer.append('&');
		buffer.append(PARAM_FIRST_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded(client.getFirstName()).encode());

		buffer.append('&');
		buffer.append(PARAM_LAST_NAME);
		buffer.append('=');
		buffer.append(new UrlEncoded((client.getLastName())).encode());

		return buffer.toString();

	}

	private int getCompanyType(String companyType) {
		if (companyType != null) {
			int type = Integer.parseInt(companyType);
			if (type == Company.ACCOUNTING_TYPE_UK
					|| type == Company.ACCOUNTING_TYPE_US
					|| type == Company.ACCOUNTING_TYPE_INDIA
					|| type == Company.ACCOUNTING_TYPE_OTHER) {
				return type;
			}
		}
		return -1;
	}

	private boolean isValidCompanyName(String companyId) {
		return companyId.trim().length() > 5;
		// return companyId.matches("^[A-Za-z0-9]{5,}$");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(request, response, LOGIN_URL);
		}

		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"max-age=0,no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		dispatch(request, response, view);
	}
}
