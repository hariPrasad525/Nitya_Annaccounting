package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class InventoryItemsAction extends Action<ClientItem> {

	private int type;

	public InventoryItemsAction() {
		super();
		this.type = type;
		this.catagory = messages.inventory();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {

		return null;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				InventoryItemsListView listview = new InventoryItemsListView();
				MainFinanceWindow.getViewManager().showView(listview, data,
						isDependent, InventoryItemsAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		// }
		// });
	}

	@Override
	public String getHistoryToken() {
		if (type == ClientItem.TYPE_INVENTORY_PART) {
			return HistoryTokens.INVENTORYITEMS;
		} else {
			return HistoryTokens.INVENTORY_ASSEMBLY_ITEMS;
		}
	}

	@Override
	public String getHelpToken() {

		if (type == ClientItem.TYPE_INVENTORY_PART) {
			return "inventoryItem";
		} else {
			return "assemblyItem";
		}

	}

	@Override
	public String getText() {

		if (type == ClientItem.TYPE_INVENTORY_PART) {
			return messages.inventoryItems();
		} else {
			return messages.inventoryAssembly() + " " + messages.items();
		}

	}

}
