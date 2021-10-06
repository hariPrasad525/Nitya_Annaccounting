package com.nitya.accounter.core;

public class ReportTemplate extends TemplateBuilder implements ITemplate {

	private String reportTitle;
	private String reportsHtml;
	private int reportType;
	private String reportDate;

	public ReportTemplate(Company company, String... params) {
		super(company);
		imgUrl = params[2];
		style1 = params[3];
		this.reportsHtml = params[0];
		this.reportTitle = params[1];
		this.reportDate = params[4];
		init();
	}

	public ReportTemplate(Company company, int reportType, String[] params,
			String reportTitle) {
		super(company);
		imgUrl = params[1];
		style1 = params[2];
		this.reportsHtml = params[0];
		this.reportType = reportType;
		this.reportDate = new FinanceDate().toString();
		this.reportTitle = reportTitle;
		init();

	}

	@Override
	public String getBody() {
		String body = "<html><head><link rel='stylesheet' type='text/css' href='"
				+ style1
				+ "' media='all'/></head><body>"
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
	public String getHeader() {
			return this.headerHtml;
	}

	@Override
	public void initBody() {
		this.bodyHtml = reportsHtml;
	}

	@Override
	public void initFooter() {
		footerHtml = "<div align=\"right\" style=\"margin-bottom:14px;\"><img src=\"footerimage\" width=\"67.5px\" height=\"25px\"></div>";
	}

	@Override
	public void initHeader() {

		StringBuffer cmpAdd = new StringBuffer("<br/><br/><br/>");
		Address registeredAddress = company.getRegisteredAddress();
		if (registeredAddress != null) {
			cmpAdd.append("<br><font style=\"font-family:sans-serif; color:#504040;\">&nbsp;");
			cmpAdd.append(forUnusedAddress(registeredAddress.getAddress1()));
			cmpAdd.append(forUnusedAddress(registeredAddress.getStreet()));
			cmpAdd.append(forUnusedAddress(registeredAddress.getCity()));
			cmpAdd.append(forUnusedAddress(registeredAddress
					.getStateOrProvinence()));
			cmpAdd.append(forUnusedAddress(registeredAddress
					.getZipOrPostalCode()));
			cmpAdd.append(forUnusedAddress(registeredAddress
					.getCountryOrRegion()));
			cmpAdd.append("</font></br>");
		}

		cmpAdd.append("<p><font style=\"font-family:sans-serif;\" size=\"6px\"><strong> "
				+ forNullValue(TemplateBuilder.getCmpName())
				+ "</strong></font></p>");

		headerHtml = ("<table style=\"width: 100%; height: 100%;\" cellspacing=\"10\" ><tr><td style=\"vertical-align: top;\" align=\"left\"><div class=\"gwt-HTML\" style=\" margin-left: 43px;\">"
				+ cmpAdd.toString()
				+ "</div></td><td style=\"vertical-align: top;\" align=\"right\"><div class=\"gwt-HTML\" style=\" margin-right: 43px;\"><p><font color=\"black\" style=\"font-family:sans-serif;\" size=\"5px\"><strong>"
				+ reportTitle
				+ "</strong></font></p><div style=\"font-family:sans-serif;\"><strong>Date: "
				+ reportDate + "</strong></div></div></td></tr></table>");

	}

	@Override
	public String getFileName() {
		return reportTitle;
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	public String forUnusedAddress(String add) {
		if (add != null && !add.equals(""))
			return add + "<br/>&nbsp;";
		return "";
	}
}
