/**
 * 
 */
package com.vimukti.accounter.company.initialize;

import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Company;

/**
 * @author Prasanna Kumar G
 * 
 */
public class OtherCompanyInitializer extends CompanyInitializer {

	/**
	 * Creates new Instance
	 */
	public OtherCompanyInitializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {
		initDefaultAccounts();
	}

	/**
	 * Initiates the Default Accounts
	 */
	private void initDefaultAccounts() {

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
