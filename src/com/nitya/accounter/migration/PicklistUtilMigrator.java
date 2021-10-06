package com.nitya.accounter.migration;

import com.nitya.accounter.core.AttendancePayHead;
import com.nitya.accounter.core.ComputionPayHead;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.web.client.core.ClientAttendancePayHead;
import com.nitya.accounter.web.client.core.ClientPayHead;

public class PicklistUtilMigrator {
	static String depreciationForIdentity(int type) {
		switch (type) {
		case 1:
			return "ALL";
		case 2:
			return "SINGLE";
		}
		return null;
	}

	static String depreciationMethodIdentity(int type) {
		switch (type) {
		case 1:
			return "STRAIGHT_LINE";
		case 2:
			return "REDUCING_BALANCE";
		}
		return null;
	}

	static String depreciationStatusIdentity(int type) {
		switch (type) {
		case 1:
			return "APPROVE";
		case 2:
			return "ROLL_BACK";
		}
		return null;
	}

	static String getAccountTypeIdentity(int type) {
		switch (type) {
		case 1:
			return "CASH";
		case 2:
			return "BANK";
		case 3:
			return "ACCOUNT_RECEIVABLE";
		case 4:
			return "OTHER_CURRENT_ASSET";
		case 5:
			return "INVENTORY_ASSET";
		case 6:
			return "FIXED_ASSET";
		case 7:
			return "OTHER_ASSET";
		case 8:
			return "ACCOUNT_PAYABLE";
		case 9:
			return "OTHER_CURRENT_LIABILITY";
		case 10:
			return "CREDIT_CARD";
		case 11:
			return "OTHER_CURRENT_LIABILITY";
		case 12:
			return "LONG_TERM_LIABILITY";
		case 13:
			return "EQUITY";
		case 14:
			return "INCOME";
		case 15:
			return "COST_OF_GOODS_SOLD";
		case 16:
			return "EXPENSE";
		case 17:
			return "OTHER_INCOME";
		case 18:
			return "OTHER_EXPENSE";
		case 19:
			return "OTHER_CURRENT_LIABILITY";
		case 20:
			return "OTHER_ASSET";
		case 21:
			return "PAY_PAL";
		}
		return null;
	}

	static String getFixedAssetStatusIdentifier(int i) {
		switch (i) {
		case 1:
			return "PENDING";
		case 2:
			return "REGISTER";
		case 3:
			return "DISPOSE";
		}
		return null;
	}

	static String getItemTypeIdentifier(int i) {
		switch (i) {
		case Item.TYPE_SERVICE:
			return "SERVICE_ITEM";
		case Item.TYPE_NON_INVENTORY_PART:
			return "NON_INVENTORY";
		case Item.TYPE_INVENTORY_PART:
			return "INVENTORY_PART";
		case Item.TYPE_INVENTORY_ASSEMBLY:
			return "INVENTORY_ASSEMBLY";
		}
		return null;
	}

	static String getPaymentMethodIdentifier(String name) {
		switch (name) {
		case "Credit Card":
			return "CREDIT_CARD";
		case "Direct Debit":
			return "DIRECT_DEBIT";
		case "Master card":
			return "MASTER_CARD";
		case "Online Banking":
			return "ONLINE_BANKING";
		case "Standing Order":
			return "STANDING_ORDER";
		case "Switch/Maestro":
			return "SWITCH_MAESTRO";
		case "Check":
			return "CHEQUE";
		case "Cash":
		case "Paypal":
			return "CASH";
		}
		return name;
	}

	static String getTransactionTypeIdentifier(int displayName, int innerType) {
		String identifier = "";

		switch (displayName) {
		case Transaction.TYPE_BUILD_ASSEMBLY:
			identifier = "BUILD_ASSEMBLY";
			break;
		case Transaction.TYPE_CASH_SALES:
			identifier = "CASH_SALE";
			break;
		case Transaction.TYPE_CASH_PURCHASE:
			identifier = "CASH_PURCHASE";
			break;
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
			identifier = "PURCHASE_EXPENSE";
			break;
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			identifier = "CREDIT_MEMO";
			break;
		case Transaction.TYPE_CUSTOMER_REFUNDS:
			identifier = "CUSTOMER_REFUND";
			break;
		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
			identifier = "CUSTOMER_PREPAYMENT";
			break;
		case Transaction.TYPE_ENTER_BILL:
			identifier = "ENTER_BILL";
			break;
		case Transaction.TYPE_ESTIMATE:
			if (Estimate.QUOTES == innerType) {
				identifier = "SALES_QUOTATION";
			} else if (Estimate.SALES_ORDER == innerType) {
				identifier = "SALES_ORDER";
			} else if (Estimate.CREDITS == innerType) {
				identifier = "CREDIT";
			}
			break;
		case Transaction.TYPE_INVOICE:
			identifier = "INVOICE";
			break;
		case Transaction.TYPE_MAKE_DEPOSIT:
			identifier = "MAKE_DEPOSIT";
			break;
		case Transaction.TYPE_PAY_BILL:
			identifier = "PAY_BILL";
			break;
		case Transaction.TYPE_VENDOR_PAYMENT:
			identifier = "VENDOR_PREPAYMENT";
			break;
		case Transaction.TYPE_RECEIVE_PAYMENT:
			identifier = "RECEIVE_PAYMENT";
			break;
		case Transaction.TYPE_TRANSFER_FUND:
			identifier = "TRANSFER_FUND";
			break;
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
			identifier = "DEBIT_NOTE";
			break;
		case Transaction.TYPE_WRITE_CHECK:
			identifier = "WRITE_CHECK";
			break;
		case Transaction.TYPE_JOURNAL_ENTRY:
			identifier = "JOURNAL_ENTRY";
			break;
		case Transaction.TYPE_PAY_TAX:
			identifier = "PAY_TAX";
			break;
		case Transaction.TYPE_TAX_RETURN:
			identifier = "FILE_TAX";
			break;
		case Transaction.TYPE_RECEIVE_TAX:
			identifier = "TAX_REFUND";
			break;
		case Transaction.TYPE_ADJUST_VAT_RETURN:
			identifier = "TAX_ADJUSTMENT";
			break;
		case Transaction.TYPE_PURCHASE_ORDER:
			identifier = "PURCHASE_ORDER";
			break;
		case Transaction.TYPE_CASH_EXPENSE:
			identifier = "PURCHASE_EXPENSE";
			break;
		case Transaction.TYPE_CREDIT_CARD_EXPENSE:
			identifier = "PURCHASE_EXPENSE";
			break;
		case Transaction.TYPE_STOCK_ADJUSTMENT:
			identifier = "STOCK_ADUJUSTMENT";
			break;
		case Transaction.TYPE_TDS_CHALLAN:
			identifier = "TDS_CHALLAN";
			break;
		case Transaction.TYPE_PAY_RUN:
			identifier = "PAY_RUN";
			break;
		case Transaction.TYPE_PAY_EMPLOYEE:
			identifier = "PAY_EMPLOYEE";
			break;
		}
		return identifier;
	}

	public static String getProjectStatusIdentity(String status) {

		switch (status) {
		case "NONE":
			return null;
		case "Pending":
			return "PENDING";
		case "Awarded":
			return "AWARDED";
		case "In Progress":
			return "INPROGRESS";
		case "Closed":
			return "CLOSED";
		case "Not Awarded":
			return "NOT_AWARDED";
		}
		return "";
	}

	public static String getDeductorTypeIndentity(String deductorType) {
		switch (deductorType) {
		case "Central Government":
			return "CENTRAL_GOVERNMENT";
		case "State Government":
			return "STATE_GOVERNMENT";
		case "Statutory body (Central Govt.)":
			return "CENTRAL_GOVT_STATUTORY_BODY";
		case "Statutory body (State Govt.)":
			return "STATE_GOVT_STATUTORY_BODY";
		case "Autonomous body (Central Govt.)":
			return "CENTRAL_GOVT_AUTONOMOUS_BODY";
		case "Autonomous body (State Govt.)":
			return "STATE_GOVT_AUTONOMOUS_BODY";
		case "Local Authority (Central Govt.)":
			return "CENTRAL_GOVT_LOCAL_AUTHORITY";
		case "Local Authority (State Govt.)":
			return "STATE_GOVT_LOCAL_AUTHORITY";
		case "Company":
			return "COMPANY";
		case "Association of Person (AOP)":
			return "ASSOCIATION_OF_PERSON";
		case "Association of Person (Trust)":
			return "ASSOCIATION_OF_PERSON_TRUST";
		case "Artificial Juridical Person":
			return "ARTIFICIAL_JURIDICAL_PERSON";
		case "Body of Individuals":
			return "BODY_OF_INDIVIDUALS";
		case "Individual/HUF":
			return "INDIVIDUAL_HUF";
		case "Firm":
			return "FIRM";
		case "Branch / Division of Company":
			return "BRANCH_DIVISION_OF_COMPANY";
		}
		return null;
	}

	static String getCalculationPeriod(int calculationPeriod) {
		switch (calculationPeriod) {
		case ClientPayHead.CALCULATION_PERIOD_HOURS:
			return "HOURS";
		case ClientPayHead.CALCULATION_PERIOD_DAYS:
			return "DAYS";

		case ClientPayHead.CALCULATION_PERIOD_MONTHS:
			return "MONTHS";

		case ClientPayHead.CALCULATION_PERIOD_WEEKS:
			return "WEEKS";

		default:
			return null;
		}
	}

	static String getPerdayCalculationBasis(int perDayCalculationBasis) {
		switch (perDayCalculationBasis) {
		case ClientAttendancePayHead.PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD:
			return "AS_PER_CALENDAR_PERIOD";

		case ClientAttendancePayHead.PER_DAY_CALCULATION_30_DAYS:
			return "Days_30";

		case ClientAttendancePayHead.PER_DAY_CALCULATION_USER_DEFINED_CALANDAR:
			return "USER_DEFINED_CALENDAR";

		default:
			return null;
		}
	}

	static String getComputationType(int computationType) {
		switch (computationType) {
		case ComputionPayHead.COMPUTATE_ON_SUBTOTAL:
			return "ON_SUB_TOTAL";

		case ComputionPayHead.COMPUTATE_ON_DEDUCTION_TOTAL:
			return "ON_DEDUCTION_TOTAL";

		case ComputionPayHead.COMPUTATE_ON_EARNING_TOTAL:
			return "ON_EARNING_TOTAL";

		case ComputionPayHead.COMPUTATE_ON_SPECIFIED_FORMULA:
			return "ON_SPECIFIED_FORMULA";
		}
		return null;
	}

	static String getAttendanceType(int attendanceType) {
		switch (attendanceType) {
		case AttendancePayHead.ATTENDANCE_ON_PAYHEAD:
			return "OTHER_PAYHEAD";

		case AttendancePayHead.ATTENDANCE_ON_EARNING_TOTAL:
			return "ON_EARNING_TOTAL";

		case AttendancePayHead.ATTENDANCE_ON_SUBTOTAL:
			return "ON_SUB_TOTAL";

		case AttendancePayHead.ATTENDANCE_ON_RATE:
			return "RATE";

		default:
			break;
		}
		return null;
	}

	public static String getPaymentStatusIdentifier(int status) {
		switch (status) {
		case 0:
			return "NOT_ISSUED";
		case 2:
			return "ISSUED";
		}
		return "";
	}

	public static String getQuotationTypeIdentifier(int estimateType) {
		switch (estimateType) {
		case Estimate.CHARGES:
		case Estimate.QUOTES:
			return "QUOTATION";
		case Estimate.BILLABLEEXAPENSES:
			return "BILLABLE_EXPENSES";
		}
		return "";
	}

	static String getCalculationType(int type) {
		switch (type) {
		case ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE:
			return "ON_ATTENDANCE";

		case ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE:
			return "AS_COMPUTED_VALUE";
			
		case ClientPayHead.CALCULATION_TYPE_AS_TAX:
			return "AS_TAX";

		case ClientPayHead.CALCULATION_TYPE_FLAT_RATE:
			return "FLAT_RATE";

		case ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION:
			return "ON_PRODUCTION";

		case ClientPayHead.CALCULATION_TYPE_AS_USER_DEFINED:
			return "USER_DEFINED";

		default:
			return null;
		}
	}

	public static String getPayHeadType(int type) {
		switch (type) {
		case ClientPayHead.TYPE_EARNINGS_FOR_EMPLOYEES:
			return "EARNINGS_FOR_EMPLOYEES";

		case ClientPayHead.TYPE_DEDUCTIONS_FOR_EMPLOYEES:
			return "DEDUCTIONS_FOR_EMPLOYEES";

		case ClientPayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS:
			return "EMPLOYEES_STATUTORY_DEDUCTIONS";

		case ClientPayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS:
			return "EMPLOYEES_STATUTORY_CONTRIBUTIONS";

		case ClientPayHead.TYPE_EMPLOYEES_OTHER_CHARGES:
			return "EMPLOYEES_OTHER_CHARGES";

		case ClientPayHead.TYPE_BONUS:
			return "BONUS";

		case ClientPayHead.TYPE_LOANS_AND_ADVANCES:
			return "LOANS_AND_ADVANCES";

		case ClientPayHead.TYPE_REIMBURSEMENTS_TO_EMPLOYEES:
			return "REIMBURSEMENTS_TO_EMPLOYEES";

		default:
			return null;
		}
	}

	static String getComputationSlabType(int type) {
		switch (type) {
		case 1:
			return "PERCENTAGE";

		case 2:
			return "VALUE";
		}
		return null;
	}

	static String getPayHeadFormulaFunctionType(int type) {
		switch (type) {
		case 1:
			return "ADD_PAYHEAD";
		case 2:
			return "SUBTRACT_PAYHEAD";
		case 3:
			return "DIVIDE_ATTENDANCE";
		case 4:
			return "MULTIPLY_ATTENDANCE";
		}
		return null;
	}

	static String getCurrencyIdentity(String str) {
		return "";
	}
}
