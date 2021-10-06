package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Utility;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.CustomerRequirement;
import com.nitya.accounter.mobile.requirements.ReportResultRequirement;
import com.nitya.accounter.web.client.core.Lists.PayeeStatementsList;
import com.nitya.accounter.web.server.FinanceTool;

public class CustomerStatementReportCommand extends
		NewAbstractReportCommand<PayeeStatementsList> {

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseEnter(
				getMessages().customer() + getMessages().name()), getMessages()
				.customer() + getMessages().name(), false, true, null) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});
		addDateRangeFromToDateRequirements(list);

		list.add(new ReportResultRequirement<PayeeStatementsList>() {

			@Override
			protected String onSelection(PayeeStatementsList selection,
					String name) {
				markDone();
				return "editTransaction " + selection.getTransactionId();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {

				List<PayeeStatementsList> records = getRecords(context);
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				ResultList list = new ResultList("Customer StateMent");
				for (PayeeStatementsList payeeStatementsList : records) {
					list.add(createReportRecord(payeeStatementsList));
				}
				makeResult.add(list);
			}
		});
	}

	/**
	 * 
	 * @param record
	 * @return
	 */
	protected Record createReportRecord(PayeeStatementsList record) {
		Record statementRecord = new Record(record);
		statementRecord.add(getMessages().date(),
				getDateByCompanyType(record.getTransactionDate()));
		statementRecord.add(getMessages().transactionName(),
				Utility.getTransactionName(record.getTransactiontype()));
		statementRecord.add(
				getMessages().amount(),
				getAmountWithCurrency(record.getTotal()
						* record.getCurrencyFactor()));
		return statementRecord;
	}

	/**
	 * get payees{Customer}
	 * 
	 * @return
	 */
	protected List<PayeeStatementsList> getRecords(Context context) {

		ArrayList<PayeeStatementsList> payeeStatementList = new ArrayList<PayeeStatementsList>();
		try {
			payeeStatementList = new FinanceTool().getCustomerManager()
					.getCustomerStatement(
							((Customer) get(CUSTOMER).getValue()).getID(),
							getStartDate().getDate(), getEndDate().getDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payeeStatementList;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

}