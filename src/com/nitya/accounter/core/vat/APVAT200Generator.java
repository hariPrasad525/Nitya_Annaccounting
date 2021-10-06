package com.nitya.accounter.core.vat;

import java.io.File;
import java.util.Date;
import java.util.Map;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.server.FinanceTool;

public class APVAT200Generator extends IndianVATReportGenerator {
	private Company company;
	private long fromDate;
	private long toDate;
	private Long taxAgency;

	public APVAT200Generator(Company company, Long taxAgency, long fromDate,
			long toDate) {
		this.company = company;
		this.taxAgency = taxAgency;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	@SuppressWarnings("deprecation")
	public void assignValues() {
		Map<String, Double> map = new FinanceTool().getTaxManager()
				.getTAXReturnEntriesForVat200(company.getId(), taxAgency,
						fromDate, toDate);
		try {
			VAT200 vat200 = new VAT200();
			Date from = new Date(fromDate);
			vat200.setFromDD(toLongString(from.getDate()));
			vat200.setFromMM(toLongString(from.getMonth()));
			vat200.setFromYY(toLongString(from.getYear()));

			Date to = new Date(fromDate);
			vat200.setToDD(toLongString(to.getDate()));
			vat200.setToMM(toLongString(to.getMonth()));
			vat200.setToYY(toLongString(to.getYear()));

			vat200.setCompanyName(company.getLegalName());
			Address registeredAddress = company.getRegisteredAddress();
			vat200.setCompanyAddress(registeredAddress.toString());
			// vat200.setFaxNo();

			if (map != null) {
				vat200.setNoPurchasesAndSales("");
				vat200.setExemptPurA(toDecimalString(map.get("exemptRatePA")));
				vat200.setFourPurA(toDecimalString(map.get("fourRatePA")));
				vat200.setTwelvPurA(toDecimalString(map.get("twelvRatePA")));
				vat200.setOnePurA(toDecimalString(map.get("oneRatePA")));
				vat200.setSpecialRatePurA(toDecimalString(map
						.get("specialRatePA")));

				Double fourRatePB = map.get("fourRatePB");
				vat200.setFourPurB(toDecimalString(fourRatePB));
				Double twelvRatePB = map.get("twelvRatePB");
				vat200.setTwelvPurB(toDecimalString(twelvRatePB));
				Double oneRatePB = map.get("oneRatePB");
				vat200.setOnePurB(toDecimalString(oneRatePB));
				Double specialRatePB = map.get("specialRatePB");
				vat200.setSpecialRatePurB(toDecimalString(specialRatePB));
				// TODO add to total 5th
				Double box11 = fourRatePB + twelvRatePB + oneRatePB;
				vat200.setTotalPurB(toDecimalString(box11));

				vat200.setExemptSalA(toDecimalString(map.get("exemptRateSA")));
				vat200.setZeroIESalA(toDecimalString(map.get("zeroRateIESA")));
				vat200.setZeroOtherSalA(toDecimalString(map.get("zeroRateOSA")));
				vat200.setFourSalA(toDecimalString(map.get("fourRateSA")));
				vat200.setTwelvSalA(toDecimalString(map.get("twelvRateSA")));
				vat200.setOneSalA(toDecimalString(map.get("oneRatePA")));
				vat200.setSpecialRateSalA(toDecimalString(map
						.get("specialRatePA")));

				// TODO add Tax Due on Purchase of goods, add to total
				Double fourRateSB = map.get("fourRateSB");
				vat200.setFourSalB(toDecimalString(fourRateSB));
				Double twelvRateSB = map.get("twelvRateSB");
				vat200.setTwelvSalB(toDecimalString(twelvRateSB));
				Double oneRateSB = map.get("oneRateSB");
				vat200.setOneSalB(toDecimalString(oneRateSB));
				Double specialRateSB = map.get("fourRatePB");
				vat200.setSpecialRateSalB(toDecimalString(specialRateSB));
				Double box20 = fourRateSB + twelvRateSB + oneRateSB;
				vat200.setTotalSalB(toDecimalString(box20));

				if (DecimalUtil.isGreaterThan(box11, box20)) {
					vat200.setTotalOf20And11(toDecimalString(box11 - box20));
				}
				if (DecimalUtil.isGreaterThan(box20, box11)) {
					vat200.setRefund(toDecimalString(box20 - box11));
				}

			} else {
				vat200.setNoPurchasesAndSales("NIL");// TODO
			}
			context.put("vat200ap", vat200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String toLongString(int month) {
		return String.valueOf(month);
	}

	private String toDecimalString(Double double1) {
		return String.valueOf(double1);
	}

	@Override
	public String getPdfFileName() {
		return "VAT200Report";
	}

	@Override
	public File getTemplateFile() {
		File tempFile = new File("templetes" + File.separator + "vatforms",
				"vat200ap.odt");
		return tempFile;
	}
}
