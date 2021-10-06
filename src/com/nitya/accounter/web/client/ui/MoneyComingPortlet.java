package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.banking.AccountRegisterAction;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.InvoicesAction;
import com.nitya.accounter.web.client.ui.customers.NewInvoiceAction;

public class MoneyComingPortlet extends GraphPointsPortlet {

	public double draftInvoiceAmount = 0.00;
	public double overDueInvoiceAmount = 0.00;
	public ClientAccount debitors;

	public Label draftLabel;
	public Label overDueLabel;
	public Label draftAmtLabel;
	public Label overDueAmtLabel;

	public MoneyComingPortlet(ClientPortletConfiguration pc) {
		super(pc, messages.moneyComingIn(), messages.goToAccountReceivable(),
				"60%");
		this.getElement().setId("MoneyComingPortlet");
	}

	@Override
	public void helpClicked() {

	}

	@Override
	public void goToClicked() {
		new AccountRegisterAction().run(debitors, true);
	}

	@Override
	public void createBody() {
		updateDebitorsAccount();

		StyledPanel hPanel = new StyledPanel("hPanel");
		FlexTable fTable = new FlexTable();
		// hPanel.setWidth("100%");

		hPanel.addHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				// TODO Auto-generated method stub

			}
		}, ResizeEvent.getType());
		Button addInvoiceBtn = new Button(messages.addInvoice());
		addInvoiceBtn.addStyleName("addButtonPortlet");
		addInvoiceBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new NewInvoiceAction().run(null, true);
			}
		});

		draftLabel = getLabel(messages.invoicesDue());
		overDueLabel = getLabel(messages.overDueInvoices());

		updateAmounts();

		draftAmtLabel = getAmountLabel(DataUtils.amountAsStringWithCurrency(
				draftInvoiceAmount, getCompany().getPrimaryCurrency()));
		overDueAmtLabel = getAmountLabel(DataUtils.amountAsStringWithCurrency(
				overDueInvoiceAmount, getCompany().getPrimaryCurrency()));
		overDueAmtLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);

		fTable.setWidget(0, 0, draftLabel);
		fTable.setWidget(0, 1, overDueLabel);
		fTable.setWidget(1, 0, draftAmtLabel);
		fTable.setWidget(1, 1, overDueAmtLabel);
		fTable.addStyleName("fTablePortlet");

		if (Accounter.getUser().canDoInvoiceTransactions()) {
			hPanel.add(addInvoiceBtn);

		}

		hPanel.add(fTable);

		body.add(hPanel);
		final StyledPanel chartPanel = new StyledPanel("chartPanel");
		AccounterAsyncCallback<ArrayList<Double>> callBack = new AccounterAsyncCallback<ArrayList<Double>>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(messages
						.failedtogetAccountReceivablechartvalues());
				completeInitialization();
			}

			@Override
			public void onResultSuccess(final ArrayList<Double> result) {
				if (result != null && result.size() > 0) {
					overDueInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					overDueAmtLabel.setText(DataUtils
							.amountAsStringWithCurrency(overDueInvoiceAmount,
									getCompany().getPrimaryCurrency()));
				}
				if (result != null && result.size() > 0) {
					draftInvoiceAmount = result.get(result.size() - 1);
					result.remove(result.size() - 1);
					draftAmtLabel.setText(DataUtils.amountAsStringWithCurrency(
							draftInvoiceAmount, getCompany()
									.getPrimaryCurrency()));
				}

				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						GraphChart chart = new GraphChart();
						chartPanel.add(chart
								.createAccountReceivableChart(result));
						completeInitialization();
					}
				};
				VisualizationUtils.loadVisualizationApi(runnable,
						ColumnChart.PACKAGE);

				// GraphChart chart = new GraphChart(
				// GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE, UIUtils
				// .getMaxValue(result), 400, 150, result);
				// body.add(chart);
				// chart.update();
			}

		};
		body.add(chartPanel);
		Accounter.createHomeService().getGraphPointsforAccount(
				GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE, 0, callBack);

	}

	private void updateDebitorsAccount() {
		ClientCurrency primaryCurrency = getCompany().getPrimaryCurrency();
		debitors = getCompany().getAccount(
				primaryCurrency.getAccountsReceivable());
	}

	Label getLabel(final String title) {
		final Label label = new Label(title);
		label.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				label.getElement().getStyle().setCursor(Cursor.POINTER);
				label.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		label.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				label.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		label.addClickHandler(new ClickHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(ClickEvent event) {

				label.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
				String invoicesType = null;
				if (!title.equals(messages.invoicesDue())) {
					invoicesType = messages.overDue();
				}
				InvoicesAction invoicesAction = new InvoicesAction(invoicesType);
				ViewManager.getInstance().viewDataHistory.put(
						invoicesAction.getHistoryToken(), null);
				invoicesAction.run(null, true);
			}
		});
		return label;
	}

	Label getAmountLabel(String title) {
		Label label = new Label(title);
		label.addStyleName("amountLabelPortlet");
		return label;
	}

	private void updateAmounts() {

	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}

}
