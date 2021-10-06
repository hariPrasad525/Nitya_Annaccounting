package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesByCustomerSummaryReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {

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

				ResultList customerSummaryList = new ResultList(
						"customersummary");
				addSelection("customersummary");
				makeResult.add(customerSummaryList);
				double totalAmount = 0.0;
				for (SalesByCustomerDetail record : records) {
					totalAmount += record.getAmount();
					customerSummaryList.add(createReportRecord(record));
				}
				makeResult.add(getMessages().total() + " : "
						+ getAmountWithCurrency(totalAmount));
			}
		});
	}

	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record salesRecord = new Record(record);
		salesRecord.add(getMessages().payeeName(Global.get().Customer()),
				record.getName());
		salesRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));
		return salesRecord;
	}

	protected List<SalesByCustomerDetail> getRecords() {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetails = new ArrayList<SalesByCustomerDetail>();
		try {
			salesByCustomerDetails = new FinanceTool().getSalesManager()
					.getSalesByCustomerSummary(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByCustomerDetails;
	}

	protected String addCommandOnRecordClick(SalesByCustomerDetail selection) {
		return "salesByCustomerDetail ," + selection.getName();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().salesByCustomerSummary(Global.get().Customer()));
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().salesByCustomerSummary(Global.get().Customer()));
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().salesByCustomerSummary(Global.get().Customer()));
	}

}