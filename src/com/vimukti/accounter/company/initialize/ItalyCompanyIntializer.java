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
public class ItalyCompanyIntializer extends CompanyInitializer {

	public ItalyCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {

		createDefaultTaxCodes();
	}

	private void createDefaultTaxCodes() {

		// Creating payble account for VAT

		Account vatPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.LIABLITY_VATPAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny for Italy company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultGSTAgency = new TAXAgency();
		defaultGSTAgency.setActive(Boolean.TRUE);
		defaultGSTAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultGSTAgency.setName("IVA Tax Agency");
		defaultGSTAgency.setVATReturn(0);
		defaultGSTAgency.setPurchaseLiabilityAccount(vatPayable);
		defaultGSTAgency.setSalesLiabilityAccount(vatPayable);
		defaultGSTAgency.setDefault(true);
		defaultGSTAgency.setCompany(company);
		session.save(defaultGSTAgency);

		// Creating TaxItems

		TAXItem standardTaxItem = new TAXItem(company);
		standardTaxItem.setName("IVA  21%");
		standardTaxItem.setActive(true);
		standardTaxItem.setDescription("IVA 21%");
		standardTaxItem.setTaxRate(21.0);
		standardTaxItem.setTaxAgency(defaultGSTAgency);
		standardTaxItem.setVatReturnBox(null);
		standardTaxItem.setDefault(true);
		standardTaxItem.setPercentage(true);
		session.save(standardTaxItem);

		TAXItem standardTaxItem1 = new TAXItem(company);
		standardTaxItem1.setName("IVA  23%");
		standardTaxItem1.setActive(true);
		standardTaxItem1.setDescription("IVA 23%");
		standardTaxItem1.setTaxRate(23.0);
		standardTaxItem1.setTaxAgency(defaultGSTAgency);
		standardTaxItem1.setVatReturnBox(null);
		standardTaxItem1.setDefault(true);
		standardTaxItem1.setPercentage(true);
		session.save(standardTaxItem1);

		TAXItem reducedTaxItem = new TAXItem(company);
		reducedTaxItem.setName("IVA  10%");
		reducedTaxItem.setActive(true);
		reducedTaxItem.setDescription("IVA  10%");
		reducedTaxItem.setTaxRate(10.0);
		reducedTaxItem.setTaxAgency(defaultGSTAgency);
		reducedTaxItem.setVatReturnBox(null);
		reducedTaxItem.setDefault(true);
		reducedTaxItem.setPercentage(true);
		session.save(reducedTaxItem);

		TAXItem reducedTaxItem1 = new TAXItem(company);
		reducedTaxItem1.setName("IVA  4%");
		reducedTaxItem1.setActive(true);
		reducedTaxItem1.setDescription("IVA 4%");
		reducedTaxItem1.setTaxRate(4.0);
		reducedTaxItem1.setTaxAgency(defaultGSTAgency);
		reducedTaxItem1.setVatReturnBox(null);
		reducedTaxItem1.setDefault(true);
		reducedTaxItem1.setPercentage(true);
		session.save(reducedTaxItem1);

		TAXItem exemptTaxItem = new TAXItem(company);
		exemptTaxItem.setName("Esente IVA ");
		exemptTaxItem.setActive(true);
		exemptTaxItem.setDescription("Esente IVA");
		exemptTaxItem.setTaxRate(0.0);
		exemptTaxItem.setTaxAgency(defaultGSTAgency);
		exemptTaxItem.setVatReturnBox(null);
		exemptTaxItem.setDefault(true);
		exemptTaxItem.setPercentage(true);
		session.save(exemptTaxItem);

		TAXItem zeroRatedTaxItem = new TAXItem(company);
		zeroRatedTaxItem.setName("Zero nominale  0.0%");
		zeroRatedTaxItem.setActive(true);
		zeroRatedTaxItem.setDescription("Zero nominale 0.0%");
		zeroRatedTaxItem.setTaxRate(0.0);
		zeroRatedTaxItem.setTaxAgency(defaultGSTAgency);
		zeroRatedTaxItem.setVatReturnBox(null);
		zeroRatedTaxItem.setDefault(true);
		zeroRatedTaxItem.setPercentage(true);
		session.save(zeroRatedTaxItem);

		// Creating TaxCodes

		TAXCode standardCode = new TAXCode(company);
		standardCode.setName("IVA  21%");
		standardCode.setDescription("IVA  21%");
		standardCode.setTaxable(true);
		standardCode.setActive(true);
		standardCode.setTAXItemGrpForPurchases(standardTaxItem);
		standardCode.setTAXItemGrpForSales(standardTaxItem);
		standardCode.setDefault(true);
		session.save(standardCode);

		TAXCode standardCode1 = new TAXCode(company);
		standardCode1.setName("IVA  23%");
		standardCode1.setDescription("IVA  23%");
		standardCode1.setTaxable(true);
		standardCode1.setActive(true);
		standardCode1.setTAXItemGrpForPurchases(standardTaxItem1);
		standardCode1.setTAXItemGrpForSales(standardTaxItem1);
		standardCode1.setDefault(true);
		session.save(standardCode1);

		TAXCode reducedCode = new TAXCode(company);
		reducedCode.setName("IVA  10%");
		reducedCode.setDescription("IVA  10%");
		reducedCode.setTaxable(true);
		reducedCode.setActive(true);
		reducedCode.setTAXItemGrpForPurchases(reducedTaxItem);
		reducedCode.setTAXItemGrpForSales(reducedTaxItem);
		reducedCode.setDefault(true);
		session.save(reducedCode);

		TAXCode reducedCode1 = new TAXCode(company);
		reducedCode1.setName("IVA  4%");
		reducedCode1.setDescription("IVA  4%");
		reducedCode1.setTaxable(true);
		reducedCode1.setActive(true);
		reducedCode1.setTAXItemGrpForPurchases(reducedTaxItem1);
		reducedCode1.setTAXItemGrpForSales(reducedTaxItem1);
		reducedCode1.setDefault(true);
		session.save(reducedCode1);

		TAXCode exemptCode = new TAXCode(company);
		exemptCode.setName("Esente IVA");
		exemptCode.setDescription("Esente IVA");
		exemptCode.setTaxable(true);
		exemptCode.setActive(true);
		exemptCode.setTAXItemGrpForPurchases(exemptTaxItem);
		exemptCode.setTAXItemGrpForSales(exemptTaxItem);
		exemptCode.setDefault(true);
		session.save(exemptCode);

		TAXCode zeroRatedCode = new TAXCode(company);
		zeroRatedCode.setName("Zero nominale 0.0%");
		zeroRatedCode.setDescription("Zero nominale 0.0%");
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
