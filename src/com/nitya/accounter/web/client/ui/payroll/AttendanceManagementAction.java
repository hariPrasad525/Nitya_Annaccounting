package com.nitya.accounter.web.client.ui.payroll;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientAttendanceManagement;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.AccounterAsync;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class AttendanceManagementAction extends
		Action<ClientAttendanceManagement> {

	public AttendanceManagementAction() {
		super();
	}

	@Override
	public String getText() {
		return messages.attendanceManagement();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientAttendanceManagement data,
			final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				AttendanceManagementView view = new AttendanceManagementView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, AttendanceManagementAction.this);
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.ATTENDANCE_MANAGEMENT;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
