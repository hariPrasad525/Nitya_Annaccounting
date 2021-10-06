package com.nitya.accounter.mobile.requirements;

import java.util.List;

import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.web.client.core.ClientAttendanceManagementItem;

public class AttendanceTableRequirement extends
		AbstractTableRequirement<ClientAttendanceManagementItem> {

	public AttendanceTableRequirement(String requirementName,
			String enterString, String recordName, boolean isCreatable,
			boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isCreatable,
				isOptional, isAllowFromContext);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getEmptyString() {
		return null;
	}

	@Override
	protected void getRequirementsValues(ClientAttendanceManagementItem obj) {

	}

	@Override
	protected void setRequirementsDefaultValues(
			ClientAttendanceManagementItem obj) {

	}

	@Override
	protected ClientAttendanceManagementItem getNewObject() {
		return null;
	}

	@Override
	protected Record createFullRecord(ClientAttendanceManagementItem t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<ClientAttendanceManagementItem> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createRecord(ClientAttendanceManagementItem t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAddMoreString() {
		// TODO Auto-generated method stub
		return null;
	}

}
