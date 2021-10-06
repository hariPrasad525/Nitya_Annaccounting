package com.nitya.accounter.migration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.InventoryAssembly;
import com.nitya.accounter.core.InventoryAssemblyItem;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.ItemGroup;
import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.core.Quantity;
import com.nitya.accounter.core.Unit;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.Warehouse;

public class InventoryAssemblyMigrator implements IMigrator<InventoryAssembly> {
	@Override
	public JSONObject migrate(InventoryAssembly item, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.inventory.InventoryAssembly", jsonObject);
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
			outter.put("com.nitya.accounter.shared.inventory.InventoryAssembly", inter);
			String localId = context.getLocalIdProvider().getOrCreate(parentItem);
			inter.put("@_oid", context.get("InventoryAssemblyr", parentItem.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("subItemOf", outter);
		}
		jsonObject.put("iSellThisService", item.isISellThisItem());
		jsonObject.put("salesDescription", item.getSalesDescription());
		jsonObject.put("salesPrice", item.getSalesPrice());
		Account incomeAccount = item.getIncomeAccount();
		if (incomeAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(incomeAccount);
			inter.put("@_oid", context.get("Account", incomeAccount.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("incomeAccount", outter);
		}
		jsonObject.put("isTaxable", item.isTaxable());
		jsonObject.put("isCommissionItem", item.isCommissionItem());
		jsonObject.put("standardCost", item.getStandardCost());
		ItemGroup itemGroup = item.getItemGroup();
		if (itemGroup != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.ItemGroup", inter);
			String localId = context.getLocalIdProvider().getOrCreate(itemGroup);
			inter.put("@_oid", context.get("ItemGroup", itemGroup.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("itemGroup", outter);
		}
		jsonObject.put("inActive", !item.isActive());
		jsonObject.put("iBuyThisService", item.isIBuyThisItem());
		jsonObject.put("purchaseDescription", item.getPurchaseDescription());
		jsonObject.put("purchasePrice", item.getPurchasePrice());
		Account expenseAccount = item.getExpenseAccount();
		if (expenseAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(expenseAccount);
			inter.put("@_oid", context.get("Account", expenseAccount.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("expenseAccount", outter);
		}
		Vendor preferredVendor = item.getPreferredVendor();
		if (preferredVendor != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.vendor.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(preferredVendor);
			inter.put("@_oid", context.get("Vendor", preferredVendor.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("preferredVendor", outter);
		}

		jsonObject.put("vendorServiceNumber", item.getVendorItemNumber());
		jsonObject.put("itemType", PicklistUtilMigrator.getItemTypeIdentifier(item.getType()));
		Account assestsAccount = item.getAssestsAccount();
		if (assestsAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(expenseAccount);
			inter.put("@_oid", context.get("Account", expenseAccount.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("assetAccount", outter);
		}

		Quantity reorderPoint = item.getReorderPoint();
		if (reorderPoint != null) {
			double value = reorderPoint.getValue();
			Unit unit = reorderPoint.getUnit();

			if (unit != null) {
				double val = unit.getFactor() * value;
				jsonObject.put("reOrderPoint", BigDecimal.valueOf(val));
			} else {
				jsonObject.put("reOrderPoint", BigDecimal.valueOf(value));
			}
		}
		Account costOfGoodsSold = item.getExpenseAccount();
		if (costOfGoodsSold != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(expenseAccount);
			inter.put("@_oid", context.get("Account", expenseAccount.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("costOfGoodsSold", outter);
		}
		Warehouse warehouse = item.getWarehouse();
		if (warehouse != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.inventory.Warehouse", inter);
			String localId = context.getLocalIdProvider().getOrCreate(warehouse);
			inter.put("@_oid", context.get("Warehouse", warehouse.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("warehouse", outter);
		}
		Measurement measurement = item.getMeasurement();
		if (measurement != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.inventory.Measurement", inter);
			String localId = context.getLocalIdProvider().getOrCreate(measurement);
			inter.put("@_oid", context.get("Measurement", measurement.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("measurement", outter);
		}

		jsonObject.put("averageCost", BigDecimal.valueOf(item.getAverageCost()));

		Map<String, List<Long>> childrenMap = context.getChildrenMap();
		String identity = "com.nitya.accounter.shared.inventory.InventoryAssemblyItem";
		context.putChilderName("InventoryAssembly", "assemblyItems");
		List<Long> list = childrenMap.get(identity);
		if (list == null) {
			list = new ArrayList<Long>();
			childrenMap.put(identity, list);
		}

		JSONArray array = new JSONArray();
		JSONObject items = new JSONObject();
		items.put(identity, array);
		Set<InventoryAssemblyItem> components = item.getComponents();
		for (InventoryAssemblyItem obj : components) {
			JSONObject jsonitem = new JSONObject();
			JSONObject jsonItemObj = new JSONObject();
			jsonItemObj.put("com.nitya.accounter.shared.inventory.InventoryAssemblyItem", jsonitem);
			Item inventoryItem = obj.getInventoryItem();
			if (inventoryItem != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.inventory.InventoryItem", inter);
				String localId = context.getLocalIdProvider().getOrCreate(inventoryItem);
				inter.put("@_oid", context.get("Item", inventoryItem.getID()));
				inter.put("@_lid", localId);
				jsonitem.put("item", outter);
			}
			String localId = context.getLocalIdProvider().getOrCreate(obj);
			jsonitem.put("@id", localId);
			jsonitem.put("@_lid", localId);
			jsonitem.put("description", obj.getDiscription());
			list.add(obj.getID());
			array.put(jsonitem);
		}

		jsonObject.put("assemblyItems", items);
		return external;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Item.TYPE_INVENTORY_ASSEMBLY));
	}
}
