package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.StockAdjustmentItemTableRequirement;
import com.nitya.accounter.mobile.requirements.WarehouseRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientStockAdjustment;
import com.nitya.accounter.web.client.core.ClientTransactionItem;

public class CreateStockAdjustmentCommand extends AbstractTransactionCommand {
	private static final String WAREHOUSE = "warehouse";
	private static final String STOCK_ADJUSTMENT = "stockadjustment";
	private static final String ADJUSTMENT_ACCOUNT = "adjustmentaccount";
	ClientStockAdjustment stockAdjustment;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().wareHouseTransfer()));
				return "warehouseTransferList";
			}
			stockAdjustment = getTransaction(string,
					AccounterCoreType.STOCK_ADJUSTMENT, context);
			if (stockAdjustment == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().wareHouseTransfer()));
				return "warehouseTransferList " + string;
			}
			setValues();
		} else {
			stockAdjustment = new ClientStockAdjustment();
		}
		return null;
	}

	private void setValues() {
		Warehouse warehouse = (Warehouse) CommandUtils.getServerObjectById(
				stockAdjustment.getWareHouse(), AccounterCoreType.WAREHOUSE);
		get(WAREHOUSE).setValue(warehouse);
		List<ClientTransactionItem> stockAdjustmentItems = stockAdjustment
				.getTransactionItems();
		Account account = (Account) CommandUtils.getServerObjectById(
				stockAdjustment.getAdjustmentAccount(),
				AccounterCoreType.ACCOUNT);
		get(ADJUSTMENT_ACCOUNT).setValue(account);
		get(STOCK_ADJUSTMENT).setValue(stockAdjustmentItems);
	}

	@Override
	protected String getWelcomeMessage() {
		return stockAdjustment.getID() == 0 ? getMessages().create(
				getMessages().stockAdjustment()) : getMessages().updating(
				getMessages().stockAdjustment());
	}

	@Override
	protected String getDetailsMessage() {
		return stockAdjustment.getID() == 0 ? getMessages().readyToCreate(
				getMessages().stockAdjustment()) : getMessages().readyToUpdate(
				getMessages().stockAdjustment());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(WAREHOUSE).setDefaultValue(getCompany().getDefaultWarehouse());
	}

	@Override
	public String getSuccessMessage() {
		return stockAdjustment.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().stockAdjustment()) : getMessages()
				.updateSuccessfully(getMessages().stockAdjustment());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new WarehouseRequirement(WAREHOUSE, getMessages()
				.pleaseSelect(getMessages().wareHouse()), getMessages()
				.wareHouse(), false, true, null));
		list.add(new AccountRequirement(ADJUSTMENT_ACCOUNT, getMessages()
				.pleaseSelect(getMessages().adjustmentAccount()), getMessages()
				.adjustmentAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().adjustmentAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> accounts = new ArrayList<Account>();
				for (Account account : getCompany().getAccounts()) {
					if (account.getIsActive()) {
						accounts.add(account);
					}
				}
				return accounts;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}
		});
		list.add(new StockAdjustmentItemTableRequirement(STOCK_ADJUSTMENT,
				getMessages().pleaseSelect(getMessages().stockAdjustments()),
				getMessages().stockAdjustments()) {

			@Override
			protected Warehouse getWareHouse() {
				return CreateStockAdjustmentCommand.this.get(WAREHOUSE)
						.getValue();
			}

		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Result onCompleteProcess(Context context) {
		stockAdjustment.setTransactionItems((List<ClientTransactionItem>) get(
				STOCK_ADJUSTMENT).getValue());
		stockAdjustment.setWareHouse(((Warehouse) get(WAREHOUSE).getValue())
				.getID());
		Account account = get(ADJUSTMENT_ACCOUNT).getValue();
		stockAdjustment.setAdjustmentAccount(account.getID());
		stockAdjustment.setTransactionDate(new ClientFinanceDate().getDate());
		create(stockAdjustment, context);
		return super.onCompleteProcess(context);
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		// TODO Auto-generated method stub
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Currency getCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

}
