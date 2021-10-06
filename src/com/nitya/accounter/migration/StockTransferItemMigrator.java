package com.nitya.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Quantity;
import com.nitya.accounter.core.StockTransferItem;

public class StockTransferItemMigrator {

	public static JSONObject migrate(StockTransferItem stocktransferitem)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("item", stocktransferitem.getItem());
		{
			Quantity quantity = stocktransferitem.getQuantity();
			JSONObject objectQty = new JSONObject();
			objectQty.put("value", quantity.getValue());
			objectQty.put("unitFactor", quantity.getUnit().getFactor());
			objectQty.put("unit", quantity.getUnit());
			jsonObject.put("adjustQty", objectQty);
		}
		return jsonObject;
	}
}