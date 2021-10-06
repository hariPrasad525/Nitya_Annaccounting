package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;

public class BankingPortlet extends GraphPointsPortlet {

	List<ClientAccount> bankAccounts = new ArrayList<ClientAccount>();

	public BankingPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.banking(), messages.gotoBanking(), "75%");
		this.getElement().addClassName("bank-account-portlet");
		// setHeight("270px");
		this.getElement().setId("BankingPortlet");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void goToClicked() {
		new ChartOfAccountsAction(ClientAccount.TYPE_BANK).run(null, true);
	}

	@Override
	public void createBody() {
		if (getCompany() != null) {
			bankAccounts = getCompany().getActiveBankAccounts(
					ClientAccount.TYPE_BANK);
		}
		Button addAccount = new Button(messages.addBankAccount());
		addAccount.addStyleName("add account_portlet");
		if (Accounter.getUser().canDoBanking()) {
			body.add(addAccount);
		}

		if (bankAccounts == null || bankAccounts.size() == 0) {
			// for (int i = 0; i < 4; i++) {
			addAccount.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					new NewAccountAction(ClientAccount.TYPE_BANK).run(null,
							true);
				}
			});
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					GraphChart chart = new GraphChart();
					body.add(chart.createBankingChart(null));
					completeInitialization();
				}
			};
			VisualizationUtils
					.loadVisualizationApi(runnable, LineChart.PACKAGE);
			// GraphChart chart = new GraphChart(
			// GraphChart.BANK_ACCOUNT_CHART_TYPE);
			// body.add(chart);
			// chart.update();
			// }
		} else {
			// final ScrollPanel panel = new ScrollPanel();
			for (final ClientAccount account : bankAccounts) {
				StyledPanel hPanel = new StyledPanel("hPanel");
				final Label accountLabel = new Label(account.getName());
				accountLabel.addStyleName("label-banking");
				accountLabel.addMouseOverHandler(new MouseOverHandler() {

					@Override
					public void onMouseOver(MouseOverEvent event) {
						accountLabel.getElement().getStyle()
								.setCursor(Cursor.POINTER);
						accountLabel.getElement().getStyle()
								.setTextDecoration(TextDecoration.UNDERLINE);
					}
				});
				accountLabel.addMouseOutHandler(new MouseOutHandler() {

					@Override
					public void onMouseOut(MouseOutEvent event) {
						accountLabel.getElement().getStyle()
								.setTextDecoration(TextDecoration.NONE);
					}
				});
				accountLabel.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						new AccountRegisterAction().run(account, true);
					}
				});
				ClientCurrency currency = getCompany().getCurrency(
						account.getCurrency());
				final String currencySymbol = currency == null ? getPrimaryCurrencySymbol()
						: currency.getSymbol();
				final Label amountLabel = new Label(
						DataUtils.getAmountAsStringInCurrency(
								account.getTotalBalanceInAccountCurrency(),
								currencySymbol));
				// amountLabel.setStyleName("tool-box");
				amountLabel.addStyleName("label-banking");
				// amountLabel.getElement().getStyle().setMarginLeft(295,
				// Unit.PX);
				hPanel.add(accountLabel);
				hPanel.add(amountLabel);
				// hPanel.setCellHorizontalAlignment(amountLabel,
				// HasHorizontalAlignment.ALIGN_RIGHT);
				// hPanel.setWidth("100%");
				hPanel.addStyleName("dashboard_label");
				body.add(hPanel);
				AccounterAsyncCallback<ArrayList<Double>> callBack = new AccounterAsyncCallback<ArrayList<Double>>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedtogetBankaccountchartvalues());
						completeInitialization();
					}

					@Override
					public void onResultSuccess(final ArrayList<Double> result) {
						Runnable runnable = new Runnable() {

							@Override
							public void run() {
								// if (result.size() != 0) {
								// ClientAccount account = FinanceApplication
								// .getCompany().getAccountByNumber(
								// result.get(
								// result.size() - 1)
								// .longValue());
								// result
								// .remove(result
								// .get(result.size() - 1));
								// }
								for (int i = 0; i < body.getWidgetCount(); i++) {
									if (body.getWidget(i) instanceof StyledPanel) {
										StyledPanel hPanel = (StyledPanel) body
												.getWidget(i);
										if (hPanel.getWidget(0) instanceof Label) {
											if (((Label) hPanel.getWidget(0))
													.getText().equals(
															account.getName())) {
												GraphChart chart = new GraphChart();
												body.insert(
														chart.createBankingChart(result),
														++i);
											}
										}
									}
								}
							}
						};
						VisualizationUtils.loadVisualizationApi(runnable,
								LineChart.PACKAGE);
						if (!result.isEmpty()
								&& result.get(result.size() - 1) != null) {
							amountLabel.setText(DataUtils
									.getAmountAsStringInCurrency(
											result.get(result.size() - 1),
											currencySymbol));
						}
						completeInitialization();
						// GraphChart chart = new GraphChart(
						// GraphChart.BANK_ACCOUNT_CHART_TYPE, UIUtils
						// .getMaxValue(result), 400, 150, result);
						// body.add(chart);
						// chart.update();
					}
				};
				try {
					Accounter.createHomeService().getGraphPointsforAccount(
							GraphChart.BANK_ACCOUNT_CHART_TYPE,
							account.getID(), callBack);
				} catch (Exception e) {
					System.err.println(e);
				}

			}
			// body.add(panel);
			addAccount.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					new NewAccountAction(ClientAccount.TYPE_BANK).run(null,
							true);
				}
			});
		}
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}

}
