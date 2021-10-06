package com.nitya.accounter.web.client.core;

import com.nitya.accounter.web.client.Global;

public class ClientItemReceipt extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	boolean toBePrinted;
	boolean toBeEmailed;

	long vendor;

	ClientAccount shipTo;

	long purchaseOrderDate;

	ClientContact contact;

	ClientAddress vendorAddress;

	String phone;

	long paymentTerm;

	long deliveryDate;

	ClientAddress shippingAddress;

	String shippingTerm;

	String shippingMethod;

	private long purchaseOrder;

	boolean isBilled;

	public boolean isToBePrinted() {
		return toBePrinted;
	}

	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	public long getVendor() {
		return vendor;
	}

	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	public ClientAccount getShipTo() {
		return shipTo;
	}

	public void setShipTo(ClientAccount shipTo) {
		this.shipTo = shipTo;
	}

	public long getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	public void setPurchaseOrderDate(long purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
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

	public long getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(long paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public long getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public ClientAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(ClientAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingTerm() {
		return shippingTerm;
	}

	public void setShippingTerm(String shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public boolean isBilled() {
		return isBilled;
	}

	public void setBilled(boolean isBilled) {
		this.isBilled = isBilled;
	}

	public boolean isToBeEmailed() {
		return toBeEmailed;
	}

	public void setToBeEmailed(boolean toBeEmailed) {
		this.toBeEmailed = toBeEmailed;
	}


	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ITEMRECEIPT;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public void setPurchaseOrder(long purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public long getPurchaseOrder() {
		return purchaseOrder;
	}

	public ClientItemReceipt clone() {
		ClientItemReceipt clientItemReceiptClone = (ClientItemReceipt) this
				.clone();
		clientItemReceiptClone.shipTo = this.shipTo.clone();
		clientItemReceiptClone.contact = this.contact.clone();
		clientItemReceiptClone.vendorAddress = this.vendorAddress.clone();
		clientItemReceiptClone.shippingAddress = this.shippingAddress.clone();
		return clientItemReceiptClone;
	}

	@Override
	public String getName() {
		return Global.get().messages().itemReceipt();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}
}
