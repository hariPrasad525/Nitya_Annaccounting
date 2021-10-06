package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.web.server.languages.English;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class Form16ApdfTemplate {

	FormDetails formdetails;

	List<BookEntry> bookentries = new ArrayList<BookEntry>();

	List<BookEntry> chalaLists = new ArrayList<BookEntry>();

	List<PaymentSummary> paymentSummaryList = new ArrayList<PaymentSummary>();

	List<SummaryOfTax> summaryOfTaxList = new ArrayList<SummaryOfTax>();

	Verification verification;

	public Form16ApdfTemplate() {
		formdetails = new FormDetails();
		verification = new Verification();
	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {
			FieldsMetadata summaryOfTaxData = new FieldsMetadata();
			summaryOfTaxData.addFieldAsList("tax.quarter");
			summaryOfTaxData.addFieldAsList("tax.receiptNumber");
			summaryOfTaxData.addFieldAsList("tax.amount");
			summaryOfTaxData.addFieldAsList("tax.deposited");
			summaryOfTaxData.addFieldAsList("payment.amountPaid");
			summaryOfTaxData.addFieldAsList("payment.natureOfPayment");
			summaryOfTaxData.addFieldAsList("payment.dateOfPayment");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.taxDeposited");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.bsrCode");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.dateTaxDeposited");
			summaryOfTaxData.addFieldAsList("bookEntrychalan.serialNumber");
			summaryOfTaxData.addFieldAsList("chalan.taxDeposited");
			summaryOfTaxData.addFieldAsList("chalan.bsrCode");
			summaryOfTaxData.addFieldAsList("chalan.dateTaxDeposited");
			summaryOfTaxData.addFieldAsList("chalan.serialNumber");
			report.setFieldsMetadata(summaryOfTaxData);

			context.put("form", formdetails);
			context.put("payment", paymentSummaryList);
			context.put("tax", summaryOfTaxList);
			context.put("bookEntrychalan", bookentries);
			context.put("chalan", chalaLists);
			context.put("verification", verification);

			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public class FormDetails {

		private String deductorAddress;
		private String deducteeAddress;
		private String deductorPan;
		private String deductorTan;
		private String deducteePan;
		private String cit;
		private String assessmentYear;
		private String fromDate;
		private String toDate;

		public String getDeductorAddress() {
			return deductorAddress;
		}

		public void setDeductorAddress(String deductorAddress) {
			this.deductorAddress = deductorAddress;
		}

		public String getDeducteeAddress() {
			return deducteeAddress;
		}

		public void setDeducteeAddress(String deducteeAddress) {
			this.deducteeAddress = deducteeAddress;
		}

		public String getDeductorPan() {
			return deductorPan;
		}

		public void setDeductorPan(String deductorPan) {
			this.deductorPan = deductorPan;
		}

		public String getDeductorTan() {
			return deductorTan;
		}

		public void setDeductorTan(String deductorTan) {
			this.deductorTan = deductorTan;
		}

		public String getDeducteePan() {
			return deducteePan;
		}

		public void setDeducteePan(String deducteePan) {
			this.deducteePan = deducteePan;
		}

		public String getCit() {
			return cit;
		}

		public void setCit(String cit) {
			this.cit = cit;
		}

		public String getFromDate() {
			return fromDate;
		}

		public void setFromDate(String fromDate) {
			this.fromDate = fromDate;
		}

		public String getToDate() {
			return toDate;
		}

		public void setToDate(String toDate) {
			this.toDate = toDate;
		}

		public String getAssessmentYear() {
			return assessmentYear;
		}

		public void setAssessmentYear(String assessmentYear) {
			this.assessmentYear = assessmentYear;
		}

	}

	public class PaymentSummary {

		private String amountPaid;
		private String natureOfPayment;
		private String dateOfPayment;

		public PaymentSummary(String amountPaid, String natureOfPayment,
				String dateOfPayment) {
			setAmountPaid(amountPaid);
			setNatureOfPayment(natureOfPayment);
			setDateOfPayment(dateOfPayment);
		}

		public String getAmountPaid() {
			return amountPaid;
		}

		public void setAmountPaid(String amountPaid) {
			this.amountPaid = amountPaid;
		}

		public String getNatureOfPayment() {
			return natureOfPayment;
		}

		public void setNatureOfPayment(String natureOfPayment) {
			this.natureOfPayment = natureOfPayment;
		}

		public String getDateOfPayment() {
			return dateOfPayment;
		}

		public void setDateOfPayment(String dateOfPayment) {
			this.dateOfPayment = dateOfPayment;
		}

	}

	public class SummaryOfTax {

		private String quarter;
		private String receiptNumber;
		private String amount;
		private String deposited;

		public String getQuarter() {
			return quarter;
		}

		public void setQuarter(String quarter) {
			this.quarter = quarter;
		}

		public String getReceiptNumber() {
			return receiptNumber;
		}

		public void setReceiptNumber(String receiptNumber) {
			this.receiptNumber = receiptNumber;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getDeposited() {
			return deposited;
		}

		public void setDeposited(String deposited) {
			this.deposited = deposited;
		}

	}

	public class BookEntry {

		private String taxDeposited;
		private String bsrCode;
		private String dateTaxDeposited;
		private String serialNumber;
		private String totalTaxDeposited;

		public String getTaxDeposited() {
			return taxDeposited;
		}

		public void setTaxDeposited(String taxDeposited) {
			this.taxDeposited = taxDeposited;
		}

		public String getBsrCode() {
			return bsrCode;
		}

		public void setBsrCode(String bsrCode) {
			this.bsrCode = bsrCode;
		}

		public String getDateTaxDeposited() {
			return dateTaxDeposited;
		}

		public void setDateTaxDeposited(String dateTaxDeposited) {
			this.dateTaxDeposited = dateTaxDeposited;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getTotalTaxDeposited() {
			return totalTaxDeposited;
		}

		public void setTotalTaxDeposited(String totalTaxDeposited) {
			this.totalTaxDeposited = totalTaxDeposited;
		}

	}

	public class Verification {

		private String place;
		private String signature;
		private String date;
		private String fullName;
		private String designation;
		private String name;
		private String rupees;
		private String amountinwords;

		public String getPlace() {
			return place;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getDesignation() {
			return designation;
		}

		public void setDesignation(String designation) {
			this.designation = designation;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setRupees(String rupees) {
			this.rupees = rupees;
		}

		public String getRupees() {
			return rupees;
		}

		public String getAmountinwords() {
			return amountinwords;
		}

		public void setAmountinwords(String amountinwords) {
			this.amountinwords = amountinwords;
		}
	}

	public void setVendorandCompanyData(Vendor vendor,
			TDSDeductorMasters tdsDeductorMasterDetails) {

		String buildingName;
		if (tdsDeductorMasterDetails.getBuildingName() != null)
			buildingName = tdsDeductorMasterDetails.getBuildingName();
		else
			buildingName = "";

		String cityName;
		if (tdsDeductorMasterDetails.getCity() != null)
			cityName = tdsDeductorMasterDetails.getCity();
		else
			cityName = "";

		String roadName;
		if (tdsDeductorMasterDetails.getRoadName() != null)
			roadName = tdsDeductorMasterDetails.getRoadName();
		else
			roadName = "";

		String stateName;
		if (tdsDeductorMasterDetails.getState() != null)
			stateName = tdsDeductorMasterDetails.getState();
		else
			stateName = "";

		String pinCOde;
		if (tdsDeductorMasterDetails.getPinCode() != 0)
			pinCOde = Long.toString(tdsDeductorMasterDetails.getPinCode());
		else
			pinCOde = "0";

		formdetails.setDeductorAddress(buildingName + "\n" + cityName + "\n "
				+ roadName + "\n" + stateName + "\n " + pinCOde);

		formdetails.setDeductorPan(tdsDeductorMasterDetails.getPanNumber());
		formdetails.setDeductorTan(tdsDeductorMasterDetails.getTanNumber());

		Set<Address> address = vendor.getAddress();
		if (address.size() > 0) {
			for (Address address2 : address) {
				formdetails.setDeducteeAddress(getValidAddress(address2));
			}
		} else {
			formdetails.setDeducteeAddress(" ");
		}
		if (tdsDeductorMasterDetails.getTaxOfficeAddress() == null) {
			formdetails.setCit("");
		} else {
			formdetails.setCit(getValidAddress(tdsDeductorMasterDetails
					.getTaxOfficeAddress()));
		}
		formdetails.setAssessmentYear("");
		formdetails.setDeducteePan(vendor.getTaxId());
	}

	protected String getValidAddress(Address address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	public void setDateYear(String dateRangeString) {
		String[] split = dateRangeString.split("-");
		formdetails.setFromDate(split[0]);
		formdetails.setToDate(split[1]);
	}

	public void setSummaryPaymentDetails(ArrayList<TDSChalanDetail> chalanList) {
		double total = 0.0;
		for (TDSChalanDetail chalan : chalanList) {
			Long dateTaxPaid = chalan.getDateTaxPaid();
			BookEntry bookEntry = new BookEntry();
			bookEntry.setBsrCode(chalan.getBankBsrCode());
			bookEntry.setDateTaxDeposited(new FinanceDate(chalan
					.getDateTaxPaid()).toString());
			bookEntry.setSerialNumber(String.valueOf(chalan
					.getChalanSerialNumber()));
			bookEntry.setTaxDeposited(String.valueOf(chalan.getTotal()));
			total += chalan.getTotal();
			bookEntry.setTotalTaxDeposited(String.valueOf(total));
			verification.setRupees(String.valueOf(total));
			verification.setAmountinwords(new English()
					.getAmountAsString(total));
			if (chalan.isBookEntry()) {
				bookentries.add(bookEntry);
			}
			chalaLists.add(bookEntry);
			FinanceDate financeDate = new FinanceDate(dateTaxPaid);
			for (TDSTransactionItem item : chalan.getTdsTransactionItems()) {
				paymentSummaryList.add(new PaymentSummary(String.valueOf(item
						.getTotalAmount()), chalan.getPaymentSection(),
						financeDate.toString()));
			}
			formdetails.setAssessmentYear(String.valueOf(chalan
					.getAssesmentYearStart())
					+ "-"
					+ String.valueOf(chalan.getAssessmentYearEnd()));
		}
		if (bookentries.isEmpty()) {
			BookEntry bookEntry = new BookEntry();
			bookEntry.setBsrCode("No information available");
			bookEntry.setDateTaxDeposited("");
			bookEntry.setSerialNumber("");
			bookEntry.setTaxDeposited("");
			bookEntry.setTotalTaxDeposited("");
			bookentries.add(bookEntry);
		}

	}

	public void setSummaryOfTaxDetails(
			Map<Integer, ArrayList<TDSChalanDetail>> quarterList) {
		for (int i = 0; i < quarterList.size(); i++) {
			SummaryOfTax summaryOfTax = new SummaryOfTax();
			summaryOfTax.setQuarter("Quarter " + String.valueOf(i + 1));
			if (!quarterList.get(i).isEmpty()) {
				long deposited = 0;
				for (TDSChalanDetail chalan : quarterList.get(i)) {
					deposited += chalan.getTotal();
					summaryOfTax.setReceiptNumber(chalan
							.getEtdsfillingAcknowledgementNo());
				}
				summaryOfTax.setDeposited(String.valueOf(deposited));
				summaryOfTax.setAmount(String.valueOf(deposited));
			} else {
				summaryOfTax.setAmount("");
				summaryOfTax.setDeposited("");
				summaryOfTax.setReceiptNumber("");
			}
			summaryOfTaxList.add(summaryOfTax);
		}
	}

	public void setVerificationDetails(TDSResponsiblePerson responsiblePerson,
			String place, String date) {
		verification.setDate(date);
		if (place == null || place.isEmpty()) {
			place = responsiblePerson.getCity();
		}
		verification.setPlace(place);
		verification.setDesignation(responsiblePerson.getDesignation());
		verification.setFullName(responsiblePerson.getName());
		verification.setSignature("");
		verification.setName(responsiblePerson.getName());
		if (verification.getRupees() == null) {
			verification.setRupees("");
			verification.setAmountinwords("");
		}
	}
}
