package com.nitya.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransactionIssuePayment;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.RenderContext;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

public class TransactionIssuePaymentGrid extends
		EditTable<ClientTransactionIssuePayment> {
	private final ICurrencyProvider currencyProvider;
	private final List<Integer> selectedValues = new ArrayList<Integer>();

	public TransactionIssuePaymentGrid(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected void initColumns() {
		this.addColumn(new CheckboxEditColumn<ClientTransactionIssuePayment>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTransactionIssuePayment row) {
				onSelectionChanged(row, value);
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientTransactionIssuePayment> context) {
				super.render(widget, context);
				if (isInViewMode()) {
					((CheckBox) widget).setValue(true);
				}
			}

			@Override
			public String getValueAsString(ClientTransactionIssuePayment row) {
				return "";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		TextEditColumn<ClientTransactionIssuePayment> dateCoulmn = new TextEditColumn<ClientTransactionIssuePayment>() {

			@Override
			protected void setValue(ClientTransactionIssuePayment row,
					String value) {
			}

			@Override
			protected String getValue(ClientTransactionIssuePayment row) {
				return DateUtills.getDateAsString(new ClientFinanceDate(row
						.getDate()).getDateAsObject());
			}

			@Override
			public int getWidth() {
				return 75;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.dueDate();
			}

			@Override
			public String getValueAsString(ClientTransactionIssuePayment row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(dateCoulmn);
		TextEditColumn<ClientTransactionIssuePayment> number = new TextEditColumn<ClientTransactionIssuePayment>() {

			@Override
			protected String getValue(ClientTransactionIssuePayment row) {
				return row.getNumber();
			}

			@Override
			protected void setValue(ClientTransactionIssuePayment row,
					String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 75;
			}

			@Override
			protected String getColumnName() {
				return messages.number();
			}

			@Override
			public String getValueAsString(ClientTransactionIssuePayment row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(number);
		TextEditColumn<ClientTransactionIssuePayment> transactionType = new TextEditColumn<ClientTransactionIssuePayment>() {

			@Override
			protected String getValue(ClientTransactionIssuePayment row) {
				return Utility.getTransactionName(row.getRecordType());
			}

			@Override
			protected void setValue(ClientTransactionIssuePayment row,
					String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 75;
			}

			@Override
			protected String getColumnName() {
				return messages.type();
			}

			@Override
			public String getValueAsString(ClientTransactionIssuePayment row) {
				// TODO Auto-generated method stub
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(transactionType);
		TextEditColumn<ClientTransactionIssuePayment> name = new TextEditColumn<ClientTransactionIssuePayment>() {

			@Override
			protected String getValue(ClientTransactionIssuePayment row) {
				return row.getName();
			}

			@Override
			protected void setValue(ClientTransactionIssuePayment row,
					String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 75;
			}

			@Override
			protected String getColumnName() {
				return messages.name();
			}

			@Override
			public String getValueAsString(ClientTransactionIssuePayment row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(name);
		TextEditColumn<ClientTransactionIssuePayment> memo = new TextEditColumn<ClientTransactionIssuePayment>() {

			@Override
			protected String getValue(ClientTransactionIssuePayment row) {
				return row.getMemo();
			}

			@Override
			protected void setValue(ClientTransactionIssuePayment row,
					String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 75;
			}

			@Override
			protected String getColumnName() {
				return messages.memo();
			}

			@Override
			public String getValueAsString(ClientTransactionIssuePayment row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(memo);
		this.addColumn(new AmountColumn<ClientTransactionIssuePayment>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 75;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.originalAmount());
			}

			@Override
			protected Double getAmount(ClientTransactionIssuePayment row) {
				return row.getAmount();
			}

			@Override
			protected void setAmount(ClientTransactionIssuePayment row,
					Double value) {

			}

			@Override
			public String getValueAsString(ClientTransactionIssuePayment row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
	}

	/***
	 * select the checkbox
	 * 
	 * @param obj
	 * @param isChecked
	 */
	private void onSelectionChanged(ClientTransactionIssuePayment obj,
			boolean isChecked) {
		int row = indexOf(obj);
		if (isChecked && !selectedValues.contains(row)) {
			selectedValues.add(row);
		} else {
			selectedValues.remove((Integer) row);
		}
		super.checkColumn(row, 0, isChecked);
	}

	/**
	 * get the index of selected row
	 * 
	 * @param selectedObject
	 * @return
	 */
	public int indexOf(ClientTransactionIssuePayment selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

}
