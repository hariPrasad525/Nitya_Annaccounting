package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class VendorPaymentsListGrid extends BaseListGrid<PaymentsList> {

	public VendorPaymentsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("VendorPaymentsListGrid");
	}

	public VendorPaymentsListGrid(boolean isMultiSelectionEnable,
			int transactionType) {
		super(isMultiSelectionEnable, transactionType);
		this.getElement().setId("VendorPaymentsListGrid");
	}

	boolean isDeleted;

	@Override
	protected Object getColumnValue(PaymentsList list, int col) {
		switch (col) {
		case 0:
			return UIUtils.getDateByCompanyType(list.getPaymentDate());
		case 1:
			return list.getPaymentNumber();
		case 2:
			return Utility.getStatus(list.getType(), list.getStatus());
		case 3:
			return UIUtils.getDateByCompanyType(list.getIssuedDate());
		case 4:
			return list.getName();
		case 5:
			return Utility.getTransactionName(getType(list));
		case 6:
			return list.getPaymentMethodName();
		case 7:
			if (type != 0) {
				return DataUtils.amountAsStringWithCurrency(
						list.getAmountPaid(),
						getCompany().getCurrency(list.getCurrency()));
			} else {
				return list.getCheckNumber();
			}
		case 8:
			if (type != 0) {
				if (!list.isVoided())
					return Accounter.getFinanceImages().notvoid();
				else
					return Accounter.getFinanceImages().voided();
			} else {
				return DataUtils.amountAsStringWithCurrency(
						list.getAmountPaid(),
						getCompany().getCurrency(list.getCurrency()));
			}
		case 9:
			if (!list.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 9:
			// if (list.isDeleted())
			// return FinanceApplication.getFinanceImages().delSuccess()
			// .getURL();
			// else
			// return FinanceApplication.getFinanceImages().delete().getURL();
		default:
			break;
		}
		return null;
	}

	private void showWarningDialog(final PaymentsList obj, final int col) {
		String msg = null;
		if (obj.getSaveStatus() != ClientTransaction.STATUS_DRAFT && col == 9
				&& !obj.isVoided()) {
			msg = messages.doyouwanttoVoidtheTransaction();
		} else if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT
				&& col == 9) {
			msg = messages.youCannotVoidDraftedTransaction();
			Accounter.showError(msg);
			return;
		}

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
						if (col == 9) {
							voidTransaction(obj);
						}

						// else if (col == 9)
						// deleteTransaction(obj);
						return true;
					}

				});
	}

	protected void voidTransaction(final PaymentsList obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId());
	}

	protected void deleteTransaction(final PaymentsList obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					if (viewType != null
							&& !viewType.equalsIgnoreCase(messages.all()))
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
	protected String[] getColumns() {
		if (type != 0) {
			return new String[] { messages.payDate(), messages.payNo(),
					messages.status(), messages.issueDate(), messages.name(),
					messages.type(), messages.payMethod(),
					messages.amountPaid(), messages.voided()
			// , ""
			};
		}
		return new String[] { messages.payDate(), messages.payNo(),
				messages.status(), messages.issueDate(), messages.name(),
				messages.type(), messages.payMethod(), messages.checkNo(),
				messages.amountPaid(), messages.voided()
		// , ""
		};
	}

	@Override
	protected int[] setColTypes() {
		if (type != 0) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE
			// ,ListGrid.COLUMN_TYPE_IMAGE
			};
		}
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE
		// ,ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	protected void onClick(PaymentsList obj, int row, int col) {
		if (!isCanOpenTransactionView(obj.getSaveStatus(), obj.getType())) {
			return;
		}
		if (type != 0) {
			col += 1;
		}
		if (col == 9 && !obj.isVoided()) {
			showWarningDialog(obj, col);
		}
		// else if (col == 9)
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;

	}

	@Override
	public void onDoubleClick(PaymentsList paymentsList) {
		if (isCanOpenTransactionView(paymentsList.getSaveStatus(),
				paymentsList.getType())) {
			ReportsRPC.openTransactionView(getType(paymentsList),
					paymentsList.getTransactionId());
		}
	}

	/* This method returns the Transaction type type basing on the PayBill Type */
	int getType(PaymentsList paymentsList) {

		return paymentsList.getType();
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 8:
			if (type != 0) {
				return 45;
			} else {
				return 100;
			}
		case 9:
			return 45;
		case 0:
			return 90;
		case 3:
			return 90;
		case 2:
			return 65;
		case 1:
			return 55;
		case 4:
			if (type != 0) {
				return 110;
			}
			return 115;
		case 5:
			return 85;
		case 6:
			return 80;
		case 7:
			return 100;
		}
		return -1;
	}

	@Override
	protected void onValueChange(PaymentsList obj, int col, Object value) {

	}

	@Override
	protected void executeDelete(PaymentsList object) {

	}

	@Override
	protected int sort(PaymentsList obj1, PaymentsList obj2, int index) {
		if (type != 0 && index == 7) {
			index += 1;
		}
		switch (index) {
		case 0:
			// ClientFinanceDate date1 = obj1.getPaymentDate();
			// ClientFinanceDate date2 = obj2.getPaymentDate();
			if (obj1.getPaymentDate() != null && obj2.getPaymentDate() != null)
				return obj1.getPaymentDate().compareTo(obj2.getPaymentDate());
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
			String status1 = Utility
					.getStatus(obj1.getType(), obj1.getStatus()).toLowerCase();
			String status2 = Utility
					.getStatus(obj2.getType(), obj2.getStatus()).toLowerCase();
			return status1.compareTo(status2);

		case 3:
			ClientFinanceDate issuedate1 = obj1.getIssuedDate();
			ClientFinanceDate issuedate2 = obj2.getIssuedDate();
			if (issuedate1 != null && issuedate2 != null)
				return issuedate1.compareTo(issuedate2);
			break;

		case 4:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());

		case 5:
			String type1 = Utility.getTransactionName(getType(obj1))
					.toLowerCase();
			String type2 = Utility.getTransactionName(getType(obj2))
					.toLowerCase();
			return type1.compareTo(type2);

		case 6:
			return obj1.getPaymentMethodName().toLowerCase()
					.compareTo(obj2.getPaymentMethodName().toLowerCase());

		case 7:
			Integer num = Integer.parseInt(obj1.getCheckNumber());
			Integer num3 = Integer.parseInt(obj2.getCheckNumber());
			return num.compareTo(num3);

		case 8:
			return obj1.getAmountPaid().compareTo(obj2.getAmountPaid());
		}
		return 0;
	}

	@Override
	public AccounterCoreType getType() {
		return null;
	}

	private long getTransactionID(PaymentsList obj) {
		return obj.getTransactionId();
	}

	@Override
	public boolean isVoided(PaymentsList obj) {
		return obj.isVoided();
	}

	@Override
	public AccounterCoreType getAccounterCoreType(PaymentsList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	protected String[] setHeaderStyle() {
		if (type != 0) {
			return new String[] { "paydate", "payno", "status", "issuedate",
					"name", "type", "paymethod", "amountpaid", "voided" };
		}
		return new String[] { "paydate", "payno", "status", "issuedate",
				"name", "type", "paymethod", "checkno", "amountpaid", "voided" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		if (type != 0) {
			return new String[] { "paydate-value", "payno-value",
					"status-value", "issuedate-value", "name-value",
					"type-value", "paymethod-value", "amountpaid-value",
					"voided-value" };
		}
		return new String[] { "paydate-value", "payno-value", "status-value",
				"issuedate-value", "name-value", "type-value",
				"paymethod-value", "checkno-value", "amountpaid-value",
				"voided-value" };
	}

}
