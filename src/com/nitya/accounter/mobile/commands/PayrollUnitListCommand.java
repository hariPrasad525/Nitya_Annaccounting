package com.nitya.accounter.mobile.commands;

import java.util.List;

import com.nitya.accounter.core.PayrollUnit;
import com.nitya.accounter.core.ServerConvertUtil;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientPayrollUnit;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.server.FinanceTool;

public class PayrollUnitListCommand extends AbstractCommand {

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

		list.add(new ShowListRequirement<PayrollUnit>(getMessages()
				.payrollUnitList(), getMessages().pleaseSelect(
				getMessages().payrollUnit()), 20) {

			@Override
			protected String onSelection(PayrollUnit value) {
				return "updatePayrollUnit #" + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().payrollUnitList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(PayrollUnit value) {
				return PayrollUnitListCommand.this.createRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				PayrollUnitListCommand.this.setCreateCommand(list);
			}

			@Override
			protected boolean filter(PayrollUnit e, String name) {
				return e.getFormalname().startsWith(name)
						|| String.valueOf(e.getID()).startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<PayrollUnit> getLists(Context context) {
				return getPayrollUnitList(context);
			}

		});

	}

	protected List<PayrollUnit> getPayrollUnitList(Context context) {
		try {
			PaginationList<ClientPayrollUnit> payrollUnitsList = new FinanceTool()
					.getPayrollManager().getPayrollUnitsList(0, -1,
							context.getCompany().getID());
			PaginationList<PayrollUnit> payrollUnits = new PaginationList<PayrollUnit>();
			if (payrollUnitsList != null) {
				for (ClientPayrollUnit clientPayrollUnit : payrollUnitsList) {
					PayrollUnit unit = null;
					unit = new ServerConvertUtil().toServerObject(unit,
							clientPayrollUnit,
							HibernateUtil.getCurrentSession());
					payrollUnits.add(unit);
				}
				return payrollUnits;
			}
			return new PaginationList<PayrollUnit>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void setCreateCommand(CommandList list) {
		list.add("createPayrollUnit");
	}

	protected Record createRecord(PayrollUnit value) {
		Record record = new Record(value);
		record.add(getMessages().symbol(), value.getSymbol());
		record.add(getMessages().formalName(), value.getFormalname());
		record.add(getMessages().noOfDecimalPlaces(),
				value.getNoofDecimalPlaces());
		return record;
	}

}
