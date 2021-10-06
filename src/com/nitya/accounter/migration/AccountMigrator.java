package com.nitya.accounter.migration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Utility;

public class AccountMigrator implements IMigrator<Account> {

	String[] names = new String[] { "Account Receivable", "Account Payable", "Central Sales Tax Payable", "Tax Filed",
			"Opening Balances", "Exchange Loss or Gain", "Salaries Payable", "Inventory Assets", "TDS Tax Payable",
			"Service Tax Payable", "Cost Of Goods Sold" };

	@Override
	public JSONObject migrate(Account account, MigratorContext context) throws JSONException {
		List<String> asList = Arrays.asList(names);
		JSONObject external = new JSONObject();
		if (account.isDefault() || asList.contains(account.getName()) || account.getName().equals("Debtors")
				|| account.getName().equals("Creditors")) {
			return null;
		}
		JSONObject jsonObject = new JSONObject();
		external.put("com.nitya.accounter.shared.common.Account", jsonObject);

		String _lid = context.getLocalIdProvider().getOrCreate(account);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);

		CommonFieldsMigrator.migrateCommonFields(account, jsonObject, context);
		jsonObject.put("name", account.getName());
		FinanceDate asOf = account.getAsOf();
		if (asOf != null) {
			jsonObject.put("asOf", asOf.toEpochDay());
		} else {
			jsonObject.put("asOf", new FinanceDate(account.getCreatedDate()).toEpochDay());
		}
		if (account.getParent() != null) {
			{
				Account accountsPayableAccount = account.getParent();
				JSONObject accountPayable = new JSONObject();
				JSONObject json = new JSONObject();
				accountPayable.put("com.nitya.accounter.shared.common.Account", json);
				String parentLocalID = context.getLocalIdProvider().getOrCreate(accountsPayableAccount);
				json.put("@id", parentLocalID);
				json.put("@_lid", parentLocalID);
				jsonObject.put("subAccountOf", accountPayable);
			}
		}
		jsonObject.put("type", PicklistUtilMigrator.getAccountTypeIdentity(account.getType()));
		{
			Currency currency = account.getCurrency();
			JSONObject currencyJson = new JSONObject();
			JSONObject json = new JSONObject();
			currencyJson.put("com.nitya.accounter.shared.common.AccounterCurrency", json);
			json.put("@_oid", context.get("Currency", currency.getID()));
			json.put("@_lid", context.getLocalIdProvider().getOrCreate(currency));
			jsonObject.put("currency", currencyJson);
		}

		jsonObject.put("number", context.getNextAccountNumber());
		jsonObject.put("inactive", !account.getIsActive());
		jsonObject.put("description", account.getComment());
		jsonObject.put("openingBalance", BigDecimal.valueOf(account.getOpeningBalance()));
		jsonObject.put("payPalEmail", account.getPaypalEmail());
		jsonObject.put("cashFlowCategory",
				Utility.getCashFlowCategoryName(account.getCashFlowCategory()).toUpperCase());
		jsonObject.put("currencyFactor", BigDecimal.valueOf(account.getCurrencyFactor()));
		jsonObject.put("isIncrease", account.isIncrease());
		return external;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.ne("type", 2));
	}
}