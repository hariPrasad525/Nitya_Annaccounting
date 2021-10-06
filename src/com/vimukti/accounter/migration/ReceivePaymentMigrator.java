package com.vimukti.accounter.migration;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.TransactionCreditsAndPayments;
import com.vimukti.accounter.core.TransactionReceivePayment;

public class ReceivePaymentMigrator extends TransactionMigrator<ReceivePayment> {
	@Override
	public JSONObject migrate(ReceivePayment obj, MigratorContext context) throws JSONException {
		if (obj.getNumber().equals("76")) {
			return null;
		}
		JSONObject receivePayment = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.customer.ReceivePayment", receivePayment);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		receivePayment.put("@id", _lid);
		receivePayment.put("@_lid", _lid);
		{
			Customer customer = obj.getCustomer();
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.customer.Customer", inter);
			String localId = context.getLocalIdProvider().getOrCreate(customer);
			inter.put("@_oid", context.get("Customer", customer.getID()));
			inter.put("@_lid", localId);
			receivePayment.put("payee", outter);
		}
		// Deposit In Account
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(depositIn);
			inter.put("@_oid", context.get("Account", depositIn.getID()));
			inter.put("@_lid", localId);
			receivePayment.put("paymentAccount", outter);
		}
		// Amount Received
		receivePayment.put("amountReceived", BigDecimal.valueOf(obj.getAmount()));
		// TDS Amount
		receivePayment.put("tDSAmount", obj.getTdsTotal());

		List<TransactionReceivePayment> transactionReceivePayment = obj.getTransactionReceivePayment();
		{
			JSONObject items = new JSONObject();
			JSONArray receivePaymentItems = new JSONArray();
			items.put("com.vimukti.accounter.shared.customer.ReceivePaymentItem", receivePaymentItems);
			for (TransactionReceivePayment item : transactionReceivePayment) {
				JSONObject receivePaymentItem = new JSONObject();
				String lid = context.getLocalIdProvider().getOrCreate(item);
				receivePaymentItem.put("@id", lid);
				receivePaymentItem.put("@_lid", lid);
				if (item.getInvoice() == null) {
					receivePayment = null;
					return null;
				}
				{
					Invoice invoice = item.getInvoice();
					JSONObject currencyJson = new JSONObject();
					JSONObject json = new JSONObject();
					currencyJson.put("com.vimukti.accounter.shared.customer.Invoice", json);
					json.put("@_oid", context.get("Invoice", invoice.getID()));
					json.put("@_lid", context.getLocalIdProvider().getOrCreate(invoice));
					receivePaymentItem.put("invoice", currencyJson);
				}
				ReceivePayment payment = item.getReceivePayment();
				if (payment != null) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put("com.vimukti.accounter.shared.customer.ReceivePayment", inter);
					inter.put("@id", _lid);
					inter.put("@_lid", _lid);
					receivePaymentItem.put("receivePayment", outter);
				}
				Account account = item.getDiscountAccount();
				if (account != null) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put("com.vimukti.accounter.shared.common.Account", inter);
					String localId = context.getLocalIdProvider().getOrCreate(depositIn);
					inter.put("@_oid", context.get("Account", depositIn.getID()));
					inter.put("@_lid", localId);
					receivePaymentItem.put("discountAccount", outter);
					receivePaymentItem.put("discountAmount", BigDecimal.valueOf(item.getCashDiscount()));
				}

				Account writeOffAccount = item.getWriteOffAccount();
				if (writeOffAccount != null) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put("com.vimukti.accounter.shared.common.Account", inter);
					String localId = context.getLocalIdProvider().getOrCreate(writeOffAccount);
					inter.put("@_oid", context.get("Account", writeOffAccount.getID()));
					inter.put("@_lid", localId);
					receivePaymentItem.put("writeoffAccount", outter);
					receivePaymentItem.put("writeoffAmount", BigDecimal.valueOf(item.getWriteOff()));

				}
				// Apply Credits
				{
					if (!item.getTransactionCreditsAndPayments().isEmpty()) {
						JSONObject applyCredit = new JSONObject();
						String applyCreditLID = context.getLocalIdProvider().getOrCreate();
						applyCredit.put("@id", applyCreditLID);
						applyCredit.put("@_lid", applyCreditLID);
						JSONObject acOut = new JSONObject();
						acOut.put("com.vimukti.accounter.shared.customer.ApplyCredit", applyCredit);
						JSONArray applyCreditItems = new JSONArray();
						JSONObject acItems = new JSONObject();
						acItems.put("com.vimukti.accounter.shared.customer.ApplyCreditItem", applyCreditItems);
						for (TransactionCreditsAndPayments ac : item.getTransactionCreditsAndPayments()) {
							JSONObject applyCreditItem = new JSONObject();
							String localId = context.getLocalIdProvider().getOrCreate(ac);
							applyCreditItem.put("@id", localId);
							applyCreditItem.put("@_lid", localId);
							{
								JSONObject inter = new JSONObject();
								JSONObject outter = new JSONObject();
								outter.put("com.vimukti.accounter.shared.customer.ApplyCredit", inter);
								inter.put("@id", applyCreditLID);
								inter.put("@_lid", applyCreditLID);
								applyCreditItem.put("applyCredit", outter);
							}
							{
								JSONObject inter = new JSONObject();
								JSONObject outter = new JSONObject();
								outter.put("com.vimukti.accounter.shared.customer.Credit", inter);
								inter.put("@_oid", context.get("CustomerCredit", ac.getCreditsAndPayments().getID()));
								inter.put("@_lid",
										context.getLocalIdProvider().getOrCreate(ac.getCreditsAndPayments()));
								applyCreditItem.put("credit", outter);
							}
							applyCreditItem.put("amountToUse", BigDecimal.valueOf(ac.getAmountToUse()));
							applyCreditItems.put(applyCreditItem);
						}
						applyCredit.put("creditItems", acItems);
						receivePaymentItem.put("applyCredits", acOut);
					}
				}
				receivePaymentItem.put("payment", BigDecimal.valueOf(item.getPayment()));
				receivePaymentItems.put(receivePaymentItem);
			}
			receivePayment.put("paymentItems", items);
		}
		// PaymentableTransaction
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			receivePayment.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			receivePayment.put("paymentMethod", "CASH");
		}
		try {
			if (!obj.getCheckNumber().isEmpty()) {
				Integer chequeNumber = Integer.valueOf(obj.getCheckNumber());
				receivePayment.put("chequeNumber", chequeNumber);
			}
		} catch (Exception e) {
		}
		return external;
	}
}
