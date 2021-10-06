package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.ui.settings.ItemTransactionsHistoryGrid;

public class InventoryTransactionHistoryListView extends
		AbstractView<TransactionHistory> {
	private ClientItem selectedItem;
	private ItemTransactionsHistoryGrid vendHistoryGrid;

	public InventoryTransactionHistoryListView() {
		this.getElement().setId("InventoryTransactionHistoryListView");
	}

	@Override
	public void init() {
		vendHistoryGrid = new ItemTransactionsHistoryGrid() {

			@Override
			public void initListData() {

			}
		};
		vendHistoryGrid.init();
		vendHistoryGrid.setSelectedItem(selectedItem);
		vendHistoryGrid.addEmptyMessage(messages.pleaseSelectAnyPayee(Global
				.get().Vendor()));
		vendHistoryGrid.addRangeChangeHandler2(new Handler() {

			@Override
			public void onRangeChange(RangeChangeEvent event) {

			}

		});
		this.add(vendHistoryGrid);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	public void setSelectedItem(ClientItem selected) {
		selectedItem = selected;
	}

}
