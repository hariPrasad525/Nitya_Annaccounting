package com.vimukti.accounter.web.server.managers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Depreciation;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FixedAsset;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsEntry;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.SellingOrDisposingFixedAssetList;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.web.client.core.reports.DepreciationShedule;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class FixedAssestManager extends Manager {
	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			long depreciationFrom, long depreciationTo, long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		Query query = session
				.getNamedQuery("getFixedAsset.by.checkStatusand.purchaseDate")
				.setParameter("date", (new FinanceDate(depreciationTo)))
				.setEntity("company", company);
		List<FixedAsset> fixedAssets = query.list();

		List<Long> fixedAssetIDs = new ArrayList<Long>();

		DepreciableFixedAssetsList depreciableFixedAssets = new DepreciableFixedAssetsList();

		Map<Long, List<DepreciableFixedAssetsEntry>> accountViceFixedAssets = new HashMap<Long, List<DepreciableFixedAssetsEntry>>();
		for (FixedAsset fixedAsset : fixedAssets) {
			double amountToBeDepreciatedforThisFixedAsset = Double
					.parseDouble(decimalFormat.format(fixedAsset
							.getCalculatedDepreciatedAmount(
									fixedAsset.getPurchaseDate().compareTo(
											new FinanceDate(depreciationFrom)) <= 0 ? (new FinanceDate(
											depreciationFrom)) : fixedAsset
											.getPurchaseDate(),
									(new FinanceDate(depreciationTo)))));
			// if
			// (accountViceFixedAssets.containsKey(fixedAsset.getAssetAccount()//

			// .getName())) {
			// accountViceFixedAssets.put(fixedAsset.getAssetAccount()
			// .getName(), amountToBeDepreciatedforThisFixedAsset
			// + accountViceFixedAssets.get(fixedAsset
			// .getAssetAccount().getName()));
			// } else {
			// accountViceFixedAssets.put(fixedAsset.getAssetAccount()
			// .getName(), amountToBeDepreciatedforThisFixedAsset);
			// }

			DepreciableFixedAssetsEntry depreciableFixedAssetsEntry = new DepreciableFixedAssetsEntry();
			depreciableFixedAssetsEntry.setID(fixedAsset.getID());
			depreciableFixedAssetsEntry.setFixedAssetName(fixedAsset.getName());
			depreciableFixedAssetsEntry
					.setAmountToBeDepreciated(amountToBeDepreciatedforThisFixedAsset);

			fixedAssetIDs.add(fixedAsset.getID());

			if (accountViceFixedAssets.containsKey(fixedAsset.getAssetAccount()
					.getID())) {
				accountViceFixedAssets
						.get(fixedAsset.getAssetAccount().getID()).add(
								depreciableFixedAssetsEntry);
			} else {
				List<DepreciableFixedAssetsEntry> entries = new ArrayList<DepreciableFixedAssetsEntry>();
				entries.add(depreciableFixedAssetsEntry);
				accountViceFixedAssets.put(
						fixedAsset.getAssetAccount().getID(), entries);
			}

		}

		depreciableFixedAssets
				.setAccountViceFixedAssets(accountViceFixedAssets);

		depreciableFixedAssets.setFixedAssetIDs(fixedAssetIDs);

		return depreciableFixedAssets;

	}

	public ArrayList<SellingOrDisposingFixedAssetList> getSellingOrDisposingFixedAssets(
			long companyId) throws DAOException {

		List<SellingOrDisposingFixedAssetList> fal = new ArrayList<SellingOrDisposingFixedAssetList>();

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("getFixedAsset.by.checkingstatus")
				.setEntity("company", company);

		List<FixedAsset> list = query.list();
		for (FixedAsset fixedAsset : list) {

			SellingOrDisposingFixedAssetList fixedAssetList = new SellingOrDisposingFixedAssetList();
			fixedAssetList.setID(fixedAsset.getID());
			fixedAssetList
					.setAssetAccount(fixedAsset.getAssetAccount().getID());
			fixedAssetList.setAssetNumber(fixedAsset.getAssetNumber());
			fixedAssetList.setName(fixedAsset.getName());
			fixedAssetList.setSoldOrDisposedDate(new ClientFinanceDate(
					fixedAsset.getSoldOrDisposedDate().getDate()));
			fixedAssetList.setSalePrice(fixedAsset.getSalePrice());
			fixedAssetList.setLossOrGain(fixedAsset.getLossOrGain());
			fal.add(fixedAssetList);
		}
		return new ArrayList<SellingOrDisposingFixedAssetList>(fal);
	}

	public ArrayList<ClientFinanceDate> getAllDepreciationFromDates(
			long companyId) throws DAOException {

		List<ClientFinanceDate> fromDates = new ArrayList<ClientFinanceDate>();
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate depreciationStartDate = company.getPreferences()
				.getDepreciationStartDate();
		Calendar depStartDateCal = new GregorianCalendar();
		depStartDateCal.setTime(depreciationStartDate.getAsDateObject());

		FinanceDate tempDate = depreciationStartDate;

		// Query query = session
		// .createQuery(
		// "select distinct(t.transactionDate) from com.vimukti.accounter.core.Transaction t where t.isVoid='false'"
		// );
		Query query = session.getNamedQuery("getdistnct.from.traction.by.date")
				.setParameter("status", Depreciation.APPROVE)
				.setParameter("company", company);
		List list = query.list();
		for (Object dep : list) {
			fromDates.add(new ClientFinanceDate((Long) dep));
		}
		return new ArrayList<ClientFinanceDate>(fromDates);
	}

	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery("getFixedAsset.by.statusAnd.purchaseDate")
				.setParameter("date", (new FinanceDate(depreciationTo)))
				.setEntity("company", company);
		List<FixedAsset> fixedAssets = query.list();
		org.hibernate.Transaction tx = session.beginTransaction();
		for (FixedAsset fixedAsset : fixedAssets) {
			Depreciation depreciation = new Depreciation();
			depreciation.setStatus(Depreciation.APPROVE);
			depreciation.setFixedAsset(fixedAsset);
			depreciation.setDepreciateFrom(new FinanceDate(depreciationFrom));
			depreciation.setDepreciateTo(new FinanceDate(depreciationTo));
			depreciation.setCompany(company);
			session.save(depreciation);
		}

		if (linkedAccounts != null && linkedAccounts.keySet().size() > 0) {
			query = session.getNamedQuery("getAccount.by.idInAccountList")
					.setParameterList("accountsList", linkedAccounts.keySet())
					.setEntity("company", company);
			List<Account> assetAccounts = query.list();
			for (Account assetAccount : assetAccounts) {
				Long changedLinkedAccountID = linkedAccounts.get(assetAccount
						.getID());

				if (changedLinkedAccountID != null) {
					if (!(assetAccount
							.getLinkedAccumulatedDepreciationAccount().getID() == (changedLinkedAccountID))) {
						Account changedLinkedAccount = (Account) session
								.getNamedQuery("getAccount.by.id")
								.setParameter("id", changedLinkedAccountID)
								.setEntity("company", company).uniqueResult();
						assetAccount
								.setLinkedAccumulatedDepreciationAccount(changedLinkedAccount);
						session.saveOrUpdate(assetAccount);
					}
				}

			}

		}
		tx.commit();
	}

	public ClientFinanceDate getDepreciationLastDate(long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery(
						"getDepreciation.by.check.idandStatus.depreciationFor")
				.setParameter("depreciationFor",
						Depreciation.DEPRECIATION_FOR_ALL_FIXEDASSET)
				.setParameter("status", Depreciation.APPROVE)
				.setEntity("company", company);
		List<Depreciation> list = query.list();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			Depreciation dep = list.get(0);
			return new ClientFinanceDate(dep.getDepreciateTo().getDate());
		}
		return null;
	}

	public double getCalculatedDepreciatedAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationFrom, long depreciationTo, long companyId)
			throws DAOException {

		/**
		 * Just Preparing the Dummy fixed asset object with the following fixed
		 * asset information.
		 */

		FixedAsset fixedAsset = new FixedAsset();
		fixedAsset.setBookValue(purchasePrice);
		fixedAsset.setPurchaseDate(new FinanceDate(depreciationFrom));
		fixedAsset.setPurchasePrice(purchasePrice);
		fixedAsset.setDepreciationMethod(depreciationMethod);
		fixedAsset.setDepreciationRate(depreciationRate);

		DecimalFormat decimalFormat = new DecimalFormat("##.##");

		double amountToBeDepreciatedforThisFixedAsset = 0.0;

		/**
		 * In order to calculate the Depreciation amount between two given
		 * dates, the depreciation should be calculated on monthly basis at the
		 * end of each month in the given date range. So to get the end date at
		 * each month, we need to create Calendar Instance on the given from and
		 * to dates.
		 */
		Calendar fromCal = new GregorianCalendar();
		fromCal.setTime(new FinanceDate(depreciationFrom).getAsDateObject());

		Calendar toCal = new GregorianCalendar();
		toCal.setTime(new FinanceDate(depreciationTo).getAsDateObject());

		/**
		 * This is to get the depreciation start date. Here the purpose of this
		 * depreciation start date is, if this Fixed Asset depreciation method
		 * is Reducing Balances then, we need to reduce the bookvalue of this
		 * Fixed Asset at the end of Each Financial year by the depreciation
		 * amount calculated for this fiscal year.
		 */
		Company company = getCompany(companyId);
		FinanceDate startDate = company.getPreferences()
				.getDepreciationStartDate();
		Calendar startDateCal = new GregorianCalendar();
		startDateCal.setTime(startDate.getAsDateObject());

		fixedAsset
				.setOpeningBalanceForFiscalYear(fixedAsset.getPurchasePrice());

		FinanceDate fromDate = new FinanceDate(fromCal.getTime());
		FinanceDate toDat = new FinanceDate(toCal.getTime());

		/**
		 * The following loop should iterate once for each month between the
		 * from date and end date.
		 */

		while (fromDate.compareTo(toDat) < 0) {

			/**
			 * If the depreciation method is Straight Line then The depreciation
			 * should calculate on the Purchase Price other wise the
			 * Depreciation should calculate on the Book value in that year.
			 */
			double amount = fixedAsset.getDepreciationMethod() == Depreciation.METHOD_STRAIGHT_LINE ? fixedAsset
					.getPurchasePrice() : fixedAsset
					.getOpeningBalanceForFiscalYear();

			double depreCiaAmt = getDepreciationAmountAsPerDay(amount,
					fromDate, fixedAsset.getDepreciationRate());

			/**
			 * To calculate the depreciation amount for this month based on the
			 * depreciation rate.
			 */
			double depreciationAmount = Double.parseDouble(decimalFormat
					.format(amount * fixedAsset.getDepreciationRate() / 1200));

			amountToBeDepreciatedforThisFixedAsset += depreCiaAmt;

			/**
			 * Decreasing the Book Value of this Fixed Asset by the calculated
			 * depreciation amount for this month.
			 */
			fixedAsset.setBookValue(fixedAsset.getBookValue() - depreCiaAmt);

			/**
			 * Updating this Fixed Asset's accumulated depreciation amount by
			 * this calculated depreciation amount for this month.
			 */
			fixedAsset.setAccumulatedDepreciationAmount(fixedAsset
					.getAccumulatedDepreciationAmount() + depreCiaAmt);

			/**
			 * Adjusting the from date so that it will hold the next month last
			 * date.
			 */

			int year = fromDate.getYear();
			int month = fromDate.getMonth() + 1;

			// int year = fromCal.get(Calendar.YEAR);
			// int month = fromCal.get(Calendar.MONTH) + 1;

			fromDate.setYear(year);
			fromDate.setMonth(month);

			/**
			 * Adjusting the opening balance of this Fixed Asset each year after
			 * the start date by the actual bookvalue which represents the
			 * updated bookvalue at the end of each month.
			 */
			if (fromDate.getMonth() == startDateCal.get(Calendar.MONTH)) {
				fixedAsset.setOpeningBalanceForFiscalYear(fixedAsset
						.getBookValue());
			}

		}

		return amountToBeDepreciatedforThisFixedAsset;
	}

	private double getDepreciationAmountAsPerDay(double amount,
			FinanceDate fromDate, double depreciationRate) {
		Calendar fromAsCal = Calendar.getInstance();
		double depAmt = 0.0;
		fromAsCal.setTime(fromDate.getAsDateObject());
		int noOfDaysPerYer = 365;
		int year = fromAsCal.get(Calendar.YEAR);
		int dayOfMonth = fromAsCal.get(Calendar.DAY_OF_MONTH);
		if (year % 4 == 0) {
			noOfDaysPerYer = 366;
		}
		double temp1 = (amount * depreciationRate) / 100;
		double temp2 = ((temp1) / noOfDaysPerYer);
		for (int i = dayOfMonth; i < fromAsCal
				.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			depAmt += temp2;
		}

		return depAmt;
	}

	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset, long companyId) throws DAOException {

		/**
		 * In this method we should prepare two maps named disposalSummary and
		 * disposalJournal. Each Map contains several Strings as keys and
		 * corresponing calculated values for them.
		 */

		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		SimpleDateFormat format = new SimpleDateFormat(getCompany(companyId)
				.getPreferences().getDateFormat());
		FixedAssetSellOrDisposeReviewJournal fixedAssetSellOrDisposeReviewJournal = new FixedAssetSellOrDisposeReviewJournal();

		/**
		 * Preparing the keys and values for disposalSummary Map.
		 */
		Company company = getCompany(companyId);
		String purchasedDate = Global.get().messages().purchase() + " ( "
				+ format.format(fixedAsset.getPurchaseDate().getDateAsObject())
				+ " ) ";
		String currentAccumulatedDepreciation = Global.get().messages()
				.currentAccumulatedDepreciation();
		String depreciationTobePosted = Global.get().messages()
				.deprecionToBePosted();
		String rollBackDepreciation = Global.get().messages()
				.rollbackDepreciationTill();
		FinanceDate date = Depreciation.getDepreciationLastDate(company);
		FinanceDate depreciationTillDate = null;
		double depreciationToBePostedAmount = 0.0;
		double rollBackDepreciatinAmount = 0.0;

		FinanceDate lastDepreciationDate = Depreciation
				.getDepreciationLastDate(company);
		if (lastDepreciationDate == null) {
			lastDepreciationDate = company.getPreferences()
					.getDepreciationStartDate();
		}

		// if (fixedAsset.getPurchaseDate().compareTo(lastDepreciationDate) > 0)
		// {
		// lastDepreciationDate = fixedAsset.getPurchaseDate();
		// }
		Calendar lastDepreciationDateCal = new GregorianCalendar();
		lastDepreciationDateCal.setTime(lastDepreciationDate.getAsDateObject());
		lastDepreciationDateCal.set(Calendar.DAY_OF_MONTH, 01);
		lastDepreciationDateCal.set(Calendar.MONTH,
				lastDepreciationDateCal.get(Calendar.MONTH) + 1);

		if (fixedAsset.isNoDepreciation()) {

			Calendar soldOrDisposedDateCal = new GregorianCalendar();
			soldOrDisposedDateCal.setTime(fixedAsset.getSoldOrDisposedDate()
					.getDateAsObject());
			FinanceDate startDate = company.getPreferences()
					.getDepreciationStartDate();
			Calendar startDateCal = new GregorianCalendar();
			startDateCal.setTime(startDate.getAsDateObject());

			Calendar soldYearStartDateCal = new GregorianCalendar(
					soldOrDisposedDateCal.get(Calendar.YEAR),
					startDateCal.get(Calendar.MONTH),
					startDateCal.get(Calendar.DAY_OF_MONTH));
			soldYearStartDateCal.set(Calendar.MONTH,
					soldYearStartDateCal.get(Calendar.MONTH) - 1);
			soldYearStartDateCal.set(Calendar.DAY_OF_MONTH,
					soldYearStartDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			// if (fixedAsset.getPurchaseDate().compareTo(lastDepreciationDate)
			// <= 0) {

			if (lastDepreciationDateCal.compareTo(soldYearStartDateCal) < 0) {

				// Run depreciation from last depreciation date to sold year
				// start date.
				FinanceDate depFrom = new FinanceDate(fixedAsset
						.getPurchaseDate().getDate())
						.compareTo(new FinanceDate(lastDepreciationDate
								.getDate())) <= 0 ? new FinanceDate(
						lastDepreciationDateCal.getTime()) : new FinanceDate(
						fixedAsset.getPurchaseDate().getDate());
				depreciationTobePosted += format.format(depFrom
						.getAsDateObject());
				depreciationTobePosted += Global.get().messages().to();
				depreciationTobePosted += format.format(new FinanceDate(
						soldOrDisposedDateCal.getTime()).getAsDateObject());
				depreciationTobePosted += ")";

				depreciationToBePostedAmount = getCalculatedDepreciatedAmount(
						fixedAsset.getDepreciationMethod(),
						fixedAsset.getDepreciationRate(),
						fixedAsset.getPurchasePrice(), depFrom.getDate(),
						new FinanceDate(soldYearStartDateCal.getTime())
								.getDate(), companyId);
				depreciationToBePostedAmount = Double.parseDouble(decimalFormat
						.format(depreciationToBePostedAmount));

			} else if (lastDepreciationDateCal.compareTo(soldYearStartDateCal) > 0) {

				// Roll back the Depreciation till the sold year start date.
				rollBackDepreciation += format.format(soldYearStartDateCal
						.getTime());
				rollBackDepreciatinAmount = getCalculatedRollBackDepreciationAmount2(
						fixedAsset.getFixedAssetID(), new FinanceDate(
								soldYearStartDateCal.getTime()), company);
				rollBackDepreciatinAmount = Double.parseDouble(decimalFormat
						.format(rollBackDepreciatinAmount));

			}
			if (lastDepreciationDate.compareTo(new FinanceDate(
					soldYearStartDateCal.getTime())) < 0) {
			} else if (lastDepreciationDate.compareTo(new FinanceDate(
					soldYearStartDateCal.getTime())) > 0) {
			}
			// }

			depreciationTillDate = new FinanceDate(
					soldYearStartDateCal.getTime());

		} else {
			// this is for depriciation upto given month

			depreciationTillDate = new FinanceDate(fixedAsset
					.getDepreciationTillDate().getDate());

			if (new FinanceDate(fixedAsset.getPurchaseDate().getDate())
					.compareTo(new FinanceDate(depreciationTillDate.getDate())) <= 0) {

				if (lastDepreciationDate.compareTo(depreciationTillDate) < 0) {
					lastDepreciationDateCal.set(Calendar.MONTH,
							lastDepreciationDateCal.get(Calendar.MONTH) - 1);

					FinanceDate depFrom = new FinanceDate(fixedAsset
							.getPurchaseDate().getDate())

					.compareTo(new FinanceDate(lastDepreciationDate.getDate())) <= 0 ? new FinanceDate(

					lastDepreciationDateCal.getTime())
							: new FinanceDate(fixedAsset.getPurchaseDate()
									.getDate());
					depreciationTobePosted += format.format(depFrom
							.getAsDateObject());
					depreciationTobePosted += "  "
							+ Global.get().messages().to() + "  ";
					depreciationTobePosted += format
							.format(depreciationTillDate.getAsDateObject());
					depreciationTobePosted += ")";
					depreciationToBePostedAmount = getCalculatedDepreciatedAmount(
							fixedAsset.getDepreciationMethod(),
							fixedAsset.getDepreciationRate(),
							fixedAsset.getPurchasePrice(), depFrom.getDate(),
							depreciationTillDate.getDate(), companyId);
					depreciationToBePostedAmount = Double
							.parseDouble(decimalFormat
									.format(depreciationToBePostedAmount));
				} else if (lastDepreciationDate.compareTo(depreciationTillDate) > 0) {

					// Roll back the Depreciation upto given Depreciation till
					// date.
					rollBackDepreciation += format.format(depreciationTillDate
							.getAsDateObject());
					rollBackDepreciatinAmount = getCalculatedRollBackDepreciationAmount2(
							fixedAsset.getFixedAssetID(), depreciationTillDate,
							company);
					rollBackDepreciatinAmount = Double
							.parseDouble(decimalFormat
									.format(rollBackDepreciatinAmount));
				}

			}

		}

		String soldDate = Global.get().messages().Sold()
				+ " ( "
				+ format.format(fixedAsset.getSoldOrDisposedDate()
						.getDateAsObject()) + " ) ";

		/**
		 * Preparing the disposalSummary Map with the above calculated Keys and
		 * corresponding amounts for those keys.
		 */
		Map<String, Double> disposalSummary = new LinkedHashMap<String, Double>();
		disposalSummary.put(purchasedDate, fixedAsset.getPurchasePrice());
		if (!DecimalUtil.isEquals(fixedAsset.getPurchasePrice(),
				fixedAsset.getBookValue())) {
			// disposalSummary.put(currentAccumulatedDepreciation, fixedAsset
			// .getAccumulatedDepreciationAmount());

			disposalSummary
					.put(currentAccumulatedDepreciation, (fixedAsset
							.getPurchasePrice() - fixedAsset.getBookValue()));
		}
		if (!DecimalUtil.isEquals(depreciationToBePostedAmount, 0.0)) {
			disposalSummary.put(depreciationTobePosted,
					depreciationToBePostedAmount);
		}
		if (!DecimalUtil.isEquals(rollBackDepreciatinAmount, 0.0)) {
			disposalSummary
					.put(rollBackDepreciation, rollBackDepreciatinAmount);
		}

		disposalSummary.put(soldDate, fixedAsset.getSalesPrice());

		/**
		 * Preparing the keys and values for disposalJournal Map.
		 */
		String assetAccount = fixedAsset.getAssetAccountName();
		String lessAccoummulatedDepreciation = fixedAsset
				.getLinkedAccumulatedDepreciatedAccountName();
		String salesAccount = fixedAsset.getSalesAccountName();
		String totalCapitalGainString = Global.get().messages()
				.totalCapitalGain();
		String gainOnDisposal = Global.get().messages().gainOnDisposal();
		String lossOnDisposal = Global.get().messages().lossOnDisposal();

		double salesPrice = fixedAsset.getSalesPrice();
		double purchasePrice = fixedAsset.getPurchasePrice();
		double lessAccumulatedDepreciationAmount = fixedAsset
				.getPurchasePrice() - fixedAsset.getBookValue();

		/**
		 * calculating whether there will be any capital gain or not for this
		 * Sell or Dispose of Fixed Asset.
		 */

		double totalCapitalGain = Double
				.parseDouble(decimalFormat.format(DecimalUtil.isGreaterThan(
						salesPrice, purchasePrice) ? (salesPrice - purchasePrice)
						: 0.0));

		double calculatedLessAccDep = (fixedAsset.getPurchasePrice() - fixedAsset
				.getBookValue())
				+ depreciationToBePostedAmount
				- rollBackDepreciatinAmount;
		// long factor = (long) Math.pow(10, 2);
		calculatedLessAccDep = calculatedLessAccDep * 100;
		long tmp = Math.round(calculatedLessAccDep);
		calculatedLessAccDep = (double) tmp / 100;

		/**
		 * calculating gain/loss for this Sell or Dispose of Fixed Asset. If the
		 * calculated amount is +ve then it represents gain or if the calculated
		 * amount is -ve then it represents loss.
		 */
		double lossOrGainOnDisposal = Double.parseDouble(decimalFormat
				.format((salesPrice + calculatedLessAccDep) - purchasePrice
						- totalCapitalGain));

		/**
		 * Preparing the disposalJournal Map with the above calculated Keys and
		 * corresponding amounts for those keys.
		 */
		Map<String, Double> disposalJournal = new LinkedHashMap<String, Double>();
		disposalJournal.put(assetAccount, fixedAsset.getPurchasePrice());

		if (!DecimalUtil.isEquals(calculatedLessAccDep, 0.0)) {
			disposalJournal.put(lessAccoummulatedDepreciation, -1
					* (calculatedLessAccDep));
		}

		if (salesAccount != null) {
			disposalJournal.put(salesAccount, -1 * salesPrice);
		}

		if (DecimalUtil.isGreaterThan(totalCapitalGain, 0.0)) {
			disposalJournal.put(totalCapitalGainString, totalCapitalGain);
		}
		if (!DecimalUtil.isEquals(lossOrGainOnDisposal, 0.0)) {
			if (DecimalUtil.isGreaterThan(lossOrGainOnDisposal, 0.0)) {
				disposalJournal.put(gainOnDisposal, lossOrGainOnDisposal);
			} else {
				disposalJournal.put(lossOnDisposal, lossOrGainOnDisposal);
			}
		}

		fixedAssetSellOrDisposeReviewJournal
				.setDisposalSummary(disposalSummary);
		fixedAssetSellOrDisposeReviewJournal
				.setDisposalJournal(disposalJournal);
		return fixedAssetSellOrDisposeReviewJournal;

	}

	public double getCalculatedRollBackDepreciationAmount(
			long rollBackDepreciationTo, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery("getDepreciation.by.depreciateFrom")
				.setParameter("depreciateFrom",
						(new FinanceDate(rollBackDepreciationTo)))
				.setParameter("status", Depreciation.APPROVE)
				.setEntity("company", company);
		List<Depreciation> list = query.list();
		double rollBackDepAmt = 0.0;
		for (Depreciation dep : list) {
			// for (FixedAsset fixedAsset : dep.getFixedAssets()) {
			if (dep.getFixedAsset() != null)
				for (Transaction trans : dep.getFixedAsset().getTransactions()) {
					rollBackDepAmt += ((JournalEntry) trans).getDebitTotal();
				}
			// }
		}
		return rollBackDepAmt;
	}

	private double getCalculatedRollBackDepreciationAmount2(long fixedAssetID,
			FinanceDate rollBackDepreciationTo, Company company)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getFixedAsset.from.id")
				.setParameter("id", fixedAssetID)
				.setParameter("company", company);

		List<FixedAsset> list = query.list();
		double rollBackDepAmt = 0.0;
		for (FixedAsset f : list) {

			for (Transaction t : f.getTransactions()) {
				if ((!t.isVoid())
						&& (t.getDate().after(rollBackDepreciationTo))) {

					rollBackDepAmt += t.getTotal();
				}
			}

		}
		return rollBackDepAmt;
	}

	public ArrayList<FixedAsset> getFixedAssets(int statusPending,
			long companyId) throws DAOException {
		ArrayList<FixedAsset> fixedAssetList = new ArrayList<FixedAsset>();
		Company company = getCompany(companyId);
		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getFixedAsset.from.status")
				.setParameter("status", statusPending)
				.setParameter("company", company);
		List<FixedAsset> list = query.list();
		for (FixedAsset f : list) {
			fixedAssetList.add(f);
		}
		return fixedAssetList;
	}

	public ArrayList<FixedAsset> getFixedAssetsForReport(int statusPending,
			long companyId) throws DAOException {
		ArrayList<FixedAsset> fixedAssetList = new ArrayList<FixedAsset>();
		Company company = getCompany(companyId);
		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getFixedAsset.from.status2or3")
				.setParameter("status", statusPending)
				.setParameter("company", company);
		List<FixedAsset> list = query.list();
		for (FixedAsset f : list) {
			fixedAssetList.add(f);
		}
		return fixedAssetList;
	}

	public ArrayList<DepreciationShedule> getDepreciationShedule(
			int statusPending, long companyId) throws DAOException {
		ArrayList<DepreciationShedule> list = new ArrayList<DepreciationShedule>();
		ArrayList<FixedAsset> list1 = null;
		try {

			list1 = getFixedAssetsForReport(statusPending, companyId);
			for (FixedAsset asset : list1) {
				DepreciationShedule depreciationShedule = new DepreciationShedule();
				depreciationShedule.setAccumulatedDepreciationAmount(asset
						.getAccumulatedDepreciationAmount());
				depreciationShedule.setAssetAccountName(asset.getAssetAccount()
						.getName());
				depreciationShedule.setAssetName(asset.getName());
				depreciationShedule.setCostOfAsset(asset.getPurchasePrice());
				depreciationShedule.setDepreciationRate(asset
						.getDepreciationRate());
				depreciationShedule.setDisposeDate(new ClientFinanceDate(asset
						.getSoldOrDisposedDate() == null ? new Date(0)
						.getTime() : asset.getSoldOrDisposedDate().getDate()));
				depreciationShedule.setNumber(asset.getAssetNumber());
				depreciationShedule.setPurchaseCost(asset.getPurchasePrice());
				depreciationShedule.setPurchaseDate(new ClientFinanceDate(asset
						.getPurchaseDate().getDate()));
				depreciationShedule.setSoldOrDisposalAmount(asset
						.getSalePrice());
				depreciationShedule.setType(asset.getAssetType());
				depreciationShedule.setNetTotalOfAnFixedAssetAmount(asset
						.getBookValue());
				depreciationShedule.setFixedAssetId(asset.getID());

				list.add(depreciationShedule);
			}
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return list;
	}

}
