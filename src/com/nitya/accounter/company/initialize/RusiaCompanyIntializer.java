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
public class RusiaCompanyIntializer extends CompanyInitializer {

	public RusiaCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {

		createDefaultTaxCodes();
	}

	private void createDefaultTaxCodes() {
		Account vatPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.LIABLITY_VATPAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny for Rusia company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultVATAgency = new TAXAgency();
		defaultVATAgency.setActive(Boolean.TRUE);
		defaultVATAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultVATAgency.setName("�?ДС Tax Agency");
		defaultVATAgency.setVATReturn(0);
		defaultVATAgency.setPurchaseLiabilityAccount(vatPayable);
		defaultVATAgency.setSalesLiabilityAccount(vatPayable);
		defaultVATAgency.setDefault(true);
		defaultVATAgency.setCompany(company);
		session.save(defaultVATAgency);

		TAXItem vat18TaxItem = new TAXItem(company);
		vat18TaxItem.setName("�?ДС 18%");
		vat18TaxItem.setActive(true);
		vat18TaxItem.setDescription("�?ДС 18%");
		vat18TaxItem.setTaxRate(18);
		vat18TaxItem.setTaxAgency(defaultVATAgency);
		vat18TaxItem.setVatReturnBox(null);
		vat18TaxItem.setDefault(true);
		vat18TaxItem.setPercentage(true);
		session.save(vat18TaxItem);

		TAXItem vat10TaxItem = new TAXItem(company);
		vat10TaxItem.setName("�?ДС 10%");
		vat10TaxItem.setActive(true);
		vat10TaxItem.setDescription("�?ДС 10%");
		vat10TaxItem.setTaxRate(10.0);
		vat10TaxItem.setTaxAgency(defaultVATAgency);
		vat10TaxItem.setVatReturnBox(null);
		vat10TaxItem.setDefault(true);
		vat10TaxItem.setPercentage(true);
		session.save(vat10TaxItem);

		TAXItem vatExemptTaxItem = new TAXItem(company);
		vatExemptTaxItem.setName("�?ДС не облагает�?�?");
		vatExemptTaxItem.setActive(true);
		vatExemptTaxItem.setDescription("�?ДС не облагает�?�?");
		vatExemptTaxItem.setTaxRate(0.0);
		vatExemptTaxItem.setTaxAgency(defaultVATAgency);
		vatExemptTaxItem.setVatReturnBox(null);
		vatExemptTaxItem.setDefault(true);
		vatExemptTaxItem.setPercentage(true);
		session.save(vatExemptTaxItem);

		TAXItem vatZeroRatedTaxItem = new TAXItem(company);
		vatZeroRatedTaxItem.setName("нулевой �?тавке 0.0%");
		vatZeroRatedTaxItem.setActive(true);
		vatZeroRatedTaxItem.setDescription("нулевой �?тавке 0.0%");
		vatZeroRatedTaxItem.setTaxRate(10.0);
		vatZeroRatedTaxItem.setTaxAgency(defaultVATAgency);
		vatZeroRatedTaxItem.setVatReturnBox(null);
		vatZeroRatedTaxItem.setDefault(true);
		vatZeroRatedTaxItem.setPercentage(true);
		session.save(vatZeroRatedTaxItem);

		// Creating TaxCodes

		TAXCode vat18Code = new TAXCode(company);
		vat18Code.setName("�?ДС  18%");
		vat18Code.setDescription("�?ДС  18%");
		vat18Code.setTaxable(true);
		vat18Code.setActive(true);
		vat18Code.setTAXItemGrpForPurchases(vat18TaxItem);
		vat18Code.setTAXItemGrpForSales(vat18TaxItem);
		vat18Code.setDefault(true);
		session.save(vat18Code);

		TAXCode vat10Code = new TAXCode(company);
		vat10Code.setName("�?ДС  10%");
		vat10Code.setDescription("�?ДС  10%");
		vat10Code.setTaxable(true);
		vat10Code.setActive(true);
		vat10Code.setTAXItemGrpForPurchases(vat10TaxItem);
		vat10Code.setTAXItemGrpForSales(vat10TaxItem);
		vat10Code.setDefault(true);
		session.save(vat10Code);

		TAXCode vatExemptCode = new TAXCode(company);
		vatExemptCode.setName("�?ДС не облагает�?�?");
		vatExemptCode.setDescription("�?ДС не облагает�?�?");
		vatExemptCode.setTaxable(true);
		vatExemptCode.setActive(true);
		vatExemptCode.setTAXItemGrpForPurchases(vatExemptTaxItem);
		vatExemptCode.setTAXItemGrpForSales(vatExemptTaxItem);
		vatExemptCode.setDefault(true);
		session.save(vatExemptCode);

		TAXCode zeroRatesCode = new TAXCode(company);
		zeroRatesCode.setName("нулевой �?тавке 0.0%");
		zeroRatesCode.setDescription("нулевой �?тавке 0.0%");
		zeroRatesCode.setTaxable(true);
		zeroRatesCode.setActive(true);
		zeroRatesCode.setTAXItemGrpForPurchases(vatZeroRatedTaxItem);
		zeroRatesCode.setTAXItemGrpForSales(vatZeroRatedTaxItem);
		zeroRatesCode.setDefault(true);
		session.save(zeroRatesCode);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
