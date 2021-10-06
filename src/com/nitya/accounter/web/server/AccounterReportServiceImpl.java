package com.nitya.accounter.web.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.Utility;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.IAccounterReportService;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientBudget;
import com.nitya.accounter.web.client.core.ClientBudgetItem;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.ReportInput;
import com.nitya.accounter.web.client.core.Lists.DummyDebitor;
import com.nitya.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.nitya.accounter.web.client.core.Lists.PayeeStatementsList;
import com.nitya.accounter.web.client.core.reports.AccountRegister;
import com.nitya.accounter.web.client.core.reports.AgedDebtors;
import com.nitya.accounter.web.client.core.reports.AmountsDueToVendor;
import com.nitya.accounter.web.client.core.reports.BankCheckDetail;
import com.nitya.accounter.web.client.core.reports.BankDepositDetail;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.BudgetActuals;
import com.nitya.accounter.web.client.core.reports.ClientBudgetList;
import com.nitya.accounter.web.client.core.reports.DepositDetail;
import com.nitya.accounter.web.client.core.reports.DepreciationShedule;
import com.nitya.accounter.web.client.core.reports.ECSalesList;
import com.nitya.accounter.web.client.core.reports.ECSalesListDetail;
import com.nitya.accounter.web.client.core.reports.EstimatesByJob;
import com.nitya.accounter.web.client.core.reports.ExpenseList;
import com.nitya.accounter.web.client.core.reports.IncomeByCustomerDetail;
import com.nitya.accounter.web.client.core.reports.InventoryDetails;
import com.nitya.accounter.web.client.core.reports.InventoryStockStatusDetail;
import com.nitya.accounter.web.client.core.reports.InventoryValutionDetail;
import com.nitya.accounter.web.client.core.reports.InventoryValutionSummary;
import com.nitya.accounter.web.client.core.reports.ItemActualCostDetail;
import com.nitya.accounter.web.client.core.reports.JobActualCostDetail;
import com.nitya.accounter.web.client.core.reports.JobProfitability;
import com.nitya.accounter.web.client.core.reports.JobProfitabilityDetailByJob;
import com.nitya.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.nitya.accounter.web.client.core.reports.MostProfitableCustomers;
import com.nitya.accounter.web.client.core.reports.PayHeadDetails;
import com.nitya.accounter.web.client.core.reports.PayHeadSummary;
import com.nitya.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.nitya.accounter.web.client.core.reports.RealisedExchangeLossOrGain;
import com.nitya.accounter.web.client.core.reports.ReconcilationItemList;
import com.nitya.accounter.web.client.core.reports.Reconciliation;
import com.nitya.accounter.web.client.core.reports.ReconciliationDiscrepancy;
import com.nitya.accounter.web.client.core.reports.ReverseChargeList;
import com.nitya.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.core.reports.SalesByLocationDetails;
import com.nitya.accounter.web.client.core.reports.SalesByLocationSummary;
import com.nitya.accounter.web.client.core.reports.SalesTaxLiability;
import com.nitya.accounter.web.client.core.reports.TDSAcknowledgmentsReport;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.core.reports.UnRealisedLossOrGain;
import com.nitya.accounter.web.client.core.reports.UnbilledCostsByJob;
import com.nitya.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.nitya.accounter.web.client.core.reports.VATDetail;
import com.nitya.accounter.web.client.core.reports.VATItemDetail;
import com.nitya.accounter.web.client.core.reports.VATItemSummary;
import com.nitya.accounter.web.client.core.reports.VATSummary;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.reports.CheckDetailReport;
import com.nitya.accounter.web.client.ui.reports.CurrencyExchangeRate;
import com.nitya.accounter.web.client.ui.reports.TAXItemDetail;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.managers.ExportManager;

public class AccounterReportServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterReportService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public AccounterReportServiceImpl() {
		super();

	}

	private BaseReport setStartEndDates(BaseReport obj,
			FinanceDate[] financeDates) {
		// if (Utility.stringToDate(new
		// ClientFinanceDate(transtartDate).toString()) != null)
		obj.setStartDate(financeDates[0].toClientFinanceDate());
		// if (Utility.stringToDate(new
		// ClientFinanceDate(transtartDate).toString()) != null)
		obj.setEndDate(financeDates[1].toClientFinanceDate());
		return obj;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesByCustomerSummary(startDate, endDate, getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> salesByCustomerSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {

			salesByCustomerDetailList = getFinanceTool().getSalesManager()
					.getSalesByCustomerSummary(financeDates[0],
							financeDates[1], companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	// @Override
	// public List<AccountBalance> getAccountBalances() {
	// List<AccountBalance> accountBalanceList = null;
	//
	// try {
	//
	// IAccounterReportDAOService dao =
	// dbService.getAccounterReportDAOService();
	//
	// accountBalanceList =
	// getFinanceTool().getAccountBalances(getThreadLocalCompanyId());
	//
	// accountBalanceList = (List<AccountBalance>)
	// manager.merge(accountBalanceList);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return accountBalanceList;
	// }

	@Override
	public PaginationList<AccountRegister> getAccountRegister(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long accountId, int start, int length) {
		PaginationList<AccountRegister> accountRegisterList = new PaginationList<AccountRegister>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			accountRegisterList = getFinanceTool().getAccountRegister(
					financeDates[0], financeDates[1], accountId,
					getCompanyId(), start, length);

			// if (accountRegisterList != null)
			// accountRegisterList.add((AccountRegister) setStartEndDates(obj,
			// financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return accountRegisterList;
	}

	@Override
	public ArrayList<AgedDebtors> getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return agedCreditorsReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<AgedDebtors> agedCreditorsReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<AgedDebtors> agedDebtorsList = new ArrayList<AgedDebtors>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			agedDebtorsList = getFinanceTool().getReportManager()
					.getAgedCreditors(financeDates[0], financeDates[1],
							companyId);
			List<AgedDebtors> debtors = new ArrayList<AgedDebtors>();
			for (AgedDebtors debtor : agedDebtorsList) {
				debtors = updateDebtorListByName(debtors, debtor);
			}

			Collections.sort(debtors, new Comparator<AgedDebtors>() {

				@Override
				public int compare(AgedDebtors o1, AgedDebtors o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			agedDebtorsList.addAll(debtors);

			AgedDebtors obj = new AgedDebtors();
			if (agedDebtorsList != null)
				agedDebtorsList.add((AgedDebtors) setStartEndDates(obj,
						financeDates));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedDebtorsList;
	}

	@Override
	public ArrayList<AgedDebtors> getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return agedDebtorsReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<AgedDebtors> agedDebtorsReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<AgedDebtors> agedDebtorsList = new ArrayList<AgedDebtors>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			agedDebtorsList = getFinanceTool()
					.getReportManager()
					.getAgedDebtors(financeDates[0], financeDates[1], companyId);
			List<AgedDebtors> debtors = new ArrayList<AgedDebtors>();
			for (AgedDebtors debtor : agedDebtorsList) {
				debtors = updateDebtorListByName(debtors, debtor);
			}

			Collections.sort(debtors, new Comparator<AgedDebtors>() {

				@Override
				public int compare(AgedDebtors o1, AgedDebtors o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			agedDebtorsList.addAll(debtors);

			AgedDebtors obj = new AgedDebtors();
			if (agedDebtorsList != null)
				agedDebtorsList.add((AgedDebtors) (setStartEndDates(obj,
						financeDates)));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedDebtorsList;
	}

	private AgedDebtors createdAgedDebitor(AgedDebtors debtor) {

		AgedDebtors agedDebtors = new AgedDebtors();

		agedDebtors.setName(debtor.getName());
		agedDebtors.setContact(debtor.getContact());
		// agedDebtors.setPhone(debtor.getPhone());
		// agedDebtors.setType(debtor.getType());
		// agedDebtors.setDate(debtor.getDate());
		// agedDebtors.setNumber(debtor.getNumber());
		// agedDebtors.setReference(debtor.getReference());
		// agedDebtors.setDueDate(debtor.getDueDate());
		// agedDebtors.setPaymentTermName(debtor.getPaymentTermName());
		agedDebtors.setAmount(debtor.getAmount());
		agedDebtors.setTotal(debtor.getTotal());
		// agedDebtors.setIsVoid(debtor.getIsVoid());
		agedDebtors.setTransactionId(debtor.getTransactionId());
		return agedDebtors;
	}

	private List<AgedDebtors> updateDebtorListByName(List<AgedDebtors> debtors,
			AgedDebtors agedDebtors) {
		if (debtors == null) {
			debtors = new ArrayList<AgedDebtors>();
		}

		boolean isExist = false;
		for (AgedDebtors debtor : debtors) {
			if (debtor.getName().equals(agedDebtors.getName())) {
				debtor.setTotal(debtor.getTotal() + agedDebtors.getTotal());
				isExist = true;
				break;
			}
		}
		if (debtors.isEmpty() || !isExist) {
			AgedDebtors debtor = createdAgedDebitor(agedDebtors);
			debtor.setCategory(5);
			debtors.add(debtor);
		}
		return debtors;

	}

	@Override
	public ArrayList<AmountsDueToVendor> getAmountsDueToVendor(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<AmountsDueToVendor> amountsDueToVendorList = new ArrayList<AmountsDueToVendor>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			amountsDueToVendorList = getFinanceTool().getVendorManager()
					.getAmountsDueToVendor(financeDates[0], financeDates[1],
							getCompanyId());

			AmountsDueToVendor obj = new AmountsDueToVendor();
			if (amountsDueToVendorList != null)
				amountsDueToVendorList
						.add((AmountsDueToVendor) setStartEndDates(obj,
								financeDates));

			// amountsDueToVendorList = (List<AmountsDueToVendor>) manager
			// .merge(amountsDueToVendorList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return amountsDueToVendorList;
	}

	@Override
	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return customerTransactionHistory(startDate, endDate, getCompanyId());
	}

	private ArrayList<TransactionHistory> customerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TransactionHistory> transactionHistoryList = new ArrayList<TransactionHistory>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transactionHistoryList = getFinanceTool().getCustomerManager()
					.getCustomerTransactionHistory(financeDates[0],
							financeDates[1], companyId);

			TransactionHistory obj = new TransactionHistory();
			if (transactionHistoryList != null)
				transactionHistoryList
						.add((TransactionHistory) setStartEndDates(obj,
								financeDates));

			// transactionHistoryList = (List<TransactionHistory>) manager
			// .merge(transactionHistoryList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionHistoryList;

	}

	@Override
	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<MostProfitableCustomers> mostProfitableCustomersList = new ArrayList<MostProfitableCustomers>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			mostProfitableCustomersList = getFinanceTool().getCustomerManager()
					.getMostProfitableCustomers(financeDates[0],
							financeDates[1], getCompanyId());

			MostProfitableCustomers obj = new MostProfitableCustomers();
			if (mostProfitableCustomersList != null)
				mostProfitableCustomersList
						.add((MostProfitableCustomers) setStartEndDates(obj,
								financeDates));

			// mostProfitableCustomersList = (List<MostProfitableCustomers>)
			// manager
			// .merge(mostProfitableCustomersList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mostProfitableCustomersList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomerDetailList = getFinanceTool().getPurchageManager()
					.getPurchasesByItemDetail(financeDates[0], financeDates[1],
							getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomerDetailList = getFinanceTool().getPurchageManager()
					.getPurchasesByItemSummary(financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return purchasesByVendorDetail(startDate, endDate, getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> purchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			salesByCustomerDetailList = getFinanceTool().getVendorManager()
					.getPurchasesByVendorDetail(financeDates[0],
							financeDates[1], companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;

	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomerDetailList = getFinanceTool().getVendorManager()
					.getPurchasesByVendorSummary(financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			salesByCustomerDetailList
					.add((SalesByCustomerDetail) setStartEndDates(obj,
							financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<ClientTransaction> getRegister(long accountId) {
		ArrayList<ClientTransaction> clientTransactionList = new ArrayList<ClientTransaction>();

		try {

			// serverTransactionList =
			// getFinanceTool().getRegister(getFinanceTool()
			// .getAccounterDAOService().getAccount(
			// accountId));
			// for (Transaction transaction : serverTransactionList) {
			// clientTransactionList.add(Util
			// .toClientObject(transaction, ClientTransaction.class));
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientTransactionList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesByCustomerDetailReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> salesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			salesByCustomerDetailList = getFinanceTool().getReportManager()
					.getSalesByCustomerDetailReport(financeDates[0],
							financeDates[1], companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;

	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomerDetailList = getFinanceTool().getSalesManager()
					.getSalesByItemDetail(financeDates[0], financeDates[1],
							getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesByItemSummary(startDate, endDate, getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> salesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			salesByCustomerDetailList = getFinanceTool().getSalesManager()
					.getSalesByItemSummary(financeDates[0], financeDates[1],
							companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return transactionDetailByTaxItem(startDate, endDate, getCompanyId());
	}

	private ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItemList = new ArrayList<TransactionDetailByTaxItem>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transactionDetailByTaxItemList = getFinanceTool()
					.getReportManager().getTransactionDetailByTaxItem(
							financeDates[0], financeDates[1], companyId);

			TransactionDetailByTaxItem obj = new TransactionDetailByTaxItem();
			if (transactionDetailByTaxItemList != null)
				transactionDetailByTaxItemList
						.add((TransactionDetailByTaxItem) setStartEndDates(obj,
								financeDates));

			// transactionDetailByTaxcodeList =
			// (List<TransactionDetailByTaxcode>) manager
			// .merge(transactionDetailByTaxcodeList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionDetailByTaxItemList;

	}

	@Override
	public ArrayList<TrialBalance> getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return trialBalanceReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<TrialBalance> trialBalanceReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TrialBalance> trialBalanceList = new ArrayList<TrialBalance>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			trialBalanceList = getFinanceTool().getReportManager()
					.getTrialBalance(financeDates[0], financeDates[1],
							companyId);

			TrialBalance obj = new TrialBalance();
			if (trialBalanceList != null)
				trialBalanceList.add((TrialBalance) setStartEndDates(obj,
						financeDates));

			// trialBalanceList = (List<TrialBalance>) manager
			// .merge(trialBalanceList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialBalanceList;

	}

	@Override
	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return vendorTransactionHistory(startDate, endDate, getCompanyId());
	}

	private ArrayList<TransactionHistory> vendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TransactionHistory> transactionHistoryList = new ArrayList<TransactionHistory>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transactionHistoryList = getFinanceTool().getVendorManager()
					.getVendorTransactionHistory(financeDates[0],
							financeDates[1], companyId);

			TransactionHistory obj = new TransactionHistory();
			if (transactionHistoryList != null)
				transactionHistoryList
						.add((TransactionHistory) setStartEndDates(obj,
								financeDates));

			// transactionHistoryList = (List<TransactionHistory>) manager
			// .merge(transactionHistoryList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionHistoryList;

	}

	@Override
	public ArrayList<ClientItem> getPurchaseReportItems(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<ClientItem> clientItemList = new ArrayList<ClientItem>();
		List<Item> serverItemsList = new ArrayList<Item>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			serverItemsList = getFinanceTool().getPurchageManager()
					.getPurchaseReportItems(financeDates[0], financeDates[1],
							getCompanyId());

			ClientItem obj = new ClientItem();
			if (clientItemList != null)
				clientItemList.add((ClientItem) setStartEndDates(obj,
						financeDates));

			for (Item item : serverItemsList) {
				clientItemList.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItemList;
	}

	@Override
	public ArrayList<ClientItem> getSalesReportItems(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<ClientItem> clientItemList = new ArrayList<ClientItem>();
		List<Item> serverItemsList = new ArrayList<Item>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			serverItemsList = getFinanceTool().getSalesManager()
					.getSalesReportItems(financeDates[0], financeDates[1],
							getCompanyId());

			ClientItem obj = new ClientItem();
			if (clientItemList != null)
				clientItemList.add((ClientItem) setStartEndDates(obj,
						financeDates));

			for (Item item : serverItemsList) {
				clientItemList.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItemList;
	}

	@Override
	public ArrayList<ClientCustomer> getTransactionHistoryCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<ClientCustomer> clientCustomerList = new ArrayList<ClientCustomer>();
		List<Customer> serverCustomerList = new ArrayList<Customer>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			serverCustomerList = getFinanceTool().getCustomerManager()
					.getTransactionHistoryCustomers(financeDates[0],
							financeDates[1], getCompanyId());

			for (Customer customer : serverCustomerList) {
				clientCustomerList.add(new ClientConvertUtil().toClientObject(
						customer, ClientCustomer.class));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCustomerList;
	}

	@Override
	public ArrayList<ClientVendor> getTransactionHistoryVendors(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<ClientVendor> clientVendorList = new ArrayList<ClientVendor>();
		List<Vendor> serverVendorList = new ArrayList<Vendor>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			serverVendorList = getFinanceTool().getVendorManager()
					.getTransactionHistoryVendors(financeDates[0],
							financeDates[1], getCompanyId());
			for (Vendor vendor : serverVendorList) {
				clientVendorList.add(new ClientConvertUtil().toClientObject(
						vendor, ClientVendor.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientVendorList;
	}

	@Override
	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ArrayList<SalesTaxLiability> salesTaxLiabilityList = new ArrayList<SalesTaxLiability>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesTaxLiabilityList = getFinanceTool().getReportManager()
					.getSalesTaxLiabilityReport(financeDates[0],
							financeDates[1], getCompanyId());

			SalesTaxLiability obj = new SalesTaxLiability();
			if (salesTaxLiabilityList != null)
				salesTaxLiabilityList.add((SalesTaxLiability) setStartEndDates(
						obj, financeDates));
			// salesTaxLiabilityList = (List<SalesTaxLiability>) manager
			// .merge(salesTaxLiabilityList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesTaxLiabilityList;
	}

	@Override
	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			long accountId, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return transactionDetailByAccount(accountId, startDate, endDate,
				getCompanyId());
	}

	private ArrayList<TransactionDetailByAccount> transactionDetailByAccount(
			long accountId, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		ArrayList<TransactionDetailByAccount> transDetailByAccountList = new ArrayList<TransactionDetailByAccount>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transDetailByAccountList = getFinanceTool().getReportManager()
					.getTransactionDetailByAccount(accountId, financeDates[0],
							financeDates[1], companyId);

			TransactionDetailByAccount obj = new TransactionDetailByAccount();
			if (transDetailByAccountList != null)
				transDetailByAccountList
						.add((TransactionDetailByAccount) setStartEndDates(obj,
								financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transDetailByAccountList;

	}

	@Override
	public ArrayList<TransactionDetailByAccount> getAutomaticTransactions(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<TransactionDetailByAccount> automaticTransactions = new ArrayList<TransactionDetailByAccount>();

		long companyId = getCompanyId();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			automaticTransactions = getFinanceTool().getReportManager()
					.getAutomaticTransactions(financeDates[0], financeDates[1],
							companyId);

			TransactionDetailByAccount obj = new TransactionDetailByAccount();
			if (automaticTransactions != null)
				automaticTransactions
						.add((TransactionDetailByAccount) setStartEndDates(obj,
								financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return automaticTransactions;

	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomertList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomertList = getFinanceTool().getPurchageManager()
					.getPurchasesByItemDetail(itemName, financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomertList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomertList = getFinanceTool().getVendorManager()
					.getPurchasesByVendorDetail(vendorName, financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return salesByCustomerDetailReport(customerName, startDate, endDate,
				getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> salesByCustomerDetailReport(
			String customerName, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {

		ArrayList<SalesByCustomerDetail> salesByCustomertList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			salesByCustomertList = getFinanceTool().getReportManager()
					.getSalesByCustomerDetailReport(customerName,
							financeDates[0], financeDates[1], companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;

	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomertList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomertList = getFinanceTool().getSalesManager()
					.getSalesByItemDetail(itemName, financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public ArrayList<AgedDebtors> getAgedDebtors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return agedDebtorsReport(Name, startDate, endDate, getCompanyId());
	}

	private ArrayList<AgedDebtors> agedDebtorsReport(String name,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<AgedDebtors> agedDebtorsList = new ArrayList<AgedDebtors>();
		ArrayList<AgedDebtors> agedDebtorsListForCustomer = new ArrayList<AgedDebtors>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			agedDebtorsList = getFinanceTool()
					.getReportManager()
					.getAgedDebtors(financeDates[0], financeDates[1], companyId);
			for (AgedDebtors agdDebitor : agedDebtorsList) {
				if (name.equals(agdDebitor.getName()))
					agedDebtorsListForCustomer.add(agdDebitor);
			}
			AgedDebtors obj = new AgedDebtors();
			if (agedDebtorsListForCustomer != null)
				agedDebtorsListForCustomer.add((AgedDebtors) (setStartEndDates(
						obj, financeDates)));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedDebtorsListForCustomer;

	}

	@Override
	public ArrayList<AgedDebtors> getAgedCreditors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return agedCreditorsReport(Name, startDate, endDate, getCompanyId());
	}

	private ArrayList<AgedDebtors> agedCreditorsReport(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<AgedDebtors> agedDebtorsList = new ArrayList<AgedDebtors>();
		ArrayList<AgedDebtors> agedCreditorsListForCustomer = new ArrayList<AgedDebtors>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			agedDebtorsList = getFinanceTool().getReportManager()
					.getAgedCreditors(financeDates[0], financeDates[1],
							companyId);
			for (AgedDebtors agdDebitor : agedDebtorsList) {
				if (Name.equals(agdDebitor.getName()))
					agedCreditorsListForCustomer.add(agdDebitor);
			}
			AgedDebtors obj = new AgedDebtors();
			if (agedCreditorsListForCustomer != null)
				agedCreditorsListForCustomer
						.add((AgedDebtors) (setStartEndDates(obj, financeDates)));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedCreditorsListForCustomer;

	}

	private ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate(
			long companyId) {
		ArrayList<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = getFinanceTool().getManager()
					.getMinimumAndMaximumTransactionDate(companyId);
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDates;
	}

	@Override
	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate() {
		return getMinimumAndMaximumTransactionDate(getCompanyId());
	}

	@Override
	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final ClientFinanceDate startDate,
			final ClientFinanceDate endDate) {
		return transactionDetailByTaxItem(taxItemName, startDate, endDate,
				getCompanyId());
	}

	private ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItem(
			String taxItemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItemList = new ArrayList<TransactionDetailByTaxItem>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transactionDetailByTaxItemList = getFinanceTool()
					.getReportManager().getTransactionDetailByTaxItem(
							taxItemName, financeDates[0], financeDates[1],
							companyId);

			TransactionDetailByTaxItem obj = new TransactionDetailByTaxItem();
			if (transactionDetailByTaxItemList != null)
				transactionDetailByTaxItemList
						.add((TransactionDetailByTaxItem) setStartEndDates(obj,
								financeDates));

			// transactionDetailByTaxcodeList =
			// (List<TransactionDetailByTaxcode>) manager
			// .merge(transactionDetailByTaxcodeList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionDetailByTaxItemList;
	}

	@Override
	public ArrayList<TrialBalance> getBalanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return balanceSheetReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<TrialBalance> balanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<TrialBalance> trialbalanceList = new ArrayList<TrialBalance>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			trialbalanceList = getFinanceTool().getReportManager()
					.getBalanceSheetReport(financeDates[0], financeDates[1],
							companyId);

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj,
						financeDates));

			if (trialbalanceList.size() == 1) {
				if (trialbalanceList.get(0).getAccountName()
						.equals(Global.get().messages().netIncome())
						&& DecimalUtil.isEquals(trialbalanceList.get(0)
								.getAmount(), 0)) {
					trialbalanceList.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialbalanceList;
	}

	public ArrayList<TrialBalance> getCashFlowReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TrialBalance> trialbalanceList = new ArrayList<TrialBalance>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			trialbalanceList = getFinanceTool().getReportManager()
					.getCashFlowReport(financeDates[0], financeDates[1],
							companyId);

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialbalanceList;

	}

	@Override
	public ArrayList<TrialBalance> getCashFlowReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return getCashFlowReport(startDate, endDate, getCompanyId());
	}

	@Override
	public ArrayList<ProfitAndLossByLocation> getProfitAndLossByLocationReport(
			int categoryType, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		ArrayList<ProfitAndLossByLocation> profitAndLossByLocationList = new ArrayList<ProfitAndLossByLocation>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {
			profitAndLossByLocationList = getFinanceTool().getReportManager()
					.getProfitAndLossByLocation(categoryType, financeDates[0],
							financeDates[1], getCompanyId());
			ProfitAndLossByLocation obj = new ProfitAndLossByLocation();
			if (profitAndLossByLocationList != null)
				profitAndLossByLocationList
						.add((ProfitAndLossByLocation) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return profitAndLossByLocationList;
	}

	// public ArrayList<ClientReconciliation> getReconcilationByBankIdReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long accountId) {
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, getCompanyId());
	// ArrayList<ClientReconciliation> reconciliationsByBankAccountIDList = new
	// ArrayList<ClientReconciliation>();
	// try {
	// reconciliationsByBankAccountIDList = getFinanceTool()
	// .getReportManager().getReconciliationsByBankAccountID(
	// accountId, getCompanyId());
	//
	// ClientReconciliation obj = new ClientReconciliation();
	//
	// if (reconciliationsByBankAccountIDList != null)
	// reconciliationsByBankAccountList
	// .add((ClientReconciliation) setStartEndDates(obj,
	// financeDates));
	// } catch (AccounterException e) {
	// e.printStackTrace();
	// }
	// return null;
	//
	// }

	@Override
	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsReport(
			boolean isLocation, boolean isCustomer,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesByLocationDetailsReport(isLocation, isCustomer, startDate,
				endDate, getCompanyId());
	}

	private ArrayList<SalesByLocationDetails> salesByLocationDetailsReport(
			boolean isLocation, boolean isCustomer,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<SalesByLocationDetails> salesByLocationDetailList = new ArrayList<SalesByLocationDetails>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			salesByLocationDetailList = getFinanceTool().getSalesManager()
					.getSalesByLocationDetail(isLocation, isCustomer,
							financeDates[0], financeDates[1], companyId);
			SalesByLocationDetails obj = new SalesByLocationDetails();
			if (salesByLocationDetailList != null)
				salesByLocationDetailList
						.add((SalesByLocationDetails) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByLocationDetailList;

	}

	@Override
	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsForLocation(
			boolean isLocation, boolean isCustomer, String locationName,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return getSalesByLocationDetailsForLocation(isLocation, isCustomer,
				locationName, startDate, endDate, getCompanyId());
	}

	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsForLocation(
			boolean isLocation, boolean isCustomer, String locationName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<SalesByLocationDetails> salesByLocationDetailList = new ArrayList<SalesByLocationDetails>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			salesByLocationDetailList = getFinanceTool().getSalesManager()
					.getSalesByLocationDetailForLocation(isLocation,
							isCustomer, locationName, financeDates[0],
							financeDates[1], companyId);
			SalesByLocationDetails obj = new SalesByLocationDetails();
			if (salesByLocationDetailList != null)
				salesByLocationDetailList
						.add((SalesByLocationDetails) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByLocationDetailList;
	}

	@Override
	public ArrayList<SalesByLocationSummary> getSalesByLocationSummaryReport(
			boolean isLocation, boolean isCustomer,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesByLocationSummary(isLocation, isCustomer, startDate,
				endDate, getCompanyId());
	}

	private ArrayList<SalesByLocationSummary> salesByLocationSummary(
			boolean isLocation, boolean isCustomer,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<SalesByLocationSummary> salesByLocationDetailList = new ArrayList<SalesByLocationSummary>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			salesByLocationDetailList = getFinanceTool().getSalesManager()
					.getSalesByLocationSummary(isLocation, isCustomer,
							financeDates[0], financeDates[1], companyId);
			SalesByLocationSummary obj = new SalesByLocationSummary();
			if (salesByLocationDetailList != null)
				salesByLocationDetailList
						.add((SalesByLocationSummary) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByLocationDetailList;

	}

	@Override
	public ArrayList<TrialBalance> getProfitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return profitAndLossReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<TrialBalance> profitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<TrialBalance> trialbalanceList = new ArrayList<TrialBalance>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			trialbalanceList = getFinanceTool().getReportManager()
					.getProfitAndLossReport(financeDates[0], financeDates[1],
							companyId);

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialbalanceList;
	}

	// @Override
	// public ArrayList<OpenAndClosedOrders> getPurchaseOpenOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate) {
	// return purchaseOpenOrderReport(startDate, endDate, getCompanyId());
	// }

	// private ArrayList<OpenAndClosedOrders> purchaseOpenOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	//
	// ArrayList<OpenAndClosedOrders> purchaseOrders = new
	// ArrayList<OpenAndClosedOrders>();
	//
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, companyId);
	//
	// try {
	// purchaseOrders = getFinanceTool().getPurchageManager()
	// .getOpenPurchaseOrders(financeDates[0], financeDates[1],
	// companyId);
	//
	// OpenAndClosedOrders obj = new OpenAndClosedOrders();
	// if (purchaseOrders != null)
	// purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
	// financeDates));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return purchaseOrders;
	//
	// }

	// @Override
	// public ArrayList<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate) {
	// return purchaseCompletedOrderReport(startDate, endDate, getCompanyId());
	// }

	// private ArrayList<OpenAndClosedOrders> purchaseCompletedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	//
	// ArrayList<OpenAndClosedOrders> purchaseOrders = new
	// ArrayList<OpenAndClosedOrders>();
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, companyId);
	// try {
	// purchaseOrders = getFinanceTool().getPurchageManager()
	// .getCompletedPurchaseOrders(financeDates[0],
	// financeDates[1], companyId);
	// OpenAndClosedOrders obj = new OpenAndClosedOrders();
	// if (purchaseOrders != null)
	// purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
	// financeDates));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return purchaseOrders;
	//
	// }

	// @Override
	// public ArrayList<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate) {
	// return purchaseCancelledOrderReport(startDate, endDate, getCompanyId());
	// }

	// private ArrayList<OpenAndClosedOrders> purchaseCancelledOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	// ArrayList<OpenAndClosedOrders> purchaseOrders = new
	// ArrayList<OpenAndClosedOrders>();
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, companyId);
	// try {
	// purchaseOrders = getFinanceTool().getPurchageManager()
	// .getCanceledPurchaseOrders(financeDates[0],
	// financeDates[1], companyId);
	// OpenAndClosedOrders obj = new OpenAndClosedOrders();
	// if (purchaseOrders != null)
	// purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
	// financeDates));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return purchaseOrders;
	// }

	@Override
	public ArrayList<OpenAndClosedOrders> getPurchaseOrderReport(int type,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return purchaseOrderReport(type, startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> purchaseOrderReport(int type,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<OpenAndClosedOrders> purchaseOrders = new ArrayList<OpenAndClosedOrders>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			purchaseOrders = getFinanceTool().getPurchageManager()
					.getPurchaseOrders(type, financeDates[0], financeDates[1],
							companyId);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;

	}

	// @Override
	// public ArrayList<OpenAndClosedOrders> getPurchaseClosedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate) {
	// ArrayList<OpenAndClosedOrders> purchaseOrders = new
	// ArrayList<OpenAndClosedOrders>();
	//
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, getCompanyId());
	//
	// try {
	// purchaseOrders = getFinanceTool().getPurchageManager()
	// .getClosedPurchaseOrders(financeDates[0], financeDates[1],
	// getCompanyId());
	//
	// OpenAndClosedOrders obj = new OpenAndClosedOrders();
	// if (purchaseOrders != null)
	// purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
	// financeDates));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return purchaseOrders;
	// }

	// @Override
	// public ArrayList<OpenAndClosedOrders> getSalesOpenOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate) {
	// return salesOpenOrderReport(startDate, endDate, getCompanyId());
	// }

	// private ArrayList<OpenAndClosedOrders> salesOpenOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	//
	// ArrayList<OpenAndClosedOrders> salesOrders = new
	// ArrayList<OpenAndClosedOrders>();
	//
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, companyId);
	//
	// try {
	// salesOrders = getFinanceTool().getSalesManager()
	// .getOpenSalesOrders(financeDates[0], financeDates[1],
	// companyId);
	//
	// OpenAndClosedOrders obj = new OpenAndClosedOrders();
	// if (salesOrders != null)
	// salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
	// financeDates));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return salesOrders;
	//
	// }

	// @Override
	// public ArrayList<OpenAndClosedOrders> getSalesCompletedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate) {
	// return salesCompletedOrderReport(startDate, endDate, getCompanyId());
	// }
	// private ArrayList<OpenAndClosedOrders> salesCompletedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	//
	// ArrayList<OpenAndClosedOrders> salesOrders = new
	// ArrayList<OpenAndClosedOrders>();
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, companyId);
	// try {
	// salesOrders = getFinanceTool().getSalesManager()
	// .getCompletedSalesOrders(financeDates[0], financeDates[1],
	// companyId);
	// OpenAndClosedOrders obj = new OpenAndClosedOrders();
	// if (salesOrders != null)
	// salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
	// financeDates));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return salesOrders;
	//
	// }

	@Override
	public ArrayList<OpenAndClosedOrders> getSalesOrderReport(int type,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesOrderReport(type, startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> salesOrderReport(int type,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<OpenAndClosedOrders> salesOrders = new ArrayList<OpenAndClosedOrders>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			salesOrders = getFinanceTool().getSalesManager().getSalesOrders(
					type, financeDates[0], financeDates[1], companyId);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;
	}

	// @Override
	// public ArrayList<OpenAndClosedOrders> getSalesCancelledOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate) {
	// return salesCancelledReport(startDate, endDate, getCompanyId());
	// }

	// private ArrayList<OpenAndClosedOrders> salesCancelledReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	// ArrayList<OpenAndClosedOrders> salesOrders = new
	// ArrayList<OpenAndClosedOrders>();
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, companyId);
	// try {
	// salesOrders = getFinanceTool().getSalesManager()
	// .getCanceledSalesOrders(financeDates[0], financeDates[1],
	// companyId);
	// OpenAndClosedOrders obj = new OpenAndClosedOrders();
	// if (salesOrders != null)
	// salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
	// financeDates));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return salesOrders;
	// }

	// @Override
	// public ArrayList<OpenAndClosedOrders> getSalesClosedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate) {
	// ArrayList<OpenAndClosedOrders> salesOrders = new
	// ArrayList<OpenAndClosedOrders>();
	//
	// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
	// endDate, getCompanyId());
	//
	// try {
	// salesOrders = getFinanceTool().getSalesManager()
	// .getClosedSalesOrders(financeDates[0], financeDates[1],
	// getCompanyId());
	//
	// OpenAndClosedOrders obj = new OpenAndClosedOrders();
	// if (salesOrders != null)
	// salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
	// financeDates));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return salesOrders;
	// }

	//
	// public List<OpenAndClosedOrders> getSalesOpenOrderReportByStatus(
	// int status) {
	// List<OpenAndClosedOrders> salesOpenOrders = null;
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return salesOpenOrders;
	// }

	@Override
	public ArrayList<VATDetail> getPriorVATReturnVATDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<VATDetail> vatDetailReport = new ArrayList<VATDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());
		// financeDates[0] = "";
		// financeDates[1] = endDate;

		try {
			vatDetailReport = getFinanceTool().getReportManager()
					.getVATDetailReport(financeDates[0], financeDates[1],
							getCompanyId());

			VATDetail obj = new VATDetail();
			if (vatDetailReport != null)
				vatDetailReport.add((VATDetail) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetailReport;
	}

	@Override
	public ArrayList<VATDetail> getPriorVATReturnReport(long taxAgency,
			ClientFinanceDate endDate) {
		ArrayList<VATDetail> vatDetailReport = new ArrayList<VATDetail>();

		// FinanceDate[] financeDates = getMinimumAndMaximumDates("", endDate);
		FinanceDate transtartDate = new FinanceDate();
		FinanceDate tranendDate = new FinanceDate();
		transtartDate.clear();
		tranendDate = new FinanceDate(endDate);

		try {
			TAXAgency vatAgncy = (TAXAgency) loadObjectById("TAXAgency",
					taxAgency);
			;
			vatDetailReport = getFinanceTool().getReportManager()
					.getPriorVATReturnVATDetailReport(vatAgncy, tranendDate,
							getCompanyId());

			VATDetail obj = new VATDetail();
			if (vatDetailReport != null)
				vatDetailReport.add((VATDetail) setStartEndDates(obj,
						new FinanceDate[] { transtartDate, tranendDate }));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetailReport;
	}

	@Override
	public ArrayList<VATSummary> getPriorReturnVATSummary(long taxAgncy,
			ClientFinanceDate endDate) {
		return priorReturnVATSummary(taxAgncy, endDate, getCompanyId());
	}

	private ArrayList<VATSummary> priorReturnVATSummary(long taxAgncy,
			ClientFinanceDate endDate, long companyId) {

		ArrayList<VATSummary> vatSummaryList = new ArrayList<VATSummary>();

		try {
			TAXAgency vatAgency = (TAXAgency) loadObjectById(
					AccounterCoreType.TAXAGENCY
							.getServerClassFullyQualifiedName(),
					taxAgncy);
			vatSummaryList = getFinanceTool().getTaxManager()
					.getPriorReturnVATSummary(vatAgency,
							new FinanceDate(endDate), companyId);

			VATSummary obj = new VATSummary();
			if (vatSummaryList != null)
				vatSummaryList.add((VATSummary) setStartEndDates(obj,
						new FinanceDate[] { new FinanceDate(),
								new FinanceDate() }));

			/*
			 * Removing Box 3 and Box 5 from list, as the calculations for box 3
			 * and box 5 are done in gui
			 */
			vatSummaryList.remove(2);
			vatSummaryList.remove(3);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaryList;

	}

	@Override
	public ArrayList<VATSummary> getVAT100Report(long taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		return vat100Report(taxAgency, fromDate, toDate, getCompanyId());
	}

	private ArrayList<VATSummary> vat100Report(long taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate, long companyId) {

		ArrayList<VATSummary> vatSummaryList = new ArrayList<VATSummary>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, companyId);

		try {
			TAXAgency vatAgency = (TAXAgency) loadObjectById(
					AccounterCoreType.TAXAGENCY
							.getServerClassFullyQualifiedName(),
					taxAgency);
			vatSummaryList = getFinanceTool().getReportManager()
					.getVAT100Report(vatAgency, financeDates[0],
							financeDates[1], companyId);

			VATSummary obj = new VATSummary();
			if (vatSummaryList != null)
				vatSummaryList.add((VATSummary) setStartEndDates(obj,
						financeDates));

			/*
			 * //* Removing Box 3 and Box 5 from list, as the calculations for
			 * box 3 and box 5 are done in gui
			 *///
				// vatSummaryList.remove(2);
				// vatSummaryList.remove(3);
			double box1 = vatSummaryList.get(0).getValue();
			double box2 = vatSummaryList.get(1).getValue();
			vatSummaryList.get(2).setValue(box1 + box2);

			double box3 = vatSummaryList.get(2).getValue();
			double box4 = vatSummaryList.get(3).getValue();
			vatSummaryList.get(4).setValue(box3 - box4);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaryList;

	}

	@Override
	public ArrayList<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		ArrayList<UncategorisedAmountsReport> uncategories = new ArrayList<UncategorisedAmountsReport>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			uncategories = getFinanceTool().getReportManager()
					.getUncategorisedAmountsReport(financeDates[0],
							financeDates[1], getCompanyId());

			UncategorisedAmountsReport obj = new UncategorisedAmountsReport();
			if (uncategories != null)
				uncategories.add((UncategorisedAmountsReport) setStartEndDates(
						obj, financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uncategories;
	}

	@Override
	public ArrayList<VATItemSummary> getVATItemSummaryReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		ArrayList<VATItemSummary> vatItems = new ArrayList<VATItemSummary>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			vatItems = getFinanceTool().getReportManager()
					.getVATItemSummaryReport(financeDates[0], financeDates[1],
							getCompanyId());

			VATItemSummary obj = new VATItemSummary();
			if (vatItems != null)
				vatItems.add((VATItemSummary) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatItems;
	}

	@Override
	public ArrayList<VATItemDetail> getVATItemDetailReport(String vatItemName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		ArrayList<VATItemDetail> itemsList = new ArrayList<VATItemDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			itemsList = getFinanceTool().getReportManager()
					.getVATItemDetailReport(vatItemName, financeDates[0],
							financeDates[1], getCompanyId());

			VATItemDetail obj = new VATItemDetail();
			if (itemsList != null)
				itemsList.add((VATItemDetail) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemsList;

	}

	@Override
	public ArrayList<ECSalesList> getECSalesListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		ArrayList<ECSalesList> salesList = new ArrayList<ECSalesList>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			FinanceTool tool = getFinanceTool();
			salesList = tool.getReportManager().getECSalesListReport(
					financeDates[0], financeDates[1],
					tool.getCompany(getCompanyId()));

			ECSalesList obj = new ECSalesList();
			if (salesList != null)
				salesList
						.add((ECSalesList) setStartEndDates(obj, financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public ArrayList<ECSalesList> getECSalesListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate, long companyId) {

		ArrayList<ECSalesList> salesList = new ArrayList<ECSalesList>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, companyId);

		try {
			FinanceTool tool = getFinanceTool();
			salesList = tool.getReportManager().getECSalesListReport(
					financeDates[0], financeDates[1],
					tool.getCompany(companyId));

			ECSalesList obj = new ECSalesList();
			if (salesList != null)
				salesList
						.add((ECSalesList) setStartEndDates(obj, financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public ArrayList<ECSalesListDetail> getECSalesListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {

		ArrayList<ECSalesListDetail> salesList = new ArrayList<ECSalesListDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			FinanceTool tool = getFinanceTool();
			salesList = tool.getReportManager().getECSalesListDetailReport(
					payeeName, financeDates[0], financeDates[1],
					tool.getCompany(getCompanyId()));

			ECSalesListDetail obj = new ECSalesListDetail();
			if (salesList != null)
				salesList.add((ECSalesListDetail) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public ArrayList<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {

		ArrayList<ReverseChargeListDetail> salesList = new ArrayList<ReverseChargeListDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			salesList = getFinanceTool().getReportManager()
					.getReverseChargeListDetailReport(payeeName,
							financeDates[0], financeDates[1], getCompanyId());

			ReverseChargeListDetail obj = new ReverseChargeListDetail();
			if (salesList != null)
				salesList.add((ReverseChargeListDetail) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public ArrayList<ReverseChargeList> getReverseChargeListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		ArrayList<ReverseChargeList> salesList = new ArrayList<ReverseChargeList>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			salesList = getFinanceTool().getReportManager()
					.getReverseChargeListReport(financeDates[0],
							financeDates[1], getCompanyId());

			ReverseChargeList obj = new ReverseChargeList();
			if (salesList != null)
				salesList.add((ReverseChargeList) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	/**
	 * It returns an Array of dates. array[0] is start date and array[1] is end
	 * date
	 * 
	 * @param startDate
	 * @param endDate
	 */
	private FinanceDate[] getMinimumAndMaximumDates(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		// if ((startDate.equals("") || startDate == null)
		// || (endDate.equals("") || endDate == null)) {

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
	public ArrayList<DummyDebitor> getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException {
		return debtorsReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<DummyDebitor> debtorsReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId)
			throws AccounterException {

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		ArrayList<DummyDebitor> debitors = new ArrayList<DummyDebitor>();
		Calendar calender = Calendar.getInstance();
		// calender.add(Calendar.MONTH, 1);
		// Date oneMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date twoMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date threeMonthPreviousDate = calender.getTime();
		try {
			List<AgedDebtors> agedDebtors = getFinanceTool()
					.getReportManager()
					.getAgedDebtors(financeDates[0], financeDates[1], companyId);

			debitors = getDebtorsWidSameName(agedDebtors, financeDates[0],
					financeDates[1]);

			DummyDebitor obj = new DummyDebitor();
			if (debitors != null)
				debitors.add((DummyDebitor) setStartEndDates(obj, financeDates));

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return debitors;

	}

	/*
	 * This method calculates the totals for individual debtors and prepares
	 * single obj for those debtors who has same name
	 */
	private ArrayList<DummyDebitor> getDebtorsWidSameName(
			List<AgedDebtors> agedDebtors, FinanceDate startdate,
			FinanceDate enddate) {
		ArrayList<DummyDebitor> listDebtors = new ArrayList<DummyDebitor>();
		Map<Integer, List<AgedDebtors>> sameDebtorsMap = new HashMap<Integer, List<AgedDebtors>>();
		List<AgedDebtors> sameDebtors;
		List<AgedDebtors> tempDtrs = new ArrayList<AgedDebtors>();
		String name;
		int listSize = agedDebtors.size();

		/*
		 * seperating(made a list and it to a map wid a key) debtors who has
		 * same name
		 */
		for (int i = 0; !agedDebtors.isEmpty(); i++) {

			sameDebtors = new ArrayList<AgedDebtors>();

			// if (agedDebtors.get(i).getCategory() == 5) {
			// agedDebtors.remove(i);
			// break;
			// }

			/* Initialising list with atleast one obj */
			if (!tempDtrs.contains(agedDebtors.get(0)))
				sameDebtors.add(agedDebtors.get(0));
			name = agedDebtors.get(0).getName();
			agedDebtors.remove(0);

			/* Iterating from next available obj in the list */
			for (AgedDebtors debitorAgedDebtors : agedDebtors) {
				if (name.equals(debitorAgedDebtors.getName())
						&& !tempDtrs.contains(debitorAgedDebtors)) {
					sameDebtors.add(debitorAgedDebtors);
					tempDtrs.add(debitorAgedDebtors);
				}
			}
			/*
			 * changing decision var. with modifed list size.And intialize name
			 * with 1st available obj in the list
			 */
			agedDebtors.removeAll(tempDtrs);
			listSize = agedDebtors.size();
			if (listSize != 0)
				name = agedDebtors.get(0).getName();

			/* add the list to map */
			if (sameDebtors.size() > 0) {
				sameDebtorsMap.put(i, sameDebtors);
			}
		}

		/*
		 * Iterating the map for calculating the totals for the debtors who has
		 * same name
		 */
		for (Map.Entry<Integer, List<AgedDebtors>> sameDtr : sameDebtorsMap
				.entrySet()) {
			List<AgedDebtors> SimilarDtrs = sameDtr.getValue();
			double dayscurrentTotal = 0.0;
			double days30Total = 0.0;
			double days60Total = 0.0;
			double days90Total = 0.0;
			double daysOlderTotal = 0.0;
			long days = 0;
			for (AgedDebtors agdDebtor : SimilarDtrs) {
				// // if (!agdDebtor.getDate().after(enddate)) {
				// days = UIUtils.getDays_between(agdDebtor.getDate()
				// .getDateAsObject(), enddate.getDateAsObject());
				// }

				days = agdDebtor.getAgeing();

				if (days <= 0)
					dayscurrentTotal += agdDebtor.getTotal();
				else if (days > 0 && days <= 30)
					days30Total += agdDebtor.getTotal();
				else if (days > 30 && days <= 60)
					days60Total += agdDebtor.getTotal();
				else if (days > 60 && days <= 90)
					days90Total += agdDebtor.getTotal();
				else if (days > 90)
					daysOlderTotal += agdDebtor.getTotal();
			}
			AgedDebtors dtr = SimilarDtrs.get(0);

			DummyDebitor debitor = new DummyDebitor();
			debitor.setDebitdays_incurrent(dayscurrentTotal);
			debitor.setDebitdays_in30(days30Total);
			debitor.setDebitdays_in60(days60Total);
			debitor.setDebitdays_in90(days90Total);
			debitor.setDebitdays_inolder(daysOlderTotal);
			debitor.setDebitorName(dtr.getName());
			debitor.setDueDate(dtr.getDueDate());
			debitor.setTransactionId(dtr.getTransactionId());
			listDebtors.add(debitor);
		}

		return listDebtors;
	}

	@Override
	public ArrayList<DummyDebitor> getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException {
		return creditorsReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<DummyDebitor> creditorsReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) throws AccounterException {

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		ArrayList<DummyDebitor> Creditors = new ArrayList<DummyDebitor>();
		// Calendar calender = Calendar.getInstance();
		// calender.add(Calendar.MONTH, 1);
		// Date oneMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date twoMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date threeMonthPreviousDate = calender.getTime();
		try {
			List<AgedDebtors> agedCreditors = getFinanceTool()
					.getReportManager().getAgedCreditors(financeDates[0],
							financeDates[1], companyId);

			Creditors = getDebtorsWidSameName(agedCreditors, financeDates[0],
					financeDates[1]);

			DummyDebitor obj = new DummyDebitor();
			if (Creditors != null)
				Creditors
						.add((DummyDebitor) setStartEndDates(obj, financeDates));

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return Creditors;
	}

	@Override
	public ArrayList<ExpenseList> getExpenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return expenseReportByType(status, startDate, endDate, getCompanyId());
	}

	private ArrayList<ExpenseList> expenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<ExpenseList> expenseList = new ArrayList<ExpenseList>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			expenseList = getFinanceTool().getReportManager()
					.getExpenseReportByType(status, financeDates[0],
							financeDates[1], companyId);

			ExpenseList obj = new ExpenseList();

			if (expenseList != null)
				expenseList.add((ExpenseList) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return expenseList;
	}

	private ArrayList<ClientBudgetList> budgetItemsList(long id, long companyId) {

		ArrayList<ClientBudgetList> budgetList = new ArrayList<ClientBudgetList>();

		try {
			ArrayList<ClientBudget> budgets = new ArrayList<ClientBudget>();
			budgets = getFinanceTool().getBudgetList(companyId);

			for (ClientBudget budget : budgets) {
				ArrayList<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();

				if (budget.getID() == id) {
					budgetItems = (ArrayList<ClientBudgetItem>) budget
							.getBudgetItem();

					for (ClientBudgetItem item : budgetItems) {

						if (item.getTotalAmount() > 0.0) {
							HashMap<String, Double> monthsAndAmountMap = new HashMap<String, Double>();
							ClientBudgetList e = new ClientBudgetList();
							e.setAccount(item.getAccount());
							e.setAccountName(item.getAccount().getName());
							e.setJanuaryAmount(item.getJanuaryAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.jan(),
									item.getJanuaryAmount());
							e.setFebrauaryAmount(item.getFebruaryAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.feb(),
									item.getFebruaryAmount());
							e.setMarchAmount(item.getMarchAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.mar(),
									item.getMarchAmount());
							e.setAprilAmount(item.getAprilAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.apr(),
									item.getAprilAmount());
							e.setMayAmount(item.getMayAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.mayS(),
									item.getMayAmount());
							e.setJuneAmount(item.getJuneAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.jun(),
									item.getJuneAmount());
							e.setJulyAmount(item.getJulyAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.jul(),
									item.getJulyAmount());
							e.setAugustAmount(item.getAugustAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.aug(),
									item.getAugustAmount());
							e.setSeptemberAmount(item.getSpetemberAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.sep(),
									item.getSpetemberAmount());
							e.setOctoberAmount(item.getOctoberAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.oct(),
									item.getOctoberAmount());
							e.setNovemberAmount(item.getNovemberAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.nov(),
									item.getNovemberAmount());
							e.setDecemberAmount(item.getDecemberAmount());
							monthsAndAmountMap.put(DayAndMonthUtil.dec(),
									item.getDecemberAmount());
							e.setMonthNamesMap(monthsAndAmountMap);
							e.setTotalAmount(item.getTotalAmount());
							e.setTransactionId(budget.getID());
							e.preparMonthNamesList(Utility
									.getCurrentFiscalYearStartDate(
											new FinanceTool()
													.getCompany(companyId))
									.getMonth());
							budgetList.add(e);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (budgetList.size() == 1) {
			FinanceDate[] financeDates = getMinimumAndMaximumDates(
					new ClientFinanceDate(), new ClientFinanceDate(), companyId);
			ClientBudgetList obj = new ClientBudgetList();
			budgetList.add((ClientBudgetList) setStartEndDates(obj,
					financeDates));
		}

		return budgetList;

	}

	@Override
	public ArrayList<DepositDetail> getDepositDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<DepositDetail> transDetailByAccountList = new ArrayList<DepositDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			transDetailByAccountList = getFinanceTool().getDepositDetail(
					financeDates[0], financeDates[1], getCompanyId());

			DepositDetail obj = new DepositDetail();
			if (transDetailByAccountList != null)
				transDetailByAccountList.add((DepositDetail) setStartEndDates(
						obj, financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transDetailByAccountList;
	}

	@Override
	public ArrayList<CheckDetailReport> getCheckDetailReport(
			long paymentmethod, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

		ArrayList<CheckDetailReport> checkDetailReports = new ArrayList<CheckDetailReport>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			checkDetailReports = getFinanceTool().getReportManager()
					.getCheckDetailReport(paymentmethod, financeDates[0],
							financeDates[1], getCompanyId());

			CheckDetailReport obj = new CheckDetailReport();
			if (checkDetailReports != null)
				checkDetailReports.add((CheckDetailReport) setStartEndDates(
						obj, financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return checkDetailReports;
	}

	@Override
	public ArrayList<PayeeStatementsList> getCustomerStatement(long customer,
			long fromDate, long toDate) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getCustomerManager().getCustomerStatement(customer,
					fromDate, toDate, getCompanyId());
		}
		return null;
	}

	@Override
	public ArrayList<MISC1099TransactionDetail> getMISC1099TransactionDetailReport(
			long vendorId, int boxNo, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {
		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());
		ArrayList<MISC1099TransactionDetail> result = new ArrayList<MISC1099TransactionDetail>();
		try {
			result = getFinanceTool().getVendorManager()
					.getPaybillsByVendorAndBoxNumber(financeDates[0],
							financeDates[1], vendorId, boxNo, getCompanyId());
			MISC1099TransactionDetail obj = new MISC1099TransactionDetail();
			if (result != null)
				result.add((MISC1099TransactionDetail) setStartEndDates(obj,
						financeDates));
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<DummyDebitor> getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId)
			throws AccounterException {
		return debtorsReport(startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedDebtors(String name,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return agedDebtorsReport(name, startDate, endDate, companyId);
	}

	// public ArrayList<OpenAndClosedOrders> getSalesOpenOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	// return salesOpenOrderReport(startDate, endDate, companyId);
	// }

	// public ArrayList<OpenAndClosedOrders> getSalesCompletedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	// return salesCompletedOrderReport(startDate, endDate, companyId);
	// }
	//
	// public ArrayList<OpenAndClosedOrders> getSalesCancelledOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	// return salesCancelledReport(startDate, endDate, companyId);
	// }

	public ArrayList<OpenAndClosedOrders> getSalesOrderReport(int type,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesOrderReport(type, startDate, endDate, companyId);
	}

	public ArrayList<DummyDebitor> getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId)
			throws AccounterException {
		return creditorsReport(startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedCreditors(String name,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return agedCreditorsReport(name, startDate, endDate, companyId);
	}

	// public ArrayList<OpenAndClosedOrders> getPurchaseOpenOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	// return purchaseOpenOrderReport(startDate, endDate, companyId);
	// }
	//
	// public ArrayList<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	// return purchaseCompletedOrderReport(startDate, endDate, companyId);
	// }

	// public ArrayList<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
	// ClientFinanceDate startDate, ClientFinanceDate endDate,
	// long companyId) {
	// return purchaseCancelledOrderReport(startDate, endDate, companyId);
	// }

	public ArrayList<OpenAndClosedOrders> getPurchaseOrderReport(int type,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return purchaseOrderReport(type, startDate, endDate, companyId);
	}

	public ArrayList<VATSummary> getPriorReturnVATSummary(long taxAgncy,
			ClientFinanceDate endDate, long companyId) {
		return priorReturnVATSummary(taxAgncy, endDate, companyId);
	}

	public ArrayList<VATSummary> getVAT100Report(long taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate, long companyId) {
		return vat100Report(taxAgency, fromDate, toDate, companyId);
	}

	public ArrayList<ClientBudgetList> getBudgetItemsList(long id,
			long companyId) {
		return budgetItemsList(id, companyId);
	}

	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsReport(
			boolean isLocation, boolean isCustomer,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesByLocationDetailsReport(isLocation, isCustomer, startDate,
				endDate, companyId);
	}

	public ArrayList<SalesByLocationSummary> getSalesByLocationSummaryReport(
			boolean isLocation, boolean isCustomer,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesByLocationSummary(isLocation, isCustomer, startDate,
				endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return agedCreditorsReport(startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return agedDebtorsReport(startDate, endDate, companyId);
	}

	public ArrayList<ExpenseList> getExpenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return expenseReportByType(status, startDate, endDate, companyId);
	}

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return transactionDetailByTaxItem(startDate, endDate, companyId);
	}

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String status, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return transactionDetailByTaxItem(status, startDate, endDate, companyId);
	}

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			long accountId, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return transactionDetailByAccount(accountId, startDate, endDate,
				companyId);
	}

	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return customerTransactionHistory(startDate, endDate, companyId);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesByCustomerSummary(startDate, endDate, companyId);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesByCustomerDetailReport(startDate, endDate, companyId);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String status, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return salesByCustomerDetailReport(status, startDate, endDate,
				companyId);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesByItemSummary(startDate, endDate, companyId);
	}

	public ArrayList<TrialBalance> getProfitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return profitAndLossReport(startDate, endDate, companyId);
	}

	public ArrayList<TrialBalance> getBalanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return balanceSheetReport(startDate, endDate, companyId);
	}

	public ArrayList<TrialBalance> getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return trialBalanceReport(startDate, endDate, companyId);
	}

	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return vendorTransactionHistory(startDate, endDate, companyId);
	}

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return purchasesByVendorDetail(startDate, endDate, companyId);
	}

	@Override
	public ArrayList<TAXItemDetail> getTAXItemDetailReport(long taxAgency,
			long startDate, long end) throws AccounterException {
		FinanceDate[] financeDates = getMinimumAndMaximumDates(
				new ClientFinanceDate(startDate), new ClientFinanceDate(end),
				getCompanyId());
		ArrayList<TAXItemDetail> taxItemDetailReport = getFinanceTool()
				.getReportManager().getTAXItemDetailReport(getCompanyId(),
						taxAgency, startDate, end);

		TAXItemDetail obj = new TAXItemDetail();
		if (taxItemDetailReport != null)
			taxItemDetailReport.add((TAXItemDetail) setStartEndDates(obj,
					financeDates));
		return taxItemDetailReport;
	}

	@Override
	public ArrayList<VATDetail> getVATExceptionDetailReport(
			ClientFinanceDate start, ClientFinanceDate end, long taxReturnId)
			throws AccounterException {

		return getFinanceTool().getReportManager().getVATExceptionDetailReport(
				getCompanyId(), start, end, taxReturnId);
	}

	@Override
	public ArrayList<TAXItemDetail> getTAXItemExceptionDetailReport(
			long taxAgency, long startDate, long endDate)
			throws AccounterException {

		ArrayList<TAXItemDetail> list = getFinanceTool().getReportManager()
				.getTAXItemExceptionDetailReport(getCompanyId(), taxAgency,
						startDate, endDate);
		FinanceDate[] financeDates = getMinimumAndMaximumDates(
				new ClientFinanceDate(startDate),
				new ClientFinanceDate(endDate), getCompanyId());
		TAXItemDetail obj = new TAXItemDetail();
		if (list != null)
			list.add((TAXItemDetail) setStartEndDates(obj, financeDates));
		return list;

	}

	@Override
	public ArrayList<TDSAcknowledgmentsReport> getTDSAcknowledgments(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<TDSAcknowledgmentsReport> tdsAcks = new ArrayList<TDSAcknowledgmentsReport>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			tdsAcks = getFinanceTool().getReportManager()
					.getTDSAcknowledgments(financeDates[0], financeDates[1],
							getCompanyId());

			TDSAcknowledgmentsReport obj = new TDSAcknowledgmentsReport();
			if (tdsAcks != null)
				tdsAcks.add((TDSAcknowledgmentsReport) setStartEndDates(obj,
						financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tdsAcks;
	}

	@Override
	public ArrayList<ClientBudgetList> getBudgetItemsList(long budgetID) {
		return budgetItemsList(budgetID, getCompanyId());
	}

	@Override
	public ArrayList<PayeeStatementsList> getStatements(boolean isVendor,
			long id, int viewType, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {
		ArrayList<PayeeStatementsList> resultList = new ArrayList<PayeeStatementsList>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());
		try {

			resultList = getFinanceTool().getReportManager()
					.getPayeeStatementsList(isVendor, id, viewType,
							financeDates[0], financeDates[1], getCompanyId());

			PayeeStatementsList obj = new PayeeStatementsList();
			if (resultList != null)
				resultList.add((PayeeStatementsList) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public ArrayList<Reconciliation> getAllReconciliations(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		ArrayList<Reconciliation> reconciliations = new ArrayList<Reconciliation>();
		try {
			reconciliations = getFinanceTool().getReportManager()
					.getAllReconciliationslist(financeDates[0],
							financeDates[1], companyId);
			Reconciliation reconcilationList = new Reconciliation();
			if (reconciliations != null)
				reconciliations.add((Reconciliation) setStartEndDates(
						reconcilationList, financeDates));
			return reconciliations;
		} catch (AccounterException e) {
			return reconciliations;
		}
	}

	@Override
	public ArrayList<DepreciationShedule> getDepreciationSheduleReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate, int status,
			long companyId) {
		ArrayList<DepreciationShedule> list = new ArrayList<DepreciationShedule>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			list = getFinanceTool().getFixedAssetManager()
					.getDepreciationShedule(status, companyId);

			DepreciationShedule depreciationShedule = new DepreciationShedule();
			if (list != null)
				list.add((DepreciationShedule) setStartEndDates(
						depreciationShedule, financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ArrayList<ReconcilationItemList> getReconciliationItemByBankAccountID(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long bankAccountId, long companyId) {
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		ArrayList<ReconcilationItemList> clientReconciliationItems = new ArrayList<ReconcilationItemList>();
		try {
			clientReconciliationItems = getFinanceTool().getReportManager()
					.getReconciliationItemslistByDates(bankAccountId,
							startDate, endDate, companyId);
			ReconcilationItemList reconcilationItemList = new ReconcilationItemList();
			if (clientReconciliationItems != null)
				clientReconciliationItems
						.add((ReconcilationItemList) setStartEndDates(
								reconcilationItemList, financeDates));

			return clientReconciliationItems;
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return new ArrayList<ReconcilationItemList>();
	}

	@Override
	public PaginationList<TransactionHistory> getCustomerTransactionsList(
			long id, int transactionType, int transactionStatusType,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int start,
			int length) {
		return getcustomerTransactionlist(id, transactionType,
				transactionStatusType, startDate, endDate, getCompanyId(),
				start, length);
	}


	private PaginationList<TransactionHistory> getcustomerTransactionlist(
			long customerId, int transactionType, int transactionStatusType,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			Long companyId, int start, int length) {

		FinanceDate[] dates = getMinimumAndMaximumDates(fromDate, toDate,
				getCompanyId());
		PaginationList<TransactionHistory> resultList = new PaginationList<TransactionHistory>();
		try {

			resultList = getFinanceTool().getCustomerManager()
					.getResultListbyType(customerId, transactionType,
							transactionStatusType, dates[0].getDate(),
							dates[1].getDate(), companyId, start, length);

			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public PaginationList<TransactionHistory> getConsultantTransactionsList(
			long id, int transactionType, int transactionStatusType,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int start,
			int length) {
		return getconsultantTransactionsList(id, transactionType,
				transactionStatusType, startDate, endDate, getCompanyId(),
				start, length);
		
	}
	private PaginationList<TransactionHistory> getconsultantTransactionsList(
			long customerId, int transactionType, int transactionStatusType,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			Long companyId, int start, int length) {
		FinanceDate[] dates = getMinimumAndMaximumDates(fromDate, toDate,
				getCompanyId());
		try {

			return getFinanceTool().getCustomerManager()
					.getConsultantResultListbyType(customerId, transactionType,
							transactionStatusType, dates[0].getDate(),
							dates[1].getDate(), companyId, start, length);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public PaginationList<TransactionHistory> getVendorTransactionsList(
			long vendorId, int transactionType, int transactionStatusType,
			ClientFinanceDate fromDate, ClientFinanceDate toDate, int start,
			int length) {

		FinanceDate[] dates = getMinimumAndMaximumDates(fromDate, toDate,
				getCompanyId());
		PaginationList<TransactionHistory> resultList = new PaginationList<TransactionHistory>();
		try {
			resultList = getFinanceTool().getVendorManager()
					.getResultListbyType(vendorId, transactionType,
							transactionStatusType, dates[0].getDate(),
							dates[1].getDate(), getCompanyId(), start, length);

			TransactionHistory obj = new TransactionHistory();
			if (resultList != null)
				return resultList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public ArrayList<BudgetActuals> getBudgetvsAcualReportData(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int type) {

		ArrayList<BudgetActuals> budgetActualsList = new ArrayList<BudgetActuals>();

		long companyId = getCompanyId();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			budgetActualsList = getFinanceTool().getReportManager()
					.getBudgetvsAcualReportData(financeDates[0],
							financeDates[1], companyId, id, type);

			BudgetActuals obj = new BudgetActuals();
			if (budgetActualsList != null)
				budgetActualsList.add((BudgetActuals) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return budgetActualsList;

	}

	@Override
	public ArrayList<RealisedExchangeLossOrGain> getRealisedExchangeLossesAndGains(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ArrayList<RealisedExchangeLossOrGain> list = getFinanceTool()
				.getReportManager().getRealisedExchangeLossesOrGains(
						getCompanyId(), start.getDate(), end.getDate());
		RealisedExchangeLossOrGain obj = new RealisedExchangeLossOrGain();
		if (list != null)
			list.add((RealisedExchangeLossOrGain) setStartEndDates(obj,
					minimumAndMaximumDates));
		return list;
	}

	@Override
	public ArrayList<CurrencyExchangeRate> getExchangeRatesOfDate(long date)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		ArrayList<CurrencyExchangeRate> rates = new ArrayList<CurrencyExchangeRate>();
		Company company = tool.getCompany(getCompanyId());
		for (Currency currency : company.getCurrencies()) {
			if (currency.getID() == company.getPrimaryCurrency().getID()) {
				continue;
			}
			CurrencyExchangeRate exchangeRate = new CurrencyExchangeRate();
			exchangeRate.setCurrencyId(currency.getID());
			exchangeRate.setCurrencyName(currency.getFormalName());
			double rate = tool
					.getMostRecentTransactionCurreencyFactorBasedOnCurrency(
							getCompanyId(), currency.getID(), date);
			exchangeRate.setExchangeRate(rate);
			rates.add(exchangeRate);
		}
		return rates;
	}

	@Override
	public ArrayList<UnRealisedLossOrGain> getUnRealisedExchangeLossesAndGains(
			long enterdDate, HashMap<Long, Double> exchangeRates)
			throws AccounterException {
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(
				new ClientFinanceDate(), new ClientFinanceDate(),
				getCompanyId());
		ArrayList<UnRealisedLossOrGain> list = getFinanceTool()
				.getReportManager().getUnRealisedExchangeLossesAndGains(
						enterdDate, getCompanyId(), exchangeRates);
		UnRealisedLossOrGain obj = new UnRealisedLossOrGain();
		if (list != null)
			list.add((UnRealisedLossOrGain) setStartEndDates(obj,
					minimumAndMaximumDates));
		return list;
	}

	@Override
	public ArrayList<InventoryStockStatusDetail> getInventoryStockStatusByVendor(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ArrayList<InventoryStockStatusDetail> list = getFinanceTool()
				.getInventoryManager().getInventoryStockStatusByVendor(
						getCompanyId(), start, end);
		InventoryStockStatusDetail obj = new InventoryStockStatusDetail();
		if (list != null)
			list.add((InventoryStockStatusDetail) setStartEndDates(obj,
					minimumAndMaximumDates));
		return list;
	}

	@Override
	public ArrayList<InventoryValutionDetail> getInventoryValutionDetail(
			long itemId, ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {

		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ArrayList<InventoryValutionDetail> list = getFinanceTool()
				.getInventoryManager()
				.getInventoryValutionDetail(getCompanyId(),
						minimumAndMaximumDates[0].toClientFinanceDate(),
						minimumAndMaximumDates[1].toClientFinanceDate(), itemId);
		InventoryValutionDetail obj = new InventoryValutionDetail();
		if (list != null)
			list.add((InventoryValutionDetail) setStartEndDates(obj,
					minimumAndMaximumDates));
		return list;
	}

	@Override
	public ArrayList<InventoryStockStatusDetail> getInventoryStockStatusByItem(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ArrayList<InventoryStockStatusDetail> list = getFinanceTool()
				.getInventoryManager().getInventoryStockStatusByItem(
						getCompanyId(), start, end);
		InventoryStockStatusDetail obj = new InventoryStockStatusDetail();
		if (list != null)
			list.add((InventoryStockStatusDetail) setStartEndDates(obj,
					minimumAndMaximumDates));
		return list;
	}

	@Override
	public ArrayList<InventoryValutionSummary> getInventoryValutionSummary(
			long wareHouseId, ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ArrayList<InventoryValutionSummary> list = getFinanceTool()
				.getInventoryManager().getInventoryValutionSummary(wareHouseId,
						getCompanyId(),
						minimumAndMaximumDates[0].toClientFinanceDate(),
						minimumAndMaximumDates[1].toClientFinanceDate());
		InventoryValutionSummary obj = new InventoryValutionSummary();
		if (list != null)
			list.add((InventoryValutionSummary) setStartEndDates(obj,
					minimumAndMaximumDates));
		return list;
	}

	@Override
	public ArrayList<BankDepositDetail> getBankingDepositDetils(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ArrayList<BankDepositDetail> list = getFinanceTool().getReportManager()
				.getBankDepositDetails(getCompanyId(),
						minimumAndMaximumDates[0].toClientFinanceDate(),
						minimumAndMaximumDates[1].toClientFinanceDate());
		BankDepositDetail obj = new BankDepositDetail();
		if (list != null)
			list.add((BankDepositDetail) setStartEndDates(obj,
					minimumAndMaximumDates));
		return list;

	}

	@Override
	public ArrayList<TransactionDetailByAccount> getMissingCheckDetils(
			long accountId, ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ArrayList<TransactionDetailByAccount> missionChecksByAccount = getFinanceTool()
				.getReportManager().getMissionChecksByAccount(accountId,
						minimumAndMaximumDates[0], minimumAndMaximumDates[1],
						getCompanyId());
		TransactionDetailByAccount detailByAccount = new TransactionDetailByAccount();
		if (detailByAccount != null) {
			missionChecksByAccount
					.add((TransactionDetailByAccount) setStartEndDates(
							detailByAccount, minimumAndMaximumDates));
		}
		return missionChecksByAccount;

	}

	@Override
	public ArrayList<ReconciliationDiscrepancy> getReconciliationDiscrepancy(
			long accountId, ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		ArrayList<ReconciliationDiscrepancy> reconciliationDiscrepancyByAccount = getFinanceTool()
				.getReportManager().getReconciliationDiscrepancyByAccount(
						accountId, start, end, getCompanyId());
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ReconciliationDiscrepancy discrepancy = new ReconciliationDiscrepancy();
		if (reconciliationDiscrepancyByAccount != null) {
			reconciliationDiscrepancyByAccount
					.add((ReconciliationDiscrepancy) setStartEndDates(
							discrepancy, minimumAndMaximumDates));
		}
		return reconciliationDiscrepancyByAccount;
	}

	@Override
	public ArrayList<BankCheckDetail> getBankCheckDetils(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		FinanceDate[] minimumAndMaximumDates = getMinimumAndMaximumDates(start,
				end, getCompanyId());
		ArrayList<BankCheckDetail> list = getFinanceTool().getReportManager()
				.getBankCheckDetails(
						getCompanyId(),
						new ClientFinanceDate(minimumAndMaximumDates[0]
								.getDate()),
						new ClientFinanceDate(minimumAndMaximumDates[1]
								.getDate()));
		BankCheckDetail obj = new BankCheckDetail();
		if (list != null)
			list.add((BankCheckDetail) setStartEndDates(obj,
					minimumAndMaximumDates));
		return list;

	}

	@Override
	public ArrayList<EstimatesByJob> getEstimatesByJob(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ArrayList<EstimatesByJob> estimatesByJob = new ArrayList<EstimatesByJob>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());
		try {
			estimatesByJob = getFinanceTool().getReportManager()
					.getEstimatesByJob(financeDates[0], financeDates[1],
							getCompanyId().longValue());
			EstimatesByJob obj = new EstimatesByJob();
			if (estimatesByJob != null)
				estimatesByJob.add((EstimatesByJob) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return estimatesByJob;

	}

	@Override
	public ArrayList<JobActualCostDetail> getJobActualCostOrRevenueDetails(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			boolean isActualcostDetail, long customerId, long jobId) {
		ArrayList<JobActualCostDetail> jobActualCostDetails = new ArrayList<JobActualCostDetail>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());
		try {
			jobActualCostDetails = getFinanceTool().getReportManager()
					.getJobActualCostOrRevenueDetails(financeDates[0],
							financeDates[1], getCompanyId().longValue(),
							isActualcostDetail, customerId, jobId);
			JobActualCostDetail obj = new JobActualCostDetail();
			if (jobActualCostDetails != null) {
				jobActualCostDetails
						.add((JobActualCostDetail) setStartEndDates(obj,
								financeDates));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobActualCostDetails;
	}

	@Override
	public ArrayList<JobProfitability> getJobProfitabilitySummaryReport(
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {
		JobProfitability obj = new JobProfitability();
		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());
		ArrayList<JobProfitability> list = null;
		try {
			list = getFinanceTool().getReportManager()
					.getJobProfitabilitySummaryReport(getCompanyId(),
							dates[0].toClientFinanceDate(),
							dates[1].toClientFinanceDate());
			if (list != null)
				list.add((JobProfitability) setStartEndDates(obj, dates));
		} catch (AccounterException e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public ArrayList<UnbilledCostsByJob> getUnBilledCostsByJob(
			ClientFinanceDate start, ClientFinanceDate end) {
		UnbilledCostsByJob obj = new UnbilledCostsByJob();
		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());
		ArrayList<UnbilledCostsByJob> list = null;
		try {
			list = getFinanceTool().getReportManager()
					.getUnBilledCostsByJobReport(getCompanyId(),
							dates[0].toClientFinanceDate(),
							dates[1].toClientFinanceDate());
			if (list != null)
				list.add((UnbilledCostsByJob) setStartEndDates(obj, dates));
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ArrayList<ItemActualCostDetail> getItemActualCostDetail(
			ClientFinanceDate start, ClientFinanceDate end, long itemId,
			long customerId, long jobId, boolean isActualcostDetail) {
		ArrayList<ItemActualCostDetail> itemActualCostDetails = new ArrayList<ItemActualCostDetail>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(start, end,
				getCompanyId());

		try {
			itemActualCostDetails = getFinanceTool().getReportManager()
					.getItemActualCostOrRevenueDetails(financeDates[0],
							financeDates[1], getCompanyId().longValue(),
							itemId, customerId, jobId, isActualcostDetail);
			ItemActualCostDetail obj = new ItemActualCostDetail();
			if (itemActualCostDetails != null) {
				itemActualCostDetails
						.add((ItemActualCostDetail) setStartEndDates(obj,
								financeDates));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemActualCostDetails;
	}

	@Override
	public ArrayList<JobProfitabilityDetailByJob> getJobProfitabilityDetailByJobReport(
			long payeeId, long jobId, ClientFinanceDate start,
			ClientFinanceDate end) throws AccounterException {
		JobProfitabilityDetailByJob obj = new JobProfitabilityDetailByJob();
		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());
		ArrayList<JobProfitabilityDetailByJob> list = getFinanceTool()
				.getReportManager().getJobProfitabilityDetailByJobReport(
						payeeId, jobId, getCompanyId(),
						dates[0].toClientFinanceDate(),
						dates[1].toClientFinanceDate());
		if (list != null)
			list.add((JobProfitabilityDetailByJob) setStartEndDates(obj, dates));
		return list;
	}

	@Override
	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccountAndCategory(
			int categoryType, long categoryId, long accountid,
			ClientFinanceDate start, ClientFinanceDate end) {
		TransactionDetailByAccount obj = new TransactionDetailByAccount();
		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());
		ArrayList<TransactionDetailByAccount> list = new ArrayList<TransactionDetailByAccount>();
		try {
			list = getFinanceTool().getReportManager()
					.getTransactionDetailByAccountAndCategory(categoryType,
							categoryId, accountid,
							dates[0].toClientFinanceDate(),
							dates[1].toClientFinanceDate(), getCompanyId());
			if (list != null)
				list.add((TransactionDetailByAccount) setStartEndDates(obj,
						dates));

		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccountAndCategory(
			int i, long categoryid, String status,
			ClientFinanceDate clientFinanceDate,
			ClientFinanceDate clientFinanceDate2, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> exportToFile(int exportType, int reportType,
			long startDate, long endDate, ReportInput[] input)
			throws AccounterException {
		ExportManager manager = getFinanceTool().getExportManager();
		List<ReportInput> list = Arrays.asList(input);
		return (ArrayList<String>) manager.exportReportToFile(getCompanyId(),
				exportType, reportType, startDate, endDate, list);
	}

	@Override
	public ArrayList<IncomeByCustomerDetail> getIncomeByCustomerDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate)
			throws AccounterException {
		ArrayList<IncomeByCustomerDetail> details = new ArrayList<IncomeByCustomerDetail>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());
		try {
			details = getFinanceTool().getReportManager()
					.getIncomeByCustomerDetails(
							financeDates[0].toClientFinanceDate(),
							financeDates[1].toClientFinanceDate(),
							getCompanyId());
			IncomeByCustomerDetail obj = new IncomeByCustomerDetail();
			if (details != null)
				details.add((IncomeByCustomerDetail) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return details;
	}

	@Override
	public ArrayList<InventoryDetails> getInventoryDetails(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ArrayList<InventoryDetails> inventoryDetails = new ArrayList<InventoryDetails>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());
		try {
			inventoryDetails = getFinanceTool().getReportManager()
					.getInventoryDetails(financeDates[0], financeDates[1],
							getCompanyId().longValue());
			InventoryDetails obj = new InventoryDetails();
			if (inventoryDetails != null)
				inventoryDetails.add((InventoryDetails) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inventoryDetails;

	}

	public ArrayList<PayHeadSummary> getPayHeadSummaryReport(long payHeadId,
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {

		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());
		return getPayHeadSummaryReportDetais(payHeadId, dates[0], dates[1],
				getCompanyId());

	}
	
	public ArrayList<PayHeadSummary> getPayTaxSummaryReport(String taxType, long employeeId,
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {

		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());
		ArrayList<PayHeadSummary> payHeadSummaryList = new ArrayList<PayHeadSummary>();
		payHeadSummaryList = getFinanceTool().getPayrollManager()
				.getPayTaxSummaryReport(taxType, employeeId,  dates[0], dates[1],
						getCompanyId());
		if (payHeadSummaryList != null) {
			PayHeadSummary obj = new PayHeadSummary();
			payHeadSummaryList.add((PayHeadSummary) setStartEndDates(obj,
					new FinanceDate[] {  dates[0], dates[1] }));
		}
		return payHeadSummaryList;

	}

	public ArrayList<PayHeadSummary> getPayHeadSummaryReportDetais(
			long payHeadId, FinanceDate startDate, FinanceDate endDate,
			Long companyId) throws AccounterException {
		ArrayList<PayHeadSummary> payHeadSummaryList = new ArrayList<PayHeadSummary>();
		payHeadSummaryList = getFinanceTool().getPayrollManager()
				.getPayHeadSummaryReport(payHeadId, startDate, endDate,
						companyId);
		if (payHeadSummaryList != null) {
			PayHeadSummary obj = new PayHeadSummary();
			payHeadSummaryList.add((PayHeadSummary) setStartEndDates(obj,
					new FinanceDate[] { startDate, endDate }));
		}

		return payHeadSummaryList;
	}

	@Override
	public ArrayList<PayHeadDetails> getPayHeadDetailReportList(
			long employeeId, long payHeadId, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(fromDate, toDate,
				getCompanyId());
		return getPayHeadDetailReportList(employeeId, payHeadId, dates[0],
				dates[1], getCompanyId());
	}

	public ArrayList<PayHeadDetails> getPayHeadDetailReportList(
			long employeeId, long payHeadId, FinanceDate startDate,
			FinanceDate endDate, Long companyId) throws AccounterException {
		ArrayList<PayHeadDetails> payHeadDetailsList = new ArrayList<PayHeadDetails>();
		payHeadDetailsList = getFinanceTool().getPayrollManager()
				.getpayHeadDetailsList(employeeId, payHeadId, startDate,
						endDate, companyId);
		if (payHeadDetailsList != null) {
			PayHeadDetails obj = new PayHeadDetails();
			payHeadDetailsList.add((PayHeadDetails) setStartEndDates(obj,
					new FinanceDate[] { startDate, endDate }));
		}
		return payHeadDetailsList;
	}
}
