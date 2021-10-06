package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.web.client.core.Lists.PurchaseOrdersList;

public abstract class PurchaseOrderTableRequirements extends
		AbstractTableRequirement<PurchaseOrdersList> {

	public PurchaseOrderTableRequirements(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, false, isOptional,
				isAllowFromContext);
		setDefaultValue(new ArrayList<PurchaseOrdersList>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getMessages().purchaseOrder());
	}

	@Override
	protected void getRequirementsValues(PurchaseOrdersList obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setRequirementsDefaultValues(PurchaseOrdersList obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected PurchaseOrdersList getNewObject() {
		return new PurchaseOrdersList();
	}

	@Override
	protected Record createFullRecord(PurchaseOrdersList value) {
		Record rec = new Record(value);
		rec.add(getMessages().name(), value.getVendorName());
		rec.add(getMessages().balance(), value.getBalance());
		return rec;
	}

	@Override
	protected List<PurchaseOrdersList> getList() {
		// try {
		// return new FinanceTool().getPurchageManager()
		// .getPurchaseOrdersList(getCompanyId());
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
		return new ArrayList<PurchaseOrdersList>();
	}

	@Override
	protected Record createRecord(PurchaseOrdersList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<PurchaseOrdersList> oldValues = getValue();
		return oldValues.isEmpty() ? getMessages().addOf(
				getMessages().purchaseOrders()) : getMessages().addMore(
				getMessages().purchaseOrders());
	}

	@Override
	protected void addCreateCommands(CommandList commandList) {
		commandList.add("createPurchaseOrder");
	}

	protected abstract Vendor getVendor();

	@Override
	protected boolean contains(List<PurchaseOrdersList> oldValues,
			PurchaseOrdersList t) {
		for (PurchaseOrdersList purchaseOrdersList : oldValues) {
			if (purchaseOrdersList.getTransactionId() == t.getTransactionId()) {
				return true;
			}
		}
		return false;
	}
}
