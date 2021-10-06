package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientPaypalTransation;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class PaypalTransactionListView extends
		BaseListView<ClientPaypalTransation> implements IPrintableView {

	private Button reloadTransactionsButton;
	private SelectCombo accountSelect;
	protected ArrayList<ClientAccount> accountsList = new ArrayList<ClientAccount>();

	public PaypalTransactionListView() {
		this.getElement().setId("PaypalTransactionListView");
		isDeleteDisable = true;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	protected void createListForm(DynamicForm form) {
		reloadTransactionsButton = new Button("Refresh Transactions");
		reloadTransactionsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				int selectedIndex = accountSelect.getSelectedIndex();
				ClientAccount clientAccount = accountsList.get(selectedIndex);
				Accounter.createHomeService().getNewPaypalTransactionsList(
						clientAccount.getID(),
						new AsyncCallback<List<ClientPaypalTransation>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(
									List<ClientPaypalTransation> result) {
								grid.addRecords(result);
							}
						});

			}
		});

		accountSelect = new SelectCombo(messages.Account());
		accountSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						int selectedIndex = accountSelect.getSelectedIndex();
						ClientAccount clientAccount = accountsList
								.get(selectedIndex);

						Accounter
								.createHomeService()
								.getSavedPaypalTransaction(
										clientAccount,
										new AsyncCallback<PaginationList<ClientPaypalTransation>>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onSuccess(
													PaginationList<ClientPaypalTransation> result) {
												grid.removeAllRecords();
												grid.setRecords(result);
												grid.sort(1, false);

											}
										});
					}
				});

		form.add(getSelectItem());
		form.add(reloadTransactionsButton);
		form.add(accountSelect);
	}

	public static PaypalTransactionListView getInstance() {
		return new PaypalTransactionListView();
	}

	@Override
	protected String getAddNewLabelString() {
		return "";
	}

	@Override
	protected String getListViewHeading() {
		return "Paypal Transactions List";
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void init() {
		super.init();

		Accounter.createHomeService().getPaypalAccounts(
				new AsyncCallback<ArrayList<ClientAccount>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ArrayList<ClientAccount> result) {
						List<String> acc = new ArrayList<String>();
						accountsList = result;
						for (ClientAccount clientAccount : result) {
							acc.add(clientAccount.getName());
						}
						accountSelect.initCombo(acc);
						accountSelect.setSelectedItem(0);
						readFirstAccountTransactions(result.get(0));
					}

				});

	}

	protected void readFirstAccountTransactions(ClientAccount clientAccount) {
		Accounter.createHomeService().getSavedPaypalTransaction(clientAccount,
				new AsyncCallback<PaginationList<ClientPaypalTransation>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							PaginationList<ClientPaypalTransation> result) {
						grid.removeAllRecords();
						grid.setRecords(result);
						grid.sort(1, false);

					}
				});

	}

	@Override
	protected void onPageChange(int start, int length) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(PaginationList<ClientPaypalTransation> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.setRecords(result);
		grid.sort(1, false);
		Window.scrollTo(0, 0);
		grid.enableOrDisableCheckBox(false);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
		start = result.getStart();
	}

	@Override
	protected void initGrid() {
		grid = new PaypalTransactionsListGrid(false);
		grid.init();
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> selectTypes = new ArrayList<String>();
		selectTypes.add(messages.active());
		selectTypes.add(messages.inActive());
		return selectTypes;
	}

	@Override
	protected void filterList(boolean isActive) {
		this.isActive = isActive;
		onPageChange(0, getPageSize());
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void updateInGrid(ClientPaypalTransation objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	protected String getViewTitle() {
		return "Paypal Transactions";
	}

	@Override
	protected Action getAddNewAction() {
		return null;
	}

	@Override
	public void exportToCsv() {

		int selectedIndex = accountSelect.getSelectedIndex();
		ClientAccount clientAccount = accountsList.get(selectedIndex);

		Accounter.createExportCSVService().exportSavedPaypalTransactions(
				clientAccount, getExportCSVCallback(messages.paypal()));
	}
}
