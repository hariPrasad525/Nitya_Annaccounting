package com.nitya.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

import com.nitya.accounter.web.client.ui.settings.RolePermissions;

public class ClientUser implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String firstName;

	String lastName;

	String userRole;

	private ClientUserPermissions permissions;

	private boolean canDoUserManagement;

	private String displayName;

	private boolean isAdmin;

	int version;
	long id;

	String fullName;
	String email;
	String password;
	String admin;

	private Set<ClientPortletPageConfiguration> portletPages = new HashSet<ClientPortletPageConfiguration>();
	private boolean isDeleted;

	private boolean isActive;

	public ClientUser() {
		// ClientUserPreferences userPreferences = new ClientUserPreferences();

		// this.setUserPreferences(userPreferences);

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * @return the instanceVersion
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param instanceVersion
	 *            the instanceVersion to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the admin
	 */
	public String getAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(String adminId) {
		this.admin = adminId;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getName() {
		if (getFirstName() == null && getLastName() == null)
			return "";
		else if (getFirstName() == null)
			return getLastName();
		else if (getLastName() == null)
			return getFirstName();
		return getFirstName() + " " + getLastName();

	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.USER;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public void setCanDoUserManagement(boolean canDoUserManagement) {
		this.canDoUserManagement = canDoUserManagement;
	}

	public boolean isCanDoUserManagement() {
		return canDoUserManagement;
	}

	public void setPermissions(ClientUserPermissions permissions) {
		this.permissions = permissions;
	}

	public ClientUserPermissions getPermissions() {
		return permissions;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean canDoInvoiceTransactions() {
		if (this.getPermissions().typeOfInvoicesBills == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	public boolean canChangeSettings() {
		if (this.getPermissions().typeOfCompanySettingsLockDates == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	public boolean canViewReports() {
		if (this.getPermissions().typeOfViewReports == RolePermissions.TYPE_YES
				|| this.getPermissions().typeOfViewReports == RolePermissions.TYPE_READ_ONLY)
			return true;
		else
			return false;
	}
	
	public boolean canEnterAttendanceOnly() {
		if (this.getPermissions().allowUsersToAttendance == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	public boolean canDoBanking() {
		if (this.getPermissions().typeOfBankReconcilation == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	public boolean canManageFiscalYears() {
		if (this.getPermissions().typeOfCompanySettingsLockDates == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	public boolean canSeeInvoiceTransactions() {
		if (this.getPermissions().typeOfInvoicesBills != RolePermissions.TYPE_NO)
			return true;
		else
			return false;
	}

	public boolean canSeeBanking() {
		if (this.getPermissions().typeOfBankReconcilation != RolePermissions.TYPE_NO)
			return true;
		else
			return false;
	}

	public boolean canApproveExpences() {
		if (this.getPermissions().typeOfPayBillsPayments == RolePermissions.TYPE_APPROVE)
			return true;
		else
			return false;
	}

	public boolean canSaveExpences() {
		if (this.getPermissions().typeOfPayBillsPayments != RolePermissions.TYPE_NO)
			return true;
		else
			return false;
	}

	public boolean isAdminUser() {
		if (this.getUserRole().equals(RolePermissions.ADMIN))
			return true;
		else
			return false;
	}

	/**
	 * Converts ClientUser to ClientUserInfo
	 */
	public ClientUserInfo toUserInfo() {
		ClientUserInfo userInfo = new ClientUserInfo();
		userInfo.setId(id);
		userInfo.setFirstName(firstName);
		userInfo.setLastName(lastName);
		userInfo.setFullName(fullName);
		userInfo.setPassword(password);
		userInfo.setEmail(email);
		userInfo.setUserRole(userRole);
		userInfo.setPermissions(permissions);
		userInfo.setCanDoUserManagement(canDoUserManagement);
		userInfo.setAdmin(isAdmin);
		userInfo.setActive(this.isActive);
		return userInfo;
	}

	public ClientUser clone() {
		ClientUser clientUserClone = (ClientUser) this.clone();
		clientUserClone.permissions = this.permissions.clone();
		return clientUserClone;
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

	public Set<ClientPortletPageConfiguration> getPortletPages() {
		return portletPages;
	}

	public void setPortletPages(Set<ClientPortletPageConfiguration> portletPages) {
		this.portletPages = portletPages;
	}
}
