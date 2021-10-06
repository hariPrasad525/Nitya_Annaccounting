package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;

public class InventoryTransactionHistoryListAction extends Action {

	private InventoryTransactionHistoryListView view;
	private ClientItem selected;

	public InventoryTransactionHistoryListAction(ClientItem selectedItem) {
		super();
		selected = selectedItem;
	}

	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new InventoryTransactionHistoryListView();
				view.setSelectedItem(selected);
				MainFinanceWindow.getViewManager()
						.showView(view, data, isDependent,
								InventoryTransactionHistoryListAction.this);

			}

		});
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
	public String getHistoryToken() {
		return "inventoryTransactionHistoryListAction";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}

}
