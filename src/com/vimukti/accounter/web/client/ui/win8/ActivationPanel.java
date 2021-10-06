package com.vimukti.accounter.web.client.ui.win8;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.CompanyDetails;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class ActivationPanel extends FlowPanel {
	TextItem activationCode;
	Label errorlabel;
	WebsocketAccounterInitialiser accounter;
	boolean isForgotPassword;
	HTML title;
	private HTML accounterText;

	public ActivationPanel(WebsocketAccounterInitialiser accounter) {
		getElement().setId("activationPanel");
		this.accounter = accounter;
		createControls();
	}

	private void createControls() {
		title = new HTML("<h2>" + Accounter.getMessages().activationCode()
				+ "</h2>");
		errorlabel = new Label();
		errorlabel.setStyleName("errors");

		accounterText = new HTML(Accounter.getMessages()
				.pleaseentertheactivationcodeyougotinthemail());
		activationCode = new TextItem(
				Accounter.getMessages().validActivation(), "activationCode");
		add(title);
		add(errorlabel);
		add(accounterText);
		add(activationCode);
		final AccounterAsyncCallback<Boolean> accounterAsyncCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				errorlabel.setText(exception.getMessage());

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (isForgotPassword()) {
					accounter.showView(new ChangePasswordPanel(accounter));
				} else {
					accounter.showView(new CompaniesPanel(
							new ArrayList<CompanyDetails>(), accounter));
				}
			}

		};
		Button resendActivation = new Button(Accounter.getMessages()
				.resendActivationcode(), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorlabel.setText("");
				ResendActivationCodePanel resendActivationCodePanel = new ResendActivationCodePanel(
						accounter);
				resendActivationCodePanel
						.setisForgotPassword(isForgotPassword());
				accounter.showView(new ResendActivationCodePanel(accounter));
			}

		});

		Button cancelButton = new Button(Accounter.getMessages().cancel(),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						accounter.showView(new LoginPanel(accounter));
					}

				});
		cancelButton.setStyleName("cancel");

		resendActivation.setStyleName("success");
		add(resendActivation);
		Button submitbutton = new Button(Accounter.getMessages().submit(),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						errorlabel.setText("");
						if (validate()) {
							Accounter.createWindowsRPCService().activate(
									activationCode.getValue(),
									accounterAsyncCallback);
						}
					}

				});

		submitbutton.setStyleName("success");

		add(submitbutton);
		add(cancelButton);

	}

	private boolean validate() {
		if (activationCode.getValue().isEmpty()) {
			errorlabel.setText(Accounter.getMessages().shouldNotEmpty());
			return false;
		} else {
			return true;
		}
	}

	public boolean isForgotPassword() {
		return isForgotPassword;
	}

	public void setForgotPassword(boolean isForgotPassword) {
		this.isForgotPassword = isForgotPassword;
	}
}
