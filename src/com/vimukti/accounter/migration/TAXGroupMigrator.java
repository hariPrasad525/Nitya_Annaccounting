package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;

public class TAXGroupMigrator extends TaxMigrator<TAXGroup> {

	@Override
	public JSONObject migrate(TAXGroup obj, MigratorContext context) throws JSONException {
		JSONObject tAXGroup = new JSONObject();
		{
			JSONObject jsonObject = super.migrate(obj, context);
			tAXGroup.put("com.vimukti.accounter.shared.tax.TaxGroup", jsonObject);
			jsonObject.put("isTaxGroup", true);
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			JSONArray taxItemsJSON = new JSONArray();
			JSONObject taxItemsJson = new JSONObject();
			taxItemsJson.put("com.vimukti.accounter.shared.tax.TAXITem", taxItemsJSON);
			List<TAXItem> taxItems = obj.getTAXItems();
			if (taxItems.isEmpty()) {
				return null;
			}
			for (TAXItem item : taxItems) {
				JSONObject json = new JSONObject();
				String _taxItemLid = context.getLocalIdProvider().getOrCreate(item);
				json.put("@_lid", _taxItemLid);
				json.put("@_oid", context.get("Tax", item.getID()));
				taxItemsJSON.put(json);
			}
			jsonObject.put("taxGroupItems", taxItemsJson);
		}
		return tAXGroup;
	}
}