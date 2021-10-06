package com.nitya.accounter.web.client.ui.payroll;

import java.util.HashMap;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.BaseListView;
import com.nitya.accounter.web.client.ui.core.PayRollActions;

public class AttendanceOrProductionTypeListView extends
		BaseListView<ClientAttendanceOrProductionType> {

	public AttendanceOrProductionTypeListView() {
		this.getElement().setId("AttendanceOrProductionTypeListView");
		isViewSelectRequired = false;
	}

	@Override
	public void updateInGrid(ClientAttendanceOrProductionType objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new AttendanceOrProductionTypeListGrid(false) {
			@Override
			protected void updateGrid() {
				onPageChange(0, getPageSize());
			}
		};
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.attendanceOrProductionTypeList();
	}

	@Override
	protected Action getAddNewAction() {
		PayRollActions newAttendanceProductionTypeAction = PayRollActions
				.newAttendanceProductionTypeAction();
		newAttendanceProductionTypeAction
				.setCallback(new ActionCallback<ClientAttendanceOrProductionType>() {

					@Override
					public void actionResult(
							ClientAttendanceOrProductionType result) {
						onPageChange(0, getPageSize());
					}
				});
		return newAttendanceProductionTypeAction;
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addNew(messages.attendanceOrProductionType());
	}

	@Override
	protected String getViewTitle() {
		return messages.attendanceOrProductionTypeList();
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
		Accounter.createPayrollService().getAttendanceProductionTypes(start,
				length, this);
	}

	@Override
	public void onSuccess(
			PaginationList<ClientAttendanceOrProductionType> result) {
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

}
