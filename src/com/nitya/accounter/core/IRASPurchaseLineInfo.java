package com.nitya.accounter.core;

import net.n3.nanoxml.XMLElement;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IRASPurchaseLineInfo implements IsSerializable {
	private String supplierName;
	private String supplierUEN;
	private FinanceDate invoiceDate;
	private String invoiceNo;
	private String permitNo;
	private long lineNo;
	private String productDescription;
	private double purchaseValueSGD;
	private double gstValueSGD;
	private String taxCode;
	private String fcyCode;
	private double purchaseFCY;
	private double gstFCY;

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierUEN() {
		return supplierUEN;
	}

	public void setSupplierUEN(String supplierUEN) {
		this.supplierUEN = supplierUEN;
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

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
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

	public double getPurchaseValueSGD() {
		return purchaseValueSGD;
	}

	public void setPurchaseValueSGD(double purchaseValueSGD) {
		this.purchaseValueSGD = purchaseValueSGD;
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

	public String getFCYCode() {
		return fcyCode;
	}

	public void setFCYCode(String fcyCode) {
		this.fcyCode = fcyCode;
	}

	public double getPurchaseFCY() {
		return purchaseFCY;
	}

	public void setPurchaseFCY(double purchaseFCY) {
		this.purchaseFCY = purchaseFCY;
	}

	public double getGSTFCY() {
		return gstFCY;
	}

	public void setGSTFCY(double gstFCY) {
		this.gstFCY = gstFCY;
	}

	public XMLElement toXML() {
		XMLElement purchaseElement = new XMLElement("PurchaseLines");

		purchaseElement.addChild(getXmlElement(supplierName, "SupplierName"));
		purchaseElement.addChild(getXmlElement(supplierUEN, "SupplierUEN"));
		purchaseElement.addChild(getXmlElement(invoiceDate, "InvoiceDate"));
		purchaseElement.addChild(getXmlElement(invoiceNo, "InvoiceNo"));
		purchaseElement.addChild(getXmlElement(permitNo, "PermitNo"));
		purchaseElement.addChild(getXmlElement(lineNo, "LineNo"));
		purchaseElement.addChild(getXmlElement(productDescription,
				"ProductDescription"));
		purchaseElement.addChild(getXmlElement(purchaseValueSGD,
				"PurchaseValueSGD"));
		purchaseElement.addChild(getXmlElement(gstValueSGD, "GSTValueSGD"));
		purchaseElement.addChild(getXmlElement(taxCode, "TaxCode"));
		purchaseElement.addChild(getXmlElement(fcyCode, "FCYCode"));
		purchaseElement.addChild(getXmlElement(purchaseFCY, "PurchaseFCY"));
		purchaseElement.addChild(getXmlElement(gstFCY, "GSTFCY"));

		return purchaseElement;
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
		if (supplierName != null) {
			buffer.append(supplierName);
		}
		buffer.append("|");

		if (supplierUEN != null) {
			buffer.append(supplierUEN);
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

		if (permitNo != null) {
			buffer.append(permitNo);
		}
		buffer.append("|");

		buffer.append(lineNo + "|");

		if (productDescription != null) {
			buffer.append(productDescription);
		}
		buffer.append("|");

		buffer.append(purchaseValueSGD + "|");

		buffer.append(gstValueSGD + "|");

		if (taxCode != null) {
			buffer.append(taxCode);
		}
		buffer.append("|");

		if (fcyCode != null) {
			buffer.append(fcyCode);
		}
		buffer.append("|");

		buffer.append(purchaseFCY + "|");
		buffer.append(gstFCY + "|");
		buffer.append("\n");

		return buffer.toString();
	}
}
