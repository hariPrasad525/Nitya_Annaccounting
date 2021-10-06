package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;

public class CurrencyGroupListView extends BaseListView<ClientCurrency> {
	private NewCurrencyListDialog currencyListDialog;

	public CurrencyGroupListView() {
		super();
		this.getElement().setId("CurrencyGroupListDialog");
		// setWidth("400px");
	}

	public void ShowAddEditDialog(ClientCurrency currency) {
		currencyListDialog = new NewCurrencyListDialog(this,
				messages.addCurrency(), currency);
		ViewManager.getInstance().showDialog(currencyListDialog);

	}

	private String[] setColumns() {
		return new String[] { messages.currency(), messages.delete() };
	}

	private String[] getHeaderStyle() {
		return new String[] { "currency", "delete" };
	}

	private String[] getRowElementsStyle() {
		return new String[] { "currencyValue", "deleteValue" };
	}

	protected List<ClientCurrency> getRecords() {
		return new ArrayList<ClientCurrency>(getCompany().getCurrencies());
	}

	public Object getGridColumnValue(ClientCurrency obj, int index) {
		ClientCurrency currency = (ClientCurrency) obj;
		if (currency != null) {
			switch (index) {
			case 0:
				return currency.getName();
			case 1:
				return Accounter.getFinanceImages().delete();
			}
		}
		return null;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		ClientCurrency clientCurrency = (ClientCurrency) getGrid()
				.getSelection();
		ClientCurrency currency = getCompany().getPrimaryCurrency();
		if (currency != null && clientCurrency.getID() == currency.getID()) {
			result.addError(this, messages.CannotDeletePrimaryCurrency());
		}
		return result;
	}

	protected boolean onOK() {
		saveOrUpdate(currencyListDialog.createOrEditItemGroup());
		return true;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void updateInGrid(ClientCurrency objectTobeModified) {
	}

	@Override
	protected void initGrid() {
		grid = new BaseListGrid<ClientCurrency>(false) {

			@Override
			protected String[] getColumns() {
				return CurrencyGroupListView.this.setColumns();
			}

			@Override
			public void onDoubleClick(ClientCurrency obj) {
				ClientCurrency selection = (ClientCurrency) getGrid()
						.getSelection();
				ShowAddEditDialog(getCompany().getCurrency(selection.getID()));
			}

			@Override
			protected Object getColumnValue(ClientCurrency obj, int index) {
				return CurrencyGroupListView.this
						.getGridColumnValue(obj, index);
			}

			@Override
			protected String[] setRowElementsStyle() {
				return CurrencyGroupListView.this.getRowElementsStyle();
			}

			@Override
			protected String[] setHeaderStyle() {
				return CurrencyGroupListView.this.getHeaderStyle();
			}

			@Override
			protected int[] setColTypes() {
				return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_IMAGE };
			}

			@Override
			protected void executeDelete(ClientCurrency object) {
				deleteObject(object);
			}

			protected void onClick(ClientCurrency obj, int row, int col) {
				if (col == 1) {
					showWarnDialog(obj);
				}
			};
		};
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.currencyList();
	}

	@Override
	protected Action getAddNewAction() {
		ShowAddEditDialog(null);
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addaNew(messages.currency());
	}

	@Override
	protected String getViewTitle() {
		return messages.currencyList();
	}

	@Override
	protected SelectCombo getSelectItem() {
		return null;
	}

	@Override
	public void initListCallback() {
		grid.removeAllRecords();
		grid.addRecords(getRecords());
	}
}
