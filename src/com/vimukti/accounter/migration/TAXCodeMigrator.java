package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXItemGroup;

public class TAXCodeMigrator implements IMigrator<TAXCode> {

	@Override
	public JSONObject migrate(TAXCode taxcode, MigratorContext context) throws JSONException {
		JSONObject taxcodeJSON = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(taxcode);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			taxcodeJSON.put("com.vimukti.accounter.shared.tax.TAXCode", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(taxcode, jsonObject, context);
			jsonObject.put("name", taxcode.getName());
			boolean taxable = taxcode.isTaxable();
			jsonObject.put("isTaxable", taxable);
			if (taxable) {
				// Sales Tax
				TAXItemGroup taxItemGrpForSales = taxcode.getTAXItemGrpForSales();
				if (taxItemGrpForSales != null) {
					JSONObject currencyJson = new JSONObject();
					JSONObject json = new JSONObject();
					String className;
					if (taxItemGrpForSales instanceof TAXItem) {
						className = "com.vimukti.accounter.shared.tax.TAXITem";
					} else {
						className = "com.vimukti.accounter.shared.tax.TaxGroup";
					}
					currencyJson.put(className, json);
					String _currencyLid = context.getLocalIdProvider().getOrCreate(taxItemGrpForSales);
					json.put("@_lid", _currencyLid);
					json.put("@_oid", context.get("Tax", taxItemGrpForSales.getID()));
					jsonObject.put("taxItemOrGroupForSales", currencyJson);
				}
				// Purchase Tax
				TAXItemGroup taxItemGrpForPurchases = taxcode.getTAXItemGrpForPurchases();
				if (taxItemGrpForPurchases != null) {
					String className;
					if (taxItemGrpForSales instanceof TAXItem) {
						className = "com.vimukti.accounter.shared.tax.TAXITem";
					} else {
						className = "com.vimukti.accounter.shared.tax.TaxGroup";
					}
					JSONObject taxJson = new JSONObject();
					JSONObject json = new JSONObject();
					taxJson.put(className, json);
					String _currencyLid = context.getLocalIdProvider().getOrCreate(taxItemGrpForPurchases);
					json.put("@_lid", _currencyLid);
					json.put("@_oid", context.get("Tax", taxItemGrpForPurchases.getID()));
					jsonObject.put("taxItemOrGroupForPurchases", taxJson);
				}
			}
			jsonObject.put("description", taxcode.getDescription());
			jsonObject.put("inactive", !taxcode.isActive());
		}
		return taxcodeJSON;

	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}