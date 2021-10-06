package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class ShippingTermsListCommand extends AbstractCommand {
	private static final String SHIPPING_TERMS = "ShiipingTerms";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<ShippingTerms>(SHIPPING_TERMS, null,
				20) {
			@Override
			protected Record createRecord(ShippingTerms value) {
				Record record = new Record(value);
				record.add(getMessages().shippingTerm(), value.getName());
				record.add(getMessages().description(), value.getDescription());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newShippingTerm");
			}

			@Override
			protected boolean filter(ShippingTerms e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}

			@Override
			protected List<ShippingTerms> getLists(Context context) {
				List<ShippingTerms> list = new ArrayList<ShippingTerms>();
				Set<ShippingTerms> terms = context.getCompany()
						.getShippingTerms();
				for (ShippingTerms a : terms) {
					list.add(a);
				}
				return list;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().shippingTermList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(ShippingTerms value) {
				return "updateShippingTerm " + value.getName();
			}
		});
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

}
