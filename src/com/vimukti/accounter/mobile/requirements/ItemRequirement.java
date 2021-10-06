package com.vimukti.accounter.mobile.requirements;

import org.hibernate.Session;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.utils.HibernateUtil;

public abstract class ItemRequirement extends ListRequirement<Item> {

	private boolean isSales;

	public ItemRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Item> listner, boolean isSales) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
		this.isSales = isSales;
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		setItemValue();
		return super.run(context, makeResult, list, actions);
	}

	private void setItemValue() {
		Object value = getValue();
		if (value != null) {
			Session currentSession = HibernateUtil.getCurrentSession();
			Item item = (Item) value;
			item = (Item) currentSession.load(Item.class, item.getID());
			long id = item.getID();
			super.setValue(item);
		}
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setItemValue();
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().items());
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().item());
	}

	@Override
	protected Record createRecord(Item value) {
		Record record = new Record(value);
		if (isSales) {
			record.add(value.getName(), value.getSalesPrice());
		} else {
			record.add(value.getName(), value.getPurchasePrice());
		}
		return record;
	}

	@Override
	protected String getDisplayValue(Item value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		if (isSales) {
			list.add(new UserCommand("createServiceItem", "sell"));
			list.add(new UserCommand("createNonInventoryItem", "sell"));
			list.add(new UserCommand("createInventoryItem", "sell"));
		} else {
			list.add(new UserCommand("createServiceItem", "buy"));
			list.add(new UserCommand("createNonInventoryItem", "buy"));
			list.add(new UserCommand("createInventoryItem", "buy"));
		}
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().item());
	}

	@Override
	protected boolean filter(Item e, String name) {
		return e.getName().toLowerCase().startsWith(name.toLowerCase());
	}
}
