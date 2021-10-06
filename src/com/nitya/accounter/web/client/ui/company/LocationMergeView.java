package com.nitya.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientLocation;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.LocationCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.CancelButton;
import com.nitya.accounter.web.client.ui.core.SaveAndCloseButton;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class LocationMergeView extends BaseView<ClientLocation> {

	private DynamicForm form;
	private DynamicForm form1;

	private LocationCombo locationFromCombo;
	private LocationCombo locationToCombo;
	private TextItem locationFromIDTextItem;
	private TextItem locationToIDTextItem;

	private ClientLocation clientFromLocation;
	private ClientLocation clientToLocation;

	public LocationMergeView() {
		this.getElement().setId("LocationMergeView");
		clientToLocation = null;
		clientFromLocation = null;
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		Label lab1 = new Label(messages.mergeLocations());
		lab1.setStyleName("label-title");

		form = new DynamicForm("form");
		form1 = new DynamicForm("form1");
		StyledPanel layout = new StyledPanel("layout");
		StyledPanel layout1 = new StyledPanel("layout1");
		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		locationFromCombo = createFromLocationCombo();
		locationToCombo = createToLocationCombo();

		locationFromIDTextItem = new TextItem(messages.locationId(),
				"classIDTextItem");
		locationFromIDTextItem.setEnabled(false);

		locationToIDTextItem = new TextItem(messages.locationId(),
				"classIDTextItem1");
		locationToIDTextItem.setEnabled(false);

		locationFromCombo.setRequired(true);
		locationToCombo.setRequired(true);
		form.add(locationFromCombo, locationFromIDTextItem);
		form1.add(locationToCombo, locationToIDTextItem);
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(lab1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		this.add(horizontalPanel);

	}

	private LocationCombo createToLocationCombo() {

		locationToCombo = new LocationCombo(messages.mergeTo(), false);
		locationToCombo.setRequired(true);
		locationToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLocation>() {
					@Override
					public void selectedComboBoxItem(ClientLocation selectItem) {
						clientToLocation = selectItem;
						classToSelected(selectItem);
					}
				});
		return locationToCombo;
	}

	private LocationCombo createFromLocationCombo() {
		locationFromCombo = new LocationCombo(messages.mergeFrom(), false);
		locationFromCombo.setRequired(true);
		locationFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLocation>() {
					@Override
					public void selectedComboBoxItem(ClientLocation selectItem) {
						clientFromLocation = selectItem;
						classFromSelected(selectItem);
					}
				});
		return locationFromCombo;
	}

	private void classFromSelected(ClientLocation selectItem) {
		locationFromIDTextItem.setValue(String.valueOf(selectItem.getID()));
	}

	private void classToSelected(ClientLocation selectItem) {
		locationToIDTextItem.setValue(String.valueOf(selectItem.getID()));
	}

	public ValidationResult validate() {
		ValidationResult result = form.validate();
		if (clientFromLocation != null && clientToLocation != null) {
			if (clientFromLocation.getID() == clientToLocation.getID()) {
				result.addError(clientFromLocation,
						messages.notMove(messages.location()));
				return result;
			}
		}

		result.add(form1.validate());

		return result;

	}

	protected void mergeLocations() {

		if (clientToLocation != null && clientFromLocation != null) {
			if (clientToLocation.getID() == clientFromLocation.getID()) {
			}
		}
		Accounter.createHomeService().mergeLocation(clientFromLocation,
				clientToLocation, new AccounterAsyncCallback<Void>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError(exception.getMessage());
					}

					@Override
					public void onResultSuccess(Void result) {
						onClose();
					}
				});
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.mergeLocation();
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void saveAndUpdateView() {
		mergeLocations();
	}

	@Override
	protected void createButtons() {
		saveAndCloseButton = new SaveAndCloseButton(this);
		saveAndCloseButton.setText(messages.merge());
		saveAndCloseButton.getElement().setAttribute("data-icon", "remote");
		addButton(saveAndCloseButton);

		cancelButton = new CancelButton(this);
		addButton(cancelButton);
	}
}
