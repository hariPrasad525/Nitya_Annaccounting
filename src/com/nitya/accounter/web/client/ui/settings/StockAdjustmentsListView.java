package com.nitya.accounter.web.client.ui.settings;

import java.util.ArrayList;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.InventoryActions;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;

public class StockAdjustmentsListView extends
		TransactionsListView<StockAdjustmentList> implements IPrintableView {

	private int start;

	@Override
	public void updateInGrid(StockAdjustmentList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new StockAdjustmentsListGrid(false);
		grid.init();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		onPageChange(0, getPageSize());
	}

	@Override
	protected void onPageChange(int start, int length) {
		reloadRecords();
	}

	public void reloadRecords() {
		Accounter.createHomeService().getStockAdjustments(
				getStartDate().getDate(), getEndDate().getDate(),
				new AccounterAsyncCallback<ArrayList<StockAdjustmentList>>() {

					@Override
					public void onException(AccounterException exception) {
						grid.addEmptyMessage(messages.noRecordsToShow());
					}

					@Override
					public void onResultSuccess(
							ArrayList<StockAdjustmentList> result) {
						grid.removeAllRecords();
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.addRecords(result);
						} else {
							grid.addEmptyMessage(messages.noRecordsToShow());
						}
						grid.sort(10, false);
					}
				});
	}

	@Override
	protected String getListViewHeading() {
		return messages.stockAdjustments();
	}

	@Override
	protected Action getAddNewAction() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return InventoryActions.stockAdjustment();
		else
			return null;
	}

	@Override
	protected void filterList(String selectedValue) {
		onPageChange(start, getPageSize());
	}

	@Override
	protected String getAddNewLabelString() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return messages.addaNew(messages.stockAdjustment());
		else
			return "";
	}

	@Override
	protected String getViewTitle() {
		return messages.stockAdjustments();
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getStockAdjustmentsExportCsv(
				getStartDate().getDate(), getEndDate().getDate(),
				getExportCSVCallback(messages.stockAdjustments()));
	}
}
