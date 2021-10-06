package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientCashPurchase extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int EMPLOYEE_EXPENSE_STATUS_SAVE = 0;
	public static final int EMPLOYEE_EXPENSE_STATUS_DELETE = 1;
	public static final int EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL = 2;
	public static final int EMPLOYEE_EXPENSE_STATUS_APPROVED = 3;
	public static final int EMPLOYEE_EXPENSE_STATUS_DECLINED = 4;
	public static final int EMPLOYEE_EXPENSE_STATUS_NOT_TO_SHOW = 5;

	long vendor;

	ClientContact contact;

	ClientAddress vendorAddress;

	long employee;

	String phone;

	long payFrom;

	String checkNumber;

	ClientAccount cashExpenseAccount;

	long deliveryDate;

	int expenseStatus;

	private List<ClientPurchaseOrder> purchaseOrders = new ArrayList<ClientPurchaseOrder>();

	public long getVendor() {
		return vendor;
	}

	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	public ClientContact getContact() {
		return contact;
	}

	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	public ClientAddress getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(ClientAddress vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(long payFrom) {
		this.payFrom = payFrom;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public long getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CASHPURCHASE;
	}

	public long getEmployee() {
		return employee;
	}

	public void setEmployee(long employee) {
		this.employee = employee;
	}

	public ClientAccount getCashExpenseAccount() {
		return cashExpenseAccount;
	}

	public void setCashExpenseAccount(ClientAccount cashExpenseAccount) {
		this.cashExpenseAccount = cashExpenseAccount;
	}

	public int getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(int expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public ClientCashPurchase clone() {
		ClientCashPurchase clientCashPurchaseClone = (ClientCashPurchase) this
				.clone();
		clientCashPurchaseClone.contact = this.contact.clone();
		clientCashPurchaseClone.vendorAddress = this.vendorAddress.clone();
		clientCashPurchaseClone.employee = this.employee;

		return clientCashPurchaseClone;
	}

	public List<ClientPurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<ClientPurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}
}
