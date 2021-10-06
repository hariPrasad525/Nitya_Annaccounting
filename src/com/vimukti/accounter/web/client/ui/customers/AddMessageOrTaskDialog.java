package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMessageOrTask;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

public abstract class AddMessageOrTaskDialog extends
		BaseDialog<ClientMessageOrTask> {
	private static final int TYPE_MESSAGE = 1;
	private static final int TYPE_TASK = 2;
	private static final int TYPE_WARNING = 3;
	private RadioButton messageRadioButton, taskRadioBUtton,
			warningRadioButton;
	private ListBox toListBox;
	private DynamicForm messageAreaform;
	private TextAreaItem messageArea;
	private ValueCallBack<ClientMessageOrTask> successCallback;
	ArrayList<ClientUserInfo> usersList = getCompany().getUsersList();

	public AddMessageOrTaskDialog(String text) {
		super(text);
		createControls();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void createControls() {

		setText(messages.addNewmessageOrTask());
		messageRadioButton = new RadioButton("messageOrTask",
				messages.message());
		taskRadioBUtton = new RadioButton("messageOrTask", messages.task());
		warningRadioButton = new RadioButton("messageOrTask",
				messages.warning());
		DynamicForm messageOrTaskForm = new DynamicForm("messageOrTaskForm");
		messageOrTaskForm.add(messageRadioButton);
		messageRadioButton.setValue(true);
		messageOrTaskForm.add(taskRadioBUtton);
		messageOrTaskForm.add(warningRadioButton);
		setBodyLayout(messageOrTaskForm);

		DynamicForm toForm = new DynamicForm("toForm");
		Label label = new Label(messages.to());
		toListBox = new ListBox();
		toForm.add(label);
		toForm.add(toListBox);
		addDatatoToListBox();
		setBodyLayout(toForm);
		messageAreaform = new DynamicForm("messageAreaForm");
		messageArea = new TextAreaItem(messages.message(),
				"messageOrTaskTextareaitem");
		messageArea.setRequired(true);
		messageAreaform.add(messageArea);
		setBodyLayout(messageAreaform);
		// setWidth("275px");

	}

	private void addDatatoToListBox() {
		toListBox.addItem(messages.yourself());
		toListBox.addItem(messages.allUsers());
		for (ClientUserInfo clientUserInfo : usersList) {
			toListBox.addItem(clientUserInfo.getName());
		}
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	protected boolean onOK() {
		createMessageorTask();
		return true;
	}

	private void createMessageorTask() {
		ClientMessageOrTask clientMessageOrTask = new ClientMessageOrTask();
		int type = 1;
		if (messageRadioButton.getValue()) {
			type = TYPE_MESSAGE;
		} else if (taskRadioBUtton.getValue()) {
			type = TYPE_TASK;
		} else {
			type = TYPE_WARNING;
		}
		clientMessageOrTask.setType(type);
		clientMessageOrTask.setContent(messageArea.getValue());
		clientMessageOrTask.setDate(new ClientFinanceDate().getDate());
		clientMessageOrTask.setToUser(setUsersListToObj());
		AccounterAsyncCallback<Long> callback = new AccounterAsyncCallback<Long>() {

			@Override
			public void onException(AccounterException exception) {

			}

			@Override
			public void onResultSuccess(Long result) {
				AddMessageOrTaskDialog.this.onSuccess();
			}

		};

		Accounter.createCRUDService().create(clientMessageOrTask, callback);

	}

	protected abstract void onSuccess();

	private long setUsersListToObj() {
		String value = toListBox.getValue(toListBox.getSelectedIndex());
		if (value.equals(messages.yourself())) {
			return Accounter.getUser().getID();
		} else if (value.equals(messages.allUsers())) {
			return ClientMessageOrTask.TO_USER_TYPE_ALL;
		} else {
			for (ClientUserInfo clientUser : usersList) {
				if (clientUser.getName().equals(value)) {
					return clientUser.getID();
				}
			}

		}
		return Accounter.getUser().getID();
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult validate = messageAreaform.validate();
		return validate;
	}
}
