package com.vimukti.accounter.core.migration;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.ItemUtils;

public class Migrator15 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator15.");

		Session session = getSession();
		Query query = session.getNamedQuery("get.buildassembly.items")
				.setEntity("company", company);
		List<Item> items = query.list();

		ItemUtils.remapSalesPurchases(items);

		log.info("Finished Migrator15.");
	}

	@Override
	public int getVersion() {
		return 15;
	}

}
