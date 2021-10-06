package com.nitya.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;

public class ConsultantsListGrid extends BaseListGrid<ClientItem> {
	Map<Integer, Integer> colsMap = new HashMap<Integer, Integer>();
	private ConsultantSelectionListener consultantSelectionListener;
	ClientItem selectedItem;

	public ConsultantsListGrid() {
		super(false, true);
		this.getElement().setId("VendorsListGrid");
	}

	@Override
	protected Object getColumnValue(ClientItem clientItem, int col) {

		switch (col) {

		case 0:
			return clientItem.getName();
		case 1:
			return DataUtils.getAmountAsStringInPrimaryCurrency(clientItem.getSalesPrice());

		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getColumns() {

		return new String[] { messages.name(), messages.balance() };

	}

	@Override
	protected void onClick(ClientItem obj, int row, int col) {
		if (!Utility.isUserHavePermissions(AccounterCoreType.ITEM)) {
			return;
		}
		AccounterAsyncCallback<ClientItem> callback = new AccounterAsyncCallback<ClientItem>() {

			@Override
			public void onException(AccounterException caught) {
			}

			@Override
			public void onResultSuccess(ClientItem result) {
				if (result != null) {
					setSelectedItem(result);
					consultantSelectionListener.consultantSelected();
				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.ITEM,
				obj.getID(), callback);

	}

	@Override
	public void addEmptyMessage(String msg) {
		super.addEmptyMessage(msg);
	}

	@Override
	protected void onValueChange(ClientItem obj, int col, Object value) {
		Accounter.showInformation(messages.onvaluechangecalled());
	}

	@Override
	public void onDoubleClick(ClientItem obj) {

	}

	@Override
	protected void executeDelete(final ClientItem recordToBeDeleted) {

	}

	public void setConsultantSelectionListener(
			ConsultantSelectionListener consultantSelectionListener) {
		this.consultantSelectionListener = consultantSelectionListener;

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	protected void updateTotal(ClientItem clientItem, boolean add) {

		if (add) {
			if (clientItem.isActive())
				total += clientItem.getSalesPrice();
			else
				total += clientItem.getSalesPrice();
		} else
			total -= clientItem.getSalesPrice();
	}

	@Override
	public Double getTotal() {
		return total;
	}

	@Override
	public void setTotal() {
		this.total = 0.0D;
	}

	@Override
	protected int getCellWidth(int index) {

		return 100;
	}

	@Override
	public void addRecords(List<ClientItem> list) {
		super.addRecords(list);

	}

	@Override
	protected int sort(ClientItem obj1, ClientItem obj2, int index) {
		switch (index) {
		case 0:
			return obj1.getName().compareToIgnoreCase(obj2.getName());

		default:
			break;
		}

		return 0;
	}

	@Override
	public AccounterCoreType getType() {
		return AccounterCoreType.ITEM;
	}

	@Override
	public void addData(ClientItem obj) {
		super.addData(obj);

	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.payeeInUse(Global.get().Vendor()));
			return;
		}
		super.deleteFailed(caught);
	}

	public ClientItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ClientItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "name", "balance" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "name-value", "balance-value" };
	}

}
