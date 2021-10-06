package com.nitya.accounter.core;

import java.util.Set;

public class CustomerStatementTemplate extends TemplateBuilder implements
		ITemplate {
	Customer customerStatement;
	private String reportTitle;
	private String reportsHtml;
	private String reportDate;
	private String dateRangeHtml = "";
	private Address billingaddress;
	private Set<Address> addressListOfCustomer;

	public CustomerStatementTemplate(Customer cust, String... params) {
		super(cust.getCompany());
		imgUrl = params[2];
		style1 = params[3];
		this.reportsHtml = params[0];
		this.reportTitle = params[1];
		this.reportDate = params[4];
		if (params[5] != null && !params[5].equals("null"))
			this.dateRangeHtml = params[5];
		this.customerStatement = cust;
		init();
	}

	/**
	 * HEADER
	 */
	@Override
	public void initHeader() {

		String cmpAdd = "<br/><br/><br/><br/><br/>";
		Address registeredAddress = company.getRegisteredAddress();
		if (registeredAddress != null) {
			cmpAdd = ("<br><font style=\"font-family:sans-serif; color:#504040;\">&nbsp;"
					+ forUnusedAddress(registeredAddress.getAddress1())
					+ forUnusedAddress(registeredAddress.getStreet())
					+ forUnusedAddress(registeredAddress.getCity())
					+ forUnusedAddress(registeredAddress.getStateOrProvinence())
					+ forUnusedAddress(registeredAddress.getZipOrPostalCode())
					+ forUnusedAddress(registeredAddress.getCountryOrRegion()) + "</font></br>");
		}

		cmpAdd = ("<p><font style=\"font-family:sans-serif;\" size=\"6px\"><strong> "
				+ forNullValue(TemplateBuilder.getCmpName()) + "</strong></font></p>");

		headerHtml = ("<table style=\"width: 100%; height: 100%;\" cellspacing=\"10\" ><tr><td style=\"vertical-align: top;\" align=\"left\"><div class=\"gwt-HTML\" style=\" margin-left: 43px;\">"
				+ cmpAdd
				+ dateRangeHtml
				+ "</div></td><td style=\"vertical-align: top;\" align=\"right\"><div class=\"gwt-HTML\" style=\" margin-right: 43px;\"><p><font color=\"black\" style=\"font-family:sans-serif;\" size=\"5px\"><strong>"
				+ reportTitle
				+ "</strong></font></p><div style=\"font-family:sans-serif;\"><strong>Date: "
				+ reportDate + "</strong></div></div></td></tr></table>");

	}

	public String getHeader() {
		return this.headerHtml;
	}

	/**
	 * BODY
	 */
	@Override
	public void initBody() {
		String addressHtml;
		String detailsHtml;
		String billAdrs = "<br/><br/><br/><br/><br/><br/><br/>";
		billingaddress = getAddress(Address.TYPE_BILL_TO);
		String name = this.customerStatement.getName();
		if (billingaddress != null) {
			billAdrs = "<div align=\"left\">&nbsp;"
					+ name
					+ forUnusedAddress(billingaddress.getAddress1(), true)
					+ forUnusedAddress(billingaddress.getStreet(), true)
					+ forUnusedAddress(billingaddress.getCity(), true)
					+ forUnusedAddress(billingaddress.getStateOrProvinence(),
							true)
					+ forUnusedAddress(billingaddress.getZipOrPostalCode(),
							true) + billingaddress.getCountryOrRegion()
					+ "</div>";
		} else {
			billAdrs = "<div align=\"left\">&nbsp;" + name + "</div>";
		}

		addressHtml = "<table style=\"width: 100%; height: 100%;\"  cellspacing=\"10\"><tr><td style=\"vertical-align: top;\" align=\"left\"><table style=\"width: 280px; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><center>Bill To</center></td></tr><tr><td align=\"left\" style=\"vertical-align: top;padding: 6px;height:105px;\">"
				+ billAdrs + "</td></tr></table></td></tr></table>";

		String salesPerson = (customerStatement.getSalesPerson() != null ? customerStatement
				.getSalesPerson().getFirstName() : "");
		String shipMtd = customerStatement.getShippingMethod() != null ? customerStatement
				.getShippingMethod().getName() : "";
		String paymentTerm = customerStatement.getPaymentTerm() != null ? customerStatement
				.getPaymentTerm().getName() : "";

		detailsHtml = ("<table class=\"ShiftBottom\" style=\"width: 100%; height: 100%;\"><tr><td style=\"vertical-align: top;\" align=\"left\"><table style=\"width: 100%; height: 100%;\" class=\"gridHeader\"><colgroup><col></colgroup><tr><td class=\"gridHeaderBackGround\"><center>Sales Person</center></td><td class=\"gridHeaderBackGround\"><center>Shipping Method</center></td><td class=\"gridHeaderBackGround\" width=\"22%\"><center>Payment Terms</center></td></tr><tr><td align=\"center\">"
				+ salesPerson
				+ "<br/><br/><br/>"
				+ "</td><td align=\"center\">"
				+ shipMtd
				+ "<br/><br/><br/>"
				+ "</td><td align=\"center\" width=\"22%\">"
				+ paymentTerm
				+ "<br/><br/><br/>" + "</td></tr></table></td></tr></table>");

		bodyHtml = addressHtml + detailsHtml + reportsHtml;
	}

	public Address getAddress(int type) {
		addressListOfCustomer = customerStatement.getAddress();
		for (Address address : addressListOfCustomer) {

			if (address.getType() == type) {
				return address;
			}

		}
		return null;
	}

	/**
	 * FOOTER
	 */
	@Override
	public void initFooter() {
		footerHtml = "<div align=\"right\" style=\"margin-bottom:14px;\"><img src=\"footerimage\" width=\"67.5px\" height=\"25px\"></div>";
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	@Override
	public String getBody() {
		String body = "<html><head><link rel='stylesheet' type='text/css' href='"
				+ style1
				+ "' media='all'></link></head><body>"
				+ this.bodyHtml
				+ "</body></html>";
		body = body.replace("<br>", "<br/>");
		return body;
	}

	@Override
	public String getFooter() {
		this.footerHtml = this.footerHtml.replace("footerimage", imgUrl);
		return this.footerHtml;
	}

	@Override
	public String getFileName() {
		return "Statement_Template";
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return add + ",&nbsp;";
		} else {
			if (add != null && !add.equals(""))
				return add + "<br/>&nbsp;";
		}
		return "";
	}

	public String forUnusedAddress(String add) {
		if (add != null && !add.equals(""))
			return add + "<br/>&nbsp;";
		return "";
	}
}
