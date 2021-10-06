package com.vimukti.accounter.core.migration;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MigrationUtil {

	static Logger log = Logger.getLogger(MigrationUtil.class);

	public static void migrate(Company company) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction t1 = session.beginTransaction();
		session.getNamedQuery("lock.company")
				.setParameter("companyId", company.getID()).executeUpdate();
		t1.commit();
		try {

			for (int version = company.getVersion() + 1; version <= Company.CURRENT_VERSION; version++) {
				Transaction transaction = session.beginTransaction();
				try {
					IMigrator migrator = (IMigrator) Class.forName(
							"com.vimukti.accounter.core.migration.Migrator"
									+ version).newInstance();
					migrator.migrate(company);
					company.setVersion(version);
					session.save(company);
					transaction.commit();
				} catch (Exception e) {
					log.error("Company ID : " + company.getId());
					e.printStackTrace();
					transaction.rollback();
					break;
				}
			}
		} finally {
			Transaction t2 = session.beginTransaction();
			session.getNamedQuery("unlock.company")
					.setParameter("companyId", company.getID()).executeUpdate();
			t2.commit();
		}

	}

}
