package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.FixedAsset;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;

public class SoldAndDisposedItemsListCommand extends AbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<FixedAsset>(getMessages()
				.fixedAssets(), getMessages().pleaseSelect(
				getMessages().fixedAsset()), 20) {

			@Override
			protected String onSelection(FixedAsset value) {
				return "updateFixedAsset " + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().soldAndDisposedItems();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(FixedAsset value) {
				return SoldAndDisposedItemsListCommand.this.createRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				SoldAndDisposedItemsListCommand.this.setCreateCommand(list);
			}

			@Override
			protected boolean filter(FixedAsset e, String name) {

				return e.getName().startsWith(name)
						|| String.valueOf(e.getID()).startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<FixedAsset> getLists(Context context) {
				Set<FixedAsset> assets = getFixedAssets(context);
				List<FixedAsset> result = new ArrayList<FixedAsset>();
				for (FixedAsset asset : assets) {
					if (asset.getStatus() == FixedAsset.STATUS_SOLD_OR_DISPOSED)
						result.add(asset);
				}
				return result;
			}

		});

	}

	protected Record createRecord(FixedAsset value) {
		Record record = new Record(value);
		record.add(getMessages().item(), value.getName());
		record.add(getMessages().assetNumber(), value.getAssetNumber());
		record.add(getMessages().account(), value.getAssetAccount().getName());
		record.add(getMessages().disposalDate(), value.getSoldOrDisposedDate());
		record.add(getMessages().disposalPrice(), getPreferences()
				.getPrimaryCurrency().getSymbol() + " " + value.getSalePrice());
		record.add(getMessages().gainsOrLosses(), getPreferences()
				.getPrimaryCurrency().getSymbol() + " " + value.getLossOrGain());
		return record;
	}

	protected void setCreateCommand(CommandList list) {
		list.add("newFixedAsset");
		return;
	}

	protected Set<FixedAsset> getFixedAssets(Context context) {
		return context.getCompany().getFixedAssets();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
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

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

}
