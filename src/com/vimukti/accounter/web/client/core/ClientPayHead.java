package com.vimukti.accounter.web.client.core;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

public class ClientPayHead implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_EARNINGS_FOR_EMPLOYEES = 1;
	public static final int TYPE_DEDUCTIONS_FOR_EMPLOYEES = 2;
	public static final int TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS = 3;
	public static final int TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS = 4;
	public static final int TYPE_EMPLOYEES_OTHER_CHARGES = 5;
	public static final int TYPE_BONUS = 6;
	public static final int TYPE_LOANS_AND_ADVANCES = 7;
	public static final int TYPE_REIMBURSEMENTS_TO_EMPLOYEES = 8;
	public static final int TYPE_PER_MILE = 9;
	public static final int TYPE_PER_HOUR= 10;

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

	private boolean isAffectNetSalary;
	
	private boolean isDefault;

	private List<ClientPayHeadField> companyFields;

	private List<ClientPayHeadField> employeeFields;
	
    private long id;

	private long account;

	private long liabilityAccount;

	private long assetAccount;
	
	private boolean isEmployeerDeduction;

	public boolean isEmployeerDeduction() {
		return isEmployeerDeduction;
	}

	public void setEmployeerDeduction(boolean isEmployeerDeduction) {
		this.isEmployeerDeduction = isEmployeerDeduction;
	}

	/**
	 * @return the companyFields
	 */
	public List<ClientPayHeadField> getCompanyFields() {
		return companyFields;
	}

	/**
	 * @param companyFields
	 *            the companyFields to set
	 */
	public void setCompanyFields(List<ClientPayHeadField> companyFields) {
		this.companyFields = companyFields;
	}
	

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}


	/**
	 * @return the employeeFields
	 */
	public List<ClientPayHeadField> getEmployeeFields() {
		return employeeFields;
	}

	/**
	 * @param employeeFields
	 *            the employeeFields to set
	 */
	public void setEmployeeFields(List<ClientPayHeadField> employeeFields) {
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

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAY_HEAD;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public long getExpenseAccount() {
		return account;
	}

	public void setExpenseAccount(long account) {
		this.account = account;
	}

	public static String getCalculationType(int type) {
		switch (type) {
		case CALCULATION_TYPE_ON_ATTENDANCE:
			return Global.get().messages().attendance();

		case CALCULATION_TYPE_AS_COMPUTED_VALUE:
			return Global.get().messages().asComputedValue();

		case CALCULATION_TYPE_FLAT_RATE:
			return Global.get().messages().flatRate();
			
		case CALCULATION_TYPE_AS_TAX:
			return Global.get().messages2().taxes();

		case CALCULATION_TYPE_ON_PRODUCTION:
			return Global.get().messages().production();

		case CALCULATION_TYPE_AS_USER_DEFINED:
			return Global.get().messages().asUserDefined();

		default:
			return null;
		}
	}

	public static String getPayHeadType(int type) {
		AccounterMessages messages = Global.get().messages();
		AccounterMessages2 messages2 = Global.get().messages2();

		switch (type) {
		case TYPE_EARNINGS_FOR_EMPLOYEES:
			return messages.earningsForEmployees();

		case TYPE_DEDUCTIONS_FOR_EMPLOYEES:
			return messages.deductionsForEmployees();

		case TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS:
			return messages.employeesStatutoryDeductions();

		case TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS:
			return messages.employeesStatutoryContributions();

		case TYPE_EMPLOYEES_OTHER_CHARGES:
			return messages.employeesOtherCharges();

		case TYPE_BONUS:
			return messages.bonus();

		case TYPE_LOANS_AND_ADVANCES:
			return messages.loansAndAdvances();

		case TYPE_REIMBURSEMENTS_TO_EMPLOYEES:
			return messages.reimbursmentsToEmployees();
		
		case TYPE_PER_MILE:
			return messages2.perMile();	
			
		case TYPE_PER_HOUR:
			return messages2.perHour();	
		

		default:
			return null;
		}
	}

	public static String getCalculationPeriod(int type) {
		AccounterMessages messages = Global.get().messages();
		AccounterMessages2 messages2 = Global.get().messages2();

		switch (type) {
		case CALCULATION_PERIOD_HOURS:
			return messages2.payTypeHourly();
			
		case CALCULATION_PERIOD_DAYS:
			return messages.days();

		case CALCULATION_PERIOD_WEEKS:
			return messages.weeks();

		case CALCULATION_PERIOD_MONTHS:
			return messages.months();

		default:
			return "";
		}
	}

	public boolean isEarning() {
		return getType() == TYPE_EARNINGS_FOR_EMPLOYEES
				|| getType() == TYPE_REIMBURSEMENTS_TO_EMPLOYEES
				|| getType() == TYPE_BONUS
		        || getType() == TYPE_PER_MILE
		        || getType() == TYPE_PER_HOUR;
          }

	public boolean isDeduction() {
		return getType() == TYPE_DEDUCTIONS_FOR_EMPLOYEES
				|| getType() == TYPE_EMPLOYEES_OTHER_CHARGES
				|| getType() == TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS
				|| getType() == TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS
				|| getType() == TYPE_LOANS_AND_ADVANCES;
		
	}

	/**
	 * @return the liabilityAccount
	 */
	public long getLiabilityAccount() {
		return liabilityAccount;
	}

	/**
	 * @param liabilityAccount
	 *            the payableAccount to set
	 */
	public void setLiabilityAccount(long liabilityAccount) {
		this.liabilityAccount = liabilityAccount;
	}

	/**
	 * @return the assetAccount
	 */
	public long getAssetAccount() {
		return assetAccount;
	}

	/**
	 * @param assetAccount
	 *            the assetAccount to set
	 */
	public void setAssetAccount(long assetAccount) {
		this.assetAccount = assetAccount;
	}
}
