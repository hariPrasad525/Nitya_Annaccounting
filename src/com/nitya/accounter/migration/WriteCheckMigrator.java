package com.nitya.accounter.migration;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.WriteCheck;

public class WriteCheckMigrator extends TransactionMigrator<WriteCheck> {
	@Override
	public JSONObject migrate(WriteCheck obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.common.WriteCheck", jsonObject);
		int payToType = obj.getPayToType();
		if (payToType == WriteCheck.TYPE_VENDOR) {
			Vendor vendor = obj.getVendor();
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.vendor.Vendor", inter);
			inter.put("@_oid", context.get("Vendor", vendor.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(vendor));
			jsonObject.put("payee", outter);
		}
		Account bankAccount = obj.getBankAccount();
		if (bankAccount != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.bank.BankAccount", inter);
			inter.put("@_oid", context.get("Account", bankAccount.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(bankAccount));
			jsonObject.put("paymentAccount", outter);
		}
		String inFavourOf = obj.getInFavourOf();
		if (inFavourOf == null || inFavourOf.isEmpty()) {
			inFavourOf = getPayeeName(obj);
		}
		jsonObject.put("inFavourOf", inFavourOf);
		jsonObject.put("total", BigDecimal.valueOf(obj.getAmount()));
		jsonObject.put("date", obj.getDate().toEpochDay());
		jsonObject.put("toBePrinted", obj.isToBePrinted());
		try {
			jsonObject.put("chequeNumber", Long.parseLong(obj.getCheckNumber()));
		} catch (NumberFormatException nfe) {
		}
		jsonObject.put("notes", obj.getMemo());
		return external;
	}

	/**
	 * Get Payee Name by Write Check PaytoType
	 * 
	 * @param obj
	 * @return
	 */
	private String getPayeeName(WriteCheck obj) {
		Payee payee = null;
		int payToType = obj.getPayToType();
		switch (payToType) {
		case WriteCheck.TYPE_CUSTOMER:
			payee = obj.getCustomer();
			break;
		case WriteCheck.TYPE_VENDOR:
			payee = obj.getVendor();
			break;
		case WriteCheck.TYPE_TAX_AGENCY:
			payee = obj.getTaxAgency();
			break;
		}
		return payee.getName();
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("payToType", WriteCheck.TYPE_VENDOR));
	}
}
