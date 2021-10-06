package com.nitya.accounter.web.client.ui.win8;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.CompanyDetails;
import com.nitya.accounter.web.client.core.SignupDetails;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.AccounterInitialiser;
import com.nitya.accounter.web.client.ui.CoreUtils;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.URLLauncher;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.PasswordItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class SignUpPanel extends FlowPanel {
	
	TextItem firstname, lastname, emailid/* , confirmMailid, phone */;
	PasswordItem password, confirmPassword;
	SelectCombo countrySelect;
	SignupDetails signupDetails;
	DynamicForm signupForm;
	/* CheckBox agreeterms, newsLetters; */
	WebsocketAccounterInitialiser accounter;
	Label errorlLabel;
	Button submitbutton;

	public SignUpPanel(WebsocketAccounterInitialiser accounter) {
		getElement().setId("signUpPanel");
		this.accounter = accounter;
		createControls();
	}

	private void createControls() {
		errorlLabel = new Label();
		errorlLabel.setStyleName("errors");
		firstname = new TextItem(Accounter.getMessages().firstName(),
				"firstname");
		lastname = new TextItem(Accounter.getMessages().lastName(), "lastname");
		emailid = new TextItem(Accounter.getMessages().email(), "emailid");
		/*
		 * confirmMailid = new TextItem(Accounter.getMessages2().confirmEmail(),
		 * "confirmemailid");
		 */
		password = new PasswordItem(Accounter.getMessages().password());
		confirmPassword = new PasswordItem(Accounter.getMessages()
				.confirmPassword());
		/* phone = new TextItem(Accounter.getMessages().phone(), "phone"); */
		countrySelect = new SelectCombo(Accounter.getMessages().country());
		String[] countries = CoreUtils.getCountries();
		countrySelect.initCombo(Arrays.asList(countries));
		countrySelect.select(countries[0]);

		// agreeterms = new CheckBox();
		Anchor termaAndConditionsAnchor = new Anchor(Accounter.getMessages()
				.termsConditions());
		termaAndConditionsAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				URLLauncher launcher = GWT.create(URLLauncher.class);
				launcher.launch("http://app.annaccounting.com/site/termsandconditions");
			}
		});
		// newsLetters = new CheckBox();
		// newsLetters.setText(Accounter.getMessages().newsletter());
		// newsLetters.setValue(true);
		final AccounterAsyncCallback<Boolean> accounterAsyncCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				accounter.hideProgress();
				submitbutton.setEnabled(true);
				errorlLabel.setText(exception.getMessage());

			}

			@Override
			public void onResultSuccess(Boolean result) {
				accounter.hideProgress();
				accounter.rememberCredentials(emailid.getValue(),
						password.getValue(),
						AccounterInitialiser.PASSWORD_CRED_RESOURCE);
				accounter.showView(new CompaniesPanel(
						new ArrayList<CompanyDetails>(), accounter));

			}

		};
		submitbutton = new Button(Accounter.getMessages().submit(),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						errorlLabel.setText("");
						/*
						 * if (!emailid.getValue()
						 * .equals(confirmMailid.getValue())) {
						 * errorlLabel.setText(Accounter.getMessages()
						 * .emailIdAndConfirmEmaildMustBeSame()); }
						 */
						if (validate()) {
							submitbutton.setEnabled(false);
							signupDetails = new SignupDetails();
							signupDetails.setFirstName(firstname.getValue());
							signupDetails.setLastName(lastname.getValue());
							signupDetails.setEmailId(emailid.getValue());
							signupDetails.setPassword(password.getValue());
							signupDetails.setConfirmPassword(confirmPassword
									.getValue());
							signupDetails.setCountry(countrySelect
									.getSelectedValue());
							signupDetails.setAcceptedTerms(true);
							signupDetails.setSubscribeUpdates(true);
							accounter.showProgress();
							Accounter.createWindowsRPCService().signup(
									signupDetails, accounterAsyncCallback);

						}
					}
				});
		submitbutton.setStyleName("success");
		Button cancelButton = new Button(Accounter.getMessages().cancel(),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						accounter.showView(new LoginPanel(accounter));
					}
				});
		cancelButton.setStyleName("cancel");

		StyledPanel stylPanel = new StyledPanel("headernFooter");

		StyledPanel centerPanel = new StyledPanel("centerPanel");

		StyledPanel termsConditionsPanel = new StyledPanel(
				"termsConditionsPanel");
		termsConditionsPanel.add(termaAndConditionsAnchor);

		signupForm = new DynamicForm("signupForm");
		signupForm.add(firstname, lastname, emailid, password, confirmPassword,
				countrySelect);
		centerPanel.add(signupForm);
		centerPanel.add(termsConditionsPanel);
		// centerPanel.add(newsLetters);
		centerPanel.add(submitbutton);
		centerPanel.add(cancelButton);

		add(stylPanel);
		add(new HTML("<h2>" + Accounter.getMessages().signUp() + "</h2>"));
		add(errorlLabel);
		add(centerPanel);
		add(stylPanel);

	}

	private boolean validate() {
		if (firstname.getValue().isEmpty() || lastname.getValue().isEmpty()
				|| emailid.getValue().isEmpty()
				|| password.getValue().isEmpty()
				|| confirmPassword.getValue().isEmpty()) {
			errorlLabel.setText(Accounter.getMessages().shouldNotEmpty());
			return false;
		} else {
			return true;
		}
	}
}
