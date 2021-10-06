package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.StockAdjustment;
import com.vimukti.accounter.core.TransactionItem;

public class StockAdjustmentMigrator extends TransactionMigrator<StockAdjustment> {
	@Override
	public JSONObject migrate(StockAdjustment obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("@class", "com.vimukti.accounter.shared.inventory.StockAdjustment");
		jsonObject.put("warehouse", context.get("Warehouse", obj.getWareHouse().getID()));
		Account adjustmentAccount = obj.getAdjustmentAccount();
		if (adjustmentAccount != null) {
			jsonObject.put("adjustmentAccount", context.get("Account", adjustmentAccount.getID()));
		}

		{
			List<TransactionItem> transactionItems = obj.getTransactionItems();
			JSONArray array = new JSONArray();
			for (TransactionItem item : transactionItems) {
				JSONObject jsonobjItem = new JSONObject();
				jsonobjItem.put("@class", "com.vimukti.accounter.shared.inventory.StockAdjustmentItem");
				Item inventoryItem = item.getItem();
				if (inventoryItem != null) {
					jsonobjItem.put("item", context.get("InventoryItem", inventoryItem.getID()));
				}
				jsonobjItem.put("comment", item.getDescription());
				Quantity quantity = item.getQuantity();
				jsonobjItem.put("unit", context.get("Unit", quantity.getUnit().getID()));
				jsonobjItem.put("adjustQty", BigDecimal.valueOf(quantity.getValue()));
				jsonobjItem.put("adjustRate", BigDecimal.valueOf(item.getEffectiveAmount()));
				array.put(jsonobjItem);
			}
			jsonObject.put("stockAdjustmentItems", array);
		}
		return jsonObject;
	}
}
