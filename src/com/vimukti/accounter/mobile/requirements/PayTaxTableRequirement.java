package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayrollTransactionPayTax;

public class PayTaxTableRequirement extends AbstractTableRequirement<ClientPayrollTransactionPayTax> {
	
	private static final String TRANSACTION_NO = "transactionNum";

	private static final String TRANS_DATE = "transactionDate";

	private static final String AMOUNT_DUE = "amountDue";

	private static final String PAYMENT = "payment";

	public PayTaxTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new NumberRequirement(TRANSACTION_NO, getMessages()
				.pleaseEnter(getMessages().number()), getMessages().number(),
				false, true) {
			@Override
			public boolean isEditable() {
				return false;
			}
		});

		list.add(new DateRequirement(TRANS_DATE, getMessages().pleaseSelect(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), false, true) {
			@Override
			public boolean isEditable() {
				return false;
			}
		});

		list.add(new CurrencyAmountRequirement(AMOUNT_DUE, getMessages()
				.pleaseEnter(getMessages().amountDue()), getMessages()
				.amountDue(), false, true) {

			@Override
			protected Currency getCurrency() {
				return getCompany().getPrimaryCurrency();
			}

			@Override
			public boolean isEditable() {
				return false;
			}
		});

		list.add(new CurrencyAmountRequirement(PAYMENT, getMessages()
				.pleaseEnter(getMessages().payment()), getMessages().payment(),
				false, true) {

			@Override
			protected Currency getCurrency() {
				return getCompany().getPrimaryCurrency();
			}

			@Override
			public boolean isEditable() {
				return false;
			}
		});
	}		
    
	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
    }

	@Override
	protected void getRequirementsValues(ClientPayrollTransactionPayTax obj) {
		obj.setPayment(obj.getAmountDue());
		
	}

	@Override
	protected void setRequirementsDefaultValues(ClientPayrollTransactionPayTax obj) {
		get(TRANS_DATE).setValue(new ClientFinanceDate(obj.getDate()));
		get(TRANSACTION_NO).setValue(obj.getPayRunNumber());
		get(AMOUNT_DUE).setValue(obj.getAmountDue());
		get(PAYMENT).setValue(obj.getPayment());		
	}

	@Override
	protected ClientPayrollTransactionPayTax getNewObject() {
		return null;
	}

	@Override
	protected Record createFullRecord(ClientPayrollTransactionPayTax t) {
		Record record = new Record(t);
		record.add(getMessages().number(), t.getPayRunNumber());
		record.add(getMessages().transactionDate(),
				new ClientFinanceDate(t.getDate()));
		record.add(getMessages().amountDue(), t.getAmountDue());
		record.add(getMessages().payment(), t.getPayment());
		return record;	
		}

	@Override
	protected Record createRecord(ClientPayrollTransactionPayTax t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return null;
	}

	@Override
	protected List<ClientPayrollTransactionPayTax> getList() {
		// TODO Auto-generated method stub
		return null;
	}
}
