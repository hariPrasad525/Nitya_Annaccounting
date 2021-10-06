package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.ReportResultRequirement;
import com.nitya.accounter.mobile.requirements.TaxAgencyRequirement;
import com.nitya.accounter.web.client.core.reports.ExpenseList;
import com.nitya.accounter.web.client.core.reports.VATSummary;
import com.nitya.accounter.web.server.FinanceTool;

public class PriorVATReturnReportCommand extends
		NewAbstractReportCommand<VATSummary> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnter(getMessages().taxAgency()), getMessages()
				.taxAgency(), false, true, null) {

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
		addFromToDateRequirements(list);
		list.add(new ReportResultRequirement<ExpenseList>() {

			@Override
			protected String onSelection(ExpenseList selection, String name) {
				return "updateTransaction " + selection.getTransactionId();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<VATSummary> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}
				Map<String, List<VATSummary>> recordGroups = new HashMap<String, List<VATSummary>>();
				for (VATSummary transactionDetailByAccount : records) {
					String taxItemName = transactionDetailByAccount.getName();
					List<VATSummary> group = recordGroups.get(taxItemName);
					if (group == null) {
						group = new ArrayList<VATSummary>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				for (String accountName : keySet) {
					List<VATSummary> group = recordGroups.get(accountName);
					addSelection(accountName);
					ResultList resultList = new ResultList(accountName);
					resultList.setTitle(accountName);
					for (VATSummary rec : group) {
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
				}
			}
		});
	}

	private Record createReportRecord(VATSummary record) {
		Record vatRecord = new Record(record);
		vatRecord.add(record.getName());
		vatRecord.add("", record.getValue());
		return vatRecord;
	}

	private List<VATSummary> getRecords() {
		ArrayList<VATSummary> vatSummaries = new ArrayList<VATSummary>();
		TAXAgency agency = get(TAX_AGENCY).getValue();
		try {
			vatSummaries = new FinanceTool().getTaxManager()
					.getPriorReturnVATSummary(agency, getEndDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaries;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().priorVATReturns());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().priorVATReturns());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().priorVATReturns());
	}
}