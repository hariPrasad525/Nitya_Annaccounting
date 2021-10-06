package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.PaymentListView;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class PaymentsListGrid extends BaseListGrid<PaymentsList> {

	public PaymentsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("PaymentsListGrid");
	}

	public PaymentsListGrid(boolean isMultiSelectionEnable, int checkType) {
		super(isMultiSelectionEnable, checkType);
		this.getElement().setId("PaymentsListGrid");
	}

	@Override
	protected int[] setColTypes() {
		if (type == PaymentListView.TYPE_PAY_RUNS) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE };
		} else if (type == PaymentListView.TYPE_PAY_EMPLOYEES) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE };
		}
		if (type == 0) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE
			// ,ListGrid.COLUMN_TYPE_IMAGE
			};
		}
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE
		// ,ListGrid.COLUMN_TYPE_IMAGE
		};

	}

	@Override
	protected Object getColumnValue(PaymentsList obj, int col) {
		if (type == PaymentListView.TYPE_PAY_RUNS
				| type == PaymentListView.TYPE_PAY_EMPLOYEES) {
			return getPayRunColumnValue(obj, col);
		}
		switch (col) {
		case 0:
			return obj.getPaymentDate() != null ? UIUtils
					.getDateByCompanyType(obj.getPaymentDate()) : "";
		case 1:
			return obj.getPaymentNumber();
		case 2:
			return Utility.getStatus(obj.getType(), obj.getStatus());
		case 3:
			return obj.getIssuedDate() != null ? UIUtils
					.getDateByCompanyType(obj.getIssuedDate()) : "";
		case 4:
			return obj.getName() != null ? obj.getName() : "";
		case 5:
			if (type == 0) {
				return Utility.getTransactionName(getType(obj));
			} else {
				return obj.getCheckNumber();
			}

		case 6:
			if (type == 0) {
				return obj.getPaymentMethodName() != null ? obj
						.getPaymentMethodName() : "";
			} else {
				return DataUtils.amountAsStringWithCurrency(
						obj.getAmountPaid(),
						getCompany().getCurrency(obj.getCurrency()));
			}
		case 7:
			if (type == 0) {
				return obj.getCheckNumber();
			} else {
				if (!obj.isVoided())
					return Accounter.getFinanceImages().notvoid();
				// return "/images/not-void.png";
				else
					return Accounter.getFinanceImages().voided();
			}
		case 8:
			return DataUtils.amountAsStringWithCurrency(obj.getAmountPaid(),
					getCompany().getCurrency(obj.getCurrency()));
		case 9:
			if (!obj.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 9:
			// if (obj.getStatus() == ClientTransaction.STATUS_DELETED)
			// return FinanceApplication.getFinanceImages().delSuccess()
			// .getURL();
			// else
			// return FinanceApplication.getFinanceImages().delete().getURL();
		}
		return null;
	}

	private Object getPayRunColumnValue(PaymentsList obj, int col) {
		switch (col) {
		case 0:
			return UIUtils.getDateByCompanyType(obj.getPaymentDate());
		case 1:
			return obj.getPaymentNumber();
		case 2:
			return obj.getName();
		case 3:
			if (type == PaymentListView.TYPE_PAY_RUNS) {
				return obj.getAmountPaid();
			}
			return obj.getInFavourOf();
		case 4:
			if (type == PaymentListView.TYPE_PAY_RUNS) {
				if (!obj.isVoided())
					return Accounter.getFinanceImages().notvoid();
				else
					return Accounter.getFinanceImages().voided();
			}
			return obj.getAmountPaid();
		case 5:
			if (!obj.isVoided())
				return Accounter.getFinanceImages().notvoid();
			else
				return Accounter.getFinanceImages().voided();
		}
		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		if (type == PaymentListView.TYPE_PAY_RUNS
				|| type == PaymentListView.TYPE_PAY_EMPLOYEES) {
			if (index == 4 || index == 1) {
				return 60;
			} else {
				return 100;
			}
		}
		if (index == 9)
			return 45;
		else if (index == 0) {
			return 125;
		} else if (index == 2 || index == 3)
			return 65;
		else if (index == 1)
			return 50;
		else if (index == 4) {
			return 150;
		} else if (index == 5)
			if (type == 0) {
				return 130;
			} else {
				return 100;
			}
		else if (index == 6)
			if (type == 0) {
				return 80;
			} else {
				return 100;
			}
		else if (index == 8 || index == 7)
			if (type == 0) {
				return 100;
			} else {
				return 45;
			}

		return -1;
	}

	@Override
	protected String[] getColumns() {
		if (type == PaymentListView.TYPE_PAY_RUNS) {
			return new String[] { messages.date(), messages.number(),
					messages.name(), messages.total(), messages.voided() };
		}

		if (type == PaymentListView.TYPE_PAY_EMPLOYEES) {
			return new String[] { messages.date(), messages.number(),
					messages.name(), messages.Account(), messages.total(),
					messages.voided() };
		}

		if (type == 0) {
			return new String[] { messages.payDate(), messages.payNo(),
					messages.status(), messages.issueDate(), messages.name(),
					messages.type(), messages.payMethod(), messages.checkNo(),
					messages.amountPaid(), messages.voided()
			// , ""
			};
		} else {
			return new String[] { messages.payDate(), messages.payNo(),
					messages.status(), messages.issueDate(), messages.name(),
					messages.checkNo(), messages.amountPaid(),
					messages.voided()
			// , ""
			};
		}

	}

	@Override
	protected void onClick(PaymentsList obj, int row, int col) {
		if (!isCanOpenTransactionView(obj.getSaveStatus(), obj.getType())) {
			return;
		}
		if (type == 0) {
			if (col == 9 && !obj.isVoided()) {
				showWarningDialog(obj, col);
			}
		} else {
			if ((type == PaymentListView.TYPE_PAY_RUNS ? col == 4
					: type == PaymentListView.TYPE_PAY_EMPLOYEES ? col == 5
							: col == 7)
					&& !obj.isVoided()) {
				showWarningDialog(obj, col);
			}
		}
		// else if (col == 9)
		// showWarningDialog(obj, col);

	};

	private void showWarningDialog(final PaymentsList obj, final int col) {
		String msg = null;
		if (type == 0) {
			if (obj.getSaveStatus() != ClientTransaction.STATUS_DRAFT
					&& col == 9 && !obj.isVoided()) {
				msg = messages.doyouwanttoVoidtheTransaction();
			} else if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT
					&& col == 9) {
				msg = messages.youCannotVoidDraftedTransaction();
				Accounter.showError(msg);
				return;
			}
		} else {
			if (obj.getSaveStatus() != ClientTransaction.STATUS_DRAFT
					&& (type == PaymentListView.TYPE_PAY_RUNS ? col == 4
							: type == PaymentListView.TYPE_PAY_EMPLOYEES ? col == 5
									: col == 7) && !obj.isVoided()) {
				msg = messages.doyouwanttoVoidtheTransaction();
			} else if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT
					&& col == 7) {
				msg = messages.youCannotVoidDraftedTransaction();
				Accounter.showError(msg);
				return;
			}
		}
		// else if (col == 9) {
		// msg = "Do you want to Delete the Transaction";

		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						// NOTHING TO DO.
						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						if (type == 0) {
							if (col == 9
									&& obj.getStatus() != ClientTransaction.STATUS_DRAFT) {
								voidTransaction(obj);
							}
						} else {
							if ((type == PaymentListView.TYPE_PAY_RUNS ? col == 4
									: type == PaymentListView.TYPE_PAY_EMPLOYEES ? col == 5
											: col == 7)
									&& obj.getStatus() != ClientTransaction.STATUS_DRAFT) {
								voidTransaction(obj);
							}
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

	@Override
	public void onDoubleClick(PaymentsList payments, int currentRow,
			int currentCol) {
		if (!isCanOpenTransactionView(payments.getSaveStatus(),
				payments.getType())) {
			return;
		}
		// ReportsRPC.openTransactionView(payments.getType(), payments
		// .getTransactionId());
		ReportsRPC.openTransactionView(getType(payments),
				payments.getTransactionId());
	}

	@Override
	public void onDoubleClick(PaymentsList obj) {
		if (!Accounter.getUser().canDoInvoiceTransactions()) {
			return;
		}
		ReportsRPC.openTransactionView(getType(obj), obj.getTransactionId());
	}

	@Override
	protected void executeDelete(PaymentsList object) {
		// NOTHING TO DO.
	}

	@Override
	protected int sort(PaymentsList obj1, PaymentsList obj2, int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date1 = obj1.getPaymentDate();
			ClientFinanceDate date2 = obj2.getPaymentDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);

			// break;
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
			Integer status1 = obj1.getStatus();
			Integer status2 = obj2.getStatus();
			return status1.compareTo(status2);

		case 3:
			ClientFinanceDate issuedate1 = obj1.getIssuedDate();
			ClientFinanceDate issuedate2 = obj2.getPaymentDate();
			if (issuedate1 != null && issuedate2 != null)
				return issuedate1.compareTo(issuedate2);

		case 4:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());

		case 5:
			if (type == 0) {
				String type1 = Utility.getTransactionName(obj1.getType())
						.toLowerCase();
				String type2 = Utility.getTransactionName(obj2.getType())
						.toLowerCase();
				type1.compareTo(type2);
			} else {
				String check1 = obj1.getCheckNumber() != null ? obj1
						.getCheckNumber() : "";
				String check2 = obj2.getCheckNumber() != null ? obj2
						.getCheckNumber() : "";
				return check1.toLowerCase().compareTo(check2.toLowerCase());
			}

		case 6:
			if (type == 0) {
				String method1 = obj1.getPaymentMethodName() != null ? obj1
						.getPaymentMethodName() : "";
				String method2 = obj2.getPaymentMethodName() != null ? obj2
						.getPaymentMethodName() : "";
				return method1.toLowerCase().compareTo(method2.toLowerCase());
			} else {
				Double amt1 = obj1.getAmountPaid();
				Double amt2 = obj2.getAmountPaid();
				return amt1.compareTo(amt2);
			}

		case 7:
			if (type == 0) {
				String check1 = obj1.getCheckNumber() != null ? obj1
						.getCheckNumber() : "";
				String check2 = obj2.getCheckNumber() != null ? obj2
						.getCheckNumber() : "";
				return check1.toLowerCase().compareTo(check2.toLowerCase());
			}

		case 8:
			if (type == 0) {
				Double amt1 = obj1.getAmountPaid();
				Double amt2 = obj2.getAmountPaid();
				return amt1.compareTo(amt2);
			}

		default:
			break;
		}

		return 0;
	}

	int getType(PaymentsList paymentsList) {

		return paymentsList.getType();
	}

	@Override
	public AccounterCoreType getType() {
		return null;
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
		if (type == PaymentListView.TYPE_PAY_RUNS) {
			return new String[] { "paydate", "payno", "name", "total", "voided" };
		}
		if (type == 0) {
			return new String[] { "paydate", "payno", "status", "issuedate",
					"name", "type", "paymethod", "checkno", "amountpaid",
					"voided" };
		} else {
			return new String[] { "paydate", "payno", "status", "issuedate",
					"name", "checkno", "amountpaid", "voided" };
		}
	}

	@Override
	protected String[] setRowElementsStyle() {
		if (type == PaymentListView.TYPE_PAY_RUNS) {
			return new String[] { "paydate-value", "payno-value", "name-value",
					"total-value", "voided-value" };
		}
		if (type == 0) {
			return new String[] { "paydate-value", "payno-value",
					"status-value", "issuedate-value", "name-value",
					"type-value", "paymethod-value", "checkno-value",
					"amountpaid-value", "voided-value" };
		} else {
			return new String[] { "paydate-value", "payno-value",
					"status-value", "issuedate-value", "name-value",
					"checkno-value", "amountpaid-value", "voided-value" };
		}
	}

}
