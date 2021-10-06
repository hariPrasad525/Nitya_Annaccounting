package com.nitya.accounter.migration;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.TAXAdjustment;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXItem;

public class TAXAdjustmentMigrator extends TransactionMigrator<TAXAdjustment> {
	@Override
	public JSONObject migrate(TAXAdjustment obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("@class", "com.nitya.accounter.shared.tax.TAXAdjustment");
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			jsonObject.put("taxAgency", context.get("TaxAgency", taxAgency.getID()));
			jsonObject.put("payee", context.get("TaxAgency", taxAgency.getID()));
		}
		TAXItem taxItem = obj.getTaxItem();
		if (taxItem != null) {
			jsonObject.put("taxItem", context.get("TAXITem", taxItem.getID()));
		}
		jsonObject.put("type", obj.isSales() ? "Sales_Type" : "Purchase_Type");
		jsonObject.put("adjustmentAccount", context.get("Account", obj.getAdjustmentAccount().getID()));
		jsonObject.put("adjustmentType", obj.getIncreaseVATLine() ? "Increase_TAX_line" : "Decrease_TAX_line");
		jsonObject.put("amount", BigDecimal.valueOf(obj.getTotal()));
		return jsonObject;
	}

}
