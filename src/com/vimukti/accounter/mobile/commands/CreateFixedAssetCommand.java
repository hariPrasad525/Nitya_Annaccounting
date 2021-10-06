package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.FixedAsset;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyAmountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CreateFixedAssetCommand extends AbstractCommand {

	private static final String NEW_ITEM = "newItem";
	private static final String ASSET_NUMBER = "assetNumber";
	private static final String ACCOUNT = "account";
	private static final String PURCAHSE_DATE = "purchaseDate";
	private static final String PURCHASE_PRICE = "purchasePrice";
	private static final String DESCRIPTION = "description";
	private static final String ASSET_TYPE = "assetType";
	private static final String DEPRICATION_METHOD = "depricationMethod";
	private static final String DEPRECATION_RATE = "depreciationRate";
	private static final String DEPRECATION_ACCOUNT = "deprecationAccount";
	private static final String ACCUMULATED_DEPRECATION_ACCOUNT = "accumulatedDepreciationAccount";
	private static final String REGISTER = "register";

	private boolean isAssetAccumulated;
	private double accmulatdDepreciationAmount;
	private ClientFixedAsset fixedAsset;
	private boolean isRegistered;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new BooleanRequirement(REGISTER, true) {

			@Override
			protected String getTrueString() {
				return "Register this Item";
			}

			@Override
			protected String getFalseString() {
				return "Don not Register this Item";
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				if (value != null) {
					isRegistered = (Boolean) value;
				}
			}
		});

		list.add(new NameRequirement(NEW_ITEM, getMessages().pleaseEnter(
				getMessages().itemName()), getMessages().newItem(), false, true));

		list.add(new NumberRequirement(ASSET_NUMBER, getMessages().pleaseEnter(
				getMessages().assetNumber()), getMessages().assetNumber(),
				false, true));

		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getMessages().account()), getMessages().account(), true, true,
				null) {
			@Override
			public boolean isOptional() {
				return !isRegistered;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getType() == ClientAccount.TYPE_FIXED_ASSET;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().account());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createAccount", getMessages()
						.fixedAsset()));
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});

		list.add(new DateRequirement(PURCAHSE_DATE, getMessages().pleaseEnter(
				getMessages().purchaseDate()), getMessages().purchaseDate(),
				false, true));

		list.add(new CurrencyAmountRequirement(PURCHASE_PRICE, getMessages()
				.pleaseEnter(getMessages().purchasePrice()), getMessages()
				.purchasePrice(), true, true) {
			@Override
			public boolean isOptional() {
				return !isRegistered;
			}

			@Override
			protected Currency getCurrency() {
				return getCompany().getPrimaryCurrency();

			}
		});
		list.add(new StringRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().description()), getMessages().description(),
				true, true));

		list.add(new StringRequirement(ASSET_TYPE, getMessages().pleaseEnter(
				getMessages().assetType()), getMessages().assetType(), true,
				true) {
			@Override
			public boolean isOptional() {
				return !isRegistered;
			}
		});

		list.add(new StringListRequirement(DEPRICATION_METHOD, getMessages()
				.pleaseSelect(getMessages().depreciationMethod()),
				getMessages().depreciationMethod(), true, true, null) {
			@Override
			public boolean isOptional() {
				return !isRegistered;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().depreciationMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().depreciationMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				String payVatMethodArray[] = new String[] {
						getMessages().straightLine(),
						getMessages().reducingBalance() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().depreciationMethod());
			}
		});

		list.add(new AmountRequirement(DEPRECATION_RATE, getMessages()
				.pleaseEnter(getMessages().depreciationRate()), getMessages()
				.depreciationRate(), true, true) {
			@Override
			public boolean isOptional() {
				return !isRegistered;
			}

			@Override
			protected void createRecord(ResultList list) {
				Double t = getValue();
				Record nameRecord = new Record(getName());
				nameRecord.add(getRecordName(), getDisplayValue(t) + "%");
				list.add(nameRecord);
			}

		});

		list.add(new AccountRequirement(DEPRECATION_ACCOUNT, getMessages()
				.pleaseSelect(getMessages().depreciationAccount()),
				getMessages().depreciationAccount(), false, true, null) {
			@Override
			public boolean isOptional() {
				return !isRegistered;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().depreciationAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getType() == ClientAccount.TYPE_EXPENSE;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().depreciationAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});

		list.add(new AccountRequirement(ACCUMULATED_DEPRECATION_ACCOUNT,
				getMessages().pleaseSelect(
						getMessages().accumulatedDepreciationAccount()),
				getMessages().accumulatedDepreciationAccount(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Account account = CreateFixedAssetCommand.this.get(ACCOUNT)
						.getValue();
				if (account != null) {
					if (account.getLinkedAccumulatedDepreciationAccount() == null) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().accumulatedDepreciationAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getType() == ClientAccount.TYPE_FIXED_ASSET;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createAccount", getMessages()
						.fixedAsset()));
			}

			@Override
			public void setValue(Object value) {
				Account assetAccount = CreateFixedAssetCommand.this
						.get(ACCOUNT).getValue();
				Account selectItem = (Account) value;
				if (assetAccount != null && selectItem != null
						&& assetAccount.getID() != selectItem.getID()) {
					super.setValue(value);
					return;
				}
				addFirstMessage(getMessages()
						.accandaccumulatedDepreciationAccShouldnotbesame());
				super.setValue(null);
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().accumulatedDepreciationAccount());
			}

			@Override
			public boolean isOptional() {
				return !isRegistered;
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				return "fixedAssetsPendingItemsList";
			}
			fixedAsset = (ClientFixedAsset) CommandUtils.getClientObjectById(
					Long.parseLong(string), AccounterCoreType.FIXEDASSET,
					getCompanyId());
			IAccounterServerCore serverObjectById = CommandUtils
					.getServerObjectById(Long.parseLong(string),
							AccounterCoreType.FIXEDASSET);
			if (fixedAsset == null || serverObjectById == null) {
				return "fixedAssetsPendingItemsList";
			}
			setValues((FixedAsset) serverObjectById);
		} else {
			fixedAsset = new ClientFixedAsset();
		}

		return null;
	}

	private void setValues(FixedAsset fixedAsset) {
		get(NEW_ITEM).setValue(fixedAsset.getName());
		get(ASSET_NUMBER).setValue(fixedAsset.getAssetNumber());
		get(ACCOUNT).setValue(fixedAsset.getAssetAccount());
		get(PURCAHSE_DATE).setValue(
				new ClientFinanceDate(fixedAsset.getPurchaseDate().getDate()));
		get(PURCHASE_PRICE).setValue(fixedAsset.getPurchasePrice());
		get(DESCRIPTION).setValue(fixedAsset.getDescription());
		get(ASSET_TYPE).setValue(fixedAsset.getAssetType());
		get(DEPRICATION_METHOD)
				.setValue(
						getDepricationMethodAsString(fixedAsset
								.getDepreciationMethod()));
		get(DEPRECATION_RATE).setValue(fixedAsset.getDepreciationRate());
		get(DEPRECATION_ACCOUNT).setValue(
				fixedAsset.getDepreciationExpenseAccount());
		get(ACCUMULATED_DEPRECATION_ACCOUNT).setValue(
				fixedAsset.getAccumulatedDepreciationAccount());
		get(REGISTER).setValue(
				fixedAsset.getStatus() == ClientFixedAsset.STATUS_REGISTERED);

	}

	@Override
	protected String getWelcomeMessage() {
		return fixedAsset.getID() == 0 ? getMessages().creating(
				getMessages().fixedAsset()) : getMessages().updating(
				getMessages().fixedAsset());
	}

	@Override
	protected String getDetailsMessage() {
		return fixedAsset.getID() == 0 ? getMessages().readyToCreate(
				getMessages().fixedAsset()) : getMessages().readyToUpdate(
				getMessages().fixedAsset());
	}

	@Override
	protected void setDefaultValues(Context context) {
		try {
			String nextFixedAssetNumber = new FinanceTool()
					.getNextFixedAssetNumber(getCompanyId());
			get(ASSET_NUMBER).setValue(nextFixedAssetNumber);
			get(PURCAHSE_DATE).setValue(new ClientFinanceDate());
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getSuccessMessage() {
		return fixedAsset.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().fixedAsset()) : getMessages().updateSuccessfully(
				getMessages().fixedAsset());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		fixedAsset = new ClientFixedAsset();

		fixedAsset.setName((String) get(NEW_ITEM).getValue());
		fixedAsset.setAssetNumber((String) get(ASSET_NUMBER).getValue());
		Account fixedAccount = get(ACCOUNT).getValue();
		Account accumulatedAccount = get(ACCUMULATED_DEPRECATION_ACCOUNT)
				.getValue();
		if (fixedAccount != null) {

			if (accumulatedAccount != null) {
				fixedAsset
						.setLinkedAccumulatedDepreciationAccount(accumulatedAccount
								.getID());
				fixedAccount
						.setLinkedAccumulatedDepreciationAccount(accumulatedAccount);
			}
			fixedAsset.setAssetAccount(fixedAccount.getID());

		}
		ClientFinanceDate purchaseDate = get(PURCAHSE_DATE).getValue();
		fixedAsset.setPurchaseDate(purchaseDate.getDate());
		showAccumultdDepAmountForm(purchaseDate);
		fixedAsset.setPurchasePrice((Double) get(PURCHASE_PRICE).getValue());
		fixedAsset.setDescription((String) get(DESCRIPTION).getValue());
		fixedAsset.setAssetType((String) get(ASSET_TYPE).getValue());

		fixedAsset.setDepreciationRate((Double) get(DEPRECATION_RATE)
				.getValue());
		fixedAsset.setDepreciationMethod(getDepricationMethod());

		Account depreciationAccount = get(DEPRECATION_ACCOUNT).getValue();
		if (depreciationAccount != null) {
			fixedAsset.setDepreciationExpenseAccount(depreciationAccount
					.getID());
		}

		fixedAsset.setBookValue((Double) get(PURCHASE_PRICE).getValue());

		if (isAssetAccumulated) {
			fixedAsset
					.setAccumulatedDepreciationAmount(accmulatdDepreciationAmount);
		} else {
			fixedAsset.setAccumulatedDepreciationAmount(0.0);
		}

		fixedAsset.setStatus(isRegistered ? ClientFixedAsset.STATUS_REGISTERED
				: ClientFixedAsset.STATUS_PENDING);
		create(fixedAsset, context);
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public ClientFinanceDate getDepreciationStartDate() {
		return new ClientFinanceDate(getPreferences()
				.getDepreciationStartDate());
	}

	/*
	 * The AccumulatedDepreciationAmount field need to be added when given date
	 * is before the depreciation startdate
	 */

	private void showAccumultdDepAmountForm(ClientFinanceDate enteredDate) {
		if (getDepreciationStartDate() != null) {
			if (!enteredDate.equals(getDepreciationStartDate())
					&& enteredDate.before(getDepreciationStartDate())) {
				isAssetAccumulated = true;
				accmulatdDepreciationAmount = getDepreciationAmount();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public double getDepreciationAmount() {
		ClientFinanceDate purchaseDate = get(PURCAHSE_DATE).getValue();
		int depMethod = getDepricationMethod();
		double depRate = get(DEPRECATION_RATE).getValue();
		double purchasePrice = get(PURCHASE_PRICE).getValue();
		ClientFinanceDate depStartDate = getDepreciationStartDate();
		if (depMethod != 0 && !DecimalUtil.isEquals(depRate, 0)
				&& !DecimalUtil.isEquals(purchasePrice, 0)
				&& purchaseDate.before(depStartDate)) {
			FinanceTool tool = new FinanceTool();
			try {
				return tool.getFixedAssetManager()
						.getCalculatedDepreciatedAmount(depMethod, depRate,
								purchasePrice, purchaseDate.getDate(),
								depStartDate.getDate(), getCompanyId());
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
		return 0.0;

	}

	private int getDepricationMethod() {
		String string = get(DEPRICATION_METHOD).getValue();
		if (string == null) {
			return 0;
		} else if (string.equals(getMessages().straightLine())) {
			return 1;
		} else if (string.equals(getMessages().reducingBalance())) {
			return 2;
		}
		return 0;
	}

	private String getDepricationMethodAsString(int method) {
		if (method == 0) {
			return null;
		} else if (method == 1) {
			return getMessages().straightLine();
		} else if (method == 2) {
			return getMessages().reducingBalance();
		}
		return null;
	}
}