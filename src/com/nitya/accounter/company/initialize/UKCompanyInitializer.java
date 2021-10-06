package com.nitya.accounter.company.initialize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterServerConstants;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.NominalCodeRange;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.TAXGroup;
import com.nitya.accounter.core.TAXItem;
import com.nitya.accounter.core.TAXReturn;
import com.nitya.accounter.core.VATReturnBox;
import com.nitya.accounter.utils.HibernateUtil;

public class UKCompanyInitializer extends CompanyInitializer {

	// Account prepaidVATaccount;
	// Account ECAcquisitionVATaccount;

	// Account pendingItemReceiptsAccount;

	Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();
	/**
	 * Name of the Company
	 */
	String name;// Trading name

	/**
	 * Creates new Instance
	 */
	public UKCompanyInitializer(Company company) {
		super(company);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setNominalCodeRange(Set<NominalCodeRange> nominalCodeRange) {
		this.nominalCodeRange = nominalCodeRange;
	}

	/**
	 * Return the list of Nominal code ranges for the given sub base type of an
	 * Account
	 * 
	 * @param accountSubBaseType
	 * @return Integer[]
	 */
	public Integer[] getNominalCodeRange(int accountSubBaseType) {

		for (NominalCodeRange nomincalCode : this.getNominalCodeRange()) {
			if (nomincalCode.getAccountSubBaseType() == accountSubBaseType) {
				return new Integer[] { nomincalCode.getMinimum(),
						nomincalCode.getMaximum() };
			}
		}

		return null;
	}

	public Set<NominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	private void initDefaultUKAccounts() {

		// This is the Account created by default for the purpose of UK VAT
		Account vATliabilityAccount = createDefaultAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.TAX_VAT_UNFILED,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		createAccount(Account.TYPE_OTHER_ASSET,
				AccounterServerConstants.VAT_ON_IMPORTS,
				Account.CASH_FLOW_CATEGORY_INVESTING);

		company.setTaxLiabilityAccount(vATliabilityAccount);

		createUKDefaultVATCodesAndVATAgency();

	}

	/**
	 * Creates all the nominal code ranges for all the default accounts in the
	 * company. It sets the minimum range and maximum range of the nominal codes
	 * 
	 * @param session
	 */

	private void createNominalCodesRanges(Session session) {

		Set<NominalCodeRange> nominalCodesRangeSet = new HashSet<NominalCodeRange>();

		NominalCodeRange nominalCodeRange1 = new NominalCodeRange();
		nominalCodeRange1
				.setAccountSubBaseType(Account.SUBBASETYPE_FIXED_ASSET);
		nominalCodeRange1.setMinimum(NominalCodeRange.RANGE_FIXED_ASSET_MIN);
		nominalCodeRange1.setMaximum(NominalCodeRange.RANGE_FIXED_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange1);

		NominalCodeRange nominalCodeRange2 = new NominalCodeRange();
		nominalCodeRange2
				.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
		nominalCodeRange2
				.setMinimum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MIN);
		nominalCodeRange2
				.setMaximum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange2);

		NominalCodeRange nominalCodeRange3 = new NominalCodeRange();
		nominalCodeRange3
				.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
		nominalCodeRange3
				.setMinimum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MIN);
		nominalCodeRange3
				.setMaximum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange3);

		NominalCodeRange nominalCodeRange4 = new NominalCodeRange();
		nominalCodeRange4.setAccountSubBaseType(Account.SUBBASETYPE_EQUITY);
		nominalCodeRange4.setMinimum(NominalCodeRange.RANGE_EQUITY_MIN);
		nominalCodeRange4.setMaximum(NominalCodeRange.RANGE_EQUITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange4);

		NominalCodeRange nominalCodeRange5 = new NominalCodeRange();
		nominalCodeRange5.setAccountSubBaseType(Account.SUBBASETYPE_INCOME);
		nominalCodeRange5.setMinimum(NominalCodeRange.RANGE_INCOME_MIN);
		nominalCodeRange5.setMaximum(NominalCodeRange.RANGE_INCOME_MAX);
		nominalCodesRangeSet.add(nominalCodeRange5);

		NominalCodeRange nominalCodeRange6 = new NominalCodeRange();
		nominalCodeRange6
				.setAccountSubBaseType(Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
		nominalCodeRange6
				.setMinimum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MIN);
		nominalCodeRange6
				.setMaximum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MAX);
		nominalCodesRangeSet.add(nominalCodeRange6);

		NominalCodeRange nominalCodeRange7 = new NominalCodeRange();
		nominalCodeRange7
				.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_EXPENSE);
		nominalCodeRange7.setMinimum(NominalCodeRange.RANGE_OTHER_EXPENSE_MIN);
		nominalCodeRange7.setMaximum(NominalCodeRange.RANGE_OTHER_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange7);

		NominalCodeRange nominalCodeRange8 = new NominalCodeRange();
		nominalCodeRange8.setAccountSubBaseType(Account.SUBBASETYPE_EXPENSE);
		nominalCodeRange8.setMinimum(NominalCodeRange.RANGE_EXPENSE_MIN);
		nominalCodeRange8.setMaximum(NominalCodeRange.RANGE_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange8);

		NominalCodeRange nominalCodeRange9 = new NominalCodeRange();
		nominalCodeRange9
				.setAccountSubBaseType(Account.SUBBASETYPE_LONG_TERM_LIABILITY);
		nominalCodeRange9
				.setMinimum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MIN);
		nominalCodeRange9
				.setMaximum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange9);

		NominalCodeRange nominalCodeRange10 = new NominalCodeRange();
		nominalCodeRange10
				.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_ASSET);
		nominalCodeRange10.setMinimum(NominalCodeRange.RANGE_OTHER_ASSET_MIN);
		nominalCodeRange10.setMaximum(NominalCodeRange.RANGE_OTHER_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange10);

		this.setNominalCodeRange(nominalCodesRangeSet);

	}

	public void createUKDefaultVATCodesAndVATAgency() {
		Session session = HibernateUtil.getCurrentSession();
		try {
			VATReturnBox vt1 = new VATReturnBox(company);
			vt1.setName(AccounterServerConstants.UK_EC_PURCHASES_GOODS);
			vt1.setVatBox(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);
			vt1.setTotalBox(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS);
			vt1.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt1);

			VATReturnBox vt3 = new VATReturnBox(company);
			vt3.setName(AccounterServerConstants.UK_EC_SALES_GOODS);
			vt3.setVatBox(AccounterServerConstants.BOX_NONE);
			vt3.setTotalBox(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES);
			vt3.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt3);

			VATReturnBox vt4 = new VATReturnBox(company);
			vt4.setName(AccounterServerConstants.UK_EC_SALES_SERVICES);
			vt4.setVatBox(AccounterServerConstants.BOX_NONE);
			vt4.setTotalBox(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES);
			vt4.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt4);

			VATReturnBox vt5 = new VATReturnBox(company);
			vt5.setName(AccounterServerConstants.UK_DOMESTIC_PURCHASES);
			vt5.setVatBox(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
			vt5.setTotalBox(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES);
			vt5.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt5);

			VATReturnBox vt6 = new VATReturnBox(company);
			vt6.setName(AccounterServerConstants.UK_DOMESTIC_SALES);
			vt6.setVatBox(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt6.setTotalBox(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES);
			vt6.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt6);

			VATReturnBox vt7 = new VATReturnBox(company);
			vt7.setName(AccounterServerConstants.UK_NOT_REGISTERED_PURCHASES);
			vt7.setVatBox(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
			vt7.setTotalBox(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES);
			vt7.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt7);

			VATReturnBox vt8 = new VATReturnBox(company);
			vt8.setName(AccounterServerConstants.UK_NOT_REGISTERED_SALES);
			vt8.setVatBox(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt8.setTotalBox(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES);
			vt8.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt8);

			VATReturnBox vt11 = new VATReturnBox(company);
			vt11.setName(AccounterServerConstants.UK_REVERSE_CHARGE);
			vt11.setVatBox(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt11.setTotalBox(AccounterServerConstants.BOX_NONE);
			vt11.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt11);

			// /// For Ireland VAT Return type boxes

			VATReturnBox vt20 = new VATReturnBox(company);
			vt20.setName(AccounterServerConstants.IRELAND_DOMESTIC_SALES);
			vt20.setVatBox(AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt20.setTotalBox(AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt20.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt20);

			VATReturnBox vt21 = new VATReturnBox(company);
			vt21.setName(AccounterServerConstants.IRELAND_DOMESTIC_PURCHASES);
			vt21.setVatBox(AccounterServerConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
			vt21.setTotalBox(AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt21.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt21);

			VATReturnBox vt22 = new VATReturnBox(company);
			vt22.setName(AccounterServerConstants.IRELAND_EC_SALES_GOODS);
			vt22.setVatBox(AccounterServerConstants.BOX_NONE);
			vt22.setTotalBox(AccounterServerConstants.IRELAND_BOX6_E1_GOODS_TO_EU);
			vt22.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt22);

			VATReturnBox vt23 = new VATReturnBox(company);
			vt23.setName(AccounterServerConstants.IRELAND_EC_PURCHASES_GOODS);
			vt23.setVatBox(AccounterServerConstants.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS);
			vt23.setTotalBox(AccounterServerConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
			vt23.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt23);

			VATReturnBox vt24 = new VATReturnBox(company);
			vt24.setName(AccounterServerConstants.IRELAND_EXEMPT_SALES);
			vt24.setVatBox(AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt24.setTotalBox(AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt24.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt24);

			VATReturnBox vt25 = new VATReturnBox(company);
			vt25.setName(AccounterServerConstants.IRELAND_EXEMPT_PURCHASES);
			vt25.setVatBox(AccounterServerConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
			vt25.setTotalBox(AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt25.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt25);

			VATReturnBox vt26 = new VATReturnBox(company);
			vt26.setName(AccounterServerConstants.IRELAND_NOT_REGISTERED_SALES);
			vt26.setVatBox(AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt26.setTotalBox(AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt26.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt26);

			VATReturnBox vt27 = new VATReturnBox(company);
			vt27.setName(AccounterServerConstants.IRELAND_NOT_REGISTERED_PURCHASES);
			vt27.setVatBox(AccounterServerConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
			vt27.setTotalBox(AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt27.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt27);

			// Session session = HibernateUtil.getCurrentSession();

			TAXAgency defaultVATAgency = new TAXAgency();
			defaultVATAgency.setActive(Boolean.TRUE);
			defaultVATAgency.setName(preferences.getVATtaxAgencyName());
			defaultVATAgency.setVATReturn(TAXReturn.VAT_RETURN_UK_VAT);
			defaultVATAgency.setTaxType(TAXAgency.TAX_TYPE_VAT);

			defaultVATAgency.setPaymentTerm((PaymentTerms) session
					.getNamedQuery("unique.name.PaymentTerms")
					.setEntity("company", company)
					.setParameter("name", "Net Monthly",
							EncryptedStringType.INSTANCE).list().get(0));
			defaultVATAgency.setSalesLiabilityAccount((Account) session
					.getNamedQuery("unique.name.Account")
					.setEntity("company", company)
					.setParameter("name",
							AccounterServerConstants.TAX_VAT_UNFILED,
							EncryptedStringType.INSTANCE).list().get(0));

			defaultVATAgency.setPurchaseLiabilityAccount((Account) session
					.getNamedQuery("unique.name.Account")
					.setEntity("company", company)
					.setParameter("name",
							AccounterServerConstants.TAX_VAT_UNFILED,
							EncryptedStringType.INSTANCE).list().get(0));

			defaultVATAgency.setDefault(true);
			defaultVATAgency.setCompany(company);
			session.save(defaultVATAgency);

			TAXItem ecgPurchase17Item = new TAXItem(company);
			ecgPurchase17Item.setName("EC Purchases Goods Standard (-17.5%)");
			ecgPurchase17Item.setActive(true);
			ecgPurchase17Item
					.setDescription("EC Purchases of Goods Standard (-17.5%)");
			ecgPurchase17Item.setTaxRate(-17.5);
			ecgPurchase17Item.setTaxAgency(defaultVATAgency);

			ecgPurchase17Item.setVatReturnBox(vt1);
			ecgPurchase17Item.setDefault(true);
			ecgPurchase17Item.setPercentage(true);
			session.save(ecgPurchase17Item);

			TAXItem ecgPurchase20Item = new TAXItem(company);
			ecgPurchase20Item.setName("EC Purchases Goods Standard (-20.0%)");
			ecgPurchase20Item.setActive(true);
			ecgPurchase20Item
					.setDescription("EC Purchases of Goods Standard (-20.0%)");
			ecgPurchase20Item.setTaxRate(-20);
			ecgPurchase20Item.setTaxAgency(defaultVATAgency);

			ecgPurchase20Item.setVatReturnBox(vt1);
			ecgPurchase20Item.setDefault(true);
			ecgPurchase20Item.setPercentage(true);
			session.save(ecgPurchase20Item);

			TAXItem ecsPurchase17Item = new TAXItem(company);
			ecsPurchase17Item
					.setName("EC Purchases Services Standard (-17.5%)");
			ecsPurchase17Item.setActive(true);
			ecsPurchase17Item
					.setDescription("EC Purchases of Services Standard (-17.5%)");
			ecsPurchase17Item.setTaxRate(-17.5);
			ecsPurchase17Item.setTaxAgency(defaultVATAgency);

			ecsPurchase17Item.setVatReturnBox(vt1);
			ecsPurchase17Item.setDefault(true);
			ecsPurchase17Item.setPercentage(true);
			session.save(ecsPurchase17Item);

			TAXItem ecsPurchase20Item = new TAXItem(company);
			ecsPurchase20Item
					.setName("EC Purchases Services Standard (-20.0%)");
			ecsPurchase20Item.setActive(true);
			ecsPurchase20Item
					.setDescription("EC Purchases of Services Standard (-20.0%)");
			ecsPurchase20Item.setTaxRate(-20);
			ecsPurchase20Item.setTaxAgency(defaultVATAgency);

			ecsPurchase20Item.setVatReturnBox(vt1);
			ecsPurchase20Item.setDefault(true);
			ecsPurchase20Item.setPercentage(true);
			session.save(ecsPurchase20Item);

			TAXItem egzPurchaseItem = new TAXItem(company);
			egzPurchaseItem.setName("EC Purchases Goods Zero-Rated");
			egzPurchaseItem.setActive(true);
			egzPurchaseItem.setDescription("EC Purchases of Goods Zero-Rated");
			egzPurchaseItem.setTaxRate(0.0);
			egzPurchaseItem.setTaxAgency(defaultVATAgency);
			egzPurchaseItem.setPercentage(true);
			egzPurchaseItem.setVatReturnBox(vt1);
			egzPurchaseItem.setDefault(true);
			session.save(egzPurchaseItem);

			TAXItem egzSalesItem = new TAXItem(company);
			egzSalesItem.setName("EC Sales Goods Zero-Rated");
			egzSalesItem.setActive(true);
			egzSalesItem.setDescription("EC Sales of Goods Zero-Rated");
			egzSalesItem.setTaxRate(0.0);
			egzSalesItem.setTaxAgency(defaultVATAgency);
			egzSalesItem.setPercentage(true);
			egzSalesItem.setVatReturnBox(vt3);
			egzSalesItem.setDefault(true);
			session.save(egzSalesItem);

			TAXItem ecszSalesItem = new TAXItem(company);
			ecszSalesItem.setName("EC Sales Services Zero-Rated");
			ecszSalesItem.setActive(true);
			ecszSalesItem.setDescription("EC Sales of Services Zero-Rated");
			ecszSalesItem.setTaxRate(0.0);
			ecszSalesItem.setTaxAgency(defaultVATAgency);
			ecszSalesItem.setVatReturnBox(vt4);
			ecszSalesItem.setDefault(true);
			ecszSalesItem.setPercentage(true);
			session.save(ecszSalesItem);

			TAXItem ecszPurchaseItem = new TAXItem(company);
			ecszPurchaseItem.setName("EC Purchase Services Zero-Rated");
			ecszPurchaseItem.setActive(true);
			ecszPurchaseItem
					.setDescription("EC Purchase of Services Zero-Rated");
			ecszPurchaseItem.setTaxRate(0.0);
			ecszPurchaseItem.setTaxAgency(defaultVATAgency);
			ecszPurchaseItem.setVatReturnBox(vt4);
			ecszPurchaseItem.setDefault(true);
			ecszPurchaseItem.setPercentage(true);
			session.save(ecszPurchaseItem);

			TAXItem rczSalesitem = new TAXItem(company);
			rczSalesitem.setName("Reverse Sales Charges");
			rczSalesitem.setActive(true);
			rczSalesitem.setDescription("Reverse Sales Charges");
			rczSalesitem.setTaxRate(0.0);
			rczSalesitem.setTaxAgency(defaultVATAgency);
			rczSalesitem.setVatReturnBox(vt4);
			rczSalesitem.setDefault(true);
			rczSalesitem.setPercentage(true);
			session.save(rczSalesitem);

			TAXItem rczPurchaseItem = new TAXItem(company);
			rczPurchaseItem.setName("Reverse Purchase Charges");
			rczPurchaseItem.setActive(true);
			rczPurchaseItem.setDescription("Reverse Sales Charges");
			rczPurchaseItem.setTaxRate(0.0);
			rczPurchaseItem.setTaxAgency(defaultVATAgency);
			rczPurchaseItem.setVatReturnBox(vt4);
			rczPurchaseItem.setDefault(true);
			rczPurchaseItem.setPercentage(true);
			session.save(rczPurchaseItem);

			TAXItem vatItem5 = new TAXItem(company);
			vatItem5.setName("Exempt Purchases");
			vatItem5.setActive(true);
			vatItem5.setDescription("Exempt Purchases");
			vatItem5.setTaxRate(0.0);
			vatItem5.setTaxAgency(defaultVATAgency);
			vatItem5.setVatReturnBox(vt5);
			vatItem5.setDefault(true);
			vatItem5.setPercentage(true);
			session.save(vatItem5);

			TAXItem vatItem6 = new TAXItem(company);
			vatItem6.setName("Exempt Sales");
			vatItem6.setActive(true);
			vatItem6.setDescription("Exempt Sales");
			vatItem6.setTaxRate(0.0);
			vatItem6.setTaxAgency(defaultVATAgency);
			vatItem6.setVatReturnBox(vt6);
			vatItem6.setDefault(true);
			vatItem6.setPercentage(true);
			session.save(vatItem6);

			TAXItem vatItem7 = new TAXItem(company);
			vatItem7.setName("Not Registered Purchases");
			vatItem7.setActive(true);
			vatItem7.setDescription("Not Registered Purchases");
			vatItem7.setTaxRate(0.0);
			vatItem7.setTaxAgency(defaultVATAgency);
			vatItem7.setPercentage(true);
			// session.save(vt7);
			vatItem7.setVatReturnBox(vt7);
			vatItem7.setDefault(true);
			session.save(vatItem7);

			TAXItem vatItem8 = new TAXItem(company);
			vatItem8.setName("Not Registered Sales");
			vatItem8.setActive(true);
			vatItem8.setDescription("Not Registered Sales");
			vatItem8.setTaxRate(0.0);
			vatItem8.setTaxAgency(defaultVATAgency);
			vatItem8.setPercentage(true);
			// session.save(vt8);
			vatItem8.setVatReturnBox(vt8);
			vatItem8.setDefault(true);
			session.save(vatItem8);

			TAXItem reducePurchaseitem = new TAXItem(company);
			reducePurchaseitem.setName("Reduced Purchases");
			reducePurchaseitem.setActive(true);
			reducePurchaseitem.setDescription("Reduced Purchases");
			reducePurchaseitem.setTaxRate(5.0);
			reducePurchaseitem.setTaxAgency(defaultVATAgency);
			reducePurchaseitem.setVatReturnBox(vt5);
			reducePurchaseitem.setDefault(true);
			reducePurchaseitem.setPercentage(true);
			session.save(reducePurchaseitem);

			TAXItem reduceSalesItem = new TAXItem(company);
			reduceSalesItem.setName("Reduced Sales");
			reduceSalesItem.setActive(true);
			reduceSalesItem.setDescription("Reduced Sales");
			reduceSalesItem.setTaxRate(5.0);
			reduceSalesItem.setTaxAgency(defaultVATAgency);
			reduceSalesItem.setVatReturnBox(vt6);
			reduceSalesItem.setDefault(true);
			reduceSalesItem.setPercentage(true);
			session.save(reduceSalesItem);

			TAXItem rcPurchase17 = new TAXItem(company);
			rcPurchase17.setName("Reverse Charge Purchases Standard (-17.5%)");
			rcPurchase17.setActive(true);
			rcPurchase17
					.setDescription("Reverse Charge Purchases Standard (-17.5%)");
			rcPurchase17.setTaxRate(-17.5);
			rcPurchase17.setTaxAgency(defaultVATAgency);
			rcPurchase17.setVatReturnBox(vt11);
			rcPurchase17.setPercentage(true);
			rcPurchase17.setDefault(true);
			session.save(rcPurchase17);

			TAXItem rcPurchase20 = new TAXItem(company);
			rcPurchase20.setName("Reverse Charge Purchases Standard (-20%)");
			rcPurchase20.setActive(true);
			rcPurchase20
					.setDescription("Reverse Charge Purchases Standard (-20%)");
			rcPurchase20.setTaxRate(-20);
			rcPurchase20.setTaxAgency(defaultVATAgency);
			rcPurchase20.setVatReturnBox(vt11);
			rcPurchase20.setPercentage(true);
			rcPurchase20.setDefault(true);
			session.save(rcPurchase20);

			TAXItem standardPurchase17Item = new TAXItem(company);
			standardPurchase17Item.setName("Standard Purchases");
			standardPurchase17Item.setActive(true);
			standardPurchase17Item.setDescription("Standard Purchases");
			standardPurchase17Item.setTaxRate(17.5);
			standardPurchase17Item.setTaxAgency(defaultVATAgency);
			standardPurchase17Item.setVatReturnBox(vt5);
			standardPurchase17Item.setDefault(true);
			standardPurchase17Item.setPercentage(true);
			session.save(standardPurchase17Item);

			TAXItem standardSales17Item = new TAXItem(company);
			standardSales17Item.setName("Standard Sales");
			standardSales17Item.setActive(true);
			standardSales17Item.setDescription("Standard Sales");
			standardSales17Item.setTaxRate(17.5);
			standardSales17Item.setTaxAgency(defaultVATAgency);
			standardSales17Item.setVatReturnBox(vt6);
			standardSales17Item.setDefault(true);
			standardSales17Item.setPercentage(true);
			session.save(standardSales17Item);

			TAXItem zrPurchaseItem = new TAXItem(company);
			zrPurchaseItem.setName("Zero-Rated Purchases");
			zrPurchaseItem.setActive(true);
			zrPurchaseItem.setDescription("Zero-Rated Purchases");
			zrPurchaseItem.setTaxRate(0.0);
			zrPurchaseItem.setTaxAgency(defaultVATAgency);
			zrPurchaseItem.setVatReturnBox(vt5);
			zrPurchaseItem.setDefault(true);
			zrPurchaseItem.setPercentage(true);
			session.save(zrPurchaseItem);

			TAXItem vatItem15 = new TAXItem(company);
			vatItem15.setName("Zero-Rated Sales");
			vatItem15.setActive(true);
			vatItem15.setDescription("Zero-Rated Sales");
			vatItem15.setTaxRate(0.0);
			vatItem15.setTaxAgency(defaultVATAgency);
			vatItem15.setVatReturnBox(vt6);
			vatItem15.setDefault(true);
			vatItem15.setPercentage(true);
			session.save(vatItem15);

			TAXItem standardPurchase20Item = new TAXItem(company);
			standardPurchase20Item.setName("Standard Purchases (20%)");
			standardPurchase20Item.setActive(true);
			standardPurchase20Item.setDescription("Standard Purchases  (20%)");
			standardPurchase20Item.setTaxRate(20.0);
			standardPurchase20Item.setTaxAgency(defaultVATAgency);
			standardPurchase20Item.setVatReturnBox(vt5);
			standardPurchase20Item.setDefault(true);
			standardPurchase20Item.setPercentage(true);
			session.save(standardPurchase20Item);

			TAXItem standardSales20Item = new TAXItem(company);
			standardSales20Item.setName("Standard Sales  (20%)");
			standardSales20Item.setActive(true);
			standardSales20Item.setDescription("Standard Sales  (20%)");
			standardSales20Item.setTaxRate(20.0);
			standardSales20Item.setTaxAgency(defaultVATAgency);
			standardSales20Item.setVatReturnBox(vt6);
			standardSales20Item.setDefault(true);
			standardSales20Item.setPercentage(true);
			session.save(standardSales20Item);

			// TAXGroup vatGroup1 = new TAXGroup(company);
			// vatGroup1.setName("EC Purchases Goods 0% Group");
			// vatGroup1.setDescription("EC Purchases of Goods Zero-Rated Group");
			// vatGroup1.setActive(true);
			// vatGroup1.setSalesType(false);
			// vatGroup1.setGroupRate(0.0);
			// List<TAXItem> vatItms1 = new ArrayList<TAXItem>();
			// vatItms1.add(egzPurchaseItem);
			// vatItms1.add(zrPurchaseItem);
			// vatGroup1.setTAXItems(vatItms1);
			// vatGroup1.setDefault(true);
			// session.save(vatGroup1);

			TAXGroup ecgPurchase17Group = new TAXGroup(company);
			ecgPurchase17Group.setName("EC Purchases Goods 17.5% Group");
			ecgPurchase17Group.setDescription("EC Purchases of Goods Group");
			ecgPurchase17Group.setActive(true);
			ecgPurchase17Group.setGroupRate(0);
			List<TAXItem> vatItms2 = new ArrayList<TAXItem>();
			vatItms2.add(standardPurchase17Item);
			vatItms2.add(ecgPurchase17Item);
			ecgPurchase17Group.setTAXItems(vatItms2);
			ecgPurchase17Group.setDefault(true);
			session.save(ecgPurchase17Group);

			TAXGroup ecgPurchase20Group = new TAXGroup(company);
			ecgPurchase20Group.setName("EC Purchases Goods 20% Group");
			ecgPurchase20Group.setDescription("EC Purchases of Goods Group");
			ecgPurchase20Group.setActive(true);
			ecgPurchase20Group.setGroupRate(0);
			List<TAXItem> items = new ArrayList<TAXItem>();
			items.add(standardPurchase20Item);
			items.add(ecgPurchase20Item);
			ecgPurchase20Group.setTAXItems(items);
			ecgPurchase20Group.setDefault(true);
			session.save(ecgPurchase20Group);

			TAXGroup ecsPurchase17Group = new TAXGroup(company);
			ecsPurchase17Group.setName("EC Purchases Services 17.5% Group");
			ecsPurchase17Group.setDescription("EC Purchases of Services Group");
			ecsPurchase17Group.setActive(true);
			ecsPurchase17Group.setGroupRate(0);
			List<TAXItem> itemss = new ArrayList<TAXItem>();
			itemss.add(standardPurchase17Item);
			itemss.add(ecsPurchase17Item);
			ecsPurchase17Group.setTAXItems(itemss);
			ecsPurchase17Group.setDefault(true);
			session.save(ecsPurchase17Group);

			TAXGroup ecsPurchase20Group = new TAXGroup(company);
			ecsPurchase20Group.setName("EC Purchases Services 20% Group");
			ecsPurchase20Group.setDescription("EC Purchases of Services Group");
			ecsPurchase20Group.setActive(true);
			ecsPurchase20Group.setGroupRate(0);
			List<TAXItem> items2 = new ArrayList<TAXItem>();
			items2.add(standardPurchase20Item);
			items2.add(ecsPurchase20Item);
			ecsPurchase20Group.setTAXItems(items2);
			ecsPurchase20Group.setDefault(true);
			session.save(ecsPurchase20Group);

			// TAXGroup vatGroup3 = new TAXGroup(company);
			// vatGroup3.setName("EC Sales Goods 0% Group");
			// vatGroup3.setDescription("EC Sales of Goods Group");
			// vatGroup3.setActive(true);
			// vatGroup3.setSalesType(true);
			// vatGroup3.setGroupRate(0.0);
			// List<TAXItem> vatItms3 = new ArrayList<TAXItem>();
			// vatItms3.add(ecszSalesItem);
			// vatItms3.add(egzSalesItem);
			// vatGroup3.setTAXItems(vatItms3);
			// vatGroup3.setDefault(true);
			// vatGroup3.setPercentage((egzSalesItem.isPercentage() &&
			// ecszSalesItem
			// .isPercentage()) ? true : false);
			// session.save(vatGroup3);

			TAXGroup rcPurchase17Group = new TAXGroup(company);
			rcPurchase17Group.setName("Reverse Charge Purchases 17.5% Group");
			rcPurchase17Group.setDescription("Reverse Charge Purchases Group");
			rcPurchase17Group.setActive(true);
			rcPurchase17Group.setGroupRate(0);
			List<TAXItem> vatItms4 = new ArrayList<TAXItem>();
			vatItms4.add(standardPurchase17Item);
			vatItms4.add(rcPurchase17);
			rcPurchase17Group.setTAXItems(vatItms4);
			rcPurchase17Group.setDefault(true);
			session.save(rcPurchase17Group);

			TAXGroup rcPurchase20Group = new TAXGroup(company);
			rcPurchase20Group.setName("Reverse Charge Purchases 20% Group");
			rcPurchase20Group.setDescription("Reverse Charge Purchases Group");
			rcPurchase20Group.setActive(true);
			rcPurchase20Group.setGroupRate(0);
			List<TAXItem> list = new ArrayList<TAXItem>();
			list.add(standardPurchase20Item);
			list.add(rcPurchase20);
			rcPurchase20Group.setTAXItems(list);
			rcPurchase20Group.setDefault(true);
			session.save(rcPurchase20Group);

			// ---------------------TAX_CODES-----------------

			TAXCode s17Code = new TAXCode(company);
			s17Code.setName("S 17.5%");
			s17Code.setDescription("Standard (17.5%)");
			s17Code.setTaxable(true);
			s17Code.setActive(true);
			s17Code.setTAXItemGrpForPurchases(standardPurchase17Item);
			s17Code.setTAXItemGrpForSales(standardSales17Item);
			s17Code.setDefault(true);
			session.save(s17Code);

			TAXCode s20Code = new TAXCode(company);
			s20Code.setName("S 20%");
			s20Code.setDescription("Standard (20%)");
			s20Code.setTaxable(true);
			s20Code.setActive(true);
			s20Code.setTAXItemGrpForPurchases(standardPurchase20Item);
			s20Code.setTAXItemGrpForSales(standardSales20Item);
			s20Code.setDefault(true);
			session.save(s20Code);

			TAXCode rCode = new TAXCode(company);
			rCode.setName("R 5%");
			rCode.setDescription("Reduced (5%)");
			rCode.setTaxable(true);
			rCode.setActive(true);
			rCode.setTAXItemGrpForPurchases(reducePurchaseitem);
			rCode.setTAXItemGrpForSales(reduceSalesItem);
			rCode.setDefault(true);
			session.save(rCode);

			TAXCode zCode = new TAXCode(company);
			zCode.setName("Z 0%");
			zCode.setDescription("Zero-Rated (0%)");
			zCode.setTaxable(true);
			zCode.setActive(true);
			zCode.setTAXItemGrpForPurchases(zrPurchaseItem);
			zCode.setTAXItemGrpForSales(vatItem15);
			zCode.setDefault(true);
			session.save(zCode);

			TAXCode vatCode1 = new TAXCode(company);
			vatCode1.setName("Exempt");
			vatCode1.setDescription("Exempt");
			vatCode1.setTaxable(true);
			vatCode1.setActive(true);
			vatCode1.setTAXItemGrpForPurchases(vatItem5);
			vatCode1.setTAXItemGrpForSales(vatItem6);
			vatCode1.setDefault(true);
			session.save(vatCode1);

			TAXCode egsPurchase17Code = new TAXCode(company);
			egsPurchase17Code.setName("EGS 17.5%");
			egsPurchase17Code.setDescription("EC Goods Standard (17.5%)");
			egsPurchase17Code.setTaxable(true);
			egsPurchase17Code.setActive(true);
			egsPurchase17Code.setTAXItemGrpForPurchases(ecgPurchase17Group);
			egsPurchase17Code.setDefault(true);
			session.save(egsPurchase17Code);

			TAXCode egsPurchase20Code = new TAXCode(company);
			egsPurchase20Code.setName("EGS 20%");
			egsPurchase20Code.setDescription("EC Goods Standard (20%)");
			egsPurchase20Code.setTaxable(true);
			egsPurchase20Code.setActive(true);
			egsPurchase20Code.setTAXItemGrpForPurchases(ecgPurchase20Group);
			egsPurchase20Code.setDefault(true);
			session.save(egsPurchase20Code);

			TAXCode ecgzCode = new TAXCode(company);
			ecgzCode.setName("EGZ 0.0%");
			ecgzCode.setDescription("EC Goods Zero-Rated (0.0%)");
			ecgzCode.setTaxable(true);
			ecgzCode.setActive(true);
			ecgzCode.setTAXItemGrpForPurchases(egzPurchaseItem);
			ecgzCode.setTAXItemGrpForSales(egzSalesItem);
			ecgzCode.setDefault(true);
			session.save(ecgzCode);

			TAXCode ecsPurchase17Code = new TAXCode(company);
			ecsPurchase17Code.setName("ESS 17.5%");
			ecsPurchase17Code.setDescription("EC Services Standard (17.5%)");
			ecsPurchase17Code.setTaxable(true);
			ecsPurchase17Code.setActive(true);
			ecsPurchase17Code.setTAXItemGrpForPurchases(ecsPurchase17Group);
			ecsPurchase17Code.setDefault(true);
			session.save(ecsPurchase17Code);

			TAXCode ecsPurchase20Code = new TAXCode(company);
			ecsPurchase20Code.setName("ESS 20%");
			ecsPurchase20Code.setDescription("EC Services Standard (20%)");
			ecsPurchase20Code.setTaxable(true);
			ecsPurchase20Code.setActive(true);
			ecsPurchase20Code.setTAXItemGrpForPurchases(ecsPurchase20Group);
			ecsPurchase20Code.setDefault(true);
			session.save(ecsPurchase20Code);

			TAXCode ecszCode = new TAXCode(company);
			ecszCode.setName("ESZ 0.0%");
			ecszCode.setDescription("EC Services Zero-Rated (0.0%)");
			ecszCode.setTaxable(true);
			ecszCode.setActive(true);
			ecszCode.setTAXItemGrpForPurchases(ecszPurchaseItem);
			ecszCode.setTAXItemGrpForSales(ecszSalesItem);
			ecszCode.setDefault(true);
			session.save(ecszCode);

			TAXCode rc17Code = new TAXCode(company);
			rc17Code.setName("RC 17.5%");
			rc17Code.setDescription("Reverse Charge (17.5%)");
			rc17Code.setTaxable(true);
			rc17Code.setActive(true);
			rc17Code.setTAXItemGrpForPurchases(rcPurchase17Group);
			rc17Code.setDefault(true);
			session.save(rc17Code);

			TAXCode rc20Code = new TAXCode(company);
			rc20Code.setName("RC 20%");
			rc20Code.setDescription("Reverse Charge  (20%)");
			rc20Code.setTaxable(true);
			rc20Code.setActive(true);
			rc20Code.setTAXItemGrpForPurchases(rcPurchase20Group);
			rc20Code.setDefault(true);
			session.save(rc20Code);

			TAXCode rczCode = new TAXCode(company);
			rczCode.setName("RC 0%");
			rczCode.setDescription("Reverse Charge");
			rczCode.setTaxable(true);
			rczCode.setActive(true);
			rczCode.setTAXItemGrpForSales(rczSalesitem);
			rczCode.setTAXItemGrpForPurchases(rczPurchaseItem);
			rczCode.setDefault(true);
			session.save(rczCode);

			TAXCode notRegisteredCode = new TAXCode(company);
			notRegisteredCode.setName("N");
			notRegisteredCode.setDescription("Not Registered");
			notRegisteredCode.setTaxable(true);
			notRegisteredCode.setActive(true);
			notRegisteredCode.setTAXItemGrpForPurchases(vatItem7);
			notRegisteredCode.setTAXItemGrpForSales(vatItem8);
			notRegisteredCode.setDefault(true);
			session.save(notRegisteredCode);

			TAXCode oCode = new TAXCode(company);
			oCode.setName("O");
			oCode.setDescription("Outside the Scope of VAT");
			oCode.setTaxable(false);
			oCode.setActive(true);
			oCode.setTAXItemGrpForPurchases(null);
			oCode.setTAXItemGrpForSales(null);
			oCode.setDefault(true);
			session.save(oCode);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void init() {
		initDefaultUKAccounts();
		// createUKDefaultVATCodesAndVATAgency(session);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}
}
