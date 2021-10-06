package com.vimukti.accounter.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.*;
import com.vimukti.accounter.web.client.core.reports.ETDsFilingData;

/**
 * for exporting the the list as CSV file
 * 
 * @author Lingarao.R
 * 
 */
public interface IAccounterExportCSVServiceAsync {

	public void getPayeeListExportCsv(int transactionCategory,
			boolean isActive, AsyncCallback<String> callBack);

	public void getInvoiceListExportCsv(long fromDate, long toDate,
			int invoicesType, int viewType, AsyncCallback<String> callback);

	public void getEstimatesExportCsv(int type, int status, long fromDate,
			long toDate, AsyncCallback<String> callback);

	public void getReceivePaymentsListExportCsv(long fromDate, long toDate,
			int transactionType, int viewType, AsyncCallback<String> callback);

	void getCustomerRefundsListExportCsv(long fromDate, long toDate,
			int viewId, AsyncCallback<String> callback);

	public void getBillsAndItemReceiptListExportCsv(boolean isExpensesList,
			int transactionType, long fromDate, long toDate, int viewType,
			AsyncCallback<String> callback);

	public void getVendorPaymentsListExportCsv(long fromDate, long toDate,
			int viewType, AsyncCallback<String> callBack);

	public void getPaymentsListExportCsv(long fromDate, long toDate,
			int viewType, AsyncCallback<String> callBack);

	public void getPayeeChecksExportCsv(int type, long fromDate, long toDate,
			int viewType, AsyncCallback<String> callBack);

	void getFixedAssetListExportCsv(int status, AsyncCallback<String> callback);

	void getAccountsExportCsv(int typeOfAccount, boolean isActiveAccount,
			AsyncCallback<String> callBack);

	public void getJournalEntriesExportCsv(long fromDate, long toDate,
			AsyncCallback<String> callback);

	public void getUsersActivityLogExportCsv(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long value,
			AsyncCallback<String> callback);

	public void getRecurringsListExportCsv(long fromDate, long toDate,
			AsyncCallback<String> callBack);

	public void getRemindersListExportCsv(int viewType1,
			AsyncCallback<String> callBack);

	public void getWarehousesExportCsv(AsyncCallback<String> callBack);

	void getWarehouseTransfersListExportCsv(AsyncCallback<String> callback);

	void getStockAdjustmentsExportCsv(long startDate, long endDate,
			AsyncCallback<String> callback);

	public void getAllUnitsExportCsv(AsyncCallback<String> callBack);

	public void getItemsExportCsv(boolean isPurchaseType, boolean isSaleType,
			String viewType, int itemType, AsyncCallback<String> callBack);

	public void getTaxItemsListExportCsv(String viewType,
			AsyncCallback<String> callBack);

	public void getTaxCodesListExportCsv(String selectedValue,
			AsyncCallback<String> callBack);

	public void getSalesPersonsListExportCsv(String selectedValue,
			AsyncCallback<String> exportCSVCallback);

	public void getCustomerTransactionsListExportCsv(
			ClientCustomer selectedCustomer, int transactionType,
			int transactionStatusType, ClientFinanceDate startDate,
			ClientFinanceDate endDate, AsyncCallback<String> callback);

	public void getVendorTransactionsListExportCsv(ClientVendor selectedVendor,
			int transactionType, int transactionStatusType,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<String> callback);

	public void getExportListCsv(long startDate, long endDate,
			int transactionType, int viewId, String selectedItem,
			AsyncCallback<String> exportCSVCallback);

	public void getPurchaseOrderExportCsv(int type, long startDate,
			long endDate, AsyncCallback<String> exportCSVCallback);

	public void getBillsAndItemReceiptListExportCSV(boolean b,
			int transactionType, long date, long date2, int checkViewType,
			int i, int j, AsyncCallback<String> exportCSVCallback);

	public void getAccounterRegister(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long id, int start, int length,
			AsyncCallback<String> exportCSVCallback);

	public void getTaxAdjustmentsList(int viewId, long startDate, long endDate,
			int start, int length, AsyncCallback<String> exportCSVCallback);

	public void getPayRunExportCsv(long startDate, long endDate, int type,
			int transactionType, AsyncCallback<String> exportCSVCallback);

	public void getFileTaxesExportCsv(long startDate, long endDate, int type,
			AsyncCallback<String> exportCSVCallback);

	public void getTaxRefundsExportCsv(long startDate, long endDate,
			int viewId, AsyncCallback<String> exportCSVCallback);

	public void getPayTaxesExportCsv(long startDate, long endDate, int viewId,
			AsyncCallback<String> exportCSVCallback);

	public void getBuildAssembliesExportCsv(long startDate, long endDate,
			int viewId, AsyncCallback<String> exportCSVCallback);

	public void exportSavedPaypalTransactions(ClientAccount clientAccount,
			AsyncCallback<String> exportCSVCallback);

	public void generateETDSFillingtext(ETDsFilingData etDsFilingData,
			AsyncCallback<List<String>> callback);

	void generateFrom16APDF(long vendorID, String datesRange, String place,
			String printDate, String tdsCertificateNumber, int type,
			AsyncCallback<List<String>> callback);

	public void getConsultantTransactionsListExportCsv(
			ConsultantsDetailsList selectedCustomer, int transactionType,
			int transactionStatusType, ClientFinanceDate startDate,
			ClientFinanceDate endDate, AsyncCallback<String> callback);

}
