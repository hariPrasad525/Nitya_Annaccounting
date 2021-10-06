package com.nitya.accounter.web.client.core;

public class ClientTDSResponsiblePerson implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String responsibleName;
	private String designation;
	private String branch;
	private String flatNo;
	private String buildingName;
	private String street;
	private String area;
	private String city;
	private String stateName;
	private long pinCode;
	private boolean addressChanged;
	private long telephoneNumber;
	private long faxNo;
	private String emailAddress;
	private String financialYear;
	private String assesmentYear;
	private int returnType;
	private boolean existingTDSassesse;
	private String panNumber;
	private String tanNumber;

	private long mobileNumber;
	private long stdCode;

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public String getName() {
		return "ClientTDSResponsiblePerson";
	}

	@Override
	public String getDisplayName() {
		return "ClientTDSResponsiblePerson";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TDSRESPONSIBLEPERSON;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public String getResponsibleName() {
		return responsibleName;
	}

	public void setResponsibleName(String responsibleName) {
		this.responsibleName = responsibleName;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public long getPinCode() {
		return pinCode;
	}

	public void setPinCode(long pinCode) {
		this.pinCode = pinCode;
	}

	public boolean isAddressChanged() {
		return addressChanged;
	}

	public void setAddressChanged(boolean addressChanged) {
		this.addressChanged = addressChanged;
	}

	public long getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(long telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public long getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(long faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getAssesmentYear() {
		return assesmentYear;
	}

	public void setAssesmentYear(String assesmentYear) {
		this.assesmentYear = assesmentYear;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public boolean isExistingTDSassesse() {
		return existingTDSassesse;
	}

	public void setExistingTDSassesse(boolean existingTDSassesse) {
		this.existingTDSassesse = existingTDSassesse;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public long getStdCode() {
		return stdCode;
	}

	public void setStdCode(long stdCode) {
		this.stdCode = stdCode;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the tanNumber
	 */
	public String getTanNumber() {
		return tanNumber;
	}

	/**
	 * @param tanNumber
	 *            the tanNumber to set
	 */
	public void setTanNumber(String tanNumber) {
		this.tanNumber = tanNumber;
	}

}
