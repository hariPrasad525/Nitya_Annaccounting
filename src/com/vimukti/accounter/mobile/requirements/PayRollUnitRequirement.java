package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.PayrollUnit;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.utils.HibernateUtil;

public class PayRollUnitRequirement extends ListRequirement<PayrollUnit> {

	public PayRollUnitRequirement(String requirementName, String enterString,
			String recordName, ChangeListner<PayrollUnit> listner) {
		super(requirementName, enterString, recordName, false, true, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().payrollUnitList());
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().payrollUnit());
	}

	@Override
	protected Record createRecord(PayrollUnit value) {
		Record record = new Record(value);
		record.add(
				getMessages().unitName(),
				value == null ? "" : value.getSymbol() + " "
						+ value.getFormalname());
		return record;
	}

	@Override
	protected String getDisplayValue(PayrollUnit value) {
		return value == null ? "" : value.getSymbol() + " "
				+ value.getFormalname();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newPayrollUnit");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().payrollUnit());
	}

	@Override
	protected boolean filter(PayrollUnit e, String name) {
		return e.getName().equals(name);
	}

	@Override
	protected List<PayrollUnit> getLists(Context context) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("list.PayrollUnit").setEntity(
				"company", getCompany());
		List<PayrollUnit> units = query.list();
		return units;
	}

}
