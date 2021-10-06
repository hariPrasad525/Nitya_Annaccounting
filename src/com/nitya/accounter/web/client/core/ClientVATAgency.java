/**
 * 
 */
package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Murali
 * 
 */
public class ClientVATAgency extends ClientPayee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Payment Term for this TaxAgency
	 * 
	 * @see
	 */
	String paymentTerm;

	/**
	 * Liability Account for this TaxAgency
	 * 
	 * @see
	 */
	String purchaseLiabilityAccount;

	String salesLiabilityAccount;

	public final static int RETURN_TYPE_NONE = 0;
	public final static int RETURN_TYPE_UK_VAT = 1;
	public final static int RETURN_TYPE_IRELAND_VAT = 2;

	int VATReturn;

	/**
	 * 
	 */
	public ClientVATAgency() {
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nitya.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TAXAGENCY;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the purchaseLiabilityAccount
	 */
	public String getPurchaseLiabilityAccount() {
		return purchaseLiabilityAccount;
	}

	/**
	 * @param purchaseLiabilityAccount
	 *            the purchaseLiabilityAccount to set
	 */
	public void setPurchaseLiabilityAccount(String purchaseLiabilityAccount) {
		this.purchaseLiabilityAccount = purchaseLiabilityAccount;
	}

	public String getSalesLiabilityAccount() {
		return salesLiabilityAccount;
	}

	public void setSalesLiabilityAccount(String salesLiabilityAccount) {
		this.salesLiabilityAccount = salesLiabilityAccount;
	}

	public int getVATReturn() {
		return VATReturn;
	}

	public void setVATReturn(int vATReturn) {
		VATReturn = vATReturn;
	}

	public ClientVATAgency clone() {
		ClientVATAgency vatAgency = (ClientVATAgency) this.clone();
		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		for (ClientAddress clientAddress : this.address) {
			addresses.add(clientAddress.clone());
		}
		vatAgency.address = address;

		Set<ClientContact> contacts = new HashSet<ClientContact>();
		for (ClientContact clientContact : this.contacts) {
			contacts.add(clientContact.clone());
		}
		vatAgency.contacts = contacts;

		Set<ClientEmail> emails = new HashSet<ClientEmail>();
		for (ClientEmail clientEmail : this.emails) {
			emails.add(clientEmail.clone());
		}
		vatAgency.emails = emails;

		Set<ClientFax> faxes = new HashSet<ClientFax>();
		for (ClientFax clientFax : this.faxNumbers) {
			faxes.add(clientFax.clone());
		}
		vatAgency.faxNumbers = faxes;

		Set<ClientPhone> phones = new HashSet<ClientPhone>();
		for (ClientPhone clientPhone : this.phoneNumbers) {
			phones.add(clientPhone.clone());
		}
		vatAgency.phoneNumbers = phones;

		return vatAgency;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}
}
