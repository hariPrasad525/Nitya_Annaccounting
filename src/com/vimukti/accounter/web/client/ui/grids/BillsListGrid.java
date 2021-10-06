package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class BillsListGrid extends BaseListGrid<BillsList> {

	ClientCurrency currency = getCompany().getPrimaryCurrency();

	public BillsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("BillsListGrid");
	}

	public BillsListGrid(boolean isMultiSelectionEnable, int transactionType) {
		super(isMultiSelectionEnable, transactionType);
		this.getElement().setId("BillsListGrid");
	}

	boolean isDeleted;

	@Override
	protected Object getColumnValue(BillsList bills, int col) {
		if (type != 0) {
			col += 1;
		}
		switch (col) {
		case 0:
			return Utility.getTransactionName((bills.getType()));
		case 1:
			return UIUtils.getDateByCompanyType(bills.getDate());
		case 2:
			return bills.getNumber();
		case 3:
			return bills.getVendorName() != null ? bills.getVendorName() : "";
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					bills.getOriginalAmount(), bills.getCurrency());
		case 5:
			if (bills.getBalance() != null)
				return DataUtils.amountAsStringWithCurrency(bills.getBalance(),
						bills.getCurrency());
			else
				return DataUtils.amountAsStringWithCurrency(0.00D,
						bills.getCurrency());
		case 6:
			if (!bills.isVoided())
				return Accounter.getFinanceImages().notvoid();
			else
				return Accounter.getFinanceImages().voided();
		default:
			break;
		}
		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		if (type != 0) {
			index = index + 1;
		}
		if (index == 0)
			return 115;
		if (index == 1)
			return 100;
		if (index == 2)
			return 50;
		if (index == 3)
			return 200;		
		if (index == 4)
			return 110;
		if (index == 5)
			return 100;
		if (index == 6)
			return 43;
		return -1;
	};

	@Override
	protected String[] getColumns() {
		if (type != 0) {
			return new String[] { messages.date(), messages.no(),
					Global.get().messages().payeeName(Global.get().Vendor()),
					messages.originalAmount(), messages.balance(),
					messages.voided()
			// , ""
			};
		}
		return new String[] { messages.type(), messages.date(), messages.no(),
				Global.get().messages().payeeName(Global.get().Vendor()),
				messages.originalAmount(), messages.balance(),
				messages.voided()
		// , ""
		};
	}

	@Override
	public void onDoubleClick(BillsList bills) {
		if (isCanOpenTransactionView(bills.getSaveStatus(), bills.getType())) {
			ReportsRPC.openTransactionView(bills.getType(),
					bills.getTransactionId());
		}
	}

	private boolean isUserHavePermissions(BillsList obj) {
		ClientUser user = Accounter.getUser();
		if (user.canDoInvoiceTransactions()) {
			return true;
		}

		if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT
				&& user.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
			return true;
		}
		return false;
	}

	@Override
	protected void onValueChange(BillsList obj, int col, Object value) {

	}

	@Override
	protected int[] setColTypes() {
		if (type != 0) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE
			// ,ListGrid.COLUMN_TYPE_IMAGE
			};
		}
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE
		// ,ListGrid.COLUMN_TYPE_IMAGE
		};
	}

	@Override
	protected void onClick(BillsList obj, int row, int col) {
		if (!isCanOpenTransactionView(obj.getSaveStatus(), obj.getType()))
			return;
		if (type == 0 && col == 6 && !obj.isVoided()) {
			processVoidTransaction(obj, col);
		} else if (type != 0 && col == 5 && !obj.isVoided()) {
			processVoidTransaction(obj, col);
		}
		// else if (col == 7) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	private void processVoidTransaction(BillsList obj, int col) {
		if (obj.getType() == ClientTransaction.TYPE_ENTER_BILL
				&& (obj.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED || obj
						.getStatus() == ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)) {
			Accounter.showError(messages.billPaidSoYouCantVoid());
			// "You have already paid some amount for this Bill, You can't Edit and Void it.");
		} else if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
			Accounter.showError(messages.youCannotVoidDraftedTransaction());
		} else if (obj.getType() != ClientTransaction.TYPE_EMPLOYEE_EXPENSE
				|| (obj.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE && obj
						.getExpenseStatus() == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)) {
			showWarningDialog(obj, this.getAccounterCoreType(obj),
					this.getTransactionID(obj), col);
		} else {
			Accounter.showError(messages.expensecantbevoiditisApproved());
		}
	}

	@Override
	protected void showWarningDialog(final BillsList obj,
			final AccounterCoreType coreType, final long transactionsID,
			final int col) {
		String msg = null;
		if (obj.getSaveStatus() != ClientTransaction.STATUS_DRAFT && col == 6) {
			msg = messages.doyouwanttoVoidtheTransaction();
		} else if (obj.getSaveStatus() != ClientTransaction.STATUS_DRAFT
				&& col == 5) {
			msg = messages.doyouwanttoVoidtheTransaction();
		} else if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT
				&& (col == 6 || col == 5)) {
			msg = messages.youCannotVoidDraftedTransaction();
			Accounter.showError(msg);
			return;
		}

		// else if (col == 7) {
		// if (!viewType.equalsIgnoreCase("Deleted"))
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
						if (col == 6 || col == 5) {
							voidTransaction(coreType, transactionsID);
						}
						// else if (col == 7)
						// deleteTransaction(obj);
						return true;

					}

				});
	}

	protected void deleteTransaction(final BillsList obj) {
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
	protected void executeDelete(BillsList object) {

	}

	@Override
	protected int sort(BillsList obj1, BillsList obj2, int index) {
		if (type != 0) {
			index = index + 1;
		}
		switch (index) {
		case 0:
			String type1 = Utility.getTransactionName((obj1.getType()))
					.toLowerCase();
			String type2 = Utility.getTransactionName((obj2.getType()))
					.toLowerCase();
			return type1.compareTo(type2);

		case 1:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;

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
			return obj1.getVendorName().toLowerCase()
					.compareTo(obj2.getVendorName().toLowerCase());

		case 4:
			return obj1.getOriginalAmount().compareTo(obj2.getOriginalAmount());

		case 5:
			Double balance1 = obj1.getBalance();
			Double balance2 = obj2.getBalance();
			return UIUtils.compareTo(balance1, balance2);

		default:
			break;
		}
		return 0;
	}

	private long getTransactionID(BillsList obj) {
		return obj.getTransactionId();
	}

	@Override
	public boolean isVoided(BillsList obj) {
		return obj.isVoided();
	}

	@Override
	public AccounterCoreType getAccounterCoreType(BillsList obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	protected String[] setHeaderStyle() {
		if (type != 0) {
			return new String[] { "date", "no",
					"Vendor",
					"originalAmount", "balance",
					"voided"
			};
		}
		return new String[] { "type", "date", "no",
				"Vendor",
				"originalAmount", "balance",
				"voided"
		};
	}

	@Override
	protected String[] setRowElementsStyle() {
		if (type != 0) {
			return new String[] { "dateValue", "noValue",
					"VendorValue",
					"originalAmountValue", "balanceValue",
					"voidedValue"
			};
		}
		return new String[] { "typeValue", "dateValue", "noValue",
				"VendorValue",
				"originalAmountValue", "balanceValue",
				"voidedValue"
		};
	}

}