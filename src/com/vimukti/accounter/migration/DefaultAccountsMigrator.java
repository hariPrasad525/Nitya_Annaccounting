
package com.vimukti.accounter.migration;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility;

public class DefaultAccountsMigrator implements IMigrator<Account> {
	String[] names = new String[] { "Account Receivable", "Account Payable", "Central Sales Tax Payable", "Tax Filed",
			"Opening Balances", "Exchange Loss or Gain", "Salaries Payable", "Inventory Assets", "TDS Tax Payable",
			"Service Tax Payable", "Cost Of Goods Sold" };

	@Override
	public JSONObject migrate(Account account, MigratorContext context) throws JSONException {
		List<String> asList = Arrays.asList(names);
		if (account.isDefault() || asList.contains(account.getName()) || account.getName().equals("Debtors")
				|| account.getName().equals("Creditors")) {
			JSONObject obj = new JSONObject();
			{
				JSONObject accountJSON = new JSONObject();
				obj.put("com.vimukti.accounter.shared.common.Account", accountJSON);
				String _lid = context.getLocalIdProvider().getOrCreate(account);
				accountJSON.put("@id", _lid);
				accountJSON.put("@_lid", _lid);
				CommonFieldsMigrator.migrateCommonFields(account, accountJSON, context);
				// Name
				String name = account.getName();
				if (name.equals("Debtors")) {
					name = "Account Receivable";
				} else if (name.equals("Creditors")) {
					name = "Account Payable";
				}
				accountJSON.put("name", name);
				// AsOF Date
				FinanceDate asOf = account.getAsOf();
				if (asOf != null) {
					accountJSON.put("asOf", asOf.toEpochDay());
				} else {
					accountJSON.put("asOf", new FinanceDate(account.getCreatedDate()).toEpochDay());
				}
				Account parent = account.getParent();
				if (parent != null) {
					JSONObject currencyJson = new JSONObject();
					JSONObject json = new JSONObject();
					currencyJson.put("com.vimukti.accounter.shared.common.Account", json);
					String parentLocalID = context.getLocalIdProvider().getOrCreate(parent);
					json.put("@id", parentLocalID);
					json.put("@_lid", parentLocalID);
					accountJSON.put("subAccountOf", currencyJson);
				}
				accountJSON.put("type", PicklistUtilMigrator.getAccountTypeIdentity(account.getType()));
				// Currency
				{
					JSONObject currencyJson = new JSONObject();
					JSONObject json = new JSONObject();
					currencyJson.put("com.vimukti.accounter.shared.common.AccounterCurrency", json);
					String _currencyLid = context.getLocalIdProvider().getOrCreate(account.getCurrency());
					json.put("@_lid", _currencyLid);
					json.put("@_oid", context.get("Currency", account.getCurrency().getID()));
					accountJSON.put("currency", currencyJson);
				}

				accountJSON.put("number", context.getNextAccountNumber());
				accountJSON.put("inactive", !account.getIsActive());
				accountJSON.put("description", account.getComment());
				accountJSON.put("openingBalance", BigDecimal.ZERO);
				accountJSON.put("payPalEmail", account.getPaypalEmail());
				accountJSON.put("cashFlowCategory",
						Utility.getCashFlowCategoryName(account.getCashFlowCategory()).toUpperCase());
				accountJSON.put("currencyFactor", BigDecimal.valueOf(account.getCurrencyFactor()));
				accountJSON.put("isIncrease", account.isIncrease());
			}
			return obj;
		}
		return null;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.ne("type", 2));
	}
}