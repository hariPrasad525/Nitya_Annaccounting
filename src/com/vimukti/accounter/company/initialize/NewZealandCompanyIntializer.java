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
public class NewZealandCompanyIntializer extends CompanyInitializer {

	public NewZealandCompanyIntializer(Company company) {
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
		defaultGSTAgency.setName("GST TaxAgency");
		defaultGSTAgency.setVATReturn(0);
		defaultGSTAgency.setPurchaseLiabilityAccount(gstPayable);
		defaultGSTAgency.setSalesLiabilityAccount(gstPayable);
		defaultGSTAgency.setDefault(true);
		defaultGSTAgency.setCompany(company);
		session.save(defaultGSTAgency);

		// Creating Tax Items for purchases

		TAXItem gstOnIncomeTaxItem = new TAXItem(company);
		gstOnIncomeTaxItem.setName("GST on Income 15%");
		gstOnIncomeTaxItem.setActive(true);
		gstOnIncomeTaxItem.setDescription("GST on Income 15%");
		gstOnIncomeTaxItem.setTaxRate(15.0);
		gstOnIncomeTaxItem.setTaxAgency(defaultGSTAgency);
		gstOnIncomeTaxItem.setVatReturnBox(null);
		gstOnIncomeTaxItem.setDefault(true);
		gstOnIncomeTaxItem.setPercentage(true);
		session.save(gstOnIncomeTaxItem);

		TAXItem gstOnExpensesTaxItem = new TAXItem(company);
		gstOnExpensesTaxItem.setName("GST on Expenses 15%");
		gstOnExpensesTaxItem.setActive(true);
		gstOnExpensesTaxItem.setDescription("GST on Expenses 15%");
		gstOnExpensesTaxItem.setTaxRate(15.0);
		gstOnExpensesTaxItem.setTaxAgency(defaultGSTAgency);
		gstOnExpensesTaxItem.setVatReturnBox(null);
		gstOnExpensesTaxItem.setDefault(true);
		gstOnExpensesTaxItem.setPercentage(true);
		session.save(gstOnExpensesTaxItem);

		TAXItem gstOnIncomeTaxItem1 = new TAXItem(company);
		gstOnIncomeTaxItem1.setName("GST on Income 12.5%");
		gstOnIncomeTaxItem1.setActive(true);
		gstOnIncomeTaxItem1.setDescription("GST on Income 12.5%");
		gstOnIncomeTaxItem1.setTaxRate(15.0);
		gstOnIncomeTaxItem1.setTaxAgency(defaultGSTAgency);
		gstOnIncomeTaxItem1.setVatReturnBox(null);
		gstOnIncomeTaxItem1.setDefault(true);
		gstOnIncomeTaxItem1.setPercentage(true);
		session.save(gstOnIncomeTaxItem1);

		TAXItem gstOnExpensesTaxItem1 = new TAXItem(company);
		gstOnExpensesTaxItem1.setName("GST on Expenses 12.5%");
		gstOnExpensesTaxItem1.setActive(true);
		gstOnExpensesTaxItem1.setDescription("GST on Expenses 12.5% ");
		gstOnExpensesTaxItem1.setTaxRate(15.0);
		gstOnExpensesTaxItem1.setTaxAgency(defaultGSTAgency);
		gstOnExpensesTaxItem1.setVatReturnBox(null);
		gstOnExpensesTaxItem1.setDefault(true);
		gstOnExpensesTaxItem1.setPercentage(true);
		session.save(gstOnExpensesTaxItem1);

		TAXItem gstOnImportsTaxItem = new TAXItem(company);
		gstOnImportsTaxItem.setName("GST on Imports 0.0%");
		gstOnImportsTaxItem.setActive(true);
		gstOnImportsTaxItem.setDescription("GST on Imports 0.0% ");
		gstOnImportsTaxItem.setTaxRate(15.0);
		gstOnImportsTaxItem.setTaxAgency(defaultGSTAgency);
		gstOnImportsTaxItem.setVatReturnBox(null);
		gstOnImportsTaxItem.setDefault(true);
		gstOnImportsTaxItem.setPercentage(true);
		session.save(gstOnImportsTaxItem);

		TAXItem noGSTTaxItem = new TAXItem(company);
		noGSTTaxItem.setName("No GST");
		noGSTTaxItem.setActive(true);
		noGSTTaxItem.setDescription("No GST");
		noGSTTaxItem.setTaxRate(15.0);
		noGSTTaxItem.setTaxAgency(defaultGSTAgency);
		noGSTTaxItem.setVatReturnBox(null);
		noGSTTaxItem.setDefault(true);
		noGSTTaxItem.setPercentage(true);
		session.save(noGSTTaxItem);

		TAXItem zeroRatedTaxItem = new TAXItem(company);
		zeroRatedTaxItem.setName("Zero Rated");
		zeroRatedTaxItem.setActive(true);
		zeroRatedTaxItem.setDescription("Zero Rated");
		zeroRatedTaxItem.setTaxRate(15.0);
		zeroRatedTaxItem.setTaxAgency(defaultGSTAgency);
		zeroRatedTaxItem.setVatReturnBox(null);
		zeroRatedTaxItem.setDefault(true);
		zeroRatedTaxItem.setPercentage(true);
		session.save(zeroRatedTaxItem);

		// Creating TaxCodes

		TAXCode gstOnIncomeCode = new TAXCode(company);
		gstOnIncomeCode.setName("GST on Income 15%");
		gstOnIncomeCode.setDescription("GST on Income 15%");
		gstOnIncomeCode.setTaxable(true);
		gstOnIncomeCode.setActive(true);
		gstOnIncomeCode.setTAXItemGrpForPurchases(gstOnIncomeTaxItem);
		gstOnIncomeCode.setTAXItemGrpForSales(gstOnIncomeTaxItem);
		gstOnIncomeCode.setDefault(true);
		session.save(gstOnIncomeCode);

		TAXCode gstOnExpensesCode = new TAXCode(company);
		gstOnExpensesCode.setName("GST on Expenses 15%");
		gstOnExpensesCode.setDescription("GST on Expenses 15%");
		gstOnExpensesCode.setTaxable(true);
		gstOnExpensesCode.setActive(true);
		gstOnExpensesCode.setTAXItemGrpForPurchases(gstOnExpensesTaxItem);
		gstOnExpensesCode.setTAXItemGrpForSales(gstOnExpensesTaxItem);
		gstOnExpensesCode.setDefault(true);
		session.save(gstOnExpensesCode);

		TAXCode gstOnIncomeCode1 = new TAXCode(company);
		gstOnIncomeCode1.setName("GST on Income 12.5%");
		gstOnIncomeCode1.setDescription("GST on Income 12.5%");
		gstOnIncomeCode1.setTaxable(true);
		gstOnIncomeCode1.setActive(true);
		gstOnIncomeCode1.setTAXItemGrpForPurchases(gstOnIncomeTaxItem1);
		gstOnIncomeCode1.setTAXItemGrpForSales(gstOnIncomeTaxItem1);
		gstOnIncomeCode1.setDefault(true);
		session.save(gstOnIncomeCode1);

		TAXCode gstOnExpensesCode1 = new TAXCode(company);
		gstOnExpensesCode1.setName("GST on Expenses 12.5%");
		gstOnExpensesCode1.setDescription("GST on Expenses 12.5%");
		gstOnExpensesCode1.setTaxable(true);
		gstOnExpensesCode1.setActive(true);
		gstOnExpensesCode1.setTAXItemGrpForPurchases(gstOnExpensesTaxItem1);
		gstOnExpensesCode1.setTAXItemGrpForSales(gstOnExpensesTaxItem1);
		gstOnExpensesCode1.setDefault(true);
		session.save(gstOnExpensesCode1);
		
		
		TAXCode gstOnImportsCode = new TAXCode(company);
		gstOnImportsCode.setName("GST on Imports 0.0%");
		gstOnImportsCode.setDescription("GST on Imports 0.0%");
		gstOnImportsCode.setTaxable(true);
		gstOnImportsCode.setActive(true);
		gstOnImportsCode.setTAXItemGrpForPurchases(gstOnImportsTaxItem);
		gstOnImportsCode.setTAXItemGrpForSales(gstOnImportsTaxItem);
		gstOnImportsCode.setDefault(true);
		session.save(gstOnImportsCode);

		TAXCode noGSTCode = new TAXCode(company);
		noGSTCode.setName("No GST");
		noGSTCode.setDescription("No GST");
		noGSTCode.setTaxable(true);
		noGSTCode.setActive(true);
		noGSTCode.setTAXItemGrpForPurchases(noGSTTaxItem);
		noGSTCode.setTAXItemGrpForSales(noGSTTaxItem);
		noGSTCode.setDefault(true);
		session.save(noGSTCode);

		TAXCode zeroRatedCode = new TAXCode(company);
		zeroRatedCode.setName("Zero Rated 0.0%");
		zeroRatedCode.setDescription("Zero Rated 0.0%");
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
