package com.nitya.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.StyledPanel;

public class SetupTDSSelectionPage extends AbstractSetupPage {

	private static SetupTDSSelectionPageUiBinder uiBinder = GWT
			.create(SetupTDSSelectionPageUiBinder.class);

	interface SetupTDSSelectionPageUiBinder extends
			UiBinder<Widget, SetupTDSSelectionPage> {
	}

	public SetupTDSSelectionPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@UiField
	StyledPanel viewPanel;
	@UiField
	RadioButton tdsYes;
	@UiField
	RadioButton tdsNo;
	@UiField
	Label headerLabel;

	public SetupTDSSelectionPage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.doYouWantTDS());
		tdsYes.setText(messages.yes());
		tdsNo.setText(messages.no());
	}

	@Override
	protected void onSave() {
		preferences.setTDSEnabled(tdsYes.getValue());

	}

	@Override
	protected void onLoad() {
		tdsNo.setValue(true);

	}

	@Override
	protected boolean validate() {

		return true;
	}

	@Override
	public String getViewName() {
		return messages.tds();
	}

}
