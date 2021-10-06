package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.ReportResultRequirement;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.web.client.core.reports.AgedDebtors;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.server.FinanceTool;

public class APAgingDetailReportCommand extends
		NewAbstractReportCommand<AgedDebtors> {

	protected static final int LESS_THAN_30 = 1;
	protected static final int BETWEEN_30_60 = 2;
	protected static final int BETWEEN_60_90 = 3;
	protected static final int MORE_THAN_90 = 4;
	private String vendorName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ReportResultRequirement<AgedDebtors>() {

			@Override
			protected String onSelection(AgedDebtors selection, String name) {
				markDone();
				return "updateTransaction " + selection.getTransactionId();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<AgedDebtors> records = getRecords(context);
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}
				Map<String, List<AgedDebtors>> recordGroups = new HashMap<String, List<AgedDebtors>>();
				for (AgedDebtors agedDebtors : records) {
					String agedDebitorName = getCategoryName(agedDebtors
							.getCategory());
					List<AgedDebtors> group = recordGroups.get(agedDebitorName);
					if (group == null) {
						group = new ArrayList<AgedDebtors>();
						recordGroups.put(agedDebitorName, group);
					}
					group.add(agedDebtors);
				}

				Map<String, Double> customerBalance = new HashMap<String, Double>();
				for (String name : recordGroups.keySet()) {
					List<AgedDebtors> lessThan30 = recordGroups.get(name);
					addResultList(makeResult, lessThan30, customerBalance, name);
				}

				Set<String> keySet = customerBalance.keySet();
				List<String> customerNames = new ArrayList<String>(keySet);
				Collections.sort(customerNames);

				ResultList list = new ResultList(getMessages().totalBalance());
				list.setTitle(getMessages().totalBalance());

				Double total = 0.0;
				for (String name : customerNames) {
					Double vendorAmount = customerBalance.get(name);
					total += vendorAmount;
					Record record = new Record(name);
					record.add("", name);
					record.add("", getMessages().openBalance());
					record.add("", getAmountWithCurrency(vendorAmount));
					list.add(record);
				}
				if (vendorName.equals(""))
					makeResult.add(list);
				makeResult.add("Total :" + getAmountWithCurrency(total));
			}

			private void addResultList(Result makeResult,
					List<AgedDebtors> records,
					Map<String, Double> customerBalance, String string) {
				ResultList list = new ResultList(string);
				list.setTitle(string);
				addSelection(string);
				double total = 0.0;
				for (AgedDebtors record : records) {
					Record createReportRecord = createReportRecord(record);
					total += record.getTotal();
					list.add(createReportRecord);
					String name = record.getName();
					Double balance = customerBalance.get(name);
					if (balance == null) {
						balance = 0d;
					}
					balance += record.getTotal();
					customerBalance.put(name, balance);
				}
				makeResult.add(list);
				makeResult.add(string + " total "
						+ getAmountWithCurrency(total));
			}

			/**
			 * get category Name By ID
			 * 
			 * @param category
			 * @return
			 */
			private String getCategoryName(int category) {
				String categoryName = null;
				switch (category) {
				case LESS_THAN_30:
					categoryName = "0-30";
					break;
				case BETWEEN_30_60:
					categoryName = "30-60";
					break;
				case BETWEEN_60_90:
					categoryName = "60-90";
					break;
				case MORE_THAN_90:
					categoryName = "MoreThan90";
					break;
				}
				return categoryName;
			}

		});
	}

	@Override
	public String getId() {
		return null;
	}

	/**
	 * 
	 * @param recordReport
	 * @return
	 */
	protected Record createReportRecord(AgedDebtors recordReport) {
		Record record = new Record(recordReport);
		record.add(getMessages().name(), recordReport.getName());
		record.add(getMessages().date(),
				getDateByCompanyType(recordReport.getDate()));
		record.add(getMessages().transactionType(),
				ReportUtility.getTransactionName(recordReport.getType()));
		record.add(getMessages().total(),
				getAmountWithCurrency(recordReport.getTotal()));
		return record;
	}

	/***
	 * get AP Aging details records
	 * 
	 * @return {@link List<AgedDebtors>}
	 */

	protected List<AgedDebtors> getRecords(Context context) {
		FinanceDate startOfFiscalYear = context.getCompany().getPreferences()
				.getStartOfFiscalYear();
		FinanceDate end = new FinanceDate();
		ArrayList<AgedDebtors> apAgingDetailsReport = new ArrayList<AgedDebtors>();
		ArrayList<AgedDebtors> apAgingDetailsReportForVendor = new ArrayList<AgedDebtors>();
		try {
			if (vendorName == null) {
				apAgingDetailsReport = new FinanceTool().getReportManager()
						.getAgedCreditors(startOfFiscalYear, end,
								getCompanyId());
			} else if (vendorName != null) {
				apAgingDetailsReport = new FinanceTool().getReportManager()
						.getAgedCreditors(startOfFiscalYear, end,
								getCompanyId());
				for (AgedDebtors agDebitor : apAgingDetailsReport) {
					if (vendorName.equals(agDebitor.getName())) {
						apAgingDetailsReportForVendor.add(agDebitor);
					}
				}
			}
			if (apAgingDetailsReportForVendor != null
					&& !apAgingDetailsReportForVendor.isEmpty()) {
				return apAgingDetailsReportForVendor;
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return apAgingDetailsReport;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		vendorName = context.getString();
		return null;
	}
}