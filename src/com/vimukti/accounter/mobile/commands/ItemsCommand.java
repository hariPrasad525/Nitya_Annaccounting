package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class ItemsCommand extends AbstractCommand {

	private static final String ITEMS_TYPE = "itemsType";

	private static final int CUSTOMER_ITEMS = 1;

	private static final int VENDOR_ITEMS = 2;

	private static final int INVENTORY_ITEMS = 3;

	private int itemType;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(ITEMS_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<Item>(getMessages().items(),
				getMessages().pleaseSelect(getMessages().item()), 20) {

			@Override
			protected String onSelection(Item value) {
				return "updateItem " + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().itemList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(Item value) {
				return ItemsCommand.this.createRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				ItemsCommand.this.setCreateCommand(list);
			}

			@Override
			protected boolean filter(Item e, String name) {

				return e.getName().startsWith(name)
						|| String.valueOf(e.getID()).startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Item> getLists(Context context) {
				Set<Item> items = getItems(context);
				List<Item> result = new ArrayList<Item>();
				String type = get(ITEMS_TYPE).getValue();
				boolean isActive = type.equalsIgnoreCase("Active") ? true
						: false;
				for (Item item : items) {
					if (isActive == item.isActive()) {
						if (itemType == 0) {
							result.add(item);
						} else if (itemType == INVENTORY_ITEMS ? item.getType() == Item.TYPE_INVENTORY_PART
								: itemType == CUSTOMER_ITEMS ? item
										.isISellThisItem() : item
										.isIBuyThisItem()) {
							result.add(item);
						}
					}
				}
				return result;
			}

		});

	}

	protected Record createRecord(Item value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().description(), value.getPurchaseDescription());
		record.add(getMessages().type(), value.getType());
		if (value.isIBuyThisItem()) {
			record.add(getMessages().purchasePrice(),
					getPreferences().getPrimaryCurrency().getSymbol() + " "
							+ value.getPurchasePrice());
		}
		if (value.isISellThisItem()) {
			record.add(getMessages().salesPrice(),
					getPreferences().getPrimaryCurrency().getSymbol() + " "
							+ value.getSalesPrice());
		}
		return record;
	}

	protected String getItemTypeString(int type) {
		if (type == Item.TYPE_SERVICE) {
			return getMessages().serviceItem();
		} else if (type == Item.TYPE_INVENTORY_PART) {
			return getMessages().inventoryItem();
		} else if (type == Item.TYPE_NON_INVENTORY_PART) {
			return getMessages().nonInventoryItem();
		}
		return getMessages().productItem();
	}

	protected void setCreateCommand(CommandList list) {
		if (itemType == INVENTORY_ITEMS) {
			list.add(new UserCommand("createNewInventoryItem", "sell"));
			return;
		}
		addCreateCommands(list);
	}

	private void addCreateCommands(CommandList list) {
		String isCustomer = itemType == VENDOR_ITEMS ? "buy"
				: itemType == CUSTOMER_ITEMS ? "sell" : "";
		boolean sellProducts = getPreferences().isSellProducts();
		boolean sellServices = getPreferences().isSellServices();
		if (getPreferences().isInventoryEnabled()) {
			if (sellProducts && sellServices) {
				list.add(new UserCommand("createNewServiceItem", isCustomer));
				list.add(new UserCommand("createNewNonInventoryItem",
						isCustomer));
				list.add(new UserCommand("createNewInventoryItem", isCustomer));
				list.add(new UserCommand("createNewInventoryAssemblyItem",
						"sell,buy"));
			} else if (sellProducts) {
				list.add(new UserCommand("createNewNonInventoryItem",
						isCustomer));
				list.add(new UserCommand("createNewInventoryItem", isCustomer));
			} else if (sellServices) {
				list.add(new UserCommand("createNewServiceItem", isCustomer));
			}
		} else {
			list.add(new UserCommand("createNewServiceItem", isCustomer));
			list.add(new UserCommand("newProductItem", isCustomer));
		}
	}

	protected Set<Item> getItems(Context context) {
		return context.getCompany().getItems();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (context.getString().equalsIgnoreCase("customer")) {
			itemType = CUSTOMER_ITEMS;
		}
		if (context.getString().equalsIgnoreCase("inventory")) {
			itemType = INVENTORY_ITEMS;
		}

		if (context.getString().equalsIgnoreCase("vendor")) {
			itemType = VENDOR_ITEMS;
		}
		context.setString("");
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
		get(ITEMS_TYPE).setDefaultValue(getMessages().active());

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

}
