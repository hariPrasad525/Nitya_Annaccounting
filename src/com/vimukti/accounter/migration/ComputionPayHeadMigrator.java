package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.ComputaionFormulaFunction;
import com.vimukti.accounter.core.ComputationSlab;
import com.vimukti.accounter.core.ComputionPayHead;

public class ComputionPayHeadMigrator implements IMigrator<ComputionPayHead> {

	@Override
	public JSONObject migrate(ComputionPayHead obj, MigratorContext context) throws JSONException {
		JSONObject payHead = new JSONObject();
		payHead.put("@class", "com.vimukti.accounter.shared.payroll.PayHead");
		CommonFieldsMigrator.migrateCommonFields(obj, payHead, context);
		payHead.put("name", obj.getName());
		payHead.put("payHeadType", PicklistUtilMigrator.getPayHeadType(obj.getType()));
		Account account = obj.getAccount();
		if (account != null) {
			payHead.put("expenseAccount", context.get("Account", account.getID()));
		}
		payHead.put("calculationType", PicklistUtilMigrator.getCalculationType(obj.getCalculationType()));
		payHead.put("paySlipName", obj.getNameToAppearInPaySlip());
		payHead.put("isDeduction", obj.isDeduction());
		payHead.put("isEarning", obj.isEarning());

		// assert Account
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			payHead.put("assetAccount", context.get("Account", assetAccount.getID()));
		}
		// laibility Account
		Account liabilityAccount = obj.getLiabilityAccount();
		if (liabilityAccount != null) {
			payHead.put("statutoryLiabilityAccount", context.get("Account", liabilityAccount.getID()));
		}

		payHead.put("calculationPeriod", PicklistUtilMigrator.getCalculationPeriod(obj.getCalculationPeriod()));

		payHead.put("perDayCalculationBasis",
				PicklistUtilMigrator.getPerdayCalculationBasis(obj.getCalculationPeriod()));

		payHead.put("computeOn", PicklistUtilMigrator.getComputationType(obj.getComputationType()));

		JSONArray jsonSlabs = new JSONArray();
		for (ComputationSlab slab : obj.getSlabs()) {
			JSONObject jsonSlab = new JSONObject();
			jsonSlab.put("@class", "com.vimukti.accounter.shared.payroll.ComputationSlab");
			jsonSlab.put("effectiveFrom", slab.getEffectiveFrom().getAsDateObject().getTime());
			jsonSlab.put("fromAmount", slab.getFromAmount());
			jsonSlab.put("amountUpto", slab.getToAmount());
			jsonSlab.put("slabType", PicklistUtilMigrator.getComputationSlabType(slab.getSlabType()));
			jsonSlab.put("value", slab.getValue());
			jsonSlabs.put(jsonSlab);
		}
		payHead.put("computationSlabs", jsonSlabs);
		JSONArray jsonformulaItems = new JSONArray();
		for (ComputaionFormulaFunction formulaItem : obj.getFormulaFunctions()) {
			JSONObject jsonFormula = new JSONObject();
			jsonFormula.put("@class", "com.vimukti.accounter.shared.payroll.PayHeadFormulaItem");
			jsonFormula.put("functionType",
					PicklistUtilMigrator.getPayHeadFormulaFunctionType(formulaItem.getFunctionType()));
			jsonformulaItems.put(jsonFormula);
		}
		payHead.put("formulaItems", jsonformulaItems);
		return payHead;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
