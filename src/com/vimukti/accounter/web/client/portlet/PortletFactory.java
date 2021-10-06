package com.vimukti.accounter.web.client.portlet;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BankAccountsPortlet;
import com.vimukti.accounter.web.client.ui.BankingPortlet;
import com.vimukti.accounter.web.client.ui.ExpensesBreakdownPortlet;
import com.vimukti.accounter.web.client.ui.MessagesAndTasksPortlet;
import com.vimukti.accounter.web.client.ui.MoneyComingPortlet;
import com.vimukti.accounter.web.client.ui.MoneyGoingPortlet;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.QuickLinksPortlet;
import com.vimukti.accounter.web.client.ui.RecentTransactionsPortlet;
import com.vimukti.accounter.web.client.ui.WhoIOwePortlet;
import com.vimukti.accounter.web.client.ui.WhoOwesMePortlet;

public class PortletFactory {

	private static PortletFactory factory;

	public static final String BANK_ACCOUNT = "BANK_ACCOUNT";
	public static final String MONEY_COMING = "MONEY_COMING";
	public static final String MONEY_GOING = "MONEY_GOING";
	public static final String EXPENSES_CLAIM = "EXPENSES_CLAIM";
	public static final String WHO_I_OWE = "WHO_I_OWE";
	public static final String WHO_OWES_ME = "WHO_OWES_ME";
	public static final String RECENT_TRANSACTIONS = "RECENT_TRANSACTIONS";
	public static final String MESSAGES_AND_TASKS = "Messages And Tasks";
	public static final String QUICK_LINKS = "QUICK_LINKS";
	public static final String BANKING = "BANKING";
	public static final String ACCOUNTS_BALANCES_PORTLET = "ACCOUNTS_BALANCES_PORTLET";
	public static final String TOP_CUSTOMERS_BY_SALES_PORTLET = "TOP_CUSTOMERS_BY_SALES_PORTLET";
	public static final String TOP_VENDORS_BY_EXPENSES_PORTLET = "TOP_VENDORS_BY_EXPENSES_PORTLET";
	public static final String TOP_ITEMS_BY_SALES_PORTLET = "TOP_ITEMS_BY_SALES_PORTLET";
	public static final String INCOME_EXPENSE_BREAKDOWN_PORTLET = "INCOME_EXPENSE_BREAKDOWN_PORTLET";
	public static final String INCOME_BREAKDOWN_PORTLET = "INCOME_BREAKDOWN_PORTLET";
	public static final String YEAR_OVER_YEAR_INCOME_PORTLET = "YEAR_OVER_YEAR_INCOME_PORTLET";
	public static final String YEAR_OVER_YEAR_EXPENSE_PORTLET = "YEAR_OVER_YEAR_EXPENSE_PORTLET";

	public static PortletFactory get() {
		if (factory == null) {
			factory = new PortletFactory();
		}
		return factory;
	}

	public Portlet createPortlet(ClientPortletConfiguration pc,
			PortletPage portletPage) {
		String name = pc.getName();
		Portlet portlet = null;
		if (name.equals(BANK_ACCOUNT)) {
			portlet = new BankAccountsPortlet(pc);
		} else if (name.equals(MONEY_COMING)) {
			portlet = new MoneyComingPortlet(pc);
		} else if (name.equals(MONEY_GOING)) {
			portlet = new MoneyGoingPortlet(pc);
		} else if (name.equals(EXPENSES_CLAIM)) {
			portlet = new ExpensesBreakdownPortlet(pc);
		} else if (name.equals(WHO_I_OWE)) {
			portlet = new WhoIOwePortlet(pc);
		} else if (name.equals(WHO_OWES_ME)) {
			portlet = new WhoOwesMePortlet(pc);
		} else if (name.equals(RECENT_TRANSACTIONS)) {
			portlet = new RecentTransactionsPortlet(pc);
		} else if (name.equals(QUICK_LINKS)) {
			portlet = new QuickLinksPortlet(pc);
		} else if (name.equals(BANKING)) {
			portlet = new BankingPortlet(pc);
		} else if (name.equals(MESSAGES_AND_TASKS)) {
			portlet = new MessagesAndTasksPortlet(pc);
		} else if (name.equals(ACCOUNTS_BALANCES_PORTLET)) {
			portlet = new AccountBalancesPortlet(pc);
		} else if (name.equals(TOP_CUSTOMERS_BY_SALES_PORTLET)) {
			portlet = new TopPayeesBySalesPortlet(pc,
					TopPayeesBySalesPortlet.CUSTOMER_PORTLET);
		} else if (name.equals(TOP_VENDORS_BY_EXPENSES_PORTLET)) {
			portlet = new TopPayeesBySalesPortlet(pc,
					TopPayeesBySalesPortlet.VENDOR_PORTLET);
		} else if (name.equals(TOP_ITEMS_BY_SALES_PORTLET)) {
			portlet = new TopPayeesBySalesPortlet(pc,
					TopPayeesBySalesPortlet.ITEM_PORTLET);
		} else if (name.equals(INCOME_EXPENSE_BREAKDOWN_PORTLET)) {
			portlet = new IncomeAndExpensesBreakdownPortlet(pc);
		} else if (name.equals(INCOME_BREAKDOWN_PORTLET)) {
			portlet = new IncomeBreakdownPortlet(pc);
		} else if (name.equals(YEAR_OVER_YEAR_INCOME_PORTLET)) {
			portlet = new YearOverYearPortlet(pc,
					YearOverYearPortlet.YEAR_OVER_YEAR_INCOME);
		} else if (name.equals(YEAR_OVER_YEAR_EXPENSE_PORTLET)) {
			portlet = new YearOverYearPortlet(pc,
					YearOverYearPortlet.YEAR_OVER_YEAR_EXPENSE);
		}
		return portlet;
	}

	public Portlet getPortletByName(ClientPortletConfiguration pc, String name) {
		Portlet portlet = null;
		if (name.equals(BANK_ACCOUNT)) {
			portlet = new BankAccountsPortlet(pc);
		} else if (name.equals(MONEY_COMING)) {
			portlet = new MoneyComingPortlet(pc);
		} else if (name.equals(MONEY_GOING)) {
			portlet = new MoneyGoingPortlet(pc);
		} else if (name.equals(EXPENSES_CLAIM)) {
			portlet = new ExpensesBreakdownPortlet(pc);
		} else if (name.equals(WHO_I_OWE)) {
			portlet = new WhoIOwePortlet(pc);
		} else if (name.equals(WHO_OWES_ME)) {
			portlet = new WhoOwesMePortlet(pc);
		} else if (name.equals(RECENT_TRANSACTIONS)) {
			portlet = new RecentTransactionsPortlet(pc);
		} else if (name.equals(QUICK_LINKS)) {
			portlet = new QuickLinksPortlet(pc);
		} else if (name.equals(BANKING)) {
			portlet = new BankingPortlet(pc);
		} else if (name.equals(MESSAGES_AND_TASKS)) {
			portlet = new MessagesAndTasksPortlet(pc);
		} else if (name.equals(ACCOUNTS_BALANCES_PORTLET)) {
			portlet = new AccountBalancesPortlet(pc);
		} else if (name.equals(TOP_CUSTOMERS_BY_SALES_PORTLET)) {
			portlet = new TopPayeesBySalesPortlet(pc,
					TopPayeesBySalesPortlet.CUSTOMER_PORTLET);
		} else if (name.equals(TOP_VENDORS_BY_EXPENSES_PORTLET)) {
			portlet = new TopPayeesBySalesPortlet(pc,
					TopPayeesBySalesPortlet.VENDOR_PORTLET);
		} else if (name.equals(TOP_ITEMS_BY_SALES_PORTLET)) {
			portlet = new TopPayeesBySalesPortlet(pc,
					TopPayeesBySalesPortlet.ITEM_PORTLET);
		} else if (name.equals(INCOME_EXPENSE_BREAKDOWN_PORTLET)) {
			portlet = new IncomeAndExpensesBreakdownPortlet(pc);
		} else if (name.equals(INCOME_BREAKDOWN_PORTLET)) {
			portlet = new IncomeBreakdownPortlet(pc);
		} else if (name.equals(YEAR_OVER_YEAR_INCOME_PORTLET)) {
			portlet = new YearOverYearPortlet(pc,
					YearOverYearPortlet.YEAR_OVER_YEAR_INCOME);
		} else if (name.equals(YEAR_OVER_YEAR_EXPENSE_PORTLET)) {
			portlet = new YearOverYearPortlet(pc,
					YearOverYearPortlet.YEAR_OVER_YEAR_EXPENSE);
		}
		return portlet;
	}

	public String getPortletName(ClientPortletConfiguration pc) {
		AccounterMessages messages = Global.get().messages();
		String portletName = "";
		String name = pc.getName();
		if (name.equals(BANK_ACCOUNT)) {
			portletName = messages.bankAccount();
		} else if (name.equals(MONEY_COMING)) {
			portletName = messages.moneyComingIn();
		} else if (name.equals(MONEY_GOING)) {
			portletName = messages.moneyGoingOut();
		} else if (name.equals(EXPENSES_CLAIM)) {
			portletName = messages.expenses();
		} else if (name.equals(WHO_I_OWE)) {
			portletName = messages.whoIOwe();
		} else if (name.equals(WHO_OWES_ME)) {
			portletName = messages.whoOwesMe();
		} else if (name.equals(RECENT_TRANSACTIONS)) {
			portletName = messages.recentTransactions();
		} else if (name.equals(QUICK_LINKS)) {
			portletName = messages.quickLinks();
		} else if (name.equals(BANKING)) {
			portletName = messages.banking();
		} else if (name.equals(MESSAGES_AND_TASKS)) {
			portletName = messages.messagesAndTasks();
		} else if (name.equals(ACCOUNTS_BALANCES_PORTLET)) {
			portletName = messages.accountBalances();
		} else if (name.equals(TOP_CUSTOMERS_BY_SALES_PORTLET)) {
			portletName = messages
					.topCustomersBySales(Global.get().Customers());
		} else if (name.equals(TOP_VENDORS_BY_EXPENSES_PORTLET)) {
			portletName = messages.topVendorsByExpense(Global.get().Vendors());
		} else if (name.equals(TOP_ITEMS_BY_SALES_PORTLET)) {
			portletName = messages.topItemsBySales();
		} else if (name.equals(INCOME_EXPENSE_BREAKDOWN_PORTLET)) {
			portletName = messages.incomeAndExpenseAccounts();
		} else if (name.equals(INCOME_BREAKDOWN_PORTLET)) {
			portletName = messages.incomes();
		} else if (name.equals(YEAR_OVER_YEAR_INCOME_PORTLET)) {
			portletName = messages.yearOverYearIncome();
		} else if (name.equals(YEAR_OVER_YEAR_EXPENSE_PORTLET)) {
			portletName = messages.yearOverYearExpense();
		}
		return portletName;
	}

	public String getPortletNames(String name) {
		AccounterMessages messages = Global.get().messages();
		if (name.equals(messages.bankAccount())) {
			return BANK_ACCOUNT;
		} else if (name.equals(messages.moneyComingIn())) {
			return MONEY_COMING;
		} else if (name.equals(messages.moneyGoingOut())) {
			return MONEY_GOING;
		} else if (name.equals(messages.expenses())) {
			return EXPENSES_CLAIM;
		} else if (name.equals(messages.whoIOwe())) {
			return WHO_I_OWE;
		} else if (name.equals(messages.whoOwesMe())) {
			return WHO_OWES_ME;
		} else if (name.equals(messages.recentTransactions())) {
			return RECENT_TRANSACTIONS;
		} else if (name.equals(messages.quickLinks())) {
			return QUICK_LINKS;
		} else if (name.equals(messages.banking())) {
			return BANKING;
		} else if (name.equals(messages.messagesAndTasks())) {
			return MESSAGES_AND_TASKS;
		} else if (name.equals(messages.accountBalances())) {
			return ACCOUNTS_BALANCES_PORTLET;
		} else if (name.equals(messages.topCustomersBySales(Global.get()
				.Customers()))) {
			return TOP_CUSTOMERS_BY_SALES_PORTLET;
		} else if (name.equals(messages.topVendorsByExpense(Global.get()
				.Vendors()))) {
			return TOP_VENDORS_BY_EXPENSES_PORTLET;
		} else if (name.equals(messages.topItemsBySales())) {
			return TOP_ITEMS_BY_SALES_PORTLET;
		} else if (name.equals(messages.incomeAndExpenseAccounts())) {
			return INCOME_EXPENSE_BREAKDOWN_PORTLET;
		} else if (name.equals(messages.incomes())) {
			return INCOME_BREAKDOWN_PORTLET;
		} else if (name.equals(messages.yearOverYearIncome())) {
			return YEAR_OVER_YEAR_INCOME_PORTLET;
		} else if (name.equals(messages.yearOverYearExpense())) {
			return YEAR_OVER_YEAR_EXPENSE_PORTLET;
		}
		return null;
	}

	public ClientPortletPageConfiguration getDefaultConfiguration(String page) {
		if (page.equals(PortletPage.DASHBOARD)) {
			return new ClientPortletPageConfiguration(2,
					new String[][] { { BANKING, EXPENSES_CLAIM },
							{ MONEY_COMING, MONEY_GOING } });
		}
		return null;
	}

	public ClientPortletPageConfiguration getPrefferedConfiguration(String page) {
		if (page.equals(PortletPage.DASHBOARD)) {
			if (!Accounter.hasPermission(Features.DASHBOARD_WIDGETS)) {
				return getDefaultConfiguration(page);
			}
			return new ClientPortletPageConfiguration(2, new String[][] {
					{ BANKING, EXPENSES_CLAIM, WHO_I_OWE, RECENT_TRANSACTIONS,
							ACCOUNTS_BALANCES_PORTLET,
							TOP_CUSTOMERS_BY_SALES_PORTLET,
							TOP_ITEMS_BY_SALES_PORTLET,
							YEAR_OVER_YEAR_INCOME_PORTLET },
					{ BANK_ACCOUNT, MONEY_COMING, MONEY_GOING, WHO_OWES_ME,
							QUICK_LINKS, MESSAGES_AND_TASKS,
							TOP_VENDORS_BY_EXPENSES_PORTLET,
							INCOME_EXPENSE_BREAKDOWN_PORTLET,
							INCOME_BREAKDOWN_PORTLET,
							YEAR_OVER_YEAR_EXPENSE_PORTLET } });
		}
		return null;
	}
}
