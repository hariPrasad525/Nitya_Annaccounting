package com.nitya.accounter.mobile.commands;

import java.util.HashSet;
import java.util.Set;

import com.nitya.accounter.core.Item;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.UserCommand;

public class InventoryItemsListCommand extends ItemsCommand {
	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(new UserCommand("createNewInventoryItem", "sell"));
	}

	@Override
	protected Set<Item> getItems(Context context) {
		Set<Item> items = new HashSet<Item>();
		for (Item item : context.getCompany().getItems()) {
			if (item.getType() == Item.TYPE_INVENTORY_PART) {
				items.add(item);
			}
		}
		return items;
	}

	@Override
	protected Record createRecord(Item value) {
		Record createRecord = super.createRecord(value);
		createRecord.add(getMessages().salesPrice(), value.getSalesPrice());
		createRecord.add(getMessages().purchase(), value.getPurchasePrice());
		return createRecord;
	}
}
