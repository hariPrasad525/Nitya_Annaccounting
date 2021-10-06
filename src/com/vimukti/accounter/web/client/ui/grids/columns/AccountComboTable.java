package com.vimukti.accounter.web.client.ui.grids.columns;

import java.util.ArrayList;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.Utility;

public class AccountComboTable extends CellTable<ClientAccount> {

	private int cols = 3;

	public AccountComboTable(ArrayList<ClientAccount> ArrayList) {
		ListDataProvider<ClientAccount> list = new VListDataProvider<ClientAccount>(
				ArrayList);
		list.addDataDisplay(this);

		initColumns();
	}

	private void initColumns() {
		for (int i = 0; i < cols; i++) {
			this.addColumn(createColumn(i));
		}
	}

	private Column<ClientAccount, String> createColumn(final int col) {
		Column<ClientAccount, String> column = new Column<ClientAccount, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientAccount object) {
				return getColumnData(object, 0, col);
			}

		};
		return column;
	}

	private String getColumnData(ClientAccount object, int row, int col) {

		switch (col) {
		case 0:
			// if (ClientCompanyPreferences.get().getUseAccountNumbers() ==
			// true) {
			return object.getNumber();
			// } else {
			// return null;
			// }
		case 1:
			return object.getName();
		case 2:
			return Utility.getAccountTypeString(object.getType());
		default:
			break;
		}
		return null;
	}
}
