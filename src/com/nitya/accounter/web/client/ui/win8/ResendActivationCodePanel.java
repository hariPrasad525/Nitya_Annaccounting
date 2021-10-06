package com.nitya.accounter.web.client.ui.win8;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class ResendActivationCodePanel extends FlowPanel {
	HTML title;
	TextItem emailTxt;
	Button getNewActivCode;

	Anchor login;
	WebsocketAccounterInitialiser accounterInitialiser;
	Label errorlabel;
	private boolean isForgotPassword;
	private HTML accounterText;

	public ResendActivationCodePanel(WebsocketAccounterInitialiser accounter) {
		getElement().setId("resendActivationPanel");
		this.accounterInitialiser = accounter;
		createControls();
	}

	private void createControls() {
		errorlabel = new Label();
		title = new HTML("<h2>"
				+ Accounter.getMessages().resendActivationcode() + "</h2>");
		accounterText = new HTML(Accounter.getMessages().forgotpasswordMsg());
		emailTxt = new TextItem(Accounter.getMessages().email(), "emailTxt");
		getNewActivCode = new Button(Accounter.getMessages().submit());
		final AccounterAsyncCallback<Boolean> accounterAsyncCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				errorlabel.setText(exception.getMessage());

			}

			@Override
			public void onResultSuccess(Boolean result) {
				ActivationPanel activationPanel = new ActivationPanel(
						accounterInitialiser);
				activationPanel.setForgotPassword(isForgotPassword());
				accounterInitialiser.showView(activationPanel);

			}

		};
		getNewActivCode.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorlabel.setText("");
				if (validate()) {
					Accounter.createWindowsRPCService().forgotPassword(
							emailTxt.getValue(), accounterAsyncCallback);
				}
			}
		});
		login = new Anchor(Accounter.getMessages().login());
		login.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				accounterInitialiser.showView(new LoginPanel(
						accounterInitialiser));

			}
		});

		Button cancelButton = new Button(Accounter.getMessages().cancel(),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						accounterInitialiser.showView(new LoginPanel(
								accounterInitialiser));
					}

				});
		cancelButton.setStyleName("cancel");

		add(title);
		add(errorlabel);
		add(accounterText);
		add(emailTxt);
		add(getNewActivCode);
		add(cancelButton);
	}

	private boolean validate() {
		if (emailTxt.getValue().isEmpty()) {
			errorlabel.setText(Accounter.getMessages().shouldNotEmpty());
			return false;
		} else {
			return true;
		}
	}

	public void setisForgotPassword(boolean forgotPassword) {
		this.isForgotPassword = forgotPassword;
	}

	public boolean isForgotPassword() {
		return isForgotPassword;
	}

}
