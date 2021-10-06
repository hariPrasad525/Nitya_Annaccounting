package com.nitya.accounter.core.migration;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CompanyPreferences;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.ItemUtils;

/**
 * All Transactions Migration
 * 
 */
public class Migrator26 extends Migrator21 {

	@Override
	public void migrate(Company company) throws AccounterException {

		super.migrate(company);

		if (company.getPreferences().getActiveInventoryScheme() != CompanyPreferences.INVENTORY_SCHME_AVERAGE) {
			ItemUtils.remapAllInventory(company);
		}

	}

	@Override
	public int getVersion() {
		return 26;
	}
}
