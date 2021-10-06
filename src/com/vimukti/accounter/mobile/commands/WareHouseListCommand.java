package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class WareHouseListCommand extends AbstractCommand {

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
		list.add(new ShowListRequirement<Warehouse>(getMessages().wareHouses(),
				getMessages().pleaseSelectAnyObjToViewDetails(
						getMessages().wareHouses()), 40) {

			@Override
			protected String onSelection(Warehouse value) {
				return "showWareHouse " + value.getName();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().warehouseList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(Warehouse value) {
				Record record = new Record(value);
				record.add(getMessages().warehouseCode(),
						value.getWarehouseCode());
				record.add(getMessages().warehouseName(), value.getName());
				record.add(getMessages().ddiNumber(), value.getDDINumber());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createWarehouse");
			}

			@Override
			protected boolean filter(Warehouse e, String name) {
				return false;
			}

			@Override
			protected List<Warehouse> getLists(Context context) {
				return new ArrayList<Warehouse>(context.getCompany()
						.getWarehouses());
			}
		});
	}

}
