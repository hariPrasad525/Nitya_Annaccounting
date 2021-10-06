package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientTAXItem;

public abstract class TaxItemsTableRequirement extends
		AbstractTableRequirement<ClientTAXItem> {

	private static final String NAME = "name";
	private static final String TAX_RATE = "taxRate";

	public TaxItemsTableRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		NameRequirement name = new NameRequirement(NAME, "", getMessages()
				.name(), true, true);
		name.setEditable(false);
		list.add(name);

		AmountRequirement taxRate = new AmountRequirement(TAX_RATE, "",
				getMessages().taxRate(), true, true);
		taxRate.setEditable(false);
		list.add(taxRate);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().taxItem());
	}

	@Override
	protected void getRequirementsValues(ClientTAXItem obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setRequirementsDefaultValues(ClientTAXItem obj) {
		get(NAME).setDefaultValue(obj.getName());
		get(TAX_RATE).setDefaultValue(obj.getTaxRate());
	}

	@Override
	protected ClientTAXItem getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTAXItem t) {
		Record record = new Record(t);
		record.add(getMessages().name(), t.getName());
		record.add(getMessages().taxRate(), t.getTaxRate());
		return record;
	}

	@Override
	protected Record createRecord(ClientTAXItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().taxItem());
	}

}
