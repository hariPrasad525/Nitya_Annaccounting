package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class ReceivedPaymentListGrid extends BaseListGrid<ReceivePaymentsList> {

	public ReceivedPaymentListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("ReceivedPaymentListGrid");
	}

	public ReceivedPaymentListGrid(boolean isMultiSelectionEnable,
			int transactionType) {
		super(isMultiSelectionEnable, transactionType);
		this.getElement().setId("ReceivedPaymentListGrid");
	}

	boolean isDeleted;

	@Override
	protected Object getColumnValue(ReceivePaymentsList receivePayment, int col) {
		if (type != 0) {
			col += 1;
		}
		switch (col) {
		case 0:
			return Utility.getTransactionName(receivePayment.getType());
		case 1:
			return UIUtils
					.getDateByCompanyType(receivePayment.getPaymentDate());
		case 2:
			return receivePayment.getNumber();
		case 3:
			return receivePayment.getCustomerName();
		case 4:
			return receivePayment.getPaymentMethodName();
		case 5:
			return receivePayment.getCheckNumber();
		case 6:
			return DataUtils.amountAsStringWithCurrency(
					receivePayment.getAmountPaid(),
					getCompany().getCurrency(receivePayment.getCurrency()));
		case 7:
			if (!receivePayment.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";
			// case 7:
			// if (receivePayment.isDeleted())
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
		if (type != 0) {
			return new String[] { messages.paymentDate(), messages.no(),
					messages.payeeName(Global.get().Customer()),
					messages.payMethod(), messages.checkNo(),
					messages.amountPaid(), messages.voided()
			// , ""
			};
		}
		return new String[] { messages.type(), messages.paymentDate(),
				messages.no(), messages.payeeName(Global.get().Customer()),
				messages.payMethod(), messages.checkNo(),
				messages.amountPaid(), messages.voided()
		// , ""
		};
	}

	@Override
	protected int[] setColTypes() {
		if (type != 0) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE,
			// ListGrid.COLUMN_TYPE_IMAGE
			};
		}
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE,
		// ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	public void onDoubleClick(ReceivePaymentsList receivePaymentList) {
		if (isCanOpenTransactionView(0, receivePaymentList.getType())) {
			ReportsRPC.openTransactionView(receivePaymentList.getType(),
					receivePaymentList.getTransactionId());
		}
	}

	@Override
	protected void onClick(ReceivePaymentsList obj, int row, int col) {
		if (!isCanOpenTransactionView(0, obj.getType())) {
			return;
		}
		if (type != 0) {
			col += 1;
		}
		if (col == 7 && !obj.isVoided()) {
			showWarningDialog(obj, col);
		}
		// else if (col == 7) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	private void showWarningDialog(final ReceivePaymentsList obj, final int col) {
		String msg = null;
		if (obj.getStatus() != ClientTransaction.STATUS_DRAFT && col == 7
				&& !obj.isVoided()) {
			msg = messages.doyouwanttoVoidtheTransaction();
		} else if (obj.getStatus() == ClientTransaction.STATUS_DRAFT
				&& col == 7) {
			Accounter.showError(messages.youCannotVoidDraftedTransaction());
			return;
		}
		// else if (col == 7) {
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
						// else if (col == 7)
						// deleteTransaction(obj);
						return true;
					}

				});

	}

	protected void voidTransaction(final ReceivePaymentsList obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId());
	}

	protected void deleteTransaction(final ReceivePaymentsList obj) {
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
		if (type != 0) {
			index += 1;
		}
		if (index == 4 || index == 5)
			return 70;
		if (index == 6)
			return 100;
		else if (index == 0)
			return 150;
		else if (index == 1)
			return 90;

		else if (index == 2)
			return 50;

		else if (index == 3)
			return 90;

		else if (index == 7)
			return 43;

		return -1;
	}

	@Override
	protected void executeDelete(ReceivePaymentsList object) {
		// its not using any where return null;

	}

	@Override
	protected int sort(ReceivePaymentsList obj1, ReceivePaymentsList obj2,
			int index) {
		if (type != 0) {
			index += 1;
		}
		switch (index) {
		case 0:
			String type1 = Utility.getTransactionName(obj1.getType());
			String type2 = Utility.getTransactionName(obj2.getType());
			return type1.toLowerCase().compareTo(type2.toLowerCase());
		case 1:
			ClientFinanceDate paymentDate1 = obj1.getPaymentDate();
			ClientFinanceDate paymentDate2 = obj2.getPaymentDate();
			return paymentDate1.compareTo(paymentDate2);

		case 2:
			int num1 = UIUtils.isInteger(obj1.getNumber()) ? Integer
					.parseInt(obj1.getNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getNumber()) ? Integer
					.parseInt(obj2.getNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getNumber().compareTo(obj2.getNumber());

		case 3:
			String name1 = obj1.getCustomerName();
			String name2 = obj2.getCustomerName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());

		case 4:
			String paymentMethod1 = obj1.getPaymentMethodName().toLowerCase();
			String paymentMethod2 = obj2.getPaymentMethodName().toLowerCase();
			return paymentMethod1.compareTo(paymentMethod2);

		case 5:
			Integer num = Integer.parseInt(obj1.getCheckNumber());
			Integer cnum = Integer.parseInt(obj2.getCheckNumber());
			return num.compareTo(cnum);

		case 6:
			Double amt1 = obj1.getAmountPaid();
			Double amt2 = obj2.getAmountPaid();
			return amt1.compareTo(amt2);

		}
		return 0;
	}

	@Override
	public AccounterCoreType getType() {
		return null;
	}

	private long getTransactionID(ReceivePaymentsList obj) {
		return obj.getTransactionId();
	}

	@Override
	public boolean isVoided(ReceivePaymentsList obj) {
		return obj.isVoided();
	}

	@Override
	public AccounterCoreType getAccounterCoreType(ReceivePaymentsList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	protected String[] setHeaderStyle() {
		if (type != 0) {
			return new String[] { "paymentDate", "no", "payeeName",
					"payMethod", "checkNo", "amountPaid", "voided" };
		}
		return new String[] { "type", "paymentDate", "no", "payeeName",
				"payMethod", "checkNo", "amountPaid", "voided" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		if (type != 0) {
			return new String[] { "paymentDate-value", "no-value",
					"payeeName-value", "payMethod-value", "checkNo-value",
					"amountPaid-value", "voided-value" };
		}
		return new String[] { "type-value", "paymentDate-value", "no-value",
				"payeeName-value", "payMethod-value", "checkNo-value",
				"amountPaid-value", "voided-value" };
	}
}
