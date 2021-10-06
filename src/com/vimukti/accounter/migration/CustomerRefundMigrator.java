package com.vimukti.accounter.migration;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;

public class CustomerRefundMigrator extends TransactionMigrator<CustomerRefund> {
	@Override
	public JSONObject migrate(CustomerRefund obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.customer.CustomerRefund", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		Address addr = obj.getAddress();
		if (addr != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String localId = context.getLocalIdProvider().getOrCreate(addr);
			jsonAddress.put("@_lid", localId);
			jsonAddress.put("@id", localId);
			jsonAddress.put("street", addr.getStreet());
			jsonAddress.put("city", addr.getCity());
			jsonAddress.put("stateOrProvince", addr.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", addr.getZipOrPostalCode());
			jsonAddress.put("country", addr.getCountryOrRegion());
			jsonObject.put("address", address);
		}
		jsonObject.put("amount", BigDecimal.valueOf(obj.getTotal()));
		// paymentMethod
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		// Check Number
		Long checkNumber = 0L;
		try {
			checkNumber = Long.parseLong(obj.getCheckNumber());
		} catch (Exception e) {
			// Nothing to do
		}
		jsonObject.put("chequeNumber", checkNumber);
		// PayTo
		Customer payTo = obj.getPayTo();
		if (payTo != null) {
			jsonObject.put("payee", context.get("Customer", payTo.getID()));
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.customer.Customer", inter);
			String localId = context.getLocalIdProvider().getOrCreate(payTo);
			inter.put("@_oid", context.get("Customer", payTo.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);
		}
		// Account
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			jsonObject.put("paymentAccount", context.get("Account", payFrom.getID()));
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(payFrom);
			inter.put("@_oid", context.get("Account", payFrom.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("paymentAccount", outter);
		}
		jsonObject.put("toBePrinted", obj.getIsToBePrinted());
		jsonObject.put("paymentStatus", PicklistUtilMigrator.getPaymentStatusIdentifier(obj.getStatus()));
		return external;
	}
}
