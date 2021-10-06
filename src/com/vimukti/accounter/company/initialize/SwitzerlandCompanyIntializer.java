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
public class SwitzerlandCompanyIntializer extends CompanyInitializer {

	public SwitzerlandCompanyIntializer(Company company) {
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

		// Creating default TaxAgecny for Brazil company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultVATAgency = new TAXAgency();
		defaultVATAgency.setActive(Boolean.TRUE);
		defaultVATAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultVATAgency.setName("Mehrwertsteuer Tax Agency");
		defaultVATAgency.setVATReturn(0);
		defaultVATAgency.setPurchaseLiabilityAccount(vatPayable);
		defaultVATAgency.setSalesLiabilityAccount(vatPayable);
		defaultVATAgency.setDefault(true);
		defaultVATAgency.setCompany(company);
		session.save(defaultVATAgency);

		TAXItem vat8TaxItem = new TAXItem(company);
		vat8TaxItem.setName("MwSt. 8%");
		vat8TaxItem.setActive(true);
		vat8TaxItem.setDescription("MwSt. 8%");
		vat8TaxItem.setTaxRate(8.0);
		vat8TaxItem.setTaxAgency(defaultVATAgency);
		vat8TaxItem.setVatReturnBox(null);
		vat8TaxItem.setDefault(true);
		vat8TaxItem.setPercentage(true);
		session.save(vat8TaxItem);

		TAXItem vat4TaxItem = new TAXItem(company);
		vat4TaxItem.setName("MwSt. 4%");
		vat4TaxItem.setActive(true);
		vat4TaxItem.setDescription("MwSt. 4%");
		vat4TaxItem.setTaxRate(4.0);
		vat4TaxItem.setTaxAgency(defaultVATAgency);
		vat4TaxItem.setVatReturnBox(null);
		vat4TaxItem.setDefault(true);
		vat4TaxItem.setPercentage(true);
		session.save(vat4TaxItem);

		TAXItem vat2_8TaxItem = new TAXItem(company);
		vat2_8TaxItem.setName("MwSt. 2.8%");
		vat2_8TaxItem.setActive(true);
		vat2_8TaxItem.setDescription("MwSt. 2.8%");
		vat2_8TaxItem.setTaxRate(2.8);
		vat2_8TaxItem.setTaxAgency(defaultVATAgency);
		vat2_8TaxItem.setVatReturnBox(null);
		vat2_8TaxItem.setDefault(true);
		vat2_8TaxItem.setPercentage(true);
		session.save(vat2_8TaxItem);

		TAXItem vatzeroTaxItem = new TAXItem(company);
		vatzeroTaxItem.setName("Zero-Rate MwSt.  0.0%");
		vatzeroTaxItem.setActive(true);
		vatzeroTaxItem.setDescription("Zero-Rate MwSt. 0.0%");
		vatzeroTaxItem.setTaxRate(0.0);
		vatzeroTaxItem.setTaxAgency(defaultVATAgency);
		vatzeroTaxItem.setVatReturnBox(null);
		vatzeroTaxItem.setDefault(true);
		vatzeroTaxItem.setPercentage(true);
		session.save(vatzeroTaxItem);

		TAXItem vatExemptTaxItem = new TAXItem(company);
		vatExemptTaxItem.setName("USt.-frei.");
		vatExemptTaxItem.setActive(true);
		vatExemptTaxItem.setDescription("USt.-frei");
		vatExemptTaxItem.setTaxRate(0);
		vatExemptTaxItem.setTaxAgency(defaultVATAgency);
		vatExemptTaxItem.setVatReturnBox(null);
		vatExemptTaxItem.setDefault(true);
		vatExemptTaxItem.setPercentage(true);
		session.save(vatExemptTaxItem);

		// Creating TaxCodes

		TAXCode vat8Code = new TAXCode(company);
		vat8Code.setName("MwSt.  8%");
		vat8Code.setDescription("MwSt.  8%");
		vat8Code.setTaxable(true);
		vat8Code.setActive(true);
		vat8Code.setTAXItemGrpForPurchases(vat8TaxItem);
		vat8Code.setTAXItemGrpForSales(vat8TaxItem);
		vat8Code.setDefault(true);
		session.save(vat8Code);

		TAXCode vat2_8Code = new TAXCode(company);
		vat2_8Code.setName("MwSt.  2.8%");
		vat2_8Code.setDescription("MwSt.  2.8%");
		vat2_8Code.setTaxable(true);
		vat2_8Code.setActive(true);
		vat2_8Code.setTAXItemGrpForPurchases(vat2_8TaxItem);
		vat2_8Code.setTAXItemGrpForSales(vat2_8TaxItem);
		vat2_8Code.setDefault(true);
		session.save(vat2_8Code);

		TAXCode vat4Code = new TAXCode(company);
		vat4Code.setName("MwSt.  4%");
		vat4Code.setDescription("MwSt.  4%");
		vat4Code.setTaxable(true);
		vat4Code.setActive(true);
		vat4Code.setTAXItemGrpForPurchases(vat4TaxItem);
		vat4Code.setTAXItemGrpForSales(vat4TaxItem);
		vat4Code.setDefault(true);
		session.save(vat4Code);

		TAXCode vatZeroCode = new TAXCode(company);
		vatZeroCode.setName("MwSt.  4%");
		vatZeroCode.setDescription("MwSt.  4%");
		vatZeroCode.setTaxable(true);
		vatZeroCode.setActive(true);
		vatZeroCode.setTAXItemGrpForPurchases(vatzeroTaxItem);
		vatZeroCode.setTAXItemGrpForSales(vatzeroTaxItem);
		vatZeroCode.setDefault(true);
		session.save(vatZeroCode);

		TAXCode vatExemptCode = new TAXCode(company);
		vatExemptCode.setName("USt.-frei.");
		vatExemptCode.setDescription("USt.-frei.");
		vatExemptCode.setTaxable(true);
		vatExemptCode.setActive(true);
		vatExemptCode.setTAXItemGrpForPurchases(vatExemptTaxItem);
		vatExemptCode.setTAXItemGrpForSales(vatExemptTaxItem);
		vatExemptCode.setDefault(true);
		session.save(vatExemptCode);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
