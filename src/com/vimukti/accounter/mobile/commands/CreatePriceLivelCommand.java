package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;

public class CreatePriceLivelCommand extends AbstractCommand {

	private static final String PRICE_LEVEL_NAME = "pricelevelName";

	private static final String PRICE_LEVEL_PERCENT = "levelPercentage";

	private static final String IS_INCREASEIN_PERCENT = "isIncreaseInPercent";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().priceLevel());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().priceLevel());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(PRICE_LEVEL_PERCENT).setValue(1.0);
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().priceLevel());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(PRICE_LEVEL_NAME,
				getMessages()
						.pleaseEnter(
								getMessages().priceLevel() + " "
										+ getMessages().name()), getMessages()
						.priceLevel() + " " + getMessages().name(), false, true));
		list.add(new AmountRequirement(PRICE_LEVEL_PERCENT, getMessages()
				.pleaseEnter(getMessages().percentage()), getMessages()
				.percentage(), false, true) {
			@Override
			protected String getDisplayValue(Double value) {
				return super.getDisplayValue(value) + "%";
			}
		});
		list.add(new BooleanRequirement(IS_INCREASEIN_PERCENT, true) {

			@Override
			protected String getTrueString() {
				return getMessages().increasePriceLevelByThisPercentage();
			}

			@Override
			protected String getFalseString() {
				return getMessages().decreasePriceLevelByThisPercentage();
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientPriceLevel priceLevel = new ClientPriceLevel();
		String priceLevelName = get(PRICE_LEVEL_NAME).getValue();
		priceLevel.setName(priceLevelName);
		Double percentage = get(PRICE_LEVEL_PERCENT).getValue();
		priceLevel.setPercentage(percentage);
		Boolean isIncreaseInPercent = get(IS_INCREASEIN_PERCENT).getValue();
		priceLevel.setPriceLevelDecreaseByThisPercentage(isIncreaseInPercent);
		create(priceLevel, context);
		return super.onCompleteProcess(context);
	}
}
