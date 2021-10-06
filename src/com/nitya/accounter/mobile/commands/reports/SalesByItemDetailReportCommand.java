package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.ReportResultRequirement;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.server.FinanceTool;

public class SalesByItemDetailReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {
	private String itemName;

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
					String taxItemName = transactionDetailByAccount
							.getItemName();
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
						resultList.setTitle(rec.getItemName());
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
					makeResult.add(getMessages().total() + " : "
							+ getAmountWithCurrency(totalAmount));
				}
			}
		});

	}

	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record salesRecord = new Record(record);
		salesRecord.add(getMessages().date(),
				getDateByCompanyType(record.getDate()));
		salesRecord.add(getMessages().type(),
				ReportUtility.getTransactionName(record.getType()));
		salesRecord.add(getMessages().number(), record.getNumber());
		salesRecord.add(getMessages().quantity(), record.getQuantity());
		salesRecord.add(getMessages().unitPrice(),
				getAmountWithCurrency(record.getUnitPrice()));
		salesRecord.add(getMessages().discount(),
				getAmountWithCurrency(record.getDiscount()));
		salesRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));
		return salesRecord;
	}

	protected List<SalesByCustomerDetail> getRecords() {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetails = new ArrayList<SalesByCustomerDetail>();
		try {
			if (itemName == null) {
				salesByCustomerDetails = new FinanceTool().getSalesManager()
						.getSalesByItemDetail(getStartDate(), getEndDate(),
								getCompanyId());
			} else if (itemName != null && !itemName.isEmpty()) {
				salesByCustomerDetails = new FinanceTool().getSalesManager()
						.getSalesByItemDetail(itemName, getStartDate(),
								getEndDate(), getCompanyId());
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
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			itemName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().salesByItemSummary());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().salesByItemSummary());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().salesByItemSummary());
	}

}