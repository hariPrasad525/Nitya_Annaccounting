package com.nitya.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.company.ChartOfAccountsAction;

public class BankAccountsPortlet extends Portlet {
	private DashBoardBankAccountGrid grid;

	public BankAccountsPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.bankAccounts(), messages
				.gotoBankAccountsList(), "60%");
		this.getElement().setId("BankAccountsPortlet");
	}

	@Override
	public void goToClicked() {
		new ChartOfAccountsAction(ClientAccount.TYPE_BANK).run();
	}

	@Override
	public void createBody() {
		Accounter.createHomeService().getAccounts(ClientAccount.TYPE_BANK,
				true, 0, -1,
				new AsyncCallback<PaginationList<ClientAccount>>() {

					@Override
					public void onSuccess(PaginationList<ClientAccount> result) {
						grid = new DashBoardBankAccountGrid();
						grid.init();
						if (result != null && !(result.isEmpty())) {
							grid.setRecords(result);
						} else {
							grid.addEmptyMessage(messages.noBanksToShow());
						}
						body.add(grid);
						completeInitialization();
					}

					@Override
					public void onFailure(Throwable caught) {
						completeInitialization();
					}
				});
		grid = new DashBoardBankAccountGrid();

	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
