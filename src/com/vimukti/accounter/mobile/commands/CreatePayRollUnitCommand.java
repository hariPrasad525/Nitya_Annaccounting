package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.IntegerRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;

public class CreatePayRollUnitCommand extends AbstractCommand {

	private static final String UNIT_NAME = "unitName";

	private static final String FORMAL_NAME = "formalName";

	private static final String NO_OF_DECIMAL = "noOfDecimalPlaces";

	ClientPayrollUnit payrollUnit;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "payrollUnitList";
			}
			long numberFromString = getNumberFromString(string);
			payrollUnit = (ClientPayrollUnit) CommandUtils.getClientObjectById(
					numberFromString, AccounterCoreType.PAY_ROLL_UNIT,
					getCompanyId());
			if (payrollUnit == null) {
				return "payrollUnitList" + string;
			}
			setValues();
		} else {
			payrollUnit = new ClientPayrollUnit();
		}
		return null;
	}

	private void setValues() {
		get(UNIT_NAME).setValue(payrollUnit.getName());
		get(FORMAL_NAME).setValue(payrollUnit.getFormalname());
		get(NO_OF_DECIMAL).setValue(payrollUnit.getNoofDecimalPlaces());
	}

	@Override
	protected String getWelcomeMessage() {
		return payrollUnit.getID() == 0 ? getMessages().creating(
				getMessages().payrollUnit()) : getMessages().updating(
				getMessages().payrollUnit());
	}

	@Override
	protected String getDetailsMessage() {
		return payrollUnit.getID() == 0 ? getMessages().readyToCreate(
				getMessages().payrollUnit()) : getMessages().readyToUpdate(
				getMessages().payrollUnit());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return payrollUnit.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().payrollUnit()) : getMessages()
				.updateSuccessfully(getMessages().payrollUnit());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(UNIT_NAME, getMessages().pleaseEnter(
				getMessages().symbol()), getMessages().symbol(), false, true));

		list.add(new StringRequirement(FORMAL_NAME, getMessages().pleaseEnter(
				getMessages().formalName()), getMessages().formalName(), false,
				true));

		list.add(new IntegerRequirement(NO_OF_DECIMAL, getMessages()
				.pleaseEnter(getMessages().noOfDigitsAfterDecimal()),
				getMessages().noOfDigitsAfterDecimal(), false, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String formalname = get(FORMAL_NAME).getValue();
		payrollUnit.setFormalname(formalname);
		String symbol = get(UNIT_NAME).getValue();
		payrollUnit.setSymbol(symbol);
		int noOfDecimals = get(NO_OF_DECIMAL).getValue();
		payrollUnit.setNoofDecimalPlaces(noOfDecimals);
		create(payrollUnit, context);
		return null;
	}
}
