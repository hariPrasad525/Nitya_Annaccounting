package com.vimukti.accounter.services;

import java.util.ArrayList;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Bank;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Expense;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.FixedAsset;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.PayExpense;
import com.vimukti.accounter.core.PayTAX;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TaxRates;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.UnitOfMeasure;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorCreditMemo;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.core.WriteCheck;

public interface IAccounterDAOService {

	/**
	 * Core Related
	 */

	public Boolean checkLogin(String email, String password)
			throws DAOException;

	public Account getAccount(long companyId, String accountName)
			throws DAOException;

	public Account getAccount(long companyId, long accountId)
			throws DAOException;

	public ArrayList<Account> getAccounts(long companyId) throws DAOException;

	public ArrayList<Account> getAccounts(long companyId, int type)
			throws DAOException;

	public ArrayList<Bank> getBanks(long companyId) throws DAOException;

	public FiscalYear getFiscalYear(long companyId, long yearId)
			throws DAOException;

	public ArrayList<FiscalYear> getFiscalYears(long companyId)
			throws DAOException;

	public CreditRating getCreditRating(long companyId, String creditRatingName)
			throws DAOException;

	public CreditRating getCreditRating(long companyId, long creditRatingId)
			throws DAOException;

	public ArrayList<CreditRating> getCreditRatings(long companyId)
			throws DAOException;

	public Currency getCurrency(long companyId, String currencyName)
			throws DAOException;

	public Currency getCurrency(long companyId, long currencyId)
			throws DAOException;

	public ArrayList<Currency> getCurrencies(long companyId)
			throws DAOException;

	public ArrayList<Payee> getPayee(long companyId) throws DAOException;

	public Customer getCustomer(long companyId, String customerName)
			throws DAOException;

	public Customer getCustomer(long companyId, long customerId)
			throws DAOException;

	public ArrayList<Customer> getCustomers(long companyId) throws DAOException;

	public CustomerGroup getCustomerGroup(long companyId,
			String customerGroupName) throws DAOException;

	public CustomerGroup getCustomerGroup(long companyId, long customerGroupId)
			throws DAOException;

	public ArrayList<CustomerGroup> getCustomerGroups(long companyId)
			throws DAOException;

	public Item getItem(long companyId, String itemName) throws DAOException;

	public Item getItem(long companyId, long itemId) throws DAOException;

	public ArrayList<Item> getItems(long companyId) throws DAOException;

	public ItemGroup getItemGroup(long companyId, String itemGroupName)
			throws DAOException;

	public ItemGroup getItemGroup(long companyId, long itemGroupId)
			throws DAOException;

	public ArrayList<ItemGroup> getItemGroups(long companyId)
			throws DAOException;

	public PaymentTerms getPaymentTerms(long companyId, String paymentsTermsName)
			throws DAOException;

	public PaymentTerms getPaymentTerms(long companyId, long paymentsTermsId)
			throws DAOException;

	public ArrayList<PaymentTerms> getPaymentTerms(long companyId)
			throws DAOException;

	public PriceLevel getPriceLevel(long companyId, String priceLevelName)
			throws DAOException;

	public PriceLevel getPriceLevel(long companyId, long priceLevelId)
			throws DAOException;

	public ArrayList<PriceLevel> getPriceLevels(long companyId)
			throws DAOException;

	public SalesPerson getSalesPerson(long companyId, String salesPersonName)
			throws DAOException;

	public SalesPerson getSalesPerson(long companyId, long salesPersonId)
			throws DAOException;

	public ArrayList<SalesPerson> getSalesPersons(long companyId)
			throws DAOException;

	public ShippingMethod getShippingMethod(long companyId,
			String shippingMethodName) throws DAOException;

	public ShippingMethod getShippingMethod(long companyId,
			long shippingMethodId) throws DAOException;

	public ArrayList<ShippingMethod> getShippingMethods(long companyId)
			throws DAOException;

	public ShippingTerms getShippingTerms(long companyId,
			String shippingTermsName) throws DAOException;

	public ShippingTerms getShippingTerms(long companyId, long shippingTermsId)
			throws DAOException;

	public ArrayList<ShippingTerms> getShippingTerms(long companyId)
			throws DAOException;

	public TAXAgency getTaxAgency(long companyId, String taxAgencyName)
			throws DAOException;

	public TAXAgency getTaxAgency(long companyId, long taxAgencyID)
			throws DAOException;

	public ArrayList<TAXAgency> getTaxAgencies(long companyId)
			throws DAOException;

	public TAXCode getTaxCode(long companyId, String taxCodeName)
			throws DAOException;

	public TAXCode getTaxCode(long companyId, long taxCodeID)
			throws DAOException;

	public ArrayList<TAXCode> getTaxCodes(long companyId) throws DAOException;

	public TAXGroup getTaxGroup(long companyId, String taxGroupName)
			throws DAOException;

	public TAXGroup getTaxGroup(long companyId, long taxGroupID)
			throws DAOException;

	public ArrayList<TAXGroup> getTaxGroups(long companyId) throws DAOException;

	public TaxRates getTaxRates(long companyId, Double rate)
			throws DAOException;

	public TaxRates getTaxRates(long companyId, long taxRateID)
			throws DAOException;

	public ArrayList<TaxRates> getRates(long companyId) throws DAOException;

	public UnitOfMeasure getUnitOfMeasure(long companyId,
			String unitOfMeausreName) throws DAOException;

	public UnitOfMeasure getUnitOfMeasure(long companyId, long unitOfMeausreId)
			throws DAOException;

	public ArrayList<UnitOfMeasure> getUnitOfMeasures(long companyId)
			throws DAOException;

	public User getUser(long userID) throws DAOException;

	public User getUser(String email) throws DAOException;

	public ArrayList<User> getUsers(long companyId) throws DAOException;

	public Vendor getVendor(long companyId, String vendorName)
			throws DAOException;

	public Vendor getVendor(long companyId, long vendorId) throws DAOException;

	public ArrayList<Vendor> getVendors(long companyId) throws DAOException;

	public VendorGroup getVendorGroup(long companyId, String vendorGroupName)
			throws DAOException;

	public VendorGroup getVendorGroup(long companyId, long vendorGroupId)
			throws DAOException;

	public ArrayList<VendorGroup> getVendorGroups(long companyId)
			throws DAOException;

	public FixedAsset getFixedAsset(long companyId, long fixedAssetID)
			throws DAOException;

	/**
	 * Can Delete Objects
	 */

	public boolean canDeleteAccount(long account) throws DAOException;

	public boolean canDeleteCompany(long companyId) throws DAOException;

	public boolean canDeleteCreditRating(CreditRating creditRating)
			throws DAOException;

	public boolean canDeleteCustomer(Customer customer) throws DAOException;

	public boolean canDeleteCustomerGroup(CustomerGroup customerGroup)
			throws DAOException;

	public boolean canDeleteItem(Item item) throws DAOException;

	public boolean canDeleteItemGroup(ItemGroup itemGroup) throws DAOException;

	public boolean canDeletePaymentTerms(PaymentTerms paymentTerms)
			throws DAOException;

	public boolean canDeletePriceLevel(PriceLevel priceLevel)
			throws DAOException;

	public boolean canDeleteSalesPerson(SalesPerson salesPerson)
			throws DAOException;

	public boolean canDeleteShippingMethod(ShippingMethod shippingMethod)
			throws DAOException;

	public boolean canDeleteShippingTerms(ShippingTerms shippingTerms)
			throws DAOException;

	public boolean canDeleteTaxAgency(TAXAgency taxAgency) throws DAOException;

	public boolean canDeleteTaxCode(TAXCode taxCode) throws DAOException;

	public boolean canDeleteTaxGroup(TAXGroup taxGroup) throws DAOException;

	public boolean canDeleteTaxRates(TaxRates taxRates) throws DAOException;

	public boolean canDeleteUnitOfMeasure(UnitOfMeasure unitOfMeasure)
			throws DAOException;

	public boolean canDeleteUser(long user) throws DAOException;

	public boolean canDeleteVendor(Vendor vendor) throws DAOException;

	public boolean canDeleteVendorGroup(VendorGroup vendorGroup)
			throws DAOException;

	/**
	 * Transaction Related
	 */

	public CashPurchase getCashPurchase(long companyId, long cashPurchaseId)
			throws DAOException;

	public CashSales getCashSales(long companyId, long cashSalesId)
			throws DAOException;

	public CreditCardCharge getCreditCardCharge(long companyId,
			long creditCardChargeId) throws DAOException;

	public CustomerCreditMemo getCustomerCreditMemo(long companyId,
			long customerCreditMemoId) throws DAOException;

	public CustomerRefund getCustomerRefunds(long companyId,
			long customerRefundsId) throws DAOException;

	public EnterBill getEnterBill(long companyId, long enterBillId)
			throws DAOException;

	public Estimate getEstimate(long companyId, long estimateId)
			throws DAOException;

	public Invoice getInvoice(long companyId, long invoiceId)
			throws DAOException;

	// public IssuePayment getIssuePayment(long companyId, long issuePaymentId)
	// throws DAOException;

	public TransferFund getMakeDeposit(long companyId, long makeDepositId)
			throws DAOException;

	public PayBill getPayBill(long companyId, long payBillId)
			throws DAOException;

	public PurchaseOrder getPurchaseOrder(long companyId, long purchaseOrderId)
			throws DAOException;

	public ReceivePayment getReceivePayment(long companyId,
			long receivePaymentId) throws DAOException;

	public Estimate getSalesOrder(long companyId, long salesOrderId)
			throws DAOException;

	public VendorCreditMemo getVendorCreditMemo(long companyId,
			long vendorrCreditMemoId) throws DAOException;

	public WriteCheck getwriterCheck(long companyId, long writeCheckId)
			throws DAOException;

	// Credits and Payments Related

	public ArrayList<CreditsAndPayments> getCreditsAndPayments(long companyId)
			throws DAOException;

	public CreditsAndPayments getCreditAndPayment(long companyId, long id)
			throws DAOException;

	public ArrayList<CashSales> getCashSales(long companyId)
			throws DAOException;

	public ArrayList<Invoice> getInvoices(long companyId) throws DAOException;

	public ArrayList<CustomerCreditMemo> getCustomerCreditMemos(long companyId)
			throws DAOException;

	public ArrayList<CustomerRefund> getCustomerRefunds(long companyId)
			throws DAOException;

	public ArrayList<EnterBill> getEnterBills(long companyId)
			throws DAOException;

	public ArrayList<CashPurchase> getCashPurchases(long companyId)
			throws DAOException;

	public ArrayList<PayBill> getPayBills(long companyId) throws DAOException;

	public ArrayList<TransferFund> getMakeDeposits(long companyId)
			throws DAOException;

	public ArrayList<VendorCreditMemo> getVendorCreditMemos(long companyId)
			throws DAOException;

	public ArrayList<WriteCheck> getWriteChecks(long companyId)
			throws DAOException;

	public ArrayList<CreditCardCharge> getCreditCardCharges(long companyId)
			throws DAOException;

	public Company getCompany(long userId) throws DAOException;

	public ArrayList<ReceivePayment> getReceivePayments(long companyId)
			throws DAOException;

	public PayTAX getPaySalesTax(long companyId, long id) throws DAOException;

	public ArrayList<PayTAX> getPaySalesTaxes(long companyId)
			throws DAOException;

	public ArrayList<Company> getCompanies(long userId) throws DAOException;

	public Expense getExpense(long companyId, long expenseId)
			throws DAOException;

	public PayExpense getPayExpense(long companyId, long payExpenseId)
			throws DAOException;

	public ArrayList<Expense> getUnPaidExpense(long companyId)
			throws DAOException;

	public User getUserByCompany(long userId, long company) throws DAOException;

	public Company getCompany(String name) throws DAOException;

	public User getUserByDomainURL(String domainURL) throws DAOException;

}
