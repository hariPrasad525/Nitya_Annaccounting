package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.company.NewBudgetAction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.BudgetListGrid;

/**
 * 
 * @author Amrit Mishra
 * 
 */
public class BudgetListView extends BaseListView<ClientBudget> {

	List<ClientBudget> listOfBudgets = new ArrayList<ClientBudget>();
	private Button budgetEdit;
	private ClientBudget budgetData;
	private SelectCombo viewSelect;

	public BudgetListView() {
		this.getElement().setId("BudgetListView");
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = (AccounterException) caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	protected void createListForm(DynamicForm form) {
		budgetEdit = new Button(messages.edit());
		// budgetEdit.setWidth("10");

		budgetEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new NewBudgetAction().run(budgetData, false);
			}
		});
		form.add(getSelectItem());
		form.add(budgetEdit);
	}

	@Override
	protected void initGrid() {
		grid = new BudgetListGrid();
		grid.init();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getBudgetList(this);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.budgetList();
	}

	@Override
	protected Action getAddNewAction() {
		new NewBudgetAction().run(listOfBudgets);
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addNewBudget();
	}

	@Override
	protected String getViewTitle() {
		return messages.payees(messages.budgets());
	}

	@Override
	public void updateInGrid(ClientBudget objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(messages.currentBudget());
		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							budgetData = listOfBudgets.get(viewSelect
									.getSelectedIndex());
							changeBudgetGrid(viewSelect.getSelectedIndex());
						}

					}
				});
		return viewSelect;

	}

	@Override
	protected void changeBudgetGrid(int numberSelected) {
		grid.removeAllRecords();
		grid.setTotal();

		if (listOfBudgets.size() > 0) {
			ClientBudget budget = listOfBudgets.get(numberSelected);
			setData(budget);
			List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();
			budgetItems = budget.getBudgetItem();
			for (ClientBudgetItem budgetItem : budgetItems) {

				budgetItem.setAccountsName(budgetItem.getAccount().getName());

				if (budgetItem.getTotalAmount() > 0) {
					grid.addData(budgetItem);
				}
			}
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());

		getTotalLayout(grid);
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		grid.setTotal();

		if (listOfBudgets.size() > 0) {
			ClientBudget budget = listOfBudgets.get(0);
			setData(budget);
			List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();
			budgetItems = budget.getBudgetItem();

			for (ClientBudgetItem budgetItem : budgetItems) {

				budgetItem.setAccountsName(budgetItem.getAccount().getName());

				if (budgetItem.getTotalAmount() > 0) {
					grid.addData(budgetItem);
				}
			}
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());

		getTotalLayout(grid);
	}

	@Override
	public void onSuccess(PaginationList<ClientBudget> result) {
		if (result.isEmpty()) {
			grid.removeAllRecords();
			grid.addEmptyMessage(messages.noRecordsToShow());
			budgetEdit.setEnabled(false);
			return;
		}
		grid.removeLoadingImage();
		if (result != null) {
			initialRecords = result;
			this.records = result;

			this.listOfBudgets = (PaginationList<ClientBudget>) result;

			List<String> typeList = new ArrayList<String>();
			for (ClientBudget budget : (List<ClientBudget>) result) {
				typeList.add(budget.getBudgetName());
			}
			if (typeList.size() < 1) {
				typeList.add(messages.NoBudgetadded());
			}

			viewSelect.initCombo(typeList);
			viewSelect.setSelectedItem(0);
			if (listOfBudgets.size() > 0)
				budgetData = listOfBudgets.get(0);

			if (result.size() > 0) {
				ClientBudget budget = (ClientBudget) result.get(0);
				List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();
				budgetItems = budget.getBudgetItem();
				for (ClientBudgetItem budgetItem : budgetItems) {
					budgetItem.setAccountsName(budgetItem.getAccount()
							.getName());
				}
				grid.setRecords(budgetItems);
				budgetEdit.setEnabled(true);
			} else {

				grid.addEmptyMessage(messages.noRecordsToShow());
				budgetEdit.setEnabled(false);
			}

		} else {
			Accounter.showInformation(messages.noRecordsToShow());
			grid.removeLoadingImage();
		}
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
		grid.sort(10, false);

	}

	@Override
	protected boolean filterBeforeShow() {
		return true;
	}
}
