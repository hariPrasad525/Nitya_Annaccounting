package com.nitya.accounter.web.client.ui.company.options;

import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.LabelItem;

public class JobTrackingOption extends AbstractPreferenceOption {

	CheckboxItem jobTrackingCheckBox;

	LabelItem jobTrackingdescriptionLabel;

	public JobTrackingOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.jobTracking();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setJobTrackingEnabled(
				jobTrackingCheckBox.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.jobTracking();
	}

	@Override
	public void createControls() {
		jobTrackingCheckBox = new CheckboxItem(messages.jobTracking(), "header");
		add(jobTrackingCheckBox);
	}

	@Override
	public void initData() {
		jobTrackingCheckBox.setValue(getCompanyPreferences()
				.isJobTrackingEnabled());
	}

}
