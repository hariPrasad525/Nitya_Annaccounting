package com.nitya.accounter.web.client.ui.combo;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientEmailAccount;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.EmailAccountDialog;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class EmailCombo extends CustomCombo<ClientEmailAccount> {

	public EmailCombo(String title) {
		super(title, "emailCombo");
		initCombo(getEmailAccounts());
	}

	public EmailCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1, "emailCombo");
		initCombo(getEmailAccounts());
	}

	public List<ClientEmailAccount> getEmailAccounts() {
		// String companyEmail = Accounter.getCompany().getCompanyEmail();
		//
		// ArrayList<String> toAdd = new ArrayList<String>();
		// if (companyEmail != null && companyEmail.trim().length() != 0)
		// toAdd.add(companyEmail);
		//
		// String userEmail = getCompany().getLoggedInUser().getEmail();
		// if (userEmail != null && userEmail.trim().length() != 0)
		// toAdd.add(userEmail);

		return Accounter.getCompany().getEmailAccounts();
	}

	@Override
	protected String getDisplayName(ClientEmailAccount object) {
		return object.getEmailId() != null ? object.getEmailId() : "";
	}

	@Override
	protected String getColumnData(ClientEmailAccount object, int col) {
		switch (col) {
		case 0:
			return object.getEmailId();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.email();
	}

	@Override
	public void onAddNew() {
		EmailAccountDialog dialog = new EmailAccountDialog(null);
		dialog.setCallback(getActionCallBack());
		ViewManager.getInstance().showDialog(dialog);
	}

	private ActionCallback<ClientEmailAccount> getActionCallBack() {
		ActionCallback<ClientEmailAccount> callBack = new ActionCallback<ClientEmailAccount>() {

			@Override
			public void actionResult(ClientEmailAccount result) {
				setComboItem(result);
				if (getHandler() != null) {
					getHandler().selectedComboBoxItem(result);
				}
			}
		};
		return callBack;
	}
}
