package com.vimukti.accounter.migration;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TransactionCreditsAndPayments;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.Vendor;

public class PayBillMigrator extends TransactionMigrator<PayBill> {
	@Override
	public JSONObject migrate(PayBill obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);

		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);

		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.vendor.PayBill", jsonObject);

		List<TransactionPayBill> transactionPayBill = obj.getTransactionPayBill();
		JSONArray payBillItems = new JSONArray();
		JSONObject items = new JSONObject();
		items.put("com.vimukti.accounter.shared.vendor.PayBillItem", payBillItems);
		for (TransactionPayBill tBill : transactionPayBill) {

			JSONObject payBillItem = new JSONObject();
			String piLocalId = context.getLocalIdProvider().getOrCreate(tBill);
			payBillItem.put("@id", piLocalId);
			payBillItem.put("@_lid", piLocalId);
			EnterBill enterBill = tBill.getEnterBill();
			if (enterBill == null) {
				return null;
			}
			payBillItem.put("bill", context.get("EnterBill", enterBill.getID()));
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.vendor.EnterBill", inter);
			String localId = context.getLocalIdProvider().getOrCreate(enterBill);
			inter.put("@_oid", context.get("EnterBill", enterBill.getID()));
			inter.put("@_lid", localId);
			payBillItem.put("bill", outter);
			PayBill payBill = tBill.getPayBill();
			if (payBill != null) {
				JSONObject payBillInter = new JSONObject();
				JSONObject payBillOutter = new JSONObject();
				payBillOutter.put("com.vimukti.accounter.shared.vendor.PayBill", payBillInter);
				payBillInter.put("@id", _lid);
				payBillInter.put("@_lid", _lid);
				payBillItem.put("payBill", payBillOutter);
			}
			payBillItem.put("payment", BigDecimal.valueOf(tBill.getPayment()));
			// discountAccount
			Account discountAccount = tBill.getDiscountAccount();
			if (discountAccount != null) {
				JSONObject discountAccountInner = new JSONObject();
				JSONObject discountAccountOutter = new JSONObject();
				discountAccountOutter.put("com.vimukti.accounter.shared.common.Account", discountAccountInner);
				String discountAccountLid = context.getLocalIdProvider().getOrCreate(discountAccount);
				discountAccountInner.put("@_oid", context.get("Account", discountAccount.getID()));
				discountAccountInner.put("@_lid", discountAccountLid);
				payBillItem.put("discountAccount", discountAccountOutter);
				payBillItem.put("discountAmount", BigDecimal.valueOf(tBill.getCashDiscount()));
			}
			// Apply Credits
			List<TransactionCreditsAndPayments> transactionCreditsAndPayments = tBill
					.getTransactionCreditsAndPayments();
			if (!transactionCreditsAndPayments.isEmpty()) {
				JSONObject applyDebit = new JSONObject();
				String applyDebitLID = context.getLocalIdProvider().getOrCreate();
				applyDebit.put("@id", applyDebitLID);
				applyDebit.put("@_lid", applyDebitLID);
				JSONObject acOut = new JSONObject();
				acOut.put("com.vimukti.accounter.shared.vendor.ApplyDebit", applyDebit);
				JSONArray applyDebitItems = new JSONArray();
				JSONObject acItems = new JSONObject();
				acItems.put("com.vimukti.accounter.shared.vendor.ApplyDebitItem", applyDebitItems);
				for (TransactionCreditsAndPayments ac : transactionCreditsAndPayments) {
					JSONObject applyDebitItemJSON = new JSONObject();
					String applyDebitItem = context.getLocalIdProvider().getOrCreate(ac);
					applyDebitItemJSON.put("@id", applyDebitItem);
					applyDebitItemJSON.put("@_lid", applyDebitItem);
					{
						JSONObject applyDebitInter = new JSONObject();
						JSONObject applyDebitOutter = new JSONObject();
						applyDebitOutter.put("com.vimukti.accounter.shared.vendor.ApplyDebit", applyDebitInter);
						applyDebitInter.put("@id", applyDebitLID);
						applyDebitInter.put("@_lid", applyDebitLID);
						applyDebitItemJSON.put("applyDebit", applyDebitOutter);
					}
					{
						JSONObject debitInter = new JSONObject();
						JSONObject debitOutter = new JSONObject();
						debitOutter.put("com.vimukti.accounter.shared.vendor.VendorDebit", debitInter);
						debitInter.put("@_oid", context.get("VendorDebit", ac.getCreditsAndPayments().getID()));
						debitInter.put("@_lid", context.getLocalIdProvider().getOrCreate(ac.getCreditsAndPayments()));
						applyDebitItemJSON.put("debitNote", debitOutter);
					}
					applyDebitItemJSON.put("amountToUse", BigDecimal.valueOf(ac.getAmountToUse()));
					applyDebitItems.put(applyDebitItemJSON);
				}
				applyDebit.put("debitItems", acItems);
				payBillItem.put("applyDebits", acOut);
			}
			payBillItems.put(payBillItem);
		}
		jsonObject.put("payBillItems", items);
		TAXItem tdsTaxItem = obj.getTdsTaxItem();
		if (tdsTaxItem != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.tax.TAXITem", inter);
			String localId = context.getLocalIdProvider().getOrCreate(tdsTaxItem);
			inter.put("@_oid", context.get("Tax", tdsTaxItem.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("tDS", outter);
		}
		jsonObject.put("tDSAmount", BigDecimal.valueOf(obj.getTdsTotal()));
		jsonObject.put("filterByBillDueOnOrBefore", obj.getBillDueOnOrBefore().toEpochDay());
		Vendor vendor = obj.getVendor();
		if (vendor != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.vendor.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(vendor);
			inter.put("@_oid", context.get("Vendor", vendor.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);
		}
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(payFrom);
			inter.put("@_oid", context.get("Account", payFrom.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("paymentAccount", outter);
		}
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		jsonObject.put("toBePrinted", obj.isToBePrinted());
		Long chequeNumber = null;
		try {
			if (!obj.getCheckNumber().isEmpty()) {
				chequeNumber = Long.valueOf(obj.getCheckNumber());
				jsonObject.put("chequeNumber", chequeNumber);
			}
		} catch (Exception e) {
		}
		return external;
	}
}
