package com.nitya.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientStatement;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.grids.BankStatementsGrid;

/**
 * 
 * @author Lingarao
 * 
 */
public class BankStatementsView extends BaseListView<ClientStatement> {

	private List<ClientStatement> listBankStatementLists;
	private ClientAccount account;

	public BankStatementsView(ClientAccount account) {
		this.account = account;
		this.getElement().setId("BankStatementsView");

	}

	@Override
	public void updateInGrid(ClientStatement bankStatementList) {
	}

	@Override
	protected void initGrid() {
		grid = new BankStatementsGrid();
		grid.init();

	}

	@Override
	protected String getListViewHeading() {
		return messages.bankStatements();
	}

	@Override
	public void initListCallback() {
		onPageChange(0, getPageSize());
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
		return "Bank Statements";
	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getBankStatements(account.getID(), this);
	}

	@Override
	protected int getPageSize() {
		return 50;
	}

	@Override
	public void onSuccess(PaginationList<ClientStatement> result) {
		this.listBankStatementLists = result;
		grid.sort(10, false);
		if (result != null) {
			if (result.isEmpty()) {
				grid.addEmptyMessage(messages.noRecordsToShow());
			}
			grid.setRecords(result);
			Window.scrollTo(0, 0);
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
		}
	}
}
