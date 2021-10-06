package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXItemGroup;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ListRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXCode;

public class CreateVATCodeCommand extends AbstractCommand {

	private static final String DESCRIPTION = "description";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String VATITEM_FOR_SALES = "vatItemForSales";
	private static final String VATITEM_FOR_PURCHASE = "vatItemForPurchase";
	private static final String IS_ACTIVE = "isActive";
	private static final String TAX_CODE = "Tax Code";

	private ClientTAXCode taxCode;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringRequirement(TAX_CODE, getMessages().pleaseEnter(
				getMessages().taxCode() + getMessages().name()), getMessages()
				.taxCode() + getMessages().name(), false, true));

		list.add(new NameRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().taxCode()), getMessages().description(), true,
				true));

		list.add(new BooleanRequirement(IS_TAXABLE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().taxable();
			}

			@Override
			protected String getFalseString() {
				return getMessages().taxExempt();
			}

			@Override
			public boolean isOptional() {
				return false;
			}
		});

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inActive();
			}
		});

		list.add(getVatItemRequirement(VATITEM_FOR_SALES, getMessages()
				.taxItemForSales(), true));

		list.add(getVatItemRequirement(VATITEM_FOR_PURCHASE, getMessages()
				.taxItemForPurchases(), false));

	}

	private Requirement getVatItemRequirement(String reqName,
			final String recordName, final boolean isSales) {
		return new ListRequirement<TAXItemGroup>(reqName, getMessages()
				.pleaseSelect(recordName), recordName, false, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Boolean value = get(IS_TAXABLE).getValue();
				if (value
						&& (isSales ? true : context.getPreferences()
								.isTrackPaidTax())) {
					setVATItemValue();
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().vatItemsList());
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().taxCode());
			}

			@Override
			protected Record createRecord(TAXItemGroup value) {
				Record record = new Record(value);
				record.add(getMessages().name(), value.getName());
				return record;
			}

			@Override
			protected String getDisplayValue(TAXItemGroup value) {
				return value != null ? value.getName() : "";
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newTaxItem");
				list.add("newTaxGroup");
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(recordName);
			}

			@Override
			protected boolean filter(TAXItemGroup e, String name) {
				return e.getName().toLowerCase().startsWith(name.toLowerCase());
			}

			@Override
			protected List<TAXItemGroup> getLists(Context context) {
				return getFilteredVATItems(context, isSales);
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				setVATItemValue();
				String validateResult = validateTAXItem(
						(TAXItemGroup) getValue(), isSales);
				if (validateResult == null) {
					setEnterString(getMessages().pleaseSelect(recordName));
					super.setValue(value);
				} else {
					super.setValue(null);
					setEnterString(validateResult);
				}
				setVATItemValue();
			}

			private void setVATItemValue() {
				Object value = getValue();
				if (value != null) {
					Session currentSession = HibernateUtil.getCurrentSession();
					TAXItemGroup taxItemGroup = (TAXItemGroup) value;
					if (taxItemGroup instanceof TAXItem) {
						taxItemGroup = (TAXItem) currentSession.load(
								TAXItem.class, taxItemGroup.getID());
					} else if (taxItemGroup instanceof TAXGroup) {
						taxItemGroup = (TAXGroup) currentSession.load(
								TAXGroup.class, taxItemGroup.getID());
					} else {
						taxItemGroup = (TAXItemGroup) currentSession.load(
								TAXItemGroup.class, taxItemGroup.getID());
					}
					super.setValue(taxItemGroup);
				}
			}
		};
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(TAX_CODE).getValue();
		String description = (String) get(DESCRIPTION).getValue();
		Boolean isTaxable = (Boolean) get(IS_TAXABLE).getValue();
		Boolean isActive = (Boolean) get(IS_ACTIVE).getValue();

		taxCode.setName(name);
		taxCode.setDescription(description);
		taxCode.setTaxable(isTaxable);
		taxCode.setActive(isActive);
		if (isTaxable && context.getPreferences().isTrackPaidTax()) {
			TAXItemGroup salesVatItem = get(VATITEM_FOR_SALES).getValue();
			TAXItemGroup purchaseVatItem = get(VATITEM_FOR_PURCHASE).getValue();
			if (salesVatItem == null) {
				addFirstMessage(context,
						getMessages()
								.pleaseSelect(getMessages().salesTaxItem()));
				return new Result();
			}
			taxCode.setTAXItemGrpForSales(salesVatItem.getID());
			taxCode.setTAXItemGrpForPurchases(purchaseVatItem.getID());
		}

		create(taxCode, context);
		return null;

	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = taxCode.getID();
		return id != 0 ? "deleteVatCode " + id : null;
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
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().taxCode()));
				return "vATCodesList";
			}
			taxCode = (ClientTAXCode) CommandUtils.getClientObjectById(
					Long.parseLong(string), AccounterCoreType.TAX_CODE,
					getCompanyId());

			if (taxCode == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().taxCode()));
				return "vATCodesList " + string;
			}
			get(TAX_CODE).setValue(taxCode.getName());
			get(DESCRIPTION).setValue(taxCode.getDescription());
			get(IS_ACTIVE).setValue(taxCode.isActive());
			get(IS_TAXABLE).setValue(taxCode.isTaxable());
			get(VATITEM_FOR_SALES).setValue(
					CommandUtils.getServerObjectById(
							taxCode.getTAXItemGrpForSales(),
							AccounterCoreType.TAX_ITEM_GROUP));
			get(VATITEM_FOR_PURCHASE).setValue(
					CommandUtils.getServerObjectById(
							taxCode.getTAXItemGrpForPurchases(),
							AccounterCoreType.TAX_ITEM_GROUP));
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(TAX_CODE).setValue(string);
			}
			taxCode = new ClientTAXCode();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return taxCode.getID() == 0 ? getMessages().create(
				getMessages().taxItem()) : getMessages().updating(
				getMessages().taxItem());
	}

	@Override
	protected String getDetailsMessage() {
		return taxCode.getID() == 0 ? getMessages().readyToCreate(
				getMessages().taxCode()) : getMessages().readyToUpdate(
				getMessages().taxCode());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		return taxCode.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().taxCode()) : getMessages().updateSuccessfully(
				getMessages().taxCode());
	}

	private List<TAXItemGroup> getFilteredVATItems(Context context,
			boolean salesItems) {
		List<TAXItemGroup> vatItmsList = new ArrayList<TAXItemGroup>();
		Set<TAXGroup> taxGroups = context.getCompany().getTaxGroups();
		vatItmsList.addAll(taxGroups);
		Set<TAXItem> taxItems = context.getCompany().getTaxItems();
		List<TAXItem> activeItems = new ArrayList<TAXItem>();
		for (TAXItem taxItem : taxItems) {
			if (taxItem.isActive()) {
				activeItems.add(taxItem);
			}
		}
		vatItmsList.addAll(activeItems);
		return vatItmsList;
	}

	private String validateTAXItem(TAXItemGroup selectedValue, boolean isSales) {
		if (selectedValue != null) {
			if (selectedValue instanceof TAXItem) {
				TAXAgency taxAgency = ((TAXItem) selectedValue).getTaxAgency();
				if (taxAgency != null) {
					if (isSales) {
						if (taxAgency.getSalesLiabilityAccount() == null) {
							return getMessages()
									.pleaseSelectAnotherSalesTAXItem();
						}
					} else if (taxAgency.getPurchaseLiabilityAccount() == null) {
						return getMessages()
								.pleaseSelectAnotherPurchaseTAXItem();
					}
				}
			} else {
				List<TAXItem> taxItems = ((TAXGroup) selectedValue)
						.getTAXItems();
				for (TAXItem item : taxItems) {
					TAXAgency taxAgency = item.getTaxAgency();
					if (taxAgency != null) {
						if (isSales) {
							if (taxAgency.getSalesLiabilityAccount() == null) {
								return getMessages()
										.pleaseSelectAnotherSalesTAXItem();
							}
						} else if (taxAgency.getPurchaseLiabilityAccount() == null) {
							return getMessages()
									.pleaseSelectAnotherPurchaseTAXItem();
						}
					}
				}
			}
		}
		return null;
	}

}
