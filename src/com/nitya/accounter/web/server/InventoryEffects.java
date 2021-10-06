package com.nitya.accounter.web.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.nitya.accounter.core.InventoryPurchase;
import com.nitya.accounter.core.Quantity;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.TransactionEffectsImpl;
import com.nitya.accounter.core.TransactionItem;
import com.nitya.accounter.web.client.exception.AccounterException;

public class InventoryEffects {

	private Map<TransactionItem, Set<InventoryPurchase>> itemPurchases = new HashMap<TransactionItem, Set<InventoryPurchase>>();

	public void addPurchase(TransactionItem tItem, Quantity qty, double cost) {
		Set<InventoryPurchase> list = itemPurchases.get(tItem);
		if (list == null) {
			list = new HashSet<InventoryPurchase>();
		}
		InventoryPurchase purchase = new InventoryPurchase(tItem.getItem(),
				tItem.getExpenseAccountForInventoryPurchase(), qty, cost);
		list.add(purchase);
		itemPurchases.put(tItem, list);
	}

	public void addPurchase(TransactionItem tItem,
			Map<Double, Quantity> purchases, boolean useAverage,
			Double averageCost) {
		Transaction transaction = tItem.getTransaction();
		Quantity mapped = tItem.getQuantityCopy();

		// Creating New Purchases
		for (Entry<Double, Quantity> entry : purchases.entrySet()) {
			Quantity qty = entry.getValue();
			double purchaseCost = transaction.isVendorCreditMemo() ? entry
					.getKey() - tItem.getUnitPriceInBaseCurrency() : entry
					.getKey();
			double cost = useAverage ? averageCost : purchaseCost;
			mapped = mapped.subtract(qty);
			addPurchase(tItem, qty, cost);
		}

		if (!mapped.isEmpty()) {
			double purchaseCost = transaction.isVendorCreditMemo() ? 0 : tItem
					.getUnitPriceInBaseCurrency();
			double cost = (useAverage && averageCost != null) ? averageCost
					: purchaseCost;
			// finally what ever is unmapped add the that using unitprice
			addPurchase(tItem, mapped, cost);
		}
	}

	public void doEffect() throws AccounterException {
		Set<Transaction> effectedTransactions = new HashSet<Transaction>();
		for (Entry<TransactionItem, Set<InventoryPurchase>> entry : itemPurchases
				.entrySet()) {

			TransactionItem tItem = entry.getKey();
			Transaction transaction = tItem.getTransaction();

			Set<InventoryPurchase> oldPurchases = new HashSet<InventoryPurchase>(
					tItem.getPurchases());
			Set<InventoryPurchase> newPurchases = new HashSet<InventoryPurchase>(
					entry.getValue());

			// Something changed. So do effect the Transaction
			if (oldPurchases.size() != newPurchases.size()) {
				tItem.setPurchases(entry.getValue());
				effectedTransactions.add(transaction);
				continue;
			}

			Iterator<InventoryPurchase> oldIterator = oldPurchases.iterator();
			Iterator<InventoryPurchase> newIterator = newPurchases.iterator();
			while (oldIterator.hasNext()) {
				InventoryPurchase oldP = oldIterator.next();
				boolean isEqual = false;
				while (newIterator.hasNext()) {
					InventoryPurchase newP = newIterator.next();
					isEqual = oldP.getQuantity().equals(newP.getQuantity())
							&& oldP.getCost() == newP.getCost()
							&& oldP.getEffectingAccount().getID() == newP
									.getEffectingAccount().getID();
					if (isEqual) {
						newIterator.remove();
					}
				}
				if (isEqual) {
					oldIterator.remove();
				}
			}
			// Something changed. So do effect the Transaction
			if (!oldPurchases.isEmpty() && !newPurchases.isEmpty()) {
				tItem.setPurchases(entry.getValue());
				effectedTransactions.add(transaction);
			}
		}

		// Finally Effecting Transactions
		for (Transaction transaction : effectedTransactions) {
			TransactionEffectsImpl tEffects = new TransactionEffectsImpl(
					transaction);
			transaction.getEffects(tEffects);
			if (!tEffects.isEmpty()) {
				tEffects.saveOrUpdate();
			}
		}
	}

}
