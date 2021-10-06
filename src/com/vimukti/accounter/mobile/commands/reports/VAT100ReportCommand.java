package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.mobile.requirements.TaxAgencyRequirement;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.server.FinanceTool;

public class VAT100ReportCommand extends NewAbstractReportCommand<VATSummary> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnter(getMessages().taxAgencie()), getMessages()
				.taxAgencie(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(getCompany().getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return false;
			}
		});
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<VATSummary>() {

			@Override
			protected String onSelection(VATSummary selection, String name) {
				return null;
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<VATSummary> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				makeResult.add(getMessages().vat100());
				ResultList vat100reportsList = new ResultList(
						"vat100reportlist");
				makeResult.add(vat100reportsList);
				vat100reportsList.setTitle(getMessages().vatDue());
				addSelection("vat100reportlist");
				for (VATSummary vatSummary : records) {
					vat100reportsList.add(createReportRecord(vatSummary));
				}
			}
		});
	}

	protected Record createReportRecord(VATSummary record) {
		Record vatItemRecord = new Record(record);
		vatItemRecord.add(getMessages().name(), record.getVatReturnEntryName());
		vatItemRecord.add(getDateByCompanyType(getStartDate()
				.toClientFinanceDate())
				+ "_"
				+ getDateByCompanyType(getEndDate().toClientFinanceDate()),
				record.getValue());
		return vatItemRecord;
	}

	protected List<VATSummary> getRecords() {
		ArrayList<VATSummary> vatSummaries = new ArrayList<VATSummary>();
		try {
			TAXAgency agency = get(TAX_AGENCY).getValue();
			if (agency != null) {
				vatSummaries = new FinanceTool().getReportManager()
						.getVAT100Report(agency, getStartDate(), getEndDate(),
								getCompanyId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaries;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(getMessages().vat100());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().vat100());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().vat100());
	}
}