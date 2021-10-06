package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.core.Utility;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.ReportResultRequirement;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.server.FinanceTool;

public class CustomerTransactionHistoryReportCommand extends
		NewAbstractReportCommand<TransactionHistory> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<TransactionHistory>() {

			@Override
			protected String onSelection(TransactionHistory selection,
					String name) {
				markDone();
				return "updateTransaction " + selection.getTransactionId();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TransactionHistory> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<TransactionHistory>> recordGroups = new HashMap<String, List<TransactionHistory>>();
				for (TransactionHistory transactionHistoryRecord : records) {
					String customerName = transactionHistoryRecord.getName();
					List<TransactionHistory> group = recordGroups
							.get(customerName);
					if (group == null) {
						group = new ArrayList<TransactionHistory>();
						recordGroups.put(customerName, group);
					}
					group.add(transactionHistoryRecord);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> transactions = new ArrayList<String>(keySet);
				Collections.sort(transactions);

				for (String tax : transactions) {
					List<TransactionHistory> group = recordGroups.get(tax);
					addSelection(tax);
					ResultList resultList = new ResultList(tax);
					for (TransactionHistory rec : group) {
						resultList.setTitle(rec.getName());
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
				}
			}
		});
	}

	/**
	 * 
	 * @param record
	 * @return
	 */
	private Record createReportRecord(TransactionHistory record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add(getMessages().date(),
				getDateByCompanyType(record.getDate()));
		transactionRecord.add(getMessages().transactionType(),
				Utility.getTransactionName(record.getType()));
		transactionRecord.add(getMessages().account(), record.getAccount());
		transactionRecord.add(
				getMessages().amount(),
				getAmountWithCurrency(DecimalUtil.isEquals(
						record.getInvoicedAmount(), 0.0) ? record
						.getPaidAmount() : record.getInvoicedAmount()));
		return transactionRecord;
	}

	/**
	 * 
	 * @return
	 */
	private List<TransactionHistory> getRecords() {
		ArrayList<TransactionHistory> transatiHistories = new ArrayList<TransactionHistory>();
		try {
			transatiHistories = new FinanceTool().getCustomerManager()
					.getCustomerTransactionHistory(getStartDate(),
							getEndDate(), getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transatiHistories;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		endDate = new ClientFinanceDate();
		get(TO_DATE).setValue(endDate);
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

}