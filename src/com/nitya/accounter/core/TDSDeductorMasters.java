package com.nitya.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;

public class TDSDeductorMasters extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String deductorName;
	private String branch;
	private String flatNo;
	private String buildingName;
	private String roadName;
	private String area;
	private String city;
	private String state;
	private long pinCode;
	private boolean addressdChanged;
	private long telephoneNumber;
	private long faxNo;
	private String emailID;
	private String status;
	private String deductorType;
	private String govtState;
	private String paoCode;
	private long paoRegistration;
	private String ddoCode;
	private String ddoRegistration;
	private String ministryDeptName;
	private String ministryDeptOtherName;

	private String tanNumber;
	private String panNumber;
	private String stdCode;

	private boolean isAddressSameForResopsiblePerson;

	private Address taxOfficeAddress;

	@Override
	public String getName() {
		return deductorName;
	}

	@Override
	public void setName(String name) {
		this.deductorName = name;
	}

	public String getDeductorName() {
		return deductorName;
	}

	public void setDeductorName(String deductorName) {
		this.deductorName = deductorName;
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

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getPinCode() {
		return pinCode;
	}

	public void setPinCode(long pinCode) {
		this.pinCode = pinCode;
	}

	public boolean isAddressdChanged() {
		return addressdChanged;
	}

	public void setAddressdChanged(boolean addressdChanged) {
		this.addressdChanged = addressdChanged;
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

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeductorType() {
		return deductorType;
	}

	public void setDeductorType(String deductorType) {
		this.deductorType = deductorType;
	}

	public String getGovtState() {
		return govtState;
	}

	public void setGovtState(String govtState) {
		this.govtState = govtState;
	}

	public String getPaoCode() {
		return paoCode;
	}

	public void setPaoCode(String paoCode) {
		this.paoCode = paoCode;
	}

	public long getPaoRegistration() {
		return paoRegistration;
	}

	public void setPaoRegistration(long paoRegistration) {
		this.paoRegistration = paoRegistration;
	}

	public String getDdoCode() {
		return ddoCode;
	}

	public void setDdoCode(String ddoCode) {
		this.ddoCode = ddoCode;
	}

	public String getDdoRegistration() {
		return ddoRegistration;
	}

	public void setDdoRegistration(String ddoRegistration) {
		this.ddoRegistration = ddoRegistration;
	}

	public String getMinistryDeptName() {
		return ministryDeptName;
	}

	public void setMinistryDeptName(String ministryDeptName) {
		this.ministryDeptName = ministryDeptName;
	}

	public String getMinistryDeptOtherName() {
		return ministryDeptOtherName;
	}

	public void setMinistryDeptOtherName(String ministryDeptOtherName) {
		this.ministryDeptOtherName = ministryDeptOtherName;
	}

	public String getTanNumber() {
		return tanNumber;
	}

	public void setTanNumber(String tanNumber) {
		this.tanNumber = tanNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getStdCode() {
		return stdCode;
	}

	public void setStdCode(String stdCode) {
		this.stdCode = stdCode;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.TDSDEDUCTORMASTER;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		getCompany().setTdsDeductor(this);
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
	}

	public boolean isAddressSameForResopsiblePerson() {
		return isAddressSameForResopsiblePerson;
	}

	public void setAddressSameForResopsiblePerson(
			boolean isAddressSameForResopsiblePerson) {
		this.isAddressSameForResopsiblePerson = isAddressSameForResopsiblePerson;
	}

	public Address getTaxOfficeAddress() {
		return taxOfficeAddress;
	}

	public void setTaxOfficeAddress(Address taxOfficeAddress) {
		this.taxOfficeAddress = taxOfficeAddress;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (flatNo == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().flatNo());
		}
		if (state == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().state());
		}
		if (pinCode == 0) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().postalCode());
		}
		if (emailID == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().email());
		}
		if (panNumber == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().panNumber());
		}
		if (tanNumber == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().tanNumber());
		}
		if (status == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().status());
		}
	}
}
