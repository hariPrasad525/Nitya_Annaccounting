package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.EmployeesAndGroupsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class NewPayStructureView extends BaseView<ClientPayStructure> {

	private EmployeesAndGroupsCombo empsAndGroups;
	private EmployeesAndGroupsCombo copyFrom;
	private PayStructureTable grid;
	private AddNewButton itemTableButton;
	private StyledPanel mainVLay;

	public NewPayStructureView() {
		this.getElement().setId("NewPayStructureView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		Label lab1 = new Label(messages.payStructure());
		lab1.setStyleName("label-title");

		empsAndGroups = new EmployeesAndGroupsCombo(messages.employeeOrGroup(),
				"empsAndGroups");
		empsAndGroups.setEnabled(!isInViewMode());
		empsAndGroups.setRequired(true);
		empsAndGroups
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayStructureDestination>() {

					@Override
					public void selectedComboBoxItem(
							ClientPayStructureDestination selectItem) {
						getPayStructure(selectItem, true);
					}
				});

		copyFrom = new EmployeesAndGroupsCombo(messages.copyFrom(), "copyFrom");
		copyFrom.setEnabled(!isInViewMode());
		copyFrom.setVisible(!isInViewMode());
		copyFrom.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayStructureDestination>() {

			@Override
			public void selectedComboBoxItem(
					ClientPayStructureDestination selectItem) {
				if (selectItem == null) {
					return;
				}
				if (selectItem.equals(empsAndGroups.getSelectedValue())) {
					copyFrom.setComboItem(null);
					Accounter.showError(messages.copyFromShouldBeDiff());
					return;
				}
				getPayStructure(selectItem, false);
			}
		});

		grid = new PayStructureTable();
		grid.setEnabled(!isInViewMode());

		this.mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(empsAndGroups);
		mainVLay.add(copyFrom);
		Label payStrctTableTitle = new Label(messages2.table(messages
				.payStructure()));
		payStrctTableTitle.addStyleName("editTableTitle");
		StyledPanel itemPanel = new StyledPanel("payStrctTableContainer");
		itemPanel.add(payStrctTableTitle);
		itemPanel.add(grid);
		mainVLay.add(itemPanel);

		this.add(mainVLay);
	}

	protected void getPayStructure(ClientPayStructureDestination selectItem,
			final boolean isDuplicate) {
		Accounter.createPayrollService().getPayStructure(selectItem,
				new AsyncCallback<ClientPayStructure>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ClientPayStructure result) {
						if (result != null) {
							ArrayList<ClientPayStructureItem> items = new ArrayList<ClientPayStructureItem>();
							items.addAll(result.getItems());
							if (isDuplicate) {
								data.setID(result.getID());
								data.setEmployee(result.getEmployee());
								data.setEmployeeGroup(result.getEmployeeGroup());
								data.setItems(result.getItems());
							} else {
								for (ClientPayStructureItem item : items) {
									item.setPayStructure(0);
									item.setID(0);
								}
							}
							grid.initPayStructureItems(items);
						}
					}
				});
	}

	protected void addItem() {
		ClientPayStructureItem transactionItem = new ClientPayStructureItem();
		grid.add(transactionItem);

	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientPayStructure());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientPayStructure data) {
		long employee = data.getEmployee();
		if (employee == 0) {
			employee = data.getEmployeeGroup();
		}
		empsAndGroups.setEmpGroup(employee, data.getEmployee() != 0);

		List<ClientPayStructureItem> items = data.getItems();
		grid.initPayStructureItems(items);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.newPayee(messages.payStructure());
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		empsAndGroups.setFocus();
	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = data.getObjectType();
		this.rpcDoSerivce.canEdit(type, data.getID(), editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);

		empsAndGroups.setEnabled(!isInViewMode());
		grid.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		copyFrom.setEnabled(!isInViewMode());
		copyFrom.setVisible(!isInViewMode());
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {
		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();
		if (selectedValue instanceof ClientEmployee) {
			data.setEmployee(selectedValue.getID());
		} else {
			data.setEmployeeGroup(selectedValue.getID());
		}

		data.setItems(grid.getRows());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult validate = new ValidationResult();
		ClientPayStructureDestination selectedValue = empsAndGroups
				.getSelectedValue();
		if (selectedValue == null) {
			validate.addError(empsAndGroups,
					messages.pleaseSelect(messages.employeeGroup()));
			return validate;
		}

		validate = grid.validate(validate);
		List<ClientPayStructureItem> rows = grid.getRows();
		if (rows.isEmpty()) {
			validate.addError(grid, messages.noItemSelected());
		}
		return validate;

	}

	@Override
	protected void createButtons() {
		super.createButtons();
		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		addButton(mainVLay, itemTableButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(mainVLay, itemTableButton);
	}
}
