package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.UserDefinedPayHead;

public class UserDefinedPayHeadMigrator implements IMigrator<UserDefinedPayHead> {

	@Override
	public JSONObject migrate(UserDefinedPayHead obj, MigratorContext context) throws JSONException {
		JSONObject payHead = new JSONObject();
		payHead.put("@class", "com.vimukti.accounter.shared.payroll.PayHead");
		CommonFieldsMigrator.migrateCommonFields(obj, payHead, context);
		payHead.put("name", obj.getName());
		payHead.put("payHeadType", PicklistUtilMigrator.getPayHeadType(obj.getType()));
		payHead.put("isAffectNetSalary", obj.isAffectNetSalary());
		Account account = obj.getAccount();
		if (account != null) {
			payHead.put("expenseAccount", context.get("Account", account.getID()));
		}
		payHead.put("calculationType", PicklistUtilMigrator.getCalculationType(obj.getCalculationType()));
		payHead.put("paySlipName", obj.getNameToAppearInPaySlip());
		payHead.put("isDeduction", obj.isDeduction());
		payHead.put("isEarning", obj.isEarning());
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			payHead.put("assetAccount", context.get("Account", assetAccount.getID()));
		}
		Account liabilityAccount = obj.getLiabilityAccount();
		if (liabilityAccount != null) {
			payHead.put("statutoryLiabilityAccount", context.get("Account", liabilityAccount.getID()));
		}
		return payHead;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
