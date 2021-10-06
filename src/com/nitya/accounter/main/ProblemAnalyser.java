package com.nitya.accounter.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccountTransaction;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.Quantity;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class ProblemAnalyser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ServerConfiguration.init(null);
		initLogger();

		Session session = HibernateUtil.openSession();
		try {
			analyse(session, 11158);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static void analyse(Session session, long companyID) {

		Account inventoryAsset = (Account) session.get(Account.class, 528022L);

		List<Transaction> transactions = session
				.getNamedQuery("get.All.Transactions")
				.setParameter("companyID", companyID).list();
		ArrayList<Transaction> purchaseTransactions = new ArrayList<Transaction>();
		ArrayList<Transaction> saleTransactions = new ArrayList<Transaction>();
		for (Transaction t : transactions) {
			if (t.getTransactionCategory() == 1) {
				saleTransactions.add(t);
			} else if (t.getTransactionCategory() == 2) {
				purchaseTransactions.add(t);
			}
		}

		HashMap<Long, ArrayList<Double>> itemPurchases = new HashMap<Long, ArrayList<Double>>();
		for (Transaction pt : purchaseTransactions) {
			double atSum = getAssetATsSum(pt, inventoryAsset);
			double tiSum = 0.00D;
			for (TransactionItem ti : pt.getTransactionItems()) {
				Item item = ti.getItem();
				if (item == null || !item.isInventory()) {
					continue;
				}
				ArrayList<Double> purchases = itemPurchases.get(item.getID());
				if (purchases == null) {
					purchases = new ArrayList<Double>();
					itemPurchases.put(item.getID(), purchases);
				}
				Quantity quantity = ti.getQuantityCopy();
				while (!quantity.isEmpty()) {
					Double unitPrice = ti.getUnitPriceInBaseCurrency();
					tiSum += unitPrice;
					purchases.add(unitPrice);
					quantity.setValue(quantity.getValue() - 1);
				}
			}
			if (!DecimalUtil.isEquals(atSum, tiSum)) {
				System.out.println("Error in Transaction : " + pt + "("
						+ pt.getID() + ") AT Sum - " + atSum + " TI Sum - "
						+ tiSum);
			}
		}

		for (Transaction st : saleTransactions) {
			double atSum = getAssetATsSum(st, inventoryAsset);
			double tiSum = 0.00D;
			for (TransactionItem ti : st.getTransactionItems()) {
				Item item = ti.getItem();
				if (item == null) {
					continue;
				}
				ArrayList<Double> purchases = itemPurchases.get(item.getID());
				if (purchases == null || purchases.isEmpty()) {
					// TODO
					System.out.println("No Purchase found for Item: " + item
							+ "(" + item.getID() + ") Transaction - "
							+ st.getID() + " TI ID - " + ti.getID());
					continue;
				}
				Quantity quantity = ti.getQuantityCopy();
				while (!quantity.isEmpty()) {
					Double suitablePurchase = purchases.remove(0);
					tiSum += suitablePurchase;
					quantity.setValue(quantity.getValue() - 1);
				}
			}
			if (!DecimalUtil.isEquals(-atSum, tiSum)) {
				System.out.println("Error in Sale Transaction : " + st + "("
						+ st.getID() + ") AT Sum - " + atSum + " TI Sum - "
						+ tiSum);
			}
		}

	}

	private static double getAssetATsSum(Transaction t, Account inventoryAsset) {
		double sum = 0.00D;
		for (AccountTransaction at : t.getAccountTransactionEntriesList()) {
			if (at.getAccount() != inventoryAsset) {
				continue;
			}
			sum += at.getAmount();
		}
		return sum;
	}

	private static void initLogger() {
		File logsdir = new File(ServerConfiguration.getLogsDir());
		if (!logsdir.exists()) {
			logsdir.mkdirs();
		}
		String path = new File(logsdir, "serverlog").getAbsolutePath();
		try {
			Layout layout = new PatternLayout(
					"%d{dd MMM yyyy HH:mm:ss} %p [%c{1}] - %m %n");
			Logger.getRootLogger().addAppender(
					new DailyRollingFileAppender(layout, path,
							"'.'yyyy-MM-dd-a"));
			Appender appender = Logger.getRootLogger().getAppender("console");
			appender.setLayout(layout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getLogger("org.hibernate").setLevel(Level.INFO);
		Logger.getLogger("com.nitya").setLevel(Level.INFO);

	}
}
