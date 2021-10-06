package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.ReportResultRequirement;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.PaySlipSummary;
import com.nitya.accounter.web.server.FinanceTool;

public class PaySlipSummaryReportCommand extends
		NewAbstractReportCommand<PaySlipSummary> {

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		endDate = new ClientFinanceDate();
		get(TO_DATE).setValue(endDate);
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<PaySlipSummary>() {

			@Override
			protected String onSelection(PaySlipSummary selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<PaySlipSummary> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				ResultList resultList = new ResultList("paySlipSummary");
				addSelection("paySlipSummary");
				makeResult.add(resultList);
				double totalAmount = 0.0;
				for (PaySlipSummary record : records) {
					totalAmount += record.getAmount();
					resultList.add(createReportRcord(record));
				}
				makeResult
						.add("Total :- " + getAmountWithCurrency(totalAmount));
			}
		});
	}

	protected Record createReportRcord(PaySlipSummary record) {
		Record payrecord = new Record(record);
		payrecord.add(getMessages().name(), record.getName());
		payrecord.add(getMessages().number(), record.getNumber());
		payrecord.add(getMessages().accountNumber(), record.getAccountNo());
		payrecord.add(getMessages().bankName(), record.getBankName());
		payrecord.add(getMessages().branchOrdivison(), record.getBranch());
		payrecord.add(getMessages().email(), record.getEmail());
		payrecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));
		return payrecord;
	}

	protected List<PaySlipSummary> getRecords() {
		List<PaySlipSummary> paySlipSummaries = new ArrayList<PaySlipSummary>();
		paySlipSummaries = new FinanceTool()
				.getPayrollManager()
				.getPaySlipSummary(getStartDate(), getEndDate(), getCompanyId());
		if (paySlipSummaries != null) {
			return paySlipSummaries;
		}
		return new ArrayList<PaySlipSummary>();
	}

	protected String addCommandOnRecordClick(PaySlipSummary selection) {
		return "payslipDetail #" + selection.getEmployeeId();
	}

}
