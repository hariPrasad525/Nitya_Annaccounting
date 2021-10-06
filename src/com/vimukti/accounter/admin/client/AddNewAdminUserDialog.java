package com.vimukti.accounter.admin.client;

import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddNewAdminUserDialog extends AdminBaseDialog<ClientAdminUser> {
	DynamicForm form;
	TextItem userNameBox, emailIdBox;
	PasswordItem passwordBox;

	public AddNewAdminUserDialog(String text) {
		super(text, "");
		this.getElement().setId("AddNewAdminUserDialog");
		createControls();
		center();
	}

	public void createControls() {
		form = new DynamicForm("addNewAdminUser");
		userNameBox = new TextItem(
				AdminHomePage.getAdminConstants().username(), "userNameBox");
		emailIdBox = new TextItem(AdminHomePage.getAdminConstants().emailID(),
				"emailIdBox");
		passwordBox = new PasswordItem(AdminHomePage.getAdminConstants()
				.password());
		form.add(userNameBox, emailIdBox, passwordBox);
		setBodyLayout(form);
	}

	protected void addNewAdminUser() {
		ClientAdminUser clientUser = new ClientAdminUser();
		clientUser.setName(UIUtils.toStr(userNameBox.getValue()));
		clientUser.setEmailId(UIUtils.toStr(emailIdBox.getValue()));
		clientUser.setPassword(UIUtils.toStr(passwordBox.getValue()));
		saveOrUpdate(clientUser);

	}

	@Override
	protected boolean onOK() {
		addNewAdminUser();
		return true;
	}
}
