package com.nitya.accounter.company.initialize;

import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterServerConstants;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.TAXItem;
import com.nitya.accounter.utils.HibernateUtil;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class FranceCompanyIntializer extends CompanyInitializer {

	public FranceCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {

		createDefaultTaxCodes();
	}

	private void createDefaultTaxCodes() {

		// Creating payble account for TVA

		Account tvaPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.GST_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultGSTAgency = new TAXAgency();
		defaultGSTAgency.setActive(Boolean.TRUE);
		defaultGSTAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultGSTAgency.setName("TVA fiscaux de l'Agence");
		defaultGSTAgency.setVATReturn(0);
		defaultGSTAgency.setPurchaseLiabilityAccount(tvaPayable);
		defaultGSTAgency.setSalesLiabilityAccount(tvaPayable);
		defaultGSTAgency.setDefault(true);
		defaultGSTAgency.setCompany(company);
		session.save(defaultGSTAgency);

		// Creating Tax Items for purchases

		TAXItem tvaStandardTaxItem = new TAXItem(company);
		tvaStandardTaxItem.setName("tarif standard 19.6%");
		tvaStandardTaxItem.setActive(true);
		tvaStandardTaxItem.setDescription("tarif standard 19.6%");
		tvaStandardTaxItem.setTaxRate(19.6);
		tvaStandardTaxItem.setTaxAgency(defaultGSTAgency);
		tvaStandardTaxItem.setVatReturnBox(null);
		tvaStandardTaxItem.setDefault(true);
		tvaStandardTaxItem.setPercentage(true);
		session.save(tvaStandardTaxItem);

		TAXItem tvaSpecificTaxItem = new TAXItem(company);
		tvaSpecificTaxItem.setName("taux spécifique 2.1%");
		tvaSpecificTaxItem.setActive(true);
		tvaSpecificTaxItem.setDescription("taux spécifique 2.1%");
		tvaSpecificTaxItem.setTaxRate(2.1);
		tvaSpecificTaxItem.setTaxAgency(defaultGSTAgency);
		tvaSpecificTaxItem.setVatReturnBox(null);
		tvaSpecificTaxItem.setDefault(true);
		tvaSpecificTaxItem.setPercentage(true);
		session.save(tvaSpecificTaxItem);

		TAXItem tvaReducedTaxItem = new TAXItem(company);
		tvaReducedTaxItem.setName("tarif réduit 5.5%");
		tvaReducedTaxItem.setActive(true);
		tvaReducedTaxItem.setDescription("tarif réduit 5.5%");
		tvaReducedTaxItem.setTaxRate(5.5);
		tvaReducedTaxItem.setTaxAgency(defaultGSTAgency);
		tvaReducedTaxItem.setVatReturnBox(null);
		tvaReducedTaxItem.setDefault(true);
		tvaReducedTaxItem.setPercentage(true);
		session.save(tvaReducedTaxItem);

		// Creating TaxCodes

		TAXCode tvaStandardCode = new TAXCode(company);
		tvaStandardCode.setName("tarif standard 19.6%");
		tvaStandardCode.setDescription("tarif standard 19.6%");
		tvaStandardCode.setTaxable(true);
		tvaStandardCode.setActive(true);
		tvaStandardCode.setTAXItemGrpForPurchases(tvaStandardTaxItem);
		tvaStandardCode.setTAXItemGrpForSales(tvaStandardTaxItem);
		tvaStandardCode.setDefault(true);
		session.save(tvaStandardCode);

		TAXCode tvaSpecificCode = new TAXCode(company);
		tvaSpecificCode.setName("taux spécifique 2.1%");
		tvaSpecificCode.setDescription("taux spécifique 2.1%");
		tvaSpecificCode.setTaxable(true);
		tvaSpecificCode.setActive(true);
		tvaSpecificCode.setTAXItemGrpForPurchases(tvaSpecificTaxItem);
		tvaSpecificCode.setTAXItemGrpForSales(tvaSpecificTaxItem);
		tvaSpecificCode.setDefault(true);
		session.save(tvaSpecificCode);

		TAXCode tvaReducedCode = new TAXCode(company);
		tvaReducedCode.setName("tarif réduit 5.5%");
		tvaReducedCode.setDescription("tarif réduit 5.5%");
		tvaReducedCode.setTaxable(true);
		tvaReducedCode.setActive(true);
		tvaReducedCode.setTAXItemGrpForPurchases(tvaReducedTaxItem);
		tvaReducedCode.setTAXItemGrpForSales(tvaReducedTaxItem);
		tvaReducedCode.setDefault(true);

		session.save(tvaReducedCode);
	}

	@Override
	String getDateFormat() {

		return AccounterServerConstants.ddMMyyyy;
	}

}
