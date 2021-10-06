package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class StockAdjustmentItemTableRequirement extends
		AbstractTableRequirement<ClientTransactionItem> {

	private static final String ITEM = "inventoryItems";
	private static final String COMMENT = "comment";
	private static final String ADJUSTMENT_QTY = "adjustmentQty";
	private static final String UNIT_PRICE = "unitprice";

	public StockAdjustmentItemTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, true, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM, getMessages().pleaseSelect(
				getMessages().item()), getMessages().item(), false, true,
				new ChangeListner<Item>() {

					@Override
					public void onSelection(Item item) {
						ClientQuantity value2 = get(ADJUSTMENT_QTY).getValue();
						if (value2 != null) {
							value2.setUnit(item.getMeasurement()
									.getDefaultUnit().getID());
						}

						QuantityRequirement requirement = (QuantityRequirement) get(ADJUSTMENT_QTY);
						requirement.setDefaultUnit(item.getMeasurement()
								.getDefaultUnit());
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

		list.add(new QuantityRequirement(ADJUSTMENT_QTY, getMessages()
				.pleaseEnter(getMessages().adjustedQty()), getMessages()
				.adjustedQty(), false) {

			@Override
			protected List<Unit> getLists(Context context) {
				return StockAdjustmentItemTableRequirement.this.getUnits();
			}
		});

		list.add(new AmountRequirement(UNIT_PRICE, getMessages().pleaseEnter(
				getMessages().salesOrPurchaseRate()), getMessages()
				.salesOrPurchaseRate(), false, true));
	}

	protected void itemChanged(Object value) {
		if (value != null) {
			Item item = (Item) value;
			ClientQuantity qty = new ClientQuantity();
			Unit unit = item.getOnhandQty().getUnit();
			qty.setUnit(unit.getID());
			get(ADJUSTMENT_QTY).setValue(qty);
			get(UNIT_PRICE).setValue(0.0);
		}
	}

	protected List<Unit> getUnits() {
		Item item = get(ITEM).getValue();
		Set<Unit> units = item.getMeasurement().getUnits();
		return new ArrayList<Unit>(units);
	}

	protected List<Item> getItems() {
		List<Item> returnedItems = new ArrayList<Item>();
		Warehouse wareHouse = getWareHouse();
		Set<Item> items = getCompany().getItems();
		for (Item item : items) {
			if ((item.getWarehouse() == null ? true : item.getWarehouse()
					.getID() == wareHouse.getID())
					&& item.isActive()
					&& (item.getType() == Item.TYPE_INVENTORY_PART || item
							.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY)) {
				returnedItems.add(item);
			}
		}
		return returnedItems;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected void getRequirementsValues(ClientTransactionItem obj) {
		Item item = get(ITEM).getValue();
		item = (Item) HibernateUtil.getCurrentSession().get(Item.class,
				item.getID());
		obj.setItem(item.getID());
		String comment = get(COMMENT).getValue();
		obj.setDescription(comment);
		ClientQuantity clientQuantity = get(ADJUSTMENT_QTY).getValue();
		Quantity onhandQty = item.getOnhandQty();
		Quantity quantity = new Quantity();
		Unit unit = (Unit) HibernateUtil.getCurrentSession().get(Unit.class,
				clientQuantity.getUnit());
		quantity.setUnit(unit);
		quantity.setValue(clientQuantity.getValue());
		ClientQuantity qty = CommandUtils.subtractQuantities(quantity,
				onhandQty);
		obj.setQuantity(qty);
		Double unitPrice = get(UNIT_PRICE).getValue();
		obj.setUnitPrice(unitPrice);
		double lt = obj.getQuantity().getValue() * obj.getUnitPrice();
		double disc = obj.getDiscount();
		obj.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionItem obj) {
		Item item = (Item) CommandUtils.getServerObjectById(obj.getItem(),
				AccounterCoreType.ITEM);
		get(ITEM).setValue(item);
		get(COMMENT).setValue(obj.getDescription());
		get(ADJUSTMENT_QTY).setValue(obj.getQuantity());
	}

	@Override
	protected ClientTransactionItem getNewObject() {
		ClientTransactionItem adjustmentItem = new ClientTransactionItem();
		adjustmentItem.setDiscount(0.0D);
		adjustmentItem.setUnitPrice(0.0D);
		return adjustmentItem;
	}

	@Override
	protected Record createFullRecord(ClientTransactionItem t) {
		Record record = new Record(t);
		Item item = (Item) CommandUtils.getServerObjectById(t.getItem(),
				AccounterCoreType.ITEM);
		record.add(getMessages().itemName(), item != null ? item.getName() : "");
		record.add(getMessages().comment(), t.getDescription());
		Unit unit = (Unit) CommandUtils.getServerObjectById(t.getQuantity()
				.getUnit(), AccounterCoreType.UNIT);
		record.add(getMessages().currentQty(),
				getCurrentQuantity(t, item, unit));
		record.add(getMessages().adjustedQty(),
				getAdjustmentQuantity(t, item, unit));
		record.add(getMessages().salesOrPurchaseRate(), t.getUnitPrice());
		return record;
	}

	protected String getCurrentQuantity(ClientTransactionItem row, Item item,
			Unit unit) {
		if (item == null || item.getOnhandQty() == null
				|| row.getQuantity() == null) {
			return "";
		} else {
			Quantity onhandQty = item.getOnhandQty();
			StringBuffer data = new StringBuffer();
			data.append(String.valueOf(onhandQty.getValue()));
			if (getPreferences().isUnitsEnabled()) {
				data.append(" ");
				if (unit != null) {
					data.append(unit.getType());
				}
			}
			return data.toString();
		}
	}

	protected String getAdjustmentQuantity(ClientTransactionItem row,
			Item item, Unit clientUnit) {
		if (item == null || row.getQuantity() == null) {
			return "";
		}
		Quantity onhandQty = item.getOnhandQty();
		ClientQuantity quantity = row.getQuantity();
		Quantity quantity2 = new Quantity();
		quantity2.setUnit(clientUnit);
		quantity2.setValue(quantity.getValue());
		ClientQuantity qty = CommandUtils.addQuantities(onhandQty, quantity2);
		StringBuffer data = new StringBuffer();
		data.append(String.valueOf(qty.getValue()));
		if (getPreferences().isUnitsEnabled()) {
			data.append(" ");
			if (clientUnit != null) {
				data.append(clientUnit.getType());
			}
		}
		return data.toString();
	}

	protected abstract Warehouse getWareHouse();

	@Override
	protected List<ClientTransactionItem> getList() {
		return null;
	}

	@Override
	protected Record createRecord(ClientTransactionItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().items());
	}

}
