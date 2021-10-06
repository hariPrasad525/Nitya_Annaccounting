package com.nitya.accounter.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLWriter;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IRASInformation implements IsSerializable {
	private IRASCompanyInfo companyInfo;
	private List<IRASSupplyLineInfo> supplyLines;
	private List<IRASPurchaseLineInfo> purchaseLines;
	private List<IRASGeneralLedgerLineInfo> generalLedgerLines;

	public IRASCompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(IRASCompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	public List<IRASSupplyLineInfo> getSupplyLines() {
		return supplyLines;
	}

	public void setSupplyLines(List<IRASSupplyLineInfo> supplyLines) {
		this.supplyLines = supplyLines;
	}

	public List<IRASPurchaseLineInfo> getPurchaseLines() {
		return purchaseLines;
	}

	public void setPurchaseLines(List<IRASPurchaseLineInfo> purchaseLines) {
		this.purchaseLines = purchaseLines;
	}

	public List<IRASGeneralLedgerLineInfo> getGeneralLedgerLines() {
		return generalLedgerLines;
	}

	public void setGeneralLedgerLines(
			List<IRASGeneralLedgerLineInfo> generalLedgerLines) {
		this.generalLedgerLines = generalLedgerLines;
	}

	public void toXML(OutputStream stream) throws Exception {
		XMLWriter writer = new XMLWriter(stream);

		XMLElement element = new XMLElement("Company");

		if (companyInfo != null) {
			element.addChild(companyInfo.toXML());
		}
		if (supplyLines != null) {
			XMLElement supplyElement = new XMLElement("Supply");
			double supplyTotal = 0;
			double gstTotal = 0;
			long count = 0;
			for (IRASSupplyLineInfo supplyLineInfo : supplyLines) {
				supplyElement.addChild(supplyLineInfo.toXML());
				supplyTotal += supplyLineInfo.getSupplyValueSGD();
				gstTotal += supplyLineInfo.getGSTValueSGD();
				count++;
			}
			supplyElement.setAttribute("SupplyTotalSGD",
					String.valueOf(supplyTotal));
			supplyElement.setAttribute("GSTTotalSGC", String.valueOf(gstTotal));
			supplyElement.setAttribute("TransactionCountTotal",
					String.valueOf(count));

			element.addChild(supplyElement);
		}
		if (purchaseLines != null) {
			XMLElement purchaseElement = new XMLElement("Purchase");
			double purchaseTotal = 0;
			double gstTotal = 0;
			long count = 0;
			for (IRASPurchaseLineInfo purchaseLineInfo : purchaseLines) {
				purchaseElement.addChild(purchaseLineInfo.toXML());
				purchaseTotal += purchaseLineInfo.getPurchaseValueSGD();
				gstTotal += purchaseLineInfo.getGSTValueSGD();
				count++;
			}
			purchaseElement.setAttribute("PurchaseTotalSGD",
					String.valueOf(purchaseTotal));
			purchaseElement.setAttribute("GSTTotalSGD",
					String.valueOf(gstTotal));
			purchaseElement.setAttribute("TransactionCountTotal",
					String.valueOf(count));

			element.addChild(purchaseElement);
		}

		if (generalLedgerLines != null) {
			XMLElement glElement = new XMLElement("GLData");
			double totalDebit = 0;
			double totalCredit = 0;
			long count = 0;
			for (IRASGeneralLedgerLineInfo glLineInfo : generalLedgerLines) {
				glElement.addChild(glLineInfo.toXML());
				totalDebit += glLineInfo.getDebit();
				totalCredit += glLineInfo.getCredit();
				count++;
			}
			glElement.setAttribute("TotalDebit", String.valueOf(totalDebit));
			glElement.setAttribute("TotalCredit", String.valueOf(totalCredit));
			glElement.setAttribute("TransactionCountTotal",
					String.valueOf(count));
			glElement.setAttribute("GLTCurrency", "SGD");

			element.addChild(glElement);
		}

		writer.write(element);
		stream.flush();
		stream.close();
	}

	public void toTxt(DataOutputStream stream) throws IOException {
		if (companyInfo != null) {
			stream.write("CompanyInfoStart|\n".getBytes("UTF-8"));
			stream.write("CompanyName|CompanyUEN|GSTNo|PeriodStart|PeriodEnd|IAFCreationDate|ProductVersion|IAFVersion|\n"
					.getBytes("UTF-8"));
			stream.write(companyInfo.toTxt().getBytes("UTF-8"));
			stream.write("CompanyInfoEnd|\n".getBytes("UTF-8"));
		}

		if (purchaseLines != null) {
			stream.write("PurcDataStart|\n".getBytes("UTF-8"));
			stream.write("SupplierName|SupplierUEN|InvoiceDate|InvoiceNo|PermitNo|LineNo|ProductDescription|PurchaseValueSGD|GSTValueSGD|TaxCode|FCYCode|PurchaseFCY|GSTFCY|\n"
					.getBytes("UTF-8"));

			double supplyTotal = 0;
			double gstTotal = 0;
			long count = 0;
			for (IRASPurchaseLineInfo purchaseLineInfo : purchaseLines) {
				stream.write(purchaseLineInfo.toTxt().getBytes("UTF-8"));
				supplyTotal += purchaseLineInfo.getPurchaseValueSGD();
				gstTotal += purchaseLineInfo.getGSTValueSGD();
				count++;
			}

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("PurcDataEnd|");
			stringBuffer.append(supplyTotal + "|");
			stringBuffer.append(gstTotal + "|");
			stringBuffer.append(count + "|");
			stringBuffer.append("\n");

			stream.write(stringBuffer.toString().getBytes("UTF-8"));
		}

		if (supplyLines != null) {
			stream.write("SuppDataStart|\n".getBytes("UTF-8"));
			stream.write("CustomerName|CustomerUEN|InvoiceDate|InvoiceNo|LineNo|ProductDescription|SupplyValueSGD|GSTValueSGD|TaxCode|Country|FCYCode|SupplyFCY|GSTFCY|\n"
					.getBytes("UTF-8"));

			double supplyTotal = 0;
			double gstTotal = 0;
			long count = 0;
			for (IRASSupplyLineInfo supplyLineInfo : supplyLines) {
				stream.write(supplyLineInfo.toTxt().getBytes("UTF-8"));
				supplyTotal += supplyLineInfo.getSupplyValueSGD();
				gstTotal += supplyLineInfo.getGSTValueSGD();
				count++;
			}

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("SuppDataEnd|");
			stringBuffer.append(supplyTotal + "|");
			stringBuffer.append(gstTotal + "|");
			stringBuffer.append(count + "|");
			stringBuffer.append("\n");

			stream.write(stringBuffer.toString().getBytes("UTF-8"));
		}

		if (generalLedgerLines != null) {
			stream.write("GLDataStart|\n".getBytes("UTF-8"));
			stream.write("TransactionDate|AccountID|AccountName|TransactionDescription|Name|TransactionID|SourceDocumentID|SourceType|Debit|Credit|Balance|\n"
					.getBytes("UTF-8"));

			double totalDebit = 0;
			double totalCredit = 0;
			long count = 0;
			for (IRASGeneralLedgerLineInfo glLineInfo : generalLedgerLines) {
				stream.write(glLineInfo.toTxt().getBytes("UTF-8"));
				totalDebit += glLineInfo.getDebit();
				totalCredit += glLineInfo.getCredit();
				count++;
			}

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("GLDataEnd|");
			stringBuffer.append(totalDebit + "|");
			stringBuffer.append(totalCredit + "|");
			stringBuffer.append(count + "|");
			stringBuffer.append("SGD|");
			stringBuffer.append("\n");

			stream.write(stringBuffer.toString().getBytes("UTF-8"));
		}

		stream.flush();
		stream.close();
	}
}
