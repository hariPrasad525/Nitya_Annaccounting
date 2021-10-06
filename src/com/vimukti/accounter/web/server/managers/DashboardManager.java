package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IncomeExpensePortletInfo;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AccountDetail;
import com.vimukti.accounter.web.client.ui.ExpensePortletData;
import com.vimukti.accounter.web.client.ui.GraphChart;
import com.vimukti.accounter.web.client.ui.PayeesBySalesPortletData;
import com.vimukti.accounter.web.client.ui.YearOverYearPortletData;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class DashboardManager extends Manager {

	public ArrayList<Double> getBankingChartValues(long accountId,
			long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		FinanceDate currentDate = new FinanceDate();

		Calendar dateCal[] = new Calendar[4];
		if (dateCal[3] == null)
			dateCal[3] = new GregorianCalendar();

		dateCal[3].setTime(currentDate.getAsDateObject());

		for (int i = 2; i >= 0; i--) {
			if (dateCal[i] == null)
				dateCal[i] = new GregorianCalendar();

			dateCal[i].setTime(dateCal[i + 1].getTime());
			dateCal[i].set(Calendar.DATE, dateCal[i].get(Calendar.DATE) - 1);

			if (dateCal[i].get(Calendar.DATE) <= 0) {
				dateCal[i].set(Calendar.MONTH,
						dateCal[i].get(Calendar.MONTH) - 1);
				dateCal[i].set(
						Calendar.DATE,
						dateCal[i].getActualMaximum(Calendar.DATE)
								- dateCal[i].get(Calendar.DATE));
			}

		}

		Query query = session
				.getNamedQuery("getPointsForBankAccount")
				.setLong("companyId", companyId)
				.setParameter("accountId", accountId)
				.setParameter("previousThreeDaysBackDateCal",
						new FinanceDate(dateCal[0].getTime()).getDate())
				.setParameter("previousTwoDaysBackDateCal",
						new FinanceDate(dateCal[1].getTime()).getDate())
				.setParameter("previousOneDayBackDateCal",
						new FinanceDate(dateCal[2].getTime()).getDate());

		List<Double> gPoints = new ArrayList<Double>();
		List list = query.list();
		if (list != null) {
			Object[] object = null;
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				gPoints.add(object[0] == null ? 0 : (Double) object[0]);
				gPoints.add(object[1] == null ? 0 : (Double) object[1]);
				gPoints.add(object[2] == null ? 0 : (Double) object[2]);
				gPoints.add(object[3] == null ? 0 : (Double) object[3]);

			}
		}
		return new ArrayList<Double>(gPoints);
	}

	public ArrayList<CreditCardCharge> getCreditCardChargesThisMonth(
			final long date, long companyId) throws DAOException {
		// SELECT * from com.vimukti.accounter.core.CREDIT_CARD_CHARGES CCC JOIN
		// TRANSACTION T ON T.ID =
		// CCC.ID AND T.T_DATE = CURRENT_DATE
		// List<CreditCardCharge> list = template.find(
		// "from CreditCardCharge ccc where ccc.company = ? and  MONTH(ccc.date) = MONTH(?) and YEAR(ccc.date) = YEAR(?)"
		// ,new Object[] {company, date, date});

		Session session = HibernateUtil.getCurrentSession();

		int month = 0;
		int year = 0;
		FinanceDate fdate = new FinanceDate(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(fdate.getAsDateObject());
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);

		Query query = session.getNamedQuery("getCreditCardChargesThisMonth")
				.setInteger("month", month).setLong("companyId", companyId);
		Iterator iterator = query.list().iterator();
		List<CreditCardCharge> list = new ArrayList<CreditCardCharge>();
		while (iterator.hasNext()) {
			list.add((CreditCardCharge) iterator.next());
		}

		return new ArrayList<CreditCardCharge>(list);
	}

	public List<ClientPayee> getWhoIOwe(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		List<BigInteger> vendors = session.getNamedQuery("getWhoIOweVendors")
				.setParameter("companyId", companyId).list();
		List<ClientPayee> clientPayees = new ArrayList<ClientPayee>();
		ClientCompany company;
		try {
			company = new ClientConvertUtil().toClientObject(
					getCompany(companyId), ClientCompany.class);
			for (BigInteger vendorId : vendors) {
				ClientVendor vendor = company.getVendor(vendorId.longValue());
				if (DecimalUtil.isEquals(vendor.getBalance(), 0.0)) {
					continue;
				}
				clientPayees.add(vendor);
			}
		} catch (AccounterException e) {
		}
		return clientPayees;
	}

	public List<ClientPayee> getWhoOwesMe(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		List<BigInteger> customers = session
				.getNamedQuery("getWhoOwesMeCustomers")
				.setParameter("companyId", companyId).list();
		List<ClientPayee> clientPayees = new ArrayList<ClientPayee>();
		ClientCompany company;
		try {
			company = new ClientConvertUtil().toClientObject(
					getCompany(companyId), ClientCompany.class);

			for (BigInteger customerId : customers) {
				ClientCustomer customer = company.getCustomer(customerId
						.longValue());
				if (DecimalUtil.isEquals(customer.getBalance(), 0.0)) {
					continue;
				}
				clientPayees.add(company.getCustomer(customerId.longValue()));
			}
		} catch (AccounterException e) {
		}
		return clientPayees;
	}

	public ArrayList<OverDueInvoicesList> getOverDueInvoices(long companyId)
			throws DAOException {
		// try {
		// Session session = getSessionFactory().openSession();
		// Query query = session.createSQLQuery(
		// "SELECT * FROM INVOICE I LEFT JOIN TRANSACTION T ON I.ID = T.ID WHERE T.COMPANY_ID = :C_ID AND I.BALANCE_DUE > 0.0"
		// ).addEntity(Invoice.class);
		// query.setLong("C_ID", company);
		// List<Invoice> list = query.list();
		// // HibernateTemplate template = getHibernateTemplate();
		// // List<Invoice> list = template.find(
		// "from Invoice i where i.company = ? and i.balanceDue > 0.0 and i.dueDate <= current_date"
		// ,new Object[] {company});
		//
		// if (list != null) {
		// return list;
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DAOException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }

		try {
			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestOverDueInvoices")
					.setParameter("companyId", companyId);
			// FIXME ::: check the sql query and change it to hql query if
			// required
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<OverDueInvoicesList> queryResult = new ArrayList<OverDueInvoicesList>();
				while ((iterator).hasNext()) {

					OverDueInvoicesList overDueInvoicesList = new OverDueInvoicesList();
					object = (Object[]) iterator.next();

					overDueInvoicesList
							.setTransactionId((object[0] == null ? null
									: ((String) object[0])));
					overDueInvoicesList.setDueDate((object[1] == null ? null
							: (new ClientFinanceDate((Long) object[1]))));
					overDueInvoicesList.setCustomerName((String) object[2]);
					overDueInvoicesList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					overDueInvoicesList.setTotalAmount((Double) object[4]);
					overDueInvoicesList.setPayment((Double) object[5]);

					overDueInvoicesList.setBalanceDue((Double) object[6]);

					queryResult.add(overDueInvoicesList);
				}
				return new ArrayList<OverDueInvoicesList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<Double> getMoneyInChartValues(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = null;

		FinanceDate currentDate = new FinanceDate();

		List<Double> gPoints = new ArrayList<Double>();

		for (int i = 4; i >= -1; i--) {
			Calendar startDateCal = Calendar.getInstance();
			startDateCal.setTime(currentDate.getAsDateObject());
			startDateCal.set(Calendar.MONTH, startDateCal.get(Calendar.MONTH)
					- i);
			startDateCal.set(Calendar.DATE, 1);

			Calendar endDateCal = Calendar.getInstance();
			endDateCal.setTime(currentDate.getAsDateObject());
			endDateCal.set(Calendar.MONTH, endDateCal.get(Calendar.MONTH) - i);
			endDateCal.set(Calendar.DATE,
					endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			gPoints.add(getMoneyInForDates(
					new FinanceDate(startDateCal.getTime()).getDate(),
					new FinanceDate(endDateCal.getTime()).getDate(), companyId));
		}

		Object res = session.getNamedQuery("getInvoicesDue")
				.setParameter("presentDate", 0).setLong("companyId", companyId)
				.uniqueResult();
		double amount = res == null ? 0 : (Double) res;
		gPoints.add(amount);

		res = session.getNamedQuery("getInvoicesDue")
				.setParameter("presentDate", (new FinanceDate()).getDate())
				.setLong("companyId", companyId).uniqueResult();
		amount = res == null ? 0 : (Double) res;
		gPoints.add(amount);

		return new ArrayList<Double>(gPoints);
	}

	public ArrayList<Double> getMoneyOutChartValues(long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		Query query = null;

		FinanceDate currentDate = new FinanceDate();

		Calendar dateCal = Calendar.getInstance();

		List<Double> gPoints = new ArrayList<Double>();
		for (int i = 0; i < 30; i++) {

			dateCal.setTime(currentDate.getAsDateObject());
			dateCal.set(Calendar.DATE, dateCal.get(Calendar.DATE) + i);

			gPoints.add(getMoneyOutOnDate(
					new FinanceDate(dateCal.getTime()).getDate(), companyId));
		}

		Object res = session.getNamedQuery("getBillsDue")
				.setParameter("presentDate", 0).setLong("companyId", companyId)
				.uniqueResult();
		double amount = res == null ? 0 : (Double) res;
		gPoints.add(amount);

		res = session.getNamedQuery("getBillsDue")
				.setParameter("presentDate", (new FinanceDate()).getDate())
				.setLong("companyId", companyId).uniqueResult();
		amount = res == null ? 0 : (Double) res;
		gPoints.add(amount);

		return new ArrayList<Double>(gPoints);
	}

	public ArrayList<Double> getExpensePortletValues(long companyId) {
		Session session = HibernateUtil.getCurrentSession();

		Double cashExpenseTotal = (Double) session
				.getNamedQuery("getCashExpenseTotal")
				.setLong("companyId", companyId).uniqueResult();
		Double creditCardExpenseTotal = (Double) session
				.getNamedQuery("getCreditCardExpenseTotal")
				.setLong("companyId", companyId).uniqueResult();
		Double employeeExpenseTotal = (Double) session
				.getNamedQuery("getEmployeeExpenseTotal")
				.setLong("companyId", companyId).uniqueResult();
		double allExpensesTotal = (cashExpenseTotal != null ? cashExpenseTotal
				: 0)
				+ (creditCardExpenseTotal != null ? creditCardExpenseTotal : 0)
				+ (employeeExpenseTotal != null ? employeeExpenseTotal : 0);
		List<Double> gPoints = new ArrayList<Double>();
		// List list = query.list();
		// if (list != null) {
		// Object[] object = null;
		// Iterator iterator = list.iterator();
		//
		// while (iterator.hasNext()) {
		// object = (Object[]) iterator.next();

		gPoints.add(cashExpenseTotal != null ? cashExpenseTotal : 0);
		gPoints.add(creditCardExpenseTotal != null ? creditCardExpenseTotal : 0);
		gPoints.add(employeeExpenseTotal != null ? employeeExpenseTotal : 0);
		gPoints.add(allExpensesTotal);
		// }
		// }
		return new ArrayList<Double>(gPoints);
	}

	private double getMoneyInForDates(long startDate, long endDate,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getMoneyInForDates")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setLong("companyId", companyId);

		List<Double> list = query.list();
		double amount = 0;
		if (list != null && !list.isEmpty()) {
			for (Double d : list) {
				amount += (d != null ? d : 0);
			}
			return amount;
		} else {
			return 0.0D;
		}
	}

	private double getMoneyOutOnDate(long date, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getMoneyOutOnDate")
				.setParameter("date", date).setLong("companyId", companyId);

		List<Double> list = query.list();
		double amount = 0;
		if (list != null && !list.isEmpty()) {
			for (Double d : list) {
				amount += (d != null ? d : 0);
			}
			return amount;
		} else {
			return 0.0D;
		}
	}

	public ArrayList<Double> getGraphPointsforAccount(int chartType,
			long accountId, long companyId) throws DAOException {

		if (chartType == GraphChart.BANK_ACCOUNT_CHART_TYPE) {
			return getBankingChartValues(accountId, companyId);
		} else if (chartType == GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE) {
			return getMoneyInChartValues(companyId);
		} else if (chartType == GraphChart.ACCOUNTS_PAYABLE_CHART_TYPE) {
			return getMoneyOutChartValues(companyId);
		} else {
			return getExpensePortletValues(companyId);
		}
	}

	public ExpensePortletData getExpensesAccountsBalances(long companyId,
			long startDate, long endDate) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			Double cashExpenseTotal = (Double) session
					.getNamedQuery("getExpensesTotalByType")
					.setParameter("expenseType", 26)
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate).uniqueResult();

			Double creditCardExpenseTotal = (Double) session
					.getNamedQuery("getExpensesTotalByType")
					.setParameter("expenseType", 27)
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate).uniqueResult();

			Query query = session.getNamedQuery("getRecordExpensesAccounts")
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate);
			List<Object[]> objects = query.list();

			ExpensePortletData expensePortletData = new ExpensePortletData();
			Iterator<Object[]> iterator = objects.iterator();
			Double expenseAccountTotal = 0.0;
			List<AccountDetail> accountDetails = new ArrayList<AccountDetail>();
			while (iterator.hasNext()) {
				Object[] object = iterator.next();
				String accountName = (String) object[0];
				Double accountBalance = (Double) object[1];
				expenseAccountTotal = expenseAccountTotal + accountBalance;
				if (accountBalance > 0) {
					AccountDetail accountDetail = new AccountDetail(
							accountName, accountBalance);
					accountDetails.add(accountDetail);
				}

			}
			expensePortletData.setAccountDetails(accountDetails);
			expensePortletData.setCashExpenseTotal(cashExpenseTotal == null ? 0
					: cashExpenseTotal);
			expensePortletData
					.setCreditCardExpensesTotal(creditCardExpenseTotal == null ? 0
							: creditCardExpenseTotal);
			expensePortletData
					.setAllExpensesTotal(expenseAccountTotal == null ? 0
							: expenseAccountTotal);

			return expensePortletData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<BillsList> getEmployeeExpensesByStatus(
			String employeeName, int status, long companyId)
			throws DAOException {

		List<BillsList> billsList = new ArrayList<BillsList>();
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = null;
		if (employeeName != null)
			query = session
					.getNamedQuery(
							"getCashPurchase.by.employeeNmae.expenseStatusandtype")
					.setParameter("employeeName", employeeName,
							EncryptedStringType.INSTANCE)
					.setParameter("expenseStatus", status)
					.setParameter("type", Transaction.TYPE_EMPLOYEE_EXPENSE)
					.setEntity("company", company);
		else
			query = session
					.getNamedQuery("getCashPurchase.by.expenseStatusandtype")
					.setParameter("expenseStatus", status)
					.setParameter("type", Transaction.TYPE_EMPLOYEE_EXPENSE)
					.setEntity("company", company);

		List<CashPurchase> cashpurchase = query.list();
		for (CashPurchase cp : cashpurchase) {
			BillsList bills = new BillsList();
			bills.setTransactionId(cp.getID());
			bills.setCurrency(cp.getCurrency().getID());
			bills.setOriginalAmount(cp.getTotal());
			bills.setVendorName(cp.getEmployee() != null ? cp.getEmployee()
					.getName() : "");
			bills.setDate(new ClientFinanceDate(cp.getDate().getDate()));
			bills.setExpenseStatus(status);
			bills.setType(Transaction.TYPE_EMPLOYEE_EXPENSE);
			bills.setPayFrom(cp.getPayFrom() != null ? cp.getPayFrom().getID()
					: 0);

			/*
			 * Here, to set transaction created date temporarly using setDueDate
			 * method
			 */
			bills.setDueDate(new ClientFinanceDate(cp.getCreatedDate()
					.getTime()));
			billsList.add(bills);
		}
		return new ArrayList<BillsList>(billsList);
	}

	public KeyFinancialIndicators getKeyFinancialIndicators(long companyId)
			throws DAOException {

		KeyFinancialIndicators keyFinancialIndicators = new KeyFinancialIndicators();

		Map<String, Map<Integer, Double>> rows = new LinkedHashMap<String, Map<Integer, Double>>();

		Session session = HibernateUtil.getCurrentSession();
		List<Account> accounts = new ArrayList<Account>(session
				.getNamedQuery("list.Account")
				.setEntity("company", getCompany(companyId)).list());
		// List<Account> accounts = getCompany().getAccounts();

		Set<Account> sales = new HashSet<Account>();
		Set<Account> directCosts = new HashSet<Account>();
		Set<Account> indirectCosts = new HashSet<Account>();
		Set<Account> bankAccounts = new HashSet<Account>();

		for (Account account : accounts) {
			switch (account.getType()) {
			case Account.TYPE_INCOME:
				sales.add(account);
				break;
			case Account.TYPE_COST_OF_GOODS_SOLD:
				directCosts.add(account);
				break;
			case Account.TYPE_OTHER_EXPENSE:
				directCosts.add(account);
				break;
			case Account.TYPE_EXPENSE:
				indirectCosts.add(account);
				break;
			case Account.TYPE_OTHER_CURRENT_ASSET:
				Long l1 = Long.parseLong(account.getNumber());
				if (l1 >= 1100 && l1 <= 1179)
					bankAccounts.add(account);
				break;
			}
		}

		Map<Integer, Double> salesEntries = new LinkedHashMap<Integer, Double>();
		for (Account account : sales) {
			Map monthViceAmounts = account.getMonthViceAmounts();
			Set<Integer> keys = monthViceAmounts.keySet();
			for (Integer key : keys) {
				if (salesEntries.containsKey(key)) {
					salesEntries.put(key, salesEntries.get(key)
							+ (Double) monthViceAmounts.get(key));
				} else {
					salesEntries.put(key, (Double) monthViceAmounts.get(key));
				}
			}
		}
		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_SALES,
				salesEntries);

		Map<Integer, Double> grossProfitEntries = new LinkedHashMap<Integer, Double>();

		Map<Integer, Double> directCostsEntries = new LinkedHashMap<Integer, Double>();
		for (Account account : directCosts) {
			Map monthViceAmounts = account.getMonthViceAmounts();
			Set<Integer> keys = monthViceAmounts.keySet();
			for (Integer key : keys) {
				if (directCostsEntries.containsKey(key)) {
					directCostsEntries.put(key, directCostsEntries.get(key)
							+ (Double) monthViceAmounts.get(key));
				} else {
					directCostsEntries.put(key,
							(Double) monthViceAmounts.get(key));
				}

				// double salesEntryAmount = 0.0;
				// if (salesEntries.containsKey(key)) {
				// salesEntryAmount = salesEntries.get(key);
				// }
				// double directCostsAmount = 0.0;
				// if (directCostsEntries.containsKey(key)) {
				// directCostsAmount = directCostsEntries.get(key);
				// }
				//
				// // to calculate the gross profit
				// if (grossProfitEntries.containsKey(key)) {
				// grossProfitEntries.put(key, grossProfitEntries.get(key)
				// + (salesEntryAmount - directCostsAmount));
				// } else {
				// grossProfitEntries.put(key, salesEntryAmount
				// - directCostsAmount);
				// }
			}
		}

		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_DIRECT_COSTS,
				directCostsEntries);

		// for (Integer key : salesEntries.keySet()) {
		//
		// double grossProfitAmount = 0.0;
		// if (directCostsEntries.containsKey(key)) {
		//
		// grossProfitAmount = salesEntries.get(key)
		// - directCostsEntries.get(key);
		// } else {
		// grossProfitAmount = salesEntries.get(key);
		// }
		//
		// // to calculate the gross profit
		// if (grossProfitEntries.containsKey(key)) {
		// grossProfitEntries.put(key, grossProfitEntries.get(key)
		// + (grossProfitAmount));
		// } else {
		// grossProfitEntries.put(key, grossProfitAmount);
		// }
		//
		// }
		List<Integer> keysList = session
				.createSQLQuery(
						"select month from ACCOUNT_AMOUNTS group by month order by month")
				.list();
		for (int key : keysList) {
			double salesEntryAmount = 0.0;
			if (salesEntries.containsKey(key)) {
				salesEntryAmount = salesEntries.get(key);
			}
			double directCostsAmount = 0.0;
			if (directCostsEntries.containsKey(key)) {
				directCostsAmount = directCostsEntries.get(key);
			}

			// to calculate the gross profit
			if (grossProfitEntries.containsKey(key)) {
				grossProfitEntries.put(key, grossProfitEntries.get(key)
						+ (salesEntryAmount - directCostsAmount));
			} else {
				grossProfitEntries.put(key, salesEntryAmount
						- directCostsAmount);
			}
		}

		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_GROSS_PROFIT,
				grossProfitEntries);

		Map<Integer, Double> netProfitEntries = new LinkedHashMap<Integer, Double>();

		Map<Integer, Double> indirectCostsEntries = new LinkedHashMap<Integer, Double>();
		for (Account account : indirectCosts) {
			Map monthViceAmounts = account.getMonthViceAmounts();
			Set<Integer> keys = monthViceAmounts.keySet();
			for (Integer key : keys) {
				if (indirectCostsEntries.containsKey(key)) {
					indirectCostsEntries.put(key, indirectCostsEntries.get(key)
							+ (Double) monthViceAmounts.get(key));
				} else {
					indirectCostsEntries.put(key,
							(Double) monthViceAmounts.get(key));
				}
				// to calculate the net profit.

				// double grossProfitAmount = 0.0;
				// if (grossProfitEntries.containsKey(key)) {
				// grossProfitAmount = grossProfitEntries.get(key);
				// }
				// double indirectCostsAmount = 0.0;
				// if (indirectCostsEntries.containsKey(key)) {
				// indirectCostsAmount = indirectCostsEntries.get(key);
				// }
				//
				// if (netProfitEntries.containsKey(key)) {
				// netProfitEntries.put(key, netProfitEntries.get(key)
				// + (grossProfitAmount - indirectCostsAmount));
				// } else {
				// netProfitEntries.put(key, grossProfitAmount
				// - indirectCostsAmount);
				// }

			}
		}
		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_INDIRECT_COSTS,
				indirectCostsEntries);

		// for (Integer key : grossProfitEntries.keySet()) {
		//
		// double netProfitAmount = 0.0;
		// if (indirectCostsEntries.containsKey(key)) {
		//
		// netProfitAmount = grossProfitEntries.get(key) -
		// indirectCostsEntries.get(key);
		// } else {
		// netProfitAmount = grossProfitEntries.get(key);
		// }
		//
		// // to calculate the gross profit
		// if (netProfitEntries.containsKey(key)) {
		// netProfitEntries.put(key, grossProfitEntries.get(key)
		// + (netProfitAmount));
		// } else {
		// netProfitEntries.put(key, netProfitAmount);
		// }
		//
		// }

		for (int key : keysList) {
			double grossProfitAmount = 0.0;
			if (grossProfitEntries.containsKey(key)) {
				grossProfitAmount = grossProfitEntries.get(key);
			}
			double indirectCostsAmount = 0.0;
			if (indirectCostsEntries.containsKey(key)) {
				indirectCostsAmount = indirectCostsEntries.get(key);
			}

			if (netProfitEntries.containsKey(key)) {
				netProfitEntries.put(key, netProfitEntries.get(key)
						+ (grossProfitAmount - indirectCostsAmount));
			} else {
				netProfitEntries.put(key, grossProfitAmount
						- indirectCostsAmount);
			}
		}

		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_NET_PROFIT,
				netProfitEntries);

		Map<Integer, Double> bankEntries = new LinkedHashMap<Integer, Double>();
		for (Account account : bankAccounts) {
			Map monthViceAmounts = account.getMonthViceAmounts();
			Set<Integer> keys = monthViceAmounts.keySet();
			for (Integer key : keys) {
				if (bankEntries.containsKey(key)) {
					bankEntries.put(key, bankEntries.get(key)
							+ (Double) monthViceAmounts.get(key));
				} else {
					bankEntries.put(key, (Double) monthViceAmounts.get(key));
				}
			}
		}
		Map<Integer, Double> empty = new LinkedHashMap<Integer, Double>();
		rows.put("empty", empty);

		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_BANK_ACCOUNTS,
				bankEntries);
		keyFinancialIndicators.setIndicators(rows);
		return keyFinancialIndicators;
	}

	public ArrayList<IncomeExpensePortletInfo> getIncomeExpensePortletInfo(
			long companyId, int type, long startDate, long endDate) {

		ArrayList<IncomeExpensePortletInfo> result = new ArrayList<IncomeExpensePortletInfo>();

		if (type == 0 || type == 1) {
			IncomeExpensePortletInfo incomeExpensePortletInfo = getIncomExpenseRecordsByMonth(
					startDate, endDate, companyId);
			result.add(incomeExpensePortletInfo);
		} else if (type == 4 || type == 5) {
			List<IncomeExpensePortletInfo> incomeExpensePortletInfos = getIncomExpenseRecordsOfMonths(
					startDate, endDate, companyId, 3);
			result.addAll(incomeExpensePortletInfos);
		} else if (type == 2 || type == 3) {
			List<IncomeExpensePortletInfo> incomeExpensePortletInfos = getIncomExpenseRecordsOfMonths(
					startDate, endDate, companyId, 12);
			result.addAll(incomeExpensePortletInfos);
		}

		return result;
	}

	private List<IncomeExpensePortletInfo> getIncomExpenseRecordsOfMonths(
			long startDate, long endDate, long companyId, int j) {
		Session session = HibernateUtil.getCurrentSession();

		FinanceDate currentDate = new FinanceDate(startDate);

		List<IncomeExpensePortletInfo> incomeExpensePortletInfos = new ArrayList<IncomeExpensePortletInfo>();
		for (int i = 0; i < j; i++) {
			Calendar startDateCal = Calendar.getInstance();
			startDateCal.setTime(currentDate.getAsDateObject());
			startDateCal.set(Calendar.MONTH, startDateCal.get(Calendar.MONTH)
					+ i);
			startDateCal.set(Calendar.DATE, 1);

			Calendar endDateCal = Calendar.getInstance();
			endDateCal.setTime(currentDate.getAsDateObject());
			endDateCal.set(Calendar.MONTH, endDateCal.get(Calendar.MONTH) + i);
			endDateCal.set(Calendar.DATE,
					endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			IncomeExpensePortletInfo incomeExpensePortletInfo = new IncomeExpensePortletInfo();

			Query query = session
					.getNamedQuery("getIncomeExpensePortletInfo")
					.setParameter("companyId", companyId)
					.setParameter("startDate",
							new FinanceDate(startDateCal.getTime()).getDate())
					.setParameter("endDate",
							new FinanceDate(endDateCal.getTime()).getDate());
			Object[] object = (Object[]) query.uniqueResult();

			if (object[0] != null) {
				incomeExpensePortletInfo.setIncome((Double) object[0]);
			} else {
				incomeExpensePortletInfo.setIncome(0.0);
			}
			if (object[1] != null) {
				incomeExpensePortletInfo.setExpense((Double) object[1]);
			} else {
				incomeExpensePortletInfo.setExpense(0.0);
			}
			incomeExpensePortletInfo.setMonth(startDateCal.get(Calendar.MONTH));
			incomeExpensePortletInfos.add(incomeExpensePortletInfo);
		}
		return incomeExpensePortletInfos;
	}

	private IncomeExpensePortletInfo getIncomExpenseRecordsByMonth(
			long startDate, long endDate, long companyId) {
		Session session = HibernateUtil.getCurrentSession();

		int month = new FinanceDate(startDate).getMonth();
		IncomeExpensePortletInfo incomeExpensePortletInfo = new IncomeExpensePortletInfo();

		Query query = session.getNamedQuery("getIncomeExpensePortletInfo")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
		Object[] object = (Object[]) query.uniqueResult();
		if (object[0] != null) {
			incomeExpensePortletInfo.setIncome((Double) object[0]);
		} else {
			incomeExpensePortletInfo.setIncome(0.0);
		}
		if (object[1] != null) {
			incomeExpensePortletInfo.setExpense((Double) object[1]);
		} else {
			incomeExpensePortletInfo.setExpense(0.0);
		}
		incomeExpensePortletInfo.setMonth(month);
		return incomeExpensePortletInfo;
	}

	public ArrayList<PayeesBySalesPortletData> getCustomersBySales(
			Long companyId, FinanceDate startDate, FinanceDate endDate,
			int limit) {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<PayeesBySalesPortletData> payeesBySales = new ArrayList<PayeesBySalesPortletData>();

		Query query = session.getNamedQuery("getCustomersBySales")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("limit", limit);
		List list = query.list();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			PayeesBySalesPortletData payeesBySalesPortletData = new PayeesBySalesPortletData();
			Object[] next = (Object[]) iterator.next();
			payeesBySalesPortletData.setName((String) next[0]);
			payeesBySalesPortletData.setNoOfTrans((Integer) next[1]);
			payeesBySalesPortletData.setAmount((Double) next[2]);
			payeesBySalesPortletData.setCurrency((Long) next[3]);
			payeesBySales.add(payeesBySalesPortletData);
		}
		return payeesBySales;
	}

	public ArrayList<PayeesBySalesPortletData> getVendorsBySales(
			Long companyId, FinanceDate startDate, FinanceDate endDate,
			int limit) {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<PayeesBySalesPortletData> payeesBySales = new ArrayList<PayeesBySalesPortletData>();

		Query query = session.getNamedQuery("getVendorsBySales")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("limit", limit);
		List list = query.list();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			PayeesBySalesPortletData payeesBySalesPortletData = new PayeesBySalesPortletData();
			Object[] next = (Object[]) iterator.next();
			payeesBySalesPortletData.setName((String) next[0]);
			payeesBySalesPortletData.setNoOfTrans((Integer) next[1]);
			payeesBySalesPortletData.setAmount((Double) next[2]);
			payeesBySalesPortletData.setCurrency((Long) next[3]);
			payeesBySales.add(payeesBySalesPortletData);
		}
		return payeesBySales;
	}

	public ExpensePortletData getIncomeAccountsBalances(Long companyId,
			long startDate, long endDate) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			Query query = session.getNamedQuery("getIncomeAccounts")
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate);
			List<Object[]> objects = query.list();

			ExpensePortletData expensePortletData = new ExpensePortletData();
			Iterator<Object[]> iterator = objects.iterator();
			Double expenseAccountTotal = 0.0;
			List<AccountDetail> accountDetails = new ArrayList<AccountDetail>();
			while (iterator.hasNext()) {
				Object[] object = iterator.next();
				String accountName = (String) object[0];
				Double accountBalance = (Double) object[1];
				expenseAccountTotal = expenseAccountTotal + accountBalance;
				if (accountBalance > 0) {
					AccountDetail accountDetail = new AccountDetail(
							accountName, accountBalance);
					accountDetails.add(accountDetail);
				}

			}
			expensePortletData.setAccountDetails(accountDetails);
			expensePortletData
					.setAllExpensesTotal(expenseAccountTotal == null ? 0
							: expenseAccountTotal);

			return expensePortletData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<PayeesBySalesPortletData> getItemsBySalesQuantity(
			Long companyId, FinanceDate startDate, FinanceDate endDate,
			int limit) {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<PayeesBySalesPortletData> payeesBySales = new ArrayList<PayeesBySalesPortletData>();

		Query query = session.getNamedQuery("getItemsBySalesQuantity")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("limit", limit);
		List list = query.list();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			PayeesBySalesPortletData payeesBySalesPortletData = new PayeesBySalesPortletData();
			Object[] next = (Object[]) iterator.next();
			payeesBySalesPortletData.setName((String) next[0]);
			payeesBySalesPortletData.setQuantity((Double) next[1]);
			payeesBySales.add(payeesBySalesPortletData);
		}
		return payeesBySales;
	}

	public ArrayList<PayeesBySalesPortletData> getItemsByPurchaseQuantity(
			Long companyId, FinanceDate startDate, FinanceDate endDate,
			int limit) {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<PayeesBySalesPortletData> payeesBySales = new ArrayList<PayeesBySalesPortletData>();

		Query query = session.getNamedQuery("getItemsByPurchaseQuantity")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("limit", limit);
		List list = query.list();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			PayeesBySalesPortletData payeesBySalesPortletData = new PayeesBySalesPortletData();
			Object[] next = (Object[]) iterator.next();
			payeesBySalesPortletData.setName((String) next[0]);
			payeesBySalesPortletData.setQuantity((Double) next[1]);
			payeesBySales.add(payeesBySalesPortletData);
		}
		return payeesBySales;
	}

	public ArrayList<YearOverYearPortletData> getAccountsBalancesByDate(
			Long companyId, FinanceDate startDate, FinanceDate endDate,
			long accountId, int type) {
		ArrayList<YearOverYearPortletData> result = new ArrayList<YearOverYearPortletData>();

		if (type == 0 || type == 1) {
			YearOverYearPortletData yearOverYearData = getAccountsBalancesByDate(
					companyId, accountId, startDate, endDate);
			result.add(yearOverYearData);
		} else if (type == 4 || type == 5) {
			List<YearOverYearPortletData> incomeExpensePortletInfos = getAccountsBalancesByMonths(
					companyId, accountId, startDate, endDate, 3);
			result.addAll(incomeExpensePortletInfos);
		} else if (type == 2 || type == 3) {
			List<YearOverYearPortletData> incomeExpensePortletInfos = getAccountsBalancesByMonths(
					companyId, accountId, startDate, endDate, 12);
			result.addAll(incomeExpensePortletInfos);
		}

		return result;
	}

	private List<YearOverYearPortletData> getAccountsBalancesByMonths(
			Long companyId, Long accountId, FinanceDate startDate,
			FinanceDate endDate, int j) {

		Session session = HibernateUtil.getCurrentSession();
		FinanceDate currentDate = startDate;

		List<YearOverYearPortletData> yearOverYearDatas = new ArrayList<YearOverYearPortletData>();
		for (int i = 0; i < j; i++) {
			Calendar startDateCal = Calendar.getInstance();
			startDateCal.setTime(currentDate.getAsDateObject());
			startDateCal.set(Calendar.MONTH, startDateCal.get(Calendar.MONTH)
					+ i);
			startDateCal.set(Calendar.DATE, 1);

			Calendar endDateCal = Calendar.getInstance();
			endDateCal.setTime(currentDate.getAsDateObject());
			endDateCal.set(Calendar.MONTH, endDateCal.get(Calendar.MONTH) + i);
			endDateCal.set(Calendar.DATE,
					endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			Query query = session
					.getNamedQuery("getAccountBalances")
					.setParameter("companyId", companyId)
					.setParameter("accountId", accountId)
					.setParameter("startDate",
							new FinanceDate(startDateCal.getTime()).getDate())
					.setParameter("endDate",
							new FinanceDate(endDateCal.getTime()).getDate());
			Object[] next = (Object[]) query.uniqueResult();

			YearOverYearPortletData yearOverYearData = new YearOverYearPortletData();
			if (next != null) {
				yearOverYearData.setName((String) next[0]);
				yearOverYearData.setAmount(next[1] != null ? (Double) next[1]
						: 0.0);
				Currency primaryCurrency = getCompany(companyId)
						.getPrimaryCurrency();
				yearOverYearData
						.setCurrency(primaryCurrency != null ? primaryCurrency
								.getID() : 0);
			} else {
				yearOverYearData.setAmount(0.0);
			}
			yearOverYearData.setMonth(startDateCal.get(Calendar.MONTH));

			yearOverYearDatas.add(yearOverYearData);
		}
		return yearOverYearDatas;

	}

	public YearOverYearPortletData getAccountsBalancesByDate(Long companyId,
			Long accountId, FinanceDate startDate, FinanceDate endDate) {
		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getAccountBalances")
				.setParameter("companyId", companyId)
				.setParameter("accountId", accountId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		Object[] next = (Object[]) query.uniqueResult();
		YearOverYearPortletData yearOverYearData = new YearOverYearPortletData();
		if (next != null) {
			yearOverYearData.setName(next[0] != null ? (String) next[0] : "");
			yearOverYearData
					.setAmount(next[1] != null ? (Double) next[1] : 0.0);
			Currency primaryCurrency = getCompany(companyId)
					.getPrimaryCurrency();
			yearOverYearData
					.setCurrency(primaryCurrency != null ? primaryCurrency
							.getID() : 0);
			yearOverYearData.setMonth(startDate.getMonth());
		}
		return yearOverYearData;
	}
}
