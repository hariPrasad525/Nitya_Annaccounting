package com.vimukti.accounter.server.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.imports.BooleanField;
import com.vimukti.accounter.web.client.imports.DoubleField;
import com.vimukti.accounter.web.client.imports.FinanceDateField;
import com.vimukti.accounter.web.client.imports.Integer2Field;
import com.vimukti.accounter.web.client.imports.LongField;
import com.vimukti.accounter.web.client.imports.StringField;
import com.vimukti.accounter.web.server.FinanceTool;

public class ItemImporter extends AbstractImporter<ClientItem> {

	private long itemGrupId;
	private long measurmentId;
	private long warehouseId;

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> fields = new ArrayList<ImportField>();
		fields.add(new StringField("itemName", messages.itemName(), true));
		fields.add(new Integer2Field("weight", messages.weight()));
		fields.add(new StringField("salesDescription", messages
				.salesDescription()));
		fields.add(new DoubleField("salesPrice", messages.salesPrice()));
		fields.add(new StringField("incomeAccount", messages.incomeAccount()
				+ messages.number(), true));
		fields.add(new StringField("incomeAccountName", messages.accountName(),
				true));
		fields.add(new BooleanField("isTaxble", messages.isTaxable()));
		fields.add(new BooleanField("CommissionItem", messages.commissionItem()));
		fields.add(new DoubleField("standardCost", messages.standardCost()));
		fields.add(new StringField("itemGroup", messages.itemGroup()));
		fields.add(new StringField(messages.orderNo(), messages.orderNo()));

		fields.add(new StringField("purchaseDescription", messages
				.purchaseDescription()));
		fields.add(new DoubleField("purchasePrice", messages.purchasePrice()));
		fields.add(new StringField("expenseAccount", messages.expenseAccount()
				+ messages.number(), true));
		fields.add(new StringField("expenseAccountName", messages
				.expenseAccount() + messages.name(), true));
		fields.add(new StringField("preferdVendor", messages
				.preferredVendor(Global.get().Vendor())));
		fields.add(new StringField("vendorServiceNo", messages
				.vendorServiceNo(Global.get().Vendor())));
		fields.add(new StringField("assetAccount", messages.assetsAccount()
				+ messages.number(), true));
		fields.add(new StringField("assetAccountName", messages.assetsAccount()
				+ messages.name(), true));
		fields.add(new Integer2Field("reOrderPts", messages.reorderPoint()));
		fields.add(new LongField("onHandQuantity", messages.onHandQty()));
		fields.add(new FinanceDateField("asOf", messages.asOf()));
		fields.add(new StringField("wareHouse", messages.wareHouse()));
		fields.add(new StringField("measurement", messages.measurement()));
		fields.add(new Integer2Field("numberItemType", messages.itemType()));
		fields.add(new StringField("stringItemType", messages.nameItemType()));

		return fields;

	}

	@Override
	public ClientItem getData() {
		ClientItem item = new ClientItem();
		item.setName(getString("itemName"));
		item.setType(getItemType());
		item.setActive(true);
		item.setSalesDescription(getString("salesDescription"));
		item.setSalesPrice(getDouble("salesPrice"));

		long accountId = getAccountByNumberOrName("incomeAccount", false);
		if (accountId == 0) {
			accountId = getAccountByNumberOrName("incomeAccountName", true);
		}
		item.setIncomeAccount(accountId);

		item.setISellThisItem(accountId == 0 ? false : true);

		item.setTaxable(getBoolean("isTaxble"));
		item.setCommissionItem(getBoolean("CommissionItem"));

		long assetAccountId = getAccountByNumberOrName("assetAccount", false);
		if (assetAccountId == 0) {
			assetAccountId = getAccountByNumberOrName("assetAccountName", true);
		}
		item.setAssestsAccount(assetAccountId);
		int itemType = getItemType();

		if (getInteger("weight") != 0) {
			item.setWeight(getInteger("weight"));
		}
		if (itemType == ClientItem.TYPE_INVENTORY_PART
				|| itemType == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
			item.setIBuyThisItem(true);
			item.setISellThisItem(true);
			item.setMinStockAlertLevel(null);
			item.setMaxStockAlertLevel(null);
			item.setWarehouse(getWarehouse("wareHouse"));
			item.setMeasurement(getMeasurement("measurement") != 0 ? measurmentId
					: getDefaultMeasurmentId());
			if (getLong("onHandQuantity") != 0) {
				ClientQuantity clientQty = getClientQty("onHandQuantity");
				clientQty.setUnit(getDefaultUnits(item.getMeasurement()));
				item.setOnhandQty(clientQty);
			}
			if (getInteger("reOrderPts") != 0) {
				ClientQuantity reorderQty = getClientQty("reOrderPts");
				reorderQty.setUnit(getDefaultUnits(item.getMeasurement()));
				item.setReorderPoint(reorderQty);
			}
			item.setAsOfDate(getFinanceDate("asOf") == null ? new ClientFinanceDate()
					: getFinanceDate("asOf"));
		}
		item.setItemGroup(getItemsGroup("itemGroup"));
		item.setPurchasePrice(getDouble("purchasePrice"));
		item.setPurchaseDescription(getString("purchaseDescription"));
		item.setStandardCost(getDouble("standardCost"));
		long expenxeAccountId = getAccountByNumberOrName("expenseAccount",
				false);
		if (expenxeAccountId == 0) {
			expenxeAccountId = getAccountByNumberOrName("expenseAccountName",
					true);
		}
		item.setExpenseAccount(expenxeAccountId);

		item.setIBuyThisItem(expenxeAccountId == 0 ? false : true);
		item.setPreferredVendor(getPayeeByName("preferdVendor"));
		item.setVendorItemNumber(getString("vendorServiceNo"));
		return item;

	}

	private long getDefaultUnits(long measurementID) {
		Set<Measurement> measurements = getCompanyById(getCompanyId())
				.getMeasurements();
		Measurement measurement = ImporterUtils.getMeasurementByID(
				measurements, measurementID);
		return measurement.getDefaultUnit().getID();
	}

	private int getItemType() {
		int itemType = 0;
		itemType = getInteger("numberItemType");
		if (itemType == 0) {
			String itemName = getString("stringItemType");
			if (itemName != null && !itemName.trim().isEmpty()) {
				if (itemName.trim().contains("service")
						|| itemName.trim().contains("Service")) {
					itemType = ClientItem.TYPE_SERVICE;
				} else if (itemName.trim().contains("non")
						|| itemName.trim().contains("Non")) {
					itemType = ClientItem.TYPE_NON_INVENTORY_PART;

				} else if (itemName.trim().contains("inventory")
						|| itemName.trim().contains("Inventory")) {
					itemType = ClientItem.TYPE_INVENTORY_PART;

				}
			}
		}
		return itemType;
	}

	private ClientQuantity getClientQty(String quantity) {
		ClientQuantity qty = new ClientQuantity();
		qty.setValue(getLong(quantity));
		return qty;
	}

	private long getDefaultMeasurmentId() {
		return getCompanyById(getCompanyId()).getDefaultMeasurement().getID();
	}

	private long getItemsGroup(String itemGroup) {
		final String itemGrupName = getString(itemGroup);
		if (itemGrupName != null && (itemGrupName.isEmpty())) {
			itemGrupId = new FinanceTool().getItemGrupByName(getCompanyId(),
					itemGrupName);
		}
		return itemGrupId;
	}

	private long getInvoiceFrequencyGroup(String itemGroup) {
		final String itemGrupName = getString(itemGroup);
		if (itemGrupName != null && (itemGrupName.isEmpty())) {
			itemGrupId = new FinanceTool().getInvoiceFrequencyByCompany(getCompanyId());
		}
		return itemGrupId;
	}

	private long getMeasurement(String measurementName) {

		final String measurement = getString(measurementName);
		if (measurement != null && (measurement.isEmpty())) {
			measurmentId = new FinanceTool().getMeasurmentByName(
					getCompanyId(), measurement);
		}
		return measurmentId;
	}

	private long getWarehouse(String wareHouseName) {
		final String warehouse = getString(wareHouseName);
		if (warehouse != null && (warehouse.isEmpty())) {
			warehouseId = new FinanceTool().getWarehouseByName(getCompanyId(),
					warehouse);
		}
		return warehouseId;
	}

	@Override
	protected void validate(List<AccounterException> exceptions) {
		Company company = getCompanyById(getCompanyId());
		// ValidationResult result = new ValidationResult();
		//
		// String name = nameText.getValue().toString();
		// TODO (1).
		int itemType = getItemType();
		if (!isValidItemType(itemType)) {
			AccounterException exception = new AccounterException(
					AccounterException.ERROR_PLEASE_ENTER_OR_MAP,
					messages.itemType());
			exceptions.add(exception);
		}

		String itemName = getString("itemName");
		if (itemName == null || itemName.isEmpty()) {
			AccounterException exception = new AccounterException(
					AccounterException.ERROR_PLEASE_ENTER_OR_MAP,
					messages.itemName());
			exceptions.add(exception);
		} else {
			Set<Item> items = company.getItems();
			for (Item item : items) {
				if (item.getName().equalsIgnoreCase(itemName)) {
					AccounterException exception = new AccounterException(
							AccounterException.ERROR_NAME_ALREADY_EXIST,
							messages.item());
					exceptions.add(exception);
				}
			}
		}

		Double salesPrice = getDouble("salesPrice");

		if (salesPrice != 0) {
			if (ImporterUtils.isNegativeAmount(salesPrice)) {
				AccounterException exception = new AccounterException(
						AccounterException.ERROR_NEGATIVE_AMOUNT);
				exceptions.add(exception);
			}
		}

		Double purchasePrice = getDouble("purchasePrice");

		if (purchasePrice != 0) {
			if (ImporterUtils.isNegativeAmount(purchasePrice)) {
				AccounterException exception = new AccounterException(
						AccounterException.ERROR_NEGATIVE_AMOUNT);
				exceptions.add(exception);
			}
		}

	}

	private boolean isValidItemType(int itemType) {
		switch (itemType) {
		case ClientItem.TYPE_SERVICE:
		case ClientItem.TYPE_INVENTORY_PART:
		case ClientItem.TYPE_NON_INVENTORY_PART:
			return true;
		}
		return false;
	}
}
