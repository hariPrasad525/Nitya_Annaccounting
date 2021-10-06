package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author vimukti2
 * 
 */
public class ECSalesListDetailReportCommand extends
		NewAbstractReportCommand<ECSalesListDetail> {
	private Customer customer;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<ECSalesListDetail>() {

			@Override
			protected String onSelection(ECSalesListDetail selection,
					String name) {
				markDone();
				return "editTransaction " + selection.getTransactionId();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				ResultList resultList = new ResultList("ECSalesListDetail");
				addSelection("ECSalesListDetail");
				List<ECSalesListDetail> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				double total = 0.0;
				for (ECSalesListDetail salesListDetail : records) {
					total += salesListDetail.getAmount();
					resultList.add(createReportRecord(salesListDetail));
				}
				resultList.setTitle(customer.getName());
				makeResult.add(resultList);
				makeResult.add(customer.getName() + getMessages().total()
						+ total);

			}
		});
	}

	/**
	 * 
	 * @param record
	 * @return
	 */
	private Record createReportRecord(ECSalesListDetail record) {
		Record ecRecord = new Record(record);
		ecRecord.add(getMessages().type(),
				Utility.getTransactionName(record.getTransactionType()));
		ecRecord.add(getMessages().date(),
				getDateByCompanyType(record.getDate()));
		ecRecord.add(getMessages().name(), record.getName());
		ecRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));
		return ecRecord;
	}

	@Override
	public String getId() {
		return null;
	}

	/**
	 * get EcsalesListDetails
	 * 
	 * @return
	 */
	private List<ECSalesListDetail> getRecords() {
		ArrayList<ECSalesListDetail> ecsalSalesListDetails = new ArrayList<ECSalesListDetail>();
		try {
			if (customer != null) {
				ecsalSalesListDetails = new FinanceTool().getReportManager()
						.getECSalesListDetailReport(customer.getName(),
								getStartDate(), getEndDate(), getCompany());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ecsalSalesListDetails;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String customerName = context.getString();
		if (customerName != null) {
			customer = CommandUtils.getCustomerByName(getCompany(),
					customerName);
		}
		return null;
	}

}
