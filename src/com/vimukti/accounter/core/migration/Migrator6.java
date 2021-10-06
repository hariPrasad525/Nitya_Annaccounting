package com.vimukti.accounter.core.migration;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TDSChalanDetail;

public class Migrator6 extends AbstractMigrator {
	Logger log = Logger.getLogger(Migrator6.class);

	@Override
	public void migrate(Company company) {
		log.info("Started Migrator6");

		Session session = getSession();

		List<TDSChalanDetail> challans = session
				.getNamedQuery("get.All.TdsChallans")
				.setEntity("company", company).list();

		for (TDSChalanDetail chalan : challans) {
			if (chalan.getFromDate() == null || chalan.getToDate() == null) {
				FinanceDate[] dates = getFinancialQuarter(company,
						chalan.getChalanPeriod());
				if (chalan.getFromDate() == null) {
					chalan.setFromDate(dates[0]);
				}
				if (chalan.getToDate() == null) {
					chalan.setToDate(dates[1]);
				}
				session.saveOrUpdate(chalan);
			}
		}

		log.info("Fininshed Migrator6");
	}

	@Override
	public int getVersion() {
		return 6;
	}

	public static FinanceDate[] getFinancialQuarter(Company company, int quarter) {

		FinanceDate startDate;
		FinanceDate endDate;

		FinanceDate start = getCurrentFiscalYearStartDate(company);

		switch (quarter) {
		case 1:
			startDate = start;
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(start.getAsDateObject());
			endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 3);
			endCal.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new FinanceDate(endCal.getTime());
			break;

		case 2:
			startDate = start;
			startDate.setMonth(start.getMonth() + 3);
			Calendar endCal2 = Calendar.getInstance();
			endCal2.setTime(startDate.getAsDateObject());
			endCal2.set(Calendar.MONTH, endCal2.get(Calendar.MONTH) + 3);
			endCal2.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new FinanceDate(endCal2.getTime());
			break;

		case 3:
			startDate = start;
			startDate.setMonth(start.getMonth() + 6);
			Calendar endCal3 = Calendar.getInstance();
			endCal3.setTime(startDate.getAsDateObject());
			endCal3.set(Calendar.MONTH, endCal3.get(Calendar.MONTH) + 3);
			endCal3.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new FinanceDate(endCal3.getTime());
			break;
		default:
			startDate = start;
			startDate.setMonth(start.getMonth() + 9);
			Calendar endCal4 = Calendar.getInstance();
			endCal4.setTime(startDate.getAsDateObject());
			endCal4.set(Calendar.MONTH, endCal4.get(Calendar.MONTH) + 3);
			endCal4.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new FinanceDate(endCal4.getTime());
			break;
		}
		FinanceDate[] dates = new FinanceDate[] { startDate, endDate };
		return dates;
	}

	private static FinanceDate getCurrentFiscalYearStartDate(Company company) {

		Calendar cal = Calendar.getInstance();
		FinanceDate startDate = new FinanceDate();
		cal.setTime(startDate.getAsDateObject());
		cal.set(Calendar.MONTH, company.getPreferences()
				.getFiscalYearFirstMonth());
		cal.set(Calendar.DAY_OF_MONTH, 1);

		while (new FinanceDate(cal.getTime()).after(new FinanceDate())) {
			cal.add(Calendar.YEAR, -1);
		}
		startDate = new FinanceDate(cal.getTime());
		return startDate;

	}
}
