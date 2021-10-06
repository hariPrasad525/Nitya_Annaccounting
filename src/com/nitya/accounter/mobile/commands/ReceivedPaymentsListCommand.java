package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Currency;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.nitya.accounter.web.server.FinanceTool;

public class ReceivedPaymentsListCommand extends AbstractTransactionListCommand {

	private static final int NO_OF_RECORDS_TO_SHOW = 20;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().paid());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new ShowListRequirement<ReceivePaymentsList>(getMessages()
				.receivedPayments(), "", NO_OF_RECORDS_TO_SHOW) {

			@Override
			protected String onSelection(ReceivePaymentsList value) {
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().receivedPayments();
			}

			@Override
			protected String getEmptyString() {

				return "No" + getMessages().receivedPayments();
			}

			@Override
			protected Record createRecord(ReceivePaymentsList value) {

				Record record = new Record(value);

				record.add(getMessages().paymentDate(),
						getDateByCompanyType(value.getPaymentDate()));
				record.add(getMessages().number(), value.getNumber());
				record.add(getMessages().name(), value.getCustomerName());
				record.add(getMessages().paymentMethod(),
						value.getPaymentMethodName());
				record.add(
						getMessages().amountPaid(),
						getAmountWithCurrency(
								value.getAmountPaid(),
								getServerObject(Currency.class,
										value.getCurrency()).getSymbol()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createCustomerPrepayment");
				list.add("createReceivePayment");

			}

			@Override
			protected boolean filter(ReceivePaymentsList e, String name) {
				return e.getCustomerName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<ReceivePaymentsList> getLists(Context context) {
				return getListData(context);
			}
		});
	}

	protected List<ReceivePaymentsList> getListData(Context context) {
		FinanceTool tool = new FinanceTool();
		String viewType = get(VIEW_BY).getValue();
		try {
			return tool.getCustomerManager().getReceivePaymentsList(
					context.getCompany().getID(), getStartDate().getDate(),
					getEndDate().getDate(), 0, 0, -1,
					getViewByList().indexOf(viewType) + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ReceivePaymentsList>();
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().all());
		list.add(getMessages().paid());
		list.add(getMessages().voided());
		return list;
	}

}