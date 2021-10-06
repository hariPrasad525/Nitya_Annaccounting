package com.nitya.accounter.web.client.ui.company.options;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;

public class TrackEstimatesOption extends AbstractPreferenceOption {

	LabelItem trackEstimateHeader;

	Label trackEstimateDescLabel;

	CheckboxItem yesOrNoRadioButton;

	RadioGroupItem inCludeOrNotRadoiGroup;

	CheckboxItem useDelayedCharges;

	StyledPanel mainPanel;

	StyledPanel hiddenPanel;

	interface TrackEstimatesOptionUiBinder extends
			UiBinder<Widget, TrackEstimatesOption> {
	}

	public TrackEstimatesOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public void createControls() {
		trackEstimateHeader = new LabelItem(messages.trackingEstimates(),
				"header");

		yesOrNoRadioButton = new CheckboxItem(messages.trackingEstimates(),
				"yesnobutton");
		// yesOrNoRadioButton.setShowTitle(false);
		// yesOrNoRadioButton.setValueMap(messages.yes(), messages.no());
		// yesOrNoRadioButton.setDefaultValue(messages.no());

		inCludeOrNotRadoiGroup = new RadioGroupItem("", "doincludeaccepted");
		inCludeOrNotRadoiGroup.setGroupName("inCludeOrNotRadoiGroup");
		inCludeOrNotRadoiGroup.setShowTitle(false);
		inCludeOrNotRadoiGroup.setValueMap(
				messages.dontWantToIncludeEstimates(),
				messages.includeAcceptedEstimates(),
				messages.includePendingAndAcceptedEstimates());
		inCludeOrNotRadoiGroup.setDefaultValue(messages
				.includePendingAndAcceptedEstimates());
		hiddenPanel = new StyledPanel("estiamtesHiddenPanel");
		useDelayedCharges = new CheckboxItem(messages.delayedCharges(), "bold");
		useDelayedCharges.setVisible(Accounter
				.hasPermission(Features.CREDITS_CHARGES));
		hiddenPanel.setVisible(getCompanyPreferences().isDoyouwantEstimates());

		yesOrNoRadioButton.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (yesOrNoRadioButton.getValue()) {
					hiddenPanel.setVisible(true);
				} else {
					hiddenPanel.setVisible(false);
					inCludeOrNotRadoiGroup.setValue(messages
							.includePendingAndAcceptedEstimates());

				}
			}
		});
		mainPanel = new StyledPanel("estimatesMainPanel");
		mainPanel.add(trackEstimateHeader);
		mainPanel.add(yesOrNoRadioButton);

		hiddenPanel.add(inCludeOrNotRadoiGroup);
		mainPanel.add(hiddenPanel);
		mainPanel.add(useDelayedCharges);
		add(mainPanel);

	}

	@Override
	public String getAnchor() {
		return messages.trackingEstimates();
	}

	@Override
	public String getTitle() {
		return messages.trackingEstimates();
	}

	@Override
	public void initData() {
		if (getCompanyPreferences().isDoyouwantEstimates()) {
			yesOrNoRadioButton.setValue(true);
		} else {
			yesOrNoRadioButton.setValue(false);
		}

		if (getCompanyPreferences().isDoyouwantEstimates()) {
			if (getCompanyPreferences().isDontIncludeEstimates()) {
				inCludeOrNotRadoiGroup.setValue(messages
						.dontWantToIncludeEstimates());
			} else if (getCompanyPreferences().isIncludeAcceptedEstimates()) {
				inCludeOrNotRadoiGroup.setValue(messages
						.includeAcceptedEstimates());
			} else if (getCompanyPreferences()
					.isIncludePendingAcceptedEstimates()) {
				inCludeOrNotRadoiGroup.setValue(messages
						.includePendingAndAcceptedEstimates());
			}
		}

		useDelayedCharges.setValue(getCompanyPreferences()
				.isDelayedchargesEnabled());
	}

	@Override
	public void onSave() {
		boolean isTracking = yesOrNoRadioButton.getValue();
		getCompanyPreferences().setDoyouwantEstimates(isTracking);
		getCompanyPreferences().setDelayedchargesEnabled(
				useDelayedCharges.getValue());
		if (!isTracking) {
			getCompanyPreferences().setDontIncludeEstimates(false);
			getCompanyPreferences().setIncludeAcceptedEstimates(false);
			getCompanyPreferences().setIncludePendingAcceptedEstimates(false);
		}
		if (isTracking) {
			if (inCludeOrNotRadoiGroup.getValue().equals(
					messages.dontWantToIncludeEstimates())) {
				getCompanyPreferences().setDontIncludeEstimates(true);
				getCompanyPreferences().setIncludeAcceptedEstimates(false);
				getCompanyPreferences().setIncludePendingAcceptedEstimates(
						false);
			} else if (inCludeOrNotRadoiGroup.getValue().equals(
					messages.includeAcceptedEstimates())) {
				getCompanyPreferences().setIncludeAcceptedEstimates(true);
				getCompanyPreferences().setDontIncludeEstimates(false);
				getCompanyPreferences().setIncludePendingAcceptedEstimates(
						false);
			} else if (inCludeOrNotRadoiGroup.getValue().equals(
					messages.includePendingAndAcceptedEstimates())) {
				getCompanyPreferences()
						.setIncludePendingAcceptedEstimates(true);
				getCompanyPreferences().setDontIncludeEstimates(false);
				getCompanyPreferences().setIncludeAcceptedEstimates(false);
			}
		}
	}

}
