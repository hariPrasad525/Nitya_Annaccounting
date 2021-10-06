package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.DialogGrid;
import com.nitya.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author Umasree V
 * 
 */
public class SelectItemsTo1099Dialog<T extends IAccounterCore> extends
		BaseDialog<T> {

	protected DialogGrid<T> availItemsGrid;
	protected DialogGrid<T> selectItemsGrid;
	protected Button addButton, removeButton;

	public ArrayList<T> tempAvailItemsList;
	public ArrayList<T> tempSelectedItemsList;
	public DynamicForm form1;
	private ArrayList<T> availItemsList;
	private static boolean flag = false;
	private ActionCallback<ArrayList<T>> callback;

	public SelectItemsTo1099Dialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("SelectItemsTo1099Dialog");
		createControls();
	}

	private void fillSelectedVendors() {
		for (T codeInternal : getSelectedItems()) {
			selectItemsGrid.addData(codeInternal);
		}
	}

	private ArrayList<T> getSelectedItems() {
		return tempSelectedItemsList;
	}

	public void setSelectedItems(ArrayList<T> selectedItems) {
		this.tempSelectedItemsList = selectedItems;
		fillSelectedVendors();
	}

	private void fillAvailableVendors() {
		for (T vendor : getAvailableItems())
			availItemsGrid.addData(vendor);
	}

	private ArrayList<T> getAvailableItems() {
		boolean isFound = false;
		tempAvailItemsList = new ArrayList<T>();
		for (T codeInternal : availItemsList) {
			if (tempSelectedItemsList != null)
				for (T internal : tempSelectedItemsList) {
					if (codeInternal.getID() == internal.getID()) {
						isFound = true;
						break;
					} else
						isFound = false;
				}
			if (!isFound)
				tempAvailItemsList.add(codeInternal);
		}
		return tempAvailItemsList;

	}

	public void setAvailableItems(ArrayList<T> availableItems) {
		this.availItemsList = availableItems;
		fillAvailableVendors();
	}

	private void createControls() {
		// setWidth("570px");
		StyledPanel mainPanel = new StyledPanel("mainPanel");

		addButton = new Button(messages.add());
		// addButton.setWidth("80px");
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (availItemsGrid.getSelection() != null) {
					T gridRecord = (T) availItemsGrid.getSelection();
					selectItemsGrid.addData(gridRecord);
					tempSelectedItemsList.add(gridRecord);

					availItemsGrid.deleteRecord(gridRecord);
					if (tempAvailItemsList != null)
						tempAvailItemsList.remove(gridRecord);
					if (availItemsGrid.getRecords() == null
							|| availItemsGrid.getRecords().size() == 0
							|| availItemsGrid.getSelection() == null) {
						addButton.setEnabled(false);
					}

				}
			}
		});
		addButton.setEnabled(false);

		removeButton = new Button(messages.remove());
		// removeButton.setWidth("80px");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (selectItemsGrid.getSelection() != null) {
					T gridRecord = (T) selectItemsGrid.getSelection();
					selectItemsGrid.deleteRecord(gridRecord);
					tempSelectedItemsList.remove(gridRecord);
					int selectedIndex = availItemsGrid.getSelectedRecordIndex();
					availItemsGrid.addData(gridRecord);
					availItemsGrid.selectRecord(selectedIndex);
					if (tempAvailItemsList != null)
						tempAvailItemsList.add(gridRecord);
					if (selectItemsGrid.getRecords() == null
							|| selectItemsGrid.getRecords().size() == 0
							|| selectItemsGrid.getSelection() == null) {
						removeButton.setEnabled(false);
					}
				}
			}
		});
		removeButton.setEnabled(false);

		availItemsGrid = new DialogGrid<T>(false) {

			@Override
			protected String getHeaderStyle(int index) {
				switch (index) {
				case 0:
					return "selectFromList";
				default:
					break;
				}
				return "";
			}

			@Override
			protected String getRowElementsStyle(int index) {
				switch (index) {
				case 0:
					return "selectFromList";
				default:
					break;
				}
				return "";
			}

		};

		availItemsGrid.setName(messages.available());
		setAvailVendorsGridFields();
		setAvalableVendorsGridCellWidths();
		availItemsGrid.setView(SelectItemsTo1099Dialog.this);
		availItemsGrid.addRecordClickHandler(new GridRecordClickHandler<T>() {

			@Override
			public boolean onRecordClick(T core, int column) {
				addButton.setEnabled(true);
				return true;
			}
		});
		availItemsGrid.init();

		// Buttons Layout
		StyledPanel buttonsLayout = new StyledPanel("buttonsLayout");
		addButton.getElement().setAttribute("data-icon", "add");
		removeButton.getElement().setAttribute("data-icon", "delete");
		addButton(buttonsLayout, addButton);
		addButton(buttonsLayout, removeButton);

		selectItemsGrid = new DialogGrid<T>(false) {

			@Override
			protected String getHeaderStyle(int index) {
				switch (index) {
				case 0:
					return "trackPaymentsFor1099";
				default:
					break;
				}
				return "";
			}

			@Override
			protected String getRowElementsStyle(int index) {
				switch (index) {
				case 0:
					return "trackPaymentsFor1099";
				default:
					break;
				}
				return "";
			}

		};
		selectItemsGrid.setName(messages.selected());
		setSelectedVendorsGridFields();
		setSelectedVendorGridCellWidths();
		selectItemsGrid.setView(SelectItemsTo1099Dialog.this);
		selectItemsGrid.addRecordClickHandler(new GridRecordClickHandler<T>() {

			@Override
			public boolean onRecordClick(T core, int column) {
				removeButton.setEnabled(true);
				return true;
			}
		});
		selectItemsGrid.init();

		// mainPanel.setCellHorizontalAlignment(availItemsGrid,
		// HasHorizontalAlignment.ALIGN_LEFT);
		mainPanel.add(availItemsGrid);
		// mainPanel.setCellHorizontalAlignment(buttonsLayout,
		// HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(buttonsLayout);
		// mainPanel.setCellHorizontalAlignment(selectItemsGrid,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		mainPanel.add(selectItemsGrid);

		setBodyLayout(mainPanel);
	}

	private void setSelectedVendorsGridFields() {
		selectItemsGrid.setCellsWidth(new Integer[] { 200 });

	}

	private void setSelectedVendorGridCellWidths() {
		selectItemsGrid.addColumns(new String[] { messages
				.trackPaymentsFor1099() });

	}

	private void setAvalableVendorsGridCellWidths() {
		availItemsGrid.setCellsWidth(new Integer[] { 200 });

	}

	private void setAvailVendorsGridFields() {
		availItemsGrid.addColumns(new String[] { messages.selectFromList() });

	}

	@Override
	public Object getGridColumnValue(T obj, int index) {
		if (!flag) {
			flag = true;
			return getAvailableGridItemsColumnValue(obj, index);
		} else {
			flag = false;
			return getSelectedGridItemsColumnValue(obj, index);
		}

	}

	private Object getSelectedGridItemsColumnValue(T obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();

		default:
			return null;
		}
	}

	private Object getAvailableGridItemsColumnValue(T obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();

		default:
			return null;
		}
	}

	public void setCallBack(ActionCallback<ArrayList<T>> callback) {
		this.callback = callback;
	}

	@Override
	protected boolean onOK() {
		callback.actionResult(tempSelectedItemsList);
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}
