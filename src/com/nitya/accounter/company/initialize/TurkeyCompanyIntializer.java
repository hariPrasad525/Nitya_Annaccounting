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
public class TurkeyCompanyIntializer extends CompanyInitializer {

	public TurkeyCompanyIntializer(Company company) {
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

		// Creating default TaxAgecny for Turkey company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultGSTAgency = new TAXAgency();
		defaultGSTAgency.setActive(Boolean.TRUE);
		defaultGSTAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultGSTAgency.setName("Katma Değer Vergisi - KDV");
		defaultGSTAgency.setVATReturn(0);
		defaultGSTAgency.setPurchaseLiabilityAccount(vatPayable);
		defaultGSTAgency.setSalesLiabilityAccount(vatPayable);
		defaultGSTAgency.setDefault(true);
		defaultGSTAgency.setCompany(company);
		session.save(defaultGSTAgency);

		// Creating TaxItems

		TAXItem standardTaxItem = new TAXItem(company);
		standardTaxItem.setName("KDV  18%");
		standardTaxItem.setActive(true);
		standardTaxItem.setDescription("KDV(Katma Değer Vergisi) 18%");
		standardTaxItem.setTaxRate(18.0);
		standardTaxItem.setTaxAgency(defaultGSTAgency);
		standardTaxItem.setVatReturnBox(null);
		standardTaxItem.setDefault(true);
		standardTaxItem.setPercentage(true);
		session.save(standardTaxItem);

		TAXItem standardTaxItem1 = new TAXItem(company);
		standardTaxItem1.setName("KDV  1%");
		standardTaxItem1.setActive(true);
		standardTaxItem1.setDescription("Katma Değer Vergisi");
		standardTaxItem1.setTaxRate(1.0);
		standardTaxItem1.setTaxAgency(defaultGSTAgency);
		standardTaxItem1.setVatReturnBox(null);
		standardTaxItem1.setDefault(true);
		standardTaxItem1.setPercentage(true);
		session.save(standardTaxItem1);

		TAXItem standardTaxItem2 = new TAXItem(company);
		standardTaxItem2.setName("KDV  8%");
		standardTaxItem2.setActive(true);
		standardTaxItem2.setDescription("Katma Değer Vergisi");
		standardTaxItem2.setTaxRate(8.0);
		standardTaxItem2.setTaxAgency(defaultGSTAgency);
		standardTaxItem2.setVatReturnBox(null);
		standardTaxItem2.setDefault(true);
		standardTaxItem2.setPercentage(true);
		session.save(standardTaxItem2);

		TAXItem exemptTaxItem = new TAXItem(company);
		exemptTaxItem.setName("muaf");
		exemptTaxItem.setActive(true);
		exemptTaxItem.setDescription("muaf");
		exemptTaxItem.setTaxRate(0.0);
		exemptTaxItem.setTaxAgency(defaultGSTAgency);
		exemptTaxItem.setVatReturnBox(null);
		exemptTaxItem.setDefault(true);
		exemptTaxItem.setPercentage(true);
		session.save(exemptTaxItem);

		TAXItem zeroRatedTaxItem = new TAXItem(company);
		zeroRatedTaxItem.setName("Sıfır beğenilenler  0.0%");
		zeroRatedTaxItem.setActive(true);
		zeroRatedTaxItem.setDescription("Sıfır beğenilenler 0.0%");
		zeroRatedTaxItem.setTaxRate(0.0);
		zeroRatedTaxItem.setTaxAgency(defaultGSTAgency);
		zeroRatedTaxItem.setVatReturnBox(null);
		zeroRatedTaxItem.setDefault(true);
		zeroRatedTaxItem.setPercentage(true);
		session.save(zeroRatedTaxItem);

		// Creating TaxCodes

		TAXCode standardCode = new TAXCode(company);
		standardCode.setName("KDV  18%");
		standardCode.setDescription("KDV  18%");
		standardCode.setTaxable(true);
		standardCode.setActive(true);
		standardCode.setTAXItemGrpForPurchases(standardTaxItem);
		standardCode.setTAXItemGrpForSales(standardTaxItem);
		standardCode.setDefault(true);
		session.save(standardCode);

		TAXCode standardCode1 = new TAXCode(company);
		standardCode1.setName("KDV  1%");
		standardCode1.setDescription("KDV  1%");
		standardCode1.setTaxable(true);
		standardCode1.setActive(true);
		standardCode1.setTAXItemGrpForPurchases(standardTaxItem1);
		standardCode1.setTAXItemGrpForSales(standardTaxItem1);
		standardCode1.setDefault(true);
		session.save(standardCode1);

		TAXCode standardCode2 = new TAXCode(company);
		standardCode2.setName("KDV  8%");
		standardCode2.setDescription("KDV  8%");
		standardCode2.setTaxable(true);
		standardCode2.setActive(true);
		standardCode2.setTAXItemGrpForPurchases(standardTaxItem2);
		standardCode2.setTAXItemGrpForSales(standardTaxItem2);
		standardCode2.setDefault(true);
		session.save(standardCode2);

		TAXCode exemptCode = new TAXCode(company);
		exemptCode.setName("muaf");
		exemptCode.setDescription("muaf");
		exemptCode.setTaxable(true);
		exemptCode.setActive(true);
		exemptCode.setTAXItemGrpForPurchases(exemptTaxItem);
		exemptCode.setTAXItemGrpForSales(exemptTaxItem);
		exemptCode.setDefault(true);
		session.save(exemptCode);

		TAXCode zeroRatedCode = new TAXCode(company);
		zeroRatedCode.setName("Sıfır beğenilenler  0.0%");
		zeroRatedCode.setDescription("Sıfır beğenilenler  0.0%");
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
