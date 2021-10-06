package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.JournalEntry;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.web.client.core.ClientJournalEntry;
import com.nitya.accounter.web.server.FinanceTool;

public class JournalEntryListCommand extends AbstractTransactionListCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new ShowListRequirement<ClientJournalEntry>(getMessages()
				.journalEntryList(), "", 20) {

			@Override
			protected String onSelection(ClientJournalEntry value) {
				return "editTransaction " + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().journalEntryList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(ClientJournalEntry entry) {
				Record record = new Record(entry);
				record.add(getMessages().number(), entry.getNumber());
				record.add(getMessages().date(),
						getDateByCompanyType(entry.getDate()));
				record.add(
						getMessages().amount(),
						getAmountWithCurrency(
								entry.getTotal(),
								getServerObject(Currency.class,
										entry.getCurrency()).getSymbol()));
				record.add(getMessages().memo(), entry.getMemo());
				record.add(getMessages().voided(),
						entry.isVoid() == true ? getMessages().voided()
								: getMessages().nonVoided());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createJournalEntry");
			}

			@Override
			protected boolean filter(ClientJournalEntry e, String name) {
				return e.getName().toLowerCase().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<ClientJournalEntry> getLists(Context context) {
				List<ClientJournalEntry> clientJournalEntries = new ArrayList<ClientJournalEntry>();
				List<JournalEntry> serverJournalEntries = null;
				try {

					// FIXME
					serverJournalEntries = new FinanceTool().getJournalEntries(
							context.getCompany().getID(), new FinanceDate(
									getStartDate()), new FinanceDate(
									getEndDate()), 0, -1);
					for (JournalEntry journalEntry : serverJournalEntries) {
						clientJournalEntries.add(new ClientConvertUtil()
								.toClientObject(journalEntry,
										ClientJournalEntry.class));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return clientJournalEntries;
			}
		});

	}

	@Override
	protected List<String> getViewByList() {
		return null;
	}
}
