package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyAmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CreateVendorPrepaymentCommand extends AbstractTransactionCommand {
	ClientPayBill paybill;

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().payeePrePayment(
										Global.get().Vendor())));
				return "vendorPaymentsList";
			}
			paybill = getTransaction(string, AccounterCoreType.PAYBILL, context);
			if (paybill == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().payeePrePayment(
										Global.get().Vendor())));
				return "vendorPaymentsList " + string;
			}
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			paybill = new ClientPayBill();
		}
		setTransaction(paybill);
		return null;
	}

	private void setValues() {
		get(VENDOR).setValue(
				CommandUtils.getServerObjectById(paybill.getVendor(),
						AccounterCoreType.VENDOR));
		get(BILL_TO).setValue(paybill.getAddress());
		get(PAY_FROM).setValue(
				CommandUtils.getServerObjectById(paybill.getPayFrom(),
						AccounterCoreType.ACCOUNT));
		/* get(CURRENCY_FACTOR).setValue(paybill.getCurrencyFactor()); */
		get(AMOUNT).setValue(paybill.getTotal());
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						paybill.getPaymentMethodForCommands(), getMessages()));
		get(TO_BE_PRINTED).setValue(paybill.isToBePrinted());
		get(MEMO).setValue(paybill.getMemo());
		get(NUMBER).setValue(paybill.getNumber());
		get(CHEQUE_NO).setValue(paybill.getCheckNumber());
		get(DATE).setValue(paybill.getDate());
	}

	@Override
	protected String getWelcomeMessage() {
		return paybill.getID() == 0 ? getMessages().create(
				getMessages().payeePrePayment(Global.get().Vendor()))
				: getMessages().readyToUpdate(
						getMessages().payeePrePayment(Global.get().Vendor()));
	}

	@Override
	protected String getDetailsMessage() {
		return paybill.getID() == 0 ? getMessages().readyToCreate(
				getMessages().payeePrePayment(Global.get().Vendor()))
				: getMessages().readyToUpdate(
						getMessages().payeePrePayment(Global.get().Vendor()));
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_VENDOR_PAYMENT,
						context.getCompany()));

	}

	@Override
	public String getSuccessMessage() {

		return paybill.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().payeePrePayment(Global.get().Vendor()))
				: getMessages().updateSuccessfully(
						getMessages().payeePrePayment(Global.get().Vendor()));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getMessages().Vendor()), getMessages().Vendor(), false, true,
				new ChangeListner<Vendor>() {

					@Override
					public void onSelection(Vendor value) {
						get(BILL_TO).setValue(null);
						Set<Address> addresses = value.getAddress();
						for (Address address : addresses) {
							if (address.getType() == Address.TYPE_BILL_TO) {
								try {
									ClientAddress addr = new ClientConvertUtil()
											.toClientObject(address,
													ClientAddress.class);
									get(BILL_TO).setValue(addr);
								} catch (AccounterException e) {
									e.printStackTrace();
								}
								break;
							}
						}

						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateVendorPrepaymentCommand.this.get(
									CURRENCY_FACTOR).setValue(
									mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}

					}
				})

		{

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
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
		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Vendor vendor = (Vendor) get(VENDOR).getValue();
				return vendor.getCurrency();
			}
		});
		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().billNo()), getMessages().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(), getMessages().bankAccount(),
				false, false, null) {
			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createBankAccount", getMessages()
						.bank()));
				list.add(new UserCommand("createBankAccount",
						"Create Other CurrentAsset Account", getMessages()
								.otherCurrentAsset()));
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
							if (e.getIsActive()
									&& (e.getType() == Account.TYPE_BANK || e
											.getType() == Account.TYPE_OTHER_ASSET)) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {

				/*
				 * Map<String, String> paymentMethods =
				 * context.getClientCompany() .getPaymentMethods(); List<String>
				 * paymentMethod = new ArrayList<String>(
				 * paymentMethods.values());
				 */
				String payVatMethodArray[] = new String[] {
						getMessages().cash(), getMessages().creditCard(),
						getMessages().check(), getMessages().directDebit(),
						getMessages().masterCard(),
						getMessages().onlineBanking(),
						getMessages().standingOrder(),
						getMessages().switchMaestro() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});
		list.add(new CurrencyAmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getMessages().amount()), getMessages().amount(),
				false, true) {

			@Override
			protected Currency getCurrency() {
				Payee payee = (Payee) CreateVendorPrepaymentCommand.this.get(
						VENDOR).getValue();
				return payee.getCurrency();
			}
		});

		list.add(new BooleanRequirement(TO_BE_PRINTED, true) {

			@Override
			protected String getTrueString() {
				return getMessages().toBePrinted();
			}

			@Override
			protected String getFalseString() {
				return getMessages().notPrinted();
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getMessages().checkNo()), getMessages().checkNo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if ((Boolean) get(TO_BE_PRINTED).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;

			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Vendor vendor = (Vendor) get(VENDOR).getValue();
		ClientAddress billTo = (ClientAddress) get(BILL_TO).getValue();
		Account pay = (Account) get(PAY_FROM).getValue();

		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * paybill.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * paybill.setCurrencyFactor(factor); }
		 */
		double amount = get(AMOUNT).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		Boolean toBePrinted = (Boolean) get(TO_BE_PRINTED).getValue();
		String memo = get(MEMO).getValue();
		String chequeNumber = get(CHEQUE_NO).getValue();

		ClientFinanceDate transactionDate = get(DATE).getValue();
		paybill.setDate(transactionDate.getDate());
		paybill.setType(ClientTransaction.TYPE_PAY_BILL);
		paybill.setVendor(vendor.getID());
		paybill.setAddress(billTo);
		paybill.setPayFrom(pay.getID());
		paybill.setTotal(amount);
		paybill.setStatus(ClientPayBill.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED);
		paybill.setPaymentMethod(paymentMethod);
		paybill.setMemo(memo);
		paybill.setToBePrinted(toBePrinted);
		paybill.setNumber((String) get(NUMBER).getValue());
		paybill.setCheckNumber(chequeNumber);
		paybill.setCurrency(vendor.getCurrency().getID());
		paybill.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		create(paybill, context);
		return null;
	}

	@Override
	protected Currency getCurrency() {
		return ((Vendor) CreateVendorPrepaymentCommand.this.get(VENDOR)
				.getValue()).getCurrency();
	}
}