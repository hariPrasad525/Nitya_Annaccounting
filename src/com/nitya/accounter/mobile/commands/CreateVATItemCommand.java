package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.core.VATReturnBox;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.AmountRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.requirements.TaxAgencyRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientVATReturnBox;
import com.nitya.accounter.web.server.util.CountryPreferenceFactory;

public class CreateVATItemCommand extends AbstractCommand {

	private static final String DESCRIPTION = "description";
	private static final String IS_ACTIVE = "isActive";
	private static final String VAT_RETURN_BOX = "vatReturnBox";
	private static final String TAX_ITEM_NAME = "Tax Item Name";
	private static final String TAX_RATE = "Tax Rate";
	private static final String TAX_AGENCY = "Tax Agency";

	private ClientTAXItem taxItem;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(TAX_ITEM_NAME, getMessages().pleaseEnter(
				getMessages().taxItemName()), getMessages().taxItemName(),
				false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
		});

		list.add(new NameRequirement(DESCRIPTION, getMessages().enterObj(
				getMessages().description()), getMessages().description(),
				true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
		});

		list.add(new AmountRequirement(TAX_RATE, getMessages().enterObj(
				getMessages().taxRateP()), getMessages().taxRate(), false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
		});

		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages().create(
				getMessages().taxAgency()), getMessages().taxAgency(), false,
				true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

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
				return null;
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new StringListRequirement(VAT_RETURN_BOX, null, getMessages()
				.vatReturnBox(), false, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {

				if (context.getPreferences().isTrackTax()
						&& context.getCompany().getCountryPreferences()
								.isVatAvailable()
						&& context
								.getCompany()
								.getCountry()
								.equals(CountryPreferenceFactory.UNITED_KINGDOM)) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().vatReturnBox());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().vatReturnBox());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getVatReturnBoxes(context.getCompany());
			}

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().objIsActive(getMessages().item());
			}

			@Override
			protected String getFalseString() {
				return "item is in active";
			}
		});
	}

	protected List<String> getVatReturnBoxes(Company clientCompany) {
		Set<VATReturnBox> vatReturnBoxList = clientCompany.getVatReturnBoxes();
		List<String> vatReturnBoxListNames = new ArrayList<String>();
		if (!vatReturnBoxListNames.isEmpty()) {
			for (VATReturnBox vatReturnBox : vatReturnBoxList) {
				vatReturnBoxListNames.add(vatReturnBox.getName());
			}
			return vatReturnBoxListNames;
		}
		return null;
	}

	protected List<String> getTaxagencyList(Company company) {
		Set<TAXAgency> taxAgencyList = company.getTaxAgencies();
		List<String> taxAgencyListNames = new ArrayList<String>();
		if (!taxAgencyListNames.isEmpty()) {
			for (TAXAgency taxAgency : taxAgencyList) {
				if (taxAgency.isActive())
					taxAgencyListNames.add(taxAgency.getName());
			}
			return taxAgencyListNames;
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = (String) get(TAX_ITEM_NAME).getValue();
		String description = (String) get(DESCRIPTION).getValue();
		double taxRate = get(TAX_RATE).getValue();
		boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		ClientVATReturnBox vatReturnBox = (ClientVATReturnBox) get(
				VAT_RETURN_BOX).getValue();
		taxItem.setVatReturnBox(vatReturnBox == null ? 0 : vatReturnBox.getID());
		taxItem.setPercentage(true);
		taxItem.setName(name);
		taxItem.setDescription(description);
		taxItem.setTaxRate(taxRate);
		taxItem.setActive(isActive);
		taxItem.setTaxAgency(taxAgency.getID());
		create(taxItem, context);
		return null;
	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = taxItem.getID();
		return id != 0 ? "deleteVatItem " + id : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
		}

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context,
						getMessages().pleaseSelect(getMessages().taxItem()));
				return "vATItems";
			}
			taxItem = (ClientTAXItem) CommandUtils.getClientObjectById(
					Long.parseLong(string), AccounterCoreType.TAXITEM,
					getCompanyId());
			if (taxItem == null) {
				addFirstMessage(context,
						getMessages().pleaseSelect(getMessages().taxItem()));
				return "vATItems " + string;
			}
			get(TAX_ITEM_NAME).setValue(taxItem.getName());
			get(DESCRIPTION).setValue(taxItem.getDescription());
			get(TAX_RATE).setValue(taxItem.getTaxRate());
			get(IS_ACTIVE).setValue(taxItem.isActive());
			get(TAX_AGENCY).setValue(
					CommandUtils.getServerObjectById(taxItem.getTaxAgency(),
							AccounterCoreType.TAXAGENCY));
			get(VAT_RETURN_BOX).setValue(
					CommandUtils.getClientObjectById(taxItem.getVatReturnBox(),
							AccounterCoreType.VATRETURNBOX, getCompanyId()));
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(TAX_ITEM_NAME).setValue(string);
			}
			taxItem = new ClientTAXItem();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return taxItem.getID() == 0 ? "New Vat Item commond is activated"
				: "Update VAT Item command activated";
	}

	@Override
	protected String getDetailsMessage() {
		return taxItem.getID() == 0 ? "New vat item is ready to create with the following values"
				: "VAT Item is ready to update with following values";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		return taxItem.getID() == 0 ? "New vat Item created Successfully"
				: "VAT Item updated successfully";
	}

}
