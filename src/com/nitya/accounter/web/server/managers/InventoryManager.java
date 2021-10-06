package com.nitya.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.BuildAssembly;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.CustomerRefund;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.core.StockAdjustment;
import com.nitya.accounter.core.StockTransfer;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.core.Unit;
import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.core.WriteCheck;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientBuildAssembly;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItemStatus;
import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientStockTransfer;
import com.nitya.accounter.web.client.core.ClientStockTransferItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientWarehouse;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.InvoicesList;
import com.nitya.accounter.web.client.core.Lists.TransactionsList;
import com.nitya.accounter.web.client.core.reports.InventoryStockStatusDetail;
import com.nitya.accounter.web.client.core.reports.InventoryValutionDetail;
import com.nitya.accounter.web.client.core.reports.InventoryValutionSummary;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.settings.StockAdjustmentList;
import com.nitya.accounter.web.server.ItemUtils;

public class InventoryManager extends Manager {

	private static final Object Integer = null;

	public PaginationList<InvoicesList> getInvoiceList(long companyId,
			long fromDate, long toDate, int invoicesType, int viewType,
			int start, int length) throws DAOException {

		try {
			Session session = HibernateUtil.getCurrentSession();
			int total = 0;
			List list;
			// FIXME :: query optimization
			Query query = session.getNamedQuery("getInvoicesList")
					.setParameter("companyId", companyId)
					.setLong("fromDate", fromDate).setLong("toDate", toDate)
					.setParameter("viewType", viewType)
					.setParameter("todayDate", new FinanceDate().getDate());

			if (invoicesType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				query = session.getNamedQuery("getCustomerCreditMemos")
						.setParameter("companyId", companyId)
						.setLong("fromDate", fromDate)
						.setLong("toDate", toDate)
						.setParameter("viewType", viewType);
			} else if (invoicesType == Transaction.TYPE_INVOICE) {
				query = session.getNamedQuery("getInvoicesOnly")
						.setParameter("companyId", companyId)
						.setLong("fromDate", fromDate)
						.setLong("toDate", toDate)
						.setParameter("viewType", viewType)
						.setParameter("todayDate", new FinanceDate().getDate());
			} else if (invoicesType == Transaction.TYPE_CASH_SALES) {
				query = session.getNamedQuery("getCashSalesList")
						.setParameter("companyId", companyId)
						.setLong("fromDate", fromDate)
						.setLong("toDate", toDate)
						.setParameter("viewType", viewType);
			}
			// /If length will be -1 then get list for mobile With out limits
			if (length == -1) {
				list = query.list();
			} else {
				total = query.list().size();
				list = query.setFirstResult(start).setMaxResults(length).list();
			}

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				PaginationList<InvoicesList> queryResult = new PaginationList<InvoicesList>();
				while ((iterator).hasNext()) {

					InvoicesList invoicesList = new InvoicesList();
					object = (Object[]) iterator.next();

					long transactionNumber = (object[0] == null ? 0
							: ((Long) object[0]));
					invoicesList.setTransactionId(transactionNumber);
					invoicesList.setType((Integer) object[1]);
					invoicesList
							.setDate(new ClientFinanceDate((Long) object[2]));
					invoicesList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					invoicesList.setCustomerName((String) object[4]);
					invoicesList.setNetAmount((Double) object[5]);
					invoicesList.setDueDate(object[6] == null ? null
							: new ClientFinanceDate((Long) object[6]));
					invoicesList.setTotalPrice((Double) object[7]);
					invoicesList.setBalance(object[8] == null ? null
							: (Double) object[8]);
					invoicesList.setVoided((Boolean) object[9]);
					invoicesList.setStatus((Integer) object[10]);
					invoicesList.setCurrency((Long) object[11]);
					invoicesList.setSaveStatus((Integer) object[12]);
					if (invoicesType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
						invoicesList.setRemainingCredits((Double) object[13]);
					}
					queryResult.add(invoicesList);
				}
				queryResult.setTotalCount(total);
				queryResult.setStart(start);
				return queryResult;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public PaginationList<TransactionsList> getReceivedTransactionsList(
			long companyId, long fromDate, long toDate, int start, int length)
			throws DAOException {

		try {
			Session session = HibernateUtil.getCurrentSession();
			int total = 0;

			PaginationList<TransactionsList> queryResult = new PaginationList<TransactionsList>();

			// for allreceived payments
			Query query = session.getNamedQuery("getAllCustomersPaymentsList")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate).setParameter("viewType", 0);
			List list = query.list();
			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				while ((iterator).hasNext()) {
					TransactionsList vendorPaymentsList = new TransactionsList();
					object = (Object[]) iterator.next();
					if ((Integer) object[11] == null && (Double) object[6] > 0) {
						vendorPaymentsList
								.setTransactionId((object[0] == null ? null
										: ((Long) object[0])));
						vendorPaymentsList.setType((Integer) object[1]);
						vendorPaymentsList.setDate(new ClientFinanceDate(
								object[2] == null ? null : ((Long) object[2])));
						vendorPaymentsList.setNumber((object[3] == null ? null
								: ((String) object[3])));
						vendorPaymentsList.setCustomerName((String) object[4]);
						vendorPaymentsList
								.setReceivedAmount((Double) object[6]);
						vendorPaymentsList.setCurrency((Long) object[10]);
						queryResult.add(vendorPaymentsList);
					}
				}
			}

			return queryResult;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public PaginationList<TransactionsList> getSpentTransactionsList(
			long companyId, long fromDate, long toDate, int start, int length)
			throws DAOException {
		int total = 0;
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		PaginationList<TransactionsList> customerRefundsList = new PaginationList<TransactionsList>();
		try {
			// for all customer payments
			Query query = session.getNamedQuery("getCustomerRefund")
					.setEntity("company", company)
					.setParameter("fromDate", new FinanceDate(fromDate))
					.setParameter("toDate", new FinanceDate(toDate));
			List list = query.list();

			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					TransactionsList customerRefund = new TransactionsList();
					CustomerRefund cr = (CustomerRefund) i.next();
					if (cr.getStatementRecord() == null
							&& cr.getBalanceDue() == 0) {
						customerRefund.setTransactionId(cr.getID());
						customerRefund.setType(cr.getType());
						customerRefund.setDate(new ClientFinanceDate(cr
								.getDate().getDate())); //
						// customerRefund.setPaymentDate(new
						// ClientFinanceDate(cr //
						// .getDate().getDate())); //
						// customerRefund.setIssueDate(new
						// ClientFinanceDate(cr // .getDate().getDate()));
						customerRefund.setDate(new ClientFinanceDate(cr
								.getDate().getDate()));
						customerRefund
								.setCustomerName(((cr.getPayTo() != null) ? cr
										.getPayTo().getName() : null));
						customerRefund.setSpentAmount(cr.getTotal());
						customerRefund.setCurrency(cr.getCurrency().getID());

						customerRefundsList.add(customerRefund);
					}
				}
			}
			query = session.getNamedQuery("getWriteCheck.by.payToType")
					.setParameter("type", WriteCheck.TYPE_CUSTOMER)
					.setEntity("company", company);
			list = query.list();
			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {

					TransactionsList customerRefund = new TransactionsList();
					WriteCheck wc = (WriteCheck) i.next();
					if (wc.getStatementRecord() == null) {
						customerRefund.setTransactionId(wc.getID());
						customerRefund.setType(wc.getType());
						customerRefund.setDate(new ClientFinanceDate(wc
								.getDate().getDate()));
						customerRefund
								.setCustomerName((wc.getCustomer() != null) ? wc
										.getCustomer().getName() : ((wc
										.getVendor() != null) ? wc.getVendor()
										.getName()
										: (wc.getTaxAgency() != null ? wc
												.getTaxAgency().getName()
												: null)));
						customerRefund.setSpentAmount(wc.getAmount());
						customerRefund.setCurrency(wc.getCurrency().getID());
						customerRefundsList.add(customerRefund);
					}
				}
			}

			// for VendorPaymentList
			query = session.getNamedQuery("getVendorPaymentsList")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate)
					.setParameter("viewType", 1000);
			list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();

				while ((iterator).hasNext()) {

					object = (Object[]) iterator.next();

					String name = (String) object[6];
					if ((Integer) object[14] == null && (Double) object[8] > 0) {
						TransactionsList vendorPaymentsList = new TransactionsList();
						vendorPaymentsList
								.setTransactionId((object[0] == null ? null
										: ((Long) object[0])));
						vendorPaymentsList.setType((Integer) object[1]);
						vendorPaymentsList.setDate(new ClientFinanceDate(
								(Long) object[2]));
						// vendorPaymentsList
						// .setPaymentDate(new ClientFinanceDate(
						// (Long) object[2]));
						// vendorPaymentsList.setIssuedDate(new
						// ClientFinanceDate(
						// (Long) object[5]));
						vendorPaymentsList.setCustomerName(name);
						vendorPaymentsList.setSpentAmount((Double) object[8]);
						vendorPaymentsList.setCurrency((Long) object[12]);
						customerRefundsList.add(vendorPaymentsList);
					}
				}
			}
		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
		return customerRefundsList;

	}

	public ArrayList<InvoicesList> getLatestInvoices(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestInvoices")
					.setParameter("companyId", companyId);
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<InvoicesList> queryResult = new ArrayList<InvoicesList>();
				while ((iterator).hasNext()) {

					InvoicesList invoicesList = new InvoicesList();
					object = (Object[]) iterator.next();

					long transactionNumber = (object[0] == null ? 0
							: ((Long) object[0]));
					invoicesList.setTransactionId(transactionNumber);
					invoicesList.setType((Integer) object[1]);
					invoicesList
							.setDate(new ClientFinanceDate((Long) object[2]));
					invoicesList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					invoicesList.setCustomerName((String) object[4]);
					invoicesList.setNetAmount((Double) object[5]);
					invoicesList.setDueDate(new ClientFinanceDate(
							(Long) object[6]));
					invoicesList.setTotalPrice((Double) object[7]);
					invoicesList.setBalance((Double) object[8]);
					invoicesList.setCurrency((Long) object[9]);
					queryResult.add(invoicesList);
				}
				return new ArrayList<InvoicesList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public PaginationList<ClientMeasurement> getAllUnits(Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.All.Units").setEntity(
				"company", company);
		List<Measurement> measurements = query.list();
		if (measurements == null) {
			return null;
		}
		PaginationList<ClientMeasurement> clientMeasurements = new PaginationList<ClientMeasurement>();
		for (Measurement measurement : measurements) {
			ClientMeasurement clientMeasurement;
			try {
				clientMeasurement = new ClientConvertUtil().toClientObject(
						measurement, ClientMeasurement.class);
				clientMeasurements.add(clientMeasurement);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientMeasurements;
	}

	public PaginationList<ClientWarehouse> getWarehouses(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.Warehouse").setEntity(
				"company", company);
		List<Warehouse> warehouses = query.list();
		if (warehouses == null) {
			return null;
		}
		PaginationList<ClientWarehouse> clientWarehouses = new PaginationList<ClientWarehouse>();
		for (Warehouse warehouse : warehouses) {
			ClientWarehouse clientWarehouse;
			try {
				clientWarehouse = new ClientConvertUtil().toClientObject(
						warehouse, ClientWarehouse.class);
				clientWarehouses.add(clientWarehouse);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientWarehouses;

	}

	public ArrayList<ClientStockTransferItem> getStockTransferItems(
			Long companyId, long wareHouse) {
		Session session = HibernateUtil.getCurrentSession();
		List<Object[]> itemStatuses = session.getNamedQuery("getItemStatuses")
				.setLong("wareHouse", wareHouse).list();

		ArrayList<ClientStockTransferItem> stockTransferItems = new ArrayList<ClientStockTransferItem>();
		for (Object[] record : itemStatuses) {
			ClientStockTransferItem item = new ClientStockTransferItem();
			item.setItem((Long) record[0]);
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue((Double) record[1]);
			quantity.setUnit((Long) record[2]);
			item.setQuantity(new ClientQuantity());
			item.setTotalQuantity(quantity);
			stockTransferItems.add(item);
		}
		return stockTransferItems;
	}

	public ArrayList<ClientStockTransfer> getWarehouseTransfersList(
			Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<StockTransfer> list = session
				.getNamedQuery("getStockTransfersList")
				.setLong("companyId", companyId).list();
		ArrayList<ClientStockTransfer> result = new ArrayList<ClientStockTransfer>();
		for (StockTransfer record : list) {
			ClientStockTransfer clientRecord = new ClientConvertUtil()
					.toClientObject(record, ClientStockTransfer.class);
			result.add(clientRecord);
		}
		return result;
	}

	public ArrayList<StockAdjustmentList> getStockAdjustments(long startDate,
			long endDate, Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<StockAdjustment> list = session
				.getNamedQuery("getStockAdjustmentsList")
				.setLong("companyId", companyId)
				.setLong("startDate", startDate).setLong("endDate", endDate)
				.list();
		ArrayList<StockAdjustmentList> result = new ArrayList<StockAdjustmentList>();
		for (StockAdjustment record : list) {
			for (TransactionItem item : record.getTransactionItems()) {
				StockAdjustmentList s = new StockAdjustmentList();
				s.setWareHouse(record.getWareHouse().getName());
				s.setItem(item.getItem().getName());
				s.setQuantity(new ClientConvertUtil().toClientObject(
						item.getQuantity(), ClientQuantity.class));
				s.setStockAdjustment(record.getID());
				result.add(s);
			}
		}
		return result;
	}

	public ArrayList<ClientItemStatus> getItemStatuses(long wareHouse,
			Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<Object[]> itemStatuses = session.getNamedQuery("getItemStatuses")
				.setLong("wareHouse", wareHouse).list();

		ArrayList<ClientItemStatus> result = new ArrayList<ClientItemStatus>();
		for (Object[] record : itemStatuses) {
			ClientItemStatus itemStatus = new ClientItemStatus();
			itemStatus.setItem((Long) record[0]);
			itemStatus.setWarehouse(wareHouse);

			ClientQuantity qty = new ClientQuantity();
			qty.setValue((Double) record[1]);
			qty.setUnit((Long) record[2]);
			itemStatus.setQuantity(qty);
			result.add(itemStatus);
		}
		return result;
	}

	public Map<Long, Double> getAssetValuesOfAllInventory(Long companyId) {

		List<Item> inventory = ItemUtils.getInventoryItems(companyId);
		Map<Long, Double> result = new HashMap<Long, Double>();
		for (Item item : inventory) {
			double assetValue = ItemUtils.getAssetValueOfItem(item);
			result.put(item.getID(), assetValue);
		}

		return result;
	}

	public ArrayList<InventoryValutionSummary> getInventoryValutionSummary(
			long wareHouseId, Long companyId, ClientFinanceDate start,
			ClientFinanceDate end) {
		Session session = HibernateUtil.getCurrentSession();

		Query totalQuery = session.getNamedQuery("getInventoryTotalAsset")
				.setParameter("wareHouseId", wareHouseId)
				.setParameter("companyId", companyId)
				.setParameter("fromDate", start.getDate())
				.setParameter("toDate", end.getDate());
		Double total = (Double) totalQuery.uniqueResult();

		Query query = session.getNamedQuery("getInventoryValutionSummary")
				.setParameter("wareHouseId", wareHouseId)
				.setParameter("companyId", companyId)
				.setParameter("fromDate", start.getDate())
				.setParameter("toDate", end.getDate());
		List<Object[]> list = query.list();
		Iterator<Object[]> iterator = list.iterator();
		ArrayList<InventoryValutionSummary> result = new ArrayList<InventoryValutionSummary>();
		while (iterator.hasNext()) {
			Object[] next = iterator.next();
			InventoryValutionSummary detail = new InventoryValutionSummary();
			detail.setItemName((String) next[0]);
			detail.setItemDescription((String) next[1]);
			detail.setOnHand(next[2] != null ? (Double) next[2] : 0);
			detail.setUnit(next[3] != null ? (String) next[3] : "");
			detail.setAvgCost(next[4] != null ? (Double) next[4] : 0);
			detail.setSalesPrice(next[5] != null ? (Double) next[5] : 0);
			detail.setItemId(next[6] != null ? (Long) next[6] : 0);
			detail.setAssetValue(next[7] != null ? (Double) next[7] : 0);
			detail.setRetailValue(detail.getOnHand() * detail.getSalesPrice());
			double totalRetail = next[8] != null ? (Double) next[8] : 0;
			detail.setPerOfTotRetail((detail.getRetailValue() * 100)
					/ totalRetail);
			detail.setPerOfTotAsset(detail.getAssetValue() != 0 ? ((detail
					.getAssetValue() * 100) / total) : 0);
			result.add(detail);
		}
		return result;
	}

	public ArrayList<InventoryStockStatusDetail> getInventoryStockStatusByVendor(
			long id, ClientFinanceDate start, ClientFinanceDate end) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getInventoryStockStatusByVendor")
				.setParameter("companyId", id)
				.setParameter("fromDate", start.getDate())
				.setParameter("toDate", end.getDate());
		List<Object[]> list = query.list();
		Iterator<Object[]> iterator = list.iterator();
		ArrayList<InventoryStockStatusDetail> result = new ArrayList<InventoryStockStatusDetail>();
		while (iterator.hasNext()) {
			Object[] next = iterator.next();
			InventoryStockStatusDetail detail = new InventoryStockStatusDetail();
			detail.setItemName((String) next[0]);
			detail.setItemDesc((String) next[1]);
			detail.setPreferVendor((String) next[2]);
			detail.setOnHand(((Long) next[3]));
			detail.setItemId((Long) next[4]);
			detail.setAssemblies(next[5] != null ? (Long) next[5] : 0);
			detail.setOrderOnPo(next[6] != null ? (Long) next[6] : 0);
			detail.setOnSalesOrder(next[7] != null ? (Long) next[7] : 0);
			detail.setReorderPts(next[8] != null ? (Integer) next[8] : 0);
			detail.setUnit((String) next[9]);
			long onHandQt = next[3] != null ? (Long) next[3] : 0;
			long assemblys = next[5] != null ? (Long) next[5] : 0;
			long salesOrder = next[7] != null ? (Long) next[7] : 0;
			detail.setAvilability(onHandQt - salesOrder - assemblys);
			result.add(detail);
		}
		return result;

	}

	public ArrayList<InventoryValutionDetail> getInventoryValutionDetail(
			Long companyId, ClientFinanceDate start, ClientFinanceDate end,
			long itemId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getInventoryValutionDetails")
				.setParameter("companyId", companyId)
				.setParameter("fromDate", start.getDate())
				.setParameter("toDate", end.getDate())
				.setParameter("itemID", itemId);
		List<Object[]> list = query.list();
		Iterator<Object[]> iterator = list.iterator();
		ArrayList<InventoryValutionDetail> result = new ArrayList<InventoryValutionDetail>();
		while (iterator.hasNext()) {
			Object[] next = iterator.next();
			InventoryValutionDetail detail = new InventoryValutionDetail();
			detail.setTransactionId(next[0] != null ? (Long) next[0] : 0);
			detail.setTransactionDate(next[1] != null ? (Long) next[1] : 0);
			detail.setTransType(next[2] != null ? (Integer) next[2] : 0);
			detail.setTransactionNo((String) next[3]);

			detail.setQuantity(next[4] != null ? (Double) next[4] : 0);
			detail.setUnit(next[5] != null ? (String) next[5] : "");
			detail.setCost(next[6] != null ? (Double) next[6] : 0);

			detail.setOnHand(next[7] != null ? (Double) next[7] : 0);
			detail.setOnHandUnit(next[8] != null ? (String) next[8] : "");

			detail.setItemName((String) next[9]);
			detail.setItemId(next[10] != null ? (Long) next[10] : 0);
			detail.setPayeeName((String) next[11]);
			detail.setPayeeId(next[12] != null ? (Long) next[12] : 0);
			detail.setAvgCost(next[13] != null ? (Double) next[13] : 0);
			detail.setEstimateType(next[14] != null ? (Integer) next[14] : 0);
			result.add(detail);
		}
		return result;
	}

	public ArrayList<InventoryStockStatusDetail> getInventoryStockStatusByItem(
			Long companyId, ClientFinanceDate start, ClientFinanceDate end) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getInventoryStockStatusByItem")
				.setParameter("companyId", companyId)
				.setParameter("fromDate", start.getDate())
				.setParameter("toDate", end.getDate());
		List<Object[]> list = query.list();
		Iterator<Object[]> iterator = list.iterator();
		ArrayList<InventoryStockStatusDetail> result = new ArrayList<InventoryStockStatusDetail>();
		while (iterator.hasNext()) {
			Object[] next = iterator.next();
			InventoryStockStatusDetail detail = new InventoryStockStatusDetail();
			detail.setItemName((String) next[0]);
			detail.setItemDesc((String) next[1]);
			detail.setPreferVendor((String) next[2]);
			detail.setOnHand(next[3] != null ? (Long) next[3] : 0);
			detail.setItemId((Long) next[4]);
			detail.setAssemblies(next[5] != null ? (Long) next[5] : 0);
			detail.setOrderOnPo(next[6] != null ? (Long) next[6] : 0);
			detail.setOnSalesOrder(next[7] != null ? (Long) next[7] : 0);
			long onHandQt = next[3] != null ? (Long) next[3] : 0;
			long assemblys = next[5] != null ? (Long) next[5] : 0;
			long salesOrder = next[7] != null ? (Long) next[7] : 0;
			detail.setAvilability(onHandQt - salesOrder - assemblys);

			detail.setReorderPts(next[8] != null ? (Integer) next[8] : 0);
			detail.setUnit((String) next[9]);
			result.add(detail);
		}
		return result;
	}

	public PaginationList<TransactionHistory> getTransactionsByType(
			long itemId, int transactionType, int transactionStatusType,
			long startDate, long endDate, Long companyId, int start, int length) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = null;
		String queryName = null;
		int saveStatus = 0;
		if (transactionType == Transaction.TYPE_INVOICE) {

			if (transactionStatusType == TransactionHistory.ALL_INVOICES) {
				saveStatus = 0;
			}
			if (transactionStatusType == TransactionHistory.OPENED_INVOICES) {
				queryName = "getOpenInvoicesListByItem";

			}
			if (transactionStatusType == TransactionHistory.DRAFT_INVOICES) {
				saveStatus = Transaction.STATUS_DRAFT;
			}
			if (transactionStatusType == TransactionHistory.OVER_DUE_INVOICES) {
				queryName = "getOverdueInvoicesListByItem";
				query = session
						.getNamedQuery(queryName)
						.setParameter("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("currentDate",
								new FinanceDate().getDate())
						.setParameter("itemId", itemId);
			}

		} else if (transactionType == Transaction.TYPE_CASH_SALES) {
			if (transactionStatusType == TransactionHistory.ALL_CASHSALES) {
				saveStatus = 0;
			} else {
				saveStatus = Transaction.STATUS_DRAFT;
			}

		} else if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
			if (transactionStatusType == TransactionHistory.ALL_CREDITMEMOS) {
				saveStatus = 0;
			} else if (transactionStatusType == TransactionHistory.OPEND_CREDITMEMOS) {
				queryName = "getOpendCustomerCreditMemosByItem";
			} else {
				saveStatus = Transaction.STATUS_DRAFT;
			}

		} else if (transactionType == Transaction.TYPE_ESTIMATE) {
			int typeOfEstiate = 1;
			if (transactionStatusType == TransactionHistory.ALL_QUOTES
					|| transactionStatusType == TransactionHistory.ALL_CREDITS
					|| transactionStatusType == TransactionHistory.ALL_CHARGES) {
				saveStatus = 0;
			} else {
				saveStatus = Transaction.STATUS_DRAFT;
			}
			if (transactionStatusType == TransactionHistory.ALL_QUOTES
					|| transactionStatusType == TransactionHistory.DRAFT_QUOTES) {
				typeOfEstiate = 1;

			} else if (transactionStatusType == TransactionHistory.ALL_CREDITS
					|| transactionStatusType == TransactionHistory.DRAFT_CREDITS) {
				typeOfEstiate = 2;

			} else { // charges
				typeOfEstiate = 3;
			}
			query = session.getNamedQuery("getEstimatesForItem")
					.setParameter("companyId", companyId)
					.setParameter("saveStatus", saveStatus)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("itemId", itemId)
					.setParameter("estimateType", typeOfEstiate);

		} else if (transactionType == Transaction.TYPE_SALES_ORDER) {

			int typeOfEstiate = 6;
			if (transactionStatusType == TransactionHistory.ALL_SALES_ORDERS) {
				saveStatus = 0;
			} else {
				saveStatus = Transaction.STATUS_DRAFT;
			}

			query = session.getNamedQuery("getEstimatesForItem")
					.setParameter("companyId", companyId)
					.setParameter("saveStatus", saveStatus)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("itemId", itemId)
					.setParameter("estimateType", typeOfEstiate);

		} else if (transactionType == TransactionHistory.TYPE_PURCHASE_ORDER) {

			if (transactionStatusType == TransactionHistory.ALL_PURCHASE_ORDERS) {
				saveStatus = 0;
			} else {
				saveStatus = Transaction.STATUS_DRAFT;
			}

			query = session.getNamedQuery("getPurchaseOrderForItem")
					.setParameter("companyId", companyId)
					.setParameter("saveStatus", saveStatus)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("itemId", itemId);

		} else if (transactionType == Transaction.TYPE_CASH_PURCHASE) {
			if (transactionStatusType == TransactionHistory.ALL_CASH_PURCHASES) {
				saveStatus = 0;
			} else {
				saveStatus = Transaction.STATUS_DRAFT;
			}

		} else if (transactionType == Transaction.TYPE_ENTER_BILL) {
			if (transactionStatusType == TransactionHistory.ALL_BILLS) {
				saveStatus = 0;
			} else if (transactionStatusType == TransactionHistory.OPEND_BILLS) {
				queryName = "getOpenBillsListByItem";

			} else if (transactionStatusType == TransactionHistory.DRAFT_BILLS) {
				saveStatus = Transaction.STATUS_DRAFT;

			} else if (transactionStatusType == TransactionHistory.OVERDUE_BILLS) {
				queryName = "getOverDueBillsListByItem";
				query = session
						.getNamedQuery(queryName)
						.setParameter("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("itemId", itemId)
						.setParameter("currentDate",
								new FinanceDate().getDate());
			}

		} else if (transactionType == Transaction.TYPE_VENDOR_CREDIT_MEMO) {
			if (transactionStatusType == TransactionHistory.ALL_VENDOR_CREDITNOTES) {
				saveStatus = 0;
			} else {
				saveStatus = Transaction.STATUS_DRAFT;
			}
		} else if (transactionType == Transaction.TYPE_EXPENSE) {
			if (transactionStatusType == TransactionHistory.ALL_EXPENSES) {
				queryName = "getAllExpensesByItem";
			} else if (transactionStatusType == TransactionHistory.CREDIT_CARD_EXPENSES) {
				transactionType = Transaction.TYPE_CREDIT_CARD_EXPENSE;
				saveStatus = 0;
			} else if (transactionStatusType == TransactionHistory.CASH_EXPENSES) {
				transactionType = Transaction.TYPE_CASH_EXPENSE;
				saveStatus = 0;
			} else if (transactionStatusType == TransactionHistory.DRAFT_CREDIT_CARD_EXPENSES) {
				transactionType = Transaction.TYPE_CREDIT_CARD_EXPENSE;
				saveStatus = Transaction.STATUS_DRAFT;
			} else if (transactionStatusType == TransactionHistory.DRAFT_CASH_EXPENSES) {
				transactionType = Transaction.TYPE_CASH_EXPENSE;
				saveStatus = Transaction.STATUS_DRAFT;
			}

		} else {
			transactionType = 0;
		}
		if (query == null) {
			if (queryName != null) {
				query = session.getNamedQuery(queryName)
						.setLong("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("itemId", itemId);
			} else {
				query = session.getNamedQuery("getTransactionForItem")
						.setLong("companyId", companyId)
						.setInteger("transactionType", transactionType)
						.setInteger("saveStatus", saveStatus)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("itemId", itemId);
			}
		}
		List list;
		int total = 0;
		if (length == -1) {
			list = query.list();
		} else {
			total = query.list().size();
			list = query.setFirstResult(start).setMaxResults(length).list();
		}
		PaginationList<TransactionHistory> transactionsList = getItemTransactionsList(
				session, list, itemId);
		transactionsList.setTotalCount(transactionsList.size());
		transactionsList.setStart(start);
		return transactionsList;
	}

	public PaginationList<TransactionHistory> getItemTransactionsList(
			Session session, List l, long itemId) {

		Item item = (Item) session.get(Item.class, itemId);

		Object[] object = null;
		Iterator iterator = l.iterator();
		PaginationList<TransactionHistory> queryResult = new PaginationList<TransactionHistory>();
		double qtyValue = 0;
		double totalAmount = 0;
		long transactionId = 0, previousId = 0;
		TransactionHistory transactionHistory = null;
		while ((iterator).hasNext()) {
			object = (Object[]) iterator.next();

			transactionId = object[0] != null ? (Long) object[0] : 0;

			if (previousId != 0 && previousId != transactionId) {
				queryResult.add(transactionHistory);
				qtyValue = 0;
				totalAmount = 0;
			}

			transactionHistory = new TransactionHistory();

			transactionHistory.setTransactionId(transactionId);
			transactionHistory
					.setDate(object[1] != null ? new ClientFinanceDate(
							(Long) object[1]) : null);

			transactionHistory.setType(object[2] != null ? (Integer) object[2]
					: 0);
			transactionHistory.setNumber(object[3] != null ? (String) object[3]
					: "");

			transactionHistory
					.setIsVoid(object[4] != null ? (Boolean) object[4] : false);
			transactionHistory.setMemo(object[5] != null ? (String) object[5]
					: "");
			double amount = (object[8] == null ? 0 : ((Double) object[8])
					.doubleValue());

			long currecy = (Long) (object[9] == null ? 0 : object[9]);
			transactionHistory.setCurrency(currecy);

			if (transactionHistory.getType() == ClientTransaction.TYPE_ESTIMATE) {
				transactionHistory
						.setEstimateType((Integer) (object[10] == null ? 0
								: object[10]));
			}
			totalAmount += amount;
			transactionHistory.setAmount(totalAmount);

			double value = object[6] != null ? (Double) object[6] : 0;
			long unitId = object[7] != null ? (Long) object[7] : 0;
			Unit unit = (Unit) session.get(Unit.class, unitId);
			Measurement defaultMeasurement = item.getMeasurement();
			Unit defaultUnit = defaultMeasurement.getDefaultUnit();
			double calValue = (value * unit.getFactor())
					/ defaultUnit.getFactor();

			qtyValue += calValue;

			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(qtyValue);
			quantity.setUnit(defaultUnit.getID());

			transactionHistory.setQuantity(quantity);

			previousId = transactionId;
		}
		if (transactionHistory != null) {
			queryResult.add(transactionHistory);
		}

		return queryResult;
	}

	public PaginationList<ClientBuildAssembly> getBuildAssembly(long companyId,
			long startDate, long endDate, int start, int length, int viewId) {
		int total = 0;
		try {
			Session session = HibernateUtil.getCurrentSession();
			List<Object[]> list = null;
			list = session.getNamedQuery("list.buildAssemblies")
					.setParameter("companyId", companyId)
					.setParameter("startdate", startDate)
					.setParameter("enddate", endDate)
					.setParameter("viewId", viewId).list();
			PaginationList<ClientBuildAssembly> paginationList = new PaginationList<ClientBuildAssembly>();
			Iterator i = list.iterator();
			while ((i).hasNext()) {
				Object object = i.next();
				if (object == null) {
					continue;
				}
				long buildAssemblyId = (Long) object;
				BuildAssembly buildAssembly = (BuildAssembly) session.get(
						BuildAssembly.class, buildAssemblyId);
				ClientBuildAssembly clientBuildAssembly;
				try {
					clientBuildAssembly = new ClientConvertUtil()
							.toClientObject(buildAssembly,
									ClientBuildAssembly.class);
					paginationList.add(clientBuildAssembly);
				} catch (AccounterException e) {
					e.printStackTrace();
				}

			}
			total = paginationList.size();
			PaginationList<ClientBuildAssembly> result = new PaginationList<ClientBuildAssembly>();
			if (length < 0) {
				result.addAll(paginationList);
			} else {
				int toIndex = start + length;
				if (toIndex > paginationList.size()) {
					toIndex = paginationList.size();
				}
				result.addAll(paginationList.subList(start, toIndex));
			}
			result.setTotalCount(total);
			result.setStart(start);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new PaginationList<ClientBuildAssembly>();

	}
}
