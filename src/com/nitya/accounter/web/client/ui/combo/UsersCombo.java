package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientUserInfo;

public class UsersCombo extends CustomCombo<ClientUserInfo> {

	public UsersCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 2,"UsersCombo");
	}

	public UsersCombo(String title) {
		super(title,"UsersCombo");
	}

	public void init() {
		initCombo(getCompany().getUsersList());
		setComboItem(getCompany().getUsersList().get(0));
	}

	@Override
	protected String getDisplayName(ClientUserInfo object) {
		if (object != null)
			return object.getDisplayName() != null ? object.getDisplayName()
					: object.getName();
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientUserInfo object, int col) {
		switch (col) {
		case 0:
			return object.getFullName();
		case 1:
			return object.getUserRole();
		default:
			break;
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	public void onAddNew() {

	}

}
