package com.nitya.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientCashSales;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientEnterBill;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientInvoice;
import com.nitya.accounter.web.client.core.ClientPurchaseOrder;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.FormItem;

public class TaxItemsForm extends DynamicForm {

	Map<ClientTAXItem, List<Double>> codeValues;
	ClientTransaction transaction;
	double totalTax;

	public TaxItemsForm() {
		super("taxItemsForm");
	}

	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
		updateForm();
	}

	private void updateForm() {
		ClientCompany company = Accounter.getCompany();

		this.clear();
		codeValues = new HashMap<ClientTAXItem, List<Double>>();
		int category = getTransactionCategory(transaction.getObjectType());
		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : transaction
				.getTransactionItems()) {
			if (clientTransactionItem.getLineTotal() != null
					&& clientTransactionItem.getVATfraction() != null) {
				transactionItems.add(clientTransactionItem);
			}
		}

		for (ClientTransactionItem transactionItem : transactionItems) {
			if (!transactionItem.isTaxable()) {
				continue;
			}
			ClientTAXCode taxCode = company.getTAXCode(transactionItem
					.getTaxCode());
			if (taxCode == null) {
				continue;
			}

			long taxItemGroupId = 0;
			if (category == ClientTransaction.CATEGORY_CUSTOMER) {
				taxItemGroupId = taxCode.getTAXItemGrpForSales();
			} else if (category == ClientTransaction.CATEGORY_VENDOR
					|| category == ClientTransaction.CATEGORY_BANKING) {
				taxItemGroupId = taxCode.getTAXItemGrpForPurchases();
			}
			ClientTAXItemGroup taxItemGroup = company
					.getTAXItemGroup(taxItemGroupId);
			if (taxItemGroup == null) {
				continue;
			}
			List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();

			if (taxItemGroup instanceof ClientTAXItem) {
				ClientTAXItem taxItem = (ClientTAXItem) taxItemGroup;
				taxItems.add(taxItem);

			} else if (taxItemGroup instanceof ClientTAXGroup) {
				taxItems = ((ClientTAXGroup) taxItemGroup).getTaxItems();
			}

			double value = transactionItem.getLineTotal() != null ? transactionItem
					.getLineTotal() : 0;
			if (transactionItem.isAmountIncludeTAX()) {
				value = transactionItem.getLineTotal()
						- transactionItem.getVATfraction();
			}

			for (ClientTAXItem clientTAXItem : taxItems) {
				double netAmt = value;
				double taxRate = clientTAXItem.getTaxRate();
				double taxAmount = netAmt * taxRate / 100;

				if (codeValues.containsKey(clientTAXItem)) {
					netAmt += codeValues.get(clientTAXItem).get(0);
					taxAmount += codeValues.get(clientTAXItem).get(1);
				}
				if (clientTAXItem != null && taxRate != 0 && value != 0) {
					ArrayList<Double> arrayList = new ArrayList<Double>();
					arrayList.add(netAmt);
					arrayList.add(taxAmount);
					codeValues.put(clientTAXItem, arrayList);
				}
			}

		}

		FormItem[] items = new FormItem[codeValues.size()];
		totalTax = 0;
		int i = 0;

		for (Entry<ClientTAXItem, List<Double>> entry : codeValues.entrySet()) {
			ClientTAXItem taxCode = entry.getKey();
			Double value = entry.getValue().get(0);

			if (taxCode != null) {
				double taxRate = taxCode.getTaxRate();

				double taxAmount = entry.getValue().get(1);
				totalTax += taxAmount;

				if (taxRate != 0) {
					ClientCurrency currency = Accounter.getCompany()
							.getCurrency(transaction.getCurrency());

					String amount = currency == null ? DataUtils
							.getAmountAsStringInPrimaryCurrency(value)
							: DataUtils.getAmountAsStringInCurrency(value,
									currency.getSymbol());

					AmountLabel amountLabel = new AmountLabel(Global
							.get()
							.messages()
							.taxAtOnValue(
									DataUtils.getAmountAsStrings(taxRate),
									amount));
					amountLabel.setAmount(taxAmount);
					amountLabel.setCurrency(currency == null ? Accounter
							.getCompany().getPrimaryCurrency() : currency);
					items[i] = amountLabel;
					i++;
				}
			}

		}

		this.add(items);
	}

	public double getTotalTax() {
		return totalTax;
	}

	public static int getTransactionCategory(AccounterCoreType accounterCoreType) {
		switch (accounterCoreType) {
		case CASHSALES:
		case CUSTOMERCREDITMEMO:
		case CUSTOMERREFUND:
		case ESTIMATE:
		case INVOICE:
		case RECEIVEPAYMENT:
		case SALESORDER:
		case CUSTOMERPREPAYMENT:
			return ClientTransaction.CATEGORY_CUSTOMER;

		case CASHPURCHASE:
		case CREDITCARDCHARGE:
		case ENTERBILL:
		case ISSUEPAYMENT:
		case PAYBILL:
		case VENDORCREDITMEMO:
		case PURCHASEORDER:
		case ITEMRECEIPT:
		case EXPENSE:
			return ClientTransaction.CATEGORY_VENDOR;

		case MAKEDEPOSIT:
		case TRANSFERFUND:
		case WRITECHECK:
			return ClientTransaction.CATEGORY_BANKING;

		default:
			break;
		}
		return 0;
	}

}
