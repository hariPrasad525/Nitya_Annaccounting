/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCurrency;
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
public class RegisteredItemsListGrid extends BaseListGrid<ClientFixedAsset> {

	private NoteDialog noteDialog;
	private final ClientCurrency currency = getCompany().getPrimaryCurrency();

	/**
	 * @param isMultiSelectionEnable
	 */
	public RegisteredItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("RegisteredItemsListGrid");
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.BaseListGrid#setColTypes()
	 */
	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 7)
			return 20;
		else if (index == 6 || index == 5)
			return 90;
		else if (index == 4 || index == 3)
			return 100;
		else if (index == 0)
			return 120;
		else if (index == 1)
			return 85;
		else if (index == 2) {
			return 218;
		}
		return super.getCellWidth(index);
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#getColumnValue(java
	 * .lang.Object, int)
	 */
	@Override
	protected Object getColumnValue(ClientFixedAsset item, int index) {
		switch (index) {
		case 0:
			return item.getName();
		case 1:
			return item.getAssetNumber();
		case 2:
			return getCompany().getAccount(item.getAssetAccount()) != null ? Accounter
					.getCompany().getAccount(item.getAssetAccount()).getName()
					: "";
		case 3:
			return item.getPurchaseDate() != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(item
							.getPurchaseDate())) : "";
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					item.getPurchasePrice(), currency);
		case 5:
			return DataUtils.amountAsStringWithCurrency(item.getBookValue(),
					currency);
		case 6:
			return messages.showHistory();
		case 7:
			return Accounter.getFinanceImages().delete();
			// return "/images/delete.png";
		}
		return "";
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
		if (!Utility.isUserHavePermissions(IAccounterCore.FIXED_ASSET)) {
			return;
		}
		switch (col) {
		case 7:
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

	private void executeUpdate(ClientFixedAsset asset, String value) {
		Accounter.createOrUpdate(this, asset);
	}

	@Override
	protected void executeDelete(ClientFixedAsset asset) {
		deleteObject(asset);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { messages.item(), messages.assetNumber(),
				messages.Account(), messages.purchaseDate(),
				messages.purchasePrice(), messages.bookValue(),
				messages.showHistory(), "" };
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

		case 5:
			Double bookValue1 = obj1.getBookValue();
			Double bookValue2 = obj2.getBookValue();
			return bookValue1.compareTo(bookValue2);
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
		return null;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "item", "assetnumber", "account", "purchasedate",
				"purchaseprice", "bookvalue", "showhistory", "last-col" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "item-value", "assetnumber-value",
				"account-value", "purchasedate-value", "purchaseprice-value",
				"bookvalue-value", "showhistory-value", "last-col-value" };
	}

}
