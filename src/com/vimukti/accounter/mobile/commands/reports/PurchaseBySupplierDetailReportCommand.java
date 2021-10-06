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
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseBySupplierDetailReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {
	private String supplierName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<SalesByCustomerDetail>() {

			@Override
			protected String onSelection(SalesByCustomerDetail selection,
					String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<SalesByCustomerDetail> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<SalesByCustomerDetail>> recordGroups = new HashMap<String, List<SalesByCustomerDetail>>();
				for (SalesByCustomerDetail transactionDetailByAccount : records) {
					String taxItemName = transactionDetailByAccount.getName();
					List<SalesByCustomerDetail> group = recordGroups
							.get(taxItemName);
					if (group == null) {
						group = new ArrayList<SalesByCustomerDetail>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> taxItems = new ArrayList<String>(keySet);
				Collections.sort(taxItems);
				for (String accountName : taxItems) {
					List<SalesByCustomerDetail> group = recordGroups
							.get(accountName);
					double totalAmount = 0.0;
					addSelection(accountName);
					ResultList resultList = new ResultList(accountName);
					for (SalesByCustomerDetail rec : group) {
						totalAmount += rec.getAmount();
						resultList.setTitle(rec.getName());
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
					makeResult.add("Total: "
							+ getAmountWithCurrency(totalAmount));
				}
			}
		});
	}

	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add(getMessages().payeeName(Global.get().vendor()),
				"");
		transactionRecord.add(getMessages().date(),
				getDateByCompanyType(record.getDate()));
		transactionRecord.add(getMessages().type(),
				Utility.getTransactionName(record.getType()));
		transactionRecord.add(getMessages().number(), record.getNumber());
		transactionRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));
		return transactionRecord;
	}

	protected List<SalesByCustomerDetail> getRecords() {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetails = new ArrayList<SalesByCustomerDetail>();
		try {
			if (supplierName == null || supplierName.isEmpty()) {
				salesByCustomerDetails = new FinanceTool().getVendorManager()
						.getPurchasesByVendorDetail(getStartDate(),
								getEndDate(), getCompanyId());
			} else if (supplierName != null) {
				salesByCustomerDetails = new FinanceTool().getVendorManager()
						.getPurchasesByVendorDetail(supplierName,
								getStartDate(), getEndDate(), getCompanyId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByCustomerDetails;
	}

	protected String addCommandOnRecordClick(SalesByCustomerDetail selection) {
		return "updateTransaction " + selection.getTransactionId();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (!string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			supplierName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().purchaseByVendorDetail(Global.get().Vendor()));
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().purchaseByVendorDetail(Global.get().Vendor()));
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().purchaseByVendorDetail(Global.get().Vendor()));
	}

}