package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.PayHeadRequirement;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.web.client.core.reports.PayHeadSummary;
import com.vimukti.accounter.web.server.FinanceTool;

public class PayheadSummaryCommand extends
		NewAbstractReportCommand<PayHeadSummary> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new PayHeadRequirement(PAY_HEAD) {

			@Override
			protected List<PayHead> getLists(Context context) {
				return super.getLists(context);
			}
		});

		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<PayHeadSummary>() {

			@Override
			protected String onSelection(PayHeadSummary selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<PayHeadSummary> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<PayHeadSummary>> recordGroups = new HashMap<String, List<PayHeadSummary>>();
				for (PayHeadSummary payheadSummary : records) {
					String payheadName = payheadSummary.getPayHeadName();
					List<PayHeadSummary> group = recordGroups.get(payheadName);
					if (group == null) {
						group = new ArrayList<PayHeadSummary>();
						recordGroups.put(payheadName, group);
					}
					group.add(payheadSummary);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> payHeadItems = new ArrayList<String>(keySet);
				Collections.sort(payHeadItems);
				for (String accountName : payHeadItems) {
					List<PayHeadSummary> group = recordGroups.get(accountName);
					double totalAmount = 0.0;
					addSelection(accountName);
					ResultList resultList = new ResultList(accountName);
					resultList.setTitle(accountName);
					for (PayHeadSummary rec : group) {
						totalAmount += rec.getPayHeadAmount();
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
					makeResult.add("Total: "
							+ getAmountWithCurrency(totalAmount));
				}
			}
		});
	}

	protected Record createReportRecord(PayHeadSummary record) {
		Record payHeadRecord = new Record(record);
		payHeadRecord.add(getMessages().payhead(), record.getPayHeadName());
		payHeadRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getPayHeadAmount()));
		return payHeadRecord;
	}

	protected List<PayHeadSummary> getRecords() {
		ArrayList<PayHeadSummary> PayHeadSummarys = new ArrayList<PayHeadSummary>();
		PayHead value = get(PAY_HEAD).getValue();
		try {
			PayHeadSummarys = new FinanceTool().getPayrollManager()
					.getPayHeadSummaryReport(value.getID(), getStartDate(),
							getEndDate(), getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return PayHeadSummarys;
	}

	protected String addCommandOnRecordClick(PayHeadSummary selection) {
		return "payHeadDetailReport #" + selection.getPayHead();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportDetails(getMessages().payHeadDetailReport());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages()
				.reportDetails(getMessages().payHeadSummaryReport());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().payHeadSummaryReport());
	}

}