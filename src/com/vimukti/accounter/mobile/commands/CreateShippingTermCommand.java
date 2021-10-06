package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;

public class CreateShippingTermCommand extends AbstractCommand {

	protected static final String SHIPPING_TERMNAME = "shippingTermsName";
	protected static final String DESCRIPTION = "description";

	private ClientShippingTerms shippingTerm;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(SHIPPING_TERMNAME, getMessages()
				.pleaseEnter(getMessages().shippingTerm()), getMessages()
				.shippingTerm(), false, true));
		list.add(new NameRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().description()), getMessages().description(),
				true, true));
	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = shippingTerm.getID();
		return id != 0 ? "deleteShippingTerm " + id : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().shippingTerm()));
				return "shippingTerms";
			}
			ClientShippingTerms shippingTermsByName = CommandUtils
					.getshippingTermsByNameByName(context.getCompany(), string);
			if (shippingTermsByName == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().shippingTerm()));
				return "shippingTerms " + string.trim();
			}
			shippingTerm = shippingTermsByName;
			get(SHIPPING_TERMNAME).setValue(shippingTerm.getName());
			get(DESCRIPTION).setValue(shippingTerm.getDescription());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(SHIPPING_TERMNAME).setValue(string);
			}
			shippingTerm = new ClientShippingTerms();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return shippingTerm.getID() == 0 ? getMessages().create(
				getMessages().shippingTerm()) : getMessages().updating(
				getMessages().shippingTerm());
	}

	@Override
	protected String getDetailsMessage() {
		if (shippingTerm.getID() == 0) {
			return getMessages().readyToCreate(getMessages().shippingTerm());
		} else {
			return getMessages().readyToUpdate(getMessages().shippingTerm());
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		if (shippingTerm.getID() == 0) {
			return getMessages().createSuccessfully(
					getMessages().shippingTerm());
		} else {
			return getMessages().updateSuccessfully(
					getMessages().shippingTerm());
		}
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		shippingTerm.setName((String) get(SHIPPING_TERMNAME).getValue());
		shippingTerm.setDescription((String) get(DESCRIPTION).getValue());
		create(shippingTerm, context);
		markDone();
		return null;
	}
}
