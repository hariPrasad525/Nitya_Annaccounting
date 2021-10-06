package com.nitya.accounter.migration;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.ItemGroup;
import com.nitya.accounter.core.Vendor;

public class ServiceItemMigrator implements IMigrator<Item> {

	@Override
	public JSONObject migrate(Item item, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject extenal = new JSONObject();
		extenal.put("com.nitya.accounter.shared.common.ServiceItem", jsonObject);
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
			outter.put("com.nitya.accounter.shared.common.ServiceItem", inter);
			String localId = context.getLocalIdProvider().getOrCreate(parentItem);
			inter.put("@_oid", context.get("ServiceItem", parentItem.getID()));
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
			outter.put("com.nitya.accounter.shared.common.Account", inter);
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
			outter.put("com.nitya.accounter.shared.common.ItemGroup", inter);
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
		return extenal;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Item.TYPE_SERVICE));
	}
}