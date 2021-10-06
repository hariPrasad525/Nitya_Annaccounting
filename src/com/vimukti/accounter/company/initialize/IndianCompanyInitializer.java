package com.vimukti.accounter.company.initialize;

import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.utils.HibernateUtil;

public class IndianCompanyInitializer extends CompanyInitializer {

	/**
	 * @return the preferences
	 */
	public CompanyPreferences getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences
	 *            the preferences to set
	 */
	public void setPreferences(CompanyPreferences preferences) {
		this.preferences = preferences;
	}

	/**
	 * Creates new Instance
	 */
	public IndianCompanyInitializer(Company company) {
		super(company);
	}

	@Override
	public void init() {
		initDefaultIndiaAccounts();
	}

	private void initDefaultIndiaAccounts() {
		createDefaults();
	}

	public void createDefaults() {

		Account tdsPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.TDS_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		Account tdsDeductedByOthers = createAccount(Account.TYPE_OTHER_ASSET,
				AccounterServerConstants.TDS_DEDUCTED_BY_OTHERS,
				Account.CASH_FLOW_CATEGORY_INVESTING);

		Account serviceTaxPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.SERVICE_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		Account cstPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.CENTRAL_SALES_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		Session session = HibernateUtil.getCurrentSession();
		PaymentTerms paymentTerms = (PaymentTerms) session
				.getNamedQuery("unique.name.PaymentTerms")
				.setEntity("company", company)
				.setParameter("name", "Net Monthly",
						EncryptedStringType.INSTANCE).uniqueResult();

		TAXAgency defaultTDSAgency = new TAXAgency();
		defaultTDSAgency.setActive(Boolean.TRUE);
		defaultTDSAgency.setTaxType(TAXAgency.TAX_TYPE_TDS);
		defaultTDSAgency.setName("TDS Tax Agency");
		defaultTDSAgency.setVATReturn(0);
		defaultTDSAgency.setPurchaseLiabilityAccount(tdsPayable);
		defaultTDSAgency.setSalesLiabilityAccount(tdsDeductedByOthers);
		defaultTDSAgency.setDefault(true);
		defaultTDSAgency.setCompany(company);
		defaultTDSAgency.setPaymentTerm(paymentTerms);
		session.save(defaultTDSAgency);

		TAXAgency defaultServiceTAXAgency = new TAXAgency();
		defaultServiceTAXAgency.setActive(Boolean.TRUE);
		defaultServiceTAXAgency.setTaxType(TAXAgency.TAX_TYPE_SERVICETAX);
		defaultServiceTAXAgency.setName("Service Tax Agency");
		defaultServiceTAXAgency.setVATReturn(0);
		defaultServiceTAXAgency.setSalesLiabilityAccount(serviceTaxPayable);
		defaultServiceTAXAgency.setPurchaseLiabilityAccount(serviceTaxPayable);
		defaultServiceTAXAgency.setDefault(true);
		defaultServiceTAXAgency.setCompany(company);
		defaultServiceTAXAgency.setPaymentTerm(paymentTerms);
		session.save(defaultServiceTAXAgency);

		TAXAgency defaultCSTAgency = new TAXAgency();
		defaultCSTAgency.setActive(Boolean.TRUE);
		defaultCSTAgency.setTaxType(TAXAgency.TAX_TYPE_SALESTAX);
		defaultCSTAgency.setName("Central Sales Tax Agency");
		defaultCSTAgency.setVATReturn(0);
		defaultCSTAgency.setSalesLiabilityAccount(cstPayable);
		defaultCSTAgency.setDefault(true);
		defaultCSTAgency.setCompany(company);
		defaultCSTAgency.setPaymentTerm(paymentTerms);
		session.save(defaultCSTAgency);

		TAXItem tdsItem1 = new TAXItem(company);
		tdsItem1.setName("Exempt Purchases");
		tdsItem1.setActive(true);
		tdsItem1.setDescription("Exempt Purchases");
		tdsItem1.setTaxRate(0.0);
		tdsItem1.setTaxAgency(defaultTDSAgency);
		tdsItem1.setVatReturnBox(null);
		tdsItem1.setDefault(true);
		tdsItem1.setPercentage(true);
		session.save(tdsItem1);

		TAXItem tdsItem2 = new TAXItem(company);
		tdsItem2.setName("Professional");
		tdsItem2.setActive(true);
		tdsItem2.setDescription("Professional");
		tdsItem2.setTaxRate(10);
		tdsItem2.setTaxAgency(defaultTDSAgency);
		tdsItem1.setVatReturnBox(null);
		tdsItem2.setDefault(true);
		tdsItem2.setPercentage(true);
		session.save(tdsItem2);

		TAXItem tdsItem3 = new TAXItem(company);
		tdsItem3.setName("Contractors");
		tdsItem3.setActive(true);
		tdsItem3.setDescription("Contractors");
		tdsItem3.setTaxRate(2);
		tdsItem3.setTaxAgency(defaultTDSAgency);
		tdsItem1.setVatReturnBox(null);
		tdsItem3.setDefault(true);
		tdsItem3.setPercentage(true);
		session.save(tdsItem3);

		TAXItem tdsItem4 = new TAXItem(company);
		tdsItem4.setName("Sub Contractors");
		tdsItem4.setActive(true);
		tdsItem4.setDescription("Sub Contractors");
		tdsItem4.setTaxRate(1);
		tdsItem4.setTaxAgency(defaultTDSAgency);
		tdsItem1.setVatReturnBox(null);
		tdsItem4.setDefault(true);
		tdsItem4.setPercentage(true);
		session.save(tdsItem4);

		TAXCode none = new TAXCode(company);
		none.setName("None");
		none.setDescription("None");
		none.setTaxable(false);
		none.setActive(true);
		session.save(none);

		// TAXCode tdsCode1 = new TAXCode();
		//
		// tdsCode1.setName("E");
		// tdsCode1.setDescription("Exempt");
		// tdsCode1.setTaxable(true);
		// tdsCode1.setActive(true);
		// tdsCode1.setTAXItemGrpForPurchases(tdsItem4);
		// tdsCode1.setTAXItemGrpForSales(tdsItem4);
		// tdsCode1.setDefault(true);
		// session.save(tdsCode1);
		// TAXCode tdsCode2 = new TAXCode();
		// tdsCode2.setName("P");
		// tdsCode2.setDescription("Professional");
		// tdsCode2.setTaxable(true);
		// tdsCode2.setActive(true);
		// tdsCode1.setTAXItemGrpForPurchases(tdsItem2);
		// tdsCode1.setTAXItemGrpForSales(tdsItem2);
		// tdsCode2.setDefault(true);
		// session.save(tdsCode2);
		// TAXCode tdsCode3 = new TAXCode();
		// tdsCode3.setName("C");
		// tdsCode3.setDescription("Contractor");
		// tdsCode3.setTaxable(true);
		// tdsCode3.setActive(true);
		// tdsCode1.setTAXItemGrpForPurchases(tdsItem3);
		// tdsCode1.setTAXItemGrpForSales(tdsItem3);
		// tdsCode3.setDefault(true);
		// session.save(tdsCode3);
		// TAXCode tdsCode4 = new TAXCode();
		// tdsCode4.setName("SC");
		// tdsCode4.setDescription("Sub Contractor");
		// tdsCode4.setTaxable(true);
		// tdsCode4.setActive(true);
		// tdsCode1.setTAXItemGrpForPurchases(tdsItem4);
		// tdsCode1.setTAXItemGrpForSales(tdsItem4);
		// tdsCode4.setDefault(true);
		// session.save(tdsCode4);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}
}
