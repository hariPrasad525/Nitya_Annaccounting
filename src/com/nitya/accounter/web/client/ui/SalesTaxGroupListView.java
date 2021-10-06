package com.nitya.accounter.web.client.ui;

import java.util.HashMap;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.company.ManageSupportListAction;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.grids.AbstractTransactionGrid.RecordClickHandler;

public class SalesTaxGroupListView extends BaseListView<ClientTAXGroup> {
	protected SalesTaxItemsGrid itemsGrid;

	public SalesTaxGroupListView() {
		this.getElement().setId("SalesTaxGroupListView");
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.taxGroupInUse());
			return;
		}

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		grid.deleteRecord((ClientTAXGroup) result);
		itemsGrid.removeAllRecords();
	}

	protected List<ClientTAXGroup> getRecords() {
		return getCompany().getTaxGroups();
	}

	private void setTaxItemsToGrid(List<ClientTAXItem> taxItems) {
		itemsGrid.removeAllRecords();
		itemsGrid.addRecords(taxItems);

	}

	@Override
	protected String getViewTitle() {
		return messages.vatGroupList();
	}

	@Override
	public void updateInGrid(ClientTAXGroup objectTobeModified) {
	}

	@Override
	protected void initGrid() {
		grid = new TaxGroupGrid(false);
		grid.init();
		grid.setView(this);
		grid.addRecordClickHandler(new RecordClickHandler<ClientTAXGroup>() {

			@Override
			public boolean onRecordClick(ClientTAXGroup core, int column) {
				setTaxItemsToGrid(core.getTaxItems());
				return true;
			}
		});

		itemsGrid = new SalesTaxItemsGrid(false);
		itemsGrid.init();
	}

	@Override
	protected void createControls() {
		super.createControls();
		gridLayout.add(itemsGrid);
	}

	@Override
	public void initListCallback() {
		grid.removeAllRecords();
		grid.addRecords(getRecords());
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	protected String getListViewHeading() {
		return messages.vatGroupList();
	}

	@Override
	protected Action getAddNewAction() {
		ManageSupportListAction salesTaxGroupAction = ManageSupportListAction
				.salesTaxGroup();
		return salesTaxGroupAction;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return messages.addaNew(messages.taxGroup());
		}
		return null;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		return hashMap;
	}

	@Override
	protected SelectCombo getSelectItem() {
		return null;
	}
}
