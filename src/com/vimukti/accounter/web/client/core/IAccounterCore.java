/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * Implemented by All Client Core Class
 * 
 * @author Fernandez
 * 
 * 
 */
public interface IAccounterCore extends IsSerializable, Serializable,
		Cloneable, IVersionable {

	public static final int ACCOUNT = 51;
	public static final int CUSTOMER = 52;
	public static final int VENDOR = 53;
	public static final int TAXAGENCY = 54;
	public static final int ITEM = 55;
	public static final int ASSEMBLY = 91;
	public static final int BANK = 56;
	public static final int BRANDING_THEME = 57;
	public static final int CREDIT_RATING = 58;
	public static final int CURRENCY = 59;
	public static final int CUSTOMER_GROUP = 60;
	public static final int EMPLOYEE = 61;
	public static final int ITEM_GROUP = 62;
	public static final int LOCATION = 63;
	public static final int MEASUREMENT = 64;
	public static final int PAYMENT_TERMS = 65;
	public static final int PRICE_LEVEL = 66;
	public static final int SALES_PERSON = 67;
	public static final int SHIPPING_METHOD = 68;
	public static final int SHIPPING_TERMS = 69;
	public static final int USER = 70;
	public static final int VENDOR_GROUP = 71;
	public static final int WAREHOUSE = 72;
	public static final int TAXITEM = 73;
	public static final int TAXGROUP = 74;
	public static final int TAXCODE = 75;
	public static final int STOCK_ADJUSTMENT = 76;
	public static final int STOCK_TRANSFER = 77;
	public static final int ACCOUNTER_CLASS = 78;
	public static final int BANK_ACCOUNT = 79;
	public static final int FIXED_ASSET = 80;
	public static final int BUDGET = 81;
	public static final int RECONCILIATION = 82;
	public static final int TDSCHALANDETAIL = 83;
	public static final int TDSTRANSACTIONITEM = 84;
	public static final int TDSRESPONSIBLEPERSON = 85;
	public static final int TDSDEDUCTORMASTER = 86;
	public static final int REPORT_GROUP = 87;
	public static final int BANK_STATEMENT = 88;
	public static final int BANKSTATEMENT_RECORD = 88;
	public static final int JOB = 90;
	public static final int RECURING_TRANSACTION = 89;
	public static final int EMAIL_ACCOUNT = 90;
	public static final int EMAIL_TEMPLATE = 91;
	public static final int CHECK_LAYOUT = 94;
	public static final int CUSTOM_FIELD = 95;

	public static final int PAYROLL_UNIT = 101;
	public static final int PAY_HEAD = 102;
	public static final int PAY_STRUCTURE = 103;
	public static final int ATTENDANCE_PRODUCTION_TYPE = 104;
	public static final int EMPLOYEE_GROUP = 105;
	public static final int PAYPALTRANSACTION = 106;
	
	public static final int EMPLOYEE_TAX=107;
	public static final int EMPLOYEE_COMPENSANTION=108;

	public static final int INVOICE_FREQUENCY=109;


	String getName();

	// Display name as in Combo
	String getDisplayName();

	AccounterCoreType getObjectType();

	void setID(long id);

	long getID();

	// String getClientClassSimpleName();

}
