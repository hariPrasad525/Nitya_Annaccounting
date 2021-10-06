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
import com.vimukti.accounter.web.client.ui.grids.RegisteredItemsListGrid;

/**
 * @author Murali.A
 * 
 */
public class RegisteredItemsListView extends BaseListView<ClientFixedAsset>
		implements IPrintableView {

	public RegisteredItemsListView() {
		this.isViewSelectRequired = false;
		this.getElement().setId("RegisteredItemsListView");
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewAction()
	 */
	@Override
	protected Action getAddNewAction() {
		return new NewFixedAssetAction();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 */
	@Override
	protected String getAddNewLabelString() {
		return messages.addNewAsset();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 */
	@Override
	protected String getListViewHeading() {
		return messages.registeredItemsList();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.BaseListView#initGrid()
	 */

	@Override
	protected void initGrid() {
		grid = new RegisteredItemsListGrid(false);
		grid.init();
		getRegistredFixedAssetsList();
		// grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_REGISTERED,
		// getCompany().getFixedAssets()));
		if (viewSelect != null) {
			viewSelect.setEnabled(false);
		}
		;
	}

	private void getRegistredFixedAssetsList() {

		Accounter.createHomeService().getFixedAssetList(
				ClientFixedAsset.STATUS_REGISTERED,
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

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#initListCallback()
	 */
	@Override
	public void initListCallback() {

	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.IAccounterList#updateInGrid(
	 * java.lang.Object)
	 */
	@Override
	public void updateInGrid(ClientFixedAsset objectTobeModified) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.registeredItemsList();
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
				ClientFixedAsset.STATUS_REGISTERED,
				getExportCSVCallback(messages.registeredItemsList()));
	}
}
