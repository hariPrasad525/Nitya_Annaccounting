package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.mobile.requirements.TaxAgencyRequirement;
import com.vimukti.accounter.web.client.ui.reports.TAXItemDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class VATItemDetailReportCommand extends
		NewAbstractReportCommand<TAXItemDetail> {
	TAXAgency taxAgency;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseSelect(getMessages().taxAgency()), getMessages()
				.taxAgency(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().taxAgency());
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(getCompany().getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return getMessages()
						.youDontHaveAny(getMessages().taxAgencies());
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return false;
			}
		});
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<TAXItemDetail>() {

			@Override
			protected String onSelection(TAXItemDetail selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<TAXItemDetail> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<TAXItemDetail>> recordGroups = new HashMap<String, List<TAXItemDetail>>();
				for (TAXItemDetail transactionDetailByAccount : records) {
					String taxItemName = transactionDetailByAccount
							.getTaxItemName();
					List<TAXItemDetail> group = recordGroups.get(taxItemName);
					if (group == null) {
						group = new ArrayList<TAXItemDetail>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> taxItems = new ArrayList<String>(keySet);
				Collections.sort(taxItems);
				for (String accountName : taxItems) {
					List<TAXItemDetail> group = recordGroups.get(accountName);
					double totalAmount = 0.0;
					addSelection(accountName);
					ResultList resultList = new ResultList(accountName);
					resultList.setTitle(accountName);
					for (TAXItemDetail rec : group) {
						totalAmount += rec.getTaxAmount();
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
					makeResult.add("Total: "
							+ getAmountWithCurrency(totalAmount));
				}
			}
		});
	}

	protected Record createReportRecord(TAXItemDetail record) {
		Record ecRecord = new Record(record);
		ecRecord.add(getMessages().type(),
				Utility.getTransactionName(record.getTransactionType()));
		ecRecord.add(getMessages().date(),
				getDateByCompanyType(record.getTransactionDate()));
		ecRecord.add(getMessages().number(), record.getTransactionNumber());
		ecRecord.add(getMessages().taxRate(), record.isPercentage() ? "    "
				+ record.getTAXRate() + "%" : record.getTAXRate());
		ecRecord.add(getMessages().grossProfit(),
				getAmountWithCurrency(record.getTotal()));
		ecRecord.add(getMessages().taxAmount(),
				getAmountWithCurrency(record.getTaxAmount()));
		ecRecord.add(getMessages().netAmount(),
				getAmountWithCurrency(record.getNetAmount()));
		return ecRecord;
	}

	protected List<TAXItemDetail> getRecords() {
		TAXAgency taxItem = get(TAX_AGENCY).getValue();
		return new FinanceTool().getReportManager().getTAXItemDetailReport(
				getCompanyId(), taxItem.getID(), getStartDate().getDate(),
				getEndDate().getDate());
	}

	protected String addCommandOnRecordClick(TAXItemDetail selection) {
		return "updateTransaction " + selection.getTransactionId();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String itemName = null;
		String string = context.getString();
		if (string != null) {
			String[] split = string.split(",");
			if (split.length > 1) {
				context.setString(split[0]);
				itemName = split[1];
			}
		}
		if (itemName != null) {
			Set<TAXAgency> taxItems = context.getCompany().getTaxAgencies();
			for (TAXAgency taxItem : taxItems) {
				if (taxItem.getName().equalsIgnoreCase(itemName)) {
					get(TAX_AGENCY).setValue(taxItem);
					break;
				}
			}
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "VAT Item detail report command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "VAT Item report details";
	}

	@Override
	public String getSuccessMessage() {
		return "VAT Item detail report command closed successfully";
	}

}
