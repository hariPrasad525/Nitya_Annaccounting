package com.nitya.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.exception.AccounterException;

public class ItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		isActive = readBoolean(req, "active");

		int type = readInt(req, "item_type", 0);

		// Items(0),InventoryAssemblyItems(1),InventoryItem(2),VensorItems(3),CustomerItems(4)

		ClientConvertUtil convertUtil = new ClientConvertUtil();
		List<ClientItem> resultList = new ArrayList<ClientItem>();
		Set<Item> items = getCompany().getItems();
		for (Item item : items) {
			if (isActive != null && isActive != item.isActive()) {
				continue;
			}
			boolean canAdd = false;
			switch (type) {
			case 0:// All
				canAdd = true;
				break;
			case 1:// InventoryAssemblyItems
				checkPermission(Features.INVENTORY);
				if (item.getType() == Item.TYPE_INVENTORY_ASSEMBLY) {
					canAdd = true;
				}
				break;
			case 2:// InventoryItem
				checkPermission(Features.INVENTORY);
				if (item.getType() == Item.TYPE_INVENTORY_PART) {
					canAdd = true;
				}
				break;
			case 3:// VensorItems
				if (item.isIBuyThisItem()) {
					canAdd = true;
				}
				break;

			case 4:// CustomerItems
				if (item.isISellThisItem()) {
					canAdd = true;
				}
				break;
			default:
				throw new AccounterException("Wrong itemType");
			}
			if (canAdd) {
				resultList.add(convertUtil.toClientObject(item,
						ClientItem.class));
			}
		}
		sendResult(resultList);
	}
}
