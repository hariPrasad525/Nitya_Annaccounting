package com.nitya.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.EditTable;

public abstract class AbstractTransactionTable extends EditTable<ClientTransactionItem> {

	double lineTotal;
	double taxableLineTotal;
	double totalTax;
	double grandTotal;
	double totaldiscount;

	protected boolean enableTax;
	protected boolean showTaxCode;
	protected boolean enableDisCount;
	protected boolean showDiscount;
	protected boolean isCustomerAllowedToAdd;
	protected boolean enableClass;
	protected boolean showClass;

	protected boolean needDiscount = true;

	private final boolean isSales;

	protected ICurrencyProvider currencyProvider;

	public AbstractTransactionTable(int rowsPerObject, boolean needDiscount,
			boolean isSales, boolean isCustomerAllowedToAdd,
			ICurrencyProvider currencyProvider) {
		super(rowsPerObject);
		this.currencyProvider = currencyProvider;
		this.needDiscount = needDiscount;
		this.isCustomerAllowedToAdd = isCustomerAllowedToAdd;
		this.isSales = isSales;
	}

	public AbstractTransactionTable(int rowsPerObject, boolean needDiscount,
			boolean isSales, ICurrencyProvider currencyProvider) {
		this(rowsPerObject, needDiscount, isSales, false, currencyProvider);
	}

	public void updateTotals() {

		List<ClientTransactionItem> allrecords = getAllRows();
		totaldiscount = 0;
		lineTotal = 0.0;
		taxableLineTotal = 0.0;
		totalTax = 0.0;

		for (ClientTransactionItem record : allrecords) {
			if (record.getItem() != 0 || record.getAccount() != 0) {
				int type = record.getType();

				if (type == 0) {
					continue;
				}
				if (record.getDiscount() != null) {
					totaldiscount += record.getDiscount();
				}

				Double lineTotalAmt = record.getLineTotal();
				if (lineTotalAmt != null) {
					lineTotal += lineTotalAmt;
				}

				if (record != null && record.isTaxable()) {
					// ClientTAXItem taxItem = getCompany().getTAXItem(
					// citem.getTaxCode());
					// if (taxItem != null) {
					// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
					// }
					taxableLineTotal += lineTotalAmt;

					double taxAmount = getVATAmount(record.getTaxCode(), record);
					if (isShowPriceWithVat()) {
						lineTotal -= taxAmount;
					}
					record.setVATfraction(taxAmount);
					totalTax += record.getVATfraction();

				}
			} else {

			}

			// super.update(record);
			// totalVat += citem.getVATfraction();
		}

		// if (getCompany().getPreferences().isChargeSalesTax()) {
		grandTotal = totalTax + lineTotal;
		// } else {
		// grandTotal = totallinetotal;
		// totalValue = grandTotal;
		// }
		// if (getCompany().getPreferences().isRegisteredForVAT()) {

		// grandTotal = totallinetotal;
		// totalValue = grandTotal + totalTax;
		// }
		// } else {
		// grandTotal = totallinetotal;
		// totalValue = grandTotal;
		// }

		updateNonEditableItems();

	}

	@Override
	public void delete(ClientTransactionItem row) {
		super.delete(row);
		updateTotals();
	}

	@Override
	public void setAllRows(List<ClientTransactionItem> rows) {
		createColumns();
		for (ClientTransactionItem item : rows) {
			item.setID(0);
			item.taxRateCalculationEntriesList.clear();
		}
		super.setAllRows(rows);

	}

	@Override
	public void addRows(List<ClientTransactionItem> rows) {
		for (ClientTransactionItem item : getRecords()) {
			if (item.isEmpty()) {
				delete(item);
			}
		}
		createColumns();
		super.addRows(rows);
		List<ClientTransactionItem> itemList = new ArrayList<ClientTransactionItem>();
		if (getAllRows().size() < 4) {
			for (int ii = 0; ii < (4 - getAllRows().size()); ii++) {
				ClientTransactionItem item = new ClientTransactionItem();
				itemList.add(item);
			}
			createColumns();
			super.addRows(itemList);
		}
	}

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	@Override
	public void update(ClientTransactionItem row) {
		super.update(row);
		updateTotals();
	}

	protected abstract void updateNonEditableItems();

	public double getTaxableLineTotal() {
		return taxableLineTotal;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public double getLineTotal() {
		return lineTotal;
	}

	public double getTotalTax() {
		return totalTax;
	}

	// public double getTotalValue() {
	// return totalValue;
	// }
	//
	// public Double getTotal() {
	// return lineTotal != null ? lineTotal.doubleValue() : 0.0d;
	// }

	public void removeAllRecords() {
		clear();
	}

	public void setRecords(List<ClientTransactionItem> transactionItems) {
		setAllRows(transactionItems);
		updateTotals();
	}

	public abstract boolean isShowPriceWithVat();

	public List<ClientTransactionItem> getRecords() {
		return getAllRows();
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		// Validations
		// 1. checking for the name of the transaction item
		// 2. checking for the vat code if the company is of type UK
		// TODO::: check whether this validation is working or not
		for (ClientTransactionItem transactionItem : this.getRecords()) {
			if (transactionItem.isEmpty()) {
				continue;
			}
			if (transactionItem.getAccountable() == null) {
				result.addError("GridItem-" + transactionItem.getType(),
						messages.pleaseSelect(Utility
								.getItemType(transactionItem.getType())));
			}
			if (enableTax && showTaxCode) {
				if (transactionItem.getTaxCode() == 0) {
					result.addError(
							"GridItemUK-" + transactionItem.getAccount(),
							messages.pleaseSelect(messages.taxCode()));
				}

			}

			if (transactionItem.isBillable()) {
				if (transactionItem.getCustomer() == 0) {
					result.addError("Customer",
							messages.mustSelectCustomerForBillable());
				}
				switch (transactionItem.getType()) {
				case ClientTransactionItem.TYPE_ITEM:
					if (transactionItem.getItem() > 0) {
						ClientItem item = getCompany().getItem(
								transactionItem.getItem());
						if (!item.isIBuyThisItem || !item.isISellThisItem) {
							result.addError("Item", messages
									.onlySellableItemsCanBeMarkedAsBillable());
						}
					} else {
						result.addError("Item", messages.pleaseSelect(messages
								.transactionItem()));
					}
					break;
				default:
					break;
				}
			}
		}
		if (DecimalUtil.isLessThan(lineTotal, 0.0)) {
			result.addError(this, messages.invalidTransactionAmount());
		}
		return result;
	}

	public double getVATAmount(long TAXCodeID, ClientTransactionItem record) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				vatRate = UIUtils.getVATItemRate(Accounter.getCompany()
						.getTAXCode(TAXCodeID), isSales());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (isShowPriceWithVat()) {
			vat = record.getLineTotal()
					- (100 * (record.getLineTotal() / (100 + vatRate)));
		} else {
			vat = record.getLineTotal() * vatRate / 100;
		}
		return vat;
	}

	public double getDiscountValue() {
		return totaldiscount;
	}

	public void setTaxCode(long taxCode, boolean force) {
		for (ClientTransactionItem item : getRecords()) {
			if ((item.getTaxCode() == 0) || force) {
				// Only set this for account and if we have not specified
				// already
				// This works only once
				item.setTaxCode(taxCode);
				// update(item);
			}
			update(item);
		}
	}

	public void setClass(long classId, boolean force) {
		for (ClientTransactionItem item : getRecords()) {
			if ((item.getAccounterClass() == 0) || force) {
				// Only set this for account and if we have not specified
				// already
				// This works only once
				item.setAccounterClass(classId);
				// update(item);
			}
			update(item);
		}
	}

	public void setDiscount(double amount) {
		for (ClientTransactionItem item : getRecords()) {
			if (item.getItem() != 0 || item.getAccount() != 0) {
				item.setDiscount(amount);
				double lt = item.getQuantity().getValue() * item.getUnitPrice();
				double disc = item.getDiscount();
				item.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100))
						: lt);
			}
			update(item);
		}
	}

	public void resetRecords() {
		clear();
		addEmptyRecords();
	}

	public void updateAmountsFromGUI() {
		updateTotals();
		for (ClientTransactionItem item : this.getAllRows()) {
			updateFromGUI(item);
			update(item);
		}
		updateColumnHeaders();
	}

	public void updateRecords() {
		for (ClientTransactionItem item : this.getAllRows()) {
			update(item);
		}
	}

	public void setDisableTax(boolean isDisable) {
		if (getCompany().getPreferences().isTrackTax()) {
			enableTax = !isDisable;
		}
	}

	public void setShowTax(boolean isShow) {
		if (getCompany().getPreferences().isTrackTax()) {
			showTaxCode = isShow;
		}
	}

	@Override
	protected abstract boolean isInViewMode();

	public boolean isSales() {
		return isSales;
	}

	public boolean isEmpty() {
		List<ClientTransactionItem> records = getRecords();
		boolean isEmpty = true;
		for (ClientTransactionItem item : records) {
			if (!item.isEmpty()) {
				isEmpty = false;
				break;
			}
		}
		return isEmpty;
	}

	public List<ClientTransactionItem> getTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : getRecords()) {
			if (!item.isEmpty()) {
				list.add(item);
			}
		}
		return list;
	}

	protected abstract void updateDiscountValues(ClientTransactionItem row);
}
