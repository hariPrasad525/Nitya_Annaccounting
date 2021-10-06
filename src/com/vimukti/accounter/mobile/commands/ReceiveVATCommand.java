package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.ReceiveVATEntries;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.ReceiveVatTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientReceiveVATEntries;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionReceiveVAT;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

public class ReceiveVATCommand extends AbstractTransactionCommand {

	private static final String VAT_RETURN_END_DATE = "vatReturnEndDate";
	private static final String BILLS_TO_RECEIVE = "billToReceive";
	private static final String DEPOSIT_TO = "depositTo";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new AccountRequirement(DEPOSIT_TO, getMessages().pleaseSelect(
				getMessages().depositAccount()),
				getMessages().depositAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createBankAccount", getMessages()
						.bank()));
				list.add(new UserCommand("createBankAccount",
						"Create Other CurrentAsset Account", getMessages()
								.otherCurrentAsset()));
				list.add(new UserCommand("createBankAccount",
						"Create CreditAccount", getMessages().creditCard()));
				list.add(new UserCommand("createBankAccount",
						"Create FixedAsset Account", getMessages().fixedAsset()));
			}

			@Override
			protected List<Account> getLists(final Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getIsActive()
									&& Arrays
											.asList(ClientAccount.TYPE_BANK,
													ClientAccount.TYPE_CREDIT_CARD,
													ClientAccount.TYPE_OTHER_CURRENT_ASSET,
													ClientAccount.TYPE_FIXED_ASSET)
											.contains(e.getType())
									&& e.getType() != Account.TYPE_ACCOUNT_RECEIVABLE;
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
				return false;
			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()), getMessages()
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
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});

		list.add(new DateRequirement(VAT_RETURN_END_DATE, getMessages()
				.pleaseEnter(getMessages().returnsDueOnOrBefore()),
				getMessages().returnsDueOnOrBefore(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new Requirement(BILLS_TO_RECEIVE, false, true));
		list.add(new ReceiveVatTableRequirement(BILLS_TO_RECEIVE, getMessages()
				.pleaseSelect(getMessages().billsToReceive()), getMessages()
				.billsToReceive()) {

			@Override
			protected List<ClientTransactionReceiveVAT> getList() {
				return getTransactionReceiveVatBills();
			}

		});
	}

	private List<ClientTransactionReceiveVAT> getTransactionReceiveVatBills() {

		ArrayList<ClientReceiveVATEntries> clientEntries = new ArrayList<ClientReceiveVATEntries>();

		FinanceTool tool = new FinanceTool();
		List<ReceiveVATEntries> entries = tool.getTaxManager()
				.getReceiveVATEntries(getCompanyId());
		for (ReceiveVATEntries entry : entries) {
			ClientReceiveVATEntries clientEntry = new ClientReceiveVATEntries();
			// VATReturn vatReturn =(VATReturn) entry.getTransaction();
			// ClientVATReturn clientVATReturn = new
			// ClientConvertUtil().toClientObject(vatReturn,ClientVATReturn.class);
			clientEntry.setTAXReturn(entry.getTransaction().getID());
			clientEntry.setTAXAgency(entry.getTAXAgency() != null ? entry
					.getTAXAgency().getID() : null);
			clientEntry.setBalance(entry.getBalance());
			clientEntry.setAmount(entry.getAmount());

			clientEntries.add(clientEntry);
		}

		return getClientTransactionReceiveVATRecords(clientEntries);
	}

	private List<ClientTransactionReceiveVAT> getClientTransactionReceiveVATRecords(
			ArrayList<ClientReceiveVATEntries> clientEntries) {
		List<ClientTransactionReceiveVAT> records = new ArrayList<ClientTransactionReceiveVAT>();
		for (ClientReceiveVATEntries entry : clientEntries) {
			ClientTransactionReceiveVAT clientEntry = new ClientTransactionReceiveVAT();

			clientEntry.setTaxAgency(entry.getTAXAgency());
			clientEntry.setTAXReturn(entry.getTAXReturn());
			// clientEntry.setAmountToReceive(entry.getAmount())
			double total = entry.getAmount();
			double balance = entry.getBalance();
			// clientEntry
			// .setTaxDue(total - balance > 0.0 ? total - balance : 0.0);
			clientEntry.setTaxDue(balance);

			records.add(clientEntry);
		}

		return records;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientReceiveVAT receiveVAT = new ClientReceiveVAT();
		ClientAccount depositTo = get(DEPOSIT_TO).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<ClientTransactionReceiveVAT> billsToReceive = get(BILLS_TO_RECEIVE)
				.getValue();
		ClientFinanceDate vatReturnDate = get(VAT_RETURN_END_DATE).getValue();
		ClientFinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();

		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) {
		 * ClientCurrency currency = get(CURRENCY).getValue(); if (currency !=
		 * null) { receiveVAT.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * receiveVAT.setCurrencyFactor(factor); }
		 */

		receiveVAT.setDepositIn(depositTo.getID());
		receiveVAT.setPaymentMethod(paymentMethod);
		receiveVAT.setClientTransactionReceiveVAT(billsToReceive);
		receiveVAT.setReturnsDueOnOrBefore(vatReturnDate.getDate());
		receiveVAT.setDate(transactionDate.getDate());
		receiveVAT.setNumber(orderNo);

		create(receiveVAT, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().receiveVAT());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().receiveVAT());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(PAYMENT_METHOD).setDefaultValue(getMessages().cash());
		get(VAT_RETURN_END_DATE).setDefaultValue(new ClientFinanceDate());
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(ORDER_NO).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_RECEIVE_TAX, getCompany()));

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().receiveVAT());
	}

	@Override
	protected Currency getCurrency() {
		return null;
	}

}
