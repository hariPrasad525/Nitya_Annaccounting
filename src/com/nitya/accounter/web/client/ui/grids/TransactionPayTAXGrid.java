package com.nitya.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientTransactionPayTAX;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.combo.CustomCombo;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.vat.PayTAXView;

public class TransactionPayTAXGrid extends
		AbstractTransactionGrid<ClientTransactionPayTAX> {

	private int[] columns = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXTBOX };
	private PayTAXView payVATView;
	ClientCurrency currency = getCompany().getPrimaryCurrency();

	public TransactionPayTAXGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	public TransactionPayTAXGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionPayTAX obj,
			int colIndex) {
		// currently not using anywhere in the project.
		return null;
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		return null;
	}

	@Override
	protected int getColumnType(int index) {
		return columns[index];
	}

	@Override
	protected boolean isEditable(ClientTransactionPayTAX obj, int row, int index) {
		if (index == 2)
			return true;
		return false;
	}

	@Override
	public void editComplete(ClientTransactionPayTAX editingRecord,
			Object value, int col) {

		try {

			double payment = Double.parseDouble(DataUtils
					.getReformatedAmount(value.toString()) + "");
			editingRecord.setAmountToPay(payment);
			updateData(editingRecord);

			double toBeSetAmount = 0.0;
			for (ClientTransactionPayTAX rec : this.getSelectedRecords()) {
				toBeSetAmount += rec.getAmountToPay();
			}

			payVATView.adjustAmountAndEndingBalance(toBeSetAmount);
			// updateFooterValues(amountAsString(toBeSetAmount),
			// 2);

		} catch (Exception e) {
			Accounter.showError(messages.invalidAmount());
		}
		super.editComplete(editingRecord, value, col);
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 200;
		case 1:
			return 290;
		case 2:
			return 300;
		}
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.taxFiledDate(), messages.taxDue(),
				messages.payment() };
	}

	@Override
	protected Object getColumnValue(ClientTransactionPayTAX payVAT, int index) {
		switch (index) {
		case 0:
			return payVAT.getFiledDate() != null ? payVAT.getFiledDate()
					.toString() : "";
		case 1:
			return DataUtils.getAmountAsStringInPrimaryCurrency(payVAT
					.getTaxDue());

		case 2:
			return DataUtils.getAmountAsStringInCurrency(
					payVAT.getAmountToPay(), "");
		default:
			break;
		}
		return null;
	}

	public void setPayVATView(PayTAXView payVATView) {
		this.payVATView = payVATView;
	}

	@Override
	public void onHeaderCheckBoxClick(boolean isChecked) {
		if (isChecked) {
			selectAllRows();
		} else {
			resetValues();
		}
		super.onHeaderCheckBoxClick(isChecked);
	}

	/*
	 * This method inkvoked when headercheckbox value is true.And calls the
	 * methods to update the non editable fileds and record's payment value.
	 */
	public void selectAllRows() {

		double totalAmount = 0.0;
		for (ClientTransactionPayTAX obj : this.getRecords()) {
			if (!isSelected(obj)) {
				((CheckBox) this.body.getWidget(indexOf(obj), 0))
						.setValue(true);

				// selectedValues.add(indexOf(obj));
				// updateValue(obj);
			}
			totalAmount += obj.getAmountToPay();
		}
		payVATView.adjustAmountAndEndingBalance(totalAmount);
		// updateFooterValues(amountAsString(totalAmount), 2);
	}

	public boolean isSelected(ClientTransactionPayTAX obj) {
		return ((CheckBox) getWidget(indexOf(obj), 0)).getValue();

	}

	/*
	 * This method invoked when header checkbox value is false(unchecked)
	 */
	public void resetValues() {
		payVATView.adjustAmountAndEndingBalance(0.0);
	}

	@Override
	protected void onSelectionChanged(ClientTransactionPayTAX obj, int row,
			boolean isChecked) {
		super.onSelectionChanged(obj, row, isChecked);
		double toBeSetAmount = 0.0;
		for (ClientTransactionPayTAX rec : this.getSelectedRecords()) {
			toBeSetAmount += rec.getAmountToPay();
		}

		payVATView.adjustAmountAndEndingBalance(toBeSetAmount);
		// updateFooterValues(amountAsString(toBeSetAmount), 2);
	}

	@Override
	public Double getTotal() {

		return super.getTotal();
	}

	@Override
	public void setTaxCode(long taxCode) {

	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (getSelectedRecords().size() == 0) {
			result.addError(this, messages.pleaseSelectAtLeastOneRecord());
		} else {
			for (ClientTransactionPayTAX tax : getSelectedRecords()) {
				if (!DecimalUtil.isGreaterThan(tax.getAmountToPay(), 0.00)) {
					result.addError(this, messages.pleaseEnterAmountToPay());
				}

			}
		}
		return result;
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 1:
			return "taxFiledDate";
		case 2:
			return "taxDue";
		case 3:
			return "payment";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "taxFiledDate-value";
		case 1:
			return "taxDue-value";
		case 2:
			return "payment-value";
		default:
			return "";
		}
	}
}
