package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientBank;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.company.AddBankDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class BankNameCombo extends CustomCombo<ClientBank> {

	public BankNameCombo(String title) {
		super(title, "bankNameCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.bankName();
	}

	@Override
	protected String getDisplayName(ClientBank object) {
		if (object != null)
			return object.getDisplayName();
		else
			return "";
	}

	@Override
	public void onAddNew() {
		AddBankDialog bankNameDialog = new AddBankDialog(null);
		bankNameDialog.addCallBack(new AccounterAsyncCallback<ClientBank>() {

			@Override
			public void onResultSuccess(ClientBank result) {
				createAddNewCallBack().onResultSuccess(result);
			}

			@Override
			public void onException(AccounterException exception) {
				createAddNewCallBack().onException(exception);
			}
		});
		ViewManager.getInstance().showDialog(bankNameDialog);
	}

	@Override
	protected String getColumnData(ClientBank object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
