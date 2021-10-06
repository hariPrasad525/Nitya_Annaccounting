package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransactionReceiveVAT;

public abstract class ReceiveVatTableRequirement extends
		AbstractTableRequirement<ClientTransactionReceiveVAT> {

	private static final String VAT_AGENCY = "vatAgency";
	private static final String TAX_DUE = "taxDue";
	private static final String AMOUNT = "amount";

	public ReceiveVatTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		NameRequirement vatAgency = new NameRequirement(VAT_AGENCY, "",
				getMessages().vatAgency(), true, true);
		vatAgency.setEditable(false);
		list.add(vatAgency);

		AmountRequirement taxDue = new AmountRequirement(TAX_DUE, "",
				getMessages().taxDue(), true, true);
		taxDue.setEditable(false);
		list.add(taxDue);

		AmountRequirement amountReceive = new AmountRequirement(AMOUNT,
				getMessages().pleaseEnter(getMessages().amountToReceive()),
				getMessages().amountToReceive(), false, true);
		list.add(amountReceive);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(
				getMessages().noFiledTaxEntriesToReceive());
	}

	@Override
	protected void getRequirementsValues(ClientTransactionReceiveVAT obj) {
		Double amount = get(AMOUNT).getValue();
		obj.setAmountToReceive(amount);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionReceiveVAT obj) {
		ClientTAXAgency taxAgency = (ClientTAXAgency) CommandUtils
				.getClientObjectById(obj.getTaxAgency(),
						AccounterCoreType.TAXAGENCY, getCompanyId());
		if (taxAgency != null) {
			get(VAT_AGENCY).setDefaultValue(taxAgency.getName());
		}
		get(TAX_DUE).setDefaultValue(obj.getTaxDue());
	}

	@Override
	protected ClientTransactionReceiveVAT getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionReceiveVAT t) {
		ClientTAXAgency taxAgency = (ClientTAXAgency) CommandUtils
				.getClientObjectById(t.getTaxAgency(),
						AccounterCoreType.TAXAGENCY, getCompanyId());

		Record record = new Record(t);
		record.add(getMessages().vatAgency(), taxAgency.getName());
		record.add(getMessages().taxDue(), t.getTaxDue());
		record.add(getMessages().amountToReceive(), t.getAmountToReceive());
		return record;
	}

	@Override
	protected Record createRecord(ClientTransactionReceiveVAT t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().billsToReceive());
	}

}
