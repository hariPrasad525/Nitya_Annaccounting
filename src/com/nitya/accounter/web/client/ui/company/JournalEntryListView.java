package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.core.ClientJournalEntry;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.JournalEntriesListGrid;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class JournalEntryListView extends
		TransactionsListView<ClientJournalEntry> implements IPrintableView {
	List<ClientJournalEntry> allEntries;

	public JournalEntryListView() {
		super(messages.all());
		isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		return new NewJournalEntryAction();
	}

	@Override
	protected void createListForm(DynamicForm form) {
		super.createListForm(form);
		form.remove(viewSelect);
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().getPermissions().getTypeOfManageAccounts() == RolePermissions.TYPE_YES) {
			return messages.addNewJournalEntry();
		}
		return null;
	}

	@Override
	protected String getListViewHeading() {
		return messages.journalEntryList();
	}

	@Override
	public void initListCallback() {

		super.initListCallback();

		onPageChange(0, getPageSize());
	}

	@Override
	protected void initGrid() {
		grid = new JournalEntriesListGrid(false);
		grid.init();
	}

	@Override
	protected int getPageSize() {

		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		rpcUtilService.getJournalEntries(getStartDate().getDate(), getEndDate()
				.getDate(), start, length, this);

	}

	@Override
	public void onSuccess(PaginationList<ClientJournalEntry> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), result.size(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.removeLoadingImage();
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
	}

	@Override
	public void updateInGrid(ClientJournalEntry objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		// listOfTypes.add(FinanceApplication.constants().nonVoided());
		// listOfTypes.add(FinanceApplication.constants().Voided());
		// listOfTypes.add(FinanceApplication.constants().cashBasis());
		// listOfTypes.add(FinanceApplication.constants()
		// .voidedCashBasis());
		listOfTypes.add(messages.all());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		/*
		 * if (currentView.getSelectedValue().equalsIgnoreCase("Non Voided")) {
		 * List<ClientJournalEntry> nonVoidedRecs = new
		 * ArrayList<ClientJournalEntry>(); List<ClientJournalEntry> allRecs =
		 * initialRecords; for (ClientJournalEntry rec : allRecs) { if
		 * (!rec.isVoid()) { nonVoidedRecs.add(rec); } }
		 * grid.setRecords(nonVoidedRecs);
		 * 
		 * } else if (currentView.getSelectedValue().equalsIgnoreCase("Voided"))
		 * { List<ClientJournalEntry> voidedRecs = new
		 * ArrayList<ClientJournalEntry>(); List<ClientJournalEntry> allRecs =
		 * initialRecords; for (ClientJournalEntry rec : allRecs) { if
		 * (rec.isVoid() && rec.getStatus() != ClientTransaction.STATUS_DELETED)
		 * { voidedRecs.add(rec); } } grid.setRecords(voidedRecs);
		 * 
		 * } else if (currentView.getSelectedValue()
		 * .equalsIgnoreCase("Cash Basis")) { List<ClientJournalEntry>
		 * cashBasisRecs = new ArrayList<ClientJournalEntry>();
		 * List<ClientJournalEntry> allRecs = initialRecords; for
		 * (ClientJournalEntry rec : allRecs) { if (rec.getType() ==
		 * ClientJournalEntry.TYPE_CASH_BASIS_JOURNAL_ENTRY) {
		 * cashBasisRecs.add(rec); } } grid.setRecords(cashBasisRecs);
		 * 
		 * } else if (currentView.getSelectedValue().equalsIgnoreCase(
		 * "Voided Cash Basis")) { List<ClientJournalEntry> voidedCashBasisRecs
		 * = new ArrayList<ClientJournalEntry>(); List<ClientJournalEntry>
		 * allRecs = initialRecords; for (ClientJournalEntry rec : allRecs) { if
		 * (rec.getType() == ClientJournalEntry.TYPE_CASH_BASIS_JOURNAL_ENTRY &&
		 * rec.isVoid() && rec.getStatus() != ClientTransaction.STATUS_DELETED)
		 * { voidedCashBasisRecs.add(rec); } }
		 * grid.setRecords(voidedCashBasisRecs);
		 * 
		 * }
		 */
		// else if (currentView.getValue().toString().equalsIgnoreCase(
		// "Deleted")) {
		// List<ClientJournalEntry> deletedRecs = new
		// ArrayList<ClientJournalEntry>();
		// List<ClientJournalEntry> allRecs = initialRecords;
		// for (ClientJournalEntry rec : allRecs) {
		// if (rec.getStatus()==ClientTransaction.STATUS_DELETED) {
		// deletedRecs.add(rec);
		// }
		// }
		// grid.setRecords(deletedRecs);
		//
		// }
		grid.removeAllRecords();
		grid.addLoadingImagePanel();
		onPageChange(start, getPageSize());

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.journalEntries();
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getJournalEntriesExportCsv(
				getStartDate().getDate(), getEndDate().getDate(),
				getExportCSVCallback(messages.journalEntries()));
	}
}
