package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.CompanyPreferences;

public class DefaultCustomerAndSalesSettingsMigrator implements IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context) throws JSONException {
		JSONObject customerAndSalesSetting = new JSONObject();
		JSONObject external = new JSONObject();
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		customerAndSalesSetting.put("@id", _lid);
		customerAndSalesSetting.put("@_lid", _lid);
		external.put("com.nitya.accounter.shared.customer.CustomerAndSalesSettings", customerAndSalesSetting);
		customerAndSalesSetting.put("itemType", CustomerAndSalesSettingsMigrator.itemTypeByIdentity(true, true));
		customerAndSalesSetting.put("inventoryTracking", true);
		customerAndSalesSetting.put("haveMultipleWarehouses", true);
		customerAndSalesSetting.put("enableInventoryUnits", obj.isUnitsEnabled());
		customerAndSalesSetting.put("inventoryScheme",
				CustomerAndSalesSettingsMigrator.getInventorySchemeString(obj.getActiveInventoryScheme()));
		customerAndSalesSetting.put("useDelayedCharges", true);
		customerAndSalesSetting.put("enablePriceLevel", true);
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}

}
