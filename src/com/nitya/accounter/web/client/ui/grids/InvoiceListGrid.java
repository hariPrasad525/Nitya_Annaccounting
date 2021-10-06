package com.nitya.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.Lists.InvoicesList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class InvoiceListGrid extends BaseListGrid<InvoicesList> {

	public InvoiceListGrid() {
		super(false);
		this.getElement().setId("InvoiceListGrid");
	}

	public InvoiceListGrid(int transactionType) {
		super(false, transactionType);
		this.getElement().setId("InvoiceListGrid");
	}

	boolean isDeleted;

	@Override
	protected int[] setColTypes() {
		if (type != 0) {
			if (type == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				return new int[] { ListGrid.COLUMN_TYPE_TEXT,
						ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
						ListGrid.COLUMN_TYPE_TEXT,
						ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
						ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
						ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
						ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
						ListGrid.COLUMN_TYPE_IMAGE,
				// ListGrid.COLUMN_TYPE_IMAGE
				};
			} else {
				return new int[] { ListGrid.COLUMN_TYPE_TEXT,
						ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
						ListGrid.COLUMN_TYPE_TEXT,
						ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
						ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
						ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
						ListGrid.COLUMN_TYPE_IMAGE,
				// ListGrid.COLUMN_TYPE_IMAGE
				};
			}

		}
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE,
		// ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	protected Object getColumnValue(InvoicesList invoicesList, int col) {
		if (type != 0) {
			col += 2;
		}
		switch (col) {
		case 0:
			return false;
		case 1:
			return Utility.getTransactionName((invoicesList.getType()));
		case 2:
			return UIUtils.getDateByCompanyType(invoicesList.getDate());
		case 3:
			return invoicesList.getNumber();
		case 4:
			return invoicesList.getCustomerName();
		case 5:
			if (invoicesList.getDueDate() != null)
				return UIUtils.getDateByCompanyType(invoicesList.getDueDate());
			else
				return "";
		case 6:
			return DataUtils.amountAsStringWithCurrency(
					invoicesList.getNetAmount(), invoicesList.getCurrency());
		case 7:
			return DataUtils.amountAsStringWithCurrency(
					invoicesList.getTotalPrice(), invoicesList.getCurrency());
		case 8:
			return DataUtils.amountAsStringWithCurrency(
					invoicesList.getBalance(), invoicesList.getCurrency());
		case 9:
			if (type == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				return DataUtils.amountAsStringWithCurrency(
						invoicesList.getRemainingCredits(),
						invoicesList.getCurrency());
			} else {
				if (!invoicesList.isVoided())
					return Accounter.getFinanceImages().notvoid();
				// return "/images/not-void.png";
				else
					return Accounter.getFinanceImages().voided();
			}
		case 10:

			if (!invoicesList.isVoided())
				return Accounter.getFinanceImages().notvoid();
			// return "/images/not-void.png";
			else
				return Accounter.getFinanceImages().voided();
			// return "/images/voided.png";

			// case 10:
			// if (invoicesList.getStatus() == ClientTransaction.STATUS_DELETED)
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
			if (type == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				return new String[] {
						messages.date(),
						messages.no(),
						Global.get().messages()
								.payeeName(Global.get().Customer()),
						messages.dueDate(), messages.netPrice(),
						messages.totalPrice(), messages.balance(),
						messages.remainingCredits(), messages.voided()
				// , ""
				};
			} else {
				return new String[] {
						messages.date(),
						messages.no(),
						Global.get().messages()
								.payeeName(Global.get().Customer()),
						messages.dueDate(), messages.netPrice(),
						messages.totalPrice(), messages.balance(),
						messages.voided()
				// , ""
				};
			}

		}
		return new String[] { " ", messages.type(), messages.date(),
				messages.no(),
				Global.get().messages().payeeName(Global.get().Customer()),
				messages.dueDate(), messages.netPrice(), messages.totalPrice(),
				messages.balance(), messages.voided()
		// , ""
		};
	}

	@Override
	public void onDoubleClick(InvoicesList obj) {
		if (isCanOpenTransactionView(obj.getSaveStatus(), obj.getType())) {
			ReportsRPC.openTransactionView(obj.getType(),
					obj.getTransactionId());
		}
	}

	@Override
	protected void onClick(InvoicesList obj, int row, int col) {
		if (type != 0) {
			col += 2;
		}

		if (col == 0) {

			boolean isSelected = ((CheckBox) this.getWidget(row, col))
					.getValue();

			obj.setPrint(isSelected);

			// updateData(obj);

		}

		if (!isCanOpenTransactionView(obj.getSaveStatus(), obj.getType()))
			return;
		if (((col == 9 && type != ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) || col == 10)
				&& !obj.isVoided()) {
			if (obj.getType() == ClientTransaction.TYPE_INVOICE
					&& (obj.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED || obj
							.getStatus() == ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)) {
				Accounter.showError(messages.billPaidSoYouCantVoid());
			} else
				showWarningDialog(obj, col);
		}
		// else if (col == 9) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	private void showWarningDialog(final InvoicesList obj, final int col) {
		String msg = null;
		if (obj.getSaveStatus() != ClientTransaction.STATUS_DRAFT
				&& !obj.isVoided()
				&& ((col == 9 && type != ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) || col == 10)) {
			msg = messages.doyouwanttoVoidtheTransaction();
		} else if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT
				&& ((col == 9 && type != ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) || col == 10)) {
			Accounter.showError(messages.youCannotVoidDraftedTransaction());
			return;
		}

		// else if (col == 9) {
		// msg = "Do you want to Delete the Transaction";

		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						if (((col == 9 && type != ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) || col == 10)) {
							voidTransaction(obj);
						} else if (col == 10) {
							deleteTransaction(obj);
						}
						return true;
					}

				});

	}

	protected void voidTransaction(final InvoicesList obj) {
		voidTransaction(UIUtils.getAccounterCoreType(obj.getType()),
				obj.getTransactionId());
	}

	protected void deleteTransaction(final InvoicesList obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {

					if (viewType.equalsIgnoreCase(messages.open())
							|| viewType.equalsIgnoreCase(messages.overDue()))
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
			index += 2;
		}

		if (type == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
			switch (index) {
			case 0:
				return 20;
			case 1:
				return 100;
			case 2:
				return 80;
			case 3:
				return 50;
			case 4:
				return 100;
			case 5:
			case 6:
			case 7:
			case 8:
				return 90;
			case 9:
				return 80;
			case 10:
				return 45;
			default:
				return -1;
			}

		} else {
			switch (index) {
			case 0:
				return 20;
			case 1:
				return 100;
			case 2:
				return 90;
			case 3:
				return 60;
			case 4:
				return 120;
			case 5:
				return 90;
			case 6:
				return 100;
			case 7:
				return 100;
			case 8:
				return 100;
			case 9:
				return 45;
			case 10:
				return 45;
			default:
				return -1;
			}
		}
	}

	@Override
	protected void executeDelete(InvoicesList object) {
		// NOTHING TO DO.
	}

	@Override
	protected int sort(InvoicesList obj1, InvoicesList obj2, int index) {
		if (type == 0) {
			index = index - 1;
		} else {
			index = index + 1;
		}

		switch (index) {
		case 0:
			String type1 = Utility.getTransactionName((obj1.getType()));
			String type2 = Utility.getTransactionName((obj2.getType()));
			return type1.toLowerCase().compareTo(type2.toLowerCase());
		case 1:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;
		case 2:
			Integer num1 = Integer.parseInt(obj1.getNumber());
			Integer num2 = Integer.parseInt(obj2.getNumber());
			return num1.compareTo(num2);

		case 3:
			String name1 = obj1.getCustomerName().toLowerCase();
			String name2 = obj2.getCustomerName().toLowerCase();
			return name1.compareTo(name2);

		case 4:
			ClientFinanceDate dueDate1 = obj1.getDueDate();
			ClientFinanceDate dueDate2 = obj2.getDueDate();
			if (dueDate1 != null && dueDate2 != null) {
				return dueDate1.compareTo(dueDate2);
			}
			break;

		case 5:
			Double netPrice1 = obj1.getNetAmount();
			Double netPrice2 = obj2.getNetAmount();
			return netPrice1.compareTo(netPrice2);

		case 6:
			Double price1 = obj1.getTotalPrice();
			Double price2 = obj2.getTotalPrice();
			return price1.compareTo(price2);

		case 7:
			Double bal1 = obj1.getBalance();
			Double bal2 = obj2.getBalance();
			return bal1.compareTo(bal2);

		default:
			break;
		}

		return 0;
	}

	@Override
	public boolean isVoided(InvoicesList obj) {
		return obj.isVoided();
	}

	@Override
	public AccounterCoreType getAccounterCoreType(InvoicesList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	public void editComplete(InvoicesList item, Object value, int col) {

		if (col == 0) {
		}
		// TODO Auto-generated method stub
		super.editComplete(item, value, col);
	}

	@Override
	protected String[] setHeaderStyle() {
		if (type != 0) {
			if (type == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				return new String[] { "date", "no", "Customer", "dueDate",
						"netPrice", "totalPrice", "balance",
						"remainingCredits", "voided" };
			} else {
				return new String[] { "date", "no", "Customer", "dueDate",
						"netPrice", "totalPrice", "balance", "voided" };
			}

		}
		return new String[] { "unknown", "type", "date", "no", "Customer",
				"dueDate", "netPrice", "totalPrice", "balance", "voided" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		if (type != 0) {
			if (type == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				return new String[] { "dateValue", "noValue", "CustomerValue",
						"dueDateValue", "netPriceValue", "totalPriceValue",
						"balanceValue", "remainingCreditsValue", "voidedValue", };
			} else {
				return new String[] { "dateValue", "noValue", "CustomerValue",
						"dueDateValue", "netPriceValue", "totalPriceValue",
						"balanceValue", "voidedValue", };
			}

		}
		return new String[] { "unknownValue", "typeValue", "dateValue",
				"noValue", "CustomerValue", "dueDateValue", "netPriceValue",
				"totalPriceValue", "balanceValue", "voidedValue", };
	}
}
