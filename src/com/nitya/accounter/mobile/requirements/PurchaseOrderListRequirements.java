package com.nitya.accounter.mobile.requirements;

import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Lists.PurchaseOrdersList;

public abstract class PurchaseOrderListRequirements extends
		ListRequirement<PurchaseOrdersList> {

	public PurchaseOrderListRequirements(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext,
			ChangeListner<PurchaseOrdersList> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getMessages().purchaseOrders());
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().purchaseOrder());
	}

	@Override
	protected Record createRecord(PurchaseOrdersList value) {
		Record rec = new Record(value);
		if (value.getType() == ClientTransaction.TYPE_PURCHASE_ORDER) {
			rec.add(getMessages().purchaseOrder(), value.getPurchasePrice());
		}
		return rec;
	}

	@Override
	protected String getDisplayValue(PurchaseOrdersList value) {

		return value != null ? value.getVendorName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("addNewPurchaseOrder");

	}

	@Override
	protected String getSelectString() {
		return getMessages().selectTypeOfThis(getMessages().purchaseOrder());
	}
}
