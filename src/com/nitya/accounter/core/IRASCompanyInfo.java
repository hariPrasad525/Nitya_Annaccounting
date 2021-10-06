package com.nitya.accounter.core;

import java.io.IOException;
import java.io.Serializable;

import net.n3.nanoxml.XMLElement;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IRASCompanyInfo implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String companyName;
	private String companyUEN;
	private String gstNo;
	private FinanceDate periodStart;
	private FinanceDate periodEnd;
	private FinanceDate iafCreationDate;
	private String productVersion;
	private String iafVersion;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyUEN() {
		return companyUEN;
	}

	public void setCompanyUEN(String companyUEN) {
		this.companyUEN = companyUEN;
	}

	public String getGSTNo() {
		return gstNo;
	}

	public void setGSTNo(String gSTNo) {
		this.gstNo = gSTNo;
	}

	public FinanceDate getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(FinanceDate periodStart) {
		this.periodStart = periodStart;
	}

	public FinanceDate getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(FinanceDate periodEnd) {
		this.periodEnd = periodEnd;
	}

	public FinanceDate getIAFCreationDate() {
		return iafCreationDate;
	}

	public void setIAFCreationDate(FinanceDate iAFCreationDate) {
		this.iafCreationDate = iAFCreationDate;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getIAFVersion() {
		return iafVersion;
	}

	public void setIAFVersion(String iAFVersion) {
		this.iafVersion = iAFVersion;
	}

	public XMLElement toXML() {
		XMLElement companyInfoElement = new XMLElement("CompanyInfo");

		companyInfoElement.addChild(getXmlElement(companyName, "CompanyName"));
		companyInfoElement.addChild(getXmlElement(companyUEN, "CompanyUEN"));
		companyInfoElement.addChild(getXmlElement(gstNo, "GSTNo"));
		companyInfoElement.addChild(getXmlElement(periodStart, "PeriodStart"));
		companyInfoElement.addChild(getXmlElement(periodEnd, "PeriodEnd"));
		companyInfoElement.addChild(getXmlElement(iafCreationDate,
				"IAFCreationDate"));
		companyInfoElement.addChild(getXmlElement(productVersion,
				"ProductVersion"));
		companyInfoElement.addChild(getXmlElement(iafVersion, "IAFVersion"));
		return companyInfoElement;
	}

	private XMLElement getXmlElement(Object value, String name) {
		XMLElement xmlElement = new XMLElement(name);
		if (value != null) {
			String string = value.toString();
			if (value instanceof FinanceDate) {
				string = ((FinanceDate) value).toString("-");
			}
			xmlElement.setContent(string);
		}
		return xmlElement;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(companyName + "|");
		buffer.append(companyUEN + "|");
		buffer.append(gstNo + "|");
		buffer.append(periodStart.toString("-") + "|");
		buffer.append(periodEnd.toString("-") + "|");
		buffer.append(iafCreationDate.toString("-") + "|");
		buffer.append(productVersion + "|");
		buffer.append(iafVersion + "|");
		buffer.append("\n");

		return buffer.toString();
	}

	public String toTxt() throws IOException {
		StringBuffer buffer = new StringBuffer();
		if (companyName != null) {
			buffer.append(companyName);
		}
		buffer.append("|");
		if (companyUEN != null) {
			buffer.append(companyUEN);
		}
		buffer.append("|");
		if (gstNo != null) {
			buffer.append(gstNo);
		}
		buffer.append("|");
		if (periodStart != null) {
			buffer.append(periodStart.toString("-"));
		}
		buffer.append("|");
		if (periodEnd != null) {
			buffer.append(periodEnd.toString("-"));
		}
		buffer.append("|");
		if (iafCreationDate != null) {
			buffer.append(iafCreationDate.toString("-"));
		}
		buffer.append("|");
		if (productVersion != null) {
			buffer.append(productVersion);
		}
		buffer.append("|");
		if (iafVersion != null) {
			buffer.append(iafVersion);
		}
		buffer.append("|");
		buffer.append("\n");

		return buffer.toString();
	}

}
