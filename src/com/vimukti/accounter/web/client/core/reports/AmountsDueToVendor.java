package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class AmountsDueToVendor extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String name;

	String fileAs;

	boolean isActive;

	String webPageAddress;

	ClientFinanceDate taxAgencySince;

	double amount = 0D;

	String address;

	String city;

	String state;

	String zip;

	String phone;

	String fax;

	String email;

	int type;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the fileAs
	 */
	public String getFileAs() {
		return fileAs;
	}

	/**
	 * @param fileAs
	 *            the fileAs to set
	 */
	public void setFileAs(final String fileAs) {
		this.fileAs = fileAs;
	}

	/**
	 * @return the isActive
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(final boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the webPageAddress
	 */
	public String getWebPageAddress() {
		return webPageAddress;
	}

	/**
	 * @param webPageAddress
	 *            the webPageAddress to set
	 */
	public void setWebPageAddress(final String webPageAddress) {
		this.webPageAddress = webPageAddress;
	}

	/**
	 * @return the taxAgencySince
	 */
	public ClientFinanceDate getTaxAgencySince() {
		return taxAgencySince;
	}

	/**
	 * @param taxAgencySince
	 *            the taxAgencySince to set
	 */
	public void setTaxAgencySince(final ClientFinanceDate taxAgencySince) {
		this.taxAgencySince = taxAgencySince;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(final double amount) {
		this.amount = amount;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(final String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(final String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(final String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(final String zip) {
		this.zip = zip;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(final String fax) {
		this.fax = fax;
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
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final int type) {
		this.type = type;
	}

	public boolean equals(final AmountsDueToVendor ad) {

		if (this.name.equals(ad.name) && this.fileAs.equals(ad.fileAs)
				&& this.isActive == ad.isActive
				&& this.webPageAddress.equals(ad.webPageAddress)
				&& DecimalUtil.isEquals(this.amount, ad.amount)
				&& this.address.equals(ad.address) && this.city.equals(ad.city)
				&& this.state.equals(ad.state) && this.zip.equals(ad.zip)
				&& this.phone.equals(ad.phone) && this.fax.equals(ad.fax)
				&& this.email.equals(ad.email) && this.type == ad.type)
			return true;
		return false;

	}

}
