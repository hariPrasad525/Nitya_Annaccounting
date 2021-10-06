package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.InventoryRemapTask;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;

public class InventoryRemappingService extends Thread {

	private static final Logger LOG = Logger
			.getLogger(InventoryRemappingService.class);

	private class InventoyMap {
		long latestTaskId;
		long itemId;
		long userId;

		public InventoyMap(long itemId, long userId, long latestTask) {
			this.itemId = itemId;
			this.userId = userId;
			this.latestTaskId = latestTask;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		List<InventoyMap> items = new ArrayList<InventoyMap>();
		Session session = HibernateUtil.openSession();
		try {
			LOG.info("Loading inventory remap tasks...");
			Query query = session.getNamedQuery("getInventoryRemapTasks");
			List<Object[]> list = query.list();
			for (Object[] row : list) {
				items.add(new InventoyMap(Long.valueOf(row[0].toString()), Long
						.valueOf(row[1].toString()), Long.valueOf(row[2]
						.toString())));
			}
		} catch (Exception e) {
			LOG.error("Error while loading inventies to remap", e);
		} finally {
			session.close();
		}
		if (items.isEmpty()) {
			LOG.info("No inventory remap task found.");
			return;
		}
		LOG.info("Found inventory remap task. Count " + items.size());
		for (InventoyMap map : items) {
			remapInventory(map);
		}
		LOG.info("Finished executing inventory remap tasks.");
	}

	private void remapInventory(InventoyMap im) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			AccounterThreadLocal
					.set((User) session.load(User.class, im.userId));
			Item item = (Item) session.load(Item.class, im.itemId);
			LOG.info("Remapping invertort for the item '" + item.getName()
					+ "' of company '"
					+ AccounterThreadLocal.getCompany().getId());
			ItemUtils.remapSalesPurchases(item);
			deleteRemapTasks(session, im);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			LOG.error("Error while remapping inventory", e);
		} finally {
			AccounterThreadLocal.set(null);
			session.close();
		}
	}

	private void deleteRemapTasks(Session session, InventoyMap im) {
		Query q = session.getNamedQuery("deleteInventoryRemapTask");
		q.setLong("latestId", im.latestTaskId);
		q.setLong("itemId", im.itemId);
		q.executeUpdate();
	}

	public static void sheduleRemap(List<Item> inventory) {
		for (Item item : inventory) {
			sheduleRemap(item);
		}
	}

	public static void sheduleRemap(Item item) {
		InventoryRemapTask task = new InventoryRemapTask();
		task.setCompany(AccounterThreadLocal.getCompany());
		task.setUser(AccounterThreadLocal.get());
		task.setItem(item);
		 HibernateUtil.getCurrentSession().save(task);		
	}
}
