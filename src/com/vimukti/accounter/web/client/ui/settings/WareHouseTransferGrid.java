package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WareHouseTransferGrid extends
		AbstractTransactionGrid<ClientWarehouse> {

	private OtherAccountsCombo accountsCombo = new OtherAccountsCombo("");
	private WareHouseTransferView view;

	public WareHouseTransferGrid(boolean b) {
		super(b);
	}

	@Override
	public void init() {
		super.init();
		initTransactionData();
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientWarehouse obj, int colIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		// TODO Auto-generated method stub
		return null;
	}

	public void initTransactionData() {
		initAccountsCombo();
	}

	private void initAccountsCombo() {
		List<ClientAccount> accounts = Accounter.getCompany()
				.getActiveAccounts();
		if (accounts != null) {
			accountsCombo.initCombo(accounts);
		}

	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 0:
			return ListGrid.COLUMN_TYPE_IMAGE;
		case 2:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		case 4:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return ListGrid.COLUMN_TYPE_TEXT;
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", messages.name(),
				messages.quantity(),
				messages.comment(), messages.delete() };
	}

	public void setView(WareHouseTransferView view) {
		this.view = view;
	}

	@Override
	protected Object getColumnValue(ClientWarehouse obj, int index) {
		switch (index) {
		case 1:
			obj.getName();
		case 3:
			obj.getItemStatuses();
		default:
			break;
		}
		return obj;
	}

	@Override
	protected boolean isEditable(ClientWarehouse obj, int row, int index) {

		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	public void setTaxCode(long taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "name";
		case 1:
			return "quantity";
		case 2:
			return "comment";
		case 3:
			return "delete";

		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "name-value";
		case 1:
			return "quantity-value";
		case 2:
			return "comment-value";
		case 3:
			return "delete-value";
		default:
			return "";
		}
	}

}