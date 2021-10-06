package com.nitya.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.core.TAXCode;

public class CompanyDefatultTaxCodeMigrator implements IMigrator<CompanyPreferences> {
	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context) throws JSONException {
		JSONObject object = new JSONObject();
		{
			JSONObject commonSettings = new JSONObject();
			object.put("com.nitya.accounter.shared.common.CommonSettings", commonSettings);
			commonSettings.put("@_oid",
					context.get(CompanyMigrator.COMMON_SETTINGS, CompanyMigrator.COMMON_SETTINGS_OLD_ID));
			commonSettings.put("@_lid", context.getLocalIdProvider().getOrCreate(obj));
			TAXCode defaultTaxCode = obj.getDefaultTaxCode();
			if (defaultTaxCode != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.tax.TAXCode", inter);
				inter.put("@_oid", context.get("TaxCode", defaultTaxCode.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(defaultTaxCode));
				commonSettings.put("noneTaxCode", outter);
			}
		}
		return object;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
	}

}
