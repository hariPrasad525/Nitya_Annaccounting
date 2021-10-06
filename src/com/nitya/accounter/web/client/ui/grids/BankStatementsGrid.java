package com.nitya.accounter.web.client.ui.grids;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientStatement;
import com.nitya.accounter.web.client.core.ClientStatementRecord;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.banking.StatementReconcilationAction;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class BankStatementsGrid extends BaseListGrid<ClientStatement> {

	public BankStatementsGrid() {
		super(false, true);
		this.getElement().setId("BankStatementsGrid");
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected void executeDelete(ClientStatement object) {

	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 15;
		case 1:
			return 30;
		case 2:
			return 35;
		case 3:
			return 52;
		case 4:
			return 52;
		case 5:
			return 52;
		case 6:
			return 52;
		case 7:
			return 52;

		}
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientStatement obj, int index) {
		switch (index) {
		case 0:
			return String.valueOf(obj.getID());
		case 1:
			return obj.getStatementList().size();
		case 2:
			return getTotalReconciledStatementRecords(obj);
		case 3:
			return obj.getImporttedDate();
		case 4:
			return obj.getStartDate();
		case 5:
			return obj.getEndDate();
		case 6:
			return DataUtils.amountAsStringWithCurrency(obj.getStartBalance(),
					getCompany().getPrimaryCurrency());
		case 7:
			return DataUtils.amountAsStringWithCurrency(
					obj.getClosingBalance(), getCompany().getPrimaryCurrency());

		}
		return null;
	}

	private int getTotalReconciledStatementRecords(ClientStatement obj) {
		int matched = 0;
		List<ClientStatementRecord> statementList = obj.getStatementList();
		for (ClientStatementRecord stRecord : statementList) {
			if (stRecord.isMatched()) {

				matched++;
			}
		}
		return matched;
	}

	@Override
	public void onDoubleClick(ClientStatement obj) {
		if (obj.isReconciled()) {
			Accounter.showInformation(messages
					.thisStatementIsAlreadyReconciled());
		} else {
			UIUtils.runAction(null,
					new StatementReconcilationAction(obj.getAccount(), obj));
		}

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.no(), messages.totalRecords(),
				messages.reconciledRecords(), messages.importedDate(),
				messages.startDate(), messages.endDate(),
				messages.startingBalance(), messages.ClosingBalance() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "no", "totalRecords", "reconciledRecords",
				"importedDate", "startDate", "endDate", "startingBalance",
				"ClosingBalance" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "no-value", "totalRecords-value",
				"reconciledRecords-value", "importedDate-value",
				"startDate-value", "endDate-value", "startingBalance-value",
				"ClosingBalance-value" };
	}
}
