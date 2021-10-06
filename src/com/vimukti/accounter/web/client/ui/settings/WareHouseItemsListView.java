package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.HashMap;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class WareHouseItemsListView extends BaseListView<ClientItemStatus> {

	private final long wareHouse;
	private int start;

	public WareHouseItemsListView(long wareHouse) {
		this.wareHouse = wareHouse;
		this.getElement().setId("WareHouseItemsListView");
	}

	@Override
	public void updateInGrid(ClientItemStatus objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new WareHouseItemsListGrid(false);
		grid.init();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getItemStatuses(wareHouse,
				new AccounterAsyncCallback<ArrayList<ClientItemStatus>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientItemStatus> result) {
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.setRecords(result);
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
	protected String getListViewHeading() {
		return messages.itemsDash(
				getCompany().getWarehouse(wareHouse).getName());
	}

	@Override
	protected Action getAddNewAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return "";
	}

	@Override
	protected String getViewTitle() {
		return messages.wareHouseItems();
	}

}
