package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientStockTransfer;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

public class WareHouseTransferListCommand extends AbstractCommand {

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

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<ClientStockTransfer>(
				"warehousetransfers", getMessages().pleaseSelect(
						"any " + getMessages().wareHouseTransfer()
								+ " to view details"), 40) {

			@Override
			protected String onSelection(ClientStockTransfer value) {
				return "updateWarehouseTransfer " + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().warehouseTransferList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(ClientStockTransfer value) {
				Record record = new Record(value);
				Warehouse fromWarehouse = (Warehouse) CommandUtils
						.getServerObjectById(value.getFromWarehouse(),
								AccounterCoreType.WAREHOUSE);
				record.add(getMessages().fromWarehouse(),
						fromWarehouse.getName());
				fromWarehouse = (Warehouse) CommandUtils.getServerObjectById(
						value.getToWarehouse(), AccounterCoreType.WAREHOUSE);
				record.add(getMessages().fromWarehouse(),
						fromWarehouse.getName());
				record.add(getMessages().comment(), value.getMemo());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createWarehouseTransfer");
			}

			@Override
			protected boolean filter(ClientStockTransfer e, String name) {
				return false;
			}

			@Override
			protected ArrayList<ClientStockTransfer> getLists(Context context) {
				try {
					return new FinanceTool().getInventoryManager()
							.getWarehouseTransfersList(getCompanyId());
				} catch (AccounterException e) {
					e.printStackTrace();
				}
				return new ArrayList<ClientStockTransfer>();
			}
		});
	}

}
