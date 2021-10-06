package com.nitya.accounter.developer.api;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.developer.api.core.ApiResult;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.DummyDebitor;
import com.nitya.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.nitya.accounter.web.client.core.Lists.PayeeList;
import com.nitya.accounter.web.client.core.Lists.PayeeStatementsList;
import com.nitya.accounter.web.client.core.reports.AgedDebtors;
import com.nitya.accounter.web.client.core.reports.AmountsDueToVendor;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.DepositDetail;
import com.nitya.accounter.web.client.core.reports.ECSalesList;
import com.nitya.accounter.web.client.core.reports.ECSalesListDetail;
import com.nitya.accounter.web.client.core.reports.ExpenseList;
import com.nitya.accounter.web.client.core.reports.MostProfitableCustomers;
import com.nitya.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.core.reports.SalesTaxLiability;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.nitya.accounter.web.client.core.reports.VATDetail;
import com.nitya.accounter.web.client.core.reports.VATItemDetail;
import com.nitya.accounter.web.client.core.reports.VATItemSummary;
import com.nitya.accounter.web.client.core.reports.VATSummary;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.reports.CheckDetailReport;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class ApiSerializationFactory {
	private static XStream stream;

	public ApiSerializationFactory(boolean isJson) {
		if (isJson) {
			stream = new XStream(new JettisonMappedXmlDriver());
			stream.setMode(XStream.NO_REFERENCES);
		} else {
			stream = new XStream();
		}
		initializeStream();
	}

	private void initializeStream() {
		stream.alias("SalesByCustomerDetail", SalesByCustomerDetail.class);
		stream.alias("PayeeStatementsList", PayeeStatementsList.class);
		stream.alias("AgedDebtors", AgedDebtors.class);
		stream.alias("CheckDetailReport", CheckDetailReport.class);
		stream.alias("DepositDetail", DepositDetail.class);
		stream.alias("ExpenseList", ExpenseList.class);
		stream.alias("DummyDebitor", DummyDebitor.class);
		stream.alias("AmountsDueToVendor", AmountsDueToVendor.class);
		stream.alias("TransactionHistory", TransactionHistory.class);
		stream.alias("MostProfitableCustomers", MostProfitableCustomers.class);
		stream.alias("ReverseChargeListDetail", ReverseChargeListDetail.class);
		stream.alias("ECSalesListDetail", ECSalesListDetail.class);
		stream.alias("ECSalesList", ECSalesList.class);
		stream.alias("TransactionDetailByTaxItem",
				TransactionDetailByTaxItem.class);
		stream.alias("TrialBalance", TrialBalance.class);
		stream.alias("Item", ClientItem.class);
		stream.alias("Customer", ClientCustomer.class);
		stream.alias("VATItemDetail", VATItemDetail.class);
		stream.alias("VATItemSummary", VATItemSummary.class);
		stream.alias("Vendor", ClientVendor.class);
		stream.alias("UncategorisedAmountsReport",
				UncategorisedAmountsReport.class);
		stream.alias("SalesTaxLiability", SalesTaxLiability.class);
		stream.alias("VATSummary", VATSummary.class);
		stream.alias("TransactionDetailByAccount",
				TransactionDetailByAccount.class);
		stream.alias("VATDetail", VATDetail.class);
		stream.alias("OpenAndClosedOrders", OpenAndClosedOrders.class);
		stream.alias("FinanceDate", ClientFinanceDate.class);

		stream.alias("PaginationList", PaginationList.class);
		stream.alias("PayeeList", PayeeList.class);
		stream.alias("ApiResult", ApiResult.class);
	}

	public IAccounterCore deserialize(InputStream inputStream) throws Exception {
		return (IAccounterCore) stream.fromXML(inputStream);
	}

	public ApiResult deserializeApiResult(InputStream inputStream)
			throws Exception {
		return (ApiResult) stream.fromXML(inputStream);
	}

	public IAccounterCore deserialize(String inputStream) throws Exception {
		return (IAccounterCore) stream.fromXML(inputStream);
	}

	public String serialize(IAccounterCore str) throws Exception {
		return stream.toXML(str);
	}

	public String serialize(ApiResult str) throws Exception {
		return stream.toXML(str);
	}

	public List<IAccounterCore> deserializeList(String str) throws Exception {
		// TODO
		return null;
	}

	public String serialize(AccounterException ex) throws Exception {
		return stream.toXML(ex);
	}

	public String serializeResult(ApiResult apiResult) {
		return stream.toXML(apiResult);
	}

	public String serializeList(List<? extends IAccounterCore> str)
			throws Exception {
		return stream.toXML(str);
	}

	public String serializeAnyList(List<?> str) throws Exception {
		return stream.toXML(str);
	}

	public String serializeReportsList(List<? extends BaseReport> list) {
		return stream.toXML(list);
	}

	public String serializeTransacHistCustomerList(List<ClientCustomer> list) {
		return stream.toXML(list);
	}

	public String serializeTransacHistVendorList(List<ClientVendor> list) {
		return stream.toXML(list);
	}

	public String serializeMinAndMaxTrasacDate(List<ClientFinanceDate> list) {
		return stream.toXML(list);
	}

	public String serializeDateList(List<ClientFinanceDate> list) {
		return stream.toXML(list);
	}

	public String serializeCompanyMap(Map<String, Long> list) {
		return stream.toXML(list);
	}

	public <T> T deserialize(InputStream inputStream, T objectById) {
		return (T) stream.fromXML(inputStream, objectById);
	}

	public <T> T deserialize(String inputStream, T objectById) {
		return (T) stream.fromXML(inputStream, objectById);
	}
}
