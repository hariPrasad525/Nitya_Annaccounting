package com.vimukti.accounter.web.server.managers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Box;
import com.vimukti.accounter.core.Budget;
import com.vimukti.accounter.core.BudgetItem;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXAdjustment;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXRateCalculation;
import com.vimukti.accounter.core.TAXReturn;
import com.vimukti.accounter.core.TAXReturnEntry;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBox;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTAXReturnEntry;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.BankCheckDetail;
import com.vimukti.accounter.web.client.core.reports.BankDepositDetail;
import com.vimukti.accounter.web.client.core.reports.BudgetActuals;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.EstimatesByJob;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.IncomeByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.InventoryDetails;
import com.vimukti.accounter.web.client.core.reports.ItemActualCostDetail;
import com.vimukti.accounter.web.client.core.reports.JobActualCostDetail;
import com.vimukti.accounter.web.client.core.reports.JobProfitability;
import com.vimukti.accounter.web.client.core.reports.JobProfitabilityDetailByJob;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByClass;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.vimukti.accounter.web.client.core.reports.RealisedExchangeLossOrGain;
import com.vimukti.accounter.web.client.core.reports.ReconcilationItemList;
import com.vimukti.accounter.web.client.core.reports.Reconciliation;
import com.vimukti.accounter.web.client.core.reports.ReconciliationDiscrepancy;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TDSAcknowledgmentsReport;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.UnRealisedLossOrGain;
import com.vimukti.accounter.web.client.core.reports.UnbilledCostsByJob;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.core.reports.VATDetailReport;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;
import com.vimukti.accounter.web.client.ui.reports.TAXItemDetail;

public class ReportManager extends Manager {
	public static final int TYPE_AGEING_FROM_DUEDATE = 2;
	public static final int TYPE_AGEING_FROM_TRANSACTIONDATE = 1;

	public ArrayList<TrialBalance> getCashFlowReport(FinanceDate startDate,
			FinanceDate endDate, long companyId) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		ClientFinanceDate date[] = this
				.getMinimumAndMaximumTransactionDate(companyId);
		long start = date[0] != null ? date[0].getDate() : startDate.getDate();
		// Calendar cal = Calendar.getInstance();
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// cal.setTime(startDate.getAsDateObject());

		// cal.add(Calendar.DAY_OF_MONTH, -1);
		//
		// String end = cal.get(Calendar.YEAR) + "-";
		// end += ((((cal.get(Calendar.MONTH) + 1) + "").length() == 1) ? "0"
		// + cal.get(Calendar.MONTH) : cal.get(Calendar.MONTH) + 1)
		// + "-";
		// end += (((cal.get(Calendar.DAY_OF_MONTH)) + "").length() == 1) ? "0"
		// + cal.get(Calendar.DAY_OF_MONTH) : cal
		// .get(Calendar.DAY_OF_MONTH);

		long end = date[1] != null ? date[0].getDate() : endDate.getDate();

		List l = session.getNamedQuery("getCashFlowStatement")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("start", start).setParameter("end", end).list();

		double netIncome = 0.0;
		netIncome = getNetIncome(startDate.getDate(), endDate.getDate(),
				"getNetIncome", companyId);

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();
		TrialBalance netIncomeTB = new TrialBalance();
		netIncomeTB.setAccountName("Net Income");
		netIncomeTB.setAmount(netIncome);
		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {

			t.setAccountId(((Long) object[0]).longValue());
			t.setAccountName((String) object[1]);
			t.setAccountNumber((String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());
			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((Long) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}
			if (t.getAccountType() == Account.TYPE_ACCOUNT_RECEIVABLE
					|| t.getAccountType() == Account.TYPE_OTHER_CURRENT_ASSET
					|| t.getAccountType() == Account.TYPE_FIXED_ASSET
					|| t.getAccountType() == Account.TYPE_OTHER_ASSET) {

				t.setAmount(object[6] == null ? 0 : -1
						* ((Double) object[6]).doubleValue());
			} else {
				t.setAmount(object[6] == null ? 0 : ((Double) object[6])
						.doubleValue());
			}

			t.setCashAtBeginningOfPeriod((object[7] == null ? 0.0
					: ((Double) object[7]).doubleValue()));
			t.setAccountFlow((String) object[8]);
			t.setBaseType(object[9] == null ? 0 : (Integer) object[9]);
			t.setSubBaseType(object[10] == null ? 0 : (Integer) object[10]);
			t.setGroupType(object[11] == null ? 0 : (Integer) object[11]);

			queryResult.add(t);
			// }

		}
		List<TrialBalance> sortedList = sortTheList(queryResult);
		sortedList.add(0, netIncomeTB);
		return new ArrayList<TrialBalance>(sortedList);

	}

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTransactionDetailByTaxItem")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		if (l.size() > 0) {
			return createTransactionDetailByTaxItemEntries(new ArrayList<TransactionDetailByTaxItem>(
					l));
		} else
			return null;

	}

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final FinanceDate startDate,
			final FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery(
						"getTransactionDetailByTaxItemForParticularTaxItem")
				.setParameter("companyId", companyId)
				.setParameter("taxItemName", taxItemName,
						EncryptedStringType.INSTANCE)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		if (l.size() > 0) {
			return createTransactionDetailByTaxItemEntries(new ArrayList<TransactionDetailByTaxItem>(
					l));
		} else
			return null;

	}

	private ArrayList<TransactionDetailByTaxItem> createTransactionDetailByTaxItemEntries(
			List l) {
		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TransactionDetailByTaxItem> queryResult = new ArrayList<TransactionDetailByTaxItem>();
		while ((iterator).hasNext()) {

			TransactionDetailByTaxItem TransactionDetailByTaxItem = new TransactionDetailByTaxItem();
			object = (Object[]) iterator.next();
			TransactionDetailByTaxItem.setTransactionId((Long) object[0]);
			// transactionDetailByTaxcode
			// .setType(object[1]== null ? 0 :((Integer) object[1]).intValue());
			TransactionDetailByTaxItem.setPayeeName((String) object[1]);
			TransactionDetailByTaxItem.setTaxAgencyName((String) object[2]);
			TransactionDetailByTaxItem.setTaxItemName((String) object[3]);
			TransactionDetailByTaxItem.setRate(object[4] == null ? 0
					: ((Double) object[4]).doubleValue());
			TransactionDetailByTaxItem.setTransactionType(object[5] == null ? 0
					: ((Integer) object[5]).intValue());
			TransactionDetailByTaxItem.setDate(new ClientFinanceDate(
					(Long) object[6]));
			TransactionDetailByTaxItem.setNumber((String) object[7]);

			/*
			 * if (object[8] == null || object[8].equals("")) {
			 * 
			 * transactionDetailByTaxcode.setMemo("");
			 * 
			 * } else { Clob cl = (Clob) object[8]; StringBuffer strOut = new
			 * StringBuffer(); String aux; try { BufferedReader br = new
			 * BufferedReader(cl .getCharacterStream()); while ((aux =
			 * br.readLine()) != null) strOut.append(aux);
			 * transactionDetailByTaxcode.setMemo(strOut.toString()); } catch
			 * (java.sql.SQLException e1) {
			 * 
			 * } catch (java.io.IOException e2) {
			 * 
			 * } }
			 */
			TransactionDetailByTaxItem.setMemo((String) object[8]);
			TransactionDetailByTaxItem.setTaxableAmount(((Double) object[9])
					.doubleValue());
			TransactionDetailByTaxItem.setSalesTaxAmount(((Double) object[10])
					.doubleValue());
			TransactionDetailByTaxItem.setIsVoid(((Boolean) object[11])
					.booleanValue());

			queryResult.add(TransactionDetailByTaxItem);
		}
		return new ArrayList<TransactionDetailByTaxItem>(queryResult);
	}

	/**
	 * 
	 * @param accountID
	 * @param startDate
	 * @param endDate
	 * @param companyID
	 * @return
	 */
	public ArrayList<ReconcilationItemList> getReconciliationItemslistByDates(
			long accountID, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyID) {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyID);
		List list = (session
				.getNamedQuery("get.reconciliations.by.accountId_by_dates")
				.setParameter("account_Id", accountID)
				.setParameter("company_Id", company.getId())
				.setParameter("startDate", startDate.getDate()).setParameter(
				"endDate", endDate.getDate())).list();
		Object[] object = null;
		Iterator iterator = list.iterator();
		ArrayList<ReconcilationItemList> queryResult = new ArrayList<ReconcilationItemList>();
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			ReconcilationItemList itemList = new ReconcilationItemList();
			itemList.setId((Long) (object[0]));
			itemList.setBankAccountName(object[1] != null ? (String) object[1]
					: null);
			itemList.setTransationType(object[2] == null ? 0
					: ((Integer) object[2]).intValue());
			itemList.setTransactionNo(object[3] != null ? (String) object[3]
					: null);
			itemList.setTransactionDate(object[4] == null ? null
					: new ClientFinanceDate((Long) object[4]));
			itemList.setTransaction((Long) (object[5]));
			itemList.setAmount(object[6] == null ? 0 : (Double) object[6]);
			queryResult.add(itemList);
		}
		return queryResult;
	}

	public ArrayList<ProfitAndLossByLocation> getProfitAndLossByLocation(
			int categoryType, FinanceDate startDate, FinanceDate endDate,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate1 = getCurrentFiscalYearStartDate(company);

		/*
		 * Here endDate1 is used to store the previous month of endDate value
		 */
		int year = endDate.getYear();
		int month = endDate.getMonth() - 1;
		year = (month == 0) ? year - 1 : year;
		month = (month == 0) ? 12 : month;
		FinanceDate endDate1 = new FinanceDate(year, month, 31);
		Map<Long, Long> inneequeryMap = new HashMap<Long, Long>();
		if (year != startDate1.getYear())
			startDate1 = new FinanceDate(year, 01, 01);
		List l;
		List inerlist = null;

		if (categoryType == 2) {
			l = session.getNamedQuery("getProfitAndLossByLocation")
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate()).list();
		} else if (categoryType == 1) {
			return getProfitAndLossByClass(companyId, startDate, endDate);
		} else {
			l = session.getNamedQuery("getProfitAndLossByJob")
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate()).list();
		}
		Object[] object = null;
		Iterator iterator = l.iterator();
		List<ProfitAndLossByLocation> queryResult = new ArrayList<ProfitAndLossByLocation>();
		long previousAccountID = 0;
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			long accountId = ((Long) object[0]).longValue();
			if (previousAccountID == 0 || previousAccountID != accountId) {
				previousAccountID = accountId;
				ProfitAndLossByLocation record = new ProfitAndLossByLocation();
				record.setAccountId(accountId == 0 ? 0 : accountId);
				record.setAccountName(object[1] == null ? null
						: (String) object[1]);
				record.setAccountNumber(object[2] == null ? null
						: (String) object[2]);
				record.setAccountType(object[3] == null ? 0
						: ((Integer) object[3]).intValue());
				long location;
				location = object[4] == null ? 0 : ((Long) object[4])
						.longValue();

				double amount = object[5] == null ? 0 : (Double) object[5];

				record.getMap().put(location, amount);

				queryResult.add(record);
			} else {
				ProfitAndLossByLocation record = queryResult.get(queryResult
						.size() - 1);
				long location = object[4] == null ? 0 : ((Long) object[4])
						.longValue();
				double amount = object[5] == null ? 0 : (Double) object[5];
				/* + record.getMap().get(location) */;
				record.getMap().get(location);
				record.getMap().put(location, amount);
				record.setCategoryId(location);
			}
		}

		return new ArrayList<ProfitAndLossByLocation>(queryResult);
	}

	/**
	 * 
	 * @param companyId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ArrayList<ProfitAndLossByLocation> getProfitAndLossByClass(
			long companyId, FinanceDate startDate, FinanceDate endDate) {
		Session session = HibernateUtil.getCurrentSession();
		List l = session.getNamedQuery("getProfitAndLossByClass")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate()).list();

		Company company = getCompany(companyId);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		int index = 1;
		ArrayList<AccounterClass> accounterClasses = new ArrayList<AccounterClass>(
				company.getAccounterClasses());
		Collections.sort(accounterClasses, new Comparator<AccounterClass>() {
			@Override
			public int compare(AccounterClass o1, AccounterClass o2) {
				int res = o1.getPath().compareTo(o2.getPath());
				return true ? (-1 * res) : (res);
			}
		});

		long previousparentID = 0;
		for (AccounterClass clientAccounterClass : accounterClasses) {
			String className = clientAccounterClass.getclassName();
			if (previousparentID != 0
					&& clientAccounterClass.getParent() != null
					&& previousparentID != clientAccounterClass.getParent()
							.getID()) {
				map.put(clientAccounterClass.getID(), index);
				index++;
				map1.put(className, index);
			} else {
				map.put(clientAccounterClass.getID(), index);
				if (clientAccounterClass.getParent() == null) {
					index++;
					map1.put(className, index);
				}
			}
			previousparentID = clientAccounterClass.getParent() == null ? 0
					: clientAccounterClass.getParent().getID();
			index++;
		}

		long previousAccountID = 0;
		double totalAmount = 0.0;
		long previousRecordParent = 0;
		Object[] object = null;
		Iterator iterator = l.iterator();
		List<ProfitAndLossByLocation> queryResult = new ArrayList<ProfitAndLossByLocation>();
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			long accountId = ((Long) object[0]).longValue();
			if (previousAccountID == 0 || previousAccountID != accountId) {
				totalAmount = 0.0;
				previousAccountID = accountId;
				ProfitAndLossByLocation record = new ProfitAndLossByLocation();

				long presentClassID = object[4] == null ? 0
						: ((Long) object[4]).longValue();
				long presentRecordParent = object[7] == null ? 0
						: ((Long) object[7]).longValue();
				double amount = object[5] == null ? 0 : (Double) object[5];
				if (previousRecordParent == 0) {
					previousRecordParent = presentRecordParent;
					totalAmount += object[5] == null ? 0 : (Double) object[5];
				} else {
					if (previousRecordParent == presentRecordParent) {
						totalAmount += object[5] == null ? 0
								: (Double) object[5];
					} else if (previousRecordParent == presentClassID) {
						totalAmount += object[5] == null ? 0
								: (Double) object[5];
					} else {
						totalAmount += object[5] == null ? 0
								: (Double) object[5];
						ProfitAndLossByClass profitAndLossByClass = new ProfitAndLossByClass();
						profitAndLossByClass.setTotal(totalAmount);
						profitAndLossByClass.setClassId(previousRecordParent);
						AccounterClass previousAccounterClass = (AccounterClass) getServerObjectForid(
								AccounterCoreType.ACCOUNTER_CLASS,
								previousRecordParent);
						profitAndLossByClass.setTotal(true);
						if (map1.containsKey(previousAccounterClass
								.getclassName())) {
							profitAndLossByClass
									.setIndex(map1.get(previousAccounterClass
											.getclassName()));
						} else {
							profitAndLossByClass.setIndex(map
									.get(previousRecordParent));
						}
						record.getRecords().add(profitAndLossByClass);
						previousRecordParent = presentRecordParent;
					}
				}
				record.setAccountId(accountId == 0 ? 0 : accountId);
				record.setAccountName(object[1] == null ? null
						: (String) object[1]);
				record.setAccountNumber(object[2] == null ? null
						: (String) object[2]);
				record.setAccountType(object[3] == null ? 0
						: ((Integer) object[3]).intValue());

				ProfitAndLossByClass profitAndLossByClass = new ProfitAndLossByClass();
				profitAndLossByClass.setTotal(amount);
				profitAndLossByClass.setClassId(presentClassID);
				profitAndLossByClass.setTotal(false);
				profitAndLossByClass.setIndex(map.get(presentClassID));
				record.getRecords().add(profitAndLossByClass);
				AccounterClass previousAccounterClass = (AccounterClass) getServerObjectForid(
						AccounterCoreType.ACCOUNTER_CLASS, presentClassID);
				if (map1.containsKey(previousAccounterClass.getclassName())) {
					ProfitAndLossByClass totalprofitAndLossByClass = new ProfitAndLossByClass();
					totalprofitAndLossByClass.setTotal(totalAmount);
					totalprofitAndLossByClass.setClassId(previousRecordParent);
					totalprofitAndLossByClass.setTotal(true);
					totalprofitAndLossByClass.setIndex(map1
							.get(previousAccounterClass.getclassName()));
					record.getRecords().add(totalprofitAndLossByClass);
				}

				queryResult.add(record);
			} else {
				ProfitAndLossByLocation record = queryResult.get(queryResult
						.size() - 1);
				long presentClassID = object[4] == null ? 0
						: ((Long) object[4]).longValue();
				long presentRecordParent = object[7] == null ? 0
						: ((Long) object[7]).longValue();
				if (previousRecordParent == 0) {
					previousRecordParent = presentRecordParent;
					totalAmount += object[5] == null ? 0 : (Double) object[5];
				} else {
					if (previousRecordParent == presentRecordParent) {
						totalAmount += object[5] == null ? 0
								: (Double) object[5];
					} else if (previousRecordParent == presentClassID) {
						totalAmount += object[5] == null ? 0
								: (Double) object[5];
					} else {
						totalAmount += object[5] == null ? 0
								: (Double) object[5];

						ProfitAndLossByClass profitAndLossByClass = new ProfitAndLossByClass();
						profitAndLossByClass.setTotal(totalAmount);
						profitAndLossByClass.setClassId(previousRecordParent);
						AccounterClass previousAccounterClass = (AccounterClass) getServerObjectForid(
								AccounterCoreType.ACCOUNTER_CLASS,
								previousRecordParent);
						profitAndLossByClass.setTotal(true);
						if (map1.containsKey(previousAccounterClass
								.getclassName())) {
							profitAndLossByClass
									.setIndex(map1.get(previousAccounterClass
											.getclassName()));
						} else {
							profitAndLossByClass.setIndex(map
									.get(previousRecordParent));
						}
						record.getRecords().add(profitAndLossByClass);
						previousRecordParent = presentRecordParent;
					}
				}
				double amount = object[5] == null ? 0 : (Double) object[5];
				List<ProfitAndLossByClass> records = getrecords(record,
						presentClassID);
				if (!records.isEmpty()) {
					for (ProfitAndLossByClass profitAndLossByClass : records) {
						double total = profitAndLossByClass.getTotal();
						profitAndLossByClass.setTotal(amount + total);
					}
				} else {
					ProfitAndLossByClass profitAndLossByClass1 = new ProfitAndLossByClass();
					profitAndLossByClass1.setTotal(amount);
					profitAndLossByClass1.setClassId(presentClassID);
					profitAndLossByClass1.setTotal(false);
					profitAndLossByClass1.setIndex(map.get(presentClassID));
					record.getRecords().add(profitAndLossByClass1);
					AccounterClass previousAccounterClass = (AccounterClass) getServerObjectForid(
							AccounterCoreType.ACCOUNTER_CLASS, presentClassID);
					if (map1.containsKey(previousAccounterClass.getclassName())) {
						ProfitAndLossByClass profitAndLossByClass = new ProfitAndLossByClass();
						profitAndLossByClass.setTotal(totalAmount);
						profitAndLossByClass.setClassId(previousRecordParent);
						profitAndLossByClass.setTotal(true);
						profitAndLossByClass.setIndex(map1
								.get(previousAccounterClass.getclassName()));
						record.getRecords().add(profitAndLossByClass);
					}
				}
				record.setCategoryId(presentClassID);
			}
			// totalAmount = 0.0;
		}

		return new ArrayList<ProfitAndLossByLocation>(queryResult);
	}

	private List<ProfitAndLossByClass> getrecords(
			ProfitAndLossByLocation record, long presentClassID) {
		List<ProfitAndLossByClass> lossByClasses = new ArrayList<ProfitAndLossByClass>();
		for (ProfitAndLossByClass profitAndLossByClass : record.getRecords())
			if (profitAndLossByClass.getClassId() == presentClassID) {
				lossByClasses.add(profitAndLossByClass);
			}
		return lossByClasses;
	}

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			long accountId, final FinanceDate startDate,
			final FinanceDate endDate, long companyId) throws DAOException {

		try {

			Session session = HibernateUtil.getCurrentSession();

			List<TransactionDetailByAccount> transactionDetailByAccountList = new ArrayList<TransactionDetailByAccount>();
			Query query = session
					.getNamedQuery(
							"getTransactionDetailByAccount_ForParticularAccount")
					.setParameter("companyId", companyId)
					.setParameter("accountId", accountId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate());

			List list = query.list();

			if (list != null && list.size() > 0) {
				createTransasctionDetailByAccount(list,
						transactionDetailByAccountList);
			}

			if (transactionDetailByAccountList != null) {
				return new ArrayList<TransactionDetailByAccount>(
						transactionDetailByAccountList);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<TransactionDetailByAccount> getAutomaticTransactions(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {

		try {

			Session session = HibernateUtil.getCurrentSession();

			List<TransactionDetailByAccount> automaticTransactions = new ArrayList<TransactionDetailByAccount>();

			Query query = session.getNamedQuery("getAutomaticTransactions")
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate());

			List list = query.list();

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();

				TransactionDetailByAccount record = new TransactionDetailByAccount();
				record.setName((object[0] == null ? null : ((String) object[0])));
				record.setTransactionId((Long) object[1]);
				record.setTransactionType(object[2] == null ? 0
						: (((Integer) object[2]).intValue()));
				record.setTransactionDate(object[3] == null ? null
						: new ClientFinanceDate((Long) object[3]));
				record.setTransactionNumber((object[4] == null ? null
						: ((String) object[4])));
				record.setTotal(object[5] != null ? (Double) object[5] : 0.0);
				record.setMemo(object[6] == null ? null : ((String) object[6]));

				automaticTransactions.add(record);
			}

			return new ArrayList<TransactionDetailByAccount>(
					automaticTransactions);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	private void createTransasctionDetailByAccount(List list,
			List<TransactionDetailByAccount> transactionMakeDepositsList)
			throws DAOException {
		Object object[] = null;

		Iterator i = list.iterator();
		while (i.hasNext()) {

			object = (Object[]) i.next();
			TransactionDetailByAccount transactionDetailByAccount = new TransactionDetailByAccount();

			transactionDetailByAccount.setAccountName((object[0] == null ? null
					: ((String) object[0])));
			transactionDetailByAccount.setTransactionType(object[3] == null ? 0
					: (((Integer) object[3]).intValue()));
			transactionDetailByAccount
					.setTotal(object[6] != null ? (Double) object[6] : 0.0);

			if (transactionDetailByAccount.getAccountName().equals(
					AccounterServerConstants.TAX_VAT_UNFILED)
					&& transactionDetailByAccount.getTransactionType() == Transaction.TYPE_TAX_RETURN) {
				if (transactionDetailByAccount.getTotal() < 0)
					transactionDetailByAccount.setName("Box3");
				if (transactionDetailByAccount.getTotal() > 0)
					transactionDetailByAccount.setName("Box4");

			} else {
				if (transactionDetailByAccount.getTransactionType() == 28) {
					if (object.length > 7) {
						transactionDetailByAccount
								.setName((object[8] == null ? null
										: ((String) object[8])));
					}
				} else {
					transactionDetailByAccount
							.setName((object[1] == null ? null
									: ((String) object[1])));
				}
			}
			transactionDetailByAccount.setTransactionId((Long) object[2]);

			transactionDetailByAccount
					.setTransactionDate(object[4] == null ? null
							: new ClientFinanceDate((Long) object[4]));
			transactionDetailByAccount.setTransactionNumber((String) object[5]);

			transactionDetailByAccount
					.setTotal(object[6] != null ? (Double) object[6] : 0.0);
			transactionDetailByAccount.setMemo(object[7] == null ? ""
					: (String) object[7]);
			transactionMakeDepositsList.add(transactionDetailByAccount);

		}
	}

	private List<TrialBalance> sortTheList(List<TrialBalance> queryResult) {
		List<TrialBalance> sortedList = new ArrayList<TrialBalance>();

		for (TrialBalance t : queryResult) {

			if (t.getParentAccount() == 0) {

				if (!sortedList.contains(t)) {
					String str = t.getAccountFlow();
					// if (t.getAmount() != 0.0) {
					sortedList.add(t);
					// }
					if (str != null) {
						getChilds(str, queryResult, sortedList);
					}
				}
			}

		}

		return removeUnwantedEntries(sortedList);
	}

	private void getChilds(String str, List<TrialBalance> queryResult,
			List<TrialBalance> sortedList) {

		for (TrialBalance t : queryResult) {

			if (t.getAccountFlow() != null
					&& t.getAccountFlow().startsWith(str + ".")) {

				if (!t.getAccountFlow().substring(str.length() + 1)
						.contains(".")) {

					if (!sortedList.contains(t)) {
						// if (t.getAmount() != 0.0) {
						sortedList.add(t);
						// }
						str = t.getAccountFlow();

						getChilds(str, queryResult, sortedList);
					}
				}
			}
		}
		if (str.contains(".")) {

			getChilds(str.substring(0, str.lastIndexOf(".")), queryResult,
					sortedList);
		} else {
			return;
		}

	}

	private double getNetIncome(long startDate, long endDate, String query,
			long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		Query q = session.getNamedQuery(query)
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);

		List l1 = q.list();
		double netIncome = 0.0;
		if (l1 != null && l1.size() > 0) {
			if (l1.get(0) != null)
				netIncome = (Double) l1.get(0);
		}
		return netIncome;
	}

	private ArrayList<TrialBalance> removeUnwantedEntries(
			List<TrialBalance> sortTheList) {
		ArrayList<TrialBalance> list = new ArrayList<TrialBalance>();
		for (TrialBalance tb : sortTheList) {
			if (!DecimalUtil.isEquals(tb.getAmount(), 0.0)
					|| !DecimalUtil.isEquals(tb.getTotalAmount(), 0.0)
					|| hasChilds(tb, sortTheList)) {
				list.add(tb);
			}
		}
		return list;
	}

	private boolean hasChilds(TrialBalance tb, List<TrialBalance> sortTheList) {

		for (TrialBalance balance : sortTheList) {
			if (balance.getAccountFlow().startsWith(tb.getAccountFlow() + "."))
				return true;
		}

		return false;
	}

	public ArrayList<TrialBalance> getTrialBalance(final FinanceDate startDate,
			final FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTrialBalance")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();
		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {
			t.setAccountId(((Long) object[0]));
			t.setAccountName(object[1] == null ? null : (String) object[1]);
			t.setAccountNumber(object[2] == null ? null : (String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());

			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((Long) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}

			t.setAmount(object[6] == null ? 0d : ((Double) object[6])
					.doubleValue());
			if (DecimalUtil.isLessThan(t.getAmount(), 0.0)) {
				t.setCreditAmount(-1 * t.getAmount());

			} else {
				t.setDebitAmount(t.getAmount());
			}

			t.setAccountFlow((String) object[7]);
			queryResult.add(t);

			// }

		}
		return removeUnwantedEntries(new ArrayList<TrialBalance>(queryResult));

		// Object obj = template.execute(new HibernateCallback(){
		// Ac
		// public Object doInHibernate(Session session)
		// throws HibernateException, SQLException {
		//
		// return new TrialBalanceReport(session,startDate,endDate);
		// }
		// });
		//
		// return obj !=null?(TrialBalanceReport)obj:null;

	}

	public ArrayList<TrialBalance> getBalanceSheetReport(FinanceDate startDate,
			FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		List l = session.getNamedQuery("getBalanceSheet")
				.setParameter("companyId", companyId)
				.setParameter("endDate", endDate.getDate()).list();

		double netIncome = 0.0;
		netIncome = getNetIncome(0, endDate.getDate(),
				"getNetIncome_Closing_postings_Included", companyId);

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();

		TrialBalance netIncomeTB = new TrialBalance();
		netIncomeTB.setAccountName("Net Profit");
		netIncomeTB.setBaseType(Account.BASETYPE_EQUITY);
		netIncomeTB.setSubBaseType(Account.SUBBASETYPE_EQUITY);
		// netIncomeTB.setAccountNumber("3100");
		netIncomeTB.setAmount(netIncome);

		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {

			t.setAccountId(((Long) object[0]).longValue());
			t.setAccountName((String) object[1]);
			t.setAccountNumber((String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());
			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((Long) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}
			t.setAmount(object[6] == null ? 0 : ((Double) object[6])
					.doubleValue());
			t.setAccountFlow((String) object[7]);
			t.setBaseType(object[8] == null ? 0 : (Integer) object[8]);
			t.setSubBaseType(object[9] == null ? 0 : (Integer) object[9]);
			t.setGroupType(object[10] == null ? 0 : (Integer) object[10]);

			if (t.getAmount() != 0) {
				queryResult.add(t);
			}

			// }

		}
		// List<TrialBalance> sortedList = sortTheList(queryResult);
		// sortedList.add(netIncomeTB);

		// List<TrialBalance> sortedList = getBalanceSheetSorted(queryResult);
		// sortedList.add(netIncomeTB);
		queryResult.add(netIncomeTB);

		return new ArrayList<TrialBalance>(queryResult);

	}

	public ArrayList<TrialBalance> getProfitAndLossReport(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate1 = getCurrentFiscalYearStartDate(company);

		/*
		 * Here endDate1 is used to store the previous month of endDate value
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate.getAsDateObject());
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		FinanceDate endDate1 = new FinanceDate(cal.getTime());

		// if (year != startDate1.getYear())
		// startDate1 = new FinanceDate(year, 01, 01);
		// + ((month + "").length() == 1 ? "0" + month : month) + "01");

		if (startDate1.getYear() == endDate1.getYear()
				&& startDate1.getMonth() > endDate1.getMonth()) {
			startDate1.setYear(startDate1.getYear() - 1);
		}

		List l = session.getNamedQuery("getProfitAndLoss")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("startDate1", startDate1.getDate())
				.setParameter("endDate1", endDate1.getDate()).list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();
		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {

			t.setAccountId((Long) object[0]);
			t.setAccountName((String) object[1]);
			t.setAccountNumber((String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());
			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((Long) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}
			t.setAmount(object[6] == null ? 0 : ((Double) object[6])
					.doubleValue());
			t.setTotalAmount(object[7] == null ? 0 : ((Double) object[7])
					.doubleValue());
			t.setAccountFlow((String) object[8]);
			t.setBaseType(object[9] == null ? 0 : (Integer) object[9]);
			t.setSubBaseType(object[10] == null ? 0 : (Integer) object[10]);
			t.setGroupType(object[11] == null ? 0 : (Integer) object[11]);

			queryResult.add(t);
			// }

		}
		List<TrialBalance> sortedResult = new ArrayList<TrialBalance>();
		List<TrialBalance> otherExpenseList = new ArrayList<TrialBalance>();

		sortedResult = sortTheList(queryResult);
		int index = 0;
		Iterator iter = sortedResult.listIterator();
		while (iter.hasNext()) {
			TrialBalance tb = (TrialBalance) iter.next();
			if (tb.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				index = sortedResult.indexOf(tb);
			if (tb.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {
				otherExpenseList.add(tb);
				iter.remove();
			}
		}
		if (sortedResult.isEmpty()) {
			sortedResult = otherExpenseList;
		} else {
			if (otherExpenseList.size() != 0)
				sortedResult.addAll((index + 1), otherExpenseList);
		}
		return new ArrayList<TrialBalance>(sortedResult);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, FinanceDate startDate, FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		List l = session
				.getNamedQuery("getSalesByCustomerDetailForParticularCustomer")
				.setParameter("companyId", companyId)
				.setParameter("customerName", customerName,
						EncryptedStringType.INSTANCE).setParameter("startDate",

				startDate.getDate()).setParameter("endDate", endDate.getDate())
				.list();

		return createSalesByCustomerDetailReport(new ArrayList<SalesByCustomerDetail>(
				l));

	}

	private ArrayList<SalesByCustomerDetail> createSalesByCustomerDetailReport(
			List l) {

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<SalesByCustomerDetail> queryResult = new ArrayList<SalesByCustomerDetail>();
		while ((iterator).hasNext()) {

			SalesByCustomerDetail salesByCustomerDetail = new SalesByCustomerDetail();
			object = (Object[]) iterator.next();

			salesByCustomerDetail.setName((String) object[0]);
			salesByCustomerDetail.setType(object[1] == null ? 0
					: ((Integer) object[1]).intValue());
			salesByCustomerDetail.setDate(new ClientFinanceDate(
					((Long) object[2]).longValue()));

			salesByCustomerDetail.setNumber((String) object[3]);
			/*
			 * Clob cl = (Clob) object[4]; if (cl == null) {
			 * 
			 * salesByCustomerDetail.setMemo("");
			 * 
			 * } else {
			 * 
			 * StringBuffer strOut = new StringBuffer(); String aux; try {
			 * BufferedReader br = new BufferedReader(cl .getCharacterStream());
			 * while ((aux = br.readLine()) != null) strOut.append(aux);
			 * salesByCustomerDetail.setMemo(strOut.toString()); } catch
			 * (java.sql.SQLException e1) {
			 * 
			 * } catch (java.io.IOException e2) {
			 * 
			 * } }
			 */
			salesByCustomerDetail.setMemo((String) object[4]);
			salesByCustomerDetail.setDueDate(object[5] == null ? null
					: new ClientFinanceDate(((Long) object[5]).longValue()));
			salesByCustomerDetail.setPaymentTermName((String) object[6]);
			salesByCustomerDetail.setItemName((String) object[7]);
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(object[8] == null ? 0 : ((Double) object[8])
					.intValue());
			salesByCustomerDetail.setQuantity(quantity);
			salesByCustomerDetail.setUnitPrice(object[9] == null ? 0
					: ((Double) object[9]).doubleValue());
			salesByCustomerDetail.setAmount(object[10] == null ? 0
					: ((Double) object[10]).doubleValue());
			salesByCustomerDetail.setDeliveryDate(object[11] == null ? null
					: new ClientFinanceDate(((Long) object[11]).longValue()));
			// salesByCustomerDetail.setIsVoid(object[12] == null ? true
			// : ((Boolean) object[12]).booleanValue());
			salesByCustomerDetail.setReference((String) object[12]);
			salesByCustomerDetail.setTransactionId((object[13] == null ? 0
					: (((Long) object[13]).longValue())));
			queryResult.add(salesByCustomerDetail);
		}
		// return prepareSalesPurchaseEntriesForVoid(queryResult);
		return new ArrayList<SalesByCustomerDetail>(queryResult);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getSalesByCustomerDetail")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();

		return createSalesByCustomerDetailReport(new ArrayList<SalesByCustomerDetail>(
				l));

	}

	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		ClientFinanceDate date[] = this
				.getMinimumAndMaximumTransactionDate(companyId);
		long start = date[0] != null ? date[0].getDate() : startDate.getDate();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		cal.setTime(startDate.getAsDateObject());
		cal.add(Calendar.DAY_OF_MONTH, -1);

		long end = cal.get(Calendar.YEAR);
		end = (end * 100) + (cal.get(Calendar.MONTH) + 1);
		end = (end * 100) + cal.get(Calendar.DAY_OF_MONTH);

		Query query = session.getNamedQuery("getSalesTaxLiabilityReport")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("start", start).setParameter("end", end);

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<SalesTaxLiability> queryResult = new ArrayList<SalesTaxLiability>();
		while ((iterator).hasNext()) {

			SalesTaxLiability salesTaxLiability = new SalesTaxLiability();
			object = (Object[]) iterator.next();
			salesTaxLiability.setTaxAgencyName(object[0] == null ? null
					: (String) object[0]);
			salesTaxLiability.setTaxItemName(object[1] == null ? null
					: (String) object[1]);
			salesTaxLiability.setTaxRate(object[2] == null ? 0.0
					: ((Double) object[2]).doubleValue());
			salesTaxLiability.setTaxCollected(object[3] == null ? 0.0
					: ((Double) object[3]).doubleValue());
			salesTaxLiability.setTotalSales(object[4] == null ? 0.0
					: ((Double) object[4]).doubleValue());
			salesTaxLiability.setTaxable(object[5] == null ? 0.0
					: ((Double) object[5]).doubleValue());
			salesTaxLiability.setNonTaxable(object[6] == null ? 0.0
					: ((Double) object[6]).doubleValue());
			// salesTaxLiability.setNonTaxableOther(object[7] == null ?
			// null:(Double)object[7]);
			salesTaxLiability.setBeginningBalance(object[7] == null ? 0.0
					: ((Double) object[7]).doubleValue());
			queryResult.add(salesTaxLiability);
		}
		return new ArrayList<SalesTaxLiability>(queryResult);
	}

	private void prepareVATDetailReport(VATDetailReport vatDetailReport,
			TAXAgency taxAgency, FinanceDate startDate, FinanceDate endDate,
			Company company) {

		Session session = HibernateUtil.getCurrentSession();
		Query query = null;

		// /getting entries from VATRAteCalculation
		if (taxAgency != null) {

			query = session
					.getNamedQuery(
							"getTaxCalc.by.TaxAgencyId.and.withOtherDetails")
					.setParameter("taxAgency", taxAgency.getID())
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setEntity("company", company);
		} else {

			query = session
					.getNamedQuery(
							"getTaxrateCalc.by.TaxAgencyandItem.and.Dates")
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setEntity("company", company);
		}

		List<TAXRateCalculation> vats = query.list();

		List<VATDetail> vatDetails = new ArrayList<VATDetail>();

		for (TAXRateCalculation v : vats) {

			VATDetail vd = new VATDetail();

			vd.setBoxName(setVATBoxName(v));

			vd.setNetAmount(v.getLineTotal());

			if (v.getTransaction().getInvolvedPayee() != null)
				vd.setPayeeName(v.getTransaction().getInvolvedPayee().getName());

			/*
			 * Here all box values other than Vat code EGS (for purchase) of
			 * box2 and Vat code RC (for purchase) of box1 are getting it's
			 * native values
			 */

			if (vd.getBoxName().equals(
					VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
					|| (vd.getBoxName().equals(
							VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES) && !v
							.isVATGroupEntry())
					|| (vd.getBoxName().equals(
							VATSummary.UK_BOX1_VAT_DUE_ON_SALES) && v
							.getTaxItem().getVatReturnBox().getTotalBox()
							.equals(AccounterServerConstants.BOX_NONE))) {
				double amount = -1 * (v.getTAXAmount());
				// double amount = (!v.getTransactionItem().isVoid()) ? (-1 * (v
				// .getVatAmount())) : 0;
				vd.setTotal(amount);
			} else {
				double amount = v.getTAXAmount();
				// double amount = (!v.getTransactionItem().isVoid()) ? v
				// .getVatAmount() : 0;
				vd.setTotal(amount);
			}
			vd.setTransactionDate(new ClientFinanceDate(v.getTransactionDate()
					.getDate()));
			vd.setTransactionName(v.getTransaction().toString());
			vd.setTransactionNumber(v.getTransaction().getNumber());
			vd.setTransactionType(v.getTransaction().getType());
			vd.setVatRate(v.getTaxItem().getTaxRate());
			vd.setTransactionId(v.getTransaction().getID());
			vd.setPercentage(v.getTaxItem().isPercentage());
			vatDetailReport.getEntries().get(vd.getBoxName()).add(vd);

			// Adding Box2 entries to Box4
			// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
			// messages.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
			//
			// VATDetail vd2 = new VATDetail();
			// vd2
			// .setBoxName(setVATBoxName(messages.UK_BOX4_VAT_RECLAMED_ON_PURCHASES));
			//
			// vd2.setNetAmount(vd.getNetAmount());
			// vd2.setPayeeName(vd.getPayeeName());
			// vd2.setTotal(vd.getTotal());
			// vd2.setTransactionDate(vd.getTransactionDate());
			// vd2.setTransactionName(vd.getTransactionName());
			// vd2.setTransactionNumber(vd.getTransactionNumber());
			// vd2.setTransactionType(vd.getTransactionType());
			// vd2.setVatRate(vd.getVatRate());
			// vd2.setTransactionId(vd.getTransactionId());
			// vd2.setPercentage(v.getVatItem().isPercentage());
			//
			// vatDetailReport.getEntries().get(vd2.getBoxName()).add(vd2);
			// }

			// /////////////////////////////

			// Adding RC vat code transaction entry to Box4
			// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
			// messages.UK_BOX1_VAT_DUE_ON_SALES)
			// && (v.getVatItem().getVatReturnBox().getTotalBox()
			// .equals(messages.BOX_NONE))) {
			//
			// VATDetail vd3 = new VATDetail();
			// vd3
			// .setBoxName(setVATBoxName(messages.UK_BOX4_VAT_RECLAMED_ON_PURCHASES));
			//
			// vd3.setNetAmount(vd.getNetAmount());
			// vd3.setPayeeName(vd.getPayeeName());
			// vd3.setTotal(vd.getTotal());
			// vd3.setTransactionDate(vd.getTransactionDate());
			// vd3.setTransactionName(vd.getTransactionName());
			// vd3.setTransactionNumber(vd.getTransactionNumber());
			// vd3.setTransactionType(vd.getTransactionType());
			// vd3.setVatRate(vd.getVatRate());
			// vd3.setTransactionId(vd.getTransactionId());
			// vd3.setPercentage(v.getVatItem().isPercentage());
			//
			// vatDetailReport.getEntries().get(vd3.getBoxName()).add(vd3);
			// }
			// /////////////////////////////////////

			VATDetail vd1 = new VATDetail();

			vd1.setBoxName(setTotalBoxName(v));

			if ((vd1.getBoxName().equals(
					AccounterServerConstants.UK_BOX10_UNCATEGORISED) && v
					.getTransaction().getTransactionCategory() == Transaction.CATEGORY_CUSTOMER)
					|| !vd1.getBoxName().equals(
							AccounterServerConstants.UK_BOX10_UNCATEGORISED)) {

				vd1.setTotal(v.getLineTotal());
				if (v.getTransaction().getInvolvedPayee() != null)
					vd1.setPayeeName(v.getTransaction().getInvolvedPayee()
							.getName());
				vd1.setTransactionDate(new ClientFinanceDate(v
						.getTransactionDate().getDate()));
				vd1.setTransactionName(v.getTransaction().toString());
				vd1.setTransactionNumber(v.getTransaction().getNumber());
				vd1.setTransactionType(v.getTransaction().getType());
				// vd1.setVatRate(v.getTransactionItem().getLineTotal());
				vd1.setTransactionId(v.getTransaction().getID());

				vatDetailReport.getEntries().get(vd1.getBoxName()).add(vd1);

			}

		}

		{

			// getting entries from VATAdjustment
			if (taxAgency != null) {
				query = session
						.getNamedQuery(
								"getTaxadjustment.by.allDetails.withOrder")
						.setEntity("company", company)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("taxAgency", taxAgency.getID());
			} else {
				query = session
						.getNamedQuery("getTaxadjustment.by.betweenDates")
						.setEntity("company", company)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate);
			}

			// Adding adjustment entries to it's Respective boxes, except
			// Uncategorised Tax Amounts box
			List<TAXAdjustment> vas = query.list();

			// double box2Amount = 0.0;

			for (TAXAdjustment v : vas) {

				VATDetail vd = new VATDetail();

				vd.setBoxName(setVATBoxName(v.getTaxItem().getVatReturnBox()
						.getVatBox()));

				if (!vd.getBoxName().equals(
						AccounterServerConstants.UK_BOX10_UNCATEGORISED)) {

					if (v.getIncreaseVATLine()) {
						if (!v.isSales()) {
							vd.setTotal(v.getTotal());
						} else {
							vd.setTotal(-1 * v.getTotal());
						}
					} else {
						if (v.isSales()) {
							vd.setTotal(v.getTotal());
						} else {
							vd.setTotal(-1 * v.getTotal());
						}
					}

					Account liabilityAccount = v.isSales() == true ? v
							.getTaxItem().getTaxAgency()
							.getSalesLiabilityAccount() : v.getTaxItem()
							.getTaxAgency().getPurchaseLiabilityAccount();

					if (vd.getBoxName().equals("VAT Due on sales (Box 1)"))
						vd.setTotal(-1 * vd.getTotal());

					vd.setPayeeName(liabilityAccount.getName());

					vd.setTransactionDate(new ClientFinanceDate(v.getDate()
							.getDate()));
					vd.setTransactionName(v.toString());
					vd.setTransactionNumber(v.getNumber());
					vd.setTransactionType(v.getType());
					vd.setTransactionId(v.getID());

					vatDetailReport.getEntries().get(vd.getBoxName()).add(vd);
				}

				// Adding Box2 adjustment entries to Box4
				// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
				// messages.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
				// VATDetail vd2 = new VATDetail();
				// vd2
				// .setBoxName(setVATBoxName(messages.UK_BOX4_VAT_RECLAMED_ON_PURCHASES));
				//
				// vd2.setTotal(vd.getTotal());
				// vd2.setPayeeName(vd.getPayeeName());
				// vd2.setTransactionDate(vd.getTransactionDate());
				// vd2.setTransactionName(vd.getTransactionName());
				// vd2.setTransactionNumber(vd.getTransactionNumber());
				// vd2.setTransactionType(vd.getTransactionType());
				// vd2.setTransactionId(vd.getTransactionId());
				//
				// vatDetailReport.getEntries().get(vd2.getBoxName()).add(vd2);
				// }

				// Adding all adjustment entries to Uncategorised Tax Amounts
				// Box
				VATDetail vd1 = new VATDetail();

				vd1.setBoxName(AccounterServerConstants.UK_BOX10_UNCATEGORISED);

				if (v.getIncreaseVATLine()) {
					if (!v.isSales()) {
						vd.setTotal(v.getTotal());
					} else {
						vd.setTotal(-1 * v.getTotal());
					}
				} else {
					if (v.isSales()) {
						vd.setTotal(v.getTotal());
					} else {
						vd.setTotal(-1 * v.getTotal());
					}
				}

				if (v.getTaxItem().getName().equals("EC Sales Goods Standard")
						|| v.getTaxItem().getName()
								.equals("EC Sales Services Standard"))
					vd1.setTotal(-1 * vd1.getTotal());

				Account liabilityAccount = v.isSales() == true ? v.getTaxItem()
						.getTaxAgency().getSalesLiabilityAccount() : v
						.getTaxItem().getTaxAgency()
						.getPurchaseLiabilityAccount();

				vd1.setPayeeName(liabilityAccount.getName());

				vd1.setTransactionDate(new ClientFinanceDate(v.getDate()
						.getDate()));
				vd1.setTransactionName(v.toString());
				vd1.setTransactionNumber(v.getNumber());
				vd1.setTransactionType(v.getType());
				vd1.setTransactionId(v.getID());

				if (vatDetailReport.getEntries().get(vd1.getBoxName()) != null)
					vatDetailReport.getEntries().get(vd1.getBoxName()).add(vd1);
			}
		}

		// Getting journal entries from VATReturn
		{
			if (taxAgency != null) {
				query = session
						.getNamedQuery("getVat.by.taxAgency.and.VatPeriod")
						.setEntity("company", company)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("taxAgency", taxAgency.getID());
			} else {
				query = session.getNamedQuery("getVat.by.BetweenendDates")
						.setEntity("company", company)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate);
			}

			List<TAXReturn> vatReturns = query.list();
			for (TAXReturn v : vatReturns) {
				List<Box> boxes = toServerBoxes(v.getTaxReturnEntries(),
						v.getTaxAgency());
				// Adding Filed vat entries to it's Respective boxes, except
				// Box3 and Box5
				// Query query1 = session.getNamedQuery("getFiledBoxValues")
				// .setParameter("id", v.getID())
				// .setParameter("companyId", company.getID());
				//
				// List list = query1.list();
				// Object[] object = null;
				// Iterator iterator = list.iterator();

				for (Box box : boxes) {

					// object = (Object[]) iterator.next();

					VATDetail vd = new VATDetail();

					vd.setBoxName(setVATBoxName(box.getName()));

					// if (((String) object[0])
					// .equals("Box 2 VAT due in this period on acquisitions from other EC member states")
					// && vd.getBoxName().equals(
					// VATSummary.UK_BOX10_UNCATEGORISED))
					// vd
					// .setBoxName(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);

					if (box.getAmount() != 0.0
							&& (!vd.getBoxName().equals(
									VATSummary.UK_BOX3_TOTAL_OUTPUT) && !vd
									.getBoxName().equals(
											VATSummary.UK_BOX5_NET_VAT))) {

						vd.setTotal(-1 * box.getAmount());
						vd.setTransactionDate(v.getDate().toClientFinanceDate());
						vd.setTransactionName("TAXReturn");
						vd.setTransactionNumber(v.getNumber());
						vd.setTransactionType(v.getType());

						if (vatDetailReport.getEntries().get(vd.getBoxName()) != null)
							vatDetailReport.getEntries().get(vd.getBoxName())
									.add(vd);
					}
				}
			}
		}
	}

	private String setVATBoxName(String getBoxName) {
		String boxName = null;
		if (getBoxName
				.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES)) {
			boxName = VATSummary.UK_BOX1_VAT_DUE_ON_SALES;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT)) {
			boxName = VATSummary.UK_BOX3_TOTAL_OUTPUT;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
			boxName = VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES;
		} else if (getBoxName.equals(AccounterServerConstants.UK_BOX5_NET_VAT)) {
			boxName = VATSummary.UK_BOX5_NET_VAT;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES)) {
			boxName = VATSummary.UK_BOX6_TOTAL_NET_SALES;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES)) {
			boxName = VATSummary.UK_BOX7_TOTAL_NET_PURCHASES;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES)) {
			boxName = VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS;
		} else {
			boxName = VATSummary.UK_BOX10_UNCATEGORISED;
		}

		return boxName;
	}

	private String setTotalBoxName(TAXRateCalculation v) {
		String boxName = null;
		if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES)) {
			boxName = VATSummary.UK_BOX1_VAT_DUE_ON_SALES;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getTotalBox()
				.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT)) {
			boxName = VATSummary.UK_BOX3_TOTAL_OUTPUT;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getTotalBox()
				.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
			boxName = VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX5_NET_VAT)) {
			boxName = VATSummary.UK_BOX5_NET_VAT;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES)) {
			boxName = VATSummary.UK_BOX6_TOTAL_NET_SALES;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES)) {
			boxName = VATSummary.UK_BOX7_TOTAL_NET_PURCHASES;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES)) {
			boxName = VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getTotalBox()
				.equals(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS;
		} else {
			boxName = VATSummary.UK_BOX10_UNCATEGORISED;
		}

		return boxName;
	}

	public ArrayList<VATDetail> getPriorVATReturnVATDetailReport(
			TAXAgency vatAgency, FinanceDate endDate, long companyId)
			throws DAOException, ParseException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate;
		{
			Query q1 = session.getNamedQuery("getVATReturn.by.enddate")
					.setParameter("endDate", endDate)
					.setEntity("company", company);

			TAXReturn vatReturn = (TAXReturn) q1.uniqueResult();
			if (vatReturn == null) {
				throw new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						new NullPointerException(
								"No VAT Return found in database with VATAgency '"
										+ vatAgency.getName()
										+ "' and End date :"
										+ endDate.toString()));
			}

			startDate = vatReturn.getPeriodStartDate();

		}

		VATDetailReport vatDetailReport = new VATDetailReport(
				vatAgency.getVATReturn());
		prepareVATDetailReport(vatDetailReport, vatAgency, startDate, endDate,
				company);

		return getListOfVATDetails(vatDetailReport);
	}

	public ArrayList<VATDetail> getVATDetailReport(FinanceDate startDate,
			FinanceDate endDate, long companyId) throws DAOException,
			ParseException {

		VATDetailReport vatDetailReport = new VATDetailReport();
		Company company = getCompany(companyId);
		prepareVATDetailReport(vatDetailReport, null, startDate, endDate,
				company);

		return getListOfVATDetails(vatDetailReport);
	}

	private ArrayList<VATDetail> getListOfVATDetails(
			VATDetailReport vatDetailReport) {

		List<VATDetail> vatDetails = new ArrayList<VATDetail>();

		LinkedHashMap<String, List<VATDetail>> map = vatDetailReport
				.getEntries();

		if (map.containsKey(VATSummary.UK_BOX10_UNCATEGORISED)
				&& map.get(VATSummary.UK_BOX10_UNCATEGORISED).size() > 0)
			vatDetails.addAll((map.get(VATSummary.UK_BOX10_UNCATEGORISED)));

		// if (map.containsKey(VATDetailReport.IRELAND_BOX10_UNCATEGORISED)
		// && map.get(VATDetailReport.IRELAND_BOX10_UNCATEGORISED).size() > 0)
		// vatDetails.addAll(map
		// .get(VATDetailReport.IRELAND_BOX10_UNCATEGORISED));

		if (map.containsKey(VATSummary.UK_BOX1_VAT_DUE_ON_SALES)
				&& map.get(VATSummary.UK_BOX1_VAT_DUE_ON_SALES).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX1_VAT_DUE_ON_SALES));

		if (map.containsKey(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
				&& map.get(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS).size() > 0)
			vatDetails.addAll(map
					.get(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS));

		if (map.containsKey(VATSummary.UK_BOX3_TOTAL_OUTPUT)
				&& map.get(VATSummary.UK_BOX3_TOTAL_OUTPUT).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX3_TOTAL_OUTPUT));

		if (map.containsKey(VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)
				&& map.get(VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES).size() > 0)
			vatDetails.addAll(map
					.get(VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES));

		if (map.containsKey(VATSummary.UK_BOX5_NET_VAT)
				&& map.get(VATSummary.UK_BOX5_NET_VAT).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX5_NET_VAT));

		if (map.containsKey(VATSummary.UK_BOX6_TOTAL_NET_SALES)
				&& map.get(VATSummary.UK_BOX6_TOTAL_NET_SALES).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX6_TOTAL_NET_SALES));

		if (map.containsKey(VATSummary.UK_BOX7_TOTAL_NET_PURCHASES)
				&& map.get(VATSummary.UK_BOX7_TOTAL_NET_PURCHASES).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX7_TOTAL_NET_PURCHASES));

		if (map.containsKey(VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES)
				&& map.get(VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES));

		if (map.containsKey(VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS)
				&& map.get(VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS).size() > 0)
			vatDetails.addAll(map
					.get(VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS));

		if (map.containsKey(VATDetailReport.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES)
				&& map.get(VATDetailReport.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES));

		if (map.containsKey(VATDetailReport.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS)
				&& map.get(
						VATDetailReport.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS)
						.size() > 0)
			vatDetails
					.addAll(map
							.get(VATDetailReport.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS));

		if (map.containsKey(VATDetailReport.IRELAND_BOX3_VAT_ON_SALES)
				&& map.get(VATDetailReport.IRELAND_BOX3_VAT_ON_SALES).size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX3_VAT_ON_SALES));

		if (map.containsKey(VATDetailReport.IRELAND_BOX4_VAT_ON_PURCHASES)
				&& map.get(VATDetailReport.IRELAND_BOX4_VAT_ON_PURCHASES)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX4_VAT_ON_PURCHASES));

		if (map.containsKey(VATDetailReport.IRELAND_BOX5_T3_T4_PAYMENT_DUE)
				&& map.get(VATDetailReport.IRELAND_BOX5_T3_T4_PAYMENT_DUE)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX5_T3_T4_PAYMENT_DUE));

		if (map.containsKey(VATDetailReport.IRELAND_BOX6_E1_GOODS_TO_EU)
				&& map.get(VATDetailReport.IRELAND_BOX6_E1_GOODS_TO_EU).size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX6_E1_GOODS_TO_EU));

		if (map.containsKey(VATDetailReport.IRELAND_BOX7_E2_GOODS_FROM_EU)
				&& map.get(VATDetailReport.IRELAND_BOX7_E2_GOODS_FROM_EU)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX7_E2_GOODS_FROM_EU));

		if (map.containsKey(VATDetailReport.IRELAND_BOX8_TOTAL_NET_SALES)
				&& map.get(VATDetailReport.IRELAND_BOX8_TOTAL_NET_SALES).size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX8_TOTAL_NET_SALES));

		if (map.containsKey(VATDetailReport.IRELAND_BOX9_TOTAL_NET_PURCHASES)
				&& map.get(VATDetailReport.IRELAND_BOX9_TOTAL_NET_PURCHASES)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX9_TOTAL_NET_PURCHASES));

		return new ArrayList<VATDetail>(vatDetails);

	}

	private String setVATBoxName(TAXRateCalculation v) {
		String boxName = null;
		if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES)) {
			boxName = VATSummary.UK_BOX1_VAT_DUE_ON_SALES;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getVatBox()
				.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT)) {
			boxName = VATSummary.UK_BOX3_TOTAL_OUTPUT;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getVatBox()
				.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
			boxName = VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX5_NET_VAT)) {
			boxName = VATSummary.UK_BOX5_NET_VAT;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES)) {
			boxName = VATSummary.UK_BOX6_TOTAL_NET_SALES;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES)) {
			boxName = VATSummary.UK_BOX7_TOTAL_NET_PURCHASES;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES)) {
			boxName = VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getVatBox()
				.equals(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS;
		} else {
			boxName = VATSummary.UK_BOX10_UNCATEGORISED;
		}

		return boxName;
	}

	public ArrayList<CheckDetailReport> getCheckDetailReport(
			long paymentmethod, FinanceDate startDate, FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery("getCheckDetailReport")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("paymentmethod", paymentmethod,
						EncryptedStringType.INSTANCE);
		List list = query.list();
		Object[] object = null;
		Iterator iterator = list.iterator();
		List<CheckDetailReport> queryResult = new ArrayList<CheckDetailReport>();
		while ((iterator).hasNext()) {

			CheckDetailReport checkDetail = new CheckDetailReport();
			object = (Object[]) iterator.next();

			checkDetail.setTransactionId((Long) object[0]);
			checkDetail.setTransactionType(((Integer) object[1]));
			checkDetail.setNumber((String) object[2]);
			checkDetail.setTransactionDate(new ClientFinanceDate(
					((Long) object[3])));
			checkDetail.setPaymentMethod((String) object[4]);
			checkDetail.setPayeeName((String) object[5]);
			checkDetail.setAccountName((String) object[6]);
			checkDetail.setAmount((Double) object[7]);
			checkDetail.setMemo(object[8] == null ? "" : (String) object[8]);

			queryResult.add(checkDetail);

		}

		return new ArrayList<CheckDetailReport>(queryResult);
	}

	public ArrayList<ExpenseList> getExpenseReportByType(int type,
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {
		List list = null;
		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getExpenseReportByType")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("type", type);
		list = query.list();

		Object[] object = null;
		Iterator iterator = list.iterator();
		List<ExpenseList> queryResult = new ArrayList<ExpenseList>();
		while ((iterator).hasNext()) {

			ExpenseList expense = new ExpenseList();
			object = (Object[]) iterator.next();

			expense.setTransactionId(object[0] != null ? (Long) object[0] : 0);
			expense.setTransactionType(object[1] != null ? (Integer) object[1]
					: 0);
			expense.setTransactionDate(object[2] != null ? new ClientFinanceDate(
					((Long) object[2])) : new ClientFinanceDate());
			expense.setTransactionNumber(object[3] != null ? (String) object[3]
					: "");
			expense.setMemo(object[4] != null ? (String) object[4] : "");
			String name = "";
			if (object[5] != null) {
				name = object[5].toString();
			}
			if (expense.getTransactionType() == 28) {
				name = object[6].toString();
			}
			expense.setName(name);
			expense.setTotal(object[7] != null ? (Double) object[7] : 0);
			queryResult.add(expense);

		}

		return new ArrayList<ExpenseList>(queryResult);
	}

	public ArrayList<ReverseChargeList> getReverseChargeListReport(
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {
		// /////
		// ////////
		// /////////
		// //////
		// //////

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		// Getting entries from VATRateCalculation
		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.by.datesand.orderby.transactionItem")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();

		Map<String, Double> maps = new LinkedHashMap<String, Double>();

		long transactionItemId = 0;

		for (TAXRateCalculation v : taxRateCalculations) {

			if (v.getTransaction().getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {

				String s = v.getTransaction().getInvolvedPayee() != null ? v
						.getTransaction().getInvolvedPayee().getName() : "";

				if (maps.containsKey(s)) {
					maps.put(s,
							maps.get(s) + v.getTAXAmount() + v.getLineTotal());
					if (v.getTransaction().isVoid()) {
						maps.put(
								s,
								maps.get(s)
										- (v.getTAXAmount() + v.getLineTotal()));
					}
				} else {
					maps.put(s, v.getTAXAmount() + v.getLineTotal());
					if (v.getTransaction().isVoid()) {
						maps.put(
								s,
								maps.get(s)
										- (v.getTAXAmount() + v.getLineTotal()));
					}
				}

			}
		}

		String[] names = new String[maps.keySet().size()];

		maps.keySet().toArray(names);

		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);

		List<ReverseChargeList> reverseCharges = new ArrayList<ReverseChargeList>();

		for (String s : names) {

			ReverseChargeList r = new ReverseChargeList();
			r.setAmount(maps.get(s));
			r.setName(s);

			reverseCharges.add(r);
		}

		return new ArrayList<ReverseChargeList>(reverseCharges);
	}

	public ArrayList<VATSummary> getVAT100Report(TAXAgency taxAgency,
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {
		Session session = HibernateUtil.getCurrentSession();

		List<VATSummary> vatSummaries = createRows(taxAgency);
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery("getVATReturn.checkingby.taxagencyidand.dates")
				.setLong("id", taxAgency.getID()).setEntity("company", company);

		Object object[] = null;
		List list = query.list();
		Iterator it = list.iterator();
		FinanceDate leastStartDate = null;
		FinanceDate highestEndDate = null;

		while (it.hasNext()) {
			object = (Object[]) it.next();
			leastStartDate = (((FinanceDate) object[0]) == null ? null
					: ((FinanceDate) object[0]));
			highestEndDate = (((FinanceDate) object[1]) == null ? null
					: ((FinanceDate) object[1]));

		}

		if (leastStartDate != null && highestEndDate != null) {

			query = session
					.getNamedQuery(
							"getTAXRateCalculation.by.datesand.vatReturn")
					.setParameter("startDate", fromDate)
					.setParameter("endDate", toDate)
					.setEntity("company", company);
			// .setParameter("startDate1",
			// leastStartDate).setParameter("endDate1",
			// highestEndDate);
			// v.transactionDate not between :startDate1 and :endDate1")
		} else {
			query = session.getNamedQuery("getTAXRateCalculation.by.dates")
					.setParameter("startDate", fromDate)
					.setParameter("endDate", toDate)
					.setEntity("company", company);
		}

		List<TAXRateCalculation> vats = query.list();

		// double rcAmount = 0;

		for (TAXRateCalculation v : vats) {

			// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
			// messages.UK_BOX1_VAT_DUE_ON_SALES)
			// && (v.getVatItem().getVatReturnBox().getTotalBox()
			// .equals(messages.BOX_NONE))) {
			// rcAmount += -1 * (v.getVatAmount());
			// }

			// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
			// messages.UK_BOX2_VAT_DUE_ON_ACQUISITIONS))
			// rcAmount += -1 * (v.getVatAmount());

			/*
			 * Here all box values other than Vat code EGS (for purchase) of
			 * box2 and Vat code RC (for purchase) of box1 are getting it's
			 * native values
			 */

			for (VATSummary vs : vatSummaries) {

				if (v.getTaxItem().getVatReturnBox().getVatBox()
						.equals(vs.getVatReturnEntryName())) {

					if (v.getTaxItem()
							.getVatReturnBox()
							.getVatBox()
							.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
							|| (v.getTaxItem()
									.getVatReturnBox()
									.getVatBox()
									.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES) && !v
									.isVATGroupEntry())
							|| (v.getTaxItem()
									.getVatReturnBox()
									.getVatBox()
									.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES) && v
									.getTaxItem().getVatReturnBox()
									.getTotalBox()
									.equals(AccounterServerConstants.BOX_NONE)))
						vs.setValue(vs.getValue() + (-1 * (v.getTAXAmount())));
					else
						vs.setValue(vs.getValue() + v.getTAXAmount());
					// if (v
					// .getVatItem()
					// .getVatReturnBox()
					// .getVatBox()
					// .equals(
					// messages.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
					// vs.setValue(vs.getValue() + rcAmount);
					// rcAmount = 0.0;
					// }
				}

				if (v.getTaxItem().getVatReturnBox().getTotalBox()
						.equals(vs.getVatReturnEntryName())) {

					vs.setValue(vs.getValue() + v.getLineTotal());

				}
			}
		}

		query = session
				.getNamedQuery("getTAXAdjustment.by.taxAgencyidanddates")
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("taxAgency", taxAgency.getID())
				.setEntity("company", company);

		List<TAXAdjustment> vas = query.list();
		// double box2Amount = 0.0;

		if (vas != null) {
			for (TAXAdjustment v : vas) {
				for (VATSummary vs : vatSummaries) {

					// if (vs
					// .getVatReturnEntryName()
					// .equals(
					// messages.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
					// vs.setValue(vs.getValue() + box2Amount);
					// box2Amount = 0.0;
					// }

					if (v.getTaxItem().getVatReturnBox().getVatBox()
							.equals(vs.getVatReturnEntryName())) {
						if (v.getIncreaseVATLine())
							vs.setValue(vs.getValue() + v.getTotal());
						else
							vs.setValue(vs.getValue() - v.getTotal());

						// if (v
						// .getVatItem()
						// .getVatReturnBox()
						// .getVatBox()
						// .equals(
						// messages.UK_BOX2_VAT_DUE_ON_ACQUISITIONS))
						// box2Amount += (v.getIncreaseVATLine() == true ? v
						// .getTotal() : -1 * v.getTotal());
					}

					else if (vs.getVatReturnEntryName().equals(
							"Uncategorised Tax Amounts")) {

						if (v.isSales()) {
							if ((v.getTaxItem().getID() != 3)
									&& (v.getTaxItem().getID() != 4)) {
								if (v.getIncreaseVATLine())
									vs.setValue(vs.getValue() - v.getTotal());
								else
									vs.setValue(vs.getValue() + v.getTotal());

							} else {

								if (v.getIncreaseVATLine())
									vs.setValue(vs.getValue() + v.getTotal());
								else
									vs.setValue(vs.getValue() - v.getTotal());
							}
						}

						else {
							if (v.getIncreaseVATLine())
								vs.setValue(vs.getValue() + v.getTotal());
							else
								vs.setValue(vs.getValue() - v.getTotal());
						}
					}
				}
			}

		}

		// for (VATSummary v : vatSummaries) {
		// if (v.getVatReturnEntryName().equals(
		// messages.UK_BOX2_VAT_DUE_ON_ACQUISITIONS))
		// v.setVatReturnEntryName("Box 2 "
		// + messages.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);
		// }

		return new ArrayList<VATSummary>(vatSummaries);
	}

	private List<VATSummary> createRows(TAXAgency taxAgency) {

		List<VATSummary> vatSummaries = new ArrayList<VATSummary>();

		if (taxAgency.getVATReturn() == TAXAgency.RETURN_TYPE_UK_VAT) {
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES,
					VATSummary.UK_BOX1_VAT_DUE_ON_SALES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS,
					VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT,
					VATSummary.UK_BOX3_TOTAL_OUTPUT, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES,
					VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX5_NET_VAT,
					VATSummary.UK_BOX5_NET_VAT, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES,
					VATSummary.UK_BOX6_TOTAL_NET_SALES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES,
					VATSummary.UK_BOX7_TOTAL_NET_PURCHASES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES,
					VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS,
					VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX10_UNCATEGORISED,
					VATSummary.UK_BOX10_UNCATEGORISED, 0d));
		} else if (taxAgency.getVATReturn() == TAXAgency.RETURN_TYPE_IRELAND_VAT) {
			vatSummaries
					.add(new VATSummary(
							AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES,
							VATSummary.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES, 0d));
			vatSummaries
					.add(new VATSummary(
							AccounterServerConstants.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS,
							VATSummary.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS,
							0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX3_VAT_ON_SALES,
					VATSummary.IRELAND_BOX3_VAT_ON_SALES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX4_VAT_ON_PURCHASES,
					VATSummary.IRELAND_BOX4_VAT_ON_PURCHASES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX5_T3_T4_PAYMENT_DUE,
					VATSummary.IRELAND_BOX5_T3_T4_PAYMENT_DUE, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX6_E1_GOODS_TO_EU,
					VATSummary.IRELAND_BOX6_E1_GOODS_TO_EU, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX7_E2_GOODS_FROM_EU,
					VATSummary.IRELAND_BOX7_E2_GOODS_FROM_EU, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES,
					VATSummary.IRELAND_BOX8_TOTAL_NET_SALES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES,
					VATSummary.IRELAND_BOX9_TOTAL_NET_PURCHASES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX10_UNCATEGORISED,
					VATSummary.IRELAND_BOX10_UNCATEGORISED, 0d));
		}
		return vatSummaries;
	}

	public ArrayList<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws ParseException {
		List<UncategorisedAmountsReport> uncategorisedAmounts = new ArrayList<UncategorisedAmountsReport>();

		Session session = HibernateUtil.getCurrentSession();

		// Entries from Sales where Vat Codes EGS and RC are used
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("getEGSandRCentriesFromSales")
				.setParameter("startDate", fromDate.getDate())
				.setParameter("endDate", toDate.getDate())
				.setLong("companyId", companyId);

		List list2 = query.list();
		Object[] object = null;
		Iterator iterator = list2.iterator();
		while (iterator.hasNext()) {

			object = (Object[]) iterator.next();
			UncategorisedAmountsReport u = new UncategorisedAmountsReport();
			u.setTransactionType((Integer) object[0]);
			u.setDate(new ClientFinanceDate((Long) object[1]));
			u.setTransactionNumber((String) object[2]);
			u.setSourceName((String) object[4]);
			u.setMemo((String) object[5]);
			u.setAmount(object[6] == null ? 0 : (Double) object[6]);

			uncategorisedAmounts.add(u);
		}

		query = session
				.getNamedQuery("getTAXAdjustment.checkingby.transactionDate")
				.setParameter("endDate", toDate)
				.setEntity("company", getCompany(companyId));

		List<TAXAdjustment> vatAdjustments = query.list();

		for (TAXAdjustment v : vatAdjustments) {

			if (!DecimalUtil.isEquals(v.getTotal(), 0)) {

				UncategorisedAmountsReport u = new UncategorisedAmountsReport();

				u.setTransactionType(v.getType());
				u.setTransactionNumber(v.getNumber());
				u.setID(v.getID());
				u.setSourceName(v.getAdjustmentAccount().getName());
				u.setMemo("VAT Adjustment");
				u.setDate(new ClientFinanceDate(v.getDate().getDate()));
				if (v.isSales()) {
					if (v.getIncreaseVATLine()) {

						u.setAmount(-1 * (v.getTotal()));
					} else {
						u.setAmount(v.getTotal());
					}

					if ((v.getTaxItem().getName()
							.equals("EC Sales Services Standard"))
							|| (v.getTaxItem().getName()
									.equals("EC Sales Goods Standard"))) {

						u.setAmount(-1 * u.getAmount());
					}

				} else {
					if (v.getIncreaseVATLine()) {

						u.setAmount(v.getTotal());
					} else {
						u.setAmount(-1 * v.getTotal());
					}
				}

				uncategorisedAmounts.add(u);
			}
		}

		// Entries from VATReturn;

		query = session
				.getNamedQuery("getTAXAdjustment.checkingby.VATperiodEndDate")
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXReturn> vatReturns = query.list();
		for (TAXReturn v : vatReturns) {

			double amount = v.getBoxes().isEmpty() ? 0.00 : v.getBoxes()
					.get(v.getBoxes().size() - 1).getAmount();

			if (!DecimalUtil.isEquals(amount, 0)) {

				UncategorisedAmountsReport u = new UncategorisedAmountsReport();

				u.setTransactionType(v.getType());
				u.setTransactionNumber(v.getNumber());
				u.setID(v.getID());
				u.setSourceName(v.getTaxAgency().getPurchaseLiabilityAccount()
						.getName());
				u.setMemo("Filed Uncategorised amounts");
				u.setDate(new ClientFinanceDate(v.getDate().getDate()));

				// if (amount > 0)
				u.setAmount(-1 * (amount));
				// else
				// u.setAmount(amount);

				uncategorisedAmounts.add(u);
			}
		}

		return new ArrayList<UncategorisedAmountsReport>(uncategorisedAmounts);
	}

	public ArrayList<VATItemDetail> getVATItemDetailReport(
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		// Entries from the VATRate calculation

		Query query = session
				.getNamedQuery("getTAXAdjustment.by.taxAgencyidanddates")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();
		for (TAXRateCalculation v : taxRateCalculations) {

			VATItemDetail vi = new VATItemDetail();
			vi.setAmount(v.getLineTotal());
			vi.setDate(new ClientFinanceDate(v.getTransactionDate().getDate()));
			vi.setName(v.getTransaction().getInvolvedPayee().getName());
			vi.setTransactionId(v.getTransaction().getID());
			vi.setMemo(v.getTransaction().getMemo());
			vi.setTransactionNumber(v.getTransaction().getNumber());
			vi.setTransactionType(v.getTransaction().getType());
			vi.setSalesPrice(vi.getAmount());

			vatItemDetails.add(vi);

		}

		// Entries from the VATAdjustment
		query = session
				.getNamedQuery(
						"getTAXAdjustment.by.dates.orderby.taxItemNameand.TransactionDate")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXAdjustment> vatAdjustments = query.list();
		for (TAXAdjustment v : vatAdjustments) {

			VATItemDetail vi = new VATItemDetail();

			if (v.getIncreaseVATLine()) {
				vi.setAmount(v.getTotal());
			} else {
				vi.setAmount(-1 * v.getTotal());
			}
			vi.setDate(new ClientFinanceDate(v.getDate().getDate()));
			vi.setMemo("VAT Adjustment");
			vi.setName(v.getTaxItem().getTaxAgency().getName());
			vi.setTransactionId(v.getID());
			vi.setTransactionNumber(v.getNumber());
			vi.setTransactionType(v.getType());
			vatItemDetails.add(vi);
		}

		return new ArrayList<VATItemDetail>(vatItemDetails);
	}

	public ArrayList<VATItemDetail> getVATItemDetailReport(String taxItemName,
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();

		Company company = getCompany(companyId);
		long transactionItemId = 0, vatItemId = 0;

		// Entries from the VATRate calculation

		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.by.dates.groupedByIdtransactionItem")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate)
				.setParameter("taxItemName", taxItemName)
				.setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();
		for (TAXRateCalculation v : taxRateCalculations) {

			if ((v.getTaxItem().getID() != 4 && v.getTaxItem().getID() != 12 && v
					.getTaxItem().getID() != 14)
					|| (v.getTaxItem().getID() == 4 && v.isVATGroupEntry() == false)
					|| (v.getTaxItem().getID() == 12 && v.isVATGroupEntry() == false)
					|| (v.getTaxItem().getID() == 14 && v.isVATGroupEntry() == false)) {

				VATItemDetail vi = new VATItemDetail();
				double amount = (!v.getTransaction().isVoid()) ? v
						.getLineTotal() : 0;
				vi.setAmount(amount);
				vi.setDate(new ClientFinanceDate(v.getTransactionDate()
						.getDate()));

				if (v.getTransaction().getInvolvedPayee() != null)
					vi.setName(v.getTransaction().getInvolvedPayee().getName());

				else {

					CashPurchase cashPurchase = (CashPurchase) (v
							.getTransaction());
					if (cashPurchase.getCashExpenseAccount() != null)
						vi.setName(cashPurchase.getCashExpenseAccount()
								.getName());
					else if (cashPurchase.getEmployee() != null)
						vi.setName(cashPurchase.getEmployee().getName());
				}

				vi.setTransactionId(v.getTransaction().getID());
				vi.setMemo(v.getTransaction().getMemo());
				vi.setTransactionNumber(v.getTransaction().getNumber());
				vi.setTransactionType(v.getTransaction().getType());
				vi.setSalesPrice(v.getLineTotal());

				vatItemId = v.getTaxItem().getID();

				vatItemDetails.add(vi);
			}
		}

		// Entries from the VATAdjustment
		query = session
				.getNamedQuery("getTAXAdjustment.by.dates.and.taxItemName")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate)
				.setParameter("taxItemName", taxItemName,
						EncryptedStringType.INSTANCE)
				.setEntity("company", company);

		List<TAXAdjustment> vatAdjustments = query.list();
		for (TAXAdjustment v : vatAdjustments) {

			VATItemDetail vi = new VATItemDetail();

			if (v.getIncreaseVATLine()) {
				vi.setAmount(v.getTotal());
			} else {
				vi.setAmount(-1 * v.getTotal());
			}
			vi.setDate(new ClientFinanceDate(v.getDate().getDate()));
			vi.setMemo("VAT Adjustment");
			vi.setName(v.getTaxItem().getTaxAgency().getName());
			vi.setTransactionId(v.getID());
			vi.setTransactionNumber(v.getNumber());
			vi.setTransactionType(v.getType());
			// vatItemDetails.add(vi);
		}

		return new ArrayList<VATItemDetail>(vatItemDetails);
	}

	public ArrayList<VATItemSummary> getVATItemSummaryReport(
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {

		List<VATItemSummary> vatItemSummaries = new ArrayList<VATItemSummary>();

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		// Getting entries from VATRateCalculation
		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.by.dates.orderbytaxItem.name")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();

		String tempVATItemName = null;
		long transactionItemId = 0;
		VATItemSummary vi = null;

		for (TAXRateCalculation v : taxRateCalculations) {

			if (tempVATItemName == null
					|| (!tempVATItemName.equals(v.getTaxItem().getName()))) {
				// if (transactionItemId == 0
				// || (transactionItemId != v.getTransactionItem().getID())) {

				if (vi != null) {
					vatItemSummaries.add(vi);
				}

				tempVATItemName = v.getTaxItem().getName();
				// transactionItemId = v.getTransactionItem().getID();

				vi = new VATItemSummary();
				vi.setName(tempVATItemName);
				// vi.setAmount(v.getVatAmount());
				vi.setAmount(v.getLineTotal());
				vi.setTaxAmount(v.getTAXAmount());
				vi.setTaxRate(v.getTaxItem().getTaxRate());
				if (v.getTransaction().isVoid()) {
					vi.setAmount(vi.getAmount() - v.getLineTotal());
				}
			} else {

				vi.setAmount(vi.getAmount() + v.getLineTotal());
				if (v.getTransaction().isVoid()) {
					vi.setAmount(vi.getAmount() - v.getLineTotal());
				}
			}

		}
		if (vi != null)
			vatItemSummaries.add(vi);

		return new ArrayList<VATItemSummary>(vatItemSummaries);
	}

	public ArrayList<ECSalesListDetail> getECSalesListDetailReport(
			String payeeName, FinanceDate fromDate, FinanceDate toDate,
			Company company) throws DAOException, ParseException {

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();

		// Getting entries from VATRateCalculation
		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.by.check.taxItemandDates.orderBy.transactionItem")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();

		List<ECSalesListDetail> details = new ArrayList<ECSalesListDetail>();

		Map<String, List<ECSalesListDetail>> customerWiseDetail = new LinkedHashMap<String, List<ECSalesListDetail>>();

		boolean isAlreadyPut = false;
		long transactionItemId = 0;
		for (TAXRateCalculation v : taxRateCalculations) {
			if (v.getTransaction().getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {

				if (v.getTransaction().getInvolvedPayee() != null
						&& (v.getTransaction().getInvolvedPayee().getName())
								.equals(payeeName)) {

					ECSalesListDetail e = new ECSalesListDetail();
					double amount = (!v.getTransaction().isVoid()) ? v
							.getLineTotal() : 0;
					e.setAmount(amount);
					e.setDate(new ClientFinanceDate(v.getTransactionDate()
							.getDate()));
					e.setMemo(v.getTransaction().getMemo());
					e.setName(v.getTransaction().getInvolvedPayee() != null ? v
							.getTransaction().getInvolvedPayee().getName() : "");
					e.setSalesPrice(v.getLineTotal());
					e.setTransactionId(v.getTransaction().getID());
					e.setTransactionNumber(v.getTransaction().getNumber());
					e.setTransactionid(v.getTransaction().getID());
					e.setTransactionType(v.getTransaction().getType());

					if (customerWiseDetail.containsKey(e.getName())) {
						customerWiseDetail.get(e.getName()).add(e);
					} else {
						List<ECSalesListDetail> list = new ArrayList<ECSalesListDetail>();
						list.add(e);
						customerWiseDetail.put(e.getName(), list);
					}

					// if (v.getTransactionItem().isVoid()) {
					//
					// ECSalesListDetail e2 = new ECSalesListDetail();
					//
					// e2.setAmount(-1 * (v.getLineTotal()));
					// e2.setDate(new
					// ClientFinanceDate(v.getTransactionDate()
					// .getTime()));
					// e2.setMemo(v.getTransaction()
					// .getMemo());
					// e2.setName(v.getTransaction()
					// .getInvolvedPayee() != null ? v
					// .getTransaction()
					// .getInvolvedPayee().getName() : null);
					// e2.setSalesPrice(v.getTransactionItem().getLineTotal());
					// e2.setTransactionId(v.getTransactionItem()
					// .getTransaction().getID());
					// e2.setTransactionNumber(v.getTransactionItem()
					// .getTransaction().getNumber());
					// e2.setTransactionid(v.getTransactionItem()
					// .getTransaction().getID());
					// e2.setTransactionType(v.getTransactionItem()
					// .getTransaction().getType());
					//
					// customerWiseDetail.get(e2.getName()).add(e2);
					// }
				}
			}

		}

		String arr[] = new String[customerWiseDetail.keySet().size()];
		customerWiseDetail.keySet().toArray(arr);
		Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
		for (String a : arr) {
			details.addAll(customerWiseDetail.get(a));
		}

		return new ArrayList<ECSalesListDetail>(details);

	}

	public ArrayList<ECSalesList> getECSalesListReport(FinanceDate fromDate,
			FinanceDate toDate, Company company) throws DAOException,
			ParseException {

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();

		// Getting entries from VATRateCalculation
		Query query = session
				.getNamedQuery("getTAXRateCalculation.by.check.taxItemandDates")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();

		List<ECSalesList> details = new ArrayList<ECSalesList>();

		Map<String, Double> customerWiseDetail = new LinkedHashMap<String, Double>();

		for (TAXRateCalculation v : taxRateCalculations) {
			if (v.getTransaction().getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {

				String customerName = v.getTransaction().getInvolvedPayee() != null ? v
						.getTransaction().getInvolvedPayee().getName()
						: "";

				if (customerWiseDetail.containsKey(customerName)) {
					customerWiseDetail.put(customerName, (customerWiseDetail
							.get(customerName) + v.getLineTotal()));

				} else {
					customerWiseDetail.put(customerName, v.getLineTotal());

				}
				if (v.getTransaction().isVoid()) {
					customerWiseDetail.put(customerName, (customerWiseDetail
							.get(customerName) - v.getLineTotal()));
				}

			}
		}

		String arr[] = new String[customerWiseDetail.keySet().size()];
		customerWiseDetail.keySet().toArray(arr);
		Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
		for (String a : arr) {
			ECSalesList e = new ECSalesList();
			e.setName(a);
			e.setAmount(customerWiseDetail.get(a));
			details.add(e);
		}

		return new ArrayList<ECSalesList>(details);

	}

	public ArrayList<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, FinanceDate fromDate, FinanceDate toDate,
			long companyId) throws DAOException, ParseException {
		// /////
		// ////////
		// /////////
		// //////
		// //////

		Session session = HibernateUtil.getCurrentSession();

		Query query = session
				.getNamedQuery("getReverseChargeListDetailReportEntries")
				.setParameter("startDate", fromDate.getDate())
				.setParameter("endDate", toDate.getDate())
				.setParameter("companyId", companyId);

		Map<String, List<ReverseChargeListDetail>> maps = new LinkedHashMap<String, List<ReverseChargeListDetail>>();

		List list = query.list();
		Iterator it = list.iterator();
		long tempTransactionItemID = 0;
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();

			if (((String) object[1]) != null
					&& ((String) object[1]).equals(payeeName)) {

				ReverseChargeListDetail r = new ReverseChargeListDetail();

				r.setAmount((Double) object[0]);
				r.setCustomerName((String) object[1]);
				r.setMemo((String) object[2]);
				r.setName(payeeName);
				r.setNumber((String) object[3]);
				r.setPercentage((Boolean) object[4]);
				r.setSalesPrice((Double) object[5]);
				r.setTransactionId((Long) object[6]);
				r.setTransactionType((Integer) object[7]);
				r.setDate(new ClientFinanceDate((Long) object[9]));

				if (maps.containsKey(r.getCustomerName())) {
					maps.get(r.getCustomerName()).add(r);
				} else {
					List<ReverseChargeListDetail> reverseChargesList = new ArrayList<ReverseChargeListDetail>();
					reverseChargesList.add(r);
					maps.put(r.getCustomerName(), reverseChargesList);
				}

				if (tempTransactionItemID == 0
						|| ((Long) object[12]) != tempTransactionItemID) {

					ReverseChargeListDetail r2 = new ReverseChargeListDetail();

					tempTransactionItemID = ((Long) object[12]);

					r2.setAmount((Double) object[8]);
					r2.setCustomerName((String) object[1]);
					r2.setDate(new ClientFinanceDate((Long) object[9]));
					r2.setMemo((String) object[10]);
					r2.setName((String) object[1]);
					r2.setNumber((String) object[3]);
					r2.setPercentage(false);
					r2.setSalesPrice((Double) object[11]);
					r2.setTransactionId((Long) object[6]);
					r2.setTransactionType((Integer) object[7]);

					if (maps.containsKey(r2.getCustomerName())) {
						maps.get(r2.getCustomerName()).add(r2);
					} else {
						List<ReverseChargeListDetail> reverseChargesList = new ArrayList<ReverseChargeListDetail>();
						reverseChargesList.add(r2);
						maps.put(r2.getCustomerName(), reverseChargesList);
					}
				}
			}
		}
		String[] names = new String[maps.keySet().size()];

		maps.keySet().toArray(names);

		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);

		List<ReverseChargeListDetail> reverseCharges = new ArrayList<ReverseChargeListDetail>();

		for (String s : names) {

			reverseCharges.addAll(maps.get(s));
		}

		return new ArrayList<ReverseChargeListDetail>(reverseCharges);
	}

	public ArrayList<AgedDebtors> getAgedDebtors(final FinanceDate startDate,
			final FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getAgedDebtors")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return prepareAgedDebotOrsorCreditors(new ArrayList<AgedDebtors>(l),
				startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedDebtors(FinanceDate startDate,
			FinanceDate endDate, int intervalDays, int throughDaysPassOut,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("getTransactionDate.by.dates")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate).setEntity("company", company);
		return null;
	}

	public ArrayList<AgedDebtors> getAgedCreditors(final FinanceDate startDate,
			FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getAgedCreditors")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return prepareAgedDebotOrsorCreditors(new ArrayList<AgedDebtors>(l),
				startDate, endDate, companyId);

	}

	private ArrayList<AgedDebtors> prepareAgedDebotOrsorCreditors(List list,
			final FinanceDate startDate, FinanceDate endDate, long companyId) {
		Object[] object = null;
		Iterator iterator = list.iterator();
		List<AgedDebtors> queryResult = new ArrayList<AgedDebtors>();
		while ((iterator).hasNext()) {

			AgedDebtors agedDebtors = new AgedDebtors();
			object = (Object[]) iterator.next();

			agedDebtors.setName((String) object[0]);
			agedDebtors.setContact((String) object[1]);
			agedDebtors.setPhone((String) object[2]);
			agedDebtors.setType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			agedDebtors.setDate(new ClientFinanceDate((Long) object[4]));
			agedDebtors.setNumber((String) object[5]);
			agedDebtors.setReference((String) object[6]);
			agedDebtors.setDueDate(object[7] == null ? null
					: new ClientFinanceDate((Long) object[7]));
			agedDebtors.setPaymentTermName((String) object[8]);
			agedDebtors.setAmount(object[9] == null ? 0 : ((Double) object[9])
					.doubleValue());
			agedDebtors.setTotal(object[9] == null ? 0 : ((Double) object[9])
					.doubleValue());
			agedDebtors.setIsVoid(object[11] == null ? true
					: ((Boolean) object[11]).booleanValue());
			agedDebtors.setTransactionId((Long) object[12]);
			agedDebtors.setMemo(object[13] != null ? (String) object[13] : "");
			long ageing = getAgeing(agedDebtors.getDate(),
					agedDebtors.getDueDate(), endDate, companyId);
			int category = getCategory(ageing);
			agedDebtors.setAgeing(ageing);
			agedDebtors.setCategory(category);

			if (agedDebtors.getAmount() != 0)
				queryResult.add(agedDebtors);
		}

		Collections.sort(queryResult, new Comparator<AgedDebtors>() {

			@Override
			public int compare(AgedDebtors arg0, AgedDebtors arg1) {
				return arg0.getCategory() > arg1.getCategory() ? 2 : arg0
						.getCategory() < arg1.getCategory() ? -1 : 0;
			}
		});
		return new ArrayList<AgedDebtors>(queryResult);
	}

	public long getAgeing(ClientFinanceDate transactionDate,
			ClientFinanceDate dueDate, final FinanceDate endDate, long companyId) {

		long ageing = 0;

		try {

			ClientFinanceDate ageingForDueorTranactionDate;
			Company company = getCompany(companyId);
			if (company.getPreferences()
					.getAgeingFromTransactionDateORDueDate() == TYPE_AGEING_FROM_DUEDATE)
				ageingForDueorTranactionDate = dueDate;
			else
				ageingForDueorTranactionDate = transactionDate;

			if (!ageingForDueorTranactionDate.after(endDate
					.toClientFinanceDate()))
				ageing = UIUtils.getDays_between(ageingForDueorTranactionDate
						.getDateAsObject(), endDate.toClientFinanceDate()
						.getDateAsObject());

			// if (agedDebtors.getDueDate() != null
			// && !agedDebtors.getDueDate().equals("")
			// && !agedDebtors.getDueDate().after(
			// format.parse(endDate))) {
			//
			// diff = UIUtils.getDays_between(format.parse(endDate),
			// agedDebtors.getDueDate());
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ageing;
	}

	public ArrayList<PayeeStatementsList> getPayeeStatementsList(
			boolean isVendor, long id, int viewType, FinanceDate fromDate,
			FinanceDate toDate, long companyId) throws DAOException {
		ArrayList<PayeeStatementsList> statementsLists;
		if (isVendor) {
			statementsLists = getVendorStatementsList(id, viewType, fromDate,
					toDate, companyId);
		} else {
			statementsLists = getCustomerStatementsList(id, viewType, fromDate,
					toDate, companyId);
		}
		return statementsLists;
	}

	/**
	 * 
	 * @param financeDates
	 * @param financeDates2
	 * @param companyID
	 * @return
	 */
	public ArrayList<Reconciliation> getAllReconciliationslist(
			FinanceDate financeDates, FinanceDate financeDates2, long companyID) {
		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery(
				"get.all.reconciliations.group.by.account.id").setParameter(
				"company_Id", companyID);
		List list = query.list();
		if (list != null) {
			Object[] object = null;
			Iterator iterator = list.iterator();
			List<Reconciliation> queryResult = new ArrayList<Reconciliation>();
			while (iterator.hasNext()) {
				Reconciliation reconcilationList = new Reconciliation();
				object = (Object[]) iterator.next();
				reconcilationList.setAccountId(object[0] == null ? null
						: (Long) object[0]);
				reconcilationList.setAccountName(object[1] == null ? null
						: (String) object[1]);

				reconcilationList.setAccountType(object[2] == null ? null
						: (Integer) object[2]);
				reconcilationList.setStatementdate(object[3] == null ? null
						: new ClientFinanceDate((Long) object[3]));
				reconcilationList
						.setReconcilationdate((object[4] == null ? null
								: new ClientFinanceDate((Long) object[4])));
				queryResult.add(reconcilationList);
			}
			return new ArrayList<Reconciliation>(queryResult);
		}

		return null;

	}

	private ArrayList<PayeeStatementsList> getCustomerStatementsList(long id,
			int viewType, FinanceDate fromDate, FinanceDate toDate,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			if (id == 0) {
				return null;
			}
			List<PayeeStatementsList> queryResult = new ArrayList<PayeeStatementsList>();
			if (viewType == 0) {
				Query balanceQuery = session
						.getNamedQuery("getOpeningBalanceForCustomerByDate")
						.setParameter("uptoDate", fromDate.getDate())
						.setParameter("payeeId", id)
						.setParameter("companyId", companyId);
				Object balance = balanceQuery.uniqueResult();
				PayeeStatementsList balanceStatementsList = new PayeeStatementsList();
				balanceStatementsList.setTransactionDate(new ClientFinanceDate(
						fromDate.getAsDateObject()));
				balanceStatementsList
						.setBalance(balance != null ? (Double) balance : 0);
				balanceStatementsList.setpayeeId(id);
				queryResult.add(balanceStatementsList);
			}

			Payee payee = (Payee) session.get(Payee.class, id);
			List<Address> address = new ArrayList<Address>(payee.getAddress());
			Collections.sort(address, new Comparator<Address>() {

				@Override
				public int compare(Address o1, Address o2) {
					return o2.getType() - o1.getType();
				}
			});
			ClientAddress billToAddr = address.isEmpty() ? null
					: new ClientConvertUtil().toClientObject(address.get(0),
							ClientAddress.class);

			Query query = session
					.getNamedQuery("getCreatableStatementForCustomer")
					.setParameter("startDate", fromDate.getDate())
					.setParameter("endDate", toDate.getDate())
					.setParameter("payeeId", id)
					.setParameter("companyId", companyId)
					.setParameter("viewType", viewType)
					.setParameter("todayDate", new FinanceDate().getDate());
			List list = query.list();
			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				while ((iterator).hasNext()) {

					PayeeStatementsList statementsList = new PayeeStatementsList();
					object = (Object[]) iterator.next();

					statementsList.setTransactiontype(object[0] == null ? null
							: (Integer) object[0]);
					statementsList.setTransactionNumber((String) object[1]);
					statementsList
							.setTransactionDate(((Long) object[2]) == null ? null
									: new ClientFinanceDate((Long) object[2]));
					statementsList.setDueDate(((Long) object[3]) == null ? null
							: new ClientFinanceDate((Long) object[3]));
					statementsList.setTotal((Double) object[4]);
					statementsList.setBalance((Double) object[5]);
					statementsList
							.setPayeeName(((String) object[6]) == null ? null
									: (String) object[6]);

					if (billToAddr != null) {
						statementsList.setBillingAddress(billToAddr);
					}

					statementsList.setSalesPerson(object[7] == null ? null
							: (String) object[7]);
					statementsList.setShippingMethod(object[8] == null ? null
							: (String) object[8]);
					statementsList.setPaymentTerm(object[9] == null ? null
							: (String) object[9]);
					statementsList.setTransactionId(object[10] == null ? null
							: ((Long) object[10]).longValue());
					long ageing = getAgeing(
							statementsList.getTransactionDate(),
							statementsList.getDueDate(), toDate, companyId);
					statementsList.setAgeing(ageing);
					// statementsList.setCategory(getCategory(ageing));

					statementsList.setCurrency((Long) object[11]);
					statementsList.setCurrencyFactor((Double) object[12]);
					statementsList.setSaveStatus((Integer) object[13]);
					statementsList.setpayeeId(id);

					queryResult.add(statementsList);
				}
				return new ArrayList<PayeeStatementsList>(queryResult);
			} else {
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ArrayList<PayeeStatementsList> getVendorStatementsList(long id,
			int viewType, FinanceDate fromDate, FinanceDate toDate,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			if (id == 0) {
				return null;
			}
			List<PayeeStatementsList> queryResult = new ArrayList<PayeeStatementsList>();
			if (viewType == 0) {
				Query balanceQuery = session
						.getNamedQuery("getOpeningBalanceForVendorByDate")
						.setParameter("uptoDate", fromDate.getDate())
						.setParameter("payeeId", id)
						.setParameter("companyId", companyId);
				Object balance = balanceQuery.uniqueResult();
				PayeeStatementsList balanceStatementsList = new PayeeStatementsList();
				balanceStatementsList.setTransactionDate(new ClientFinanceDate(
						fromDate.getAsDateObject()));
				balanceStatementsList
						.setBalance(balance != null ? (Double) balance : 0);
				balanceStatementsList.setpayeeId(id);
				queryResult.add(balanceStatementsList);
			}

			Query query = session
					.getNamedQuery("getCreatableStatementForVendor")
					.setParameter("startDate", fromDate.getDate())
					.setParameter("endDate", toDate.getDate())
					.setParameter("payeeId", id)
					.setParameter("companyId", companyId)
					.setParameter("viewType", viewType)
					.setParameter("todayDate", new FinanceDate().getDate());

			List list = query.list();
			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				while ((iterator).hasNext()) {
					PayeeStatementsList statementsList = new PayeeStatementsList();
					object = (Object[]) iterator.next();

					statementsList.setTransactiontype(object[0] == null ? null
							: (Integer) object[0]);
					statementsList.setTransactionNumber((String) object[1]);
					statementsList
							.setTransactionDate(((Long) object[2]) == null ? null
									: new ClientFinanceDate((Long) object[2]));
					statementsList.setTransactionId((Long) object[3]);
					statementsList.setTotal((Double) object[4]);
					statementsList.setBalance((Double) object[5]);
					statementsList.setCurrency((Long) object[6]);
					statementsList.setCurrencyFactor((Double) object[7]);
					statementsList.setSaveStatus((Integer) object[8]);
					statementsList.setpayeeId(id);
					queryResult.add(statementsList);
				}
				return new ArrayList<PayeeStatementsList>(queryResult);
			} else {
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getCategory(long days) {
		// if (diff > 0)
		// agedDebtors.setCategory(1);
		if (days >= 0 && days <= 30)
			return 1;
		else if (days > 30 && days <= 60)
			return 2;
		else if (days > 60 && days <= 90)
			return 3;
		else if (days > 90)
			// else if (diff > 90 && diff < 120)
			return 4;
		// else if (diff > 120 && diff < 150)
		// agedDebtors.setCategory(5);
		// else if (diff > 150 && diff < 180)
		// agedDebtors.setCategory(6);
		return 0;
	}

	public ArrayList<TAXItemDetail> getTAXItemDetailReport(Long companyId,
			long taxAgency, long startDate, long endDate) {

		ArrayList<TAXItemDetail> vatItemDetails = new ArrayList<TAXItemDetail>();

		Session session = HibernateUtil.getCurrentSession();

		Company company = getCompany(companyId);
		long transactionItemId = 0, vatItemId = 0;

		// Entries from the VATRate calculation

		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.for.TaxReturn.for.reports")
				.setParameter("taxAgency", taxAgency)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("companyId", companyId);

		List<Object[]> taxRateCalculations = query.list();
		for (Object[] objects : taxRateCalculations) {

			TAXItemDetail taxItemDetail = new TAXItemDetail();
			taxItemDetail.setTaxAmount((Double) objects[0]);
			taxItemDetail.setNetAmount((Double) objects[1]);
			taxItemDetail.setTotal(taxItemDetail.getNetAmount()
					+ taxItemDetail.getTaxAmount());
			taxItemDetail.setTransactionId((Long) objects[2]);
			taxItemDetail.setTransactionNumber((String) objects[3]);
			taxItemDetail.setTransactionType((Integer) objects[4]);
			taxItemDetail.setTransactionDate(new ClientFinanceDate(
					(Long) objects[5]));

			TAXItem taxItem = (TAXItem) session.get(TAXItem.class,
					(Long) objects[6]);

			taxItemDetail.setTaxItemName(taxItem.getName());
			taxItemDetail.setTAXRate(taxItem.getTaxRate());
			taxItemDetail.setPercentage(true);
			vatItemDetails.add(taxItemDetail);

		}

		// // Entries from the VATAdjustment
		query = session
				.getNamedQuery("getTAXAdjustment.by.dates.and.taxAgency")
				.setParameter("startDate", new FinanceDate(startDate))
				.setParameter("endDate", new FinanceDate(endDate))
				.setParameter("taxAgency", taxAgency)
				.setEntity("company", company);

		List<TAXAdjustment> vatAdjustments = query.list();
		for (TAXAdjustment v : vatAdjustments) {

			TAXItemDetail tid = new TAXItemDetail();

			tid.setTaxAmount(v.getTotal());
			tid.setTaxItemName(v.getTaxItem().getName());
			tid.setTAXRate(v.getTaxItem().getTaxRate());
			tid.setTransactionDate(v.getDate().toClientFinanceDate());
			tid.setTransactionId(v.getID());
			tid.setTransactionNumber(v.getNumber());
			tid.setTransactionType(v.getType());
			vatItemDetails.add(tid);
		}

		return vatItemDetails;
	}

	public ArrayList<TAXItemDetail> getTaxItemDetailByTaxReturnId(long id,
			long companyId) {

		List<ClientTAXReturnEntry> taxEntries = null;
		ArrayList<TAXItemDetail> details = new ArrayList<TAXItemDetail>();
		Session session = HibernateUtil.getCurrentSession();
		try {
			ArrayList<TAXReturn> list = new ArrayList<TAXReturn>(session
					.getNamedQuery("getTaxReturnById").setParameter("id", id)
					.list());
			for (TAXReturn tax : list) {
				ClientTAXReturn obj = new ClientConvertUtil().toClientObject(
						tax, ClientTAXReturn.class);
				taxEntries = obj.getTaxReturnEntries();

				for (ClientTAXReturnEntry c : taxEntries) {
					TAXItemDetail detail = new TAXItemDetail();

					TAXItem taxItem = (TAXItem) session.get(TAXItem.class,
							c.getTaxItem());
					ClientTAXItem clientObj = new ClientConvertUtil()
							.toClientObject(taxItem, ClientTAXItem.class);

					detail.setTaxAmount(c.getTaxAmount());
					detail.setTransactionId(c.getTransaction());
					// detail.setTaxItemName(company.getTAXItem(c.getTaxItem())
					// .getName());

					detail.setTaxItemName(clientObj.getName());
					detail.setTransactionType(c.getTransactionType());
					detail.setTransactionDate(new ClientFinanceDate(c
							.getTransactionDate()));
					detail.setNetAmount(c.getNetAmount());
					// detail.setTAXRate(company.getTaxItem(c.getTaxItem())
					// .getTaxRate());
					detail.setTAXRate(clientObj.getTaxRate());
					detail.setTotal(c.getGrassAmount());
					details.add(detail);
				}
			}

		} catch (Exception e) {
		}
		return details;

	}

	private ArrayList<TAXItemDetail> getData(
			List<ClientTAXReturnEntry> taxEntries, long companyId) {

		Company company = getCompany(companyId);
		ArrayList<TAXItemDetail> details = new ArrayList<TAXItemDetail>();

		return details;
	}

	public ArrayList<VATDetail> getVATExceptionDetailReport(Long companyId,
			ClientFinanceDate start, ClientFinanceDate end, long taxReturnId) {

		ArrayList<VATDetail> vatDetails = new ArrayList<VATDetail>();
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			List<TAXReturn> list = session.getNamedQuery("list.TAXReturns")
					.setEntity("company", company).list();

			ClientConvertUtil convertUttils = new ClientConvertUtil();
			for (TAXReturn taxReturn : list) {
				ClientTAXReturn clientObject = convertUttils.toClientObject(
						taxReturn, ClientTAXReturn.class);
				if (company.getCountry().equals("United Kingdom")
						&& clientObject.getID() == taxReturnId) {
					// clientObject.setBoxes(toBoxes(
					// clientObject.getTaxReturnEntries(),
					// taxReturn.getTaxAgency()));

					List<ClientBox> boxes = clientObject.getBoxes();

					for (ClientBox c : boxes) {

						VATDetail vatDetail = new VATDetail();
						vatDetail.setBoxName(c.getName());
						vatDetail.setBoxNumber(c.getBoxNumber());
						vatDetail.setNetAmount(c.getAmount());
						vatDetails.add(vatDetail);
					}

				}

			}
		} catch (Exception e) {
		}
		return vatDetails;

	}

	public ArrayList<VATDetail> getVATExpectionsForPrint(long companyId,
			long taxAgency, long taxReturnId) throws AccounterException {

		ArrayList<VATDetail> vatDetails = new ArrayList<VATDetail>();
		if (taxReturnId != 0) {
			Session session = HibernateUtil.getCurrentSession();

			TAXReturn taxReturn = (TAXReturn) session.get(TAXReturn.class,
					taxReturnId);

			ClientConvertUtil convertUttils = new ClientConvertUtil();
			ClientTAXReturn clientObject = convertUttils.toClientObject(
					taxReturn, ClientTAXReturn.class);
			clientObject.setBoxes(toBoxes(clientObject.getTaxReturnEntries(),
					taxReturn.getTaxAgency()));

			List<ClientBox> boxes = clientObject.getBoxes();
			for (ClientBox c : boxes) {
				VATDetail vatDetail = new VATDetail();
				vatDetail.setBoxName(c.getName());
				vatDetail.setBoxNumber(c.getBoxNumber());
				vatDetail.setNetAmount(c.getAmount());
				vatDetails.add(vatDetail);
			}
		} else {
			getTAXItemExceptionDetailReport(companyId, taxAgency, 0, 0);
		}
		return vatDetails;

	}

	private ArrayList<TAXItemDetail> getExceptionDetailData(
			List<TAXReturnEntry> taxEntries, FinanceDate financeDate,
			long companyId) {

		ArrayList<TAXItemDetail> details = new ArrayList<TAXItemDetail>();

		for (TAXReturnEntry c : taxEntries) {

			if (c.getTransactionDate().before(financeDate)) {

				TAXItemDetail detail = new TAXItemDetail();
				detail.setTaxAmount(c.getTaxAmount());
				detail.setTransactionId(c.getTransaction().getID());
				TAXItem taxItem = c.getTaxItem();
				detail.setTaxItemName(taxItem.getName());
				detail.setTransactionType(c.getTransactionType());
				detail.setTransactionDate(c.getTransactionDate()
						.toClientFinanceDate());
				detail.setNetAmount(c.getNetAmount());
				detail.setTAXRate(taxItem.getTaxRate());
				detail.setTotal(c.getGrassAmount());
				detail.setFiledTAXAmount(c.getFiledTAXAmount());
				details.add(detail);
			}

		}
		return details;
	}

	public ArrayList<TAXItemDetail> getTAXExceptionsForPrint(long companyId,
			long taxreturnID, long taxAgency) {

		if (taxreturnID == 0) {
			return getTAXItemExceptionDetailReport(companyId, taxAgency, 0, 0);
		} else {
			Session session = HibernateUtil.getCurrentSession();
			ArrayList<TAXItemDetail> details = new ArrayList<TAXItemDetail>();
			TAXReturn tasxReturn = (TAXReturn) session.get(TAXReturn.class,
					taxreturnID);
			List<TAXReturnEntry> taxEntries = tasxReturn.getTaxReturnEntries();
			return getExceptionDetailData(taxEntries,
					tasxReturn.getPeriodStartDate(), companyId);
		}

	}

	public TAXItem getTaxItem(long taxItem, long companyId) {
		Company company = getCompany(companyId);
		Set<TAXItem> taxItems = company.getTaxItems();
		for (TAXItem item : taxItems) {
			if (item.getID() == taxItem) {
				return item;
			}
		}
		return null;

	}

	public ArrayList<TAXItemDetail> getTAXItemExceptionDetailReport(
			long companyId, long taxAgency, long startDate, long endDate) {

		Session session = HibernateUtil.getCurrentSession();

		// Entries from the VATRate calculation

		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.for.TaxReturn.Exception.for.reports")
				.setParameter("taxAgency", taxAgency)
				.setParameter("companyId", companyId);

		List<Object[]> taxReturnEntries = session
				.getNamedQuery(
						"getAllTAXReturnEntries.groupby.transaction.id.for.reports")
				.setParameter("companyId", companyId)
				.setParameter("taxAgency", taxAgency).list();

		List<Object[]> list = query.list();

		ArrayList<TAXItemDetail> resultTAXReturnEntries = new ArrayList<TAXItemDetail>();

		Iterator<Object[]> iterator = list.iterator();
		for (Object[] entry : taxReturnEntries) {
			Long transaction = (Long) entry[0];
			TAXItemDetail newEntry = null;
			while (iterator.hasNext()) {
				Object[] objects = iterator.next();
				double taxAmount = (Double) objects[0];
				double netAmount = (Double) objects[1];
				long transactionID = (Long) objects[2];

				if (transaction != null && transactionID == transaction) {
					newEntry = new TAXItemDetail();
					newEntry.setFiledTAXAmount((Double) entry[5]);
					double amount = taxAmount - (Double) entry[5];
					newEntry.setTaxAmount(amount >= 0 ? amount : -1 * amount);
					newEntry.setNetAmount(netAmount - (Double) entry[6]);
					newEntry.setTotal(newEntry.getTaxAmount()
							+ newEntry.getNetAmount());
					newEntry.setTransactionId((Long) objects[2]);
					newEntry.setTransactionNumber((String) objects[3]);
					newEntry.setTransactionType((Integer) objects[4]);
					newEntry.setTransactionDate(new ClientFinanceDate(
							(Long) objects[5]));
					TAXItem taxItem = (TAXItem) session.get(TAXItem.class,
							(Long) objects[6]);
					newEntry.setTaxItemName(taxItem.getName());
					newEntry.setTAXRate((Double) objects[7]);
					newEntry.setPercentage(true);
					resultTAXReturnEntries.add(newEntry);
					iterator.remove();
				}
			}
			if (newEntry == null && (transaction == null || (Boolean) entry[4])
					&& (Double) entry[5] != 0) {
				newEntry = new TAXItemDetail();
				newEntry.setFiledTAXAmount((Double) entry[5]);
				newEntry.setTaxAmount(0);
				newEntry.setNetAmount((Double) entry[6]);
				newEntry.setTotal(newEntry.getNetAmount()
						+ newEntry.getTaxAmount());
				Object object = entry[0];
				if (object != null)
					newEntry.setTransactionId((Long) object);
				newEntry.setTransactionNumber((String) entry[1]);
				newEntry.setTransactionType((Integer) entry[2]);
				newEntry.setTransactionDate(new ClientFinanceDate(
						(Long) entry[3]));
				newEntry.setTaxItemName((String) entry[8]);
				newEntry.setTAXRate((Double) entry[9]);
				newEntry.setPercentage(true);
				resultTAXReturnEntries.add(newEntry);
			}
		}

		for (Object[] objects : list) {
			TAXItemDetail vi = new TAXItemDetail();
			vi.setTaxAmount((Double) objects[0]);
			vi.setNetAmount((Double) objects[1]);
			vi.setTotal(vi.getNetAmount() + vi.getTaxAmount());
			vi.setTransactionId((Long) objects[2]);
			vi.setTransactionNumber((String) objects[3]);
			vi.setTransactionType((Integer) objects[4]);
			vi.setTransactionDate(new ClientFinanceDate((Long) objects[5]));
			TAXItem taxItem = (TAXItem) session.get(TAXItem.class,
					(Long) objects[6]);
			vi.setTaxItemName(taxItem.getName());
			vi.setTAXRate((Double) objects[7]);
			vi.setPercentage(true);
			resultTAXReturnEntries.add(vi);
		}

		// // Entries from the VATAdjustment
		// query = session
		// .getNamedQuery("getTAXAdjustment.by.dates.and.taxItemName")
		// .setParameter("startDate", startDate)
		// .setParameter("endDate", endDate)
		// .setParameter("taxItemName", taxItemName)
		// .setEntity("company", company);
		//
		// List<TAXAdjustment> vatAdjustments = query.list();
		// for (TAXAdjustment v : vatAdjustments) {
		//
		// VATItemDetail vi = new VATItemDetail();
		//
		// if (v.getIncreaseVATLine()) {
		// vi.setAmount(v.getJournalEntry().getTotal());
		// } else {
		// vi.setAmount(-1 * v.getJournalEntry().getTotal());
		// }
		// vi.setDate(new ClientFinanceDate(v.getJournalEntry().getDate()
		// .getDate()));
		// vi.setMemo("VAT Adjustment");
		// vi.setName(v.getTaxItem().getTaxAgency().getName());
		// vi.setTransactionId(v.getJournalEntry().getID());
		// vi.setTransactionNumber(v.getJournalEntry().getNumber());
		// vi.setTransactionType(v.getJournalEntry().getType());
		// vatItemDetails.add(vi);
		// }

		return resultTAXReturnEntries;
	}

	public ArrayList<TDSAcknowledgmentsReport> getTDSAcknowledgments(
			FinanceDate startDate, FinanceDate endDate, long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		List list = session.getNamedQuery("get.TDS.Filed.Records")
				.setLong("companyId", companyId)
				.setLong("startDate", startDate.getDate())
				.setLong("endDate", endDate.getDate()).list();

		Object[] object = null;
		Iterator iterator = list.iterator();
		Map<String, TDSAcknowledgmentsReport> map = new HashMap<String, TDSAcknowledgmentsReport>();
		while ((iterator).hasNext()) {

			TDSAcknowledgmentsReport t = new TDSAcknowledgmentsReport();
			object = (Object[]) iterator.next();

			t.setAckNo(object[0] == null ? "" : (String) object[0]);
			t.setFormType(object[1] == null ? 0 : ((Integer) object[1])
					.intValue());
			t.setQuater(object[2] == null ? 0 : ((Integer) object[2])
					.intValue());
			t.setFinancialYearStart(object[3] == null ? 0
					: ((Integer) object[3]).intValue());
			t.setFinancialYearEnd(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());
			t.setDate(object[5] == null ? 0 : ((Long) object[5]).longValue());

			if (!map.containsKey(t.getAckNo())) {
				map.put(t.getAckNo(), t);
			}
		}
		return new ArrayList<TDSAcknowledgmentsReport>(map.values());
	}

	public ArrayList<BudgetActuals> getBudgetvsAcualReportData(
			FinanceDate startDate, FinanceDate endDate, long companyId,
			long id, int type) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate1 = getCurrentFiscalYearStartDate(company);

		/*
		 * Here endDate1 is used to store the previous month of endDate value
		 */
		int year = endDate.getYear();
		int month = endDate.getMonth() - 1;
		year = (month == 0) ? year - 1 : year;
		month = (month == 0) ? 12 : month;
		FinanceDate endDate1 = new FinanceDate(year, month, 31);

		if (year != startDate1.getYear())
			startDate1 = new FinanceDate(year, 01, 01);
		// + ((month + "").length() == 1 ? "0" + month : month) + "01");

		List l = session.getNamedQuery("getProfitAndLoss")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("startDate1", startDate1.getDate())
				.setParameter("endDate1", endDate1.getDate()).list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();
		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {

			t.setAccountId(((Long) object[0]).longValue());
			t.setAccountName((String) object[1]);
			t.setAccountNumber((String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());
			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((Long) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}
			t.setAmount(object[6] == null ? 0 : ((Double) object[6])
					.doubleValue());
			t.setTotalAmount(object[7] == null ? 0 : ((Double) object[7])
					.doubleValue());
			t.setAccountFlow((String) object[8]);
			t.setBaseType(object[9] == null ? 0 : (Integer) object[9]);
			t.setSubBaseType(object[10] == null ? 0 : (Integer) object[10]);
			t.setGroupType(object[11] == null ? 0 : (Integer) object[11]);

			queryResult.add(t);

		}
		List<TrialBalance> sortedResult = new ArrayList<TrialBalance>();
		List<TrialBalance> otherExpenseList = new ArrayList<TrialBalance>();

		sortedResult = sortTheList(queryResult);
		int index = 0;
		Iterator iter = sortedResult.listIterator();
		while (iter.hasNext()) {
			TrialBalance tb = (TrialBalance) iter.next();
			if (tb.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				index = sortedResult.indexOf(tb);
			if (tb.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {
				otherExpenseList.add(tb);
				iter.remove();
			}
		}

		ArrayList<Budget> budgetList = new ArrayList<Budget>(session
				.getNamedQuery("list.Budget")
				.setEntity("company", getCompany(companyId)).list());

		PaginationList<ClientBudget> clientBudgetObjs = new PaginationList<ClientBudget>();

		for (Budget budget : budgetList) {
			ClientBudget clientObject = new ClientConvertUtil().toClientObject(
					budget, ClientBudget.class);

			clientBudgetObjs.add(clientObject);
		}

		ArrayList<BudgetActuals> actualList = new ArrayList<BudgetActuals>();
		for (TrialBalance bal : queryResult) {

			BudgetActuals actual = new BudgetActuals();
			actual.setAccountName(bal.getAccountName());
			actual.setAtualAmount(bal.getTotalAmount());
			actual.setType(bal.getAccountType());

			boolean got = false;

			for (Budget budget : budgetList) {
				if (budget.getID() == id) {
					List<BudgetItem> budgetItems = budget.getBudgetItems();
					for (BudgetItem budgetItem : budgetItems) {
						if (bal.getAccountId() == budgetItem.getAccount()
								.getID()) {
							actual.setBudgetAmount(budgetItem.getTotalAmount());
							actualList.add(actual);
							got = true;
							break;
						} else {

						}
					}
					if (got == false) {
						actual.setBudgetAmount(0.00);
						if (type == 0) {
							actualList.add(actual);
						}
					}
				}
			}

		}
		return actualList;
	}

	public ArrayList<RealisedExchangeLossOrGain> getRealisedExchangeLossesOrGains(
			long companyId, long startDate, long endDate) {
		Session session = HibernateUtil.getCurrentSession();

		List list = session.getNamedQuery("getRealisedExchangeLossesOrGains")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate).list();

		Iterator iterator = list.iterator();
		ArrayList<RealisedExchangeLossOrGain> result = new ArrayList<RealisedExchangeLossOrGain>();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			RealisedExchangeLossOrGain record = new RealisedExchangeLossOrGain();
			record.setTransaction(objects[0] != null ? (Long) objects[0] : 0);
			record.setTransactionType(objects[1] != null ? (Integer) objects[1]
					: 0);
			record.setTransactionDate(objects[2] != null ? new ClientFinanceDate(
					(Long) objects[2]) : null);
			record.setPayeeName(objects[3] != null ? (String) objects[3] : "");
			record.setCurrency(objects[4] != null ? (String) objects[4] : "");
			record.setExchangeRate(objects[5] != null ? (Double) objects[5]
					: 0.00D);
			record.setRealisedLossOrGain(objects[6] != null ? -(Double) objects[6]
					: 0.00D);
			result.add(record);
		}

		return result;
	}

	public ArrayList<UnRealisedLossOrGain> getUnRealisedExchangeLossesAndGains(
			Long enteredDate, long companyId, Map<Long, Double> exchangeRates) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<UnRealisedLossOrGain> list = new ArrayList<UnRealisedLossOrGain>();
		List result = session.getNamedQuery("getUnrealisedExchangeLossOrGain")
				.setParameter("companyId", companyId)
				.setParameter("enteredDate", enteredDate)
				.setParameterList("currencies", exchangeRates.keySet()).list();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			UnRealisedLossOrGain lossOrGain = new UnRealisedLossOrGain();
			lossOrGain.setAccountName((String) objects[0]);
			lossOrGain.setCurrency((String) objects[1]);
			double foreignBalance = (Double) objects[2];
			lossOrGain.setForeignBalance(foreignBalance);
			lossOrGain.setExchangeRate(exchangeRates.get(objects[4]));
			lossOrGain
					.setCurrentBalance(objects[3] != null ? (Double) objects[3]
							: 0);
			double adjustedBalance = foreignBalance
					* lossOrGain.getExchangeRate();
			lossOrGain.setAdjustedBalance(adjustedBalance);
			lossOrGain.setLossOrGain(adjustedBalance - foreignBalance);
			list.add(lossOrGain);
		}
		return list;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param companyId
	 * @return
	 */
	public ArrayList<EstimatesByJob> getEstimatesByJob(FinanceDate startDate,
			FinanceDate endDate, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<EstimatesByJob> estimatesByJobs = new ArrayList<EstimatesByJob>();
		Query query = session.getNamedQuery("getEstimatesByJobs")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List list = query.list();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			EstimatesByJob estimatesByJob = new EstimatesByJob();
			estimatesByJob.setCustomerName((String) objects[0]);
			estimatesByJob.setEstimateId((Long) objects[1]);
			estimatesByJob.setJobName((String) objects[2]);
			estimatesByJob.setJobId((Long) objects[3]);
			estimatesByJob.setTransactionType((Integer) objects[4]);
			estimatesByJob.setEstimateDate(new ClientFinanceDate(
					(Long) objects[5]));
			estimatesByJob.setNum((String) objects[6]);
			estimatesByJob.setMemo((String) objects[7]);
			estimatesByJob.setAmount((Double) objects[8]);
			estimatesByJob.setEstimateType((Integer) objects[9]);
			estimatesByJobs.add(estimatesByJob);
		}
		return estimatesByJobs;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param companyId
	 * @param isActualcostDetail
	 * @return
	 */
	public ArrayList<JobActualCostDetail> getJobActualCostOrRevenueDetails(
			FinanceDate startDate, FinanceDate endDate, long companyId,
			boolean isActualcostDetail, long customerId, long jobId) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<JobActualCostDetail> jobActualCostDetails = new ArrayList<JobActualCostDetail>();
		Query query = null;
		if (isActualcostDetail) {
			query = session.getNamedQuery("getJobActualCostTransactions")
					.setParameter("companyId", companyId)
					.setParameter("customerId", customerId)
					.setParameter("jobId", jobId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate());
		} else {
			query = session.getNamedQuery("getJobActualRevenueTransactions")
					.setParameter("companyId", companyId)
					.setParameter("customerId", customerId)
					.setParameter("jobId", jobId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate());
		}
		List list = query.list();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			JobActualCostDetail costDetail = new JobActualCostDetail();
			Company company = (Company) session.get(Company.class, companyId);
			Account accountsReceivableAccount = company.getPrimaryCurrency()
					.getAccountsReceivable();
			costDetail.setSplitAccountName(accountsReceivableAccount.getName());
			costDetail.setCustomerName((String) objects[0]);
			costDetail.setJobName((String) objects[1]);
			costDetail.setType(((Integer) objects[2]).intValue());
			costDetail.setTransactionDate(new ClientFinanceDate(
					((Long) objects[3]).longValue()));
			costDetail.setNumber((String) objects[4]);
			costDetail.setMemo((String) objects[5]);
			costDetail.setAccountName(((String) objects[6]));
			costDetail.setTransaction(((Long) objects[7]).longValue());
			costDetail.setTotal(((Double) objects[8]).doubleValue());

			jobActualCostDetails.add(costDetail);

		}
		return jobActualCostDetails;
	}

	public ArrayList<BankDepositDetail> getBankDepositDetails(Long companyId,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<BankDepositDetail> list = new ArrayList<BankDepositDetail>();
		List result = session.getNamedQuery("getBankDepositDetails")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate()).list();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {

			Object[] objects = (Object[]) iterator.next();
			BankDepositDetail depositDetail = new BankDepositDetail();
			depositDetail.setTransactionId((Long) objects[0]);
			depositDetail.setTransactionType((Integer) objects[1]);
			depositDetail.setNumber((String) objects[2]);
			depositDetail
					.setTransactionDate(objects[3] != null ? new ClientFinanceDate(
							(Long) objects[3]) : null);
			depositDetail.setPayeeName(objects[4] != null ? (String) objects[4]
					: "");
			depositDetail.setAccountName((String) objects[5]);
			depositDetail.setAmount((Double) objects[6]);
			list.add(depositDetail);
		}

		return list;
	}

	/**
	 * 
	 * @param companyId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ArrayList<UnbilledCostsByJob> getUnBilledCostsByJobReport(
			long companyId, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<UnbilledCostsByJob> list = new ArrayList<UnbilledCostsByJob>();
		List result = session.getNamedQuery("getUnBilledCostsByJob")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate()).list();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			UnbilledCostsByJob costsByJob = new UnbilledCostsByJob();
			costsByJob.setTransaction((Long) objects[0]);
			costsByJob.setAmount((Double) objects[1]);
			costsByJob.setJobId((Long) objects[2]);
			costsByJob.setType((Integer) objects[3]);
			costsByJob.setMemo((String) objects[4]);
			costsByJob.setCustomerName((String) objects[5]);
			costsByJob.setTransactionDate(new ClientFinanceDate(
					(Long) objects[6]));
			costsByJob.setAccountName((String) objects[7]);
			costsByJob.setCustomerId((Long) objects[8]);
			costsByJob.setJobName((String) objects[9]);
			list.add(costsByJob);
		}
		return list;
	}

	public ArrayList<BankCheckDetail> getBankCheckDetails(Long companyId,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<BankCheckDetail> list = new ArrayList<BankCheckDetail>();
		List result = session
				.getNamedQuery("getBankCheckDetails")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("paymentMethodCheque",
						Global.get().messages().cheque(),
						EncryptedStringType.INSTANCE)
				.setParameter("paymentMethodCheck",
						Global.get().messages().check(),
						EncryptedStringType.INSTANCE).list();

		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {

			Object[] objects = (Object[]) iterator.next();
			BankCheckDetail checkDetail = new BankCheckDetail();
			checkDetail.setTransactionId((Long) objects[0]);
			checkDetail.setTransactionType((Integer) objects[1]);
			checkDetail.setTransactionNumber((String) objects[2]);
			checkDetail
					.setTransactionDate(objects[3] != null ? new ClientFinanceDate(
							(Long) objects[3]) : null);
			checkDetail.setPayeeName((String) objects[4]);
			checkDetail.setCheckAmount((Double) objects[5]);
			String checkNumber = objects[6] != null ? (String) objects[6] : "";
			checkDetail.setCheckNumber(checkNumber);
			list.add(checkDetail);
		}
		return list;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param companyId
	 * @param itemId
	 * @return
	 */
	public ArrayList<ItemActualCostDetail> getItemActualCostOrRevenueDetails(
			FinanceDate startDate, FinanceDate endDate, long companyId,
			long itemId, long customerId, long jobId, boolean isActualcostDetail) {

		Session session = HibernateUtil.getCurrentSession();

		List list;
		if (isActualcostDetail) {
			list = session.getNamedQuery("getItemActualRevenueDetails")
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())
					.setParameter("companyId", companyId)
					.setParameter("itemId", itemId)
					.setParameter("customerId", customerId)
					.setParameter("jobId", jobId).list();
		} else {
			list = session.getNamedQuery("getItemActualCostsDetails")
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())
					.setParameter("companyId", companyId)
					.setParameter("itemId", itemId)
					.setParameter("customerId", customerId)
					.setParameter("jobId", jobId).list();
		}
		ArrayList<ItemActualCostDetail> querylist = new ArrayList<ItemActualCostDetail>();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			ItemActualCostDetail actualCostDetail = new ItemActualCostDetail();
			actualCostDetail.setTransactionId((Long) objects[0]);
			actualCostDetail.setItemName((String) objects[1]);
			actualCostDetail.setItemType((Integer) objects[2]);
			actualCostDetail.setDate(new ClientFinanceDate((Long) objects[3]));
			actualCostDetail.setNumber((String) objects[4]);
			actualCostDetail.setCustomerName((String) objects[5]);
			actualCostDetail.setQuantity(String.valueOf(objects[6]));
			actualCostDetail.setMemo((String) objects[7]);
			actualCostDetail.setAmount((Double) objects[8]);
			actualCostDetail.setTransationType((Integer) objects[9]);
			querylist.add(actualCostDetail);
		}
		return querylist;
	}

	public ArrayList<JobProfitability> getJobProfitabilitySummaryReport(
			Long companyId, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<JobProfitability> list = new ArrayList<JobProfitability>();
		List result = session.getNamedQuery("getJobProfitabilitySummary")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate()).list();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			JobProfitability job = new JobProfitability();
			job.setJobId((Long) objects[0]);
			job.setCustomerId((Long) objects[1]);
			job.setCustomerName((String) objects[2]);
			job.setName((String) objects[3]);
			job.setCostAmount((Double) (objects[4] == null ? 0.0 : objects[4]));
			job.setRevenueAmount((Double) (objects[5] == null ? 0.0
					: objects[5]));
			list.add(job);
		}
		return list;
	}

	public ArrayList<JobProfitabilityDetailByJob> getJobProfitabilityDetailByJobReport(
			long payeeId, long jobId, Long companyId,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<JobProfitabilityDetailByJob> list = new ArrayList<JobProfitabilityDetailByJob>();
		List result = session.getNamedQuery("JobProfitabilityDetailByJob")
				.setParameter("payeeId", payeeId).setParameter("jobId", jobId)
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate()).list();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			JobProfitabilityDetailByJob job = new JobProfitabilityDetailByJob();
			job.setItemId((Long) objects[0]);
			job.setItemType((Long) objects[1]);
			job.setItemName((String) objects[2]);
			job.setCostAmount((Double) (objects[3] == null ? 0.0 : objects[3]));
			job.setRevenueAmount((Double) (objects[4] == null ? 0.0
					: objects[4]));
			job.setCustomerId((Long) objects[5]);
			job.setJobId((Long) objects[6]);
			list.add(job);
		}
		return list;
	}

	/**
	 * Getting the Missing checks Details
	 * 
	 * @param accountId
	 * @param start
	 * @param end
	 * @param companyId
	 * @return
	 * @throws AccounterException
	 */
	public ArrayList<TransactionDetailByAccount> getMissionChecksByAccount(
			long accountId, FinanceDate start, FinanceDate end, long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<TransactionDetailByAccount> list = new ArrayList<TransactionDetailByAccount>();

		Account account = (Account) session.get(Account.class, accountId);
		if (account == null) {
			throw new AccounterException(Global.get().messages()
					.pleaseSelect(Global.get().messages().account()));
		}
		List result = new ArrayList();
		if (account.getType() == Account.TYPE_OTHER_CURRENT_ASSET) {
			result = session
					.getNamedQuery("get.all.invoices.by.account")
					.setParameter("startDate", start.getDate())
					.setParameter("endDate", end.getDate())
					.setParameter("companyId", companyId)
					.setParameter("accountId", accountId)
					.setParameter("tobePrint", "TO BE PRINTED",
							EncryptedStringType.INSTANCE)
					.setParameter("empty", "", EncryptedStringType.INSTANCE)
					.list();
		} else if (account.getType() == ClientAccount.TYPE_BANK) {
			result = session
					.getNamedQuery("get.missing.checks.by.account")
					.setParameter("accountId", accountId)
					.setParameter("startDate", start.getDate())
					.setParameter("endDate", end.getDate())
					.setParameter("companyId", companyId)
					.setParameter("tobePrint", "TO BE PRINTED",
							EncryptedStringType.INSTANCE)
					.setParameter("empty", "", EncryptedStringType.INSTANCE)
					.list();
		}
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();
			TransactionDetailByAccount detailByAccount = new TransactionDetailByAccount();
			detailByAccount
					.setTransactionId((Long) (objects[0] != null ? objects[0]
							: 0));
			detailByAccount
					.setTransactionType((Integer) (objects[1] != null ? objects[1]
							: 0));
			detailByAccount
					.setTransactionNumber((String) (objects[2] != null ? objects[2]
							: ""));
			ClientFinanceDate date = new ClientFinanceDate(
					(Long) (objects[3] != null ? objects[3] : 0));
			detailByAccount.setTransactionDate(date);
			detailByAccount.setName((String) (objects[4] != null ? objects[4]
					: ""));
			detailByAccount
					.setAccountName((String) (objects[5] != null ? objects[5]
							: ""));
			detailByAccount.setMemo((String) (objects[6] != null ? objects[6]
					: ""));
			detailByAccount.setTotal((Double) (objects[7] != null ? objects[7]
					: 0.0));
			list.add(detailByAccount);

		}
		return list;
	}

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccountAndCategory(
			int categoryType, long categoryId, long accountId,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session
				.getNamedQuery(
						"getTransactionDetailByAccount_ForParticularAccountAndCategory")
				.setParameter("companyId", companyId)
				.setParameter("categoryId", categoryId)
				.setParameter("categoryType", categoryType)
				.setParameter("accountID", accountId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List<TransactionDetailByAccount> transactionDetailByAccountList = new ArrayList<TransactionDetailByAccount>();
		List list = query.list();

		if (list != null && list.size() > 0) {
			try {
				createTransasctionDetailByAccount(list,
						transactionDetailByAccountList);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}

		if (transactionDetailByAccountList != null) {
			return new ArrayList<TransactionDetailByAccount>(
					transactionDetailByAccountList);
		}
		return new ArrayList<TransactionDetailByAccount>();

	}

	/**
	 * Getting the Reconciliation DisCrepany Details
	 * 
	 * @param accountId
	 * @param start
	 * @param end
	 * @param companyId
	 * @return
	 */
	public ArrayList<ReconciliationDiscrepancy> getReconciliationDiscrepancyByAccount(
			long accountId, ClientFinanceDate start, ClientFinanceDate end,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<ReconciliationDiscrepancy> list = new ArrayList<ReconciliationDiscrepancy>();
		List result = session
				.getNamedQuery("get.reconcilition.discrepancy.by.account")
				.setParameter("companyId", companyId)
				.setParameter("startDate", start.getDate())
				.setParameter("enteredDate", end.getDate())
				.setParameter("accountId", accountId).list();

		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			Object[] objects = (Object[]) iterator.next();

			ReconciliationDiscrepancy discrepancy = new ReconciliationDiscrepancy();
			discrepancy.setTransactionType((Integer) objects[0]);
			discrepancy
					.setTransactionId((Long) (objects[1] != null ? objects[1]
							: 0));
			ClientFinanceDate date = new ClientFinanceDate(
					(Long) (objects[2] != null ? objects[2] : 0));
			discrepancy.setTransactionDate(date);

			discrepancy
					.setTransactionNumber((String) (objects[3] != null ? objects[3]
							: ""));
			discrepancy.setTransactionAmount((Double) objects[4]);
			discrepancy.setReconciliedAmount((Double) objects[5]);
			discrepancy
					.setName((String) (objects[6] != null ? objects[6] : ""));

			list.add(discrepancy);
		}
		return list;
	}

	// public ArrayList<JobProfitability> getJobProfitabilityReport(
	// Long companyId, ClientFinanceDate start, ClientFinanceDate end) {
	// return null;
	// }

	public ArrayList<InventoryDetails> getInventoryDetails(
			FinanceDate startDate, FinanceDate endDate, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<InventoryDetails> list = new ArrayList<InventoryDetails>();
		List result = session.getNamedQuery("getInventoryDetails")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate()).list();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			Object[] object = (Object[]) iterator.next();
			InventoryDetails record = new InventoryDetails();
			record.setId(((Long) object[0]).longValue());
			record.setItemName((String) (object[1] != null ? object[1] : ""));
			record.setCostValuation((Double) (object[2] != null ? object[2] : 0));
			double qtyIn = (Double) (object[3] != null ? object[3] : 0);
			record.setQtyIn(qtyIn);
			double qtyOut = (Double) (object[4] != null ? object[4] : 0);
			record.setQtyOut(qtyOut);
			record.setCost((Double) (object[5] != null ? object[5] : 0));
			record.setPricesold((Double) (object[6] != null ? object[6] : 0));
			record.setOnHandqty(qtyIn - qtyOut);
			list.add(record);
		}
		return list;
	}

	/**
	 * Getting the Customer Income Details
	 * 
	 * @param startDate
	 * @param endDate
	 * @param companyId
	 * @return
	 */
	public ArrayList<IncomeByCustomerDetail> getIncomeByCustomerDetails(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		ArrayList<IncomeByCustomerDetail> customerDetails = new ArrayList<IncomeByCustomerDetail>();
		Query result = session.getNamedQuery("getIncomeByCustomerDetails")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List list = result.list();
		Iterator iterator = list.iterator();
		if (list != null) {
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				IncomeByCustomerDetail customerDetail = new IncomeByCustomerDetail();
				customerDetail
						.setId((Long) (object[0] != null ? object[0] : 0));
				ClientFinanceDate date = new ClientFinanceDate(
						(Long) (object[1] != null ? object[1] : 0));
				customerDetail.setTransactionDate(date);
				customerDetail.setTransactionType((Integer) object[2]);
				customerDetail
						.setTransactionNumber((String) (object[3] != null ? object[3]
								: ""));
				customerDetail.setName((String) (object[5] != null ? object[5]
						: ""));
				customerDetail
						.setJobName((String) (object[6] != null ? object[6]
								: ""));
				customerDetail
						.setAccountName((String) (object[7] != null ? object[7]
								: ""));
				customerDetail
						.setCredit((Double) (object[9] != null ? object[9]
								: 0.0d));
				customerDetail.setDebit((Double) (object[8] != null ? object[8]
						: 0.0d));
				customerDetails.add(customerDetail);
			}
		}
		return customerDetails;
	}
}
