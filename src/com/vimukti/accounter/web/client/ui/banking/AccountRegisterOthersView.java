package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.AccountRegisterOtherListGrid;

public class AccountRegisterOthersView extends
		TransactionsListView<AccountRegister> implements IPrintableView {
	AccountRegisterOtherListGrid grid;

	private ClientAccount takenaccount;
	private int length = 0;
	private final int TOP = 120;
	private final int FOOTER = 25;
	private final int BORDER = 20;

	public AccountRegisterOthersView(ClientAccount account2) {
		super();
		this.takenaccount = account2;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void fitToSize(int height, int width) {

		if (grid.isShowFooter())
			grid.setHeight(height - TOP - FOOTER + "px");
		else
			grid.setHeight(height - TOP + "px");

		grid.setWidth(width - BORDER + "px");

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	public long getCurrency() {
		return takenaccount.getCurrency();
	}

	@Override
	public void updateInGrid(AccountRegister objectTobeModified) {
	}

	@Override
	protected void initGrid() {
		grid = new AccountRegisterOtherListGrid(false);
		grid.init();
		grid.setView(this);
		super.grid = this.grid;
	}

	@Override
	protected String getListViewHeading() {
		return messages.accountRegister() + " - " + takenaccount.getName();
	}

	@Override
	protected Action getAddNewAction() {
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return null;
	}

	@Override
	protected String getViewTitle() {
		return messages.accountRegister();
	}

	@Override
	protected void onPageChange(int start, int length) {
		if (takenaccount == null) {
			return;
		}
		this.length = length;
		grid.setAccount(takenaccount);
		Accounter.createReportService().getAccountRegister(getStartDate(),
				getEndDate(), takenaccount.getID(), start, length, this);
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void onSuccess(PaginationList<AccountRegister> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), result.size(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		AccountRegister accountRegister = result.get(0);
		result.remove(0);
		grid.setOpeningBalance(accountRegister.getAmount());
		grid.removeLoadingImage();
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	protected SelectCombo getSelectItem() {
		return null;
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		grid.totalBalance = 0.0;
		grid.balance = 0.0;
		super.changeDates(startDate, endDate);
	}

	@Override
	public void initData() {
		grid.totalBalance = 0.0;
		grid.balance = 0.0;
		super.initData();
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
		Accounter.createExportCSVService().getAccounterRegister(getStartDate(),
				getEndDate(), takenaccount.getID(), start, length,
				getExportCSVCallback(getViewTitle()));
	}
}
