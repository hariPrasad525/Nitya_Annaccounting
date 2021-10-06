package com.vimukti.accounter.company.initialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.AttendanceOrProductionType;
import com.vimukti.accounter.core.AttendancePayHead;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FlatRatePayHead;
import com.vimukti.accounter.core.LiveTaxRate;
import com.vimukti.accounter.core.LiveTaxRateRange;
import com.vimukti.accounter.core.NominalCodeRange;
import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TaxComputationPayHead;
import com.vimukti.accounter.core.UserDefinedPayHead;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.server.managers.LiveTaxRateManager;

public class USCompanyInitializer extends CompanyInitializer {

	// Account prepaidVATaccount;
	// Account ECAcquisitionVATaccount;

	// Account pendingItemReceiptsAccount;
	/**
	 * This is the Account created by default for the purpose of UK when VAT is
	 * Filed
	 */
	// Account VATFiledLiabilityAccount;

	Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();
	/**
	 * Name of the Company
	 */
	String name;// Trading name

	/**
	 * Creates new Instance
	 */
	public USCompanyInitializer(Company company) {
		super(company);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

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
				return new Integer[] { nomincalCode.getMinimum(), nomincalCode.getMaximum() };
			}
		}

		return null;
	}

	public Set<NominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	/**
	 * Initializes all the US default accounts that are useful in the company
	 * 
	 * @param session
	 */
	public void initDefaultUSAccounts() {
		// setDefaultsUSValues();
		String country = company.getCountry();
		String state = company.getPreferences().getTradingAddress().getStateOrProvinence();

		// This is the Account created by default for the purpose of US SalesTax
		Account salesTaxPayable = createDefaultAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.SALES_TAX_PAYABLE, Account.CASH_FLOW_CATEGORY_OPERATING);

		company.setTaxLiabilityAccount(salesTaxPayable);

		generateDefaultPayHeads(country, state);

		createUSDefaultTaxGroup();
	}

	private void generateDefaultPayHeads(String country, String state) {

		Account federalAcc = createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.FEDERAL_WITHHOLDING_TAX, Account.CASH_FLOW_CATEGORY_OPERATING);
		Account stateAcc = createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				company.getPreferences().getTradingAddress().getStateOrProvinence()
						+ AccounterServerConstants.STATE_WITHHOLDING_TAX,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		Account ssnTaxAcc = createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY, AccounterServerConstants.SSN_TAX,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		Account medicareTaxAcc = createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.MEDICARE_TAX, Account.CASH_FLOW_CATEGORY_OPERATING);
		Account disabilityTaxAcc = createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				company.getPreferences().getTradingAddress().getStateOrProvinence()
						+ AccounterServerConstants.DISABILITY_TAX,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		Account trainingTaxAcc = createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				company.getPreferences().getTradingAddress().getStateOrProvinence()
						+ AccounterServerConstants.TRAINING_TAX,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		Account unemploymentTaxAcc = createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				company.getPreferences().getTradingAddress().getStateOrProvinence()
						+ AccounterServerConstants.UNEMPLOYEMENT_TAX,
				Account.CASH_FLOW_CATEGORY_OPERATING);
		
		if(!this.company.getPreferences().isPayrollOnly()) {
			return;
		}

		Session session = HibernateUtil.getCurrentSession();

		List<Object> objectsToSave = new ArrayList<Object>();

		List<LiveTaxRate> taxRates = LiveTaxRateManager.getLiveTaxRatesByCountry(country, state);

		LiveTaxRate federalLiveTax = taxRates.stream()
				.filter(rate -> rate.getTaxType().equals(LiveTaxRate.TAX_TYPE_FEDERAL)).distinct().findFirst().get();
		LiveTaxRate stateLiveTax = taxRates.stream()
				
				.filter(rate -> rate.getTaxType().equals(LiveTaxRate.TAX_TYPE_STATE) && rate.getName().equals(state))
				.distinct().findFirst().get();

		LiveTaxRate medicareTax = taxRates.stream()
				.filter(rate -> rate.getTaxType().equals(LiveTaxRate.TAX_TYPE_MEDICARE)
						&& rate.getName().equals(AccounterServerConstants.MEDICARE_TAX))
				.distinct().findFirst().get();

		LiveTaxRate ssnTax = taxRates.stream().filter(rate -> rate.getTaxType().equals(LiveTaxRate.TAX_TYPE_SSN)
				&& rate.getName().equals(AccounterServerConstants.SSN_TAX)).distinct().findFirst().get();
		LiveTaxRate disabilty = taxRates.stream()
				.filter(rate -> rate.getTaxType().equals(LiveTaxRate.TAX_TYPE_DISABILITY)
						&& rate.getName().equals(state + AccounterServerConstants.DISABILITY_TAX))
				.distinct().findFirst().get();
		LiveTaxRate trainingTax = taxRates.stream()
				.filter(rate -> rate.getTaxType().equals(LiveTaxRate.TAX_TYPE_TRAINING)
						&& rate.getName().equals(state + AccounterServerConstants.TRAINING_TAX))
				.distinct().findFirst().get();
		LiveTaxRate unemployementTax = taxRates.stream()
				.filter(rate -> rate.getTaxType().equals(LiveTaxRate.TAX_TYPE_UNEMPLOYEMENT)
						&& rate.getName().equals(state + AccounterServerConstants.UNEMPLOYEMENT_TAX))
				.distinct().findFirst().get();

		TaxComputationPayHead fcomputationPayHead = new TaxComputationPayHead();
		fcomputationPayHead.setLiveTaxRate(federalLiveTax);
		fcomputationPayHead.setName(PayHead.DEFAULT_FEDERAL);
		fcomputationPayHead.setCompany(company);
		fcomputationPayHead.setLiabilityAccount(federalAcc);
		fcomputationPayHead.setDefault(true);
		fcomputationPayHead.setAffectNetSalary(true);
		fcomputationPayHead.setType(PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS);
		objectsToSave.add(fcomputationPayHead);

		TaxComputationPayHead statecomputationPayHead = new TaxComputationPayHead();
		statecomputationPayHead.setLiveTaxRate(stateLiveTax);
		statecomputationPayHead.setName(state + " " + PayHead.DEFAULT_STATE);
		statecomputationPayHead.setCompany(company);
		statecomputationPayHead.setLiabilityAccount(stateAcc);
		statecomputationPayHead.setDefault(true);
		statecomputationPayHead.setAffectNetSalary(true);
		statecomputationPayHead.setType(PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS);
		objectsToSave.add(statecomputationPayHead);

		TaxComputationPayHead ssnPayHead = new TaxComputationPayHead();
		ssnPayHead.setLiveTaxRate(ssnTax);
		ssnPayHead.setName(PayHead.DEFAULT_SSN_TAX);
		ssnPayHead.setCompany(company);
		ssnPayHead.setEmployeerDeduction(true);
		ssnPayHead.setLiabilityAccount(ssnTaxAcc);
		ssnPayHead.setDefault(true);
		ssnPayHead.setAffectNetSalary(true);
		ssnPayHead.setType(PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS);
		objectsToSave.add(ssnPayHead);

		TaxComputationPayHead medicarePayHead = new TaxComputationPayHead();
		medicarePayHead.setLiveTaxRate(medicareTax);
		medicarePayHead.setName(PayHead.DEFAULT_MEDICARE_TAX);
		medicarePayHead.setCompany(company);
		medicarePayHead.setAffectNetSalary(true);
		medicarePayHead.setEmployeerDeduction(true);
		medicarePayHead.setLiabilityAccount(medicareTaxAcc);
		medicarePayHead.setType(PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS);
		medicarePayHead.setDefault(true);
		objectsToSave.add(medicarePayHead);

		TaxComputationPayHead disabilityPayHead = new TaxComputationPayHead();
		disabilityPayHead.setLiveTaxRate(disabilty);
		disabilityPayHead.setName(state + " " + PayHead.DEFAULT_DISABILITY_TAX);
		disabilityPayHead.setCompany(company);
		disabilityPayHead.setDefault(true);
		disabilityPayHead.setAffectNetSalary(false);
		disabilityPayHead.setEmployeerDeduction(true);
		disabilityPayHead.setLiabilityAccount(disabilityTaxAcc);
		disabilityPayHead.setType(PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS);
		objectsToSave.add(disabilityPayHead);

		TaxComputationPayHead trainingTaxPayHead = new TaxComputationPayHead();
		trainingTaxPayHead.setLiveTaxRate(trainingTax);
		trainingTaxPayHead.setName(state + " " +PayHead.DEFAULT_TRAINING_TAX);
		trainingTaxPayHead.setCompany(company);
		trainingTaxPayHead.setDefault(true);
		trainingTaxPayHead.setAffectNetSalary(false);
		trainingTaxPayHead.setEmployeerDeduction(true);
		trainingTaxPayHead.setLiabilityAccount(trainingTaxAcc);
		trainingTaxPayHead.setType(PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS);
		objectsToSave.add(trainingTaxPayHead);
		
		TaxComputationPayHead unemployementTaxPayHead = new TaxComputationPayHead();
		unemployementTaxPayHead.setLiveTaxRate(unemployementTax);
		unemployementTaxPayHead.setName(state + " " + PayHead.DEFAULT_UNEMPLOYEMENT_TAX);
		unemployementTaxPayHead.setCompany(company);
		unemployementTaxPayHead.setDefault(true);
		unemployementTaxPayHead.setAffectNetSalary(false);
		unemployementTaxPayHead.setEmployeerDeduction(true);
		unemployementTaxPayHead.setLiabilityAccount(unemploymentTaxAcc);
		unemployementTaxPayHead.setType(PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS);
		objectsToSave.add(unemployementTaxPayHead);

		FlatRatePayHead flatRatePayHead = new FlatRatePayHead();
		flatRatePayHead.setName(PayHead.DEFAULT_BASE_SALARY);
		flatRatePayHead.setCalculationPeriod(PayHead.CALCULATION_PERIOD_SALARY);
		flatRatePayHead.setCalculationType(PayHead.CALCULATION_TYPE_FLAT_RATE);
		flatRatePayHead.setType(PayHead.TYPE_EARNINGS_FOR_EMPLOYEES);
		flatRatePayHead.setDefault(true);
		flatRatePayHead.setAffectNetSalary(true);
		flatRatePayHead.setLiabilityAccount(company.getSalariesPayableAccount());
		flatRatePayHead.setCompany(company);
		objectsToSave.add(flatRatePayHead);
		
		AttendanceOrProductionType attendanceOrProductionType = new AttendanceOrProductionType();
		attendanceOrProductionType.setCompany(company);
		attendanceOrProductionType.setName(ClientAttendanceOrProductionType.DEFAULT_MILE);
		attendanceOrProductionType.setPeriodType(PayHead.CALCULATION_PERIOD_SALARY);
		objectsToSave.add(attendanceOrProductionType);

		AttendancePayHead attPayHead = new AttendancePayHead();
		attPayHead.setName(AttendancePayHead.DEFAULT_MILE);
		attPayHead.setCalculationPeriod(PayHead.CALCULATION_PERIOD_SALARY);
		attPayHead.setCalculationType(PayHead.CALCULATION_TYPE_ON_ATTENDANCE);
		attPayHead.setType(PayHead.TYPE_EARNINGS_FOR_EMPLOYEES);
		attPayHead.setCompany(company);
		attPayHead.setAffectNetSalary(true);
		attPayHead.setLiabilityAccount(company.getSalariesPayableAccount());
		attPayHead.setDefault(true);
		attPayHead.setProductionType(attendanceOrProductionType);
		attPayHead.setAttendanceType(AttendancePayHead.ATTENDANCE_ON_EMPLOYEE);
		objectsToSave.add(attPayHead);
		
		AttendanceOrProductionType attendanceOrProductionTypeWork = new AttendanceOrProductionType();
		attendanceOrProductionTypeWork.setCompany(company);
		attendanceOrProductionTypeWork.setName(ClientAttendanceOrProductionType.DEFAULT_WORK);
		attendanceOrProductionTypeWork.setPeriodType(PayHead.CALCULATION_PERIOD_SALARY);
		objectsToSave.add(attendanceOrProductionTypeWork);

		AttendancePayHead attPayHead1 = new AttendancePayHead();
		attPayHead1.setName(AttendancePayHead.DEFAULT_WORK);
		attPayHead1.setCalculationPeriod(PayHead.CALCULATION_PERIOD_SALARY);
		attPayHead1.setCalculationType(PayHead.CALCULATION_TYPE_ON_ATTENDANCE);
		attPayHead1.setType(PayHead.TYPE_EARNINGS_FOR_EMPLOYEES);
		attPayHead1.setCompany(company);
		attPayHead1.setAffectNetSalary(true);
		attPayHead1.setLiabilityAccount(company.getSalariesPayableAccount());
		attPayHead1.setDefault(true);
		attPayHead1.setProductionType(attendanceOrProductionTypeWork);
		attPayHead1.setAttendanceType(AttendancePayHead.ATTENDANCE_ON_EMPLOYEE);
		objectsToSave.add(attPayHead1);

		AttendanceOrProductionType attendanceOrProductionTypeHours = new AttendanceOrProductionType();
		attendanceOrProductionTypeHours.setCompany(company);
		attendanceOrProductionTypeHours.setName(ClientAttendanceOrProductionType.DEFAULT_HOUR);
		attendanceOrProductionTypeHours.setPeriodType(PayHead.CALCULATION_PERIOD_HOURS);
		objectsToSave.add(attendanceOrProductionTypeHours);
		
		AttendancePayHead attPayHead2 = new AttendancePayHead();
		attPayHead2.setName(AttendancePayHead.DEFAULT_HOUR);
		attPayHead2.setCalculationPeriod(PayHead.CALCULATION_PERIOD_HOURS);
		attPayHead2.setCalculationType(PayHead.CALCULATION_TYPE_ON_ATTENDANCE);
		attPayHead2.setType(PayHead.TYPE_EARNINGS_FOR_EMPLOYEES);
		attPayHead2.setCompany(company);
		attPayHead2.setAffectNetSalary(true);
		attPayHead2.setLiabilityAccount(company.getSalariesPayableAccount());
		attPayHead2.setDefault(true);
		attPayHead2.setProductionType(attendanceOrProductionTypeHours);
		attPayHead2.setAttendanceType(AttendancePayHead.ATTENDANCE_ON_EMPLOYEE);
		objectsToSave.add(attPayHead2);

		UserDefinedPayHead userPayHead = new UserDefinedPayHead();
		userPayHead.setName(PayHead.DEFAULT_FLAT_LOANS);
		userPayHead.setType(PayHead.TYPE_LOANS_AND_ADVANCES);
		userPayHead.setLiabilityAccount(company.getSalariesPayableAccount());
		userPayHead.setCompany(company);
		userPayHead.setDefault(true);
		userPayHead.setAffectNetSalary(true);
		objectsToSave.add(userPayHead);

		userPayHead = new UserDefinedPayHead();
		userPayHead.setName(PayHead.DEFAULT_REIMBURSEMENTS_TO_EMPLOYEES);
		userPayHead.setType(PayHead.TYPE_REIMBURSEMENTS_TO_EMPLOYEES);
		userPayHead.setCompany(company);
		userPayHead.setDefault(true);
		userPayHead.setAffectNetSalary(true);
		objectsToSave.add(userPayHead);

		for (Object obj : objectsToSave) {
			session.saveOrUpdate(obj);
		}
	}

	private void createNominalCodesRanges(Session session) {

		Set<NominalCodeRange> nominalCodesRangeSet = new HashSet<NominalCodeRange>();

		NominalCodeRange nominalCodeRange1 = new NominalCodeRange();
		nominalCodeRange1.setAccountSubBaseType(Account.SUBBASETYPE_FIXED_ASSET);
		nominalCodeRange1.setMinimum(NominalCodeRange.RANGE_FIXED_ASSET_MIN);
		nominalCodeRange1.setMaximum(NominalCodeRange.RANGE_FIXED_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange1);

		NominalCodeRange nominalCodeRange2 = new NominalCodeRange();
		nominalCodeRange2.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
		nominalCodeRange2.setMinimum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MIN);
		nominalCodeRange2.setMaximum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange2);

		NominalCodeRange nominalCodeRange3 = new NominalCodeRange();
		nominalCodeRange3.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
		nominalCodeRange3.setMinimum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MIN);
		nominalCodeRange3.setMaximum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MAX);
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
		nominalCodeRange6.setAccountSubBaseType(Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
		nominalCodeRange6.setMinimum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MIN);
		nominalCodeRange6.setMaximum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MAX);
		nominalCodesRangeSet.add(nominalCodeRange6);

		NominalCodeRange nominalCodeRange7 = new NominalCodeRange();
		nominalCodeRange7.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_EXPENSE);
		nominalCodeRange7.setMinimum(NominalCodeRange.RANGE_OTHER_EXPENSE_MIN);
		nominalCodeRange7.setMaximum(NominalCodeRange.RANGE_OTHER_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange7);

		NominalCodeRange nominalCodeRange8 = new NominalCodeRange();
		nominalCodeRange8.setAccountSubBaseType(Account.SUBBASETYPE_EXPENSE);
		nominalCodeRange8.setMinimum(NominalCodeRange.RANGE_EXPENSE_MIN);
		nominalCodeRange8.setMaximum(NominalCodeRange.RANGE_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange8);

		NominalCodeRange nominalCodeRange9 = new NominalCodeRange();
		nominalCodeRange9.setAccountSubBaseType(Account.SUBBASETYPE_LONG_TERM_LIABILITY);
		nominalCodeRange9.setMinimum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MIN);
		nominalCodeRange9.setMaximum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange9);

		NominalCodeRange nominalCodeRange10 = new NominalCodeRange();
		nominalCodeRange10.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_ASSET);
		nominalCodeRange10.setMinimum(NominalCodeRange.RANGE_OTHER_ASSET_MIN);
		nominalCodeRange10.setMaximum(NominalCodeRange.RANGE_OTHER_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange10);

		this.setNominalCodeRange(nominalCodesRangeSet);

	}

	public void createUSDefaultTaxGroup() {

		try {

			Session session = HibernateUtil.getCurrentSession();

			// Default TaxGroup Creation
			TAXAgency defaultTaxAgency = new TAXAgency();
			defaultTaxAgency.setCompany(company);
			defaultTaxAgency.setActive(Boolean.TRUE);
			defaultTaxAgency.setName("Tax Agency");
			defaultTaxAgency.setTaxType(TAXAgency.TAX_TYPE_SALESTAX);
			defaultTaxAgency.setPaymentTerm(
					(PaymentTerms) session.getNamedQuery("unique.name.PaymentTerms").setEntity("company", company)
							.setParameter("name", "Net Monthly", EncryptedStringType.INSTANCE).list().get(0));
			Account salesTaxPayable = (Account) session.getNamedQuery("unique.name.Account")
					.setEntity("company", company)
					.setParameter("name", "Sales Tax Payable", EncryptedStringType.INSTANCE).list().get(0);
			defaultTaxAgency.setSalesLiabilityAccount(salesTaxPayable);
			defaultTaxAgency.setPurchaseLiabilityAccount(salesTaxPayable);

			defaultTaxAgency.setDefault(true);
			session.save(defaultTaxAgency);

			// Set<TaxRates> taxRates = new HashSet<TaxRates>();
			//
			// TaxRates taxRate = new TaxRates();
			// taxRate.setRate(0);
			// taxRate.setAsOf(new FinanceDate());
			// taxRate.setID(SecureUtils.createID());
			// taxRates.add(taxRate);

			TAXItem defaultTaxItem = new TAXItem(company);
			defaultTaxItem.setActive(Boolean.TRUE);
			defaultTaxItem.setName("None");
			defaultTaxItem.setTaxAgency(defaultTaxAgency);
			defaultTaxItem.setVatReturnBox(null);
			defaultTaxItem.setTaxRate(0);
			defaultTaxItem.setDefault(true);
			session.save(defaultTaxItem);

			// TAXCode defaultTaxCodeforTaxItem = new TAXCode(
			// (TAXItemGroup) defaultTaxItem);
			// session.save(defaultTaxCodeforTaxItem);

			// TAXGroup defaultTaxGroup = new TAXGroup();
			// defaultTaxGroup.setName("Tax Group");
			// defaultTaxGroup.setID(SecureUtils.createID());
			// defaultTaxGroup.setActive(Boolean.TRUE);
			// defaultTaxGroup.setSalesType(true);
			//
			// List<TAXItem> taxItems = new ArrayList<TAXItem>();
			// taxItems.add(defaultTaxItem);
			// defaultTaxGroup.setTAXItems(taxItems);
			// defaultTaxGroup.setGroupRate(0);
			// defaultTaxGroup.setDefault(true);
			// session.save(defaultTaxGroup);
			// TAXCode defaultTaxCodeforTaxGroup = new TAXCode((TAXItemGroup)
			// defaultTaxGroup);
			// session.save(defaultTaxCodeforTaxGroup);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init() {
		// super.init();
		initDefaultUSAccounts();
	}

	/*
	 * @Override public void office_expense() { stub
	 * 
	 * }
	 * 
	 * @Override public void motor_veichel_expense() { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 * 
	 * @Override public void travel_expenses() { stub
	 * 
	 * }
	 * 
	 * @Override public void other_expenses() { stub
	 * 
	 * }
	 * 
	 * @Override public void Cost_of_good_sold() { stub
	 * 
	 * }
	 * 
	 * @Override public void other_direct_cost() { stub
	 * 
	 * }
	 */

	@Override
	String getDateFormat() {
		return AccounterServerConstants.MMddyyyy;
	}

}
