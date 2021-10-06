package com.nitya.accounter.company.initialize;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.web.server.util.CountryPreferenceFactory;

public class CompanyInitializedFactory {

	/**
	 * this is use to get the initializer for the company
	 * 
	 * @param company
	 */

	public static CompanyInitializer getInitializer(Company company) {
		if (company.getCountry().equals(CountryPreferenceFactory.UNITED_STATES)) {
			return new USCompanyInitializer(company);
		} else if (company.getCountry().equals(
				CountryPreferenceFactory.UNITED_KINGDOM)) {
			return new UKCompanyInitializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.INDIA)) {
			return new IndianCompanyInitializer(company);
		} else if (company.getCountry().equals(
				CountryPreferenceFactory.AUSTRALIA)) {
			return new AustraliaCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.FRANCE)) {
			return new FranceCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.JAPAN)) {
			return new JapanCompanyIntializer(company);
		} else if (company.getCountry().equals(
				CountryPreferenceFactory.NEW_ZEALAND)) {
			return new NewZealandCompanyIntializer(company);
		} else if (company.getCountry().equals(
				CountryPreferenceFactory.NETHERLANDS)) {
			return new NetherlandCompanyIntializer(company);
		} else if (company.getCountry().equals(
				CountryPreferenceFactory.SINGAPORE)) {
			return new SingaporeCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.CANADA)) {
			return new CanadaCompanyIntializer(company);
		} else if (company.getCountry()
				.equals(CountryPreferenceFactory.GERMANY)) {
			return new GermanyCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.ITALY)) {
			return new ItalyCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.BRAZIL)) {
			return new BrazilCompanyIntializer(company);
		} else if (company.getCountry().equals(
				CountryPreferenceFactory.SWITZERLAND)) {
			return new SwitzerlandCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.RUSSIA)) {
			return new RusiaCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.SPAIN)) {
			return new SpainCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.MEXICO)) {
			return new MexicoCompanyIntializer(company);
		} else if (company.getCountry().equals(
				CountryPreferenceFactory.INDONESIA)) {
			return new IndonesianCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.TURKEY)) {
			return new TurkeyCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.CHINA)) {
			return new ChinaCompanyIntializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.SWEDEN)) {
			return new SwedenCompanyInitializer(company);
		} else if (company.getCountry().equals(CountryPreferenceFactory.POLAN)) {
			return new PolandCompanyInitializer(company);
		} else if (company.getCountry().equals(
				CountryPreferenceFactory.KOREA_SOUTH)) {
			return new SouthCoreaCompanyIntializer(company);
		}

		else {
			return new OtherCompanyInitializer(company);
		}

	}
}
