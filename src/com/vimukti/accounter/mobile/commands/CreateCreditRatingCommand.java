package com.vimukti.accounter.mobile.commands;

import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCreditRating;

public class CreateCreditRatingCommand extends AbstractCommand {

	private static final String CREDIT_RATING_NAME = "CreditRationgName";

	private ClientCreditRating creditRating;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(CREDIT_RATING_NAME, getMessages()
				.pleaseEnter(getMessages().creditRatingName()), getMessages()
				.creditRatingName(), false, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		creditRating.setName(get(CREDIT_RATING_NAME).getValue().toString());
		create(creditRating, context);
		return null;

	}

	@Override
	protected String getDetailsMessage() {
		return creditRating.getID() == 0 ? getMessages().readyToCreate(
				getMessages().creditRating()) : getMessages().readyToUpdate(
				getMessages().creditRating());
	}

	@Override
	public String getSuccessMessage() {
		return creditRating.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().creditRating()) : getMessages()
				.createSuccessfully(getMessages().creditRating());
	}

	@Override
	protected String getWelcomeMessage() {
		return creditRating.getID() == 0 ? getMessages().creating(
				getMessages().creditRating()) : getMessages().readyToUpdate(
				getMessages().creditRating());
	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = creditRating.getID();
		return id != 0 ? "deleteCreditRating " + id : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				return "creditRatingList";
			}
			Set<CreditRating> shippingMethods = context.getCompany()
					.getCreditRatings();
			for (CreditRating shippingMethod : shippingMethods) {
				if (shippingMethod.getName().equals(string)) {
					creditRating = (ClientCreditRating) CommandUtils
							.getClientObjectById(shippingMethod.getID(),
									AccounterCoreType.CREDIT_RATING,
									getCompanyId());
				}
			}
			if (creditRating == null) {
				return "creditRatingList " + string;
			}
			get(CREDIT_RATING_NAME).setValue(creditRating.getName());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(CREDIT_RATING_NAME).setValue(string);
			}
			creditRating = new ClientCreditRating();
		}
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}
}
