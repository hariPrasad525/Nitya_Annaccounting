package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Unit;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.settings.StockAdjustmentList;
import com.nitya.accounter.web.server.FinanceTool;

public class StockAdjustmentListCommand extends AbstractTransactionListCommand {

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
		list.add(new ShowListRequirement<StockAdjustmentList>(
				"stockadjustments", getMessages()
						.pleaseSelectAnyStockAdjustmentToViewDetails(), 40) {

			@Override
			protected String onSelection(StockAdjustmentList value) {
				return "updateStockAdjustment " + value.getStockAdjustment();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().stockAdjustments();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(StockAdjustmentList value) {
				Record record = new Record(value);
				record.add(getMessages().wareHouse(), value.getWareHouse());
				record.add(getMessages().itemName(), value.getItem());
				Unit unit = (Unit) CommandUtils.getServerObjectById(value
						.getQuantity().getUnit(), AccounterCoreType.UNIT);
				record.add(getMessages().adjustedQty(), value.getQuantity()
						.getValue() + " " + unit.getType());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createStockAdjustment");
			}

			@Override
			protected boolean filter(StockAdjustmentList e, String name) {
				return false;
			}

			@Override
			protected ArrayList<StockAdjustmentList> getLists(Context context) {
				try {
					return new FinanceTool().getInventoryManager()
							.getStockAdjustments(getStartDate().getDate(),
									getEndDate().getDate(), getCompanyId());
				} catch (AccounterException e) {
					e.printStackTrace();
				}
				return new ArrayList<StockAdjustmentList>();

			}
		});
	}

	@Override
	protected List<String> getViewByList() {
		// TODO Auto-generated method stub
		return null;
	}
}
