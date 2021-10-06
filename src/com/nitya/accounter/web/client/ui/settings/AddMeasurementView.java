package com.nitya.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.AddNewButton;
import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.core.ClientUnit;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.edittable.tables.UnitsTable;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class AddMeasurementView extends BaseView<ClientMeasurement> {

	private TextItem nameItem;
	private TextAreaItem description;
	private UnitsTable unitsTable;
	private AddNewButton addUnitButton;
	private DynamicForm addMeasurmentForm;
	private AccounterMessages settingsMessages = messages;
	private StyledPanel unitsPanel;

	public AddMeasurementView() {

	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("AddMeasurementView");
		createControls();

	}

	@Override
	public void initData() {
		if (data == null) {
			data = new ClientMeasurement();
		} else {
			initMeasurementData(getData());
		}
		super.initData();
	}

	private void initMeasurementData(ClientMeasurement measurement) {

		nameItem.setValue(measurement.getName());
		description.setValue(measurement.getDesctiption());

		int row = 0;
		for (ClientUnit clientUnit : data.getUnits()) {
			if (clientUnit.isEmpty()) {
				continue;
			}
			if (clientUnit.isDefault()) {
				unitsTable.add(clientUnit);
				unitsTable.checkColumn(row, 0, true);
			} else {
				unitsTable.add(clientUnit);
			}
			row++;
		}

	}

	private void createControls() {
		Label lab1 = new Label(messages.measurement());
		lab1.setStyleName("label-title");

		StyledPanel panel = new StyledPanel("panel");
		panel.add(lab1);
		addMeasurmentForm = new DynamicForm("addMeasurmentForm");
		nameItem = new TextItem(settingsMessages.measurementName(), "nameItem");
		nameItem.setRequired(true);
		nameItem.setEnabled(!isInViewMode());

		description = new TextAreaItem(settingsMessages.description(),
				"description");
		description.setDisabled(isInViewMode());

		unitsTable = new UnitsTable() {

			@Override
			protected boolean isInViewMode() {
				return AddMeasurementView.this.isInViewMode();
			}
		};
		unitsTable.setEnabled(!isInViewMode());

		unitsPanel = new StyledPanel("addnew_edit_panel");
		Label tableTitle = new Label(messages2.table(messages.units()));
		tableTitle.addStyleName("editTableTitle");
		unitsPanel.add(tableTitle);
		unitsPanel.add(unitsTable);

		addMeasurmentForm.add(nameItem, description);
		addMeasurmentForm.addStyleName("fields-panel");
		panel.add(addMeasurmentForm);
		panel.add(unitsPanel);
		this.add(panel);
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		result.add(addMeasurmentForm.validate());
		result.add(unitsTable.validate());

		return result;
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {

		data.setName(nameItem.getValue());
		data.setDesctiption(description.getValue());
		data.setUnits(unitsTable.getRecords());

	}

	@Override
	public void setFocus() {
		this.nameItem.setFocus();

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					enableFormItems();
				}
			}

			@Override
			public void onException(AccounterException exception) {
				Accounter.showError(exception.getMessage());
			}
		};
		this.rpcDoSerivce.canEdit(AccounterCoreType.MEASUREMENT, data.getID(),
				editCallback);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		nameItem.setEnabled(!isInViewMode());
		description.setDisabled(isInViewMode());
		unitsTable.setEnabled(!isInViewMode());
		addUnitButton.setEnabled(!isInViewMode());
	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		measurementSaveFailed(exception);
	}

	public void measurementSaveFailed(AccounterException exception) {
		String message = null;
		if (exception.getMessage() != null) {
			message = exception.getMessage();
		}
		if (exception.getErrorCode() != 0) {
			message = AccounterExceptions.getErrorString(exception);
		} else {
			message = messages.failedTransaction(messages.measurement());
		}

		Accounter.showError(message);

	}

	@Override
	protected void createButtons() {
		super.createButtons();
		addUnitButton = getAddUnitButton();

		addUnitButton.setEnabled(!isInViewMode());
		addButton(unitsPanel, addUnitButton);
	}

	private AddNewButton getAddUnitButton() {
		AddNewButton addUnitButton = new AddNewButton(settingsMessages.addUnitButton());
		addUnitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClientUnit clientUnit = new ClientUnit();
				if (unitsTable.getAllRows().isEmpty()) {
					clientUnit.setDefault(true);
					clientUnit.setFactor(1);
				}else{
					clientUnit.setFactor(1);
				}
				unitsTable.add(clientUnit);
			}
		});
		return addUnitButton;
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(unitsPanel, addUnitButton);
	}
}
