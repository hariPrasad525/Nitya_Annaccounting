package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
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
import com.nitya.accounter.web.client.core.reports.VATDetail;
import com.nitya.accounter.web.server.FinanceTool;

public class VATDetailReportCommand extends NewAbstractReportCommand<VATDetail> {

	private String currentsectionName;
	private double accountbalance;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<VATDetail>() {

			@Override
			protected String onSelection(VATDetail selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<VATDetail> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<VATDetail>> recordGroups = new HashMap<String, List<VATDetail>>();
				for (VATDetail vatDetailReport : records) {
					String boxName = vatDetailReport.getBoxName();
					List<VATDetail> group = recordGroups.get(boxName);
					if (group == null) {
						group = new ArrayList<VATDetail>();
						recordGroups.put(boxName, group);
					}
					group.add(vatDetailReport);
				}

				Set<String> boxNames = recordGroups.keySet();
				for (String boxName : boxNames) {
					List<VATDetail> group = recordGroups.get(boxName);
					addSelection(boxName);
					ResultList resultList = new ResultList(boxName);
					resultList.setTitle(boxName);
					double totalAmount = 0.0;
					for (VATDetail rec : group) {
						resultList.add(createReportRecord(rec));
						totalAmount += rec.getTotal();
					}
					makeResult.add(resultList);
					makeResult.add(boxName + " : " + totalAmount);
				}
			}
		});
	}

	protected Record createReportRecord(VATDetail record) {
		Record salesRecord = new Record(record);
		salesRecord.add(getMessages().name(), record.getTransactionName());
		salesRecord.add(getMessages().date(),
				getDateByCompanyType(record.getTransactionDate()));
		salesRecord.add(getMessages().number(), record.getTransactionNumber());
		salesRecord.add(
				getMessages().vatRate(),
				record.isPercentage() ? record.getVatRate() + "%" : record
						.getVatRate());
		salesRecord.add(getMessages().netAmount(),
				getAmountWithCurrency(record.getNetAmount()));
		salesRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getTotal()));
		if (currentsectionName == null
				|| !currentsectionName.equals(record.getBoxName())) {
			currentsectionName = record.getBoxName();
			accountbalance = 0.0D;
		}
		salesRecord.add(getMessages().balance(),
				getAmountWithCurrency(accountbalance += record.getTotal()));

		return salesRecord;
	}

	protected List<VATDetail> getRecords() {
		ArrayList<VATDetail> vatDetails = new ArrayList<VATDetail>();
		try {
			vatDetails = new FinanceTool().getReportManager()
					.getVATDetailReport(getStartDate(), getEndDate(),
							getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetails;
	}

	protected String addCommandOnRecordClick(VATDetail selection) {
		return "updateTransaction " + selection.getTransactionId();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(getMessages().vatDetail());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().vatDetail());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().vatDetail());
	}

}