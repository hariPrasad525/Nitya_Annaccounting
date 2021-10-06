package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.TaxItemsTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;

public class CreateTaxGroupCommand extends AbstractCommand {

	private static final String TAX_ITEMS_LIST = "taxItemsList";
	private static final String NAME = "name";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(NAME, getMessages().pleaseEnter(
				getMessages().taxGroup() + " " + getMessages().name()),
				getMessages().taxGroup(), false, true));
		list.add(new TaxItemsTableRequirement(TAX_ITEMS_LIST, getMessages()
				.pleaseSelect(getMessages().taxItemsList()), getMessages()
				.taxItemsList()) {

			@Override
			protected List<ClientTAXItem> getList() {
				return CommandUtils.getClientTaxItems(getCompanyId());
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(NAME).getValue();
		List<ClientTAXItem> taxItems = get(TAX_ITEMS_LIST).getValue();
		ClientTAXGroup taxGroup = new ClientTAXGroup();
		taxGroup.setName(name);
		taxGroup.setActive(true);
		taxGroup.setPercentage(true);
		taxGroup.setSalesType(true);
		taxGroup.setTaxItems(taxItems);
		create(taxGroup, context);
		return null;

	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().taxGroup());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().taxGroup());
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().taxGroup());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
		}
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}
}
