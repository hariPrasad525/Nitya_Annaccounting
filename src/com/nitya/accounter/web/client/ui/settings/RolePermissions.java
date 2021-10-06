package com.nitya.accounter.web.client.ui.settings;

public class RolePermissions {

	public static final String CUSTOM = "Custom";
	public static String READ_ONLY = "Read Only";
	// public static String INVOICE_ONLY = "Invoice Only";
	// public static String BASIC_EMPLOYEE = "Basic Employee";
	public static String FINANCIAL_ADVISER = "Financial Adviser";
	// public static String FINANCE_ADMIN = "Finance Admin";
	public static String ADMIN = "Admin";

	public static String BANK_RECONCILATION = "Bank Reconcilation";
	public static String INVOICES = "Invoices";
	public static String EMPLOYEE_EXPENCES = "Employee Expences";
	public static String EDIT_SYSTEM_SETTINGS = "Edit System Settings";
	public static String VIEW_REPORTS = "View Reports";
	public static String PUBLISH_REPORTS = "Publish Reports";
	public static String LOCK_DATES = "Lock Dates";
	public static String MANAGE_USERS = "Manage Users";

	public static int TYPE_YES = 1;
	public static int TYPE_NO = 3;
	public static int TYPE_READ_ONLY = 2;
	public static int TYPE_DRAFT_ONLY = 4;
	public static int TYPE_APPROVE = 5;

	String roleName;

	int typeOfBankReconcilation;

	int typeOfSaveasDrafts;

	int typeOfInvoicesBills;

	int typeOfPayBillsPayments;

	int typeOfCompanySettingsLockDates;

	int typeOfViewReports;

	int typeOfMangeAccounts;

	int typeOfInvoiceAndPayments;
	
	int typeAllowUsersToAttendance;

	boolean canDoUserManagement;

	private int typeOfInventoryWarehouse;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getTypeOfBankReconcilation() {
		return typeOfBankReconcilation;
	}

	public void setTypeOfBankReconcilation(int typeOfBankReconcilation) {
		this.typeOfBankReconcilation = typeOfBankReconcilation;
	}

	public int getTypeOfInvoicesBills() {
		return typeOfInvoicesBills;
	}

	public void setTypeOfInvoicesBills(int typeOfInvoices) {
		this.typeOfInvoicesBills = typeOfInvoices;
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

	public boolean isCanDoUserManagement() {
		return canDoUserManagement;
	}

	public void setCanDoUserManagement(boolean canDoUserManagement) {
		this.canDoUserManagement = canDoUserManagement;
	}

	public int getTypeOfInventoryWarehouse() {
		return typeOfInventoryWarehouse;
	}

	public void setTypeOfInventoryWarehouse(int typeOfInventoryWarehouse) {
		this.typeOfInventoryWarehouse = typeOfInventoryWarehouse;
	}

	public int getTypeOfSaveasDrafts() {
		return typeOfSaveasDrafts;
	}

	public void setTypeOfSaveasDrafts(int typeOfSaveasDrafts) {
		this.typeOfSaveasDrafts = typeOfSaveasDrafts;
	}

	/**
	 * @return the typeOfInvoiceAndPayments
	 */
	public int getTypeOfInvoiceAndPayments() {
		return typeOfInvoiceAndPayments;
	}

	/**
	 * @param typeOfInvoiceAndPayments
	 *            the typeOfInvoiceAndPayments to set
	 */
	public void setTypeOfInvoiceAndPayments(int typeOfInvoiceAndPayments) {
		this.typeOfInvoiceAndPayments = typeOfInvoiceAndPayments;
	}
	
	/**
	 * @return the typeAllowUsersToAttendance
	 */
	public int getTypeAllowUsersToAttendance() {
		return typeAllowUsersToAttendance;
	}

	/**
	 * @param typeAllowUsersToAttendance
	 *            the typeAllowUsersToAttendance to set
	 */
	public void setTypeAllowUsersToAttendance(int typeAllowUsersToAttendance) {
		this.typeAllowUsersToAttendance = typeAllowUsersToAttendance;
	}
}
