package com.vimukti.accounter.core.migration;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.StockTransfer;
import com.vimukti.accounter.core.StockTransferItem;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Migrator27 extends AbstractMigrator {

	@SuppressWarnings("unchecked")
	@Override
	public void migrate(Company company) throws AccounterException {
		log.info("Started Migrator" + getVersion());
		List<Object[]> list = getSession().getNamedQuery("getStockTransfers")
				.setParameter("companyId", company.getId()).list();

		for (Object[] objs : list) {
			final User createdBy = (User) getSession().get(User.class,
					(Long) objs[0]);
			final User lastModifiesBy = (User) getSession().get(User.class,
					(Long) objs[1]);

			final Timestamp createdOn = (Timestamp) objs[2];
			final Timestamp lastModifiedOn = (Timestamp) objs[3];

			Warehouse fromWareHouse = (Warehouse) getSession().get(
					Warehouse.class, (Long) objs[4]);
			Warehouse toWareHouse = (Warehouse) getSession().get(
					Warehouse.class, (Long) objs[5]);

			long stockTransferId = (Long) objs[6];

			AccounterThreadLocal.set(createdBy);

			StockTransfer stockTransfer = new StockTransfer();
			stockTransfer.setCompany(company);
			stockTransfer.setDate(new FinanceDate(createdOn));

			stockTransfer.setFromWarehouse(fromWareHouse);
			stockTransfer.setToWarehouse(toWareHouse);

			Query query = getSession().getNamedQuery("getStockTransfersItems")
					.setParameter("stockTranfer", stockTransferId);

			((SQLQuery) query).addEntity(StockTransferItem.class);

			List<StockTransferItem> sItems = query.list();

			Set<StockTransferItem> stockTransferItems = new HashSet<StockTransferItem>();

			stockTransferItems.addAll(sItems);

			stockTransfer.setStockTransferItems(stockTransferItems);

			getSession().saveOrUpdate(stockTransfer);
			// getSession().getNamedQuery("delete.StockTransfer")
			// .setParameter("stockTranfer", stockTransferId)
			// .executeUpdate();
		}

		Set<Warehouse> warehouses = company.getWarehouses();
		// Has Only one WareHouse, So set it to all Inventory History
		if (warehouses.size() == 1) {
			Query query = getSession()
					.getNamedQuery("updateInventoryHistory")
					.setParameter("warehouse",
							warehouses.iterator().next().getID())
					.setParameter("companyId", company.getId());
			query.executeUpdate();
		} else {
			// Migrate All Transactions
			for (Transaction t : company.getTransactions()) {
				// (201,202,204)
				int status = t.getSaveStatus();
				if (status == 201 || status == 202 || status == 204) {
					continue;
				}
				migrate(t);
			}
		}

		log.info("Finished Migrator" + getVersion());

	}

	@Override
	public int getVersion() {
		return 27;
	}

}
