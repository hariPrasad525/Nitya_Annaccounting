package com.vimukti.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class GovtTalkDetails {
	/**
	 * 0..1
	 */
	private Keys keys = new Keys();
	/**
	 * 0..1
	 */
	private TargetDetails targetDetails = new TargetDetails();
	/**
	 * 0..1
	 */
	private GatewayValidation gatewayValidation = new GatewayValidation();
	/**
	 * 0..∞
	 */
	private List<ChannelRouting> channelRoutings = new ArrayList<ChannelRouting>();
	/**
	 * 0..1
	 */
	private GovTalkErrors govTalkErrors = new GovTalkErrors();
	/**
	 * 0..1
	 */
	private GatewayAdditions gatewayAdditions = new GatewayAdditions();

	public GovtTalkDetails() {
	}

	public TargetDetails getTargetDetails() {
		return targetDetails;
	}

	public void setTargetDetails(TargetDetails targetDetails) {
		this.targetDetails = targetDetails;
	}

	public Keys getKeys() {
		return keys;
	}

	public void setKeys(Keys keys) {
		this.keys = keys;
	}

	public GatewayValidation getGatewayValidation() {
		return gatewayValidation;
	}

	public void setGatewayValidation(GatewayValidation gatewayValidation) {
		this.gatewayValidation = gatewayValidation;
	}

	public GatewayAdditions getGatewayAdditions() {
		return gatewayAdditions;
	}

	public void setGatewayAdditions(GatewayAdditions gatewayAdditions) {
		this.gatewayAdditions = gatewayAdditions;
	}

	public GovTalkErrors getGovTalkErrors() {
		return govTalkErrors;
	}

	public void setGovTalkErrors(GovTalkErrors govTalkErrors) {
		this.govTalkErrors = govTalkErrors;
	}

	public List<ChannelRouting> getChannelRoutings() {
		return channelRoutings;
	}

	public void setChannelRoutings(List<ChannelRouting> channelRoutings) {
		this.channelRoutings = channelRoutings;
	}

	public IXMLElement toXML() {
		XMLElement govtTalkDetailsElement = new XMLElement("GovTalkDetails");
		if (keys != null) {
			govtTalkDetailsElement.addChild(keys.toXML());
		}
		if (targetDetails != null) {
			govtTalkDetailsElement.addChild(targetDetails.toXML());
		}
		if (gatewayValidation != null) {
			govtTalkDetailsElement.addChild(gatewayValidation.toXML());
		}
		if (channelRoutings != null) {
			for (ChannelRouting channelRouting : channelRoutings) {
				govtTalkDetailsElement.addChild(channelRouting.toXML());
			}
		}
		if (govTalkErrors != null) {
			govtTalkDetailsElement.addChild(govTalkErrors.toXML());
		}
		if (gatewayAdditions != null) {
			govtTalkDetailsElement.addChild(gatewayAdditions.toXML());
		}
		return govtTalkDetailsElement;

	}

}
