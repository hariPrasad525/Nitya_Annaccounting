package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;

public class TaxAgencyColumn<T> extends ComboColumn<T, ClientTAXAgency> {

	@Override
	protected ClientTAXAgency getValue(T row) {
		return null;
	}

	@Override
	public AbstractDropDownTable<ClientTAXAgency> getDisplayTable(T row) {
		return new TaxAgencyTable();
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	public String getValueAsString(T row) {
		return null;
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}

	@Override
	protected void setValue(T row, ClientTAXAgency newValue) {
		
	}
	
	@Override
	protected String getColumnName() {
		return Global.get().messages().taxAgency();
	}

}
