package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.reports.AccountRegister;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.combo.DepositInAccountCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.PayeeCombo;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.AccountRegisterListGrid;

public class AccountRegisterView extends AbstractBaseView<AccountRegister> {

	AccountRegisterListGrid grid;

	private DepositInAccountCombo bankAccSelect;
	PayeeCombo paytoSelect;
	private final ClientAccount takenaccount;
	private ClientAccount account;
	protected List<AccountRegister> accountRegister;
	private AccountRegister accRegister;
	private AccountRegisterListGrid grid2;
	private StyledPanel mainVLay;
	private StyledPanel hlayTop, gridLayout;

	private Label lab1;

	private List<ClientAccount> listOfAccounts;
	private double total = 0.0;
	private boolean isBankActGrid = true;

	public AccountRegisterView(ClientAccount account2) {
		super();
		this.takenaccount = account2;

	}

	@Override
	public void init() {
		createControls();
	}

	@Override
	public void initData() {
		// initAccountsToList();
		getDepositInAccounts();
		// initPayToCombo();
		super.initData();
		accountSelected(takenaccount);
	}

	public void initAccountsToList() {
		bankAccSelect.initCombo(getCompany().getActiveAccounts());

	}

	public void getDepositInAccounts() {

		List<ClientAccount> listOfAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_BANK
					|| account.getType() == ClientAccount.TYPE_CREDIT_CARD) {
				listOfAccounts.add(account);
			}
		}
		bankAccSelect.initCombo(listOfAccounts);
		bankAccSelect.setComboItem(takenaccount);

	}

	protected void createControls() {

		bankAccSelect = new DepositInAccountCombo(messages.bankAccount());
		bankAccSelect.setRequired(true);

		bankAccSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						ClientAccount selectBankAccMethod = selectItem;

						if (selectBankAccMethod.getType() == ClientAccount.TYPE_BANK) {
							if (!isBankActGrid()) {
								grid2.removeAllRecords();
								gridLayout.remove(grid2);

								gridLayout.add(grid);
								mainVLay.add(hlayTop);
								mainVLay.add(gridLayout);
								setBankActGrid(true);
							}

							accountSelected(selectBankAccMethod);

						} else if (selectBankAccMethod.getType() == ClientAccount.TYPE_CREDIT_CARD) {
							if (isBankActGrid()) {
								grid.removeAllRecords();
								gridLayout.remove(grid);

								gridLayout.add(grid2);
								mainVLay.add(hlayTop);
								mainVLay.add(gridLayout);
								setBankActGrid(false);
							}

							accountSelected(selectBankAccMethod);

						}

					}

				});

		StyledPanel hlay = new StyledPanel("hlay");
		DynamicForm form = new DynamicForm("form");

		form.add(bankAccSelect);

		hlay.add(form);

		hlayTop = new StyledPanel("hlayTop");

		hlayTop.add(hlay);

		lab1 = new Label(messages.accountRegister() + " - "
				+ takenaccount.getName());

		grid = new AccountRegisterListGrid(false, ClientAccount.TYPE_BANK);
		grid.addStyleName("listgrid-tl");
		grid.init();

		grid2 = new AccountRegisterListGrid(false,
				ClientAccount.TYPE_CREDIT_CARD);
		// grid2.addStyleName("listgrid-tl");
		grid2.init();

		gridLayout = new StyledPanel("gridLayout");

		mainVLay = new StyledPanel("mainVLay");

		if (takenaccount.getType() == ClientAccount.TYPE_BANK) {
			gridLayout.add(grid);
			mainVLay.add(hlayTop);
			mainVLay.add(gridLayout);
		} else if (takenaccount.getType() == ClientAccount.TYPE_CREDIT_CARD) {
			setBankActGrid(false);
			gridLayout.add(grid2);
			mainVLay.add(hlayTop);
			mainVLay.add(gridLayout);
		}

		add(mainVLay);

		setSize("100%", "100%");

	}

	public void getAccountRegisterGrid(List<AccountRegister> result) {
		grid.removeAllRecords();
		grid.balance = 0.0;
		grid.deposit = 0.0;
		grid.payment = 0.0;
		grid.totalBalance = 0.0;
		grid2.removeAllRecords();
		grid2.balance = 0.0;
		grid2.deposit = 0.0;
		grid2.payment = 0.0;
		grid2.totalBalance = 0.0;
		if (accountRegister != null) {

			for (int i = 0; i < accountRegister.size(); i++) {
				accRegister = this.accountRegister.get(i);
				if (account.getType() == ClientAccount.TYPE_BANK) {
					grid.addData(accRegister);
					// this.total += accRegister.getBalance();
				} else {
					grid2.addData(accRegister);
					// this.total += accRegister.getBalance();
				}
			}
		}
		// grid.updateFooterValues(FinanceApplication.constants()
		// .endingbalance(), 7);
		// grid.addFooterValue(DataUtils.getAmountAsString(this.account
		// .getCurrentBalance()), 8);
		// grid2.updateFooterValues(FinanceApplication.constants()
		// .endingbalance(), 7);
		// grid2.addFooterValue(DataUtils.getAmountAsString(this.account
		// .getCurrentBalance()), 8);
		this.total = 0;

	}

	protected void accountSelected(final ClientAccount takenaccount) {

		if (takenaccount == null) {
			accountRegister = null;
			return;
		}

		this.account = takenaccount;

		grid.setAccount(takenaccount);
		grid2.setAccount(takenaccount);

		ClientFinanceDate endDate = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();

		if (endDate == null)
			endDate = new ClientFinanceDate();

		// Accounter.createReportService().getAccountRegister(
		// Accounter.getStartDate(), endDate, takenaccount.getID(),
		// new AccounterAsyncCallback<ArrayList<AccountRegister>>() {
		//
		// @Override
		// public void onException(AccounterException caught) {
		// Accounter.showError(messages
		// .failedtoGetListofAccounts(takenaccount
		// .getName()));
		//
		// }
		//
		// @Override
		// public void onResultSuccess(
		// ArrayList<AccountRegister> result) {
		// accountRegister = result;
		//
		// getAccountRegisterGrid(result);
		//
		// }
		//
		// });

	}

	protected void clearFields() {
	}

	public boolean isBankActGrid() {
		return isBankActGrid;
	}

	public void setBankActGrid(boolean isBankActGrid) {
		this.isBankActGrid = isBankActGrid;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		grid.setHeight(height - 140 + "px");
		grid2.setHeight(height - 140 + "px");

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.accountRegister();

	}

	@Override
	public void saveFailed(AccounterException exception) {
	}
}
