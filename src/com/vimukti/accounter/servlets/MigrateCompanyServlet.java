package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.MigrateDetails;

public class MigrateCompanyServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final String SELECT_COMPANY_VIEW = "/WEB-INF/migratecompany.jsp";
	private static final String STATUS_URL = "/main/migrationstatus";

	@Override
	protected void doPost(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		String emailId = req.getParameter("emailId").trim();
		final MigrateDetails details = new MigrateDetails();
		details.setFirstName(req.getParameter("firstName").trim());
		details.setLastName(req.getParameter("lastName").trim());
		details.setEmailId(emailId);
		details.setPassword(req.getParameter("password").trim());
		details.setDomain(req.getParameter("domain").trim());
		final Long companyId = Long.valueOf(req.getParameter("company"));
		details.setCompanyID(companyId);
		// Starting Migration Separate Thread
		MigrationTheard thread = new MigrationTheard(details);
		thread.start();
		// Redirecting To Status Servlet
		redirectExternal(req, resp, STATUS_URL + "?companyId=" + companyId);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession httpSession = req.getSession();
		String emailID = (String) httpSession.getAttribute(EMAIL_ID);
		Client client = getClient(emailID);
		List<Company> list = getCompanyList(client.getUsers());
		req.setAttribute(ATTR_COMPANY_LIST, list);
		dispatch(req, resp, SELECT_COMPANY_VIEW);
	}

	@SuppressWarnings("unchecked")
	private List<Company> getCompanyList(Set<User> users) {
		List<Company> list = new ArrayList<Company>();
		List<Long> userIds = new ArrayList<Long>();
		for (User user : users) {
			if (!user.isDeleted()) {
				userIds.add(user.getID());
			}
		}
		List<Object[]> objects = new ArrayList<Object[]>();
		if (!userIds.isEmpty()) {
			Session session = HibernateUtil.getCurrentSession();
			objects = session
					.getNamedQuery(
							"get.CompanyId.Tradingname.and.Country.of.user")
					.setParameterList("userIds", userIds).list();
			addCompanies(list, objects);
		}
		return list;
	}

	private void addCompanies(List<Company> list, List<Object[]> objects) {
		for (Object[] obj : objects) {
			Company com = new Company();
			com.setId((Long) obj[0]);
			com.getPreferences().setTradingName((String) obj[1]);
			com.getRegisteredAddress().setCountryOrRegion((String) obj[2]);
			list.add(com);
		}
	}

}
