package com.vimukti.accounter.company.initialize;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * 
 * @author Sai Prasad N
 * 
 */

public class IndonesianCompanyIntializer extends CompanyInitializer {

	public IndonesianCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {

		createDeafultTaxCodes();

	}

	private void createDeafultTaxCodes() {

		// Creating payble account for VAT

		Account vatPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.LIABLITY_VATPAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultGSTAgency = new TAXAgency();
		defaultGSTAgency.setActive(Boolean.TRUE);
		defaultGSTAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultGSTAgency.setName("PPN Pajak Badan");
		defaultGSTAgency.setVATReturn(0);
		defaultGSTAgency.setPurchaseLiabilityAccount(vatPayable);
		defaultGSTAgency.setSalesLiabilityAccount(vatPayable);
		defaultGSTAgency.setDefault(true);
		defaultGSTAgency.setCompany(company);
		session.save(defaultGSTAgency);

		// Creating TaxItems

		TAXItem standardTaxItem = new TAXItem(company);
		standardTaxItem.setName("PPN  10%");
		standardTaxItem.setActive(true);
		standardTaxItem.setDescription("PPN 10%");
		standardTaxItem.setTaxRate(10.0);
		standardTaxItem.setTaxAgency(defaultGSTAgency);
		standardTaxItem.setVatReturnBox(null);
		standardTaxItem.setDefault(true);
		standardTaxItem.setPercentage(true);
		session.save(standardTaxItem);

		TAXItem standardTaxItem1 = new TAXItem(company);
		standardTaxItem1.setName("PPN  1%");
		standardTaxItem1.setActive(true);
		standardTaxItem1.setDescription("PPN 1%");
		standardTaxItem1.setTaxRate(1.0);
		standardTaxItem1.setTaxAgency(defaultGSTAgency);
		standardTaxItem1.setVatReturnBox(null);
		standardTaxItem1.setDefault(true);
		standardTaxItem1.setPercentage(true);
		session.save(standardTaxItem1);

		TAXItem standardTaxItem2 = new TAXItem(company);
		standardTaxItem2.setName("PPN  5%");
		standardTaxItem2.setActive(true);
		standardTaxItem2.setDescription("PPN 5%");
		standardTaxItem2.setTaxRate(5.0);
		standardTaxItem2.setTaxAgency(defaultGSTAgency);
		standardTaxItem2.setVatReturnBox(null);
		standardTaxItem2.setDefault(true);
		standardTaxItem2.setPercentage(true);
		session.save(standardTaxItem2);

		TAXItem exemptTaxItem = new TAXItem(company);
		exemptTaxItem.setName("bebas");
		exemptTaxItem.setActive(true);
		exemptTaxItem.setDescription("bebas");
		exemptTaxItem.setTaxRate(0.0);
		exemptTaxItem.setTaxAgency(defaultGSTAgency);
		exemptTaxItem.setVatReturnBox(null);
		exemptTaxItem.setDefault(true);
		exemptTaxItem.setPercentage(true);
		session.save(exemptTaxItem);

		TAXItem zeroRatedTaxItem = new TAXItem(company);
		zeroRatedTaxItem.setName("Nilai nol 0.0%");
		zeroRatedTaxItem.setActive(true);
		zeroRatedTaxItem.setDescription("Nilai nol 0.0%");
		zeroRatedTaxItem.setTaxRate(0.0);
		zeroRatedTaxItem.setTaxAgency(defaultGSTAgency);
		zeroRatedTaxItem.setVatReturnBox(null);
		zeroRatedTaxItem.setDefault(true);
		zeroRatedTaxItem.setPercentage(true);
		session.save(zeroRatedTaxItem);

		// Creating TaxCodes

		TAXCode standardCode = new TAXCode(company);
		standardCode.setName("PPN  10%");
		standardCode.setDescription("PPN  10%");
		standardCode.setTaxable(true);
		standardCode.setActive(true);
		standardCode.setTAXItemGrpForPurchases(standardTaxItem);
		standardCode.setTAXItemGrpForSales(standardTaxItem);
		standardCode.setDefault(true);
		session.save(standardCode);

		TAXCode standardCode1 = new TAXCode(company);
		standardCode1.setName("PPN 1%");
		standardCode1.setDescription("PPN 1%");
		standardCode1.setTaxable(true);
		standardCode1.setActive(true);
		standardCode1.setTAXItemGrpForPurchases(standardTaxItem1);
		standardCode1.setTAXItemGrpForSales(standardTaxItem1);
		standardCode1.setDefault(true);
		session.save(standardCode1);

		TAXCode standardCode2 = new TAXCode(company);
		standardCode2.setName("PPN 5%");
		standardCode2.setDescription("PPN 5%");
		standardCode2.setTaxable(true);
		standardCode2.setActive(true);
		standardCode2.setTAXItemGrpForPurchases(standardTaxItem2);
		standardCode2.setTAXItemGrpForSales(standardTaxItem2);
		standardCode2.setDefault(true);
		session.save(standardCode2);

		TAXCode exemptCode = new TAXCode(company);
		exemptCode.setName("bebas");
		exemptCode.setDescription("bebas");
		exemptCode.setTaxable(true);
		exemptCode.setActive(true);
		exemptCode.setTAXItemGrpForPurchases(exemptTaxItem);
		exemptCode.setTAXItemGrpForSales(exemptTaxItem);
		exemptCode.setDefault(true);
		session.save(exemptCode);

		TAXCode zeroRatedCode = new TAXCode(company);
		zeroRatedCode.setName("Nilai nol  0.0%");
		zeroRatedCode.setDescription("Nilai nol  0.0%");
		zeroRatedCode.setTaxable(true);
		zeroRatedCode.setActive(true);
		zeroRatedCode.setTAXItemGrpForPurchases(zeroRatedTaxItem);
		zeroRatedCode.setTAXItemGrpForSales(zeroRatedTaxItem);
		zeroRatedCode.setDefault(true);
		session.save(zeroRatedCode);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
