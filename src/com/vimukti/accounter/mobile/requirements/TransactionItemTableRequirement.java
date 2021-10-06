package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class TransactionItemTableRequirement extends
		AbstractTableRequirement<ClientTransactionItem> {
	private static final String QUANITY = "Quantity";
	private static final String INVENTORY_QUANITY = "InventoryQuantity";
	private static final String ITEM = "Item";
	private static final String UNITPTICE = "UnitPrice";
	private static final String DISCOUNT = "itemDiscount";
	private static final String TAXCODE = "TaxCode";
	private static final String TAX = "Tax";
	private static final String DESCRIPTION = "Description";
	protected static final String IS_BILLABLE = "isBillable";
	protected static final String ITEM_CUSTOMER = "itemcustomer";
	private static final String WARE_HOUSE = "itemwarehouse";

	public TransactionItemTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
	}

	public abstract boolean isSales();

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM, getMessages().pleaseSelect(
				getMessages().item()), getMessages().item(), false, true, null,
				isSales()) {

			@Override
			protected List<Item> getLists(Context context) {
				return getItems(context);
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				Item item = getValue();
				if (item != null) {
					setOtherRequirementValues(item);
				}
			}

		});

		list.add(new AmountRequirement(QUANITY, getMessages().pleaseEnter(
				getMessages().quantity()), getMessages().quantity(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Item item = get(ITEM).getValue();
				if (item.getType() != Item.TYPE_INVENTORY_PART) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new QuantityRequirement(INVENTORY_QUANITY, getMessages()
				.pleaseEnter(getMessages().quantity()), getMessages()
				.quantity(), true) {

			@Override
			protected List<Unit> getLists(Context context) {
				Item item = get(ITEM).getValue();
				if (item.getType() == Item.TYPE_INVENTORY_PART) {
					return new ArrayList<Unit>(item.getMeasurement().getUnits());
				}
				return new ArrayList<Unit>();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Item item = get(ITEM).getValue();
				if (item.getType() == Item.TYPE_INVENTORY_PART) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getValueRecordName() {
				return getMessages().value();
			}
		});

		list.add(new WarehouseRequirement(WARE_HOUSE, getMessages()
				.pleaseSelect(getMessages().wareHouse()), getMessages()
				.wareHouse(), true, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Item item = get(ITEM).getValue();
				if (item.getType() == Item.TYPE_INVENTORY_PART) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new CurrencyAmountRequirement(UNITPTICE, getMessages()
				.pleaseEnter(getMessages().unitPrice()), getMessages()
				.unitPrice(), false, true) {

			@Override
			protected Currency getCurrency() {
				return TransactionItemTableRequirement.this.getCurrency();
			}
		});

		list.add(new AmountRequirement(DISCOUNT, getMessages().pleaseEnter(
				getMessages().discount()), getMessages().discount(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackDiscounts()
						&& getPreferences().isDiscountPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnter(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& (isSales() ? true : getPreferences()
								.isTrackPaidTax())
						&& getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				Set<TAXCode> taxCodes = context.getCompany().getTaxCodes();
				List<TAXCode> clientcodes = new ArrayList<TAXCode>();
				for (TAXCode taxCode : taxCodes) {
					if (taxCode.isActive()) {
						clientcodes.add(taxCode);
					}
				}
				return clientcodes;
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(TAX, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& (isSales() ? true : getPreferences()
								.isTrackPaidTax())
						&& !getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().taxable();
			}

			@Override
			protected String getFalseString() {
				return getMessages().taxExempt();
			}
		});

		list.add(new StringRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().description()), getMessages().description(),
				true, true));
	}

	protected void setOtherRequirementValues(Item item) {
		if (isSales()) {
			get(UNITPTICE).setValue(

			item.getSalesPrice() / getCurrencyFactor());
		} else {
			get(UNITPTICE).setValue(

			item.getPurchasePrice() / getCurrencyFactor());
		}
		get(TAXCODE).setValue(
				get(TAXCODE).getValue() == null ? item.getTaxCode() : get(
						TAXCODE).getValue());
		get(TAX).setValue(item.isTaxable());
		get(QUANITY).setValue(1.0);
		if (item.getType() == Item.TYPE_INVENTORY_PART) {
			ClientQuantity quantity = new ClientQuantity();
			quantity.setUnit(item.getMeasurement().getDefaultUnit().getID());
			quantity.setValue(1.0);
			get(INVENTORY_QUANITY).setValue(quantity);
			get(WARE_HOUSE).setValue(item.getWarehouse());
		}
	}

	public abstract List<Item> getItems(Context context);

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected void getRequirementsValues(ClientTransactionItem obj) {
		Item clientItem = get(ITEM).getValue();
		Item item = (Item) CommandUtils.getServerObjectById(clientItem.getID(),
				AccounterCoreType.ITEM);
		clientItem = item;
		obj.setItem(clientItem.getID());
		Warehouse warehouse = get(WARE_HOUSE).getValue();
		if (warehouse == null) {
			warehouse = clientItem.getWarehouse();
		}
		obj.setWareHouse(warehouse == null ? 0 : warehouse.getID());
		ClientQuantity quantity = get(INVENTORY_QUANITY).getValue();
		if (clientItem.getType() != Item.TYPE_INVENTORY_PART) {
			double value = get(QUANITY).getValue();
			quantity.setValue(value);
		}
		obj.setQuantity(quantity);
		Measurement measurement = clientItem.getMeasurement();
		if (quantity.getUnit() == 0 && measurement != null) {
			obj.getQuantity().setUnit(measurement.getDefaultUnit().getID());
		}
		obj.setUnitPrice((Double) get(UNITPTICE).getValue());
		obj.setDiscount((Double) get(DISCOUNT).getValue());
		TAXCode taxCode = get(TAXCODE).getValue();
		if (taxCode != null) {
			obj.setTaxCode(taxCode.getID());
		}
		obj.setTaxable((Boolean) get(TAX).getValue());
		if (get(IS_BILLABLE) != null) {
			Boolean isBillable = get(IS_BILLABLE).getValue();
			obj.setIsBillable(isBillable);
			Customer customer = get(ITEM_CUSTOMER).getValue();
			if (customer != null) {
				obj.setCustomer(customer.getID());
			}
		}
		obj.setDescription((String) get(DESCRIPTION).getValue());
		double lt = obj.getQuantity().getValue() * obj.getUnitPrice();
		if (getPreferences().isTrackDiscounts()
				&& !getPreferences().isDiscountPerDetailLine()) {
			obj.setDiscount(getDiscount());
		}
		double disc = obj.getDiscount();
		obj.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionItem obj) {
		Item item = (Item) CommandUtils.getServerObjectById(obj.getItem(),
				AccounterCoreType.ITEM);
		get(ITEM).setValue(item);
		if (item != null) {
			get(WARE_HOUSE).setValue(item.getWarehouse());
		}
		get(QUANITY).setValue(obj.getQuantity().getValue());
		get(INVENTORY_QUANITY).setValue(obj.getQuantity());
		get(UNITPTICE).setValue(obj.getUnitPrice());

		get(DISCOUNT).setDefaultValue(obj.getDiscount());
		get(TAXCODE).setValue(
				CommandUtils.getServerObjectById(obj.getTaxCode(),
						AccounterCoreType.TAX_CODE));
		get(TAX).setDefaultValue(obj.isTaxable());
		get(DESCRIPTION).setDefaultValue(obj.getDescription());
		if (get(IS_BILLABLE) != null) {
			get(IS_BILLABLE).setDefaultValue(obj.isBillable());
			get(ITEM_CUSTOMER).setValue(
					CommandUtils.getServerObjectById(obj.getCustomer(),
							AccounterCoreType.CUSTOMER));
		}
	}

	@Override
	protected ClientTransactionItem getNewObject() {
		ClientTransactionItem clientTransactionItem = new ClientTransactionItem();
		clientTransactionItem.setType(ClientTransactionItem.TYPE_ITEM);
		if (isSales() ? false : getPreferences().isTrackPaidTax())
			clientTransactionItem.setTaxable(true);
		clientTransactionItem.setIsBillable(false);
		ClientQuantity clientQuantity = new ClientQuantity();
		clientQuantity.setValue(1.0);
		clientTransactionItem.setQuantity(clientQuantity);
		clientTransactionItem.setDiscount(0.0);
		clientTransactionItem.setLineTotal(0d);
		clientTransactionItem.setUnitPrice(0.0);
		return clientTransactionItem;
	}

	@Override
	protected Record createFullRecord(ClientTransactionItem t) {
		Record record = new Record(t);
		Item item = (Item) CommandUtils.getServerObjectById(t.getItem(),
				AccounterCoreType.ITEM);
		if (item != null) {
			record.add(getMessages().name(), item.getName());
		}
		if (item != null && item.getType() == Item.TYPE_INVENTORY_PART) {
			Warehouse warehouse = (Warehouse) CommandUtils.getServerObjectById(
					t.getWareHouse(), AccounterCoreType.WAREHOUSE);
			long unitId = t.getQuantity().getUnit();
			Unit unit;
			if (unitId == 0) {
				unit = item.getMeasurement().getDefaultUnit();
			} else {
				unit = (Unit) CommandUtils.getServerObjectById(unitId,
						AccounterCoreType.UNIT);
			}
			record.add(getMessages().quantity(),
					t.getQuantity().getValue() + " " + unit.getType() + " W"
							+ warehouse.getWarehouseCode());
		} else {
			record.add(getMessages().quantity(), t.getQuantity().getValue());
		}
		String formalName;
		String curencySymbol;
		if (getPreferences().isEnableMultiCurrency()) {
			Currency currency = getCurrency();
			formalName = currency.getFormalName();
			curencySymbol = currency.getSymbol();
		} else {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
			curencySymbol = getPreferences().getPrimaryCurrency().getSymbol();
		}
		record.add(
				getMessages().unitPrice() + "(" + formalName + ")",
				Global.get().toCurrencyFormat(
						t.getUnitPrice() == null ? 0d : t.getUnitPrice(),
						curencySymbol));
		if (getPreferences().isTrackTax() && isSales() ? false
				: getPreferences().isTrackPaidTax()) {
			if (getPreferences().isTaxPerDetailLine()) {
				record.add(getMessages().taxCode(),
						((ClientTAXCode) (CommandUtils.getClientObjectById(
								t.getTaxCode(), AccounterCoreType.TAX_CODE,
								getCompanyId()))).getDisplayName());
			} else {
				if (t.isTaxable()) {
					record.add(getMessages().taxable());
				} else {
					record.add(getMessages().taxExempt());
				}
			}
		}
		record.add(getMessages().totalPrice() + "(" + formalName + ")",
				t.getLineTotal());
		if (getPreferences().isTrackDiscounts()
				&& getPreferences().isDiscountPerDetailLine()) {
			record.add(getMessages().discount(), t.getDiscount());
		}
		record.add(getMessages().description(), t.getDescription());
		return record;
	}

	@Override
	protected List<ClientTransactionItem> getList() {
		return null;
	}

	@Override
	protected Record createRecord(ClientTransactionItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<ClientTransactionItem> items = getValue();
		return items.isEmpty() ? getMessages().addOf(getMessages().items())
				: getMessages().addMore(getMessages().items());
	}

	@Override
	protected boolean contains(List<ClientTransactionItem> oldValues,
			ClientTransactionItem t) {
		for (ClientTransactionItem clientTransactionItem : oldValues) {
			if (clientTransactionItem.getItem() != 0
					&& clientTransactionItem.getItem() == t.getItem()) {
				return true;
			}
		}
		return false;
	}

	protected abstract double getCurrencyFactor();

	// protected abstract Payee getPayee();

	protected abstract Currency getCurrency();

	protected abstract double getDiscount();

	@Override
	public <T> T getValue() {
		List<ClientTransactionItem> oldValues = super.getValue();
		for (ClientTransactionItem obj : oldValues) {
			double lt = obj.getQuantity().getValue() * obj.getUnitPrice();
			if (getPreferences().isTrackDiscounts()
					&& !getPreferences().isDiscountPerDetailLine()) {
				obj.setDiscount(getDiscount());
			}
			double disc = obj.getDiscount();
			obj.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);
		}
		return (T) oldValues;
	}
}
