package com.nitya.accounter.migration;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.CustomerPrePayment;

public class CustomerPrepaymentMigrator extends TransactionMigrator<CustomerPrePayment> {
	@Override
	public JSONObject migrate(CustomerPrePayment obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.customer.CustomerPrepayment", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		context.putChilderName("CustomerPrepayment", "credit");
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		// customer
		Customer customer = obj.getCustomer();
		if (customer != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.customer.Customer", inter);
			String localId = context.getLocalIdProvider().getOrCreate(customer);
			inter.put("@_oid", context.get("Customer", customer.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);
		}
		// address
		Address billingAddress = obj.getAddress();
		if (billingAddress != null) {
			JSONObject address = new JSONObject();
			JSONObject jsonAddress = new JSONObject();
			address.put("org.ecgine.core.shared.Address", jsonAddress);
			String localId = context.getLocalIdProvider().getOrCreate(billingAddress);
			jsonAddress.put("@_lid", localId);
			jsonAddress.put("@id", localId);
			jsonAddress.put("street", billingAddress.getStreet());
			jsonAddress.put("city", billingAddress.getCity());
			jsonAddress.put("stateOrProvince", billingAddress.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", billingAddress.getZipOrPostalCode());
			jsonAddress.put("country", billingAddress.getCountryOrRegion());
			jsonObject.put("address", address);
		}
		// Account
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(depositIn);
			inter.put("@_oid", context.get("Account", depositIn.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("depositIn", outter);
		}
		// Amount
		jsonObject.put("amount", BigDecimal.valueOf(obj.getTotal()));

		// paymentMethod
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		try {
			String checkNumber = obj.getCheckNumber();
			if (checkNumber != null) {
				jsonObject.put("chequeNumber", Long.valueOf(checkNumber));
			}
		} catch (Exception e) {
			// Nothing to do
		}
		return external;
	}
}