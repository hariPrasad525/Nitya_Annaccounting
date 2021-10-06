package com.nitya.accounter.web.client.core;

import com.nitya.accounter.web.client.Global;

public class ClientCreditCardCharge extends ClientTransaction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long vendor;

	ClientContact contact;

	ClientAddress vendorAddress;

	String phone;

	long payFrom;

	String checkNumber;

	long deliveryDate;

	/**
	 * @return the vendorId
	 */
	public long getVendor() {
		return vendor;
	}

	/**
	 * @param vendorId
	 *            the vendorId to set
	 */
	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the contactId
	 */
	public ClientContact getContact() {
		return contact;
	}

	/**
	 * @param contactId
	 *            the contactId to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the vendorAddressId
	 */
	public ClientAddress getVendorAddress() {
		return vendorAddress;
	}

	/**
	 * @param vendorAddressId
	 *            the vendorAddressId to set
	 */
	public void setVendorAddress(ClientAddress vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	/**
	 * @return the phone
	 */

	/**
	 * @return the payFromId
	 */

	/**
	 * @param payFromId
	 *            the payFromId to set
	 */
	public void setPayFrom(long payFrom) {
		this.payFrom = payFrom;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @param checkNumber
	 *            the checkNumber to set
	 */

	/**
	 * @return the deliveryDate
	 */
	public long getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setPhone(String string) {
		this.phone = string;

	}

	public void setCheckNumber(String text) {
		this.checkNumber = text;

	}

	public void setDeliveryDate(ClientFinanceDate date) {
		this.deliveryDate = date.getDate();

	}

	public long getPayFrom() {
		return this.payFrom;
	}

	public String getPhone() {
		return this.phone;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Global.get().messages().creditCardCharge();
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
		return AccounterCoreType.CREDITCARDCHARGE;
	}

	public ClientCreditCardCharge clone() {
		ClientCreditCardCharge clientCreditCardChargeClone = (ClientCreditCardCharge) this
				.clone();
		clientCreditCardChargeClone.contact = this.contact.clone();
		clientCreditCardChargeClone.vendorAddress = this.vendorAddress.clone();

		return clientCreditCardChargeClone;
	}
}
