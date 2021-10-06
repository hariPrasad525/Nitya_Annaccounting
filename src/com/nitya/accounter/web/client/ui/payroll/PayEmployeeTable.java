package com.nitya.accounter.web.client.ui.payroll;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransactionPayEmployee;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.nitya.accounter.web.client.ui.edittable.DateColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class PayEmployeeTable extends
		EditTable<ClientTransactionPayEmployee> {
	private final ICurrencyProvider currencyProvider;

	public PayEmployeeTable(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected void initColumns() {
		this.addColumn(new CheckboxEditColumn<ClientTransactionPayEmployee>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTransactionPayEmployee row) {
				onSelectionChanged(row, value);
			}

			@Override
			public String getValueAsString(ClientTransactionPayEmployee row) {
				return null;
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientTransactionPayEmployee>() {

			@Override
			protected String getValue(ClientTransactionPayEmployee row) {
				return row.getPayRunNumber();
			}

			@Override
			protected void setValue(ClientTransactionPayEmployee row,
					String value) {

			}

			@Override
			protected String getColumnName() {
				return messages.number();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientTransactionPayEmployee row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new DateColumn<ClientTransactionPayEmployee>() {

			@Override
			protected ClientFinanceDate getValue(
					ClientTransactionPayEmployee row) {
				return new ClientFinanceDate(row.getDate());
			}

			@Override
			protected void setValue(ClientTransactionPayEmployee row,
					ClientFinanceDate value) {
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.transactionDate();
			}

			@Override
			public String getValueAsString(ClientTransactionPayEmployee row) {
				return "";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new AmountColumn<ClientTransactionPayEmployee>(
				currencyProvider, false) {

			@Override
			protected Double getAmount(ClientTransactionPayEmployee row) {
				return row.getAmountDue();
			}

			@Override
			protected void setAmount(ClientTransactionPayEmployee row,
					Double value) {

			}

			@Override
			protected String getColumnName() {
				return messages.amountDue();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientTransactionPayEmployee row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

	}

	protected void selectAllRows(boolean value) {
		List<ClientTransactionPayEmployee> allRows = getAllRows();
		for (ClientTransactionPayEmployee row : allRows) {
			row.setPayment(row.getAmountDue());
			onSelectionChanged(row, value);
		}
	}

	private void onSelectionChanged(ClientTransactionPayEmployee obj,
			boolean isChecked) {
		int row = indexOf(obj);
		if (isChecked) {
			obj.setPayment(obj.getAmountDue());
			updateValue(obj);
		} else {
			resetValue(obj);
		}
		super.checkColumn(row, 0, isChecked);
	}

	/*
	 * This method invoked each time when record(s) get selected & it updates
	 * the footer values
	 */
	public void updateValue(ClientTransactionPayEmployee obj) {
		update(obj);
		updateTransactionTotlas();
	}

	public int indexOf(ClientTransactionPayEmployee selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public List<ClientTransactionPayEmployee> getSelectedRecords() {
		return super.getSelectedRecords(0);
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (this.getSelectedRecords().size() == 0) {
			result.addError(this,
					messages.pleaseSelectAnyOneOfTheTransactions());
		}

		// validates receive payment amount excesses due amount or not
		for (ClientTransactionPayEmployee transactionPayBill : this
				.getSelectedRecords()) {

			double totalValue = getTotalValue(transactionPayBill);
			if (DecimalUtil.isEquals(totalValue, 0)) {
				result.addError(this,
						messages.totalPaymentNotZeroForSelectedRecords());
			} else if (DecimalUtil.isGreaterThan(totalValue,
					transactionPayBill.getAmountDue())) {
				result.addError(this,
						messages.totalPaymentNotExceedDueForSelectedRecords());
			}
		}

		return result;
	}

	public void resetValues() {
		/* Revert all credits to its original state */
		for (ClientTransactionPayEmployee obj : this.getAllRows()) {

			obj.setPayment(0.0d);
			update(obj);
		}
		updateTransactionTotlas();
	}

	protected abstract void updateTransactionTotlas();

	private void resetValue(ClientTransactionPayEmployee obj) {
		obj.setPayment(0.0d);
		update(obj);
		updateTransactionTotlas();
	}

	private double getTotalValue(ClientTransactionPayEmployee payment) {
		double totalValue = payment.getPayment();
		return totalValue;
	}

	public void setRecords(List<ClientTransactionPayEmployee> records) {
		setAllRows(records);
	}

	public void removeAllRecords() {
		clear();
	}

	public void selectRow(int count) {
		onSelectionChanged(getRecords().get(count), true);
	}

	public List<ClientTransactionPayEmployee> getRecords() {
		return getAllRows();
	}

	public void updateAmountsFromGUI() {
		for (ClientTransactionPayEmployee item : this.getAllRows()) {
			updateFromGUI(item);
		}
		updateColumnHeaders();
	}

}
