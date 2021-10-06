package com.vimukti.accounter.web.client.ui.payroll;

import java.util.HashMap;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.PayRollActions;

public class PayrollUnitListView extends BaseListView<ClientPayrollUnit> {

	public PayrollUnitListView() {
		this.getElement().setId("PayrollUnitListView");
		isViewSelectRequired = false;
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
	protected String getListViewHeading() {
		return messages.payrollUnitList();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Action<ClientPayrollUnit> getAddNewAction() {
		PayRollActions newPayRollUnitAction = PayRollActions
				.newPayRollUnitAction();
		newPayRollUnitAction.setFromEmployeeView(true);
		newPayRollUnitAction
				.setCallback(new ActionCallback<ClientPayrollUnit>() {

					@Override
					public void actionResult(ClientPayrollUnit result) {
						onPageChange(0, getPageSize());
					}
				});
		return newPayRollUnitAction;
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addaNew(messages.payrollUnit());
	}

	@Override
	protected String getViewTitle() {
		return messages.payrollUnitList();
	}

	@Override
	protected void filterList(boolean isActive) {
		this.isActive = isActive;
		onPageChange(0, getPageSize());
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
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		isActive = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		if (isActive) {
			viewSelect.setComboItem(messages.active());
		} else {
			viewSelect.setComboItem(messages.inActive());
		}

	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createPayrollService().getPayrollUnitsList(start, length,
				this);
	}

	@Override
	public void onSuccess(PaginationList<ClientPayrollUnit> result) {
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
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

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

	@Override
	protected void initGrid() {
		grid = new PayrollUnitListGrid(false) {
			@Override
			protected void updateGrid() {
				onPageChange(0, getPageSize());
			}
		};
		grid.init();
	}

	@Override
	public void updateInGrid(ClientPayrollUnit objectTobeModified) {
		// TODO Auto-generated method stub

	}
}
