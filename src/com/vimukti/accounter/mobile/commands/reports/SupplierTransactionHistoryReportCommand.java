package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public class SupplierTransactionHistoryReportCommand extends
		NewAbstractReportCommand<TransactionHistory> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<TransactionHistory>() {

			@Override
			protected String onSelection(TransactionHistory selection,
					String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TransactionHistory> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(Global.get().messages().noRecordsToShow());
					return;
				}

				Map<String, List<TransactionHistory>> recordGroups = new HashMap<String, List<TransactionHistory>>();
				for (TransactionHistory transactionDetailByAccount : records) {
					String vendorName = transactionDetailByAccount.getName();
					List<TransactionHistory> group = recordGroups
							.get(vendorName);
					if (group == null) {
						group = new ArrayList<TransactionHistory>();
						recordGroups.put(vendorName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> vendorNames = new ArrayList<String>(keySet);
				Collections.sort(vendorNames);
				for (String vendorName : vendorNames) {
					List<TransactionHistory> group = recordGroups
							.get(vendorName);
					addSelection(vendorName);
					ResultList resultList = new ResultList(vendorName);
					for (TransactionHistory rec : group) {
						resultList.setTitle(rec.getName());
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
				}
			}
		});
	}

	protected Record createReportRecord(TransactionHistory record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add(getMessages().date(),
				getDateByCompanyType(record.getDate()));
		transactionRecord.add(getMessages().type(),
				Utility.getTransactionName(record.getType()));
		transactionRecord.add(getMessages().number(), record.getNumber());
		transactionRecord.add(getMessages().account(), record.getAccount());
		transactionRecord.add(
				getMessages().amount(),
				getAmountWithCurrency(DecimalUtil.isEquals(
						record.getInvoicedAmount(), 0.0) ? record
						.getPaidAmount() : record.getInvoicedAmount()));
		return transactionRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<TransactionHistory> getRecords() {
		ArrayList<TransactionHistory> transactionHistories = new ArrayList<TransactionHistory>();
		try {
			transactionHistories = new FinanceTool().getVendorManager()
					.getVendorTransactionHistory(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionHistories;
	}

	protected String addCommandOnRecordClick(TransactionHistory selection) {
		return "updateTransaction " + selection.getTransactionId();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().payeeTransactionHistory(Global.get().Vendor()));
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().payeeTransactionHistory(Global.get().Vendor()));
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().payeeTransactionHistory(Global.get().Vendor()));
	}

}