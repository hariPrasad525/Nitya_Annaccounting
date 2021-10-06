package com.vimukti.accounter.migration;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.BuildAssembly;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.InventoryAssembly;
import com.vimukti.accounter.core.InventoryAssemblyItem;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.Location;

public class BuildAssemblyMigrator extends TransactionMigrator<BuildAssembly> {

	@Override
	public JSONObject migrate(BuildAssembly obj, MigratorContext context) throws JSONException {
		JSONObject buildAssembly = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.inventory.BuildAssembly", buildAssembly);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		buildAssembly.put("@id", _lid);
		buildAssembly.put("@_lid", _lid);

		CommonFieldsMigrator.migrateCommonFields(obj, buildAssembly, context);

		InventoryAssembly inventoryAssembly = obj.getInventoryAssembly();
		buildAssembly.put("inventoryAssembly", context.get("Item", inventoryAssembly.getID()));
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.inventory.InventoryAssembly", inter);
			String localId = context.getLocalIdProvider().getOrCreate(inventoryAssembly);
			inter.put("@_oid", context.get("Item", inventoryAssembly.getID()));
			inter.put("@_lid", localId);
			buildAssembly.put("inventoryAssembly", outter);
		}

		buildAssembly.put("date", obj.getDate().toEpochDay());
		buildAssembly.put("number", obj.getNumber());
		AccounterClass accounterClass = obj.getAccounterClass();
		if (accounterClass != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.AccountClass", inter);
			String localId = context.getLocalIdProvider().getOrCreate(inventoryAssembly);
			inter.put("@_oid", context.get("AccounterClass", inventoryAssembly.getID()));
			inter.put("@_lid", localId);
			buildAssembly.put("accountClass", outter);
		}
		Location location = obj.getLocation();
		if (location != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Location", inter);
			String localId = context.getLocalIdProvider().getOrCreate(location);
			inter.put("@_oid", context.get("Location", location.getID()));
			inter.put("@_lid", localId);
			buildAssembly.put("location", outter);
		}
		JSONArray array = new JSONArray();
		JSONObject items = new JSONObject();
		items.put("com.vimukti.accounter.shared.inventory.BuildAssemblyItem", array);
		Set<InventoryAssemblyItem> transactionItems = inventoryAssembly.getComponents();
		for (InventoryAssemblyItem transactionItem : transactionItems) {
			JSONObject object = new JSONObject();
			String unitlid = context.getLocalIdProvider().getOrCreate(transactionItem);
			object.put("@id", unitlid);
			object.put("@_lid", unitlid);
			{
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.vimukti.accounter.shared.inventory.InventoryAssemblyItem", inter);
				String localId = context.getLocalIdProvider().getOrCreate(transactionItem);
				inter.put("@_oid", context.get("com.vimukti.accounter.shared.inventory.InventoryAssemblyItem",
						transactionItem.getID()));
				inter.put("@_lid", localId);
				object.put("item", outter);
			}
			object.put("description", transactionItem.getDiscription());
			array.put(object);
		}
		buildAssembly.put("assemblyItems", items);
		// TODO Currency
		Currency currency = obj.getCurrency();
		if (currency != null) {
			JSONObject currencyJson = new JSONObject();
			JSONObject json = new JSONObject();
			currencyJson.put("com.vimukti.accounter.shared.common.AccounterCurrency", json);
			json.put("@_oid", context.get("Currency", currency.getID()));
			json.put("@_lid", context.getLocalIdProvider().getOrCreate(currency));
			buildAssembly.put("currency", currencyJson);
		}
		buildAssembly.put("currencyFactor", BigDecimal.valueOf(obj.getCurrencyFactor()));
		buildAssembly.put("notes", obj.getMemo());
		buildAssembly.put("transactionType", PicklistUtilMigrator.getTransactionTypeIdentifier(obj.getType(), 0));
		Job job = obj.getJob();
		if (job != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.customer.Project", inter);
			String localId = context.getLocalIdProvider().getOrCreate(location);
			inter.put("@_oid", context.get("Job", location.getID()));
			inter.put("@_lid", localId);
			buildAssembly.put("project", outter);
		}
		buildAssembly.put("quantitytoBuild", BigDecimal.valueOf(obj.getQuantityToBuild()));
		return external;
	}
}
