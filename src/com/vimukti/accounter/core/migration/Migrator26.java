package com.vimukti.accounter.core.migration;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.ItemUtils;

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
