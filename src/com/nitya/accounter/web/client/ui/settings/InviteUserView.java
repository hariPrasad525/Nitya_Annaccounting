package com.nitya.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.ClientUserInfo;
import com.nitya.accounter.web.client.core.ClientUserPermissions;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.InvitableUser;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.EmailField;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.FormItem;
import com.nitya.accounter.web.client.ui.forms.InviteEmailCombo;
import com.nitya.accounter.web.client.ui.forms.PasswordItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class InviteUserView extends BaseView<ClientUserInfo> {

	TextItem firstNametext;
	TextItem lastNametext;
	PasswordItem passwdText;
	// SelectCombo emailCombo;
	EmailField emailField;
	Set<InvitableUser> usersList;
	DynamicForm custForm;
	CheckBox userManagementBox;
	String[] permissions = { messages.createInvoicesAndBills(),
			messages.billsAndPayments(), messages2.invoicesAndPayments(),
			messages.bankingAndReconcialiation(),
			messages.changeCompanySettings(), messages.manageAccounts(),
			messages.manageUsers(), messages.viewReports(),
			messages.Saveasdraft(), messages2.allowUserToEnterAttedOnly() };
	List<CheckBox> permissionsBoxes;
	private RadioButton readOnly;
	private RadioButton custom;
	private RadioButton admin;
	private RadioButton financialAdviser;
	//private RadioButton allowUsersToAttendance;
	private InviteEmailCombo emails;

	@Override
	public void init() {
		super.init();
		this.getElement().setId("inviteUser");
		createControls();
	}

	private void initUsers() {
		Accounter.createHomeService().getIvitableUsers(
				new AccounterAsyncCallback<Set<InvitableUser>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(Set<InvitableUser> result) {
						usersList = result;
						emails.setInvitableUser(usersList);

					}
				});
	}

	private void createControls() {
		custForm = new DynamicForm("viewform");
		StyledPanel vPanel = new StyledPanel("vPanel");

		Label lab1 = new Label(messages.inviteUser());
		lab1.setStyleName("label-title");

		firstNametext = new TextItem(messages.firstName(), "firstNametext");
		firstNametext.setRequired(true);
		firstNametext.setEnabled(!isInViewMode());
		lastNametext = new TextItem(messages.lastName(), "lastNametext");
		lastNametext.setRequired(true);
		lastNametext.setEnabled(!isInViewMode());
		passwdText = new PasswordItem(messages.password());
		passwdText.setRequired(true);
		passwdText.setEnabled(!isInViewMode());
		emailField = new EmailField(messages.email());
		emailField.setRequired(true);
		emailField.setEnabled(!isInViewMode());
		emailField.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					final String em = emailField.getValue().toString().trim();
					validateEmailId(em);
				}
			}
		});
		emails = new InviteEmailCombo(messages.email(), "email");
		emails.setRequired(true);
		emails.setEnabled(!isInViewMode());
		emails.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String selectItem = emails.getValue();
				validateEmailId(selectItem);
				if (selectItem != null) {
					for (InvitableUser user : usersList) {
						if (selectItem.equals(user.getEmail())) {
							firstNametext.setValue(user.getFirstName());
							lastNametext.setValue(user.getLastName());
						}
					}
				}
			}
		});

		// emailCombo = new SelectCombo(messages.email());
		// emailCombo.setRequired(true);
		// emailCombo.setEnabled(!isInViewMode());
		// emailCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		// validateEmailId(selectItem);
		// if (emailCombo.getSelectedValue() != null) {
		// for (InvitableUser user : usersList) {
		// if (selectItem.equals(user.getEmail())) {
		// firstNametext.setValue(user.getFirstName());
		// lastNametext.setValue(user.getLastName());
		// }
		// }
		// }
		// }
		// });

		userManagementBox = new CheckBox(
				messages.allowThisUsertoAddorRemoveusers());
		// userManagementBox.getElement().getStyle().setPadding(5, Unit.PX);
		String choose = messages.chooselevelaccessyouwantthisusertohave();
		Label chooseLabel = new Label(choose);
		// chooseLabel.getElement().getStyle().setPadding(5, Unit.PX);
		Label setPerLabel = new Label(messages.setUserpermissions());
		setPerLabel.addStyleName("inviteUserLabel");

		Label manageLabel = new Label(messages.manageUsers());
		manageLabel.addStyleName("inviteUserLabel");

		if (getCompany().isUnlimitedUser()) {
			custForm.add(emailField);
		} else {
			custForm.add(emails);
		}
		custForm.add(firstNametext);
		custForm.add(lastNametext);
		custForm.add(passwdText);

		// element2.setAttribute("width", "150px");
		vPanel.add(custForm);
		vPanel.add(setPerLabel);
		vPanel.add(chooseLabel);

		StyledPanel permissionsPanel = getPermissionsPanel();
		vPanel.add(permissionsPanel);

		// vPanel.add(grid);
		// vPanel.add(manageLabel);
		// vPanel.add(userManagementBox);
		this.add(lab1);
		this.add(vPanel);
	}

	protected void validateEmailId(final String em) {
		if (em.length() != 0) {
			if (!UIUtils.isValidEmail(em)) {
				Accounter.showError(messages.invalidEmail());
				emailField.setText("");
				emails.setValue(null);
			} else {
				// ClientEmail email = new ClientEmail();
				// email.setType(UIUtils.getEmailType(businesEmailSelect
				// .getSelectedValue()));
				// email.setEmail(em);
				// allEmails.put(UIUtils.getEmailType(businesEmailSelect
				// .getSelectedValue()), email);
				Accounter
						.createHomeService()
						.getAllUsers(
								new AccounterAsyncCallback<ArrayList<ClientUserInfo>>() {

									@Override
									public void onResultSuccess(
											ArrayList<ClientUserInfo> result) {
										for (int i = 0; i < result.size(); i++) {
											if (em.equals(result.get(i)
													.getEmail())) {
												Accounter.showError(messages
														.mailExistedAlready());
												emailField.setText("");
												emails.setValue(null);
												if (getCompany()
														.isUnlimitedUser()) {
													firstNametext.setValue("");
													lastNametext.setValue("");
												}
												enableFormItems();
											}
										}
									}

									@Override
									public void onException(
											AccounterException caught) {
										Accounter.showError(messages
												.failedtoloadusersList());
									}
								});

			}
		}
	}

	private StyledPanel getPermissionsPanel() {
		StyledPanel verticalPanel = new StyledPanel("verticalPanel");
		readOnly = new RadioButton("permissions", messages.readOnly()
				+ messages.readOnlyDesc());
		readOnly.setValue(true);
		readOnly.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				disableOrEnableCheckboxes();
			}
		});
		custom = new RadioButton("permissions", messages.custom());
		custom.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				disableOrEnableCheckboxes();
			}
		});
		admin = new RadioButton("permissions", messages.admin()
				+ messages.adminDesc());
		admin.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				disableOrEnableCheckboxes();
			}
		});
		financialAdviser = new RadioButton("permissions",
				messages.financialAdviser() + messages.financialAdviserDesc());
		financialAdviser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				disableOrEnableCheckboxes();
			}
		});
	

		readOnly.setEnabled(!isInViewMode());
		custom.setEnabled(!isInViewMode());
		admin.setEnabled(!isInViewMode());
		financialAdviser.setEnabled(!isInViewMode());

		StyledPanel permissionOptions = new StyledPanel("permissionOptions");
		permissionsBoxes = new ArrayList<CheckBox>();

		for (String permission : permissions) {
			CheckBox checkBox = new CheckBox(permission);
			checkBox.setName(permission);
			checkBox.setEnabled(!isInViewMode());
			permissionOptions.add(checkBox);
			permissionsBoxes.add(checkBox);
		}
		if (getPreferences().isInventoryEnabled()) {
			CheckBox checkBox = new CheckBox(messages.inventoryWarehouse());
			checkBox.setName(messages.inventoryWarehouse());
			checkBox.setEnabled(!isInViewMode());
			permissionOptions.add(checkBox);
			permissionsBoxes.add(checkBox);
		}
		permissionOptions.getElement().getStyle().setPaddingLeft(25, Unit.PX);

		verticalPanel.add(readOnly);
		verticalPanel.add(custom);
		verticalPanel.add(permissionOptions);
		verticalPanel.add(admin);
		verticalPanel.add(financialAdviser);

		disableOrEnableCheckboxes();
		return verticalPanel;
	}

	private void disableOrEnableCheckboxes() {
		for (CheckBox box : permissionsBoxes) {
			if (custom.getValue()) {
				box.setEnabled(!isInViewMode());
			} else {
				box.setEnabled(false);
			}
		}
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientUserInfo());
			initUsers();
		}
		super.initData();
		firstNametext.setValue(data.getFirstName());
		lastNametext.setValue(data.getLastName());
		passwdText.setValue(data.getPassword());

		if (getCompany().isUnlimitedUser()) {
			emailField.setEmail(data.getEmail());
		} else {
			emails.setValue(data.getEmail());
		}

		String userRole = data.getUserRole();

		if (userRole != null) {
			if (userRole.equals(messages.readOnly())) {
				readOnly.setValue(true);
			} else if (userRole.equals(messages.admin())) {
				admin.setValue(true);
			} else if (userRole.equals(messages.financialAdviser())) {
				financialAdviser.setValue(true);
			} else if (userRole.equals(messages.custom())) {
				custom.setValue(true);
			}

			ClientUserPermissions permissions = data.getPermissions();
			if (userRole.equals(messages.custom())) {
				if (permissions.getTypeOfInvoicesBills() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages.createInvoicesAndBills());
				}
				if (permissions.getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages.billsAndPayments());
				}
				if (permissions.getTypeOfBankReconcilation() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages.bankingAndReconcialiation());
				}
				if (permissions.getTypeOfCompanySettingsLockDates() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages.changeCompanySettings());
				}
				if (permissions.getTypeOfManageAccounts() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages.manageAccounts());
				}
				if (permissions.getTypeOfViewReports() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages.viewReports());
				}
				if (permissions.getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages.inventoryWarehouse());
				}
				if (permissions.getAllowUsersToAttendance() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages2.allowUserToEnterAttedOnly());
				}
				if (data.isCanDoUserManagement()) {
					setCheckBoxChecked(messages.manageUsers());
				}
				if (permissions.getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages.Saveasdraft());
				}

				if (permissions.getInvoicesAndPayments() == RolePermissions.TYPE_YES) {
					setCheckBoxChecked(messages2.invoicesAndPayments());
				}
			}
		}

		// userManagementBox.setValue(takenUser.isCanDoUserManagement());
		// grid.setRecords(getRolePermissionsForUser(data));
		// if (data.isActive()) {
		// firstNametext.setDisabled(true);
		// lastNametext.setDisabled(true);
		// emailField.setDisabled(true);
		// // if (takenUser.isAdmin()) {
		// // userManagementBox.setEnabled(false);
		// // }
		// }
		// if(takenUser.isAdmin()) {
		// grid.setDisabled(true);
		// }
	}

	private void setCheckBoxChecked(String string) {
		for (CheckBox box : permissionsBoxes) {
			if (box.getName().equals(string)) {
				box.setValue(true);
			}
		}
	}

	@Override
	public List<DynamicForm> getForms() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}
		};
		this.rpcDoSerivce.canEdit(AccounterCoreType.USER, data.getID(),
				editCallBack);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	private void enableFormItems() {
		firstNametext.setEnabled(!isInViewMode());
		lastNametext.setEnabled(!isInViewMode());
		// if (!Accounter.getUser().getEmail().equals(data.getEmail())) {
		// emailField.setEnabled(!isInViewMode());
		// emailCombo.setEnabled(!isInViewMode());
		// }
		// grid.setDisabled(isInViewMode());
		ClientUser user = Accounter.getUser();
		if (data.isAdmin()) {
			if (user.isAdmin() && data.getID() != user.getID()) {
				readOnly.setEnabled(!isInViewMode());
				custom.setEnabled(!isInViewMode());
				admin.setEnabled(!isInViewMode());
				financialAdviser.setEnabled(!isInViewMode());
				for (CheckBox box : permissionsBoxes) {
					box.setEnabled(!isInViewMode());
				}
			}
		} else if ((data == null || data.getID() == 0 ? true
				: data.getID() != user.getID())) {
			readOnly.setEnabled(!isInViewMode());
			custom.setEnabled(!isInViewMode());
			admin.setEnabled(!isInViewMode());
			financialAdviser.setEnabled(!isInViewMode());
			for (CheckBox box : permissionsBoxes) {
				box.setEnabled(!isInViewMode());
			}
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub
	}

	public void checkBoxClicked(RolePermissions obj) {
		if (canDoUserManagement(obj)) {
			userManagementBox.setValue(true);
			if (isInViewMode() && data.isAdmin())
				userManagementBox.setEnabled(false);
			else
				userManagementBox.setEnabled(true);
		} else {
			userManagementBox.setValue(false);
			userManagementBox.setEnabled(false);
		}
	}

	public boolean canDoUserManagement(RolePermissions role) {
		// if (role.getRoleName().equals(RolePermissions.BASIC_EMPLOYEE)
		// || role.getRoleName().equals(RolePermissions.FINANCIAL_ADVISER))
		if (role.canDoUserManagement)
			return true;

		return false;
	}

	@Override
	public ClientUserInfo saveView() {
		ClientUserInfo saveView = super.saveView();
		if (saveView != null) {
			updateData();
		}
		return saveView;
	}

	private void updateData() {
		data.setFirstName(firstNametext.getValue().toString());
		data.setLastName(lastNametext.getValue().toString());
		data.setPassword(passwdText.getValue().toString());
		data.setFullName(data.getName());
		if (getCompany().isUnlimitedUser()) {
			data.setEmail(emailField.getValue());
		} else {
			data.setEmail(emails.getValue());
		}
		// user.setCanDoUserManagement(userManagementBox.getValue());
		RolePermissions selectedRole = getSelectedRolePermission();
		if (selectedRole != null) {
			data.setUserRole(selectedRole.getRoleName());
			if (selectedRole.getRoleName().equals(RolePermissions.ADMIN)) {
				data.setAdmin(true);
			} else {
				data.setAdmin(false);
			}
			ClientUserPermissions permissions = new ClientUserPermissions();
			permissions.setTypeOfBankReconcilation(selectedRole
					.getTypeOfBankReconcilation());
			permissions.setTypeOfInvoicesBills(selectedRole
					.getTypeOfInvoicesBills());
			permissions.setTypeOfPayBillsPayments(selectedRole
					.getTypeOfPayBillsPayments());
			permissions.setTypeOfCompanySettingsLockDates(selectedRole
					.getTypeOfCompanySettingsLockDates());
			permissions.setTypeOfViewReports(selectedRole
					.getTypeOfViewReports());
			permissions.setTypeOfManageAccounts(selectedRole
					.getTypeOfManageAccounts());
			permissions.setTypeOfInventoryWarehouse(selectedRole
					.getTypeOfInventoryWarehouse());
			permissions.setTypeOfSaveasDrafts(selectedRole
					.getTypeOfSaveasDrafts());
			permissions.setInvoicesAndPayments(selectedRole
					.getTypeOfInvoiceAndPayments());
			permissions.setAllowUsersToAttendance(selectedRole
					.getTypeAllowUsersToAttendance());
			data.setPermissions(permissions);

			data.setCanDoUserManagement(selectedRole.isCanDoUserManagement());
		}

	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdateUser(data);
	}

	private RolePermissions getSelectedRolePermission() {
		// for (int i = 0; i < grid.getTableRowCount(); i++) {
		// if (((CheckBox) grid.getWidget(i, 0)).getValue() == true)
		// return grid.getRecordByIndex(i);
		// }

		RolePermissions rolePermissions = null;
		if (readOnly.getValue()) {
			rolePermissions = getReadOnlyPermission();
		} else if (admin.getValue()) {
			rolePermissions = getAdminPermission();
		} else if (financialAdviser.getValue()) {
			rolePermissions = getFinancialAdviserPermission();
		} else if (custom.getValue()) {
			rolePermissions = getCustomPermission();
		}
		return rolePermissions;
	}

	private RolePermissions getCustomPermission() {
		RolePermissions custom = null;

		for (CheckBox box : permissionsBoxes) {
			if (box.getValue()) {
				if (custom == null) {
					custom = new RolePermissions();
					custom.setRoleName(RolePermissions.CUSTOM);
				}
				if (box.getName().equals(messages.createInvoicesAndBills())) {
					custom.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
				} else if (box.getName().equals(messages.billsAndPayments())) {
					custom.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
				} else if (box.getName().equals(
						messages.bankingAndReconcialiation())) {
					custom.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
				} else if (box.getName().equals(
						messages.changeCompanySettings())) {
					custom.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
				} else if (box.getName().equals(messages.manageAccounts())) {
					custom.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
				} else if (box.getName().equals(messages.manageUsers())) {
					custom.setCanDoUserManagement(true);
				} else if (box.getName().equals(messages.viewReports())) {
					custom.setTypeOfViewReports(RolePermissions.TYPE_YES);
				} else if (box.getName().equals(messages.inventoryWarehouse())) {
					custom.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
				} else if (box.getName().equals(messages.Saveasdraft())) {
					custom.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);
				} else if (box.getName()
						.equals(messages2.invoicesAndPayments())) {
					custom.setTypeOfInvoiceAndPayments(RolePermissions.TYPE_YES);
				} else if (box.getName().equals(messages2.allowUserToEnterAttedOnly())) {
					custom.setTypeAllowUsersToAttendance(RolePermissions.TYPE_YES);
				}
			}
		}

		return custom;
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
		readOnly.setTypeOfSaveasDrafts(RolePermissions.TYPE_READ_ONLY);
		readOnly.setCanDoUserManagement(false);

		return readOnly;
	}

	private RolePermissions getFinancialAdviserPermission() {
		RolePermissions financialAdviser = new RolePermissions();
		financialAdviser.setRoleName(RolePermissions.FINANCIAL_ADVISER);
		financialAdviser.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
		financialAdviser
				.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfViewReports(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);
		financialAdviser.setCanDoUserManagement(false);
		return financialAdviser;
	}
	
	/*
	 * private RolePermissions getAllowUserToEnterAttendanceOnly() { RolePermissions
	 * allowUserToEnterAttendanceOnly = new RolePermissions();
	 * allowUserToEnterAttendanceOnly.setRoleName(RolePermissions.READ_ONLY);
	 * allowUserToEnterAttendanceOnly.setTypeOfBankReconcilation(RolePermissions.
	 * TYPE_NO);
	 * allowUserToEnterAttendanceOnly.setTypeOfInvoicesBills(RolePermissions.
	 * TYPE_READ_ONLY);
	 * allowUserToEnterAttendanceOnly.setTypeOfPayBillsPayments(RolePermissions.
	 * TYPE_NO); allowUserToEnterAttendanceOnly.setTypeOfCompanySettingsLockDates(
	 * RolePermissions.TYPE_NO);
	 * allowUserToEnterAttendanceOnly.setTypeOfViewReports(RolePermissions.
	 * TYPE_READ_ONLY);
	 * allowUserToEnterAttendanceOnly.setTypeOfManageAccounts(RolePermissions.
	 * TYPE_NO);
	 * allowUserToEnterAttendanceOnly.setTypeOfInventoryWarehouse(RolePermissions.
	 * TYPE_NO);
	 * allowUserToEnterAttendanceOnly.setTypeOfSaveasDrafts(RolePermissions.
	 * TYPE_READ_ONLY);
	 * allowUserToEnterAttendanceOnly.setCanDoUserManagement(false); return
	 * allowUserToEnterAttendanceOnly; }
	 */

	private RolePermissions getAdminPermission() {
		RolePermissions admin = new RolePermissions();
		admin.setRoleName(RolePermissions.ADMIN);
		admin.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		admin.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		admin.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
		admin.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		admin.setTypeOfViewReports(RolePermissions.TYPE_YES);
		admin.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		admin.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		admin.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);
		admin.setCanDoUserManagement(true);

		return admin;
	}

	public List<RolePermissions> getDefaultRolesAndPermissions() {
		List<RolePermissions> list = new ArrayList<RolePermissions>();

		RolePermissions readOnly = new RolePermissions();
		readOnly.setRoleName(RolePermissions.READ_ONLY);
		readOnly.setTypeOfBankReconcilation(RolePermissions.TYPE_NO);
		readOnly.setTypeOfInvoicesBills(RolePermissions.TYPE_READ_ONLY);
		readOnly.setTypeOfPayBillsPayments(RolePermissions.TYPE_NO);
		readOnly.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_NO);
		readOnly.setTypeOfViewReports(RolePermissions.TYPE_READ_ONLY);
		readOnly.setTypeOfManageAccounts(RolePermissions.TYPE_NO);
		readOnly.setTypeOfInventoryWarehouse(RolePermissions.TYPE_NO);
		readOnly.setTypeOfSaveasDrafts(RolePermissions.TYPE_READ_ONLY);
		readOnly.setCanDoUserManagement(false);
		list.add(readOnly);

		RolePermissions financialAdviser = new RolePermissions();
		financialAdviser.setRoleName(RolePermissions.FINANCIAL_ADVISER);
		financialAdviser.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
		financialAdviser
				.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfViewReports(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);
		financialAdviser.setCanDoUserManagement(false);
		list.add(financialAdviser);
		
		RolePermissions allowUserToEnterAttendanceOnly = new RolePermissions();
		allowUserToEnterAttendanceOnly.setRoleName(RolePermissions.READ_ONLY);
		allowUserToEnterAttendanceOnly.setTypeOfBankReconcilation(RolePermissions.TYPE_NO);
		allowUserToEnterAttendanceOnly.setTypeOfInvoicesBills(RolePermissions.TYPE_READ_ONLY);
		allowUserToEnterAttendanceOnly.setTypeOfPayBillsPayments(RolePermissions.TYPE_NO);
		allowUserToEnterAttendanceOnly.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_NO);
		allowUserToEnterAttendanceOnly.setTypeOfViewReports(RolePermissions.TYPE_READ_ONLY);
		allowUserToEnterAttendanceOnly.setTypeOfManageAccounts(RolePermissions.TYPE_NO);
		allowUserToEnterAttendanceOnly.setTypeOfInventoryWarehouse(RolePermissions.TYPE_NO);
		allowUserToEnterAttendanceOnly.setTypeOfSaveasDrafts(RolePermissions.TYPE_READ_ONLY);
		allowUserToEnterAttendanceOnly.setCanDoUserManagement(false);
		list.add(allowUserToEnterAttendanceOnly);

		RolePermissions admin = new RolePermissions();
		admin.setRoleName(RolePermissions.ADMIN);
		admin.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		admin.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		admin.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
		admin.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		admin.setTypeOfViewReports(RolePermissions.TYPE_YES);
		admin.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		admin.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
		admin.setTypeOfSaveasDrafts(RolePermissions.TYPE_YES);
		admin.setCanDoUserManagement(true);
		list.add(admin);

		return list;
	}

	public List<RolePermissions> getRolePermissionsForUser(ClientUserInfo user) {
		List<RolePermissions> defaultRoles = getDefaultRolesAndPermissions();
		List<RolePermissions> roles = new ArrayList<RolePermissions>();
		// for (RolePermissions role : defaultRoles) {
		// if (user.isAdmin()
		// && (!role.getRoleName().equals(RolePermissions.READ_ONLY) && !role
		// .getRoleName().equals(RolePermissions.INVOICE_ONLY))) {
		// roles.add(role);
		// }
		// }
		roles = roles.isEmpty() ? defaultRoles : roles;
		for (RolePermissions role : roles) {
			if (role.getRoleName().equals(user.getUserRole())) {
				int index = roles.indexOf(role);
				roles.remove(index);
				RolePermissions toBeAdded = new RolePermissions();
				toBeAdded.setRoleName(user.getUserRole());
				toBeAdded.setTypeOfBankReconcilation(user.getPermissions()
						.getTypeOfBankReconcilation());
				toBeAdded.setTypeOfInvoicesBills(user.getPermissions()
						.getTypeOfInvoicesBills());
				toBeAdded.setTypeOfPayBillsPayments(user.getPermissions()
						.getTypeOfPayBillsPayments());
				toBeAdded.setTypeOfCompanySettingsLockDates(user
						.getPermissions().getTypeOfCompanySettingsLockDates());
				toBeAdded.setTypeOfViewReports(user.getPermissions()
						.getTypeOfViewReports());
				toBeAdded.setTypeOfManageAccounts(user.getPermissions()
						.getTypeOfManageAccounts());
				toBeAdded.setTypeOfInventoryWarehouse(user.getPermissions()
						.getTypeOfInventoryWarehouse());
				toBeAdded.setTypeOfSaveasDrafts(user.getPermissions()
						.getTypeOfSaveasDrafts());
				toBeAdded.setCanDoUserManagement(user.isCanDoUserManagement());
				roles.add(index, toBeAdded);
				break;
			}
		}
		return roles;
	}

	@Override
	public ValidationResult validate() {
		updateData();
		ValidationResult result = new ValidationResult();

		if (getCompany().isUnlimitedUser()) {
			result.add(FormItem.validate(firstNametext, lastNametext,
					emailField));
			if (isEmailIDExist(getData())) {
				result.addError(emailField, messages.userExistsWithThisMailId());
			}

			if (!(readOnly.getValue() || custom.getValue() || admin.getValue() || financialAdviser
					.getValue())) {
				result.addError(emailField,
						messages.pleaseSelect(messages.levelOfAccess()));
			}
		} else {
			result.add(FormItem.validate(firstNametext, lastNametext, emails));
			if (isEmailIDExist(getData())) {
				result.addError(emails, messages.userExistsWithThisMailId());
			}

			if (!(readOnly.getValue() || custom.getValue() || admin.getValue() || financialAdviser
					.getValue())) {
				result.addError(emails,
						messages.pleaseSelect(messages.levelOfAccess()));
			}
		}

		if (custom.getValue() && getCustomPermission() == null) {
			result.addError(custForm,
					messages.pleaseSelect(messages.permissions()));
		}

		// for checking the userList for another admin role
		ArrayList<ClientUserInfo> usersList = getCompany().getUsersList();
		boolean hasAnotherAdmin = false;
		for (ClientUserInfo user : usersList) {
			if (user.isAdmin() && !(user.getEmail().equals(emails.getValue()))) {
				hasAnotherAdmin = true;
			}
		}

		if (getData().getID() != 0) {
			if (hasAnotherAdmin == false && !admin.getValue())
				result.addError(getData(),
						messages.cannotCreateUserAsTheirIsNoUserWithAdminRole());
		} else {
			clearError(getData());
		}

		return result;
	}

	private boolean isEmailIDExist(ClientUserInfo object) {
		List<ClientUserInfo> list = getCompany().getUsersList();
		if (list == null || list.isEmpty())
			return false;
		for (ClientUserInfo user : list) {
			if (user.getEmail() != null
					&& user.getEmail().equals(object.getEmail())) {
				if (user.getID() != object.getID()) {
					return true;
				}
				if (user.getID() == object.getID()) {
					continue;
				} else {
					if (user.getEmail() != null && object.getEmail() != null
							&& user.getEmail().equals(object.getEmail())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected String getViewTitle() {
		return messages.inviteUser();
	}

	@Override
	public void setFocus() {
		this.firstNametext.setFocus();

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		ClientUser user = getCompany().getLoggedInUser();
		if (data == null || data.getID() == 0 ? true : data.getID() != user
				.getID()) {
			if (data.isAdmin() && !user.isAdmin()) {
				return false;
			}
			return super.canDelete();
		}
		return false;
	}
}
