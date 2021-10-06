package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.ui.banking.SelectPayeeDialog;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class PayeeCombo extends CustomCombo<ClientPayee> {

	public PayeeCombo(String title) {
		super(title, "PayeeCombo");
	}

	public PayeeCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "PayeeCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.payee();
	}

	@Override
	protected String getDisplayName(ClientPayee object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {

		SelectPayeeDialog dialog = new SelectPayeeDialog();
		dialog.setCallback(new ActionCallback<ClientPayee>() {

			@Override
			public void actionResult(ClientPayee result) {
				addItemThenfireEvent(result);
			}
		});
		ViewManager.getInstance().showDialog(dialog);

		// NewPayeeAction action = ActionFactory.getNewPayeeAction();
		// action.setCallback(new ActionCallback<ClientPayee>() {
		//
		// @Override
		// public void actionResult(ClientPayee result) {
		// addItemThenfireEvent(result);
		// }
		// });
		// action.run(this, null, true);
	}

	// NewPayeeAction action = ActionFactory.getNewPayeeAction();
	// action.setCallback(new ActionCallback<ClientPayee>() {
	//
	// @Override
	// public void actionResult(ClientPayee result) {
	// addItemThenfireEvent(result);
	// }
	// });
	// action.run(this, null, true);

	@Override
	protected String getColumnData(ClientPayee object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
