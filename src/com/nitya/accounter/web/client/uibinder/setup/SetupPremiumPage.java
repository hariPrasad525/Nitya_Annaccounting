package com.nitya.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.ui.Accounter;

public class SetupPremiumPage extends AbstractSetupPage {

	private static SetupPremiumPageUiBinder uiBinder = GWT
			.create(SetupPremiumPageUiBinder.class);
	@UiField
	FlowPanel viewPanel;
	@UiField
	Label headerLabel;
	@UiField
	Label passwordLabel;
	@UiField
	Label conformPasswordLabel;
	@UiField
	Label encryptionInfo;
	@UiField
	CheckBox enableBox;
	@UiField
	PasswordTextBox passwordField;
	@UiField
	PasswordTextBox conformPasswordField;
	@UiField
	Label passwordInfo;
	@UiField
	Label hintLabel;
	@UiField
	TextBox hintField;
	@UiField
	Hyperlink moreInfoLink;
	private SetupWizard setupWizard;

	interface SetupPremiumPageUiBinder extends
			UiBinder<Widget, SetupPremiumPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 * 
	 * @param setupWizard
	 */
	public SetupPremiumPage(SetupWizard setupWizard) {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		this.setupWizard = setupWizard;
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.encryption());
		passwordLabel.setText(messages.companyPassword());
		conformPasswordLabel.setText(messages.confirmPassword());
		passwordField.setEnabled(false);
		conformPasswordField.setEnabled(false);
		hintLabel.setText(messages.passwordHint());
		hintField.setEnabled(false);
		encryptionInfo.setText(messages.encryptionEnablesyourCompanydata());
		encryptionInfo.setStyleName("organisation_comment");
		passwordInfo.setText(messages.encryptionPasswordisImportant());
		passwordInfo.setStyleName("organisation_comment");
		moreInfoLink.setText(messages.forMoreInfoOnEncryption());
		enableBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (enableBox.getValue()) {
					passwordField.setEnabled(true);
					conformPasswordField.setEnabled(true);
					hintField.setEnabled(true);
				} else {
					passwordField.setEnabled(false);
					conformPasswordField.setEnabled(false);
					hintField.setEnabled(false);
				}
			}
		});

	}

	@Override
	protected void onSave() {
		setupWizard.setPassword(enableBox.getValue() ? passwordField.getValue()
				: null);
		setupWizard.setPasswordHint(enableBox.getValue() ? hintField.getValue()
				: null);
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean validate() {
		if (enableBox.getValue()) {
			if (passwordField.getValue().length() < 6) {
				Accounter.showError(messages
						.passwordshouldcontainminimum6characters());
				return false;
			} else if (!passwordField.getValue().equals(
					conformPasswordField.getValue())) {
				Accounter.showError(messages.passwordsnotmatched());
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	public String getViewName() {
		return messages.encryption();
	}
}