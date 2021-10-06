package com.nitya.accounter.web.client.ui.company;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.ClientUserInfo;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.Header;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class EditProfileDialog extends BaseDialog<ClientUserInfo> {
	private TextItem firstNameTextItem;
	private TextItem lastNameTextItem;
	private StyledPanel mainPanel;
	private ClientUser clientUser;

	public EditProfileDialog(String editProfile, String desc) {
		super(editProfile, desc);
		this.getElement().setId("EditProfileDialog");
		clientUser = Accounter.getUser();
		createControls();
	}

	private void createControls() {
		firstNameTextItem = new TextItem(messages.firstName(),
				"firstNameTextItem");
		firstNameTextItem.setValue(clientUser.getFirstName());
		lastNameTextItem = new TextItem(messages.lastName(), "lastNameTextItem");
		lastNameTextItem.setValue(clientUser.getLastName());
		mainPanel = new StyledPanel("mainPanel");
		DynamicForm form = new DynamicForm("form");
		form.setStyleName("edit_user_profile");
		form.add(firstNameTextItem, lastNameTextItem);
		mainPanel.add(form);
		okbtn.setText(messages.save());
		setBodyLayout(mainPanel);

	}

	protected boolean saveEditedProfile() {
		// Send request to edit profile.
		String firstName = firstNameTextItem.getValue();
		String lastName = lastNameTextItem.getValue();
		if (firstName.isEmpty() || firstName == null || lastName.isEmpty()
				|| lastName == null) {
			addError(this, messages.nameShouldnotbeempty());
			return false;
		} else if (firstName.equals(clientUser.getFirstName())
				&& lastName.equals(clientUser.getLastName())) {
			addError(this, messages.bothnamessameasprevious());
			return false;
		} else {
			final ClientUserInfo userInfo = clientUser.toUserInfo();
			userInfo.setFirstName(firstName);
			userInfo.setLastName(lastName);
			userInfo.setFullName(firstName + " " + lastName);
			Accounter.createCRUDService().updateUser(userInfo,
					new AccounterAsyncCallback<Long>() {
						@Override
						public void onException(AccounterException caught) {

						}

						@Override
						public void onResultSuccess(Long result) {
							if (result != null) {
								removeFromParent();
								Accounter.showInformation(messages
										.updatedSuccessfully());
								Header.userName.setText(userInfo.getFullName());
								getCallback().actionResult(userInfo);
							} else {
								addError(this,
										messages.yourPresentPasswordisWrong());
							}
						}

					});

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
	protected boolean onOK() {
		return saveEditedProfile();
	}

	@Override
	public void setFocus() {
		firstNameTextItem.setFocus();
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}
