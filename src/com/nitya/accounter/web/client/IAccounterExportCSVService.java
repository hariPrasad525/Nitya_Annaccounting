package com.nitya.accounter.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.nitya.accounter.web.client.core.*;
import com.nitya.accounter.web.client.core.reports.ETDsFilingData;

/**
 * for exporting the the list as CSV file
 * 
 * @author Lingarao.R
 * 
 */
public interface IAccounterExportCSVService extends RemoteService {

	String getPayeeListExportCsv(int transactionCategory, boolean isActive);

	String getInvoiceListExportCsv(long fromDate, long toDate,
			int invoicesType, int viewType);

	String getEstimatesExportCsv(int type, int status, long fromDate,
			long toDate);

	String getReceivePaymentsListExportCsv(long fromDate, long toDate,
			int transactionType, int viewType);

	String getCustomerRefundsListExportCsv(long fromDate, long toDate,
			int viewId);

	String getBillsAndItemReceiptListExportCsv(boolean isExpensesList,
			int transactionType, long fromDate, long toDate, int viewType);

	String getVendorPaymentsListExportCsv(long fromDate, long toDate,
			int viewType);

	String getPaymentsListExportCsv(long fromDate, long toDate, int viewType);

	String getPayeeChecksExportCsv(int type, long fromDate, long toDate,
			int viewType);

	String getFixedAssetListExportCsv(int status);

	String getAccountsExportCsv(int typeOfAccount, boolean isActiveAccount);

	String getJournalEntriesExportCsv(long fromDate, long toDate);

	String getUsersActivityLogExportCsv(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long value);

	String getRecurringsListExportCsv(long fromDate, long toDate);

	String getRemindersListExportCsv(int viewType1);

	String getWarehousesExportCsv();

	String getWarehouseTransfersListExportCsv();

	String getStockAdjustmentsExportCsv(long startDate, long endDate);

	String getAllUnitsExportCsv();

	String getItemsExportCsv(boolean isPurchaseType, boolean isSaleType,
			String viewType, int itemType);

	String getTaxItemsListExportCsv(String viewType);

	String getTaxCodesListExportCsv(String selectedValue);

	String getSalesPersonsListExportCsv(String selectedValue);

	String getCustomerTransactionsListExportCsv(
			ClientCustomer selectedCustomer, int transactionType,
			int transactionStatusType, ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	String getVendorTransactionsListExportCsv(ClientVendor selectedVendor,
			int transactionType, int transactionStatusType,
			ClientFinanceDate startDate, ClientFinanceDate endDate);

	String getExportListCsv(long startDate, long endDate, int transactionType,
			int viewId, String selectedItem);

	String getPurchaseOrderExportCsv(int type, long startDate, long endDate);

	String getBillsAndItemReceiptListExportCSV(boolean b, int transactionType,
			long date, long date2, int checkViewType, int i, int j);

	String getAccounterRegister(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long id, int start, int length);

	String getTaxAdjustmentsList(int viewId, long startDate, long endDate,
			int start, int length);

	String getPayRunExportCsv(long startDate, long endDate, int type,
			int transactionType);

	String getFileTaxesExportCsv(long startDate, long endDate, int type);

	String getTaxRefundsExportCsv(long startDate, long endDate, int viewId);

	String getPayTaxesExportCsv(long startDate, long endDate, int viewId);

	String getBuildAssembliesExportCsv(long startDate, long endDate, int viewId);

	String exportSavedPaypalTransactions(ClientAccount clientAccount);

	List<String> generateETDSFillingtext(ETDsFilingData etDsFilingData);

	List<String> generateFrom16APDF(long vendorID, String datesRange,
			String place, String printDate, String tdsCertificateNumber,
			int type);

	String getConsultantTransactionsListExportCsv(
			ConsultantsDetailsList selectedCustomer, int transactionType,
			int transactionStatusType, ClientFinanceDate startDate,
			ClientFinanceDate endDate);
}
