package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EmployeeAndEmployeeGroupRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPayRun;

public class CreatePayRunCommand extends AbstractCommand {

	ClientPayRun payRun;

	private static final String WORKING_DAYS = "workingDays";

	private static final String TRANSACTION_DATE = "transactionDate";

	private static final String NUMBER = "number";

	private static final String EMPLOYEE_OR_GROUP = "employeeOrGroup";

	private static final String FROM_DATE = "fromDate";

	private static final String TO_DATE = "toDate";

	private static final String ATTENDANCE_TABLE = "attendanceTable";

	private static final String PAY_RUN_TABLE = "payRunTable";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "payRuns";
			}
			long numberFromString = getNumberFromString(string);
			payRun = (ClientPayRun) CommandUtils
					.getClientObjectById(numberFromString,
							AccounterCoreType.PAY_RUN, getCompanyId());
			if (payRun == null) {
				return "payRuns" + string;
			}
			setValues();
		} else {
			payRun = new ClientPayRun();
		}
		return null;
	}

	private void setValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getWelcomeMessage() {
		return payRun.getID() == 0 ? getMessages().creating(
				getMessages().payrun()) : getMessages().updating(
				getMessages().payrun());
	}

	@Override
	protected String getDetailsMessage() {
		return payRun.getID() == 0 ? getMessages().readyToCreate(
				getMessages().payrun()) : getMessages().readyToUpdate(
				getMessages().payrun());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return payRun.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().payrun()) : getMessages().updateSuccessfully(
				getMessages().payrun());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NumberRequirement(WORKING_DAYS, getMessages().pleaseEnter(
				getMessages().noOfWorkingDays()), getMessages()
				.noOfWorkingDays(), false, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), false, true));

		list.add(new DateRequirement(TRANSACTION_DATE, getMessages()
				.pleaseEnter(getMessages().date()), getMessages().date(),
				false, true));

		list.add(new EmployeeAndEmployeeGroupRequirement(EMPLOYEE_OR_GROUP,
				getMessages().pleaseSelect(getMessages().employeeOrGroup()),
				getMessages().employeeOrGroup(), null));

		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().fromDate()), getMessages().fromDate(), false,
				true));

		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().toDate()), getMessages().toDate(), false, true));
	}

}
