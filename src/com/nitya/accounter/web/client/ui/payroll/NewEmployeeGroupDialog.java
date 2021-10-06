package com.nitya.accounter.web.client.ui.payroll;

import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class NewEmployeeGroupDialog extends BaseDialog<ClientEmployeeGroup> {

	private TextItem nameItem;
	private DynamicForm form;
	ClientEmployeeGroup group;
	private boolean fromEmployeeView;

	public NewEmployeeGroupDialog(String title, ClientEmployeeGroup data,
			boolean fromEmployeeView) {
		super(title);
		this.fromEmployeeView = fromEmployeeView;
		if (data == null) {
			group = new ClientEmployeeGroup();
		} else {
			group = data;
		}
		this.getElement().setId("NewEmployeeGroupDialog");
		createControls();
	}

	public void createControls() {
		nameItem = new TextItem(messages.employeeGroup(), "nameItem");
		nameItem.setValue(group.getName());
		form = new DynamicForm("form");
		nameItem.setRequired(true);
		form.add(nameItem);
		bodyLayout.add(form);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		return result;
	}

	@Override
	protected boolean onOK() {
		updateData();
		return false;
	}

	@Override
	protected boolean onCancel() {
		if (fromEmployeeView)
			return true;
		else
			return super.onCancel();
	}

	private void updateData() {
		group.setName(nameItem.getValue());
		saveOrUpdate(group);
		if (getCallback() != null) {
			getCallback().actionResult(group);
		}
	}

	@Override
	public void setFocus() {
		nameItem.setFocus();
	}

	@Override
	public void saveFailed(AccounterException exception) {
		String errorString = AccounterExceptions.getErrorString(exception);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		super.saveSuccess(object);
		this.removeFromParent();
		onCancel();
	}

}
