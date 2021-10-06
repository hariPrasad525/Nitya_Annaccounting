package com.vimukti.accounter.web.client.ui.win8;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Devaraju.K
 * 
 */
public class ForgotPasswordPanel extends FlowPanel {
	HTML accounterText, forgottenPassword;
	TextItem emailTxt;
	Button getNewPassword, login;
	WebsocketAccounterInitialiser accounter;
	Label errorlabel;

	public ForgotPasswordPanel(WebsocketAccounterInitialiser accounter) {
		getElement().setId("forgotPasswordPanel");
		this.accounter = accounter;
		createControls();
	}

	private void createControls() {
		forgottenPassword = new HTML("<h2>"
				+ Accounter.getMessages().forgottenPassword() + "</h2>");
		errorlabel = new Label();
		errorlabel.setStyleName("errors");
		accounterText = new HTML(Accounter.getMessages().forgotpasswordMsg());
		emailTxt = new TextItem(Accounter.getMessages().email(), "emailTxt");
		getNewPassword = new Button(Accounter.getMessages().getNewPassword());
		getNewPassword.setStyleName("success");
		final AccounterAsyncCallback<Boolean> accounterAsyncCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				getNewPassword.setEnabled(true);
				errorlabel.setText(exception.getMessage());

			}

			@Override
			public void onResultSuccess(Boolean result) {
				ActivationPanel activationPanel = new ActivationPanel(accounter);
				activationPanel.setForgotPassword(true);
				accounter.showView(activationPanel);

			}

		};
		getNewPassword.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorlabel.setText("");
				if (validate()) {
					getNewPassword.setEnabled(false);
					Accounter.createWindowsRPCService().forgotPassword(
							emailTxt.getValue(), accounterAsyncCallback);
				}
			}
		});
		login = new Button(Accounter.getMessages().cancel());
		login.setStyleName("cancel");
		login.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				accounter.showView(new LoginPanel(accounter));

			}
		});

		this.add(forgottenPassword);
		this.add(errorlabel);
		this.add(accounterText);
		this.add(emailTxt);
		this.add(getNewPassword);
		this.add(login);
	}

	private boolean validate() {
		if (emailTxt.getValue().isEmpty()) {
			errorlabel.setText(Accounter.getMessages().shouldNotEmpty());
			return false;
		} else {
			return true;
		}
	}
}
