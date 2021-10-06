/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.fixedassets.HistoryListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NoteDialog;

/**
 * @author Murali.A
 * 
 */
public class PendingItemsListGrid extends BaseListGrid<ClientFixedAsset> {

	private NoteDialog noteDialog;

	/**
	 * @param isMultiSelectionEnable
	 */
	public PendingItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("PendingItemsListGrid");
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.BaseListGrid#setColTypes()
	 */
	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_IMAGE };
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { messages.item(), messages.assetNumber(),
				messages.Account(), messages.purchaseDate(),
				messages.purchasePrice(), messages.showHistory(), "" };
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#getColumnValue(java
	 * .lang.Object, int)
	 */
	@Override
	protected Object getColumnValue(ClientFixedAsset asset, int index) {
		switch (index) {
		case 0:
			return asset.getName() != null ? asset.getName() : "";
		case 1:
			return asset.getAssetNumber();
		case 2:
			return getCompany().getAccount(asset.getAssetAccount()) != null ? Accounter
					.getCompany().getAccount(asset.getAssetAccount()).getName()
					: "";
		case 3:
			return asset.getPurchaseDate() != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(asset
							.getPurchaseDate())) : "";
		case 4:
			return DataUtils
					.amountAsStringWithCurrency(asset.getPurchasePrice(),
							getCompany().getPrimaryCurrency());
		case 5:
			return messages.showHistory();
		case 6:
			return Accounter.getFinanceImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0 || index == 1) {
			return 105;
		} else if (index == 2) {
			return 200;
		} else if (index == 3 || index == 5) {
			return 105;
		} else if (index == 4) {
			return 105;
		} else if (index == 6) {
			return 20;
		}
		return super.getCellWidth(index);
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#onDoubleClick(java
	 * .lang.Object)
	 */
	@Override
	public void onDoubleClick(ClientFixedAsset obj) {

	}

	@Override
	protected void onDoubleClick(ClientFixedAsset obj, int row, int index) {
		if (!(Utility.isUserHavePermissions(IAccounterCore.FIXED_ASSET))) {
			return;
		}
		switch (index) {
		case 5:
			openHistoryView(obj);
			break;
		default:
			new NewFixedAssetAction().run(obj, false);
			break;
		}

	}

	@Override
	protected void onClick(ClientFixedAsset obj, int row, int col) {
		if (!Utility.isUserHavePermissions(IAccounterCore.FIXED_ASSET)) {
			return;
		}
		switch (col) {
		case 6:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	private void openHistoryView(ClientFixedAsset obj) {
		Action action = new HistoryListAction();
		action.catagory = messages.fixedAssetsPendingItemsList();
		action.run(obj, true);
	}

	@Override
	protected void executeDelete(ClientFixedAsset asset) {
		deleteObject(asset);
	}

	@Override
	protected int sort(ClientFixedAsset obj1, ClientFixedAsset obj2, int index) {

		switch (index) {
		case 0:
			return obj1.getName().compareTo(obj2.getName());

		case 1:
			Integer num1 = Integer.parseInt(obj1.getAssetNumber());
			Integer num2 = Integer.parseInt(obj2.getAssetNumber());
			return num1.compareTo(num2);

		case 2:
			return getAccount(obj1).compareTo(getAccount(obj2));

		case 3:
			ClientFinanceDate date1 = new ClientFinanceDate(
					obj1.getPurchaseDate());
			ClientFinanceDate date2 = new ClientFinanceDate(
					obj2.getPurchaseDate());
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;

		case 4:
			Double price1 = obj1.getPurchasePrice();
			Double price2 = obj2.getPurchasePrice();
			return price1.compareTo(price2);
		}

		return 0;

	}

	private String getAccount(ClientFixedAsset obj) {

		return getCompany().getAccount(obj.getAssetAccount()) != null ? Accounter
				.getCompany().getAccount(obj.getAssetAccount()).getName()
				: "";
	}

	@Override
	public AccounterCoreType getType() {
		return AccounterCoreType.FIXEDASSET;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "item", "assetnumber", "account", "purchasedate",
				"purchaseprice", "showhistory", "col-last" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "item-value", "assetnumber-value",
				"account-value", "purchasedate-value", "purchaseprice-value",
				"showhistory-value", "col-last-value" };
	}

}
