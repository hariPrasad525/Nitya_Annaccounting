package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.requirements.WareHouseTransferTableRequirement;
import com.nitya.accounter.mobile.requirements.WarehouseRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientStockTransfer;
import com.nitya.accounter.web.client.core.ClientStockTransferItem;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;
import com.nitya.accounter.web.server.OperationContext;

public class CreateWareHouseTransferCommand extends AbstractCommand {
	private final static String WAREHOUSE_FROM = "warehousefrom";
	private final static String WAREHOUSE_TO = "warehouseto";
	private final static String COMMENT = "comment";
	private final static String TRANSFERED_ITEMS = "transfereditems";
	private static final String DELETE_CREATE_NEW = "canDeleteWareHouseTransfer";
	ClientStockTransfer stockTransfer;

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
			ClientStockTransfer clientWarehouse = CommandUtils
					.getWareHouseTransfer(context.getCompany(), string);
			if (clientWarehouse == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().wareHouseTransfer()));
				return "warehouseTransferList " + string;
			}
			stockTransfer = clientWarehouse;
			lists.add(getMessages().IwantToDeleteThisObject(
					getMessages().wareHouseTransfer()));
			lists.add(getMessages().noIdontWantToDeleteThisObj(
					getMessages().wareHouseTransfer()));
			setValues();
		} else {
			stockTransfer = new ClientStockTransfer();
		}
		return null;
	}

	private void setValues() {
		get(WAREHOUSE_FROM).setValue(
				CommandUtils.getServerObjectById(
						stockTransfer.getFromWarehouse(),
						AccounterCoreType.WAREHOUSE));
		get(WAREHOUSE_TO).setValue(
				CommandUtils.getServerObjectById(
						stockTransfer.getToWarehouse(),
						AccounterCoreType.WAREHOUSE));
		get(COMMENT).setValue(stockTransfer.getMemo());
		get(TRANSFERED_ITEMS).setValue(stockTransfer.getStockTransferItems());
		updateRequirementsStatus(false);
	}

	@Override
	protected String getWelcomeMessage() {
		return stockTransfer.getID() == 0 ? getMessages().readyToCreate(
				getMessages().wareHouseTransfer()) : getMessages()
				.updateSuccessfully(getMessages().wareHouseTransfer());
	}

	@Override
	protected String getDetailsMessage() {
		return stockTransfer.getID() == 0 ? getMessages().readyToCreate(
				getMessages().wareHouseTransfer()) : getMessages()
				.readyToUpdate(getMessages().wareHouseTransfer());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DELETE_CREATE_NEW).setValue(
				getMessages().noIdontWantToDeleteThisObj(
						getMessages().wareHouseTransfer()));
	}

	@Override
	public String getSuccessMessage() {
		return stockTransfer.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().wareHouseTransfer()) : getMessages()
				.updateSuccessfully(getMessages().wareHouseTransfer());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(DELETE_CREATE_NEW, getMessages()
				.pleaseSelectAnyOneOption(), getMessages().edit(), true, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelectAnyOneOption();
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelectAnyOneOption();
			}

			@Override
			protected List<String> getLists(Context context) {
				return lists;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (canEdit()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new WarehouseRequirement(WAREHOUSE_FROM,
				getMessages().pleaseSelect(
						getMessages().from() + getMessages().wareHouse()),
				getMessages().from() + getMessages().wareHouse(), false, true,
				null) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				CreateWareHouseTransferCommand.this.get(TRANSFERED_ITEMS)
						.setValue(new ArrayList<ClientStockTransferItem>());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						"from " + getMessages().wareHouse());
			}
		});
		list.add(new WarehouseRequirement(WAREHOUSE_TO, getMessages()
				.pleaseSelect("to " + getMessages().wareHouse()), "to "
				+ getMessages().wareHouse(), false, true, null) {
			@Override
			public void setValue(Object value) {
				if (CreateWareHouseTransferCommand.this
						.isFromToWareHousesSame((Warehouse) value)) {
					setEnterString(getMessages()
							.pleaseSelectDiffWarehousesToTransfer());
					return;
				}
				setEnterString(getMessages().pleaseSelect(
						"to " + getMessages().wareHouse()));
				super.setValue(value);
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						"to " + getMessages().wareHouse());
			}

		});
		list.add(new StringRequirement(COMMENT, getMessages().pleaseEnter(
				getMessages().comment()), getMessages().comment(), true, true));
		list.add(new WareHouseTransferTableRequirement(TRANSFERED_ITEMS,
				getMessages().pleaseSelect(
						getMessages().pleaseSelectAtLeastOneRecord()),
				getMessages().wareHouseTransferItem()) {

			@Override
			protected List<ClientStockTransferItem> getList() {
				return CreateWareHouseTransferCommand.this
						.getStockTransferItems();
			}

		});
	}

	protected boolean isFromToWareHousesSame(Warehouse value) {
		Warehouse from = get(WAREHOUSE_FROM).getValue();
		if (from.getID() == value.getID()) {
			return true;
		}
		return false;
	}

	protected List<ClientStockTransferItem> getStockTransferItems() {
		Warehouse fromWarehouse = get(WAREHOUSE_FROM).getValue();
		if (fromWarehouse == null) {
			return new ArrayList<ClientStockTransferItem>();
		}
		return new FinanceTool().getInventoryManager().getStockTransferItems(
				getCompanyId(), fromWarehouse.getID());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Warehouse fromWarehouse = get(WAREHOUSE_FROM).getValue();
		stockTransfer.setFromWarehouse(fromWarehouse.getID());
		Warehouse toWarehouse = get(WAREHOUSE_TO).getValue();
		stockTransfer.setToWarehouse(toWarehouse.getID());
		stockTransfer.setMemo((String) get(COMMENT).getValue());
		List<ClientStockTransferItem> stockTransferItems = get(TRANSFERED_ITEMS)
				.getValue();
		stockTransfer.setStockTransferItems(stockTransferItems);
		create(stockTransfer, context);
		return super.onCompleteProcess(context);
	}

	protected boolean canEdit() {
		return stockTransfer.getID() != 0;
	}

	List<String> lists = new ArrayList<String>();

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		String canDelte = get(DELETE_CREATE_NEW).getValue();
		if (canDelte != null && lists.indexOf(canDelte) == 0) {
			try {
				String serverClassFullyQualifiedName = stockTransfer
						.getObjectType().getClientClassSimpleName();

				OperationContext opContext = new OperationContext(context
						.getCompany().getID(), stockTransfer, context
						.getIOSession().getUserEmail(),
						String.valueOf(stockTransfer.getID()),
						serverClassFullyQualifiedName);
				new FinanceTool().delete(opContext);
			} catch (AccounterException e) {
				addFirstMessage(context, "" + e.getErrorCode());
				updateRequirementsStatus(false);
			}
			get(DELETE_CREATE_NEW).setEditable(false);
			get(DELETE_CREATE_NEW).setValue(
					getMessages().noIdontWantToDeleteThisObj(
							getMessages().wareHouseTransfer()));
			updateRequirementsStatus(true);
			List<ClientStockTransferItem> stockTransferItems = get(
					TRANSFERED_ITEMS).getValue();
			for (ClientStockTransferItem clientStockTransferItem : stockTransferItems) {
				clientStockTransferItem.setID(0);
			}
			stockTransfer = new ClientStockTransfer();
			this.run(context);
		}
	}

	private void updateRequirementsStatus(boolean isEditable) {
		get(WAREHOUSE_FROM).setEditable(isEditable);
		get(WAREHOUSE_TO).setEditable(isEditable);
		get(COMMENT).setEditable(isEditable);
		get(TRANSFERED_ITEMS).setEditable(isEditable);
	}
}
