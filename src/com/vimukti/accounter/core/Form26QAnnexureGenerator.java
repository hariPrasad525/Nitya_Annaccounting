package com.vimukti.accounter.core;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.core.Utility;

public class Form26QAnnexureGenerator extends ETDSAnnexuresGenerator {

	public Form26QAnnexureGenerator(
			ClientTDSDeductorMasters tdsDeductorMasterDetails2,
			ClientTDSResponsiblePerson responsiblePersonDetails2,
			Company company, String panList, String codeList, String remarkList) {
		super(tdsDeductorMasterDetails2, responsiblePersonDetails2, company,
				panList, codeList, remarkList);
	}

	public String generateFile() {
		Double total = 0.00;

		for (ClientTDSChalanDetail chalan : getChalanDetailsList()) {
			total = total + chalan.getIncomeTaxAmount()
					+ chalan.getSurchangePaidAmount()
					+ chalan.getEducationCessAmount()
					+ chalan.getInterestPaidAmount() + chalan.getOtherAmount();

		}

		fileText = generateFileHeaderRecord();
		fileText = fileText + generateBatchHeaderRecord(total);
		int lineNumber = 3;
		int chalNum = 1;
		codesArrayIndex = 0;
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (ClientTDSChalanDetail chalan : getChalanDetailsList()) {
				chalanDetails = chalan;
				setLineNumber(lineNumber);
				setRunningChalanNumber(chalNum);
				chalNum++;
				fileText = fileText + generateChalanVoucherDetailsRecord();
				int ddNumber = 1;
				for (ClientTDSTransactionItem items : chalanDetails
						.getTdsTransactionItems()) {
					setRunningSerialNumber(ddNumber);
					ddNumber++;
					Set<Vendor> vendors = company.getVendors();
					for (Vendor vendor : vendors) {
						if (vendor.getID() == items.getVendor()) {
							vendorFinal = vendor;
						}
					}
					lineNumber++;
					transactionItems = items;
					setLineNumber(lineNumber);
					fileText = fileText + generateDeducteeDetailsRecord();
					codesArrayIndex++;

					TDSTransactionItem serverObject = new ServerConvertUtil()
							.toServerObject(null, transactionItems, session);
					session.saveOrUpdate(serverObject);
				}
				lineNumber++;
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		}

		return fileText;
	}

	public String generateDeducteeDetailsRecord() {

		String deucteeDetailsRecordString = getLineNumber() + addDelimiter();

		// Record Type Value "DD"(Deductee Detail) for Deductee-detail record
		deucteeDetailsRecordString = deucteeDetailsRecordString + "DD"
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getBatchNumber() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getRunningChalanNumber() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getRunningSerialNumber() + addDelimiter();

		// Mode Value should be O
		deucteeDetailsRecordString = deucteeDetailsRecordString + "O"
				+ addDelimiter();

		// Employee Serial No(Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDeducteeCode() + addDelimiter();
		transactionItems.setDeducteeCode(Integer.valueOf(getDeducteeCode()));

		// Last Employee / Party PAN ( Used for Verification) (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDeducteePan() + addDelimiter();

		// Last Employee/Party PAN Ref. No. (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString + getPANRefNo()
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDeducteeName() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getTDSIncomeTaxforPeriod() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getTDSSurchargeforPeriod() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString + getTDSECess()
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getTotalIncomeTaxDeductedatSource() + addDelimiter();

		// Last Total Income Tax Deducted at Source (Income Tax+Surcharge+Cess)
		// ( Used for Verification) (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getTotalTaxDeposited() + addDelimiter();

		// Last Total Tax Deposited ( Used for Verification) (Not applicable)
		// Total Value of Purchase (Not applicable)

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getAmountOFpaymentorCredit() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDateonWhichAmountPaid() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDateOnWhichTaxCollected() + addDelimiter();

		// Date of Deposit (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getRateAtWhichTaxDeducted() + addDelimiter();

		// Grossing up Indicator (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getBookEntry() + addDelimiter();

		// Date of furnishing Tax Deduction Certificate (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString + getRemarks()
				+ addDelimiter();
		transactionItems.setRemark(getRemarks());

		// Remarks 2 (For future use)
		// Remarks 3 (For future use)

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter() + addDelimiter();

		return deucteeDetailsRecordString + endLine();

	}

	/**
	 * Remarks 1 (Reason for non-deduction / lower deduction/ higher
	 * deduction/threshold)
	 * 
	 * @param i
	 * 
	 * @return
	 */
	private String getRemarks() {
		if (remarkListArray.length > codesArrayIndex) {
			return remarkListArray[codesArrayIndex];
		} else {
			return "";
		}
	}

	/**
	 * Mention whether it is paid by Book entry or otherwise ('Y' or 'N').
	 * Mention "Y" for Book entry and "N" otherwise.
	 * 
	 * @return
	 */
	private String getBookEntry() {

		if (chalanDetails.isBookEntry()) {
			return "Y";
		} else {
			return "N";
		}

	}

	/**
	 * Rate at which Tax is deducted, with decimal precision of 4 point. E.g. if
	 * the rate is 2 then the same should be mentioned as 2.0000. If value in
	 * field 'Total Income Tax Deducted at Source' is 0.00 then rate at which
	 * tax deducted should be 0.0000
	 * 
	 * @return
	 */
	private String getRateAtWhichTaxDeducted() {
		return getRate(transactionItems.getTaxRate());
	}

	/**
	 * Date of tax deduction. Mandatory if 'Total Income Tax Deducted at Source'
	 * is greater than Zero (0.00) . No value needs to be specified if 'Total
	 * Income Tax Deducted at Source' is Zero (0.00) .
	 * 
	 * @return
	 */
	private String getDateOnWhichTaxCollected() {

		return getDateAsString(new FinanceDate(
				transactionItems.getTransactionDate()));
	}

	/**
	 * Date on which Amount paid/ Credited /Debited to deductee.
	 * 
	 * @return
	 */
	private String getDateonWhichAmountPaid() {

		return getDateAsString(new FinanceDate(
				transactionItems.getTransactionDate()));
	}

	/**
	 * Mention the Amount paid to deductee. Value should always be greater than
	 * 0.00
	 * 
	 * @return
	 */
	private String getAmountOFpaymentorCredit() {
		return getAmountAsString(transactionItems.getTotalAmount());
	}

	/**
	 * Mention the Total Tax Deposited for the Deductee. Zero (0.00 ) for a Nil
	 * Statement . Value should be equal to 0.00 if value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction)' is B and value in field
	 * 'Section' is 194, 194A, 194EE or 193. If value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction) is 'A' or 'null' then value
	 * should be greater than or equal to 0.00
	 * 
	 * @return
	 */
	private String getTotalTaxDeposited() {
		return getAmountAsString(transactionItems.getTdsAmount()
				+ transactionItems.getSurchargeAmount()
				+ transactionItems.getEduCess());
	}

	/**
	 * Total Income Tax Deducted at Source (TDS / TCS Income Tax+ TDS / TCS
	 * Surcharge + TDS/TCS Cess) I.e. (421+ 422 + 423 )
	 * 
	 * @return
	 */
	private String getTotalIncomeTaxDeductedatSource() {
		return getAmountAsString(transactionItems.getTdsAmount()
				+ transactionItems.getSurchargeAmount()
				+ transactionItems.getEduCess());
	}

	/**
	 * Total Income Tax Deducted at Source (TDS / TCS Income Tax+ TDS / TCS
	 * Surcharge + TDS/TCS Cess) I.e. (421+ 422 + 423 )
	 * 
	 * @return
	 */
	private String getTotalIncomeTaxDeductedatSourceInChallan() {
		Double total = chalanDetails.getIncomeTaxAmount()
				+ chalanDetails.getSurchangePaidAmount()
				+ chalanDetails.getEducationCessAmount();
		return getAmountAsString(total);
	}

	/**
	 * Decimal with precision value 2 is allowed. If value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction)' is 'B' and value in field
	 * 'Section' is 194, 194A, 194EE or 193 then value should be equal to 0.00.
	 * If value in field Remarks 1 (Reason for non-deduction / lower deduction)
	 * is "A" or "null" then value should be greater than or equal to 0.00
	 * 
	 * @return
	 */
	private String getTDSECess() {
		return getAmountAsString(transactionItems.getEduCess());
	}

	/**
	 * Decimal with precision value 2 is allowed. If value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction)' is 'B' and value in field
	 * 'Section' is 194, 194A, 194EE or 193 then value should be equal to 0.00.
	 * If value in field Remarks 1 (Reason for non-deduction / lower deduction)
	 * is "A" or "null" then value should be greater than or equal to 0.00
	 */
	private String getTDSSurchargeforPeriod() {
		return getAmountAsString(transactionItems.getSurchargeAmount());
	}

	/**
	 * Decimal with precision value 2 is allowed. If value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction)' is 'B' and value in field
	 * 'Section' is 194, 194A, 194EE or 193 then value should be equal to 0.00.
	 * If value in field Remarks 1 (Reason for non-deduction / lower deduction)
	 * is "A" or "null" then value should be greater than or equal to 0.00
	 * 
	 * @return
	 */
	private String getTDSIncomeTaxforPeriod() {
		return getAmountAsString(transactionItems.getTdsAmount());
	}

	/**
	 * Mention the Name of the deductee.
	 * 
	 * @return
	 */
	private String getDeducteeName() {
		return vendorFinal.getName();
	}

	/**
	 * The PAN Ref No is a unique identifier to identify an deductee Assessee
	 * where PAN is not available This is quoted by the deductor. (A deductee
	 * may have multiple entries in a Statement).
	 * 
	 * @return
	 */
	private String getPANRefNo() {
		return "";
	}

	/**
	 * PAN of the deductee. If available should be Valid PAN Format. There may
	 * be deductees who have not been issued PAN however who have applied for a
	 * PAN and have given adequate declaration to the deductor indicating the
	 * same. In such cases, deduction schedule in the statement will not reflect
	 * PAN and instead state PAN Ref. Number for the deductee. The deductor will
	 * however have to mention ‘PANAPPLIED’ in place of PAN. If the deductee is
	 * not sure of the PAN format he will have to mention 'PANINVALID'. However
	 * if the deductee has not given any declaration, deductor will have to
	 * mention ‘PANNOTAVBL’ in place of PAN.
	 * 
	 * @return
	 */
	private String getDeducteePan() {
		String panOfDeductee = getPanOfDeductee();
		if (panOfDeductee != null && !panOfDeductee.isEmpty()) {
			if (panOfDeductee.length() == 10) {
				return panOfDeductee;
			} else {
				return "PANINVALID";
			}
		} else {
			return "PANNOTAVBL";
		}
	}

	/**
	 * Deductee Code 1 for Companies / 2 for other than companies.
	 * 
	 * @param i
	 * 
	 * @return
	 */
	private String getDeducteeCode() {
		if (codeListArray.length > codesArrayIndex) {
			return codeListArray[codesArrayIndex];
		} else {
			return "";
		}
	}

	private String getPanOfDeductee() {
		if (panListArray.length > codesArrayIndex) {
			return panListArray[codesArrayIndex];
		} else {
			return "";
		}
	}

	// /**
	// * Running serial no to indicate detail record no.
	// *
	// * @return
	// */
	// private String getRunningSerialNumber() {
	// return null;
	// }
	//
	// /**
	// * Running serial number for 'Challan Detail' records in a batch.
	// *
	// * @return
	// */
	// private String getRunningChalanNmber() {
	// return null;
	// }

	/**
	 * Value should be same as 'Batch Number' field in 'Batch Header' record
	 * 
	 * @return
	 */
	private String getBatchNumber() {
		return "1";
	}

	/**
	 * TDS Statement for Non Salary category (Challan / Transfer Voucher Detail
	 * Record)
	 * 
	 * @param lineNumber
	 * @return
	 */
	public String generateChalanVoucherDetailsRecord() {

		String chalanDetailsRecordString = null;
		chalanDetailsRecordString = getLineNumber() + addDelimiter();

		// Record Type
		chalanDetailsRecordString = chalanDetailsRecordString + "CD"
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getBatchNumber() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getRunningChalanNumber() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getDeducteeRecordsCount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getNILChalanIndicator() + addDelimiter();

		// Challan Updation Indicator (Not applicable)
		// Filler 3
		// Filler 4
		// Filler 5
		// Last Bank Challan No ( Used for Verification) (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter()
				+ addDelimiter() + addDelimiter() + addDelimiter()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getBankChalanNumber() + addDelimiter();

		// Last Transfer Voucher No ( Used for Verification) (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTransferVoucherNo() + addDelimiter();

		// Last Bank-Branch Code/ Form 24G Receipt Number ( Used for
		// Verification) (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getBankBranchCode() + addDelimiter();

		// Last Date of 'Bank Challan No / Transfer Voucher No' ( Used for
		// Verification) (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getDateofBankChalanNo() + addDelimiter();

		// Filler 6
		// Filler 7
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString + getSection()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getOLTASIncomeTax() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getOLTASSurcharge() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString + getOLTASECess()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getOLTASInterestAmount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getOLTASOtherAmount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalOLTASDepositeAmount() + addDelimiter();

		// Last Total of Deposit Amount as per Challan ( Used for Verification)
		// (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalTaxDepositedInChallan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalIncomeTaxinChalan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalSurchargeinChalan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalECessinChalan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalIncomeTaxDeductedatSourceInChallan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTDSInterestAMount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTDSOtherAmount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString + getChequeDDNo()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getByBookEntry() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getInternalChalanReferenceNo() + addDelimiter();

		return chalanDetailsRecordString + endLine();

	}

	/**
	 * Internal Challan Reference No. or Other comments.
	 * 
	 * @return
	 */
	private String getInternalChalanReferenceNo() {
		return "";
	}

	/**
	 * Allowed values - Y/N. If Transfer Voucher Number is provided this is
	 * mandatory and only allowed value is 'Y'. If Bank Challan Number is
	 * provided value 'N' should be provided. However, for a Nil Statement -
	 * value can be either 'Y' / 'N' / Null.
	 * 
	 * @return
	 */
	private String getByBookEntry() {
		if (chalanDetails.isBookEntry()) {
			return "Y";
		} else {
			return "N";
		}

	}

	/**
	 * Mention the Cheque /DD No ,for which the challan is issued . Value should
	 * be "0" where tax is deposited in cash. No value to be provided if value
	 * in field "NIL Challan Indicator" is "Y". No value to be provided if tax
	 * deposited by book entry.
	 * 
	 * @return
	 */
	private String getChequeDDNo() {

		if (!getNILChalanIndicator().equals("Y")
				&& !chalanDetails.isBookEntry()) {
			return Long.toString(chalanDetails.getCheckNumber());
		} else {
			return "";
		}
	}

	/**
	 * Statement Other amount as per the respective deductee Annexure
	 * 
	 * @return
	 */
	private String getTDSOtherAmount() {
		return getAmountAsString(chalanDetails.getOtherAmount());
	}

	/**
	 * Statement Interest amount as per the respective deductee Annexure
	 * 
	 * @return
	 */
	private String getTDSInterestAMount() {
		return getAmountAsString(chalanDetails.getInterestPaidAmount());
	}

	private String getTotalECessinChalan() {
		return getAmountAsString(chalanDetails.getEducationCessAmount());
	}

	private String getTotalSurchargeinChalan() {
		return getAmountAsString(chalanDetails.getSurchangePaidAmount());
	}

	/**
	 * Total sum of column no. 421 for the respective Challan
	 * 
	 * @return
	 */
	private String getTotalIncomeTaxinChalan() {
		return getAmountAsString(chalanDetails.getIncomeTaxAmount());
	}

	/**
	 * Mention the sum of 'Deductee Deposit Amount' of the underlying Deductee
	 * Records
	 * 
	 * @return
	 */
	private String getTotalTaxDepositedInChallan() {
		Double total = chalanDetails.getIncomeTaxAmount()
				+ chalanDetails.getSurchangePaidAmount()
				+ chalanDetails.getEducationCessAmount();
		return getAmountAsString(total);
	}

	/**
	 * Mention the amount of 'Total tax deposited' through Challan. No
	 * fractional portion is allowed in this field (value should be integer) ,
	 * I.e. value "1000.50" will not be allowed, whereas value "1000.00" will be
	 * considered to be valid value. Value in this field should be equal to
	 * total of values in fields with column numbers 403, 404, 405, 406 & 407.
	 * Total of Deposit Amount as per Challan/Transfer Voucher Number ( 'Oltas
	 * TDS/ TCS -Income Tax ' + 'Oltas TDS/ TCS -Surcharge ' + 'Oltas TDS/ TCS -
	 * Cess' + Oltas TDS/ TCS - Interest Amount + Oltas TDS/ TCS - Others
	 * (amount) )
	 * 
	 * @return
	 */
	private String getTotalOLTASDepositeAmount() {
		Double total = chalanDetails.getIncomeTaxAmount()
				+ chalanDetails.getSurchangePaidAmount()
				+ chalanDetails.getEducationCessAmount()
				+ chalanDetails.getInterestPaidAmount()
				+ chalanDetails.getOtherAmount();
		return getAmountWithNoFractionPortion(total);
	}

	/**
	 * Mention the amount of "Other Amount" out of the 'Total tax deposited'
	 * through Challan. No fractional portion is allowed in this field (value
	 * should be integer) , I.e. value "1000.50" will not be allowed, whereas
	 * value "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASOtherAmount() {
		return getAmountWithNoFractionPortion(chalanDetails.getOtherAmount());
	}

	/**
	 * Mention the amount of "Interest" out of the 'Total tax deposited' through
	 * Challan. No fractional portion is allowed in this field (value should be
	 * integer) , I.e. value "1000.50" will not be allowed, whereas value
	 * "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASInterestAmount() {
		return getAmountWithNoFractionPortion(chalanDetails
				.getInterestPaidAmount());
	}

	/**
	 * Mention the amount of "Education Cess" out of the 'Total tax deposited'
	 * through Challan. No fractional portion is allowed in this field (value
	 * should be integer) , I.e. value "1000.50" will not be allowed, whereas
	 * value "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASECess() {
		return getAmountWithNoFractionPortion(chalanDetails
				.getEducationCessAmount());
	}

	/**
	 * Mention the amount of "Surcharge" out of the 'Total tax deposited'
	 * through Challan. No fractional portion is allowed in this field (value
	 * should be integer) , I.e. value "1000.50" will not be allowed, whereas
	 * value "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASSurcharge() {
		return getAmountWithNoFractionPortion(chalanDetails
				.getSurchangePaidAmount());
	}

	/**
	 * Mention the amount of "Income Tax" out of the 'Total tax deposited'
	 * through Challan. No fractional portion is allowed in this field (value
	 * should be integer) , I.e. value "1000.50" will not be allowed, whereas
	 * value "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASIncomeTax() {
		return getAmountWithNoFractionPortion(chalanDetails
				.getIncomeTaxAmount());
	}

	/**
	 * Section Code under which Tax has been deducted. Refer Annexure 2.
	 * 
	 * @return
	 */
	private String getSection() {
		if (chalanDetails.getPaymentSection() != null) {
			return chalanDetails.getPaymentSection();
		} else {
			return "";
		}
	}

	/**
	 * Returns the section code depending on the section name.
	 * 
	 * @param sectionName
	 * @return
	 */
	String getSectionCode(String sectionName) {

		String[] split = sectionName.split("-");

		List<String> sectionNamesList = Utility.get26QSectionNames();

		String codeReturned = null;

		List<String> sectionCodesList = Utility.get26QSectionCodes();

		for (int i = 0; i < sectionNamesList.size(); i++) {
			if (split[0].equals(sectionNamesList.get(i))) {
				codeReturned = sectionCodesList.get(i);
				break;
			}
		}
		return codeReturned;
	}

	/**
	 * Date of payment of tax to Govt. It cannot be Future Date. Value should be
	 * equal to last date of respective quarter if the value in field
	 * "NIL Challan Indicator" is "Y".
	 * 
	 * @return
	 */
	private String getDateofBankChalanNo() {
		if (!getNILChalanIndicator().equals("Y")) {
			FinanceDate date = new FinanceDate(chalanDetails.getDateTaxPaid());
			return getDateAsString(date);
		} else {
			FinanceDate[] dates = com.vimukti.accounter.core.Utility
					.getFinancialQuarter(company, Integer.parseInt(quater));
			return getDateAsString(dates[1]);
		}
	}

	/**
	 * In case TDS deposited by 1) Challan:BSR Code of the receiving branch 2)
	 * Transfer voucher: Quote seven digit receipt number provided by AO.
	 * Applicable for govt. deductor/ collector where TDS is deposited by book
	 * entry. 3) No value to be quoted in case of Nil Statement (value in field
	 * "NIL Challan Indicator" field is "Y").
	 * 
	 * @return
	 */
	private String getBankBranchCode() {
		if (!getNILChalanIndicator().equals("Y")) {
			return chalanDetails.getBankBsrCode();
		}
		// String bankBsrCode = chalanDetails.getBankBsrCode();
		// if (bankBsrCode.length() > 1)
		// return bankBsrCode;
		else {
			return "";
		}
	}

	/**
	 * 1) Applicable only in case of a Government deductor/collector where
	 * TDS/TCS has been deposited by Book entry. 2) Quote the five digit DDO
	 * serial number provided by Accounts Officer (AO) 2) No value should be
	 * present in this column in case of a NIL Statement (value in field
	 * "NIL Challan Indicator" field is "Y")
	 * 
	 * @return
	 */
	private String getTransferVoucherNo() {
		if (!getNILChalanIndicator().equals("Y")
				&& (getDeductorType().equals("A") || getDeductorType().equals(
						"S"))) {
			return Long.toString(chalanDetails.getChalanSerialNumber());
		} else {
			return "";
		}
	}

	/**
	 * Challan Number issued by Bank . Applicable to both Govt and Non Govt,
	 * Non-Nil statements. No value is required to be provided in case of a NIL
	 * return (I.e. the cases in which the value in field 'NIL Challan
	 * Indicator' is "Y"). Also, no value is required to be provided when some
	 * value in "Transfer Voucher No" field is provided.
	 * 
	 * @return
	 */
	private String getBankChalanNumber() {
		if (!getNILChalanIndicator().equals("Y")
				|| !chalanDetails.isBookEntry()) {
			return Long.toString(chalanDetails.getBankChalanNumber());
		} else {
			return "";
		}
	}

	/**
	 * Value should be "N". In cases where no tax has been deposited in bank,
	 * value should be "Y" (applicable in case of NIL return)
	 * 
	 * @return
	 */
	private String getNILChalanIndicator() {
		// TODO Auto-generated method stub
		return "N";
	}

	/**
	 * Count of total number of 'Deductee Detail Records' within e-TDS statement
	 * 
	 * @return
	 */
	private String getDeducteeRecordsCount() {
		return Integer.toString(chalanDetails.getTdsTransactionItems().size());
	}

	public void setFormDetails(String formNo, String quater, String startYear,
			String endYear) {
		super.setFormDetails(formNo, quater, startYear, endYear);
	}

	public int getRunningSerialNumber() {
		return runningSerialNumber;
	}

	public void setRunningSerialNumber(int runningSerialNumber) {
		this.runningSerialNumber = runningSerialNumber;
	}

	/**
	 * @return the runningChalanNumber
	 */
	public int getRunningChalanNumber() {
		return runningChalanNumber;
	}

	/**
	 * @param runningChalanNumber
	 *            the runningChalanNumber to set
	 */
	public void setRunningChalanNumber(int runningChalanNumber) {
		this.runningChalanNumber = runningChalanNumber;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber
	 *            the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
