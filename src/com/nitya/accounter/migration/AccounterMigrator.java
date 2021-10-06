package com.nitya.accounter.migration;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.MigrateDetails;

public class AccounterMigrator {

	protected Logger log = Logger.getLogger(getClass());

	public void migrate(MigrateDetails details) throws Exception {
		try {
			Session session = HibernateUtil.getCurrentSession();
			Company company = (Company) session.load(Company.class,
					details.getCompanyID());
			CompanyMigrator migrator = new CompanyMigrator(company, details);
			migrator.migrate();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
