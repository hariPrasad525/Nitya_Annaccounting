package com.nitya.accounter.migration;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.TDSChalanDetail;
import com.nitya.accounter.core.TDSTransactionItem;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.Vendor;

public class TdsChallanMigrator extends TransactionMigrator<TDSChalanDetail> {

	@Override
	public JSONObject migrate(TDSChalanDetail obj, MigratorContext context) throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		String _lid = context.getLocalIdProvider().getOrCreate(obj);
		jsonObject.put("@id", _lid);
		jsonObject.put("@_lid", _lid);
		JSONObject external = new JSONObject();
		external.put("com.nitya.accounter.shared.tax.TdsChallan", jsonObject);
		jsonObject.put("formType", getFromTypeIdentity(obj.getFormType()));
		jsonObject.put("challanSerialNo", obj.getChalanSerialNumber());
		jsonObject.put("challanPeriod", obj.getChalanPeriod());
		jsonObject.put("financialStartYear", obj.getCompany().getFirstMonthOfFiscalYear());
		jsonObject.put("financialEndYear", obj.getCompany().getFirstMonthOfFiscalYear() + 12);
		jsonObject.put("assesmentStartYear", obj.getAssesmentYearStart());
		jsonObject.put("assesmentEndYear", obj.getAssessmentYearEnd());
		jsonObject.put("fromDate", obj.getFromDate().toEpochDay());
		jsonObject.put("toDate", obj.getToDate().toEpochDay());
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObject.put("paymentMethod", PicklistUtilMigrator.getPaymentMethodIdentifier(paymentMethod));
		} else {
			jsonObject.put("paymentMethod", "CASH");
		}
		jsonObject.put("isTdsDepositedByBookEntry", obj.getIsDeposited());
		jsonObject.put("natureOfPayment", obj.getPaymentSection().toUpperCase());
		jsonObject.put("chequeOrReferenceNo", obj.getCheckNumber());
		jsonObject.put("dateOnTaxPaid", obj.getDateTaxPaid());
		jsonObject.put("bankBSRCode", obj.getBankBsrCode());
		Account payFrom = obj.getPayFrom();
		{
			JSONObject inter = new JSONObject();
			JSONObject outter = new JSONObject();
			outter.put("com.nitya.accounter.shared.common.Account", inter);
			inter.put("@_oid", context.get("Account", payFrom.getID()));
			inter.put("@_lid", context.getLocalIdProvider().getOrCreate(payFrom));
			jsonObject.put("payFrom", outter);
		}
		jsonObject.put("interestPaid", BigDecimal.valueOf(obj.getInterestPaidAmount()));
		jsonObject.put("otherAmountPaid", BigDecimal.valueOf(obj.getOtherAmount()));

		JSONArray array = new JSONArray();
		JSONObject items = new JSONObject();
		items.put("com.nitya.accounter.shared.tax.TdsChallanItem", array);
		List<TDSTransactionItem> tdsTransactionItems = obj.getTdsTransactionItems();
		for (TDSTransactionItem tdsTransactionItem : tdsTransactionItems) {
			JSONObject transactionJson = new JSONObject();
			String _llid = context.getLocalIdProvider().getOrCreate(tdsTransactionItem);
			transactionJson.put("@id", _llid);
			transactionJson.put("@_lid", _llid);
			Transaction transaction = tdsTransactionItem.getTransaction();
			{
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.vendor.PayBill", inter);
				inter.put("@_oid", context.get("Customer", transaction.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(transaction));
				transactionJson.put("payBill", outter);
			}
			Vendor vendor = tdsTransactionItem.getVendor();
			{
				JSONObject inter = new JSONObject();
				JSONObject outter = new JSONObject();
				outter.put("com.nitya.accounter.shared.vendor.Vendor", inter);
				inter.put("@_oid", context.get("Customer", vendor.getID()));
				inter.put("@_lid", context.getLocalIdProvider().getOrCreate(vendor));
				transactionJson.put("deducteeName", outter);
			}
			transactionJson.put("surchargeAmount", BigDecimal.valueOf(tdsTransactionItem.getSurchargeAmount()));
			transactionJson.put("educationCess", tdsTransactionItem.getEduCess());
			array.put(transactionJson);
		}
		jsonObject.put("tDSChallanItems", items);
		return external;
	}

	private String getFromTypeIdentity(int formType) {
		switch (formType) {
		case 1:
			return "Form26Q";
		case 2:
			return "Form27Q";
		case 3:
			return "Form27EQ";
		}
		return null;
	}
}
