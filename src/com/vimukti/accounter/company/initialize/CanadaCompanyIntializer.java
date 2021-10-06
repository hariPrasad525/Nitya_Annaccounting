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

public class CanadaCompanyIntializer extends CompanyInitializer {

	public CanadaCompanyIntializer(Company company) {
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

		// Creating payble account for QST

		Account qstPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.QST_TAX_PAYABLE,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Creating default TaxAgecny for Canada company

		Session session = HibernateUtil.getCurrentSession();

		TAXAgency defaultGSTAgency = new TAXAgency();
		defaultGSTAgency.setActive(Boolean.TRUE);
		defaultGSTAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultGSTAgency.setName("GST/HST TaxAgency");
		defaultGSTAgency.setVATReturn(0);
		defaultGSTAgency.setPurchaseLiabilityAccount(gstPayable);
		defaultGSTAgency.setSalesLiabilityAccount(gstPayable);
		defaultGSTAgency.setDefault(true);
		defaultGSTAgency.setCompany(company);
		session.save(defaultGSTAgency);

		TAXAgency defaultQSTAgency = new TAXAgency();
		defaultQSTAgency.setActive(Boolean.TRUE);
		defaultQSTAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);
		defaultQSTAgency.setName("QST TaxAgency");
		defaultQSTAgency.setVATReturn(0);
		defaultQSTAgency.setPurchaseLiabilityAccount(qstPayable);
		defaultQSTAgency.setSalesLiabilityAccount(qstPayable);
		defaultQSTAgency.setDefault(true);
		defaultQSTAgency.setCompany(company);
		session.save(defaultQSTAgency);

		// Creating Tax Items for purchases ans sales

		TAXItem gst_HST15TaxItem = new TAXItem(company);
		gst_HST15TaxItem.setName("HST 15%");
		gst_HST15TaxItem.setActive(true);
		gst_HST15TaxItem.setDescription("HST 15%");
		gst_HST15TaxItem.setTaxRate(15.0);
		gst_HST15TaxItem.setTaxAgency(defaultGSTAgency);
		gst_HST15TaxItem.setVatReturnBox(null);
		gst_HST15TaxItem.setDefault(true);
		gst_HST15TaxItem.setPercentage(true);
		session.save(gst_HST15TaxItem);

		TAXItem gst_HST12TaxItem = new TAXItem(company);
		gst_HST12TaxItem.setName("HST  12%");
		gst_HST12TaxItem.setActive(true);
		gst_HST12TaxItem.setDescription("HST  12%");
		gst_HST12TaxItem.setTaxRate(12.0);
		gst_HST12TaxItem.setTaxAgency(defaultGSTAgency);
		gst_HST12TaxItem.setVatReturnBox(null);
		gst_HST12TaxItem.setDefault(true);
		gst_HST12TaxItem.setPercentage(true);
		session.save(gst_HST12TaxItem);

		TAXItem gst_HST13TaxItem = new TAXItem(company);
		gst_HST13TaxItem.setName("HST 13%");
		gst_HST13TaxItem.setActive(true);
		gst_HST13TaxItem.setDescription("HST 13%");
		gst_HST13TaxItem.setTaxRate(13.0);
		gst_HST13TaxItem.setTaxAgency(defaultGSTAgency);
		gst_HST13TaxItem.setVatReturnBox(null);
		gst_HST13TaxItem.setDefault(true);
		gst_HST13TaxItem.setPercentage(true);
		session.save(gst_HST13TaxItem);

		TAXItem gst_HST5TaxItem = new TAXItem(company);
		gst_HST5TaxItem.setName("GST 5%");
		gst_HST5TaxItem.setActive(true);
		gst_HST5TaxItem.setDescription("GST 5%");
		gst_HST5TaxItem.setTaxRate(5.0);
		gst_HST5TaxItem.setTaxAgency(defaultGSTAgency);
		gst_HST5TaxItem.setVatReturnBox(null);
		gst_HST5TaxItem.setDefault(true);
		gst_HST5TaxItem.setPercentage(true);
		session.save(gst_HST5TaxItem);

		TAXItem exemptTaxItem = new TAXItem(company);
		exemptTaxItem.setName("Exempt");
		exemptTaxItem.setActive(true);
		exemptTaxItem.setDescription("Exempt");
		exemptTaxItem.setTaxRate(0.0);
		exemptTaxItem.setTaxAgency(defaultGSTAgency);
		exemptTaxItem.setVatReturnBox(null);
		exemptTaxItem.setDefault(true);
		exemptTaxItem.setPercentage(true);
		session.save(exemptTaxItem);

		TAXItem zeroratedTaxItem = new TAXItem(company);
		zeroratedTaxItem.setName("Zero Rated 0.0%");
		zeroratedTaxItem.setActive(true);
		zeroratedTaxItem.setDescription("Zero Rated  0.0%");
		zeroratedTaxItem.setTaxRate(0.0);
		zeroratedTaxItem.setTaxAgency(defaultGSTAgency);
		zeroratedTaxItem.setVatReturnBox(null);
		zeroratedTaxItem.setDefault(true);
		zeroratedTaxItem.setPercentage(true);
		session.save(zeroratedTaxItem);

		TAXItem qstTaxItem = new TAXItem(company);
		qstTaxItem.setName("QST 9.5%");
		qstTaxItem.setActive(true);
		qstTaxItem.setDescription("QST 9.5%");
		qstTaxItem.setTaxRate(9.5);
		qstTaxItem.setTaxAgency(defaultQSTAgency);
		qstTaxItem.setVatReturnBox(null);
		qstTaxItem.setDefault(true);
		qstTaxItem.setPercentage(true);
		session.save(qstTaxItem);

		// Creating TaxCodes

		TAXCode hst15Code = new TAXCode(company);
		hst15Code.setName("HST  15%");
		hst15Code.setDescription("HST 15%");
		hst15Code.setTaxable(true);
		hst15Code.setActive(true);
		hst15Code.setTAXItemGrpForPurchases(gst_HST15TaxItem);
		hst15Code.setTAXItemGrpForSales(gst_HST15TaxItem);
		hst15Code.setDefault(true);
		session.save(hst15Code);

		TAXCode hst13Code = new TAXCode(company);
		hst13Code.setName("HST  13%");
		hst13Code.setDescription("HST  13%");
		hst13Code.setTaxable(true);
		hst13Code.setActive(true);
		hst13Code.setTAXItemGrpForPurchases(gst_HST13TaxItem);
		hst13Code.setTAXItemGrpForSales(gst_HST13TaxItem);
		hst13Code.setDefault(true);
		session.save(hst13Code);

		TAXCode hst12Code = new TAXCode(company);
		hst12Code.setName("HST 12%");
		hst12Code.setDescription("HST  12%");
		hst12Code.setTaxable(true);
		hst12Code.setActive(true);
		hst12Code.setTAXItemGrpForPurchases(gst_HST12TaxItem);
		hst12Code.setTAXItemGrpForSales(gst_HST12TaxItem);
		hst12Code.setDefault(true);
		session.save(hst12Code);

		TAXCode gst5Code = new TAXCode(company);
		gst5Code.setName("GST 5%");
		gst5Code.setDescription("GST 5%");
		gst5Code.setTaxable(true);
		gst5Code.setActive(true);
		gst5Code.setTAXItemGrpForPurchases(qstTaxItem);
		gst5Code.setTAXItemGrpForSales(qstTaxItem);
		gst5Code.setDefault(true);
		session.save(gst5Code);

		TAXCode gstExemptCode = new TAXCode(company);
		gstExemptCode.setName("Exempt");
		gstExemptCode.setDescription("Exempt");
		gstExemptCode.setTaxable(true);
		gstExemptCode.setActive(true);
		gstExemptCode.setTAXItemGrpForPurchases(exemptTaxItem);
		gstExemptCode.setTAXItemGrpForSales(exemptTaxItem);
		gstExemptCode.setDefault(true);
		session.save(gstExemptCode);

		TAXCode zeroRatedCode = new TAXCode(company);
		zeroRatedCode.setName("Zero Rated 0.0%");
		zeroRatedCode.setDescription("Zero Rated 0.0%");
		zeroRatedCode.setTaxable(true);
		zeroRatedCode.setActive(true);
		zeroRatedCode.setTAXItemGrpForPurchases(zeroratedTaxItem);
		zeroRatedCode.setTAXItemGrpForSales(zeroratedTaxItem);
		zeroRatedCode.setDefault(true);
		session.save(zeroRatedCode);

		TAXCode qstCode = new TAXCode(company);
		qstCode.setName("QST 9.5%");
		qstCode.setDescription("QST 9.5%");
		qstCode.setTaxable(true);
		qstCode.setActive(true);
		qstCode.setTAXItemGrpForPurchases(qstTaxItem);
		qstCode.setTAXItemGrpForSales(qstTaxItem);
		qstCode.setDefault(true);
		session.save(qstCode);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}

}
