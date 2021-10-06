package com.nitya.accounter.core;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTDSChalanDetail;
import com.nitya.accounter.web.client.core.ClientTDSDeductorMasters;
import com.nitya.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.nitya.accounter.web.client.core.ClientTDSTransactionItem;
import com.nitya.accounter.web.client.core.Utility;

/**
 * class used to generate the eTDS filling text file
 * 
 * @author vimukti8
 * 
 */
public abstract class ETDSAnnexuresGenerator {

	private ClientTDSDeductorMasters deductor;
	private ClientTDSResponsiblePerson responsiblePerson;
	protected String formNo;
	protected String quater;
	protected String startYear;
	protected String endYear;
	protected int chalanCount;
	protected List<ClientTDSChalanDetail> chalanDetailsList;

	String fileText = null;
	protected ClientTDSChalanDetail chalanDetails;
	protected ClientTDSTransactionItem transactionItems;
	protected Vendor vendorFinal;

	protected Company company;

	protected int runningSerialNumber;
	protected int runningChalanNumber;
	protected int lineNumber;
	protected String[] panListArray;
	protected String[] codeListArray;
	protected String[] remarkListArray;
	protected String[] grossingUpListArray;
	protected int codesArrayIndex = 0;

	public ETDSAnnexuresGenerator(
			ClientTDSDeductorMasters tdsDeductorMasterDetails2,
			ClientTDSResponsiblePerson responsiblePersonDetails2,
			Company company, String panList, String codeList, String remarkList) {
		deductor = tdsDeductorMasterDetails2;
		responsiblePerson = responsiblePersonDetails2;
		this.company = company;
		panListArray = panList.split("-");
		codeListArray = codeList.split("-");
		remarkListArray = remarkList.split("-");
	}

	/**
	 * when a new Line is started Each Record (including last record) must start
	 * on new line
	 */
	String startNewLine() {
		return "\r\n";
	}

	/**
	 * line end must end with a newline character. Hex Values : "0D" & "0A".
	 */
	String endLine() {
		return "\r\n";
	}

	/**
	 * This is a ^ delimited variable field width file. This means that in case
	 * of empty spaces there is no need to provide leading '0' for numerals and
	 * trailing spaces for character fields.
	 */
	String addDelimiter() {
		return "^";
	}

	/**
	 * add the line number before each line
	 * 
	 * @return
	 */
	String addLineNumber() {
		return null;
	}

	/**
	 * Generate header for the file
	 */
	String generateFileHeaderRecord() {

		String headerString;
		// line number
		headerString = "1" + addDelimiter();

		// "FH" signifying 'File Header'
		headerString = headerString + "FH" + addDelimiter();

		// Value should be "NS1", For 27EQ "TC1".
		headerString = headerString + getFileType() + addDelimiter();

		// Value should be R
		headerString = headerString + "R" + addDelimiter();

		headerString = headerString + addTodaysDate() + addDelimiter();

		// Indicates the running sequencenumber for the file. (Should be unique
		// across all the files)
		headerString = headerString + "1" + addDelimiter();

		// Value should be D
		headerString = headerString + "D" + addDelimiter();

		// TAN of Deductor
		headerString = headerString + getTanofDeductor() + addDelimiter();

		// Indicates the number of batches that the file contains.
		headerString = headerString + "1" + addDelimiter();

		// Name of the software used for preparing theQuarterly
		// e-TDS/TCSstatement should be mentioned.
		headerString = headerString + softwareUsedName() + addDelimiter();

		// Record Hash (Not applicable)
		headerString = headerString + addDelimiter();

		// FVU Version (Not applicable)
		headerString = headerString + addDelimiter();

		// File Hash (Not applicable)
		headerString = headerString + addDelimiter();

		// Sam Version (Not applicable)
		headerString = headerString + addDelimiter();

		// SAM Hash (Not applicable)
		headerString = headerString + addDelimiter();

		// SCM Version (Not applicable)
		headerString = headerString + addDelimiter();

		// SCM Hash (Not applicable)
		headerString = headerString + addDelimiter();
		headerString = headerString + endLine();

		return headerString;
	}

	public String getFileType() {
		return "NS1";
	}

	private String softwareUsedName() {
		return "ACCOUNTER1.0.5";
	}

	private String getTanofDeductor() {

		return deductor.getTanNumber();
	}

	/**
	 * Mention the date o fcreation of the file in ddmmyyyy format.
	 * 
	 * @return
	 */
	private String addTodaysDate() {
		ClientFinanceDate date = new ClientFinanceDate();

		String day1, month1, year1;
		if (Integer.toString(date.getDay()).length() < 2) {
			day1 = "0" + Integer.toString(date.getDay());
		} else {
			day1 = Integer.toString(date.getDay());
		}
		if (Integer.toString(date.getMonth()).length() < 2) {
			month1 = "0" + Integer.toString(date.getMonth());
		} else {
			month1 = Integer.toString(date.getMonth());
		}
		if (Integer.toString(date.getYear()).length() < 2) {
			year1 = "0" + Integer.toString(date.getYear());
		} else {
			year1 = Integer.toString(date.getYear());
		}

		return "01" + "07" + "2011";

	}

	/**
	 * generate the Batch Header for the file
	 * 
	 * @param total
	 * 
	 * @return
	 */
	String generateBatchHeaderRecord(Double total) {

		String batchHeaderString;

		// Running Sequence Number for each line in the file
		batchHeaderString = "2" + addDelimiter();

		// Value should be "BH" (Batch Header) for the batch header record
		batchHeaderString = batchHeaderString + "BH" + addDelimiter();

		// Batch Number Value must start with 1.
		batchHeaderString = batchHeaderString + "1" + addDelimiter();

		batchHeaderString = batchHeaderString + getChalanCount()
				+ addDelimiter();

		// Form Number Value must be 26Q.
		batchHeaderString = batchHeaderString + generateFormText(formNo)
				+ addDelimiter();

		// Transaction Type (Not applicable) NA 0 O No value should be specified
		// Batch Updation Indicator (Not applicable) NA 0 O No value should be
		// specified
		// Original RRR No. (Provisional Receipt Number of REGULAR File) - (Not
		// applicable) NA 0 O No value should be specified
		// Previous RRR Number (Not applicable) NA 0 O No value should be
		// specified
		// RRR Number (Provisional Receipt Number)- (Not applicable) NA 0 O No
		// value should be specified
		// RRR Date (provisional Receipt Date) - (Not applicable) NA 0 O No
		// value should be specified
		// Last TAN of Deductor / Collector ( Used for Verification) (Not
		// applicable) NA 0 O No value should be specified
		batchHeaderString = batchHeaderString + addDelimiter() + addDelimiter()
				+ addDelimiter() + addDelimiter() + addDelimiter()
				+ addDelimiter() + addDelimiter();

		batchHeaderString = batchHeaderString + getTanofDeductor()
				+ addDelimiter();

		// No value should be specified
		batchHeaderString = batchHeaderString + addDelimiter();

		batchHeaderString = batchHeaderString + getPanOfDeductor()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getAssesmentYear()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getFinancialYear()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getYearQuarter()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getNameDeductor()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getDeductorBranch()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getDeductorAddress(1)
				+ addDelimiter();
		batchHeaderString = batchHeaderString + getDeductorAddress(2)
				+ addDelimiter();
		batchHeaderString = batchHeaderString + getDeductorAddress(3)
				+ addDelimiter();
		batchHeaderString = batchHeaderString + getDeductorAddress(4)
				+ addDelimiter();
		batchHeaderString = batchHeaderString + getDeductorAddress(5)
				+ addDelimiter();
		batchHeaderString = batchHeaderString + getDeductorAddress(6)
				+ addDelimiter();

		// batchHeaderString = batchHeaderString + getState() + addDelimiter();

		// PIN Code of Deductor
		batchHeaderString = batchHeaderString + getDeductorPINCode()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getDeductorEmailID()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getDeductorSTDCode()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getDeductorTelephoneNumber()
				+ addDelimiter();

		batchHeaderString = batchHeaderString
				+ getResponsiblePersonAddressChange() + addDelimiter();

		batchHeaderString = batchHeaderString + getDeductorType()
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonName()
				+ addDelimiter();

		batchHeaderString = batchHeaderString
				+ getResponsiblePersonDesignation() + addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonAddress(1)
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonAddress(2)
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonAddress(3)
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonAddress(4)
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonAddress(5)
				+ addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonAddress(6)
				+ addDelimiter();

		// batchHeaderString = batchHeaderString + getResponsiblePersonState()
		// + addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonPin()
				+ addDelimiter();

		batchHeaderString = batchHeaderString
				+ getResponsiblePersonValidEmailID() + addDelimiter();

		batchHeaderString = batchHeaderString
				+ getResponsiblePersonMobileNumber() + addDelimiter();

		batchHeaderString = batchHeaderString + getResponsiblePersonSTDCode()
				+ addDelimiter();

		batchHeaderString = batchHeaderString
				+ getResponisblePersonTelephoenNumebr() + addDelimiter();

		batchHeaderString = batchHeaderString
				+ getAddressChangeOFResponsiblePerson() + addDelimiter();

		batchHeaderString = batchHeaderString + getAmountAsString(total)
				+ addDelimiter();

		// Unmatched challan count
		// Count of Salary Details Records (Not applicable)
		// Batch Total of - Gross Total Income as per Salary Detail (Not
		// applicable)
		batchHeaderString = batchHeaderString + addDelimiter() + addDelimiter()
				+ addDelimiter();

		// AO Approval Value should be "N"
		batchHeaderString = batchHeaderString + "N" + addDelimiter();

		// AO Approval Number CHAR 15 O No value should be specified
		// Last Deductor Type CHAR 1 NA No value should be specified
		batchHeaderString = batchHeaderString + addDelimiter();
		// batchHeaderString = batchHeaderString + addDelimiter();

		batchHeaderString = batchHeaderString + getGovtSateName();

		batchHeaderString = batchHeaderString + getGovtPAOCode();

		batchHeaderString = batchHeaderString + getGovtDDOCode();

		batchHeaderString = batchHeaderString + getGovtMinistryName();

		batchHeaderString = batchHeaderString + getMinistryOtherName();

		// Filler 2
		batchHeaderString = batchHeaderString + addDelimiter();

		batchHeaderString = batchHeaderString + getGovtPAORegistrationCode();

		batchHeaderString = batchHeaderString + getDDORegistationCode();

		// Record Hash (Not applicable)
		batchHeaderString = batchHeaderString + addDelimiter();
		batchHeaderString = batchHeaderString + endLine();
		return batchHeaderString;
	}

	private String generateFormText(String formNo2) {
		int parseInt = Integer.parseInt(formNo2);
		if (parseInt == 1) {
			return "26Q";
		} else if (parseInt == 2) {
			return "27Q";
		} else if (parseInt == 3) {
			return "27EQ";
		} else {
			return null;
		}

	}

	/**
	 * Optional for deductor type Central Govt. (A), State Govt. (S), Statutory
	 * Body - Central Govt. (D), Statutory Body - State Govt. (E), Autonomous
	 * body - Central Govt. (G), Autonomous body - State Govt. (H), Local
	 * Authority - Central Govt. (L) & Local Authority - State Govt. (N). For
	 * other deductor type no value should be provided.
	 * 
	 * @return
	 */
	private String getDDORegistationCode() {

		String ddoRegistration = deductor.getDdoRegistration();
		if (ddoRegistration == null) {
			return addDelimiter();
		} else {
			return ddoRegistration + addDelimiter();
		}
	}

	/**
	 * Optional for deductor type Central Govt. (A), State Govt. (S), Statutory
	 * Body - Central Govt. (D), Statutory Body - State Govt. (E), Autonomous
	 * body - Central Govt. (G), Autonomous body - State Govt. (H), Local
	 * Authority - Central Govt. (L) & Local Authority - State Govt. (N). For
	 * other deductor type no value should be provided.
	 * 
	 * @return
	 */
	private String getGovtPAORegistrationCode() {

		String ddoRegistration = deductor.getDdoRegistration();
		if (ddoRegistration == null) {
			return addDelimiter();
		} else {
			return ddoRegistration + addDelimiter();
		}
	}

	/**
	 * If numeric code '99' (i.e. Other) is provided in Ministry Name field then
	 * value in Ministry Name "Other" field should be provided
	 * 
	 * @return
	 */
	private String getMinistryOtherName() {
		String ministryDeptName = deductor.getMinistryDeptName();

		if (ministryDeptName.length() > 0) {
			return getMinistryCode(ministryDeptName) + addDelimiter();
		} else {
			return addDelimiter();
		}

	}

	/**
	 * Numeric code for Ministry name should be provided. For list of Ministry
	 * name codes, refer to the Annexure 3 below. Mandatory for deductor type
	 * Central Govt (A), Statutory body - Central Govt. (D) & Autonomous body -
	 * Central Govt. (G). Optional for deductor type Statutory body - State
	 * Govt. (E), Autonomous body - State Govt. (H), Local Authority - Central
	 * Govt. (L) & Local Authority -State Govt. (N). For other deductor type no
	 * value should be provided.
	 * 
	 * @return
	 */
	private String getGovtMinistryName() {
		String ministryDeptName = deductor.getMinistryDeptName();
		if (ministryDeptName.length() > 0) {
			return getMinistryCode(ministryDeptName) + addDelimiter();
		} else {
			return addDelimiter();
		}
	}

	/**
	 * Mandatory for deductor type Central Government (A). Optional for deductor
	 * type State Government (S), Statutory body - Central Govt. (D), Statutory
	 * body - State Govt. (E), Autonomous body - Central Govt. (G), Autonomous
	 * body - State Govt. (H), Local Authority -Central Govt. (L) & Local
	 * Authority - State Govt. (N). For other deductor type no value should be
	 * provided.
	 * 
	 * @return
	 */
	private String getGovtDDOCode() {
		String ddoCode = deductor.getDdoCode();
		if (ddoCode == null) {
			return addDelimiter();
		} else {
			return ddoCode + addDelimiter();
		}

	}

	/**
	 * Mandatory for central govt (A). Optional for deductor type State Govt.
	 * (S), Statutory body - Central Govt. (D), Statutory body - State Govt.
	 * (E), Autonomous body - Central Govt. (G), Autonomous body - State Govt.
	 * (H), Local Authority - Central Govt. (L) & Local Authority - State Govt.
	 * (N). For other deductor type no value should be provided.
	 * 
	 * @return
	 */
	private String getGovtPAOCode() {

		String paoCode = deductor.getPaoCode();
		if (paoCode == null) {
			return addDelimiter();
		} else {
			return paoCode + addDelimiter();

		}
	}

	/**
	 * Numeric code for state should be mentioned as per Annexure 5. Mandatory
	 * if deductor type is State Govt. (code S), Statutory body - State Govt.
	 * (code E), Autonomous body - State Govt. code H) and Local Authority -
	 * State Govt. (code N). For other deductor category no value should be
	 * provided.
	 * 
	 * @return
	 */
	private String getGovtSateName() {
		if (deductor.getGovtState() != null) {
			if (deductor.getGovtState().length() < 1)
				return addDelimiter();
			else
				return getStateCode(deductor.getGovtState()) + addDelimiter();
		} else
			return addDelimiter();

	}

	// /**
	// * Mention the Total of Deposit Amount as per Challan.The value here
	// should
	// * be same as sum of values in field 'Total of Deposit Amount as per
	// * Challan' in the 'Challan Detail' record ( please refer to the Challan
	// * Detail' record section below ). Paisa Field (Decimal Value) of the
	// Amount
	// * must be 00.
	// *
	// * @return
	// */
	// private String getTotalDespositeAmount() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	/**
	 * Change of Address of Responsible person since last Return CHAR 1 M "Y" if
	 * address has changed after filing last return, "N" otherwise.
	 * 
	 * @return
	 */
	private String getAddressChangeOFResponsiblePerson() {

		if (responsiblePerson.isAddressChanged()) {
			return "Y";
		} else {
			return "N";
		}
	}

	/**
	 * Mention telephone number if value present in field no.44 (Responsible
	 * Person's STD code). Either mobile no. should be provided or Telephone no.
	 * and STD code of deductor or responsible person should be provided.
	 * 
	 * @return
	 */
	private String getResponisblePersonTelephoenNumebr() {
		long telephoneNumber = responsiblePerson.getTelephoneNumber();
		if (telephoneNumber > 0) {
			return Long.toString(telephoneNumber);
		} else {
			return "";
		}

	}

	/**
	 * Mention STD code if value present in field no.45 (Responsible Person's
	 * Tel-Phone No.).
	 * 
	 * @return
	 */
	private String getResponsiblePersonSTDCode() {
		long stdNumber = responsiblePerson.getStdCode();
		if (stdNumber > 0) {
			return Long.toString(stdNumber);
		} else {
			return "";
		}
	}

	/**
	 * Mention 10 digit mobile no. Mandatory for Deductor category other than
	 * Central Govt. and State Govt. For deductor category Central Govt. and
	 * State Govt. either mobile no. should be provided or Telephone no. and STD
	 * code of deductor or responsible person should be provided.
	 * 
	 * @return
	 */
	private String getResponsiblePersonMobileNumber() {

		long mobileNum = responsiblePerson.getMobileNumber();
		if (mobileNum > 0) {
			return Long.toString(mobileNum);
		} else {
			return "";
		}
	}

	/**
	 * Valid E-mail should be provided. 1. Email format must be checked -atleast @
	 * and '.' should be mentioned. 2. Both @ and '.' should be preceded and
	 * succeeded by atleast one character. 3. At least one '.' should come after
	 * '@'. 4. All printable characters allowed except '^' and space. E-mail id
	 * of deductor/collector or person responsible for deducting/collecting tax
	 * should be provided.
	 * 
	 * @return
	 */
	private String getResponsiblePersonValidEmailID() {
		return responsiblePerson.getEmailAddress();
	}

	/**
	 * Responsible Person's PIN INTEGER 6 M PIN Code of Responsible Person .
	 * 
	 * @return
	 */
	private String getResponsiblePersonPin() {
		return Long.toString(responsiblePerson.getPinCode());
	}

	// /**
	// * Responsible Person's State INTEGER 2 M Numeric code for state. For list
	// * of State codes, refer to the Annexure below.
	// *
	// * @return
	// */
	// private String getResponsiblePersonState() {
	//
	// String code = null;
	// List<String> stateNames = Utility.getStatesList();
	//
	// for (int i = 0; i < stateNames.size(); i++) {
	// if (responsiblePerson.getStateName().equals(stateNames.get(i))) {
	// code = stateNames.get(i);
	// }
	// }
	// return code;
	// }

	/**
	 * Responsible Person's Address1 CHAR 25 M Mention the address of the
	 * responsible Person . Responsible Person's Address2 CHAR 25 O Length <= 25
	 * . Responsible Person's Address3 CHAR 25 O Length <= 25 . Responsible
	 * Person's Address4 CHAR 25 O Length <= 25 . Responsible Person's Address5
	 * CHAR 25 O Length <= 25 .
	 * 
	 * @param i
	 * @return
	 */
	private String getResponsiblePersonAddress(int i) {
		String address = null;
		switch (i) {
		case 1:
			address = responsiblePerson.getFlatNo();
			// if (responsiblePerson.getBuildingName() != null)
			// address = responsiblePerson.getBuildingName();
			// else
			// address = addDelimiter();
			break;
		case 2:
			address = responsiblePerson.getBuildingName();
			// if (responsiblePerson.getStreet() != null)
			// address = responsiblePerson.getStreet();
			// else
			// address = addDelimiter();
			break;
		case 3:
			address = responsiblePerson.getStreet();
			// if (responsiblePerson.getArea() != null)
			// address = responsiblePerson.getArea();
			// else
			// address = addDelimiter();
			break;
		case 4:
			address = responsiblePerson.getArea();
			// if (responsiblePerson.getCity() != null)
			// address = responsiblePerson.getCity();
			// else
			// address = addDelimiter();
			break;
		case 5:
			address = responsiblePerson.getCity();
			// address = addDelimiter();
			break;
		case 6:
			address = getStateCode(responsiblePerson.getStateName());
			break;
		default:
			break;
		}

		return address;
	}

	/**
	 * 34 Designation of the Person responsible for Deduction CHAR 20 M Mention
	 * the designation of Person responsible.
	 * 
	 * @return
	 */
	private String getResponsiblePersonDesignation() {
		if (responsiblePerson.getDesignation() != null)
			return responsiblePerson.getDesignation();
		else
			return "";
	}

	/**
	 * Name of Person responsible for Deduction CHAR 75 M Mention the Name of
	 * Person responsible for Deduction on behalf of the deductor.
	 * 
	 * @return
	 */
	private String getResponsiblePersonName() {
		return responsiblePerson.getResponsibleName();
	}

	/**
	 * Deductor Type CHAR 1 M Deductor category code to be mentioned as per
	 * Annexure 4
	 * 
	 * @return
	 */
	protected String getDeductorType() {
		String code = null;
		List<String> deductorCodes = Utility.getDeductorCodes();
		List<String> deductorTypes = Utility.getDeductorTypes();

		for (int i = 0; i < deductorCodes.size(); i++) {
			if (deductor.getDeductorType().equals(deductorTypes.get(i))) {
				code = deductorCodes.get(i);
				break;
			}
		}
		return code;

	}

	/**
	 * Change of Address of Responsible person since last Return CHAR 1 M "Y" if
	 * address has changed after filing last return, "N" otherwise.
	 * 
	 * @return
	 */
	private String getResponsiblePersonAddressChange() {

		if (responsiblePerson.isAddressChanged()) {
			return "Y";
		} else {
			return "N";
		}

	}

	/**
	 * Mention telephone number if value present in field no.29 (Employer /
	 * Deductor's STD code). Either mobile no. should be provided or Telephone
	 * no. and STD code of deductor or responsible person should be provided.
	 * 
	 * @return
	 */
	private String getDeductorTelephoneNumber() {
		long telephone = deductor.getTelephoneNumber();
		if (telephone > 0) {
			return Long.toString(telephone);
		} else {
			return "";
		}

	}

	/**
	 * Mention STD code if value present in field no.30 (Employer / Deductor's
	 * Tel-Phone No.).
	 * 
	 * @return
	 */
	private String getDeductorSTDCode() {
		String stdCode = deductor.getStdCode();
		if (stdCode != null) {
			return stdCode;
		} else {
			return "";
		}
	}

	/**
	 * Valid E-mail should be provided. 1. Email format must be checked -atleast @
	 * and '.' should be mentioned. 2. Both @ and '.' should be preceded and
	 * succeeded by atleast one character. 3. At least one '.' should come after
	 * '@'. 4. All printable characters allowed except '^' and space. E-mail id
	 * of deductor/collector or person responsible for deducting/collecting tax
	 * should be provided.
	 * 
	 * @return
	 */
	private String getDeductorEmailID() {
		return deductor.getEmailID();
	}

	private String getDeductorPINCode() {
		return Long.toString(deductor.getPinCode());
	}

	/**
	 * Numeric code for state. For list of State codes, refer to the Annexure 1
	 * below.
	 * 
	 * @return
	 */
	private String getState() {

		String state = deductor.getState();
		return getStateCode(state);
	}

	/**
	 * Mention the address of the Deductor. Length <= 25. Length <= 25. Length
	 * <= 25. Length <= 25.
	 * 
	 * @param i
	 * @return
	 */
	private String getDeductorAddress(int i) {

		String address = null;
		switch (i) {
		case 1:
			address = deductor.getFlatNo();
			// if (deductor.getBuildingName().length() > 1)
			// address = deductor.getBuildingName();
			// else
			// address = addDelimiter();
			break;
		case 2:
			address = deductor.getBuildingName();
			// if (deductor.getRoadName().length() > 1)
			// address = deductor.getRoadName();
			// else
			// address = addDelimiter();
			break;
		case 3:
			address = deductor.getRoadName();
			// if (deductor.getArea().length() > 1)
			// address = deductor.getArea();
			// else
			// address = addDelimiter();
			break;
		case 4:
			address = deductor.getArea();
			// if (deductor.getCity().length() > 1)
			// address = deductor.getCity();
			// else
			// address = addDelimiter();
			break;
		case 5:
			address = deductor.getCity();
			// address = addDelimiter();
			break;
		case 6:
			address = getState();
			break;
		default:
			break;
		}

		return address;
	}

	/**
	 * Branch/Division of Deductor its optional
	 * 
	 * @return
	 */
	private String getDeductorBranch() {
		return deductor.getBranch();
	}

	/**
	 * Mention the Name of the Deductor I.e. Deductor who deducts tax.
	 * 
	 * @return
	 */
	private String getNameDeductor() {
		return deductor.getDeductorName();
	}

	/**
	 * Valid values Q1, Q2, Q3, Q4 of the financial Year.
	 * 
	 * @return
	 */
	private String getYearQuarter() {
		int parseInt = Integer.parseInt(quater);
		if (parseInt == 1) {
			return "Q1";
		} else if (parseInt == 2) {
			return "Q2";
		} else if (parseInt == 3) {
			return "Q3";
		} else {
			return "Q4";
		}
	}

	/**
	 * Financial year e.g. value should be 200506 for Financial Yr 2005-06.
	 * 'Assessment year' - 'Financial Year' must be = 1. The financial Year
	 * cannot be a future financial year
	 */
	private String getFinancialYear() {
		String[] split = endYear.split("");
		String finalDate = startYear + split[3] + split[4];
		return finalDate;
	}

	/**
	 * Assessment year e.g. value should be 200607 for assessment yr 2006-07 ` *
	 * 
	 * @return
	 */
	private String getAssesmentYear() {

		String t1 = Integer.toString((Integer.parseInt(startYear) + 1));
		String t2 = Integer.toString((Integer.parseInt(endYear) + 1));

		String[] split = t2.split("");

		String finalDate = t1 + split[3] + split[4];
		return finalDate;
	}

	/**
	 * Mandatory to mention the PAN of the Deductor. If deductor is not //
	 * required to have a PAN mention PANNOTREQD
	 * 
	 * @return
	 */
	private String getPanOfDeductor() {
		return deductor.getPanNumber();
	}

	/**
	 * Count of total number of challans/transfer vouchers contained within the
	 * batch.
	 * 
	 * @return
	 */
	private String getChalanCount() {
		return Integer.toString(chalanCount);
	}

	/**
	 * returns the state code according to the state name provided
	 * 
	 * @param stateName
	 * @return
	 */
	String getStateCode(String stateName) {

		String stateCode = null;
		List<String> statesName = new ArrayList<String>();
		statesName = Utility.getStatesList();
		int code = 1;
		for (String string : statesName) {
			if (string.equals(stateName)) {
				stateCode = Integer.toString(code);
				break;
			} else if (string.equals("OTHERS")) {
				stateCode = Integer.toString(99);
			}
			code++;
		}

		if (Integer.parseInt(stateCode) < 10) {
			return "0" + stateCode;
		} else {
			return stateCode;
		}

	}

	/**
	 * this returns the ministry code depending on the ministry name
	 * 
	 * @param sectionName
	 * @return
	 */
	String getMinistryCode(String ministryName) {

		String ministryCode = "1";
		List<String> ministryNameList = new ArrayList<String>();
		ministryNameList = Utility.getMinistryType();
		int code = 1;
		for (String string : ministryNameList) {
			if (string.equals(ministryName)) {
				ministryCode = Integer.toString(code);
				break;
			} else if (string.equals("Others")) {
				ministryCode = Integer.toString(99);
				break;
			} else {
				ministryCode = addDelimiter();
			}
			code++;
		}
		return ministryCode;
	}

	/**
	 * returns the deductor value depeding on the deductor type
	 * 
	 * @param sectionName
	 * @return
	 */
	String getDeductorValue(String sectionName) {
		return sectionName;
	}

	public void setFormDetails(String formNo, String quater, String startYear,
			String endYear) {
		this.formNo = formNo;
		this.quater = quater;
		this.startYear = startYear;
		this.endYear = endYear;

	}

	public void setChalanCount(int size) {
		chalanCount = size;
	}

	public String getAmountAsString(double amount) {

		DecimalFormat format = new DecimalFormat();
		format.applyPattern("#0.00");

		return format.format(amount);
	}

	public String getRate(double rate) {

		DecimalFormat format = new DecimalFormat();
		format.applyPattern("#0.0000");

		return format.format(rate);
	}

	public String getAmountWithNoFractionPortion(double amount) {
		return getAmountAsString(amount / 1);
	}

	protected String getDateAsString(FinanceDate date) {
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern("ddMMyyyy");
		return format.format(date.getAsDateObject());
	}

	public List<ClientTDSChalanDetail> getChalanDetailsList() {
		return chalanDetailsList;
	}

	public void setChalanDetailsList(
			List<ClientTDSChalanDetail> chalanDetailsList) {
		this.chalanDetailsList = chalanDetailsList;
	}

	public abstract String generateFile();
}
