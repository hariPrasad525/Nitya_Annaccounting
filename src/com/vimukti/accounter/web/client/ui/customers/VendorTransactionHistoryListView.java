package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.core.AbstractView;
import com.vimukti.accounter.web.client.ui.grids.VendorTransactionsHistoryGrid;

public class VendorTransactionHistoryListView extends
		AbstractView<TransactionHistory> {
	private VendorTransactionsHistoryGrid vendHistoryGrid;
	private ClientVendor selectedVendor;

	public VendorTransactionHistoryListView() {
		this.getElement().setId("VendorTransactionHistoryListView");
	}

	@Override
	public void init() {
		vendHistoryGrid = new VendorTransactionsHistoryGrid() {
			@Override
			public void initListData() {

			}
		};
		vendHistoryGrid.init();
		vendHistoryGrid.setSelectedVendor(selectedVendor);
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

	public void setSelectedVendor(ClientVendor selected) {
		selectedVendor = selected;
	}

}
