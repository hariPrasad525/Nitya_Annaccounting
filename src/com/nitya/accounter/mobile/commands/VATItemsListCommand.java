package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.TAXItem;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;

public class VATItemsListCommand extends AbstractCommand {

	private static final String CURRENT_VIEW = "currentView";

	private boolean isCompany;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(CURRENT_VIEW) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<TAXItem>("taxItemsList", getMessages()
				.pleaseSelect(getMessages().taxItem()), 20) {
			@Override
			protected Record createRecord(TAXItem value) {
				Record record = new Record(value);
				if (isCompany) {
					record.add(getMessages().taxItemName(), value.getName());
					record.add(getMessages().description(),
							value.getDescription());
					record.add(getMessages().taxRate(), value.getTaxRate());
					if (value.getTaxAgency() != null) {
						record.add(getMessages().taxAgency(), value
								.getTaxAgency().getName());
					} else {
						record.add(getMessages().taxAgency(), " ");
					}
				} else {
					record.add(getMessages().product(), value.getName());
					if (value.getTaxAgency() != null) {
						record.add(getMessages().taxAgency(), value
								.getTaxAgency().getName());
					} else {
						record.add(getMessages().taxAgency(), " ");
					}
					record.add(getMessages().description(),
							value.getDescription());
					record.add(getMessages().taxRate(), value.getTaxRate());
				}
				return record;
			}

			@Override
			protected String onSelection(TAXItem value) {
				return "updateTaxItem " + value.getID();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createTaxItem");
			}

			@Override
			protected boolean filter(TAXItem e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<TAXItem> getLists(Context context) {
				Set<TAXItem> completeList = vatItemssList(context);
				List<TAXItem> result = new ArrayList<TAXItem>();

				String type = VATItemsListCommand.this.get(CURRENT_VIEW)
						.getValue();

				for (TAXItem taxItem : completeList) {

					if (type.equals("Active")) {
						if (taxItem.isActive())

							result.add(taxItem);
					}
					if (type.equals("In-Active")) {
						if (!taxItem.isActive())
							result.add(taxItem);
					}

				}
				return result;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().vatItemList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

		});
	}

	private Set<TAXItem> vatItemssList(Context context) {
		return context.getCompany().getTaxItems();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, "You dnt have permission to do this.");
			return "cancel";
		}

		if (context.getString().equals("company")) {
			isCompany = true;
		}
		context.setString("");
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
		get(CURRENT_VIEW).setDefaultValue(getMessages().active());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

}
