package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.*;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CreateClassDialog;
import com.nitya.accounter.web.client.ui.ISaveCallback;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.InputDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;

/**
 * 
 * @author V.L.Pavani
 * 
 */

public class ManageSupportListView extends BaseListView<IAccounterCore> {

	private InputDialog inputDlg;
	private int coreType;
	private ArrayList<ClientEmployeeGroup> employeeGroups;

	public ManageSupportListView(int coreType) {
		super();
		this.coreType = coreType;
		this.getElement().setId("ManageSupportListView");
	}

	public void showAddEditGroupDialog(IAccounterCore rec) {
		if (coreType == IAccounterCore.ACCOUNTER_CLASS) {
			CreateClassDialog classDialog = new CreateClassDialog(
					(ClientAccounterClass) rec, messages.createClass(), "");
			classDialog
					.addSuccessCallback(new ValueCallBack<ClientAccounterClass>() {

						@Override
						public void execute(
								final ClientAccounterClass accounterClass) {

							Accounter.createOrUpdate(new ISaveCallback() {

								@Override
								public void saveSuccess(IAccounterCore object) {
									initListCallback();
								}

								@Override
								public void saveFailed(
										AccounterException exception) {
									Accounter.showError(exception.getMessage());
								}
							}, accounterClass);
						}
					});
		} else {
			String[] setColumns = setColumns();
			String[] itemNames = setColumns.length == 2 ? new String[] { setColumns[0] }
					: new String[] { setColumns[0], setColumns[1] };
			inputDlg = new InputDialog(this, getDialogueTitle(), itemNames);
			if (rec != null) {
				inputDlg.setTextItemValue(0, rec.getName());

				if (coreType == IAccounterCore.SHIPPING_TERMS) {
					ClientShippingTerms shippingTerms = (ClientShippingTerms) rec;
					inputDlg.setTextItemValue(1, shippingTerms.getDescription());
				}

				if (coreType == IAccounterCore.SHIPPING_METHOD) {
					ClientShippingMethod shippingTerms = (ClientShippingMethod) rec;
					inputDlg.setTextItemValue(1, shippingTerms.getDescription());
				}
			} else {
				if (grid != null) {
					grid.setSelection(null);
				}
			}

			ViewManager.getInstance().showDialog(inputDlg);
		}
	}

	private String getDialogueTitle() {
		if (coreType == IAccounterCore.VENDOR_GROUP) {
			return messages.payeeGroup(Global.get().Vendor());
		} else if (coreType == IAccounterCore.CUSTOMER_GROUP) {
			return messages.payeeGroup(Global.get().Customer());
		} else if (coreType == IAccounterCore.ITEM_GROUP) {
			return messages.itemGroup();
		} else if (coreType == IAccounterCore.SHIPPING_METHOD) {
			return messages.shippingMethod();
		} else if (coreType == IAccounterCore.SHIPPING_TERMS) {
			return messages.shippingTerms();
		} else if (coreType == IAccounterCore.ACCOUNTER_CLASS) {
			return messages.accounterClass();
		} else if (coreType == IAccounterCore.LOCATION) {
			return messages.locationsList(Global.get().Location());
		} else if (coreType == IAccounterCore.PRICE_LEVEL) {
			return messages.priceLevelList();
		} else if (coreType == IAccounterCore.EMPLOYEE_GROUP) {
			return messages.employeeGroupList();
		} else if (coreType == IAccounterCore.INVOICE_FREQUENCY) {
			return messages2.invoiceGroup();
		}
		return null;
	}

	public Object getGridColumnValue(IAccounterCore obj, int index) {
		switch (index) {
		case 0:
			if (obj != null) {
				return obj.getName();
			}
		case 1:
			if (coreType == IAccounterCore.SHIPPING_TERMS) {
				ClientShippingTerms shippingTerms = (ClientShippingTerms) obj;
				return shippingTerms.getDescription();
			} else if (coreType == IAccounterCore.SHIPPING_METHOD) {
				ClientShippingMethod shippingMethod = (ClientShippingMethod) obj;
				return shippingMethod.getDescription();
			}
			return Accounter.getFinanceImages().delete();
		case 2:
			return Accounter.getFinanceImages().delete();
		}
		return null;
	}

	public String[] setColumns() {
		if (coreType == IAccounterCore.SHIPPING_TERMS
				|| coreType == IAccounterCore.SHIPPING_METHOD) {
			return new String[] { messages.name(), messages.description(),
					messages.delete() };
		} else if (coreType == IAccounterCore.ACCOUNTER_CLASS) {
			return new String[] { messages.accounterClass(), messages.delete() };
		} else if (coreType == IAccounterCore.LOCATION) {
			return new String[] { messages.location(), messages.delete() };
		} else if (coreType == IAccounterCore.CURRENCY) {
			return new String[] { messages.currency(), messages.delete() };
		} else if (coreType == IAccounterCore.EMPLOYEE_GROUP) {
			return new String[] { messages.employeeGroup(), messages.delete() };
		}
		return new String[] { messages.name(), messages.delete() };
	}

	public String[] getHeaderStyle() {
		if (coreType == IAccounterCore.SHIPPING_TERMS
				|| coreType == IAccounterCore.SHIPPING_METHOD) {
			return new String[] { "name", "description", "delete" };
		}

		return new String[] { "name", "delete" };
	}

	public String[] getRowElementsStyle() {
		if (coreType == IAccounterCore.SHIPPING_TERMS
				|| coreType == IAccounterCore.SHIPPING_METHOD) {
			return new String[] { "nameValue", "descriptionValue",
					"deleteValue" };
		}
		return new String[] { "nameValue", "deleteValue" };
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (inputDlg != null) {
			String vendorName = inputDlg.getTextItems().get(0).getValue()
					.toString();
			IAccounterCore vendorByName = getObjectByName(vendorName);
			if (vendorByName != null) {
				IAccounterCore selection = (IAccounterCore) (grid == null ? null
						: grid.getSelection());
				if (selection == null
						|| vendorByName.getID() != selection.getID()) {
					result.addError(this, messages.alreadyExist());
				}
			}
		}
		return result;
	}

	private IAccounterCore getObjectByName(String vendorName) {
		if (coreType == IAccounterCore.VENDOR_GROUP) {
			return getCompany().getVendorGroupByName(vendorName);
		} else if (coreType == IAccounterCore.CUSTOMER_GROUP) {
			return getCompany().getCustomerGroupByName(vendorName);
		} else if (coreType == IAccounterCore.ITEM_GROUP) {
			return getCompany().getItemGroupByName(vendorName);
		} else if (coreType == IAccounterCore.SHIPPING_TERMS) {
			return getCompany().getShippingTermByName(vendorName);
		} else if (coreType == IAccounterCore.SHIPPING_METHOD) {
			return getCompany().getShippingMethodByName(vendorName);
		} else if (coreType == IAccounterCore.ACCOUNTER_CLASS) {
			ArrayList<ClientAccounterClass> accounterClasses = getCompany()
					.getAccounterClasses();
			for (ClientAccounterClass clientAccounterClass : accounterClasses) {
				if (clientAccounterClass.getName().equals(vendorName)) {
					return clientAccounterClass;
				}
			}
		} else if (coreType == IAccounterCore.LOCATION) {
			return getCompany().getLocationByName(vendorName);
		} else if (coreType == IAccounterCore.PRICE_LEVEL) {
			return getCompany().getPriceLevelByName(vendorName);
		} else if (coreType == IAccounterCore.CURRENCY) {
			return getCompany().getCurrency(vendorName);
		} else if (coreType == IAccounterCore.INVOICE_FREQUENCY) {
			return getCompany().getInvoiceFrequencyGroupByName(vendorName);
		} else if (coreType == IAccounterCore.EMPLOYEE_GROUP) {
			List<ClientEmployeeGroup> groups = getEmployeeGroups();
			for (ClientEmployeeGroup group : groups) {
				if (group.getName().equals(vendorName)) {
					return group;
				}
			}
		}
		return null;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void updateInGrid(IAccounterCore objectTobeModified) {

	}

	@Override
	public void initListCallback() {
		grid.removeAllRecords();
		grid.addRecords(getRecords());
	}

	private List<?> getRecords() {
		if (coreType == IAccounterCore.VENDOR_GROUP) {
			return getCompany().getVendorGroups();
		} else if (coreType == IAccounterCore.CUSTOMER_GROUP) {
			return getCompany().getCustomerGroups();
		} else if (coreType == IAccounterCore.ITEM_GROUP) {
			return getCompany().getItemGroups();
		} else if (coreType == IAccounterCore.SHIPPING_TERMS) {
			return getCompany().getShippingTerms();
		} else if (coreType == IAccounterCore.SHIPPING_METHOD) {
			return getCompany().getShippingMethods();
		} else if (coreType == IAccounterCore.ACCOUNTER_CLASS) {
			return getCompany().getAccounterClasses();
		} else if (coreType == IAccounterCore.LOCATION) {
			return getCompany().getLocations();
		} else if (coreType == IAccounterCore.PRICE_LEVEL) {
			return getCompany().getPriceLevels();
		} else if (coreType == IAccounterCore.CURRENCY) {
			return getCompany().getCurrencies();
		} else if (coreType == IAccounterCore.EMPLOYEE_GROUP) {
			return getEmployeeGroups();
		} else if (coreType == IAccounterCore.INVOICE_FREQUENCY) {
			return getCompany().getInvoiceFrequencyGroups();
		}
		return null;
	}

	private List<ClientEmployeeGroup> getEmployeeGroups() {
		if (employeeGroups == null) {
			Accounter.createPayrollService().getEmployeeGroups(
					new AsyncCallback<ArrayList<ClientEmployeeGroup>>() {

						@Override
						public void onSuccess(
								ArrayList<ClientEmployeeGroup> result) {
							if (result == null) {
								result = new ArrayList<ClientEmployeeGroup>();
							}
							grid.addRecords(result);
							employeeGroups = result;
						}

						@Override
						public void onFailure(Throwable caught) {

						}
					});
			return new ArrayList<ClientEmployeeGroup>();
		}

		return employeeGroups;
	}

	@Override
	protected void initGrid() {
		grid = new BaseListGrid<IAccounterCore>(false) {

			@Override
			protected int[] setColTypes() {
				if (coreType == IAccounterCore.SHIPPING_TERMS
						|| coreType == IAccounterCore.SHIPPING_METHOD) {
					return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
							COLUMN_TYPE_IMAGE };
				}
				return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_IMAGE };
			}

			@Override
			protected String[] setHeaderStyle() {
				return ManageSupportListView.this.getHeaderStyle();
			}

			@Override
			protected String[] setRowElementsStyle() {
				return ManageSupportListView.this.getRowElementsStyle();
			}

			@Override
			protected void executeDelete(IAccounterCore object) {
				deleteObject(object);
			}

			@Override
			protected Object getColumnValue(IAccounterCore obj, int index) {
				return ManageSupportListView.this
						.getGridColumnValue(obj, index);
			}

			@Override
			public void onDoubleClick(IAccounterCore obj) {
				showAddEditGroupDialog(obj);
			}

			@Override
			protected String[] getColumns() {
				return ManageSupportListView.this.setColumns();
			}

			@Override
			protected void onClick(IAccounterCore obj, int row, int col) {
				if (((coreType != IAccounterCore.SHIPPING_TERMS && coreType != IAccounterCore.SHIPPING_METHOD) && col == 1)
						|| ((coreType == IAccounterCore.SHIPPING_TERMS || coreType == IAccounterCore.SHIPPING_METHOD) && col == 2)) {
					showWarnDialog(obj);
				} else {
					onDoubleClick(obj);
				}
				super.onClick(obj, row, col);
			}

			@Override
			protected int getCellWidth(int index) {
				switch (index) {
				case 1:
					if (coreType != IAccounterCore.SHIPPING_TERMS
							&& coreType != IAccounterCore.SHIPPING_METHOD) {
						return 40;
					}
					break;
				case 2:
					if (coreType == IAccounterCore.SHIPPING_TERMS
							|| coreType == IAccounterCore.SHIPPING_METHOD) {
						return 40;
					}
					break;
				default:
					break;
				}
				return -1;
			}
		};
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		if (coreType == IAccounterCore.VENDOR_GROUP) {
			return messages.manageVendorGroup(Global.get().Vendors());
		} else if (coreType == IAccounterCore.CUSTOMER_GROUP) {
			return messages.manageCustomerGroup(Global.get().Customers());
		} else if (coreType == IAccounterCore.ITEM_GROUP) {
			return messages.itemGroupList();
		} else if (coreType == IAccounterCore.SHIPPING_TERMS) {
			return messages.shippingTermList();
		} else if (coreType == IAccounterCore.SHIPPING_METHOD) {
			return messages.shippingMethodList();
		} else if (coreType == IAccounterCore.ACCOUNTER_CLASS) {
			return messages.accounterClassList();
		} else if (coreType == IAccounterCore.LOCATION) {
			return messages.locationsList(Global.get().Location());
		} else if (coreType == IAccounterCore.PRICE_LEVEL) {
			return messages.priceLevelList();
		} else if (coreType == IAccounterCore.CURRENCY) {
			return messages.currencyList();
		} else if (coreType == IAccounterCore.EMPLOYEE_GROUP) {
			return messages.employeeGroupList();
		} else if (coreType == IAccounterCore.INVOICE_FREQUENCY) {
			return messages2.invoiceGroup();
		}
		return null;

	}

	@Override
	protected Action getAddNewAction() {
		showAddEditGroupDialog(null);
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (coreType == IAccounterCore.VENDOR_GROUP) {
			return messages.addaNew(messages.payeeGroup(Global.get().Vendor()));
		} else if (coreType == IAccounterCore.CUSTOMER_GROUP) {
			return messages.addaNew(messages
					.payeeGroup(Global.get().Customer()));
		} else if (coreType == IAccounterCore.ITEM_GROUP) {
			return messages.addaNew(messages.itemGroup());
		} else if (coreType == IAccounterCore.SHIPPING_TERMS) {
			return messages.addaNew(messages.shippingTerms());
		} else if (coreType == IAccounterCore.SHIPPING_METHOD) {
			return messages.addaNew(messages.shippingMethod());
		} else if (coreType == IAccounterCore.ACCOUNTER_CLASS) {
			return messages.addaNew(messages.accounterClass());
		} else if (coreType == IAccounterCore.LOCATION) {
			return messages.addaNew(messages.location());
		} else if (coreType == IAccounterCore.PRICE_LEVEL) {
			return messages.addaNew(messages.priceLevel());
		} else if (coreType == IAccounterCore.CURRENCY) {
			return messages.addaNew(messages.currency());
		} else if (coreType == IAccounterCore.EMPLOYEE_GROUP) {
			return messages.addaNew(messages.employeeGroup());
		} else if (coreType == IAccounterCore.INVOICE_FREQUENCY) {
			return messages.addaNew(messages2.invoiceGroup());
		}
		return null;
	}

	@Override
	protected String getViewTitle() {
		return getListViewHeading();
	}

	public boolean onOK() {
		String name = inputDlg.getTextValueByIndex(0);
		IAccounterCore selection = grid == null ? null : (IAccounterCore) grid
				.getSelection();
		if (coreType == IAccounterCore.VENDOR_GROUP) {
			ClientVendorGroup clientVendorGroup = new ClientVendorGroup();
			if (selection != null) {
				clientVendorGroup = (ClientVendorGroup) selection;
			}
			clientVendorGroup.setName(name);
			selection = clientVendorGroup;
		} else if (coreType == IAccounterCore.CUSTOMER_GROUP) {
			ClientCustomerGroup clientCustomerGroup = new ClientCustomerGroup();
			if (selection != null) {
				clientCustomerGroup = (ClientCustomerGroup) selection;
			}
			clientCustomerGroup.setName(name);
			selection = clientCustomerGroup;
		} else if (coreType == IAccounterCore.ITEM_GROUP) {
			ClientItemGroup clientItemGroup = new ClientItemGroup();
			if (selection != null) {
				clientItemGroup = (ClientItemGroup) selection;
			}
			clientItemGroup.setName(name);
			selection = clientItemGroup;
		} else if (coreType == IAccounterCore.ACCOUNTER_CLASS) {
			ClientAccounterClass accounterClass = new ClientAccounterClass();
			if (selection != null) {
				accounterClass = (ClientAccounterClass) selection;
			}
			accounterClass.setClassName(name);
			selection = accounterClass;
		} else if (coreType == IAccounterCore.LOCATION) {
			ClientLocation accounterClass = new ClientLocation();
			if (selection != null) {
				accounterClass = (ClientLocation) selection;
			}
			accounterClass.setLocationName(name);
			selection = accounterClass;
		} else if (coreType == IAccounterCore.PRICE_LEVEL) {
			ClientPriceLevel accounterClass = new ClientPriceLevel();
			if (selection != null) {
				accounterClass = (ClientPriceLevel) selection;
			}
			accounterClass.setName(name);
			selection = accounterClass;
		} else if (coreType == IAccounterCore.SHIPPING_TERMS) {
			ClientShippingTerms shippingTerms = new ClientShippingTerms();
			if (selection != null) {
				shippingTerms = (ClientShippingTerms) selection;
			}
			String description = inputDlg.getTextValueByIndex(1);
			shippingTerms.setName(name);
			shippingTerms.setDescription(description);
			selection = shippingTerms;
		} else if (coreType == IAccounterCore.SHIPPING_METHOD) {
			ClientShippingMethod shippingMethod = new ClientShippingMethod();
			if (selection != null) {
				shippingMethod = (ClientShippingMethod) selection;
			}
			String description = inputDlg.getTextValueByIndex(1);
			shippingMethod.setName(name);
			shippingMethod.setDescription(description);
			selection = shippingMethod;
		} else if (coreType == IAccounterCore.EMPLOYEE_GROUP) {
			ClientEmployeeGroup group = new ClientEmployeeGroup();
			if (selection != null) {
				group = (ClientEmployeeGroup) selection;
			}
			group.setName(name);
			selection = group;
		} else if (coreType == IAccounterCore.INVOICE_FREQUENCY) {
			ClientInvoiceFrequencyGroup invoiceFrequencyGroup = new ClientInvoiceFrequencyGroup();
			if (selection != null) {
				invoiceFrequencyGroup = (ClientInvoiceFrequencyGroup) selection;
			}
			invoiceFrequencyGroup.setName(name);
			selection = invoiceFrequencyGroup;
		}

		saveOrUpdate(selection);
		return true;
	}

	@Override
	protected SelectCombo getSelectItem() {
		return null;
	}
}
