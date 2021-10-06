package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.FocusWidget;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientDepreciationDummyEntry;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.FixedAssetAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;

public class DepreciationGrid extends ListGrid<ClientDepreciationDummyEntry> {
	private FixedAssetAccountCombo accountsCombo;

	// private
	// DepreciationGrid.RecordDoubleClickHandler<ClientDepreciationDummyEntry>
	// doubleClickHandler;

	public DepreciationGrid() {
		super(false);
	}

	@Override
	protected int getColumnType(int col) {
		if (col == 2) {
			return ListGrid.COLUMN_TYPE_SELECT;
		}
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	public void init() {
		super.init();
		createControls();

	}

	private void createControls() {
		accountsCombo = new FixedAssetAccountCombo(messages.Accounts());
//		accountsCombo.setGrid(this);
		accountsCombo.setRequired(true);
		accountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedObject.setAssetAccount(selectItem.getID());
						setText(currentRow, currentCol, selectItem.getName());
					}
				});

		/*
		 * Filtering the Accumulated depreciation accounts which are already
		 * used as Asset account in the existing Fixed Assets.
		 */

		List<ClientAccount> accumulatedAccounts = accountsCombo.getAccounts();
		List<ClientFixedAsset> fixedAssets = Accounter.getCompany()
				.getFixedAssets();
		for (ClientFixedAsset asset : fixedAssets) {
			for (ClientAccount accumulatedAccount : accumulatedAccounts) {
				if (asset.getAssetAccount() != 0
						&& asset.getAssetAccount() != accumulatedAccount
								.getID())
					accountsCombo.setValue(accumulatedAccount);
			}
		}
		// addRecordDoubleClickHandler(new
		// RecordDoubleClickHandler<ClientDepreciationDummyEntry>() {
		//
		// @Override
		// public void OnCellDoubleClick(ClientDepreciationDummyEntry core,
		// int column) {
		// if (column == 2)
		// accountsCombo.setComboItem(FinanceApplication.getCompany()
		// .getAccount(core.getAssetAccount()));
		// }
		// });

	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 400;
		case 2:
			return 400;
		default:
			return -1;
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.Account(),
				messages.amounttobeDepreciated(),
				messages.accumulatedDepreciationAccount() };
	}

	@Override
	protected Object getColumnValue(ClientDepreciationDummyEntry item, int index) {
		switch (index) {
		case 0:
			return item.getFixedAssetName();
		case 1:
			return DataUtils.amountAsStringWithCurrency(item
					.getAmountToBeDepreciated(), getCompany()
					.getPrimaryCurrency());
		case 2:
			return item.getAssetAccount() != 0 ? Accounter.getCompany()
					.getAccount(item.getAssetAccount()).getName() : "";
		default:
			return "";
		}
	}

	@Override
	protected String[] getSelectValues(ClientDepreciationDummyEntry obj,
			int index) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientDepreciationDummyEntry obj, int row,
			int index) {
		switch (index) {
		case 2:
			return true;
		}
		return false;
	}

	@Override
	protected void onClick(ClientDepreciationDummyEntry obj, int row, int index) {
		// NOTHING TO DO
	}

	@Override
	public void onDoubleClick(ClientDepreciationDummyEntry obj) {
		// NOTHING TO DO
	}

	@Override
	protected void onValueChange(ClientDepreciationDummyEntry obj, int index,
			Object value) {
		// NOTHING TO DO
	}

	@Override
	protected void addOrEditSelectBox(ClientDepreciationDummyEntry obj,
			Object value) {
		CustomCombo box = getCustomCombo(obj, currentCol);
		if (box != null) {
			FocusWidget widget = (FocusWidget) box.getMainWidget();
			this.setWidget(currentRow, currentCol, widget);
		} else
			super.addOrEditSelectBox(obj, value);
	}

	private CustomCombo getCustomCombo(ClientDepreciationDummyEntry obj,
			int currentCol) {
		if (currentCol == 2)
			return accountsCombo;
		return null;
	}

	@Override
	protected int sort(ClientDepreciationDummyEntry obj1,
			ClientDepreciationDummyEntry obj2, int index) {
		return 0;
	}

	@SuppressWarnings("hiding")
	public interface RecordDoubleClickHandler<ClientDepreciationDummyEntry> {
		public void OnCellDoubleClick(ClientDepreciationDummyEntry core,
				int column);
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "Account";
		case 1:
			return "amounttobeDepreciated";
		case 2:
			return "accumulatedDepreciationAccount";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "Account-value";
		case 1:
			return "amounttobeDepreciated-value";
		case 2:
			return "accumulatedDepreciationAccount-value";
		default:
			return "";
		}
	}

	// public void addRecordDoubleClickHandler(
	// RecordDoubleClickHandler<ClientDepreciationDummyEntry>
	// doubleClickHandler) {
	// this.doubleClickHandler = doubleClickHandler;
	// }

}
