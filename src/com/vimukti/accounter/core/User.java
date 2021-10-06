package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class User extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8262438863405809492L;

	// private String firstName;
	//
	// private String lastName;
	//
	// private String email;

	private String userRole;

	private String uniqueId;

	private Client client;

	private UserPermissions permissions;

	private boolean canDoUserManagement;

	// private String displayName;

	private boolean isAdmin;

	private boolean isDeleted;

	private boolean isActive;

	private Set<PortletPageConfiguration> portletPages = new HashSet<PortletPageConfiguration>();
	/**
	 * The full name of the user.
	 */
	// private String fullName;

	/**
	 * Preferences that a user want to set.
	 */
	private UserPreferences userPreferences = new UserPreferences();

	private byte[] secretKey;

	public User() {
		UserPreferences userPreferences = new UserPreferences();

		this.setUserPreferences(userPreferences);

	}

	public User(ClientUser clientUser) {
		// User user = new User();
		this.setId(clientUser.getID());
		// this.setFirstName(clientUser.getFirstName());
		// this.setLastName(clientUser.getLastName());
		// this.setFullName(clientUser.getFullName());
		// this.setEmail(clientUser.getEmail());
		// this.setActive(clientUser.isActive());
		this.setCanDoUserManagement(clientUser.isCanDoUserManagement());
		this.setUserRole(clientUser.getUserRole());
		this.setAdmin(clientUser.isAdmin());
		ClientUserPermissions permissions = clientUser.getPermissions();
		if (permissions != null) {
			UserPermissions userPermissions = new UserPermissions();
			userPermissions.setTypeOfBankReconcilation(permissions
					.getTypeOfBankReconcilation());
			userPermissions.setTypeOfInvoicesBills(permissions
					.getTypeOfInvoicesBills());
			userPermissions.setTypeOfPayBillsPayments(permissions
					.getTypeOfPayBillsPayments());
			userPermissions.setTypeOfCompanySettingsLockDates(permissions
					.getTypeOfCompanySettingsLockDates());
			userPermissions.setTypeOfViewReports(permissions
					.getTypeOfViewReports());
			userPermissions.setTypeOfManageAccounts(permissions
					.getTypeOfManageAccounts());
			userPermissions.setTypeOfInventoryWarehouse(permissions
					.getTypeOfInventoryWarehouse());
			userPermissions.setTypeOfSaveasDrafts(permissions
					.getTypeOfSaveasDrafts());
			userPermissions.setAllowUsersToAttendance(permissions.getAllowUsersToAttendance());
			this.setPermissions(userPermissions);
		}
	}

	// /**
	// * @return the fullName
	// */
	// public String getFullName() {
	// return fullName;
	// }

	/**
	 * @return the userPreferences
	 */
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	/**
	 * @param userPreferences
	 *            the userPreferences to set
	 */
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	// public void setFullName(String fname) {
	// this.fullName = fname;
	//
	// }

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	// public String getFirstName() {
	// return firstName;
	// }
	//
	// public void setFirstName(String firstName) {
	// this.firstName = firstName;
	// }
	//
	// public String getLastName() {
	// return lastName;
	// }
	//
	// public void setLastName(String lastName) {
	// this.lastName = lastName;
	// }
	//
	// public String getEmail() {
	// return email;
	// }
	//
	// public void setEmail(String emailId) {
	// this.email = emailId;
	// }

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public boolean isCanDoUserManagement() {
		return canDoUserManagement;
	}

	public void setCanDoUserManagement(boolean canDoUserManagement) {
		this.canDoUserManagement = canDoUserManagement;
	}

	public void setPermissions(UserPermissions permissions) {
		this.permissions = permissions;
	}

	public UserPermissions getPermissions() {
		return permissions;
	}

	public String getName() {
		return null;
		// if (getFirstName() == null && getLastName() == null)
		// return "";
		// else if (getFirstName() == null)
		// return getLastName();
		// else if (getLastName() == null)
		// return getFirstName();
		// return getFirstName() + " " + getLastName();

	}

	public ClientUser getClientUser() {
		ClientUser user = new ClientUser();
		user.setFirstName(this.getClient().getFirstName());
		user.setLastName(this.getClient().getLastName());
		user.setFullName(this.getClient().getFullName());
		user.setPassword(this.getClient().getPassword());
		user.setEmail(this.getClient().getEmailId());
		user.setCanDoUserManagement(this.isCanDoUserManagement());
		user.setUserRole(this.getUserRole());
		user.setAdmin(this.isAdmin);
		if (getPermissions() != null) {
			ClientUserPermissions userPermissions = new ClientUserPermissions();
			userPermissions.setTypeOfBankReconcilation(this.getPermissions()
					.getTypeOfBankReconcilation());
			userPermissions.setTypeOfInvoicesBills(this.getPermissions()
					.getTypeOfInvoicesBills());
			userPermissions.setTypeOfPayBillsPayments(this.getPermissions()
					.getTypeOfPayBillsPayments());
			userPermissions.setTypeOfCompanySettingsLockDates(this
					.getPermissions().getTypeOfCompanySettingsLockDates());
			userPermissions.setTypeOfViewReports(this.getPermissions()
					.getTypeOfViewReports());
			userPermissions.setTypeOfManageAccounts(this.getPermissions()
					.getTypeOfManageAccounts());
			userPermissions.setTypeOfInventoryWarehouse(this.getPermissions()
					.getTypeOfInventoryWarehouse());
			userPermissions.setTypeOfSaveasDrafts(this.getPermissions()
					.getTypeOfSaveasDrafts());
			userPermissions.setInvoicesAndPayments(this.getPermissions()
					.getInvoicesAndPayments());
			userPermissions.setAllowUsersToAttendance(this.getPermissions().getAllowUsersToAttendance());
			user.setPermissions(userPermissions);
		}
		Set<ClientPortletPageConfiguration> set = new HashSet<ClientPortletPageConfiguration>();
		ClientConvertUtil utils = new ClientConvertUtil();
		try {
			for (PortletPageConfiguration pc : this.getPortletPages()) {
				ClientPortletPageConfiguration clientObject = utils
						.toClientObject(pc,
								ClientPortletPageConfiguration.class);
				set.add(clientObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setPortletPages(set);
		user.setID(getID());
		return user;
	}

	// public void setDisplayName(String displayName) {
	// this.displayName = displayName;
	// }
	//
	// public String getDisplayName() {
	// return displayName;
	// }

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.USER);
		ChangeTracker.put(accounterCore);
		return false;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public void setName(String name) {
		// this.displayName = name;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.USER;
	}

	public Set<PortletPageConfiguration> getPortletPages() {
		return portletPages;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		if (client != null) {
			w.put(messages.firstName(), client.getFirstName()).gap();
			w.put(messages.lastName(), client.getLastName());

			w.put(messages.email(), client.getEmailId()).gap();
		}

		if (this.userRole != null)
			w.put(messages.userRole(), this.userRole);

		w.put(messages.isActive(), this.isActive).gap();
		w.put(messages.admin(), this.isAdmin);

	}

	public byte[] getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(byte[] secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

}
