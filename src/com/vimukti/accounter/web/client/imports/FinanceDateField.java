package com.vimukti.accounter.web.client.imports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ImportField;

public class FinanceDateField extends ImportField {

	private ClientFinanceDate financeDate;

	private String dateFormate;

	public FinanceDateField() {
		super();
	}

	public FinanceDateField(String name, String displayName) {
		super(name, displayName);
	}

	public FinanceDateField(String name, String displayName, boolean isRequired) {
		super(name, displayName, isRequired);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ClientFinanceDate getValue() {
		return financeDate;
	}

	public void setValue(ClientFinanceDate value) {
		this.financeDate = value;
	}

	public boolean validate(String value) {
		ClientFinanceDate date = Global.get().stringAsFinanceDate(value,
				dateFormate);
		if (date == null) {
			return false;
		} else {
			this.setValue(date);
		}
		return true;
	}

	@Override
	public String getValueAsString() {
		if (getValue() == null) {
			return null;
		}
		return getValue().toString();
	}

	public String getDateFormate() {
		return dateFormate;
	}

	public void setDateFormate(String dateFormate) {
		this.dateFormate = dateFormate;
	}

}
