package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public abstract class VendorTransactionsHistoryGrid extends
		BaseListGrid<TransactionHistory> {

	protected ClientVendor selectedVendor;

	public VendorTransactionsHistoryGrid() {
		super(false);
	}

	boolean isDeleted;

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE, };
	}

	@Override
	protected Object getColumnValue(TransactionHistory transactionHistory,
			int col) {
		switch (col) {
		case 0:
			return UIUtils.getDateByCompanyType(transactionHistory.getDate());
		case 1:
			return transactionHistory.getName();
		case 2:
			return transactionHistory.getNumber();
		case 3:
			return transactionHistory.getMemo();
		case 4:

			if (transactionHistory.getDueDate() != null)
				return UIUtils.getDateByCompanyType(transactionHistory
						.getDueDate());
			else
				return "";

		case 5:
			return DataUtils.getAmountAsStringInCurrency(transactionHistory
					.getAmount(),
					getCompany().getCurrency(selectedVendor.getCurrency())
							.getSymbol());
			// transactionHistory.getAmount();

		case 6:
			ClientAccount account = getCompany().getAccount(
					transactionHistory.getAccType());
			if (account != null) {
				return account.getName();
			} else
				return "";
		case 7:
			if (!transactionHistory.getIsVoid())
				return Accounter.getFinanceImages().notvoid();
			else
				return Accounter.getFinanceImages().voided();
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.date(), messages.type(), messages.no(),
				messages.memo(), messages.dueDate(), messages.amount(),
				messages.Account(), messages.status() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "date", "type", "no", "memo", "dueDate",
				"amount", "account", "status" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "dateValue", "typeValue", "noValue", "memoValue",
				"dueDateValue", "amountValue", "accountValue", "statusValue" };
	}

	@Override
	public void onDoubleClick(final TransactionHistory obj) {
		if (!isCanOpenTransactionView(obj.getSavestaus(), obj.getType())) {
			return;
		}
		if (obj.getType() == 11) {
			AccounterCoreType accounterCoreType = UIUtils
					.getAccounterCoreType(obj.getType());
			AccounterAsyncCallback<ClientPayBill> callback = new AccounterAsyncCallback<ClientPayBill>() {

				@Override
				public void onException(AccounterException caught) {
					Accounter.showMessage(messages.sessionExpired());
				}

				@Override
				public void onResultSuccess(ClientPayBill result) {
					if (result != null) {
						ReportsRPC.openTransactionView(result.getType(),
								obj.getTransactionId());
					}
				}
			};

			Accounter.createGETService().getObjectById(accounterCoreType,
					obj.getTransactionId(), callback);
		} else {
			ReportsRPC.openTransactionView(obj.getType(),
					obj.getTransactionId());
		}

	}

	protected void onClick(TransactionHistory obj, int row, int col) {
		if (!isCanOpenTransactionView(obj.getSavestaus(), obj.getType())) {
			return;
		}
		if (col == 7 && !obj.getIsVoid()) {
			showWarningDialog(obj, col, row);
		}
	}

	private void showWarningDialog(final TransactionHistory obj, final int col,
			final int row) {
		String msg = null;
		if (obj.getSavestaus() != ClientTransaction.STATUS_DRAFT
				&& !obj.getIsVoid() && col == 7) {
			msg = messages.doyouwanttoVoidtheTransaction();
		} else if (obj.getSavestaus() == ClientTransaction.STATUS_DRAFT) {
			Accounter.showError(messages.youCannotVoidDraftedTransaction());
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
						if (col == 7) {
							voidTransaction(
									UIUtils.getAccounterCoreType(obj.getType()),
									obj.getTransactionId());
							updateRecord(obj, row, col);
						}
						return true;
					}

				});
	}

	@Override
	public void addEmptyMessage(String msg) {
		super.addEmptyMessage(msg);
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 80;
		case 1:
			return 135;
		case 2:
			return 30;
		case 3:
			return 130;
		case 4:
			return 80;
		case 5:
			return 80;
		case 6:
			return 80;

		default:
			return 40;
		}
	}

	@Override
	protected void executeDelete(TransactionHistory object) {
	}

	public boolean isVoided(TransactionHistory obj) {
		return false;
	}

	public AccounterCoreType getAccounterCoreType(TransactionHistory obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	public void editComplete(TransactionHistory item, Object value, int col) {

		if (col == 0) {
		}
		super.editComplete(item, value, col);
	}

	public void setSelectedVendor(ClientVendor selectedVendor) {
		this.selectedVendor = selectedVendor;
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	@Override
	protected void addColumnData(TransactionHistory obj, int row, int col) {
		super.addColumnData(obj, row, col);
	}

	@Override
	public void addData(TransactionHistory obj) {
		super.addData(obj);

	}

	@Override
	protected int sort(TransactionHistory obj1, TransactionHistory obj2,
			int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			return date.compareTo(date2);
		case 1:
			String name2 = obj1.getName().toLowerCase();
			String name = obj2.getName().toLowerCase();
			return name2.compareTo(name);
		case 2:
			Integer num1 = Integer.parseInt(obj1.getNumber());
			Integer num2 = Integer.parseInt(obj2.getNumber());
			return num1.compareTo(num2);
		case 3:
			String memo = obj1.getMemo().toLowerCase();
			String memo2 = obj2.getMemo().toLowerCase();
			return memo.compareTo(memo2);
		case 4:

			ClientFinanceDate dueDate = obj1.getDueDate();
			ClientFinanceDate dueDate2 = obj2.getDueDate();
			return dueDate.compareTo(dueDate2);

		case 5:
			Double amount = obj1.getAmount();
			Double amount2 = obj2.getAmount();
			return amount.compareTo(amount2);
		default:
			break;
		}

		return 0;
	}

	@Override
	public void saveSuccess(IAccounterCore core) {
		initListData();
	}

	public abstract void initListData();
}
