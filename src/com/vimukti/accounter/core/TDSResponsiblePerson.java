package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TDSResponsiblePerson extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private long panRegistrationNumber;

	private long mobileNumber;
	private long stdCode;

	@Override
	public String getName() {
		return getResponsibleName();
	}

	@Override
	public void setName(String name) {
		setResponsibleName(name);
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

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getResponsibleName() {
		return responsibleName;
	}

	public void setResponsibleName(String responsibleName) {
		this.responsibleName = responsibleName;
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

	public long getPanRegistrationNumber() {
		return panRegistrationNumber;
	}

	public void setPanRegistrationNumber(long panRegistrationNumber) {
		this.panRegistrationNumber = panRegistrationNumber;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public long getStdCode() {
		return stdCode;
	}

	public void setStdCode(long stdCode) {
		this.stdCode = stdCode;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.TDSRESPONSIBLEPERSON;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		getCompany().setTdsResposiblePerson(this);
		session.saveOrUpdate(getCompany());
		return super.onSave(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (responsibleName == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().name());
		}
		if (designation == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().designation());
		}
		
		if (flatNo == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().flatNo());
		}
		if (city == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().cityOrTown());
		}
		if (pinCode == 0) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().postalCode());
		}
		if (mobileNumber == 0) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().mobileNumber());
		}
		if (returnType == 0) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().retutnType());
		}
		
	}

}
