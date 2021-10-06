package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class PaymentTermRequirement extends
		ListRequirement<PaymentTerms> {

	public PaymentTermRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<PaymentTerms> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(PaymentTerms value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		return record;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().paymentTerm());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getMessages().paymentTerm());
	}

	@Override
	protected String getDisplayValue(PaymentTerms value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newPaymentTerm");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().paymentTerm());
	}

	@Override
	protected boolean filter(PaymentTerms e, String name) {
		return e.getName().contains(name);
	}
}
