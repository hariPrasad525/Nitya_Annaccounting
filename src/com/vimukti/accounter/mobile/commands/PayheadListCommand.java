package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class PayheadListCommand extends AbstractCommand {

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

		list.add(new ShowListRequirement<PayHead>(getMessages().payheadList(),
				getMessages().pleaseSelect(getMessages().payhead()), 20) {

			@Override
			protected String onSelection(PayHead value) {
				return "updatePayhead #" + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().payheadList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(PayHead value) {
				return PayheadListCommand.this.createRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				PayheadListCommand.this.setCreateCommand(list);
			}

			@Override
			protected boolean filter(PayHead e, String name) {

				return e.getName().startsWith(name)
						|| String.valueOf(e.getID()).startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<PayHead> getLists(Context context) {
				return getPayheadList(context);
			}

		});

	}

	protected PaginationList<PayHead> getPayheadList(Context context) {
		FinanceTool tool = new FinanceTool();
		PaginationList<ClientPayHead> payheadsList = null;
		PaginationList<PayHead> heads = new PaginationList<PayHead>();
		try {
			payheadsList = tool.getPayrollManager().getPayheadsList(0, -1,
					context.getCompany().getID());
			if (payheadsList != null) {
				for (ClientPayHead payHead : payheadsList) {
					PayHead head = null;
					head = new ServerConvertUtil().toServerObject(head,
							payHead, HibernateUtil.getCurrentSession());
					heads.add(head);
				}
			}
			return heads;
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return new PaginationList<PayHead>();
	}

	protected void setCreateCommand(CommandList list) {
		list.add("newPayHead");
	}

	protected Record createRecord(PayHead value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().paySlipName(),
				value.getNameToAppearInPaySlip());
		record.add(getMessages().payHeadType(), getPayHeadType(value.getType()));
		record.add(getMessages().calculationPeriod(),
				getCalculationPeriod(value.getCalculationType()));
		return record;
	}

	public String getPayHeadType(int type) {
		switch (type) {
		case PayHead.TYPE_EARNINGS_FOR_EMPLOYEES:
			return getMessages().earningsForEmployees();

		case PayHead.TYPE_DEDUCTIONS_FOR_EMPLOYEES:
			return getMessages().deductionsForEmployees();

		case PayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS:
			return getMessages().employeesStatutoryDeductions();

		case PayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS:
			return getMessages().employeesStatutoryContributions();

		case PayHead.TYPE_EMPLOYEES_OTHER_CHARGES:
			return getMessages().employeesOtherCharges();

		case PayHead.TYPE_BONUS:
			return getMessages().bonus();

		case PayHead.TYPE_LOANS_AND_ADVANCES:
			return getMessages().loansAndAdvances();

		case PayHead.TYPE_REIMBURSEMENTS_TO_EMPLOYEES:
			return getMessages().reimbursmentsToEmployees();

		default:
			return null;
		}
	}

	public String getCalculationPeriod(int type) {
		switch (type) {
		case PayHead.CALCULATION_PERIOD_DAYS:
			return getMessages().days();

		case PayHead.CALCULATION_PERIOD_WEEKS:
			return getMessages().weeks();

		case PayHead.CALCULATION_PERIOD_MONTHS:
			return getMessages().months();

		default:
			return "";
		}
	}

	public String getCalculationType(int type) {
		switch (type) {
		case PayHead.CALCULATION_TYPE_ON_ATTENDANCE:
			return getMessages().attendance();

		case PayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE:
			return getMessages().asComputedValue();

		case PayHead.CALCULATION_TYPE_FLAT_RATE:
			return getMessages().flatRate();

		case PayHead.CALCULATION_TYPE_ON_PRODUCTION:
			return getMessages().production();

		case PayHead.CALCULATION_TYPE_AS_USER_DEFINED:
			return getMessages().asUserDefined();

		default:
			return null;
		}
	}
}
