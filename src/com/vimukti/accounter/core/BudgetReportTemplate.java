package com.vimukti.accounter.core;

public class BudgetReportTemplate extends TemplateBuilder implements ITemplate {

	private String reportTitle;
	private String reportsHtml;
	private int reportType;
	private String reportDate;
	private String dateRangeHtml = "";

	public BudgetReportTemplate(Company company, String... params) {
		super(company);
		imgUrl = params[2];
		style1 = params[3];
		this.reportsHtml = params[0];
		this.reportTitle = params[1];
		this.reportDate = params[4];
		if (params[5] != null && !params[5].equals("null"))
			this.dateRangeHtml = params[5];
		init();
	}

	public BudgetReportTemplate(Company company, int reportType,
			String[] params, String reportTitle) {
		super(company);
		imgUrl = params[1];
		style1 = params[2];
		this.reportsHtml = params[0];
		this.reportType = reportType;
		this.reportDate = new FinanceDate().toString();
		if (params[3] != null && !params[3].equals("null"))
			this.dateRangeHtml = params[3];
		this.reportTitle = reportTitle;
		init();

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
