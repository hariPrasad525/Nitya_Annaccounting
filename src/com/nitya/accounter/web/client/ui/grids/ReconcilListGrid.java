package com.nitya.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientStatementRecord;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.banking.MakeDepositAction;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.ReconcileCreateDialog;
import com.nitya.accounter.web.client.ui.customers.ReconcileItemsListDialog;

/**
 * 
 * @author vimukti10
 * 
 */
public class ReconcilListGrid extends BaseListGrid<ClientStatementRecord> {

	private ClientStatementRecord statementRecord;
	private ClientAccount bankAccount;
	private int col;
	private int row;

	private static final String FIND_MATCH = messages.findAndMatch();

	public ReconcilListGrid(ClientAccount account) {
		super(false, true);
		this.bankAccount = account;
		this.getElement().setId("ReconcilListGrid");

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_COMBO };
	}

	@Override
	protected void executeDelete(ClientStatementRecord object) {

	}

	@Override
	protected String[] getSelectValues(ClientStatementRecord obj, int col) {
		this.statementRecord = obj;
		return new String[] { messages.create(""), messages.findAndMatch(),
				messages.transfer() };
	}

	@Override
	public void onWidgetValueChanged(Widget widget, Object value) {
		super.onWidgetValueChanged(widget, value);
		if (value.equals(messages.transfer())) {
			double amount = 0.0;
			if (statementRecord.getSpentAmount() > 0) {
				amount = statementRecord.getSpentAmount();
			} else {
				amount = statementRecord.getReceivedAmount();
			}
			new MakeDepositAction(bankAccount, amount, statementRecord).run();
		} else if (value.equals(FIND_MATCH)) {
			String title = null;
			if (statementRecord.getSpentAmount() > 0) {
				// then display all the spent amount transactions
				title = messages.spentTransactions();
			} else if (statementRecord.getReceivedAmount() > 0) {
				// then display all the received amount transactions
				title = messages.receivedTransactions();
			}
			ReconcileItemsListDialog dialog = new ReconcileItemsListDialog(
					title, statementRecord, bankAccount);
			ViewManager.getInstance().showDialog(dialog);
		} else if (value.equals(messages.create(""))) {
			ReconcileCreateDialog createDialog = new ReconcileCreateDialog(
					statementRecord);
			ViewManager.getInstance().showDialog(createDialog);
		}
		setText(row, col, value.toString());
	}

	@Override
	protected boolean isEditable(ClientStatementRecord obj, int row, int col) {
		this.row = row;
		this.col = col;
		if (col == 6) {
			return true;
		}
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 15;
		case 1:
			return 35;
		case 2:
			return 55;
		case 3:
			return 35;
		case 4:
			return 45;
		case 5:
			return 45;
		case 6:
			return 75;
		default:
			return 0;

		}
	}

	@Override
	protected Object getColumnValue(ClientStatementRecord obj, int index) {

		switch (index) {
		case 0:
			return String.valueOf(obj.getID());
		case 1:
			return obj.getStatementDate();
		case 2:
			return obj.getDescription();
		case 3:
			return obj.getReferenceNumber();
		case 4:
			return DataUtils.amountAsStringWithCurrency(obj.getSpentAmount(),
					getCompany().getPrimaryCurrency());
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					obj.getReceivedAmount(), getCompany().getPrimaryCurrency());
		case 6:
			if (obj.isMatched()) {

				return messages.match();
			}
			return messages.findAndMatch();
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientStatementRecord obj) {

	}

	@Override
	protected void onClick(ClientStatementRecord obj, int row, int col) {

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.no(), messages.date(),
				messages.description(), messages.referenceNo(),
				messages.spent(), messages.received(), messages.status() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "no", "date", "description", "referenceNo",
				"spent", "received", "status" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "no-value", "date-value", "description-value",
				"referenceNo-value", "spent-value", "received-value",
				"status-value" };
	}

}
