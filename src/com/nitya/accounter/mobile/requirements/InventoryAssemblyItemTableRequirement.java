package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.Unit;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.nitya.accounter.web.client.core.ClientQuantity;

public abstract class InventoryAssemblyItemTableRequirement extends
		AbstractTableRequirement<ClientInventoryAssemblyItem> {
	private static final String ITEM = "inventoryItems";
	private static final String COMMENT = "comment";
	private static final String QUANTITYNEEDED = "quantityNeeded";
	private static final String UNITPRICE = "unitPrice";
	private static final String TOTAL = "total";

	public InventoryAssemblyItemTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM, getMessages().pleaseSelect(
				getMessages().item()), getMessages().item(), false, true,
				new ChangeListner<Item>() {

					@Override
					public void onSelection(Item item) {
						itemChanged(item);
					}

				}, true) {

			@Override
			protected List<Item> getLists(Context context) {
				return getItems();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createInventoryItem");
			}
		});

		list.add(new StringRequirement(COMMENT, getMessages().pleaseEnter(
				getMessages().comment()), getMessages().comment(), true, true));

		list.add(new QuantityRequirement(QUANTITYNEEDED, getMessages()
				.pleaseEnter(getMessages().quantityNeeded()), getMessages()
				.quantityNeeded(), false) {

			@Override
			protected List<Unit> getLists(Context context) {
				return InventoryAssemblyItemTableRequirement.this.getUnits();
			}
		});

		list.add(new AmountRequirement(UNITPRICE, getMessages().pleaseEnter(
				getMessages().unitPrice()), getMessages().unitPrice(), true,
				true));
	}

	protected void itemChanged(Item item) {
		ClientQuantity value2 = get(QUANTITYNEEDED).getValue();
		if (value2 == null) {
			ClientQuantity clientQuantity = new ClientQuantity();
			clientQuantity.setValue(1.0);
			value2 = clientQuantity;
			get(QUANTITYNEEDED).setDefaultValue(clientQuantity);
		}
		value2.setUnit(item.getMeasurement().getDefaultUnit().getID());

		QuantityRequirement requirement = (QuantityRequirement) get(QUANTITYNEEDED);
		requirement.setDefaultUnit(item.getMeasurement().getDefaultUnit());
	}

	protected List<Unit> getUnits() {
		Item item = get(ITEM).getValue();
		Set<Unit> units = item.getMeasurement().getUnits();
		return new ArrayList<Unit>(units);
	}

	protected List<Item> getItems() {
		List<Item> returnedItems = new ArrayList<Item>();
		Set<Item> items = getCompany().getItems();
		for (Item item : items) {
			if (item.isActive() && item.getType() == Item.TYPE_INVENTORY_PART) {
				returnedItems.add(item);
			}
		}
		return returnedItems;
	}

	@Override
	protected List<ClientInventoryAssemblyItem> getList() {
		return null;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected void getRequirementsValues(ClientInventoryAssemblyItem obj) {
		obj.setInventoryItem(((Item) get(ITEM).getValue()).getID());
		String comment = get(COMMENT).getValue();
		obj.setDescription(comment);
		ClientQuantity clientQuantity = get(QUANTITYNEEDED).getValue();
		obj.setQuantity(clientQuantity);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientInventoryAssemblyItem obj) {
		Item item = (Item) CommandUtils.getServerObjectById(
				obj.getInventoryItem(), AccounterCoreType.ITEM);
		get(ITEM).setValue(item);
		get(COMMENT).setValue(obj.getDiscription());
		get(QUANTITYNEEDED).setValue(obj.getQuantity());
	}

	@Override
	protected ClientInventoryAssemblyItem getNewObject() {
		ClientInventoryAssemblyItem assemblyItem = new ClientInventoryAssemblyItem();
		return assemblyItem;
	}

	@Override
	protected Record createFullRecord(ClientInventoryAssemblyItem t) {
		Record record = new Record(t);
		Item item = (Item) CommandUtils.getServerObjectById(
				t.getInventoryItem(), AccounterCoreType.ITEM);
		record.add(getMessages().itemName(), item != null ? item.getName() : "");
		record.add(getMessages().comment(), t.getDiscription());

		Unit clientUnit = (Unit) CommandUtils.getServerObjectById(t
				.getQuantity().getUnit(), AccounterCoreType.UNIT);
		if (clientUnit != null) {
			record.add(getMessages().quantityNeeded(), t.getQuantity()
					.getValue() + " " + clientUnit.getType());
		} else {
			record.add(getMessages().quantityNeeded(), t.getQuantity()
					.getValue());
		}
		return record;
	}

	@Override
	protected Record createRecord(ClientInventoryAssemblyItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().items());
	}

}
