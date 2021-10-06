package com.nitya.accounter.core.migration;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IMigrator {

	/**
	 * Migrates the given company
	 * 
	 * @param company
	 */
	public void migrate(Company company) throws AccounterException;

	/**
	 * Returns the Migration Version
	 * 
	 * @return
	 */
	public int getVersion();
}
