package com.vimukti.accounter.admin.client;

import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public class AdminUsersListView extends AdminAbstractBaseView<ClientAdminUser> {

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		ChartOfAdminUsersList chartOfAdminList = new ChartOfAdminUsersList();
		StyledPanel vpanel = new StyledPanel("vpanel");
		vpanel.add(chartOfAdminList);
		add(vpanel);

	}

	@Override
	protected String getViewTitle() {

		return "Admin Users List";
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public Widget getGrid() {
		// TODO Auto-generated method stub
		return null;
	}

}
