package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientEmployee extends ClientPayee implements
		ClientPayStructureDestination {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int GENDER_MALE = 1;

	public static final int GENDER_FEMALE = 2;
	

	public static final int EMPLOYE_TYPE_FULLTIME = 1;

	public static final int EMPLOYE_TYPE_PARTTIME = 2; 

	/**
	 * Employee belongs to Group
	 */
	private long group;

	/** General Information */

	/**
	 * Employee Number
	 */
	private String number;
	
	
	/**
	 * Employee Lastname 
	 */
	private String eLastname;

	public String geteLastname() {
		return eLastname;
	}

	public void seteLastname(String eLastname) {
		this.eLastname = eLastname;
	}
	
	
	/**
	 * Employee Social Security Number 
	 */
	private String ssn;

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * Designation of the Employee
	 */
	private String designation;

	/**
	 * Location of the Employee
	 */
	private String location;

	/**
	 * Date of Birth of the Employee. And this will be used to find if the
	 * Employee is a senior citizen
	 */
	private long dateOfBirth;

	/**
	 * Gender of the Employee
	 */
	private int gender;

	/** Passport or Visa Details */

	/**
	 * Passport Number of the Employee
	 */
	private String passportNumber;

	/**
	 * Country which is Issuing Passport
	 */
	private String countryOfIssue;

	/**
	 * Expiry Date of the Passport
	 */
	private long passportExpiryDate;

	/**
	 * Visa Number of the Employee
	 */
	private String visaNumber;

	/**
	 * Expiry Date of the Visa
	 */
	private long visaExpiryDate;
	/**
	 * lastdate of employee
	 */
	private long lastDate;

	/**
	 * reason type for employee inactive
	 */
	private int reasonType;

	
	private List<ClientEmployeeTax> employeeTaxes;
	
	private List<ClientEmployeeAttendance> employeeAttendances;
	
	private double salary;
	private int payType;
	private int payFrequency;
	private double additionalAmount;

	private ClientEmployee manager;
	
	public int getPayFrequency() {
		return payFrequency;
	}

	public void setPayFrequency(int payFrequency) {
		this.payFrequency = payFrequency;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public double getAdditionalAmount() {
		return additionalAmount;
	}

	public void setAdditionalAmount(double additionalAmount) {
		this.additionalAmount = additionalAmount;
	}
	
	private int employeType;
	

	public int getEmployeType() {
		return employeType;
	}

	public void setEmployeType(int employeType) {
		this.employeType = employeType;
	}


	public List<ClientEmployeeTax> getEmployeeTaxes() {
		return employeeTaxes;
	}

	public void setEmployeeTaxes(List<ClientEmployeeTax> employeeTaxes) {
		this.employeeTaxes = employeeTaxes;
	}
	
	
	public List<ClientEmployeeAttendance> getEmployeeAttendances() { 
		  return employeeAttendances; 
	}
	  
	public void setEmployeeAttendances(List<ClientEmployeeAttendance> employeeAttendances) {
		  this.employeeAttendances = employeeAttendances; 
	}
	 
	
	public ClientEmployee getManager() {
		return manager;
	}

	public void setManager(ClientEmployee manager) {
		this.manager = manager;
	}

	/** Contact Details */

	/**
	 * @return the group
	 */
	public long getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(long group) {
		this.group = group;
	}

	/**
	 * @return the dateOfBirth
	 */
	public long getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(long dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return the employeeNumber
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param employeeNumber
	 *            the employeeNumber to set
	 */
	public void setNumber(String employeeNumber) {
		this.number = employeeNumber;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation
	 *            the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the passportNumber
	 */
	public String getPassportNumber() {
		return passportNumber;
	}

	/**
	 * @param passportNumber
	 *            the passportNumber to set
	 */
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	/**
	 * @return the countryOfIssue
	 */
	public String getCountryOfIssue() {
		return countryOfIssue;
	}

	/**
	 * @param countryOfIssue
	 *            the countryOfIssue to set
	 */
	public void setCountryOfIssue(String countryOfIssue) {
		this.countryOfIssue = countryOfIssue;
	}

	/**
	 * @return the passportExpiryDate
	 */
	public long getPassportExpiryDate() {
		return passportExpiryDate;
	}

	/**
	 * @param passportExpiryDate
	 *            the passportExpiryDate to set
	 */
	public void setPassportExpiryDate(long passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	/**
	 * @return the visaNumber
	 */
	public String getVisaNumber() {
		return visaNumber;
	}

	/**
	 * @param visaNumber
	 *            the visaNumber to set
	 */
	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	/**
	 * @return the visaExpiryDate
	 */
	public long getVisaExpiryDate() {
		return visaExpiryDate;
	}

	/**
	 * @param visaExpiryDate
	 *            the visaExpiryDate to set
	 */
	public void setVisaExpiryDate(long visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientEmployee) {
			ClientEmployee employee = (ClientEmployee) obj;
			return this.getID() == employee.getID() ? true : false;
		}
		return false;
	}

	public long getLastDate() {
		return lastDate;
	}

	public void setLastDate(long lastDate) {
		this.lastDate = lastDate;
	}

	public int getReasonType() {
		return reasonType;
	}

	public void setReasonType(int reasonType) {
		this.reasonType = reasonType;
	}
}
