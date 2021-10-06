/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.grids.PendingItemsListGrid;

/**
 * @author Murali.A
 * 
 */
public class PendingItemsListView extends BaseListView<ClientFixedAsset>
		implements IPrintableView {

	public PendingItemsListView() {
		this.isViewSelectRequired = false;

		this.getElement().setId("PendingItemsListView");
	}

	@Override
	protected Action getAddNewAction() {
		return new NewFixedAssetAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addNewAsset();
	}

	@Override
	protected String getListViewHeading() {
		return messages.pendingItemsList();
	}

	@Override
	protected void initGrid() {
		grid = new PendingItemsListGrid(false);
		grid.init();
		getPendingFixedAssetsList();

		if (viewSelect != null) {
			viewSelect.setEnabled(false);
		}
	}

	private void getPendingFixedAssetsList() {
		Accounter.createHomeService().getFixedAssetList(
				ClientFixedAsset.STATUS_PENDING,
				new AsyncCallback<ArrayList<ClientFixedAsset>>() {

					@Override
					public void onSuccess(ArrayList<ClientFixedAsset> list) {
						if (list.isEmpty()) {
							grid.removeAllRecords();
							grid.addEmptyMessage(messages.noRecordsToShow());
							return;
						}
						grid.setRecords(list);
						grid.sort(10, false);
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public void initListCallback() {
		// FinanceApplication.createGETService().getObjects(
		// AccounterCoreType.FIXEDASSET, this);

	}

	@Override
	public void updateInGrid(ClientFixedAsset objectTobeModified) {
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// Nothing to do

	}

	@Override
	public void print() {
		// NOTHING to do

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		start = (Integer) viewDate.get("start");
	}

	@Override
	protected String getViewTitle() {
		return messages.pendingItemsList();
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
		Accounter.createExportCSVService().getFixedAssetListExportCsv(
				ClientFixedAsset.STATUS_PENDING,
				getExportCSVCallback(messages.pendingItemsList()));

	}
}
