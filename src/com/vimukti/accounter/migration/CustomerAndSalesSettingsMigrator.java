package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CompanyPreferences;

public class CustomerAndSalesSettingsMigrator implements IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.customer.CustomerAndSalesSettings", jsonObject);
		jsonObject.put("itemType", itemTypeByIdentity(obj.isSellServices(), obj.isSellProducts()));
		jsonObject.put("inventoryTracking", obj.isInventoryEnabled());
		jsonObject.put("haveMultipleWarehouses", obj.iswareHouseEnabled());
		jsonObject.put("enableInventoryUnits", obj.isUnitsEnabled());
		jsonObject.put("inventoryScheme", getInventorySchemeString(obj.getActiveInventoryScheme()));
		jsonObject.put("trackDiscount", obj.isTrackDiscounts());
		jsonObject.put("discountInTransactions",
				obj.isDiscountPerDetailLine() ? "ONE_PER_DETAIL_LINE" : "ONE_PER_TANSACTION");
		// DiscountAccount is not found
		jsonObject.put("useDelayedCharges", obj.isDelayedchargesEnabled());
		jsonObject.put("enablePriceLevel", obj.isPricingLevelsEnabled());
		if (obj.isEnabledRoundingOptions()) {
			Account roundingAccount = obj.getRoundingAccount();
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", roundingAccount.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(roundingAccount));
			jsonObject.put("roundingAccount", outter);
			jsonObject.put("roundingLimit", BigDecimal.valueOf(obj.getRoundingLimit()));
			jsonObject.put("roundingMethod", getRoundingMethodIdentity(obj.getRoundingMethod()));
		}

		return external;
	}

	public static String getInventorySchemeString(int value) {
		switch (value) {
		case 1:
			return "FIRST_IN_FIRST_OUT";
		case 2:
			return "LAST_IN_FIRST_OUT";
		default:
			return "AVERAGE";
		}
	}

	public static String itemTypeByIdentity(Boolean service, Boolean product) {
		if (service && !product) {
			return "SERVICES_ONLY";
		}
		if (product && !service) {
			return "PRODUCTS_ONLY";
		}
		return "BOTH_SERVICES_AND_PRODUCTS";
	}

	public String getRoundingMethodIdentity(int val) {
		switch (val) {
		case 1:
			return "UP";
		case 2:
			return "DOWN";
		case 3:
			return "NORMAL";
		}
		return null;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
