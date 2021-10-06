package com.nitya.accounter.core;

import java.util.LinkedHashMap;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class Address implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6945021735662392287L;
	public static final int TYPE_BUSINESS = 1;
	public static final int TYPE_BILL_TO = 2;
	public static final int TYPE_SHIP_TO = 3;
	public static final int TYPE_WAREHOUSE = 4;
	public static final int TYPE_LEGAL = 5;
	public static final int TYPE_POSTAL = 6;
	public static final int TYPE_OTHER = 7;
	public static final int TYPE_HOME = 8;
	public static final int TYPE_COMPANY = 9;
	public static final int TYPE_COMPANY_REGISTRATION = 10;

	private int type = 0;
	String address1 = "";
	String street = "";
	String city = "";
	String stateOrProvinence = "";
	private String zipOrPostalCode = "";
	String countryOrRegion = "";
	private boolean isSelected = false;
	private long id;
	private int version;

	public Address() {
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	// public void setType(int type) {
	// this.type = type;
	// }
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	// public void setStreet(String street) {
	// this.street = street;
	// }
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
	// public void setCity(String city) {
	// this.city = city;
	// }
	/**
	 * @return the stateOrProvinence
	 */
	public String getStateOrProvinence() {
		return stateOrProvinence;
	}

	/**
	 * @param stateOrProvinence
	 *            the stateOrProvinence to set
	 */
	// public void setStateOrProvinence(String stateOrProvinence) {
	// this.stateOrProvinence = stateOrProvinence;
	// }
	/**
	 * @return the zipOrPostalCode
	 */
	public String getZipOrPostalCode() {
		return zipOrPostalCode;
	}

	/**
	 * @param zipOrPostalCode
	 *            the zipOrPostalCode to set
	 */
	// public void setZipOrPostalCode(String zipOrPostalCode) {
	// this.zipOrPostalCode = zipOrPostalCode;
	// }
	/**
	 * @return the countryOrRegion
	 */
	public String getCountryOrRegion() {
		return countryOrRegion;
	}

	/**
	 * @param countryOrRegion
	 *            the countryOrRegion to set
	 */
	// public void setCountryOrRegion(String countryOrRegion) {
	// this.countryOrRegion = countryOrRegion;
	// }
	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected();
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	// public void setIsSelected(boolean isSelected) {
	// this.isSelected = isSelected;
	// }
	public LinkedHashMap<String, String> getAddressTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(TYPE_BUSINESS + "", "Business");
		valueMap.put(TYPE_BILL_TO + "", "Bill to");
		valueMap.put(TYPE_SHIP_TO + "", "Ship to");
		valueMap.put(TYPE_WAREHOUSE + "", "Warehouse");
		valueMap.put(TYPE_LEGAL + "", "Legal");
		valueMap.put(TYPE_POSTAL + "", "Postal");
		valueMap.put(TYPE_HOME + "", "Home");
		valueMap.put(TYPE_OTHER + "", "Other");
		return valueMap;

	}

	public boolean equals(Address address) {

		if (this.getType() == address.getType()
				&& this.isSelected() == address.isSelected()
				&& this.street.equals(address.street)
				&& this.city.equals(address.city)
				&& this.stateOrProvinence.equals(address.stateOrProvinence)
				&& this.getZipOrPostalCode().equals(
						address.getZipOrPostalCode())
				&& this.countryOrRegion.equals(address.countryOrRegion))
			return true;
		return false;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountryOrRegion(String countryOrRegion) {
		this.countryOrRegion = countryOrRegion;

	}

	public void setStateOrProvinence(String stateOrProvinence) {
		this.stateOrProvinence = stateOrProvinence;
	}

	public void setStreet(String street2) {
		this.street = street2;
	}

	@Override
	public long getID() {
		return this.getId();
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
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
	public String toString() {
		final StringBuffer information = new StringBuffer();
		String address1 = this.getAddress1();
		if (address1 != null && !address1.equals(""))
			information.append(address1).append(", ");
		String street = this.getStreet();
		if (street != null && !street.equals(""))
			information.append(street).append(", ");
		String city = this.getCity();
		if (city != null && !city.equals(""))
			information.append(city).append(", ");
		String state = this.getStateOrProvinence();
		if (state != null && !state.equals(""))
			information.append(state).append(", ");
		String zip = this.getZipOrPostalCode();
		if (zip != null && !zip.equals(""))
			information.append(zip).append(", ");
		String country = this.getCountryOrRegion();
		if (country != null && !country.equals(""))
			information.append(country).append(", ");

		return information.toString();
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.address()).gap();

		if (this.address1 != null) {
			w.put(messages.address1(), this.address1);
		}

		if (this.city != null) {
			w.put(messages.city(), this.city).gap();
		}

		if (this.stateOrProvinence != null) {
			w.put(messages.state(), this.stateOrProvinence);
		}

		if (this.countryOrRegion != null) {
			w.put(messages.country(), this.countryOrRegion).gap();
		}

		if (this.getZipOrPostalCode() != null) {
			w.put(messages.zipCode(), this.getZipOrPostalCode());
		}
	}

	public void setZipOrPostalCode(String zipOrPostalCode) {
		this.zipOrPostalCode = zipOrPostalCode;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}
}
