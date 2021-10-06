package com.vimukti.accounter.core;

public class CSVReportTemplate extends TemplateBuilder implements ITemplate {
	private String reportTitle;
	private String reportsHtml;
	private int reportType;
	private String reportDate;
	private String dateRangeHtml = "";

	public CSVReportTemplate(Company company, String... params) {
		super(company);
		imgUrl = params[2];
		style1 = params[3];
		this.reportsHtml = params[0];
		this.reportType = Integer.parseInt(params[1]);
		this.reportDate = params[4];
		if (params[5] != null && !params[5].equals("null"))
			this.dateRangeHtml = params[5];
		init();
	}

	public CSVReportTemplate(Company company, int reportType, String[] params,
			String reportTitle) {
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
	public void initBody() {
		this.bodyHtml = reportsHtml;
	}

	@Override
	public void initFooter() {

	}

	@Override
	public void initHeader() {
		String cmpAdd = "";
		Address registeredAddress = company.getRegisteredAddress();
		if (registeredAddress != null) {
			cmpAdd = (forUnusedAddress(registeredAddress.getAddress1())
					+ ","
					+ forUnusedAddress(registeredAddress.getStreet())
					+ ","
					+ forUnusedAddress(registeredAddress.getCity())
					+ ","
					+ forUnusedAddress(registeredAddress.getStateOrProvinence())
					+ ","
					+ forUnusedAddress(registeredAddress.getZipOrPostalCode())
					+ ","
					+ forUnusedAddress(registeredAddress.getCountryOrRegion()) + "</font></br>");
		}

		cmpAdd = (forNullValue(TemplateBuilder.getCmpName()));

		headerHtml = (cmpAdd + "\r\n" + dateRangeHtml + "\r\n" + reportTitle
				+ "\r\n" + reportDate + "\r\n");

	}

	@Override
	public String getBody() {
		return this.bodyHtml;
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

	@Override
	public String getFooter() {
		return "";
	}

	@Override
	public String getHeader() {
		return this.headerHtml;
	}

}
