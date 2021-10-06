package com.nitya.accounter.web.client.ui.company.options;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientLocation;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.LabelItem;

public class LocationTrackingOption extends AbstractPreferenceOption {
	LabelItem radioButtonsHeaderLabel;

	CheckboxItem locationTrackingCheckBoxItm;

	SelectCombo locationTermSelectCombo;

	LabelItem locationTrackingDescriptionLabel;

	public String[] locationsList = { messages.location(),
			messages.buisiness(), messages.department(), messages.division(),
			messages.property(), messages.store(), messages.territory() };

	public LocationTrackingOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.locationTracking(Global.get().Location());
	}

	@Override
	public String getAnchor() {
		return messages.locationTracking(Global.get().Location());
	}

	interface LocationTrackingPageUiBinder extends
			UiBinder<Widget, LocationTrackingOption> {
	}

	public void initData() {
		long locationType = getCompanyPreferences().getLocationTrackingId();
		locationTermSelectCombo.setComboItem(locationsList[(int) locationType]);

		// switch ((int) locationType) {
		// case ClientLocation.BUSINESS:
		// buisinessRadioButton.setValue(true);
		// break;
		// case ClientLocation.DEPARTMENT:
		// departmentRadioButton.setValue(true);
		// break;
		// case ClientLocation.DIVISION:
		// divisionRadioButton.setValue(true);
		// break;
		// case ClientLocation.LOCATION:
		// locationRadioButton.setValue(true);
		// break;
		// case ClientLocation.PROPERTY:
		// propertyRadioButton.setValue(true);
		// break;
		// case ClientLocation.STORE:
		// storeRadioButton.setValue(true);
		// break;
		// case ClientLocation.TERRITORY:
		// territoryRadioButton.setValue(true);
		// break;
		// }
		boolean isLocationEnabled = getCompanyPreferences()
				.isLocationTrackingEnabled();
		if (isLocationEnabled) {
			locationTrackingCheckBoxItm.setValue(true);
			locationTrackingCheckBoxItm.setTitle(messages
					.locationTracking(Global.get().Location()));
			locationTermSelectCombo.setVisible(locationTrackingCheckBoxItm
					.getValue());
		} else {
			locationTermSelectCombo.setVisible(locationTrackingCheckBoxItm
					.getValue());
		}

	}

	@Override
	public void onSave() {

		getCompanyPreferences().setLocationTrackingEnabled(
				locationTrackingCheckBoxItm.getValue());
		if (locationTermSelectCombo.getSelectedValue().equals(
				messages.buisiness())) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.BUSINESS);
		} else if (locationTermSelectCombo.getSelectedValue().equals(
				messages.department())) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.DEPARTMENT);
		} else if (locationTermSelectCombo.getSelectedValue().equals(
				messages.division())) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.DIVISION);
		} else if (locationTermSelectCombo.getSelectedValue().equals(
				messages.location())) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.LOCATION);
		} else if (locationTermSelectCombo.getSelectedValue().equals(
				messages.property())) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.PROPERTY);
		} else if (locationTermSelectCombo.getSelectedValue().equals(
				messages.store())) {
			getCompanyPreferences().setLocationTrackingId(ClientLocation.STORE);
		} else if (locationTermSelectCombo.getSelectedValue().equals(
				messages.territory())) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.TERRITORY);
		} else {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.LOCATION);
		}
	}

	@Override
	public void createControls() {
		locationTermSelectCombo = new SelectCombo(
				messages.useTerminologyFor(Global.get().Location()));
		locationTermSelectCombo.addStyleName("header");
		for (int i = 0; i < locationsList.length; i++) {

			locationTermSelectCombo.addItem(locationsList[i]);
		}
		locationTermSelectCombo.setComboItem(locationsList[0]);
		locationTrackingDescriptionLabel = new LabelItem(
				messages.locationTrackingDescription(),
				"locationTrackDescPanel");

		locationTrackingCheckBoxItm = new CheckboxItem(
				messages.locationTracking(Global.get().Location()), "header");
		locationTrackingCheckBoxItm.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (Accounter.hasPermission(Features.LOCATION)) {
					locationTermSelectCombo
							.setVisible(locationTrackingCheckBoxItm.getValue());
				} else {
					locationTrackingCheckBoxItm.setValue(false);
					Accounter.showSubscriptionWarning();
				}
			}
		});
		add(locationTrackingCheckBoxItm);
		add(locationTrackingDescriptionLabel);
		add(locationTermSelectCombo);
	}
}
