package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.AbstractTransactionGrid.RecordClickHandler;

/**
 * Re-implemented Class to get Correct Flow
 * 
 * @author Raj Vimal
 * @author Rajesh Alupula(Initial Implementation)
 * 
 */

public class SalesTaxGroupView extends BaseView<ClientTAXGroup> {

	protected BaseListGrid<ClientTAXItem> availTaxItemsGrid;
	protected BaseListGrid<ClientTAXItem> selectTaxItemsGrid;
	protected Button addButton, removeButton;

	private ArrayList<ClientTAXItem> tempAvailTaxItemList;
	private ArrayList<ClientTAXItem> tempSelectedTaxItemList;
	public TextItem taxGroupText;
	public DynamicForm form1;
	private List<DynamicForm> forms = new ArrayList<DynamicForm>();
	private static boolean flag = false;

	public SalesTaxGroupView() {
		this.getElement().setId("SalesTaxGroupDialog");
	}

	// Filling Available tax Codes in availTaxCodesGrid
	private void fillAvailableTaxItems() {
		for (ClientTAXItem codeInternal : getAvailableTaxItems())
			availTaxItemsGrid.addData(codeInternal);
	}

	// Filling Available tax Codes in selectTaxCodesGrid
	private void fillSelectedTaxItems() {
		ClientTAXGroup taxGroup = getData();
		if (getTaxItemsForTaxGroup(taxGroup) != null)
			for (ClientTAXItem codeInternal : getTaxItemsForTaxGroup(taxGroup)) {
				selectTaxItemsGrid.addData(codeInternal);
			}
	}

	// getting all Tax Codes from Company Object & converting these to temporary
	// TaxCodeInternal object for inserting data in Grid...
	public List<ClientTAXItem> getAllTaxItem() {
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
		List<ClientTAXItem> savedTaxItems = getCompany().getActiveTaxItems();
		for (ClientTAXItem clientTAXItem : savedTaxItems) {
			ClientTAXAgency taxAgency = Accounter.getCompany().getTaxAgency(
					clientTAXItem.getTaxAgency());
			if (taxAgency.getTaxType() != ClientTAXAgency.TAX_TYPE_TDS) {
				taxItems.add(clientTAXItem);
			}
		}
		return taxItems;
	}

	// getting all Available Tax Codes which are not available in Selected Tax
	// Code grid
	private ArrayList<ClientTAXItem> getAvailableTaxItems() {
		boolean isFound = false;
		tempSelectedTaxItemList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem codeInternal : getAllTaxItem()) {
			if (tempAvailTaxItemList != null)
				for (ClientTAXItem internal : tempAvailTaxItemList) {
					if (codeInternal.getID() == internal.getID()) {
						isFound = true;
						break;
					} else
						isFound = false;
				}
			if (!isFound)
				tempSelectedTaxItemList.add(codeInternal);
		}
		return tempSelectedTaxItemList;

	}

	// getting all available Tax Codes in Selected Tax Code grid
	private ArrayList<ClientTAXItem> getTaxItemsForTaxGroup(
			ClientTAXGroup taxGroup) {

		List<ClientTAXItem> items = taxGroup.getTaxItems();
		if (items != null)
			tempAvailTaxItemList = new ArrayList<ClientTAXItem>(items);
		// tempAvailTaxCodeList = (ArrayList) Arrays.asList(codes.toArray());

		return tempAvailTaxItemList;

	}

	public Object getAvailTaxItemsGridColumnValue(
			ClientTAXItemGroup accounterCore, int col) {
		ClientTAXItem taxItem = (ClientTAXItem) accounterCore;
		switch (col) {
		case 0:
			return taxItem.getName() != null ? taxItem.getName() : "";
		case 1:
			return taxItem.getTaxRate() + " %";
		default:
			break;
		}
		return null;
	}

	public Object getSelectedTaxItemsColumnValue(IsSerializable accounterCore,
			int col) {
		ClientTAXItem taxItem = (ClientTAXItem) accounterCore;
		switch (col) {
		case 0:
			return taxItem.getName();
		case 1:
			return taxItem.getTaxRate() + " %";
		default:
			break;
		}
		return null;
	}

	public List<ClientTAXGroup> getAvailableRecords() {

		return getCompany().getTaxGroups();
	}

	private void createControls() {
		Label label = new Label(getViewTitle());
		label.addStyleName("label-title");

		StyledPanel bodyLayout = new StyledPanel("bodyLayout");

		bodyLayout.add(label);

		form1 = new DynamicForm("form1");
		taxGroupText = new TextItem(messages.selectedTaxGroupItem(),
				"taxGroupText");
		taxGroupText.setEnabled(!isInViewMode());
		taxGroupText.setRequired(true);

		form1.add(taxGroupText);

		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");

		addButton = new Button(messages.add());
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (availTaxItemsGrid.getSelection() != null) {
					ClientTAXItem gridRecord = (ClientTAXItem) availTaxItemsGrid
							.getSelection();
					selectTaxItemsGrid.addData(gridRecord);
					tempSelectedTaxItemList.add(gridRecord);

					availTaxItemsGrid.deleteRecord(gridRecord);
					if (tempAvailTaxItemList != null)
						tempAvailTaxItemList.remove(gridRecord);
					if (availTaxItemsGrid.getRecords() == null
							|| availTaxItemsGrid.getRecords().size() == 0
							|| availTaxItemsGrid.getSelection() == null) {
						addButton.setEnabled(false);
					}

				} else {

					UIUtils.say(messages
							.selectTaxItemFromAvailableListofTaxItems());
					new Exception();
				}

			}
		});
		addButton.setEnabled(false);

		removeButton = new Button(messages.remove());
		// removeButton.setWidth("80px");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (selectTaxItemsGrid.getSelection() != null) {
					ClientTAXItem gridRecord = (ClientTAXItem) selectTaxItemsGrid
							.getSelection();
					selectTaxItemsGrid.deleteRecord(gridRecord);
					tempSelectedTaxItemList.remove(gridRecord);
					availTaxItemsGrid.addData(gridRecord);
					availTaxItemsGrid.setSelection(availTaxItemsGrid
							.getSelection());
					if (tempAvailTaxItemList != null)
						tempAvailTaxItemList.add(gridRecord);
					if (selectTaxItemsGrid.getRecords() == null
							|| selectTaxItemsGrid.getRecords().size() == 0
							|| selectTaxItemsGrid.getSelection() == null) {
						removeButton.setEnabled(false);
					}
				} else {

					Accounter.showError(messages
							.selectTaxItemFromSelectedListofTaxItems());
					new Exception();
				}

			}
		});
		removeButton.setEnabled(false);

		availTaxItemsGrid = new BaseListGrid<ClientTAXItem>(false) {
			@Override
			protected Object getColumnValue(ClientTAXItem obj, int index) {
				return getAvailTaxItemsGridColumnValue(obj, index);
			}

			@Override
			protected void onClick(ClientTAXItem obj, int row, int col) {
				super.onClick(obj, row, col);
				if (recordClickHandler != null) {
					recordClickHandler.onRecordClick(obj, col);
				}
			}

			@Override
			public String getHeaderStyle(int index) {
				switch (index) {
				case 0:
					return "name";
				case 1:
					return "currentRate";
				default:
					break;

				}
				return "";
			}

			@Override
			public String getRowElementsStyle(int index) {
				switch (index) {
				case 0:
					return "nameValue";
				case 1:
					return "currentRateValue";
				default:
					break;

				}
				return "";
			}

			@Override
			protected int[] setColTypes() {
				return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT };
			}

			@Override
			protected String[] setHeaderStyle() {
				return new String[] { "nameValue", "currentRateValue" };
			}

			@Override
			protected String[] setRowElementsStyle() {
				return new String[] { "nameValue", "currentRateValue" };
			}

			@Override
			protected void executeDelete(ClientTAXItem object) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDoubleClick(ClientTAXItem obj) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String[] getColumns() {
				return new String[] { messages.name(), messages.currentRate() };
			}

			@Override
			protected int getCellWidth(int index) {
				switch (index) {
				case 0:
					return 150;
				case 1:
					return 100;

				default:
					break;
				}

				return super.getCellWidth(index);
			}
		};
		// availTaxCodesGrid.setCellsWidth(cellsWidth)
		availTaxItemsGrid.addStyleName("available-tax-items-list");
		// getAvailableRecords();
		availTaxItemsGrid
				.addRecordClickHandler(new RecordClickHandler<ClientTAXItem>() {

					@Override
					public boolean onRecordClick(ClientTAXItem core, int column) {
						addButton.setEnabled(!isInViewMode());
						return true;
					}
				});
		availTaxItemsGrid.init();
		// availTaxCodesGrid.setWidth("80%");
		// availTaxCodesGrid.setHeight("100%");

		// Buttons Layout
		StyledPanel buttonsLayout = new StyledPanel("buttonsLayout");
		// buttonsLayout.setMembersMargin(10);
		// buttonsLayout.setLayoutMargin(10);

		buttonsLayout.add(addButton);
		buttonsLayout.add(removeButton);
		// Selected Tax Codes Layout
		// DynamicForm selectForm = new DynamicForm();

		selectTaxItemsGrid = new BaseListGrid<ClientTAXItem>(false) {
			@Override
			protected Object getColumnValue(ClientTAXItem obj, int index) {
				return getAvailTaxItemsGridColumnValue(obj, index);
			}

			@Override
			protected void onClick(ClientTAXItem obj, int row, int col) {
				super.onClick(obj, row, col);
				if (recordClickHandler != null) {
					recordClickHandler.onRecordClick(obj, col);
				}
			}

			@Override
			public String getHeaderStyle(int index) {
				switch (index) {
				case 0:
					return "name";
				case 1:
					return "currentRate";
				default:
					break;

				}
				return "";
			}

			@Override
			public String getRowElementsStyle(int index) {
				switch (index) {
				case 0:
					return "nameValue";
				case 1:
					return "currentRateValue";
				default:
					break;

				}
				return "";
			}

			@Override
			protected int[] setColTypes() {
				return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT };
			}

			@Override
			protected String[] setHeaderStyle() {
				return new String[] { "nameValue", "currentRateValue" };
			}

			@Override
			protected String[] setRowElementsStyle() {
				return new String[] { "nameValue", "currentRateValue" };
			}

			@Override
			protected void executeDelete(ClientTAXItem object) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDoubleClick(ClientTAXItem obj) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String[] getColumns() {
				return new String[] { messages.name(), messages.currentRate() };
			}

			@Override
			protected int getCellWidth(int index) {
				switch (index) {
				case 0:
					return 150;
				case 1:
					return 100;
				default:
					break;
				}
				return super.getCellWidth(index);
			}
		};
		selectTaxItemsGrid.addStyleName("select-tax-items-list");
		selectTaxItemsGrid
				.addRecordClickHandler(new RecordClickHandler<ClientTAXItem>() {

					@Override
					public boolean onRecordClick(ClientTAXItem core, int column) {
						removeButton.setEnabled(!isInViewMode());
						return true;
					}

				});
		selectTaxItemsGrid.init();
		horizontalPanel.add(availTaxItemsGrid);
		horizontalPanel.add(buttonsLayout);
		horizontalPanel.add(selectTaxItemsGrid);
		bodyLayout.add(form1);
		bodyLayout.add(horizontalPanel);
		add(bodyLayout);
	}

	public BaseListGrid<ClientTAXItem> getSelectTaxItemsGrid() {
		return selectTaxItemsGrid;
	}

	public Object getGridColumnValue(ClientTAXGroup obj, int index) {

		if (!flag) {
			flag = true;
			return getAvailTaxItemsGridColumnValue(obj, index);

		} else {
			flag = false;
			return getSelectedTaxItemsColumnValue(obj, index);

		}

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(form1.validate());
		String groupName = taxGroupText.getValue();

		ClientCompany company = getCompany();
		ClientTAXGroup taxGroupByName = company.getTaxGroupByName(groupName);
		ClientTAXItem itemByName = company.getTaxItemByName(groupName);

		if ((itemByName != null && itemByName.getID() != getData().getID())
				|| (taxGroupByName != null && taxGroupByName.getID() != getData()
						.getID())) {
			result.addError(taxGroupText, messages.alreadyExist());
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		ClientTAXGroup taxGroup = getData();
		data.setName(UIUtils.toStr(taxGroupText.getValue()));
		data.setActive(true);
		data.setPercentage(true);
		data.setSalesType(true);
		data.setTaxItems(getSelectedTaxItems(data));
		ClientCompany company = getCompany();
		ClientTAXItem itemByName = company.getTaxItemByName(data.getName());
		ClientTAXGroup taxGroupByName = company.getTaxGroupByName(data
				.getName());
		if ((itemByName != null && itemByName.getID() != getData().getID())
				|| (taxGroupByName != null && taxGroupByName.getID() != getData()
						.getID())) {
			Accounter.showError(messages.alreadyExist());
		} else {
			saveOrUpdate(taxGroup);
		}
	}

	protected void newTaxGroup() {
	}

	private List<ClientTAXItem> getSelectedTaxItems(ClientTAXGroup taxGroup) {
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
		List<ClientTAXItem> records = selectTaxItemsGrid.getRecords();
		ClientTAXItem item;
		double groupRate = 0.00D;
		for (ClientTAXItem clientTaxItem : records) {
			item = getTaxItemByName(clientTaxItem.getName());
			if (item != null) {
				taxItems.add(item);
				groupRate = item.getTaxRate() + groupRate;

			}// if
		}// for
		taxGroup.setGroupRate(groupRate);
		return taxItems;
	}

	private ClientTAXItem getTaxItemByName(String attribute) {

		for (ClientTAXItem item : getAllTaxItem()) {
			if (item.getName() != null && item.getName().equals(attribute)) {
				return item;
			}
		}
		return null;
	}

	protected List<ClientTAXGroup> getRecords() {
		return getCompany().getTaxGroups();
	}

	@Override
	public void setFocus() {
		taxGroupText.setFocus();

	}

	// @Override
	// public Object getGridColumnValue(IsSerializable obj, int index) {
	// return null;
	// }

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return this.getAction().getText();
	}

	@Override
	public List<DynamicForm> getForms() {
		return forms;
	}

	@Override
	public void initData() {
		fillAvailableTaxItems();
		if (data == null) {
			data = new ClientTAXGroup();
		} else {
			taxGroupText.setValue(data.getName());
			fillSelectedTaxItems();
		}
		super.initData();
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	public void onEdit() {
		Accounter.createCRUDService().canEdit(AccounterCoreType.TAX_ITEM_GROUP,
				getData().getID(), new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							enableFormItems();
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		taxGroupText.setEnabled(!isInViewMode());
	}
}
