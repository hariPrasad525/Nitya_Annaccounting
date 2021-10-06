package com.vimukti.accounter.migration;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Vendor;

public class ProductItemMigrator implements IMigrator<Item> {
	@Override
	public JSONObject migrate(Item item, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.common.ProductItem", jsonObject);
		CommonFieldsMigrator.migrateCommonFields(item, jsonObject, context);
		String _lid = context.getLocalIdProvider().getOrCreate(item);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		jsonObject.put("name", item.getName());
		jsonObject.put("isSubItemOf", item.isSubItemOf());
		Item parentItem = item.getParentItem();
		if (parentItem != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.ProductItem", inter);
			String localId = context.getLocalIdProvider().getOrCreate(parentItem);
			inter.put("@_oid", context.get("ProductItem", parentItem.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("subItemOf", outter);
		} else {
			jsonObject.put("isSubItemOf", false);
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
		return external;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Item.TYPE_NON_INVENTORY_PART));
	}
}
