package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientReminder;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.grids.RemindersListGrid;

public class RemindersListView extends BaseListView<ClientReminder> implements
		IPrintableView {

	public static String ALL = messages.all();
	public static String BILL = messages.bill();
	public static String CASH_EXPENSE = messages.cashExpense();
	public static String CASH_SALES = messages.cashSale();
	public static String CASH_PURCHASE = messages.cashPurchase();
	public static String CREDIT_CARD_EXPENSE = messages.creditCardExpense();
	public static String CUSTOMER_CREDIT_MEMO = messages
			.customerCreditNote(Global.get().Customer());
	public static String DEPOSIT_TRANSFER_FUNDS = messages
			.depositTransferFunds();
	public static String INVOICE = messages.invoice();
	public static String QUOTE = messages.quote();
	public static String VENDOR_CREDIT_MEMO = messages.vendorCreditMemo();
	public static String WRITE_CHECK = messages.writeCheck();

	Map<String, Integer> typesMap = new HashMap<String, Integer>();

	Button createButton, skipButton;

	ArrayList<ClientReminder> reminders = new ArrayList<ClientReminder>();
	public String viewType;
	private int viewType1;

	public RemindersListView() {
		super();
		this.getElement().setId("RemindersListView");
		initTypesMap();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		// Accounter.createHomeService().getRemindersList(this);
		// filterList(viewSelect.getSelectedValue());
	}

	@Override
	public void onSuccess(PaginationList<ClientReminder> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	public void updateInGrid(ClientReminder objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		super.createControls();

		StyledPanel panel = new StyledPanel("panel");

		createButton = new Button(messages.createSelected());
		createButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createOrSkipTransactions(true);
			}
		});

		skipButton = new Button(messages.skipSelected());
		skipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createOrSkipTransactions(false);
			}
		});

		panel.add(createButton);
		panel.add(skipButton);

		add(panel);
	}

	protected void createOrSkipTransactions(boolean isCreate) {
		ArrayList<ClientReminder> records = (ArrayList<ClientReminder>) grid
				.getSelectedRecords();
		if (!records.isEmpty()) {
			if (isCreate) {
				for (ClientReminder reminder : records) {
					if (!reminder.isValid()) {
						Accounter.showError(messages
								.someSelectedRecordsAreNeedToBeEdited());
						return;
					}
				}
			}
			createButton.setEnabled(false);
			skipButton.setEnabled(false);
			AccounterAsyncCallback<Boolean> callBack = new AccounterAsyncCallback<Boolean>() {

				@Override
				public void onException(AccounterException exception) {
					createButton.setEnabled(true);
					skipButton.setEnabled(true);
					Accounter.showError(exception.getMessage());
				}

				@Override
				public void onResultSuccess(Boolean result) {
					createButton.setEnabled(true);
					skipButton.setEnabled(true);
					initListCallback();
				}
			};
			Accounter.createCRUDService().createOrSkipTransactions(records,
					isCreate, callBack);
		} else {
			Accounter.showError(messages.pleaseSelectAtLeastOneRecord());
		}
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedValue = viewSelect.getSelectedValue();
		map.put("selectedOption", selectedValue);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		start = (Integer) viewDate.get("start");
		String object = (String) viewDate.get("selectedOption");
		if (object.equalsIgnoreCase(ALL)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(BILL)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CASH_EXPENSE)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CASH_PURCHASE)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CASH_SALES)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CREDIT_CARD_EXPENSE)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(CUSTOMER_CREDIT_MEMO)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(DEPOSIT_TRANSFER_FUNDS)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(INVOICE)) {
			viewSelect.setComboItem(object);
		} else if (object.equalsIgnoreCase(QUOTE)) {
			viewSelect.setComboItem(VENDOR_CREDIT_MEMO);
		} else if (object.equalsIgnoreCase(WRITE_CHECK)) {
			viewSelect.setComboItem(object);
		}
	}

	@Override
	protected void initGrid() {
		grid = new RemindersListGrid(true);
		grid.init();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getRemindersList(start, length,
				viewType1, this);
	}

	@Override
	protected String getListViewHeading() {
		return messages.remindersList();
	}

	@Override
	protected Action getAddNewAction() {
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		// No need to create add new label
		return null;
	}

	@Override
	protected String getViewTitle() {
		return messages.remindersList();
	}

	@Override
	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(messages.currentView());
		initTypesMap();
		ArrayList<String> listOfTypes = new ArrayList<String>(typesMap.keySet());
		viewSelect.initCombo(listOfTypes);
		if (viewType != null && !viewType.equals(""))
			viewSelect.setComboItem(viewType);
		else
			viewSelect.setComboItem(ALL);

		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue());
							filterList(viewSelect.getSelectedValue());
						}
					}
				});
		viewSelect.addStyleName("invoiceListCombo");

		return viewSelect;
	}

	protected void filterList(String selectedValue) {

		setViewType(selectedValue);
		onPageChange(0, getPageSize());
		// grid.removeAllRecords();
		//
		// if (!selectedValue.equalsIgnoreCase(ALL)) {
		// for (ClientReminder reminder : reminders) {
		// if (reminder.getRecurringTransaction().getTransaction()
		// .getType() == typesMap.get(selectedValue)) {
		// grid.addData(reminder);
		// }
		// }
		// } else {
		// grid.setRecords(reminders);
		// }
		// if (grid.getRecords().isEmpty()) {
		// grid.addEmptyMessage(messages.noRecordsToShow());
		// }
	}

	private void setViewType(String selectedValue) {
		Integer view = typesMap.get(selectedValue);
		if (view != null) {
			viewType1 = view.intValue();
		}
	}

	private void initTypesMap() {
		typesMap.put(BILL, ClientTransaction.TYPE_ENTER_BILL);
		typesMap.put(CASH_EXPENSE, ClientTransaction.TYPE_CASH_EXPENSE);
		typesMap.put(CASH_SALES, ClientTransaction.TYPE_CASH_SALES);
		typesMap.put(CASH_PURCHASE, ClientTransaction.TYPE_CASH_PURCHASE);
		typesMap.put(CREDIT_CARD_EXPENSE,
				ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
		typesMap.put(CUSTOMER_CREDIT_MEMO,
				ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
		typesMap.put(DEPOSIT_TRANSFER_FUNDS,
				ClientTransaction.TYPE_TRANSFER_FUND);
		typesMap.put(INVOICE, ClientTransaction.TYPE_INVOICE);
		typesMap.put(QUOTE, ClientTransaction.TYPE_ESTIMATE);
		typesMap.put(VENDOR_CREDIT_MEMO,
				ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
		typesMap.put(WRITE_CHECK, ClientTransaction.TYPE_WRITE_CHECK);
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
		Accounter.createExportCSVService().getRemindersListExportCsv(viewType1,
				getExportCSVCallback(messages.remindersList()));
	}
}
