package com.nitya.accounter.taxreturn.vat.request;

import java.util.List;

import com.nitya.accounter.core.Box;
import com.nitya.accounter.core.TAXReturn;

import net.n3.nanoxml.XMLElement;

public class VATDeclarationRequest {
	private double vatDueOnOutputs;
	private double vatDueOnECAcquisitions;
	private double totalVAT;
	private double vatReclaimedOnInputs;
	private double netVAT;
	private int netSalesAndOutputs;
	private int netPurchasesAndInputs;
	private int netECSupplies;
	private int netECAcquisitions;
	private Integer aasBalancingPayment;
	private String extensionPart;
	private String finalReturn;

	public VATDeclarationRequest(TAXReturn taxReturn) {
		List<Box> boxes = taxReturn.getBoxes();
		vatDueOnOutputs = getBox(boxes, 1).getAmount();

		vatDueOnECAcquisitions = getBox(boxes, 2).getAmount();

		totalVAT = getBox(boxes, 3).getAmount();

		vatReclaimedOnInputs = getBox(boxes, 4).getAmount();

		netVAT = getBox(boxes, 5).getAmount();

		netSalesAndOutputs = (int) getBox(boxes, 6).getAmount();

		netPurchasesAndInputs = (int) getBox(boxes, 7).getAmount();

		netECSupplies = (int) getBox(boxes, 8).getAmount();

		netECAcquisitions = (int) getBox(boxes, 9).getAmount();

		// aasBalancingPayment = (int) getBox(boxes, 10).getAmount();

		// extensionPart=?

		// finalReturn=?
	}

	private Box getBox(List<Box> boxes, int i) {
		for (Box b : boxes) {
			if (b.getBoxNumber() == i) {
				return b;
			}
		}
		return null;
	}

	public double getVatDueOnOutputs() {
		return vatDueOnOutputs;
	}

	public void setVatDueOnOutputs(double vatDueOnOutputs) {
		this.vatDueOnOutputs = vatDueOnOutputs;
	}

	public double getVatDueOnECAcquisitions() {
		return vatDueOnECAcquisitions;
	}

	public void setVatDueOnECAcquisitions(double vatDueOnECAcquisitions) {
		this.vatDueOnECAcquisitions = vatDueOnECAcquisitions;
	}

	public double getTotalVAT() {
		return totalVAT;
	}

	public void setTotalVAT(double totalVAT) {
		this.totalVAT = totalVAT;
	}

	public double getVatReclaimedOnInputs() {
		return vatReclaimedOnInputs;
	}

	public void setVatReclaimedOnInputs(double vatReclaimedOnInputs) {
		this.vatReclaimedOnInputs = vatReclaimedOnInputs;
	}

	public double getNetVAT() {
		return netVAT;
	}

	public void setNetVAT(double netVAT) {
		this.netVAT = netVAT;
	}

	public int getNetSalesAndOutputs() {
		return netSalesAndOutputs;
	}

	public void setNetSalesAndOutputs(int netSalesAndOutputs) {
		this.netSalesAndOutputs = netSalesAndOutputs;
	}

	public int getNetPurchasesAndInputs() {
		return netPurchasesAndInputs;
	}

	public void setNetPurchasesAndInputs(int netPurchasesAndInputs) {
		this.netPurchasesAndInputs = netPurchasesAndInputs;
	}

	public int getNetECSupplies() {
		return netECSupplies;
	}

	public void setNetECSupplies(int netECSupplies) {
		this.netECSupplies = netECSupplies;
	}

	public int getAasBalancingPayment() {
		return aasBalancingPayment;
	}

	public void setAasBalancingPayment(int aasBalancingPayment) {
		this.aasBalancingPayment = aasBalancingPayment;
	}

	public int getNetECAcquisitions() {
		return netECAcquisitions;
	}

	public void setNetECAcquisitions(int netECAcquisitions) {
		this.netECAcquisitions = netECAcquisitions;
	}

	public String getExtensionPart() {
		return extensionPart;
	}

	public void setExtensionPart(String extensionPart) {
		this.extensionPart = extensionPart;
	}

	public String getFinalReturn() {
		return finalReturn;
	}

	public void setFinalReturn(String finalReturn) {
		this.finalReturn = finalReturn;
	}

	public void toXML(XMLElement iRenvelopeElement) {
		XMLElement vATDeclarationRequestElement = new XMLElement(
				"VATDeclarationRequest");
		iRenvelopeElement.addChild(vATDeclarationRequestElement);
		XMLElement vATDueOnOutputsElement = new XMLElement("VATDueOnOutputs");
		vATDueOnOutputsElement.setContent(Double.toString(vatDueOnOutputs));
		vATDeclarationRequestElement.addChild(vATDueOnOutputsElement);
		XMLElement vATDueOnECAcquisitionsElement = new XMLElement(
				"VATDueOnECAcquisitions");
		vATDueOnECAcquisitionsElement.setContent(Double
				.toString(vatDueOnECAcquisitions));
		vATDeclarationRequestElement.addChild(vATDueOnECAcquisitionsElement);
		XMLElement totalVATElement = new XMLElement("TotalVAT");
		totalVATElement.setContent(Double.toString(totalVAT));
		vATDeclarationRequestElement.addChild(totalVATElement);
		XMLElement vatReclaimedOnInputsElement = new XMLElement(
				"VATReclaimedOnInputs");
		vatReclaimedOnInputsElement.setContent(Double
				.toString(vatReclaimedOnInputs));
		vATDeclarationRequestElement.addChild(vatReclaimedOnInputsElement);
		XMLElement netVATElement = new XMLElement("NetVAT");
		netVATElement.setContent(Double.toString(netVAT));
		vATDeclarationRequestElement.addChild(netVATElement);
		XMLElement netSalesAndOutputsElement = new XMLElement(
				"NetSalesAndOutputs");
		netSalesAndOutputsElement.setContent(Integer
				.toString(netSalesAndOutputs));
		vATDeclarationRequestElement.addChild(netSalesAndOutputsElement);
		XMLElement netPurchasesAndInputsElement = new XMLElement(
				"NetPurchasesAndInputs");
		netPurchasesAndInputsElement.setContent(Integer
				.toString(netPurchasesAndInputs));
		vATDeclarationRequestElement.addChild(netPurchasesAndInputsElement);
		XMLElement netECSuppliesElement = new XMLElement("NetECSupplies");
		netECSuppliesElement.setContent(Integer.toString(netECSupplies));
		vATDeclarationRequestElement.addChild(netECSuppliesElement);

		XMLElement netECAcquisitionsElement = new XMLElement(
				"NetECAcquisitions");
		netECAcquisitionsElement
				.setContent(Integer.toString(netECAcquisitions));
		vATDeclarationRequestElement.addChild(netECAcquisitionsElement);

		if (aasBalancingPayment != null) {
			XMLElement aasBalancingPaymentElement = new XMLElement(
					"AASBalancingPayment");
			aasBalancingPaymentElement.setContent(Integer
					.toString(aasBalancingPayment));
			vATDeclarationRequestElement.addChild(aasBalancingPaymentElement);
		}
		if (extensionPart != null) {
			XMLElement extensionPartElement = new XMLElement("ExtensionPart");
			extensionPartElement.setContent(extensionPart);
			vATDeclarationRequestElement.addChild(extensionPartElement);
		}
		if (finalReturn != null) {
			vATDeclarationRequestElement.setAttribute("FinalReturn",
					finalReturn);
		}
	}

}
