package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.AddNewContactDialog;

public class ContactCombo extends CustomCombo<ClientContact> {

	private ValueCallBack<ClientContact> newContactHandler;

	public ContactCombo(String title) {
		super(title, false, 1, "contactCombo");
	}

	public ContactCombo(String contact, boolean b) {
		super(contact, b, 1, "contactCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.contact();
	}

	@Override
	public void onAddNew() {
		AddNewContactDialog addNewContactDialog = new AddNewContactDialog(
				messages.contact(), " ") {
			@Override
			protected boolean onOK() {
				if (super.onOK()) {
					ContactCombo.this.setSelected(createContact().getName());
					return true;
				} else {
					return false;
				}
			}
		};
		addNewContactDialog.addSuccessCallback(newContactHandler);
		ViewManager.getInstance().showDialog(addNewContactDialog);
	}

	public void setDefaultToFirstOption(boolean b) {
		if (b) {
			this.setComboItem(comboItems.get(0));
		}
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
	}

	@Override
	protected String getDisplayName(ClientContact object) {
		if (object != null)
			return object.getName().toString() != null ? object.getName()
					.toString() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientContact object, int col) {
		switch (col) {
		case 0:
			return object.getName().toString();
		}
		return null;
	}

	/**
	 * @param valueCallBack
	 */

	public void addNewContactHandler(
			ValueCallBack<ClientContact> newContactHandler) {
		this.newContactHandler = newContactHandler;
	}

}
