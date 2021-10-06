package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FixedAsset;

public class FixedAssetMigrator implements IMigrator<FixedAsset> {
	@Override
	public JSONObject migrate(FixedAsset obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.common.FixedAsset", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		// Fixed Asset status
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("status", PicklistUtilMigrator.getFixedAssetStatusIdentifier(obj.getStatus()));
		jsonObject.put("name", obj.getName());
		// description
		jsonObject.put("description", obj.getDescription());
		// assetAccount
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", assetAccount.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(assetAccount));
			jsonObject.put("assetAccount", outter);
		}
		// purchasePrice
		jsonObject.put("purchaseDate", obj.getPurchaseDate().toEpochDay());
		jsonObject.put("purchasePrice", BigDecimal.valueOf(obj.getPurchasePrice()));
		// assetType
		jsonObject.put("assetType", obj.getAssetType());
		// depreciationRate
		double val = obj.getDepreciationRate() / 100;
		jsonObject.put("depreciationRate", BigDecimal.valueOf(val));
		// depreciationMethod
		jsonObject.put("depreciationMethod",
				PicklistUtilMigrator.depreciationMethodIdentity(obj.getDepreciationMethod()));
		// depreciationExpenseAccount
		Account depreciationExpenseAccount = obj.getDepreciationExpenseAccount();
		if (depreciationExpenseAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", depreciationExpenseAccount.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(depreciationExpenseAccount));
			jsonObject.put("depreciationExpenseAccount", outter);
		}
		// accumulatedDepreciationAccount
		Account accumulatedDepreciationAccount = obj.getAccumulatedDepreciationAccount();
		if (accumulatedDepreciationAccount != null) {
			jsonObject.put("accumulatedDepreciationAccount",
					context.get("Account", accumulatedDepreciationAccount.getID()));
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", accumulatedDepreciationAccount.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(accumulatedDepreciationAccount));
			jsonObject.put("accumulatedDepreciationAccount", outter);
		}
		// accumulatedDepreciationAmount
		jsonObject.put("accumulatedDepreciationAmount", BigDecimal.valueOf(obj.getAccumulatedDepreciationAmount()));
		// soldOrDisposedDate
		FinanceDate soldOrDisposedDate = obj.getSoldOrDisposedDate();
		if (soldOrDisposedDate != null) {
			jsonObject.put("soldOrDisposedDate", soldOrDisposedDate.toEpochDay());
		}
		// accountForSale
		Account accountForSale = obj.getAccountForSale();
		if (accountForSale != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", accountForSale.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(accountForSale));
			jsonObject.put("accountForSale", outter);
		}
		// salesPrice
		jsonObject.put("salesPrice", BigDecimal.valueOf(obj.getSalePrice()));
		// noDepreciation is not found
		// depreciationTillDate
		FinanceDate depreciationTillDate = obj.getDepreciationTillDate();
		if (depreciationTillDate != null) {
			jsonObject.put("depreciationTillDate", depreciationTillDate.toEpochDay());
		}
		// notes
		jsonObject.put("notes", obj.getNotes());
		jsonObject.put("assetNumber", obj.getAssetNumber());
		// lossOrDisposalAccount
		Account lossOrGainOnDisposalAccount = obj.getLossOrGainOnDisposalAccount();
		if (lossOrGainOnDisposalAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", lossOrGainOnDisposalAccount.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(lossOrGainOnDisposalAccount));
			jsonObject.put("lossOrDisposalAccount", outter);
		}
		// totalCapitalGain
		Account totalCapitalGain = obj.getTotalCapitalGain();
		if (totalCapitalGain != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", totalCapitalGain.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(totalCapitalGain));
			jsonObject.put("tcGainAccount", outter);
		}
		// depreciationToBePostedAmount is not found
		// rollBackDepreciationAmount is not found
		// journalEntry is not found
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}