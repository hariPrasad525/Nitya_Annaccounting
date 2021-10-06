package com.nitya.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.PriceLevel;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;

public class PriceLevelRequirement extends ListRequirement<PriceLevel> {

	public PriceLevelRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, true, true, null);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().priceLevelList());
	}

	@Override
	protected String getSetMessage() {
		return getMessages().pleaseSelect(getMessages().priceLevel());
	}

	@Override
	protected Record createRecord(PriceLevel value) {
		Record priceLevelRecord = new Record(value);
		priceLevelRecord.add(getMessages().priceLevel(), value.getName());
		priceLevelRecord.add(getMessages().percentage(), value.getPercentage());
		return priceLevelRecord;
	}

	@Override
	protected String getDisplayValue(PriceLevel value) {
		return value != null ? value.getName() : null;
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("addNewPriceLevel");
	}

	@Override
	protected String getSelectString() {
		return getMessages().hasSelected(getMessages().priceLevel());
	}

	@Override
	protected boolean filter(PriceLevel e, String name) {
		return e.getName().equals(name);
	}

	@Override
	protected List<PriceLevel> getLists(Context context) {
		return new ArrayList<PriceLevel>(context.getCompany().getPriceLevels());
	}

}
