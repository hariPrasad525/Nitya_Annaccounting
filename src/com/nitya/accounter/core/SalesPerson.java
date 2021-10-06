package com.nitya.accounter.core;

import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class SalesPerson extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7140827426888772170L;
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

	/**
	 * This field stores the title, of the SalesPerson
	 */
	String title;

	/**
	 * This field, stores the First Name of the SalesPerson
	 */
	String firstName;

	/**
	 * This field Stores the Middle Name of the SalesPerson
	 */
	String middleName1;
	String middleName2;
	String middleName3;

	/**
	 * This field Stores the Last Name of the SalesPerson
	 */
	String lastName;

	/**
	 * This field stores the Suffix, of the SalesPerson
	 */
	String suffix;

	/**
	 * This field, stores the Job Title for this SalesPerson
	 */
	String jobTitle;

	/**
	 * Expense {@link Account} for this SalesPerson
	 */
	Account expenseAccount;

	/**
	 * Gender type of the SalesPerson <br>
	 * 1 = UnSpecified, <br>
	 * 2 = Male, <br>
	 * 3 = Female
	 */
	String gender;

	/**
	 * Date of Birth of the Sales Person
	 */
	FinanceDate dateOfBirth;

	/**
	 * Date of Hire of this SalesPerson
	 */
	FinanceDate dateOfHire;

	/**
	 * Date of Last Review
	 */
	FinanceDate dateOfLastReview;

	/**
	 * Date of Release
	 */
	FinanceDate dateOfRelease;

	// String createdBy;
	// String lastModifier;

	private boolean isActive;
	private String memo;
	private String fileAs;
	private Address address;
	private String phoneNo;
	private String faxNo;
	private String email;
	private String webPageAddress;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFileAs() {
		return fileAs;
	}

	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddresss(Address address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebPageAddress() {
		return webPageAddress;
	}

	public void setWebPageAddress(String webPageAddress) {
		this.webPageAddress = webPageAddress;
	}

	public SalesPerson() {
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the middleName1
	 */
	public String getMiddleName1() {
		return middleName1;
	}

	/**
	 * @return the middleName2
	 */
	public String getMiddleName2() {
		return middleName2;
	}

	/**
	 * @return the middleName3
	 */
	public String getMiddleName3() {
		return middleName3;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @return the expenseAccount
	 */
	public Account getExpenseAccount() {
		return expenseAccount;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the dateOfBirth
	 */
	public FinanceDate getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @return the dateOfHire
	 */
	public FinanceDate getDateOfHire() {
		return dateOfHire;
	}

	/**
	 * @return the dateOfLastReview
	 */
	public FinanceDate getFinanceDateOfLastReview() {
		return dateOfLastReview;
	}

	/**
	 * @return the dateOfRelease
	 */
	public FinanceDate getDateOfRelease() {
		return dateOfRelease;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.SALES_PERSON);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		super.onSave(arg0);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(arg0);
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!UserUtils.canDoThis(SalesPerson.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		return true;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {
		this.firstName = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.SALES_PERSON;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.salesPerson()).gap();

		w.put(messages.title(), this.title);
		w.put(messages.firstName(), this.firstName);

		w.put(messages.lastName(), this.lastName);

		w.put(messages.jobTitle(), this.jobTitle);
		if (this.expenseAccount != null)
			w.put(messages.expenseAccount(), this.expenseAccount.getName());

		w.put(messages.gender(), this.gender);

		if (this.dateOfBirth != null)
			w.put(messages.dateofBirth(), this.dateOfBirth.toString());

		if (this.dateOfHire != null)
			w.put(messages.dateofHire(), this.dateOfHire.toString());

		if (this.dateOfLastReview != null)
			w.put(messages.dateofLastReview(), this.dateOfLastReview.toString());

		if (this.dateOfRelease != null)
			w.put(messages.dateofRelease(), this.dateOfRelease.toString());

		w.put(messages.memo(), this.memo);
		w.put(messages.fileAs(), this.fileAs);

		if (this.address != null)
			w.put(messages.address(), this.address.toString());

		w.put(messages.phone(), this.phoneNo);

		w.put(messages.faxNumber(), this.faxNo);
		w.put(messages.email(), this.email);

		w.put(messages.webPageAddress(), this.webPageAddress);

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (firstName == null || firstName.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().salesPerson());
		}

		Set<SalesPerson> salesPersons = getCompany().getSalesPersons();
		for (SalesPerson salesPerson : salesPersons) {
			if (salesPerson.getFirstName().equalsIgnoreCase(getFirstName())
					&& salesPerson.getID() != getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_ALREADY_EXIST, Global
								.get().messages().name());
			}
		}

	}
}
