/**
 * 
 */
package com.nitya.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.ReconciliationAccountCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.InvoicePrintDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

/**
 * @author Prasanna Kumar G
 * 
 */

public class ReconciliationsHistoryView extends BaseView<ClientReconciliation> {

	private ReconciliationAccountCombo bankAccountsCombo;
	private ReconciliationsTable grid;
	private ClientAccount selectedAccount;
	private List<ClientReconciliation> listOfReconcilation;

	private void createControls() {

		this.bankAccountsCombo = new ReconciliationAccountCombo(
				messages.selectBankAccount());
		this.bankAccountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						bankAccountChanged(selectItem);
					}
				});
		this.grid = new ReconciliationsTable();

		StyledPanel mainPanel = new StyledPanel("mainPanel");
		DynamicForm form = new DynamicForm("form");
		form.add(bankAccountsCombo);
		Label label = new Label(messages.ReconciliationsList());
		label.setStyleName("label-title");

		mainPanel.add(form);
		mainPanel.add(label);
		mainPanel.add(grid);
		// mainPanel.setCellHeight(grid, "200px");
		grid.getElement().getParentElement()
				.addClassName("recounciliation_grid");
		this.add(mainPanel);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setVisible(false);
		if (saveAndNewButton != null)
			saveAndNewButton.setVisible(false);
	}

	/**
	 * @param selectItem
	 */
	protected void bankAccountChanged(ClientAccount clientAccount) {
		this.selectedAccount = clientAccount;
		rpcGetService.getReconciliationsByBankAccountID(
				selectedAccount.getID(),
				new AccounterAsyncCallback<ArrayList<ClientReconciliation>>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError(exception.getMessage());
					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientReconciliation> result) {
						listOfReconcilation = result;
						grid.setData(result);

					}
				});
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("ReconciliationsHistoryView");
		createControls();
	}

	@Override
	public void initData() {
		ArrayList<ClientAccount> bankAccounts = Accounter.getCompany()
				.getAccounts(ClientAccount.TYPE_BANK);
		if (bankAccounts == null || bankAccounts.isEmpty()) {
			grid.setData(new ArrayList<ClientReconciliation>());
			return;
		}
		for (ClientAccount account : bankAccounts) {
			bankAccountsCombo.addItem(account);
		}
		if (bankAccountsCombo.getSelectedValue() == null) {
			bankAccountsCombo.setSelectedItem(0);
		}
		bankAccountChanged(bankAccountsCombo.getSelectedValue());
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {
		this.bankAccountsCombo.setFocus();

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	public void print() {
	}

	@Override
	public void printPreview() {

	}

	public void showDialogBox(String description) {
		InvoicePrintDialog printDialog = new InvoicePrintDialog(
				messages.selectReports(), "", description);
		ViewManager.getInstance().showDialog(printDialog);
	}

	@Override
	public ClientReconciliation saveView() {
		ClientReconciliation reconciliation = new ClientReconciliation();
		reconciliation.setAccount(bankAccountsCombo.getSelectedValue());
		return reconciliation;
	}

	@Override
	public void restoreView(ClientReconciliation reconciliation) {
		if (reconciliation == null) {
			return;
		}
		if (reconciliation.getAccount() != null) {
			ClientAccount account = reconciliation.getAccount();
			ClientAccount account2 = getCompany().getAccount(account.getID());
			if (account2 != null) {
				bankAccountsCombo.setSelected(reconciliation.getAccount()
						.getName());
				bankAccountChanged(bankAccountsCombo.getSelectedValue());
			}
		}
	}
}
