package com.nitya.accounter.migration;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Depreciation;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.FixedAsset;
import com.nitya.accounter.utils.HibernateUtil;

public class DepreciationMigrator implements IMigrator<Depreciation> {
	public JSONObject migrate(Depreciation obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.common.Depreciation", jsonObject);
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("status", PicklistUtilMigrator.depreciationStatusIdentity(obj.getStatus()));
		jsonObject.put("depreciateFrom", obj.getDepreciateFrom().toEpochDay());
		jsonObject.put("depreciateTo", obj.getDepreciateTo().toEpochDay());
		FixedAsset fa = obj.getFixedAsset();
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.FixedAsset", inter);
			inter.put("@_oid", context.get("FixedAsset", fa.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(fa));
			jsonObject.put("fixedAsset", outter);
		}
		jsonObject.put("depreciationFor", PicklistUtilMigrator.depreciationForIdentity(obj.getDepreciationFor()));
		try {
			jsonObject.put("depreciatedAmount", getDepreciationAmount(fa, obj, context));
		} catch (ParseException e) {
		}
		jsonObject.put("rollBackDepreciationDate", new FinanceDate().toEpochDay());
		return external;
	}

	private BigDecimal getDepreciationAmount(FixedAsset fa, Depreciation depreciation, MigratorContext context)
			throws ParseException {
		Session session = HibernateUtil.getCurrentSession();
		Criteria criteria = session.createCriteria(Depreciation.class, "obj");
		this.addRestrictions(criteria);
		List<Depreciation> depreciations = criteria.add(Restrictions.eq("company", context.getCompany()))
				.add(Restrictions.lt("depreciateFrom", depreciation.getDepreciateFrom()))
				.addOrder(Order.asc("depreciateFrom")).list();
		FinanceDate fromDate = null;
		if (!depreciations.isEmpty()) {
			fromDate = depreciations.get(0).getDepreciateFrom();
		}
		if (fromDate == null) {
			fromDate = context.getCompany().getPreferences().getDepreciationStartDate();
		}
		FinanceDate toDate = depreciation.getDepreciateTo();
		FinanceDate startDate = context.getCompany().getPreferences().getDepreciationStartDate();
		fromDate = withDayOfMonth(fromDate);
		double depreciatedAmount = 0;
		while (fromDate.compareTo(toDate) <= 0) {
			/**
			 * Adjusting the opening balance of this Fixed Asset each year after
			 * the start date by the actual bookvalue which represents the
			 * updated bookvalue at the end of each month.
			 */
			if (fromDate.getMonth() == startDate.getMonth()) {
				fa.setOpeningBalanceForFiscalYear(fa.getBookValue());
			}
			// DepreciationMethod.StraightLine=1
			double amount = (fa.getDepreciationMethod() == 1 ? fa.getPurchasePrice()
					: fa.getOpeningBalanceForFiscalYear());
			depreciatedAmount += ((amount * fa.getDepreciationRate()) / 12);
			fromDate.setMonth(fromDate.getMonth() + 1);
		}
		return BigDecimal.valueOf(depreciatedAmount);
	}

	private FinanceDate withDayOfMonth(FinanceDate fromDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date convertedDate = dateFormat.parse(fromDate.toString());
		Calendar c = Calendar.getInstance();
		c.setTime(convertedDate);
		return new FinanceDate(convertedDate.getYear(), convertedDate.getMonth(),
				c.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}