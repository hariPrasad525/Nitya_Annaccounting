package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.mobile.requirements.TaxAgencyRequirement;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientBox;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientTAXReturnEntry;
import com.nitya.accounter.web.client.ui.widgets.DateUtil;
import com.nitya.accounter.web.server.FinanceTool;
import com.nitya.accounter.web.server.countries.UnitedKingdom;
import com.nitya.accounter.web.server.util.ICountryPreferences;

public class FileVATCommand extends AbstractTransactionCommand {

	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";
	private static final String TAXRETURNENTRIES = "taxreturnentries";
	private static final String TAX_AGENCY = "taxAgency";
	private static final String BOXES = "boxes";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnterName(getMessages().taxAgency()), getMessages()
				.taxAgency(), false, true, new ChangeListner<TAXAgency>() {

			@Override
			public void onSelection(TAXAgency value) {
				taxAgencySelected(value);
			}
		}) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().taxAgency());
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(context.getCompany()
						.getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().taxAgency());
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return e.getName().toLowerCase().startsWith(name);
			}
		});

		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().fromDate()), getMessages().fromDate(), true, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				fromDateChanged((ClientFinanceDate) value);
			}
		});

		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().toDate()), getMessages().toDate(), true, true));

		list.add(new ShowListRequirement<ClientBox>(BOXES, "", 20) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ICountryPreferences countryPreferences = context.getCompany()
						.getCountryPreferences();
				if (countryPreferences instanceof UnitedKingdom
						&& countryPreferences.isVatAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String onSelection(ClientBox value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().norecordstoshowinbetweentheselecteddates();
			}

			@Override
			protected Record createRecord(ClientBox value) {
				Record record = new Record(value);
				record.add(getMessages().vatLine(), value.getName());
				record.add(getMessages().amount(),
						getAmountWithCurrency(value.getAmount()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {

			}

			@Override
			protected boolean filter(ClientBox e, String name) {
				return false;
			}

			@Override
			protected List<ClientBox> getLists(Context context) {
				return getClientBoxes(context);
			}
		});

		list.add(new ShowListRequirement<ClientTAXReturnEntry>(
				TAXRETURNENTRIES, "", 20) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ICountryPreferences countryPreferences = context.getCompany()
						.getCountryPreferences();
				if (!(countryPreferences instanceof UnitedKingdom && countryPreferences
						.isVatAvailable())) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String onSelection(ClientTAXReturnEntry value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().norecordstoshowinbetweentheselecteddates();
			}

			@Override
			protected Record createRecord(ClientTAXReturnEntry value) {
				Record record = new Record(value);
				record.add(getMessages().vatLine(), value.getName());
				record.add(getMessages().amount(),
						getAmountWithCurrency(value.getTaxAmount()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {

			}

			@Override
			protected boolean filter(ClientTAXReturnEntry e, String name) {
				return false;
			}

			@Override
			protected List<ClientTAXReturnEntry> getLists(Context context) {
				return getClientTAXReturnEntries(context);
			}
		});
	}

	protected List<ClientBox> getClientBoxes(Context context) {
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ClientFinanceDate fromDate = get(FROM_DATE).getValue();
		ClientFinanceDate toDate = get(TO_DATE).getValue();
		try {
			ClientTAXReturn vatReturn = new FinanceTool().getTaxManager()
					.getVATReturnDetails(taxAgency, new FinanceDate(fromDate),
							new FinanceDate(toDate), getCompanyId());
			List<ClientBox> boxes = new ArrayList<ClientBox>();
			double box3Amt = 0.0;
			double box5Amt = 0.0;
			double box4Amt = 0.0;
			for (ClientBox box : vatReturn.getBoxes()) {

				if (box.getBoxNumber() == 1 || box.getBoxNumber() == 2) {
					box3Amt = box3Amt + box.getAmount();
				}
				if (box.getBoxNumber() == 3) {
					box.setAmount(box3Amt);
				}
				if (box.getBoxNumber() == 4) {
					box4Amt = box4Amt + box.getAmount();
				}
				if (box.getBoxNumber() == 4) {
					box.setAmount(box4Amt);
				}
				if (box.getBoxNumber() == 4) {
					box5Amt = box3Amt - box.getAmount();
				}

				if (box.getBoxNumber() == 5) {
					box.setAmount(box5Amt);
				}
				boxes.add(box);
			}
			if (boxes.isEmpty()) {
				context.makeResult().add(
						getMessages()
								.norecordstoshowinbetweentheselecteddates());
			}
			get(BOXES).setValue(boxes);
			return boxes;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ClientBox>();
		}
	}

	protected void taxAgencySelected(TAXAgency taxAgency) {
		get(TAXRETURNENTRIES).setValue(new ArrayList<ClientTAXReturnEntry>());
		get(BOXES).setValue(new ArrayList<ClientBox>());
		long lastTaxReturnEndDate = new FinanceTool().getTaxManager()
				.getLastTaxReturnEndDate(taxAgency.getID(), getCompanyId());
		ClientFinanceDate clientFinanceDate;
		if (lastTaxReturnEndDate != 0) {
			clientFinanceDate = new ClientFinanceDate(lastTaxReturnEndDate);
		} else {
			clientFinanceDate = new ClientFinanceDate(
					DateUtil.getCurrentMonthFirstDate());
		}
		get(FROM_DATE).setValue(clientFinanceDate);
	}

	protected void fromDateChanged(ClientFinanceDate value) {
		Date date = value.getDateAsObject();
		int frequency = ClientTAXAgency.TAX_RETURN_FREQUENCY_MONTHLY;
		TAXAgency selectedTaxAgency = get(TAX_AGENCY).getValue();
		if (selectedTaxAgency != null) {
			frequency = selectedTaxAgency.getTAXFilingFrequency();
		}
		date = DateUtil.getMonthFirstDay(date);
		switch (frequency) {
		case ClientTAXAgency.TAX_RETURN_FREQUENCY_MONTHLY:
			date.setMonth(date.getMonth() + 1);
			break;
		case ClientTAXAgency.TAX_RETURN_FREQUENCY_QUARTERLY:
			date.setMonth(date.getMonth() + 3);
			break;
		case ClientTAXAgency.TAX_RETURN_FREQUENCY_HALF_YEARLY:
			date.setMonth(date.getMonth() + 6);
			break;
		case ClientTAXAgency.TAX_RETURN_FREQUENCY_YEARLY:
			date.setMonth(date.getMonth() + 12);
			break;
		}
		date.setDate(date.getDate() - 1);
		get(TO_DATE).setValue(new ClientFinanceDate(date));
	}

	private List<ClientTAXReturnEntry> getClientTAXReturnEntries(Context context) {
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ClientFinanceDate fromDate = get(FROM_DATE).getValue();
		ClientFinanceDate toDate = get(TO_DATE).getValue();
		try {
			List<ClientTAXReturnEntry> taxReturnEntries = new FinanceTool()
					.getTaxManager().getTAXReturnEntries(getCompanyId(),
							taxAgency.getID(), fromDate.getDate(),
							toDate.getDate());
			if (taxReturnEntries.isEmpty()) {
				context.makeResult().add(getMessages().selectTAXAgency());
			}
			get(TAXRETURNENTRIES).setValue(taxReturnEntries);
			return taxReturnEntries;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ClientTAXReturnEntry>();
		}
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientTAXReturn clientVATReturn = new ClientTAXReturn();
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ClientFinanceDate fromDate = get(FROM_DATE).getValue();
		ClientFinanceDate toDate = get(TO_DATE).getValue();
		List<ClientTAXReturnEntry> taxReturnEntries = get(TAXRETURNENTRIES)
				.getValue();
		if (!taxReturnEntries.isEmpty()) {
			clientVATReturn.setPeriodStartDate(fromDate.getDate());
			clientVATReturn.setPeriodEndDate(toDate.getDate());
			clientVATReturn.setTaxReturnEntries(taxReturnEntries);
		}
		clientVATReturn.setTransactionDate(new ClientFinanceDate().getDate());
		clientVATReturn.setTAXAgency(taxAgency.getID());

		List<ClientBox> boxes = get(BOXES).getValue();
		clientVATReturn.setBoxes(boxes);
		if (!boxes.isEmpty()) {
			if (taxAgency != null) {
				clientVATReturn.setTransactionDate(new ClientFinanceDate()
						.getDate());
			}

			double salesTaxamount = 0, purchaseTaxAmount = 0;
			for (ClientBox b : boxes) {
				if (b.getBoxNumber() == 1 || b.getBoxNumber() == 2) {
					salesTaxamount = salesTaxamount + b.getAmount();
				} else if (b.getBoxNumber() == 4) {
					purchaseTaxAmount = purchaseTaxAmount + b.getAmount();
				} else if (b.getBoxNumber() == 10) {
					purchaseTaxAmount = purchaseTaxAmount - b.getAmount();
				}
			}

			double amount = clientVATReturn.getBoxes().get(4).getAmount()
					+ clientVATReturn.getBoxes()
							.get(clientVATReturn.getBoxes().size() - 1)
							.getAmount();

			clientVATReturn.setSalesTaxTotal(clientVATReturn.getBoxes().get(2)
					.getAmount());
			clientVATReturn.setPurchaseTaxTotal(clientVATReturn.getBoxes()
					.get(3).getAmount());
			clientVATReturn.setTotalTAXAmount(amount);
		}
		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * clientVATReturn.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * clientVATReturn.setCurrencyFactor(factor); }
		 */
		create(clientVATReturn, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, Global.get().messages()
					.youDoNotHavePermissionToDoThisAction());
			return "cancel";
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().fileVAT());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().fileVAT());
	}

	@Override
	protected void setDefaultValues(Context context) {
		Date currentMonthFirstDate = DateUtil.getCurrentMonthFirstDate();
		get(FROM_DATE).setDefaultValue(
				new ClientFinanceDate(currentMonthFirstDate));
		get(TO_DATE).setDefaultValue(new ClientFinanceDate());
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().fileVAT());
	}

	@Override
	protected Currency getCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

}
