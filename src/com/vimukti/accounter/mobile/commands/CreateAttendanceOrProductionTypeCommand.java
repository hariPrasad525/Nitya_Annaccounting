package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.PayrollUnit;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.PayRollUnitRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientPayHead;

public class CreateAttendanceOrProductionTypeCommand extends AbstractCommand {

	private static final String ATT_OR_PRO_TYPE = "attOrProType";

	private static final String LEAVE_TYPE = "leaveType";

	private static final String PAY_ROLL_UNIT = "payrollUnit";

	ClientAttendanceOrProductionType attOrProType;

	private ArrayList<String> leaveTypes = new ArrayList<String>();

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "attendanceOrProductionTypeList";
			}
			long numberFromString = getNumberFromString(string);
			attOrProType = (ClientAttendanceOrProductionType) CommandUtils
					.getClientObjectById(numberFromString,
							AccounterCoreType.ATTENDANCE_PRODUCTION_TYPE,
							getCompanyId());
			if (attOrProType == null) {
				return "attendanceOrProductionTypeList" + string;
			}
			setValues();
		} else {
			attOrProType = new ClientAttendanceOrProductionType();
		}
		return null;
	}

	private void setValues() {
		get(ATT_OR_PRO_TYPE).setValue(attOrProType.getName());
		get(LEAVE_TYPE).setValue(
				ClientAttendanceOrProductionType.getTypeName(attOrProType
						.getType()));
		get(PAY_ROLL_UNIT).setValue(
				getServerObject(PayrollUnit.class, attOrProType.getUnit()));
	}

	@Override
	protected String getWelcomeMessage() {
		return attOrProType.getID() == 0 ? getMessages().creating(
				getMessages().attendanceOrProductionType()) : getMessages()
				.updating(getMessages().attendanceOrProductionType());
	}

	@Override
	protected String getDetailsMessage() {
		return attOrProType.getID() == 0 ? getMessages().readyToCreate(
				getMessages().attendanceOrProductionType()) : getMessages()
				.readyToUpdate(getMessages().attendanceOrProductionType());
	}

	@Override
	protected void setDefaultValues(Context context) {
		leaveTypes.add(getMessages().productionType());
		leaveTypes.add(getMessages().userDefindCalendar());
	}

	@Override
	public String getSuccessMessage() {
		return attOrProType.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().attendanceOrProductionType()) : getMessages()
				.updateSuccessfully(getMessages().attendanceOrProductionType());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(ATT_OR_PRO_TYPE, getMessages()
				.pleaseEnter(getMessages().name()), getMessages().name(),
				false, true));

		list.add(new StringListRequirement(LEAVE_TYPE, getMessages()
				.pleaseSelect(getMessages().attendanceOrProductionType()),
				getMessages().attendanceOrProductionType(), false, true, null) {

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().attendanceOrProductionType());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().attendanceOrProductionType());
			}

			@Override
			protected List<String> getLists(Context context) {
				return leaveTypes;
			}

		});

		list.add(new PayRollUnitRequirement(PAY_ROLL_UNIT, getMessages()
				.pleaseSelect(getMessages().payrollUnit()), getMessages()
				.payrollUnit(), null) {
			public Result run(Context context, Result makeResult,
					com.vimukti.accounter.mobile.ResultList list,
					com.vimukti.accounter.mobile.ResultList actions) {
				String value = CreateAttendanceOrProductionTypeCommand.this
						.get(LEAVE_TYPE).getValue();
				if (value.equals(getMessages().productionType())) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			};

		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(ATT_OR_PRO_TYPE).getValue();
		attOrProType.setName(name);
		String leaveType = get(LEAVE_TYPE).getValue();
		attOrProType.setType(leaveTypes.indexOf(leaveType) + 1);
		if (attOrProType.getType() != ClientAttendanceOrProductionType.TYPE_PRODUCTION) {
			attOrProType.setPeriodType(ClientPayHead.CALCULATION_PERIOD_DAYS);
		} else {
			PayrollUnit selectedValue = get(PAY_ROLL_UNIT).getValue();
			if (selectedValue != null) {
				attOrProType.setUnit(selectedValue.getID());
			}
		}
		create(attOrProType, context);
		return null;
	}
}
