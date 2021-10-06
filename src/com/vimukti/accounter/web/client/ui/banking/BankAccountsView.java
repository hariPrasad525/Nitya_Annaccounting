package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AccoutsPortlet;
import com.vimukti.accounter.web.client.ui.CustomLabel;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class BankAccountsView extends BaseView<ClientAccount> {

	private ArrayList<ClientAccount> bankAccounts = new ArrayList<ClientAccount>();

	public BankAccountsView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("BankAccountsView");
		createControls();
		if (saveAndCloseButton != null)
			saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(false);
	}

	private void createControls() {
		StyledPanel mainPanel = new StyledPanel("mainPanel");
		CustomLabel titleLabel = new CustomLabel(messages.bankAccounts());
		titleLabel.setStyleName("label-title");
		Button button = new Button(messages.newAccount());
		mainPanel.add(titleLabel);
		mainPanel.add(button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new NewAccountAction(ClientAccount.TYPE_BANK).run();

			}
		});
		bankAccounts = getCompany().getActiveBankAccounts(
				ClientAccount.TYPE_BANK);
		for (ClientAccount account : bankAccounts) {
			AccoutsPortlet accoutsPortlet = new AccoutsPortlet(account);
			accoutsPortlet.refreshWidget();
			mainPanel.add(accoutsPortlet);
		}
		this.add(mainPanel);
	}

	@Override
	public void initData() {
		super.initData();
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
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
