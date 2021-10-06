package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.UserCommand;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.AmountRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.MeasurementRequirement;
import com.nitya.accounter.mobile.requirements.WarehouseRequirement;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;

public class CreateInventoryItemCommand extends CreateNonInventoryItemCommand {

	@Override
	public int getItemType() {
		return ClientItem.TYPE_INVENTORY_PART;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new AccountRequirement(ASSESTS_ACCOUNT, getMessages()
				.pleaseSelect(getMessages().assetsAccount()), getMessages()
				.assetsAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().assetsAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> list = new ArrayList<Account>();
				Set<Account> accounts = getCompany().getAccounts();
				for (Account account : accounts) {
					if (account.getType() == ClientAccount.TYPE_INVENTORY_ASSET
							&& account.getIsActive()) {
						list.add(account);
					}
				}
				return list;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().assetsAccount() + "s");
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createBankAccount", getMessages()
						.inventoryAsset()));
			}
		});

		list.add(new AmountRequirement(ON_HAND_QTY, getMessages().pleaseEnter(
				getMessages().onHandQty()), getMessages().onHandQty(), true,
				true));

		list.add(new MeasurementRequirement(MEASUREMENT, getMessages()
				.pleaseSelect(getMessages().measurement()), getMessages()
				.measurementName(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().measurement());
			}

		});

		list.add(new WarehouseRequirement(WARE_HOUSE, getMessages()
				.pleaseSelect(getMessages().wareHouse()), getMessages()
				.wareHouse(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().inventoryItem());
			}

		});

		list.add(new DateRequirement(AS_OF, getMessages().pleaseEnter(
				getMessages().asOf()), getMessages().asOf(), true, true));
	}

	@Override
	protected String getWelcomeMessage() {
		return getItem().getID() == 0 ? getMessages().creating(
				getMessages().inventoryItem()) : "Updating Inventory Item";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
		get(MEASUREMENT).setDefaultValue(
				context.getCompany().getDefaultMeasurement());
		get(WARE_HOUSE).setDefaultValue(getCompany().getDefaultWarehouse());
		get(AS_OF).setDefaultValue(new ClientFinanceDate());
	}

}
