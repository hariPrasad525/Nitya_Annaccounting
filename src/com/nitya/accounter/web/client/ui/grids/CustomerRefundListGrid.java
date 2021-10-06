package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.Lists.CustomerRefundsList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class CustomerRefundListGrid extends BaseListGrid<CustomerRefundsList> {

	public CustomerRefundListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("CustomerRefundListGrid");
	}

	boolean isDeleted;

	@Override
	protected Object getColumnValue(CustomerRefundsList customerRefund, int col) {
		switch (col) {
		case 0:
			return UIUtils
					.getDateByCompanyType(customerRefund.getPaymentDate());
		case 1:
			return customerRefund.getPaymentNumber();
		case 2:
			return (customerRefund.getStatus() == ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED && customerRefund
					.getSaveStatus() == ClientTransaction.STATUS_DRAFT) ? "Draft"
					: Utility.getStatus(
							ClientTransaction.TYPE_CUSTOMER_REFUNDS,
							customerRefund.getStatus());
		case 3:
			return UIUtils.getDateByCompanyType(customerRefund.getIssueDate());
		case 4:
			return customerRefund.getName() == null ? "" : customerRefund
					.getName();
			// case 5:
			// return Utility.getTransactionName((customerRefund.getType()));
		case 5:
			return customerRefund.getPaymentMethod();
		case 6:
			return DataUtils.amountAsStringWithCurrency(
					customerRefund.getAmountPaid(),
					getCompany().getCurrency(customerRefund.getCurrency()));
		case 7:
			if (!customerRefund.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 9:
			// if (customerRefund.isDeleted())
			// return FinanceApplication.getFinanceImages().delSuccess()
			// .getURL();
			// else
			// return FinanceApplication.getFinanceImages().delete().getURL();

		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.paymentDate(), messages.paymentNo(),
				messages.status(), messages.issueDate(), messages.name(),
				messages.paymentMethod(), messages.amountPaid(),
				messages.voided()
		// , ""
		};
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE
		// ,ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	public void onDoubleClick(CustomerRefundsList customerRefunds) {
		if (isCanOpenTransactionView(customerRefunds.getSaveStatus(),
				customerRefunds.getType())) {
			ReportsRPC.openTransactionView(customerRefunds.getType(),
					customerRefunds.getTransactionId());
		}
	}

	@Override
	protected void onClick(CustomerRefundsList obj, int row, int col) {
		if (!isCanOpenTransactionView(obj.getSaveStatus(), obj.getType())) {
			return;
		}
		if (col == 7 && !obj.isVoided()) {
			showWarningDialog(obj, col);
		}
		// else if (col == 9) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	private void showWarningDialog(final CustomerRefundsList obj, final int col) {
		String msg = null;
		if (obj.getSaveStatus() != ClientTransaction.STATUS_DRAFT && col == 7) {
			msg = messages.doyouwanttoVoidtheTransaction();
		} else if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT
				&& col == 7) {
			msg = messages.youCannotVoidDraftedTransaction();
			Accounter.showError(msg);
			return;
		}
		// else if (col == 9) {
		// msg = "Do you want to Delete the Transaction";
		//
		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						if (col == 7) {
							voidTransaction(obj);
						}
						// else if (col == 9)
						// deleteTransaction(obj);
						return true;
					}

				});

	}

	protected void voidTransaction(final CustomerRefundsList obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId());
	}

	protected void deleteTransaction(final CustomerRefundsList obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					if (!viewType.equalsIgnoreCase(messages.all()))
						deleteRecord(obj);
					obj.setStatus(ClientTransaction.STATUS_DELETED);
					isDeleted = true;
					obj.setVoided(true);
					updateData(obj);
				}

			}
		};
		AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
		rpcDoSerivce.deleteTransaction(type, obj.getTransactionId(), callback);
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 7)
			return 50;
		else if (index == 0)
			return 100;
		else if (index == 1)
			return 80;
		else if (index == 2)
			return 80;
		else if (index == 3)
			return 80;
		else if(index == 4)
			return 150;
		else if (index == 5)
			return 100;
		else if (index == 6)
			return 80;
		return -1;
	}

	@Override
	protected void executeDelete(CustomerRefundsList object) {
		// its not using any where

	}

	@Override
	protected int sort(CustomerRefundsList obj1, CustomerRefundsList obj2,
			int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date1 = obj1.getPaymentDate();
			ClientFinanceDate date2 = obj2.getPaymentDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;

		case 1:
			int num1 = UIUtils.isInteger(obj1.getPaymentNumber()) ? Integer
					.parseInt(obj1.getPaymentNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getPaymentNumber()) ? Integer
					.parseInt(obj2.getPaymentNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getPaymentNumber().compareTo(
						obj2.getPaymentNumber());

		case 2:
			String status1 = Utility.getStatus(
					ClientTransaction.TYPE_CUSTOMER_REFUNDS, obj1.getStatus());
			String status2 = Utility.getStatus(
					ClientTransaction.TYPE_CUSTOMER_REFUNDS, obj2.getStatus());
			return status1.compareTo(status2);

		case 3:
			ClientFinanceDate issuedate1 = obj1.getIssueDate();
			ClientFinanceDate issuedate2 = obj2.getIssueDate();
			if (issuedate1 != null && issuedate2 != null)
				return issuedate1.compareTo(issuedate2);
			break;

		case 4:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());

			// case 5:
			// String type1 = Utility.getTransactionName((obj1.getType()))
			// .toLowerCase();
			// String type2 = Utility.getTransactionName((obj2.getType()))
			// .toLowerCase();
			// return type1.compareTo(type2);

		case 5:
			return obj1.getPaymentMethod().toLowerCase()
					.compareTo(obj2.getPaymentMethod().toLowerCase());

		case 6:
			return obj1.getAmountPaid().compareTo(obj2.getAmountPaid());

		default:
			break;
		}
		return 0;
	}

	@Override
	public AccounterCoreType getType() {
		return AccounterCoreType.CUSTOMERREFUND;
	}

	private long getTransactionID(CustomerRefundsList obj) {
		return obj.getTransactionId();
	}

	@Override
	public boolean isVoided(CustomerRefundsList obj) {
		return obj.isVoided();
	}

	@Override
	public AccounterCoreType getAccounterCoreType(CustomerRefundsList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "payment-date", "payment-no", "status",
				"issue-date", "name", "payment-method", "amount-paid", "voided" };

	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "payment-date-value", "payment-no-value",
				"status-value", "issue-date-value", "name-value",
				"payment-method-value", "amount-paid-value", "voided-value" };
	}
}
