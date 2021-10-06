package com.nitya.accounter.web.client.ui;

import java.util.LinkedHashMap;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.core.IGenericCallback;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author venki.p
 * 
 */

public class CashDiscountDialog extends BaseDialog<ClientAccount> {
	List<ClientAccount> allAccounts;
	private int key;

	private LinkedHashMap<String, ClientAccount> discountAccounts;

	public ClientAccount selectedDiscountAccount;
	private AmountField discAmtText;
	private Double cashDiscountValue = 0.0d;
	private IGenericCallback<String> callback;
	private boolean canEdit;
	OtherAccountsCombo discAccSelect;
	public DynamicForm form;
	private ICurrencyProvider currencyProvider;

	public CashDiscountDialog(List<ClientAccount> allAccounts,
			Double cashDiscountValue, IGenericCallback<String> callback) {
		super(messages.cashDiscount(), messages.cashDiscountPleaseAddDetails());
		this.getElement().setId("CashDiscountDialog");
		this.callback = callback;
		this.allAccounts = allAccounts;
		this.cashDiscountValue = cashDiscountValue;
		createControls();
		ViewManager.getInstance().showDialog(this);
	}

	public CashDiscountDialog() {
		super(messages.cashDiscount(), messages.cashDiscountPleaseAddDetails());
		createControls();
		this.getElement().setId("CashDiscountDialog");
	}

	public CashDiscountDialog(boolean canEdit, Double discountValue,
			ClientAccount account, ICurrencyProvider currencyProvider) {
		super(messages.cashDiscount(), messages.cashDiscountPleaseAddDetails());
		this.getElement().setId("CashDiscountDialog");
		this.currencyProvider = currencyProvider;
		this.cashDiscountValue = discountValue;
		this.canEdit = canEdit;
		this.selectedDiscountAccount = account;
		createControls();
	}

	public void setAllAccounts(List<ClientAccount> allAccounts) {
		this.allAccounts = allAccounts;
	}

	public void setSelectedDiscountAccount(ClientAccount selectedDiscountAccount) {
		this.selectedDiscountAccount = selectedDiscountAccount;
	}

	public ClientAccount getSelectedDiscountAccount() {
		return this.selectedDiscountAccount;
	}

	public void setCashDiscountValue(Double cashDiscountValue) {
		this.cashDiscountValue = cashDiscountValue;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	private void createControls() {

		discAccSelect = new OtherAccountsCombo(messages.discountAccount(),
				false);

		discAccSelect.setComboItem(selectedDiscountAccount);

		discAccSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setSelectedDiscountAccount(selectItem);
					}

				});
		discAccSelect.setRequired(true);

		discAmtText = new AmountField(messages.discountAmount(), this,
				currencyProvider.getTransactionCurrency(), "discAmtText");
		discAmtText.setAmount(cashDiscountValue);

		form = new DynamicForm("discountForm");
		form.add(discAccSelect, discAmtText);
		if (!canEdit) {
			discAccSelect.setEnabled(false);
			discAmtText.setEnabled(false);
			form.setEnabled(false);
		}

		setBodyLayout(form);
	}

	@Override
	public Object getGridColumnValue(ClientAccount obj, int index) {
		return null;
	}

	@Override
	public ValidationResult validate() {
		// if (getSelectedDiscountAccount() == null) {
		ValidationResult result = new ValidationResult();
		result.add(form.validate());
		if (discAmtText.getAmount() < 0) {
			result.addError(discAmtText,
					messages.pleaseEnter(messages.discountAmount()));
		}
		return result;
		// }
		// return true;
	}

	public Double getCashDiscount() {
		cashDiscountValue = discAmtText.getAmount();
		return cashDiscountValue;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	public void setFocus() {
		discAccSelect.setFocus();
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}