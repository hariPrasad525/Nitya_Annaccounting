package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author vimukti2
 * 
 */
public class ECSalesListReportCommand extends
		NewAbstractReportCommand<ECSalesList> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<ECSalesList>() {

			@Override
			protected String onSelection(ECSalesList selection, String name) {
				markDone();
				return "ecSalesListDetails " + selection.getName();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				ResultList resultList = new ResultList("ECSalesListReport");
				double total = 0.0;
				List<ECSalesList> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				addSelection("ECSalesListReport");
				for (ECSalesList salesList : records) {
					resultList.add(createReportRecord(salesList));
					total += salesList.getAmount();
				}
				makeResult.add(resultList);
				makeResult.add("Total :" + total);
			}
		});
	}

	private Record createReportRecord(ECSalesList record) {
		Record salesRecord = new Record(record);
		salesRecord.add("", record.getName());
		salesRecord.add(getDateByCompanyType(getStartDate()
				.toClientFinanceDate())
				+ "_"
				+ getDateByCompanyType(getEndDate().toClientFinanceDate()),
				getAmountWithCurrency(record.getAmount()));
		return salesRecord;
	}

	@Override
	public String getId() {
		return null;
	}

	private List<ECSalesList> getRecords() {
		ArrayList<ECSalesList> ecSalesLists = new ArrayList<ECSalesList>();
		try {
			ecSalesLists = new FinanceTool().getReportManager()
					.getECSalesListReport(getStartDate(), getEndDate(),
							getCompany());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ecSalesLists;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

}