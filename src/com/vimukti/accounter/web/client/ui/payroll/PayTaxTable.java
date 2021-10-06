package com.vimukti.accounter.web.client.ui.payroll;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionPayEmployee;
import com.vimukti.accounter.web.client.core.ClientPayrollTransactionPayTax;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.DateColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class PayTaxTable extends EditTable<ClientPayrollTransactionPayTax> {

	private final ICurrencyProvider currencyProvider;

	public PayTaxTable(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected void initColumns() {
		this.addColumn(new CheckboxEditColumn<ClientPayrollTransactionPayTax>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientPayrollTransactionPayTax row) {
				onSelectionChanged(row, value);
			}

			@Override
			public String getValueAsString(ClientPayrollTransactionPayTax row) {
				return null;
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextEditColumn<ClientPayrollTransactionPayTax>() {

			@Override
			protected String getValue(ClientPayrollTransactionPayTax row) {
				return row.getPayRunNumber();
			}

			@Override
			protected void setValue(ClientPayrollTransactionPayTax row,
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
			public String getValueAsString(ClientPayrollTransactionPayTax row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new DateColumn<ClientPayrollTransactionPayTax>() {

			@Override
			protected ClientFinanceDate getValue(
					ClientPayrollTransactionPayTax row) {
				return new ClientFinanceDate(row.getDate());
			}

			@Override
			protected void setValue(ClientPayrollTransactionPayTax row,
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
			public String getValueAsString(ClientPayrollTransactionPayTax row) {
				return "";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new AmountColumn<ClientPayrollTransactionPayTax>(
				currencyProvider, false) {

			@Override
			protected Double getAmount(ClientPayrollTransactionPayTax row) {
				return row.getAmountDue();
			}

			@Override
			protected void setAmount(ClientPayrollTransactionPayTax row,
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
			public String getValueAsString(ClientPayrollTransactionPayTax row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

	}

	protected void selectAllRows(boolean value) {
		List<ClientPayrollTransactionPayTax> allRows = getAllRows();
		for (ClientPayrollTransactionPayTax row : allRows) {
			row.setPayment(row.getAmountDue());
			onSelectionChanged(row, value);
		}
	}

	private void onSelectionChanged(ClientPayrollTransactionPayTax obj,
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
	public void updateValue(ClientPayrollTransactionPayTax obj) {
		update(obj);
		updateTransactionTotlas();
	}

	public int indexOf(ClientPayrollTransactionPayTax selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public List<ClientPayrollTransactionPayTax> getSelectedRecords() {
		return super.getSelectedRecords(0);
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (this.getSelectedRecords().size() == 0) {
			result.addError(this,
					messages.pleaseSelectAnyOneOfTheTransactions());
		}

		// validates receive payment amount excesses due amount or not
		for (ClientPayrollTransactionPayTax transactionPayBill : this
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
		for (ClientPayrollTransactionPayTax obj : this.getAllRows()) {

			obj.setPayment(0.0d);
			update(obj);
		}
		updateTransactionTotlas();
	}

	protected void updateTransactionTotlas() {
	}

	private void resetValue(ClientPayrollTransactionPayTax obj) {
		obj.setPayment(0.0d);
		update(obj);
		updateTransactionTotlas();
	}

	private double getTotalValue(ClientPayrollTransactionPayTax payment) {
		double totalValue = payment.getPayment();
		return totalValue;
	}

	public void setRecords(List<ClientPayrollTransactionPayTax> records) {
		setAllRows(records);
	}

	public void removeAllRecords() {
		clear();
	}

	public void selectRow(int count) {
		onSelectionChanged(getRecords().get(count), true);
	}

	public List<ClientPayrollTransactionPayTax> getRecords() {
		return getAllRows();
	}

	public void updateAmountsFromGUI() {
		for (ClientPayrollTransactionPayTax item : this.getAllRows()) {
			updateFromGUI(item);
		}
		updateColumnHeaders();
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

	public void settaxGroup(long tax, boolean b) {
		// TODO Auto-generated method stub
		
	}
}
	
