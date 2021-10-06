/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientFixedAsset;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.fixedassets.HistoryListAction;
import com.nitya.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.nitya.accounter.web.client.ui.fixedassets.NoteDialog;

/**
 * @author Murali.A
 * 
 */
public class SoldAndDisposedItemsListGrid extends
		BaseListGrid<ClientFixedAsset> {

	private NoteDialog noteDialog;

	/**
	 * @param isMultiSelectionEnable
	 */
	public SoldAndDisposedItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("SoldAndDisposedItemsListGrid");
	}

	/**
	 * @param isMultiSelectionEnable
	 * @param showFooter
	 */
	public SoldAndDisposedItemsListGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.grids.BaseListGrid#setColTypes()
	 */
	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK };
	}

	@Override
	protected int getCellWidth(int index) {
		return 111;
	}

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.grids.ListGrid#getColumnValue(java
	 * .lang.Object, int)
	 */
	@Override
	protected Object getColumnValue(ClientFixedAsset asset, int index) {
		switch (index) {
		case 0:
			return asset.getName();
		case 1:
			return asset.getAssetNumber();
		case 2:
			return getCompany().getAccount(asset.getAssetAccount()) != null ? Accounter
					.getCompany().getAccount(asset.getAssetAccount()).getName()
					: "";
		case 3:
			long date;
			if (asset.getSoldOrDisposedDate() != 0) {
				return UIUtils.getDateByCompanyType(new ClientFinanceDate(asset
						.getSoldOrDisposedDate()));
			} else
				date = 0;
			return date != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(date)) : "";

		case 4:
			return DataUtils.amountAsStringWithCurrency(asset.getSalePrice(),
					getCompany().getPrimaryCurrency());
		case 5:
			return DataUtils.amountAsStringWithCurrency(asset.getLossOrGain(),
					getCompany().getPrimaryCurrency());

		case 6:
			return messages.showHistory();
		}
		return "";
	}

	// public ClientSellingOrDisposingFixedAsset getSellingDisposedItem(
	// String assetID) {
	//
	// return FinanceApplication.getCompany().getSellingDisposedItem(assetID);
	//
	// }

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.grids.ListGrid#onDoubleClick(java
	 * .lang.Object)
	 */
	@Override
	public void onDoubleClick(ClientFixedAsset obj) {

	}

	@Override
	protected void onDoubleClick(ClientFixedAsset obj, int row, int col) {
		if (!Utility.isUserHavePermissions(IAccounterCore.FIXED_ASSET)) {
			return;
		}
		switch (col) {
		case 6:
			openHistoryView(obj);
			break;
		default:
			new NewFixedAssetAction().run(obj, false);
			break;
		}
	}

	@Override
	protected void onClick(ClientFixedAsset obj, int row, int col) {

	}

	private void openHistoryView(ClientFixedAsset obj) {
		Action action = new HistoryListAction();
		action.catagory = messages.fixedAssetsPendingItemsList();
		action.run(obj, true);
	}

	private void executeUpdate(ClientFixedAsset asset, String value) {
		createOrUpdate(asset);
	}

	@Override
	protected void executeDelete(ClientFixedAsset asset) {
		deleteObject(asset);
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { messages.item(), messages.assetNumber(),
				messages.Account(), messages.disposalDate(),
				messages.disposalPrice(), messages.gainsOrLosses(),
				messages.showHistory() };
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
					obj1.getSoldOrDisposedDate());
			ClientFinanceDate date2 = new ClientFinanceDate(
					obj2.getSoldOrDisposedDate());
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;

		case 4:
			Double price1 = obj1.getSalePrice();
			Double price2 = obj2.getSalePrice();
			return price1.compareTo(price2);

		case 5:
			Double gain1 = obj1.getLossOrGain();
			Double gain2 = obj2.getLossOrGain();
			return gain1.compareTo(gain2);
		}

		return 0;

	}

	private String getAccount(ClientFixedAsset obj) {

		return getCompany().getAccount(obj.getAssetAccount()) != null ? Accounter
				.getCompany().getAccount(obj.getAssetAccount()).getName()
				: "";
	}

	// its not using any where

	@Override
	public AccounterCoreType getType() {
		return null;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "item", "assetnumber", "account", "disposaldate",
				"disposalprice", "gainsorlosses", "showhistory" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "item-value", "assetnumber-value",
				"account-value", "disposaldate-value", "disposalprice-value",
				"gainsorlosses-value", "showhistory-value" };
	}

}
