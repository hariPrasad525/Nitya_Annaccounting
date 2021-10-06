package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;

public class DefaultFeaturesMigrator implements IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context) throws JSONException {
		JSONObject features = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.common.Features", features);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		features.put("@id", _lid);
		features.put("@_lid", _lid);
		features.put("enableLocationTracking", obj.isLocationTrackingEnabled());
		features.put("enableClassTracking", obj.isClassTrackingEnabled());
		if (obj.isClassTrackingEnabled()) {
			features.put("classTrackingType",
					obj.isClassPerDetailLine() ? "ONE_PER_DETAILLINE" : "ONE_PER_TRANSACTION");
		}
		features.put("enableShipping", obj.isDoProductShipMents());
		features.put("projectTracking", obj.isJobTrackingEnabled());
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}

}
