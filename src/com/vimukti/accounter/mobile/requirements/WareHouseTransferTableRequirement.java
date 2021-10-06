package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientStockTransferItem;

public abstract class WareHouseTransferTableRequirement extends
		AbstractTableRequirement<ClientStockTransferItem> {
	private static final String ITEM_NAME = "item_name";
	private static final String QUANTITY = "quantity";

	public WareHouseTransferTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		StringRequirement itemNameReq = new StringRequirement(ITEM_NAME,
				getMessages().pleaseEnter(getMessages().itemName()),
				getMessages().itemName(), true, true);
		itemNameReq.setEditable(false);
		list.add(itemNameReq);

		list.add(new QuantityRequirement(QUANTITY, getMessages().pleaseEnter(
				getMessages().adjustedQty()), getMessages().adjustedQty(),
				false) {

			@Override
			protected List<Unit> getLists(Context context) {
				return WareHouseTransferTableRequirement.this.getUnits();
			}
		});
	}

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected void getRequirementsValues(ClientStockTransferItem obj) {
		ClientQuantity clientQuantity = get(QUANTITY).getValue();
		obj.setQuantity(clientQuantity);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientStockTransferItem obj) {
		Item item = (Item) CommandUtils.getServerObjectById(obj.getItem(),
				AccounterCoreType.ITEM);
		get(ITEM_NAME).setValue(item.getName());
		get(QUANTITY).setValue(obj.getQuantity());
		ClientQuantity value2 = get(QUANTITY).getValue();
		value2.setUnit(item.getMeasurement().getDefaultUnit().getID());
		QuantityRequirement requirement = (QuantityRequirement) get(QUANTITY);
		requirement.setDefaultUnit(item.getMeasurement().getDefaultUnit());
	}

	@Override
	protected ClientStockTransferItem getNewObject() {
		return null;
	}

	@Override
	protected Record createFullRecord(ClientStockTransferItem t) {
		Record record = new Record(t);
		ClientItem item = (ClientItem) CommandUtils.getClientObjectById(
				t.getItem(), AccounterCoreType.ITEM, getCompanyId());
		record.add(getMessages().itemName(),
				item == null ? " " : item.getName());
		String totalQty = " ";
		if (item == null) {
			totalQty = String.valueOf(t.getTotalQuantity().getValue());
		} else {
			Unit unit = null;
			StringBuffer data = new StringBuffer();
			if (t.getTotalQuantity() != null) {
				unit = (Unit) CommandUtils.getServerObjectById(t
						.getTotalQuantity().getUnit(), AccounterCoreType.UNIT);
				data.append(String.valueOf(t.getTotalQuantity().getValue()));
				data.append(" ");
			}
			if (unit != null) {
				data.append(unit.getType());
			}
			totalQty = data.toString();
		}
		record.add(getMessages().totalQuantity(), totalQty);
		String quantity = "";
		if (item == null || t.getQuantity() == null) {
			quantity = "";
		} else {
			Unit unit = null;
			StringBuffer data = new StringBuffer();
			if (t.getQuantity() != null) {
				unit = (Unit) CommandUtils.getServerObjectById(t.getQuantity()
						.getUnit(), AccounterCoreType.UNIT);
				data.append(String.valueOf(t.getQuantity().getValue()));
				data.append(" ");
			}
			if (unit != null) {
				data.append(unit.getType());
			}
			quantity = data.toString();
		}
		record.add(getMessages().transferQuantity(), quantity);
		return record;
	}

	@Override
	protected Record createRecord(ClientStockTransferItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().items());
	}

	@Override
	protected boolean contains(List<ClientStockTransferItem> oldValues,
			ClientStockTransferItem t) {
		for (ClientStockTransferItem clientStockTransferItem : oldValues) {
			if (clientStockTransferItem.getItem() == t.getItem()) {
				return true;
			}
		}
		return false;
	}

	protected List<Unit> getUnits() {
		String itemName = get(ITEM_NAME).getValue();
		Item item = CommandUtils.getItemByName(getCompany(), itemName);
		Set<Unit> units = item.getMeasurement().getUnits();
		return new ArrayList<Unit>(units);
	}

	@Override
	public void setEditable(boolean isEditable) {
		get(QUANTITY).setEditable(isEditable);
		super.setEditable(isEditable);
	}
}
