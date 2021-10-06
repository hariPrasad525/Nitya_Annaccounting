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
public class AustraliaCompanyIntializer extends CompanyInitializer {

	public AustraliaCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {

		createDefualtTaxCodes();
	}

	private void createDefualtTaxCodes() {
		// Creating payble account for GST

		Account gstPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.GST_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny for Australia company

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

		TAXItem gstOnExpensesTaxItem = new TAXItem(company);
		gstOnExpensesTaxItem.setName("GST on Expenses");
		gstOnExpensesTaxItem.setActive(true);
		gstOnExpensesTaxItem.setDescription("GST on Expenses");
		gstOnExpensesTaxItem.setTaxRate(10.0);
		gstOnExpensesTaxItem.setTaxAgency(defaultGSTAgency);
		gstOnExpensesTaxItem.setVatReturnBox(null);
		gstOnExpensesTaxItem.setDefault(true);
		gstOnExpensesTaxItem.setPercentage(true);
		session.save(gstOnExpensesTaxItem);

		TAXItem gstOnCapitalTaxItem = new TAXItem(company);
		gstOnCapitalTaxItem.setName("GST on Capital");
		gstOnCapitalTaxItem.setActive(true);
		gstOnCapitalTaxItem.setDescription("GST on Capital");
		gstOnCapitalTaxItem.setTaxRate(10.0);
		gstOnCapitalTaxItem.setTaxAgency(defaultGSTAgency);
		gstOnCapitalTaxItem.setVatReturnBox(null);
		gstOnCapitalTaxItem.setDefault(true);
		gstOnCapitalTaxItem.setPercentage(true);
		session.save(gstOnCapitalTaxItem);

		TAXItem gstOnIncomeTaxItem = new TAXItem(company);
		gstOnIncomeTaxItem.setName("GST on Income");
		gstOnIncomeTaxItem.setActive(true);
		gstOnIncomeTaxItem.setDescription("GST on Income");
		gstOnIncomeTaxItem.setTaxRate(10.0);
		gstOnIncomeTaxItem.setTaxAgency(defaultGSTAgency);
		gstOnIncomeTaxItem.setVatReturnBox(null);
		gstOnIncomeTaxItem.setDefault(true);
		gstOnIncomeTaxItem.setPercentage(true);
		session.save(gstOnIncomeTaxItem);

		TAXItem gstFreeExportsTaxItem = new TAXItem(company);
		gstFreeExportsTaxItem.setName("GST Free Exports");
		gstFreeExportsTaxItem.setActive(true);
		gstFreeExportsTaxItem.setDescription("GST Free Exports");
		gstFreeExportsTaxItem.setTaxRate(0.0);
		gstFreeExportsTaxItem.setTaxAgency(defaultGSTAgency);
		gstFreeExportsTaxItem.setVatReturnBox(null);
		gstFreeExportsTaxItem.setDefault(true);
		gstFreeExportsTaxItem.setPercentage(true);
		session.save(gstFreeExportsTaxItem);

		TAXItem gstFreeIncomeTaxItem = new TAXItem(company);
		gstFreeIncomeTaxItem.setName("GST Free Exports");
		gstFreeIncomeTaxItem.setActive(true);
		gstFreeIncomeTaxItem.setDescription("GST Free Exports");
		gstFreeIncomeTaxItem.setTaxRate(0.0);
		gstFreeIncomeTaxItem.setTaxAgency(defaultGSTAgency);
		gstFreeIncomeTaxItem.setVatReturnBox(null);
		gstFreeIncomeTaxItem.setDefault(true);
		gstFreeIncomeTaxItem.setPercentage(true);
		session.save(gstFreeIncomeTaxItem);

		TAXItem gstExemptTaxItem = new TAXItem(company);
		gstExemptTaxItem.setName("GST Exempt");
		gstExemptTaxItem.setActive(true);
		gstExemptTaxItem.setDescription("GST Exempt");
		gstExemptTaxItem.setTaxRate(0.0);
		gstExemptTaxItem.setTaxAgency(defaultGSTAgency);
		gstExemptTaxItem.setVatReturnBox(null);
		gstExemptTaxItem.setDefault(true);
		gstExemptTaxItem.setPercentage(true);
		session.save(gstExemptTaxItem);

		TAXItem gstOnImportsTaxItem = new TAXItem(company);
		gstOnImportsTaxItem.setName("GST on Imports");
		gstOnImportsTaxItem.setActive(true);
		gstOnImportsTaxItem.setDescription("GST on Imports");
		gstOnImportsTaxItem.setTaxRate(0.0);
		gstOnImportsTaxItem.setTaxAgency(defaultGSTAgency);
		gstOnImportsTaxItem.setVatReturnBox(null);
		gstOnImportsTaxItem.setDefault(true);
		gstOnImportsTaxItem.setPercentage(true);
		session.save(gstOnImportsTaxItem);

		TAXItem gstOnCapitalImportsTaxItem = new TAXItem(company);
		gstOnCapitalImportsTaxItem.setName("GST on Capital Imports");
		gstOnCapitalImportsTaxItem.setActive(true);
		gstOnCapitalImportsTaxItem.setDescription("GST on Capital Imports");
		gstOnCapitalImportsTaxItem.setTaxRate(0.0);
		gstOnCapitalImportsTaxItem.setTaxAgency(defaultGSTAgency);
		gstOnCapitalImportsTaxItem.setVatReturnBox(null);
		gstOnCapitalImportsTaxItem.setDefault(true);
		gstOnCapitalImportsTaxItem.setPercentage(true);
		session.save(gstOnCapitalImportsTaxItem);

		TAXItem noGSTTaxItem = new TAXItem(company);
		noGSTTaxItem.setName("No GST");
		noGSTTaxItem.setActive(true);
		noGSTTaxItem.setDescription("No GST");
		noGSTTaxItem.setTaxRate(0.0);
		noGSTTaxItem.setTaxAgency(defaultGSTAgency);
		noGSTTaxItem.setVatReturnBox(null);
		noGSTTaxItem.setDefault(true);
		noGSTTaxItem.setPercentage(true);
		session.save(noGSTTaxItem);

		TAXItem inputTaxedTaxItem = new TAXItem(company);
		inputTaxedTaxItem.setName("Input Taxed");
		inputTaxedTaxItem.setActive(true);
		inputTaxedTaxItem.setDescription("Input Taxed");
		inputTaxedTaxItem.setTaxRate(0.0);
		inputTaxedTaxItem.setTaxAgency(defaultGSTAgency);
		inputTaxedTaxItem.setVatReturnBox(null);
		inputTaxedTaxItem.setDefault(true);
		inputTaxedTaxItem.setPercentage(true);
		session.save(inputTaxedTaxItem);

		// Creating TaxCodes

		TAXCode gstOnExpensesCode = new TAXCode(company);
		gstOnExpensesCode.setName("GST on Expenses 10.0%");
		gstOnExpensesCode.setDescription("GST on Expenses");
		gstOnExpensesCode.setTaxable(true);
		gstOnExpensesCode.setActive(true);
		gstOnExpensesCode.setTAXItemGrpForPurchases(gstOnExpensesTaxItem);
		gstOnExpensesCode.setTAXItemGrpForSales(gstOnExpensesTaxItem);
		gstOnExpensesCode.setDefault(true);
		session.save(gstOnExpensesCode);

		TAXCode gstOnCapitalCode = new TAXCode(company);
		gstOnCapitalCode.setName("GST on Capital 10.0%");
		gstOnCapitalCode.setDescription("GST on Capital");
		gstOnCapitalCode.setTaxable(true);
		gstOnCapitalCode.setActive(true);
		gstOnCapitalCode.setTAXItemGrpForPurchases(gstOnCapitalTaxItem);
		gstOnCapitalCode.setTAXItemGrpForSales(gstOnCapitalTaxItem);

		gstOnCapitalCode.setDefault(true);
		session.save(gstOnCapitalCode);

		TAXCode gstOnIncomeCode = new TAXCode(company);
		gstOnIncomeCode.setName("GST on Income 10.0%");
		gstOnIncomeCode.setDescription("GST on Income");
		gstOnIncomeCode.setTaxable(true);
		gstOnIncomeCode.setActive(true);
		gstOnIncomeCode.setTAXItemGrpForPurchases(gstOnIncomeTaxItem);
		gstOnIncomeCode.setTAXItemGrpForSales(gstOnIncomeTaxItem);
		gstOnIncomeCode.setDefault(true);
		session.save(gstOnIncomeCode);

		TAXCode gstFreeExportsCode = new TAXCode(company);
		gstFreeExportsCode.setName("GST Free Exports 0.0%");
		gstFreeExportsCode.setDescription("GST Free Exports");
		gstFreeExportsCode.setTaxable(true);
		gstFreeExportsCode.setActive(true);
		gstFreeExportsCode.setTAXItemGrpForPurchases(gstFreeExportsTaxItem);
		gstFreeExportsCode.setTAXItemGrpForSales(gstFreeExportsTaxItem);

		gstFreeExportsCode.setDefault(true);
		session.save(gstFreeExportsCode);

		TAXCode gstFreeIncomeCode = new TAXCode(company);
		gstFreeIncomeCode.setName("GST Free Income 0.0%");
		gstFreeIncomeCode.setDescription("GST Free Income");
		gstFreeIncomeCode.setTaxable(true);
		gstFreeIncomeCode.setActive(true);
		gstFreeIncomeCode.setTAXItemGrpForPurchases(gstFreeIncomeTaxItem);
		gstFreeIncomeCode.setTAXItemGrpForSales(gstFreeIncomeTaxItem);

		gstFreeIncomeCode.setDefault(true);
		session.save(gstFreeIncomeCode);

		TAXCode gstExemptCode = new TAXCode(company);
		gstExemptCode.setName("GST Exempt  0.0%");
		gstExemptCode.setDescription("GST Exempt");
		gstExemptCode.setTaxable(true);
		gstExemptCode.setActive(true);
		gstExemptCode.setTAXItemGrpForPurchases(gstExemptTaxItem);
		gstExemptCode.setTAXItemGrpForSales(gstExemptTaxItem);

		gstExemptCode.setDefault(true);
		session.save(gstExemptCode);

		TAXCode gstOnImportsCode = new TAXCode(company);
		gstOnImportsCode.setName("GST on Imports 0.0%");
		gstOnImportsCode.setDescription("GST on Imports");
		gstOnImportsCode.setTaxable(true);
		gstOnImportsCode.setActive(true);
		gstOnImportsCode.setTAXItemGrpForPurchases(gstOnImportsTaxItem);
		gstOnImportsCode.setTAXItemGrpForSales(gstOnImportsTaxItem);
		gstOnImportsCode.setDefault(true);
		session.save(gstOnImportsCode);

		TAXCode gstOnCapitalImportsCode = new TAXCode(company);
		gstOnCapitalImportsCode.setName("GST on Capital Imports 0.0%");
		gstOnCapitalImportsCode.setDescription("GST on Capital Imports");
		gstOnCapitalImportsCode.setTaxable(true);
		gstOnCapitalImportsCode.setActive(true);
		gstOnCapitalImportsCode
				.setTAXItemGrpForPurchases(gstOnCapitalImportsTaxItem);
		gstOnCapitalImportsCode
				.setTAXItemGrpForSales(gstOnCapitalImportsTaxItem);
		gstOnCapitalImportsCode.setDefault(true);
		session.save(gstOnCapitalImportsCode);

		TAXCode noGSTCode = new TAXCode(company);
		noGSTCode.setName("No GST");
		noGSTCode.setDescription("No GST");
		noGSTCode.setTaxable(true);
		noGSTCode.setActive(true);
		noGSTCode.setTAXItemGrpForPurchases(noGSTTaxItem);
		noGSTCode.setTAXItemGrpForSales(noGSTTaxItem);

		noGSTCode.setDefault(true);
		session.save(noGSTCode);

		TAXCode inputTaxedCode = new TAXCode(company);
		inputTaxedCode.setName("Input Taxed 0.0%");
		inputTaxedCode.setDescription("Input Taxed 0.0%");
		inputTaxedCode.setTaxable(true);
		inputTaxedCode.setActive(true);
		inputTaxedCode.setTAXItemGrpForPurchases(inputTaxedTaxItem);
		inputTaxedCode.setDefault(true);
		session.save(inputTaxedCode);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
