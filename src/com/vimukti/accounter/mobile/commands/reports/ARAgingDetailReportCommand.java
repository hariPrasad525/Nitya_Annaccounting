package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.server.FinanceTool;

public class ARAgingDetailReportCommand extends
		NewAbstractReportCommand<AgedDebtors> {
	protected static final int LESS_THAN_30 = 1;
	protected static final int BETWEEN_30_60 = 2;
	protected static final int BETWEEN_60_90 = 3;
	protected static final int MORE_THAN_90 = 4;
	private String customerName;

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

				Map<String, Double> vendrBalance = new HashMap<String, Double>();
				for (String name : recordGroups.keySet()) {
					List<AgedDebtors> lessThan30 = recordGroups.get(name);
					addResultList(makeResult, lessThan30, vendrBalance, name);
				}

				Set<String> keySet = vendrBalance.keySet();
				List<String> vendorNames = new ArrayList<String>(keySet);
				Collections.sort(vendorNames);

				ResultList list = new ResultList("totalBalance");
				list.setTitle("Total Balance");

				Double total = 0.0;
				for (String name : vendorNames) {
					Double vendorAmount = vendrBalance.get(name);
					total += vendorAmount;
					Record record = new Record(name);
					record.add("Name",name);
					record.add(getMessages().openingBalance(), getAmountWithCurrency(vendorAmount));
					list.add(record);
				}
				if (customerName.equals(""))
					makeResult.add(list);
				makeResult.add("Total :" + getAmountWithCurrency(total));
			}

			private void addResultList(Result makeResult,
					List<AgedDebtors> records,
					Map<String, Double> vendrBalance, String string) {
				ResultList list = new ResultList(string);
				list.setTitle(string);
				addSelection(string);
				double total = 0.0;
				for (AgedDebtors record : records) {
					Record createReportRecord = createReportRecord(record);
					total += record.getTotal();
					list.add(createReportRecord);
					String name = record.getName();
					Double balance = vendrBalance.get(name);
					if (balance == null) {
						balance = 0d;
					}
					balance += record.getTotal();
					vendrBalance.put(name, balance);
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

	protected Record createReportRecord(AgedDebtors record) {
		Record agingRecord = new Record(record);
		agingRecord.add(getMessages().name(), record.getName());
		agingRecord.add(getMessages().date(),
				getDateByCompanyType(record.getDate()));
		agingRecord.add(getMessages().type(),
				Utility.getTransactionName(record.getType()));
		agingRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getTotal()));
		return agingRecord;
	}

	@Override
	public String getId() {
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return {@link List<AgedDebtors>}
	 */
	private List<AgedDebtors> getRecords(Context context) {
		FinanceDate startOfFiscalYear = context.getCompany().getPreferences()
				.getStartOfFiscalYear();
		FinanceDate end = new FinanceDate();
		ArrayList<AgedDebtors> debitors = new ArrayList<AgedDebtors>();
		ArrayList<AgedDebtors> agedCreditorsListForCustomer = new ArrayList<AgedDebtors>();
		try {
			if (customerName == null) {
				debitors = new FinanceTool().getReportManager().getAgedDebtors(
						startOfFiscalYear, end, getCompanyId());
			} else if (customerName != null) {
				debitors = new FinanceTool().getReportManager().getAgedDebtors(
						getStartDate(), getEndDate(), getCompanyId());
				for (AgedDebtors agDebitor : debitors) {
					if (customerName.equals(agDebitor.getName())) {
						agedCreditorsListForCustomer.add(agDebitor);
					}
				}
			}
			if (agedCreditorsListForCustomer != null
					&& !agedCreditorsListForCustomer.isEmpty()) {
				return agedCreditorsListForCustomer;
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return debitors;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		customerName = context.getString();
		endDate = new ClientFinanceDate();
		return null;
	}

}
