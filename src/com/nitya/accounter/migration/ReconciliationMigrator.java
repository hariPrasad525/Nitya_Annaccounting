package com.nitya.accounter.migration;

import java.math.BigDecimal;
import java.util.Set;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Reconciliation;
import com.nitya.accounter.core.ReconciliationItem;

public class ReconciliationMigrator implements IMigrator<Reconciliation> {
	@Override
	public JSONObject migrate(Reconciliation obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.bank.Reconciliation", jsonObject);
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		Account account = obj.getAccount();
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.bank.BankAccount", inter);
			inter.put("@_oid", context.get("Account", account.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(account));
			jsonObject.put("account", outter);
		}
		jsonObject.put("startDate", obj.getStartDate().toEpochDay());
		jsonObject.put("endDate", obj.getEndDate().toEpochDay());
		jsonObject.put("reconciliationDate", obj.getReconcilationDate().toEpochDay());
		jsonObject.put("closingBalance", BigDecimal.valueOf(obj.getClosingBalance()));
		Set<ReconciliationItem> items = obj.getItems();
		JSONArray array = new JSONArray();
		JSONObject rItems = new JSONObject();
		rItems.put("com.nitya.accounter.shared.bank.ReconciliationItem", array);
		for (ReconciliationItem item : items) {
			JSONObject jsonObject1 = new JSONObject();
			String _llid = context.getLocalIdProvider().getOrCreate(item);
			jsonObject1.put("@id", _llid);
			jsonObject1.put("@_lid", _llid);
			jsonObject1.put("transactionNumber", item.getTransactionNo());
			jsonObject1.put("transactionDate", item.getTransactionDate().toEpochDay());
			jsonObject1.put("memo", item.getTransctionMemo());
			jsonObject1.put("amount", BigDecimal.valueOf(item.getAmount()));
			array.put(jsonObject1);
		}
		jsonObject.put("reconciliationItems", rItems);
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
