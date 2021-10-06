package com.nitya.accounter.web.client.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.portlet.PortletFactory;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.util.ChangeType;
import com.nitya.accounter.web.client.util.CoreEvent;

public class ClientCompany implements IAccounterCore {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int ACCOUNTING_TYPE_US = 0;
    public static final int ACCOUNTING_TYPE_UK = 1;
    public static final int ACCOUNTING_TYPE_INDIA = 2;
    public static final int ACCOUNTING_TYPE_OTHER = 3;

    public static final int FREE_USER = 0;
    public static final int ONE_USER = 1;
    public static final int TWO_USERS = 2;
    public static final int FIVE_USERS = 3;
    public static final int UNLIMITED_USERS = 4;

    public static final int TYPE_BASIC = 8;

    private Map<String, String> paymentMethods = new HashMap<String, String>();

    // int accountingType = 0;
    private int premiumType;

    private int transactionsCount;

    private String registrationNumber;

    private String companyEmail;

    private String companyEmailForCustomers;

    private long createdBy;

    boolean isConfigured;

    private long defaultMeasurement;

    private long defaultWarehouse;

    private String ein;

    private boolean bookKeeping;

    private int firstMonthOfFiscalYear;

    private int firstMonthOfIncomeTaxYear;

    private int taxForm;

    private long booksClosingDate;

    private int closingDateWarningType;

    private boolean enableAccountNumbers;

    private int customerType;

    private boolean enableAutoRecall;

    boolean restartSetupInterviews;

    private int fiscalYearStarting;

    private int industry;

    private long openingBalancesAccount;

    private long retainedEarningsAccount;

    private long otherCashIncomeAccount;

    private long otherCashExpenseAccount;

    private long pendingItemReceiptsAccount;

    private long cashDiscountsGiven;

    private long cashDiscountsTaken;

    private long taxLiabilityAccount;

    private long VATFiledLiabilityAccount;

    private long exchangeLossOrGainAccount;

    private long costOfGoodsSold;

    private long salariesPayableAccount;

    // String prepaidVATaccount;
    // String ECAcquisitionVATaccount;

    String bankAccountNo;

    String sortCode;

    String serviceItemDefaultIncomeAccount = "Cash Discount Given";

    String serviceItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

    String nonInventoryItemDefaultIncomeAccount = "Cash Discount Given";

    String nonInventoryItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

    ClientCompanyPreferences preferences = new ClientCompanyPreferences();

    private ArrayList<ClientAccount> accounts;

    private ArrayList<ClientJob> jobs;

    private ArrayList<ClientCustomer> customers;

    private ArrayList<ClientVendor> vendors;

    private ArrayList<ClientLocation> locations;
    // private ArrayList<ClientTaxAgency> taxAgencies;
    private ArrayList<ClientAdvertisement> advertisements;

    private ArrayList<ClientItem> items;

    private ArrayList<ClientCustomerGroup> customerGroups;

    private ArrayList<ClientVendorGroup> vendorGroups;

    private ArrayList<ClientPaymentTerms> paymentTerms;

    private ArrayList<ClientShippingTerms> shippingTerms;

    private ArrayList<ClientShippingMethod> shippingMethods;

    private ArrayList<ClientPriceLevel> priceLevels;

    private int transactionsLimit;

    private ArrayList<ClientItemGroup> itemGroups;

    private ArrayList<ClientInvoiceFrequencyGroup> invoiceFrequencyGroups;

    private ArrayList<ClientTAXGroup> taxGroups;

    private ArrayList<ClientPayTAX> payTaxs;

    private ArrayList<ClientCreditRating> creditRatings;

    private ArrayList<ClientSalesPerson> salesPersons;

    private ArrayList<ClientPayHead> payHeads;

    // private ArrayList<ClientTaxCode> taxCodes;

    private ArrayList<ClientTAXItemGroup> taxItemGroups;

    private ArrayList<ClientPayee> payees;

    private ArrayList<ClientFiscalYear> fiscalYears;

    private ArrayList<ClientBank> banks;

    private ArrayList<ClientFixedAsset> fixedAssets;

    // private ArrayList<ClientSellingOrDisposingFixedAsset>
    // sellingDisposedItems;

    private ArrayList<ClientTAXReturn> taxReturns;

    private ArrayList<ClientTAXAgency> taxAgencies;

    private ArrayList<ClientTAXCode> taxCodes;

    private ArrayList<ClientMeasurement> measurements;

    private ArrayList<ClientBrandingTheme> brandingTheme;

    private ArrayList<ClientWarehouse> warehouses;

    private ArrayList<ClientUserInfo> usersList;

    private ArrayList<ClientCurrency> currencies;

    private ArrayList<ClientQuantity> quantities;

    private ArrayList<ClientCustomField> customFields;

    private List<ClientChequeLayout> chequeLayouts;

    private List<ClientEmailAccount> emailAccounts;
    // private ArrayList<ClientTAXItemGroup> vatItemGroups;

    Set<ClientNominalCodeRange> nominalCodeRange = new HashSet<ClientNominalCodeRange>();

    private ClientFinanceDate transactionStartDate;

    private ClientFinanceDate transactionEndDate;

    private ClientAddress registeredAddress;

    private ClientTDSDeductorMasters tdsDeductor;

    private ClientTDSResponsiblePerson tdsResposiblePerson;

    private List<ClientMessageOrTask> messagesAndTasks;

    private List<ClientEmailTemplate> emailTemplates;

    private int version;

    private long cashDiscountAccount;

    // private ArrayList<ClientPayrollUnit> payrollUnits;

    private int itemCount;

    public ArrayList<ClientInvoiceFrequencyGroup> getInvoiceFrequencyGroups() {
        return invoiceFrequencyGroups;
    }

    public void setInvoiceFrequencyGroups(ArrayList<ClientInvoiceFrequencyGroup> invoiceFrequencyGroups) {
        this.invoiceFrequencyGroups = invoiceFrequencyGroups;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setTaxItemGroups(ArrayList<ClientTAXItemGroup> taxItemGroups) {
        this.taxItemGroups = taxItemGroups;
    }

    // public void setVatItemGroups(ArrayList<ClientTAXItemGroup> vatItemGroups)
    // {
    // this.vatItemGroups = vatItemGroups;
    // }
    //
    // public ArrayList<ClientTAXItemGroup> getVatItemGroups() {
    // return vatItemGroups;
    // }

    public void setTaxCodes(ArrayList<ClientTAXCode> TaxCodes) {
        this.taxCodes = TaxCodes;
    }

    public ArrayList<ClientTAXCode> getTaxCodes() {
        return taxCodes;
    }

    public ArrayList<ClientTAXCode> getActiveTaxCodes() {
        ArrayList<ClientTAXCode> activeTaxCodes = new ArrayList<ClientTAXCode>();
        for (ClientTAXCode taxCode : taxCodes) {
            if (taxCode.isActive())
                activeTaxCodes.add(taxCode);
        }
        return activeTaxCodes;
    }

    public void setTaxAgencies(ArrayList<ClientTAXAgency> taxAgencies) {
        this.taxAgencies = taxAgencies;
    }

    public ArrayList<ClientTAXAgency> getTaxAgencies() {
        return taxAgencies;
    }

    public ArrayList<ClientTAXAgency> getActiveTAXAgencies() {

        return Utility.filteredList(new ListFilter<ClientTAXAgency>() {

            @Override
            public boolean filter(ClientTAXAgency e) {
                return e.isActive();
            }
        }, this.taxAgencies);
        // List<ClientTAXAgency> activeTaxAgencies = new
        // ArrayList<ClientTAXAgency>();
        // for (ClientTAXAgency taxAgency : taxAgencies) {
        // if (taxAgency.isActive())
        // activeTaxAgencies.add(taxAgency);
        // }
        // return activeTaxAgencies;
    }

    /**
     * @return the vatReturns
     */
    public ArrayList<ClientTAXReturn> getTAXReturns() {
        return taxReturns;
    }

    /**
     * @param vatReturns the vatReturns to set
     */
    public void setTAXReturns(ArrayList<ClientTAXReturn> vatReturns) {
        this.taxReturns = vatReturns;
    }

    /**
     * @return the vatReturns
     */
    public ClientTAXReturn getVatReturn(long vatReturnID) {
        return Utility.getObject(this.taxReturns, vatReturnID);
    }

    /**
     * @return the vatAdjustments
     */
    public ArrayList<ClientTAXAdjustment> getTaxAdjustments() {
        return taxAdjustments;
    }

    /**
     * @param taxAdjustments the vatAdjustments to set
     */
    public void setTaxAdjustments(ArrayList<ClientTAXAdjustment> taxAdjustments) {
        this.taxAdjustments = taxAdjustments;
    }

    /**
     * @return the vatGroups
     */
    public ArrayList<ClientTAXGroup> getVatGroups() {
        return vatGroups;
    }

    /**
     * @param vatGroups the vatGroups to set
     */
    public void setVatGroups(ArrayList<ClientTAXGroup> vatGroups) {
        this.vatGroups = vatGroups;
    }

    // /**
    // * @return the vatItems
    // */
    // public ArrayList<ClientTAXItem> getVatItems() {
    // return vatItems;
    // }

    public Set<ClientNominalCodeRange> getNominalCodeRange() {
        return nominalCodeRange;
    }

    public long getPendingItemReceiptsAccount() {
        return pendingItemReceiptsAccount;
    }

    public void setPendingItemReceiptsAccount(long pendingItemReceiptsAccount) {
        this.pendingItemReceiptsAccount = pendingItemReceiptsAccount;
    }

    // public ArrayList<ClientTAXItem> getActiveVatItems() {
    // List<ClientTAXItem> activeVatItems = new ArrayList<ClientTAXItem>();
    // for (ClientTAXItem vatitem : vatItems) {
    // if (vatitem.isActive())
    // activeVatItems.add(vatitem);
    // }
    // return activeVatItems);
    // }

    public void setNominalCodeRange(Set<ClientNominalCodeRange> nominalCodeRange) {
        this.nominalCodeRange = nominalCodeRange;
    }

    // /**
    // *
    // * @param vatAgency
    // * @return
    // */
    // public ArrayList<ClientTAXItem> getVatItems(ClientTAXAgency taxAgency) {
    //
    // List<ClientTAXItem> vatItems = new ArrayList<ClientTAXItem>();
    //
    // if (this.vatItems == null) {
    // return vatItems;
    // }
    //
    // if (taxAgency == null) {
    // return vatItems;
    // }
    //
    // for (ClientTAXItem clientVATItem : getVatItems()) {
    //
    // if (clientVATItem.getTaxAgency().equals(taxAgency.getID())) {
    // vatItems.add(clientVATItem);
    // }
    // }
    //
    // return vatItems;
    // }

    // /**
    // * @param vatItems
    // * the vatItems to set
    // */
    // public void setVatItems(ArrayList<ClientTAXItem> vatItems) {
    // this.vatItems = vatItems;
    // }

    public ArrayList<ClientTAXItem> getActiveTaxItems() {

        return Utility.filteredList(new ListFilter<ClientTAXItem>() {

            @Override
            public boolean filter(ClientTAXItem e) {
                return e.isActive();
            }
        }, this.taxItems);
    }

    public ArrayList<ClientTAXItem> getActiveTaxItemsWithOutTDS() {

        return Utility.filteredList(new ListFilter<ClientTAXItem>() {

            @Override
            public boolean filter(ClientTAXItem e) {
                if (!e.isActive()) {
                    return false;
                }
                ClientTAXAgency taxAgency = getTaxAgency(e.getTaxAgency());
                if (taxAgency != null) {
                    return taxAgency.getTaxType() != ClientTAXAgency.TAX_TYPE_TDS;
                } else {
                    return false;
                }
            }
        }, this.taxItems);
    }

    public ArrayList<ClientTAXItem> getTaxItems() {
        return taxItems;
    }

    public ArrayList<ClientTAXItem> getTaxItems(ClientTAXAgency taxAgency) {
        ArrayList<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();

        if (this.taxItems == null) {
            return taxItems;
        }
        if (taxAgency == null) {
            return taxItems;
        }
        for (ClientTAXItem clientTaxItem : getTaxItems()) {
            if (clientTaxItem.getTaxAgency() == taxAgency.getID()) {
                taxItems.add(clientTaxItem);

            }
        }
        return taxItems;

    }

    // public ArrayList<ClientTaxItem> getTaxItems(ClientTaxAgency taxAgency) {
    // List<ClientTaxItem> taxItems = new ArrayList<ClientTaxItem>();
    //
    // if (this.taxItems == null) {
    // return taxItems;
    // }
    // if (taxAgency == null) {
    // return taxItems;
    // }
    // for (ClientTaxItem clientTaxItem : getTaxItems()) {
    // if (clientTaxItem.getTaxAgency().equals(taxAgency.getID())) {
    // taxItems.add(clientTaxItem);
    //
    // }
    // }
    // return taxItems;
    //
    // }

    public void setTaxItems(ArrayList<ClientTAXItem> taxItems) {
        this.taxItems = taxItems;
    }

    private ArrayList<ClientTAXAdjustment> taxAdjustments;

    private ArrayList<ClientTAXGroup> vatGroups;
    private ArrayList<ClientTAXItem> taxItems;

    // private List<ClientTAXItem> vatItems;
    private ArrayList<ClientVATReturnBox> vatReturnBoxes;
    public long id;

    private ClientUser loggedInUser;

    private ArrayList<ClientUnit> units;

    private ArrayList<ClientAccounterClass> accounterClasses = new ArrayList<ClientAccounterClass>();

    private CountryPreferences countryPreferences;

    private ArrayList<ClientEmployee> employees = new ArrayList<ClientEmployee>();

    private ArrayList<ClientPayStructureDestination> employeesGroups;

    // private List<ClientPayHead> payheads;
    //
    // private List<ClientEmployeeCategory> employeeCategories;
    //
    // private List<ClientAttendanceOrProductionType> attendanceProductionTypes
    // = new ArrayList<ClientAttendanceOrProductionType>();

    // private List<ClientTaxItem> taxItems;

    // List<ClientPayType> payTypes;

    public ArrayList<ClientCustomer> getCustomers() {
        return customers;
    }

    public ArrayList<ClientSalesPerson> getsalesPerson() {
        return salesPersons;
    }

    public ArrayList<ClientCustomer> getActiveCustomers() {
        return Utility.filteredList(new ListFilter<ClientCustomer>() {

            @Override
            public boolean filter(ClientCustomer e) {
                return e.isActive();
            }
        }, this.customers);
        // List<ClientCustomer> activeCustomers = new
        // ArrayList<ClientCustomer>();
        // for (ClientCustomer customer : customers) {
        // if (customer.isActive())
        // activeCustomers.add(customer);
        // }
        // return activeCustomers;
    }

    public ArrayList<ClientBank> getBanks() {
        return banks;
    }

    public void setBanks(ArrayList<ClientBank> banks) {
        this.banks = banks;
    }

    public ArrayList<ClientPayee> getPayees() {
        return payees;
    }

    public ArrayList<ClientPayee> getActivePayees() {

        return Utility.filteredList(new ListFilter<ClientPayee>() {

            @Override
            public boolean filter(ClientPayee e) {
                return e.isActive() && e.getType() != ClientPayee.TYPE_EMPLOYEE;
            }
        }, this.getPayees());
        // List<ClientPayee> activePayees = new ArrayList<ClientPayee>();
        // for (ClientPayee payee : getPayees()) {
        // if (payee.isActive())
        // activePayees.add(payee);
        // }
        // return activePayees);
    }

    public void setCustomers(ArrayList<ClientCustomer> customers) {
        this.customers = customers;
    }

    public ArrayList<ClientFiscalYear> getFiscalYears() {
        return fiscalYears;
    }

    public void setFiscalYears(ArrayList<ClientFiscalYear> fiscalYears) {
        this.fiscalYears = fiscalYears;
    }

    public ArrayList<ClientVendor> getVendors() {
        return vendors;
    }

    public ArrayList<ClientVendor> getActiveVendors() {

        return Utility.filteredList(new ListFilter<ClientVendor>() {

            @Override
            public boolean filter(ClientVendor e) {
                return e.isActive();
            }
        }, this.vendors);
        // List<ClientVendor> activeVendors = new ArrayList<ClientVendor>();
        // for (ClientVendor vendor : vendors) {
        // if (vendor.isActive())
        // activeVendors.add(vendor);
        // }
        // return activeVendors);
    }

    public void setVendors(ArrayList<ClientVendor> vendors) {
        this.vendors = vendors;
    }

    // public ArrayList<ClientTAXAgency> getTaxAgencies() {
    // return taxAgencies);
    // }
    //
    // public void setTaxAgencies(ArrayList<ClientTAXAgency> taxAgencies) {
    // this.taxAgencies = taxAgencies;
    // }

    public void setPreferences(ClientCompanyPreferences preferences) {
        this.preferences = preferences;
    }

    public ArrayList<ClientItem> getItems() {
        return items;
    }

    public ArrayList<ClientItem> getServiceItems() {

        return Utility.filteredList(new ListFilter<ClientItem>() {

            @Override
            public boolean filter(ClientItem e) {
                return e.getType() == ClientItem.TYPE_SERVICE;
            }
        }, this.getActiveItems());
        // ArrayList<ClientItem> serviceitems = new ArrayList<ClientItem>();
        // for (ClientItem item : getActiveItems()) {
        // if (item.getType() == ClientItem.TYPE_SERVICE)
        // serviceitems.add(item);
        //
        // }
        // return serviceitems;
    }

    public ArrayList<ClientItem> getProductItems() {

        return Utility.filteredList(new ListFilter<ClientItem>() {

            @Override
            public boolean filter(ClientItem e) {
                return e.getType() != ClientItem.TYPE_SERVICE;
            }
        }, this.getActiveItems());
        // List<ClientItem> productitems = new ArrayList<ClientItem>();
        // for (ClientItem item : getActiveItems()) {
        // if (item.getType() != ClientItem.TYPE_SERVICE)
        // productitems.add(item);
        //
        // }
        // return productitems);
    }

    public ArrayList<ClientItem> getInventoryItems() {

        return Utility.filteredList(new ListFilter<ClientItem>() {

            @Override
            public boolean filter(ClientItem e) {
                return e.getType() == ClientItem.TYPE_INVENTORY_PART;
            }
        }, this.getActiveItems());
    }

    public ArrayList<ClientItem> getInventoryAssemblyItems() {

        return Utility.filteredList(new ListFilter<ClientItem>() {

            @Override
            public boolean filter(ClientItem e) {
                return e.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY;
            }
        }, this.getActiveItems());
    }


    public ArrayList<ClientItem> getPurchaseItems() {
        return Utility.filteredList(new ListFilter<ClientItem>() {

            @Override
            public boolean filter(ClientItem e) {
                return e.isIBuyThisItem();
            }
        }, this.getAllItems());
        // List<ClientItem> purchaseitems = new ArrayList<ClientItem>();
        // for (ClientItem item : getAllItems()) {
        // if (item.isIBuyThisItem)
        // purchaseitems.add(item);
        //
        // }
        // return purchaseitems);
    }

    public ArrayList<ClientItem> getSalesItems() {

        return Utility.filteredList(new ListFilter<ClientItem>() {

            @Override
            public boolean filter(ClientItem e) {
                return e.isISellThisItem();
            }
        }, this.getAllItems());
        // List<ClientItem> salesitems = new ArrayList<ClientItem>();
        // for (ClientItem item : getAllItems()) {
        // if (item.isISellThisItem)
        // salesitems.add(item);
        //
        // }
        // return salesitems);
    }

    public ArrayList<ClientItem> getActiveItems() {

        return Utility.filteredList(new ListFilter<ClientItem>() {

            @Override
            public boolean filter(ClientItem e) {
                return e.isActive();
            }
        }, this.items);
        // List<ClientItem> activeItems = new ArrayList<ClientItem>();
        // for (ClientItem item : items) {
        // if (item.isActive())
        // activeItems.add(item);
        // }
        // return activeItems);
    }

    public ArrayList<ClientItem> getAllItems() {
        return items;
        // List<ClientItem> activeItems = new ArrayList<ClientItem>();
        // for (ClientItem item : items) {
        // // if (item.isActive())
        // activeItems.add(item);
        // }
        // return activeItems;
    }

    public void setItems(ArrayList<ClientItem> items) {
        this.items = items;
    }

    public ArrayList<ClientCustomerGroup> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(ArrayList<ClientCustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }

    public ArrayList<ClientVendorGroup> getVendorGroups() {
        return vendorGroups;
    }

    public void setVendorGroups(ArrayList<ClientVendorGroup> vendorGroups) {
        this.vendorGroups = vendorGroups;
    }

    public ArrayList<ClientPaymentTerms> getPaymentsTerms() {
        return paymentTerms;
    }

    public Map<String, String> getPaymentMethods() {

        return paymentMethods;
    }

    public void setPaymentsTerms(ArrayList<ClientPaymentTerms> paymentsTerms) {
        this.paymentTerms = paymentsTerms;
    }

    public ArrayList<ClientShippingTerms> getShippingTerms() {
        return shippingTerms;
    }

    public void setShippingTerms(ArrayList<ClientShippingTerms> shippingTerms) {
        this.shippingTerms = shippingTerms;
    }

    public ArrayList<ClientShippingMethod> getShippingMethods() {
        return shippingMethods;
    }

    public void setShippingMethods(
            ArrayList<ClientShippingMethod> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public ArrayList<ClientPriceLevel> getPriceLevels() {
        return priceLevels;
    }

    public ArrayList<ClientTAXItemGroup> getTaxItemGroups() {
        return taxItemGroups;
    }

    public void setPriceLevels(ArrayList<ClientPriceLevel> priceLevels) {
        this.priceLevels = priceLevels;
    }

    public ArrayList<ClientItemGroup> getItemGroups() {
        return itemGroups;
    }

    public void setItemGroups(ArrayList<ClientItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
    }

    // public ArrayList<ClientTaxCode> getTaxcodes() {
    // return taxCodes);
    // }
    // public ArrayList<ClientTaxCode> getActiveTaxCodes() {
    // List<ClientTaxCode> activeTaxCodes = new ArrayList<ClientTaxCode>();
    // for (ClientTaxCode taxCode : taxCodes) {
    // if (taxCode.getIsActive())
    // activeTaxCodes.add(taxCode);
    // }
    // return activeTaxCodes);
    // }
    //
    // public void setTaxcodes(ArrayList<ClientTaxCode> taxcodes) {
    // this.taxCodes = taxcodes;
    // }

    public ArrayList<ClientTAXGroup> getTaxGroups() {
        return taxGroups;
    }

    public void setTaxGroups(ArrayList<ClientTAXGroup> taxGroups) {
        this.taxGroups = taxGroups;
    }

    public ArrayList<ClientTAXAgency> gettaxAgencies() {
        return taxAgencies;
    }

    public ArrayList<ClientTAXAgency> getActiveTaxAgencies() {
        return Utility.filteredList(new ListFilter<ClientTAXAgency>() {

            @Override
            public boolean filter(ClientTAXAgency e) {
                return e.isActive();
            }
        }, this.taxAgencies);
        // List<ClientTAXAgency> activeTaxAgencies = new
        // ArrayList<ClientTAXAgency>();
        // for (ClientTAXAgency taxAgency : taxAgencies) {
        // if (taxAgency.isActive())
        // activeTaxAgencies.add(taxAgency);
        // }
        // return activeTaxAgencies);
    }

    public void settaxAgencies(ArrayList<ClientTAXAgency> taxAgencies) {
        this.taxAgencies = taxAgencies;
    }

    public ArrayList<ClientPayTAX> getPaySalesTaxs() {
        return payTaxs;
    }

    public void setPaySalesTaxs(ArrayList<ClientPayTAX> paySalesTaxs) {
        this.payTaxs = paySalesTaxs;
    }

    public ArrayList<ClientCreditRating> getCreditRatings() {
        return creditRatings;
    }

    public void setCreditRatings(ArrayList<ClientCreditRating> creditRatings) {
        this.creditRatings = creditRatings;
    }

    public ArrayList<ClientSalesPerson> getSalesPersons() {
        return salesPersons;
    }

    public ArrayList<ClientSalesPerson> getActiveSalesPersons() {

        return Utility.filteredList(new ListFilter<ClientSalesPerson>() {

            @Override
            public boolean filter(ClientSalesPerson e) {
                return e.isActive();
            }
        }, this.salesPersons);
        // List<ClientSalesPerson> activeSalesPersons = new
        // ArrayList<ClientSalesPerson>();
        // for (ClientSalesPerson salesPerson : salesPersons) {
        // if (salesPerson.isActive())
        // activeSalesPersons.add(salesPerson);
        // }
        // return activeSalesPersons);
    }

    public void setSalesPersons(ArrayList<ClientSalesPerson> salesPersons) {
        this.salesPersons = salesPersons;
    }

    // public void setTaxCodes(ArrayList<ClientTaxCode> taxCodes) {
    // this.taxCodes = taxCodes;
    // }

    public String getCompanyEmailForCustomers() {
        return companyEmailForCustomers;
    }

    public void setCompanyEmailForCustomers(String companyEmailForCustomers) {
        this.companyEmailForCustomers = companyEmailForCustomers;
    }

    // public void setContact(String contact) {
    // this.contact = contact;
    // }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public int getFirstMonthOfFiscalYear() {
        return firstMonthOfFiscalYear;
    }

    public void setFirstMonthOfFiscalYear(int firstMonthOfFiscalYear) {
        this.firstMonthOfFiscalYear = firstMonthOfFiscalYear;
    }

    public int getFirstMonthOfIncomeTaxYear() {
        return firstMonthOfIncomeTaxYear;
    }

    public void setFirstMonthOfIncomeTaxYear(int firstMonthOfIncomeTaxYear) {
        this.firstMonthOfIncomeTaxYear = firstMonthOfIncomeTaxYear;
    }

    public int getTaxForm() {
        return taxForm;
    }

    public void setTaxForm(int taxForm) {
        this.taxForm = taxForm;
    }

    public long getBooksClosingDate() {
        return booksClosingDate;
    }

    public void setBooksClosingDate(long booksClosingDate) {
        this.booksClosingDate = booksClosingDate;
    }

    public int getClosingDateWarningType() {
        return closingDateWarningType;
    }

    public void setClosingDateWarningType(int closingDateWarningType) {
        this.closingDateWarningType = closingDateWarningType;
    }

    public boolean isEnableAccountNumbers() {
        return enableAccountNumbers;
    }

    public void setEnableAccountNumbers(boolean enableAccountNumbers) {
        this.enableAccountNumbers = enableAccountNumbers;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public boolean isEnableAutoRecall() {
        return enableAutoRecall;
    }

    public void setEnableAutoRecall(boolean enableAutoRecall) {
        this.enableAutoRecall = enableAutoRecall;
    }

    public boolean isRestartSetupInterviews() {
        return restartSetupInterviews;
    }

    public void setRestartSetupInterviews(boolean restartSetupInterviews) {
        this.restartSetupInterviews = restartSetupInterviews;
    }

    public int getFiscalYearStarting() {
        return fiscalYearStarting;
    }

    public void setFiscalYearStarting(int fiscalYearStarting) {
        this.fiscalYearStarting = fiscalYearStarting;
    }

    public long getOpeningBalancesAccount() {
        return openingBalancesAccount;
    }

    public void setOpeningBalancesAccountId(long openingBalancesAccount) {
        this.openingBalancesAccount = openingBalancesAccount;
    }

    public long getRetainedEarningsAccount() {
        return retainedEarningsAccount;
    }

    public void setRetainedEarningsAccount(long retainedEarningsAccount) {
        this.retainedEarningsAccount = retainedEarningsAccount;
    }

    public long getOtherCashIncomeAccount() {
        return otherCashIncomeAccount;
    }

    public void setOtherCashIncomeAccount(long otherCashIncomeAccount) {
        this.otherCashIncomeAccount = otherCashIncomeAccount;
    }

    public long getOtherCashExpenseAccount() {
        return otherCashExpenseAccount;
    }

    public void setOtherCashExpenseAccount(long otherCashExpenseAccount) {
        this.otherCashExpenseAccount = otherCashExpenseAccount;
    }

    @Override
    public String getDisplayName() {
        return this.preferences.getTradingName();
    }

    @Override
    public String getName() {

        return this.preferences.getTradingName();
    }

    /**
     * Called Later at Process Command
     *
     * @param accounts
     */

    /**
     * @return the accounts
     */

    public void setAccounts(ArrayList<ClientAccount> accounts) {
        this.accounts = accounts;
    }

    public ArrayList<ClientAccount> getAccounts() {
        return accounts;
    }

    public ArrayList<ClientAccount> getActiveAccounts() {

        return Utility.filteredList(new ListFilter<ClientAccount>() {

            @Override
            public boolean filter(ClientAccount e) {
                return e.getIsActive();
            }
        }, this.accounts);
        // List<ClientAccount> activeAccounts = new ArrayList<ClientAccount>();
        // for (ClientAccount account : accounts) {
        // if (account.getIsActive())
        // activeAccounts.add(account);
        // }
        // return activeAccounts);
    }

    public ClientCompanyPreferences getPreferences() {
        return this.preferences;
    }

    public String getTradingName() {
        return this.preferences.getLegalName();
    }

    public String getTaxId() {

        return this.preferences.getTaxId();
    }

    public String getFax() {
        return this.preferences.getFax();
    }

    public String getPhone() {
        return this.preferences.getPhone();
    }

    public String getWebSite() {
        return this.preferences.getWebSite();
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getCompanyEmail() {
        return this.preferences.getCompanyEmail();
    }

    public void setTradingName(String tradingName) {
        this.preferences.setTradingName(tradingName);
    }

    public void setLegalName(String stringValue) {
        this.preferences.setLegalName(stringValue);
    }

    public String gettimezone() {
        return preferences.getTimezone();
    }

    public void settimezone(String timezone) {
        this.preferences.setTimezone(timezone);
    }

    public void setPhone(String stringValue) {
        this.preferences.setPhone(stringValue);
    }

    public void setCompanyEmail(String stringValue) {
        this.companyEmail = stringValue;
    }

    public void setTaxId(String stringValue) {
        this.preferences.setTaxId(stringValue);
    }

    public void setFax(String stringValue) {
        this.preferences.setFax(stringValue);
    }

    public void setWebSite(String stringValue) {
        this.preferences.setWebSite(stringValue);
    }

    public void setIndustry(int typeBasic) {
        this.industry = typeBasic;

    }

    public int getIndustry() {
        return this.industry;
    }

    public ClientPaymentTerms getPaymentTerms(long paymentTermsId) {

        return Utility.getObject(this.paymentTerms, paymentTermsId);
    }

    public ClientPayTAX getPaySalesTax(long paysalesTaxId) {

        return Utility.getObject(this.payTaxs, paysalesTaxId);
    }

    public ClientCustomerGroup getCustomerGroup(long customerGroupId) {

        return Utility.getObject(this.customerGroups, customerGroupId);
    }

    public ClientVendorGroup getVendorGroup(long vendorGroupId) {

        return Utility.getObject(this.vendorGroups, vendorGroupId);
    }

    public ClientShippingMethod getShippingMethod(long shippingMethodId) {

        return Utility.getObject(this.shippingMethods, shippingMethodId);
    }

    public ClientShippingTerms getShippingTerms(long shippingTermsId) {

        return Utility.getObject(this.shippingTerms, shippingTermsId);
    }

    public ClientItemGroup getItemGroup(long itemGroupId) {

        return Utility.getObject(this.itemGroups, itemGroupId);
    }

    public ClientInvoiceFrequencyGroup getInvoiceFrequencyGroup(long invoiceFrequencyGroupId) {

        return Utility.getObject(this.invoiceFrequencyGroups, invoiceFrequencyGroupId);
    }

    public ClientCreditRating getCreditRating(long creditRatingId) {

        return Utility.getObject(this.creditRatings, creditRatingId);
    }

    public ClientTAXAgency getTaxAgency(long taxAgencyId) {

        return Utility.getObject(this.taxAgencies, taxAgencyId);
    }

    // public ClientTaxCode getTaxCode(long taxCodeId) {
    //
    // return Utility.getObject(this.taxCodes, taxCodeId);
    // }

    public ClientCustomer getCustomer(long customerId) {

        return Utility.getObject(this.customers, customerId);
    }

    public ClientPayHead getPayHead(long payHeadID) {
        return Utility.getObject(this.payHeads, payHeadID);
    }

    public ClientEmployee getEmployee(long employeeID) {
        return Utility.getObject(this.getEmployees(), employeeID);
    }

    public ClientCurrency getCurrency(long currencyId) {
        ClientCurrency object = Utility.getObject(this.currencies, currencyId);
        return object;
    }

    public ClientPayee getPayee(long id) {
        return Utility.getObject(this.payees, id);
    }

    public ClientCustomer getCustomerByName(String customerName) {
        return Utility.getObjectByName(this.customers, customerName);
    }

    public ClientPayHead getPayHeadByName(String payHeadName) {
        return Utility.getObjectByName(this.payHeads, payHeadName);
    }

    public ClientEmployee getEmployeeByName(String employee) {
        return Utility.getObjectByName(this.getEmployees(), employee);
    }

    public ClientBank getBank(long bankId) {

        return Utility.getObject(this.banks, bankId);
    }

    public ClientVendor getVendor(long vendorId) {

        return Utility.getObject(this.vendors, vendorId);
    }

    public ClientVendor getVendorByName(String vendorName) {

        return Utility.getObjectByName(this.vendors, vendorName);
    }

    public ClientItem getItem(long itemId) {

        return Utility.getObject(this.items, itemId);
    }

    public ClientAccount getAccount(long accountId) {

        return Utility.getObject(this.accounts, accountId);
    }

    public ClientAccount getAccountByName(String accountName) {

        return Utility.getObjectByName(this.accounts, accountName);
    }

    public ClientTAXGroup getTaxGroup(long taxGroupId) {

        return Utility.getObject(this.taxGroups, taxGroupId);
    }

    public ClientTAXItem getTaxItem(long taxGroupId) {

        return Utility.getObject(this.taxItems, taxGroupId);
    }

    public ClientSalesPerson getSalesPerson(long salesPersonId) {

        return Utility.getObject(this.salesPersons, salesPersonId);
    }

    public ClientFixedAsset getFixedAsset(long fixedAssetID) {

        return Utility.getObject(this.fixedAssets, fixedAssetID);
    }

    public ClientEmailTemplate getEmailTemplate(long templateId) {

        return Utility.getObject(this.emailTemplates, templateId);
    }

    public ClientTAXCode getTAXCode(long taxCodeId) {

        return Utility.getObject(this.taxCodes, taxCodeId);
    }

    private ClientQuantity getQuantity(long quantityId) {
        return Utility.getObject(this.quantities, quantityId);
    }

    // public ClientTAXItemGroup getVATItemGroup(long vatItemGrpId) {
    //
    // return Utility.getObject(this.vatItemGroups, vatItemGrpId);
    // }

    public ClientTAXItemGroup getTAXItemGroup(long taxItemGrpId) {
        return Utility.getObject(this.taxItemGroups, taxItemGrpId);
    }

    public ClientPriceLevel getPriceLevel(long priceLevelId) {
        return Utility.getObject(this.priceLevels, priceLevelId);
    }

    public ClientTAXItem getTAXItem(long id) {
        return Utility.getObject(this.taxItems, id);
    }

    // public ClientTAXAgency getTaxAgency(long id) {
    // return Utility.getObject(this.taxAgencies, id);
    // }

    public ClientVATReturnBox getVatReturnBox(long id) {
        return Utility.getObject(this.vatReturnBoxes, id);
    }

    public ClientCustomField getClientCustomField(long id) {
        return Utility.getObject(this.customFields, id);
    }

    // public ClientFiscalYear getFixelYear(long id) {
    // return Utility.getObject(this.fiscalYears, id);
    // }

    public ClientBrandingTheme getBrandingTheme(long id) {
        return Utility.getObject(this.brandingTheme, id);
    }

    public ClientMeasurement getMeasurement(long measurementId) {
        return Utility.getObject(this.measurements, measurementId);
    }

    public ClientEmailAccount getEmailAccount(long emailAccount) {
        return Utility.getObject(this.emailAccounts, emailAccount);
    }

    public ClientWarehouse getWarehouse(long id) {
        return Utility.getObject(this.warehouses, id);
    }

    public ClientTAXAgency getVatAgencyByName(String name) {
        return Utility.getObjectByName(this.taxAgencies, name);
    }

    public void deleteSalesPerson(long salesPersonId) {
        ClientSalesPerson person = this.getSalesPerson(salesPersonId);
        if (person != null) {
            this.salesPersons.remove(person);
            fireEvent(new CoreEvent<ClientSalesPerson>(ChangeType.DELETE,
                    person));
        }
    }

    public void deletePaymentTerms(long paymentTermsId) {
        ClientPaymentTerms paymentterm = this.getPaymentTerms(paymentTermsId);
        if (paymentterm != null) {
            this.paymentTerms.remove(paymentterm);
            fireEvent(new CoreEvent<ClientPaymentTerms>(ChangeType.DELETE,
                    paymentterm));
        }
    }

    public void deletePriceLevel(long priceLevelId) {
        ClientPriceLevel priceLevel = this.getPriceLevel(priceLevelId);
        if (priceLevel != null) {
            this.priceLevels.remove(priceLevel);
            fireEvent(new CoreEvent<ClientPriceLevel>(ChangeType.DELETE,
                    priceLevel));
        }
    }

    public void deleteCustomerGroup(long customerGroup) {
        ClientCustomerGroup customergroup = this
                .getCustomerGroup(customerGroup);
        if (customergroup != null) {
            this.customerGroups.remove(customergroup);
            fireEvent(new CoreEvent<ClientCustomerGroup>(ChangeType.DELETE,
                    customergroup));
        }
    }

    public void deleteVendorGroup(long vendorGroup) {
        ClientVendorGroup vendorgroup = this.getVendorGroup(vendorGroup);
        if (vendorgroup != null) {
            this.vendorGroups.remove(vendorgroup);
            fireEvent(new CoreEvent<ClientVendorGroup>(ChangeType.DELETE,
                    vendorgroup));
        }
    }

    public void deleteShippingMethod(long shippingMethod) {
        ClientShippingMethod clientShippingMethod = this
                .getShippingMethod(shippingMethod);
        if (clientShippingMethod != null) {
            this.shippingMethods.remove(clientShippingMethod);
            fireEvent(new CoreEvent<ClientShippingMethod>(ChangeType.DELETE,
                    clientShippingMethod));
        }
    }

    public void deleteShippingTerms(long shippingTerm) {
        ClientShippingTerms clientShippingTerms = this
                .getShippingTerms(shippingTerm);
        if (clientShippingTerms != null) {
            this.shippingTerms.remove(clientShippingTerms);
            fireEvent(new CoreEvent<ClientShippingTerms>(ChangeType.DELETE,
                    clientShippingTerms));
        }
    }

    public void deleteItemGroup(long itemGroup) {
        ClientItemGroup clientItemGroup = this.getItemGroup(itemGroup);
        if (clientItemGroup != null) {
            this.itemGroups.remove(clientItemGroup);
            fireEvent(new CoreEvent<ClientItemGroup>(ChangeType.DELETE,
                    clientItemGroup));
        }
    }

    public void deleteCreditRating(long creditRating) {
        ClientCreditRating clientCreditRating = this
                .getCreditRating(creditRating);
        if (clientCreditRating != null) {
            this.creditRatings.remove(clientCreditRating);
            fireEvent(new CoreEvent<ClientCreditRating>(ChangeType.DELETE,
                    clientCreditRating));
        }
    }

    // public void deleteTaxAgency(long taxAgency) {
    // this.taxAgencies.remove(this.getTaxAgency(taxAgency));
    // }

    // public void deleteTaxCode(long taxCode) {
    // this.taxCodes.remove(this.getTaxCode(taxCode));
    // }

    public void deleteCustomer(long customerId) {

        ClientCustomer clientCustomer = this.getCustomer(customerId);
        if (clientCustomer != null) {
            this.customers.remove(clientCustomer);
            fireEvent(new CoreEvent<ClientCustomer>(ChangeType.DELETE,
                    clientCustomer));
        }
    }

    public void deletePayee(long payeeId) {

        ClientPayee clientPayee = this.getPayee(payeeId);
        if (clientPayee != null) {
            this.payees.remove(clientPayee);
            fireEvent(new CoreEvent<ClientPayee>(ChangeType.DELETE, clientPayee));
        }
    }

    public void deleteVendor(long vendorId) {
        ClientVendor clientVendor = this.getVendor(vendorId);
        if (clientVendor != null) {
            this.vendors.remove(clientVendor);
            fireEvent(new CoreEvent<ClientVendor>(ChangeType.DELETE,
                    clientVendor));
        }
    }

    public void deleteItem(long itemId) {
        ClientItem clientItem = this.getItem(itemId);
        if (clientItem != null) {
            this.items.remove(clientItem);
            fireEvent(new CoreEvent<ClientItem>(ChangeType.DELETE, clientItem));
        }
    }

    public void deleteAccount(long accountId) {
        ClientAccount account = this.getAccount(accountId);
        if (account != null) {
            this.accounts.remove(account);
            fireEvent(new CoreEvent<ClientAccount>(ChangeType.DELETE, account));
        }
    }

    private void fireEvent(CoreEvent<?> event) {
        Accounter.getEventBus().fireEvent(event);
    }

    private void deleteLocation(long locationId) {
        ClientLocation location = this.getLocation(locationId);
        if (location != null) {
            this.locations.remove(location);
            fireEvent(new CoreEvent<ClientLocation>(ChangeType.DELETE, location));
        }
    }

    private void deleteCurrency(long currencyId) {
        ClientCurrency currency = this.getCurrency(currencyId);
        if (currency != null) {
            this.currencies.remove(currency);
            fireEvent(new CoreEvent<ClientCurrency>(ChangeType.DELETE, currency));
        }
    }

    public ClientLocation getLocation(long locationId) {
        return Utility.getObject(this.locations, locationId);
    }

    public ClientJob getjob(long jobId) {
        return Utility.getObject(this.jobs, jobId);
    }

    public ClientAdvertisement getAdvertisement(long advertiseId) {
        return Utility.getObject(this.advertisements, advertiseId);
    }

    public void deleteTaxGroup(long taxGroup) {
        ClientTAXGroup clientTaxGroup = this.getTaxGroup(taxGroup);
        if (clientTaxGroup != null) {
            this.taxGroups.remove(clientTaxGroup);
            fireEvent(new CoreEvent<ClientTAXGroup>(ChangeType.DELETE,
                    clientTaxGroup));
        }
    }

    public void deleteBank(long bankId) {
        ClientBank clientBank = this.getBank(bankId);
        if (clientBank != null) {
            this.banks.remove(clientBank);
            fireEvent(new CoreEvent<ClientBank>(ChangeType.DELETE, clientBank));
        }
    }

    public void deleteTaxCode(long taxCode) {
        ClientTAXCode clientTaxCode = this.getTAXCode(taxCode);
        if (clientTaxCode != null) {
            this.taxCodes.remove(clientTaxCode);
            fireEvent(new CoreEvent<ClientTAXCode>(ChangeType.DELETE,
                    clientTaxCode));
        }
    }

    public void deleteQuantity(long quantity) {
        ClientQuantity clientQuantity = this.getQuantity(quantity);
        if (clientQuantity != null) {
            this.quantities.remove(clientQuantity);
            fireEvent(new CoreEvent<ClientQuantity>(ChangeType.DELETE,
                    clientQuantity));
        }
    }

    public void deleteMeasurement(long measurement) {
        ClientMeasurement clientMeasurement = this.getMeasurement(measurement);
        if (clientMeasurement != null) {
            this.measurements.remove(clientMeasurement);
            fireEvent(new CoreEvent<ClientMeasurement>(ChangeType.DELETE,
                    clientMeasurement));
        }
    }

    public void deleteEmailAccount(long account) {
        ClientEmailAccount emailAccount = this.getEmailAccount(account);
        if (emailAccount != null) {
            this.emailAccounts.remove(emailAccount);
            fireEvent(new CoreEvent<ClientEmailAccount>(ChangeType.DELETE,
                    emailAccount));
        }
    }

    public void deleteWarehouse(long warehouse) {
        ClientWarehouse clientWarehouse = this.getWarehouse(warehouse);
        if (clientWarehouse != null) {
            this.warehouses.remove(clientWarehouse);
            fireEvent(new CoreEvent<ClientWarehouse>(ChangeType.DELETE,
                    clientWarehouse));
        }
    }

    // public void deleteVatGroup(long vatGroup) {
    // this.vatGroups.remove(this.getVATItem(vatGroup));
    // }

    public void deleteTaxItem(long taxItem) {
        ClientTAXItem clientTaxItem = this.getTaxItem(taxItem);
        if (clientTaxItem != null) {
            this.taxItems.remove(clientTaxItem);
            fireEvent(new CoreEvent<ClientTAXItem>(ChangeType.DELETE,
                    clientTaxItem));
        }
    }

    public void deleteTaxAgency(long taxAgencyId) {
        ClientTAXAgency clientTaxAgency = this.getTaxAgency(taxAgencyId);
        if (clientTaxAgency != null) {
            this.taxAgencies.remove(clientTaxAgency);
            fireEvent(new CoreEvent<ClientTAXAgency>(ChangeType.DELETE,
                    clientTaxAgency));
        }
    }

    public void deleteVAtReturn(long vatReturnId) {
        ClientTAXReturn clientVatReturn = this.getVatReturn(vatReturnId);
        if (clientVatReturn != null) {
            this.taxReturns.remove(clientVatReturn);
            fireEvent(new CoreEvent<ClientTAXReturn>(ChangeType.DELETE,
                    clientVatReturn));
        }
    }

    // public void deleteFixelYear(long fixelYearId) {
    //
    // ClientFiscalYear clientFiscalYear = this.getFixelYear(fixelYearId);
    // if (clientFiscalYear != null) {
    // this.fiscalYears.remove(clientFiscalYear);
    // fireEvent(new CoreEvent<ClientFiscalYear>(ChangeType.DELETE,
    // clientFiscalYear));
    // }
    // }

    public void deleteBrandingTheme(long themeId) {
        ClientBrandingTheme clientBrandingTheme = this
                .getBrandingTheme(themeId);
        if (clientBrandingTheme != null) {
            this.brandingTheme.remove(clientBrandingTheme);
            fireEvent(new CoreEvent<ClientBrandingTheme>(ChangeType.DELETE,
                    clientBrandingTheme));
        }
    }

    /**
     * @param accounterCoreObject
     */

    public void processCommand(Serializable accounterCoreObject) {

        if (accounterCoreObject == null)
            return;

        if (accounterCoreObject instanceof AccounterCommand) {
            AccounterCommand command = (AccounterCommand) accounterCoreObject;
            switch (command.command) {
                case AccounterCommand.CREATION_SUCCESS:
                case AccounterCommand.UPDATION_SUCCESS:
                    processUpdateOrCreateObject(command.data);
                    break;
                case AccounterCommand.DELETION_SUCCESS:
                    processDeleteObject(command.getObjectType(), command.getID());
                    return;
                default:
                    break;
            }

        }

    }

    @Override
    public AccounterCoreType getObjectType() {

        return AccounterCoreType.COMPANY;
    }

    // public int getAccountingType() {
    // return accountingType;
    // }

    // public void setAccountingType(int accountingType) {
    // this.accountingType = accountingType;
    // }

    public String getServiceItemDefaultIncomeAccount() {
        return serviceItemDefaultIncomeAccount;
    }

    public void setServiceItemDefaultIncomeAccount(
            String serviceItemDefaultIncomeAccount) {
        this.serviceItemDefaultIncomeAccount = serviceItemDefaultIncomeAccount;
    }

    public String getServiceItemDefaultExpenseAccount() {
        return serviceItemDefaultExpenseAccount;
    }

    public void setServiceItemDefaultExpenseAccount(
            String serviceItemDefaultExpenseAccount) {
        this.serviceItemDefaultExpenseAccount = serviceItemDefaultExpenseAccount;
    }

    public String getNonInventoryItemDefaultIncomeAccount() {
        return nonInventoryItemDefaultIncomeAccount;
    }

    public void setNonInventoryItemDefaultIncomeAccount(
            String nonInventoryItemDefaultIncomeAccount) {
        this.nonInventoryItemDefaultIncomeAccount = nonInventoryItemDefaultIncomeAccount;
    }

    public String getNonInventoryItemDefaultExpenseAccount() {
        return nonInventoryItemDefaultExpenseAccount;
    }

    public void setNonInventoryItemDefaultExpenseAccount(
            String nonInventoryItemDefaultExpenseAccount) {
        this.nonInventoryItemDefaultExpenseAccount = nonInventoryItemDefaultExpenseAccount;
    }

    public ArrayList<ClientAccount> getAccounts(final int accountType) {

        return Utility.filteredList(new ListFilter<ClientAccount>() {

            @Override
            public boolean filter(ClientAccount e) {
                if (e != null) {
                    return e.type == accountType;
                }
                return false;
            }
        }, getAccounts());
        // List<ClientAccount> accounts = new ArrayList<ClientAccount>();
        //
        // for (ClientAccount clientAccount : getAccounts()) {
        // if (clientAccount != null)
        // if (clientAccount.type == accountType) {
        // accounts.add(clientAccount);
        // }
        //
        // }
        //
        // return accounts;
    }

    public ArrayList<ClientAccount> getActiveBankAccounts(final int accountType) {

        return Utility.filteredList(new ListFilter<ClientAccount>() {

            @Override
            public boolean filter(ClientAccount e) {
                return e.getIsActive() && e.type == accountType;
            }
        }, getAccounts());
        // List<ClientAccount> activeAccounts = new ArrayList<ClientAccount>();
        // for (ClientAccount account : getAccounts()) {
        // if (account.getIsActive() && account.type == accountType)
        // activeAccounts.add(account);
        // }
        // return activeAccounts;
    }

    public void processUpdateOrCreateObject(IAccounterCore accounterCoreObject) {

        // try {

        if (accounterCoreObject != null)
            switch (accounterCoreObject.getObjectType()) {

                case ACCOUNT:
                case BANK_ACCOUNT:

                    ClientAccount account = (ClientAccount) accounterCoreObject;

                    // Utility.updateClientList(account, accounts);

                    ClientAccount existObj = Utility.getObject(accounts,
                            account.getID());
                    if (existObj != null) {
                        if (account.getNumber().equals(existObj.getNumber())) {
                            accounts.remove(existObj);

                            UIUtils.updateAccountsInSortedOrder(accounts, account);
                        }
                        fireEvent(new CoreEvent<ClientAccount>(ChangeType.CHANGE,
                                account));
                    } else {

                        UIUtils.updateAccountsInSortedOrder(accounts, account);
                        fireEvent(new CoreEvent<ClientAccount>(ChangeType.ADD,
                                account));
                    }

                    break;

                case CUSTOMER:

                    ClientCustomer customer = (ClientCustomer) accounterCoreObject;

                    Utility.updateClientList(customer, customers);
                    Utility.updateClientList(customer, payees);

                    break;

                case VENDOR:

                    ClientVendor vendor = (ClientVendor) accounterCoreObject;

                    Utility.updateClientList(vendor, vendors);
                    Utility.updateClientList(vendor, payees);

                    break;

                case TAXAGENCY:

                    ClientTAXAgency taxAgency = (ClientTAXAgency) accounterCoreObject;

                    Utility.updateClientList(taxAgency, taxAgencies);
                    Utility.updateClientList(taxAgency, payees);

                    break;

                case ITEM:

                    ClientItem item = (ClientItem) accounterCoreObject;

                    Utility.updateClientList(item, items);

                    break;

                case PAY_HEAD:
                case ATTENDANCE_PAY_HEAD:
                case COMPUTATION_PAY_HEAD:
                case FLATRATE_PAY_HEAD:
                case PRODUCTION_PAY_HEAD:
                case USERDEFINED_PAY_HEAD:
                    ClientPayHead payHead = (ClientPayHead) accounterCoreObject;
                    Utility.updateClientList(payHead, payHeads);
                    break;

                case TAX_GROUP:

                    ClientTAXGroup taxGroup = (ClientTAXGroup) accounterCoreObject;
                    // Utility.updateClientList(taxGroup, taxGroups);
                    UIUtils.updateClientListAndTaxItemGroup(taxGroup, taxItems,
                            taxGroups, taxItemGroups);
                    // if (getAccountingType() != ClientCompany.ACCOUNTING_TYPE_UK)
                    // {
                    // ClientTAXCode code = getTAXCodeForTAXItemGroup(taxGroup);
                    // Utility.updateClientList(code, taxCodes);
                    // }

                    break;

                case TAX_CODE:

                    ClientTAXCode taxCode = (ClientTAXCode) accounterCoreObject;

                    Utility.updateClientList(taxCode, taxCodes);

                    break;

                case TAXITEM:

                    ClientTAXItem taxItem = (ClientTAXItem) accounterCoreObject;
                    // Utility.updateClientList(taxItem, taxItems);
                    UIUtils.updateClientListAndTaxItemGroup(taxItem, taxItems,
                            taxGroups, taxItemGroups);
                    // if (getAccountingType() != ClientCompany.ACCOUNTING_TYPE_UK)
                    // {
                    // ClientTAXCode code = getTAXCodeForTAXItemGroup(taxItem);
                    // Utility.updateClientList(code, taxCodes);
                    // }

                    break;

                case CUSTOMER_GROUP:

                    ClientCustomerGroup customerGroup = (ClientCustomerGroup) accounterCoreObject;

                    Utility.updateClientList(customerGroup, customerGroups);

                    break;

                case VENDOR_GROUP:

                    ClientVendorGroup vendorGroup = (ClientVendorGroup) accounterCoreObject;

                    Utility.updateClientList(vendorGroup, vendorGroups);

                    break;

                // case EMPLOYEE_GROUP:
                //
                // ClientEmployeeGroup employeeGroup = (ClientEmployeeGroup)
                // accounterCoreObject;
                //
                // Utility.updateClientList(employeeGroup, employeeGroups);
                //
                // break;
                //
                case EMPLOYEE:

                    ClientEmployee employee = (ClientEmployee) accounterCoreObject;

                    Utility.updateClientList(employee, getEmployees());

                    break;
                //
                // case PAY_HEAD:
                //
                // ClientPayHead payhead = (ClientPayHead) accounterCoreObject;
                //
                // Utility.updateClientList(payhead, payheads);
                //
                // break;
                //
                // case EMPLOYEE_CATEGORY:
                //
                // ClientEmployeeCategory employeeCategory =
                // (ClientEmployeeCategory) accounterCoreObject;
                //
                // Utility.updateClientList(employeeCategory, employeeCategories);
                //
                // break;
                //
                // case ATTENDANCE_PRODUCTION_TYPE:
                //
                // ClientAttendanceOrProductionType attendanceProcutionType =
                // (ClientAttendanceOrProductionType) accounterCoreObject;
                //
                // Utility.updateClientList(attendanceProcutionType,
                // attendanceProductionTypes);
                //
                // break;

                case PAYMENT_TERM:

                    ClientPaymentTerms paymentsTerms = (ClientPaymentTerms) accounterCoreObject;

                    Utility.updateClientList(paymentsTerms, paymentTerms);

                    break;

                case SHIPPING_METHOD:

                    ClientShippingMethod shippingMethod = (ClientShippingMethod) accounterCoreObject;

                    Utility.updateClientList(shippingMethod, shippingMethods);

                    break;

                case SHIPPING_TERM:

                    ClientShippingTerms shippingTerm = (ClientShippingTerms) accounterCoreObject;

                    Utility.updateClientList(shippingTerm, shippingTerms);

                    break;

                case PRICE_LEVEL:

                    ClientPriceLevel priceLevel = (ClientPriceLevel) accounterCoreObject;

                    Utility.updateClientList(priceLevel, priceLevels);

                    break;

                case ITEM_GROUP:

                    ClientItemGroup itemGroup = (ClientItemGroup) accounterCoreObject;

                    Utility.updateClientList(itemGroup, itemGroups);

                    break;

                case SALES_PERSON:

                    ClientSalesPerson salesPerson = (ClientSalesPerson) accounterCoreObject;

                    Utility.updateClientList(salesPerson, salesPersons);

                    break;

                case CREDIT_RATING:

                    ClientCreditRating creditRating = (ClientCreditRating) accounterCoreObject;

                    Utility.updateClientList(creditRating, creditRatings);

                    break;

                // case PAY_TAX:
                //
                // ClientPayTAX payTax = (ClientPayTAX) accounterCoreObject;
                //
                // Utility.updateClientList(payTax, payTaxs);
                //
                // break;

                case BANK:

                    ClientBank clientBank = (ClientBank) accounterCoreObject;
                    Utility.updateClientList(clientBank, banks);
                    break;

                case FIXEDASSET:
                    ClientFixedAsset fixedAsset = (ClientFixedAsset) accounterCoreObject;
                    Utility.updateClientList(fixedAsset, fixedAssets);
                    break;
                case LOCATION:
                    ClientLocation clientLocation = (ClientLocation) accounterCoreObject;
                    Utility.updateClientList(clientLocation, locations);
                    break;
                case JOB:
                    ClientJob clientjob = (ClientJob) accounterCoreObject;
                    ClientCustomer customerByJob = getCustomerByJob(clientjob
                            .getID());
                    if (customerByJob != null) {
                        customerByJob.getJobs().remove(clientjob);
                        Utility.updateClientList(customerByJob, getCustomers());
                    }
                    ClientCustomer clientCustomer = getCustomer(clientjob
                            .getCustomer());
                    Utility.updateClientList(clientjob, clientCustomer.getJobs());
                    Utility.updateClientList(clientjob, jobs);
                    break;
                case EMAIL_TEMPLATE:
                    ClientEmailTemplate emailTemplate = (ClientEmailTemplate) accounterCoreObject;
                    Utility.updateClientList(emailTemplate, emailTemplates);
                    break;
                // case VATITEM:
                // ClientTAXItem vatItem = (ClientTAXItem)
                // accounterCoreObject;
                // Utility.updateClientList(vatItem, this.vatItems);
                // ViewManager.updateComboDataInViews(SelectItemType.VAT_ITEM,
                // accounterCoreObject);
                // break;
                // case VATGROUP:
                // ClientTAXGroup vatGroup = (ClientTAXGroup)
                // accounterCoreObject;
                // Utility.updateClientList(vatGroup, this.vatGroups);
                //
                // break;
                // case VATCODE:
                // ClientVATCode code = (ClientVATCode) accounterCoreObject;
                // Utility.updateClientList(code, this.vatCodes);
                // break;
                // case VATAGENCY:
                // ClientVATAgency vagy = (ClientVATAgency)
                // accounterCoreObject;
                // Utility.updateClientList(vagy, this.vatAgencies);
                // Utility.updateClientList(vagy, payees);
                // break;
                case TAX_RETURN:
                    ClientTAXReturn vaReturn = (ClientTAXReturn) accounterCoreObject;
                    Utility.updateClientList(vaReturn, this.taxReturns);
                    break;

                // case FISCALYEAR:
                // ClientFiscalYear fiscalYear = (ClientFiscalYear)
                // accounterCoreObject;
                // Utility.updateClientList(fiscalYear, this.fiscalYears);
                // sortFiscalYears();
                // break;
                case CURRENCY:
                    ClientCurrency currency = (ClientCurrency) accounterCoreObject;
                    if (currency.getFormalName() != null)
                        Utility.updateClientList(currency, currencies);
                    break;
                case COMPANY_PREFERENCES:
                    this.preferences = (ClientCompanyPreferences) accounterCoreObject;
                    break;
                case COMPANY:
                    ClientCompany cmp = (ClientCompany) accounterCoreObject;
                    this.getToClientCompany(cmp);
                    break;
                case BRANDINGTHEME:
                    ClientBrandingTheme theme = (ClientBrandingTheme) accounterCoreObject;
                    Utility.updateClientList(theme, brandingTheme);
                    break;
                case USER:
                    ClientUserInfo user = (ClientUserInfo) accounterCoreObject;
                    Utility.updateClientList(user, this.usersList);
                    long userID = user.getID();
                    ClientUser loggedInUser = getLoggedInUser();
                    if (loggedInUser.getID() == userID) {
                        loggedInUser.firstName = user.getFirstName();
                        loggedInUser.fullName = user.getFullName();
                        loggedInUser.lastName = user.getLastName();
                        Accounter.setUser(loggedInUser);
                    }
                    break;
                case ACCOUNTER_CLASS:
                    ClientAccounterClass accounterClass = (ClientAccounterClass) accounterCoreObject;
                    Utility.updateClientList(accounterClass, accounterClasses);
                    break;
                case MEASUREMENT:
                    ClientMeasurement measurement = (ClientMeasurement) accounterCoreObject;
                    Utility.updateClientList(measurement, measurements);
                    for (ClientUnit unit : measurement.getUnits()) {
                        if (unit.getID() != 0) {
                            Utility.updateClientList(unit, units);
                        }
                    }
                    break;
                case WAREHOUSE:
                    ClientWarehouse warehouse = (ClientWarehouse) accounterCoreObject;
                    Utility.updateClientList(warehouse, warehouses);
                    break;
                case CHEQUE_LAYOUT:
                    ClientChequeLayout chequeLayout = (ClientChequeLayout) accounterCoreObject;
                    Utility.updateClientList(chequeLayout, chequeLayouts);
                    break;
                case TDSDEDUCTORMASTER:
                    this.tdsDeductor = (ClientTDSDeductorMasters) accounterCoreObject;
                    break;
                case EMAIL_ACCOUNT:
                    ClientEmailAccount emailAccount = (ClientEmailAccount) accounterCoreObject;
                    Utility.updateClientList(emailAccount, emailAccounts);
                    break;
                case TDSRESPONSIBLEPERSON:
                    this.tdsResposiblePerson = (ClientTDSResponsiblePerson) accounterCoreObject;
                    break;
                case CUSTOMFIELD:
                    ClientCustomField customField = (ClientCustomField) accounterCoreObject;
                    Utility.updateClientList(customField, this.customFields);
                    break;
                case INVOICE_FREQUENCY:

                    ClientInvoiceFrequencyGroup clientInvoiceFrequencyGroup = (ClientInvoiceFrequencyGroup) accounterCoreObject;

                    Utility.updateClientList(clientInvoiceFrequencyGroup, invoiceFrequencyGroups);

                    break;
            }
        // } catch (Exception e) {
        // if (e instanceof JavaScriptException) {
        // Accounter.showInformation(messages
        // .exceptionOccur()
        // + ((JavaScriptException) (e)).getDescription());
        //
        // } else {
        // Accounter.showInformation(messages
        // .exceptionOccur() + e.toString());
        // }
        // }

    }

    private ClientCustomer getCustomerByJob(long jobID) {
        for (ClientCustomer customer : getCustomers()) {
            for (ClientJob job : customer.getJobs()) {
                if (job.getID() == jobID) {
                    return customer;
                }
            }
        }
        return null;
    }

    public void processDeleteObject(AccounterCoreType objectType, long id) {

        switch (objectType) {

            case ACCOUNT:
            case BANK_ACCOUNT:

                deleteAccount(id);

                break;

            case CUSTOMER:
                deleteCustomer(id);
                deletePayee(id);

                break;

            case VENDOR:

                deleteVendor(id);
                deletePayee(id);

                break;

            case TAXAGENCY:

                deleteTaxAgency(id);
                deletePayee(id);

                break;

            case ITEM:

                deleteItem(id);

                break;

            case TAX_GROUP:

                deleteTaxGroup(id);
                // if (getAccountingType() != ClientCompany.ACCOUNTING_TYPE_UK) {
                // deleteTaxCode(id);
                // // for (ClientTAXCode taxCode : getActiveTaxCodes()) {
                // // if (taxCode.id == id) {
                // // Utility.isDelete = true;
                // // Utility.updateClientList(taxCode, taxCodes);
                // // Utility.isDelete = false;
                // // }
                // // }
                // }

                break;

            case TAX_CODE:

                deleteTaxCode(id);

                break;

            case CUSTOMER_GROUP:
                deleteCustomerGroup(id);
                break;

            case VENDOR_GROUP:

                deleteVendorGroup(id);
                break;
            case PAYMENT_TERM:
                deletePaymentTerms(id);
                break;
            case SHIPPING_METHOD:
                deleteShippingMethod(id);
                break;

            case SHIPPING_TERM:
                deleteShippingTerms(id);
                break;

            case PRICE_LEVEL:
                deletePriceLevel(id);
                break;

            case ITEM_GROUP:
                deleteItemGroup(id);
                break;

            case SALES_PERSON:
                deleteSalesPerson(id);
                break;

            case CREDIT_RATING:

                deleteCreditRating(id);
                break;

            case BANK:
                deleteBank(id);
                break;

            case FIXEDASSET:
                deleteFixedAsset(id);
                break;
            case LOCATION:
                deleteLocation(id);
                break;
            case JOB:
                deleteJob(id);
                break;
            case CURRENCY:
                deleteCurrency(id);
                break;
            case TAXITEM:
                deleteTaxItem(id);
            case EMAIL_TEMPLATE:
                deleteTemplate(id);
                // if (getAccountingType() != ClientCompany.ACCOUNTING_TYPE_UK) {
                // deleteTaxCode(id);
                // for (ClientTAXCode taxCode : getActiveTaxCodes()) {
                // if (taxCode.id == id) {
                // Utility.isDelete = true;
                // Utility.updateClientList(taxCode, taxCodes);
                // Utility.isDelete = false;
                // }
                // }
                // }

                break;
            // case VATGROUP:
            // deleteVatGroup(id);
            // break;
            // case VATCODE:
            // deleteVatCode(id);
            // break;
            // case VATAGENCY:
            // deleteVatAgency(id);
            // break;
            case VATRETURN:
                deleteVAtReturn(id);
                break;
            // case FISCALYEAR:
            // deleteFixelYear(id);
            // break;
            case BRANDINGTHEME:
                deleteBrandingTheme(id);
                break;
            case ACCOUNTER_CLASS:
                deleteAccounterClass(id);
                break;
            case WAREHOUSE:
                deleteWarehouse(id);
                break;
            case MEASUREMENT:
                deleteMeasurement(id);
            case CUSTOMFIELD:
                deleteCustomField(id);
                break;

            case EMAIL_ACCOUNT:
                deleteEmailAccount(id);
                break;
            case USER:
                deleteUser(id);
                break;

            case EMPLOYEE:
                deleteEmployee(id);
                break;

            // case EMPLOYEE_GROUP:
            // deleteEmployeeGroup(id);
            // break;
            //
            case PAY_HEAD:
            case ATTENDANCE_PAY_HEAD:
            case COMPUTATION_PAY_HEAD:
            case FLATRATE_PAY_HEAD:
            case PRODUCTION_PAY_HEAD:
            case USERDEFINED_PAY_HEAD:
                deletePayHead(id);
                break;
            // case EMPLOYEE_CATEGORY:
            // deleteEmployeeCategory(id);
            // break;
            //
            // case ATTENDANCE_PRODUCTION_TYPE:
            // deleteAttendanceProductionType(id);
            // break;
        }
    }

    private void deletePayHead(long payHeadId) {
        ClientPayHead payHead = this.getPayHead(payHeadId);
        if (payHead != null) {
            this.payHeads.remove(payHead);
            fireEvent(new CoreEvent<ClientPayHead>(ChangeType.DELETE, payHead));
        }
    }

    private void deleteEmployee(long employeeID) {
        ClientEmployee employee = this.getEmployee(employeeID);
        if (employee != null) {
            this.getEmployees().remove(employee);
            fireEvent(new CoreEvent<ClientEmployee>(ChangeType.DELETE, employee));
        }
    }

    private void deleteTemplate(long id) {
        ClientEmailTemplate object = Utility.getObject(emailTemplates, id);
        this.emailTemplates.remove(object);
    }

    private void deleteJob(long id) {
        ClientJob object = Utility.getObject(this.jobs, id);
        if (object != null) {
            ClientCustomer customer = getCustomer(object.getCustomer());
            customer.getJobs().remove(object);
            processUpdateOrCreateObject(customer);
        }
        this.jobs.remove(object);

    }

    public void deleteCustomField(long id) {
        ClientCustomField object = Utility.getObject(this.customFields, id);
        this.customFields.remove(object);
    }

    private void deleteUser(long id) {
        ClientUserInfo user = this.getUserById(id);
        if (user != null) {
            this.usersList.remove(user);
            fireEvent(new CoreEvent<ClientUserInfo>(ChangeType.DELETE, user));
        }
    }

    private void deleteAccounterClass(long id) {
        ClientAccounterClass accounterClass = this.getAccounterClass(id);
        if (accounterClass != null) {
            this.accounterClasses.remove(accounterClass);
            fireEvent(new CoreEvent<ClientAccounterClass>(ChangeType.DELETE,
                    accounterClass));
        }
    }

    public ClientAccounterClass getAccounterClass(long classId) {
        return Utility.getObject(this.accounterClasses, classId);
    }

    // private void deleteSoldDisposedAsset(String id) {
    // this.sellingDisposedItems.remove(this.getSellingDisposedItem(id));
    //
    // }

    public void deleteFixedAsset(long id) {
        ClientFixedAsset fixedAsset = this.getFixedAsset(id);
        if (fixedAsset != null) {
            this.fixedAssets.remove(fixedAsset);
            fireEvent(new CoreEvent<ClientFixedAsset>(ChangeType.DELETE,
                    fixedAsset));
        }
    }

    public void deleteEmailTemplate(long id) {
        ClientEmailTemplate emailTemplate = this.getEmailTemplate(id);
        if (emailTemplate != null) {
            this.emailTemplates.remove(emailTemplate);
            fireEvent(new CoreEvent<ClientEmailTemplate>(ChangeType.DELETE,
                    emailTemplate));
        }
    }

    @Override
    public long getID() {
        return this.id;
    }

    @Override
    public void setID(long id) {
        this.id = id;

    }

    public void setFixedAssets(ArrayList<ClientFixedAsset> fixedAssets) {
        this.fixedAssets = fixedAssets;
    }

    public ArrayList<ClientFixedAsset> getFixedAssets() {
        return fixedAssets;
    }

    // public ArrayList<ClientSellingOrDisposingFixedAsset>
    // getSellingDisposedItems()
    // {
    // return sellingDisposedItems;
    // }
    //
    // public void setSellingDisposedItems(
    // List<ClientSellingOrDisposingFixedAsset> assets) {
    // this.sellingDisposedItems = assets;
    // }
    //
    // public ClientSellingOrDisposingFixedAsset getSellingDisposedItem(
    // String assetID) {
    // return Utility.getObject(this.sellingDisposedItems, assetID);
    // }

    public boolean objectExists(IAccounterCore core) {

        if (core == null || core.getObjectType() == null) {
            return false;
        }

        AccounterCoreType type = core.getObjectType();

        boolean alreadyExists = false;

        switch (type) {
            case ACCOUNT:
                alreadyExists = Utility.getObjectByName(accounts, core.getName()) != null;
                break;

            case CUSTOMER:
                alreadyExists = Utility.getObjectByName(customers, core.getName()) != null;
                break;

            case VENDOR:
                alreadyExists = Utility.getObjectByName(vendors, core.getName()) != null;
                break;

            case TAX_CODE:
                break;

            default:
                break;
        }

        return alreadyExists;
    }

    public Integer[] getNominalCodeRange(int accountSubBaseType) {

        for (ClientNominalCodeRange nomincalCode : this.getNominalCodeRange()) {
            if (nomincalCode.getAccountSubBaseType() == accountSubBaseType) {
                return new Integer[]{nomincalCode.getMinimum(),
                        nomincalCode.getMaximum()};
            }
        }

        return null;
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public void setConfigured(boolean isConfigured) {
        this.isConfigured = isConfigured;
    }

    public void setVatReturnBoxes(ArrayList<ClientVATReturnBox> vatReturnBoxes) {
        this.vatReturnBoxes = vatReturnBoxes;
    }

    public ArrayList<ClientVATReturnBox> getVatReturnBoxes() {
        return vatReturnBoxes;
    }

    public ClientVATReturnBox getVatReturnBoxByID(long vatReturnBoxID) {
        return Utility.getObject(this.vatReturnBoxes, vatReturnBoxID);
    }

    // public ClientTAXItem getVATItemByName(String name) {
    // return Utility.getObjectByName(this.vatItems, name);
    // }

    // public void setUkServiceItemDefaultIncomeAccount(
    // String ukServiceItemDefaultIncomeAccount) {
    // this.ukServiceItemDefaultIncomeAccount =
    // ukServiceItemDefaultIncomeAccount;
    // }
    //
    // public String getUkServiceItemDefaultIncomeAccount() {
    // return ukServiceItemDefaultIncomeAccount;
    // }
    //
    // public void setUkServiceItemDefaultExpenseAccount(
    // String ukServiceItemDefaultExpenseAccount) {
    // this.ukServiceItemDefaultExpenseAccount =
    // ukServiceItemDefaultExpenseAccount;
    // }
    //
    // public String getUkServiceItemDefaultExpenseAccount() {
    // return ukServiceItemDefaultExpenseAccount;
    // }

    // public void setUkNonInventoryItemDefaultIncomeAccount(
    // String ukNonInventoryItemDefaultIncomeAccount) {
    // this.ukNonInventoryItemDefaultIncomeAccount =
    // ukNonInventoryItemDefaultIncomeAccount;
    // }

    // public String getUkNonInventoryItemDefaultIncomeAccount() {
    // return ukNonInventoryItemDefaultIncomeAccount;
    // }
    //
    // public void setUkNonInventoryItemDefaultExpenseAccount(
    // String ukNonInventoryItemDefaultExpenseAccount) {
    // this.ukNonInventoryItemDefaultExpenseAccount =
    // ukNonInventoryItemDefaultExpenseAccount;
    // }

    // public String getUkNonInventoryItemDefaultExpenseAccount() {
    // return ukNonInventoryItemDefaultExpenseAccount;
    // }

    // public boolean isUKAccounting() {
    //
    // return this.accountingType == ACCOUNTING_TYPE_UK;
    // }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void getToClientCompany(ClientCompany clientCompany) {

        this.companyEmail = clientCompany.companyEmail;
        this.registrationNumber = clientCompany.registrationNumber;
        this.bankAccountNo = clientCompany.bankAccountNo;
        this.sortCode = clientCompany.sortCode;
        this.preferences = clientCompany.preferences;

    }

    public ClientTAXCode getTAXCodeForTAXItemGroup(
            ClientTAXItemGroup taxItemGroup) {
        ClientTAXCode taxCode = new ClientTAXCode();
        taxCode.setID(taxItemGroup.getID());
        taxCode.setName(taxItemGroup.getName());
        taxCode.setDescription(taxItemGroup.description);
        taxCode.setActive(taxItemGroup.isActive());
        taxCode.setTAXItemGrpForSales(taxItemGroup.getID());
        taxCode.setTaxable(true);
        taxCode.setTAXItemGrpForPurchases(0);
        return taxCode;
    }

    // public void deleteCurrency(long id) {
    // ClientCurrency clientBrandingTheme = this.getCurrency(id);
    // if (clientBrandingTheme != null) {
    // this.brandingTheme.remove(clientBrandingTheme);
    // fireEvent(new CoreEvent<ClientCurrency>(ChangeType.DELETE,
    // clientBrandingTheme));
    // }
    // }

    public void setBrandingThemes(ArrayList<ClientBrandingTheme> brandingTheme) {
        this.brandingTheme = brandingTheme;
    }

    public void setWareHouses(ArrayList<ClientWarehouse> warehouse) {
        this.warehouses = warehouse;
    }

    public ArrayList<ClientBrandingTheme> getBrandingTheme() {
        return brandingTheme;
    }

    public ArrayList<ClientWarehouse> getWarehouses() {
        return warehouses;
    }

    public void setUsersList(ArrayList<ClientUserInfo> users) {
        this.usersList = users;
    }

    public ArrayList<ClientUserInfo> getUsersList() {
        return usersList;
    }

    public ClientAccount getAccountByNumber(long accountNo) {
        for (ClientAccount account : getAccounts()) {
            if (account.getNumber().equals(String.valueOf(accountNo)))
                return account;
        }

        return null;

    }

    // public ClientFinanceDate getLastandOpenedFiscalYearStartDate() {
    // List<ClientFiscalYear> clientFiscalYears = getFiscalYears();
    // if (!clientFiscalYears.isEmpty())
    // return clientFiscalYears.get((clientFiscalYears.size() - 1))
    // .getStartDate();
    // return null;
    // }

    public ClientFinanceDate getCurrentFiscalYearStartDate() {

        Calendar cal = Calendar.getInstance();
        ClientFinanceDate startDate = new ClientFinanceDate();
        cal.setTime(startDate.getDateAsObject());
        cal.set(Calendar.MONTH, preferences.getFiscalYearFirstMonth());
        cal.set(Calendar.DAY_OF_MONTH, 1);

        while (new ClientFinanceDate(cal.getTime())
                .after(new ClientFinanceDate())) {
            cal.add(Calendar.YEAR, -1);
        }
        startDate = new ClientFinanceDate(cal.getTime());
        return startDate;
    }

    public ClientFinanceDate getCurrentFiscalYearEndDate() {

        ClientFinanceDate startDate = getCurrentFiscalYearStartDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate.getDateAsObject());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        calendar.set(Calendar.DATE,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        ClientFinanceDate endDate = new ClientFinanceDate(calendar.getTime());

        return endDate;
    }

    // public ClientFinanceDate getLastandOpenedFiscalYearEndDate() {
    // List<ClientFiscalYear> clientFiscalYears = getFiscalYears();
    //
    // if (!clientFiscalYears.isEmpty())
    // return clientFiscalYears.get((clientFiscalYears.size() - 1))
    // .getEndDate();
    //
    // return null;
    // }

    // public boolean isCurrentInFiscalYear(long date) {
    // List<ClientFiscalYear> clientFiscalYears = getFiscalYears();
    // boolean isCurrentOne = false;
    // for (int i = clientFiscalYears.size() - 1; i >= 0; i--) {
    // long startDate = Math.round(clientFiscalYears.get(i).getStartDate()
    // .getDate() / 100);
    // long endDate = Math.round(clientFiscalYears.get(i).getEndDate()
    // .getDate() / 100);
    // isCurrentOne = (date >= startDate) && (date <= endDate);
    // if (isCurrentOne)
    // return true;
    // }
    // return false;
    // }

    // public void sortFiscalYears() {
    // Collections.sort(getFiscalYears(), new Comparator<ClientFiscalYear>() {
    //
    // @Override
    // public int compare(ClientFiscalYear o1, ClientFiscalYear o2) {
    // return o1.getStartDate().compareTo(o2.getStartDate());
    // }
    // });
    // }

    /**
     * @return the transactionStartDate
     */
    public ClientFinanceDate getTransactionStartDate() {
        return transactionStartDate;
    }

    /**
     * @param transactionStartDate the transactionStartDate to set
     */
    public void setTransactionStartDate(ClientFinanceDate transactionStartDate) {
        this.transactionStartDate = transactionStartDate;
    }

    /**
     * @return the transactionEndDate
     */
    public ClientFinanceDate getTransactionEndDate() {
        return transactionEndDate;
    }

    /**
     * @param transactionEndDate the transactionEndDate to set
     */
    public void setTransactionEndDate(ClientFinanceDate transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }

    /**
     * @return the tradingAddress
     */
    public ClientAddress getTradingAddress() {
        return this.preferences.getTradingAddress();
    }

    public boolean isShowRegisteredAddress() {
        return this.preferences.isShowRegisteredAddress();
    }

    public void setShowRegisteredAddress(boolean isShowRegisteredAddress) {
        this.preferences.setShowRegisteredAddress(isShowRegisteredAddress);
        if (!isShowRegisteredAddress)
            this.registeredAddress = preferences.getTradingAddress();
    }

    /**
     * @param tradingAddress the tradingAddress to set
     */
    public void setTradingAddress(ClientAddress tradingAddress) {
        this.preferences.setTradingAddress(tradingAddress);
    }

    /**
     * @return the registeredAddress
     */
    public ClientAddress getRegisteredAddress() {
        return registeredAddress;
    }

    /**
     * @param registeredAddress the registeredAddress to set
     */
    public void setRegisteredAddress(ClientAddress registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    /**
     * @return
     */
    public ClientUser getLoggedInUser() {
        return this.loggedInUser;
    }

    public void setLoggedInUser(ClientUser user) {
        this.loggedInUser = user;
    }

    public void setCurrencies(ArrayList<ClientCurrency> currencies) {
        this.currencies = currencies;
    }

    public ArrayList<ClientCurrency> getCurrencies() {
        return currencies;
    }

    /**
     * @param string
     */
    public ClientCurrency getCurrency(String code) {
        for (ClientCurrency currency : currencies) {
            if (currency.getFormalName() != null
                    && currency.getFormalName().equals(code)) {
                return currency;
            }
        }
        return null;
    }

    public ClientCompany clone() {
        ClientCompany clientCompany = (ClientCompany) this.clone();
        ArrayList<ClientAccount> accounts = new ArrayList<ClientAccount>();
        for (ClientAccount clientAccount : this.accounts) {
            accounts.add(clientAccount.clone());
        }
        clientCompany.accounts = accounts;

        ArrayList<ClientBank> banks = new ArrayList<ClientBank>();
        for (ClientBank clientBank : this.banks) {
            banks.add(clientBank.clone());
        }
        clientCompany.banks = banks;

        ArrayList<ClientBrandingTheme> brandingThemes = new ArrayList<ClientBrandingTheme>();
        for (ClientBrandingTheme clientBrandingTheme : this.brandingTheme) {
            brandingThemes.add(clientBrandingTheme.clone());
        }
        clientCompany.brandingTheme = brandingThemes;

        ArrayList<ClientMeasurement> clientMeasurements = new ArrayList<ClientMeasurement>();
        for (ClientMeasurement clientMeasurement : this.measurements) {
            clientMeasurements.add(clientMeasurement.clone());
        }
        clientCompany.measurements = clientMeasurements;

        ArrayList<ClientWarehouse> clientWarehouses = new ArrayList<ClientWarehouse>();
        for (ClientWarehouse clientWarehouse : this.warehouses) {
            clientWarehouses.add(clientWarehouse.clone());
        }
        clientCompany.warehouses = clientWarehouses;

        ArrayList<ClientCreditRating> creditRatings = new ArrayList<ClientCreditRating>();
        for (ClientCreditRating clientCreditRating : this.creditRatings) {
            creditRatings.add(clientCreditRating.clone());
        }
        clientCompany.creditRatings = creditRatings;

        ArrayList<ClientCurrency> currencies = new ArrayList<ClientCurrency>();
        for (ClientCurrency clientCurrency : this.currencies) {
            currencies.add(clientCurrency.clone());
        }
        clientCompany.currencies = currencies;

        ArrayList<ClientCustomerGroup> customerGroups = new ArrayList<ClientCustomerGroup>();
        for (ClientCustomerGroup clientCustomerGroup : this.customerGroups) {
            customerGroups.add(clientCustomerGroup.clone());
        }
        clientCompany.customerGroups = customerGroups;

        ArrayList<ClientCustomer> customers = new ArrayList<ClientCustomer>();
        for (ClientCustomer clientCustomer : this.customers) {
            customers.add(clientCustomer.clone());
        }
        clientCompany.customers = customers;

        // ArrayList<ClientFiscalYear> fiscalYears = new
        // ArrayList<ClientFiscalYear>();
        // for (ClientFiscalYear clientFiscalYear : this.fiscalYears) {
        // fiscalYears.add(clientFiscalYear.clone());
        // }
        // clientCompany.fiscalYears = fiscalYears;

        ArrayList<ClientFixedAsset> fixedAssets = new ArrayList<ClientFixedAsset>();
        for (ClientFixedAsset clientfixedAsset : this.fixedAssets) {
            fixedAssets.add(clientfixedAsset.clone());
        }
        clientCompany.fixedAssets = fixedAssets;

        ArrayList<ClientItemGroup> itemGroups = new ArrayList<ClientItemGroup>();
        for (ClientItemGroup clientItemGroup : this.itemGroups) {
            itemGroups.add(clientItemGroup.clone());
        }
        clientCompany.itemGroups = itemGroups;

        ArrayList<ClientItem> items = new ArrayList<ClientItem>();
        for (ClientItem clientItem : this.items) {
            items.add(clientItem.clone());
        }
        clientCompany.items = items;

        Set<ClientNominalCodeRange> nominalCodeRanges = new HashSet<ClientNominalCodeRange>();
        for (ClientNominalCodeRange clientNominalCodeRange : this.nominalCodeRange) {
            nominalCodeRanges.add(clientNominalCodeRange.clone());
        }
        clientCompany.nominalCodeRange = nominalCodeRanges;

        ArrayList<ClientPayee> payees = new ArrayList<ClientPayee>();
        for (ClientPayee clientPayee : this.payees) {
            payees.add(clientPayee.clone());
        }
        clientCompany.payees = payees;

        // TODO clientCompany.paymentMethods

        ArrayList<ClientPaymentTerms> paymentTerms = new ArrayList<ClientPaymentTerms>();
        for (ClientPaymentTerms clientPaymentTerms : this.paymentTerms) {
            paymentTerms.add(clientPaymentTerms.clone());
        }
        clientCompany.paymentTerms = paymentTerms;

        ArrayList<ClientPayTAX> paySalesTaxs = new ArrayList<ClientPayTAX>();
        for (ClientPayTAX clientPaySalesTax : this.payTaxs) {
            paySalesTaxs.add(clientPaySalesTax.clone());
        }
        clientCompany.payTaxs = paySalesTaxs;

        ArrayList<ClientPriceLevel> priceLevels = new ArrayList<ClientPriceLevel>();
        for (ClientPriceLevel clientPriceLevel : this.priceLevels) {
            priceLevels.add(clientPriceLevel.clone());
        }
        clientCompany.priceLevels = priceLevels;

        ArrayList<ClientSalesPerson> salesPersons = new ArrayList<ClientSalesPerson>();
        for (ClientSalesPerson clientSalesPerson : this.salesPersons) {
            salesPersons.add(clientSalesPerson.clone());
        }
        clientCompany.salesPersons = salesPersons;

        ArrayList<ClientShippingMethod> shippingMethods = new ArrayList<ClientShippingMethod>();
        for (ClientShippingMethod clientShippingMethod : this.shippingMethods) {
            shippingMethods.add(clientShippingMethod.clone());
        }
        clientCompany.shippingMethods = shippingMethods;

        ArrayList<ClientShippingTerms> shippingTerms = new ArrayList<ClientShippingTerms>();
        for (ClientShippingTerms clientShippingTerms : this.shippingTerms) {
            shippingTerms.add(clientShippingTerms.clone());
        }
        clientCompany.shippingTerms = shippingTerms;

        ArrayList<ClientTAXAdjustment> taxAdjustments = new ArrayList<ClientTAXAdjustment>();
        for (ClientTAXAdjustment clientTAXAdjustment : this.taxAdjustments) {
            taxAdjustments.add(clientTAXAdjustment.clone());
        }
        clientCompany.taxAdjustments = taxAdjustments;

        ArrayList<ClientTAXAgency> taxAgencies = new ArrayList<ClientTAXAgency>();
        for (ClientTAXAgency clientTAXAgency : this.taxAgencies) {
            taxAgencies.add(clientTAXAgency.clone());
        }
        clientCompany.taxAgencies = taxAgencies;

        ArrayList<ClientTAXCode> taxCodes = new ArrayList<ClientTAXCode>();
        for (ClientTAXCode clientTAXCode : this.taxCodes) {
            taxCodes.add(clientTAXCode.clone());
        }
        clientCompany.taxCodes = taxCodes;

        ArrayList<ClientQuantity> quantities = new ArrayList<ClientQuantity>();
        for (ClientQuantity clientQuantity : this.quantities) {
            quantities.add(clientQuantity.clone());
        }
        clientCompany.quantities = quantities;

        ArrayList<ClientMeasurement> measurements = new ArrayList<ClientMeasurement>();
        for (ClientMeasurement measurement : this.measurements) {
            measurements.add(measurement.clone());
        }
        clientCompany.measurements = measurements;

        ArrayList<ClientWarehouse> warehouses = new ArrayList<ClientWarehouse>();
        for (ClientWarehouse warehouse : this.warehouses) {
            warehouses.add(warehouse.clone());
        }
        clientCompany.warehouses = warehouses;

        ArrayList<ClientTAXGroup> taxGroups = new ArrayList<ClientTAXGroup>();
        for (ClientTAXGroup clientTAXGroup : this.taxGroups) {
            taxGroups.add(clientTAXGroup.clone());
        }
        clientCompany.taxGroups = taxGroups;

        ArrayList<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
        for (ClientTAXItem clientTAXItem : this.taxItems) {
            taxItems.add(clientTAXItem.clone());
        }
        clientCompany.taxItems = taxItems;

        ArrayList<ClientUserInfo> usersList = new ArrayList<ClientUserInfo>();
        for (ClientUserInfo clientEmployee : this.usersList) {
            usersList.add(clientEmployee.clone());
        }
        clientCompany.usersList = usersList;

        ArrayList<ClientTAXGroup> vatGroups = new ArrayList<ClientTAXGroup>();
        for (ClientTAXGroup clientTAXGroup : this.vatGroups) {
            vatGroups.add(clientTAXGroup.clone());
        }
        clientCompany.vatGroups = vatGroups;

        ArrayList<ClientVendor> vendors = new ArrayList<ClientVendor>();
        for (ClientVendor clientVendor : this.vendors) {
            vendors.add(clientVendor.clone());
        }

        ArrayList<ClientInvoiceFrequencyGroup> invoiceFrequencyGroups = new ArrayList<ClientInvoiceFrequencyGroup>();
        for (ClientInvoiceFrequencyGroup invoiceFrequencyGroup : this.invoiceFrequencyGroups) {
            invoiceFrequencyGroups.add(invoiceFrequencyGroup.clone());
        }
        clientCompany.invoiceFrequencyGroups = invoiceFrequencyGroups;
        clientCompany.vendors = vendors;

        clientCompany.loggedInUser = this.loggedInUser.clone();
        clientCompany.preferences = this.preferences.clone();
        clientCompany.registeredAddress = this.registeredAddress.clone();
        clientCompany.setTradingAddress(this.getTradingAddress().clone());
        clientCompany.transactionEndDate = this.transactionEndDate.clone();
        clientCompany.transactionStartDate = this.transactionStartDate.clone();

        return clientCompany;

    }

    /**
     * @param unit
     * @return
     */
    public ClientUnit getUnitById(long unitID) {
        for (ClientUnit u : units) {
            if (u.getId() == unitID) {
                return u;
            }
        }
        return null;
    }

    /**
     * @param units
     */
    public void setUnits(ArrayList<ClientUnit> units) {
        this.units = units;
    }

    /**
     * @return
     */
    public ArrayList<ClientUnit> getUnits() {
        return units;
    }

    public ClientTAXCode getTaxCodeByName(String name) {
        return Utility.getObjectByName(getTaxCodes(), name);
    }

    public ClientItemGroup getItemGroupByName(String name) {
        return Utility.getObjectByName(getItemGroups(), name);
    }

    public ClientInvoiceFrequencyGroup getInvoiceFrequencyGroupByName(String name) {

        return Utility.getObjectByName(getInvoiceFrequencyGroups(), name);
    }

    public ClientLocation getLocationByName(String name) {
        return Utility.getObjectByName(getLocations(), name);
    }

    public ClientCreditRating getCreditRatingByName(String name) {
        return Utility.getObjectByName(getCreditRatings(), name);
    }

    public ClientItem getItemByName(String name) {
        return Utility.getObjectByName(getItems(), name);
    }

    public ClientPaymentTerms getPaymentTermsByName(String name) {
        return Utility.getObjectByName(getPaymentsTerms(), name);
    }

    public ClientPriceLevel getPriceLevelByName(String name) {
        return Utility.getObjectByName(getPriceLevels(), name);
    }

    public ClientTAXGroup getTaxGroupByName(String name) {
        return Utility.getObjectByName(getTaxGroups(), name);
    }

    public ClientShippingMethod getShippingMethodByName(String name) {
        return Utility.getObjectByName(getShippingMethods(), name);
    }

    public ClientShippingTerms getShippingTermByName(String name) {
        return Utility.getObjectByName(getShippingTerms(), name);
    }

    public ClientVendorGroup getVendorGroupByName(String name) {
        return Utility.getObjectByName(getVendorGroups(), name);
    }

    public ClientCustomerGroup getCustomerGroupByName(String name) {
        return Utility.getObjectByName(getCustomerGroups(), name);
    }

    public ClientFixedAsset getFixedAssetByName(String name) {
        return Utility.getObjectByName(getFixedAssets(), name);
    }

    public ClientBrandingTheme getBrandingThemeByName(String name) {
        return Utility.getObjectByName(getBrandingTheme(), name);
    }

    public ClientWarehouse getWarehouseByName(String name) {
        return Utility.getObjectByName(getWarehouses(), name);
    }

    public ClientMeasurement getMeasurementByName(String name) {
        return Utility.getObjectByName(getMeasurements(), name);
    }

    public ClientTAXItem getTaxItemByName(String name) {
        return Utility.getObjectByName(getTaxItems(), name);
    }

    public ClientSalesPerson getSalesPersonByName(String name) {
        return Utility.getObjectByName(getsalesPerson(), name);
    }

    public ClientVendorGroup getVendorGroupsByName(String name) {
        return Utility.getObjectByName(getVendorGroups(), name);
    }

    public ClientTAXGroup getVatGroupsbyname(String name) {
        return Utility.getObjectByName(getVatGroups(), name);
    }

    public ClientTAXAgency getTaxAgenciesByName(String name) {
        return Utility.getObjectByName(gettaxAgencies(), name);
    }

    public ClientTAXCode getTAXCodeByName(String name) {
        return Utility.getObjectByName(getTaxCodes(), name);
    }

    public ClientUserInfo getUserById(long id) {
        for (ClientUserInfo employee : usersList) {
            if (employee.getID() == id) {
                return employee;
            }
        }
        return null;
    }

    public long getCashDiscountsGiven() {
        return cashDiscountsGiven;
    }

    public void setCashDiscountsGiven(long cashDiscountsGiven) {
        this.cashDiscountsGiven = cashDiscountsGiven;
    }

    public long getCashDiscountsTaken() {
        return cashDiscountsTaken;
    }

    public void setCashDiscountsTaken(long cashDiscountsTaken) {
        this.cashDiscountsTaken = cashDiscountsTaken;
    }

    public long getTaxLiabilityAccount() {
        return taxLiabilityAccount;
    }

    public void setTaxLiabilityAccount(long taxLiabilityAccount) {
        this.taxLiabilityAccount = taxLiabilityAccount;
    }

    public long getVATFiledLiabilityAccount() {
        return VATFiledLiabilityAccount;
    }

    public void setVATFiledLiabilityAccount(long vATFiledLiabilityAccount) {
        VATFiledLiabilityAccount = vATFiledLiabilityAccount;
    }

    public long getNextAccountNumber(int accountSubBaseType) {

        Collections.sort(accounts, new Comparator<ClientAccount>() {

            @Override
            public int compare(ClientAccount o1, ClientAccount o2) {
                Long number1 = Long.parseLong(o1.getNumber());
                Long number2 = Long.parseLong(o2.getNumber());
                return number1.compareTo(number2);
            }
        });
        Integer[] codeRanges = getNominalCodeRange(accountSubBaseType);
        long lastUsedNo = codeRanges[0];
        for (ClientAccount account : accounts) {
            if (account.getSubBaseType() == accountSubBaseType) {
                long number = Long.parseLong(account.getNumber());
                if (number == lastUsedNo) {
                    lastUsedNo++;
                } else if (DecimalUtil.isGreaterThan(number, lastUsedNo)) {
                    lastUsedNo = number + 1;
                } else {
                    break;
                }
            }
        }
        if (preferences.isAccountnumberRangeCheckEnable()) {
            if (lastUsedNo < codeRanges[1]) {
                return lastUsedNo;
            }
        } else {
            return lastUsedNo;
        }
        return -1;
    }

    public ArrayList<ClientLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<ClientLocation> locations) {
        this.locations = locations;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public void setAccounterClasses(
            ArrayList<ClientAccounterClass> accounterClasses) {
        this.accounterClasses = accounterClasses;
    }

    public ArrayList<ClientAccounterClass> getAccounterClasses() {
        return this.accounterClasses;
    }

    public ArrayList<ClientAccounterClass> getaccounterClassesWithChilds() {
        ArrayList<ClientAccounterClass> accounterClasses = this.accounterClasses;
        ArrayList<ClientAccounterClass> classes = new ArrayList<ClientAccounterClass>();
        // dividing child's and add
        addChilds(classes, accounterClasses, 0, 0);
        return classes;
    }

    /**
     * @param classes
     * @param accounterClasses
     * @param depth
     * @param parent
     */
    private void addChilds(ArrayList<ClientAccounterClass> classes,
                           ArrayList<ClientAccounterClass> accounterClasses, int depth,
                           long parent) {
        ArrayList<ClientAccounterClass> childs = getChild(accounterClasses,
                parent);
        for (ClientAccounterClass ch : childs) {
            ch.setDepth(depth);
            classes.add(ch);
            addChilds(classes, accounterClasses, depth + 1, ch.getID());
        }
    }

    /**
     * @param accounterClasses
     * @param parent
     * @return
     */
    private ArrayList<ClientAccounterClass> getChild(
            ArrayList<ClientAccounterClass> accounterClasses, long p) {
        ArrayList<ClientAccounterClass> childs = new ArrayList<ClientAccounterClass>();
        for (ClientAccounterClass c : accounterClasses) {
            if (c.getParent() == p) {
                childs.add(c);
            }
        }
        return childs;
    }

    public long getDefaultTaxCode() {
        List<ClientTAXCode> taxCodes = getActiveTaxCodes();
        for (ClientTAXCode taxCode : taxCodes) {
            if (taxCode.getName().equals("S")) {
                return taxCode.id;
            }
        }
        return 0;
    }

    public CountryPreferences getCountryPreferences() {
        return countryPreferences;

    }

    public void setCountryPreferences(CountryPreferences countryPreferences) {
        this.countryPreferences = countryPreferences;
    }

    public String getCountry() {
        return this.registeredAddress.getCountryOrRegion();
    }

    public List<ClientMeasurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(ArrayList<ClientMeasurement> measurements) {
        this.measurements = measurements;
    }

    public void setWarehouses(ArrayList<ClientWarehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public long getDefaultMeasurement() {
        return defaultMeasurement;
    }

    public void setDefaultMeasurement(long defaultMeasurement) {
        this.defaultMeasurement = defaultMeasurement;
    }

    public long getDefaultWarehouse() {
        return defaultWarehouse;
    }

    public void setDefaultWarehouse(long defaultWarehouse) {
        this.defaultWarehouse = defaultWarehouse;
    }

    public ClientCurrency getPrimaryCurrency() {
        long id2 = getPreferences().getPrimaryCurrency().id;
        if (id2 == 0) {
            throw new RuntimeException(Global.get().messages()
                    .primaryCurrencyIdisZero());
        }
        return getCurrency(id2);
    }

    public boolean hasOtherCountryCurrency() {

        long id = getPreferences().getPrimaryCurrency().id;

        // for checking in all the Active Accounts
        ArrayList<ClientAccount> activeAccounts = getActiveAccounts();
        for (ClientAccount account : activeAccounts) {
            ClientCurrency currency = getCurrency(account.getCurrency());
            if (id != currency.id) {
                return true;
            }
        }

        // for checking in all the Active Payee
        ArrayList<ClientPayee> activePayees = getActivePayees();
        for (ClientPayee payee : activePayees) {
            ClientCurrency currency = getCurrency(payee.getCurrency());
            if (currency != null && id != currency.id) {
                return true;
            }
        }
        return false;

    }

    public ArrayList<ClientCustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<ClientCustomField> customFields) {
        this.customFields = customFields;
    }

    public ClientPortletPageConfiguration getPortletPageConfiguration(
            String name) {
        Set<ClientPortletPageConfiguration> portletPageConfigurations = getLoggedInUser()
                .getPortletPages();
        for (ClientPortletPageConfiguration pageConfiguration : portletPageConfigurations) {
            if (pageConfiguration.getPageName().equals(name)
                    && !pageConfiguration.getPortletConfigurations().isEmpty()) {
                return pageConfiguration;
            }
        }
        return PortletFactory.get().getDefaultConfiguration(name);
    }

    public void setPortletConfiguration(
            ClientPortletPageConfiguration configuration) {
        Set<ClientPortletPageConfiguration> portletPages = getLoggedInUser()
                .getPortletPages();
        Iterator<ClientPortletPageConfiguration> iterator = portletPages
                .iterator();
        while (iterator.hasNext()) {
            ClientPortletPageConfiguration next = iterator.next();
            if (next.getPageName().equals(configuration.getPageName())) {
                iterator.remove();
            }
        }

        portletPages.add(configuration);

    }

    public long getCashDiscountAccount() {
        return cashDiscountAccount;
    }

    public void setCashDiscountAccount(long cashDiscountAccount) {
        this.cashDiscountAccount = cashDiscountAccount;
    }

    public ClientCustomField getCustomFieldByTitle(String title) {
        ArrayList<ClientCustomField> customFields = getCustomFields();
        for (ClientCustomField f : customFields) {
            if (f.getName().equals(title)) {
                return f;
            }
        }
        return null;
    }

    /**
     * @return the advertisements
     */
    public ArrayList<ClientAdvertisement> getAdvertisements() {
        return advertisements;
    }

    /**
     * @param advertisements the advertisements to set
     */
    public void setAdvertisements(ArrayList<ClientAdvertisement> advertisements) {
        this.advertisements = advertisements;
    }

    /**
     * If bankAccountId is zero then it returns the default cheque layout If not
     * found any Layout for given account Id, then it will return default layout
     *
     * @param bankAccountId
     * @return
     */
    public ClientChequeLayout getCheckLayout(long bankAccountId) {
        for (ClientChequeLayout layout : chequeLayouts) {
            if (layout.getAccount() == bankAccountId) {
                return layout;
            }
        }
        // Should not reach this line
        return null;
    }

    public ClientTDSDeductorMasters getTdsDeductor() {
        return tdsDeductor;
    }

    public void setTdsDeductor(ClientTDSDeductorMasters tdsDeductor) {
        this.tdsDeductor = tdsDeductor;
    }

    public ClientTDSResponsiblePerson getTdsResposiblePerson() {
        return tdsResposiblePerson;
    }

    public void setTdsResposiblePerson(
            ClientTDSResponsiblePerson tdsResposiblePerson) {
        this.tdsResposiblePerson = tdsResposiblePerson;
    }

    public List<ClientEmailAccount> getEmailAccounts() {
        return emailAccounts;
    }

    public void setEmailAccounts(List<ClientEmailAccount> emailAccounts) {
        this.emailAccounts = emailAccounts;
    }

    public ArrayList<ClientJob> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<ClientJob> jobs) {
        this.jobs = jobs;
    }

    public boolean isUnlimitedUser() {
        return premiumType == UNLIMITED_USERS;
    }

    public boolean isPaid() {
        return premiumType != FREE_USER;
    }

    /**
     * @return the exchangeLossOrGainAccount
     */
    public long getExchangeLossOrGainAccount() {
        return exchangeLossOrGainAccount;
    }

    /**
     * @param exchangeLossOrGainAccount the exchangeLossOrGainAccount to set
     */
    public void setExchangeLossOrGainAccount(long exchangeLossOrGainAccount) {
        this.exchangeLossOrGainAccount = exchangeLossOrGainAccount;
    }

    /**
     * @return the costOfGoodsSold
     */
    public long getCostOfGoodsSold() {
        return costOfGoodsSold;
    }

    /**
     * @param costOfGoodsSold the costOfGoodsSold to set
     */
    public void setCostOfGoodsSold(long costOfGoodsSold) {
        this.costOfGoodsSold = costOfGoodsSold;
    }

    public ClientEmailAccount getEmailAccount(String email) {
        if (email != null && !email.isEmpty()) {
            for (ClientEmailAccount account : emailAccounts) {
                if (account.getEmailId().equals(email)) {
                    return account;
                }
            }
        }
        return null;
    }

    public int getPremiumType() {
        return premiumType;
    }

    public void setPremiumType(int premiumType) {
        this.premiumType = premiumType;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public int getTransactionsLimit() {
        return transactionsLimit;
    }

    public void setTransactionsLimit(int transactionsLimit) {
        this.transactionsLimit = transactionsLimit;
    }

    public boolean isBookKeeping() {
        return bookKeeping;
    }

    public void setBookKeeping(boolean bookKeeping) {
        this.bookKeeping = bookKeeping;
    }

    public List<ClientMessageOrTask> getMessagesAndTasks() {
        return messagesAndTasks;
    }

    public void setMessagesAndTasks(List<ClientMessageOrTask> messagesAndTasks) {
        this.messagesAndTasks = messagesAndTasks;
    }

    public List<ClientEmailTemplate> getEmailTemplates() {
        return emailTemplates;
    }

    public void setEmailTemplates(List<ClientEmailTemplate> emailTemplates) {
        this.emailTemplates = emailTemplates;
    }

    public long getSalariesPayableAccount() {
        return salariesPayableAccount;
    }

    public void setSalariesPayableAccount(long salariesPayableAccount) {
        this.salariesPayableAccount = salariesPayableAccount;
    }

    /**
     * @return the payHeads
     */
    public ArrayList<ClientPayHead> getPayHeads() {
        return payHeads;
    }

    /**
     * @param payHeads the payHeads to set
     */
    public void setPayHeads(ArrayList<ClientPayHead> payHeads) {
        this.payHeads = payHeads;
    }

    /**
     * @return the employees
     */
    public ArrayList<ClientEmployee> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(ArrayList<ClientEmployee> employees) {
        this.employees = employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployeeGroups(List<ClientPayStructureDestination> employees) {
        for (ClientPayStructureDestination selectedValue : employees) {
            if (selectedValue instanceof ClientEmployee) {
                this.employees.add(((ClientEmployee) selectedValue));
            } else {
                ClientEmployeeGroup clientEmployeeGroup = (ClientEmployeeGroup) selectedValue;
                for (ClientEmployee employee : clientEmployeeGroup.getEmployees()) {
                    this.employees.add(((ClientEmployee) employee));
                }
            }
        }
    }

}
