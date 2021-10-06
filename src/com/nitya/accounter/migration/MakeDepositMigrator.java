package com.nitya.accounter.migration;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AccounterClass;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Job;
import com.nitya.accounter.core.MakeDeposit;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.core.TransactionDepositItem;

public class MakeDepositMigrator extends TransactionMigrator<MakeDeposit> {
	@Override
	public JSONObject migrate(MakeDeposit obj, MigratorContext context) throws JSONException {
		// super Calling
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.bank.MakeDeposit", jsonObject);
		// deposit To
		Account depositTo = obj.getDepositTo();
		if (depositTo != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.bank.BankAccount", inter);
			String localId = context.getLocalIdProvider().getOrCreate(depositTo);
			inter.put("@_oid", context.get("Account", depositTo.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("depositTo", outter);
		}
		List<TransactionDepositItem> transactionDepositItems = obj.getTransactionDepositItems();
		JSONArray array = new JSONArray();
		JSONObject items = new JSONObject();
		items.put("com.nitya.accounter.shared.bank.MakeDepositItem", array);
		for (TransactionDepositItem item : transactionDepositItems) {
			JSONObject depositItemJson = new JSONObject();
			String unitlid = context.getLocalIdProvider().getOrCreate(item);
			depositItemJson.put("@id", unitlid);
			depositItemJson.put("@_lid", unitlid);
			Payee receivedFrom = item.getReceivedFrom();
			if (receivedFrom != null) {
				if (receivedFrom.getType() == Payee.TYPE_CUSTOMER) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put("com.nitya.accounter.shared.customer.Customer", inter);
					inter.put("@_oid", context.get("Customer", receivedFrom.getID()));
					inter.put("@_lid", context.getLocalIdProvider().getOrCreate(receivedFrom));
					depositItemJson.put("payee", outter);
				}
				if (receivedFrom.getType() == Payee.TYPE_VENDOR) {
					JSONObject inter = new JSONObject();
					JSONObject outter = new JSONObject();
					outter.put("com.nitya.accounter.shared.vendor.Vendor", inter);
					inter.put("@_oid", context.get("Venodor", receivedFrom.getID()));
					inter.put("@_lid", context.getLocalIdProvider().getOrCreate(receivedFrom));
					depositItemJson.put("payee", outter);
				}
			}
			Account account = item.getAccount();
			if (account != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.common.Account", inter);
				inter.put("@_oid", context.get("Account", account.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(account));
				depositItemJson.put("depositFrom", outter);
			}
			depositItemJson.put("description", item.getDescription());
			depositItemJson.put("amount", BigDecimal.valueOf(item.getTotal()));
			AccounterClass accounterClass = item.getAccounterClass();
			if (accounterClass != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.common.AccountClass", inter);
				inter.put("@_oid", context.get("AccounterClass", accounterClass.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(accounterClass));
				depositItemJson.put("accountClass", outter);
			}
			Customer customer = item.getCustomer();
			if (customer != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.customer.Customer", inter);
				inter.put("@_oid", context.get("Customer", customer.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(customer));
				depositItemJson.put("customer", outter);
			}
			Job job = item.getJob();
			if (job != null) {
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.customer.Project", inter);
				inter.put("@_oid", context.get("Job", job.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(job));
				depositItemJson.put("project", outter);
			}
			array.put(depositItemJson);
		}
		jsonObject.put("depositItems", items);
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		return external;
	}
}
