/**
 * 
 */
package com.nitya.accounter.services;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionTemplate;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientBank;
import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientCashSales;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCreditCardCharge;
import com.nitya.accounter.web.client.core.ClientCreditRating;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientCustomerCreditMemo;
import com.nitya.accounter.web.client.core.ClientCustomerGroup;
import com.nitya.accounter.web.client.core.ClientCustomerRefund;
import com.nitya.accounter.web.client.core.ClientEnterBill;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFiscalYear;
import com.nitya.accounter.web.client.core.ClientInvoice;
import com.nitya.accounter.web.client.core.ClientIssuePayment;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientItemGroup;
import com.nitya.accounter.web.client.core.ClientJournalEntry;
import com.nitya.accounter.web.client.core.ClientPayBill;
import com.nitya.accounter.web.client.core.ClientPayTAX;
import com.nitya.accounter.web.client.core.ClientPaymentTerms;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientPurchaseOrder;
import com.nitya.accounter.web.client.core.ClientReceivePayment;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.core.ClientShippingMethod;
import com.nitya.accounter.web.client.core.ClientShippingTerms;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTransferFund;
import com.nitya.accounter.web.client.core.ClientUnitOfMeasure;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.ClientVendorCreditMemo;
import com.nitya.accounter.web.client.core.ClientVendorGroup;
import com.nitya.accounter.web.client.core.ClientWriteCheck;

/**
 * @author vimukti5
 * 
 */
public class AccounterClientService extends HibernateDaoSupport implements
		IAccounterClientService {

	@Override
	public ClientAccount alterAccount(long companyId, ClientAccount account)
			throws DAOException {
		return null;
	}

	@Override
	public ClientCashPurchase alterCashPurchase(long company,
			ClientCashPurchase cashPurchase) throws DAOException {
		return null;
	}

	@Override
	public ClientCashSales alterCashSales(long company,
			ClientCashSales cashSales) throws DAOException {
		return null;
	}

	@Override
	public ClientCompany alterCompany(ClientCompany company)
			throws DAOException {
		return null;
	}

	@Override
	public ClientCreditCardCharge alterCreditCardCharge(long company,
			ClientCreditCardCharge creditCardCharge) throws DAOException {
		return null;
	}

	@Override
	public ClientCreditRating alterCreditRating(long company,
			ClientCreditRating creditRating) throws DAOException {
		return null;
	}

	@Override
	public ClientCurrency alterCurrency(long company, ClientCurrency currency)
			throws DAOException {
		return null;
	}

	@Override
	public ClientCustomer alterCustomer(long company, ClientCustomer customer)
			throws DAOException {

		return null;
	}

	@Override
	public ClientCustomerCreditMemo alterCustomerCreditMemo(long company,
			ClientCustomerCreditMemo customerCreditMemo) throws DAOException {

		return null;
	}

	@Override
	public ClientCustomerGroup alterCustomerGroup(long company,
			ClientCustomerGroup customerGroup) throws DAOException {

		return null;
	}

	@Override
	public ClientCustomerRefund alterCustomerRefunds(long company,
			ClientCustomerRefund customerRefunds) throws DAOException {

		return null;
	}

	@Override
	public ClientEnterBill alterEnterBill(long company,
			ClientEnterBill enterBill) throws DAOException {

		return null;
	}

	@Override
	public ClientEstimate alterEstimate(long company, ClientEstimate estimate)
			throws DAOException {

		return null;
	}

	@Override
	public ClientFiscalYear alterFiscalYear(long company,
			ClientFiscalYear fiscalYear) throws DAOException {

		return null;
	}

	@Override
	public ClientInvoice alterInvoice(long company, ClientInvoice invoice)
			throws DAOException {

		return null;
	}

	@Override
	public ClientIssuePayment alterIssuePayment(long company,
			ClientIssuePayment issuePayment) throws DAOException {

		return null;
	}

	@Override
	public ClientItem alterItem(long company, ClientItem item)
			throws DAOException {

		return null;
	}

	@Override
	public ClientItemGroup alterItemGroup(long company,
			ClientItemGroup itemGroup) throws DAOException {

		return null;
	}

	@Override
	public ClientJournalEntry alterJournalEntry(long company,
			ClientJournalEntry journalEntry) throws DAOException {

		return null;
	}

	@Override
	public ClientTransferFund alterMakeDeposit(long company,
			ClientTransferFund makeDeposit) throws DAOException {

		return null;
	}

	@Override
	public ClientPayBill alterPayBill(long company, ClientPayBill payBill)
			throws DAOException {

		return null;
	}

	@Override
	public ClientPayTAX alterPaySalesTax(long company, ClientPayTAX paySalesTax)
			throws DAOException {

		return null;
	}

	@Override
	public ClientPaymentTerms alterPaymentTerms(long company,
			ClientPaymentTerms paymentTerms) throws DAOException {

		return null;
	}

	@Override
	public ClientPriceLevel alterPriceLevel(long company,
			ClientPriceLevel priceLevel) throws DAOException {

		return null;
	}

	@Override
	public ClientPurchaseOrder alterPurchaseOrder(long company,
			ClientPurchaseOrder purchaseOrder) throws DAOException {

		return null;
	}

	@Override
	public ClientReceivePayment alterReceivePayment(long company,
			ClientReceivePayment receivePayment) throws DAOException {

		return null;
	}

	@Override
	public ClientSalesPerson alterSalesPerson(long company,
			ClientSalesPerson salesPerson) throws DAOException {

		return null;
	}

	@Override
	public ClientShippingMethod alterShippingMethod(long company,
			ClientShippingMethod shippingMethod) throws DAOException {

		return null;
	}

	@Override
	public ClientShippingTerms alterShippingTerms(long company,
			ClientShippingTerms shippingTerms) throws DAOException {

		return null;
	}

	@Override
	public ClientTAXAgency alterTaxAgency(long company,
			ClientTAXAgency taxAgency) throws DAOException {

		return null;
	}

	@Override
	public ClientTAXCode alterTaxCode(long company, ClientTAXCode taxCode)
			throws DAOException {

		return null;
	}

	@Override
	public ClientTAXGroup alterTaxGroup(long company, ClientTAXGroup taxGroup)
			throws DAOException {

		return null;
	}

	@Override
	public ClientUnitOfMeasure alterUnitOfMeasure(long company,
			ClientUnitOfMeasure untiOfMeasure) throws DAOException {

		return null;
	}

	@Override
	public ClientUser alterUser(long company, ClientUser user)
			throws DAOException {

		return null;
	}

	@Override
	public ClientVendor alterVendor(long company, ClientVendor vendor)
			throws DAOException {

		return null;
	}

	@Override
	public ClientVendorCreditMemo alterVendorCreditMemo(long company,
			ClientVendorCreditMemo vendorCreditMemo) throws DAOException {

		return null;
	}

	@Override
	public ClientVendorGroup alterVendorGroup(long company,
			ClientVendorGroup vendorGroup) throws DAOException {

		return null;
	}

	@Override
	public ClientWriteCheck alterWriteCheck(long company,
			ClientWriteCheck writeCheck) throws DAOException {

		return null;
	}

	@Override
	public ClientAccount createAccount(long companyId, ClientAccount account)
			throws DAOException {

		return null;
	}

	@Override
	public ClientBank createBank(long company, ClientBank bank)
			throws DAOException {

		return null;
	}

	@Override
	public ClientCashPurchase createCashPurchase(long company,
			ClientCashPurchase cashPurchase) throws DAOException {

		return null;
	}

	@Override
	public ClientCashSales createCashSales(long company,
			ClientCashSales cashSales) throws DAOException {

		return null;
	}

	@Override
	public ClientCompany createCompany(ClientCompany company)
			throws DAOException {

		return null;
	}

	@Override
	public ClientCreditCardCharge createCreditCardCharge(long company,
			ClientCreditCardCharge creditCardCharge) throws DAOException {

		return null;
	}

	@Override
	public ClientCreditRating createCreditRating(long company,
			ClientCreditRating creditRating) throws DAOException {

		return null;
	}

	@Override
	public ClientCurrency createCurrency(long company, ClientCurrency currency)
			throws DAOException {

		return null;
	}

	@Override
	public ClientCustomer createCustomer(long company, ClientCustomer customer)
			throws DAOException {

		return null;
	}

	@Override
	public ClientCustomerCreditMemo createCustomerCreditMemo(long company,
			ClientCustomerCreditMemo customerCreditMemo) throws DAOException {

		return null;
	}

	@Override
	public ClientCustomerGroup createCustomerGroup(long company,
			ClientCustomerGroup customerGroup) throws DAOException {

		return null;
	}

	@Override
	public ClientCustomerRefund createCustomerRefunds(long company,
			ClientCustomerRefund customerRefunds) throws DAOException {

		return null;
	}

	@Override
	public ClientEnterBill createEnterBill(long company,
			ClientEnterBill enterBill) throws DAOException {

		return null;
	}

	@Override
	public ClientEstimate createEstimate(long company, ClientEstimate estimate)
			throws DAOException {

		return null;
	}

	@Override
	public ClientFiscalYear createFiscalYear(long company,
			ClientFiscalYear fiscalYear) throws DAOException {

		return null;
	}

	@Override
	public ClientInvoice createInvoice(long company, ClientInvoice invoice)
			throws DAOException {

		return null;
	}

	@Override
	public ClientIssuePayment createIssuePayment(long company,
			ClientIssuePayment issuePayment) throws DAOException {

		return null;
	}

	@Override
	public ClientItem createItem(long company, ClientItem item)
			throws DAOException {

		return null;
	}

	@Override
	public ClientItemGroup createItemGroup(long company,
			ClientItemGroup itemGroup) throws DAOException {

		return null;
	}

	@Override
	public ClientJournalEntry createJournalEntry(long company,
			ClientJournalEntry journalEntry) throws DAOException {

		return null;
	}

	@Override
	public ClientTransferFund createMakeDeposit(long company,
			ClientTransferFund makeDeposit) throws DAOException {

		return null;
	}

	@Override
	public ClientPayBill createPayBill(long company, ClientPayBill payBill)
			throws DAOException {

		return null;
	}

	@Override
	public ClientPayTAX createPaySalesTax(long company, ClientPayTAX paySalesTax)
			throws DAOException {

		return null;
	}

	@Override
	public ClientPaymentTerms createPaymentTerms(long company,
			ClientPaymentTerms paymentTerms) throws DAOException {

		return null;
	}

	@Override
	public ClientPriceLevel createPriceLevel(long company,
			ClientPriceLevel priceLevel) throws DAOException {

		return null;
	}

	@Override
	public ClientPurchaseOrder createPurchaseOrder(long company,
			ClientPurchaseOrder purchaseOrder) throws DAOException {

		return null;
	}

	@Override
	public ClientReceivePayment createReceivePayment(long company,
			ClientReceivePayment receivePayment) throws DAOException {

		return null;
	}

	@Override
	public ClientSalesPerson createSalesPerson(long company,
			ClientSalesPerson salesPerson) throws DAOException {

		return null;
	}

	@Override
	public ClientShippingMethod createShippingMethod(long company,
			ClientShippingMethod shippingMethod) throws DAOException {

		return null;
	}

	@Override
	public ClientShippingTerms createShippingTerms(long company,
			ClientShippingTerms shippingTerms) throws DAOException {

		return null;
	}

	@Override
	public ClientTAXAgency createTaxAgency(long company,
			ClientTAXAgency taxAgency) throws DAOException {

		return null;
	}

	@Override
	public ClientTAXCode createTaxCode(long company, ClientTAXCode taxCode)
			throws DAOException {

		return null;
	}

	@Override
	public ClientTAXGroup createTaxGroup(long company, ClientTAXGroup taxGroup)
			throws DAOException {

		return null;
	}

	@Override
	public ClientUnitOfMeasure createUnitOfMeasure(long company,
			ClientUnitOfMeasure untiOfMeasure) throws DAOException {

		return null;
	}

	@Override
	public ClientUser createUser(ClientUser user) throws DAOException {

		return null;
	}

	@Override
	public ClientVendor createVendor(long company, ClientVendor vendor)
			throws DAOException {

		return null;
	}

	@Override
	public ClientVendorCreditMemo createVendorCreditMemo(long company,
			ClientVendorCreditMemo vendorCreditMemo) throws DAOException {

		return null;
	}

	@Override
	public ClientVendorGroup createVendorGroup(long company,
			ClientVendorGroup vendorGroup) throws DAOException {

		return null;
	}

	@Override
	public ClientWriteCheck createWriteCheck(long company,
			ClientWriteCheck writeCheck) throws DAOException {

		return null;
	}

	@Override
	public Boolean deleteObject(Class class1, long Id) throws DAOException {

		return null;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {

	}

	public void setAccounterDao(AccounterDAOService accounterDao) {

	}

	// TransactionTemplate transactionTemplate;
	// IAccounterDAOService accounterDao;
	// IAccounterGUIDAOService accounterGUIDao;
	//
	// public void setTransactionTemplate(TransactionTemplate template) {
	// this.transactionTemplate = template;
	// }
	//
	// public void setAccounterDao(IAccounterDAOService accounterDao) {
	// this.accounterDao = accounterDao;
	// }
	//
	// public void setAccounterGUIDao(IAccounterGUIDAOService accounterGUIDao) {
	// this.accounterGUIDao = accounterGUIDao;
	// }
	//
	// @Override
	// public ClientAccount alterAccount(long company, final ClientAccount
	// account)
	// throws DAOException {
	//
	// try {
	//
	// return (ClientAccount) updateObject(Account.class, account);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCashPurchase alterCashPurchase(long company,
	// final ClientCashPurchase cashPurchase) throws DAOException {
	//
	// try {
	// return (ClientCashPurchase) updateObject(CashPurchase.class,
	// cashPurchase);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCashSales alterCashSales(long company,
	// final ClientCashSales clientCashSales) throws DAOException {
	// try {
	//
	// return (ClientCashSales) updateObject(CashSales.class,
	// clientCashSales);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientCompany alterCompany(final ClientCompany clientCompany)
	// throws DAOException {
	// try {
	//
	// return (ClientCompany) updateObject(Company.class, clientCompany);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCreditCardCharge alterCreditCardCharge(long company,
	// final ClientCreditCardCharge clientCreditCardCharge)
	// throws DAOException {
	//
	// try {
	// return (ClientCreditCardCharge) updateObject(
	// CreditCardCharge.class, clientCreditCardCharge);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientCreditRating alterCreditRating(long company,
	// final ClientCreditRating clientCreditRating) throws DAOException {
	//
	// try {
	//
	// return (ClientCreditRating) updateObject(CreditRating.class,
	// clientCreditRating);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientCurrency alterCurrency(long company, ClientCurrency
	// currency)
	// throws DAOException {
	//
	// try {
	//
	// return (ClientCurrency) updateObject(Currency.class, currency);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCustomer alterCustomer(long company, ClientCustomer
	// customer)
	// throws DAOException {
	//
	// try {
	//
	// return (ClientCustomer) updateObject(Customer.class, customer);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCustomerCreditMemo alterCustomerCreditMemo(long company,
	// ClientCustomerCreditMemo customerCreditMemo) throws DAOException {
	//
	// try {
	// return (ClientCustomerCreditMemo) updateObject(
	// CustomerCreditMemo.class, customerCreditMemo);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCustomerGroup alterCustomerGroup(long company,
	// final ClientCustomerGroup customerGroup) throws DAOException {
	// try {
	//
	// return (ClientCustomerGroup) updateObject(CustomerGroup.class,
	// customerGroup);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCustomerRefund alterCustomerRefunds(long company,
	// ClientCustomerRefund customerRefunds) throws DAOException {
	// try {
	//
	// return (ClientCustomerRefund) updateObject(CustomerRefund.class,
	// customerRefunds);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientEnterBill alterEnterBill(long company,
	// ClientEnterBill enterBill) throws DAOException {
	//
	// try {
	//
	// return (ClientEnterBill) updateObject(EnterBill.class, enterBill);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientEstimate alterEstimate(long company, ClientEstimate
	// estimate)
	// throws DAOException {
	//
	// try {
	//
	// return (ClientEstimate) updateObject(Estimate.class, estimate);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientFiscalYear alterFiscalYear(final long company,
	// final ClientFiscalYear clientFiscalYear) throws DAOException {
	//
	// try {
	//
	// HibernateTemplate template = getHibernateTemplate();
	//
	// List list = template.find("from FiscalYear f where f.id= ? ",
	// new Object[] { clientFiscalYear.getID() });
	//
	// final FiscalYear existingFiscalYear = (FiscalYear) list.get(0);
	//
	// if (list.size() > 0) {
	//
	// return (ClientFiscalYear) template
	// .execute(new HibernateCallback() {
	//
	// @Override
	// public Object doInHibernate(Session session)
	// throws HibernateException, SQLException {
	// Util.toServerObject(existingFiscalYear,
	// clientFiscalYear, session);
	// // existingFiscalYear
	// // .setCompany(getCompanyFromSession(
	// // company, session));
	//
	// if (existingFiscalYear.getStartDate().equals(
	// existingFiscalYear
	// .getPreviousStartDate())) {
	// session.saveOrUpdate(existingFiscalYear);
	// } else if (!existingFiscalYear
	// .getStartDate()
	// .equals(
	// existingFiscalYear
	// .getPreviousStartDate())) {
	// Date modifiedStartDate = existingFiscalYear
	// .getStartDate();
	// Date existingLeastStartDate = modifiedStartDate;
	// Date existingHighestEndDate = modifiedStartDate;
	// Boolean exist = Boolean.FALSE;
	// List list = getHibernateTemplate().find(
	// "from FiscalYear f");
	// if (list.size() > 0) {
	// Iterator i = list.iterator();
	// if (i.hasNext()) {
	// FiscalYear fs = (FiscalYear) i
	// .next();
	// existingLeastStartDate = fs
	// .getStartDate();
	// existingHighestEndDate = fs
	// .getEndDate();
	// }
	// i = list.iterator();
	// while (i.hasNext()) {
	// FiscalYear fs = (FiscalYear) i
	// .next();
	// if (modifiedStartDate.after(fs
	// .getStartDate())
	// && modifiedStartDate
	// .before(fs
	// .getEndDate())) {
	// exist = Boolean.TRUE;
	// break;
	// }
	// if (fs.getStartDate().before(
	// existingLeastStartDate)) {
	// existingLeastStartDate = fs
	// .getStartDate();
	// }
	// if (fs.getEndDate().after(
	// existingHighestEndDate)) {
	// existingHighestEndDate = fs
	// .getEndDate();
	// }
	//
	// }
	// if (!exist) {
	// Calendar cal = Calendar
	// .getInstance();
	// cal.setTime(modifiedStartDate);
	// Integer modifiedYear = cal
	// .get(Calendar.YEAR);
	//
	// cal.setTime(existingLeastStartDate);
	// Integer existingLeastYear = cal
	// .get(Calendar.YEAR);
	//
	// cal.setTime(existingHighestEndDate);
	//
	// Integer existingHighestYear = cal
	// .get(Calendar.YEAR);
	// if (modifiedStartDate
	// .before(existingLeastStartDate)) {
	// int diff = existingLeastYear
	// - modifiedYear;
	// for (int k = 0; k < diff; k++) {
	//
	// cal.set(modifiedYear + k,
	// 0, 1);
	// Date startDate = cal
	// .getTime();
	//
	// cal.set(modifiedYear + k,
	// 11, 31);
	// Date endDate = cal
	// .getTime();
	//
	// FiscalYear fs = new FiscalYear();
	// // fs
	// // .setCompany(existingFiscalYear
	// // .getCompany());
	// fs.setStartDate(startDate);
	// fs.setEndDate(endDate);
	// fs
	// .setStatus(FiscalYear.STATUS_OPEN);
	// fs
	// .setIsCurrentFiscalYear(Boolean.FALSE);
	//
	// ClientFiscalYear cfs = Util
	// .toClientObject(
	// fs,
	// ClientFiscalYear.class);
	//
	// try {
	// createFiscalYear(
	// company, cfs);
	// } catch (DAOException e) {
	// // Auto-generated
	// // catch block
	// e.printStackTrace();
	// }
	// }
	//
	// } else if (modifiedStartDate
	// .after(existingHighestEndDate)) {
	// int diff = modifiedYear
	// - existingLeastYear;
	// for (int k = 1; k <= diff; k++) {
	// cal.set(existingLeastYear
	// + k, 0, 1);
	// Date startDate = cal
	// .getTime();
	//
	// cal.set(existingLeastYear
	// + k, 0, 1);
	// Date endDate = cal
	// .getTime();
	// FiscalYear fs = new FiscalYear();
	// // fs
	// // .setCompany(existingFiscalYear
	// // .getCompany());
	// fs.setStartDate(startDate);
	// fs.setEndDate(endDate);
	// fs
	// .setStatus(FiscalYear.STATUS_OPEN);
	// fs
	// .setIsCurrentFiscalYear(Boolean.FALSE);
	//
	// ClientFiscalYear cfs = Util
	// .toClientObject(
	// fs,
	// ClientFiscalYear.class);
	//
	// try {
	// createFiscalYear(
	// company, cfs);
	// } catch (DAOException e) {
	// // catch block
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// }
	// existingFiscalYear
	// .setStartDate(existingFiscalYear
	// .getPreviousStartDate());
	// session
	// .saveOrUpdate(existingFiscalYear);
	// }
	// } else if (existingFiscalYear.getStatus() == FiscalYear.STATUS_CLOSE) {
	// if (!existingFiscalYear
	// .getIsCurrentFiscalYear()) {
	// return null;
	// // throw (new DAOException(
	// // DAOException.INVALID_REQUEST_EXCEPTION
	// // ,
	// // null));
	// } else {
	// session
	// .saveOrUpdate(existingFiscalYear);
	// }
	// }
	//
	// return Util.toClientObject(existingFiscalYear,
	// ClientFiscalYear.class);
	// }
	// });
	//
	// }
	//
	// return null;
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientInvoice alterInvoice(long company, ClientInvoice invoice)
	// throws DAOException {
	// try {
	// return (ClientInvoice) updateObject(Invoice.class, invoice);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientIssuePayment alterIssuePayment(long company,
	// ClientIssuePayment issuePayment) throws DAOException {
	// try {
	//
	// return (ClientIssuePayment) updateObject(IssuePayment.class,
	// issuePayment);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientItem alterItem(long company, ClientItem item)
	// throws DAOException {
	// try {
	//
	// return (ClientItem) updateObject(Item.class, item);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientItemGroup alterItemGroup(long company,
	// ClientItemGroup itemGroup) throws DAOException {
	// try {
	// return (ClientItemGroup) updateObject(ItemGroup.class, itemGroup);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	//
	// @Override
	// public ClientJournalEntry alterJournalEntry(long company,
	// ClientJournalEntry journalEntry) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	//
	// List list = template.find(
	// "from JournalEntry pt where pt.id= ?",
	// new Object[] { journalEntry.getID() });
	//
	// if (list.size() > 0) {
	//
	// JournalEntry serveObject = (JournalEntry) list.get(0);
	// return (ClientJournalEntry) updateObject(JournalEntry.class,
	// journalEntry);
	// }
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// return null;
	// }
	//
	// @Override
	// public ClientMakeDeposit alterMakeDeposit(long company,
	// ClientMakeDeposit makeDeposit) throws DAOException {
	// try {
	// return (ClientMakeDeposit) updateObject(MakeDeposit.class,
	// makeDeposit);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientPayBill alterPayBill(long company, ClientPayBill payBill)
	// throws DAOException {
	// try {
	// return (ClientPayBill) updateObject(PayBill.class, payBill);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientPaySalesTax alterPaySalesTax(long company,
	// ClientPaySalesTax paySalesTax) throws DAOException {
	// try {
	// return (ClientPaySalesTax) updateObject(PaySalesTax.class,
	// paySalesTax);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientPaymentTerms alterPaymentTerms(long company,
	// ClientPaymentTerms paymentTerms) throws DAOException {
	// try {
	// return (ClientPaymentTerms) updateObject(PaymentTerms.class,
	// paymentTerms);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientPriceLevel alterPriceLevel(long company,
	// ClientPriceLevel priceLevel) throws DAOException {
	// try {
	// return (ClientPriceLevel) updateObject(PriceLevel.class, priceLevel);
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientPurchaseOrder alterPurchaseOrder(long company,
	// ClientPurchaseOrder purchaseOrder) throws DAOException {
	// try {
	// return (ClientPurchaseOrder) updateObject(PurchaseOrder.class,
	// purchaseOrder);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientReceivePayment alterReceivePayment(long company,
	// ClientReceivePayment receivePayment) throws DAOException {
	// try {
	// return (ClientReceivePayment) updateObject(ReceivePayment.class,
	// receivePayment);
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientSalesOrder alterSalesOrder(long company,
	// ClientSalesOrder salesOrder) throws DAOException {
	// try {
	// return (ClientSalesOrder) updateObject(SalesOrder.class, salesOrder);
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientSalesPerson alterSalesPerson(long company,
	// ClientSalesPerson salesPerson) throws DAOException {
	// try {
	// return (ClientSalesPerson) updateObject(SalesPerson.class,
	// salesPerson);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientShippingMethod alterShippingMethod(long company,
	// ClientShippingMethod shippingMethod) throws DAOException {
	// try {
	// return (ClientShippingMethod) updateObject(ShippingMethod.class,
	// shippingMethod);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientShippingTerms alterShippingTerms(long company,
	// ClientShippingTerms shippingTerms) throws DAOException {
	// try {
	// return (ClientShippingTerms) updateObject(ShippingTerms.class,
	// shippingTerms);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientTaxAgency alterTaxAgency(long company,
	// ClientTaxAgency taxAgency) throws DAOException {
	// try {
	// return (ClientTaxAgency) updateObject(TaxAgency.class, taxAgency);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientTaxCode alterTaxCode(long company, ClientTaxCode taxCode)
	// throws DAOException {
	// try {
	// return (ClientTaxCode) updateObject(TaxCode.class, taxCode);
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientTaxGroup alterTaxGroup(long company, ClientTaxGroup
	// taxGroup)
	// throws DAOException {
	// try {
	// return (ClientTaxGroup) updateObject(TaxGroup.class, taxGroup);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientTaxRates alterTaxRates(long company, ClientTaxRates
	// taxRates)
	// throws DAOException {
	// try {
	// return (ClientTaxRates) updateObject(TaxRates.class, taxRates);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientTransferFund alterTransferFund(long company,
	// ClientTransferFund transferFund) throws DAOException {
	// try {
	//
	// return (ClientTransferFund) updateObject(TransferFund.class,
	// transferFund);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientUnitOfMeasure alterUnitOfMeasure(long company,
	// ClientUnitOfMeasure untiOfMeasure) throws DAOException {
	// try {
	// return (ClientUnitOfMeasure) updateObject(UnitOfMeasure.class,
	// untiOfMeasure);
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientUser alterUser(long company, ClientUser user)
	// throws DAOException {
	// try {
	// return (ClientUser) updateObject(User.class, user);
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientVendor alterVendor(long company, ClientVendor vendor)
	// throws DAOException {
	// try {
	//
	// return (ClientVendor) updateObject(Vendor.class, vendor);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientVendorCreditMemo alterVendorCreditMemo(long company,
	// final ClientVendorCreditMemo vendorCreditMemo) throws DAOException {
	// try {
	// return (ClientVendorCreditMemo) updateObject(
	// VendorCreditMemo.class, vendorCreditMemo);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	// @Override
	// public ClientVendorGroup alterVendorGroup(long company,
	// final ClientVendorGroup vendorGroup) throws DAOException {
	// return (ClientVendorGroup) updateObject(VendorGroup.class, vendorGroup);
	// }
	//
	//
	// public IAccounterCore updateObject(final Class class1,
	// final IAccounterCore accounterCore) {
	//
	// HibernateTemplate template = getHibernateTemplate();
	// return (IAccounterCore) template.execute(new HibernateCallback() {
	// @Override
	// public Object doInHibernate(Session session)
	// throws HibernateException, SQLException {
	// IAccounterServerCore serverCore = (IAccounterServerCore) session
	// .get(class1, accounterCore.getID());
	// if (serverCore != null) {
	// Util.toServerObject(serverCore, accounterCore, session);
	// if (serverCore instanceof Lifecycle) {
	// Lifecycle lifecycle = (Lifecycle) serverCore;
	// lifecycle.onUpdate(session);
	// }
	// session.saveOrUpdate(serverCore);
	// return Util.toClientObject(serverCore, accounterCore
	// .getClass());
	// }
	// return null;
	// }
	// });
	// }
	//
	// @Override
	// public ClientWriteCheck alterWriteCheck(final long company,
	// final ClientWriteCheck writeCheck) throws DAOException {
	//
	// return (ClientWriteCheck) updateObject(WriteCheck.class, writeCheck);
	//
	// }
	//
	//
	// @Override
	// public ClientAccount createAccount(final long company,
	// final ClientAccount clientAccount) throws DAOException {
	//
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template
	// .find(
	// "from Account a where a.company.id = ? and a.name = ? and a.number = ?",
	// new Object[] { Long.valueOf(company),
	// clientAccount.getName(),
	// clientAccount.getNumber() });
	// if (list.size() > 0) {
	//
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	// Account account = new Account();
	//
	// return (ClientAccount) createObject(company, account, clientAccount);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientBank createBank(final long company, final ClientBank
	// clientBank)
	// throws DAOException {
	//
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template.find(
	// "from Bank b where b.name = ? and b.company.id = ?",
	// new Object[] { clientBank.getName(), company });
	// if (list.size() > 0) {
	// // throw (new DAOException(DAOException.DATABASE_EXCEPTION, ));
	// } else {
	// return (ClientBank) createObject(company, new Bank(),
	// clientBank);
	// }
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// return null;
	// }
	//
	// @Override
	// public ClientCashSales createCashSales(final long company,
	// final ClientCashSales clientCashSales) throws DAOException {
	// CashSales serverObj = new CashSales();
	// serverObj.setType(Transaction.TYPE_CASH_SALES);
	// return (ClientCashSales) createObject(company, serverObj,
	// clientCashSales);
	//
	// }
	//
	// @Override
	// public ClientCompany createCompany(final ClientCompany clientCompany)
	// throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// return (ClientCompany) template.execute(new HibernateCallback() {
	//
	// public Object doInHibernate(Session session)
	// throws HibernateException, SQLException {
	// final Company company = new Company();
	// Util.toServerObject(company, clientCompany, session);
	// // company.initDefaultUSAccounts();
	// session.save(company);
	// createDefaultTaxGroup(company);
	// return Util.toClientObject(company, ClientCompany.class);
	// }
	//
	// });
	//
	// } catch (DataAccessException e) {
	// e.printStackTrace();
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// private void createDefaultTaxGroup(Company company) {
	//
	// try {
	// // Default TaxGroup Creation
	//
	// TaxAgency defaultTaxAgency = new TaxAgency();
	// // defaultTaxAgency.setCompany(company);
	// defaultTaxAgency.setActive(Boolean.FALSE);
	// defaultTaxAgency.setName("Tax Agency");
	// //
	// // defaultTaxAgency.setPaymentTerm(accounterDao.getPaymentTerms(
	// // company.getID(), "Quarterly"));
	// // defaultTaxAgency.setLiabilityAccount(accounterDao.getAccount(
	// // company.getID(), "Sales Tax Payable"));
	//
	// this.createTaxAgency(company.getID(), Util.toClientObject(
	// defaultTaxAgency, ClientTaxAgency.class));
	//
	// TaxCode defaultTaxCode = new TaxCode();
	// // defaultTaxCode.setCompany(company);
	// defaultTaxCode.setIsActive(Boolean.TRUE);
	// defaultTaxCode.setName("None");
	// defaultTaxCode.setTaxAgency(defaultTaxAgency);
	// defaultTaxCode.setTaxRates(null);
	//
	// this.createTaxCode(company.getID(), Util.toClientObject(
	// defaultTaxCode, ClientTaxCode.class));
	//
	// TaxGroup defaultTaxGroup = new TaxGroup();
	// // defaultTaxGroup.setCompany(company);
	// defaultTaxGroup.setName("None");
	//
	// Set<TaxCode> taxCodes = new HashSet<TaxCode>();
	// taxCodes.add(defaultTaxCode);
	// defaultTaxGroup.setTaxCodes(taxCodes);
	//
	// this.createTaxGroup(company.getID(), Util.toClientObject(
	// defaultTaxGroup, ClientTaxGroup.class));
	// } catch (DAOException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// @Override
	// public ClientCreditCardCharge createCreditCardCharge(final long company,
	// final ClientCreditCardCharge clientCreditCardCharge)
	// throws DAOException {
	// CreditCardCharge serverObj = new CreditCardCharge();
	// serverObj.setType(Transaction.TYPE_CREDIT_CARD_CHARGE);
	// return (ClientCreditCardCharge) createObject(company,
	// new CreditCardCharge(), clientCreditCardCharge);
	//
	// }
	//
	// @Override
	// public ClientCreditRating createCreditRating(final long company,
	// final ClientCreditRating clientCreditRating) throws DAOException {
	//
	// return (ClientCreditRating) createObject(company, new CreditRating(),
	// clientCreditRating);
	// }
	//
	//
	// @Override
	// public ClientCurrency createCurrency(final long company,
	// final ClientCurrency clientCurrency) throws DAOException {
	//
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	//
	// List list = template
	// .find(
	// "from Currency cu where cu.formalName=? and cu.company.id=? ",
	// new Object[] { clientCurrency.getFormalName(),
	// company });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	// // template.save(currency);
	//
	// return (ClientCurrency) createObject(company, new Currency(),
	// clientCurrency);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @SuppressWarnings( { "unchecked", "null" })
	// @Override
	// public ClientCustomer createCustomer(final long company,
	// final ClientCustomer clientCustomer) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	//
	// List list = template.find(
	// "from Customer a where a.company.id = ? and a.name = ?",
	// new Object[] { company, clientCustomer.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// } else {
	// List<TaxAgency> taxAgencies = null;
	//
	// for (TaxAgency taxAgency : taxAgencies) {
	//
	// if (taxAgency.getLiabilityAccount().getName()
	// .equalsIgnoreCase(
	// messages.OPENING_BALANCE)) {
	//
	// if (list.size() > 0
	// && clientCustomer.getBalance() > 0.0
	// && (clientCustomer.getTaxGroup() != null && clientCustomer
	// .getTaxGroup().length() != 0)) {
	//
	// throw (new DAOException(
	// DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	//
	// }
	//
	// }
	//
	// }
	//
	// Customer serverObj = new Customer();
	// serverObj.setType(Payee.TYPE_CUSTOMER);
	// serverObj.setOpeningBalance(clientCustomer.getBalance());
	// return (ClientCustomer) createObject(company, serverObj,
	// clientCustomer);
	// }
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCustomerCreditMemo createCustomerCreditMemo(
	// final long company,
	// final ClientCustomerCreditMemo clientCustomerCreditMemo)
	// throws DAOException {
	// CustomerCreditMemo creditMemo = new CustomerCreditMemo();
	// creditMemo.setType(Transaction.TYPE_CUSTOMER_CREDIT_MEMO);
	// return (ClientCustomerCreditMemo) createObject(company, creditMemo,
	// clientCustomerCreditMemo);
	//
	// }
	//
	//
	// @Override
	// public ClientCustomerGroup createCustomerGroup(final long company,
	// final ClientCustomerGroup clientCustomerGroup) throws DAOException {
	//
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template
	// .find(
	// "from CustomerGroup cg where cg.name = ? and cg.company.id = ?",
	// new Object[] { clientCustomerGroup.getName(),
	// company });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientCustomerGroup) createObject(company,
	// new CustomerGroup(), clientCustomerGroup);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCustomerRefund createCustomerRefunds(final long company,
	// final ClientCustomerRefund clientCustomerRefunds)
	// throws DAOException {
	// CustomerRefund serverObj = new CustomerRefund();
	// serverObj.setType(Transaction.TYPE_CUSTOMER_REFUNDS);
	// return (ClientCustomerRefund) createObject(company, serverObj,
	// clientCustomerRefunds);
	// }
	//
	// @Override
	// public ClientEnterBill createEnterBill(final long company,
	// final ClientEnterBill clientEnterBill) throws DAOException {
	//
	// EnterBill serverObj = new EnterBill();
	// serverObj.setType(Transaction.TYPE_ENTER_BILL);
	// return (ClientEnterBill) createObject(company, serverObj,
	// clientEnterBill);
	// }
	//
	// @Override
	// public ClientEstimate createEstimate(final long company,
	// final ClientEstimate clientEstimate) throws DAOException {
	// Estimate serverObj = new Estimate();
	// serverObj.setType(Transaction.TYPE_ESTIMATE);
	// return (ClientEstimate) createObject(company, serverObj, clientEstimate);
	//
	// }
	//
	// @Override
	// public ClientFiscalYear createFiscalYear(final long company,
	// final ClientFiscalYear clientFiscalYear) throws DAOException {
	// return (ClientFiscalYear) createObject(company, new FiscalYear(),
	// clientFiscalYear);
	// }
	//
	// @Override
	// public ClientInvoice createInvoice(final long company,
	// final ClientInvoice clientInvoice) throws DAOException {
	// Invoice serverObj = new Invoice();
	// serverObj.setType(Transaction.TYPE_INVOICE);
	// return (ClientInvoice) createObject(company, serverObj, clientInvoice);
	// }
	//
	// @Override
	// public ClientIssuePayment createIssuePayment(final long company,
	// final ClientIssuePayment clientIssuePayment) throws DAOException {
	//
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	//
	// return (ClientIssuePayment) template
	// .execute(new HibernateCallback() {
	//
	// @Override
	// public Object doInHibernate(Session session)
	// throws HibernateException, SQLException {
	// IssuePayment issuePayment = new IssuePayment();
	//
	// Util.toServerObject(issuePayment,
	// clientIssuePayment, session);
	//
	// // Excess Information need to be set to this Object
	// issuePayment
	// .setType(Transaction.TYPE_ISSUE_PAYMENT);
	//
	// // Get the ID assigned to the Dummy WriteCheck or
	// // the
	// // CustomerRefund Object and Set the Actual Object
	// // After retrieving from Database
	// Iterator<TransactionIssuePayment> it = issuePayment
	// .getTransactionIssuePayment().iterator();
	// try {
	// while (it.hasNext()) {
	//
	// TransactionIssuePayment transactionIssuePayment = it
	// .next();
	//
	// if (transactionIssuePayment.getWriteCheck() != null) {
	//
	// Long writeCheckId = transactionIssuePayment
	// .getWriteCheck().getID();
	//
	// WriteCheck writeCheck;
	//
	// writeCheck = (WriteCheck) session.get(
	// WriteCheck.class, writeCheckId);
	//
	// if (writeCheck == null)
	// throw new DAOException(
	// DAOException.DATABASE_EXCEPTION,
	// new Exception(),
	// "WriteCheck Expected, But Found Null");
	//
	// transactionIssuePayment
	// .setWriteCheck(writeCheck);
	// } else {
	//
	// Long customerRefundId = transactionIssuePayment
	// .getCustomerRefund().getID();
	//
	// CustomerRefund customerRefund = (CustomerRefund) session
	// .get(CustomerRefund.class,
	// customerRefundId);
	//
	// if (customerRefund == null)
	// throw new DAOException(
	// DAOException.DATABASE_EXCEPTION,
	// new Exception(),
	// "Customer Refund Expected, But Found Null");
	// transactionIssuePayment
	// .setCustomerRefund(customerRefund);
	//
	// }
	//
	// }
	// session.save(issuePayment);
	// return Util.toClientObject(issuePayment,
	// ClientIssuePayment.class);
	// } catch (DAOException e) {
	// e.printStackTrace();
	// }
	// return null;
	//
	// }
	//
	// });
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientItem createItem(final long company, final ClientItem
	// clientItem)
	// throws DAOException {
	//
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template.find(
	// "from Item i where i.company.id = ? and i.name = ?",
	// new Object[] { company, clientItem.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientItem) createObject(company, new Item(), clientItem);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	// }
	//
	//
	// @Override
	// public ClientItemGroup createItemGroup(final long company,
	// final ClientItemGroup clientItemGroup) throws DAOException {
	//
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template.find(
	// "from ItemGroup i where i.company.id = ? and i.name = ?",
	// new Object[] { company, clientItemGroup.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientItemGroup) createObject(company, new ItemGroup(),
	// clientItemGroup);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientJournalEntry createJournalEntry(final long company,
	// final ClientJournalEntry clientJournalEntry) throws DAOException {
	// JournalEntry serverObj = new JournalEntry(
	// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY);
	// serverObj.setType(Transaction.TYPE_JOURNAL_ENTRY);
	// return (ClientJournalEntry) createObject(company, serverObj,
	// clientJournalEntry);
	// }
	//
	// @Override
	// public ClientMakeDeposit createMakeDeposit(final long company,
	// final ClientMakeDeposit clientMakeDeposit) throws DAOException {
	// MakeDeposit serverObj = new MakeDeposit();
	// serverObj.setType(Transaction.TYPE_MAKE_DEPOSIT);
	// return (ClientMakeDeposit) createObject(company, serverObj,
	// clientMakeDeposit);
	//
	// }
	//
	// @Override
	// public ClientPayBill createPayBill(final long company,
	// final ClientPayBill clientPayBill) throws DAOException {
	// PayBill serverObj = new PayBill();
	// serverObj.setType(Transaction.TYPE_PAY_BILL);
	// return (ClientPayBill) createObject(company, serverObj, clientPayBill);
	//
	// }
	//
	// @Override
	// public ClientPaySalesTax createPaySalesTax(final long company,
	// final ClientPaySalesTax clientPaySalesTax) throws DAOException {
	// PaySalesTax serverObj = new PaySalesTax();
	// serverObj.setType(Transaction.TYPE_PAY_SALES_TAX);
	// return (ClientPaySalesTax) createObject(company, serverObj,
	// clientPaySalesTax);
	//
	// }
	//
	//
	// @Override
	// public ClientPaymentTerms createPaymentTerms(final long company,
	// final ClientPaymentTerms clientPaymentTerms) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template.find(
	// "from PaymentTerms pt where pt.company.id=? and pt.name=?",
	// new Object[] { company, clientPaymentTerms.getName() });
	// if (list.size() > 0) {
	//
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	//
	// }
	//
	// return (ClientPaymentTerms) createObject(company,
	// new PaymentTerms(), clientPaymentTerms);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientPriceLevel createPriceLevel(final long company,
	// final ClientPriceLevel clientPriceLevel) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template
	// .find(
	// "from PriceLevel pl where pl.company.id = ? and pl.name = ?",
	// new Object[] { company, clientPriceLevel.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientPriceLevel) createObject(company, new PriceLevel(),
	// clientPriceLevel);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientPurchaseOrder createPurchaseOrder(final long company,
	// final ClientPurchaseOrder clientPurchaseOrder) throws DAOException {
	//
	// return (ClientPurchaseOrder) createObject(company, new PurchaseOrder(),
	// clientPurchaseOrder);
	// }
	//
	// @Override
	// public ClientReceivePayment createReceivePayment(final long company,
	// final ClientReceivePayment clientReceivePayment)
	// throws DAOException {
	//
	// ReceivePayment serverReceivePayment = new ReceivePayment();
	// serverReceivePayment.setType(Transaction.TYPE_RECEIVE_PAYMENT);
	// return (ClientReceivePayment) createObject(company,
	// serverReceivePayment, clientReceivePayment);
	//
	// }
	//
	// @Override
	// public ClientSalesOrder createSalesOrder(final long company,
	// final ClientSalesOrder clientSalesOrder) throws DAOException {
	//
	// return (ClientSalesOrder) createObject(company, new SalesOrder(),
	// clientSalesOrder);
	//
	// }
	//
	//
	// @Override
	// public ClientSalesPerson createSalesPerson(final long company,
	// final ClientSalesPerson clientSalesPerson) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template
	// .find(
	// "from SalesPerson sr where sr.company.id = ? and sr.firstName = ?",
	// new Object[] { company,
	// clientSalesPerson.getFirstName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// SalesPerson serverObject = new SalesPerson();
	// serverObject.setType(Payee.TYPE_EMPLOYEE);
	// return (ClientSalesPerson) createObject(company, serverObject,
	// clientSalesPerson);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientShippingMethod createShippingMethod(final long company,
	// final ClientShippingMethod clientShippingMethod)
	// throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template
	// .find(
	// "from ShippingMethod sm where sm.company.id= ? and sm.name = ?",
	// new Object[] { company,
	// clientShippingMethod.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientShippingMethod) createObject(company,
	// new ShippingMethod(), clientShippingMethod);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientShippingTerms createShippingTerms(final long company,
	// final ClientShippingTerms clientShippingTerms) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	//
	// List list = template
	// .find(
	// "from ShippingTerms st where st.company.id = ? and st.name = ?",
	// new Object[] { company,
	// clientShippingTerms.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientShippingTerms) createObject(company,
	// new ShippingTerms(), clientShippingTerms);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientTaxAgency createTaxAgency(final long company,
	// final ClientTaxAgency clientTaxAgency) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template
	// .find(
	// "from TaxAgency ta where ta.company.id = ? and ta.name = ?",
	// new Object[] { company, clientTaxAgency.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// TaxAgency serverObject = new TaxAgency();
	// serverObject.setType(Payee.TYPE_TAX_AGENCY);
	// return (ClientTaxAgency) createObject(company, serverObject,
	// clientTaxAgency);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientTaxCode createTaxCode(final long company,
	// final ClientTaxCode clientTaxCode) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template.find(
	// "from TaxCode tc where tc.company.id = ? and tc.name = ?",
	// new Object[] { company, clientTaxCode.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// final ClientTaxCode clientTaxCode2 = (ClientTaxCode) createObject(
	// company, new TaxCode(), clientTaxCode);
	// template.execute(new HibernateCallback() {
	//
	// @Override
	// public Object doInHibernate(Session session)
	// throws HibernateException, SQLException {
	// if (getCompanyFromSession(company, session)
	// .getAccountingType() == 1) {
	// ClientTaxGroup clientTaxGroup = new ClientTaxGroup();
	// clientTaxGroup.setName(clientTaxCode2.getName());
	// Set<ClientTaxCode> clientTaxCodes = new HashSet<ClientTaxCode>();
	// clientTaxCodes.add(clientTaxCode2);
	// clientTaxGroup.setTaxCodes(clientTaxCodes);
	//
	// try {
	// createObject(company, new TaxGroup(),
	// clientTaxGroup);
	// } catch (DAOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return null;
	// }
	// });
	//
	// return clientTaxCode2;
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientTaxGroup createTaxGroup(final long company,
	// final ClientTaxGroup clientTaxGroup) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template.find(
	// "from TaxGroup tg where tg.company.id = ? and tg.name = ?",
	// new Object[] { company, clientTaxGroup.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientTaxGroup) createObject(company, new TaxGroup(),
	// clientTaxGroup);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	//
	// @Override
	// public ClientTaxRates createTaxRates(final long company,
	// final ClientTaxRates clientTaxRates) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template.find(
	// "from TaxRates tr where tr.company.id = ? and tr.rate = ?",
	// new Object[] { company, clientTaxRates.getRate() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientTaxRates) createObject(company, new TaxRates(),
	// clientTaxRates);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientTransferFund createTransferFund(final long company,
	// final ClientTransferFund clientTransferFund) throws DAOException {
	//
	// TransferFund serverObject = new TransferFund();
	// serverObject.setType(Transaction.TYPE_TRANSFER_FUND);
	// return (ClientTransferFund) createObject(company, serverObject,
	// clientTransferFund);
	//
	// }
	//
	//
	// @Override
	// public ClientUnitOfMeasure createUnitOfMeasure(final long company,
	// final ClientUnitOfMeasure clientUnitOfMeasure) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template
	// .find(
	// "from UnitOfMeasure u where u.company.id = ? and u.name = ?",
	// new Object[] { company,
	// clientUnitOfMeasure.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientUnitOfMeasure) createObject(company,
	// new UnitOfMeasure(), clientUnitOfMeasure);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientUser createUser(final ClientUser clientUser)
	// throws DAOException {
	//
	// HibernateTemplate template = getHibernateTemplate();
	//
	// return (ClientUser) template.execute(new HibernateCallback() {
	//
	// @Override
	// public Object doInHibernate(Session arg0)
	// throws HibernateException, SQLException {
	//
	// User user = Util.toServerObject(new User(), clientUser, arg0);
	//
	// arg0.save(user);
	//
	// return Util.toClientObject(user, ClientUser.class);
	// }
	// });
	//
	// }
	//
	//
	// @Override
	// public ClientVendor createVendor(final long company,
	// final ClientVendor clientVendor) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template.find(
	// "from Vendor v where v.company.id = ? and v.name = ?",
	// new Object[] { company, clientVendor.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// Vendor serverVendor = new Vendor();
	// serverVendor.setType(Payee.TYPE_VENDOR);
	// serverVendor.setOpeningBalance(clientVendor.getBalance());
	// return (ClientVendor) createObject(company, serverVendor,
	// clientVendor);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientVendorCreditMemo createVendorCreditMemo(final long company,
	// final ClientVendorCreditMemo clientVendorCreditMemo)
	// throws DAOException {
	//
	// VendorCreditMemo serverObject = new VendorCreditMemo();
	// serverObject.setType(Transaction.TYPE_VENDOR_CREDIT_MEMO);
	// return (ClientVendorCreditMemo) createObject(company, serverObject,
	// clientVendorCreditMemo);
	// }
	//
	//
	// @Override
	// public ClientVendorGroup createVendorGroup(final long company,
	// final ClientVendorGroup clientVendorGroup) throws DAOException {
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	// List list = template
	// .find(
	// "from VendorGroup vg where vg.company.id = ? and vg.name = ?",
	// new Object[] { company, clientVendorGroup.getName() });
	//
	// if (list.size() > 0) {
	// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	// null));
	// }
	//
	// return (ClientVendorGroup) createObject(company, new VendorGroup(),
	// clientVendorGroup);
	//
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientWriteCheck createWriteCheck(final long company,
	// final ClientWriteCheck clientWriteCheck) throws DAOException {
	//
	// WriteCheck serverWriteCheck = new WriteCheck();
	// serverWriteCheck.setType(Transaction.TYPE_WRITE_CHECK);
	// return (ClientWriteCheck) createObject(company, serverWriteCheck,
	// clientWriteCheck);
	//
	// }
	//
	//
	// @Override
	// public Boolean deleteObject(final Class class1, final long Id)
	// throws DAOException {
	// try {
	//
	// HibernateTemplate template = getHibernateTemplate();
	// return (Boolean) template.execute(new HibernateCallback() {
	//
	// @Override
	// public Object doInHibernate(Session session)
	// throws HibernateException, SQLException {
	// Object obj = session.get(class1, Id);
	// if (obj != null) {
	// session.delete(obj);
	// return true;
	// }
	// return false;
	// }
	// });
	// } catch (DataAccessException e) {
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	//
	// }
	//
	// }
	//
	// protected Company getCompanyFromSession(long company, Session session) {
	// return (Company) session.get(Company.class, company);
	// }
	//
	// public IAccounterCore createObject(final long company,
	// final IAccounterServerCore serverObject, final IAccounterCore core)
	// throws DAOException {
	//
	// try {
	// HibernateTemplate template = getHibernateTemplate();
	//
	// return (IAccounterCore) template.execute(new HibernateCallback() {
	//
	// @Override
	// public Object doInHibernate(Session session)
	// throws HibernateException, SQLException {
	// IAccounterServerCore accounterClient = Util.toServerObject(
	// serverObject, core, session);
	// Company companyObj = getCompanyFromSession(company, session);
	// if (companyObj != null) {
	// Util.setCompany(accounterClient, companyObj);
	// session.save(accounterClient);
	// return Util.toClientObject(accounterClient, core
	// .getClass());
	// }
	// return null;
	// }
	//
	// });
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
	// }
	//
	// }
	//
	// @Override
	// public ClientCashPurchase createCashPurchase(long company,
	// ClientCashPurchase cashPurchase) throws DAOException {
	// CashPurchase serverObject = new CashPurchase();
	// serverObject.setType(Transaction.TYPE_CASH_PURCHASE);
	// return (ClientCashPurchase) createObject(company, serverObject,
	// cashPurchase);
	// }

}
