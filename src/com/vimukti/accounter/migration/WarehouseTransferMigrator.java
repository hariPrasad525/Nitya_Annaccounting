package com.vimukti.accounter.migration;

import java.math.BigDecimal;
import java.util.Set;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.StockTransfer;
import com.vimukti.accounter.core.StockTransferItem;

public class WarehouseTransferMigrator implements IMigrator<StockTransfer> {

	@Override
	public JSONObject migrate(StockTransfer obj, MigratorContext context) throws JSONException {
		JSONObject stockTransfer = new JSONObject();
		stockTransfer.put("@class", "com.vimukti.accounter.shared.inventory.WarehouseTransfer");
		CommonFieldsMigrator.migrateCommonFields(obj, stockTransfer, context);
		stockTransfer.put("fromWareHouse", context.get("Warehouse", obj.getFromWarehouse().getID()));
		stockTransfer.put("to", context.get("Warehouse", obj.getToWarehouse().getID()));
		stockTransfer.put("comment", obj.getMemo());

		JSONArray itemsArray = new JSONArray();
		Set<StockTransferItem> stockTransferItems = obj.getStockTransferItems();
		for (StockTransferItem stockTransferItem : stockTransferItems) {
			JSONObject stockItem = new JSONObject();
			stockItem.put("@class", "com.vimukti.accounter.shared.inventory.WarehouseTransferItem");
			Quantity quantity = stockTransferItem.getQuantity();
			stockItem.put("item", context.get("InventoryItem", stockTransferItem.getItem().getID()));
			stockItem.put("unit", context.get("Unit", quantity.getUnit().getID()));
			stockItem.put("transferQuantity", BigDecimal.valueOf(quantity.getValue()));
			itemsArray.put(stockItem);
		}
		stockTransfer.put("warehouseTransferItems", itemsArray);
		return stockTransfer;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
