package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.MultiSelectionStringRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

public class CreateInviteAUserCommand extends AbstractCommand {

	private static final String FIRST_NAME = "First Name";
	private static final String LAST_NAME = "Last Name";
	private static final String EMAIL = "E-mail";
	private static final String LEVEL_ACCESS = "accessLevel";
	private static final String CUSTOM_REQ = "customReq";
	ClientUser user;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(final List<Requirement> list) {
		list.add(new NameRequirement(FIRST_NAME, getMessages().pleaseEnter(
				getMessages().firstName()), getMessages().firstName(), false,
				true));

		list.add(new NameRequirement(LAST_NAME, getMessages().pleaseEnter(
				getMessages().lastName()), getMessages().lastName(), false,
				true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), false, true) {
			@Override
			public void setValue(Object value) {
				String emailId = (String) value;
				if (emailId == null) {
					return;
				} else if (CreateInviteAUserCommand.this.isUserExists(emailId)) {
					addFirstMessage(getMessages().userExistsWithThisMailId());
					return;
				}
				addFirstMessage(getMessages()
						.pleaseEnter(getMessages().email()));
				super.setValue(value);
			}
		});

		list.add(new StringListRequirement(LEVEL_ACCESS, getMessages()
				.pleaseEnter(getMessages().permissions()), getMessages()
				.permissions(), true, true, new ChangeListner<String>() {

			@Override
			public void onSelection(String value) {
				if (!value.equals(getMessages().custom())) {
					CreateInviteAUserCommand.this.get(CUSTOM_REQ)
							.setValue(null);
				}
			}
		}) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().permissions());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPermissions();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().permissions());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new MultiSelectionStringRequirement(CUSTOM_REQ, getMessages()
				.pleaseSelect(getMessages().customPermissions()), getMessages()
				.customPermissions(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (CreateInviteAUserCommand.this.get(LEVEL_ACCESS).getValue()
						.equals(getMessages().custom())) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

	}

	protected boolean isUserExists(String emailId) {
		if (user.getID() != 0 && emailId.equals(user.getEmail())) {
			return false;
		}
		User userByUserEmail = getCompany().getUserByUserEmail(emailId);
		if (userByUserEmail != null) {
			return true;
		}
		return false;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Set<User> usersList = getCompany().getUsersList();
		boolean hasAnotherAdmin = false;
		for (User user : usersList) {
			if (user.isAdmin()
					&& !(user.getClient().getEmailId().equals(this.user
							.getEmail()))) {
				hasAnotherAdmin = true;
			}
		}

		if (user.getID() != 0) {
			if (hasAnotherAdmin == false)
				addFirstMessage(context, getMessages()
						.cannotCreateUserAsTheirIsNoUserWithAdminRole());
			return null;
		}
		String firstName = get(FIRST_NAME).getValue();
		String lastName = get(LAST_NAME).getValue();
		String email = get(EMAIL).getValue();
		String access = get(LEVEL_ACCESS).getValue();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		RolePermissions rolePermissions = getUserPermissions(access);
		user.setPermissions(getUserPermission(rolePermissions));

		user.setUserRole(rolePermissions.getRoleName());
		user.setCanDoUserManagement(rolePermissions.isCanDoUserManagement());
		if (user.getID() == 0) {
			inviteUser(user, context);
		} else {
			updateUser(user, context);
		}
		return null;

	}

	private RolePermissions getUserPermissions(String access) {
		RolePermissions rolePermissions = null;
		if (access.equals(RolePermissions.READ_ONLY)) {
			rolePermissions = getReadOnlyPermission();

		} else if (access.equals(RolePermissions.FINANCIAL_ADVISER)) {
			// if FinancialAdvisor is selected, get corresponding
			// RolePermissions
			rolePermissions = getFinancialAdviserPermission();

		} else if (access.equals(RolePermissions.ADMIN)) {
			rolePermissions = getAdminPermission();
		} else if (access.equals(RolePermissions.CUSTOM)) {
			rolePermissions = getCustomPermission();
		}
		return rolePermissions;
	}

	private RolePermissions getCustomPermission() {
		RolePermissions custom = new RolePermissions();
		custom.setRoleName(RolePermissions.CUSTOM);
		List<String> value = get(CUSTOM_REQ).getValue();
		for (String string : value) {
			if (string.equals(getMessages().createInvoicesAndBills())) {
				custom.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
			} else if (string.equals(getMessages().billsAndPayments())) {
				custom.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
			} else if (string.equals(getMessages().bankingAndReconcialiation())) {
				custom.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
			} else if (string.equals(getMessages().changeCompanySettings())) {
				custom.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
			} else if (string.equals(getMessages().manageAccounts())) {
				custom.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
			} else if (string.equals(getMessages().manageUsers())) {
				custom.setCanDoUserManagement(true);
			} else if (string.equals(getMessages().viewReports())) {
				custom.setTypeOfViewReports(RolePermissions.TYPE_YES);
			} else if (string.equals(getMessages().inventoryWarehouse())) {
				custom.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
			} else if (string.equals(getMessages().Saveasdraft())) {
				custom.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);
			}
		}

		return custom;
	}

	private void updateUser(ClientUser user2, Context context) {
		try {
			String clientClassSimpleName = user.getObjectType()
					.getClientClassSimpleName();

			OperationContext opContext = new OperationContext(context
					.getCompany().getID(), user, context.getIOSession()
					.getUserEmail());
			opContext.setArg2(clientClassSimpleName);

			new FinanceTool().getUserManager().updateUser(opContext);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	private void inviteUser(ClientUser user, Context context) {
		try {
			String clientClassSimpleName = user.getObjectType()
					.getClientClassSimpleName();

			OperationContext opContext = new OperationContext(context
					.getCompany().getID(), user, context.getIOSession()
					.getUserEmail());
			opContext.setArg2(clientClassSimpleName);

			new FinanceTool().getUserManager().inviteUser(opContext);
		} catch (AccounterException e) {
			e.printStackTrace();
		}

	}

	private ClientUserPermissions getUserPermission(
			RolePermissions rolePermission) {
		ClientUserPermissions userPermissions = new ClientUserPermissions();

		userPermissions.setTypeOfBankReconcilation(rolePermission
				.getTypeOfBankReconcilation());
		userPermissions.setTypeOfInvoicesBills(rolePermission
				.getTypeOfInvoicesBills());
		userPermissions.setTypeOfPayBillsPayments(rolePermission
				.getTypeOfPayBillsPayments());
		userPermissions.setTypeOfCompanySettingsLockDates(rolePermission
				.getTypeOfCompanySettingsLockDates());
		userPermissions.setTypeOfViewReports(rolePermission
				.getTypeOfViewReports());
		userPermissions.setTypeOfManageAccounts(rolePermission
				.getTypeOfManageAccounts());
		userPermissions.setTypeOfInventoryWarehouse(rolePermission
				.getTypeOfInventoryWarehouse());
		// TODO
		// userPermissions.setTypeOfPublishReports(readOnlyPer.getTypeOfPublishReports());

		return userPermissions;
	}

	private List<String> getPermissions() {
		List<String> permissions = new ArrayList<String>();
		permissions.add(RolePermissions.READ_ONLY);
		permissions.add(RolePermissions.FINANCIAL_ADVISER);
		permissions.add(RolePermissions.CUSTOM);
		permissions.add(RolePermissions.ADMIN);
		return permissions;
	}

	private RolePermissions getReadOnlyPermission() {
		RolePermissions readOnly = new RolePermissions();
		readOnly.setRoleName(RolePermissions.READ_ONLY);
		readOnly.setTypeOfBankReconcilation(RolePermissions.TYPE_NO);
		readOnly.setTypeOfInvoicesBills(RolePermissions.TYPE_READ_ONLY);
		readOnly.setTypeOfPayBillsPayments(RolePermissions.TYPE_NO);
		readOnly.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_NO);
		readOnly.setTypeOfViewReports(RolePermissions.TYPE_READ_ONLY);
		readOnly.setTypeOfManageAccounts(RolePermissions.TYPE_NO);
		readOnly.setTypeOfInventoryWarehouse(RolePermissions.TYPE_NO);
		readOnly.setCanDoUserManagement(false);

		return readOnly;
	}

	private RolePermissions getFinancialAdviserPermission() {
		RolePermissions financialAdviser = new RolePermissions();
		financialAdviser.setRoleName(RolePermissions.FINANCIAL_ADVISER);
		financialAdviser.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		financialAdviser
				.setTypeOfPayBillsPayments(RolePermissions.TYPE_APPROVE);
		financialAdviser
				.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfViewReports(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		financialAdviser.setCanDoUserManagement(false);
		return financialAdviser;
	}

	private RolePermissions getAdminPermission() {
		RolePermissions admin = new RolePermissions();
		admin.setRoleName(RolePermissions.ADMIN);
		admin.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		admin.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		admin.setTypeOfPayBillsPayments(RolePermissions.TYPE_APPROVE);
		admin.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		admin.setTypeOfViewReports(RolePermissions.TYPE_YES);
		admin.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		admin.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		admin.setCanDoUserManagement(true);

		return admin;
	}

	@Override
	protected String getDetailsMessage() {
		return user.getID() == 0 ? getMessages().readyToCreate(
				getMessages().user()) : getMessages().readyToUpdate(
				getMessages().user());
	}

	@Override
	public String getSuccessMessage() {
		return user.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().user()) : getMessages().updateSuccessfully(
				getMessages().user());
	}

	@Override
	protected String getWelcomeMessage() {
		return user.getID() == 0 ? getMessages().creating(getMessages().user())
				: "Updating user";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		User currentUser = context.getUser();
		if (!currentUser.isCanDoUserManagement()) {
			addFirstMessage(context, getMessages()
					.youdontHavepermissiosToinviteUser());
			return "usersList";
		}
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().user()));
				return "usersList";
			}
			User userByUserEmail = context.getCompany().getUserByUserEmail(
					string);
			if (userByUserEmail == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().user()));
				return "usersList " + string;
			}
			user = userByUserEmail.getClientUser();
			setValues(context);
		} else {
			user = new ClientUser();
		}
		return null;
	}

	private void setValues(Context context) {
		get(EMAIL).setValue(user.getEmail());
		get(EMAIL).setEditable(false);
		get(FIRST_NAME).setValue(user.getFirstName());
		get(LAST_NAME).setValue(user.getLastName());
		get(LEVEL_ACCESS).setValue(user.getUserRole());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(LEVEL_ACCESS).setDefaultValue(RolePermissions.CUSTOM);
	}

	@Override
	protected String getDeleteCommand(Context context) {
		User currentUser = context.getUser();
		if (currentUser.isCanDoUserManagement() && user.getID() != 0
				&& user.getID() != currentUser.getID()) {
			return "deleteUser " + user.getID();
		}
		return null;
	}
}
