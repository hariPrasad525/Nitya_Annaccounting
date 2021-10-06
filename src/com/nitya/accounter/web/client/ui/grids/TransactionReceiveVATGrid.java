package com.nitya.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientTransactionReceiveVAT;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.combo.CustomCombo;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.vat.ReceiveVATView;

public class TransactionReceiveVATGrid extends
		AbstractTransactionGrid<ClientTransactionReceiveVAT> {
	private int[] columns = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXTBOX };
	private ReceiveVATView receiveVATView;
	private ClientCurrency currency = getCompany().getPrimaryCurrency();

	public TransactionReceiveVATGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	public TransactionReceiveVATGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionReceiveVAT obj,
			int colIndex) {

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
	protected boolean isEditable(ClientTransactionReceiveVAT obj, int row,
			int index) {
		if (index == 2)
			return true;
		return false;
	}

	@Override
	public void editComplete(ClientTransactionReceiveVAT editingRecord,
			Object value, int col) {
		try {

			double payment = DataUtils.getReformatedAmount(value.toString());
			editingRecord.setAmountToReceive(payment);
			updateData(editingRecord);

			Double toBeSetAmount = 0.0;
			for (ClientTransactionReceiveVAT rec : this.getSelectedRecords()) {
				toBeSetAmount += rec.getAmountToReceive();
			}

			receiveVATView.adjustAmountAndEndingBalance(toBeSetAmount);
			// updateFooterValues(DataUtils.getAmountAsString(toBeSetAmount),
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
		return new String[] { messages.taxAgency(), messages.taxDue(),
				messages.amountToReceive() };
	}

	@Override
	protected Object getColumnValue(ClientTransactionReceiveVAT payVAT,
			int index) {
		switch (index) {
		case 0:
			ClientTAXAgency taxAgency = Accounter.getCompany().getTaxAgency(
					payVAT.getTaxAgency());
			return taxAgency != null ? taxAgency.getName() : "";
		case 1:
			return DataUtils.amountAsStringWithCurrency(
					getAmountInForeignCurrency(payVAT.getTaxDue()), currency);

		case 2:
			return String.valueOf(getAmountInForeignCurrency(payVAT
					.getAmountToReceive()));
		default:
			break;
		}
		return null;
	}

	public void setRecieveVATView(ReceiveVATView receiveVATView) {
		this.receiveVATView = receiveVATView;

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
		for (ClientTransactionReceiveVAT obj : this.getRecords()) {
			if (!isSelected(obj)) {
				((CheckBox) this.body.getWidget(indexOf(obj), 0))
						.setValue(true);
				totalAmount += obj.getAmountToReceive();
				// selectedValues.add(indexOf(obj));
				// updateValue(obj);
			}
		}
		receiveVATView.adjustAmountAndEndingBalance(totalAmount);
		// updateFooterValues(DataUtils.getAmountAsString(totalAmount), 2);
	}

	public boolean isSelected(ClientTransactionReceiveVAT obj) {
		return ((CheckBox) getWidget(indexOf(obj), 0)).getValue();

	}

	/*
	 * This method invoked when header checkbox value is false(unchecked)
	 */
	public void resetValues() {
		// for (ClientTransactionPayVAT obj : this.getRecords()) {
		// obj.setPayment(0.0);
		// obj.setCashDiscount(0);
		// obj.setAppliedCredits(0);
		// selectedValues.remove((Integer) indexOf(obj));
		// updateData(obj);
		// }
		// updateFooterValues("0.0", 2);
		receiveVATView.adjustAmountAndEndingBalance(0.0);
	}

	@Override
	protected void onSelectionChanged(ClientTransactionReceiveVAT obj, int row,
			boolean isChecked) {
		super.onSelectionChanged(obj, row, isChecked);

		double toBeSetAmount = 0.0;
		for (ClientTransactionReceiveVAT rec : this.getSelectedRecords()) {
			toBeSetAmount += rec.getAmountToReceive();
		}

		receiveVATView.adjustAmountAndEndingBalance(toBeSetAmount);
		// updateFooterValues(DataUtils.getAmountAsString(toBeSetAmount), 2);
	}

	@Override
	public Double getTotal() {

		return super.getTotal();
	}

	// public void setRecieveVATView(RecieveVATView recieveVATView) {

	// }

	public void addData(ClientTransactionReceiveVAT record) {
		super.addData(record);
	}

	@Override
	public void setTaxCode(long taxCode) {
		// currently not using

	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		for (ClientTransactionReceiveVAT tax : getSelectedRecords()) {
			if (!DecimalUtil.isGreaterThan(tax.getAmountToReceive(), 0.00)) {
				// result.addError(this, messages
				// .pleaseEnterAmountToReceive());
			}

		}
		return result;
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 1:
			return "taxAgency";
		case 2:
			return "taxDue";
		case 3:
			return "amountToReceive";
		case 0:
			return "selectBox";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 1:
			return "taxAgency";
		case 2:
			return "taxDue";
		case 3:
			return "amountToReceive";
		case 0:
			return "selectBox";
		default:
			return "";
		}
	}
}