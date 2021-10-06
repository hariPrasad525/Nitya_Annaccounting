package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.EmployeeCombo;

public class EmployeeExpenseView extends CashPurchaseView {

	private EmployeeCombo employee;
	// private List<String> hrEmployees = new ArrayList<String>();
	public int status;
	private ImageButton approveButton;
	private ImageButton submitForApprove;

	public EmployeeExpenseView() {
		super(ClientTransaction.TYPE_EMPLOYEE_EXPENSE);
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (status == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			transaction.setExpenseStatus(status);
		else
			transaction
					.setExpenseStatus(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SAVE);

		// Setting Type
		transaction.setType(ClientTransaction.TYPE_EMPLOYEE_EXPENSE);

		transaction.setEmployee(employee.getSelectedValue().getID());

		// Setting Contact
		if (contact != null)
			transaction.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress((billingAddress));

		// Setting Phone
		if (phoneNo != null)
			transaction.setPhone(phoneNo);

		// Setting Payment Methods
		transaction.setPaymentMethod(paymentMethod);

		// Setting Pay From Account
		if (Accounter.getUser().canApproveExpences())
			transaction.setPayFrom(payFromAccount.getID());

		// Setting Check number
		transaction.setCheckNumber(checkNo.getValue().toString());
		// transaction
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getDate());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// cashPurchase.setReference(getRefText());
	}

	@Override
	protected void initViewType() {

		vendorForm.clear();
		termsForm.clear();

		final MultiWordSuggestOracle employe = new MultiWordSuggestOracle();

		titlelabel.setText(messages.employeeExpense());
		// Accounter.createGETService().getHREmployees(
		// new AccounterAsyncCallback<ArrayList<HrEmployee>>() {
		//
		// @Override
		// public void onSuccess(ArrayList<HrEmployee> result) {
		// for (HrEmployee emp : result) {
		// employe.add(emp.getEmployeeName());
		// hrEmployees.add(emp.getEmployeeName());
		// }
		// }
		//
		// @Override
		// public void onException(AccounterException caught) {
		// Accounter
		// .showInformation("Error Showing  Employees List");
		// }
		// });

		employee = new EmployeeCombo(messages.employee());
		employee.getMainWidget();
		employee.setRequired(true);
		// employee.addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientUserInfo>() {
		//
		// @Override
		// public void selectedComboBoxItem(ClientUserInfo selectItem) {
		//
		// }
		// });
		if (!Accounter.getUser().isAdminUser()) {
			// employee.setValue(Accounter.getUser().getName());
			// employee.setAdmin(false);
		}

		String listString[] = new String[] { messages.cash(),
				UIUtils.getpaymentMethodCheckBy_CompanyType(messages.check()),
				messages.creditCard(), messages.directDebit(),
				messages.masterCard(), messages.onlineBanking(),
				messages.standingOrder(), messages.switchMaestro() };
		selectedComboList = new ArrayList<String>();
		for (int i = 0; i < listString.length; i++) {
			selectedComboList.add(listString[i]);
		}

		if (!(Accounter.getUser().canApproveExpences())) {
			termsForm.setVisible(false);
		}
		paymentMethodCombo.initCombo(selectedComboList);

		vendorForm.add(employee);
		termsForm.add(paymentMethodCombo, payFromCombo, checkNo);
		// termsForm.getCellFormatter().setWidth(0, 0, "203px");

		StyledPanel verticalPanel = (StyledPanel) vendorForm.getParent();
		vendorForm.removeFromParent();
		// vendorForm.setWidth("100%");
		verticalPanel.add(vendorForm);

		StyledPanel vPanel = (StyledPanel) termsForm.getParent();
		termsForm.removeFromParent();
		// termsForm.setWidth("100%");
		vPanel.add(termsForm);

		if (isInViewMode()) {
			ClientCashPurchase cashPurchase = (ClientCashPurchase) transaction;
			// employee.setComboItem(getCompany().getUserById(
			// cashPurchase.getEmployee()));
			employee.setEnabled(false);
			if (Accounter.getUser().isAdmin()) {
				// employee.setAdmin(true);
			}
			deliveryDateItem.setValue(new ClientFinanceDate(cashPurchase
					.getDeliveryDate()));

			if (cashPurchase.getExpenseStatus() == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED) {
				if (Accounter.getUser().canApproveExpences())
					approveButton.setEnabled(false);
				else
					submitForApprove.setEnabled(false);
			}

		}

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		// result.add(vendorForm.validate());
		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDateItem,
		// messages.invalidateTransactionDate());
		// }
		// if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate))
		// {
		// result.addError(transactionDateItem,
		// messages.invalidateDate());
		// }
		if (Accounter.getUser().canApproveExpences())
			if (!payFromCombo.validate()) {
				result.addError(payFromCombo,
						messages.pleaseEnter(payFromCombo.getTitle()));
			}
		// if (!AccounterValidator.isValidDueOrDelivaryDates(deliveryDateItem
		// .getEnteredDate(), this.transactionDate)) {
		// result.addError(deliveryDateItem, messages.the()
		// + " "
		// + messages.deliveryDate()
		// + " "
		// + " "
		// + messages
		// .cannotbeearlierthantransactiondate());
		//
		// }
		// if (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
		// result.addError(vendorTransactionGrid,
		// messages.blankTransaction());
		// }
		// result.add(vendorTransactionGrid.validateGrid());
		return result;
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					ClientCashPurchase purchase = (ClientCashPurchase) transaction;
					if (purchase.getExpenseStatus() == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED) {
						Accounter.showError(messages.expenseisApproved());
					} else
						enableFormItems();
				}
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
	}

	@Override
	protected void enableFormItems() {
		super.enableFormItems();
		employee.setEnabled(isInViewMode());
		if (Accounter.getUser().isAdmin()) {
			// employee.setAdmin(true);
		}
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.productOrServiceItem());
	}

	@Override
	protected String getViewTitle() {
		return messages.employeeExpense();
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		approveButton = new ImageButton(messages.approve(), Accounter
				.getFinanceImages().approve(), "accept");
		approveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				approve();
			}
		});

		submitForApprove = new ImageButton(messages.submitForApproval(),
				Accounter.getFinanceImages().submitForApproval(), "upload");
		submitForApprove.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				onSubmitForApproval();
			}
		});
		addButton(approveButton);
		addButton(submitForApprove);
	}

	protected void onSubmitForApproval() {
		updateTransaction();
		transaction
				.setExpenseStatus(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL);
		saveOrUpdate(transaction);

	}

	protected void approve() {
		updateTransaction();
		transaction
				.setExpenseStatus(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED);
		saveOrUpdate(transaction);
	}
}
