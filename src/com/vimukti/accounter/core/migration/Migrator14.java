package com.vimukti.accounter.core.migration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.BuildAssembly;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TransactionEffectsImpl;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Migrator14 extends AbstractMigrator {

	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator14.");

		Session session = getSession();
		Query query = session.getNamedQuery("get.buildassemblies").setEntity(
				"company", company);
		List<BuildAssembly> assemblies = query.list();
		Set<Item> items = new HashSet<Item>();

		for (BuildAssembly ba : assemblies) {
			double total = 0.00D;
			for (TransactionItem ti : ba.getTransactionItems()) {
				total += ti.getLineTotal();
			}
			ba.setTotal(total);
			TransactionEffectsImpl effects = new TransactionEffectsImpl(ba);
			ba.getEffects(effects);
			if (!effects.isEmpty()) {
				effects.saveOrUpdate();
			}
			getSession().saveOrUpdate(ba);
			items.add(ba.getInventoryAssembly());
		}

		log.info("Finished Migrator14.");
	}

	@Override
	public int getVersion() {
		return 13;
	}

}
