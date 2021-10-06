package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.PayRollActions;

public class EmployeeListView extends BaseListView<ClientEmployee> {

	private PaginationList<ClientEmployee> listOfemployees;

	public EmployeeListView() {
		this.getElement().setId("EmployeeListView");
		isViewSelectRequired = true;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	protected void filterList(boolean isActive) {
		this.isActive = isActive;
		onPageChange(0, getPageSize());
	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createPayrollService().getEmployees(isActive, start, length,
				this);
	}

	@Override
	public void onSuccess(PaginationList<ClientEmployee> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		listOfemployees = result;
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedValue = viewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.active())) {
			isActive = true;
		} else {
			isActive = false;
		}
		map.put("isActive", isActive);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {
		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		isActive = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		if (isActive) {
			viewSelect.setComboItem(messages.active());
		} else {
			viewSelect.setComboItem(messages.inActive());
		}
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	protected void initGrid() {
		grid = new EmployeeListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.employeeList();
	}

	@Override
	protected Action getAddNewAction() {
		return PayRollActions.newEmployeeAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addaNew(messages.employee());
	}

	@Override
	protected String getViewTitle() {
		return messages.employeeList();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void updateInGrid(ClientEmployee objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean filterBeforeShow() {
		return true;
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> selectTypes = new ArrayList<String>();
		selectTypes.add(messages.active());
		selectTypes.add(messages.inActive());
		viewSelect.setComboItem(messages.active());
		return selectTypes;
	}

}
