package com.nitya.accounter.setup.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.setup.client.core.AccountDetails;

public class AccountDetailsPage extends AbstractPage {

	private TextBox firstNameText;
	private TextBox lastnameText;
	private TextBox emailText;
	private TextBox confirmemailText;
	private PasswordTextBox pwdText;
	private PasswordTextBox confirmPwdText;

	@Override
	protected Widget createControls() {
		FlexTable table = new FlexTable();

		this.firstNameText = new TextBox();
		this.lastnameText = new TextBox();
		this.emailText = new TextBox();
		this.confirmemailText = new TextBox();
		this.pwdText = new PasswordTextBox();
		this.confirmPwdText = new PasswordTextBox();

		table.setWidget(0, 0, createTitle("First Name", true));
		table.setWidget(0, 1, firstNameText);
		table.setWidget(1, 0, createTitle("Last Name", true));
		table.setWidget(1, 1, lastnameText);
		table.setWidget(2, 0, createTitle("Email ID", true));
		table.setWidget(2, 1, emailText);
		table.setWidget(3, 0, createTitle("Confirm Email ID", true));
		table.setWidget(3, 1, confirmemailText);
		table.setWidget(4, 0, createTitle("Password", true));
		table.setWidget(4, 1, pwdText);
		table.setWidget(5, 0, createTitle("Confirm Password", true));
		table.setWidget(5, 1, confirmPwdText);
		return table;

	}

	@Override
	public int getPageNo() {
		return 3;
	}

	@Override
	public String getPageTitle() {
		return "Admin Account Details";
	}

	@Override
	public void validate(ValidationResult result) {
		if (firstNameText.getText() == null
				|| firstNameText.getText().trim().isEmpty()) {
			result.addError(firstNameText, "Please enter Firstname");
		}
		if (lastnameText.getText() == null
				|| lastnameText.getText().trim().isEmpty()) {
			result.addError(lastnameText, "Please enter Lastname");
		}
		if (emailText.getText() == null || emailText.getText().trim().isEmpty()) {
			result.addError(emailText, "Please enter EmailID");
		}
		if (confirmemailText.getText() != null
				&& emailText.getText() != null
				&& !confirmemailText.getText().trim()
						.equals(confirmemailText.getText().trim())) {
			result.addError(firstNameText,
					"EmailId and Confirm EmailId are not equal");
		}
		if (pwdText.getText() == null || pwdText.getText().trim().isEmpty()) {
			result.addError(pwdText, "Please enter password");
		}
		if (confirmPwdText.getText() != null
				&& pwdText.getText() != null
				&& !confirmPwdText.getText().trim()
						.equals(pwdText.getText().trim())) {
			result.addError(confirmPwdText,
					"Password and Confirm Password are not equal");
		}
	}

	@Override
	protected void savePage(final Callback<Boolean, Throwable> callback) {
		SetupHome.getSetupService().saveAccountDetails(getAccountDetails(),
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(Boolean result) {
						callback.onSuccess(result);
					}
				});

	}

	private AccountDetails getAccountDetails() {
		AccountDetails details = new AccountDetails();
		details.setFirstName(firstNameText.getText());
		details.setLastName(lastnameText.getText());
		details.setEmailId(emailText.getText());
		details.setPassword(pwdText.getText());
		return details;
	}

}
