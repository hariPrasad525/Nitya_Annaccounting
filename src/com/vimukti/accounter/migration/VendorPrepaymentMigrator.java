package com.vimukti.accounter.migration;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorPrePayment;

public class VendorPrepaymentMigrator extends TransactionMigrator<VendorPrePayment> {
	@Override
	public JSONObject migrate(VendorPrePayment obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		JSONObject external = new JSONObject();
		external.put("com.vimukti.accounter.shared.vendor.VendorPrepayment", jsonObject);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		context.putChilderName("VendorPrepayment", "debit");
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
		// payFrom
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.common.Account", inter);
			String localId = context.getLocalIdProvider().getOrCreate(payFrom);
			inter.put("@_oid", context.get("Account", payFrom.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payFrom", outter);
		}
		// amount
		jsonObject.put("amount", BigDecimal.valueOf(obj.getTotal()));
		// payTo
		Vendor vendor = obj.getVendor();
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.vimukti.accounter.shared.venodr.Vendor", inter);
			String localId = context.getLocalIdProvider().getOrCreate(vendor);
			inter.put("@_oid", context.get("Vendor", vendor.getID()));
			inter.put("@_lid", localId);
			jsonObject.put("payee", outter);
		}
		// Payment Method
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		Long chequeNumber = null;
		try {
			chequeNumber = Long.valueOf(obj.getCheckNumber());
			jsonObject.put("chequeNumber", chequeNumber);
		} catch (Exception e) {
		}
		return external;
	}
}
