package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesOpenOrderReportCommand extends
		NewAbstractReportCommand<OpenAndClosedOrders> {
	private int status;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(STATUS, getMessages().pleaseSelect(
				getMessages().yourTitle()), getMessages().status(), false,
				true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						if (value.equals(getMessages().open())) {
							status = ClientEstimate.STATUS_OPEN;
						} else if (value.equals(getMessages().completed())) {
							status = ClientTransaction.STATUS_COMPLETED;
						} else if (value.equals(getMessages().cancelled())) {
							status = ClientTransaction.STATUS_CANCELLED;
						} else if (value.equals(getMessages().all())) {
							status = -1;
						} else if (value.equals(getMessages().expired())) {
							status = 6;
						}
					}
				}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().status());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().status());
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> strings = new ArrayList<String>();
				strings.add(getMessages().open());
				strings.add(getMessages().completed());
				strings.add(getMessages().cancelled());
				strings.add(getMessages().expired());
				strings.add(getMessages().all());
				return strings;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<OpenAndClosedOrders>() {

			@Override
			protected String onSelection(OpenAndClosedOrders selection,
					String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<OpenAndClosedOrders> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<OpenAndClosedOrders>> recordGroups = new HashMap<String, List<OpenAndClosedOrders>>();
				for (OpenAndClosedOrders transactionDetailByAccount : records) {
					String taxItemName = transactionDetailByAccount
							.getVendorOrCustomerName();
					List<OpenAndClosedOrders> group = recordGroups
							.get(taxItemName);
					if (group == null) {
						group = new ArrayList<OpenAndClosedOrders>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> taxItems = new ArrayList<String>(keySet);
				Collections.sort(taxItems);
				for (String accountName : taxItems) {
					List<OpenAndClosedOrders> group = recordGroups
							.get(accountName);
					double totalAmount = 0.0;
					addSelection(accountName);
					ResultList resultList = new ResultList(accountName);
					for (OpenAndClosedOrders rec : group) {
						totalAmount += rec.getAmount();
						resultList.setTitle(rec.getVendorOrCustomerName());
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
					makeResult.add(getMessages().total() + " : "
							+ getAmountWithCurrency(totalAmount));
				}

			}
		});
	}

	protected Record createReportRecord(OpenAndClosedOrders record) {
		Record openRecord = new Record(record);
		if (record.getTransactionDate() != null) {
			openRecord.add(getMessages().orderDate(),
					getDateByCompanyType(record.getTransactionDate()));
		}
		openRecord.add(Global.get().Customer(),
				record.getVendorOrCustomerName());
		openRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));

		return openRecord;
	}

	@Override
	public String getId() {
		return null;
	}

	protected List<OpenAndClosedOrders> getRecords() {

		ArrayList<OpenAndClosedOrders> openAndClosedOrders = new ArrayList<OpenAndClosedOrders>();
		try {
			openAndClosedOrders = new FinanceTool().getSalesManager()
					.getSalesOrders(status, getStartDate(), getEndDate(),
							getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return openAndClosedOrders;

	}

	protected String addCommandOnRecordClick(OpenAndClosedOrders selection) {
		return "updateTransaction " + selection.getTransactionID();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().sales() + getMessages().open());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().sales() + getMessages().open());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().sales() + getMessages().open());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(STATUS).setValue(getMessages().open());
		super.setDefaultValues(context);
	}
}