package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXReturn;
import com.vimukti.accounter.core.TAXReturnEntry;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.migration.MigratorContext.Tuple;

public class FileTaxMigrator extends TransactionMigrator<TAXReturn> {

	@Override
	public JSONObject migrate(TAXReturn obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.tax.FileTax", jsonObject);
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.tax.TaxAgency", inter);
			inter.put("@_oid", context.get("TaxAgency", taxAgency.getID()));
			inter.put("@_lid",
					context.getLocalIdProvider().getOrCreate(taxAgency));
			jsonObject.put("taxAgency", outter);
		}
		jsonObject.put("fromDate", obj.getPeriodStartDate().toEpochDay());
		jsonObject.put("to", obj.getPeriodEndDate().toEpochDay());
		List<TAXReturnEntry> taxReturnEntries = obj.getTaxReturnEntries();
		JSONArray fileTaxItems = new JSONArray();
		JSONObject items = new JSONObject();
		items.put("com.vimukti.accounter.shared.tax.FileTaxItem", fileTaxItems);
		for (TAXReturnEntry taxReturnEntry : taxReturnEntries) {
			Transaction transaction = taxReturnEntry.getTransaction();
			if (transaction == null) {
				continue;
			}
			JSONObject transactionJson = new JSONObject();
			String _llid = context.getLocalIdProvider().getOrCreate(
					taxReturnEntry);
			transactionJson.put("@id", _lid);
			transactionJson.put("@_lid", _llid);
			Long taxRateCalculationID = getTaxRateCalculationID(context,
					taxReturnEntry);
			if (taxRateCalculationID == null) {
				continue;
			}
			transactionJson.put("taxRateCalculation", taxRateCalculationID);
			fileTaxItems.put(transactionJson);
		}
		if (fileTaxItems.length() == 0) {
			return null;
		}
		jsonObject.put("taxItems", items);
		return external;
	}

	private Long getTaxRateCalculationID(MigratorContext context,
			TAXReturnEntry taxReturnEntry) {
		long id = taxReturnEntry.getTransaction().getID();
		List<Tuple> list = context.get(taxReturnEntry.getTransaction().getID());
		if (list == null) {
			return null;
		}
		for (Tuple tuple : list) {
			Long taxItem = context.get("Tax", taxReturnEntry.getTaxItem()
					.getID());
			if (tuple.getTaxItemID().equals(taxItem)) {
				break;
			}
		}
		return id;
	}
}
