package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

/**
 * Employee records the relevant information about the employee.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class Employee extends Payee implements PayStructureDestination {

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
	private EmployeeGroup group;

	/** General Information */

	/**
	 * Employee Number
	 */
	private String number;
	
	/**
	 * Employee LastName 
	 */
	private String eLastname;

	
	
	/**
	 * Employee SSN 
	 */
	private String ssn;


	/**
	 * Designation of the Employee
	 */
	private String designation;

	/**
	 * Location of the Employee
	 */
	private String location;

	/**
	 * Date of Birth of the Employee. And this will be used to find if the Employee
	 * is a senior citizen
	 */
	private FinanceDate dateOfBirth;

	/**
	 * Gender of the Employee
	 */
	private int gender;

	/** Payment Details */

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
	private FinanceDate passportExpiryDate;

	/**
	 * Visa Number of the Employee
	 */
	private String visaNumber;

	/**
	 * Expiry Date of the Visa
	 */
	private FinanceDate visaExpiryDate;

	/**
	 * lastdate of employee
	 */

	private FinanceDate lastDate;
	/**
	 * reason type for employee inactive
	 */
	private int reasonType;

	/**
	 * Employee Tax Info
	 */

	private List<EmployeeTax> employeeTaxes;
	private List<EmployeeAttendance> employeeAttendances;

	private double salary;
	private int payType;
	private int payFrequency;
	private double additionalAmount;
	
	public String geteLastname() {
		return eLastname;
	}

	public void seteLastname(String eLastname) {
		this.eLastname = eLastname;
	}
	
	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public int getPayFrequency() {
		return payFrequency;
	}

	public void setPayFrequency(Integer payFrequency) {
		this.payFrequency = payFrequency;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		if (salary == null) {
			salary = 0.0;
		}
		this.salary = salary;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public double getAdditionalAmount() {
		return additionalAmount;
	}

	public void setAdditionalAmount(Double additionalAmount) {
		this.additionalAmount = additionalAmount;
	}

	private Employee manager;

	private int employeType;

	public int getEmployeType() {
		return employeType;
	}

	public void setEmployeType(int employeType) {
		this.employeType = employeType;
	}

	public List<EmployeeTax> getEmployeeTaxes() {
		return employeeTaxes;
	}

	public void setEmployeeTaxes(List<EmployeeTax> employeeTaxes) {
		this.employeeTaxes = employeeTaxes;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	/** Contact Details */

	/**
	 * @return the group
	 */
	public EmployeeGroup getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(EmployeeGroup group) {
		this.group = group;
	}

	/**
	 * @return the dateOfBirth
	 */
	public FinanceDate getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(FinanceDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
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
	 * @param employeeNumber the employeeNumber to set
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
	 * @param designation the designation to set
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
	 * @param location the location to set
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
	 * @param passportNumber the passportNumber to set
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
	 * @param countryOfIssue the countryOfIssue to set
	 */
	public void setCountryOfIssue(String countryOfIssue) {
		this.countryOfIssue = countryOfIssue;
	}

	/**
	 * @return the passportExpiryDate
	 */
	public FinanceDate getPassportExpiryDate() {
		return passportExpiryDate;
	}

	/**
	 * @param passportExpiryDate the passportExpiryDate to set
	 */
	public void setPassportExpiryDate(FinanceDate passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	/**
	 * @return the visaNumber
	 */
	public String getVisaNumber() {
		return visaNumber;
	}

	/**
	 * @param visaNumber the visaNumber to set
	 */
	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	/**
	 * @return the visaExpiryDate
	 */
	public FinanceDate getVisaExpiryDate() {
		return visaExpiryDate;
	}

	/**
	 * @param visaExpiryDate the visaExpiryDate to set
	 */
	public void setVisaExpiryDate(FinanceDate visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject, boolean goingToBeEdit) throws AccounterException {
		if (!goingToBeEdit) {
			Session session = HibernateUtil.getCurrentSession();
			FlushMode flushMode = session.getFlushMode();
			try {
				session.setFlushMode(FlushMode.COMMIT);

				Employee employee = (Employee) clientObject;
				Query query = session.getNamedQuery("getEmployee.by.Name")
						.setParameter("name", employee.name, EncryptedStringType.INSTANCE)
						.setParameter("id", employee.getID()).setEntity("company", employee.getCompany());
				List list = query.list();
				if (list != null && list.size() > 0) {
					throw new AccounterException(AccounterException.ERROR_NAME_CONFLICT);
				}
			} finally {
				session.setFlushMode(flushMode);
			}
		}
		return super.canEdit(clientObject, goingToBeEdit);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.employee()).gap();
		w.put(messages.no(), this.number);
		w.put(messages.name(), this.name);
		w.put(messages.designation(), this.designation);
		w.put(messages.location(), this.location);
		w.put(messages.gender(), this.gender);
		w.put(messages.employeeGroup(), this.group != null ? this.getGroup().toString() : "");

		AccounterMessages2 messages2 = Global.get().messages2();

		if (this.employeeTaxes != null) {
			for (EmployeeTax tax : this.employeeTaxes) {
				if(tax == null) continue;
				w.put(messages2.employeeTax(), messages2.employeeTax());
				tax.writeAudit(w);
			}
		}

		if (this.manager != null) {
			w.put(messages2.manager(), messages2.manager());
			manager.writeAudit(w);
		}

		w.put(messages2.additionalAmountForCmp(), this.additionalAmount);
		w.put(messages2.payFrequency(), this.payFrequency + "");
		w.put(messages2.salary(), this.salary + "");
		w.put(messages2.payType(), this.payType + "");

	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(getID());
		accounterCore.setObjectType(AccounterCoreType.EMPLOYEE);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}

	@Override
	public int getObjType() {
		return IAccounterCore.EMPLOYEE;
	}

	@Override
	public Account getAccount() {
		return getCompany().getSalariesPayableAccount();
	}

	@Override
	protected String getPayeeName() {
		return Global.get().messages().employee();
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		super.onSave(session);
		isOnSaveProccessed = true;
		setType(TYPE_EMPLOYEE);

		if (this.employeeTaxes != null) {
			for (EmployeeTax tax : this.employeeTaxes) {
				tax.setCompany(getCompany());
			}
		}

		List<PayHead> payHeads = getDefaultPayHeads(session, AttendancePayHead.class.getName(), TaxComputationPayHead.class.getName(), FlatRatePayHead.class.getName());
		if (this.getID() == 0 && !payHeads.isEmpty()) {
			PayStructure payStructure = new PayStructure();
			payStructure.setCompany(getCompany());
			payStructure.setEmployee(this);
			payStructure.setEmployeeGroup(this.getGroup());
			List<PayStructureItem> items = new ArrayList<PayStructureItem>();
			for (PayHead cmp : payHeads) {
				if(cmp.getName() == null || cmp.getName().isEmpty() || !isAllowedPayHead(cmp)) continue;
				
				PayStructureItem item = new PayStructureItem();
				if(cmp instanceof TaxComputationPayHead) {
					TaxComputationPayHead taxCmp = (TaxComputationPayHead) cmp;
					if (taxCmp.getLiabilityAccount() == null || taxCmp.getLiveTaxRate() == null
							|| taxCmp.getLiveTaxRate().getTaxType() == LiveTaxRate.TAX_TYPE_UNEMPLOYEMENT) {
						continue;
					}
				} else if(cmp instanceof AttendancePayHead) {
					item.setRate(this.salary);
				} else if(cmp.getCalculationType() == PayHead.CALCULATION_TYPE_AS_USER_DEFINED) {
					if(cmp.getName() == PayHead.DEFAULT_REIMBURSEMENTS_TO_EMPLOYEES) {
						item.setRate(this.additionalAmount); 
					}
				} 
				item.setPayHead(cmp);
				item.setEffectiveFrom(new FinanceDate());
				item.setPayStructure(payStructure);
				items.add(item);
			}
			payStructure.setItems(items);
			session.save(payStructure);
		} else  if(!payHeads.isEmpty()) {
			PayStructure paySt = this.getPayStrcuture(session);
			for(PayStructureItem item : paySt.getItems()) {
				if(!isAllowedPayHead(item.getPayHead())) {
					for (PayHead cmp : payHeads) {
						if(cmp instanceof AttendancePayHead) {
							boolean change = false;
							switch (cmp.getName()) {
							case AttendancePayHead.DEFAULT_HOUR:
								change = this.payType == EmployeeCompsensation.PAY_TYPE_HOURLY || this.payType == EmployeeCompsensation.PAY_TYPE_DAILY || this.payType == EmployeeCompsensation.PAY_TYPE_ANNUAL;
							case AttendancePayHead.DEFAULT_MILE:
								change =  this.payType == EmployeeCompsensation.PAY_TYPE_MILES;
							case AttendancePayHead.DEFAULT_WORK:
								change = this.payType == EmployeeCompsensation.PAY_TYPE_PER_WORK;
							default:
								break;
							}
							if(change) {
						      item.setPayHead(cmp);
						      session.saveOrUpdate(item);
							}
						}
					}
					
				}
			}
		}
		return onUpdate(session);
	}
	
	private boolean isAllowedPayHead(PayHead payHead) {
		if(payHead instanceof AttendancePayHead) {
			switch (payHead.getName()) {
			case AttendancePayHead.DEFAULT_HOUR:
				return this.payType == EmployeeCompsensation.PAY_TYPE_HOURLY || this.payType == EmployeeCompsensation.PAY_TYPE_DAILY || this.payType == EmployeeCompsensation.PAY_TYPE_ANNUAL;
			case AttendancePayHead.DEFAULT_MILE:
				return this.payType == EmployeeCompsensation.PAY_TYPE_MILES;
			case AttendancePayHead.DEFAULT_WORK:
				return this.payType == EmployeeCompsensation.PAY_TYPE_PER_WORK;
			default:
				break;
			}
			return false;
		} else if(payHead instanceof FlatRatePayHead) {
			return false;
		}
		return true;
	}

	private List<PayHead> getDefaultPayHeads(Session session, String... nameOfClasss) {
		List<PayHead> payHeads = new ArrayList<PayHead>();
		for(String name : nameOfClasss) {
			String hqlQuery = "from " + name + " entity where entity.isDefault=:default and company=:company";
			Query query = session.createQuery(hqlQuery).setParameter("default", true).setEntity("company", this.getCompany());
			payHeads.addAll(query.list());
		}
		return payHeads;
	}
	
	private PayStructure getPayStrcuture(Session session) {
		Query query = session.getNamedQuery("getPayStructure")
				.setParameter("companyId", this.getCompany().getId())
				.setParameter("employeeId", this.getID())
				.setParameter("groupId", this.group != null ? this.group.getID() : null);
		PayStructure result = (PayStructure) query.uniqueResult();
		return result;
	}

	public FinanceDate getLastDate() {
		return lastDate;
	}

	public void setLastDate(FinanceDate lastDate) {
		this.lastDate = lastDate;
	}

	public int getReasonType() {
		return reasonType;
	}

	public void setReasonType(int reasonType) {
		this.reasonType = reasonType;
	}

	public List<EmployeeAttendance> getEmployeeAttendances() {
		return employeeAttendances;
	}

	public void setEmployeeAttendances(List<EmployeeAttendance> employeeAttendances) {
		this.employeeAttendances = employeeAttendances;
	}
}
