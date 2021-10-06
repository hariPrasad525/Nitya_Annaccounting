package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.ClientPayStructureDestination;
import com.nitya.accounter.web.client.core.ClientPayrollPayTax;
import com.nitya.accounter.web.client.core.ClientPayrollTransactionPayTax;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.BankAccountCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.PayheadCombo;
import com.nitya.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

public class PayTaxView extends AbstractTransactionBaseView<ClientPayrollPayTax> {
	
    private PayheadCombo payheadCombo;
	private BankAccountCombo bankAccountCombo;  
	private PayTaxTable table;
	private List<DynamicForm> listforms;
	public double totalOrginalAmt = 0.0D, totalDueAmt = 0.0D,
			totalPayment = 0.0D;
	
	String[] types = { messages.deductionsForEmployees(),
			 };
	
	 public PayTaxView() {
		super(ClientTransaction.TYPE_PAY_TAX);
		this.getElement().setId("pay-tax-view");
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		
	}

	@Override
	protected void createControls() {

		listforms = new ArrayList<DynamicForm>();
		StyledPanel mainPanel = new StyledPanel("main-panel");

		Label lab1 = new Label(messages.payTax());
		lab1.setStyleName("label-title");
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");

		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();
        transactionNumber.setTitle(messages.no());
		dateNoForm.setStyleName("datenumber-panel");
		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}
		payheadCombo = new PayheadCombo("Tax Agency") {
			@Override
			protected void setComboItem() {
				super.setComboItem();
				payheadSelected(getSelectedValue());
			}
			
		@Override
		public void filterResults(ArrayList<ClientPayHead> list) {
			super.filterResults(list);
		}	
		}; 
		
		payheadCombo.setRequired(true);
		payheadCombo.setEnabled(!isInViewMode());
		payheadCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayHead>() {

					@Override
					public void selectedComboBoxItem(
							ClientPayHead selectItem) {
						payheadSelected(selectItem);
					}
				});

		bankAccountCombo = new BankAccountCombo(messages.payFrom());
		bankAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						bankAccountSelected(selectItem);
					}
				});
		
		bankAccountCombo.setEnabled(!isInViewMode());
		bankAccountCombo.setRequired(true);
		table = new PayTaxTable(this) {

			protected void updateTransactionTotlas() {
				PayTaxView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isInViewMode() {
				return PayTaxView.this.isInViewMode();
			}

		};
		table.setEnabled(!isInViewMode());

		currencyWidget = createCurrencyFactorWidget();

		DynamicForm mainForm = new DynamicForm("main-form");
		mainForm.add(payheadCombo, bankAccountCombo);

		if (isMultiCurrencyEnabled()) {
			dateNoForm.add(currencyWidget);
		}

		mainPanel.add(dateNoForm);
		mainPanel.add(mainForm);
		Label payTaxTableTitle = new Label(messages2.table(messages
				.payTax()));
		payTaxTableTitle.addStyleName("editTableTitle");
		StyledPanel itemPanel = new StyledPanel("payTaxTableContainer");
		itemPanel.add(payTaxTableTitle);
		itemPanel.add(table);
		mainPanel.add(itemPanel);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setEnabled(!isInViewMode());

		DynamicForm memoTotalForm = new DynamicForm("memoTotalForm");

		DynamicForm memoForm = new DynamicForm("memoForm");

		DynamicForm totalForm = new DynamicForm("bold");
		totalForm.getElement().addClassName("totalForm");
		transactionTotalBaseCurrencyText = new AmountLabel(
				messages.currencyTotal(getCompany().getPreferences()
						.getPrimaryCurrency().getFormalName()), getCompany()
						.getPreferences().getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPreferences().getPrimaryCurrency());

		totalForm.add(transactionTotalBaseCurrencyText);
		if (isMultiCurrencyEnabled()) {
			totalForm.add(foreignCurrencyamountLabel);
		}
		memoTotalForm.add(memoForm);
		memoTotalForm.add(totalForm);
		memoForm.add(memoTextAreaItem);
        mainPanel.add(memoTotalForm);

		listforms.add(dateNoForm);
		listforms.add(mainForm);
		if (isMultiCurrencyEnabled()) {
			if (!isInViewMode()) {
				foreignCurrencyamountLabel.hide();
			}
		}
		this.add(mainPanel);
	}

	protected void payheadSelected(ClientPayHead selectedValue) {
		
	}

	protected void bankAccountSelected(ClientAccount selectItem) {
		this.currency = getCompany().getCurrency(selectItem.getCurrency());
		currencyWidget.setSelectedCurrencyFactorInWidget(currency,
				transactionDateItem.getDate().getDate());
		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
	}

	protected void employeeSelected(ClientPayStructureDestination selectItem) {
		table.clear();
		if (transaction.isVoid()) {
			setRecordsToTable(new ArrayList<ClientPayrollTransactionPayTax>());
			return;
		}
		if (selectItem != null) {
			Accounter.createPayrollService().getTransactionPayTaxList(
					selectItem,
					new AsyncCallback<List<ClientPayrollTransactionPayTax>>() {

						@Override
						public void onSuccess(
								List<ClientPayrollTransactionPayTax> result) {
							setRecordsToTable(result);
						}

						@Override
						public void onFailure(Throwable caught) {
						}
					});
		}
	}

	protected void setRecordsToTable(List<ClientPayrollTransactionPayTax> result) {
		table.setAllRows(transaction.getTransactionPayTax());
		table.selectAllRows(true);
		if (result != null) {
			for (ClientPayrollTransactionPayTax clientTransactionPayTax : result) {
				table.add(clientTransactionPayTax);
			}
		}
		updateNonEditableItems();
	}		
	
	@Override
	protected void initTransactionViewData() {
		if (transaction != null) {
		
			table.setAllRows(transaction.getTransactionPayTax());
			table.selectAllRows(true);
			
			bankAccountCombo.setComboItem(getCompany().getAccount(
					transaction.getPayAccount()));
			transactionNumber.setValue(transaction.getNumber());
			transactionDateItem.setValue(transaction.getDate());
			memoTextAreaItem.setValue(transaction.getMemo());
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			if (isMultiCurrencyEnabled()) {
				if (transaction.getCurrency() > 0) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPreferences()
							.getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				if (this.currency != null) {
					currencyWidget.setSelectedCurrency(this.currency);
				}
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}
		} else {
			setData(new ClientPayrollPayTax());
		}
	}
		
		@Override
    	public void updateNonEditableItems() {
		List<ClientPayrollTransactionPayTax> selectedRecords = table
				.getSelectedRecords();
		Double total = 0.0D;
		for (ClientPayrollTransactionPayTax clientTransactionPayTax : selectedRecords) {
			total += clientTransactionPayTax.getPayment();
		}
		transactionTotalBaseCurrencyText
				.setAmount(getAmountInBaseCurrency(total));
		foreignCurrencyamountLabel.setAmount(total);
		modifyForeignCurrencyTotalWidget();
	}		

	@Override
	protected void refreshTransactionGrid() {
		
	}

	@Override
	protected void updateDiscountValues() {
		
	}

	@Override
	public void updateAmountsFromGUI() {
		this.table.updateAmountsFromGUI();
		updateNonEditableItems();
	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		
	}

	@Override
	protected String getViewTitle() {
		return messages.payTax();
	}

	@Override
	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public void setFocus() {
		payheadCombo.setFocus();
	}

	@Override
	public void saveAndUpdateView() {
		super.saveAndUpdateView();
		transaction.setTransactionPayTax(table.getSelectedRecords());
		/*if (payheadCombo.getSelectedValue() instanceof ClientPayHead) {
			transaction.setEmployee(payheadCombo.getSelectedValue().getID());
		} else {
			transaction.setEmployeeGroup(payheadCombo.getSelectedValue()
					.getID());
		}*/
		
		transaction.setPayAccount(bankAccountCombo.getSelectedValue().getID());
		transaction.setDate(getTransactionDate().getDate());
		transaction.setNumber(transactionNumber.getValue());
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		transaction.setMemo(memoTextAreaItem.getValue());
		transaction.setTotal(foreignCurrencyamountLabel.getAmount());
		saveOrUpdate(transaction);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (payheadCombo.getSelectedValue() == null
				|| payheadCombo.getSelectedValue().getName().trim().isEmpty()) {
			result.addError(payheadCombo,
					messages.pleaseSelect(messages.taxGroup()));
		}
		if (bankAccountCombo.getSelectedValue() == null
				|| bankAccountCombo.getSelectedValue().getName().trim()
						.isEmpty()) {
			result.addError(bankAccountCombo,
					messages.pleaseSelect(messages.Account()));
		}
		if (table.getSelectedRecords().isEmpty()) {
			result.addError(table, messages.pleaseSelectAtLeastOneRecord());
		}
		return result;
	}
	 
	 /* protected void Accounts() {
		int payHeadType = payheadCombo.getPayHead() + 1;
		
		if (payHeadType == ClientPayHead.TYPE_DEDUCTIONS_FOR_EMPLOYEES) {
			payheadCombo.setVisible(true);
		} else {
			payheadCombo.setVisible(false);
		}
	}*/

    @Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

    	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		payheadCombo.setEnabled(!isInViewMode());
		bankAccountCombo.setEnabled(!isInViewMode());
		table.setEnabled(!isInViewMode());
		memoTextAreaItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		currencyWidget.setEnabled(!isInViewMode());
	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	@Override
	protected boolean canAddDraftButton() {
		return false;
	}

	public void modifyForeignCurrencyTotalWidget() {
		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(formalName));
			foreignCurrencyamountLabel.setCurrency(currencyWidget
					.getSelectedCurrency());
		}
	}
}

