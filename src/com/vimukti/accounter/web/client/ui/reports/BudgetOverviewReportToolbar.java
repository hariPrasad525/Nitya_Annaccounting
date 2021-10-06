package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public class BudgetOverviewReportToolbar extends ReportToolbar {

	protected SelectCombo budgetName;

	protected List<String> statusList;
	List<String> budgetArray = new ArrayList<String>();
	List<Long> idArray = new ArrayList<Long>();

	long budgetId = 999L;
	int monthSelected;

	public BudgetOverviewReportToolbar() {
		createData();
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

		reportview.makeReportRequest(budgetId, startDate, endDate);

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		setStartDate(startDate);
		setEndDate(endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeChanged(defaultDateRange);
	}

	public void createControls() {

		budgetName = new SelectCombo(messages.budget());
		statusList = new ArrayList<String>();
		for (String str : budgetArray) {
			statusList.add(str);
		}
		if (budgetArray.size() < 1) {
			statusList.add("");
		}
		budgetName.initCombo(statusList);
		budgetName.setSelected(statusList.get(0));
		budgetName
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						budgetId = idArray.get(budgetName.getSelectedIndex());
						changeDates(startDate, endDate);
					}
				});

		addItems(budgetName);
	}

	public void createData() {

		Accounter.createHomeService().getBudgetList(
				new AsyncCallback<PaginationList<ClientBudget>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(
							PaginationList<ClientBudget> budgetList) {
						if (budgetList == null) {
							budgetList = new PaginationList<ClientBudget>();
						}
						for (ClientBudget budget : budgetList) {
							budgetArray.add(budget.getBudgetName());
							idArray.add(budget.getID());
						}
						createControls();

						if (idArray.size() > 0) {
							budgetId = idArray.get(0);
						} else {
							budgetId = 999L;
						}
						monthSelected = 1;
						changeDates(new ClientFinanceDate(),
								new ClientFinanceDate());

					}
				});

	}
}
