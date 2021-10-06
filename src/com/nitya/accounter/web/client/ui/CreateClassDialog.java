package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.combo.ClassListCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class CreateClassDialog extends BaseDialog<ClientAccounterClass> {

	DynamicForm trackClassForm;

	TextItem createClassTextItem, subclassTextItem;

	private ValueCallBack<ClientAccounterClass> successCallback;

	private ClientAccounterClass accounterClass, selectedClass;

	private CheckboxItem statusCheck;

	private ClassListCombo classListCombo;

	public CreateClassDialog(ClientAccounterClass accounterClass, String title,
			String desc) {
		super(title, desc);
		this.getElement().setId("CreateClassDialog");
		// setWidth("300px");
		this.accounterClass = accounterClass;
		initTrackClassDialog();
	}

	private void initTrackClassDialog() {
		StyledPanel verticalPanel = new StyledPanel("verticalPanel");

		createClassTextItem = new TextItem(messages.className(),
				"createClassTextItem");

		trackClassForm = new DynamicForm("trackClassForm");

		statusCheck = new CheckboxItem(messages.subclassof(), "status");

		statusCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				classListCombo.setEnabled(event.getValue());
			}
		});
		classListCombo = new ClassListCombo(" ", true);
		classListCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccounterClass>() {

					@Override
					public void selectedComboBoxItem(
							ClientAccounterClass selectItem) {
						selectedClass = selectItem;
					}
				});

		classListCombo
				.addNewAccounterClassHandler(new ValueCallBack<ClientAccounterClass>() {

					@Override
					public void execute(final ClientAccounterClass accouterClass) {
						selectedClass = accouterClass;
						Accounter.createCRUDService().create(accounterClass,
								new AsyncCallback<Long>() {

									@Override
									public void onSuccess(Long result) {
										selectedClass.setID(result);
										getCompany()
												.processUpdateOrCreateObject(
														selectedClass);
										classListCombo
												.setComboItem(selectedClass);
									}

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}
								});
					}
				});
		classListCombo.setEnabled(false);
		trackClassForm.add(createClassTextItem, statusCheck, classListCombo);
		if (accounterClass != null && !accounterClass.getClassName().isEmpty()) {
			createClassTextItem.setValue(accounterClass.getClassName());
			statusCheck.setValue(accounterClass.getParent() != 0);
			classListCombo.setEnabled(statusCheck.getValue());
			classListCombo.setComboItem(getCompany().getAccounterClass(
					accounterClass.getParent()));
		}
		verticalPanel.add(trackClassForm);

		setBodyLayout(verticalPanel);

		ViewManager.getInstance().showDialog(this);

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String className = createClassTextItem.getValue();
		if (className == null || className.trim().isEmpty()) {
			result.addError(createClassTextItem,
					messages.pleaseEnter(createClassTextItem.getTitle()));
		}

		ArrayList<ClientAccounterClass> accounterClasses = getCompany()
				.getAccounterClasses();
		if (statusCheck.getValue()) {
			ClientAccounterClass selectedValue = classListCombo
					.getSelectedValue();
			while (selectedValue != null) {
				if (createClassTextItem.getValue().equals(
						selectedValue.getClassName())) {
					result.addError(classListCombo,
							"You cannot make a class a subclass of itself.");
					break;
				}
				selectedValue = getCompany().getAccounterClass(
						selectedValue.getParent());
			}
		}
		for (ClientAccounterClass clientAccounterClass : accounterClasses) {
			if (accounterClass != null
					&& clientAccounterClass.getID() == this.accounterClass
							.getID()) {
				continue;
			}
			if (clientAccounterClass.getName().toLowerCase()
					.equalsIgnoreCase(className.toLowerCase())) {
				result.addError(createClassTextItem, messages.alreadyExist());
				break;
			}
		}
		return result;
	};

	@Override
	protected boolean onOK() {
		if (successCallback != null) {
			successCallback.execute(createAccounterClass());
		}
		return true;
	}

	private ClientAccounterClass createAccounterClass() {
		if (this.accounterClass == null) {
			accounterClass = new ClientAccounterClass();
		}
		accounterClass.setClassName(createClassTextItem.getValue());
		if (statusCheck.getValue()) {
			ClientAccounterClass parentclass = classListCombo
					.getSelectedValue();
			if (parentclass != null) {
				accounterClass.setParent(parentclass.getID());
			}
		} else {
			accounterClass.setParent(0);
		}
		return accounterClass;
	}

	public void addSuccessCallback(
			ValueCallBack<ClientAccounterClass> newClassHandler) {
		this.successCallback = newClassHandler;
	}

	@Override
	public void setFocus() {
		createClassTextItem.setFocus();

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
