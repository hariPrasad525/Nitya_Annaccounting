package com.nitya.accounter.web.client.ui.grids;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.CustomCombo;
import com.nitya.accounter.web.client.ui.core.AbstractTransactionBaseView;


public abstract class AbstractTransactionGrid<T> extends ListGrid<T> {
	protected AbstractTransactionBaseView<?> transactionView;

	private boolean isDeleteEnable;

	public static final int NON_TRANSACTIONAL = 0;
	public static final int CUSTOMER_TRANSACTION = 1;
	public static final int VENDOR_TRANSACTION = 2;
	public static final int BANKING_TRANSACTION = 3;

	public static final int TYPE_ITEM = 1;
	public static final int TYPE_COMMENT = 2;
	public static final int TYPE_SALESTAX = 3;
	public static final int TYPE_ACCOUNT = 4;
	public static final int TYPE_VATITEM = 5;
	public static final int TYPE_SERVICE = 6;
	public static final int TYPE_NONE = 0;

	private static final String EMPTY_STRING = "";

	private RecordClickHandler<T> recordClickHandler;
	private RecordDoubleClickHandler<T> doubleClickHandler;
	public boolean isItemRecieptView;


	public AbstractTransactionGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	public AbstractTransactionGrid() {
		super(false);
	}

	public AbstractTransactionGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);

	}

	/**
	 * Override this method if you want to show a menu when add new is clicked,
	 * and then when user selects one menu item you want to do some thing.
	 * 
	 * @param selection
	 */
	protected void onAddNew(String selection) {

	}

	public String[] getColumnNamesForPrinting() {
		return null;
	}

	public String getColumnValueForPrinting(ClientTransactionItem item,
			int index) {
		return null;
	}

	public Map<Integer, Map<String, String>> getVATDetailsMapForPrinting() {
		return null;
	}

	public Double getVatTotal() {
		return 0.0;
	}

	public void setAllTransactionItems(
			List<ClientTransactionItem> transactionItems) {

	}

	public abstract List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object);

	@Override
	protected void addOrEditSelectBox(T obj, Object value) {
		CustomCombo box = getCustomCombo(obj, currentCol);
		if (box != null) {
			Widget widget = box.getMainWidget();
			this.widgetsMap.put(currentCol, widget);
//			widget.getElement().getStyle().setWidth(100, Unit.PCT);
			this.setWidget(currentRow, currentCol, widget);
			//box.setFocus();
		} else
			super.addOrEditSelectBox(obj, value);
	}

	public void addRecordDoubleClickHandler(
			RecordDoubleClickHandler<T> doubleClickHandler) {
		this.doubleClickHandler = doubleClickHandler;
	}

	/**
	 * check the whether listgrid contains records or not
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (this.getRecords().size() > 1);
	}

	public void addRecordClickHandler(RecordClickHandler<T> recordClickHandler) {
		this.recordClickHandler = recordClickHandler;
	}

	/**
	 * set Enable or disable delete button on each record, by default
	 * isDeleteEnable is true
	 * 
	 * @param enable
	 */

	public void canDeleteRecord(boolean isDeleteEnable) {
		this.isDeleteEnable = isDeleteEnable;
	}

	public void setAddRequired(boolean isAddRequired) {
		this.isAddRequired = isAddRequired;
	}

	@Override
	protected String[] getSelectValues(T obj, int index) {
		return null;// this.getCurrentView().getGridSelectBoxValues(index);
	}

	@Override
	protected void onClick(T obj, int row, int index) {
		if (isDeleteEnable) {
			if (index == nofCols - 1) {
				deleteRecord(obj);
				return;
			}
		}
		if (recordClickHandler != null) {
			recordClickHandler.onRecordClick(obj, index);
		}
	}

	@Override
	protected void onValueChange(T obj, int index, Object value) {

	}

	@Override
	protected int sort(T obj1, T obj2, int index) {

		return 0;
	}

	public interface RecordDoubleClickHandler<T> {
		public void OnCellDoubleClick(T core, int column);
	}

	public interface RecordClickHandler<T> {
		public boolean onRecordClick(T core, int column);
	}

	public interface EditCompleteHandler<T> {
		public void OnEditComplete(T core, Object value, int col);
	}

	public void canDelete(boolean canDelete) {
		this.isDeleteEnable = canDelete;
	}

	@Override
	protected void onDoubleClick(T obj, int row, int col) {
		if (this.doubleClickHandler != null)
			this.doubleClickHandler.OnCellDoubleClick(obj, col);
		super.onDoubleClick(obj, row, col);
	}

	// @Override
	// public void addFooterValue(String value, int col) {
	// super.addFooterValue(value, col);
	// }
	//
	// @Override
	// public void addFooterValues(String... values) {
	// super.addFooterValues(values);
	// }

	@Override
	public void onDoubleClick(T obj) {

	}

	public void setCustomerTaxCode(ClientTransactionItem selectedObject) {

	}

	public void setVendorTaxCode(ClientTransactionItem selectedObject) {

	}

	public void setTransactionView(
			AbstractTransactionBaseView<?> abstractTransactionBaseView) {
		this.transactionView = abstractTransactionBaseView;
	}

	@Override
	public ValidationResult validateGrid() {
		return null;
	}

	public abstract <E> CustomCombo<E> getCustomCombo(T obj, int colIndex);

	public Double getTotal() {
		return 0.0;
	}

	public void refreshVatValue(ClientTransactionItem rec) {

	}

	public void refreshVatValue() {

	}

	public void updateTotals() {

	}

	public void resetGridEditEvent() {

	}

	public void refreshVatValue(IsSerializable record) {

	}

	public void updatePriceLevel() {

	}

	public void priceLevelSelected(ClientPriceLevel priceLevel) {

	}

	public Double getTaxableLineTotal() {

		return 0.0;
	}

	public double getGrandTotal() {

		return 0.0;
	}

	public double getTotalValue() {
		return 0.0;
	}

	public abstract void setTaxCode(long taxCode);

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	protected static String toStringValue(Object obj) {
		if (obj == null) {
			return EMPTY_STRING;
		}
		String str = obj.toString();
		if (str == null) {
			return EMPTY_STRING;
		}
		return str;

	}

	// protected boolean isNewTransactionEntry() {
	// return transactionView.getTransactionObject() == null;
	// }

	/**
	 * This method checks if multiple currency is enabled in the company or not.
	 * 
	 * @return
	 */

	// protected boolean isMultiCurrencyEnabled() {
	// return transactionView.getCurrencyWidget() != null;
	// }

	/**
	 * This method will check for multiple currency enable criteria internally.
	 * 
	 * @param amount
	 * @return amount/factor if multiCurrency enabled.
	 */
	protected double getAmountInBaseCurrency(double amount) {
		// if (isMultiCurrencyEnabled()) {
		// return amount
		// / transactionView.getCurrencyWidget().getCurrencyFactor();
		// }
		return amount;
	}

	/**
	 * This method will check for multiple currency enable criteria internally.
	 * 
	 * @param amount
	 *            amount is in base currency
	 * @return amount*factor if multiCurrency enabled.
	 */
	protected double getAmountInForeignCurrency(double amount) {
		// if (isMultiCurrencyEnabled()) {
		// return amount
		// * transactionView.getCurrencyWidget().getCurrencyFactor();
		// }
		return amount;
	}
}
