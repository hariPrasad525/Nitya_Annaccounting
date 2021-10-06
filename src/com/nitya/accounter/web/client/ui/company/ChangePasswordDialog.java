package com.nitya.accounter.web.client.ui.company;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.PasswordItem;

public class ChangePasswordDialog extends BaseDialog {

	private PasswordItem oldPasswordTextItem, newPasswordTextItem,
			confirmNewPasswordTextItem;
	private DynamicForm textItemsForm;
	private StyledPanel mainPanel;
	private String oldPassword, newPassword, confirmNewPassword;
	private LabelItem openIdMsgLabel;

	public ChangePasswordDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("ChangePasswordDialog");
		createControls();
	}

	private void createControls() {
		openIdMsgLabel = new LabelItem("", "bold");
		openIdMsgLabel
				.setValue(messages
						.leaveOldPasswordEmptyIfYouSignedUpWithGoogleYahooAOLOrFacebook());
		oldPasswordTextItem = new PasswordItem(messages.oldPassword());
		newPasswordTextItem = new PasswordItem(messages.newPassword());
		confirmNewPasswordTextItem = new PasswordItem(
				messages.confirmNewPassword());
		// oldPasswordTextItem.setRequired(true);
		newPasswordTextItem.setRequired(true);
		confirmNewPasswordTextItem.setRequired(true);

		textItemsForm = new DynamicForm("textItemsForm");

		mainPanel = new StyledPanel("mainPanel");

		textItemsForm.add(openIdMsgLabel, oldPasswordTextItem,
				newPasswordTextItem, confirmNewPasswordTextItem);

		mainPanel.add(textItemsForm);
		okbtn.setText(messages.save());

		setBodyLayout(mainPanel);

	}

	protected boolean savePassword() {

		oldPassword = oldPasswordTextItem.getValue().toString();
		newPassword = newPasswordTextItem.getValue().toString();
		confirmNewPassword = confirmNewPasswordTextItem.getValue().toString();
		String emailID = Accounter.getUser().getEmail();

		if (!(newPassword.toString().length() < 6)) {
			if (newPassword.equals(confirmNewPassword)) {
				Accounter.createHomeService().changePassWord(emailID,
						oldPassword, newPassword,
						new AccounterAsyncCallback<Boolean>() {

							@Override
							public void onException(AccounterException caught) {

							}

							@Override
							public void onResultSuccess(Boolean result) {
								if (result) {
									removeFromParent();
									Accounter.showInformation(messages
											.passwordSuccessfullyChanged());
								} else {
									Accounter.showError(messages
											.youHaveEnteredWrongPassword());
								}
							}

						});
			} else {
				addError(this, messages.passwordsnotmatched());
				return false;
			}
		} else {
			addError(this, messages.passwordshouldcontainminimum6characters());
			return false;
		}
		return true;

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public ValidationResult validate() {
		return textItemsForm.validate();
	}

	@Override
	protected boolean onOK() {
		return savePassword();
	}

	@Override
	public void setFocus() {
		oldPasswordTextItem.setFocus();
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}
