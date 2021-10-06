package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Currency;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;

public class CurrencyListCommand extends AbstractCommand {

	private static final String CURRENCIES = "currencies";

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
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<Currency>(CURRENCIES, null, 20) {

			@Override
			protected String onSelection(Currency value) {
				return "updateCurrency " + value.getName();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().currencyList();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected Record createRecord(Currency value) {
				Record record = new Record(value);
				record.add(value.getName());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("addCurrency");
			}

			@Override
			protected boolean filter(Currency e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});

	}

}
