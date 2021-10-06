package com.nitya.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.GroupDialog;
import com.nitya.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.nitya.accounter.web.client.ui.core.InputDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

public class EmployeeGroupListDialog extends GroupDialog<ClientEmployeeGroup> {

	private InputDialog inputDlg;
	private ClientEmployeeGroup employeeGroup;
	List<ClientEmployeeGroup> employeeGroups;
	private GroupDialogButtonsHandler dialogButtonsHandler;
	protected ArrayList<ClientEmployeeGroup> list = new ArrayList<ClientEmployeeGroup>();

	public EmployeeGroupListDialog(String title, String descript) {
		super(title, descript);
		this.getElement().setId("EmployeeGroupListDialog");
		setWidth("380px");
		initialise();
		initList();
	}

	private void initList() {
		Accounter.createPayrollService().getEmployeeGroups(
				new AsyncCallback<ArrayList<ClientEmployeeGroup>>() {

					@Override
					public void onSuccess(ArrayList<ClientEmployeeGroup> result) {
						list = result;
						initGrid(list);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void initialise() {
		this.button1.setFocus(true);
		getGrid().setType(AccounterCoreType.EMPLOYEE_GROUP);
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientEmployeeGroup>() {

					@Override
					public boolean onRecordClick(ClientEmployeeGroup core,
							int column) {
						enableEditRemoveButtons(true);
						return true;
					}

				});

		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditGroupDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditGroupDialog((ClientEmployeeGroup) getSelectedRecord());
			}

			public void onThirdButtonClick() {
				if (listGridView != null) {
					deleteObject((IAccounterCore) listGridView.getSelection());
					if (employeeGroups == null) {
						enableEditRemoveButtons(false);
					}
				}
			}

		};

		addGroupButtonsHandler(dialogButtonsHandler);
		this.okbtn.setVisible(false);
	}

	protected void showAddEditGroupDialog(ClientEmployeeGroup rec) {
		employeeGroup = rec;
		inputDlg = new InputDialog(this, messages.employeeGroup(), "",
				messages.employeeGroup()) {
		};

		if (employeeGroup != null) {
			inputDlg.setTextItemValue(0, employeeGroup.getName());
		}
		ViewManager.getInstance().showDialog(inputDlg);
	}

	@Override
	public Object getGridColumnValue(ClientEmployeeGroup group, int index) {
		switch (index) {
		case 0:
			if (group != null)
				return group.getName();
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { messages.name() };
	}

	@Override
	protected List<ClientEmployeeGroup> getRecords() {
		return list;
	}

	@Override
	protected boolean onOK() {
		if (inputDlg != null) {
			if (employeeGroup != null) {
				editCustomerGroups();
				inputDlg = null;
			} else {
				createCustomerGroups();
				inputDlg = null;
			}
		}
		return true;
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (inputDlg != null) {
			String value = inputDlg.getTextItems().get(0).getValue().toString();
			ClientEmployeeGroup employeeGroupByName = Utility.getObjectByName(
					list, UIUtils.toStr(value));
			if (employeeGroup != null) {
				if (!(employeeGroup.getName().equalsIgnoreCase(value) ? true
						: employeeGroupByName == null)) {
					result.addError(this, messages.alreadyExist());
				}
			}
		} else {
			ClientEmployeeGroup employeeGroupByName2 = Utility.getObjectByName(
					list, inputDlg.getTextItems().get(0).getValue().toString());
			if (employeeGroupByName2 != null) {
				result.addError(this,
						messages.payeeGroupAlreadyExists(messages.employee()));
			}
		}
		return result;
	}

	private void editCustomerGroups() {
		employeeGroup.setName(inputDlg.getTextValueByIndex(0));
		saveOrUpdate(employeeGroup);
		for (ClientEmployeeGroup group : list) {
			if (group.getID() == employeeGroup.getID()) {
				int index = list.indexOf(group);
				list.remove(group);
				list.add(index, employeeGroup);
				break;
			}
		}
	}

	private void createCustomerGroups() {
		ClientEmployeeGroup group = new ClientEmployeeGroup();
		group.setName(inputDlg.getTextItems().get(0).getValue().toString());
		saveOrUpdate(group);
		list.add(group);
	}

	@Override
	public void setFocus() {

	}

	@Override
	public String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "name";

		default:
			break;
		}
		return null;
	}

	@Override
	public String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "name";

		default:
			break;
		}
		return null;
	}

}
