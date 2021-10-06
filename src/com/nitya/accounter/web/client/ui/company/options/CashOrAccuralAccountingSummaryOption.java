package com.nitya.accounter.web.client.ui.company.options;

import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.RadioGroupItem;

public class CashOrAccuralAccountingSummaryOption extends
		AbstractPreferenceOption {

	LabelItem accountingMethodForSummaryLabel;

	RadioGroupItem cashOrAccuralRadioButton;

	StyledPanel mainpanel;

	public CashOrAccuralAccountingSummaryOption() {
		super("");
		createControls();
		initData();
	}

	public void initData() {

	}

	public void createControls() {
		accountingMethodForSummaryLabel.setValue(messages
				.getDefaultAccountingMethodForSummary());

		cashOrAccuralRadioButton = new RadioGroupItem();
		cashOrAccuralRadioButton.setGroupName("cashOrAccuralRadioButton");
		cashOrAccuralRadioButton.setShowTitle(false);

		cashOrAccuralRadioButton.setValueMap(messages.cashoraccural(),
				messages.cashoraccural());
		cashOrAccuralRadioButton.setDefaultValue(messages.cashoraccural());

		mainpanel = new StyledPanel("cashOrAccuralAccountingSummaryOption");
		add(mainpanel);

	}

	@Override
	public String getTitle() {
		return messages.cashOrAccural();
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		return messages.cashOrAccural();
	}

}
