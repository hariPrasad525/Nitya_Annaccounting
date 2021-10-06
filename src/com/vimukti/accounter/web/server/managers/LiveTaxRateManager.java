package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.LiveTaxRate;
import com.vimukti.accounter.core.LiveTaxRateRange;
import com.vimukti.accounter.utils.HibernateUtil;

public class LiveTaxRateManager {

	public static List<LiveTaxRate> getLiveTaxRatesByCountry(String country, String state) {
		Session session = HibernateUtil.getCurrentSession();

		List<LiveTaxRate> taxs = getLiveTaxRates(country);

		try {
			if (taxs == null || taxs.isEmpty()) {
				generateRanges(session);
				
				LiveTaxRate medicareTax = new LiveTaxRate();
				medicareTax.setCountry(country);
				medicareTax.setTaxType(LiveTaxRate.TAX_TYPE_MEDICARE);
				medicareTax.setName(AccounterServerConstants.MEDICARE_TAX);
				medicareTax.setTaxFilingStatus(LiveTaxRate.FILING_STATUS_SINGLE);
				
				List<LiveTaxRateRange> ssnranges = new ArrayList<LiveTaxRateRange>();
				LiveTaxRateRange ssnRate = new LiveTaxRateRange();
				ssnRate.setStart(0);
				ssnRate.setEnd(0);
				ssnRate.setRate(new Double(1.45));
				ssnRate.setTaxRate(medicareTax);
				ssnranges.add(ssnRate);
				medicareTax.setRates(ssnranges);
				
				LiveTaxRate ssnTax = new LiveTaxRate();
				ssnTax.setCountry(country);
				ssnTax.setTaxType(LiveTaxRate.TAX_TYPE_SSN);
				ssnTax.setName(AccounterServerConstants.SSN_TAX);
				ssnTax.setTaxFilingStatus(LiveTaxRate.FILING_STATUS_SINGLE);
				
				ssnranges = new ArrayList<LiveTaxRateRange>();
				ssnRate = new LiveTaxRateRange();
				ssnRate.setStart(0);
				ssnRate.setEnd(0);
				ssnRate.setRate(new Double(6.2));
				ssnRate.setTaxRate(ssnTax);
				ssnranges.add(ssnRate);
				ssnTax.setRates(ssnranges);
				
				LiveTaxRate disabilty = new LiveTaxRate();
				disabilty.setCountry(country);
				disabilty.setTaxType(LiveTaxRate.TAX_TYPE_DISABILITY);
				disabilty.setName(state + AccounterServerConstants.DISABILITY_TAX);
				disabilty.setTaxFilingStatus(LiveTaxRate.FILING_STATUS_SINGLE);
				
				ssnranges = new ArrayList<LiveTaxRateRange>();
				ssnRate = new LiveTaxRateRange();
				ssnRate.setStart(0);
				ssnRate.setEnd(0);
				ssnRate.setRate(new Double(0.9));
				ssnRate.setTaxRate(disabilty);
				ssnranges.add(ssnRate);
				disabilty.setRates(ssnranges);
				
				LiveTaxRate trainingTax = new LiveTaxRate();
				trainingTax.setCountry(country);
				trainingTax.setTaxType(LiveTaxRate.TAX_TYPE_TRAINING);
				trainingTax.setName(state + AccounterServerConstants.TRAINING_TAX);
				trainingTax.setTaxFilingStatus(LiveTaxRate.FILING_STATUS_SINGLE);
				
				ssnranges = new ArrayList<LiveTaxRateRange>();
				ssnRate = new LiveTaxRateRange();
				ssnRate.setStart(0);
				ssnRate.setEnd(7000);
				ssnRate.setRate(new Double(0.1));
				ssnRate.setTaxRate(trainingTax);
				ssnranges.add(ssnRate);
				trainingTax.setRates(ssnranges);
				
				LiveTaxRate umployementTax = new LiveTaxRate();
				umployementTax.setCountry(country);
				umployementTax.setTaxType(LiveTaxRate.TAX_TYPE_UNEMPLOYEMENT);
				umployementTax.setName(state + AccounterServerConstants.UNEMPLOYEMENT_TAX);
				umployementTax.setTaxFilingStatus(LiveTaxRate.FILING_STATUS_SINGLE);
				
				ssnranges = new ArrayList<LiveTaxRateRange>();
				ssnRate = new LiveTaxRateRange();
				ssnRate.setStart(0);
				ssnRate.setEnd(7000);
				ssnRate.setRate(new Double(0.1));
				ssnRate.setTaxRate(umployementTax);
				ssnranges.add(ssnRate);
				umployementTax.setRates(ssnranges);

				session.saveOrUpdate(disabilty);
				session.saveOrUpdate(ssnTax);
				session.saveOrUpdate(medicareTax);
				session.saveOrUpdate(trainingTax);
				session.saveOrUpdate(umployementTax);
				
				taxs = getLiveTaxRates(country);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taxs;
	}

	private static void generateRanges(Session session) {
		List<List<String>> taxLines = CSVFileReader.readFile("config/USIncomeTaxBrackets.csv");
		for (List<String> taxLine : taxLines) {
			if (taxLine == taxLines.get(0)) {
				continue;
			}
			LiveTaxRate taxRate = new LiveTaxRate();
			for (int i = 0; i < taxLine.size(); i++) {
				String item = taxLine.get(i).trim();
				if (i == 0) {
					taxRate.setName(item);
				} else if (i == 1) {
					taxRate.setTaxFilingStatus(item);
				} else if (i == 2) {
					taxRate.setStandardDeductions(new Long(item));
				} else if (i == 3) {
					taxRate.setTaxType(item);
				} else if (i == 4) {
					taxRate.setCountry(item);
				} else if (i == 5) {
					taxRate.setYear(item);
				} else if (i == 6) {
					List<String> ranges = Arrays.asList(item.split("-"));
					List<LiveTaxRateRange> fenderranges = new ArrayList<LiveTaxRateRange>();
					for (String range : ranges) {
						String[] rates = range.split("/");
						LiveTaxRateRange rr = new LiveTaxRateRange();
						rr.setStart(new Double(rates[1].trim()));
						String endAmt = rates[2].trim();
						rr.setPlusMore(endAmt.endsWith("+"));
						rr.setEnd(new Double(endAmt.replace("+", "")));
						rr.setRate(new Double(rates[0].trim()));
						rr.setTaxRate(taxRate);
						fenderranges.add(rr);
					}
					taxRate.setRates(fenderranges);
				}
			}
			session.saveOrUpdate(taxRate);
		}
	}

	private static List<LiveTaxRate> getLiveTaxRates(String country) {
		Session session = HibernateUtil.getCurrentSession();
		String hqlQuery = "from " + LiveTaxRate.class.getName() + " entity where entity.country=:country";
		Query query = session.createQuery(hqlQuery).setParameter("country", country);
		return query.list();
	}

}
