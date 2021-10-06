/**
 * 
 */
package com.nitya.accounter.web.client.ui.fixedassets;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFixedAsset;
import com.nitya.accounter.web.client.core.ClientFixedAssetHistory;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.grids.HistoryListGrid;

/**
 * @author Murali.A
 * 
 */
public class HistoryListView extends BaseListView<ClientFixedAssetHistory> {

	private final ClientFixedAsset asset;
	private int start;

	public HistoryListView(ClientFixedAsset asset) {
		this.getElement().setId("HistoryListView");
		this.asset = asset;
		this.isViewSelectRequired = false;
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.core.BaseListView#getAddNewAction()
	 */
	@Override
	protected Action getAddNewAction() {
		return null;
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 * ()
	 */
	@Override
	protected String getAddNewLabelString() {
		return "";
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 * ()
	 */
	@Override
	protected String getListViewHeading() {
		return messages.history();
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.core.BaseListView#initGrid()
	 */

	@Override
	protected void initGrid() {
		grid = new HistoryListGrid(false);
		grid.init();
		grid.setRecords(asset.getFixedAssetsHistory());

	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.core.BaseListView#initListCallback()
	 */
	@Override
	public void initListCallback() {

	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.core.IAccounterList#updateInGrid(
	 * java.lang.Object)
	 */
	@Override
	public void updateInGrid(ClientFixedAssetHistory objectTobeModified) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {

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

		// if (isActiveAccounts) {
		// viewSelect.setComboItem(messages.active());
		// } else {
		// viewSelect.setComboItem(messages.inActive());
		// }

	}

	@Override
	public void onSuccess(PaginationList<ClientFixedAssetHistory> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.history();
	}

}
