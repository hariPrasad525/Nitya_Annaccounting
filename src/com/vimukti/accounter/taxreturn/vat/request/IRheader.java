package com.vimukti.accounter.taxreturn.vat.request;

import net.n3.nanoxml.XMLElement;

import com.vimukti.accounter.taxreturn.core.Keys;

public class IRheader {

	private Keys keys = new Keys();
	private String periodID;
	private String periodStart;
	private String periodEnd;
	private Principal principal;
	private Agent agent;
	private String defaultCurrency;
	private Manifest manifest;
	private IRmark iRmark;
	private String sender;

	public Keys getKeys() {
		return keys;
	}

	public void setKeys(Keys keys) {
		this.keys = keys;
	}

	public String getPeriodID() {
		return periodID;
	}

	public void setPeriodID(String periodID) {
		this.periodID = periodID;
	}

	public String getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(String periodStart) {
		this.periodStart = periodStart;
	}

	public String getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public Manifest getManifest() {
		return manifest;
	}

	public void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}

	public IRmark getiRmark() {
		return iRmark;
	}

	public void setiRmark(IRmark iRmark) {
		this.iRmark = iRmark;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void toXML(XMLElement iRenvelopeElement) {

		XMLElement iRheaderElement = new XMLElement("IRheader");
		iRenvelopeElement.addChild(iRheaderElement);
		if (keys != null) {
			iRheaderElement.addChild(keys.toXML());
		}
		if (periodID != null) {
			XMLElement periodIDElement = new XMLElement("PeriodID");
			periodIDElement.setContent(periodID);
			iRheaderElement.addChild(periodIDElement);
		}
		if (periodStart != null) {
			XMLElement periodStartElement = new XMLElement("PeriodStart");
			periodStartElement.setContent(periodStart.toString());
			iRheaderElement.addChild(periodStartElement);
		}
		if (periodEnd != null) {
			XMLElement periodEndElement = new XMLElement("PeriodEnd");
			periodEndElement.setContent(periodEnd.toString());
			iRheaderElement.addChild(periodEndElement);
		}
		if (principal != null) {
			principal.toXML(iRheaderElement);
		}
		if (agent != null) {
			agent.toXML(iRheaderElement);
		}
		if (defaultCurrency != null) {
			XMLElement defaultCurrencyElement = new XMLElement(
					"DefaultCurrency");
			defaultCurrencyElement.setContent(defaultCurrency);
			iRheaderElement.addChild(defaultCurrencyElement);
		}
		if (manifest != null) {
			manifest.toXML(iRheaderElement);
		}
		if (iRmark != null) {
			iRmark.toXML(iRheaderElement);
		}
		if (sender != null) {
			XMLElement senderElement = new XMLElement("Sender");
			senderElement.setContent(sender);
			iRheaderElement.addChild(senderElement);
		}

	}

}
