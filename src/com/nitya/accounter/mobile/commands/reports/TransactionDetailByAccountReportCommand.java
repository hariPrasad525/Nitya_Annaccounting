package com.nitya.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Utility;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.ReportResultRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.nitya.accounter.web.server.FinanceTool;
import com.nitya.accounter.web.server.managers.ReportManager;

public class TransactionDetailByAccountReportCommand extends
		NewAbstractReportCommand<TransactionDetailByAccount> {
	private static String ACCOUNT = "account";

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getMessages().Account()), getMessages().Account(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return TransactionDetailByAccountReportCommand.this
						.getAccounts();
			}

			@Override
			protected boolean filter(Account e, String name) {
				return false;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}
		});
		list.add(new ReportResultRequirement<TransactionDetailByAccount>() {

			@Override
			protected String onSelection(TransactionDetailByAccount selection,
					String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TransactionDetailByAccount> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<TransactionDetailByAccount>> recordGroups = new HashMap<String, List<TransactionDetailByAccount>>();
				for (TransactionDetailByAccount transactionDetailByAccount : records) {
					String taxItemName = transactionDetailByAccount
							.getAccountName();
					List<TransactionDetailByAccount> group = recordGroups
							.get(taxItemName);
					if (group == null) {
						group = new ArrayList<TransactionDetailByAccount>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> taxItems = new ArrayList<String>(keySet);
				Collections.sort(taxItems);
				double grandTotal = 0.0;
				for (String accountName : taxItems) {
					List<TransactionDetailByAccount> group = recordGroups
							.get(accountName);
					double totalAmount = 0.0;
					addSelection(accountName);
					ResultList resultList = new ResultList(accountName);
					for (TransactionDetailByAccount rec : group) {
						resultList.setTitle(rec.getAccountName());
						totalAmount += rec.getTotal();
						Record createReportRecord = createReportRecord(rec);
						createReportRecord.add(getMessages().balance(),
								getAmountWithCurrency(totalAmount));
						resultList.add(createReportRecord);
					}
					makeResult.add(resultList);
					grandTotal += totalAmount;
					makeResult.add(getMessages().valueTotal(
							getMessages().amount())
							+ getAmountWithCurrency(totalAmount));
				}
				Account account = get(ACCOUNT).getValue();
				if (account == null) {
					makeResult.add(getMessages().valueTotal(
							getMessages().grand())
							+ getAmountWithCurrency(grandTotal));
				}
			}
		});
	}

	protected List<Account> getAccounts() {
		List<Account> accounts = new ArrayList<Account>();
		Set<Account> companyAccs = getCompany().getAccounts();
		for (Account account : companyAccs) {
			if (account.getIsActive()) {
				accounts.add(account);
			}
		}
		return accounts;
	}

	protected Record createReportRecord(TransactionDetailByAccount record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add(getMessages().name(),
				record.getName() == null ? "\t" : record.getName());
		transactionRecord.add(getMessages().date(),
				getDateByCompanyType(record.getTransactionDate()));
		transactionRecord.add(getMessages().transactionName(),
				Utility.getTransactionName(record.getTransactionType()));
		transactionRecord.add(getMessages().number(),
				record.getTransactionNumber());
		transactionRecord.add(getMessages().total(),
				getAmountWithCurrency(record.getTotal()));
		return transactionRecord;
	}

	protected List<TransactionDetailByAccount> getRecords() {
		List<TransactionDetailByAccount> transactionDetailsByAc = new ArrayList<TransactionDetailByAccount>();
		ReportManager reportManager = new FinanceTool().getReportManager();
		Account account = get(ACCOUNT).getValue();
		try {
			transactionDetailsByAc = reportManager
					.getTransactionDetailByAccount(account == null ? 0
							: account.getID(), getStartDate(), getEndDate(),
							getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
			transactionDetailsByAc = new ArrayList<TransactionDetailByAccount>();
		}
		return transactionDetailsByAc;
	}

	protected String addCommandOnRecordClick(
			TransactionDetailByAccount selection) {
		return "updateTransaction " + selection.getTransactionId();
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().valueActivated(
				getMessages().transactionDetailsByAccount());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().viewDetailsAsFollows(
				getMessages().transactionDetailsByAccount());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().viewClosedSuccessfully(
				getMessages().transactionDetailsByAccount());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String accountName = null;
		String string = context.getString();
		if (string != null) {
			String[] split = string.split(",");
			if (split.length > 1) {
				context.setString(split[0]);
				accountName = split[1];
			}
		}
		ClientAccount accountByNumber = null;
		if (accountName != null) {
			accountByNumber = CommandUtils.getAccountByNumber(getCompany(),
					accountName);
			if (accountByNumber == null) {
				accountByNumber = CommandUtils.getAccountByName(getCompany(),
						accountName);
			}
		}
		if (accountByNumber != null) {
			Account account = CommandUtils.getaccount(getCompany(),
					accountByNumber.getName());
			get(ACCOUNT).setValue(account);
		}
		endDate = new ClientFinanceDate();
		get(TO_DATE).setValue(endDate);
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}
}
