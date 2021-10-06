package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Bank;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility;

public class BankAccountMigrator implements IMigrator<BankAccount> {

	@Override
	public JSONObject migrate(BankAccount bankAccount, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.bank.BankAccount", jsonObject);

		String _lid = context.getLocalIdProvider().getOrCreate(bankAccount);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);

		CommonFieldsMigrator.migrateCommonFields(bankAccount, jsonObject, context);

		Bank bank = bankAccount.getBank();
		if (bank != null) {
			jsonObject.put("bankName", bank.getName());
		}
		jsonObject.put("bankAccountType", getBankAccountTypeIdentity(bankAccount.getBankAccountType()));
		String bankAccountNumber = bankAccount.getBankAccountNumber();
		if (bankAccountNumber != null && !bankAccountNumber.isEmpty()) {
			jsonObject.put("bankAccountNumber", Long.valueOf(bankAccountNumber));
		}
		jsonObject.put("number", context.getNextAccountNumber());
		jsonObject.put("name", bankAccount.getName());
		FinanceDate asOf = bankAccount.getAsOf();
		if (asOf != null) {
			jsonObject.put("asOf", asOf.toEpochDay());
		} else {
			jsonObject.put("asOf", new FinanceDate(bankAccount.getCreatedDate()).toEpochDay());
		}
		if (bankAccount.getParent() != null) {
			{
				Account accountsPayableAccount = bankAccount.getParent();
				JSONObject accountPayable = new JSONObject();
				JSONObject json = new JSONObject();
				accountPayable.put("com.vimukti.accounter.shared.common.Account", json);
				String parentLocalID = context.getLocalIdProvider().getOrCreate(accountsPayableAccount);
				json.put("@id", parentLocalID);
				json.put("@_lid", parentLocalID);
				jsonObject.put("subAccountOf", accountPayable);
			}
		}
		{
			Currency currency = bankAccount.getCurrency();
			JSONObject currencyJson = new JSONObject();
			JSONObject json = new JSONObject();
			currencyJson.put("com.vimukti.accounter.shared.common.AccounterCurrency", json);
			String orCreate = context.getLocalIdProvider().getOrCreate(currency);
			json.put("@_lid", orCreate);
			json.put("@_oid", context.get("Currency", currency.getID()));
			jsonObject.put("currency", currencyJson);
		}

		jsonObject.put("currencyFactor", BigDecimal.valueOf(bankAccount.getCurrencyFactor()));
		jsonObject.put("inactive", !bankAccount.getIsActive());
		jsonObject.put("description", bankAccount.getComment());
		jsonObject.put("openingBalance", BigDecimal.valueOf(bankAccount.getOpeningBalance()));
		jsonObject.put("payPalEmail", bankAccount.getPaypalEmail());
		jsonObject.put("cashFlowCategory",
				Utility.getCashFlowCategoryName(bankAccount.getCashFlowCategory()).toUpperCase());
		jsonObject.put("isIncrease", bankAccount.isIncrease());

		return external;
	}

	String getBankAccountTypeIdentity(int type) {
		switch (type) {
		case 0:
		case 4:
			return "CURRENT_ACCOUNT";
		case 1:
			return "CHECKING";
		case 2:
			return "SAVINGS";
		case 3:
			return "MONEY_MARKET";
		}
		return null;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", 2));
	}
}