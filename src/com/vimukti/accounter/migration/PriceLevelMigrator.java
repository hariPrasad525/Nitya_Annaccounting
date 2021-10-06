package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PriceLevel;

public class PriceLevelMigrator implements IMigrator<PriceLevel> {

	@Override
	public JSONObject migrate(PriceLevel obj, MigratorContext context) throws JSONException {
		JSONObject priceLevel = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			priceLevel.put("com.vimukti.accounter.shared.customer.PriceLevel", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("name", obj.getName());
		}
		return priceLevel;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}