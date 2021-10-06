package com.vimukti.accounter.web.client.core;

public class ClientSalesPerson implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TITLE_DR = 1;
	public static final int TITLE_MISS = 2;
	public static final int TITLE_MR = 3;
	public static final int TITLE_MRS = 4;

	public static final int SUFFIX_I = 1;
	public static final int SUFFIX_II = 2;
	public static final int SUFFIX_III = 3;
	public static final int SUFFIX_JR = 4;
	public static final int SUFFIX_SR = 5;

	public static final int GENDER_UNSPECIFIED = 1;
	public static final int GENDER_MALE = 2;
	public static final int GENDER_FEMALE = 3;

	long id;
	String title;
	String firstName;
	String middleName1;
	String middleName2;
	String middleName3;
	String lastName;
	String suffix;

	String jobTitle;

	long expenseAccount;

	String gender;
	long dateOfBirth;
	long dateOfHire;
	long dateOfLastReview;
	long dateOfRelease;
	private boolean isActive;
	private String memo;
	private String fileAs;
	private ClientAddress address;
	private String phoneNo;
	private String faxNo;
	private String email;
	private String webPageAddress;

	private int version;

	public ClientSalesPerson() {
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName1
	 */
	public String getMiddleName1() {
		return middleName1;
	}

	/**
	 * @param middleName1
	 *            the middleName1 to set
	 */
	public void setMiddleName1(String middleName1) {
		this.middleName1 = middleName1;
	}

	/**
	 * @return the middleName2
	 */
	public String getMiddleName2() {
		return middleName2;
	}

	/**
	 * @param middleName2
	 *            the middleName2 to set
	 */
	public void setMiddleName2(String middleName2) {
		this.middleName2 = middleName2;
	}

	/**
	 * @return the middleName3
	 */
	public String getMiddleName3() {
		return middleName3;
	}

	/**
	 * @param middleName3
	 *            the middleName3 to set
	 */
	public void setMiddleName3(String middleName3) {
		this.middleName3 = middleName3;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix
	 *            the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle
	 *            the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the expenseAccount
	 */

	/**
	 * @param expenseAccount
	 *            the expenseAccount to set
	 */
	public void setExpenseAccount(long expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	/**
	 * @return the memo
	 */

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
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
	 * @return the dateOfHire
	 */
	public long getDateOfHire() {
		return dateOfHire;
	}

	/**
	 * @param dateOfHire
	 *            the dateOfHire to set
	 */
	public void setDateOfHire(long dateOfHire) {
		this.dateOfHire = dateOfHire;
	}

	/**
	 * @return the dateOfLastReview
	 */
	public long getDateOfLastReview() {
		return dateOfLastReview;
	}

	/**
	 * @param dateOfLastReview
	 *            the dateOfLastReview to set
	 */
	public void setDateOfLastReview(long dateOfLastReview) {
		this.dateOfLastReview = dateOfLastReview;
	}

	/**
	 * @return the dateOfRelease
	 */
	public long getDateOfRelease() {
		return dateOfRelease;
	}

	/**
	 * @param dateOfRelease
	 *            the dateOfRelease to set
	 */
	public void setDateOfRelease(long dateOfRelease) {
		this.dateOfRelease = dateOfRelease;
	}

	@Override
	public String getName() {
		return firstName;
	}

	public long getExpenseAccount() {
		return this.expenseAccount;
	}

	public void setDateOfHire(ClientFinanceDate date) {
		this.dateOfHire = date.getDate();
	}

	public void setDateOfBirth(ClientFinanceDate date) {
		this.dateOfBirth = date.getDate();
	}

	public void setDateOfLastReview(ClientFinanceDate date) {
		this.dateOfLastReview = date.getDate();
	}

	public void setDateOfRelease(ClientFinanceDate date) {
		this.dateOfRelease = date.getDate();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.SALES_PERSON;
	}

	@Override
	public String getDisplayName() {

		StringBuffer buffer = new StringBuffer();

		buffer.append(String.valueOf(firstName));

		if (jobTitle != null) {
			buffer.append("( " + String.valueOf(jobTitle) + " )");
		}

		return buffer.toString();
	}


	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public ClientSalesPerson clone() {
		ClientSalesPerson salesPerson = (ClientSalesPerson) this.clone();
		salesPerson.setAddress(address);

		salesPerson.memo = memo;

		salesPerson.email = email;

		salesPerson.faxNo = faxNo;

		salesPerson.setPhoneNo(getPhoneNo());
		return salesPerson;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setAddress(ClientAddress address) {
		this.address = address;

	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;

	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setWebPageAddress(String webPageAddress) {
		this.webPageAddress = webPageAddress;
	}

	public ClientAddress getAddress() {
		return address;
	}

	public String getFileAs() {
		return fileAs;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public String getWebPageAddress() {
		return webPageAddress;
	}

	public String getEmail() {
		return email;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getMemo() {
		return memo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientSalesPerson) {
			ClientSalesPerson salesPerson = (ClientSalesPerson) obj;
			return this.getID() == salesPerson.getID() ? true : false;
		}
		return false;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
	this.version=version;
	}
}
