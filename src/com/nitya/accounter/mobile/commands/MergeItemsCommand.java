package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.ItemRequirement;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.server.FinanceTool;

public class MergeItemsCommand extends AbstractCommand {

	private static final String ITEM_FROM = "itemFrom";
	private static final String ITEM_TO = "itemTo";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().merging(getMessages().items());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToMerge(getMessages().items());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().mergingCompleted(getMessages().items());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ItemRequirement(ITEM_FROM, getMessages().payeeFrom(
				getMessages().item()), getMessages().item(), false, true, null,
				false) {

			@Override
			protected List<Item> getLists(Context context) {
				return new ArrayList<Item>(getCompany().getItems());
			}

			@Override
			public void setValue(Object value) {
				Item itemTo = get(ITEM_TO).getValue();
				Item itemFrom = (Item) value;
				String checkDifferentItems = null;
				if (itemFrom != null && itemTo != null) {
					checkDifferentItems = checkDifferentItems(itemFrom, itemTo);
				}
				if (checkDifferentItems != null) {
					addFirstMessage(checkDifferentItems);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeFrom(getMessages().item()));
			}

			@Override
			protected String getSetMessage() {
				Item value = getValue();
				if (value != null) {
					return getMessages().selectedAs(value.getName(),
							getMessages().payeeFrom(getMessages().item()));
				}
				return null;
			}
		});

		list.add(new ItemRequirement(ITEM_TO, getMessages().payeeTo(
				getMessages().item()), getMessages().item(), false, true, null,
				false) {

			@Override
			protected List<Item> getLists(Context context) {
				return new ArrayList<Item>(getCompany().getItems());
			}

			@Override
			public void setValue(Object value) {
				Item itemFrom = get(ITEM_FROM).getValue();
				Item itemTo = (Item) value;
				String checkDifferentItems = null;
				if (itemFrom != null && itemTo != null) {
					checkDifferentItems = checkDifferentItems(itemFrom, itemTo);
				}
				if (checkDifferentItems != null) {
					addFirstMessage(checkDifferentItems);
					super.setValue(null);
					return;
				}
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeTo(getMessages().item()));
			}

			@Override
			protected String getSetMessage() {
				Item value = getValue();
				if (value != null) {
					return getMessages().selectedAs(value.getName(),
							getMessages().payeeTo(getMessages().item()));
				}
				return null;
			}
		});

	}

	protected String checkDifferentItems(Item itemFrom, Item itemTo) {
		if (itemFrom.getID() == itemTo.getID()) {
			return getMessages().notMove(Global.get().customers());
		}
		if (itemFrom.getType() != itemTo.getType()) {
			return getMessages().typesMustbeSame(getMessages().items());
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
		Item itemFrom = get(ITEM_FROM).getValue();
		Item itemTo = get(ITEM_TO).getValue();

		try {
			ClientItem clientFrom = clientConvertUtil.toClientObject(itemFrom,
					ClientItem.class);
			ClientItem clientTo = clientConvertUtil.toClientObject(itemTo,
					ClientItem.class);

			new FinanceTool().mergeItem(clientFrom, clientTo, getCompany()
					.getID());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.onCompleteProcess(context);
	}

}
