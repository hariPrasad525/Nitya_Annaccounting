package com.vimukti.accounter.web.client.ui.win8;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.CompanyDetails;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AccounterInitialiser;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class LoginPanel extends FlowPanel {

	TextItem useritem;
	HTML signInItem;
	PasswordItem password;
	WebsocketAccounterInitialiser accounterInitialiser;
	Label errorlLabel;
	Button submitbutton;

	public LoginPanel(WebsocketAccounterInitialiser accounter) {
		getElement().setId("loginPanel");
		this.accounterInitialiser = accounter;
		createControls();

	}

	private void createControls() {
		StyledPanel mainPanel = new StyledPanel("main_loginpanel");
		signInItem = new HTML("<h2>" + Accounter.getMessages().signIn()
				+ "</h2>");
		useritem = new TextItem(Accounter.getMessages().email(), "useritem");
		password = new PasswordItem(Accounter.getMessages().password());
		errorlLabel = new Label();
		errorlLabel.setStyleName("errors");

		final AccounterAsyncCallback<ArrayList<CompanyDetails>> accounterAsyncCallback = new AccounterAsyncCallback<ArrayList<CompanyDetails>>() {

			@Override
			public void onException(AccounterException exception) {
				accounterInitialiser.hideProgress();
				submitbutton.setEnabled(true);
				errorlLabel.setText(exception.getMessage());
			}

			@Override
			public void onResultSuccess(ArrayList<CompanyDetails> result) {
				accounterInitialiser.hideProgress();
				accounterInitialiser.rememberCredentials(useritem.getValue(),
						password.getValue(),
						AccounterInitialiser.PASSWORD_CRED_RESOURCE);
				if (result.size() == 1) {
					loadCompany(result.get(0).getCompanyId());
					return;
				}
				accounterInitialiser.showView(new CompaniesPanel(result,
						accounterInitialiser));
			}

		};

		KeyPressListener listener = new KeyPressListener() {

			@Override
			public void onKeyPress(int keyCode) {
				if (KeyCodes.KEY_ENTER == keyCode) {
					submit(accounterAsyncCallback);
				}
			}
		};

		useritem.setKeyPressHandler(listener);

		password.setKeyPressHandler(listener);

		submitbutton = new Button(Accounter.getMessages().submit(),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						submit(accounterAsyncCallback);
					}
				});

		LinkItem signuplink = new LinkItem();
		signuplink.setValue("Dont have Accounter Id");
		LinkItem forgotPassword = new LinkItem();
		forgotPassword.setValue("Cannot access your account");
		mainPanel.add(signInItem);
		mainPanel.add(errorlLabel);
		mainPanel.add(useritem);
		mainPanel.add(password);
		mainPanel.add(signuplink);
		mainPanel.add(forgotPassword);

		mainPanel.add(submitbutton);
		add(mainPanel);

		signuplink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				accounterInitialiser.showView(new SignUpPanel(
						accounterInitialiser));

			}
		});

		forgotPassword.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				accounterInitialiser.showView(new ForgotPasswordPanel(
						accounterInitialiser));

			}
		});
	}

	protected void submit(
			AccounterAsyncCallback<ArrayList<CompanyDetails>> accounterAsyncCallback) {
		submitbutton.setEnabled(false);
		errorlLabel.setText("");
		if (validate()) {
			accounterInitialiser.showProgress();
			Accounter.createWindowsRPCService().login(useritem.getValue(),
					password.getValue(), false, accounterAsyncCallback);

		}
	}

	private void loadCompany(final long comId) {
		LoginPanel.this.removeFromParent();
		accounterInitialiser.loadCompany(comId);
	}

	private boolean validate() {
		if (useritem.getValue().isEmpty() || password.getValue().isEmpty()) {
			errorlLabel.setText(Accounter.getMessages().shouldNotEmpty());
			return false;
		} else {
			return true;
		}
	}
}
