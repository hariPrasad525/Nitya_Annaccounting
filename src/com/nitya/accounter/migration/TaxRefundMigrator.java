package com.nitya.accounter.migration;

import java.math.BigDecimal;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.ReceiveVAT;
import com.nitya.accounter.core.ReceiveVATEntries;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.Transaction;

public class TaxRefundMigrator extends TransactionMigrator<ReceiveVAT> {

	@Override
	public JSONObject migrate(ReceiveVAT obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.tax.TAXRefund", jsonObject);
		Account depositIn = obj.getDepositIn();
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", depositIn.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(depositIn));
			jsonObject.put("depositIn", outter);
		}
		jsonObject.put("filterbyTAXreturnendDate", obj.getReturnsDueOnOrBefore().toEpochDay());
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.tax.TaxAgency", inter);
			inter.put("@_oid", context.get("TaxAgency", taxAgency.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(taxAgency));
			jsonObject.put("involvedPayee", outter);
		}
		JSONArray array = new JSONArray();
		JSONObject items = new JSONObject();
		items.put("com.nitya.accounter.shared.tax.TAXRefundItem", array);
		Set<ReceiveVATEntries> receiveVATEntriesList = obj.getReceiveVATEntriesList();
		for (ReceiveVATEntries vatEntries : receiveVATEntriesList) {
			JSONObject itemJson = new JSONObject();
			String _llid = context.getLocalIdProvider().getOrCreate(vatEntries);
			itemJson.put("@id", _llid);
			itemJson.put("@_lid", _llid);
			TAXAgency taxAgency2 = vatEntries.getTAXAgency();
			if (taxAgency2 != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.tax.TaxAgency", inter);
				inter.put("@_oid", context.get("TaxAgency", taxAgency2.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(taxAgency2));
				itemJson.put("taxAgency", outter);
			}
			Transaction transaction = vatEntries.getTransaction();
			{
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.tax.FileTax", inter);
				inter.put("@_oid", context.get("FileTax", transaction.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(transaction));
				itemJson.put("fileTax", outter);
			}
			itemJson.put("taxDue", BigDecimal.valueOf(vatEntries.getBalance()));
			itemJson.put("amountToReceive", BigDecimal.valueOf(vatEntries.getAmount()));
			array.put(itemJson);
		}
		jsonObject.put("taxRefundItems", items);
		return external;
	}
}
