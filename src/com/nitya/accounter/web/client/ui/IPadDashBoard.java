package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.core.ClientPortletPageConfiguration;
import com.nitya.accounter.web.client.portlet.PortletFactory;
import com.nitya.accounter.web.client.portlet.PortletPage;
import com.nitya.accounter.web.client.ui.core.AbstractView;
import com.nitya.accounter.web.client.ui.core.ButtonGroup;
import com.nitya.accounter.web.client.ui.core.IButtonContainer;

public class IPadDashBoard extends AbstractView implements IButtonContainer {

	private List<ClientPortletConfiguration> portletConfigurationList;
	private ClientPortletPageConfiguration defaultConfiguration;
	private StyledPanel displayPage;
	private PortletPage page;

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

	public void createControls() {

		final StyledPanel portLetsList = new StyledPanel("portLetsList");
		displayPage = new StyledPanel("displayPage");

		List<String> portletNames = new ArrayList<String>();

		portletNames.add(messages.bankAccount());
		portletNames.add(messages.moneyComingIn());
		portletNames.add(messages.moneyGoingOut());
		portletNames.add(messages.expenses());
		portletNames.add(messages.whoIOwe());
		portletNames.add(messages.whoOwesMe());
		portletNames.add(messages.recentTransactions());
		portletNames.add(messages.quickLinks());
		portletNames.add(messages.banking());
		portletNames.add(messages.messagesAndTasks());
		portletNames.add(messages.accountBalances());
		portletNames
				.add(messages.topCustomersBySales(Global.get().Customers()));
		portletNames.add(messages.topVendorsByExpense(Global.get().Vendors()));
		portletNames.add(messages.topItemsBySales());
		portletNames.add(messages.incomeAndExpenseAccounts());
		portletNames.add(messages.incomes());
		portletNames.add(messages.yearOverYearIncome());
		portletNames.add(messages.yearOverYearExpense());

		page = new PortletPage(PortletPage.DASHBOARD);

		defaultConfiguration = Accounter.getCompany()
				.getPortletPageConfiguration(page.getName());
		portletConfigurationList = defaultConfiguration
				.getPortletConfigurations();

		for (int i = 0; i < portletConfigurationList.size(); i++) {
			String portletName = PortletFactory.get().getPortletName(
					portletConfigurationList.get(i));

			final Anchor link = new Anchor(portletName);
			link.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					for (int i = 0; i < portLetsList.getWidgetCount(); i++) {
						portLetsList.getWidget(i).removeStyleName("clicked");
					}
					link.addStyleName("clicked");
					addPortlet(link.getText());
				}
			});
			portLetsList.add(link);

		}

		addPortlet(PortletFactory.get().getPortletName(
				portletConfigurationList.get(0)));

		StyledPanel mainPanel = new StyledPanel("ipadDashboard");
		mainPanel.add(portLetsList);
		mainPanel.add(displayPage);

		this.add(mainPanel);

	}

	protected void addPortlet(String name) {
		String portletNames = PortletFactory.get().getPortletNames(name);
		for (ClientPortletConfiguration pc : portletConfigurationList) {

			if (pc.getName().equals(portletNames)) {
				Portlet portletByName = PortletFactory.get().getPortletByName(
						pc, portletNames);
				displayPage.clear();
				displayPage.add(portletByName);
			}
		}

	}

	private void addPortletToPage(ClientPortletConfiguration pc) {
		Portlet portlet = createPortlet(pc);
		displayPage.add(portlet);

	}

	private Portlet createPortlet(ClientPortletConfiguration pc) {
		return PortletFactory.get().createPortlet(pc, null);
	}

	@Override
	public void init() {
		createControls();
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {

	}

	@Override
	public void addButtons(ButtonGroup group) {
		final ImageButton configButton = new ImageButton(Accounter
				.getFinanceImages().ipadSettings(), "settings");
		configButton.addStyleName("settingsButton");
		configButton.getElement().setId("configButton");
		configButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				page.createSettingsDialog().showRelativeTo(configButton);
			}
		});
		group.add(configButton);
	}

}
