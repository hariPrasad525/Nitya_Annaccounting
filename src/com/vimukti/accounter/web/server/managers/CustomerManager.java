package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class CustomerManager extends PayeeManager {

	public String getNextCustomerNumber(long companyId) {
		return NumberUtils.getNextAutoCustomerNumber(getCompany(companyId));
		// return NumberUtils.getNextCustomerNumber();
	}

	public PaginationList<CustomerRefundsList> getCustomerRefundsList(
			long companyId, FinanceDate fromDate, FinanceDate toDate, int viewId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			PaginationList<CustomerRefundsList> customerRefundsList = new PaginationList<CustomerRefundsList>();
			Query query = session.getNamedQuery("getCustomerRefund")
					.setEntity("company", company)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate)
					.setParameter("viewId", viewId);
			List list = query.list();

			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					CustomerRefundsList customerRefund = new CustomerRefundsList();
					CustomerRefund cr = (CustomerRefund) i.next();
					customerRefund.setTransactionId(cr.getID());
					customerRefund.setType(cr.getType());
					customerRefund.setPaymentDate(new ClientFinanceDate(cr
							.getDate().getDate()));
					customerRefund.setIssueDate(new ClientFinanceDate(cr
							.getDate().getDate()));
					customerRefund.setPaymentNumber(cr.getNumber());
					customerRefund.setStatus(cr.getStatus());
					customerRefund.setName((cr.getPayTo() != null) ? cr
							.getPayTo().getName() : null);
					customerRefund
							.setPaymentMethod((cr.getPaymentMethod() != null) ? cr
									.getPaymentMethod() : null);
					customerRefund.setAmountPaid(cr.getTotal());
					customerRefund.setVoided(cr.isVoid());
					customerRefund.setCurrency(cr.getCurrency().getID());
					customerRefund.setSaveStatus(cr.getSaveStatus());
					customerRefundsList.add(customerRefund);
				}
			}
			// query = session.getNamedQuery("getWriteCheck.by.payToType")
			// .setParameter("type", WriteCheck.TYPE_CUSTOMER)
			// .setEntity("company", company);
			// list = query.list();
			//
			// if (list != null) {
			// Iterator i = list.iterator();
			// while (i.hasNext()) {
			//
			// CustomerRefundsList customerRefund = new CustomerRefundsList();
			// WriteCheck wc = (WriteCheck) i.next();
			// customerRefund.setTransactionId(wc.getID());
			// customerRefund.setType(wc.getType());
			// customerRefund.setPaymentDate(new ClientFinanceDate(wc
			// .getDate().getDate()));
			// customerRefund.setIssueDate(null);
			// customerRefund.setPaymentNumber(wc.getNumber());
			// customerRefund.setStatus(wc.getStatus());
			// customerRefund.setName((wc.getCustomer() != null) ? wc
			// .getCustomer().getName()
			// : ((wc.getVendor() != null) ? wc.getVendor()
			// .getName()
			// : (wc.getTaxAgency() != null ? wc
			// .getTaxAgency().getName() : null)));
			// customerRefund.setPaymentMethod(null);
			// customerRefund.setAmountPaid(wc.getAmount());
			// customerRefund.setVoided(wc.isVoid());
			// customerRefund.setCurrency(wc.getCurrency().getID());
			// customerRefund.setSaveStatus(wc.getSaveStatus());
			// customerRefundsList.add(customerRefund);
			//
			// }
			// }

			if (customerRefundsList != null) {
				return customerRefundsList;
			} else
				throw new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null);
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<Estimate> getLatestQuotes(long companyId)
			throws DAOException {
		// SELECT E1.* FROM ESTIMATE E1 WHERE 10>(SELECT COUNT(*) FROM
		// TRANSACTION E2 WHERE E1.ID<E2.ID)

		try {
			Session session = HibernateUtil.getCurrentSession();
			// FIXME::: query optimization
			Query query = session.getNamedQuery("getLatestQuotes")
					.setParameter("companyId", companyId);
			List list2 = query.list();
			Object object[] = null;

			Iterator iterator = list2.iterator();
			List<Estimate> list = new ArrayList<Estimate>();
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				Estimate estimate = new Estimate();
				// TODO :: change the query
				// estimate.setID((object[0] == null ? null : ((Long)
				// object[0])));
				estimate.setDate(new FinanceDate((Long) (object[1])));
				estimate.setCustomer(object[2] != null ? (Customer) session
						.get(Customer.class, ((Long) object[2])) : null);
				estimate.setSalesPerson(object[3] != null ? (SalesPerson) session
						.get(SalesPerson.class, ((Long) object[3])) : null);
				estimate.setTotal((Double) object[4]);
				list.add(estimate);
			}
			if (list != null) {
				return new ArrayList<Estimate>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<CustomerRefund> getLatestCustomerRefunds(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestCustomerRefunds")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<CustomerRefund> list = new ArrayList<CustomerRefund>();
			while (iterator.hasNext()) {

				object = (Object[]) iterator.next();
				CustomerRefund customerRefund = new CustomerRefund();
				// customerRefund.setID((object[0] == null ? null
				// : ((Long) object[0])));
				customerRefund.setDate(new FinanceDate((Long) object[1]));
				customerRefund.setPayTo(object[2] != null ? (Customer) session
						.get(Customer.class, ((Long) object[2])) : null);
				customerRefund.setTotal((Double) object[3]);
				customerRefund.setBalanceDue((Double) object[3]);

				list.add(customerRefund);
			}
			if (list != null) {
				return new ArrayList<CustomerRefund>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			long customerId, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getEstimatesAndSalesOrdersList")
				.setParameter("customerId", customerId)
				.setParameter("companyId", companyId);
		CompanyPreferences preferences = getCompany(companyId).getPreferences();
		List list = query.list();
		List<EstimatesAndSalesOrdersList> esl = new ArrayList<EstimatesAndSalesOrdersList>();

		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			int status = ((Integer) obj[8]).intValue();
			int estimateType = ((Integer) obj[7]).intValue();
			if (estimateType == Estimate.QUOTES) {
				if (preferences.isDontIncludeEstimates()) {
					continue;
				} else if (preferences.isIncludeAcceptedEstimates()
						&& status != Estimate.STATUS_ACCECPTED) {
					continue;
				}
			} else if ((estimateType == Estimate.CHARGES || estimateType == Estimate.CREDITS)
					&& !preferences.isDelayedchargesEnabled()) {
				continue;
			} else if (estimateType == Estimate.SALES_ORDER
					&& !preferences.isSalesOrderEnabled()) {
				continue;
			} else if ((estimateType == ClientEstimate.BILLABLEEXAPENSES || estimateType == ClientEstimate.DEPOSIT_EXAPENSES)
					&& !(preferences
							.isBillableExpsesEnbldForProductandServices() && preferences
							.isProductandSerivesTrackingByCustomerEnabled())) {
				continue;
			}

			// for (int j = 0; j < obj.length; j++)
			EstimatesAndSalesOrdersList el = new EstimatesAndSalesOrdersList();
			el.setTransactionId(((Long) obj[0]).longValue());
			el.setType(((Integer) obj[1]).intValue());
			el.setTransactionNumber(((String) obj[2]));
			el.setTotal(((Double) obj[3]).doubleValue());
			el.setDate(new ClientFinanceDate((Long) obj[4]));
			el.setCustomerName((String) obj[5]);
			el.setRemainingTotal(obj[6] == null ? 0.0 : ((Double) obj[6])
					.doubleValue());
			if (obj[7] != null) {
				el.setEstimateType(((Integer) obj[7]).intValue());
			}
			el.setStatus(((Integer) obj[8]).intValue());
			esl.add(el);
		}

		return new ArrayList<EstimatesAndSalesOrdersList>(esl);
	}

	public ArrayList<Estimate> getEstimates(long companyId, int estimateType,
			int viewType, FinanceDate fromDate, FinanceDate toDate, int start,
			int length) throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();
			List<Estimate> list;
			Company company = getCompany(companyId);
			Query query;
			if (viewType == 6) {
				query = session.getNamedQuery("getExpiredEstimate")
						.setEntity("company", company)
						.setParameter("estimateType", estimateType)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("today", new FinanceDate());
			} else {
				query = session.getNamedQuery("getEstimate")
						.setEntity("company", company)
						.setParameter("estimateType", estimateType)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("status", viewType);
			}
			if (length == -1) {
				list = query.list();
			} else {
				list = query.setFirstResult(start).setMaxResults(length).list();
			}
			if (list != null) {
				return new ArrayList<Estimate>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<ReceivePayment> getLatestReceivePayments(long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getLatestReceivePayments")
				.setParameter("companyId", companyId);
		List list2 = query.list();

		Object object[] = null;
		Iterator iterator = list2.iterator();
		List<ReceivePayment> list = new ArrayList<ReceivePayment>();
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			ReceivePayment receivePayment = new ReceivePayment();
			// receivePayment
			// .setID((object[0] == null ? null : ((Long) object[0])));
			receivePayment.setDate(new FinanceDate((Long) object[1]));
			receivePayment.setCustomer(object[2] != null ? (Customer) session
					.get(Customer.class, ((Long) object[2])) : null);
			receivePayment.setAmount((Double) object[3]);

			list.add(receivePayment);
		}
		if (list != null) {
			return new ArrayList<ReceivePayment>(list);
		} else
			throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
					null));
	}

	public PaginationList<ReceivePaymentsList> getReceivePaymentsList(
			long companyId, long fromDate, long toDate, int transactionType,
			int start, int length, int viewType) throws DAOException {
		int total = 0;
		List list;
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getAllCustomersPaymentsList")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate)
					.setParameter("viewType", viewType);
			if (transactionType == Transaction.TYPE_RECEIVE_PAYMENT) {
				query = session.getNamedQuery("getCustomerReceivePaymentsList")
						.setParameter("companyId", companyId)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("viewType", viewType);
			} else if (transactionType == Transaction.TYPE_CUSTOMER_PRE_PAYMENT) {
				query = session.getNamedQuery("getCustomerPrepaymentsList")
						.setParameter("companyId", companyId)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
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
				PaginationList<ReceivePaymentsList> queryResult = new PaginationList<ReceivePaymentsList>();
				while ((iterator).hasNext()) {

					ReceivePaymentsList receivePaymentsList = new ReceivePaymentsList();
					object = (Object[]) iterator.next();

					receivePaymentsList
							.setTransactionId((object[0] == null ? null
									: ((Long) object[0])));
					receivePaymentsList.setType((Integer) object[1]);
					receivePaymentsList.setPaymentDate(new ClientFinanceDate(
							object[2] == null ? null : ((Long) object[2])));
					receivePaymentsList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					receivePaymentsList.setCustomerName((String) object[4]);
					receivePaymentsList
							.setPaymentMethodName((String) object[5]);
					receivePaymentsList.setAmountPaid((Double) object[6]);
					receivePaymentsList.setVoided((Boolean) object[7]);
					receivePaymentsList.setStatus((Integer) object[8]);
					receivePaymentsList.setCheckNumber((String) object[9]);
					receivePaymentsList.setCurrency((Long) object[10]);
					// receivePaymentsList.setStatus((Integer) object[11]);

					queryResult.add(receivePaymentsList);
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

	public ArrayList<PaymentsList> getLatestPayments(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestPayments")
					.setParameter("companyId", companyId);
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<PaymentsList> queryResult = new ArrayList<PaymentsList>();
				while ((iterator).hasNext()) {

					PaymentsList paymentsList = new PaymentsList();
					object = (Object[]) iterator.next();

					paymentsList.setTransactionId((object[0] == null ? null
							: ((Long) object[0])));
					paymentsList.setType((Integer) object[1]);
					paymentsList.setPaymentDate(new ClientFinanceDate(
							(Long) object[2]));
					paymentsList.setPaymentNumber((object[3] == null ? null
							: ((String) object[3])));
					paymentsList.setStatus((Integer) object[4]);
					paymentsList.setIssuedDate(new ClientFinanceDate(
							(Long) object[5]));
					paymentsList.setName((String) object[6]);
					paymentsList.setPaymentMethodName((String) object[7]);
					paymentsList.setAmountPaid((Double) object[8]);
					paymentsList.setCurrency((Long) object[9]);
					queryResult.add(paymentsList);
				}
				return new ArrayList<PaymentsList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public PaginationList<PaymentsList> getPaymentsList(long companyId,
			long fromDate, long toDate, int start, int length, int viewType)
			throws DAOException {
		PaginationList<PaymentsList> queryResult = new PaginationList<PaymentsList>();
		int total = 0;
		List list;
		try {
			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getPaymentsList")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate)
					.setParameter("viewType", viewType);
			// FIXME ::: check the sql query and change it to hql query if
			// required
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

				while ((iterator).hasNext()) {

					PaymentsList paymentsList = new PaymentsList();
					object = (Object[]) iterator.next();

					String name = (String) object[6];
					// if (name != null) {
					paymentsList.setTransactionId((object[0] == null ? 0
							: ((Long) object[0])));
					paymentsList.setType((Integer) object[1]);
					int tr_t = paymentsList.getType();
					if (tr_t == 17 || tr_t == 30 || tr_t == 31) {
						name = Global.get().messages().taxAgencyPayment();
					}
					paymentsList.setPaymentDate((object[2] == null ? null
							: (new ClientFinanceDate(((Long) object[2])))));
					paymentsList.setPaymentNumber((object[3] == null ? null
							: ((String) object[3])));
					paymentsList.setStatus((Integer) object[4]);
					paymentsList.setIssuedDate(new ClientFinanceDate(
							(Long) object[5]));
					paymentsList.setName(name);
					paymentsList.setPaymentMethodName((String) object[7]);
					paymentsList.setAmountPaid((Double) object[8]);
					paymentsList.setVoided((Boolean) object[9]);
					paymentsList
							.setCheckNumber((String) object[10] == null ? ""
									: (String) object[10]);
					paymentsList.setCurrency((Long) object[11]);
					paymentsList.setSaveStatus((Integer) object[12]);
					queryResult.add(paymentsList);
					// }
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

	public ArrayList<Estimate> getEstimates(long customer, long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session
					.getNamedQuery("getEstimate.by.check.id.status")
					.setParameter("id", customer).setEntity("company", company);
			List<Estimate> list = query.list();

			if (list != null) {
				return new ArrayList<Estimate>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public void mergeCustomer(ClientCustomer fromClientCustomer,
			ClientCustomer toClientCustomer, long companyId)
			throws DAOException, AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		double mergeBalance = fromClientCustomer.getBalance()
				+ toClientCustomer.getBalance();
		Company company = getCompany(companyId);
		// Updating

		Customer from = (Customer) session.get(Customer.class,
				fromClientCustomer.getID());
		Customer to = (Customer) session.get(Customer.class,
				toClientCustomer.getID());

		if (from.getCompany().getId() != companyId
				|| to.getCompany().getId() != companyId) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT,
					"Illegal Access for the Object");
		}

		try {
			session.getNamedQuery(
					"update.merge.Payee.mergeoldbalance.tonewbalance")
					.setLong("id", toClientCustomer.getID())
					.setDouble("balance", mergeBalance)
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.invoice.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.CreditsAndPayments")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID()).executeUpdate();

			session.getNamedQuery("update.merge.CustomFieldValue")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID()).executeUpdate();

			session.getNamedQuery("update.merge.cashsale.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.customercreditmemo.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.salesOrder.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.CustomerPrePayment.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.CustomerRefund.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.ReceivePayment.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.Estimate.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.writeCheck.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.JournalEntry.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.Job.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID()).executeUpdate();

			session.getNamedQuery("update.merge.TransactionItem.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID()).executeUpdate();

			session.getNamedQuery("update.merge.payee.update.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID()).executeUpdate();

			session.getNamedQuery(
					"update.merge.TransactionDepositItem.update.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID()).executeUpdate();

			session.getNamedQuery(
					"update.merge.TransactionDepositItem.customer.update.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID()).executeUpdate();

			// session.getNamedQuery("update.merge.loan.old.tonew")
			// .setLong("fromID", fromClientCustomer.getID())
			// .setLong("toID", toClientCustomer.getID()).executeUpdate();

			User user = AccounterThreadLocal.get();

			Customer toCustomer = (Customer) session.get(Customer.class,
					toClientCustomer.getID());

			Customer fromcustomer = (Customer) session.get(Customer.class,
					fromClientCustomer.getID());

			Activity activity = new Activity(company, user, ActivityType.MERGE,
					toCustomer);
			session.save(activity);

			company.getCustomers().remove(fromcustomer);
			session.saveOrUpdate(company);
			// fromcustomer.setCompany(null);

			session.delete(fromcustomer);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}

	}

	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("getMostProfitableCustomers")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<MostProfitableCustomers> queryResult = new ArrayList<MostProfitableCustomers>();
		while ((iterator).hasNext()) {

			MostProfitableCustomers mostProfitableCustomers = new MostProfitableCustomers();
			object = (Object[]) iterator.next();

			mostProfitableCustomers.setCustomer((String) object[0]);
			mostProfitableCustomers.setInvoicedAmount(object[1] == null ? 0
					: ((Double) object[1]).doubleValue());
			mostProfitableCustomers.setStandardCost(object[2] == null ? 0
					: ((Double) object[2]).doubleValue());
			mostProfitableCustomers.setMargin(object[3] == null ? 0
					: ((Double) object[3]).doubleValue());
			mostProfitableCustomers.setCustomerGroup((String) object[4]);
			mostProfitableCustomers.setFileAs((String) object[5]);
			if (mostProfitableCustomers.getInvoicedAmount() == 0.0)
				mostProfitableCustomers.setMarginPercentage(0.0);
			else {
				mostProfitableCustomers
						.setMarginPercentage(mostProfitableCustomers
								.getMargin()
								/ mostProfitableCustomers.getInvoicedAmount()
								* 100);
			}
			mostProfitableCustomers.setCost(mostProfitableCustomers
					.getStandardCost()
					+ mostProfitableCustomers.getBilledCost());

			queryResult.add(mostProfitableCustomers);
		}

		query = session.getNamedQuery("getEntry.from.journalEntryType")
				.setParameter("company", company);

		List<JournalEntry> nonInvoicedLines = query.list();

		for (MostProfitableCustomers mpc : queryResult) {
			for (JournalEntry je : nonInvoicedLines) {
				for (TransactionItem item : je.getTransactionItems()) {
					if (je.getInvolvedPayee() != null
							&& je.getInvolvedPayee() instanceof Customer
							&& je.getInvolvedPayee().getName()
									.equals(mpc.getCustomer())
							&& item.getAccount().getType() == Account.TYPE_ACCOUNT_RECEIVABLE) {
						mpc.setBilledCost(mpc.getBilledCost()
								+ item.getLineTotal());
					}
				}
			}

		}

		return new ArrayList<MostProfitableCustomers>(queryResult);
	}

	public ArrayList<PayeeStatementsList> getCustomerStatement(long customer,
			long fromDate, long toDate, long companyId) {
		Session session = HibernateUtil.getCurrentSession();

		List<PayeeStatementsList> result = new ArrayList<PayeeStatementsList>();

		Query query = session.getNamedQuery("getCustomerPreviousBalance");
		query.setParameter("customerId", customer);
		query.setParameter("companyId", companyId);
		query.setParameter("fromDate", fromDate);
		Object uniqueResult = query.uniqueResult();
		PayeeStatementsList ob = new PayeeStatementsList();
		ob.setTransactionDate(new ClientFinanceDate(fromDate));
		ob.setTotal((Double) uniqueResult);
		result.add(ob);

		Query query1 = session.getNamedQuery("getCustomerStatement");
		query1.setParameter("customerId", customer);
		query1.setParameter("companyId", companyId);
		query1.setParameter("fromDate", fromDate);
		query1.setParameter("toDate", toDate);

		List l = query1.list();
		Iterator it = l.iterator();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			PayeeStatementsList record = new PayeeStatementsList();
			record.setTransactionId((Long) object[0]);
			record.setTransactionDate(new ClientFinanceDate((Long) object[1]));
			record.setTransactiontype((Integer) object[2]);
			record.setTransactionNumber((String) object[3]);
			record.setTotal((Double) object[4]);

			result.add(record);
		}
		return new ArrayList<PayeeStatementsList>(result);
	}

	public PaginationList<ClientJob> getJobsList(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("getJobsList").setParameter(
				"company", company);
		PaginationList<ClientJob> clientJobs = new PaginationList<ClientJob>();
		List<Job> list = query.list();
		for (Job job : list) {
			try {
				clientJobs.add(new ClientConvertUtil().toClientObject(job,
						ClientJob.class));
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}

		return clientJobs;
	}

	public ArrayList<Customer> getTransactionHistoryCustomers(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTransactionHistoryCustomers")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<Customer> queryResult = new ArrayList<Customer>();
		while ((iterator).hasNext()) {

			Customer customer = new Customer();
			object = (Object[]) iterator.next();
			customer.setName((String) object[1]);
			queryResult.add(customer);
		}
		return new ArrayList<Customer>(queryResult);
	}

	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		ClientFinanceDate date[] = this
				.getMinimumAndMaximumTransactionDate(companyId);
		long start = date[0] != null ? date[0].getDate() : startDate.getDate();
		// Calendar cal = Calendar.getInstance();
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// try {
		// cal.setTime(startDate.getAsDateObject());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// cal.add(Calendar.DAY_OF_MONTH, -1);
		//
		// String end = cal.get(Calendar.YEAR) + "-";
		// end += ((((cal.get(Calendar.MONTH) + 1) + "").length() == 1) ? "0"
		// + cal.get(Calendar.MONTH) : cal.get(Calendar.MONTH) + 1)
		// + "-";
		// end += (((cal.get(Calendar.DAY_OF_MONTH)) + "").length() == 1) ? "0"
		// + cal.get(Calendar.DAY_OF_MONTH) : cal
		// .get(Calendar.DAY_OF_MONTH);

		long end = date[1] != null ? date[1].getDate() : endDate.getDate();

		Query query = session.getNamedQuery("getCustomerTransactionHistory")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("start", start).setParameter("end", end);

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TransactionHistory> queryResult = new ArrayList<TransactionHistory>();
		Set<String> payee = new HashSet<String>();
		Map<String, TransactionHistory> openingBalnaceEntries = new HashMap<String, TransactionHistory>();
		while ((iterator).hasNext()) {

			TransactionHistory transactionHistory = new TransactionHistory();
			object = (Object[]) iterator.next();

			transactionHistory.setName((String) object[0]);
			transactionHistory
					.setType((object[1] == null || ((String) object[16] != null ? (String) object[16]
							: "")
							.equals(AccounterServerConstants.MEMO_OPENING_BALANCE)) ? 0
							: ((Integer) object[1]).intValue());
			transactionHistory.setNumber((String) object[2]);

			transactionHistory.setDate(new ClientFinanceDate(((Long) object[3])
					.longValue()));
			transactionHistory.setInvoicedAmount(object[4] == null ? 0
					: ((Double) object[4]).doubleValue());
			transactionHistory.setPaidAmount(object[5] == null ? 0
					: ((Double) object[5]).doubleValue());
			transactionHistory.setPaymentTerm((String) object[6]);
			transactionHistory.setDueDate(((Long) object[7]) == null ? null
					: new ClientFinanceDate(((Long) object[7]).longValue()));
			transactionHistory.setDebit(object[8] == null ? 0
					: ((Double) object[8]).doubleValue());
			transactionHistory.setCredit(object[9] == null ? 0
					: ((Double) object[9]).doubleValue());
			transactionHistory.setDiscount(object[10] == null ? 0
					: ((Double) object[10]).doubleValue());
			transactionHistory.setWriteOff(object[11] == null ? 0
					: ((Double) object[11]).doubleValue());
			transactionHistory
					.setTransactionId(((Long) object[12]).longValue());

			// transactionHistory
			// .setBeginningBalance((object[13] != null ? ((Double) object[13])
			// .doubleValue() : 0.0));
			transactionHistory
					.setIsVoid(object[13] != null ? (Boolean) object[13]
							: false);
			transactionHistory
					.setStatus((object[14] != null) ? (Integer) object[14] : 0);
			transactionHistory.setMemo((String) object[15]);

			// Transaction t = (Transaction) getServerObjectForid(
			// AccounterCoreType.TRANSACTION,
			// transactionHistory.getTransactionId());
			// Account account = (t).getEffectingAccount() == null ?
			// t.getPayee() == null ? null
			// : t.getPayee().getAccount()
			// : t.getEffectingAccount();

			transactionHistory.setAccount((String) object[16]);

			if (transactionHistory.getType() == 0) {
				openingBalnaceEntries.put(transactionHistory.getName(),
						transactionHistory);
			} else {

				queryResult.add(transactionHistory);
			}
			payee.add(transactionHistory.getName());
		}

		mergeOpeningBalanceEntries(queryResult, payee, openingBalnaceEntries);

		// return prepareEntriesForVoid(queryResult);
		return new ArrayList<TransactionHistory>(queryResult);
	}

	public PaginationList<TransactionHistory> getCustomerTransactionsList(List l) {

		Object[] object = null;
		Iterator iterator = l.iterator();
		PaginationList<TransactionHistory> queryResult = new PaginationList<TransactionHistory>();
		Set<String> payee = new HashSet<String>();
		Map<String, TransactionHistory> openingBalnaceEntries = new HashMap<String, TransactionHistory>();
		while ((iterator).hasNext()) {

			TransactionHistory transactionHistory = new TransactionHistory();
			object = (Object[]) iterator.next();
			transactionHistory.setTransactionId((Long) object[0]);
			int ttype = (Integer) object[2];
			if (ttype == ClientTransaction.TYPE_ESTIMATE) {
				Session currentSession = HibernateUtil.getCurrentSession();
				Estimate estimate = (Estimate) currentSession.get(
						Estimate.class, (Long) object[0]);
				if (estimate.getEstimateType() == Estimate.BILLABLEEXAPENSES) {
					continue;
				}
				if (estimate.getEstimateType() == Estimate.CHARGES) {
					transactionHistory
							.setName(Global.get().messages().charge());
				} else if (estimate.getEstimateType() == Estimate.QUOTES) {
					transactionHistory.setName(Global.get().messages().quote());
				} else if (estimate.getEstimateType() == Estimate.SALES_ORDER) {
					transactionHistory.setName(Global.get().messages()
							.salesOrder());
				} else {
					transactionHistory
							.setName(Global.get().messages().credit());
				}
			} else {
				transactionHistory.setName(Utility
						.getTransactionName((Integer) object[2]));
			}

			transactionHistory.setType((Integer) object[2]);
			// transactionHistory
			// .setType((object[2] == null || ((String) object[16] != null ?
			// (String) object[16]
			// : "")
			// .equals(AccounterServerConstants.MEMO_OPENING_BALANCE)) ? 0
			// : ((Integer) object[1]).intValue());
			transactionHistory.setNumber((String) object[3]);

			transactionHistory.setDate(new ClientFinanceDate((Long) object[1]));

			// transactionHistory.setInvoicedAmount(object[4] == null ? 0
			// : ((Double) object[4]).doubleValue());
			// transactionHistory.setPaidAmount(object[5] == null ? 0
			// : ((Double) object[5]).doubleValue());
			// transactionHistory.setPaymentTerm((String) object[6]);
			transactionHistory.setDueDate(((Long) object[4]) == null ? null
					: new ClientFinanceDate((Long) object[4]));
			// transactionHistory.setDebit(object[8] == null ? 0
			// : ((Double) object[8]).doubleValue());
			// transactionHistory.setCredit(object[9] == null ? 0
			// : ((Double) object[9]).doubleValue());
			// transactionHistory.setDiscount(object[10] == null ? 0
			// : ((Double) object[10]).doubleValue());
			// transactionHistory.setWriteOff(object[11] == null ? 0
			// : ((Double) object[11]).doubleValue());
			// transactionHistory.setTransactionId(((BigInteger) object[12])
			// .longValue());
			//
			// transactionHistory
			// .setBeginningBalance((object[13] != null ? ((Double) object[13])
			// .doubleValue() : 0.0));
			transactionHistory
					.setIsVoid(object[6] != null ? (Boolean) object[6] : false);
			// transactionHistory
			// .setStatus((object[15] != null) ? (Integer) object[15] : 0);
			transactionHistory.setMemo((String) object[7]);
			transactionHistory.setStatus((Integer) object[8]);

			// transactionHistory.setAccount((String) object[17]);
			transactionHistory.setAmount((object[5] == null ? 0
					: ((Double) object[5]).doubleValue()));

			if (transactionHistory.getType() == 0) {
				openingBalnaceEntries.put(transactionHistory.getName(),
						transactionHistory);
			} else {

				queryResult.add(transactionHistory);
			}
			payee.add(transactionHistory.getName());
		}

		mergeOpeningBalanceEntries(queryResult, payee, openingBalnaceEntries);

		// return prepareEntriesForVoid(queryResult);
		return queryResult;
	}

	public PaginationList<TransactionHistory> getResultListbyType(
			long customerId, int transactionType, int transactionStatusType,
			long startDate, long endDate, Long companyId, int start, int length) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = null;
		int total = 0;
		String queryName = null;
		if (transactionType == Transaction.TYPE_INVOICE) {

			if (transactionStatusType == TransactionHistory.ALL_INVOICES) {
				queryName = "getInvoicesListByCustomer";

			}
			if (transactionStatusType == TransactionHistory.OPENED_INVOICES) {
				queryName = "getOpenInvoicesListByCustomer";

			}
			if (transactionStatusType == TransactionHistory.DRAFT_INVOICES) {
				queryName = "getDraftInvoicesListByCustomer";

			}
			if (transactionStatusType == TransactionHistory.OVER_DUE_INVOICES) {
				queryName = "getOverdueInvoicesListByCustomer";
				query = session
						.getNamedQuery(queryName)
						.setParameter("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("currentDate",
								new FinanceDate().getDate())
						.setParameter("customerId", customerId);
			}

		} else if (transactionType == Transaction.TYPE_CASH_SALES) {
			if (transactionStatusType == TransactionHistory.ALL_CASHSALES) {
				queryName = "getCashSalesByCustomer";
			} else {
				queryName = "getDraftCashSalesByCustomer";
			}

		} else if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
			if (transactionStatusType == TransactionHistory.ALL_CREDITMEMOS) {
				queryName = "getAllCustomerCreditMemosByCustomer";
			} else if (transactionStatusType == TransactionHistory.OPEND_CREDITMEMOS) {
				queryName = "getOpendCustomerCreditMemosByCustomer";
			} else {
				queryName = "getDraftCustomerCreditMemosByCustomer";
			}

		} else if (transactionType == Transaction.TYPE_RECEIVE_PAYMENT) {
			String typeOfRPString = null;
			if (transactionStatusType == TransactionHistory.ALL_RECEIVEDPAYMENTS) {
				queryName = "getAllReceivePaymentsByCustomer";

			} else {

				if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_CASH) {
					typeOfRPString = "Cash";

				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_CHEQUE) {
					typeOfRPString = "Cheque";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_CREDITCARD) {
					typeOfRPString = "Credit Card";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_STANDING_ORDER) {
					typeOfRPString = "Standing Order";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_MAESTRO) {
					typeOfRPString = "Switch/Maestro";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_ONLINE) {
					typeOfRPString = "Online Banking";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_MASTERCARD) {
					typeOfRPString = "Master card";
				} else {
					// (transactionStatusType ==
					// TransactionHistory.RECEV_PAY_BY_DIRECT_DEBIT)
					typeOfRPString = "Direct Debit";
				}
				query = session
						.getNamedQuery("getReceivePaymentsbyTypeByCustomer")
						.setParameter("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("customerId", customerId)
						.setParameter("paymentmethod", typeOfRPString,
								EncryptedStringType.INSTANCE);

			}
		} else if (transactionType == Transaction.TYPE_CUSTOMER_REFUNDS) {
			if (transactionStatusType == TransactionHistory.ALL_CUSTOMER_REFUNDS) {
				queryName = "getAllCustomerRefundsByCustomer";

			} else if (transactionStatusType == TransactionHistory.DRAFT_CUSTOMER_REFUNDS) {
				queryName = "getDraftCustomerRefundsByCustomer";
			} else {
				String typeOfRPString = null;
				if (transactionStatusType == TransactionHistory.REFUNDS_BYCASH) {
					typeOfRPString = "Cash";
				} else if (transactionStatusType == TransactionHistory.REFUNDS_BYCHEQUE) {
					typeOfRPString = "Cheque";
				} else {
					// if (transactionStatusType ==
					// TransactionHistory.REFUNDS_BY_CREDITCARD)
					typeOfRPString = "Credit Card";
				}
				query = session
						.getNamedQuery("getCustomerRefundsByTypeByCustomer")
						.setParameter("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("customerId", customerId)
						.setParameter("paymentmethod", typeOfRPString,
								EncryptedStringType.INSTANCE);
			}
		} else if (transactionType == Transaction.TYPE_ESTIMATE) {
			int typeOfEstiate = 1;
			if (transactionStatusType == TransactionHistory.ALL_QUOTES
					|| transactionStatusType == TransactionHistory.ALL_CREDITS
					|| transactionStatusType == TransactionHistory.ALL_CHARGES) {
				queryName = "getAllQuotesByCustomer";
			} else {
				queryName = "getDraftQuotesByCustomer";
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
			query = session.getNamedQuery(queryName)
					.setParameter("companyId", companyId)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("customerId", customerId)
					.setParameter("estimateType", typeOfEstiate);

		} else if (transactionType == Transaction.TYPE_SALES_ORDER) {
			int typeOfEstiate = 1;
			if (transactionStatusType == TransactionHistory.COMPLETED_SALES_ORDERS) {
				typeOfEstiate = ClientTransaction.STATUS_COMPLETED;
			}
			if (transactionStatusType == TransactionHistory.OPEN_SALES_ORDERS) {

				typeOfEstiate = 0;

			}
			queryName = "getAllSalesOrdersByCustomer";
			query = session.getNamedQuery(queryName)
					.setParameter("companyId", companyId)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("customerId", customerId)
					.setParameter("estimateType", 6)
					.setParameter("status", typeOfEstiate);

		} else if (transactionType == Transaction.TYPE_WRITE_CHECK) {

			if (transactionStatusType == TransactionHistory.ALL_CHEQUES) {
				queryName = "getAllChequesListByCustomer";
			} else {
				queryName = "getDraftChequesListByCustomer";
			}
		} else {
			queryName = "getAllTransactionsByCustomer";

		}
		if (query == null) {
			query = session.getNamedQuery(queryName)
					.setParameter("companyId", companyId)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("customerId", customerId);
		}
		List list;
		if (length == -1) {
			list = query.list();
		} else {
			total = query.list().size();
			list = query.setFirstResult(start).setMaxResults(length).list();
		}
		PaginationList<TransactionHistory> customerTransactionsList = getCustomerTransactionsList(list);
		customerTransactionsList.setTotalCount(total);
		customerTransactionsList.setStart(start);
		return customerTransactionsList;
	}

	public ArrayList<EstimatesAndSalesOrdersList> getSalesOrdersList(
			long customerId, Long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getSalesOrdersList")
				.setParameter("customerId", customerId)
				.setParameter("companyId", companyId);
		CompanyPreferences preferences = getCompany(companyId).getPreferences();
		List list = query.list();
		List<EstimatesAndSalesOrdersList> esl = new ArrayList<EstimatesAndSalesOrdersList>();

		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			int estimateType = ((Integer) obj[7]).intValue();
			if (estimateType == Estimate.SALES_ORDER
					&& !preferences.isSalesOrderEnabled()) {
				continue;
			}

			// for (int j = 0; j < obj.length; j++)
			EstimatesAndSalesOrdersList el = new EstimatesAndSalesOrdersList();
			el.setTransactionId(((Long) obj[0]).longValue());
			el.setType(((Integer) obj[1]).intValue());
			el.setTransactionNumber(((String) obj[2]));
			el.setTotal(((Double) obj[3]).doubleValue());
			el.setDate(new ClientFinanceDate((Long) obj[4]));
			el.setCustomerName((String) obj[5]);
			el.setRemainingTotal(obj[6] == null ? 0.0 : ((Double) obj[6])
					.doubleValue());
			if (obj[7] != null) {
				el.setEstimateType(((Integer) obj[7]).intValue());
			}
			el.setStatus(((Integer) obj[8]).intValue());
			esl.add(el);
		}

		return new ArrayList<EstimatesAndSalesOrdersList>(esl);
	}

	/**
	 * Get Customer Obj By Id
	 * 
	 * @param company
	 * @param customerID
	 * @return
	 */
	public Customer getCustomerByID(long customerID) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Customer customer = (Customer) currentSession.get(Customer.class,
				customerID);
		return customer;
	}


	public PaginationList<TransactionHistory> getConsultantResultListbyType(
			long consultantId, int transactionType, int transactionStatusType,
			long startDate, long endDate, Long companyId, int start, int length) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = null;
		int total = 0;
		String queryName = null;
		if (transactionType == Transaction.TYPE_INVOICE) {

			if (transactionStatusType == TransactionHistory.ALL_INVOICES) {
				queryName = "getInvoicesListByConsultant";

			}
			if (transactionStatusType == TransactionHistory.OPENED_INVOICES) {
				queryName = "getOpenInvoicesListByConsultant";

			}
			if (transactionStatusType == TransactionHistory.DRAFT_INVOICES) {
				queryName = "getDraftInvoicesListByConsultant";

			}
			if (transactionStatusType == TransactionHistory.OVER_DUE_INVOICES) {
				queryName = "getOverdueInvoicesListByConsultant";
				query = session
						.getNamedQuery(queryName)
						.setParameter("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("currentDate",
								new FinanceDate().getDate())
						.setParameter("consultantId", consultantId);
			}
//			else
//				queryName = "getInvoicesListByConsultant";

		} else if (transactionType == Transaction.TYPE_CASH_SALES) {
			if (transactionStatusType == TransactionHistory.ALL_CASHSALES) {
				queryName = "getCashSalesByConsultant";
			} else {
				queryName = "getDraftCashSalesByConsultant";
			}

		} else if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
			if (transactionStatusType == TransactionHistory.ALL_CREDITMEMOS) {
				queryName = "getAllCustomerCreditMemosByConsultant";
			} else if (transactionStatusType == TransactionHistory.OPEND_CREDITMEMOS) {
				queryName = "getOpendCustomerCreditMemosByConsultant";
			} else {
				queryName = "getDraftCustomerCreditMemosByConsultant";
			}

		} else if (transactionType == Transaction.TYPE_RECEIVE_PAYMENT) {
			String typeOfRPString = null;
			if (transactionStatusType == TransactionHistory.ALL_RECEIVEDPAYMENTS) {
				queryName = "getAllReceivePaymentsByConsultant";

			} else {

				if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_CASH) {
					typeOfRPString = "Cash";

				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_CHEQUE) {
					typeOfRPString = "Cheque";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_CREDITCARD) {
					typeOfRPString = "Credit Card";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_STANDING_ORDER) {
					typeOfRPString = "Standing Order";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_MAESTRO) {
					typeOfRPString = "Switch/Maestro";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_ONLINE) {
					typeOfRPString = "Online Banking";
				} else if (transactionStatusType == TransactionHistory.RECEV_PAY_BY_MASTERCARD) {
					typeOfRPString = "Master card";
				} else {
					// (transactionStatusType ==
					// TransactionHistory.RECEV_PAY_BY_DIRECT_DEBIT)
					typeOfRPString = "Direct Debit";
				}
				query = session
						.getNamedQuery("getReceivePaymentsbyTypeByConsultant")
						.setParameter("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("consultantId", consultantId)
						.setParameter("paymentmethod", typeOfRPString,
								EncryptedStringType.INSTANCE);

			}
		} else if (transactionType == Transaction.TYPE_CUSTOMER_REFUNDS) {
			if (transactionStatusType == TransactionHistory.ALL_CUSTOMER_REFUNDS) {
				queryName = "getAllCustomerRefundsByConsultant";

			} else if (transactionStatusType == TransactionHistory.DRAFT_CUSTOMER_REFUNDS) {
				queryName = "getDraftCustomerRefundsByConsultant";
			} else {
				String typeOfRPString = null;
				if (transactionStatusType == TransactionHistory.REFUNDS_BYCASH) {
					typeOfRPString = "Cash";
				} else if (transactionStatusType == TransactionHistory.REFUNDS_BYCHEQUE) {
					typeOfRPString = "Cheque";
				} else {
					// if (transactionStatusType ==
					// TransactionHistory.REFUNDS_BY_CREDITCARD)
					typeOfRPString = "Credit Card";
				}
				query = session
						.getNamedQuery("getCustomerRefundsByTypeByConsultant")
						.setParameter("companyId", companyId)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("customerId", consultantId)
						.setParameter("paymentmethod", typeOfRPString,
								EncryptedStringType.INSTANCE);
			}
		} else if (transactionType == Transaction.TYPE_ESTIMATE) {
			int typeOfEstiate = 1;
			if (transactionStatusType == TransactionHistory.ALL_QUOTES
					|| transactionStatusType == TransactionHistory.ALL_CREDITS
					|| transactionStatusType == TransactionHistory.ALL_CHARGES) {
				queryName = "getAllQuotesByConsultant";
			} else {
				queryName = "getDraftQuotesByConsultant";
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
			query = session.getNamedQuery(queryName)
					.setParameter("companyId", companyId)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("consultantId", consultantId)
					.setParameter("estimateType", typeOfEstiate);

		} else if (transactionType == Transaction.TYPE_SALES_ORDER) {
			int typeOfEstiate = 1;
			if (transactionStatusType == TransactionHistory.COMPLETED_SALES_ORDERS) {
				typeOfEstiate = ClientTransaction.STATUS_COMPLETED;
			}
			if (transactionStatusType == TransactionHistory.OPEN_SALES_ORDERS) {

				typeOfEstiate = 0;

			}
			queryName = "getAllSalesOrdersByConsultant";
			query = session.getNamedQuery(queryName)
					.setParameter("companyId", companyId)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("consultantId", consultantId)
					.setParameter("estimateType", 6)
					.setParameter("status", typeOfEstiate);

		} else if (transactionType == Transaction.TYPE_WRITE_CHECK) {

			if (transactionStatusType == TransactionHistory.ALL_CHEQUES) {
				queryName = "getAllChequesListByCustomer";
			} else {
				queryName = "getDraftChequesListByCustomer";
			}
		} else {
			queryName = "getAllTransactionsByConsultant";

		}
		if (query == null ) {
			query = session.getNamedQuery(queryName)
					.setParameter("companyId", companyId)
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setParameter("consultantId", consultantId);
		}
//		TransactionHistory transactionObj = new TransactionHistory();
		List<TransactionHistory> list;
		List<TransactionHistory> result = new ArrayList<TransactionHistory>();
		if (length == -1) {
			list = query.list();
		} else {
			Object[] object = null;
			total = query.list().size();
//			list = query.setFirstResult(start).setMaxResults(length).list();
			list = query.list();
			Iterator iterator = list.iterator();
			
			while (iterator.hasNext()) {
				TransactionHistory transactionObj = new TransactionHistory();
				object = (Object[]) iterator.next();
				transactionObj.setTransactionId((long) object[0]);
				ClientFinanceDate cfd = new ClientFinanceDate((long)object[1]);
				transactionObj.setDate(cfd);
				transactionObj.setType((int) object[2]);
				transactionObj.setNumber((String) object[3]);
				if(object[4] != null) {
					ClientFinanceDate sed = new ClientFinanceDate((long)object[4]);
					transactionObj.setDueDate(sed);
				}
				else 
				transactionObj.setDueDate(cfd);
				transactionObj.setInvoicedAmount((double) object[5]);
				transactionObj.setIsVoid((boolean) object[6]);
				transactionObj.setMemo((String) object[7]);
				transactionObj.setStatus((int) object[8]);
				transactionObj.setName((String) object[9]);
				if(object[10]!= null && object[10].getClass().getName() != "java.lang.String" )
				transactionObj.setAmount((double) object[10]);
//				transactionObj.setBeginningBalance((double) object[11]);
//				transactionObj.setCurrency((long) object[12]);
				result.add(transactionObj);
			}
		}
		PaginationList<TransactionHistory> consultantTransactionsList = new PaginationList<TransactionHistory>();
		consultantTransactionsList.addAll(result);
		consultantTransactionsList.setTotalCount(total);
		consultantTransactionsList.setStart(start);
		return consultantTransactionsList;
	}

}
