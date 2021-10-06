package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseOrderListCommand extends AbstractTransactionListCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new ShowListRequirement<PurchaseOrdersList>(
				"purchaseOrderList", getMessages().pleaseSelect(
						getMessages().purchaseOrder()), 5) {
			@Override
			protected Record createRecord(PurchaseOrdersList value) {
				Record record = new Record(value);
				record.add(getMessages().number(), value.getNumber());
				record.add(getMessages().name(), value.getVendorName());
				record.add(getMessages().purchasePrice(),
						getAmountWithCurrency(value.getPurchasePrice()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("addNewPurchaseOrder");
			}

			@Override
			protected boolean filter(PurchaseOrdersList e, String name) {
				return e.getVendorName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<PurchaseOrdersList> getLists(Context context) {
				return getPurchaseOrder(context);
			}

			@Override
			protected String getShowMessage() {
				return getMessages().purchaseOrderList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(PurchaseOrdersList value) {
				return "editTransaction " + value.getTransactionId();
			}
		});
	}

	private List<PurchaseOrdersList> getPurchaseOrder(Context context) {
		FinanceTool tool = new FinanceTool();
		int type = -1;
		if (getViewType().equals(getMessages().open())) {
			type = ClientTransaction.STATUS_OPEN;
		} else if (getViewType().equals(getMessages().completed())) {
			type = ClientTransaction.STATUS_COMPLETED;
		} else if (getViewType().equals(getMessages().cancelled())) {
			type = ClientTransaction.STATUS_CANCELLED;
		} else if (getViewType().equals(getMessages().drafts())) {
			type = ClientTransaction.STATUS_DRAFT;
		} else if (getViewType().equalsIgnoreCase(getMessages().expired())) {
			type = 6;
		}
		try {
			return tool.getPurchageManager().getPurchaseOrdersList(
					context.getCompany().getID(), getStartDate().getDate(),
					getEndDate().getDate(), type, 0, -1);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getViewType() {
		return PurchaseOrderListCommand.this.get(VIEW_BY).getValue();
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
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().open());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().open());
		list.add(getMessages().cancelled());
		list.add(getMessages().completed());
		list.add(getMessages().expired());
		list.add(getMessages().all());
		return list;
	}

}
