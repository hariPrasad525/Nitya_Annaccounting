package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.HashMap;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.WarehouseActions;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;

public class WarehouseTransferListView extends
		BaseListView<ClientStockTransfer> implements IPrintableView {

	private int start;

	public WarehouseTransferListView() {
		this.getElement().setId("WarehouseTransferListView");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void updateInGrid(ClientStockTransfer objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		reloadRecords();
	}

	public void reloadRecords() {
		Accounter.createHomeService().getWarehouseTransfersList(
				new AccounterAsyncCallback<ArrayList<ClientStockTransfer>>() {

					@Override
					public void onException(AccounterException exception) {
						grid.addEmptyMessage(messages.noRecordsToShow());
					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientStockTransfer> result) {
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
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("isActive", isActiveAccounts);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		// isActiveAccounts = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		// if (isActiveAccounts) {
		// viewSelect.setComboItem(messages.active());
		// } else {
		// viewSelect.setComboItem(messages.inActive());
		// }

	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new WarehouseTransferListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.warehouseTransferList();
	}

	@Override
	protected Action getAddNewAction() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return WarehouseActions.warehouseTransfer();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return messages.addNewWarehouseTransfer();
		else
			return "";
	}

	@Override
	protected String getViewTitle() {
		return messages.warehouseTransferList();
	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

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
		Accounter.createExportCSVService().getWarehouseTransfersListExportCsv(
				getExportCSVCallback(messages.warehouseTransferList()));
	}
}
