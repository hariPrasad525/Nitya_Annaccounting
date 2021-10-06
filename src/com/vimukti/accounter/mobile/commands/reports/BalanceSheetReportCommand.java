package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author vimukti2
 * 
 */
public class BalanceSheetReportCommand extends
		NewAbstractReportCommand<TrialBalance> {
	private ClientAccount account;
	double liabilitiesAndEquitityTotal = 0.0;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeToDateRequirements(list);

		list.add(new ReportResultRequirement<TrialBalance>() {

			@Override
			protected String onSelection(TrialBalance selection, String name) {
				return "transactionDetailByAccount ,"
						+ selection.getAccountNumber();
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {

				List<TrialBalance> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<TrialBalance>> recordGroups = new HashMap<String, List<TrialBalance>>();
				for (TrialBalance trailBalanceRecord : records) {
					String subBaseTypeName = getSubBaseType(trailBalanceRecord
							.getSubBaseType());
					List<TrialBalance> group = recordGroups
							.get(subBaseTypeName);
					if (group == null) {
						group = new ArrayList<TrialBalance>();
						recordGroups.put(subBaseTypeName, group);
					}
					group.add(trailBalanceRecord);
				}
				Set<String> keySet = recordGroups.keySet();
				List<String> balanceSheetrecords = new ArrayList<String>(keySet);
				Collections.sort(balanceSheetrecords);

				for (String name : balanceSheetrecords) {
					List<TrialBalance> recordGroup = recordGroups.get(name);
					addResultList(makeResult, recordGroup, name);
				}
			}

			private void addResultList(Result makeResult,
					List<TrialBalance> records, String string) {
				ResultList list = new ResultList(string);
				list.setTitle(string);
				addSelection(string);
				double total = 0.0;
				for (TrialBalance record : records) {
					Record createReportRecord = createReportRecord(record);
					total += record.getAmount();
					list.add(createReportRecord);
				}
				makeResult.add(list);
				makeResult.add(string + getMessages().total()
						+ getAmountWithCurrency(total));

				if (string.equals(getMessages().currentLiabilities())
						|| string.equals(getMessages().equity())) {
					liabilitiesAndEquitityTotal += total;
				}
				if (string.equals(getMessages().currentAssets())) {
					makeResult.add(getMessages().assetsTotal()
							+ getAmountWithCurrency(total));
				}
				if (string.equals(getMessages().equity())) {
					makeResult
							.add(getMessages().liabilitiesandEquityTotal()
									+ getAmountWithCurrency(liabilitiesAndEquitityTotal));
				}
			}
		});
	}

	private Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);
		trialRecord.add(getMessages().accountName(), record.getAccountName());
		trialRecord.add(getDateByCompanyType(getStartDate()
				.toClientFinanceDate())
				+ "-"
				+ getDateByCompanyType(getEndDate().toClientFinanceDate()),
				getAmountWithCurrency(record.getAmount()));
		return trialRecord;
	}

	@Override
	public String getId() {
		return null;
	}

	/**
	 * get balance balance Records
	 * 
	 * @return
	 */
	private List<TrialBalance> getRecords() {
		ArrayList<TrialBalance> trailBalanceReport = new ArrayList<TrialBalance>();
		try {
			trailBalanceReport = new FinanceTool().getReportManager()
					.getBalanceSheetReport(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trailBalanceReport;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		String number = null;
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			number = split[1];
		}
		if (number != null) {
			account = CommandUtils.getAccountByNumber(getCompany(), number);
			if (account == null) {
				account = CommandUtils.getAccountByName(getCompany(), number);
			}
		}
		endDate = new ClientFinanceDate();
		get(TO_DATE).setValue(endDate);
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

	/**
	 * get account SubbaseType Name
	 * 
	 * @param subBaseType
	 */
	public String getSubBaseType(int subBaseType) {

		if (subBaseType == Account.SUBBASETYPE_CURRENT_ASSET
				|| subBaseType == Account.SUBBASETYPE_FIXED_ASSET
				|| subBaseType == Account.SUBBASETYPE_OTHER_ASSET) {
			return getMessages().currentAssets();
		}

		if (subBaseType == Account.SUBBASETYPE_CURRENT_LIABILITY
				|| subBaseType == Account.SUBBASETYPE_LONG_TERM_LIABILITY) {
			return getMessages().currentLiabilities();
		}

		if (subBaseType == Account.SUBBASETYPE_EQUITY) {
			return getMessages().equity();
		}
		return null;

	}
}
