package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.company.NewItemAction;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.grids.ItemsListGrid;

/**
 * 
 * @author Mandeep Singh modified by Rajesh.A
 * 
 */
public class ItemListView extends BaseListView<ClientItem> implements
		IPrintableView {
	ArrayList<ClientItem> allItems = new ArrayList<ClientItem>();
	private Double total = 0.00;
	private ClientItem toBeDeletedItem;
	private ArrayList<ClientItem> listOfItems = new ArrayList<ClientItem>();

	private String catageory;

	/*
	 * To Identify from which view the request is generated.i.e SalesItemview or
	 * PurchaseItemview
	 */
	public static boolean isPurchaseType = false;
	public static boolean isSalesType = false;

	public ItemListView() {
		super();
		this.getElement().setId("ItemListView");
	}

	public static ItemListView getInstance() {
		return new ItemListView();

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		getCompany().deleteItem(result.getID());
		allItems.remove(toBeDeletedItem);
		refreshTotal();
		// Accounter.showInformation(FinanceApplication.constants()
		// .itemDeletedSuccessfully());

	}

	public void refreshTotal() {

		total = 0.00;
		for (ClientItem item : allItems) {
			if (!DecimalUtil.isEquals(item.getSalesPrice(), 0))
				total += item.getSalesPrice();
		}
		if (totalLabel != null) {
			totalLabel.setText(messages.totalSalesPrice() + " = "
					+ DataUtils.getAmountAsStringInPrimaryCurrency(total));
		}
	}

	@Override
	protected NewItemAction getAddNewAction() {
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return null;
		else {
			NewItemAction action;
			if (this.catageory.equals(Global.get().customer())) {
				action = new NewItemAction(true);
			} else {
				action = new NewItemAction(false);
			}
			action.setFrmAnyView(true);
			return action;
		}
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addNewItem();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return messages.productList();
	}

	@Override
	public void initListCallback() {
		filterList(isActive);
	}

	@Override
	public void updateInGrid(ClientItem objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new ItemsListGrid(false);
		grid.init();
		// isPurchaseType = !isSalesType;
		createFilteredItemsObj();
		filterList(true);
	}

	private void createFilteredItemsObj() {
		ArrayList<ClientItem> items = new ArrayList<ClientItem>();
		if (isSalesType && isPurchaseType) {
			listOfItems = getCompany().getAllItems();
		} else if (isPurchaseType)
			listOfItems = getCompany().getPurchaseItems();
		else if (isSalesType)
			listOfItems = getCompany().getSalesItems();

		UIUtils.getFilteredListByDepth(items, listOfItems, 0, 0);
		listOfItems = items;
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		createFilteredItemsObj();
		for (ClientItem item : listOfItems) {
			if (isActive) {
				if (item.isActive() == true)
					grid.addData(item);
			} else if (item.isActive() == false) {
				grid.addData(item);
			}

		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());
	}

	public void setCatageoryType(String catagory) {
		this.catageory = catagory;
		if (this.catageory.equalsIgnoreCase(Global.get().customer())) {
			isSalesType = true;
			isPurchaseType = false;
		} else if (this.catageory.equalsIgnoreCase(Global.get().vendor())) {
			isPurchaseType = true;
			isSalesType = false;
		} else if (this.catageory.equalsIgnoreCase(messages
				.bothCustomerAndVendor(Global.get().Customer(), Global.get()
						.Vendor()))) {
			isSalesType = isPurchaseType = true;
		}
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

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
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedValue = viewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.active())) {
			isActive = true;
		} else {
			isActive = false;
		}
		map.put("isActive", isActive);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		isActive = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		if (isActive) {
			viewSelect.setComboItem(messages.active());
		} else {
			viewSelect.setComboItem(messages.inActive());
		}
	}

	@Override
	protected String getViewTitle() {
		return messages.items();
	}

	@Override
	public void onSuccess(PaginationList<ClientItem> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getItemsExportCsv(isPurchaseType,
				isSalesType, viewSelect.getSelectedValue(), 0,
				getExportCSVCallback(messages.items()));

	}
}
