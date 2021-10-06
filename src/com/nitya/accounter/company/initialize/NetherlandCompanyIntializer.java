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
public class NetherlandCompanyIntializer extends CompanyInitializer {

	public NetherlandCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {

		createDefaultTaxCodes();

	}

	private void createDefaultTaxCodes() {

		// Creating payble account for GST

		Account gstPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.GST_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny for NewZealand company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultGSTAgency = new TAXAgency();
		defaultGSTAgency.setActive(Boolean.TRUE);
		defaultGSTAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultGSTAgency.setName("BTW TaxAgency");
		defaultGSTAgency.setVATReturn(0);
		defaultGSTAgency.setPurchaseLiabilityAccount(gstPayable);
		defaultGSTAgency.setSalesLiabilityAccount(gstPayable);
		defaultGSTAgency.setDefault(true);
		defaultGSTAgency.setCompany(company);
		session.save(defaultGSTAgency);

		// Creating TaxItems

		TAXItem btwFoodAndLuxioursTaxItem = new TAXItem(company);
		btwFoodAndLuxioursTaxItem.setName("BTW  6%");
		btwFoodAndLuxioursTaxItem.setActive(true);
		btwFoodAndLuxioursTaxItem.setDescription("BTW 6%");
		btwFoodAndLuxioursTaxItem.setTaxRate(6.0);
		btwFoodAndLuxioursTaxItem.setTaxAgency(defaultGSTAgency);
		btwFoodAndLuxioursTaxItem.setVatReturnBox(null);
		btwFoodAndLuxioursTaxItem.setDefault(true);
		btwFoodAndLuxioursTaxItem.setPercentage(true);
		session.save(btwFoodAndLuxioursTaxItem);

		TAXItem btwNonFoodAndLuxioursTaxItem = new TAXItem(company);
		btwNonFoodAndLuxioursTaxItem.setName("BTW  19%");
		btwNonFoodAndLuxioursTaxItem.setActive(true);
		btwNonFoodAndLuxioursTaxItem.setDescription("BTW 19%");
		btwNonFoodAndLuxioursTaxItem.setTaxRate(19.0);
		btwNonFoodAndLuxioursTaxItem.setTaxAgency(defaultGSTAgency);
		btwNonFoodAndLuxioursTaxItem.setVatReturnBox(null);
		btwNonFoodAndLuxioursTaxItem.setDefault(true);
		btwNonFoodAndLuxioursTaxItem.setPercentage(true);
		session.save(btwNonFoodAndLuxioursTaxItem);

		TAXCode btwFoodCode = new TAXCode(company);
		btwFoodCode.setName("BTW 6%");
		btwFoodCode.setDescription("BTW 6%");
		btwFoodCode.setTaxable(true);
		btwFoodCode.setActive(true);
		btwFoodCode.setTAXItemGrpForPurchases(btwFoodAndLuxioursTaxItem);
		btwFoodCode.setTAXItemGrpForSales(btwFoodAndLuxioursTaxItem);
		btwFoodCode.setDefault(true);
		session.save(btwFoodCode);

		TAXCode btwNonFoodCode = new TAXCode(company);
		btwNonFoodCode.setName("BTW 19%");
		btwNonFoodCode.setDescription("BTW 19%");
		btwNonFoodCode.setTaxable(true);
		btwNonFoodCode.setActive(true);
		btwNonFoodCode.setTAXItemGrpForPurchases(btwNonFoodAndLuxioursTaxItem);
		btwNonFoodCode.setTAXItemGrpForSales(btwNonFoodAndLuxioursTaxItem);
		btwNonFoodCode.setDefault(true);
		session.save(btwNonFoodCode);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
