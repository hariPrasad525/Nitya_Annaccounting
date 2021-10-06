package com.vimukti.accounter.migration;

import java.util.List;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Budget;
import com.vimukti.accounter.core.BudgetItem;

public class BudgetMigrator implements IMigrator<Budget> {

	@Override
	public JSONObject migrate(Budget budget, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(budget, jsonObject, context);
		jsonObject.put("budgetName", budget.getBudgetName());
		jsonObject.put("financialYear", budget.getFinancialYear());
		List<BudgetItem> budgetItems = budget.getBudgetItems();
		JSONArray array = new JSONArray();
		for (BudgetItem budgetItem : budgetItems) {
			JSONObject jsonObject1 = new JSONObject();
			// Account
			Account account = budgetItem.getAccount();
			JSONObject budgetAccount = new JSONObject();
			budgetAccount.put("name", account.getName());
			jsonObject1.put("account", budgetAccount);

			jsonObject1.put("january", budgetItem.getJanuaryAmount());
			jsonObject1.put("february", budgetItem.getFebruaryAmount());
			jsonObject1.put("march", budgetItem.getMarchAmount());
			jsonObject1.put("april", budgetItem.getAprilAmount());
			jsonObject1.put("may", budgetItem.getMayAmount());
			jsonObject1.put("june", budgetItem.getJuneAmount());
			jsonObject1.put("july", budgetItem.getJulyAmount());
			jsonObject1.put("august", budgetItem.getAugustAmount());
			jsonObject1.put("september", budgetItem.getSeptemberAmount());
			jsonObject1.put("october", budgetItem.getOctoberAmount());
			jsonObject1.put("november", budgetItem.getNovemberAmount());
			jsonObject1.put("december", budgetItem.getDecemberAmount());
			jsonObject1.put("totalAmount", budgetItem.getTotalAmount());
			array.put(jsonObject1);
		}
		jsonObject.put("budgetItems", array);

		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}