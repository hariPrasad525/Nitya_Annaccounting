package com.vimukti.accounter.core;

import net.n3.nanoxml.XMLElement;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IRASSupplyLineInfo implements IsSerializable {
	private String customerName;
	private String customerUEN;
	private FinanceDate invoiceDate;
	private String invoiceNo;
	private long lineNo;
	private String productDescription;
	private double supplyValueSGD;
	private double gstValueSGD;
	private String taxCode;
	private String country;
	private String fcyCode;
	private double supplyFCY;
	private double gstFCY;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerUEN() {
		return customerUEN;
	}

	public void setCustomerUEN(String customerUEN) {
		this.customerUEN = customerUEN;
	}

	public FinanceDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(FinanceDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public long getLineNo() {
		return lineNo;
	}

	public void setLineNo(long lineNo) {
		this.lineNo = lineNo;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public double getSupplyValueSGD() {
		return supplyValueSGD;
	}

	public void setSupplyValueSGD(double supplyValueSGD) {
		this.supplyValueSGD = supplyValueSGD;
	}

	public double getGSTValueSGD() {
		return gstValueSGD;
	}

	public void setGSTValueSGD(double gstValueSGD) {
		this.gstValueSGD = gstValueSGD;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFCYCode() {
		return fcyCode;
	}

	public void setFCYCode(String fcyCode) {
		this.fcyCode = fcyCode;
	}

	public double getSupplyFCY() {
		return supplyFCY;
	}

	public void setSupplyFCY(double supplyFCY) {
		this.supplyFCY = supplyFCY;
	}

	public double getGSTFCY() {
		return gstFCY;
	}

	public void setGSTFCY(double gstFCY) {
		this.gstFCY = gstFCY;
	}

	public XMLElement toXML() {
		XMLElement supplyElement = new XMLElement("SupplyLines");

		supplyElement.addChild(getXmlElement(customerName, "CustomerName"));
		supplyElement.addChild(getXmlElement(customerUEN, "CustomerUEN"));
		supplyElement.addChild(getXmlElement(invoiceDate, "InvoiceDate"));
		supplyElement.addChild(getXmlElement(invoiceNo, "InvoiceNo"));
		supplyElement.addChild(getXmlElement(lineNo, "LineNo"));
		supplyElement.addChild(getXmlElement(productDescription,
				"ProductDescription"));
		supplyElement.addChild(getXmlElement(supplyValueSGD, "SupplyValueSGD"));
		supplyElement.addChild(getXmlElement(gstValueSGD, "GSTValueSGD"));
		supplyElement.addChild(getXmlElement(taxCode, "TaxCode"));
		supplyElement.addChild(getXmlElement(country, "Country"));
		supplyElement.addChild(getXmlElement(fcyCode, "FCYCode"));
		supplyElement.addChild(getXmlElement(supplyFCY, "SupplyFCY"));
		supplyElement.addChild(getXmlElement(gstFCY, "GSTFCY"));

		return supplyElement;
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

	public String toTxt() {
		StringBuffer buffer = new StringBuffer();
		if (customerName != null) {
			buffer.append(customerName);
		}
		buffer.append("|");

		if (customerUEN != null) {
			buffer.append(customerUEN);
		}
		buffer.append("|");

		if (invoiceDate != null) {
			buffer.append(invoiceDate.toString("-"));
		}
		buffer.append("|");

		if (invoiceNo != null) {
			buffer.append(invoiceNo);
		}
		buffer.append("|");

		buffer.append(lineNo + "|");

		if (productDescription != null) {
			buffer.append(productDescription);
		}
		buffer.append("|");

		buffer.append(supplyValueSGD + "|");

		buffer.append(gstValueSGD + "|");

		if (taxCode != null) {
			buffer.append(taxCode);
		}
		buffer.append("|");

		if (country != null) {
			buffer.append(country);
		}
		buffer.append("|");

		if (fcyCode != null) {
			buffer.append(fcyCode);
		}
		buffer.append("|");

		buffer.append(supplyFCY + "|");

		buffer.append(gstFCY + "|");

		buffer.append("\n");

		return buffer.toString();
	}
}
