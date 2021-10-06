package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public class CreditsandPaymentsGrid extends
		AbstractTransactionGrid<ClientCreditsAndPayments> {

	List<Integer> selectedValues = new ArrayList<Integer>();
	private int columns[] = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX,
			ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX };

	public CreditsandPaymentsGrid(boolean isMultiSelectionEnable,
			ClientCurrency currency) {
		super(isMultiSelectionEnable);
		this.currency = currency;
	}

	@Override
	protected int getColumnType(int index) {
		return columns[index];
	}

	@Override
	protected boolean isEditable(ClientCreditsAndPayments obj, int row,
			int index) {
		if (index == 4) {
			return true;
		}
		return false;
	}

	@Override
	protected String[] getColumns() {

		return new String[] { messages.date(), messages.memo(),
				messages.creditAmount(), messages.balance(),
				messages.amountToUse() };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 125;
		case 1:
		case 2:
		case 3:
		case 4:
			return 100;
		default:
			return -1;
		}
	}

	@Override
	protected Object getColumnValue(
			ClientCreditsAndPayments creditsAndPayments, int index) {

		switch (index) {
		case 0:
			return DateUtills.getDateAsString(creditsAndPayments
					.getTransactionDate());
		case 1:
			return creditsAndPayments.getMemo();
		case 2:
			return DataUtils.amountAsStringWithCurrency(
					getAmountInForeignCurrency(creditsAndPayments
							.getCreditAmount()), currency);
		case 3:
			return DataUtils.amountAsStringWithCurrency(
					getAmountInForeignCurrency(creditsAndPayments
							.getRemaoningBalance()), currency);
		case 4:
			return DataUtils
					.amountAsStringWithCurrency(
							getAmountInForeignCurrency(creditsAndPayments
									.getAmtTouse()), currency);
		default:
			break;
		}
		return null;
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientCreditsAndPayments obj,
			int colIndex) {
		// its not using any where
		return null;
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		return null;
	}

	public void setCheckboxValue(ClientCreditsAndPayments record) {
		((CheckBox) getWidget(indexOf(record), 0)).setValue(record
				.isRecordChanged());
	}

	@Override
	public void setTaxCode(long taxCode) {
		// TODO Auto-generated method stub
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "date";
		case 1:
			return "memo";
		case 2:
			return "creditAmount";
		case 3:
			return "balance";
		case 4:
			return "amountToUse";
		default:
			return "";
		}

	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "date-value";
		case 1:
			return "memo-value";
		case 2:
			return "creditAmount-value";
		case 3:
			return "balance-value";
		case 4:
			return "amountToUse-value";
		default:
			return "";
		}
	}

}
