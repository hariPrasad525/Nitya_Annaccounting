package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.ui.core.AbstractView;
import com.nitya.accounter.web.client.ui.grids.CustomerTransactionsHistoryGrid;

public class CustomerTransactionHistoryListView extends
		AbstractView<TransactionHistory> {
	private CustomerTransactionsHistoryGrid vendHistoryGrid;
	private ClientCustomer selectedCustomer;

	public CustomerTransactionHistoryListView() {
		this.getElement().setId("CustomerTransactionHistoryListView");
	}

	@Override
	public void init() {
		vendHistoryGrid = new CustomerTransactionsHistoryGrid() {
			@Override
			public void initListData() {

			}
		};
		vendHistoryGrid.init();
		vendHistoryGrid.setSelectedCustomer(selectedCustomer);
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

	public void setSelectedCustomer(ClientCustomer selected) {
		selectedCustomer = selected;
	}

}
