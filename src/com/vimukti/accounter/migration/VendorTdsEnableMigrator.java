package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.Vendor;

public class VendorTdsEnableMigrator implements IMigrator<Vendor> {
	boolean isResetTds;

	public VendorTdsEnableMigrator(boolean enableTds) {
		this.isResetTds = enableTds;
	}

	@Override
	public JSONObject migrate(Vendor obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("@_lid", context.getLocalIdProvider().getOrCreate(obj));
		jsonObject.put("@_oid", context.get("Vendor", obj.getID()));
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.vendor.Vendor", jsonObject);
		if (isResetTds) {
			jsonObject.put("tDSApplicable", obj.isTdsApplicable());
		} else {
			jsonObject.put("tDSApplicable", true);
		}
		TAXItem taxItem = obj.getTAXItem();
		if (taxItem != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.tax.TAXItem", inter);
			inter.put("@_oid", context.get("Tax", taxItem.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(taxItem));
			jsonObject.put("vendorTDSCode", outter);
		}
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
