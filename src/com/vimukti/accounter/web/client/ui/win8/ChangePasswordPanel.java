package com.vimukti.accounter.web.client.ui.win8;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.CompanyDetails;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;

public class ChangePasswordPanel extends FlowPanel {
	Label accounterTitle;
	Label accounterText;
	PasswordItem newpasswordbox;
	PasswordItem conformPasswordbox;
	Button submitButton;
	Anchor login;
	WebsocketAccounterInitialiser accounter;
	Label errorlabel;
	private Label description;
	private HTML title;

	public ChangePasswordPanel(WebsocketAccounterInitialiser accounter) {
		getElement().setId("changePasswordPanel");
		this.accounter = accounter;
		createControls();
	}

	private void createControls() {
		errorlabel = new Label();
		accounterTitle = new Label();
		accounterTitle.setText(Accounter.getMessages().accounter());
		description = new Label();
		description.setText(Accounter.getMessages2().changePasswordDesc());
		accounterText = new Label(Accounter.getMessages().registeredEmailId());
		newpasswordbox = new PasswordItem(Accounter.getMessages().newPassword());
		conformPasswordbox = new PasswordItem(Accounter.getMessages()
				.confirmNewPassword());
		submitButton = new Button(Accounter.getMessages().submit());
		final AccounterAsyncCallback<ArrayList<CompanyDetails>> accounterAsyncCallback = new AccounterAsyncCallback<ArrayList<CompanyDetails>>() {

			@Override
			public void onException(AccounterException exception) {
				errorlabel.setText(exception.getMessage());

			}

			@Override
			public void onResultSuccess(ArrayList<CompanyDetails> result) {
				accounter.showView(new CompaniesPanel(result, accounter));

			}

		};
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				errorlabel.setText("");
				if (validate()) {
					Accounter.createWindowsRPCService().updateClient(
							newpasswordbox.getValue(),
							conformPasswordbox.getValue(),
							accounterAsyncCallback);
				}
			}
		});
		login = new Anchor(Accounter.getMessages().login());
		login.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				accounter.showView(new LoginPanel(accounter));

			}
		});

		title = new HTML("<h2>" + Accounter.getMessages().resetPassword()
				+ "</h2>");
		this.add(title);
		this.add(description);
		// this.add(accounterText);
		this.add(newpasswordbox);
		this.add(conformPasswordbox);
		this.add(errorlabel);
		this.add(submitButton);
	}

	private boolean validate() {
		errorlabel.setText("");
		if (newpasswordbox.getValue().isEmpty()
				|| conformPasswordbox.getValue().isEmpty()) {
			errorlabel.setText(Accounter.getMessages().shouldNotEmpty());
			return false;
		} else if (!newpasswordbox.getValue().equals(
				conformPasswordbox.getValue())) {
			errorlabel.setText(Accounter.getMessages().passwordsnotmatched());
			return false;
		} else {
			return true;
		}
	}
}
