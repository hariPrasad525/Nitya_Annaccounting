package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class UserPermissions implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7933490266078955247L;

	long id;

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

	// int typeOfPublishReports;
	//
	// int typeOfLockDates;

	User user;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return false;
	}

	@Override
	public long getID() {

		return 0;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public int getTypeOfSaveasDrafts() {
		return typeOfSaveasDrafts;
	}

	public void setTypeOfSaveasDrafts(int typeOfSaveasDrafts) {
		this.typeOfSaveasDrafts = typeOfSaveasDrafts;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

	public int getInvoicesAndPayments() {
		return invoicesAndPayments;
	}
	
	public void setInvoicesAndPayments(int invoicesAndPayments) {
		this.invoicesAndPayments = invoicesAndPayments;
	}

	public int getAllowUsersToAttendance() {
		return allowUsersToAttendance;
	}

	public void setAllowUsersToAttendance(int allowUsersToAttendance) {
		this.allowUsersToAttendance = allowUsersToAttendance;
	}

}
