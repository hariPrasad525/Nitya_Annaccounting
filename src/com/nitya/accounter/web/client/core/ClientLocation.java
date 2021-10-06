package com.nitya.accounter.web.client.core;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ClientLocation implements IAccounterCore {

	public static final int LOCATION = 0;

	public static final int BUSINESS = 1;

	public static final int DEPARTMENT = 2;

	public static final int DIVISION = 3;

	public static final int PROPERTY = 4;

	public static final int STORE = 5;

	public static final int TERRITORY = 6;

	private static final long serialVersionUID = 1L;

	private String locationName;

	private long id;

	private int version;
	private String title;
	private String companyName;

	private ClientAddress address;
	private String email;
	private String phone;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public ClientAddress getAddress() {
		return address;
	}

	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public String getName() {
		return locationName;
	}

	@Override
	public String getDisplayName() {
		return locationName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.LOCATION;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}


	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public ClientLocation clone() {
		ClientLocation location = (ClientLocation) this.clone();
		return location;
	}

}
