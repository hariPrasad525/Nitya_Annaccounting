package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.PayRollActions;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;

public class PayHeadDropDownTable extends AbstractDropDownTable<ClientPayHead> {

	public PayHeadDropDownTable(boolean isAddNewRequired) {
		super(new ArrayList<ClientPayHead>(), isAddNewRequired);
		reInitData();
	}

	@Override
	public List<ClientPayHead> getTotalRowsData() {
		return Accounter.getCompany().getPayHeads();
	}

	@Override
	protected ClientPayHead getAddNewRow() {
		ClientPayHead payhead = new ClientPayHead();
		payhead.setName(messages.comboDefaultAddNew(messages.payhead()));
		return payhead;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientPayHead> textColumn = new TextColumn<ClientPayHead>() {

			@Override
			public String getValue(ClientPayHead object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected boolean filter(ClientPayHead t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientPayHead value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem(String text) {
		PayRollActions action = PayRollActions.newPayHeadAction();
		action.setCallback(new ActionCallback<ClientPayHead>() {

			@Override
			public void actionResult(ClientPayHead result) {
				selectRow(result);
			}
		});
		action.run(null, true);
	}

	@Override
	protected void addNewItem() {
		addNewItem("");

	}

	public ClientPayHead getPayHead(long payHead) {
		for (ClientPayHead ph : getTotalRowsData()) {
			if (ph.getID() == payHead) {
				return ph;
			}
		}
		return null;
	}

}
