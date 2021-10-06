package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class QuotesListCommand extends AbstractTransactionListCommand {
	int estimateType;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		estimateType = ClientEstimate.QUOTES;
		String commandString = context.getCommandString().toLowerCase();
		if (commandString.contains("charges")) {
			estimateType = ClientEstimate.CHARGES;
		} else if (commandString.contains("credits")) {
			estimateType = ClientEstimate.CREDITS;
		} else if (commandString.contains("sales")) {
			estimateType = ClientEstimate.SALES_ORDER;
		}

		if (commandString.contains("quote")) {
			if (!context.getPreferences().isDoyouwantEstimates()) {
				addFirstMessage(context, getMessages()
						.youDntHavePermissionToDoThis());
				return "cancel";
			}
		} else {
			if (!context.getPreferences().isDelayedchargesEnabled()) {
				addFirstMessage(context, getMessages()
						.youDntHavePermissionToDoThis());
				return "cancel";
			}
		}
		context.setString("");
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
		if (estimateType == ClientEstimate.QUOTES) {
			return getMessages().success() + getMessages().quotesList();
		} else if (estimateType == ClientEstimate.CREDITS) {
			return getMessages().success() + getMessages().creditsList();
		} else if (estimateType == ClientEstimate.CHARGES) {
			return getMessages().success() + getMessages().chargesList();
		} else if (estimateType == ClientEstimate.SALES_ORDER) {
			return getMessages().success() + getMessages().salesOrderList();
		}
		return "";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new ShowListRequirement<Estimate>("Estimates",
				"Please select.", 20) {

			@Override
			protected String onSelection(Estimate value) {
				return "editTransaction " + value.getID();
			}

			@Override
			protected String getShowMessage() {
				if (estimateType == ClientEstimate.QUOTES) {
					return getMessages().quotesList();
				} else if (estimateType == ClientEstimate.CREDITS) {
					return getMessages().creditsList();
				} else if (estimateType == ClientEstimate.CHARGES) {
					return getMessages().chargesList();
				} else if (estimateType == ClientEstimate.SALES_ORDER) {
					return getMessages().salesOrderList();
				}
				return "";
			}

			@Override
			protected String getEmptyString() {
				if (estimateType == ClientEstimate.QUOTES) {
					return getMessages().youDontHaveAny(getMessages().quotes());
				} else if (estimateType == ClientEstimate.CREDITS) {
					return getMessages()
							.youDontHaveAny(getMessages().credits());
				} else if (estimateType == ClientEstimate.CHARGES) {
					return getMessages()
							.youDontHaveAny(getMessages().Charges());
				} else if (estimateType == ClientEstimate.SALES_ORDER) {
					return getMessages().youDontHaveAny(
							getMessages().salesOrders());
				}
				return "";
			}

			@Override
			protected Record createRecord(Estimate value) {
				Record estrecord = new Record(value);
				estrecord.add(getMessages().date(), getDateByCompanyType(value
						.getDate().toClientFinanceDate()));
				estrecord.add(getMessages().number(), value.getNumber());
				estrecord
						.add(getMessages().name(), value.getCustomer()
								.getName() != null ? value.getCustomer()
								.getName() : "");
				estrecord.add(getMessages().expirationDate(),
						getDateByCompanyType(value.getExpirationDate()
								.toClientFinanceDate()));
				estrecord.add(
						getMessages().total(),
						getAmountWithCurrency(value.getTotal(), value
								.getCurrency().getSymbol()));
				return estrecord;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				if (estimateType == ClientEstimate.QUOTES) {
					list.add("createQuote");
				} else if (estimateType == ClientEstimate.CHARGES) {
					list.add("newCharge");
				} else if (estimateType == ClientEstimate.CREDITS) {
					list.add("newCredit");
				} else if (estimateType == ClientEstimate.SALES_ORDER) {
					list.add("newSalesOrder");
				}
			}

			@Override
			protected boolean filter(Estimate e, String name) {
				return e.getCustomer().getName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Estimate> getLists(Context context) {
				return getEstimates(context);
			}

		});

	}

	private List<Estimate> getEstimates(Context context) {
		String type = get(VIEW_BY).getValue();

		int viwType = -1;
		if (type.equalsIgnoreCase(getMessages().open())) {
			viwType = ClientEstimate.STATUS_OPEN;
		} else if (type.equalsIgnoreCase(getMessages().rejected())) {
			viwType = ClientEstimate.STATUS_REJECTED;
		} else if (type.equalsIgnoreCase(getMessages().accepted())) {
			viwType = ClientEstimate.STATUS_ACCECPTED;
		} else if (type.equalsIgnoreCase(getMessages().applied())) {
			viwType = ClientEstimate.STATUS_COMPLETED;
		} else if (type.equalsIgnoreCase(getMessages().close())) {
			viwType = ClientEstimate.STATUS_CLOSE;
		} else if (type.equalsIgnoreCase(getMessages().drafts())) {
			viwType = ClientTransaction.STATUS_DRAFT;
		} else if (type.equalsIgnoreCase(getMessages().expired())) {
			viwType = 6;
		} else if (type.equalsIgnoreCase(getMessages().completed())) {
			viwType = ClientTransaction.STATUS_COMPLETED;
		} else if (type.equalsIgnoreCase(getMessages().cancelled())) {
			viwType = ClientTransaction.STATUS_CANCELLED;
		}
		List<Estimate> result = new ArrayList<Estimate>();
		try {
			result = new FinanceTool().getCustomerManager().getEstimates(
					context.getCompany().getID(), estimateType, viwType,
					new FinanceDate(getStartDate()),
					new FinanceDate(getEndDate()), 0, -1);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return result;

	}

	@Override
	protected List<String> getViewByList() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(getMessages().open());
		if (estimateType == ClientEstimate.SALES_ORDER) {
			listOfTypes.add(getMessages().completed());
			listOfTypes.add(getMessages().cancelled());
		} else if (estimateType == ClientEstimate.QUOTES) {
			listOfTypes.add(getMessages().rejected());
			listOfTypes.add(getMessages().accepted());
			listOfTypes.add(getMessages().close());
			listOfTypes.add(getMessages().applied());
		}
		listOfTypes.add(getMessages().expired());
		listOfTypes.add(getMessages().all());
		listOfTypes.add(getMessages().drafts());
		return listOfTypes;
	}
}