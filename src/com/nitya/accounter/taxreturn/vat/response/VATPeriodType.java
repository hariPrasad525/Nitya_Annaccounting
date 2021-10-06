package com.nitya.accounter.taxreturn.vat.response;

import java.util.Date;

import net.n3.nanoxml.XMLElement;

public class VATPeriodType {
	private String periodID;
	private Date periodStartDate;
	private Date periodEndDate;

	public String getPeriodID() {
		return periodID;
	}

	public void setPeriodID(String periodID) {
		this.periodID = periodID;
	}

	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public void toXML(XMLElement vATDeclarationHeaderTypeElement) {
		XMLElement vATPeriodTypeElement = new XMLElement("VATPeriod");
		vATDeclarationHeaderTypeElement.addChild(vATPeriodTypeElement);
		if (periodID != null) {
			XMLElement periodIDElement = new XMLElement("PeriodID");
			periodIDElement.setContent(periodID);
			vATPeriodTypeElement.addChild(periodIDElement);
		}
		if (periodStartDate != null) {
			XMLElement periodStartDateElement = new XMLElement(
					"PeriodStartDate");
			periodStartDateElement.setContent(periodID);
			vATPeriodTypeElement.addChild(periodStartDateElement);
		}
		if (periodEndDate != null) {
			XMLElement periodEndDateElement = new XMLElement("periodEndDate");
			periodEndDateElement.setContent(periodID);
			vATPeriodTypeElement.addChild(periodEndDateElement);
		}
	}

}
