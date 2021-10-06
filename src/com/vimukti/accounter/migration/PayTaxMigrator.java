package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PayTAX;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TransactionPayTAX;

public class PayTaxMigrator extends TransactionMigrator<PayTAX> {
	@Override
	public JSONObject migrate(PayTAX obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);

		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.tax.PayTAX", jsonObject);
		// filterbyTAXreturnenddate
		FinanceDate returnsDueOnOrBefore = obj.getReturnsDueOnOrBefore();
		if (returnsDueOnOrBefore != null) {
			jsonObject.put("filterbyTAXreturnenddate", returnsDueOnOrBefore.toEpochDay());
		}
		// Tax Agency
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.tax.TaxAgency", inter);
			inter.put("@_oid", context.get("Account", taxAgency.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(taxAgency));
			jsonObject.put("payee", outter);
		}

		List<TransactionPayTAX> transactionPayTAXs = obj.getTransactionPayTAX();
		JSONArray array = new JSONArray();
		JSONObject items = new JSONObject();
		items.put("com.vimukti.accounter.shared.tax.PayTAXItem", array);
		for (TransactionPayTAX transactionPayTAX : transactionPayTAXs) {
			JSONObject transactionJson = new JSONObject();
			String _clid = context.getLocalIdProvider().getOrCreate(transactionPayTAX);
			transactionJson.put("@id", _clid);
			transactionJson.put("@_lid", _clid);
			transactionJson.put("taxDue", BigDecimal.valueOf(transactionPayTAX.getTaxDue()));
			transactionJson.put("payment", BigDecimal.valueOf(transactionPayTAX.getAmountToPay()));
			transactionJson.put("filedDate", transactionPayTAX.getFiledDate().toEpochDay());
			// fileTax not found
			array.put(transactionJson);
		}
		jsonObject.put("payTaxItems", items);
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		try {
			Long chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObject.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		jsonObject.put("total", BigDecimal.valueOf(obj.getTotal()));
		jsonObject.put("memo", obj.getMemo());
		jsonObject.put("toBePrinted", true);
		Account payFrom = obj.getPayFrom();
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", payFrom.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(payFrom));
			jsonObject.put("paymentAccount", outter);
		}
		jsonObject.put("number", obj.getNumber());
		return external;
	}

}
