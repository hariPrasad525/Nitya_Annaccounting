package com.vimukti.accounter.migration;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.TransferFund;

public class TransferFundMigrator extends TransactionMigrator<TransferFund> {
	@Override
	public JSONObject migrate(TransferFund obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.bank.TransferFund", jsonObject);
		jsonObject.put("amount", BigDecimal.valueOf(obj.getTotal()));
		// DepositFrom
		Account depositFrom = obj.getDepositFrom();
		if (depositFrom != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", depositFrom.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(depositFrom));
			jsonObject.put("transferFrom", outter);
		}
		// DepositTo
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", depositIn.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(depositIn));
			jsonObject.put("transferTo", outter);
		}
		return external;
	}
}
