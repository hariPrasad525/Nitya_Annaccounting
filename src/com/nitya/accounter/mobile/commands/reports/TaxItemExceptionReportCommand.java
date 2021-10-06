package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.ReportResultRequirement;
import com.nitya.accounter.mobile.requirements.TaxAgencyRequirement;
import com.nitya.accounter.web.client.ui.reports.TAXItemDetail;
import com.nitya.accounter.web.server.FinanceTool;

public class TaxItemExceptionReportCommand extends
		NewAbstractReportCommand<TAXItemDetail> {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnter(getMessages().taxAgency() + getMessages().name()),
				getMessages().taxAgency() + getMessages().name(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				TAXAgency value = get(TAX_AGENCY).getValue();
				return getMessages().selectedAs(value.getName(),
						getMessages().taxAgency());
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(getCompany().getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return "";
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return false;
			}
		});
		list.add(new ReportResultRequirement<TAXItemDetail>() {

			@Override
			protected String onSelection(TAXItemDetail selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TAXItemDetail> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}
				ResultList list = new ResultList("Tax Item Exception Report");
				for (TAXItemDetail taxItemDetail : records) {
					list.add(createReportRecord(taxItemDetail));
				}
				makeResult.add(list);
			}
		});
	}

	protected Record createReportRecord(TAXItemDetail record) {
		Record rec = new Record(record);
		rec.add(getMessages().name(), record.getTransactionName());
		rec.add(getMessages().date(),
				getDateByCompanyType(record.getTransactionDate()));
		rec.add(getMessages().number(), record.getTransactionNumber());
		rec.add(getMessages().vatRate(), record.isPercentage() ? "    "
				+ record.getTAXRate() + "%" : record.getTAXRate());
		rec.add(getMessages().filedAmount(),
				getAmountWithCurrency(record.getFiledTAXAmount()));
		rec.add(getMessages().currentAmount(),
				getAmountWithCurrency(record.getTaxAmount()));
		rec.add(getMessages().amountDifference(),
				getAmountWithCurrency(record.getTaxAmount()
						- record.getFiledTAXAmount()));
		return rec;
	}

	protected List<TAXItemDetail> getRecords() {
		ArrayList<TAXItemDetail> itemDetails = new ArrayList<TAXItemDetail>();
		try {
			itemDetails = new FinanceTool().getReportManager()
					.getTAXItemExceptionDetailReport(getCompanyId(),
							((TAXAgency) get(TAX_AGENCY).getValue()).getID(),
							getStartDate().getDate(), getEndDate().getDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDetails;
	}

	protected String addCommandOnRecordClick(TAXItemDetail selection) {
		return "updateTransaction " + selection.getTransactionId();
	}
}
