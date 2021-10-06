package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.InventoryAssemblyItem;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.InventoryAssemblyItemTableRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientInventoryAssembly;
import com.nitya.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.nitya.accounter.web.client.core.ClientItem;

public class CreateInventoryAssemblyItemCommand extends
		CreateInventoryItemCommand {
	private static final String INVENTORYITEMS = "inventoryItems";

	@Override
	public int getItemType() {
		return ClientItem.TYPE_INVENTORY_ASSEMBLY;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new InventoryAssemblyItemTableRequirement(INVENTORYITEMS,
				getMessages().pleaseEnter(getMessages().name()), getMessages()
						.items(), true, true) {
		});
	}

	@Override
	protected String getWelcomeMessage() {
		return getItem().getID() == 0 ? getMessages().creating(
				getMessages().inventoryItem())
				: "Updating Inventory Assembly Item";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientInventoryAssemblyItem> items = get(INVENTORYITEMS)
				.getValue();
		Set<ClientInventoryAssemblyItem> set = new HashSet<ClientInventoryAssemblyItem>();
		set.addAll(items);
		((ClientInventoryAssembly) getItem()).setComponents(set);
		return super.onCompleteProcess(context);
	}

	protected List<InventoryAssemblyItem> getItems() {
		List<InventoryAssemblyItem> returnedItems = new ArrayList<InventoryAssemblyItem>();
		Set<Item> items = getCompany().getItems();
		for (Item item : items) {
			if (item.isActive() && item.getType() == Item.TYPE_INVENTORY_PART) {
				InventoryAssemblyItem assemblyItem = new InventoryAssemblyItem();
				assemblyItem.setInventoryItem(item);
				assemblyItem.setDiscription(item.getPurchaseDescription());
				assemblyItem.setWarehouse(item.getWarehouse());
				returnedItems.add(assemblyItem);
			}
		}
		return returnedItems;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (!isUpdate) {
			setItem(new ClientInventoryAssembly());
		} else {
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages().selectItemToUpdate());
				return "listOfItems";
			}
			Item customerByName = CommandUtils.getItemByName(
					context.getCompany(), string);
			if (customerByName == null) {
				addFirstMessage(context, getMessages().selectItemToUpdate());
				return "listOfItems " + string;
			}
			setItem((ClientInventoryAssembly) CommandUtils.getClientObjectById(
					customerByName.getID(), AccounterCoreType.ITEM, context
							.getCompany().getId()));
			setValues();
		}
		get(I_SELL_THIS).setValue(true);
		get(I_BUY_THIS).setValue(true);
		return null;
	}
}
