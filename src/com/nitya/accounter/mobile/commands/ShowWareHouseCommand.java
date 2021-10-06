package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Item;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientItemStatus;
import com.nitya.accounter.web.client.core.ClientWarehouse;

public class ShowWareHouseCommand extends AbstractCommand {

	ClientWarehouse warehouse;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string.isEmpty()) {
			addFirstMessage(
					context,
					getMessages().selectATransactionToUpdate(
							getMessages().wareHouse()));
			return "warehouseList";
		}
		warehouse = CommandUtils.getWareHouse(context.getCompany(), string);
		if (warehouse == null) {
			addFirstMessage(
					context,
					getMessages().selectATransactionToUpdate(
							getMessages().wareHouse()));
			return "warehouseList " + string;
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().wareHouse();
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().details();
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<ClientItemStatus>("warehouseitems",
				"", 100) {

			@Override
			protected String onSelection(ClientItemStatus value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().wareHouseItems());
			}

			@Override
			protected Record createRecord(ClientItemStatus value) {
				Record itemrRecord = new Record(value);
				Item item = (Item) CommandUtils.getServerObjectById(
						value.getItem(), AccounterCoreType.ITEM);
				itemrRecord.add(getMessages().itemName(), item.getName());
				itemrRecord.add(getMessages().quantity(), value.getQuantity()
						.getValue());
				return itemrRecord;
			}

			@Override
			public boolean isEditable() {
				return false;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected boolean filter(ClientItemStatus e, String name) {
				return true;
			}

			@Override
			protected List<ClientItemStatus> getLists(Context context) {
				return new ArrayList<ClientItemStatus>(warehouse
						.getItemStatuses());
			}
		});

	}

	@Override
	protected String getThirdCommand(Context context) {
		return "updateWarehouse " + warehouse.getName();
	}

	@Override
	protected String getThirdCommandString() {
		return "Edit";
	}

	@Override
	protected String getDeleteCommand(Context context) {
		return "deleteWareHouse " + warehouse.getID();
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		return super.onCompleteProcess(context);
	}
}
