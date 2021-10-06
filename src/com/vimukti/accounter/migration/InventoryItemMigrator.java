package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.Warehouse;

public class InventoryItemMigrator implements IMigrator<Item> {
	@Override
	public JSONObject migrate(Item item, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.inventory.InventoryItem", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(item);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		CommonFieldsMigrator.migrateCommonFields(item, jsonObject, context);
		jsonObject.put("name", item.getName());
		jsonObject.put("isSubItemOf", item.isSubItemOf());
		Item parentItem = item.getParentItem();
		if (parentItem != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.inventory.InventoryItem", inter);
			String localId = context.getLocalIdProvider().getOrCreate(parentItem);
			inter.put("@_oid", context.get("Item", parentItem.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("subItemOf", outter);
		}
		jsonObject.put("iSellThisService", item.isISellThisItem());
		jsonObject.put("salesDescription", item.getSalesDescription());
		jsonObject.put("salesPrice", BigDecimal.valueOf(item.getSalesPrice()));
		Account incomeAccount = item.getIncomeAccount();
		if (incomeAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(incomeAccount);
			inter.put("@_oid", context.get("Account", incomeAccount.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("incomeAccount", outter);
		}
		jsonObject.put("isTaxable", item.isTaxable());
		jsonObject.put("isCommissionItem", item.isCommissionItem());
		jsonObject.put("standardCost", BigDecimal.valueOf(item.getStandardCost())); 
		ItemGroup itemGroup = item.getItemGroup();
		if (itemGroup != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.ItemGroup", inter);
			String localId = context.getLocalIdProvider().getOrCreate(itemGroup);
			inter.put("@_oid", context.get("ItemGroup", itemGroup.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("itemGroup", outter);
		}
		jsonObject.put("inActive", !item.isActive());
		jsonObject.put("iBuyThisService", item.isIBuyThisItem());
		jsonObject.put("purchaseDescription", item.getPurchaseDescription());
		jsonObject.put("purchasePrice", BigDecimal.valueOf(item.getPurchasePrice()));
		Account expenseAccount = item.getExpenseAccount();
		if (expenseAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(expenseAccount);
			inter.put("@_oid", context.get("Account", expenseAccount.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("expenseAccount", outter);
		}
		Vendor preferredVendor = item.getPreferredVendor();
		if (preferredVendor != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.vendor.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(preferredVendor);
			inter.put("@_oid", context.get("Vendor", preferredVendor.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("preferredVendor", outter);
		}
		jsonObject.put("vendorServiceNumber", item.getVendorItemNumber());
		jsonObject.put("itemType", PicklistUtilMigrator.getItemTypeIdentifier(item.getType()));
		// assertAccount
		Account assestsAccount = item.getAssestsAccount();
		if (assestsAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(assestsAccount);
			inter.put("@_oid", context.get("Account", assestsAccount.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("assetAccount", outter);
		}
		Quantity reorderPoint = item.getReorderPoint();
		if (reorderPoint != null) {
			double value = reorderPoint.getValue();
			Unit unit = reorderPoint.getUnit();
			if (unit != null) {
				value = unit.getFactor() * value;
			}
			jsonObject.put("reOrderPoint", BigDecimal.valueOf(value));
		}

		Account costOfGoodsSold = item.getExpenseAccount();
		if (costOfGoodsSold != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(costOfGoodsSold);
			inter.put("@_oid", context.get("Account", costOfGoodsSold.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("costOfGoodsSold", outter);
		}

		Warehouse warehouse = item.getWarehouse();
		if (warehouse != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.inventory.Warehouse", inter);
			String localId = context.getLocalIdProvider().getOrCreate(warehouse);
			inter.put("@_oid", context.get("Warehouse", warehouse.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("warehouse", outter);
		}

		Measurement measurement = item.getMeasurement();
		if (measurement != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.inventory.Measurement", inter);
			String localId = context.getLocalIdProvider().getOrCreate(measurement);
			inter.put("@_oid", context.get("Measurement", measurement.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("measurement", outter);
		}
		jsonObject.put("averageCost", BigDecimal.valueOf(item.getAverageCost()));
		return external;

	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Item.TYPE_INVENTORY_PART));
	}
}
