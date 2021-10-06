package com.vimukti.accounter.web.client.ui.payroll;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;

public abstract class PayHeadColumn extends
		ComboColumn<ClientPayStructureItem, ClientPayHead> {
	PayHeadDropDownTable dropdown = new PayHeadDropDownTable(true);

	@Override
	protected ClientPayHead getValue(ClientPayStructureItem row) {
		return getPayHead(row.getPayHead());
	}

	@Override
	public AbstractDropDownTable<ClientPayHead> getDisplayTable(
			ClientPayStructureItem row) {
		return dropdown;
	}

	@Override
	public int getWidth() {
		return 120;
	}

	@Override
	protected String getColumnName() {
		return messages.payhead();
	}

	public List<ClientPayHead> getList() {
		return dropdown.getTotalRowsData();
	}

	public ClientPayHead getPayHead(long payhead) {
		for (ClientPayHead ph : getList()) {
			if (ph.getID() == payhead) {
				return ph;
			}
		}
		return null;
	}
}
