package com.nitya.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.ItemListView;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.company.NewItemAction;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.InventoryItemsListGrid;

public class InventoryItemsListView extends BaseListView<ClientItem> implements
		IPrintableView {
	ArrayList<ClientItem> allItems = new ArrayList<ClientItem>();
	private Double total = 0.00;
	private ClientItem toBeDeletedItem;
	private ArrayList<ClientItem> listOfItems = new ArrayList<ClientItem>();
	protected SelectCombo reorderedPointCombo;
	private boolean isActiveAccounts = true;

	public InventoryItemsListView() {
		this.getElement().setId("InventoryItemsListView");
	}

	public static ItemListView getInstance() {
		return new ItemListView();
	}

	@Override
	public void onSuccess(PaginationList<ClientItem> result) {
		super.onSuccess(result);
		grid.sort(10, false);
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
		allItems.remove(toBeDeletedItem);
		refreshTotal();

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
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			NewItemAction action = new NewItemAction(true);
			return action;
		}
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return messages.addaNewInventoryItem();
		}
		return null;
	}

	@Override
	protected String getListViewHeading() {
		return messages.productList();
	}

	@Override
	public void initListCallback() {
		filterList(isActiveAccounts);
	}

	@Override
	public void updateInGrid(ClientItem objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new InventoryItemsListGrid(false);
		grid.init();
		listOfItems = getCompany().getAllItems();
		filterList(true);
	}

	@Override
	protected void createListForm(DynamicForm form) {
		reorderedPointCombo = getReorderedPointItem();
		if (reorderedPointCombo != null) {
			form.add(reorderedPointCombo);
		}
		super.createListForm(form);

	}

	protected SelectCombo getReorderedPointItem() {
		reorderedPointCombo = new SelectCombo(messages.reorderPoint());
		reorderedPointCombo.setComboItem("All");
		List<String> typeList = new ArrayList<String>();
		typeList.add(messages.all());
		typeList.add(messages2.safe());
		typeList.add(messages2.reached());
		reorderedPointCombo.initCombo(typeList);
		reorderedPointCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (reorderedPointCombo.getSelectedValue() != null) {
							if (reorderedPointCombo.getSelectedValue()
									.toString()
									.equalsIgnoreCase(messages.active()))
								filterList(true);
							else
								filterList(false);
						}
					}
				});
		return reorderedPointCombo;
	}

	@Override
	protected void filterList(boolean isActive) {
		String selectedValue = viewSelect.getSelectedValue();
		grid.removeAllRecords();
		for (ClientItem item : listOfItems) {
			if (item.getType() == ClientItem.TYPE_INVENTORY_PART
					|| item.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
				if (selectedValue.toString()
						.equalsIgnoreCase(messages.active()) && item.isActive()) {
					addItemsToGrid(item);
				} else if (selectedValue.toString().equalsIgnoreCase(
						messages.inActive())
						&& !item.isActive()) {
					addItemsToGrid(item);
				} else if (selectedValue.toString().equalsIgnoreCase(
						messages.all())) {
					addItemsToGrid(item);
				}
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	private void addItemsToGrid(ClientItem item) {
		String selectedValue = reorderedPointCombo.getSelectedValue();
		ClientQuantity onhandQty = item.getOnhandQty();
		ClientQuantity reorderPoint = item.getReorderPoint();
		if (selectedValue.equalsIgnoreCase(messages2.reached())
				&& reorderPoint != null
				&& onhandQty.getValue() >= reorderPoint.getValue()) {
			grid.addData(item);
		} else if (selectedValue.equalsIgnoreCase(messages2.safe())
				&& (reorderPoint == null || onhandQty.getValue() < reorderPoint
						.getValue())) {
			grid.addData(item);
		} else if (selectedValue.equalsIgnoreCase(messages.all())) {
			grid.addData(item);
		}
	}

	// @Override
	// protected void filterList(boolean isActive) {
	// grid.removeAllRecords();
	// for (ClientItem item : listOfItems) {
	// if (isActive) {
	// if ((item.isActive() == true) && (item.getType() == type))
	// grid.addData(item);
	// } else if ((item.isActive() == false) && (item.getType() == type)) {
	// grid.addData(item);
	// }
	//
	// }
	// if (grid.getRecords().isEmpty())
	// grid.addEmptyMessage(messages.noRecordsToShow());
	// }

	public void setCatageoryType(String catagory) {
		messages.inventory();
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

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
		if (reorderedPointCombo != null) {
			map.put("reorderedPoint", reorderedPointCombo.getValue().toString());
		}
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		// Current View Combo
		isActive = (Boolean) viewDate.get("isActive");
		if (isActive) {
			viewSelect.setComboItem(messages.active());
		} else {
			viewSelect.setComboItem(messages.inActive());
		}
		// Restore the Reordered COmbo
		String reorderedPoint = null;
		if (reorderedPointCombo != null) {
			reorderedPoint = (String) viewDate.get("reorderedPoint");
			reorderedPointCombo.setComboItem(reorderedPoint);
		}
		if (reorderedPoint.equalsIgnoreCase(messages2.reached())) {
			reorderedPointCombo.setComboItem(messages2.reached());
		} else if (reorderedPoint.equalsIgnoreCase(messages2.safe())) {
			reorderedPointCombo.setComboItem(messages2.safe());
		} else {
			reorderedPointCombo.setComboItem(messages.all());
		}

		start = (Integer) viewDate.get("start");
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
	protected String getViewTitle() {
		return messages.items();
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
		Accounter.createExportCSVService().getItemsExportCsv(
				ItemListView.isPurchaseType, ItemListView.isSalesType,
				viewSelect.getSelectedValue(), 2,
				getExportCSVCallback(messages.items()));
	}
}
