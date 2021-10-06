package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Warehouse;

public class CommonSettingsMigrator implements IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject object = new JSONObject();
		{
			JSONObject commonSettings = new JSONObject();
			object.put("com.vimukti.accounter.shared.common.CommonSettings",
					commonSettings);
			commonSettings.put("@_oid", context.get(
					CompanyMigrator.COMMON_SETTINGS,
					CompanyMigrator.COMMON_SETTINGS_OLD_ID));
			commonSettings.put("@_lid", context.getLocalIdProvider()
					.getOrCreate(obj));
			Company company = context.getCompany();
			{
				Account accountsPayableAccount = company
						.getAccountsPayableAccount();
				JSONObject accountPayable = new JSONObject();
				JSONObject json = new JSONObject();
				accountPayable.put(
						"com.vimukti.accounter.shared.common.Account", json);
				String parentLocalID = context.getLocalIdProvider()
						.getOrCreate(accountsPayableAccount);
				json.put("@_oid",
						context.get("Account", accountsPayableAccount.getID()));
				json.put("@_lid", parentLocalID);
				commonSettings.put("accountPayable", accountPayable);
			}
			{
				Account accountsReceivableAccount = company
						.getAccountsReceivableAccount();
				JSONObject accountReceivable = new JSONObject();
				JSONObject json = new JSONObject();
				accountReceivable.put(
						"com.vimukti.accounter.shared.common.Account", json);
				String parentLocalID = context.getLocalIdProvider()
						.getOrCreate(accountsReceivableAccount);
				json.put(
						"@_oid",
						context.get("Account",
								accountsReceivableAccount.getID()));
				json.put("@_lid", parentLocalID);
				commonSettings.put("accountReceivable", accountReceivable);
			}
			{
				Account salariesPayableAccount = company
						.getSalariesPayableAccount();
				if (salariesPayableAccount != null) {
					commonSettings.put(
							"salariesPayable",
							context.get("Account",
									salariesPayableAccount.getID()));
					JSONObject salariesPayable = new JSONObject();
					JSONObject json = new JSONObject();
					salariesPayable
							.put("com.vimukti.accounter.shared.common.Account",
									json);
					String parentLocalID = context.getLocalIdProvider()
							.getOrCreate(salariesPayableAccount);
					json.put(
							"@_oid",
							context.get("Account",
									salariesPayableAccount.getID()));
					json.put("@_lid", parentLocalID);
					commonSettings.put("salariesPayable", salariesPayable);
				}
			}
			{
				Account taxFiledLiabilityAccount = company
						.getTAXFiledLiabilityAccount();
				JSONObject taxFiled = new JSONObject();
				JSONObject json = new JSONObject();
				taxFiled.put("com.vimukti.accounter.shared.common.Account",
						json);
				String parentLocalID = context.getLocalIdProvider()
						.getOrCreate(taxFiledLiabilityAccount);
				json.put("@_oid", context.get("Account",
						taxFiledLiabilityAccount.getID()));
				json.put("@_lid", parentLocalID);
				commonSettings.put("taxFiled", taxFiled);
			}
			Measurement measurement = company.getDefaultMeasurement();
			if (measurement != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put(
						"com.vimukti.accounter.shared.inventory.Measurement",
						inter);
				String localId = context.getLocalIdProvider().getOrCreate(
						measurement);
				inter.put("@_oid",
						context.get("Measurement", measurement.getID()));
				inter.put("@_lid", localId);
				commonSettings.put("defaultMeasurement", outter);
			}
			Warehouse warehouse = company.getDefaultWarehouse();
			if (warehouse != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.vimukti.accounter.shared.inventory.Warehouse",
						inter);
				String localId = context.getLocalIdProvider().getOrCreate(
						warehouse);
				inter.put("@_oid", context.get("Warehouse", warehouse.getID()));
				inter.put("@_lid", localId);
				commonSettings.put("defaultWarehouse", outter);
			}
			{
				Account openingBalancesAccount = company
						.getOpeningBalancesAccount();
				JSONObject openingBalances = new JSONObject();
				JSONObject json = new JSONObject();
				openingBalances.put(
						"com.vimukti.accounter.shared.common.Account", json);
				String parentLocalID = context.getLocalIdProvider()
						.getOrCreate(openingBalancesAccount);
				json.put("@_oid",
						context.get("Account", openingBalancesAccount.getID()));
				json.put("@_lid", parentLocalID);
				commonSettings.put("openingBalances", openingBalances);
			}
			{
				Account exchangeLossOrGainAccount = company
						.getExchangeLossOrGainAccount();
				JSONObject exchangeLossorGain = new JSONObject();
				JSONObject json = new JSONObject();
				exchangeLossorGain.put(
						"com.vimukti.accounter.shared.common.Account", json);
				String parentLocalID = context.getLocalIdProvider()
						.getOrCreate(exchangeLossOrGainAccount);
				json.put(
						"@_oid",
						context.get("Account",
								exchangeLossOrGainAccount.getID()));
				json.put("@_lid", parentLocalID);
				commonSettings.put("exchangeLossorGain", exchangeLossorGain);
			}
			{
				Account costOfGoodsSoldACC = company.getCostOfGoodsSold();
				JSONObject costOfGoodsSold = new JSONObject();
				JSONObject json = new JSONObject();
				costOfGoodsSold.put(
						"com.vimukti.accounter.shared.common.Account", json);
				String parentLocalID = context.getLocalIdProvider()
						.getOrCreate(costOfGoodsSoldACC);
				json.put("@_oid",
						context.get("Account", costOfGoodsSoldACC.getID()));
				json.put("@_lid", parentLocalID);
				commonSettings.put("costOfGoodsSold", costOfGoodsSold);
			}
		}
		return object;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
	}

}
