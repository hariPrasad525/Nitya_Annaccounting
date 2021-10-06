package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.math.BigDecimal;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.SalesOrder;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Warehouse;

public class TransactionMigrator<T extends Transaction> implements IMigrator<T> {
	boolean enableTaxCode = false;
	boolean enableDiscount = false;
	boolean taxCodeOnePerTransaction = false;
	boolean discountOnePerTransaction = false;
	TAXCode onePerTXNCode = null;
	Double onePerTXNDiscount = 0.0;

	@Override
	public JSONObject migrate(T obj, MigratorContext context)
			throws JSONException {
		Invoice invoice = null;
		EnterBill enterBill = null;
		CashPurchase cashPurchase = null;
		if (obj instanceof Invoice) {
			invoice = (Invoice) obj;
		}
		if (obj instanceof EnterBill) {
			enterBill = (EnterBill) obj;
		}
		if (obj instanceof CashPurchase) {
			cashPurchase = (CashPurchase) obj;
		}
		// Migrating children's of SalesOrder,SalesQuotation,Credit,Charge
		String key = null;
		Map<String, List<Long>> childrenMap = context.getChildrenMap();
		if (obj instanceof SalesOrder) {
			key = "com.vimukti.accounter.shared.customer.SalesOrderItem";
			context.putChilderName("SalesOrder", "transactionItems");
		}
		if (obj instanceof PurchaseOrder) {
			key = "com.vimukti.accounter.shared.vendor.PurchaseOrderItem";
			context.putChilderName("PurchaseOrder", "transactionItems");
		}
		if (obj instanceof Estimate) {
			Estimate estimate = (Estimate) obj;
			int estimateType = estimate.getEstimateType();
			if (estimateType == Estimate.QUOTES
					|| estimateType == Estimate.CHARGES
					|| estimateType == Estimate.BILLABLEEXAPENSES) {
				key = "com.vimukti.accounter.shared.customer.SalesQuotationItem";
				context.putChilderName("SalesQuotation", "transactionItems");
			} else if (estimateType == Estimate.CREDITS) {
				key = "com.vimukti.accounter.shared.customer.TransactionCreditItem";
				context.putChilderName("Credit", "transactionItems");
			}
		}
		List<Long> list = childrenMap.get(key);
		if (list == null) {
			list = new ArrayList<Long>();
			if (key != null) {
				childrenMap.put(key, list);
			}
		}

		List<TransactionItem> transactionItems = obj.getTransactionItems();
		CompanyPreferences preferences = context.getCompany().getPreferences();

		// CHECKING TAXCODE IS ONE PER TRANSACTION THEN ALL TRANSACTIONS ITEMS
		// ARE SAME OR NOT
		boolean trackTax = preferences.isTrackTax();
		boolean taxPerDetailLine = preferences.isTaxPerDetailLine();
		if (trackTax && !taxPerDetailLine && !transactionItems.isEmpty()) {
			isAllTransactionItemsTAXCodeSame(transactionItems, preferences);

		}
		// CHECKING DISCOUNT IS ONE PER TRANSACTION THEN ALL TRANSACTIONS ITEMS
		// ARE SAME OR NOT
		Boolean trackDiscounts = preferences.isTrackDiscounts();
		boolean discountPerDetailLine = preferences.isDiscountPerDetailLine();
		if (trackDiscounts && !discountPerDetailLine
				&& !transactionItems.isEmpty()) {
			isAllTransactionItemsDiscountsSame(transactionItems);
		}

		JSONObject transaction = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, transaction, context);
		transaction.put("date", obj.getDate().toEpochDay());
		{
			String tXNNumber = obj.getNumber();
			int type = obj.getType();
			if (obj instanceof CashPurchase || tXNNumber.isEmpty()
					|| context.isDuplicateTXNNumber(type, tXNNumber)) {
				String maxTXNNumber = context.getMaxTXNNumber();
				if (maxTXNNumber == null) {
					// THIS EXCUTE ONLY FIRST TIME
					maxTXNNumber = NumberUtils.getNextTransactionNumber(type,
							context.getCompany().getId());
				} else {
					maxTXNNumber = NumberUtils
							.getStringwithIncreamentedDigit(maxTXNNumber);
				}
				context.setMaxTXNNumber(maxTXNNumber);
				tXNNumber = maxTXNNumber;
			}
			transaction.put("number", tXNNumber);
			Set<String> txns = context.getTransactionNumbers().get(type);
			if (txns == null) {
				txns = new HashSet<>();
				context.getTransactionNumbers().put(type, txns);
			}
			txns.add(tXNNumber);
		}
		if (obj.isDraft()) {
			transaction.put("objStatus", "DRAFT");
		}
		AccounterClass accounterClass = obj.getAccounterClass();
		if (accounterClass != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.AccountClass",
					inter);
			String localId = context.getLocalIdProvider().getOrCreate(
					accounterClass);
			inter.put("@_oid",
					context.get("AccounterClass", accounterClass.getID()));
			inter.put("@_lid", localId);
			transaction.put("accountClass", outter);
		}
		Location location = obj.getLocation();
		if (location != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Location", inter);
			String localId = context.getLocalIdProvider().getOrCreate(location);
			inter.put("@_oid", context.get("Location", location.getID()));
			inter.put("@_lid", localId);
			transaction.put("location", outter);
		}
		transaction.put("currencyFactor",
				BigDecimal.valueOf(obj.getCurrencyFactor()));
		transaction.put("notes", obj.getMemo());
		int type = obj.getType();
		transaction.put("transactionType", PicklistUtilMigrator
				.getTransactionTypeIdentifier(
						type,
						type == Transaction.TYPE_ESTIMATE ? ((Estimate) obj)
								.getEstimateType() : 0));
		Job job = obj.getJob();
		if (job != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.customer.Project", inter);
			String localId = context.getLocalIdProvider().getOrCreate(job);
			inter.put("@_oid", context.get("Job", job.getID()));
			inter.put("@_lid", localId);
			transaction.put("project", outter);
		}
		if (obj instanceof JournalEntry) {
			return transaction;
		}
		String fullyQualifiedName = getType(obj);
		if (!transactionItems.isEmpty()) {
			JSONArray tItems = new JSONArray();
			JSONObject items = new JSONObject();
			items.put(fullyQualifiedName, tItems);
			for (TransactionItem transactionItem : transactionItems) {
				JSONObject tItem = new JSONObject();
				String _lid = context.getLocalIdProvider().getOrCreate(
						transactionItem);
				tItem.put("@id", _lid);
				tItem.put("@_lid", _lid);
				if (transactionItem.getType() == TransactionItem.TYPE_ACCOUNT) {
					tItem.put("transactionItemType", "ACCOUNT");
					Account account = transactionItem.getAccount();
					{
						JSONObject inter = new JSONObject();
						JSONObject outter = new JSONObject();
						outter.put(
								"com.vimukti.accounter.shared.common.Account",
								inter);
						inter.put("@_oid",
								context.get("Account", account.getID()));
						inter.put("@_lid", context.getLocalIdProvider()
								.getOrCreate(account));
						tItem.put("account", outter);
					}
				} else {
					tItem.put("transactionItemType", "ITEM");
					Item item = transactionItem.getItem();
					String itemType = getItemType(item);
					{
						JSONObject inter = new JSONObject();
						JSONObject outter = new JSONObject();
						outter.put(itemType, inter);
						inter.put("@_oid", context.get("Item", item.getID()));
						inter.put("@_lid", context.getLocalIdProvider()
								.getOrCreate(item));
						tItem.put("item", outter);
					}
					{
						Quantity quantity = transactionItem.getQuantity();
						Unit unit = quantity.getUnit();
						if (unit != null) {
							JSONObject inter = new JSONObject();
							JSONObject outter = new JSONObject();
							outter.put(
									"com.vimukti.accounter.shared.inventory.Unit",
									inter);
							String localId = context.getLocalIdProvider()
									.getOrCreate(unit);
							inter.put(
									"@_oid",
									context.get(
											"com.vimukti.accounter.shared.inventory.Unit",
											unit.getID()));
							inter.put("@_lid", localId);
							tItem.put("unit", outter);
						}
						tItem.put("quantity", quantity.getValue());
					}
				}

				tItem.put("description", transactionItem.getDescription());
				tItem.put("unitPrice",
						BigDecimal.valueOf(transactionItem.getUnitPrice()));
				tItem.put("amountIncludesTax",
						transactionItem.isAmountIncludeTAX());
				AccounterClass classCategory = transactionItem
						.getAccounterClass();
				if (classCategory != null) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put(
							"com.vimukti.accounter.shared.common.AccountClass",
							inter);
					inter.put("@_oid", context.get("AccounterClass",
							classCategory.getID()));
					inter.put("@_lid", context.getLocalIdProvider()
							.getOrCreate(classCategory));
					tItem.put("accountClass", outter);
				}
				TAXCode itemTaxCode = transactionItem.getTaxCode();
				if (taxPerDetailLine && itemTaxCode != null) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put("com.vimukti.accounter.shared.tax.TAXCode",
							inter);
					inter.put("@_oid",
							context.get("TaxCode", itemTaxCode.getID()));
					inter.put("@_lid", context.getLocalIdProvider()
							.getOrCreate(itemTaxCode));
					tItem.put("taxCode", outter);
				}
				Transaction txn = transactionItem.getTransaction();
				if (txn != null) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put(
							"com.vimukti.accounter.shared.common.TaxableTransaction",
							inter);
					String txnLid = context.getLocalIdProvider().getOrCreate(
							txn);
					inter.put("@id", txnLid);
					inter.put("@_lid", txnLid);
					tItem.put("transaction", outter);
				}
				tItem.put("taxable", transactionItem.isTaxable());
				double itemDiscount = transactionItem.getDiscount();
				if (discountPerDetailLine && itemDiscount != 0) {
					tItem.put("discount",
							BigDecimal.valueOf(itemDiscount / 100));
				}
				Warehouse wareHouse = transactionItem.getWareHouse();
				if (wareHouse != null) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put(
							"com.vimukti.accounter.shared.inventory.Warehouse",
							inter);
					inter.put("@_oid",
							context.get("Warehouse", wareHouse.getID()));
					inter.put("@_lid", context.getLocalIdProvider()
							.getOrCreate(wareHouse));
					tItem.put("warehouse", outter);
				}
				if (key != null) {
					// Setting children of SalesOrder,SalesQuotation,Credit to
					// context
					list.add(transactionItem.getID());
				}
				// Setting Billable Transaction related things
				if (obj instanceof EnterBill) {
					boolean billable = transactionItem.isBillable();
					tItem.put("isBillable", billable);
					Job billableJob = transactionItem.getJob();
					if (billableJob != null) {
						JSONObject inter = new JSONObject();
						JSONObject outter = new JSONObject();
						outter.put(
								"com.vimukti.accounter.shared.customer.Project",
								inter);
						String localId = context.getLocalIdProvider()
								.getOrCreate(billableJob);
						inter.put("@_oid",
								context.get("Job", billableJob.getID()));
						inter.put("@_lid", localId);
						tItem.put("project", outter);
					}
					Customer customer = transactionItem.getCustomer();
					if (customer != null) {
						JSONObject inter = new JSONObject();
						JSONObject outter = new JSONObject();
						outter.put(
								"com.vimukti.accounter.shared.customer.Customer",
								inter);
						String localId = context.getLocalIdProvider()
								.getOrCreate(customer);
						inter.put("@_oid",
								context.get("Customer", customer.getID()));
						inter.put("@_lid", localId);
						tItem.put("customer", outter);
					}
				}
				TransactionItem referringTransactionItem = transactionItem
						.getReferringTransactionItem();
				boolean isReferenceTransactionItem = transactionItem.getType() == TransactionItem.TYPE_ITEM
						&& referringTransactionItem != null;
				// InvoiceItem migration
				if (invoice != null && isReferenceTransactionItem) {
					createJSON(context,
							getEstimateType(referringTransactionItem
									.getTransaction()),
							referringTransactionItem,
							getPropName(referringTransactionItem), tItem);
				}
				// EnterBillItem or CashPurchaseItem Migration and Setting
				// PurchaseOrderItem in CashPurchaseItem
				if ((enterBill != null || cashPurchase != null)
						&& isReferenceTransactionItem) {
					createJSON(
							context,
							"com.vimukti.accounter.shared.vendor.PurchaseOrderItem",
							referringTransactionItem, "purchaseOrderItem",
							tItem);
				}
				tItems.put(tItem);
			}
			transaction.put("transactionItems", items);
			transaction.put("amountIncludesTax", transactionItems.get(0)
					.isAmountIncludeTAX());
			if (!taxPerDetailLine) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.vimukti.accounter.shared.tax.TAXCode", inter);
				inter.put("@_oid",
						context.get("TaxCode", onePerTXNCode.getID()));
				inter.put("@_lid",
						context.getLocalIdProvider().getOrCreate(onePerTXNCode));
				transaction.put("taxCode", outter);
			}
			if (!discountPerDetailLine && onePerTXNDiscount != 0) {
				double value = onePerTXNDiscount.doubleValue() / 100;
				transaction.put("discount", BigDecimal.valueOf(value));
			}
		}
		return transaction;
	}

	/**
	 * CHECKING TAXCODE IS ONE PER TRANSACTION THEN ALL TRANSACTIONS ITEMS ARE
	 * SAME OR NOT
	 * 
	 * @param transactionItems
	 * @param preferences
	 */
	private void isAllTransactionItemsTAXCodeSame(
			List<TransactionItem> transactionItems,
			CompanyPreferences preferences) {
		TransactionItem transactionItem = transactionItems.get(0);
		TAXCode taxCode = transactionItem.getTaxCode();
		onePerTXNCode = taxCode != null ? taxCode : preferences
				.getDefaultTaxCode();
		for (TransactionItem ti : transactionItems) {
			if (taxCode != null && ti.getTaxCode() != null
					&& ti.getTaxCode() != taxCode) {
				onePerTXNCode = preferences.getDefaultTaxCode();
				break;
			}
		}
	}

	/**
	 * CHECKING DISCOUNT IS ONE PER TRANSACTION THEN ALL TRANSACTIONS ITEMS ARE
	 * SAME OR NOT
	 * 
	 * @param transactionItems
	 */
	private void isAllTransactionItemsDiscountsSame(
			List<TransactionItem> transactionItems) {
		TransactionItem transactionItem = transactionItems.get(0);
		onePerTXNDiscount = transactionItem.getDiscount();
		for (TransactionItem ti : transactionItems) {
			if (!ti.getDiscount().equals(onePerTXNDiscount)) {
				onePerTXNDiscount = 0.0;
				break;
			}
		}
	}

	private String getPropName(TransactionItem referringTransactionItem) {
		Transaction transaction = referringTransactionItem.getTransaction();
		if (!(transaction instanceof Estimate)) {
			return null;
		}
		Estimate estimate = (Estimate) transaction;
		switch (estimate.getEstimateType()) {
		case Estimate.QUOTES:
		case Estimate.CHARGES:
			return "salesQuotationItem";
		case Estimate.BILLABLEEXAPENSES:
			return "billableExpenseItem";
		case Estimate.CREDITS:
			return "creditItem";
		case Estimate.SALES_ORDER:
			return "salesOrderItem";
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param referringTransactionItem
	 * @param propName
	 * @param tItem
	 * @throws JSONException
	 */
	private void createJSON(MigratorContext context, String key,
			TransactionItem referringTransactionItem, String propName,
			JSONObject tItem) throws JSONException {
		Long _newid = context.get(key, referringTransactionItem.getID());
		if (_newid == null) {
			return;
		}
		JSONObject inter = new JSONObject();
		JSONObject outter = new JSONObject();
		outter.put(key, inter);
		inter.put("@_oid", _newid);
		inter.put(
				"@_lid",
				context.getLocalIdProvider().getOrCreate(
						referringTransactionItem));
		tItem.put(propName, outter);
	}

	private String getItemType(Item item) {
		int type = item.getType();
		switch (type) {
		case Item.TYPE_SERVICE:
			return "com.vimukti.accounter.shared.common.ServiceItem";
		case Item.TYPE_NON_INVENTORY_PART:
			return "com.vimukti.accounter.shared.common.ProductItem";
		case Item.TYPE_INVENTORY_PART:
			return "com.vimukti.accounter.shared.inventory.InventoryItem";
		case Item.TYPE_INVENTORY_ASSEMBLY:
			return "com.vimukti.accounter.shared.inventory.InventoryAssembly";
		}
		return null;
	}

	private String getType(T obj) {
		switch (obj.getType()) {
		case Transaction.TYPE_CASH_SALES:
			return "com.vimukti.accounter.shared.customer.CashSaleItem";
		case Transaction.TYPE_CASH_PURCHASE:
			return "com.vimukti.accounter.shared.vendor.CashPurchaseItem";
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			return "com.vimukti.accounter.shared.customer.CreditMemoItem";
		case Transaction.TYPE_ENTER_BILL:
			return "com.vimukti.accounter.shared.vendor.EnterBillItem";
		case Transaction.TYPE_ESTIMATE:
			return getEstimateType(obj);
		case Transaction.TYPE_INVOICE:
			return "com.vimukti.accounter.shared.customer.InvoiceItem";
		case Transaction.TYPE_PAY_BILL:
			return "com.vimukti.accounter.shared.vendor.PayBillItem";
		case Transaction.TYPE_RECEIVE_PAYMENT:
			return "com.vimukti.accounter.shared.customer.ReceivePaymentItem";
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
			return "com.vimukti.accounter.shared.vendor.DebitNoteItem";
		case Transaction.TYPE_WRITE_CHECK:
			return "com.vimukti.accounter.shared.common.WriteCheckAccountItem";
		case Transaction.TYPE_JOURNAL_ENTRY:
			return "com.vimukti.accounter.shared.common.JournalEntryItem";
		case Transaction.TYPE_PAY_TAX:
			return "com.vimukti.accounter.shared.tax.PayTAXItem";
		case Transaction.TYPE_EXPENSE:
			return "";
		case Transaction.TYPE_PAY_EXPENSE:
			return "";
		case Transaction.TYPE_TAX_RETURN:
			return "com.vimukti.accounter.shared.tax.FileTaxItem";
		case Transaction.TYPE_PURCHASE_ORDER:
			return "com.vimukti.accounter.shared.vendor.PurchaseOrderItem";
		case Transaction.TYPE_ITEM_RECEIPT:
			return "";
		case Transaction.TYPE_ADJUST_VAT_RETURN:
			return "";
		case Transaction.TYPE_CASH_EXPENSE:
			return "com.vimukti.accounter.shared.vendor.PurchaseExpenseItem";
		case Transaction.TYPE_CREDIT_CARD_EXPENSE:
			return "com.vimukti.accounter.shared.vendor.PurchaseExpenseItem";
		case Transaction.TYPE_EMPLOYEE_EXPENSE:
			return "";
		case Transaction.TYPE_RECEIVE_TAX:
			return "com.vimukti.accounter.shared.tax.TAXRefundItem";
		case Transaction.TYPE_TDS_CHALLAN:
			return "com.vimukti.accounter.shared.tax.TdsChallanItem";
		case Transaction.TYPE_MAKE_DEPOSIT:
			return "com.vimukti.accounter.shared.bank.MakeDepositItem";
		case Transaction.TYPE_STOCK_ADJUSTMENT:
			return "com.vimukti.accounter.shared.inventory.StockAdjustmentItem";
		case Transaction.TYPE_BUILD_ASSEMBLY:
			return "com.vimukti.accounter.shared.inventory.BuildAssemblyItem";
		case Transaction.TYPE_SALES_ORDER:
			return "com.vimukti.accounter.shared.customer.SalesOrderItem";
		case Transaction.TYPE_STOCK_TRANSFER:
			return "com.vimukti.accounter.shared.inventory.WarehouseTransferItem";
		}
		return "com.vimukti.accounter.shared.common.TransactionItem";
	}

	private String getEstimateType(Transaction transaction) {
		Estimate estimate = (Estimate) transaction;
		switch (estimate.getEstimateType()) {
		case Estimate.QUOTES:
		case Estimate.BILLABLEEXAPENSES:
		case Estimate.CHARGES:
			return "com.vimukti.accounter.shared.customer.SalesQuotationItem";
		case Estimate.CREDITS:
			return "com.vimukti.accounter.shared.customer.TransactionCreditItem";
		case Estimate.DEPOSIT_EXPENSES:
			return "";
		case Estimate.SALES_ORDER:
			return "com.vimukti.accounter.shared.customer.SalesOrderItem";
		}
		return null;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}