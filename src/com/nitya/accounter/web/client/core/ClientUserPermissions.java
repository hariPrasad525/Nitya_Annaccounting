package com.nitya.accounter.web.client.core;

import com.nitya.accounter.web.client.ui.settings.RolePermissions;

public class ClientUserPermissions implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// public static int TYPE_YES = 1;
	// public static int TYPE_NO = 3;
	// public static int TYPE_READ_ONLY = 2;

	int typeOfBankReconcilation;

	int typeOfInvoicesBills;

	int typeOfSaveasDrafts;

	int typeOfPayBillsPayments;

	int typeOfCompanySettingsLockDates;

	int typeOfViewReports;

	int typeOfMangeAccounts;

	int typeOfInventoryWarehouse;

	int invoicesAndPayments;
	
	int allowUsersToAttendance;

	ClientUser user;

	private int version;

	public int getTypeOfBankReconcilation() {
		return typeOfBankReconcilation;
	}

	public void setTypeOfBankReconcilation(int typeOfBankReconcilation) {
		this.typeOfBankReconcilation = typeOfBankReconcilation;
	}

	public int getTypeOfInvoicesBills() {
		return typeOfInvoicesBills;
	}

	public void setTypeOfInvoicesBills(int typeOfInvoicesBills) {
		this.typeOfInvoicesBills = typeOfInvoicesBills;
	}

	public int getTypeOfPayBillsPayments() {
		return typeOfPayBillsPayments;
	}

	public void setTypeOfPayBillsPayments(int typeOfPayBillsPayments) {
		this.typeOfPayBillsPayments = typeOfPayBillsPayments;
	}

	public int getTypeOfCompanySettingsLockDates() {
		return typeOfCompanySettingsLockDates;
	}

	public void setTypeOfCompanySettingsLockDates(
			int typeOfCompanySettingsLockDates) {
		this.typeOfCompanySettingsLockDates = typeOfCompanySettingsLockDates;
	}

	public int getTypeOfViewReports() {
		return typeOfViewReports;
	}

	public void setTypeOfViewReports(int typeOfViewReports) {
		this.typeOfViewReports = typeOfViewReports;
	}

	public int getTypeOfManageAccounts() {
		return typeOfMangeAccounts;
	}

	public void setTypeOfManageAccounts(int typeOfMangeAccounts) {
		this.typeOfMangeAccounts = typeOfMangeAccounts;
	}

	public int getTypeOfInventoryWarehouse() {
		return typeOfInventoryWarehouse;
	}

	public void setTypeOfInventoryWarehouse(int typeOfInventoryWarehouse) {
		this.typeOfInventoryWarehouse = typeOfInventoryWarehouse;
	}
	
	/*
	 * public boolean isAllowUsersToAttendance() { if (allowUsersToAttendance ==
	 * RolePermissions.TYPE_YES) {
	 * 
	 * 
	 * return true; } return false; }
	 */
	
	public int getAllowUsersToAttendance() {
		return allowUsersToAttendance;
	}

	public void setAllowUsersToAttendance(int allowUsersToAttendance) {
		this.allowUsersToAttendance = allowUsersToAttendance;
	}

	public ClientUser getUser() {
		return user;
	}

	public void setUser(ClientUser user) {
		this.user = user;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	public ClientUserPermissions clone() {
		ClientUserPermissions clientUserPreferencesClone = (ClientUserPermissions) this
				.clone();
		clientUserPreferencesClone.user = this.user.clone();

		return clientUserPreferencesClone;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public int getTypeOfSaveasDrafts() {
		return typeOfSaveasDrafts;
	}

	public void setTypeOfSaveasDrafts(int typeOfSaveasDrafts) {
		this.typeOfSaveasDrafts = typeOfSaveasDrafts;
	}

	public boolean isOnlySeeInvoiceandBills() {
		if ((typeOfInvoicesBills == RolePermissions.TYPE_YES && typeOfPayBillsPayments == RolePermissions.TYPE_YES)
				&& (typeOfBankReconcilation != RolePermissions.TYPE_YES
						&& typeOfCompanySettingsLockDates != RolePermissions.TYPE_YES
						&& typeOfInventoryWarehouse != RolePermissions.TYPE_YES
						&& typeOfMangeAccounts != RolePermissions.TYPE_YES
						&& typeOfCompanySettingsLockDates != RolePermissions.TYPE_YES
						&& typeOfViewReports != RolePermissions.TYPE_YES && typeOfSaveasDrafts != RolePermissions.TYPE_YES)) {
			return true;
		}
		return false;
	}

	public boolean isOnlyInvoiceAndPayments() {
		if (invoicesAndPayments == RolePermissions.TYPE_YES
				&& (typeOfPayBillsPayments != RolePermissions.TYPE_YES
						&& typeOfBankReconcilation != RolePermissions.TYPE_YES
						&& typeOfCompanySettingsLockDates != RolePermissions.TYPE_YES
						&& typeOfInventoryWarehouse != RolePermissions.TYPE_YES
						&& typeOfMangeAccounts != RolePermissions.TYPE_YES
						&& typeOfCompanySettingsLockDates != RolePermissions.TYPE_YES
						&& typeOfViewReports != RolePermissions.TYPE_YES && typeOfSaveasDrafts != RolePermissions.TYPE_YES)) {
			return true;
		}
		return false;
	}

	public int getInvoicesAndPayments() {
		return invoicesAndPayments;
	}

	public void setInvoicesAndPayments(int invoicesAndPayments) {
		this.invoicesAndPayments = invoicesAndPayments;
	}
}
