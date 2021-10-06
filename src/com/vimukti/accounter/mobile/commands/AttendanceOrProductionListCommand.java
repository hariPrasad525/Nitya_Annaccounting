package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.AttendanceOrProductionType;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.server.FinanceTool;

public class AttendanceOrProductionListCommand extends AbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<AttendanceOrProductionType>(
				getMessages().attendanceOrProductionTypeList(), getMessages()
						.pleaseSelect(
								getMessages().attendanceOrProductionType()), 20) {

			@Override
			protected String onSelection(AttendanceOrProductionType value) {
				return "updateAttOrProductionType #" + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().attendanceOrProductionTypeList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(AttendanceOrProductionType value) {
				return AttendanceOrProductionListCommand.this
						.createRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				AttendanceOrProductionListCommand.this.setCreateCommand(list);
			}

			@Override
			protected boolean filter(AttendanceOrProductionType e, String name) {
				return e.getName().startsWith(name)
						|| String.valueOf(e.getID()).startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<AttendanceOrProductionType> getLists(Context context) {
				return getAttendanceOrProductionsList(context);
			}

		});

	}

	protected List<AttendanceOrProductionType> getAttendanceOrProductionsList(
			Context context) {
		try {
			PaginationList<ClientAttendanceOrProductionType> attendanceProductionTypes = new FinanceTool()
					.getPayrollManager().getAttendanceProductionTypes(0, -1,
							context.getCompany().getID());
			PaginationList<AttendanceOrProductionType> productionTypes = new PaginationList<AttendanceOrProductionType>();
			if (attendanceProductionTypes != null) {
				for (ClientAttendanceOrProductionType productionType : attendanceProductionTypes) {
					AttendanceOrProductionType type = null;
					type = new ServerConvertUtil().toServerObject(type,
							productionType, HibernateUtil.getCurrentSession());
					productionTypes.add(type);
				}
				return productionTypes;
			}
			return new PaginationList<AttendanceOrProductionType>();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	protected void setCreateCommand(CommandList list) {
		list.add("newAttOrProductionType");
	}

	protected Record createRecord(AttendanceOrProductionType value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().type(), value.getType());
		record.add(getMessages().period(), value.getPeriodType());
		return record;
	}

}
