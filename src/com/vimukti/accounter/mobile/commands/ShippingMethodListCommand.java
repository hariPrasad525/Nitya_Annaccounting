package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class ShippingMethodListCommand extends AbstractCommand {
	private static final String SHIPPING_METHODS = "ShiipingMethods";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<ShippingMethod>(SHIPPING_METHODS,
				null, 20) {
			@Override
			protected Record createRecord(ShippingMethod value) {
				Record record = new Record(value);
				record.add(getMessages().shippingMethod(), value.getName());
				record.add(getMessages().description(), value.getDescription());
				return record;
			}

			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// ShippingMethod value) {
			//
			// commandList.add(new UserCommand("Edit Shipping Method", value
			// .getName()));
			//
			// commandList.add(new UserCommand("Delete Shipping Method", value
			// .getName()));
			//
			// }
			@Override
			protected String onSelection(ShippingMethod value) {
				return "editShippingMethod " + value.getID();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newShippingMethod");
			}

			@Override
			protected boolean filter(ShippingMethod e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());

			}

			@Override
			protected List<ShippingMethod> getLists(Context context) {
				List<ShippingMethod> list = new ArrayList<ShippingMethod>();
				Set<ShippingMethod> methods = context.getCompany()
						.getShippingMethods();
				for (ShippingMethod a : methods) {
					list.add(a);
				}
				return list;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

		});
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return null;
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
