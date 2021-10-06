package com.nitya.accounter.core.vat;

import java.io.OutputStream;

import com.nitya.accounter.core.Company;

public class IndianVATTemplate {
	private IndianVATReportGenerator generator;

	public IndianVATTemplate(Company company, long taxAgency, long statrDate,
			long endDate) {
		generator = getGenerator(company, taxAgency, statrDate, endDate);
		generator.generate();
	}

	private IndianVATReportGenerator getGenerator(Company company,
			long taxAgency, long fromDate, long toDate) {
		// company.getPreferences().getTradingAddress().getStateOrProvinence()
		IndianVATReportGenerator generator = new APVAT200Generator(company,
				taxAgency, fromDate, toDate);
		return generator;
	}

	public String getFileName() {
		return generator.getPdfFileName();
	}

	public void writeResponse(OutputStream os) throws Exception {
		generator.createPDF(os);
	}

}
