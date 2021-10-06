package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.server.FinanceTool;

public class MergeCustomersCommand extends AbstractCommand {

	private static final String CUSTOMER_FROM = "customerFrom";
	private static final String CUSTOMER_TO = "customerTo";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().merging(Global.get().Customers());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToMerge(Global.get().Customers());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().mergingCompleted(Global.get().Customers());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER_FROM, getMessages()
				.payeeFrom(Global.get().customer()), Global.get().Customer(),
				false, true, null) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}

			@Override
			public void setValue(Object value) {
				Customer customerTo = get(CUSTOMER_TO).getValue();
				Customer customerFrom = (Customer) value;
				String checkDifferentCustomers = null;
				if (customerFrom != null && customerTo != null) {
					checkDifferentCustomers = checkDifferentCustomers(
							customerFrom, customerTo);
				}
				if (checkDifferentCustomers != null) {
					addFirstMessage(checkDifferentCustomers);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeFrom(Global.get().Customer()));
			}

			@Override
			protected String getSetMessage() {
				Customer value = getValue();
				if (value != null) {
					return getMessages().selectedAs(value.getName(),
							getMessages().payeeFrom(Global.get().Customer()));
				}
				return null;
			}
		});

		list.add(new CustomerRequirement(CUSTOMER_TO, getMessages().payeeTo(
				Global.get().customer()), Global.get().Customer(), false, true,
				null) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}

			@Override
			public void setValue(Object value) {
				Customer customerFrom = get(CUSTOMER_FROM).getValue();
				Customer customerTo = (Customer) value;
				String checkDifferentCustomers = null;
				if (customerFrom != null && customerTo != null) {
					checkDifferentCustomers = checkDifferentCustomers(
							customerFrom, customerTo);
				}
				if (checkDifferentCustomers != null) {
					addFirstMessage(checkDifferentCustomers);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeTo(Global.get().Customer()));
			}

			@Override
			protected String getSetMessage() {
				Customer value = getValue();
				if (value != null) {
					return getMessages().selectedAs(value.getName(),
							getMessages().payeeTo(Global.get().Customer()));
				}
				return null;
			}
		});

	}

	protected String checkDifferentCustomers(Customer customerFrom,
			Customer customerTo) {
		if (customerFrom.getID() == customerTo.getID()) {
			return getMessages().notMove(Global.get().customers());
		}
		if (getPreferences().isEnableMultiCurrency()) {
			long from = customerFrom.getCurrency().getID();
			long to = customerTo.getCurrency().getID();
			if (from != to) {
				return getMessages().currenciesOfTheBothCustomersMustBeSame(
						Global.get().customers());
			}
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
		Customer customerFrom = get(CUSTOMER_FROM).getValue();
		Customer customerTo = get(CUSTOMER_TO).getValue();

		try {
			ClientCustomer clientFrom = clientConvertUtil.toClientObject(
					customerFrom, ClientCustomer.class);
			ClientCustomer clientTo = clientConvertUtil.toClientObject(
					customerTo, ClientCustomer.class);

			new FinanceTool().getCustomerManager().mergeCustomer(clientFrom,
					clientTo, getCompany().getID());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.onCompleteProcess(context);
	}
}
