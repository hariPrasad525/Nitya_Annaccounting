package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ForwardRequirement;
import com.vimukti.accounter.mobile.requirements.ListRequirement;

public class UpdateItemCommand extends AbstractCommand {

	private static final String ITEM = "item";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long itemId = Long.valueOf(context.getString());
		Session session = context.getHibernateSession();
		Item item = (Item) session.get(Item.class, itemId);
		get(ITEM).setValue(item);
		context.setString("");
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ListRequirement<Item>(ITEM, getMessages().pleaseEnter(
				"Item name"), "Item", false, true, null) {

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected Record createRecord(Item value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getDisplayValue(Item value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getSelectString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected boolean filter(Item e, String name) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			protected List<Item> getLists(Context context) {
				// TODO Auto-generated method stub
				return null;
			}
		});

		list.add(new ForwardRequirement() {

			@Override
			public String getNextCommand() {
				Item item = get(ITEM).getValue();
				String name = item.getName();
				String nextCommand = null;
				switch (item.getType()) {
				case Item.TYPE_SERVICE:
					nextCommand = "updateServiceItem " + name;
					break;
				case Item.TYPE_NON_INVENTORY_PART:
					nextCommand = "updateNonInventoryItem " + name;
					break;
				case Item.TYPE_INVENTORY_PART:
					nextCommand = "updateInventoryItem " + name;
					break;
				}
				return nextCommand;

			}
		});

	}
}
