package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class UserPreferences implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6090587330383430576L;
	String dashBoardPreferences = "WELCOME,BANKING_SUMMARY,PROFIT_AND_LOSS,CREDIT_OVERVIEW,DEBIT_OVERVIEW,LATEST_QUOTE,EXPENSES";
	String customerSectionViewPreferences = "NEW_CUSTOMER,SALES_ITEM,PAYMENT_RECEIVED,CASH_SALES,CREDIT_AND_REFUNDS";
	String vendorSectionViewPreferences = "NEW_VENDOR,ITEM_PURCHASE,BILL_PAID,CASH_PURCHASE";
	String bankingSectionViewPreferences = "BANKING_SUMMARY,CHECK_ISSUED,DEPOSITE,FUND_TRANSFERED,CREDIT_CARD_CHARGES";
	private int version;

	public UserPreferences() {

	}

	/**
	 * 
	 * @return DashBoard Preferences made last time user visited
	 */
	public String getDashBoardPreferences() {
		return dashBoardPreferences;
	}

	public String getCustomerSectionViewPreferences() {
		return customerSectionViewPreferences;
	}

	public String getVendorSectionViewPreferences() {
		return vendorSectionViewPreferences;
	}

	public String getBankingSectionViewPreferences() {
		return bankingSectionViewPreferences;
	}

	@Override
	public long getID() {

		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return true;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
