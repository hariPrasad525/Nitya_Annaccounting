package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author vimukti5
 * @Implementation Fernandez
 * 
 */

public class WriteOffDialog extends BaseDialog<ClientAccount> {

	List<ClientAccount> allAccounts;
	private ClientAccount selectedWriteOffAccount;
	private AmountField discAmtText;
	private Double writeOffAmount;

	private ClientTransactionReceivePayment record;
	public DynamicForm form;
	private boolean canEdit;
	OtherAccountsCombo discAccSelect;
	private ICurrencyProvider currencyProvider;

	public void setCashDiscountValue(Double cashDiscountValue) {
		if (cashDiscountValue == null)
			cashDiscountValue = 0.0D;
		this.writeOffAmount = cashDiscountValue;
		discAmtText.setAmount(cashDiscountValue);
	}

	public Double getCashDiscountValue() {
		writeOffAmount = discAmtText.getAmount();
		return writeOffAmount;
	}

	public WriteOffDialog(List<ClientAccount> allAccounts,
			ClientTransactionReceivePayment record, boolean canEdit,
			ClientAccount clientAccount, ICurrencyProvider currencyProvider) {
		super(messages.writeOff(), messages.writeOffPleaseAddDetails());
		this.currencyProvider = currencyProvider;
		this.record = record;
		this.allAccounts = allAccounts;
		this.setSelectedWriteOffAccount(clientAccount);
		this.canEdit = canEdit;
		this.getElement().setId("WriteOffDialog");
		createControls();
		setCashDiscountValue(record.getWriteOff());

	}

	public WriteOffDialog() {
		super(messages.cashDiscount(), messages.writeOffPleaseAddDetails());
		this.getElement().setId("WriteOffDialog");
		createControls();
	}

	private void createControls() {

		discAccSelect = new OtherAccountsCombo(messages.writeOffAccount(),
				false);
		discAccSelect.initCombo(allAccounts);
		discAccSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setSelectedWriteOffAccount(selectItem);
					}

				});
		discAccSelect.setRequired(true);
		discAccSelect.setEnabled(canEdit);
		if (getSelectedWriteOffAccount() != null)
			discAccSelect.setComboItem(getSelectedWriteOffAccount());

		discAmtText = new AmountField(messages.writeOffAmount(), this,
				currencyProvider.getTransactionCurrency(), "discAmtText");
		discAmtText.setEnabled(canEdit);
		setCashDiscountValue(writeOffAmount);

		discAmtText.addFocusHandler(new FocusHandler() {

			public void onFocus(FocusEvent event) {
				double amount = writeOffAmount != null ? writeOffAmount
						.doubleValue() : 0.0D;
				discAmtText.setAmount(amount);
				discAmtText.focusInItem();

			}

		});

		if (!canEdit) {

			// okbtn.hide();
			okbtn.setVisible(false);
			cancelBtn.setTitle(messages.close());

		}

		form = new DynamicForm("form");
		form.add(discAccSelect, discAmtText);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(form);

		setBodyLayout(mainVLay);
		ViewManager.getInstance().showDialog(this);
	}

	public ValidationResult validate() {
		// if (getSelectedWriteOffAccount() == null) {
		// AccounterValidator.validateForm(form, true);
		// }
		return form.validate();
	}

	@Override
	public Object getGridColumnValue(ClientAccount obj, int index) {

		return null;
	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	public void setSelectedWriteOffAccount(ClientAccount selectedWriteOffAccount) {
		this.selectedWriteOffAccount = selectedWriteOffAccount;
	}

	public ClientAccount getSelectedWriteOffAccount() {
		return selectedWriteOffAccount;
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}

}
