package com.nitya.accounter.mobile.requirements;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.AttendanceOrProductionType;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.utils.HibernateUtil;

public class AttendanceOrProductionTypeRequirement extends
		ListRequirement<AttendanceOrProductionType> {

	public AttendanceOrProductionTypeRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, true, null);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(
				getMessages().attendanceOrProductionTypeList());
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(
				getMessages().attendanceOrProductionType());
	}

	@Override
	protected Record createRecord(AttendanceOrProductionType value) {
		Record record = new Record(value);
		record.add(getMessages().attendanceOrProductionType(),
				value == null ? "" : value.getName());
		record.add(getMessages().period(), value == null ? ""
				: getNameByType(value.getPeriodType()));
		return record;
	}

	@Override
	protected String getDisplayValue(AttendanceOrProductionType value) {
		return value == null ? "" : value.getName();
	}

	private String getNameByType(int type) {
		switch (type) {
		case AttendanceOrProductionType.TYPE_LEAVE_WITH_PAY:
			return getMessages().leaveWithPay();

		case AttendanceOrProductionType.TYPE_LEAVE_WITHOUT_PAY:
			return getMessages().leaveWithoutPay();

		case AttendanceOrProductionType.TYPE_PRODUCTION:
			return getMessages().productionType();

		case AttendanceOrProductionType.TYPE_USER_DEFINED_CALENDAR:
			return getMessages().userDefinedCalendar();

		default:
			return null;
		}
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newAttOrProductionType");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(
				getMessages().attendanceOrProductionType());
	}

	@Override
	protected boolean filter(AttendanceOrProductionType e, String name) {
		return e.getName().equals(name);
	}

	@Override
	protected List<AttendanceOrProductionType> getLists(Context context) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("list.AttendanceProductionType")
				.setEntity("company", getCompany());
		List<AttendanceOrProductionType> types = query.list();
		return types;
	}

}
