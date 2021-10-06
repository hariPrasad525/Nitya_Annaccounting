package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.ItemGroup;
import com.nitya.accounter.core.Measurement;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.AmountRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.CurrencyAmountRequirement;
import com.nitya.accounter.mobile.requirements.ItemGroupRequirement;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.TaxCodeRequirement;
import com.nitya.accounter.mobile.requirements.VendorRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.UIUtils;

public abstract class AbstractItemCreateCommand extends AbstractCommand {

	private static final String NAME = "name";
	protected static final String I_SELL_THIS = "iSellthis";
	private static final String SALES_DESCRIPTION = "salesDescription";
	private static final String SALES_PRICE = "salesPrice";
	private static final String INCOME_ACCOUNT = "incomeAccount";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String IS_COMMISION_ITEM = "isCommisionItem";
	private static final String STANDARD_COST = "standardCost";
	private static final String ITEM_GROUP = "itemGroup";
	private static final String IS_ACTIVE = "isActive";
	protected static final String I_BUY_THIS = "iBuyService";
	private static final String PURCHASE_DESCRIPTION = "purchaseDescription";
	private static final String PURCHASE_PRICE = "purchasePrice";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String PREFERRED_SUPPLIER = "preferredSupplier";
	private static final String SERVICE_NO = "supplierServiceNo";
	protected static final String WEIGHT = "weight";
	private static final String TAXCODE = "taxCode";
	protected static final String WARE_HOUSE = "wareHouse";
	protected static final String MEASUREMENT = "measurement";
	protected static final String AS_OF = "asOf";
	protected static final String ON_HAND_QTY = "onhandqty";
	protected static final String REORDER_POINT = "reorderpoint";
	protected static final String ASSESTS_ACCOUNT = "assestaccount";

	private ClientItem item;

	public AbstractItemCreateCommand() {
		super();
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(NAME, getMessages().pleaseEnter(getMessages().itemName()), getMessages().name(),
				false, true) {
			@Override
			public void setValue(Object value) {
				if (AbstractItemCreateCommand.this.isItemExistsWithSameName((String) value)) {
					addFirstMessage(getMessages().anItemAlreadyExistswiththisname());
					return;
				}
				addFirstMessage(getMessages().pleaseEnter(getMessages().itemName()));
				super.setValue(value);
			}
		});

		list.add(new BooleanRequirement(I_SELL_THIS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().isellthisservice();
			}

			@Override
			protected String getFalseString() {
				return getMessages().idontSellThisService();
			}

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (getItemType() != ClientItem.TYPE_INVENTORY_PART
						&& getItemType() != ClientItem.TYPE_INVENTORY_ASSEMBLY) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new BooleanRequirement(I_BUY_THIS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().iBuyThisItem();
			}

			@Override
			protected String getFalseString() {
				return getMessages().idontBuyThisService();
			}

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (getItemType() != ClientItem.TYPE_INVENTORY_PART
						&& getItemType() != ClientItem.TYPE_INVENTORY_ASSEMBLY) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NameRequirement(SALES_DESCRIPTION, getMessages().pleaseEnter(getMessages().salesDescription()),
				getMessages().salesDescription(), true, true) {
			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()== Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new CurrencyAmountRequirement(SALES_PRICE, getMessages().pleaseEnter(getMessages().salesPrice()),
				getMessages().salesPrice(), true, true) {
			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()== Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected Currency getCurrency() {
				return null;
			}
		});

		list.add(new AccountRequirement(INCOME_ACCOUNT, getMessages().pleaseEnter(getMessages().incomeAccount()),
				getMessages().incomeAccount(), false, true, null) {

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue() == Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return Utility.filteredList(new ListFilter<Account>() {

					@Override
					public boolean filter(Account account) {
						if (account.getIsActive() && account.getType() == Account.TYPE_INCOME
								|| account.getType() == Account.TYPE_OTHER_INCOME) {
							return true;
						} else {
							return false;
						}
					}
				}, new ArrayList<Account>(context.getCompany().getAccounts()));
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Account());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name) || e.getNumber().equals(name);
			}

			@Override
			protected String getSetMessage() {
				return getMessages().incomeAccountSelected();
			}
		});

		list.add(new BooleanRequirement(IS_TAXABLE, true) {

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()== Boolean.TRUE) {
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

		list.add(new BooleanRequirement(IS_COMMISION_ITEM, true) {

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_SELL_THIS).getValue()== Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().thisIsCommisionItem();
			}

			@Override
			protected String getFalseString() {
				return getMessages().thisIsNoCommisionItem();
			}
		});

		list.add(new CurrencyAmountRequirement(STANDARD_COST, getMessages().pleaseEnter(getMessages().standardCost()),
				getMessages().standardCost(), true, true) {

			@Override
			protected Currency getCurrency() {
				return null;
			}

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (getItemType() != ClientItem.TYPE_INVENTORY_PART
						&& getItemType() != ClientItem.TYPE_INVENTORY_ASSEMBLY) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

		});

		list.add(new ItemGroupRequirement(ITEM_GROUP, getMessages().pleaseEnter(getMessages().itemGroup()),
				getMessages().itemGroup(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().itemGroup());
			}

			@Override
			protected List<ItemGroup> getLists(Context context) {
				return new ArrayList<ItemGroup>(context.getCompany().getItemGroups());
			}

			@Override
			protected boolean filter(ItemGroup e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnter(getMessages().taxCode()),
				getMessages().taxCode(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (true) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany().getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
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

		list.add(new NameRequirement(PURCHASE_DESCRIPTION,
				getMessages().pleaseEnter(getMessages().purchaseDescription()), getMessages().purchaseDescription(),
				true, true) {
			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()== Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});
		String purchaseCost = (getItemType() != ClientItem.TYPE_INVENTORY_PART
				&& getItemType() != ClientItem.TYPE_INVENTORY_ASSEMBLY) ? getMessages().purchasePrice()
						: getMessages().standardCost();
		list.add(new AmountRequirement(PURCHASE_PRICE, getMessages().pleaseEnter(purchaseCost), purchaseCost, true,
				true) {
			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()== Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		final String purchaseAccount = (getItemType() != ClientItem.TYPE_INVENTORY_PART
				&& getItemType() != ClientItem.TYPE_INVENTORY_ASSEMBLY) ? getMessages().expenseAccount()
						: getMessages().costOfGoodSold();

		list.add(new AccountRequirement(EXPENSE_ACCOUNT, getMessages().pleaseEnter(purchaseAccount), purchaseAccount,
				false, true, null) {

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()== Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return Utility.filteredList(new ListFilter<Account>() {

					@Override
					public boolean filter(Account account) {
						return (account.getType() == Account.TYPE_COST_OF_GOODS_SOLD
								|| account.getType() == Account.TYPE_EXPENSE
								|| account.getType() == Account.TYPE_OTHER_EXPENSE) && account.getIsActive();
					}
				}, new ArrayList<Account>(context.getCompany().getAccounts()));
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(purchaseAccount);
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name) || e.getNumber().equals(name);
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(purchaseAccount);
			}
		});

		list.add(new VendorRequirement(PREFERRED_SUPPLIER,
				getMessages().pleaseEnter(getMessages().preferredVendor(Global.get().vendor())),
				getMessages().preferredVendor(Global.get().Vendor()), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()== Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().preferredVendor(Global.get().vendor()));
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return getVendors();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(SERVICE_NO,
				getMessages().pleaseEnter(getMessages().vendorServiceNo(Global.get().Vendor())),
				getMessages().vendorServiceNo(Global.get().Vendor()), true, true) {
			@Override
			public Result run(Context context, Result makeResult, ResultList list, ResultList actions) {
				if (AbstractItemCreateCommand.this.get(I_BUY_THIS).getValue()== Boolean.TRUE) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

	}

	protected boolean isItemExistsWithSameName(String value) {
		Set<Item> items = getCompany().getItems();
		for (Item item : items) {
			if (item.getID() != this.item.getID() && item.getName().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
		get(IS_TAXABLE).setDefaultValue(Boolean.TRUE);
		get(EXPENSE_ACCOUNT).setDefaultValue(getCompany().getCostOfGoodsSold());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = (String) get(NAME).getValue();

		// TODO:check weather it is product or service item
		Integer weight = 0;
		Requirement weightReq = get(WEIGHT);
		if (weightReq != null) {
			String s = weightReq.getValue();
			weight = s != null ? Integer.parseInt(s) : 0;
		}
		Boolean iSellthis = (Boolean) get(I_SELL_THIS).getValue();
		String description = (String) get(SALES_DESCRIPTION).getValue();
		double price = (Double) get(SALES_PRICE).getValue();
		Account incomeAccount = get(INCOME_ACCOUNT).getValue();
		Boolean isTaxable = (Boolean) get(IS_TAXABLE).getValue();
		Boolean isCommisionItem = (Boolean) get(IS_COMMISION_ITEM).getValue();
		double cost = (Double) get(STANDARD_COST).getValue();
		ItemGroup itemGroup = (ItemGroup) get(ITEM_GROUP).getValue();
		TAXCode vatcode = (TAXCode) get(TAXCODE).getValue();
		Boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		Boolean isBuyservice = (Boolean) get(I_BUY_THIS).getValue();
		item.setIBuyThisItem(isBuyservice);
		String purchaseDescription = (String) get(PURCHASE_DESCRIPTION).getValue();
		double purchasePrice = (Double) get(PURCHASE_PRICE).getValue();
		Account expenseAccount = (Account) get(EXPENSE_ACCOUNT).getValue();
		Vendor preferedSupplier = (Vendor) get(PREFERRED_SUPPLIER).getValue();
		String supplierServiceNo = (String) get(SERVICE_NO).getValue();

		item.setName(name);
		item.setISellThisItem(iSellthis);

		Requirement reorderPointReq = get(REORDER_POINT);
		Double reorderPoint = 0.0;
		if (reorderPointReq != null) {
			reorderPoint = reorderPointReq.getValue();
		}

		Requirement assestsAccReq = get(ASSESTS_ACCOUNT);
		if (assestsAccReq != null) {
			if (assestsAccReq.getValue() != null) {
				Account assetsAccount = assestsAccReq.getValue();
				item.setAssestsAccount(assetsAccount.getID());
			}
		}
		Requirement onHandQtyReq = get(ON_HAND_QTY);
		Double onHandQty = 0.0;
		if (onHandQtyReq != null) {
			onHandQty = onHandQtyReq.getValue();
		}
		if ((getItemType() == ClientItem.TYPE_NON_INVENTORY_PART || getItemType() == ClientItem.TYPE_INVENTORY_PART
				|| getItemType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) && weight != null) {
			item.setWeight(UIUtils.toInt(weight));

			Double amount2 = Double.valueOf(onHandQty);
			item.setItemTotalValue(purchasePrice * amount2);

			if (getItemType() == ClientItem.TYPE_INVENTORY_PART
					|| getItemType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
				item.setISellThisItem(true);
				item.setIBuyThisItem(true);
				item.setMinStockAlertLevel(null);
				item.setMaxStockAlertLevel(null);
			}
		}

		if (item.isISellThisItem()) {
			item.setSalesDescription(description);
			item.setSalesPrice(price);
			item.setIncomeAccount(incomeAccount.getID());
			item.setTaxable(isTaxable);
			item.setCommissionItem(isCommisionItem);
		}
		item.setStandardCost(cost);
		if (getItemType() == ClientItem.TYPE_NON_INVENTORY_PART || getItemType() == ClientItem.TYPE_SERVICE) {
			item.setTaxCode(vatcode != null ? vatcode.getID() : 0);
		}
		item.setActive(isActive);

		if (itemGroup != null)
			item.setItemGroup(itemGroup.getID());
		if (item.isIBuyThisItem()) {
			item.setPurchaseDescription(purchaseDescription);
			item.setPurchasePrice(purchasePrice);
			item.setExpenseAccount(expenseAccount.getID());
			if (preferedSupplier != null)
				item.setPreferredVendor(preferedSupplier.getID());
			item.setVendorItemNumber(supplierServiceNo);
		}
		item.setType(getItemType());
		Requirement requirement = get(WARE_HOUSE);
		if (requirement != null) {
			Warehouse warehouse = requirement.getValue();
			item.setWarehouse(warehouse.getID());
		}

		Requirement measurementreq = get(MEASUREMENT);

		// item.setItemTotalValue(itemTotalValue.getAmount());
		if (measurementreq != null) {
			Measurement measurement = measurementreq.getValue();
			item.setMeasurement(measurement.getID());
			Double qtyValue = null;
			if (onHandQty > 0) {
				qtyValue = onHandQty;
			} else {
				qtyValue = 0.0D;
			}
			if (qtyValue != null) {
				ClientQuantity qty = new ClientQuantity();
				qty.setValue(qtyValue.doubleValue());
				qty.setUnit(measurement.getDefaultUnit().getID());
				item.setOnhandQty(qty);
			}
			Double reorderedValue = null;
			if (onHandQty > 0) {
				reorderedValue = onHandQty;
			} else {
				reorderedValue = 0.0D;
			}
			if (reorderedValue != null) {
				ClientQuantity reorderedOty = new ClientQuantity();
				reorderedOty.setValue(reorderedValue.doubleValue());
				reorderedOty.setUnit(measurement.getDefaultUnit().getID());
				item.setOnhandQty(reorderedOty);
			}

		}

		Requirement asOfRequiremnt = get(AS_OF);
		if (asOfRequiremnt != null) {
			ClientFinanceDate asof = asOfRequiremnt.getValue();
			item.setAsOfDate(asof);
		}

		create(item, context);
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return getItem().getID() == 0 ? getMessages().readyToCreate(getMessages().item())
				: getMessages().itemIsReadyToUpdateWithFollowingDetails();

	}

	@Override
	public String getSuccessMessage() {
		return getItem().getID() == 0 ? getMessages().createSuccessfully(getMessages().item())
				: getMessages().updateSuccessfully(getMessages().item());
	}

	@Override
	protected String getDeleteCommand(Context context) {
		String itemCommand = null;
		if (item.getID() != 0)
			switch (item.getType()) {
			case Item.TYPE_SERVICE:
				itemCommand = "deleteServiceItem " + item.getID();
				break;
			case Item.TYPE_NON_INVENTORY_PART:
				itemCommand = "deleteNonInventoryItem " + item.getID();
				break;
			case Item.TYPE_INVENTORY_PART:
				itemCommand = "deleteInventoryItem " + item.getID();
				break;
			}
		return itemCommand;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (!isUpdate) {
			if (!string.isEmpty()) {
				if (string.equals("sell")) {
					get(I_SELL_THIS).setValue(true);
				} else {
					get(I_BUY_THIS).setValue(true);
				}
			} else {
				get(I_SELL_THIS).setValue(true);
			}
			setItem(new ClientItem());
		} else {
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages().selectItemToUpdate());
				return "listOfItems";
			}
			Item customerByName = CommandUtils.getItemByName(context.getCompany(), string);
			if (customerByName == null) {
				addFirstMessage(context, getMessages().selectItemToUpdate());
				return "listOfItems " + string;
			}
			setItem((ClientItem) CommandUtils.getClientObjectById(customerByName.getID(), AccounterCoreType.ITEM,
					context.getCompany().getId()));
			setValues();
		}

		if (getItemType() == ClientItem.TYPE_INVENTORY_PART) {
			get(I_SELL_THIS).setValue(true);
			get(I_BUY_THIS).setValue(true);
		}

		return null;
	}

	protected void setValues() {
		get(NAME).setValue(item.getName());

		get(I_SELL_THIS).setValue(item.isISellThisItem());
		get(SALES_DESCRIPTION).setValue(item.getSalesDescription());
		get(SALES_PRICE).setValue(item.getSalesPrice());
		get(INCOME_ACCOUNT)
				.setValue(CommandUtils.getServerObjectById(item.getIncomeAccount(), AccounterCoreType.ACCOUNT));
		get(IS_TAXABLE).setValue(item.isTaxable());
		get(IS_COMMISION_ITEM).setValue(item.isCommissionItem());
		long itemGroup = item.getItemGroup();
		get(ITEM_GROUP).setValue(CommandUtils.getServerObjectById(itemGroup, AccounterCoreType.ITEM_GROUP));
		get(TAXCODE).setValue(CommandUtils.getServerObjectById(item.getTaxCode(), AccounterCoreType.TAX_CODE));
		get(IS_ACTIVE).setValue(item.isActive());
		get(I_BUY_THIS).setValue(item.isIBuyThisItem);
		get(PURCHASE_DESCRIPTION).setValue(item.getPurchaseDescription());
		get(PURCHASE_PRICE).setValue(item.getPurchasePrice());
		get(EXPENSE_ACCOUNT)
				.setValue(CommandUtils.getServerObjectById(item.getExpenseAccount(), AccounterCoreType.ACCOUNT));
		get(PREFERRED_SUPPLIER)
				.setValue(CommandUtils.getServerObjectById(item.getPreferredVendor(), AccounterCoreType.VENDOR));

		if (get(WARE_HOUSE) != null) {
			get(WARE_HOUSE)
					.setValue(CommandUtils.getServerObjectById(item.getWarehouse(), AccounterCoreType.WAREHOUSE));
		}

		if (get(MEASUREMENT) != null) {
			get(MEASUREMENT)
					.setValue(CommandUtils.getServerObjectById(item.getMeasurement(), AccounterCoreType.MEASUREMENT));
		}

		Requirement assestsAccReq = get(ASSESTS_ACCOUNT);
		if (assestsAccReq != null) {
			Account account = getServerObject(Account.class, item.getAssestsAccount());
			assestsAccReq.setValue(account);
		}

		Requirement asOfRequiremnt = get(AS_OF);
		if (asOfRequiremnt != null) {
			asOfRequiremnt.setValue(item.getAsOfDate());
		}
	}

	public ClientItem getItem() {
		return item;
	}

	public void setItem(ClientItem item) {
		this.item = item;
	}

	public abstract int getItemType();
}
