package com.nitya.accounter.migration;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXItem;

public class TaxItemMigrator extends TaxMigrator<TAXItem> {

	@Override
	public JSONObject migrate(TAXItem obj, MigratorContext context) throws JSONException {
		JSONObject tAXItem = new JSONObject();
		{
			JSONObject jsonObject = super.migrate(obj, context);
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			tAXItem.put("com.nitya.accounter.shared.tax.TAXITem", jsonObject);
			double val = obj.getTaxRate() / 100;
			jsonObject.put("rate", BigDecimal.valueOf(val));
			TAXAgency taxAgency = obj.getTaxAgency();
			{
				JSONObject taxAgencyJson = new JSONObject();
				JSONObject json = new JSONObject();
				taxAgencyJson.put("com.nitya.accounter.shared.tax.TaxAgency", json);
				String _agencyLid = context.getLocalIdProvider().getOrCreate(taxAgency);
				json.put("@_lid", _agencyLid);
				json.put("@_oid", context.get("TaxAgency", taxAgency.getID()));
				jsonObject.put("taxAgency", taxAgencyJson);
			}
			jsonObject.put("description", obj.getDescription());
		}
		return tAXItem;
	}
}
