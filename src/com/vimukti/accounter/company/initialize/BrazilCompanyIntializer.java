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
public class BrazilCompanyIntializer extends CompanyInitializer {

	public BrazilCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {

		createDeaultTaxCodes();
	}

	private void createDeaultTaxCodes() {
		// Creating payble account for VAT

		Account icmsPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.LIABLITY_VATPAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating payble account for ISS

		Account issPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.LIABLITY_VATPAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny for Brazil company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultICMSTAgency = new TAXAgency();
		defaultICMSTAgency.setActive(Boolean.TRUE);
		defaultICMSTAgency.setTaxType(TAXAgency.TAX_TYPE_SALESTAX);
		defaultICMSTAgency.setName("ICMS Tax Agency");
		defaultICMSTAgency.setVATReturn(0);
		defaultICMSTAgency.setPurchaseLiabilityAccount(icmsPayable);
		defaultICMSTAgency.setSalesLiabilityAccount(icmsPayable);
		defaultICMSTAgency.setDefault(true);
		defaultICMSTAgency.setCompany(company);
		session.save(defaultICMSTAgency);

		TAXAgency defaultISSTAgency = new TAXAgency();
		defaultISSTAgency.setActive(Boolean.TRUE);
		defaultISSTAgency.setTaxType(TAXAgency.TAX_TYPE_SERVICETAX);
		defaultISSTAgency.setName("Governed by a Federal Law(ISS)");
		defaultISSTAgency.setVATReturn(0);
		defaultISSTAgency.setPurchaseLiabilityAccount(issPayable);
		defaultISSTAgency.setSalesLiabilityAccount(issPayable);
		defaultISSTAgency.setDefault(true);
		defaultISSTAgency.setCompany(company);
		session.save(defaultISSTAgency);

		// Creating TaxItems

		TAXItem icms18TaxItem = new TAXItem(company);
		icms18TaxItem.setName("ICMS   18%");
		icms18TaxItem.setActive(true);
		icms18TaxItem.setDescription("ICMS  18%");
		icms18TaxItem.setTaxRate(18.0);
		icms18TaxItem.setTaxAgency(defaultICMSTAgency);
		icms18TaxItem.setVatReturnBox(null);
		icms18TaxItem.setDefault(true);
		icms18TaxItem.setPercentage(true);
		session.save(icms18TaxItem);

		TAXItem icms17TaxItem = new TAXItem(company);
		icms17TaxItem.setName("ICMS   17%");
		icms17TaxItem.setActive(true);
		icms17TaxItem.setDescription("ICMS  17%");
		icms17TaxItem.setTaxRate(17.0);
		icms17TaxItem.setTaxAgency(defaultICMSTAgency);
		icms17TaxItem.setVatReturnBox(null);
		icms17TaxItem.setDefault(true);
		icms17TaxItem.setPercentage(true);
		session.save(icms17TaxItem);

		TAXItem icms19TaxItem = new TAXItem(company);
		icms19TaxItem.setName("ICMS   19%");
		icms19TaxItem.setActive(true);
		icms19TaxItem.setDescription("ICMS  19%");
		icms19TaxItem.setTaxRate(19.0);
		icms19TaxItem.setTaxAgency(defaultICMSTAgency);
		icms19TaxItem.setVatReturnBox(null);
		icms19TaxItem.setDefault(true);
		icms19TaxItem.setPercentage(true);
		session.save(icms19TaxItem);

		TAXItem iss5TaxItem = new TAXItem(company);
		iss5TaxItem.setName("ISS   5%");
		iss5TaxItem.setActive(true);
		iss5TaxItem.setDescription("ISS  5%");
		iss5TaxItem.setTaxRate(5.0);
		iss5TaxItem.setTaxAgency(defaultISSTAgency);
		iss5TaxItem.setVatReturnBox(null);
		iss5TaxItem.setDefault(true);
		iss5TaxItem.setPercentage(true);
		session.save(iss5TaxItem);

		// Creating TaxCodes

		TAXCode icms17Code = new TAXCode(company);
		icms17Code.setName("ICMS  17%");
		icms17Code.setDescription("ICMS  17%");
		icms17Code.setTaxable(true);
		icms17Code.setActive(true);
		icms17Code.setTAXItemGrpForPurchases(icms17TaxItem);
		icms17Code.setTAXItemGrpForSales(icms17TaxItem);
		icms17Code.setDefault(true);
		session.save(icms17Code);

		TAXCode icms18Code = new TAXCode(company);
		icms18Code.setName("ICMS  18%");
		icms18Code.setDescription("ICMS  18%");
		icms18Code.setTaxable(true);
		icms18Code.setActive(true);
		icms18Code.setTAXItemGrpForPurchases(icms18TaxItem);
		icms18Code.setTAXItemGrpForSales(icms18TaxItem);
		icms18Code.setDefault(true);
		session.save(icms18Code);

		TAXCode icms19Code = new TAXCode(company);
		icms19Code.setName("ICMS  19%");
		icms19Code.setDescription("ICMS  19%");
		icms19Code.setTaxable(true);
		icms19Code.setActive(true);
		icms19Code.setTAXItemGrpForPurchases(icms19TaxItem);
		icms19Code.setTAXItemGrpForSales(icms19TaxItem);
		icms19Code.setDefault(true);
		session.save(icms19Code);

		TAXCode iss5Code = new TAXCode(company);
		iss5Code.setName("ISS  5%");
		iss5Code.setDescription("ISS  5%");
		iss5Code.setTaxable(true);
		iss5Code.setActive(true);
		iss5Code.setTAXItemGrpForPurchases(iss5TaxItem);
		iss5Code.setTAXItemGrpForSales(iss5TaxItem);
		iss5Code.setDefault(true);
		session.save(iss5Code);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
