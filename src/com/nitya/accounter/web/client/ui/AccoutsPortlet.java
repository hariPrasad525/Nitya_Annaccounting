package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientStatement;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.banking.AccountRegisterAction;
import com.nitya.accounter.web.client.ui.banking.BankStatementAction;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.UploadStatementDialog;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

/**
 * 
 * @author vimukti15
 * 
 */
public class AccoutsPortlet extends GraphPointsPortlet {

	private CustomLabel accountNameLabel, statementLabel, dateLabel,
			statementamountLabel, balanceLabel, balanceNameLabel;
	private Anchor recocilalationAccountlabel, bankRulesLabel,
			reconcilationReportLabel, importStamentLabel;
	private Button reconcilButton, reconcilationItemsButton;
	private StyledPanel graphPanel;
	private ClientAccount account;

	public AccoutsPortlet(ClientAccount account) {
		super(null, null, null);
		this.getElement().setId("AccoutsPortlet");
		this.account = account;
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}

	@Override
	public void createBody() {

		if (account != null) {
			ClientCurrency currency = getCompany().getCurrency(
					account.getCurrency());
			String currencySymbol = currency == null ? getPrimaryCurrencySymbol()
					: currency.getSymbol();

			// StyledPanel horizontalPanel = new StyledPanel();

			StyledPanel subPanel = new StyledPanel("subPanel");

			accountNameLabel = new CustomLabel(account.getName());
			accountNameLabel.addStyleName("label-banking");
			accountNameLabel.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					accountNameLabel.getElement().getStyle()
							.setCursor(Cursor.POINTER);
					accountNameLabel.getElement().getStyle()
							.setTextDecoration(TextDecoration.UNDERLINE);
				}
			});
			accountNameLabel.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					accountNameLabel.getElement().getStyle()
							.setTextDecoration(TextDecoration.NONE);
				}
			});
			accountNameLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					new AccountRegisterAction().run(account, true);
				}
			});
			reconcilButton = new Button("Reconcile");

			recocilalationAccountlabel = new Anchor("Reconcile Account");
			recocilalationAccountlabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					new BankStatementAction(account).run();

				}
			});

			bankRulesLabel = new Anchor("Bank Rules");
			bankRulesLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub

				}
			});

			reconcilationReportLabel = new Anchor("Reconcilation Report");
			reconcilationReportLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub

				}
			});

			importStamentLabel = new Anchor("Import a Statemnt");
			importStamentLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (Accounter
							.hasPermission(Features.IMPORT_BANK_STATEMENTS)) {
						UploadStatementDialog dialog = new UploadStatementDialog(
								messages.uploadAttachment(), account);
						ViewManager.getInstance().showDialog(dialog);
					} else {
						Accounter.showSubscriptionWarning();
					}
				}
			});

			final PopupPanel buttonpanel = new PopupPanel(true);

			StyledPanel popupbuttonPanel = new StyledPanel("popupbuttonPanel");
			popupbuttonPanel.addStyleName("bank_recouncil-popup");

			popupbuttonPanel.add(recocilalationAccountlabel);
			popupbuttonPanel.add(bankRulesLabel);
			popupbuttonPanel.add(reconcilationReportLabel);
			popupbuttonPanel.add(importStamentLabel);

			buttonpanel.add(popupbuttonPanel);

			reconcilButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					buttonpanel.showRelativeTo(reconcilButton);

				}
			});
			subPanel.add(accountNameLabel);
			subPanel.add(reconcilButton);

			// horizontalPanel.addStyleName("bank_account-header");
			// reconcilButton.addStyleName("bank_account_reconcil");

			statementamountLabel = new CustomLabel("Statement Balance");
			statementLabel = new CustomLabel(
					DataUtils.getAmountAsStringInCurrency(
							account.getStatementBalance(), currencySymbol));
			subPanel.add(statementamountLabel);
			subPanel.add(statementLabel);
			double statementBalance = account.getStatementBalance();

			if (statementBalance != 0.0) {

				dateLabel = new CustomLabel(DateUtills.getDateAsString(account
						.getStatementLastDate()));

				reconcilationItemsButton = new Button("");
				reconcilationItemsButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						new BankStatementAction(account).run();
					}
				});
				// get bank statements
				Accounter.createHomeService().getBankStatements(
						account.getID(),
						new AsyncCallback<PaginationList<ClientStatement>>() {

							@Override
							public void onSuccess(
									PaginationList<ClientStatement> result) {
								int count = 0;
								for (ClientStatement statement : result) {
									count += statement.getStatementList()
											.size();
								}
								reconcilationItemsButton.setText("Reconcile "
										+ String.valueOf(count) + " Items");
							}

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();

							}
						});

				balanceNameLabel = new CustomLabel("Balance");
				balanceLabel = new CustomLabel(
						DataUtils.getAmountAsStringInCurrency(
								account.getTotalBalanceInAccountCurrency(),
								currencySymbol));
				subPanel.add(dateLabel);
				subPanel.add(reconcilationItemsButton);
				subPanel.add(balanceNameLabel);
				subPanel.add(balanceLabel);
				subPanel.addStyleName("bank_accounts_labels");
			}

			final FlowPanel subStyledPanel = new FlowPanel();
			subStyledPanel.add(subPanel);
			AccounterAsyncCallback<ArrayList<Double>> callBack = new AccounterAsyncCallback<ArrayList<Double>>() {

				@Override
				public void onException(AccounterException caught) {
					Accounter.showError(messages
							.failedtogetBankaccountchartvalues());
					completeInitialization();
				}

				@Override
				public void onResultSuccess(final ArrayList<Double> result) {
					graphPanel = new StyledPanel("graphPanel");
					Runnable runnable = new Runnable() {

						@Override
						public void run() {
							GraphChart chart = new GraphChart();
							graphPanel.add(chart
									.createBankingAccountsListChart(result));
						}
					};
					VisualizationUtils.loadVisualizationApi(runnable,
							LineChart.PACKAGE);
					graphPanel.addStyleName("bank_accounts_graph");
					subStyledPanel.add(graphPanel);
					completeInitialization();
				}
			};
			try {
				Accounter.createHomeService().getGraphPointsforAccount(
						GraphChart.BANK_ACCOUNT_CHART_TYPE, account.getID(),
						callBack);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// body.add(horizontalPanel);
			body.add(subStyledPanel);
		}
	}

	@Override
	protected boolean canClose() {
		return false;
	}
}
