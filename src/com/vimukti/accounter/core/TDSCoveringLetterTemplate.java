package com.vimukti.accounter.core;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class TDSCoveringLetterTemplate {

	CoverClass coverClass;

	public TDSCoveringLetterTemplate() {

		coverClass = new CoverClass();
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		context.put("cover", coverClass);
		context.put("cov", coverClass);
		return context;
	}

	public class CoverClass {

		private String cName;
		private String buildingName;
		private String streetName;
		private String cityName;
		private String stateName;
		private String pinCode;
		private String stdCode;
		private String telePhoneNumber;
		private String emailAddress;
		private String deducteeName;
		private String date;
		private String certificateNumber;
		private String amount;
		private String text;
		private String responsiblePersonName;
		private String responsiblePersonDesignation;

		public CoverClass() {
		}

		public String getcName() {
			return cName;
		}

		public void setcName(String cName) {
			this.cName = cName;
		}

		public String getBuildingName() {
			return buildingName;
		}

		public String getStreetName() {
			return streetName;
		}

		public String getCityName() {
			return cityName;
		}

		public String getStateName() {
			return stateName;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public String getDeducteeName() {
			return deducteeName;
		}

		public String getDate() {
			return date;
		}

		public String getCertificateNumber() {
			return certificateNumber;
		}

		public String getAmount() {
			return amount;
		}

		public String getText() {
			return text;
		}

		public String getResponsiblePersonName() {
			return responsiblePersonName;
		}

		public String getResponsiblePersonDesignation() {
			return responsiblePersonDesignation;
		}

		public void setCompanyName(String companyName) {
			this.cName = companyName;
		}

		public void setBuildingName(String buildingName) {
			this.buildingName = buildingName;
		}

		public void setStreetName(String streetName) {
			this.streetName = streetName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public void setStateName(String stateName) {
			this.stateName = stateName;
		}

		public String getPinCode() {
			return pinCode;
		}

		public void setPinCode(String pinCode) {
			this.pinCode = pinCode;
		}

		public String getStdCode() {
			return stdCode;
		}

		public void setStdCode(String stdCode) {
			this.stdCode = stdCode;
		}

		public String getTelePhoneNumber() {
			return telePhoneNumber;
		}

		public void setTelePhoneNumber(String telePhoneNumber) {
			this.telePhoneNumber = telePhoneNumber;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}

		public void setDeducteeName(String deducteeName) {
			this.deducteeName = deducteeName;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public void setCertificateNumber(String certificateNumber) {
			this.certificateNumber = certificateNumber;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public void setText(String text) {
			this.text = text;
		}

		public void setResponsiblePersonName(String responsiblePersonName) {
			this.responsiblePersonName = responsiblePersonName;
		}

		public void setResponsiblePersonDesignation(
				String responsiblePersonDesignation) {
			this.responsiblePersonDesignation = responsiblePersonDesignation;
		}

	}

	public void setValues(Vendor ven, Company company,
			TDSResponsiblePerson responsiblePersonDetails) {

		coverClass.setCompanyName(company.getDisplayName());
		coverClass.setDeducteeName(ven.getName());
		coverClass.setResponsiblePersonName(responsiblePersonDetails
				.getResponsibleName());
		coverClass.setResponsiblePersonDesignation(responsiblePersonDetails
				.getDesignation());
	}
}
