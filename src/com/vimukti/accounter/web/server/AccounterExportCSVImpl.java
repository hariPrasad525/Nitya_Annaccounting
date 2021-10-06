package com.vimukti.accounter.web.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.web.client.core.*;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FixedAsset;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.utils.CSVExporter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.ICSVExportRunner;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.IAccounterExportCSVService;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.DepositsTransfersList;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.ETDsFilingData;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentList;

/**
 * for exporting the the list as CSV file get
 * 
 * @author Lingarao.R
 * 
 */
public class AccounterExportCSVImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterExportCSVService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AccounterMessages messages = Global.get().messages();

	public AccounterExportCSVImpl() {
		super();

	}

	// From here export csv for list
	@Override
	public String getPayeeListExportCsv(int transactionCategory,
			boolean isActive) {
		try {
			PaginationList<PayeeList> payeeList = getFinanceTool()
					.getPayeeList(transactionCategory, isActive, 0, -1,
							getCompanyId());
			ICSVExportRunner<PayeeList> icsvExportRunner = new ICSVExportRunner<PayeeList>() {

				@Override
				public String[] getColumns() {
					return new String[] {
							messages.payeeName(Global.get().Customer()),
							messages.currentMonth(), messages.yearToDate(),
							messages.balance() };
				}

				@Override
				public String getColumnValue(PayeeList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getPayeeName();
						break;
					case 1:
						columnValue = obj.getCurrentMonth() != 0.0 ? amountAsStringWithCurrency(
								obj.getCurrentMonth(), obj.getCurrecny())
								: "0.0";
						break;
					case 2:
						columnValue = obj.getYearToDate() != 0.0 ? amountAsStringWithCurrency(
								obj.getYearToDate(), obj.getCurrecny()) : "0.0";
						break;
					case 3:
						columnValue = obj.getBalance() != 0.0 ? amountAsStringWithCurrency(
								obj.getBalance(), obj.getCurrecny()) : "0.0";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<PayeeList> csvExporter = new CSVExporter<PayeeList>(
					icsvExportRunner);
			return csvExporter.export(payeeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getInvoiceListExportCsv(long fromDate, long toDate,
			int invoicesType, int viewType) {

		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<InvoicesList> invoiceList = getFinanceTool()
					.getInventoryManager().getInvoiceList(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(),
							invoicesType, viewType, 0, -1);
			ICSVExportRunner<InvoicesList> icsvExportRunner = new ICSVExportRunner<InvoicesList>() {

				@Override
				public String[] getColumns() {

					return new String[] { messages.type(), messages.date(),
							messages.no(),
							messages.payeeName(Global.get().customer()),
							messages.dueDate(), messages.netPrice(),
							messages.totalPrice() };
				}

				@Override
				public String getColumnValue(InvoicesList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility.getTransactionName(obj.getType());
						break;
					case 1:
						columnValue = obj.getDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDate().getDate())) : "";
						break;
					case 2:
						columnValue = obj.getNumber() != null ? obj.getNumber()
								: " ";
						break;
					case 3:
						columnValue = obj.getCustomerName();
						break;
					case 4:
						columnValue = obj.getDueDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDueDate().getDate())) : " ";
						break;
					case 5:
						columnValue = obj.getNetAmount() != 0 ? amountAsStringWithCurrency(
								obj.getNetAmount(), obj.getCurrency()) : "";
						break;
					case 6:
						columnValue = obj.getTotalPrice() != 0 ? amountAsStringWithCurrency(
								obj.getTotalPrice(), obj.getCurrency()) : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<InvoicesList> csvExporter = new CSVExporter<InvoicesList>(
					icsvExportRunner);
			return csvExporter.export(invoiceList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getEstimatesExportCsv(int type, int status, long fromDate,
			long toDate) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			ArrayList<Estimate> estimates = getFinanceTool()
					.getCustomerManager().getEstimates(getCompanyId(), type,
							status, dates[0], dates[1], 0, -1);
			ICSVExportRunner<Estimate> icsvExportRunner = new ICSVExportRunner<Estimate>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.date(),
							messages.payeeName(Global.get().Customer()),
							messages.no(), messages.phone(),
							messages.expirationDate(), messages.deliveryDate(),
							messages.totalPrice() };
				}

				@Override
				public String getColumnValue(Estimate obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDate().getDate()));
						break;
					case 1:
						columnValue = obj.getCustomer().getName();
						break;
					case 2:
						columnValue = obj.getNumber() != null ? obj.getNumber()
								: "";
						break;
					case 3:
						columnValue = obj.getPhone() != null ? obj.getPhone()
								: "";
						break;
					case 4:
						columnValue = obj.getExpirationDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getExpirationDate().getDate())) : "";
						break;
					case 5:
						columnValue = obj.getDeliveryDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDeliveryDate().getDate())) : "";
						break;
					case 6:
						columnValue = obj.getTotal() != 0 ? amountAsStringWithCurrency(
								obj.getTotal(), obj.getCurrency()) : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<Estimate> csvExporter = new CSVExporter<Estimate>(
					icsvExportRunner);
			return csvExporter.export(estimates);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getReceivePaymentsListExportCsv(long fromDate, long toDate,
			int transactionType, int viewType) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<ReceivePaymentsList> receivePaymentsList = getFinanceTool()
					.getCustomerManager().getReceivePaymentsList(
							getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), transactionType, 0, -1,
							viewType);
			ICSVExportRunner<ReceivePaymentsList> icsvExportRunner = new ICSVExportRunner<ReceivePaymentsList>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.type(),
							messages.paymentDate(), messages.no(),
							messages.payeeName(Global.get().Customer()),
							messages.payMethod(), messages.checkNo(),
							messages.amountPaid() };
				}

				@Override
				public String getColumnValue(ReceivePaymentsList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility.getTransactionName(obj.getType());
						break;
					case 1:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getPaymentDate().getDate()));
						break;
					case 2:
						columnValue = obj.getNumber() != null ? obj.getNumber()
								: "";
						break;
					case 3:
						columnValue = obj.getCustomerName();
						break;
					case 4:
						columnValue = obj.getPaymentMethodName();
						break;
					case 5:
						columnValue = obj.getCheckNumber() != null ? obj
								.getCheckNumber() : "";
						break;
					case 6:
						columnValue = obj.getAmountPaid() != 0 ? amountAsStringWithCurrency(
								obj.getAmountPaid(), obj.getCurrency()) : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<ReceivePaymentsList> csvExporter = new CSVExporter<ReceivePaymentsList>(
					icsvExportRunner);
			return csvExporter.export(receivePaymentsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getCustomerRefundsListExportCsv(long fromDate, long toDate,
			int viewId) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<CustomerRefundsList> customerRefundsList = getFinanceTool()
					.getCustomerManager().getCustomerRefundsList(
							getCompanyId(), dates[0], dates[1], viewId);
			ICSVExportRunner<CustomerRefundsList> icsvExportRunner = new ICSVExportRunner<CustomerRefundsList>() {

				@Override
				public String[] getColumns() {

					return new String[] { messages.paymentDate(),
							messages.paymentNo(), messages.status(),
							messages.issueDate(), messages.name(),
							messages.paymentMethod(), messages.amountPaid() };
				}

				@Override
				public String getColumnValue(CustomerRefundsList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getPaymentDate()));
						break;
					case 1:
						columnValue = obj.getPaymentNumber();
						break;
					case 2:

						String transactionName = Utility
								.getTransactionName(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
						String transactionStatus = Utility
								.getTransactionStatus(
										ClientTransaction.TYPE_CUSTOMER_REFUNDS,
										obj.getStatus());
						columnValue = (obj.getStatus() == ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED && obj
								.getSaveStatus() == ClientTransaction.STATUS_DRAFT) ? "Draft"
								: transactionName + "" + "("
										+ transactionStatus + ")";

						break;
					case 3:
						ClientFinanceDate issueDate = obj.getIssueDate();
						if (issueDate == null) {
							columnValue = "";
						} else {
							columnValue = Utility
									.getDateInSelectedFormat(new FinanceDate(
											issueDate));
						}
						break;
					case 4:
						columnValue = obj.getName() == null ? "" : obj
								.getName();
						break;
					case 5:
						columnValue = obj.getPaymentMethod() != null ? obj
								.getPaymentMethod() : "";
						break;
					case 6:
						columnValue = amountAsStringWithCurrency(
								obj.getAmountPaid(), obj.getCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<CustomerRefundsList> csvExporter = new CSVExporter<CustomerRefundsList>(
					icsvExportRunner);
			return csvExporter.export(customerRefundsList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getBillsAndItemReceiptListExportCsv(boolean isExpensesList,
			int transactionType, long fromDate, long toDate, int viewType) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<BillsList> billsList = getFinanceTool()
					.getVendorManager().getBillsList(isExpensesList,
							getCompanyId(), transactionType,
							dates[0].getDate(), dates[1].getDate(), 0, -1,
							viewType);
			ICSVExportRunner<BillsList> icsvExportRunner = new ICSVExportRunner<BillsList>() {

				@Override
				public String[] getColumns() {

					return new String[] {
							messages.type(),
							messages.date(),
							messages.no(),
							Global.get().messages()
									.payeeName(Global.get().Vendor()),
							messages.originalAmount(), messages.balance() };
				}

				@Override
				public String getColumnValue(BillsList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility.getTransactionName(obj.getType());
						break;
					case 1:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDate()));
						break;
					case 2:
						columnValue = obj.getNumber() != null ? obj.getNumber()
								: "";
						break;
					case 3:
						columnValue = obj.getVendorName() != null ? obj
								.getVendorName() : "";
						break;
					case 4:
						columnValue = obj.getOriginalAmount() != 0 ? amountAsStringWithCurrency(
								obj.getOriginalAmount(), obj.getCurrency())
								: "";
						break;
					case 5:
						if (obj.getBalance() != null) {
							columnValue = obj.getBalance() != 0 ? amountAsStringWithCurrency(
									obj.getBalance(), obj.getCurrency()) : "";
						} else {
							columnValue = amountAsStringWithCurrency(0,
									obj.getCurrency());
						}
						break;

					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<BillsList> csvExporter = new CSVExporter<BillsList>(
					icsvExportRunner);
			return csvExporter.export(billsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getVendorPaymentsListExportCsv(long fromDate, long toDate,
			int viewType) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<PaymentsList> vendorPaymentsList = getFinanceTool()
					.getVendorManager().getVendorPaymentsList(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), 0, -1,
							viewType);
			ICSVExportRunner<PaymentsList> icsvExportRunner = new ICSVExportRunner<PaymentsList>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.payDate(), messages.payNo(),
							messages.status(), messages.issueDate(),
							messages.name(), messages.type(),
							messages.payMethod(), messages.checkNo(),
							messages.amountPaid() };
				}

				@Override
				public String getColumnValue(PaymentsList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getPaymentDate()));
						break;
					case 1:
						columnValue = obj.getPaymentNumber();
						break;
					case 2:
						String transactionStatus = Utility
								.getTransactionStatus(obj.getType(),
										obj.getStatus());
						columnValue = transactionStatus;
						break;
					case 3:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getIssuedDate()));
						break;
					case 4:
						columnValue = obj.getName();
						break;
					case 5:
						columnValue = Utility.getTransactionName(obj.getType());
						break;
					case 6:
						columnValue = obj.getPaymentMethodName();
						break;
					case 7:
						columnValue = obj.getCheckNumber();
						break;
					case 8:
						columnValue = amountAsStringWithCurrency(
								obj.getAmountPaid(), obj.getCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<PaymentsList> csvExporter = new CSVExporter<PaymentsList>(
					icsvExportRunner);
			return csvExporter.export(vendorPaymentsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getPaymentsListExportCsv(long fromDate, long toDate,
			int viewType) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<PaymentsList> paymentsList = getFinanceTool()
					.getCustomerManager().getPaymentsList(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), 0, -1,
							viewType);
			ICSVExportRunner<PaymentsList> icsvExportRunner = new ICSVExportRunner<PaymentsList>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.payDate(), messages.payNo(),
							messages.status(), messages.issueDate(),
							messages.name(), messages.type(),
							messages.payMethod(), messages.checkNo(),
							messages.amountPaid() };
				}

				@Override
				public String getColumnValue(PaymentsList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getPaymentDate()));
						break;
					case 1:
						columnValue = obj.getPaymentNumber();
						break;
					case 2:
						columnValue = Utility.getStatus(obj.getType(),
								obj.getStatus());
						break;
					case 3:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getIssuedDate()));
						break;
					case 4:
						columnValue = obj.getName();
						break;
					case 5:
						columnValue = Utility.getTransactionName(obj.getType());
						break;
					case 6:
						columnValue = obj.getPaymentMethodName();
						break;
					case 7:
						columnValue = obj.getCheckNumber();
						break;
					case 8:
						columnValue = amountAsStringWithCurrency(
								obj.getAmountPaid(), obj.getCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<PaymentsList> csvExporter = new CSVExporter<PaymentsList>(
					icsvExportRunner);
			return csvExporter.export(paymentsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getPayeeChecksExportCsv(int payeeType, long fromDate,
			long toDate, int viewType) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<PaymentsList> payeeChecks = getFinanceTool()
					.getVendorManager().getPayeeChecks(getCompanyId(),
							payeeType, dates[0], dates[1], viewType, 0, -1);
			ICSVExportRunner<PaymentsList> icsvExportRunner = new ICSVExportRunner<PaymentsList>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.payDate(), messages.payNo(),
							messages.status(), messages.issueDate(),
							messages.name(), messages.type(),
							messages.payMethod(), messages.checkNo(),
							messages.amountPaid() };
				}

				@Override
				public String getColumnValue(PaymentsList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getPaymentDate()));
						break;
					case 1:
						columnValue = obj.getPaymentNumber();
						break;
					case 2:
						columnValue = Utility.getStatus(obj.getType(),
								obj.getStatus());
						break;
					case 3:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getIssuedDate()));
						break;
					case 4:
						columnValue = obj.getName();
						break;
					case 5:
						columnValue = Utility.getTransactionName(obj.getType());
						break;
					case 6:
						columnValue = obj.getPaymentMethodName();
						break;
					case 7:
						columnValue = obj.getCheckNumber();
						break;
					case 8:
						columnValue = amountAsStringWithCurrency(
								obj.getAmountPaid(), obj.getCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<PaymentsList> csvExporter = new CSVExporter<PaymentsList>(
					icsvExportRunner);
			return csvExporter.export(payeeChecks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getFixedAssetListExportCsv(final int status) {
		try {
			ArrayList<FixedAsset> fixedAssets = getFinanceTool()
					.getFixedAssetManager().getFixedAssets(status,
							getCompanyId());
			ICSVExportRunner<FixedAsset> icsvExportRunner = new ICSVExportRunner<FixedAsset>() {

				@Override
				public String[] getColumns() {
					if (status == 1) {
						return new String[] { messages.item(),
								messages.assetNumber(), messages.Account(),
								messages.purchaseDate(),
								messages.purchasePrice() };
					} else if (status == 2) {
						return new String[] { messages.item(),
								messages.assetNumber(), messages.Account(),
								messages.purchaseDate(),
								messages.purchasePrice(), messages.bookValue() };
					} else if (status == 3) {
						return new String[] { messages.item(),
								messages.assetNumber(), messages.Account(),
								messages.disposalDate(),
								messages.disposalPrice(),
								messages.gainsOrLosses() };
					}
					return null;
				}

				@Override
				public String getColumnValue(FixedAsset obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getName() != null ? obj.getName()
								: "";
						break;
					case 1:
						columnValue = obj.getAssetNumber() != null ? obj
								.getAssetNumber() : "";
						break;
					case 2:
						columnValue = obj.getAssetAccount() != null ? obj
								.getAssetAccount().getName() : "";
						break;
					case 3:
						if (status == 3) {
							columnValue = obj.getSoldOrDisposedDate() != null ? Utility
									.getDateInSelectedFormat(obj
											.getSoldOrDisposedDate()) : "";
						} else {
							columnValue = obj.getPurchaseDate() != null ? Utility
									.getDateInSelectedFormat(obj
											.getPurchaseDate()) : "";
						}
						break;
					case 4:
						if (status == 3) {
							columnValue = obj.getSalePrice() != 0 ? amountAsStringWithCurrency(
									obj.getSalePrice(), obj.getCompany()
											.getPrimaryCurrency()) : "";
						} else {
							columnValue = obj.getPurchasePrice() != 0 ? amountAsStringWithCurrency(
									obj.getPurchasePrice(), obj.getCompany()
											.getPrimaryCurrency()) : "";
						}
						break;
					case 5:
						if (status == 2) {
							columnValue = obj.getBookValue() != 0 ? amountAsStringWithCurrency(
									obj.getBookValue(), obj.getCompany()
											.getPrimaryCurrency()) : "";
						} else {
							columnValue = obj.getLossOrGain() != 0 ? amountAsStringWithCurrency(
									obj.getLossOrGain(), obj.getCompany()
											.getPrimaryCurrency()) : "";
						}
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<FixedAsset> csvExporter = new CSVExporter<FixedAsset>(
					icsvExportRunner);
			return csvExporter.export(fixedAssets);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAccountsExportCsv(int typeOfAccount,
			boolean isActiveAccount) {
		try {
			PaginationList<ClientAccount> accounts = new PaginationList<ClientAccount>();
			if (typeOfAccount == ClientAccount.TYPE_BANK) {
				accounts = getFinanceTool()
						.getCompanyManager()
						.getBankAccounts(getCompanyId(), isActiveAccount, 0, -1);
			} else {
				accounts = getFinanceTool().getCompanyManager().getAccounts(
						typeOfAccount, isActiveAccount, 0, -1, getCompanyId());
			}
			ICSVExportRunner<ClientAccount> icsvExportRunner = new ICSVExportRunner<ClientAccount>() {

				@Override
				public String[] getColumns() {

					return new String[] { messages.no(), messages.name(),
							messages.type(), messages.balance() };
				}

				@Override
				public String getColumnValue(ClientAccount obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getNumber() != null ? obj.getNumber()
								: "";
						break;
					case 1:
						columnValue = obj.getName() != null ? obj.getName()
								: "";
						break;
					case 2:
						columnValue = obj.getType() != 0 ? Utility
								.getAccountTypeString(obj.getType()) : " ";
						break;
					case 3:
						columnValue = amountAsStringWithCurrency(
								obj.getTotalBalanceInAccountCurrency(),
								obj.getCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};

			CSVExporter<ClientAccount> csvExporter = new CSVExporter<ClientAccount>(
					icsvExportRunner);
			return csvExporter.export(accounts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getJournalEntriesExportCsv(long fromDate, long toDate) {

		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<JournalEntry> journalEntries = getFinanceTool()
					.getJournalEntries(getCompanyId(), dates[0], dates[1], 0,
							-1);
			ICSVExportRunner<JournalEntry> icsvExportRunner = new ICSVExportRunner<JournalEntry>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.no(), messages.date(),
							messages.memo(), messages.amount() };
				}

				@Override
				public String getColumnValue(JournalEntry obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getNumber();
						break;
					case 1:
						columnValue = obj.getDate() != null ? Utility
								.getDateInSelectedFormat(obj.getDate()) : "";
						break;
					case 2:
						columnValue = obj.getMemo();
						break;
					case 3:
						columnValue = obj.getTotal() != 0 ? amountAsStringWithCurrency(
								obj.getTotal(), obj.getCurrency()) : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<JournalEntry> csvExporter = new CSVExporter<JournalEntry>(
					icsvExportRunner);
			return csvExporter.export(journalEntries);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getUsersActivityLogExportCsv(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long value) {
		try {
			final Company company = getFinanceTool().getCompany(getCompanyId());
			PaginationList<ClientActivity> usersActivityLog = getFinanceTool()
					.getUserManager().getUsersActivityLog(startDate, endDate,
							0, -1, getCompanyId(), value);
			ICSVExportRunner<ClientActivity> icsvExportRunner = new ICSVExportRunner<ClientActivity>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.modifiedTime(),
							messages.userName(), messages.activity(),
							messages.name(), messages.date(), messages.amount() };
				}

				@Override
				public String getColumnValue(ClientActivity obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						String dateInSelectedFormat = Utility
								.getDateInSelectedFormat(new FinanceDate(
										new Date(obj.getTime())));
						columnValue = dateInSelectedFormat;

						break;
					case 1:
						columnValue = obj.getUserName() != null ? obj
								.getUserName() : " ";
						break;
					case 2:
						String dataType = obj.getDataType() == null ? "" : obj
								.getDataType();
						columnValue = Utility.getActivityType(
								obj.getActivityType()).concat(dataType);
						break;
					case 3:
						columnValue = obj.getName() != null ? obj.getName()
								: " ";
						break;
					case 4:
						columnValue = obj.getDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDate())) : "";
						break;
					case 5:
						columnValue = obj.getAmount() != null ? getAmountAsStringInCurrency(
								obj.getAmount(), obj.getCurrency()) : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<ClientActivity> csvExporter = new CSVExporter<ClientActivity>(
					icsvExportRunner);
			return csvExporter.export(usersActivityLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getRecurringsListExportCsv(long fromDate, long toDate) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<ClientRecurringTransaction> allRecurringTransactions = getFinanceTool()
					.getAllRecurringTransactions(getCompanyId(), dates[0],
							dates[1]);
			ICSVExportRunner<ClientRecurringTransaction> icsvExportRunner = new ICSVExportRunner<ClientRecurringTransaction>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.name(), messages.type(),
							messages.transactionType(), messages.interval(),
							messages.prevSchedule(), messages.nextSchedule(),
							messages.amount() };
				}

				@Override
				public String getColumnValue(ClientRecurringTransaction obj,
						int index) {
					String columnValue = " ";
					switch (index) {
					case 0:// Name
						columnValue = obj.getName();
						break;
					case 1: // Recurring Type
						switch (obj.getType()) {
						case 0:
							columnValue = messages.scheduled();
						case 1:
							columnValue = messages.reminder();
						case 2:
							columnValue = messages.unScheduled();
						}
						break;
					case 2: // Transaction Type
						columnValue = obj.getTransaction() != null ? Utility
								.getTransactionName(obj.getTransaction()
										.getType()) : "";
						break;
					case 3:
						if (obj.getType() != ClientRecurringTransaction.RECURRING_UNSCHEDULED) {

							columnValue = obj.getFrequencyString();
						} else {
							columnValue = "";
						}
						break;
					case 4: // prevScheduleOn
						columnValue = obj.getPrevScheduleOn() == 0 ? ""
								: Utility
										.getDateInSelectedFormat(new FinanceDate(
												obj.getPrevScheduleOn()));

						break;
					case 5: // nextScheduleOn
						columnValue = Utility.getDateInSelectedFormat(obj
								.getNextScheduleOn() == 0 ? null
								: new FinanceDate(obj.getNextScheduleOn()));
						break;
					case 6: // TODO transaction amount
						columnValue = obj.getTransaction() != null ? String
								.valueOf(obj.getTransaction().getTotal()) : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<ClientRecurringTransaction> csvExporter = new CSVExporter<ClientRecurringTransaction>(
					icsvExportRunner);
			return csvExporter.export(allRecurringTransactions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getRemindersListExportCsv(int viewType1) {
		try {
			PaginationList<ClientReminder> remindersList = getFinanceTool()
					.getCompanyManager().getRemindersList(0, -1, viewType1,
							getCompanyId());
			ICSVExportRunner<ClientReminder> icsvExportRunner = new ICSVExportRunner<ClientReminder>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.name(), " ",
							messages.transactionDate(),
							messages.transactionType(), messages.amount() };
				}

				@Override
				public String getColumnValue(ClientReminder obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getName();
						break;
					case 1:
						columnValue = messages.editAndCreate();
						break;
					case 2:
						columnValue = obj.getTransactionDate() != 0.0 ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getTransactionDate())) : "";
						break;
					case 3:
						columnValue = obj.getRecurringTransaction() != null ? Utility
								.getTransactionName(obj
										.getRecurringTransaction()
										.getTransaction().getType()) : "";
						break;
					case 4:
						columnValue = obj.getRecurringTransaction() != null ? String
								.valueOf(obj.getRecurringTransaction()
										.getTransaction().getTotal()) : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<ClientReminder> csvExporter = new CSVExporter<ClientReminder>(
					icsvExportRunner);
			return csvExporter.export(remindersList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getWarehousesExportCsv() {
		try {
			PaginationList<ClientWarehouse> warehouses = getFinanceTool()
					.getInventoryManager().getWarehouses(getCompanyId());
			ICSVExportRunner<ClientWarehouse> icsvExportRunner = new ICSVExportRunner<ClientWarehouse>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.warehouseCode(),
							messages.warehouseName(), messages.ddiNumber() };
				}

				@Override
				public String getColumnValue(ClientWarehouse obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getWarehouseCode() != null ? obj
								.getWarehouseCode() : " ";
						break;
					case 1:
						columnValue = obj.getName() != null ? obj.getName()
								: "";
						break;
					case 2:
						columnValue = obj.getDDINumber() != null ? obj
								.getDDINumber() : " ";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<ClientWarehouse> csvExporter = new CSVExporter<ClientWarehouse>(
					icsvExportRunner);
			return csvExporter.export(warehouses);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getWarehouseTransfersListExportCsv() {
		try {
			ArrayList<ClientStockTransfer> warehouseTransfersList = getFinanceTool()
					.getInventoryManager().getWarehouseTransfersList(
							getCompanyId());
			final Session session = HibernateUtil.getCurrentSession();
			final Company company = getFinanceTool().getCompany(getCompanyId());
			ICSVExportRunner<ClientStockTransfer> icsvExportRunner = new ICSVExportRunner<ClientStockTransfer>() {
				@Override
				public String[] getColumns() {
					return new String[] { messages.fromWarehouse(),
							messages.toWarehouse(), messages.itemStatus() };
				}

				@Override
				public String getColumnValue(ClientStockTransfer obj, int index) {

					String columnValue = null;
					switch (index) {
					case 0:
						if (obj.getFromWarehouse() != 0)
							for (Warehouse warehouse : company.getWarehouses()) {
								if (warehouse.getID() == obj.getFromWarehouse()) {
									columnValue = warehouse.getName();
								}
							}
						break;
					case 1:
						if (obj.getToWarehouse() != 0)
							for (Warehouse warehouse : company.getWarehouses()) {
								if (warehouse.getID() == obj.getToWarehouse()) {
									columnValue = warehouse.getName();
								}
							}
						break;
					case 2:
						StringBuffer result = new StringBuffer();
						if (!obj.getStockTransferItems().isEmpty()) {
							for (ClientStockTransferItem item : obj
									.getStockTransferItems()) {
								result.append(" ");
								Item itm = (Item) session.get(Item.class,
										item.getItem());
								result.append(itm.getName());
								result.append(" : ");
								result.append(item.getQuantity().getValue());
								result.append(" ");
								Unit unit = (Unit) session.get(Unit.class, item
										.getQuantity().getUnit());
								result.append(unit.getType());
								result.append(",");
							}
							columnValue = result.toString();
						} else {
							columnValue = " ";
						}
						break;

					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<ClientStockTransfer> csvExporter = new CSVExporter<ClientStockTransfer>(
					icsvExportRunner);
			return csvExporter.export(warehouseTransfersList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getStockAdjustmentsExportCsv(long startDate, long endDate) {
		try {
			ArrayList<StockAdjustmentList> stockAdjustments = getFinanceTool()
					.getInventoryManager().getStockAdjustments(startDate,
							endDate, getCompanyId());
			final Company company = getFinanceTool().getCompany(getCompanyId());

			ICSVExportRunner<StockAdjustmentList> icsvExportRunner = new ICSVExportRunner<StockAdjustmentList>() {
				@Override
				public String[] getColumns() {
					return new String[] { messages.wareHouse(),
							messages.itemName(), messages.adjustedQty() };
				}

				@Override
				public String getColumnValue(StockAdjustmentList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getWareHouse() != null ? obj
								.getWareHouse() : "";
						break;
					case 1:
						columnValue = obj.getItem() != null ? obj.getItem()
								: "";
						break;
					case 2:
						if (obj.getQuantity().getUnit() != 0) {
							Unit unit = (Unit) HibernateUtil
									.getCurrentSession().get(Unit.class,
											obj.getQuantity().getUnit());
							StringBuffer result = new StringBuffer();
							result.append(obj.getQuantity().getValue());
							result.append(" ");
							result.append(unit.getType());
							columnValue = result.toString();
						} else {
							columnValue = " ";
						}
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<StockAdjustmentList> csvExporter = new CSVExporter<StockAdjustmentList>(
					icsvExportRunner);
			return csvExporter.export(stockAdjustments);
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public String getAllUnitsExportCsv() {
		try {
			PaginationList<ClientMeasurement> allUnits = getFinanceTool()
					.getInventoryManager().getAllUnits(getCompanyId());
			ICSVExportRunner<ClientMeasurement> icsvExportRunner = new ICSVExportRunner<ClientMeasurement>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.measurementName(),
							messages.measurementDescription(),
							messages.unitName(), messages.factor() };
				}

				@Override
				public String getColumnValue(ClientMeasurement obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getName();
						break;
					case 1:
						columnValue = obj.getDesctiption();
						break;
					case 2:
						for (ClientUnit unit : obj.getUnits()) {
							if (unit.isDefault()) {
								columnValue = String.valueOf(unit.getType());
							}
						}
						break;
					case 3:
						for (ClientUnit unit : obj.getUnits()) {
							if (unit.isDefault()) {
								columnValue = String.valueOf(unit.getFactor());
							}
							break;
						}
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<ClientMeasurement> csvExporter = new CSVExporter<ClientMeasurement>(
					icsvExportRunner);
			return csvExporter.export(allUnits);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param companyId
	 * @return
	 */
	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate(
			long companyId) {
		List<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = getFinanceTool().getManager()
					.getMinimumAndMaximumTransactionDate(getCompanyId());
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ClientFinanceDate>(transactionDates);
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param companyId
	 * @return
	 */
	private FinanceDate[] getMinimumAndMaximumDates(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		List<ClientFinanceDate> dates = getMinimumAndMaximumTransactionDate(companyId);
		ClientFinanceDate startDate1 = dates.get(0) == null ? new ClientFinanceDate()
				: dates.get(0);
		ClientFinanceDate endDate2 = dates.get(1) == null ? new ClientFinanceDate()
				: dates.get(1);

		FinanceDate transtartDate;
		if (startDate == null || startDate.isEmpty())
			transtartDate = new FinanceDate(startDate1);
		else
			transtartDate = new FinanceDate(startDate.getDate());
		FinanceDate tranendDate;
		if (endDate == null || endDate.isEmpty())
			tranendDate = new FinanceDate(endDate2);
		else
			tranendDate = new FinanceDate(endDate.getDate());

		return new FinanceDate[] { transtartDate, tranendDate };
	}

	@Override
	public String getItemsExportCsv(final boolean isPurchaseType,
			final boolean isSalesType, String viewType, int itemType) {

		try {
			List<Item> allItems = null;

			Session session = HibernateUtil.getCurrentSession();

			List<Item> items = session.getNamedQuery("getAllItems")
					.setParameter("companyId", getCompanyId()).list();

			if (itemType == 0) {
				if (isSalesType && isPurchaseType) {
					allItems = items;
				} else if (isPurchaseType) {
					allItems = getPurchaseItems(items, viewType);
				} else if (isSalesType) {
					allItems = getSalesItems(items, viewType);
				}
			} else if (itemType == 1) {
				allItems = getInventoryItems(items, viewType);
			} else {
				allItems = getInventoryAssemblyItems(items, viewType);
			}
			ICSVExportRunner<Item> icsvExportRunner = new ICSVExportRunner<Item>() {

				@Override
				public String[] getColumns() {

					if (isPurchaseType && isSalesType) {
						return new String[] { messages.itemName(),
								messages.description(), messages.type(),
								messages.avarageCost(), messages.salesPrice(),
								messages.purchasePrice() };
					} else if (isPurchaseType) {
						return new String[] { messages.itemName(),
								messages.description(), messages.type(),
								messages.avarageCost(),
								messages.purchasePrice() };
					} else {
						return new String[] { messages.itemName(),
								messages.description(), messages.type(),
								messages.avarageCost(), messages.salesPrice() };
					}
				}

				@Override
				public String getColumnValue(Item obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						return obj.getName() != null ? obj.getName() : "";
					case 1:
						if (!isPurchaseType) {
							columnValue = obj.getSalesDescription() != null ? obj
									.getSalesDescription() : "";
						} else
							columnValue = obj.getPurchaseDescription() != null ? obj
									.getPurchaseDescription() : "";
						break;
					case 2:
						columnValue = Utility.getItemTypeText(obj) != null ? Utility
								.getItemTypeText(obj) : "";
						break;
					case 3:
						columnValue = amountAsStringWithCurrency(
								obj.getAverageCost(), obj.getCompany()
										.getPrimaryCurrency());
						break;
					case 4:
						if (!(isPurchaseType && isSalesType)) {
							if (obj.isISellThisItem()) {
								columnValue = amountAsStringWithCurrency(
										obj.getSalesPrice(), obj.getCompany()
												.getPrimaryCurrency());
							} else {
								columnValue = amountAsStringWithCurrency(
										obj.getPurchasePrice(), obj
												.getCompany()
												.getPrimaryCurrency());
							}
						}
						if (isSalesType) {
							columnValue = amountAsStringWithCurrency(
									obj.getSalesPrice(), obj.getCompany()
											.getPrimaryCurrency());
						} else
							columnValue = amountAsStringWithCurrency(
									obj.getPurchasePrice(), obj.getCompany()
											.getPrimaryCurrency());
						break;
					case 5:
						if (isPurchaseType && isSalesType) {
							columnValue = amountAsStringWithCurrency(
									obj.getPurchasePrice(), obj.getCompany()
											.getPrimaryCurrency());
						}
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<Item> csvExporter = new CSVExporter<Item>(
					icsvExportRunner);
			return csvExporter.export(allItems);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Item> getInventoryAssemblyItems(List<Item> allItems,
			String viewType) {
		List<Item> items = new ArrayList<Item>();
		for (Item item : allItems) {
			if (viewType.equalsIgnoreCase(messages.active())) {
				if (item.isActive() == true
						&& item.getType() == Item.TYPE_INVENTORY_ASSEMBLY) {
					items.add(item);
				}
			} else {
				if (item.isActive() == false
						&& item.getType() == Item.TYPE_INVENTORY_ASSEMBLY) {
					items.add(item);
				}
			}
		}
		return items;
	}

	private List<Item> getInventoryItems(List<Item> allItems, String viewType) {
		List<Item> items = new ArrayList<Item>();
		for (Item item : allItems) {
			if (viewType.equalsIgnoreCase(messages.active())) {
				if (item.isActive() == true
						&& item.getType() == Item.TYPE_INVENTORY_PART) {
					items.add(item);
				}
			} else {
				if (item.isActive() == false
						&& item.getType() == Item.TYPE_INVENTORY_PART) {
					items.add(item);
				}
			}
		}
		return items;

	}

	/**
	 * 
	 * @param allItems
	 * @param viewType
	 * @return
	 */
	private List<Item> getSalesItems(List<Item> allItems, String viewType) {
		List<Item> items = new ArrayList<Item>();
		for (Item item : allItems) {
			if (item.isISellThisItem()) {
				items.add(item);
			}
		}
		return items;

	}

	/**
	 * 
	 * @param allItems
	 * @param viewType
	 * @return
	 */
	private List<Item> getPurchaseItems(List<Item> allItems, String viewType) {

		List<Item> items = new ArrayList<Item>();
		for (Item item : allItems) {
			if (item.isIBuyThisItem()) {
				items.add(item);
			}
		}
		return items;

	}

	@Override
	public String getTaxItemsListExportCsv(String viewType) {
		List<TAXItem> taxItems = new ArrayList<TAXItem>();
		Session session = HibernateUtil.getCurrentSession();

		List<TAXItem> list = session.getNamedQuery("getTaxItems")
				.setParameter("companyId", getCompanyId()).list();
		try {
			final Company company = getFinanceTool().getCompany(getCompanyId());
			for (TAXItem taxItem : list) {
				if (viewType.equalsIgnoreCase(messages.active())) {
					if (taxItem.isActive() == true) {
						taxItems.add(taxItem);
					}
				} else {
					if (taxItem.isActive() == false) {
						taxItems.add(taxItem);
					}
				}
			}
			ICSVExportRunner<TAXItem> icsvExportRunner = new ICSVExportRunner<TAXItem>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.product(),
							messages.vatAgency(), messages.description(),
							messages.rate() };
				}

				@Override
				public String getColumnValue(TAXItem item, int index) {
					String columnValue = null;
					switch (index) {

					case 0:
						columnValue = item.getName() != null ? item.getName()
								: "";
					case 1:
						TAXAgency agency = null;
						if (item.getTaxAgency() != null) {
							agency = item.getTaxAgency();
						}
						columnValue = agency != null ? agency.getName() : "";
						break;
					case 2:
						columnValue = item.getDescription() != null ? item
								.getDescription() : "";
						break;
					case 3:
						if (item.isPercentage())
							columnValue = item.getTaxRate() + "%";
						else
							columnValue = amountAsStringWithCurrency(
									item.getTaxRate(), item.getCompany()
											.getPrimaryCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<TAXItem> csvExporter = new CSVExporter<TAXItem>(
					icsvExportRunner);
			return csvExporter.export(taxItems);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getTaxCodesListExportCsv(String viewType) {
		List<TAXCode> taxCodes = new ArrayList<TAXCode>();
		Session session = HibernateUtil.getCurrentSession();
		List<TAXCode> list = session.getNamedQuery("getTaxcodes")
				.setParameter("companyId", getCompanyId()).list();
		try {
			for (TAXCode taxCode : list) {
				if (viewType.equalsIgnoreCase(messages.active())) {
					if (taxCode.isActive() == true) {
						taxCodes.add(taxCode);
					}
				} else {
					if (taxCode.isActive() == false) {
						taxCodes.add(taxCode);
					}
				}
			}
			ICSVExportRunner<TAXCode> icsvExportRunner = new ICSVExportRunner<TAXCode>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.code(),
							messages.description(), };
				}

				@Override
				public String getColumnValue(TAXCode taxCode, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = taxCode.getName() != null ? taxCode
								.getName() : "";
						break;
					case 1:
						columnValue = taxCode.getDescription() != null ? taxCode
								.getDescription() : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<TAXCode> csvExporter = new CSVExporter<TAXCode>(
					icsvExportRunner);
			return csvExporter.export(taxCodes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getSalesPersonsListExportCsv(String viewType) {
		ArrayList<SalesPerson> salesPersons = new ArrayList<SalesPerson>();
		Session session = HibernateUtil.getCurrentSession();
		List<SalesPerson> list = session.getNamedQuery("getSalesPersons")
				.setParameter("companyId", getCompanyId()).list();
		for (SalesPerson salesPerson : list) {
			if (viewType.equalsIgnoreCase(messages.active())) {
				if (salesPerson.isActive() == true) {
					salesPersons.add(salesPerson);
				}
			} else {
				if (salesPerson.isActive() == false) {
					salesPersons.add(salesPerson);
				}
			}
		}
		ICSVExportRunner<SalesPerson> icsvExportRunner = new ICSVExportRunner<SalesPerson>() {

			@Override
			public String[] getColumns() {
				return new String[] { messages.salesPerson(),
						messages.address(), messages.city(), messages.state(),
						messages.zipCode(), messages.phone(), messages.fax() };
			}

			@Override
			public String getColumnValue(SalesPerson salesPerson, int index) {
				String columnValue = null;
				switch (index) {
				case 0:
					columnValue = salesPerson.getFirstName();
					break;
				case 1:
					columnValue = salesPerson.getAddress() != null ? salesPerson
							.getAddress().getAddress1() : "";
					break;
				case 2:
					columnValue = salesPerson.getAddress() != null ? salesPerson
							.getAddress().getCity() : "";
					break;
				case 3:
					columnValue = salesPerson.getAddress() != null ? salesPerson
							.getAddress().getStateOrProvinence() : "";
					break;

				case 4:
					columnValue = salesPerson.getAddress() != null ? salesPerson
							.getAddress().getZipOrPostalCode() : "";
					break;
				case 5:
					columnValue = salesPerson.getPhoneNo();
					break;
				case 6:
					columnValue = salesPerson.getFaxNo();
					break;
				}
				columnValue = '"' + columnValue + '"';
				return columnValue;
			}
		};
		CSVExporter<SalesPerson> csvExporter = new CSVExporter<SalesPerson>(
				icsvExportRunner);
		try {
			return csvExporter.export(salesPersons);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getCustomerTransactionsListExportCsv(
			final ClientCustomer customer, int transactionType,
			int transactionStatusType, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		PaginationList<TransactionHistory> resultList = new PaginationList<TransactionHistory>();

		try {
			resultList = getFinanceTool().getCustomerManager()
					.getResultListbyType(customer.getID(), transactionType,
							transactionStatusType, dates[0].getDate(),
							dates[1].getDate(), getCompanyId(), 0, -1);
			ICSVExportRunner<TransactionHistory> icsvExportRunner = new ICSVExportRunner<TransactionHistory>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.date(), messages.type(),
							messages.no(), messages.memo(), messages.dueDate(),
							messages.amount() };
				}

				@Override
				public String getColumnValue(TransactionHistory obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDate())) : "";
						break;
					case 1:
						columnValue = obj.getType() != 0 ? Utility
								.getTransactionName(obj.getType()) : "";
						break;
					case 2:
						columnValue = obj.getNumber() != null ? obj.getNumber()
								: "";
						break;
					case 3:
						columnValue = obj.getMemo() != null ? obj.getMemo()
								: "";
						break;
					case 4:
						columnValue = obj.getDueDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDueDate())) : "";
						break;
					case 5:
						columnValue = amountAsStringWithCurrency(
								obj.getAmount(), customer.getCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<TransactionHistory> csvExporter = new CSVExporter<TransactionHistory>(
					icsvExportRunner);
			return csvExporter.export(resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getVendorTransactionsListExportCsv(final ClientVendor vendor,
			int transactionType, int transactionStatusType,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		ArrayList<TransactionHistory> resultList = new ArrayList<TransactionHistory>();

		try {

			resultList = getFinanceTool().getVendorManager()
					.getResultListbyType(vendor.getID(), transactionType,
							transactionStatusType, dates[0].getDate(),
							dates[1].getDate(), getCompanyId(), 0, -1);
			ICSVExportRunner<TransactionHistory> icsvExportRunner = new ICSVExportRunner<TransactionHistory>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.date(), messages.type(),
							messages.no(), messages.memo(), messages.dueDate(),
							messages.amount(), messages.account() };
				}

				@Override
				public String getColumnValue(TransactionHistory obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDate())) : "";
						break;
					case 1:
						columnValue = obj.getType() != 0 ? Utility
								.getTransactionName(obj.getType()) : "";
						break;
					case 2:
						columnValue = obj.getNumber() != null ? obj.getNumber()
								: "";
						break;
					case 3:
						columnValue = obj.getMemo() != null ? obj.getMemo()
								: "";
						break;
					case 4:
						columnValue = obj.getDueDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDueDate())) : "";
						break;
					case 5:
						columnValue = amountAsStringWithCurrency(
								obj.getAmount(), vendor.getCurrency());
						break;
					case 6:
						Account account = (Account) HibernateUtil
								.getCurrentSession().get(Account.class,
										obj.getAccType());

						columnValue = account != null ? account.getName() : "";
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<TransactionHistory> csvExporter = new CSVExporter<TransactionHistory>(
					icsvExportRunner);
			return csvExporter.export(resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param amount
	 * @param currencyID
	 * @param company
	 * @return
	 */
	public String amountAsStringWithCurrency(double amount, long currencyID) {
		Company company = (Company) HibernateUtil.getCurrentSession().get(
				Company.class, getCompanyId());
		Currency currency = (Currency) HibernateUtil.getCurrentSession().get(
				Currency.class, currencyID);
		if (currency == null) {
			currency = company.getPrimaryCurrency();
		}
		return amountAsStringWithCurrency(amount, currency);
	}

	/**
	 * 
	 * @param amount
	 * @param currency
	 * @return
	 */
	public String amountAsStringWithCurrency(Double amount, Currency currency) {
		return getAmountAsStringInCurrency(amount, currency.getSymbol());
	}

	/**
	 * 
	 * @param amount
	 * @param currencySymbol
	 * @return
	 */
	public String getAmountAsStringInCurrency(double amount,
			String currencySymbol) {
		return Global.get().toCurrencyFormat(amount, currencySymbol);
	}

	public String getDepositsAndTransfersListExportCsv(long fromDate,
			long toDate, final int type, final int transactionType) {
		PaginationList<DepositsTransfersList> depositsAndTransfersList = null;
		try {
			if (transactionType == 0) {
				depositsAndTransfersList = getFinanceTool().getCompanyManager()
						.getDepositsList(getCompanyId(), fromDate, toDate, 0,
								-1, type);
			} else {
				depositsAndTransfersList = getFinanceTool().getCompanyManager()
						.getTransfersList(getCompanyId(), fromDate, toDate, 0,
								-1, type);
			}
			ICSVExportRunner<DepositsTransfersList> icsvExportRunner = new ICSVExportRunner<DepositsTransfersList>() {

				@Override
				public String[] getColumns() {
					if (transactionType == 0) {
						return new String[] { messages.transactionDate(),
								messages.type(), messages.number(),
								messages.depositTo(), messages.amount() };
					} else {
						return new String[] { messages.transactionDate(),
								messages.type(), messages.number(),
								messages.transferTo(), messages.transferFrom(),
								messages.amount() };
					}
				}

				@Override
				public String getColumnValue(DepositsTransfersList obj,
						int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = obj.getTransactionDate() != null ? Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getTransactionDate())) : "";
						break;
					case 1:
						columnValue = Utility.getTransactionName(obj.getType());
						break;
					case 2:
						columnValue = obj.getTransactionNumber();
						break;
					case 3:
						columnValue = obj.getInAccount() != null ? obj
								.getInAccount() : "";
						break;
					case 4:
						if (transactionType == 0) {
							columnValue = amountAsStringWithCurrency(
									obj.getAmount(), obj.getCurrency());
						} else {
							columnValue = obj.getFromAccount() != null ? obj
									.getFromAccount() : "";
						}
						break;
					case 5:
						if (transactionType != 0) {
							columnValue = amountAsStringWithCurrency(
									obj.getAmount(), obj.getCurrency());
						}
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<DepositsTransfersList> csvExporter = new CSVExporter<DepositsTransfersList>(
					icsvExportRunner);
			return csvExporter.export(depositsAndTransfersList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Based on the selected list, it will call the corresponding method
	 */
	@Override
	public String getExportListCsv(long startDate, long endDate,
			int transactionType, int viewId, String selectedItem) {

		if (selectedItem.equalsIgnoreCase(messages.invoices())) {

			return getInvoiceListExportCsv(startDate, endDate, transactionType,
					viewId);
		} else if (selectedItem.equalsIgnoreCase(messages.customerchecks())) {

			return getPayeeChecksExportCsv(transactionType, startDate, endDate,
					viewId);

		} else if (selectedItem.equalsIgnoreCase(messages.vendorchecks())) {
			return getPayeeChecksExportCsv(transactionType, startDate, endDate,
					viewId);
		} else if (selectedItem
				.equalsIgnoreCase(messages.customerCreditNotes())) {
			return getInvoiceListExportCsv(startDate, endDate, transactionType,
					viewId);
		} else if (selectedItem.equalsIgnoreCase(messages.cashSales())) {
			return getInvoiceListExportCsv(startDate, endDate, transactionType,
					viewId);
		} else if (selectedItem.equalsIgnoreCase(messages.receivedPayment())) {
			return getReceivePaymentsListExportCsv(startDate, endDate,
					transactionType, viewId);
		} else if (selectedItem.equals(messages.payeePayments(Global.get()
				.Customers()))) {
			return getReceivePaymentsListExportCsv(startDate, endDate,
					transactionType, viewId);
		} else if (selectedItem.equalsIgnoreCase(messages
				.customerRefunds(Global.get().Customer()))) {

			return getCustomerRefundsListExportCsv(startDate, endDate, viewId);

		} else if (selectedItem.equalsIgnoreCase(messages.receivedPayments())) {
			return getReceivePaymentsListExportCsv(startDate, endDate,
					transactionType, viewId);
		} else if (selectedItem.equalsIgnoreCase(messages.billCredits())) {

			return getBillsAndItemReceiptListExportCsv(false, transactionType,
					startDate, endDate, viewId);

		} else if (selectedItem.equalsIgnoreCase(messages.billPayments())) {
			return getVendorPaymentsListExportCsv(startDate, endDate,
					transactionType);

		} else if (selectedItem.equalsIgnoreCase(messages.bills())) {

			return getBillsAndItemReceiptListExportCsv(false, 0, startDate,
					endDate, viewId);
		} else if (selectedItem.equalsIgnoreCase(messages.creditCardExpenses())) {
			return getBillsAndItemReceiptListExportCsv(false, transactionType,
					startDate, endDate, viewId);

		} else if (selectedItem.equalsIgnoreCase(messages.cashExpenses())) {

			return getBillsAndItemReceiptListExportCsv(true, transactionType,
					startDate, endDate, viewId);

		} else if (selectedItem.equalsIgnoreCase(messages
				.inventoryAdjustments())) {
			return getStockAdjustmentsExportCsv(startDate, endDate);

		} else if (selectedItem.equalsIgnoreCase(messages.deposits())) {
			return getDepositsAndTransfersListExportCsv(startDate, endDate,
					viewId, transactionType);

		} else if (selectedItem.equalsIgnoreCase(messages.transferFunds())) {
			return getDepositsAndTransfersListExportCsv(startDate, endDate,
					viewId, transactionType);

		} else if (selectedItem.equalsIgnoreCase(messages.payments())) {
			return getPaymentsListExportCsv(startDate, endDate, viewId);
		} else if (selectedItem.equalsIgnoreCase(messages.journalEntries())) {

			return getJournalEntriesExportCsv(startDate, endDate);

		} else if (selectedItem.equalsIgnoreCase(messages.otherChecks())) {

			return getPayeeChecksExportCsv(transactionType, startDate, endDate,
					viewId);

		}
		return null;
	}

	@Override
	public String getPurchaseOrderExportCsv(int type, long startDate,
			long endDate) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			ArrayList<PurchaseOrdersList> purchaseOrders = getFinanceTool()
					.getPurchageManager()
					.getPurchaseOrdersList(getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), type, 0, -1);
			ICSVExportRunner<PurchaseOrdersList> icsvExportRunner = new ICSVExportRunner<PurchaseOrdersList>() {

				@Override
				public String[] getColumns() {
					return new String[] {
							messages.date(),
							messages.number(),
							Global.get().messages()
									.payeeName(Global.get().Vendor()),
							messages.purchasePrice() };
				}

				@Override
				public String getColumnValue(PurchaseOrdersList obj, int index) {
					switch (index) {
					case 0:
						return Utility.getDateInSelectedFormat(new FinanceDate(
								obj.getDate().getDate()));
					case 1:
						return obj.getNumber();
					case 2:
						return obj.getVendorName();
					case 3:
						return amountAsStringWithCurrency(
								obj.getPurchasePrice(), obj.getCurrency());
					default:
						break;
					}
					return null;
				}
			};
			CSVExporter<PurchaseOrdersList> csvExporter = new CSVExporter<PurchaseOrdersList>(
					icsvExportRunner);
			return csvExporter.export(purchaseOrders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getBillsAndItemReceiptListExportCSV(boolean isExpensesList,
			int transactionType, long fromDate, long toDate, int start,
			int pageSize, int checkViewType) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			PaginationList<BillsList> billsList = getFinanceTool()
					.getVendorManager().getBillsList(isExpensesList,
							getCompanyId(), transactionType,
							dates[0].getDate(), dates[1].getDate(), start,
							pageSize, checkViewType);
			ICSVExportRunner<BillsList> icsvExportRunner = new ICSVExportRunner<BillsList>() {

				@Override
				public String[] getColumns() {

					return new String[] {
							messages.type(),
							messages.date(),
							messages.no(),
							Global.get().messages()
									.payeeName(Global.get().Vendor()),
							messages.originalAmount(), messages.balance() };
				}

				@Override
				public String getColumnValue(BillsList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility.getTransactionName(obj.getType());
						break;
					case 1:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDate()));
						break;
					case 2:
						columnValue = obj.getNumber() != null ? obj.getNumber()
								: "";
						break;
					case 3:
						columnValue = obj.getVendorName() != null ? obj
								.getVendorName() : "";
						break;
					case 4:
						columnValue = obj.getOriginalAmount() != 0 ? amountAsStringWithCurrency(
								obj.getOriginalAmount(), obj.getCurrency())
								: "";
						break;
					case 5:
						if (obj.getBalance() != null) {
							columnValue = obj.getBalance() != 0 ? amountAsStringWithCurrency(
									obj.getBalance(), obj.getCurrency()) : "";
						} else {
							columnValue = amountAsStringWithCurrency(0,
									obj.getCurrency());
						}
						break;

					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<BillsList> csvExporter = new CSVExporter<BillsList>(
					icsvExportRunner);
			return csvExporter.export(billsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAccounterRegister(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long accountId, int start, int length) {
		PaginationList<AccountRegister> accountRegisterList = new PaginationList<AccountRegister>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());
		try {
			accountRegisterList = getFinanceTool().getAccountRegister(
					financeDates[0], financeDates[1], accountId,
					getCompanyId(), start, length);
			if (!accountRegisterList.isEmpty()) {
				accountRegisterList.remove(0);
			}
			ICSVExportRunner<AccountRegister> icsvExportRunner = new ICSVExportRunner<AccountRegister>() {
				double balance = 0;
				double totalBalance = 0;

				@Override
				public String[] getColumns() {
					return new String[] { messages.date(), messages.type(),
							messages.docNo(), messages.increase(),
							messages.reduce(), messages.Account(),
							messages.memo(), messages.ClosingBalance() };
				}

				@Override
				public String getColumnValue(AccountRegister accRegister,
						int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(
										accRegister.getDate()));
						break;
					case 1:
						columnValue = Utility.getTransactionName((accRegister
								.getType()));
						break;
					case 2:
						columnValue = accRegister.getNumber();
						break;
					case 3:
						if (DecimalUtil.isGreaterThan(accRegister.getAmount(),
								0.0))
							columnValue = amountAsStringWithCurrency(
									accRegister.getAmount(),
									accRegister.getCurrency());

						else
							columnValue = amountAsStringWithCurrency(0.0,
									accRegister.getCurrency());
						break;
					case 4:
						if (DecimalUtil
								.isLessThan(accRegister.getAmount(), 0.0))
							columnValue = amountAsStringWithCurrency(-1
									* accRegister.getAmount(),
									accRegister.getCurrency());
						else
							columnValue = amountAsStringWithCurrency(0.0,
									accRegister.getCurrency());
						break;
					case 5:
						columnValue = accRegister.getAccount();
						break;
					case 6:
						columnValue = accRegister.getMemo() != null ? accRegister
								.getMemo() : "";
						break;
					case 7:
						double d = accRegister.getAmount();

						if (DecimalUtil.isLessThan(d, 0.0)) {
							d = -1 * d;
							balance = balance - d;
						} else {
							balance = balance + d;
						}
						totalBalance += balance;

						columnValue = amountAsStringWithCurrency(balance,
								accRegister.getCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<AccountRegister> csvExporter = new CSVExporter<AccountRegister>(
					icsvExportRunner);
			return csvExporter.export(accountRegisterList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getTaxAdjustmentsList(int viewId, long startDate,
			long endDate, int start, int length) {
		PaginationList<ClientTAXAdjustment> taxAdjustmentsList = new PaginationList<ClientTAXAdjustment>();

		taxAdjustmentsList = new FinanceTool().getTaxManager()
				.getTaxAdjustments(viewId, getCompanyId(), startDate, endDate,
						start, length);
		try {
			ICSVExportRunner<ClientTAXAdjustment> icsvExportRunner = new ICSVExportRunner<ClientTAXAdjustment>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.date(),
							messages.taxAgency(), messages.taxItem(),
							messages.adjustmentAccount(), messages.total() };
				}

				@Override
				public String getColumnValue(ClientTAXAdjustment obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getDate()));
						break;
					case 1:
						TAXAgency taxAgency = (TAXAgency) HibernateUtil
								.getCurrentSession().get(TAXAgency.class,
										obj.getTaxAgency());
						columnValue = taxAgency != null ? taxAgency.getName()
								: "";
						break;
					case 2:
						TAXItem taxItem = (TAXItem) HibernateUtil
								.getCurrentSession().get(TAXItem.class,
										obj.getTaxItem());
						columnValue = taxItem != null ? taxItem.getName() : "";
						break;
					case 3:
						Account account = (Account) HibernateUtil
								.getCurrentSession().get(Account.class,
										obj.getAdjustmentAccount());
						columnValue = account != null ? account.getName() : "";
						break;
					case 4:
						columnValue = amountAsStringWithCurrency(
								obj.getTotal(), obj.getCurrency());
						break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<ClientTAXAdjustment> csvExporter = new CSVExporter<ClientTAXAdjustment>(
					icsvExportRunner);
			return csvExporter.export(taxAdjustmentsList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getPayRunExportCsv(long fromDate, long toDate, int type,
			final int transactionType) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(fromDate), new ClientFinanceDate(
							toDate), getCompanyId());
			final Company company = getFinanceTool().getCompany(getCompanyId());
			PaginationList<PaymentsList> payeeChecks = getFinanceTool()
					.getPayrollManager().getPayRunsList(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), type, 0,
							-1, transactionType);
			ICSVExportRunner<PaymentsList> icsvExportRunner = new ICSVExportRunner<PaymentsList>() {

				@Override
				public String[] getColumns() {
					if (transactionType == ClientTransaction.TYPE_PAY_EMPLOYEE) {
						return new String[] { messages.date(),
								messages.number(), messages.name(),
								messages.account(), messages.total() };
					}
					return new String[] { messages.date(), messages.number(),
							messages.name(), messages.total() };
				}

				@Override
				public String getColumnValue(PaymentsList obj, int index) {
					String columnValue = null;
					switch (index) {
					case 0:
						columnValue = Utility
								.getDateInSelectedFormat(new FinanceDate(obj
										.getPaymentDate()));
						break;
					case 1:
						columnValue = obj.getPaymentNumber();
						break;
					case 2:
						columnValue = obj.getName();
						break;
					case 3:
						if (transactionType == ClientTransaction.TYPE_PAY_EMPLOYEE) {
							columnValue = obj.getInFavourOf();
						} else {
							columnValue = amountAsStringWithCurrency(
									obj.getAmountPaid(),
									company.getPrimaryCurrency());
							columnValue = '"' + columnValue + '"';
						}
						break;
					case 4:
						columnValue = amountAsStringWithCurrency(
								obj.getAmountPaid(),
								company.getPrimaryCurrency());
						columnValue = '"' + columnValue + '"';
						break;
					}

					return columnValue;
				}
			};
			CSVExporter<PaymentsList> csvExporter = new CSVExporter<PaymentsList>(
					icsvExportRunner);
			return csvExporter.export(payeeChecks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getFileTaxesExportCsv(long startDate, long endDate, int type) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			final Company company = getFinanceTool().getCompany(getCompanyId());
			PaginationList<ClientTAXReturn> taxReturns = getFinanceTool()
					.getTaxManager()
					.getTaxReturns(getCompanyId(), dates[0].getDate(),
							dates[1].getDate(), 0, -1, type);
			ICSVExportRunner<ClientTAXReturn> icsvExportRunner = new ICSVExportRunner<ClientTAXReturn>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.taxAgency(),
							messages.periodStartDate(),
							messages.periodEndDate(), messages.taxFiledDate(),
							messages.taxAmount(), messages.totalPaymentMade() };
				}

				@Override
				public String getColumnValue(ClientTAXReturn obj, int index) {
					switch (index) {
					case 0:
						TAXAgency taxAgency = (TAXAgency) HibernateUtil
								.getCurrentSession().get(TAXAgency.class,
										obj.getTaxAgency());
						return taxAgency != null ? taxAgency.getName() : "";
					case 1:
						return new ClientFinanceDate(obj.getPeriodStartDate())
								.toString();
					case 2:
						return new ClientFinanceDate(obj.getPeriodEndDate())
								.toString();
					case 3:
						return new ClientFinanceDate(obj.getTransactionDate())
								.toString();
					case 4:
						return amountAsStringWithCurrency(
								obj.getTotalTAXAmount(),
								company.getPrimaryCurrency());
					case 5:
						return amountAsStringWithCurrency(
								(obj.getTotalTAXAmount() - obj.getBalance()),
								company.getPrimaryCurrency());
					default:
						break;
					}
					return null;
				}
			};
			CSVExporter<ClientTAXReturn> csvExporter = new CSVExporter<ClientTAXReturn>(
					icsvExportRunner);
			return csvExporter.export(taxReturns);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String getTaxRefundsExportCsv(long startDate, long endDate,
			int viewId) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			final Company company = getFinanceTool().getCompany(getCompanyId());
			PaginationList<ClientReceiveVAT> taxRefunds = getFinanceTool()
					.getTaxManager().getTaxRefunds(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), 0, -1,
							viewId);
			ICSVExportRunner<ClientReceiveVAT> icsvExportRunner = new ICSVExportRunner<ClientReceiveVAT>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.taxAgency(),
							messages.paymentMethod(), messages.checkNumber(),
							messages.transactionDate(), messages.amountPaid() };
				}

				@Override
				public String getColumnValue(ClientReceiveVAT obj, int index) {
					switch (index) {
					case 0:
						TAXAgency taxAgency = (TAXAgency) HibernateUtil
								.getCurrentSession().get(TAXAgency.class,
										obj.getVatAgency());
						return taxAgency != null ? taxAgency.getName() : "";
					case 1:
						return obj.getPaymentMethod();
					case 2:
						return obj.getCheckNumber();
					case 3:
						return new ClientFinanceDate(obj.getTransactionDate())
								.toString();
					case 4:
						return amountAsStringWithCurrency(obj.getTotal(),
								company.getPrimaryCurrency());
					default:
						break;
					}
					return null;
				}
			};
			CSVExporter<ClientReceiveVAT> csvExporter = new CSVExporter<ClientReceiveVAT>(
					icsvExportRunner);
			return csvExporter.export(taxRefunds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String getPayTaxesExportCsv(long startDate, long endDate, int viewId) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			final Company company = getFinanceTool().getCompany(getCompanyId());
			PaginationList<ClientPayTAX> payTaxes = getFinanceTool()
					.getTaxManager().getPayTaxList(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), 0, -1,
							viewId);
			ICSVExportRunner<ClientPayTAX> icsvExportRunner = new ICSVExportRunner<ClientPayTAX>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.taxAgency(),
							messages.paymentMethod(), messages.checkNumber(),
							messages.transactionDate(), messages.amountPaid() };
				}

				@Override
				public String getColumnValue(ClientPayTAX obj, int index) {
					switch (index) {
					case 0:
						TAXAgency taxAgency = (TAXAgency) HibernateUtil
								.getCurrentSession().get(TAXAgency.class,
										obj.getTaxAgency());
						return taxAgency != null ? taxAgency.getName() : "";
					case 1:
						return obj.getPaymentMethod();
					case 2:
						return obj.getCheckNumber();
					case 3:
						return new ClientFinanceDate(obj.getTransactionDate())
								.toString();
					case 4:
						return amountAsStringWithCurrency(obj.getTotal(),
								company.getPrimaryCurrency());
					default:
						break;
					}
					return null;
				}
			};
			CSVExporter<ClientPayTAX> csvExporter = new CSVExporter<ClientPayTAX>(
					icsvExportRunner);
			return csvExporter.export(payTaxes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String getBuildAssembliesExportCsv(long startDate, long endDate,
			int viewId) {
		try {
			FinanceDate[] dates = getMinimumAndMaximumDates(
					new ClientFinanceDate(startDate), new ClientFinanceDate(
							endDate), getCompanyId());
			PaginationList<ClientBuildAssembly> buildAssemblies = getFinanceTool()
					.getInventoryManager().getBuildAssembly(getCompanyId(),
							dates[0].getDate(), dates[1].getDate(), 0, -1,
							viewId);
			ICSVExportRunner<ClientBuildAssembly> icsvExportRunner = new ICSVExportRunner<ClientBuildAssembly>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.assemblyItem(),
							messages.description(), messages.buildQuantity(),
							messages.quantityOnHand(), messages.reorderPoint() };
				}

				@Override
				public String getColumnValue(ClientBuildAssembly obj, int index) {
					Item item = (Item) HibernateUtil.getCurrentSession().get(
							Item.class, obj.getInventoryAssembly());
					switch (index) {
					case 0:
						return item.getName();
					case 1:
						return obj.getMemo() == null ? "" : obj.getMemo();
					case 2:
						return obj.getQuantityToBuild().toString() == null ? ""
								: obj.getQuantityToBuild().toString();
					case 3:
						return item != null ? String.valueOf(item
								.getOnhandQty()) : "";
					case 4:
						return item != null ? String.valueOf(item
								.getReorderPoint()) : "";
					default:
						break;
					}
					return null;
				}
			};
			CSVExporter<ClientBuildAssembly> csvExporter = new CSVExporter<ClientBuildAssembly>(
					icsvExportRunner);
			return csvExporter.export(buildAssemblies);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String exportSavedPaypalTransactions(ClientAccount account) {
		try {
			PaginationList<ClientPaypalTransation> savedPaypalTransactions = getFinanceTool()
					.getSavedPaypalTransactions(account, getCompanyId());
			ICSVExportRunner<ClientPaypalTransation> icsvExportRunner = new ICSVExportRunner<ClientPaypalTransation>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.date(), messages.timezone(),
							messages.type(), messages.email(), messages.name(),
							messages.state(), messages.grossAmount(),
							messages.paypalCharge(), messages.netAmount() };
				}

				@Override
				public String getColumnValue(ClientPaypalTransation obj,
						int index) {
					switch (index) {
					case 0:
						return obj.getDate();
					case 1:
						return obj.getTimeZone();
					case 2:
						return obj.getType();
					case 3:
						return obj.getEmail();
					case 4:
						return obj.getName();
					case 5:
						return obj.getTransactionStatus();
					case 6:
						return obj.getGrossAmount();
					case 7:
						return obj.getPaypalFees();
					case 8:
						return obj.getNetAmount();
					default:
						break;
					}
					return null;
				}

			};
			CSVExporter<ClientPaypalTransation> csvExporter = new CSVExporter<ClientPaypalTransation>(
					icsvExportRunner);
			return csvExporter.export(savedPaypalTransactions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> generateETDSFillingtext(ETDsFilingData etDsFilingData) {
		try {
			return new FinanceTool().getPrintManager().generateETDSFillingtext(
					etDsFilingData, getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> generateFrom16APDF(long vendorID, String datesRange,
			String place, String printDate, String tdsCertificateNumber,
			int type) {
		return new FinanceTool().getPrintManager().generateFrom16APDF(
				getCompanyId(), vendorID, datesRange, type,
				tdsCertificateNumber, place, printDate);
	}

	@Override
	public String getConsultantTransactionsListExportCsv(
			final ConsultantsDetailsList customer, int transactionType,
			int transactionStatusType, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		PaginationList<TransactionHistory> resultList = new PaginationList<TransactionHistory>();

		try {
			resultList = (PaginationList<TransactionHistory>) getFinanceTool().getCustomerManager()
					.getConsultantResultListbyType(customer.getId(), transactionType,
							transactionStatusType, dates[0].getDate(),
							dates[1].getDate(), getCompanyId(), 0, -1);
			ICSVExportRunner<TransactionHistory> icsvExportRunner = new ICSVExportRunner<TransactionHistory>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.date(), messages.type(),
							messages.no(), messages.comments(), messages.dueDate(),
							messages.amount() };
				}

				@Override
				public String getColumnValue(TransactionHistory obj, int index) {
					String columnValue = null;
					switch (index) {
						case 0:
							columnValue = obj.getDate() != null ? Utility
									.getDateInSelectedFormat(new FinanceDate(obj
											.getDate())) : "";
							break;
						case 1:
							columnValue = obj.getType() != 0 ? Utility
									.getTransactionName(obj.getType()) : "";
							break;
						case 2:
							columnValue = obj.getNumber() != null ? obj.getNumber()
									: "";
							break;
						case 3:
							columnValue = obj.getMemo() != null ? obj.getMemo()
									: "";
							break;
						case 4:
							columnValue = obj.getDueDate() != null ? Utility
									.getDateInSelectedFormat(new FinanceDate(obj
											.getDueDate())) : "";
							break;
						case 5:
							columnValue = obj.getAmount() +"";
							break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			PaginationList<TransactionHistory> resultList1 = new PaginationList<TransactionHistory>();
			ICSVExportRunner<TransactionHistory> icsvExportRunnerConsultant = new ICSVExportRunner<TransactionHistory>() {

				@Override
				public String[] getColumns() {
					return new String[] { messages.name(), messages.email(),
							messages.phone(), messages.address(), messages.balance(),
							messages.client() };
				}

				@Override
				public String getColumnValue(TransactionHistory obj, int index) {
					String columnValue = null;
					switch (index) {
						case 0:
							columnValue = customer.getName() != null ? customer.getName() : "";
							break;
						case 1:
							columnValue = customer.getEmail() != null ? customer.getEmail() : "";
							break;
						case 2:
							columnValue = customer.getPhone() != null ? customer.getPhone() : "";
							break;
						case 3:
							columnValue = customer.getAddresss() != null ? customer.getAddresss() : "";
							break;
						case 4:
							columnValue = customer.getBalance() + "";
							break;
						case 5:
							columnValue = customer.getClientName() != null ? customer.getClientName() : "";
							break;
					}
					columnValue = '"' + columnValue + '"';
					return columnValue;
				}
			};
			CSVExporter<TransactionHistory> csvExporter = new CSVExporter<TransactionHistory>(
					icsvExportRunner);
			resultList1.addAll(resultList);

			return csvExporter.export(resultList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
