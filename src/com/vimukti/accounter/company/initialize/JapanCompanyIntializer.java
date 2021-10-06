package com.vimukti.accounter.company.initialize;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.utils.HibernateUtil;

public class JapanCompanyIntializer extends CompanyInitializer {

	public JapanCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {

		createDefaultTaxCodes();
	}

	private void createDefaultTaxCodes() {

		// Creating payble account for Consumption tax

		Account consumptionPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.CONSUMPTION_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny for Japan company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultConsumptionAgency = new TAXAgency();
		defaultConsumptionAgency.setActive(Boolean.TRUE);
		defaultConsumptionAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultConsumptionAgency.setName("消費税");
		defaultConsumptionAgency.setVATReturn(0);
		defaultConsumptionAgency
				.setPurchaseLiabilityAccount(consumptionPayable);
		defaultConsumptionAgency.setSalesLiabilityAccount(consumptionPayable);
		defaultConsumptionAgency.setDefault(true);
		defaultConsumptionAgency.setCompany(company);
		session.save(defaultConsumptionAgency);

		// Creating Tax Items

		TAXItem consumptionTaxItem = new TAXItem(company);
		consumptionTaxItem.setName("消費税");
		consumptionTaxItem.setActive(true);
		consumptionTaxItem.setDescription("");
		consumptionTaxItem.setTaxRate(5.0);
		consumptionTaxItem.setTaxAgency(defaultConsumptionAgency);
		consumptionTaxItem.setVatReturnBox(null);
		consumptionTaxItem.setDefault(true);
		consumptionTaxItem.setPercentage(true);
		session.save(consumptionTaxItem);

		TAXItem exemptTaxItem = new TAXItem(company);
		exemptTaxItem.setName("非課税");
		exemptTaxItem.setActive(true);
		exemptTaxItem.setDescription("");
		exemptTaxItem.setTaxRate(0.0);
		exemptTaxItem.setTaxAgency(defaultConsumptionAgency);
		exemptTaxItem.setVatReturnBox(null);
		exemptTaxItem.setDefault(true);
		exemptTaxItem.setPercentage(true);
		session.save(exemptTaxItem);

		// Creating TaxCodes

		TAXCode consumptionCode = new TAXCode(company);
		consumptionCode.setName("消費税 5.0%");
		consumptionCode.setDescription("");
		consumptionCode.setTaxable(true);
		consumptionCode.setActive(true);
		consumptionCode.setTAXItemGrpForPurchases(consumptionTaxItem);
		consumptionCode.setTAXItemGrpForSales(consumptionTaxItem);
		consumptionCode.setDefault(true);
		session.save(consumptionCode);

		TAXCode exemptCode = new TAXCode(company);
		exemptCode.setName("非課税 0.0%");
		exemptCode.setDescription("");
		exemptCode.setTaxable(true);
		exemptCode.setActive(true);
		exemptCode.setTAXItemGrpForPurchases(exemptTaxItem);
		exemptCode.setTAXItemGrpForSales(exemptTaxItem);
		exemptCode.setDefault(true);
		session.save(exemptCode);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.yyyyMMdd;
	}

}
