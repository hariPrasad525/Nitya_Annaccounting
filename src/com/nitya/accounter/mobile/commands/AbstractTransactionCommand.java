package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.nitya.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientPayBill;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.server.FinanceTool;

public abstract class AbstractTransactionCommand extends AbstractCommand {
	protected static final String CUSTOMER = "customer";
	protected static final String ITEMS = "items";
	protected static final String DATE = "date";
	protected static final String NUMBER = "number";
	protected static final String CONTACT = "contact";
	protected static final String BILL_TO = "billto";
	protected static final String MEMO = "memo";
	protected static final String TAXCODE = "taxCode";
	protected static final String VENDOR = "vendor";
	protected static final String PHONE = "phone";
	protected static final String ACCOUNTS = "acounts";
	protected static final String PAYMENT_TERMS = "paymentTerms";
	protected static final String DUE_DATE = "duedate";
	protected static final String DELIVERY_DATE = "deliveryDate";
	protected static final String CHEQUE_NO = "chequeNo";
	protected static final String PAYMENT_METHOD = "paymentMethod";
	protected static final String CURRENCY = "currency";
	protected static final String CURRENCY_FACTOR = "currencyFactor";
	protected static final String PAY_FROM = "payFrom";
	protected static final String ORDER_NO = "orderNo";
	protected static final String ESTIMATEANDSALESORDER = "estimateAndSalesOrder";
	protected static final String ESTIMATE = "estimate";
	protected static final String DISPATCH_DATE = "dispathDate";
	protected static final String RECIEVED_DATE = "deliveryDate";
	protected static final String AMOUNT = "amount";
	protected static final String TO_BE_PRINTED = "toBePrinted";
	protected static final String FILTER_BY_DUE_ON_BEFORE = "filterByDueOnBefore";
	protected static final String BILLS_DUE = "BillsDue";
	protected static final String IS_VAT_INCLUSIVE = "isVatInclusive";
	protected static final String SALES_PERSON = "sales_person";
	protected static final String SHIPPING_TERM = "shippingterm";
	protected static final String SHIPPING_METHOD = "shippingmethod";
	protected static final String DISCOUNT = "transactiondiscount";

	private ClientTransaction transaction;

	public double updateTotals(Context context, ClientTransaction transaction,
			boolean isSales) {
		List<ClientTransactionItem> allrecords = transaction
				.getTransactionItems();
		Boolean isVatInclusive = (Boolean) (get(IS_VAT_INCLUSIVE) != null ? get(
				IS_VAT_INCLUSIVE).getValue()
				: false);
		setAmountIncludeTAX(transaction, isVatInclusive);
		double[] result = getTransactionTotal(isAmountIncludeTAX(transaction),
				allrecords, isSales);
		double grandTotal = result[0] + result[1];
		transaction.setTotal(grandTotal);
		transaction.setNetAmount(result[0]);
		return result[1];
	}

	/**
	 * 
	 * @param transactionItems
	 * @param context
	 * @return
	 */
	protected TAXCode getTaxCodeForTransactionItems(
			List<ClientTransactionItem> transactionItems, Context context) {
		TAXCode taxCode = null;

		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getTaxCode() != 0) {
				taxCode = CommandUtils.getTaxCode(
						clientTransactionItem.getTaxCode(),
						context.getCompany());

				if (taxCode != null)
					break;
				else
					continue;
			}
		}
		return taxCode;
	}

	protected Double getDiscountFromTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		Double discount = null;
		for (ClientTransactionItem item : transactionItems) {
			if (item.getDiscount() != 0) {
				discount = item.getDiscount();
				if (discount != null)
					break;
				else
					continue;
			}
		}
		return discount;
	}

	public double[] getTransactionTotal(boolean isAmountsIncludeVAT,
			List<ClientTransactionItem> allrecords, boolean isSales) {
		double lineTotal = 0.0;
		double totalTax = 0.0;
		double totaldiscount = 0;

		for (ClientTransactionItem record : allrecords) {

			int type = record.getType();

			if (type == 0) {
				continue;
			}
			if (record.getDiscount() != null) {
				totaldiscount += record.getDiscount();
			}

			Double lineTotalAmt = record.getLineTotal();
			lineTotal += lineTotalAmt;

			if (record != null && record.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// citem.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				// taxableLineTotal += lineTotalAmt;

				double taxAmount = getVATAmount(isAmountsIncludeVAT,
						record.getTaxCode(), record, isSales);
				if (isAmountsIncludeVAT) {
					lineTotal -= taxAmount;
				}
				record.setVATfraction(taxAmount);
				totalTax += record.getVATfraction();

			}

			// super.update(record);
			// totalVat += citem.getVATfraction();
		}

		double[] result = new double[3];
		result[0] = lineTotal;
		result[1] = totalTax;
		result[2] = totaldiscount;
		return result;
	}

	public double getVATAmount(boolean isAmountsIncludeVAT, long TAXCodeID,
			ClientTransactionItem record, boolean isSales) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				ClientTAXCode taxCode = (ClientTAXCode) CommandUtils
						.getClientObjectById(TAXCodeID,
								AccounterCoreType.TAX_CODE, getCompanyId());

				if (!taxCode.getName().equals("EGS")
						&& !taxCode.getName().equals("EGZ")
						&& !taxCode.getName().equals("RC")) {
					ClientTAXItemGroup vatItemGroup = (ClientTAXItem) CommandUtils
							.getClientObjectById(
									isSales ? taxCode.getTAXItemGrpForSales()
											: taxCode
													.getTAXItemGrpForPurchases(),
									AccounterCoreType.TAXITEM, getCompanyId());
					if (vatItemGroup != null) {
						vatRate = ((ClientTAXItem) vatItemGroup).getTaxRate();
					}
					if (vatItemGroup == null) {
						vatItemGroup = (ClientTAXGroup) CommandUtils
								.getClientObjectById(
										isSales ? taxCode
												.getTAXItemGrpForSales()
												: taxCode
														.getTAXItemGrpForPurchases(),
										AccounterCoreType.TAX_GROUP,
										getCompanyId());
						if (vatItemGroup != null) {
							vatRate = ((ClientTAXGroup) vatItemGroup)
									.getGroupRate();
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (isAmountsIncludeVAT) {
			vat = ((ClientTransactionItem) record).getLineTotal()
					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
		} else {
			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
					/ 100;
		}
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long companyId, long customerId, long paymentDate)
			throws AccounterException {
		List<ReceivePaymentTransactionList> receivePaymentTransactionList = null;
		receivePaymentTransactionList = new FinanceTool()
				.getTransactionReceivePayments(customerId, paymentDate,
						companyId);
		return new ArrayList<ReceivePaymentTransactionList>(
				receivePaymentTransactionList);
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		// TODO
		Boolean isVatInclusive = false;
		if (get(IS_VAT_INCLUSIVE) != null) {
			isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		}

		boolean isSales = false;
		List<ClientTransactionItem> allrecords = new ArrayList<ClientTransactionItem>();
		if (get(ITEMS) != null) {
			TransactionItemTableRequirement req = (TransactionItemTableRequirement) get(ITEMS);
			List<ClientTransactionItem> itemReqValue = req.getValue();
			for (ClientTransactionItem clientTransactionItem : itemReqValue) {
				clientTransactionItem.setAmountIncludeTAX(isVatInclusive);
			}
			req.setValue(itemReqValue);
			isSales = req.isSales();
			allrecords.addAll(itemReqValue);
		}
		if (get(ACCOUNTS) != null) {
			TransactionAccountTableRequirement req = (TransactionAccountTableRequirement) get(ACCOUNTS);
			List<ClientTransactionItem> accountReqValue = req.getValue();
			for (ClientTransactionItem clientTransactionItem : accountReqValue) {
				clientTransactionItem.setAmountIncludeTAX(isVatInclusive);
			}
			req.setValue(accountReqValue);
			isSales = req.isSales();
			allrecords.addAll(accountReqValue);
		}
		if (allrecords.isEmpty()) {
			return;
		}
		ClientCompanyPreferences preferences = context.getPreferences();
		if (!allrecords.isEmpty() && preferences.isTrackTax()
				&& !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = (TAXCode) (get(TAXCODE) == null ? null : get(
					TAXCODE).getValue());
			if (taxCode != null) {
				for (ClientTransactionItem item : allrecords) {
					item.setTaxCode(taxCode.getID());
				}
			}
		}

		double[] result = getTransactionTotal(isVatInclusive, allrecords,
				isSales);

		if (context.getPreferences().isTrackTax()
				&& (isSales ? true : context.getPreferences().isTrackPaidTax())) {
			makeResult.add("Total Tax: " + result[1]);
		}

		Currency currency = getCurrency();
		String formalName = getPreferences().getPrimaryCurrency()
				.getFormalName();
		if (!currency.getFormalName().equalsIgnoreCase(formalName))
			makeResult.add("Total"
					+ "("
					+ formalName
					+ ")"
					+ ": "
					+ (result[0] * getCurrencyFactor() + result[1]
							* getCurrencyFactor()));
		makeResult.add("Total" + "(" + currency.getFormalName() + ")" + ": "
				+ (result[0] + result[1]));
	}

	protected void setTransaction(ClientTransaction transction) {
		this.transaction = transction;
	}

	@Override
	protected String getDeleteCommand(Context context) {
		if (this.transaction != null
				&& this.transaction.getID() != 0
				&& context.getUser().getPermissions().getTypeOfInvoicesBills() == RolePermissions.TYPE_YES) {
			int type = transaction.getType();
			int estimateType=0;
			if (transaction.getType() == Transaction.TYPE_PAY_BILL) {
				ClientPayBill payBill = (ClientPayBill) transaction;
				type = payBill.getPayBillType() == ClientPayBill.TYPE_PAYBILL ? ClientTransaction.TYPE_PAY_BILL
						: ClientTransaction.TYPE_VENDOR_PAYMENT;
			}else if(transaction.getType() == Transaction.TYPE_ESTIMATE)
			{
				ClientEstimate clientEstimate = (ClientEstimate)transaction;
				estimateType = clientEstimate.getEstimateType();
			}
			return "deleteTransaction " + type + " " +estimateType+ " " + transaction.getID();
		}
		return null;
	}

	@Override
	protected String getThirdCommandString() {
		return getMessages().void1();
	}

	@Override
	protected String getThirdCommand(Context context) {
		if (this.transaction != null
				&& this.transaction.getID() != 0
				&& !this.transaction.isVoid()
				&& context.getUser().getPermissions().getTypeOfInvoicesBills() == RolePermissions.TYPE_YES) {
			int type = transaction.getType();
			if (transaction.getType() == Transaction.TYPE_PAY_BILL) {
				ClientPayBill payBill = (ClientPayBill) transaction;
				type = payBill.getPayBillType() == ClientPayBill.TYPE_PAYBILL ? ClientTransaction.TYPE_PAY_BILL
						: ClientTransaction.TYPE_VENDOR_PAYMENT;
			}
			return "voidTransaction " + type + " " + transaction.getID();
		}
		return null;
	}

	protected boolean checkOpen(Collection<ClientTransactionItem> items,
			int type, boolean defaultValue) {
		if (items.isEmpty()) {
			return defaultValue;
		}
		for (ClientTransactionItem item : items) {
			if (item.getType() == type) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> T getTransaction(String string, AccounterCoreType type,
			Context context) {
		long numberFromString = getNumberFromString(string);
		T transactionByNum;
		if (numberFromString != 0) {
			string = String.valueOf(numberFromString);
			transactionByNum = (T) CommandUtils.getClientTransactionByNumber(
					context.getCompany(), string, type);
		} else {
			transactionByNum = (T) CommandUtils.getClientObjectById(
					Long.parseLong(string), type, getCompanyId());
		}
		return transactionByNum;
	}

	protected double getCurrencyFactor() {
		Requirement requirement = get(CURRENCY_FACTOR);
		if (requirement != null) {
			return requirement.getValue();
		}
		return 1.0;
	}

	// protected abstract Payee getPayee();

	protected abstract Currency getCurrency();

	protected boolean isAmountIncludeTAX(ClientTransaction transaction) {
		if (transaction == null || transaction.getTransactionItems() == null) {
			return false;
		}
		for (ClientTransactionItem item : transaction.getTransactionItems()) {
			if (item.getReferringTransactionItem() == 0) {
				return item.isAmountIncludeTAX();
			}
		}
		return false;
	}

	protected void setAmountIncludeTAX(ClientTransaction transaction,
			boolean isAmountIncludeTAX) {
		for (ClientTransactionItem item : transaction.getTransactionItems()) {
			item.setAmountIncludeTAX(isAmountIncludeTAX);
		}
	}

	public boolean isTrackDiscounts() {
		if (transaction != null && transaction.haveDiscount()) {
			return true;
		} else {
			return getPreferences().isTrackDiscounts();
		}
	}

	public boolean isDiscountPerDetailLine() {
		if (transaction != null && transaction.usesDifferentDiscounts()) {
			return true;
		} else {
			return getPreferences().isDiscountPerDetailLine();
		}
	}

}
