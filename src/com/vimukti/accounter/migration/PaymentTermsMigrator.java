package com.vimukti.accounter.migration;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PaymentTerms;

public class PaymentTermsMigrator implements IMigrator<PaymentTerms> {

	@Override
	public JSONObject migrate(PaymentTerms obj, MigratorContext context) throws JSONException {
		JSONObject paymentTerms = new JSONObject();
		{
			JSONObject jsonObject = new JSONObject();
			String _lid = context.getLocalIdProvider().getOrCreate(obj);
			jsonObject.put("@id", _lid);
			jsonObject.put("@_lid", _lid);
			paymentTerms.put("com.vimukti.accounter.shared.common.PaymentTerm", jsonObject);
			CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
			jsonObject.put("name", obj.getName());
			jsonObject.put("description", obj.getDescription());
			jsonObject.put("netDue", obj.getDue());
			jsonObject.put("dueDays", obj.getDueDays());
			double val = obj.getDiscountPercent() / 100;
			jsonObject.put("discountPercentage", BigDecimal.valueOf(val));
			jsonObject.put("ifPaidWithIn", obj.getIfPaidWithIn());
			jsonObject.put("isDateDriven", obj.isDateDriven());
		}
		return paymentTerms;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}