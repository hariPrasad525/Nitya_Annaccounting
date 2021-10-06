package com.vimukti.accounter.core.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.PortletConfiguration;
import com.vimukti.accounter.core.PortletPageConfiguration;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.portlet.PortletFactory;
import com.vimukti.accounter.web.client.portlet.PortletPage;

public class Migrator5 extends AbstractMigrator {
	Logger log = Logger.getLogger(Migrator5.class);

	@Override
	public void migrate(Company company) {
		log.info("Started Migrator5");
		Client client = company.getCreatedBy().getClient();
		int type = client.getClientSubscription().getSubscription().getType();
		if (type != Subscription.PREMIUM_USER) {
			List<String> defPortlets = new ArrayList<String>();
			defPortlets.add(PortletFactory.BANKING);
			defPortlets.add(PortletFactory.EXPENSES_CLAIM);
			defPortlets.add(PortletFactory.MONEY_COMING);
			defPortlets.add(PortletFactory.MONEY_GOING);

			CompanyPreferences preferences = company.getPreferences();
			preferences.setInventoryEnabled(false);
			preferences.setEnableMultiCurrency(false);
			preferences.setSalesOrderEnabled(false);
			preferences.setPurchaseOrderEnabled(false);

			Set<User> users = company.getUsers();
			for (User user : users) {
				Set<PortletPageConfiguration> portletPages = user
						.getPortletPages();
				for (PortletPageConfiguration pg : portletPages) {
					if (pg.getPageName().equals(PortletPage.DASHBOARD)) {
						List<PortletConfiguration> def = new ArrayList<PortletConfiguration>();
						List<PortletConfiguration> portlets = pg.getPortlets();

						for (PortletConfiguration pc : portlets) {
							if (defPortlets.contains(pc.getPortletName())) {
								def.add(pc);
								if (def.size() == defPortlets.size()) {
									break;
								}
							}
						}
						pg.setPortlets(def);
						break;
					}
				}
				HibernateUtil.getCurrentSession().saveOrUpdate(user);
			}
		}
		log.info("Fininshed Migrator5");
	}

	@Override
	public int getVersion() {
		return 5;
	}
}
