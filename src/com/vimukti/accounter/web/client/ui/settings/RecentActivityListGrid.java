package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class RecentActivityListGrid extends ListGrid<ClientUser> {

	private UsersView usersView;

	public RecentActivityListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 1:
			return ListGrid.COLUMN_TYPE_DATE;
		default:
			return ListGrid.COLUMN_TYPE_TEXT;
		}
	}

	public void setRecentActivityGridView(UsersView view) {
		this.usersView = view;
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(),
				messages.loginDate() };
	}

	@Override
	protected Object getColumnValue(ClientUser obj, int index) {
		switch (index) {
		case 0:
			return obj.getFullName();
		case 1:
			return 0;// TODO Nagaraju.
		default:
			return "";
		}
	}

	// its not using any where
	@Override
	protected String[] getSelectValues(ClientUser obj, int index) {
		return null;
	}

	// its not using any where
	@Override
	protected boolean isEditable(ClientUser obj, int row, int index) {
		return false;
	}

	// its not using any where
	@Override
	protected void onClick(ClientUser obj, int row, int index) {

	}

	// its not using any where
	@Override
	public void onDoubleClick(ClientUser obj) {

	}

	// its not using any where
	@Override
	protected void onValueChange(ClientUser obj, int index, Object value) {

	}

	// its not using any where
	@Override
	protected int sort(ClientUser obj1, ClientUser obj2, int index) {
		return 0;
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "name";
		case 1:
			return "ligin-date";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "name-val";
		case 1:
			return "ligin-date-val";
		default:
			return "";
		}
	}

}
