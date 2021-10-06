package com.nitya.accounter.core;

import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;

/**
 * The salary components constituting Pay Structures are called Pay Heads. A Pay
 * Head may be an earning, which is paid to an employee, or a deduction, which
 * is recovered from his/her salary. The value of these Pay Heads could be
 * either fixed or variable, for each Payroll period.
 * 
 * @author Prasanna Kumar G
 * 
 */
public abstract class PayHead extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_FEDERAL="Federal Income Tax";
	public static final String DEFAULT_STATE="Income Tax";
	public static final String DEFAULT_BASE_SALARY="Basic Salary";
	public static final String DEFAULT_SSN_TAX = "Social Security Tax";
	public static final String DEFAULT_MEDICARE_TAX = "Medicare Tax";
	public static final String DEFAULT_DISABILITY_TAX = "Disability Tax";
	public static final String DEFAULT_TRAINING_TAX = "Training Tax";
	public static final String DEFAULT_UNEMPLOYEMENT_TAX = "Unemployment Tax";
	public static final String DEFAULT_ATTEND_PAY="Attendance Pay";
	public static final String DEFAULT_FLAT_LOANS="Advances";
	public static final String DEFAULT_REIMBURSEMENTS_TO_EMPLOYEES="Per diem";
	public static final String DEFAULT_ATTENDENCE="Pay by Attendance";

	public static final int TYPE_EARNINGS_FOR_EMPLOYEES = 1;
	public static final int TYPE_DEDUCTIONS_FOR_EMPLOYEES = 2;
	public static final int TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS = 3;
	public static final int TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS = 4;
	public static final int TYPE_EMPLOYEES_OTHER_CHARGES = 5;
	public static final int TYPE_BONUS = 6;
	public static final int TYPE_LOANS_AND_ADVANCES = 7;
	public static final int TYPE_REIMBURSEMENTS_TO_EMPLOYEES = 8;
	public static final int TYPE_PER_MILE = 9;


	public static final int CALCULATION_TYPE_ON_ATTENDANCE = 1;
	public static final int CALCULATION_TYPE_AS_COMPUTED_VALUE = 2;
	public static final int CALCULATION_TYPE_FLAT_RATE = 3;
	public static final int CALCULATION_TYPE_ON_PRODUCTION = 4;
	public static final int CALCULATION_TYPE_AS_USER_DEFINED = 5;
	public static final int CALCULATION_TYPE_AS_TAX = 6;

	public static final int CALCULATION_PERIOD_DAYS = 1;
	public static final int CALCULATION_PERIOD_WEEKS = 2;
	public static final int CALCULATION_PERIOD_MONTHS = 3;
	public static final int CALCULATION_PERIOD_FOR_NIGHTS = 4;
	public static final int CALCULATION_PERIOD_HOURS = 5;
	public static final int CALCULATION_PERIOD_SALARY = 6;

	public static final int ROUNDING_METHOD_DOWNWORD = 1;
	public static final int ROUNDING_METHOD_NORMAL = 2;
	public static final int ROUNDING_METHOD_UPWORD = 3;

	private String name;

	private int type;

	private String nameToAppearInPaySlip;

	private int calculationType;

	private int roundingMethod;
	
	private boolean isDefault;

	private boolean isAffectNetSalary;

	private List<PayHeadField> companyFields;

	private List<PayHeadField> employeeFields;

	/**
	 * Expense Account of this PayHead
	 */
	private Account account;

	/**
	 * Exists only for Statutory PayHeads
	 */
	private Account liabilityAccount;

	/**
	 * Exists only for Loans And Advances PayHeads
	 */
	private Account assetAccount;
	
	private boolean isEmployeerDeduction;

	public boolean isEmployeerDeduction() {
		return isEmployeerDeduction;
	}

	public void setEmployeerDeduction(boolean isEmployeerDeduction) {
		this.isEmployeerDeduction = isEmployeerDeduction;
	}

	public PayHead() {
		// TODO Auto-generated constructor stub
	}

	public PayHead(int calculationType) {
		this.calculationType = calculationType;
	}
	
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the companyFields
	 */
	public List<PayHeadField> getCompanyFields() {
		return companyFields;
	}

	/**
	 * @param companyFields
	 *            the companyFields to set
	 */
	public void setCompanyFields(List<PayHeadField> companyFields) {
		this.companyFields = companyFields;
	}

	/**
	 * @return the employeeFields
	 */
	public List<PayHeadField> getEmployeeFields() {
		return employeeFields;
	}

	/**
	 * @param employeeFields
	 *            the employeeFields to set
	 */
	public void setEmployeeFields(List<PayHeadField> employeeFields) {
		this.employeeFields = employeeFields;
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
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the nameToAppearInPaySlip
	 */
	public String getNameToAppearInPaySlip() {
		return nameToAppearInPaySlip;
	}

	/**
	 * @param nameToAppearInPaySlip
	 *            the nameToAppearInPaySlip to set
	 */
	public void setNameToAppearInPaySlip(String nameToAppearInPaySlip) {
		this.nameToAppearInPaySlip = nameToAppearInPaySlip;
	}

	/**
	 * @return the calculationType
	 */
	public int getCalculationType() {
		return calculationType;
	}

	/**
	 * @param calculationType
	 *            the calculationType to set
	 */
	public void setCalculationType(int calculationType) {
		this.calculationType = calculationType;
	}

	/**
	 * @return the roundingMethod
	 */
	public int getRoundingMethod() {
		return roundingMethod;
	}

	/**
	 * @param roundingMethod
	 *            the roundingMethod to set
	 */
	public void setRoundingMethod(int roundingMethod) {
		this.roundingMethod = roundingMethod;
	}

	/**
	 * @return the isAffectNetSalary
	 */
	public boolean isAffectNetSalary() {
		return isAffectNetSalary;
	}

	/**
	 * @param isAffectNetSalary
	 *            the isAffectNetSalary to set
	 */
	public void setAffectNetSalary(boolean isAffectNetSalary) {
		this.isAffectNetSalary = isAffectNetSalary;
	}

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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.CREATION_SUCCESS);
		accounterCore.setID(getID());
		accounterCore.setObjectType(AccounterCoreType.PAY_HEAD);
		ChangeTracker.put(accounterCore);
		return super.onSave(session);
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(getID());
		accounterCore.setObjectType(AccounterCoreType.PAY_HEAD);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		PayHead payhead = (PayHead) clientObject;
		Query query = session
				.getNamedQuery("getPayhead.by.Name")
				.setParameter("name", payhead.name,
						EncryptedStringType.INSTANCE)
				.setParameter("id", payhead.getID())
				.setEntity("company", payhead.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			PayHead newPayhead = (PayHead) list.get(0);
			if (payhead.getID() != newPayhead.getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
			}
		}
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		AccounterMessages2 messages2 = Global.get().messages2();
		w.put(messages.name(), this.name);
		w.put(messages.payHeadType(), this.type);
		if (this.getAccount() != null) {
			w.put(messages.expenseAccount(), this.getAccount().toString());
		}
		if (getLiabilityAccount() != null) {
			w.put(messages2.statutoryLiabilityAccount(), this
					.getLiabilityAccount().toString());
		}

		if (getAssetAccount() != null) {
			w.put(messages2.assetAccount(), this.getAssetAccount().toString());
		}
		w.put(messages.calculationType(), this.calculationType);
	}

	public boolean isEarning() {
		return getType() == TYPE_EARNINGS_FOR_EMPLOYEES
				|| getType() == TYPE_REIMBURSEMENTS_TO_EMPLOYEES
				|| getType() == TYPE_BONUS
                || getType() == TYPE_PER_MILE;

	}

	public boolean isDeduction() {
		return getType() == TYPE_DEDUCTIONS_FOR_EMPLOYEES
				|| getType() == TYPE_EMPLOYEES_OTHER_CHARGES
				|| getType() == TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS
				|| getType() == TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS
				|| getType() == TYPE_LOANS_AND_ADVANCES;
	}

	public double calculatePayment(ClientEmployeePayHeadComponent component, PayStructureItem payStructureItem,
			double deductions, double earnings) {
		return 0;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.PAY_HEAD;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.name == null || this.name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					getName());
		}

		Set<PayHead> payHeads = getCompany().getPayHeads();
		for (PayHead ph : payHeads) {
			if (ph.getID() == getID()) {
				continue;
			}
			if (ph.getName().equalsIgnoreCase(getName())) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_ALREADY_EXIST, getName());
			}
		}
	

		if (getType() == TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS
				|| getType() == TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS) {
			if (getLiabilityAccount() == null) {
				throw new AccounterException(
						AccounterException.ERROR_ACCOUNT_NULL,
						"Statutory Liability Account");
			}
		}

		if (getType() != TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS) {
			if (getAccount() == null) {
				throw new AccounterException(
						AccounterException.ERROR_ACCOUNT_NULL,
						"Expense Account");
			}
		}

	}

	/**
	 * @return the payableAccount
	 */
	public Account getLiabilityAccount() {
		return liabilityAccount;
	}

	/**
	 * @param liabilityAccount
	 *            the payableAccount to set
	 */
	public void setLiabilityAccount(Account liabilityAccount) {
		this.liabilityAccount = liabilityAccount;
	}

	/**
	 * @return the assetAccount
	 */
	public Account getAssetAccount() {
		return assetAccount;
	}

	/**
	 * @param assetAccount
	 *            the assetAccount to set
	 */
	public void setAssetAccount(Account assetAccount) {
		this.assetAccount = assetAccount;
	}

	public boolean isEffectsPayment() {
		return getType() != TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS;
	}
}
