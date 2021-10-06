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
public class GermanyCompanyIntializer extends CompanyInitializer {

	public GermanyCompanyIntializer(Company company) {
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

		// Creating default TaxAgecny for Germany company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultGSTAgency = new TAXAgency();
		defaultGSTAgency.setActive(Boolean.TRUE);
		defaultGSTAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultGSTAgency.setName("Mehrwertsteuer Tax Agency");
		defaultGSTAgency.setVATReturn(0);
		defaultGSTAgency.setPurchaseLiabilityAccount(vatPayable);
		defaultGSTAgency.setSalesLiabilityAccount(vatPayable);
		defaultGSTAgency.setDefault(true);
		defaultGSTAgency.setCompany(company);
		session.save(defaultGSTAgency);

		// Creating TaxItems

		TAXItem btwFoodAndLuxioursTaxItem = new TAXItem(company);
		btwFoodAndLuxioursTaxItem.setName("MwSt.  7%");
		btwFoodAndLuxioursTaxItem.setActive(true);
		btwFoodAndLuxioursTaxItem.setDescription("MwSt. 7%");
		btwFoodAndLuxioursTaxItem.setTaxRate(7.0);
		btwFoodAndLuxioursTaxItem.setTaxAgency(defaultGSTAgency);
		btwFoodAndLuxioursTaxItem.setVatReturnBox(null);
		btwFoodAndLuxioursTaxItem.setDefault(true);
		btwFoodAndLuxioursTaxItem.setPercentage(true);
		session.save(btwFoodAndLuxioursTaxItem);

		TAXItem btwNonFoodAndLuxioursTaxItem = new TAXItem(company);
		btwNonFoodAndLuxioursTaxItem.setName("MwSt.  19%");
		btwNonFoodAndLuxioursTaxItem.setActive(true);
		btwNonFoodAndLuxioursTaxItem.setDescription("MwSt. 19%");
		btwNonFoodAndLuxioursTaxItem.setTaxRate(19.0);
		btwNonFoodAndLuxioursTaxItem.setTaxAgency(defaultGSTAgency);
		btwNonFoodAndLuxioursTaxItem.setVatReturnBox(null);
		btwNonFoodAndLuxioursTaxItem.setDefault(true);
		btwNonFoodAndLuxioursTaxItem.setPercentage(true);
		session.save(btwNonFoodAndLuxioursTaxItem);

		TAXItem exemptTaxItem = new TAXItem(company);
		exemptTaxItem.setName("befreit");
		exemptTaxItem.setActive(true);
		exemptTaxItem.setDescription("befreit");
		exemptTaxItem.setTaxRate(0.0);
		exemptTaxItem.setTaxAgency(defaultGSTAgency);
		exemptTaxItem.setVatReturnBox(null);
		exemptTaxItem.setDefault(true);
		exemptTaxItem.setPercentage(true);
		session.save(exemptTaxItem);

		TAXItem zeroratedTaxItem = new TAXItem(company);
		zeroratedTaxItem.setName("Nullsatz 0.0%");
		zeroratedTaxItem.setActive(true);
		zeroratedTaxItem.setDescription("Nullsatz  0.0%");
		zeroratedTaxItem.setTaxRate(0.0);
		zeroratedTaxItem.setTaxAgency(defaultGSTAgency);
		zeroratedTaxItem.setVatReturnBox(null);
		zeroratedTaxItem.setDefault(true);
		zeroratedTaxItem.setPercentage(true);
		session.save(zeroratedTaxItem);

		// Creating TaxCodes

		TAXCode btwFoodCode = new TAXCode(company);
		btwFoodCode.setName("MwSt. 7%");
		btwFoodCode.setDescription("MwSt. 7%");
		btwFoodCode.setTaxable(true);
		btwFoodCode.setActive(true);
		btwFoodCode.setTAXItemGrpForPurchases(btwFoodAndLuxioursTaxItem);
		btwFoodCode.setTAXItemGrpForSales(btwFoodAndLuxioursTaxItem);
		btwFoodCode.setDefault(true);
		session.save(btwFoodCode);

		TAXCode btwNonFoodCode = new TAXCode(company);
		btwNonFoodCode.setName("MwSt. 19%");
		btwNonFoodCode.setDescription("MwSt. 19%");
		btwNonFoodCode.setTaxable(true);
		btwNonFoodCode.setActive(true);
		btwNonFoodCode.setTAXItemGrpForPurchases(btwNonFoodAndLuxioursTaxItem);
		btwNonFoodCode.setTAXItemGrpForSales(btwNonFoodAndLuxioursTaxItem);
		btwNonFoodCode.setDefault(true);
		session.save(btwNonFoodCode);

		TAXCode gstExemptCode = new TAXCode(company);
		gstExemptCode.setName("befreit");
		gstExemptCode.setDescription("befreit");
		gstExemptCode.setTaxable(true);
		gstExemptCode.setActive(true);
		gstExemptCode.setTAXItemGrpForPurchases(exemptTaxItem);
		gstExemptCode.setTAXItemGrpForSales(exemptTaxItem);
		gstExemptCode.setDefault(true);
		session.save(gstExemptCode);

		TAXCode zeroRatedCode = new TAXCode(company);
		zeroRatedCode.setName("Nullsatz 0.0%");
		zeroRatedCode.setDescription("Nullsatz 0.0%");
		zeroRatedCode.setTaxable(true);
		zeroRatedCode.setActive(true);
		zeroRatedCode.setTAXItemGrpForPurchases(zeroratedTaxItem);
		zeroRatedCode.setTAXItemGrpForSales(zeroratedTaxItem);
		zeroRatedCode.setDefault(true);
		session.save(zeroRatedCode);
	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
