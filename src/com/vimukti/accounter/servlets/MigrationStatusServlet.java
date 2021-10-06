package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.MigrationStatus;

public class MigrationStatusServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final String IN_PROGREESS_VIEW = "/WEB-INF/migrationstatus.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("migrate_company_id",
				Long.valueOf(req.getParameter("companyId")));
		dispatch(req, resp, IN_PROGREESS_VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession httpSession = req.getSession();
		if (httpSession == null) {
			resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			return;
		}
		Long companyID = (Long) httpSession.getAttribute("migrate_company_id");
		if (companyID == null) {
			resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			return;
		}
		MigrationStatus migrationStatus = getMigrationStatus(companyID);
		resp.setContentType("text/plain");
		resp.getWriter().write(String.valueOf(migrationStatus.getStatus()));
	}

	@SuppressWarnings("rawtypes")
	public MigrationStatus getMigrationStatus(Long companyID) {
		Session hibernateSession = HibernateUtil.getCurrentSession();
		String query = "SELECT status,info FROM migration_status where company_id=:company_id";
		SQLQuery sql = hibernateSession.createSQLQuery(query);
		sql.setParameter("company_id", companyID);
		List list = sql.list();
		if (list.isEmpty()) {
			return null;
		}
		Object[] object = (Object[]) list.get(0);
		MigrationStatus migrationStatus = new MigrationStatus();
		migrationStatus.setStatus((Integer) object[0]);
		migrationStatus.setInfo((String) object[1]);
		return migrationStatus;
	}
}
