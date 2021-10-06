package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransactionPayTAX;

public abstract class PayVatTableRequirement extends
		AbstractTableRequirement<ClientTransactionPayTAX> {

	private static final String TAX_AGENCY = "taxAgency";
	private static final String TAX_ITEM = "taxItem";
	private static final String TAX_DUE = "taxDue";
	private static final String AMOUNT = "amount";

	public PayVatTableRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		NameRequirement vatAgency = new NameRequirement(TAX_AGENCY, "",
				getMessages().taxAgency(), true, true);
		vatAgency.setEditable(false);
		list.add(vatAgency);

		NameRequirement taxItem = new NameRequirement(TAX_ITEM, "",
				getMessages().taxItem(), true, true);
		taxItem.setEditable(false);
		list.add(taxItem);

		AmountRequirement taxDue = new AmountRequirement(TAX_DUE, "",
				getMessages().taxDue(), true, true);
		taxDue.setEditable(false);
		list.add(taxDue);

		AmountRequirement amountPay = new AmountRequirement(AMOUNT,
				getMessages().pleaseEnter(getMessages().amountToPay()),
				getMessages().amountToPay(), false, true);
		list.add(amountPay);

	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().noFiledTaxEntriesToReceive());
	}

	@Override
	protected void getRequirementsValues(ClientTransactionPayTAX obj) {
		Double amount = get(AMOUNT).getValue();
		obj.setAmountToPay(amount);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionPayTAX obj) {
		ClientTAXAgency taxAgency = (ClientTAXAgency) CommandUtils
				.getClientObjectById(obj.getTaxAgency(),
						AccounterCoreType.TAXAGENCY, getCompanyId());
		get(TAX_AGENCY).setDefaultValue(taxAgency.getName());
		get(TAX_DUE).setDefaultValue(obj.getTaxDue());
	}

	@Override
	protected ClientTransactionPayTAX getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionPayTAX t) {
		ClientTAXAgency taxAgency = (ClientTAXAgency) CommandUtils
				.getClientObjectById(t.getTaxAgency(),
						AccounterCoreType.TAXAGENCY, getCompanyId());

		Record record = new Record(t);
		record.add(getMessages().vatAgency(), taxAgency.getName());
		record.add(getMessages().taxDue(), t.getTaxDue());
		record.add(getMessages().amountToPay(), t.getAmountToPay());
		return record;
	}

	@Override
	protected Record createRecord(ClientTransactionPayTAX t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().billsToPay());
	}

}
