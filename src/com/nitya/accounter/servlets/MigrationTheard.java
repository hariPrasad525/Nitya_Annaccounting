package com.nitya.accounter.servlets;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.migration.AccounterMigrator;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.MigrateDetails;
import com.nitya.accounter.web.client.core.MigrationStatus;

public class MigrationTheard extends Thread {

	private MigrateDetails details;

	public MigrationTheard(MigrateDetails details) {
		this.details = details;
	}

	@Override
	public void run() {
		Session hibernateSession = HibernateUtil.openSession();
		Transaction beginTransaction = hibernateSession.beginTransaction();
		String query = "INSERT INTO migration_status (status,info,company_id) VALUES (:status,:info,:company_id)";
		SQLQuery sql = hibernateSession.createSQLQuery(query);
		sql.setParameter("status", MigrationStatus.IN_PROGRESS);
		sql.setParameter("info", "Migration Started");
		long companyId = details.getCompanyID().longValue();
		sql.setParameter("company_id", companyId);
		sql.executeUpdate();
		beginTransaction.commit();
		AccounterMigrator migrator = new AccounterMigrator();
		try {
			migrator.migrate(details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		hibernateSession.close();
	}

}
