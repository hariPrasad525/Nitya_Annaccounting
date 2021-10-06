package com.nitya.accounter.web.client.ui.settings;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientPayBill;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientUnit;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public abstract class ItemTransactionsHistoryGrid extends
		BaseListGrid<TransactionHistory> {

	private ClientItem selectedItem;

	public ItemTransactionsHistoryGrid() {
		super(false);
		this.getElement().setId("ItemTransactionsHistoryGrid");
	}

	boolean isDeleted;

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected Object getColumnValue(TransactionHistory transactionHistory,
			int col) {
		switch (col) {
		case 0:
			return UIUtils.getDateByCompanyType(transactionHistory.getDate());
		case 1:
			if (transactionHistory.getEstimateType() != 0) {
				return getEstimateTypeAsString(transactionHistory
						.getEstimateType());
			} else {
				return Utility.getTransactionName(transactionHistory.getType());
			}
		case 2:
			return transactionHistory.getNumber();
		case 3:
			return transactionHistory.getMemo();
		case 4:
			return getQty(transactionHistory.getQuantity());
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					transactionHistory.getAmount(),
					transactionHistory.getCurrency());
		default:
			break;
		}
		return null;
	}

	private String getEstimateTypeAsString(int type) {
		String title = null;
		if (type == ClientEstimate.QUOTES) {
			title = messages.quote();
		} else if (type == ClientEstimate.CHARGES) {
			title = messages.charge();
		} else if (type == ClientEstimate.CREDITS) {
			title = messages.credit();
		} else if (type == ClientEstimate.SALES_ORDER) {
			title = messages.salesOrder();
		}
		return title;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.date(), messages.type(), messages.no(),
				messages.memo(), messages.quantity(), messages.amount() };
	}

	@Override
	public void onDoubleClick(final TransactionHistory obj) {
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
						int type = result.getPayBillType() == ClientPayBill.TYPE_PAYBILL ? ClientTransaction.TYPE_PAY_BILL
								: ClientTransaction.TYPE_VENDOR_PAYMENT;

						ReportsRPC.openTransactionView(type,
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

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 80;
		case 1:
			return 120;
		case 2:
			return 40;
		case 3:
			return 265;
		case 4:
			return 80;
		case 5:
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

	@Override
	protected int sort(TransactionHistory obj1, TransactionHistory obj2,
			int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			return date.compareTo(date2);
		case 1:
			String name = Utility.getTransactionName(obj1.getType());
			String name2 = Utility.getTransactionName(obj2.getType());
			return name.compareTo(name2);
		case 2:
			Integer num1 = Integer.parseInt(obj1.getNumber());
			Integer num2 = Integer.parseInt(obj2.getNumber());
			return num1.compareTo(num2);
		case 3:
			String memo = obj1.getMemo().toLowerCase();
			String memo2 = obj2.getMemo().toLowerCase();
			return memo.compareTo(memo2);
		case 4:

			String qty = getQty(obj1.getQuantity());
			String qty2 = getQty(obj2.getQuantity());
			return qty.compareTo(qty2);

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

	public ClientItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ClientItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	private String getQty(ClientQuantity quantity) {
		StringBuffer result = new StringBuffer();
		ClientUnit unit = Accounter.getCompany()
				.getUnitById(quantity.getUnit());
		result.append(quantity.getValue());
		result.append(" ");
		result.append(unit.getName());
		return result.toString();
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "date", "type", "no", "memo", "quantity",
				"amount" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "dateValue", "typeValue", "noValue", "memoValue",
				"quantityValue", "amountValue" };
	}
}
