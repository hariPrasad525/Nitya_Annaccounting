package com.nitya.accounter.company.initialize;

import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterServerConstants;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.TAXItem;
import com.nitya.accounter.utils.HibernateUtil;

public class SingaporeCompanyIntializer extends CompanyInitializer {

	public SingaporeCompanyIntializer(Company company) {
		super(company);
	}

	@Override
	protected void init() {
		createDefaults();

	}

	/**
	 * Creating Default information for Singapore Country
	 */
	private void createDefaults() {

		// Creating payble account for GST

		Account gstPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.GST_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny for Singapore company

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

		TAXItem tx7TaxItem = new TAXItem(company);
		tx7TaxItem.setName("TX7 Item");
		tx7TaxItem.setActive(true);
		tx7TaxItem
				.setDescription("Purchases with GST incurred at 7% and directly attributable to taxable supplies");
		tx7TaxItem.setTaxRate(7.0);
		tx7TaxItem.setTaxAgency(defaultGSTAgency);
		tx7TaxItem.setVatReturnBox(null);
		tx7TaxItem.setDefault(true);
		tx7TaxItem.setPercentage(true);
		session.save(tx7TaxItem);

		TAXItem imTaxItem = new TAXItem(company);
		imTaxItem.setName("IM");
		imTaxItem.setActive(true);
		imTaxItem.setDescription("GST incurred for import of goods");
		imTaxItem.setTaxRate(7.0);
		imTaxItem.setTaxAgency(defaultGSTAgency);
		imTaxItem.setVatReturnBox(null);
		imTaxItem.setDefault(true);
		imTaxItem.setPercentage(true);
		session.save(imTaxItem);

		TAXItem meTaxItem = new TAXItem(company);
		meTaxItem.setName("ME");
		meTaxItem.setActive(true);
		meTaxItem
				.setDescription("Imports under special scheme with no GST incurred (e.g. Major Exporter Scheme, 3rd Party Logistics Scheme");
		meTaxItem.setTaxRate(0.0);
		meTaxItem.setTaxAgency(defaultGSTAgency);
		meTaxItem.setVatReturnBox(null);
		meTaxItem.setDefault(true);
		meTaxItem.setPercentage(true);
		session.save(meTaxItem);

		TAXItem blTaxItem = new TAXItem(company);
		blTaxItem.setName("BL");
		blTaxItem.setActive(true);
		blTaxItem
				.setDescription("Purchases with GST incurred but not claimable under Regulations 26/27 (e.g.medical expenses for staff)");
		blTaxItem.setTaxRate(7.0);
		blTaxItem.setTaxAgency(defaultGSTAgency);
		blTaxItem.setVatReturnBox(null);
		blTaxItem.setDefault(true);
		blTaxItem.setPercentage(true);
		session.save(blTaxItem);

		TAXItem nrTaxItem = new TAXItem(company);
		nrTaxItem.setName("NR");
		nrTaxItem.setActive(true);
		nrTaxItem
				.setDescription("Purchase from non GST-registered supplier with no GST incurred");
		nrTaxItem.setTaxRate(0.0);
		nrTaxItem.setTaxAgency(defaultGSTAgency);
		nrTaxItem.setVatReturnBox(null);
		nrTaxItem.setDefault(true);
		nrTaxItem.setPercentage(true);
		session.save(nrTaxItem);

		TAXItem zpTaxItem = new TAXItem(company);
		zpTaxItem.setName("ZP");
		zpTaxItem.setActive(true);
		zpTaxItem
				.setDescription("Purchase from GST-registered supplier with no GST incurred. (e.g. supplier provides transportation of goods that qualify as international services)");
		zpTaxItem.setTaxRate(0.0);
		zpTaxItem.setTaxAgency(defaultGSTAgency);
		zpTaxItem.setVatReturnBox(null);
		zpTaxItem.setDefault(true);
		zpTaxItem.setPercentage(true);
		session.save(zpTaxItem);

		TAXItem epTaxItem = new TAXItem(company);
		epTaxItem.setName("EP");
		epTaxItem.setActive(true);
		epTaxItem
				.setDescription("Purchases exempted from GST. E.g. purchase of residential property or financial services");
		epTaxItem.setTaxRate(0.0);
		epTaxItem.setTaxAgency(defaultGSTAgency);
		epTaxItem.setVatReturnBox(null);
		epTaxItem.setDefault(true);
		epTaxItem.setPercentage(true);
		session.save(epTaxItem);

		TAXItem opTaxItem = new TAXItem(company);
		opTaxItem.setName("OP");
		opTaxItem.setActive(true);
		opTaxItem
				.setDescription("Purchase transactions which is out of the scope of GST legislation (e.g. purchase of goods overseas)");
		opTaxItem.setTaxRate(0.0);
		opTaxItem.setTaxAgency(defaultGSTAgency);
		opTaxItem.setVatReturnBox(null);
		opTaxItem.setDefault(true);
		opTaxItem.setPercentage(true);
		session.save(opTaxItem);

		TAXItem txE33TaxItem = new TAXItem(company);
		txE33TaxItem.setName("TX-E33");
		txE33TaxItem.setActive(true);
		txE33TaxItem
				.setDescription("GST incurred directly attributable to Regulation 33 exempt supplies");
		txE33TaxItem.setTaxRate(7.0);
		txE33TaxItem.setTaxAgency(defaultGSTAgency);
		txE33TaxItem.setVatReturnBox(null);
		txE33TaxItem.setDefault(true);
		txE33TaxItem.setPercentage(true);
		session.save(txE33TaxItem);

		TAXItem txN33TaxItem = new TAXItem(company);
		txN33TaxItem.setName("TX-N33");
		txN33TaxItem.setActive(true);
		txN33TaxItem
				.setDescription("GST incurred directly attributable to Non-Regulation 33 exempt supplies");
		txN33TaxItem.setTaxRate(7.0);
		txN33TaxItem.setTaxAgency(defaultGSTAgency);
		txN33TaxItem.setVatReturnBox(null);
		txN33TaxItem.setDefault(true);
		txN33TaxItem.setPercentage(true);
		session.save(txN33TaxItem);

		TAXItem txRETaxItem = new TAXItem(company);
		txRETaxItem.setName("TX-RE");
		txRETaxItem.setActive(true);
		txRETaxItem
				.setDescription("GST incurred that is not directly attributable to taxable or exempt supplies");
		txRETaxItem.setTaxRate(7.0);
		txRETaxItem.setTaxAgency(defaultGSTAgency);
		txRETaxItem.setVatReturnBox(null);
		txRETaxItem.setDefault(true);
		txRETaxItem.setPercentage(true);
		session.save(txRETaxItem);

		// For sales
		TAXItem srTaxItem = new TAXItem(company);
		srTaxItem.setName("SR");
		srTaxItem.setActive(true);
		srTaxItem.setDescription("Standard-rated supplies with GST charged");
		srTaxItem.setTaxRate(7.0);
		srTaxItem.setTaxAgency(defaultGSTAgency);
		srTaxItem.setVatReturnBox(null);
		srTaxItem.setDefault(true);
		srTaxItem.setPercentage(true);
		session.save(srTaxItem);

		TAXItem zrTaxItem = new TAXItem(company);
		zrTaxItem.setName("ZR");
		zrTaxItem.setActive(true);
		zrTaxItem.setDescription("Zero-rated supplies");
		zrTaxItem.setTaxRate(0.0);
		zrTaxItem.setTaxAgency(defaultGSTAgency);
		zrTaxItem.setVatReturnBox(null);
		zrTaxItem.setDefault(true);
		zrTaxItem.setPercentage(true);
		session.save(zrTaxItem);

		TAXItem es33TaxItem = new TAXItem(company);
		es33TaxItem.setName("ES33");
		es33TaxItem.setActive(true);
		es33TaxItem.setDescription("Regulation 33 Exempt supplies");
		es33TaxItem.setTaxRate(0.0);
		es33TaxItem.setTaxAgency(defaultGSTAgency);
		es33TaxItem.setVatReturnBox(null);
		es33TaxItem.setDefault(true);
		es33TaxItem.setPercentage(true);
		session.save(es33TaxItem);

		TAXItem esN33TaxItem = new TAXItem(company);
		esN33TaxItem.setName("ESN33");
		esN33TaxItem.setActive(true);
		esN33TaxItem.setDescription("Non Regulation 33 Exempt supplies");
		esN33TaxItem.setTaxRate(7.0);
		esN33TaxItem.setTaxAgency(defaultGSTAgency);
		esN33TaxItem.setVatReturnBox(null);
		esN33TaxItem.setDefault(true);
		esN33TaxItem.setPercentage(true);
		session.save(esN33TaxItem);

		TAXItem dsTaxItem = new TAXItem(company);
		dsTaxItem.setName("DS");
		dsTaxItem.setActive(true);
		dsTaxItem
				.setDescription("Deemed supplies (e.g. transfer or disposal of business assets without consideration)");
		dsTaxItem.setTaxRate(7.0);
		dsTaxItem.setTaxAgency(defaultGSTAgency);
		dsTaxItem.setVatReturnBox(null);
		dsTaxItem.setDefault(true);
		dsTaxItem.setPercentage(true);
		session.save(dsTaxItem);

		TAXItem osTaxItem = new TAXItem(company);
		osTaxItem.setName("OS");
		osTaxItem.setActive(true);
		osTaxItem.setDescription("Out-of-scope supplies");
		osTaxItem.setTaxRate(0.0);
		osTaxItem.setTaxAgency(defaultGSTAgency);
		osTaxItem.setVatReturnBox(null);
		osTaxItem.setDefault(true);
		osTaxItem.setPercentage(true);
		session.save(osTaxItem);

		// ---------------------TAX_CODES-----------------

		// For purchases

		TAXCode tx7Code = new TAXCode(company);
		tx7Code.setName("TX7 7.0%");
		tx7Code.setDescription("Purchases with GST incurred at 7% and directly attributable to taxable supplies");
		tx7Code.setTaxable(true);
		tx7Code.setActive(true);
		tx7Code.setTAXItemGrpForPurchases(osTaxItem);
		tx7Code.setDefault(true);
		session.save(tx7Code);

		TAXCode imCode = new TAXCode(company);
		imCode.setName("IM 7.0%");
		imCode.setDescription("GST incurred for import of goods");
		imCode.setTaxable(true);
		imCode.setActive(true);
		imCode.setDefault(true);
		imCode.setTAXItemGrpForPurchases(meTaxItem);
		session.save(imCode);

		TAXCode meCode = new TAXCode(company);
		meCode.setName("ME 0.0%");
		meCode.setDescription("Imports under special scheme with no GST incurred (e.g. Major Exporter Scheme, 3rd Party Logistics Scheme)");
		meCode.setTaxable(true);
		meCode.setActive(true);
		meCode.setDefault(true);
		meCode.setTAXItemGrpForPurchases(meTaxItem);
		session.save(meCode);

		TAXCode blCode = new TAXCode(company);
		blCode.setName("BL 7.0%");
		blCode.setDescription(" Purchases with GST incurred but not claimable under Regulations 26/27 (e.g.medical expenses for staff)");
		blCode.setTaxable(true);
		blCode.setActive(true);
		blCode.setDefault(true);
		blCode.setTAXItemGrpForPurchases(blTaxItem);
		session.save(blCode);

		TAXCode nrCode = new TAXCode(company);
		nrCode.setName("NR 0.0%");
		nrCode.setDescription("Purchase from non GST-registered supplier with no GST incurred");
		nrCode.setTaxable(true);
		nrCode.setActive(true);
		nrCode.setDefault(true);
		nrCode.setTAXItemGrpForPurchases(nrTaxItem);
		session.save(nrCode);

		TAXCode zpCode = new TAXCode(company);
		zpCode.setName("ZP 0.0%");
		zpCode.setDescription("Purchase from GST-registered supplier with no GST incurred. (e.g. supplier provides transportation of goods that qualify as international services)");
		zpCode.setTaxable(true);
		zpCode.setActive(true);
		zpCode.setDefault(true);
		zpCode.setTAXItemGrpForPurchases(zpTaxItem);
		session.save(zpCode);

		TAXCode epCode = new TAXCode(company);
		epCode.setName("EP 0.0%");
		epCode.setDescription("Purchases exempted from GST. E.g. purchase of residential property or financial services");
		epCode.setTaxable(true);
		epCode.setActive(true);
		epCode.setDefault(true);
		epCode.setTAXItemGrpForPurchases(epTaxItem);
		session.save(epCode);

		TAXCode opCode = new TAXCode(company);
		opCode.setName("OP 0.0%");
		opCode.setDescription("Purchase transactions which is out of the scope of GST legislation (e.g. purchase of goods overseas)");
		opCode.setTaxable(true);
		opCode.setActive(true);
		opCode.setDefault(true);
		opCode.setTAXItemGrpForPurchases(opTaxItem);
		session.save(opCode);

		TAXCode txE33Code = new TAXCode(company);
		txE33Code.setName("TX-E33 7.0%");
		txE33Code
				.setDescription("GST incurred directly attributable to Regulation 33 exempt supplies");
		txE33Code.setTaxable(true);
		txE33Code.setActive(true);
		txE33Code.setDefault(true);
		txE33Code.setTAXItemGrpForPurchases(txE33TaxItem);
		session.save(txE33Code);

		TAXCode txN33Code = new TAXCode(company);
		txN33Code.setName("TX-N33 7.0%");
		txN33Code
				.setDescription("GST incurred directly attributable to Non-Regulation 33 exempt supplies");
		txN33Code.setTaxable(true);
		txN33Code.setActive(true);
		txN33Code.setDefault(true);
		txN33Code.setTAXItemGrpForPurchases(txN33TaxItem);
		session.save(txN33Code);

		TAXCode txRECode = new TAXCode(company);
		txRECode.setName("TX-RE 7.0%");
		txRECode.setDescription("GST incurred that is not directly attributable to taxable or exempt supplies");
		txRECode.setTaxable(true);
		txRECode.setActive(true);
		txRECode.setDefault(true);
		txRECode.setTAXItemGrpForPurchases(osTaxItem);
		session.save(txRECode);

		// For sales

		TAXCode srCode = new TAXCode(company);
		srCode.setName("SR 7.0%");
		srCode.setDescription("Standard-rated supplies with GST charged");
		srCode.setTaxable(true);
		srCode.setActive(true);
		srCode.setDefault(true);
		srCode.setTAXItemGrpForSales(srTaxItem);
		session.save(srCode);

		TAXCode zrCode = new TAXCode(company);
		zrCode.setName("ZR 0.0%");
		zrCode.setDescription("Zero-rated supplies");
		zrCode.setTaxable(true);
		zrCode.setActive(true);
		zrCode.setDefault(true);
		zrCode.setTAXItemGrpForSales(zrTaxItem);
		session.save(zrCode);

		TAXCode es33Code = new TAXCode(company);
		es33Code.setName("ES33 0.0%");
		es33Code.setDescription("Regulation 33 Exempt supplies");
		es33Code.setTaxable(true);
		es33Code.setActive(true);
		es33Code.setDefault(true);
		es33Code.setTAXItemGrpForSales(es33TaxItem);
		session.save(es33Code);

		TAXCode esn33Code = new TAXCode(company);
		esn33Code.setName("ESN33 0.0%");
		esn33Code.setDescription("Non Regulation 33 Exempt supplies");
		esn33Code.setTaxable(true);
		esn33Code.setActive(true);
		esn33Code.setDefault(true);
		esn33Code.setTAXItemGrpForSales(esN33TaxItem);
		session.save(esn33Code);

		TAXCode dsCode = new TAXCode(company);
		dsCode.setName("DS 7.0%");
		dsCode.setDescription("Deemed supplies (e.g. transfer or disposal of business assets without consideration)");
		dsCode.setTaxable(true);
		dsCode.setActive(true);
		dsCode.setDefault(true);
		dsCode.setTAXItemGrpForSales(dsTaxItem);
		session.save(dsCode);

		TAXCode osCode = new TAXCode(company);
		osCode.setName("OS 0.0%");
		osCode.setDescription("Out-of-scope supplies");
		osCode.setTaxable(true);
		osCode.setActive(true);
		osCode.setDefault(true);
		osCode.setTAXItemGrpForSales(osTaxItem);
		session.save(osCode);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
