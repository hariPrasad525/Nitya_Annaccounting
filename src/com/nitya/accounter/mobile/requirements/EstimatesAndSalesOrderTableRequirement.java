package com.nitya.accounter.mobile.requirements;

import java.util.List;

import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;

public abstract class EstimatesAndSalesOrderTableRequirement extends
		AbstractTableRequirement<EstimatesAndSalesOrdersList> {

	public EstimatesAndSalesOrderTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, true, true);
		// setDefaultValue(new ArrayList<EstimatesAndSalesOrdersList>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {

	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getMessages().estimate());
	}

	@Override
	protected void getRequirementsValues(EstimatesAndSalesOrdersList obj) {

	}

	@Override
	protected void setRequirementsDefaultValues(EstimatesAndSalesOrdersList obj) {

	}

	@Override
	protected boolean getIsCreatableObject() {
		return true;
	}

	@Override
	protected void addCreateCommands(CommandList commandList) {

	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		List<EstimatesAndSalesOrdersList> list2 = getList();
		List<EstimatesAndSalesOrdersList> value = getValue();
		if (!list2.isEmpty() || (value != null && !value.isEmpty())) {
			return super.run(context, makeResult, list, actions);
		} else {
			return null;
		}
	}

	@Override
	protected EstimatesAndSalesOrdersList getNewObject() {
		return new EstimatesAndSalesOrdersList();
	}

	@Override
	protected Record createFullRecord(EstimatesAndSalesOrdersList value) {
		Record rec = new Record(value);
		String name = getMessages().quote();
		int type = value.getEstimateType();
		if (type == ClientEstimate.CHARGES) {
			name = getMessages().charge();
		} else if (type == ClientEstimate.CREDITS) {
			name = getMessages().credit();
		} else if (type == ClientEstimate.SALES_ORDER) {
			name = getMessages().salesOrder();
		} else if (type == ClientEstimate.BILLABLEEXAPENSES) {
			name = getMessages().billabe();
		}

		rec.add(name, value.getTotal());

		return rec;
	}

	@Override
	protected Record createRecord(EstimatesAndSalesOrdersList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<EstimatesAndSalesOrdersList> oldValues = getValue();
		return (oldValues == null || oldValues.isEmpty()) ? getMessages()
				.addOf(getMessages().quotes()) : getMessages().addMore(
				getMessages().quotes());
	}

	@Override
	protected boolean contains(List<EstimatesAndSalesOrdersList> oldValues,
			EstimatesAndSalesOrdersList t) {
		for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : oldValues) {
			if (estimatesAndSalesOrdersList.getTransactionId() == t
					.getTransactionId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean isRequirementsEmpty() {
		return false;
	}

	@Override
	public void setOtherFields(ResultList list, EstimatesAndSalesOrdersList obj) {
		list.add(createFullRecord(obj));
		super.setOtherFields(list, obj);
	}
}