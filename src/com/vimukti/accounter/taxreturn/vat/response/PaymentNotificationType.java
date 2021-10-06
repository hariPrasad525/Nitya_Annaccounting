package com.vimukti.accounter.taxreturn.vat.response;

import net.n3.nanoxml.XMLElement;

public class PaymentNotificationType {
	private double netVAT;
	private String nilPaymentIndicator;
	private String repaymentIndicator;
	private DirectDebitPaymentStatusType directDebitPaymentStatus;
	private PaymentRequestType paymentRequest;
	private double aASBalancingPayment;

	public double getNetVAT() {
		return netVAT;
	}

	public void setNetVAT(double netVAT) {
		this.netVAT = netVAT;
	}

	public String getNilPaymentIndicator() {
		return nilPaymentIndicator;
	}

	public void setNilPaymentIndicator(String nilPaymentIndicator) {
		this.nilPaymentIndicator = nilPaymentIndicator;
	}

	public String getRepaymentIndicator() {
		return repaymentIndicator;
	}

	public void setRepaymentIndicator(String repaymentIndicator) {
		this.repaymentIndicator = repaymentIndicator;
	}

	public DirectDebitPaymentStatusType getDirectDebitPaymentStatus() {
		return directDebitPaymentStatus;
	}

	public void setDirectDebitPaymentStatus(
			DirectDebitPaymentStatusType directDebitPaymentStatus) {
		this.directDebitPaymentStatus = directDebitPaymentStatus;
	}

	public PaymentRequestType getPaymentRequest() {
		return paymentRequest;
	}

	public void setPaymentRequest(PaymentRequestType paymentRequest) {
		this.paymentRequest = paymentRequest;
	}

	public double getaASBalancingPayment() {
		return aASBalancingPayment;
	}

	public void setaASBalancingPayment(double aASBalancingPayment) {
		this.aASBalancingPayment = aASBalancingPayment;
	}

	public void toXML(XMLElement vATDeclarationResponseTypeElement) {
		XMLElement paymentNotificationTypeElement = new XMLElement(
				"PaymentNotificationType");
		vATDeclarationResponseTypeElement
				.addChild(paymentNotificationTypeElement);
		if (netVAT != 0) {
			XMLElement netVATElement = new XMLElement("NetVAT");
			netVATElement.setContent(Double.toString(netVAT));
			paymentNotificationTypeElement.addChild(netVATElement);
		}
		if (nilPaymentIndicator != null) {
			XMLElement nilPaymentIndicatorElement = new XMLElement(
					"NilPaymentIndicator");
			nilPaymentIndicatorElement.setContent(Double.toString(netVAT));
			paymentNotificationTypeElement.addChild(nilPaymentIndicatorElement);
		}
		if (repaymentIndicator != null) {
			XMLElement repaymentIndicatorElement = new XMLElement(
					"RepaymentIndicator");
			repaymentIndicatorElement.setContent(repaymentIndicator);
			paymentNotificationTypeElement.addChild(repaymentIndicatorElement);
		}
		if (directDebitPaymentStatus != null) {
			directDebitPaymentStatus.toXML(paymentNotificationTypeElement);
		}
		if (paymentRequest != null) {
			paymentRequest.toXML(paymentNotificationTypeElement);
		}
		if (aASBalancingPayment != 0) {
			XMLElement aASBalancingPaymentElement = new XMLElement(
					"AASBalancingPayment");
			aASBalancingPaymentElement.setContent(Double
					.toString(aASBalancingPayment));
			paymentNotificationTypeElement.addChild(aASBalancingPaymentElement);
		}
	}
}
