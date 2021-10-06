package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.ListRequirement;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.ui.CoreUtils;

public class AddNewCurrencyCommand extends AbstractCommand {

	private static final String ADDCURRENCY = "Add New Currency";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Adding New Currecy..";
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().currency());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().currency());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ListRequirement<ClientCurrency>(ADDCURRENCY, getMessages()
				.pleaseEnter(getMessages().currency()), ADDCURRENCY, false,
				true, null) {

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().currency());
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().currency());
			}

			@Override
			protected Record createRecord(ClientCurrency value) {
				Record record = new Record(value);
				record.add(value.getDisplayName());
				return record;
			}

			@Override
			protected String getDisplayValue(ClientCurrency value) {
				return value != null ? value.getDisplayName() : "";
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().currency());
			}

			@Override
			protected boolean filter(ClientCurrency e, String name) {
				return e.getDisplayName().toLowerCase()
						.startsWith(name.toLowerCase());
			}

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				List<ClientCurrency> currenciesList = new ArrayList<ClientCurrency>();
				List<ClientCurrency> currencies = CoreUtils
						.getCurrencies(new ArrayList<ClientCurrency>());
				for (ClientCurrency currency : currencies) {
					currenciesList.add(currency);
				}
				return currenciesList;
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientCurrency clientCurrency = new ClientCurrency();

		ClientCurrency item = get(ADDCURRENCY).getValue();
		clientCurrency.setName(item.getName());
		clientCurrency.setFormalName(item.getFormalName());
		clientCurrency.setSymbol(item.getSymbol());
		create(clientCurrency, context);
		return null;
	}
}
