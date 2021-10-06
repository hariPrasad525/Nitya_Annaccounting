package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Utility;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.AccountRegister;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class AccountRegisterCommand extends AbstractCommand {

	private static final String SHOWTRANSACTION_TYPE = "ShowTransactions Type";
	private static final String ACCOUNT = "account";
	private static final String ACCOUNT_REGISTER = "accountRegister";
	private ClientFinanceDate startDate, endDate;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		Account account = CommandUtils.getaccount(context.getCompany(), string);
		get(ACCOUNT).setValue(account);
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
		get(SHOWTRANSACTION_TYPE).setDefaultValue(getMessages().all());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getMessages().Account()), getMessages().Account(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return new ArrayList<Account>(context.getCompany()
						.getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().account());
			}
		});

		list.add(new CommandsRequirement(SHOWTRANSACTION_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().all());
				list.add(getMessages().today());
				list.add(getMessages().last30Days());
				list.add(getMessages().last45Days());
				return list;
			}
		});

		list.add(new ShowListRequirement<AccountRegister>(ACCOUNT_REGISTER,
				getMessages().pleaseEnter(getMessages().name()), 20) {

			@Override
			protected String onSelection(AccountRegister value) {
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(AccountRegister accRegister) {
				long currencyId = accRegister.getCurrency();
				Currency currency = (Currency) HibernateUtil
						.getCurrentSession().get(Currency.class, currencyId);
				Account account = get(ACCOUNT).getValue();
				if (account != null) {
					currency = account.getCurrency();
				}

				if (currency == null) {
					currency = getCompany().getPrimaryCurrency();
				}
				Record record = new Record(accRegister);
				record.add(getMessages().date(),
						getDateByCompanyType(accRegister.getDate()));
				record.add(getMessages().transactionType(),
						Utility.getTransactionName(accRegister.getType()));
				record.add(getMessages().docNo(), accRegister.getNumber());
				String amount = "";
				if (DecimalUtil.isGreaterThan(accRegister.getAmount(), 0.0))
					amount = getAmountWithCurrency(accRegister.getAmount(),
							currency.getSymbol());
				else
					amount = getAmountWithCurrency(0.00, currency.getSymbol());
				record.add(getMessages().increase(), amount);
				String decreaseAmount = "";
				if (DecimalUtil.isLessThan(accRegister.getAmount(), 0.0))
					decreaseAmount = getAmountWithCurrency(
							-1 * accRegister.getAmount(), currency.getSymbol());
				else
					decreaseAmount = getAmountWithCurrency(0.00,
							currency.getSymbol());
				record.add(getMessages().decrease(), decreaseAmount);
				record.add(getMessages().account(), accRegister.getAccount());
				record.add(getMessages().memo(), accRegister.getMemo());
				record.add(
						getMessages().balance(),
						getAmountWithCurrency(getBalanceValue(accRegister),
								currency.getSymbol()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected boolean filter(AccountRegister e, String name) {
				return false;
			}

			@Override
			protected List<AccountRegister> getLists(Context context) {
				dateRangeChanged(context);
				balance = 0.0;
				totalBalance = 0.0;
				return getAccountRegister();

			}

			@Override
			protected String getShowMessage() {
				return null;
			}
		});
	}

	public double balance = 0.0;
	public double totalBalance = 0.0;

	private double getBalanceValue(AccountRegister accountRegister) {
		/* Here 'd' value might be "positive" or "negative" */
		double d = accountRegister.getAmount();

		if (DecimalUtil.isLessThan(d, 0.0)) {
			d = -1 * d;
			balance = balance - d;
		} else {
			balance = balance + d;
		}
		totalBalance += balance;
		return balance;
	}

	/**
	 * getting the account register.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param accountId
	 * @return
	 */
	public ArrayList<AccountRegister> getAccountRegister() {
		ArrayList<AccountRegister> accountRegisterList = new ArrayList<AccountRegister>();
		Account account = get(ACCOUNT).getValue();
		FinanceDate[] financeDates = CommandUtils.getMinimumAndMaximumDates(
				startDate, endDate, getCompanyId());
		try {
			accountRegisterList = new FinanceTool().getAccountRegister(
					financeDates[0], financeDates[1], account.getID(),
					getCompanyId(), 0, -1);
			if (accountRegisterList != null && !accountRegisterList.isEmpty()) {
				accountRegisterList.remove(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountRegisterList;
	}

	/**
	 * changed show transaction type
	 */
	private void dateRangeChanged(Context context) {
		ClientFinanceDate todaydate = new ClientFinanceDate();
		String selectedOption = get(SHOWTRANSACTION_TYPE).getValue();
		if (selectedOption.equals(getMessages().all())) {
			startDate = new ClientFinanceDate(0);
			endDate = new ClientFinanceDate(0);
		} else if (selectedOption.equals(getMessages().today())) {
			startDate = todaydate;
			endDate = todaydate;
		} else if (selectedOption.equals(getMessages().last30Days())) {
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 1, todaydate.getDay());
			endDate = todaydate;
		} else if (selectedOption.equals(getMessages().last45Days())) {
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 2, todaydate.getDay() + 16);
			endDate = todaydate;
		}
	}
}
