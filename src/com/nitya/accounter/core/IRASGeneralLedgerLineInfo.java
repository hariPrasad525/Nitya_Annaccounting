package com.nitya.accounter.core;

import net.n3.nanoxml.XMLElement;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IRASGeneralLedgerLineInfo implements IsSerializable {
	private FinanceDate transactionDate;
	private String accountID;
	private String accountName;
	private String transactionDescription;
	private String name;
	private String transactionID;
	private String sourceDocumentID;
	private String sourceType;
	private double debit;
	private double credit;
	private double balance;

	public FinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(FinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getSourceDocumentID() {
		return sourceDocumentID;
	}

	public void setSourceDocumentID(String sourceDocumentID) {
		this.sourceDocumentID = sourceDocumentID;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public double getDebit() {
		return debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public XMLElement toXML() {
		XMLElement glElement = new XMLElement("GLDataLines");

		glElement.addChild(getXmlElement(transactionDate, "TransactionDate"));
		glElement.addChild(getXmlElement(accountID, "AccountID"));
		glElement.addChild(getXmlElement(accountName, "AccountName"));
		glElement.addChild(getXmlElement(transactionDescription,
				"TransactionDescription"));
		glElement.addChild(getXmlElement(name, "Name"));
		glElement.addChild(getXmlElement(transactionID, "TransactionID"));
		glElement.addChild(getXmlElement(sourceDocumentID, "SourceDocumentID"));
		glElement.addChild(getXmlElement(sourceType, "SourceType"));
		glElement.addChild(getXmlElement(debit, "Debit"));
		glElement.addChild(getXmlElement(credit, "Credit"));
		glElement.addChild(getXmlElement(balance, "Balance"));

		return glElement;
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
		if (transactionDate != null) {
			buffer.append(transactionDate.toString("-"));
		}
		buffer.append("|");

		if (accountID != null) {
			buffer.append(accountID);
		}
		buffer.append("|");

		if (accountName != null) {
			buffer.append(accountName);
		}
		buffer.append("|");

		if (transactionDescription != null) {
			buffer.append(transactionDescription);
		}
		buffer.append("|");

		if (name != null) {
			buffer.append(name);
		}
		buffer.append("|");

		if (transactionID != null) {
			buffer.append(transactionID);
		}
		buffer.append("|");

		if (sourceDocumentID != null) {
			buffer.append(sourceDocumentID);
		}
		buffer.append("|");

		if (sourceType != null) {
			buffer.append(sourceType);
		}
		buffer.append("|");

		buffer.append(debit + "|");

		buffer.append(credit + "|");

		buffer.append(balance + "|");

		buffer.append("\n");

		return buffer.toString();
	}

}
