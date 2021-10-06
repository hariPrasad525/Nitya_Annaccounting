package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.grids.CompanyFinancialWidgetGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerWidgetGrid;

public class WidgetCreator {
	Portlet welcomePortlet;
	private Portlet bankingSummaryPortlet;
	private CustomerWidgetGrid customerWidgetGrid;
	private CompanyFinancialWidgetGrid grid;

	private boolean continueRequest = true;

	public boolean isContinueRequest() {
		return continueRequest;
	}

	public void setContinueRequest(boolean continueRequest) {
		this.continueRequest = continueRequest;
	}

	public Portlet getWidgetByName(String widgetName) {
		// if (widgetName.equals(messages.WELCOME())) {
		// return getWelcomePortlet();
		// } else if (widgetName.equals(messages.BANKINGSUMMARY()))
		// {
		// return getBankingSummaryWidget();
		// }
		/*
		 * else if (widgetName.equals("PROFIT_AND_LOSS")) { return
		 * getProfitAndLossWidget(); } else if
		 * (widgetName.equals("CREDIT_OVERVIEW")) { return
		 * getCreditOverviewWidget(); } else if
		 * (widgetName.equals("DEBIT_OVERVIEW")) { return
		 * getDebitOverviewWidget(); } else if
		 * (widgetName.equals("LATEST_QUOTE")) { return getLatestQuoteWidget();
		 * } else if (widgetName.equals("EXPENSES")) { return
		 * getExpensesWidget(); } else if (widgetName.equals("NEW_CUSTOMER")) {
		 * return getNewCustomerWidget(); } else if
		 * (widgetName.equals("ITEM_PURCHASE")) { return
		 * getPurchaseItemWidget(); } else if (widgetName.equals("SALES_ITEM"))
		 * { return getSalesItemWidget(); } else if
		 * (widgetName.equals("PAYMENT_RECEIVED")) { return
		 * getPaymentRecievedWidget(); } else if
		 * (widgetName.equals("CASH_SALES")) { return getCashSaleWidget(); }
		 * else if (widgetName.equals("CREDIT_AND_REFUNDS")) { return
		 * getCreditAndRefundWidget(); } else if
		 * (widgetName.equals("NEW_VENDOR")) { return getNewVendorWidget(); }
		 * else if (widgetName.equals("BILL_PAID")) { return
		 * getBillsPaidWidget(); } else if (widgetName.equals("CASH_PURCHASE"))
		 * { return getCashPurchaseWidget(); } else if
		 * (widgetName.equals("CHECK_ISSUED")) { return getCheckIssuedWidget();
		 * } else if (widgetName.equals("DEPOSITE")) { return
		 * getDepositeWidget(); } else if (widgetName.equals("FUND_TRANSFERED"))
		 * { return getFundTransferedWidget(); } else if
		 * (widgetName.equals("CREDIT_CARD_CHARGES")) { return
		 * getCreditCardChargesWidget(); }
		 */

		return null;
	}

	public Portlet getWelcomePortlet() {

		// welcomePortlet.setShowCloseButton(false);
		// welcomePortlet.setCanDrag(false);
		// welcomePortlet.setName("Bank Current Accounts");s

		// StyledPanel layout = new StyledPanel();
		grid = new CompanyFinancialWidgetGrid();
		grid.init();
//		grid.setHeight("180px");
		grid.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		grid.getElement().getStyle().setBorderWidth(0, Unit.PX);
		grid.addLoadingImagePanel();

		// grid.setRecords(initCompanyFinancialWidgetData());

		// welcomePortlet = new Portlet() {
		// @Override
		// protected void onAttach() {
		// this.titleName = "Key Financial Indicators";
		// grid.setHeight("180px");
		// grid.getElement().getStyle().setBorderWidth(0, Unit.PX);
		// grid.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		// super.onAttach();
		// }
		//
		// @Override
		// public void linkClicked() {
		//
		// }
		//
		// @Override
		// public void refreshClicked() {
		// grid.clear();
		// grid.addLoadingImagePanel();
		// // grid.addEmptyMessage("please wait...");
		// reloadKeyIndicators(grid);
		// }
		//
		// @Override
		// public void createBody() {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public String getGoToText() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// public void goToClicked() {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void helpClicked() {
		// // TODO Auto-generated method stub
		//
		// }
		// };

		// welcomePortlet.setTitle("Key Financial Indicators"reloadKeyIndicators);
		// layout.setBackgroundColor("white");
		welcomePortlet.add(grid);
//		welcomePortlet.setHeight("184px");
		reloadKeyIndicators(grid);
		return welcomePortlet;

	}

	public void reloadKeyIndicators(final CompanyFinancialWidgetGrid widgetGrid) {

		Accounter.createGETService().getKeyFinancialIndicators(
				new AccounterAsyncCallback<KeyFinancialIndicators>() {

					@Override
					public void onResultSuccess(KeyFinancialIndicators result) {
						// if (continueRequest) {
						List<KeyFinancialIndicator> financialIndicators = new ArrayList<KeyFinancialIndicator>();
						for (String key : result.getIndicators().keySet()) {
							KeyFinancialIndicator financialIndicator = new KeyFinancialIndicator();
							financialIndicator.setIndicators(result
									.getIndicators().get(key));
							financialIndicator.setKeyIndicator(key);
							financialIndicators.add(financialIndicator);
						}
						widgetGrid.clear();
						widgetGrid.setRecords(financialIndicators);
						welcomePortlet.refresh.setStyleName("refresh");
						WidgetCreator.this.setCompanyFinancialWidgetHeight(205);
						// }

					}

					@Override
					public void onException(AccounterException caught) {
						// Accounter.showInformation("Request Failed");
					}
				});
	}

	/*
	 * public Portlet getCreditOverviewWidget() { Portlet creditOverviewPortlet
	 * = new Portlet(); creditOverviewPortlet.setTitle("Who Do I Owe");
	 * creditOverviewPortlet.setName("CREDIT_OVERVIEW");
	 * 
	 * ListGrid creditOverviewGrid = new ListGrid();
	 * creditOverviewGrid.setHeight("100%");
	 * creditOverviewGrid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("bill",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("status",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * creditOverviewGrid.setFields(billField, statusField, owedField); //
	 * creditOverviewGrid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * // getBillsOwedData(creditOverviewGrid);
	 * 
	 * creditOverviewPortlet.addItem(creditOverviewGrid);
	 * 
	 * return creditOverviewPortlet; }
	 * 
	 * private void getBillsOwedData(final ListGrid creditOverviewGrid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService.getBillsOwed(new
	 * AccounterAsyncCallback<ArrayList<ClientEnterBill>>() { public void
	 * onException(AccounterException caught) { Window.alert("Get Failed:" +
	 * caught); }
	 * 
	 * public void onSuccess(ArrayList<ClientEnterBill> result) {
	 * fillBillOwedGrid(result, creditOverviewGrid); creditOverviewGrid.show();
	 * }
	 * 
	 * }); }
	 * 
	 * private void fillBillOwedGrid(List<ClientEnterBill> result, final
	 * ListGrid creditOverviewGrid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientEnterBill bill; for (int recordIndex
	 * = 0; recordIndex < records.length; ++recordIndex) { bill =
	 * result.get(recordIndex); records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("bill_id", bill.getID());
	 * records[recordIndex].setAttribute("bill", FinanceApplication
	 * .getCompany().getVendor(bill.getVendor()).getName());
	 * records[recordIndex].setAttribute("status", bill.getStatus());
	 * records[recordIndex].setAttribute("owed", bill.getBalanceDue());
	 * 
	 * }
	 * 
	 * creditOverviewGrid.setRecords(records); creditOverviewGrid.fetchData(); }
	 */
	public Portlet getBankingSummaryWidget() {

		// bankingSummaryPortlet.setName("BANKING_SUMMARY");
		customerWidgetGrid = new CustomerWidgetGrid();
		customerWidgetGrid.isEnable = false;
		customerWidgetGrid.init();
		// grid.addLoadingImagePanel();
//		customerWidgetGrid.setHeight("250px");
		customerWidgetGrid.getElement().getStyle().setBorderWidth(0, Unit.PX);
		customerWidgetGrid.addLoadingImagePanel();

		// bankingSummaryPortlet = new Portlet() {
		// @Override
		// protected void onAttach() {
		// this.titleName = "Debtors";
		// customerWidgetGrid.getElement().getStyle()
		// .setBorderWidth(0, Unit.PX);
		// super.onAttach();
		// }
		//
		// @Override
		// public void linkClicked() {
		//
		// }
		//
		// @Override
		// public void refreshClicked() {
		// customerWidgetGrid.clear();
		//
		// // customerWidgetGrid.addEmptyMessage("please wait...");
		// customerWidgetGrid.addLoadingImagePanel();
		//
		// reloadDebitors(customerWidgetGrid);
		//
		// }
		// };
		reloadDebitors(customerWidgetGrid);

		bankingSummaryPortlet.add(customerWidgetGrid);

		return bankingSummaryPortlet;
	}

	public void reloadDebitors(final CustomerWidgetGrid grid) {

		if (Accounter.getStartDate() != null)
			Accounter.createReportService().getDebitors(
					new ClientFinanceDate(), new ClientFinanceDate(),
					new AccounterAsyncCallback<ArrayList<DummyDebitor>>() {

						@Override
						public void onResultSuccess(
								ArrayList<DummyDebitor> result) {
							// if (continueRequest) {
							result.remove(result.size() - 1);
							Collections.sort(result,
									new Comparator<DummyDebitor>() {

										@Override
										public int compare(DummyDebitor o1,
												DummyDebitor o2) {
											return o1
													.getDebitorName()
													.compareTo(
															o2.getDebitorName());
										}
									});
							sortDebitors(result, grid);
							bankingSummaryPortlet.refresh
									.setStyleName("refresh");
							// }
						}

						@Override
						public void onException(AccounterException caught) {
							grid.clear();
							grid.removeLoadingImage();

						}
					});
		else {
			// reloadDebitors(grid);
		}
	}

	public void sortDebitors(List<DummyDebitor> result, CustomerWidgetGrid grid) {
		grid.clear();
		grid.removeLoadingImage();
		grid.initParentAndChildIcons(Accounter.getFinanceImages()
				.customerIcon(), Accounter.getFinanceImages().customerIcon());
		grid.addParentWithChilds(Global.get().messages().DebtorsTotal(), result);
	}

	public void setCustomerWidgetHeight(int height) {
//		if (height >= 180) {
//			bankingSummaryPortlet.setHeight(height - 110 + "px");
//			customerWidgetGrid.setHeight(height - 110 + "px");
//		} else {
//			bankingSummaryPortlet.setHeight(80 + "px");
//			customerWidgetGrid.setHeight(80 + "px");
//		}
	}

	public void setCompanyFinancialWidgetHeight(int height) {
//		if (height > 50)
//			welcomePortlet.setHeight(height - 42 + "px");
//		grid.setHeight("100%");
	}
	/*
	 * private void getBankSummaryData(final ListGrid bankingSummaryGrid) {
	 * 
	 * // List<ClientAccount> accounts = Utility //
	 * .getBankingAccountSummary(FinanceApplication.getCompany()); final
	 * List<ClientAccount> accounts = new ArrayList<ClientAccount>(); for
	 * (ClientAccount account : FinanceApplication.getCompany().getAccounts()) {
	 * if (account.getType() == ClientAccount.TYPE_BANK &&
	 * (account.getBankAccountType() ==
	 * ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING || account.getBankAccountType()
	 * == ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET || account
	 * .getBankAccountType() == ClientAccount.BANK_ACCCOUNT_TYPE_SAVING)) {
	 * accounts.add(account); } }
	 * 
	 * // FinanceApplication.createGETService().getAccounts( // new
	 * AccounterAsyncCallback<ArrayList<ClientAccount>>() { // // public void
	 * onException(AccounterException caught) { // UIUtils //
	 * .logError("Failed to get the Accounts..", // caught); // // } // //
	 * public void onSuccess(ArrayList<ClientAccount> result) { // if (result ==
	 * null) { // onFailure(null); // return; // } // // for (ClientAccount
	 * account : result) { // if (account.getType() == ClientAccount.TYPE_BANK
	 * // && (account.getBankAccountType() ==
	 * ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING // ||
	 * account.getBankAccountType() ==
	 * ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET || account //
	 * .getBankAccountType() == ClientAccount.BANK_ACCCOUNT_TYPE_SAVING)) { //
	 * accounts.add(account); // } // } // // } // // }); fillGrid(accounts,
	 * bankingSummaryGrid); bankingSummaryGrid.show(); }
	 * 
	 * private void fillGrid(List<ClientAccount> result, final ListGrid
	 * bankingSummaryGrid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientAccount account; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) { account =
	 * result.get(recordIndex); records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("account_id", account.getID());
	 * records[recordIndex].setAttribute("account", account.getName());
	 * records[recordIndex].setAttribute("balance", account .getTotalBalance());
	 * }
	 * 
	 * bankingSummaryGrid.setRecords(records); bankingSummaryGrid.fetchData(); }
	 * 
	 * public Portlet getProfitAndLossWidget() { Portlet profitAndLossPortlet =
	 * new Portlet(); profitAndLossPortlet.setTitle("Profit And Loss");
	 * profitAndLossPortlet.setName("PROFIT_AND_LOSS"); ListGrid
	 * profitAndLossGrid = new ListGrid(); profitAndLossGrid.setHeight("100%");
	 * profitAndLossGrid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("customer",
	 * "<Font size= \"2\" color= \"Blue\">Customer</font>", 75); ListGridField
	 * statusField = new ListGridField("status",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * profitAndLossGrid.setFields(billField, statusField, owedField); //
	 * profitAndLossGrid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * // getProfitAndLossData(profitAndLossGrid);
	 * 
	 * profitAndLossPortlet.addItem(profitAndLossGrid);
	 * 
	 * return profitAndLossPortlet; }
	 * 
	 * private void getProfitAndLossData(final ListGrid profitAndLossGrid) {
	 * 
	 * final IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService.getBillsOwed(new
	 * AccounterAsyncCallback<ArrayList<ClientEnterBill>>() { public void
	 * onException(AccounterException caught) { Window.alert("Get Failed:" +
	 * caught); }
	 * 
	 * public void onSuccess(ArrayList<ClientEnterBill> result) {
	 * fillProfitAndLossGrid(result, profitAndLossGrid);
	 * profitAndLossGrid.show(); }
	 * 
	 * }); }
	 * 
	 * protected void fillProfitAndLossGrid(List<ClientEnterBill> result, final
	 * ListGrid profitAndLossGrid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientEnterBill enterBill; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) { enterBill
	 * = result.get(recordIndex); records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("invoice_id", enterBill.getID());
	 * records[recordIndex].setAttribute("customer", FinanceApplication
	 * .getCompany().getVendor(enterBill.getVendor()).getName());
	 * 
	 * records[recordIndex].setAttribute("status", enterBill.getStatus());
	 * records[recordIndex] .setAttribute("owed", enterBill.getBalanceDue());
	 * 
	 * }
	 * 
	 * profitAndLossGrid.setRecords(records); profitAndLossGrid.fetchData(); }
	 * 
	 * public Portlet getDebitOverviewWidget() { Portlet debitOverviewPortlet =
	 * new Portlet(); debitOverviewPortlet.setTitle("Debit Overview");
	 * debitOverviewPortlet.setName("DEBIT_OVERVIEW"); ListGrid
	 * debitOverviewGrid = new ListGrid(); debitOverviewGrid.setHeight("100%");
	 * debitOverviewGrid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("customer",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("dueDate",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("dueAmount",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * debitOverviewGrid.setFields(billField, statusField, owedField); //
	 * debitOverviewGrid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * // getDebitOverviewData(debitOverviewGrid);
	 * 
	 * debitOverviewPortlet.addItem(debitOverviewGrid);
	 * 
	 * return debitOverviewPortlet; }
	 * 
	 * private void getDebitOverviewData(final ListGrid debitOverviewGrid) {
	 * final IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getOverDueInvoices(new
	 * AccounterAsyncCallback<ArrayList<OverDueInvoicesList>>() { public void
	 * onException(AccounterException caught) { Window.alert("Get Failed:" +
	 * caught); }
	 * 
	 * public void onSuccess(ArrayList<OverDueInvoicesList> result) {
	 * fillDebitOverviewGrid(result, debitOverviewGrid);
	 * debitOverviewGrid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * private void fillDebitOverviewGrid(List<OverDueInvoicesList> result,
	 * final ListGrid debitOverviewGrid) { if (result != null) {
	 * ListGridRecord[] records = new ListGridRecord[result.size()];
	 * OverDueInvoicesList invoice; for (int recordIndex = 0; recordIndex <
	 * records.length; ++recordIndex) { invoice = result.get(recordIndex);
	 * records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("invoice_id", invoice
	 * .getTransactionId()); records[recordIndex].setAttribute("customer",
	 * invoice .getCustomerName()); records[recordIndex].setAttribute("dueDate",
	 * invoice .getDueDate()); records[recordIndex].setAttribute("dueAmount",
	 * invoice .getBalanceDue()); } debitOverviewGrid.setRecords(records);
	 * debitOverviewGrid.fetchData(); }
	 * 
	 * }
	 * 
	 * public Portlet getLatestQuoteWidget() { Portlet latestQuotePortlet = new
	 * Portlet(); latestQuotePortlet.setTitle("Latest Quote");
	 * latestQuotePortlet.setName("LATEST_QUOTE"); ListGrid latestQuoteGrid =
	 * new ListGrid(); latestQuoteGrid.setHeight("100%");
	 * latestQuoteGrid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("customer",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("status",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * latestQuoteGrid.setFields(billField, statusField, owedField);
	 * latestQuoteGrid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * // getLatestQuoteData(latestQuoteGrid);
	 * 
	 * latestQuotePortlet.addItem(latestQuoteGrid);
	 * 
	 * return latestQuotePortlet; }
	 * 
	 * private void getLatestQuoteData(final ListGrid latestQuoteGrid) {
	 * 
	 * final IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService.getLatestQuotes(new
	 * AccounterAsyncCallback<ArrayList<ClientEstimate>>() { public void
	 * onException(AccounterException caught) { Window.alert("Get Failed:" +
	 * caught); }
	 * 
	 * public void onSuccess(ArrayList<ClientEstimate> result) {
	 * fillLatestQuoteGrid(result, latestQuoteGrid); latestQuoteGrid.show(); }
	 * 
	 * }); }
	 * 
	 * private void fillLatestQuoteGrid(List<ClientEstimate> result, final
	 * ListGrid latestQuoteGrid) {
	 * 
	 * ListGridRecord[] records = new ListGridRecord[result.size()];
	 * ClientEstimate estimate; for (int recordIndex = 0; recordIndex <
	 * records.length; ++recordIndex) { estimate = result.get(recordIndex);
	 * records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("estimate_id", estimate.getID());
	 * records[recordIndex].setAttribute("customer", estimate .getCustomer());
	 * records[recordIndex].setAttribute("status", estimate.getStatus());
	 * records[recordIndex].setAttribute("owed", estimate.getTotal());
	 * 
	 * }
	 * 
	 * latestQuoteGrid.setRecords(records); latestQuoteGrid.fetchData(); }
	 * 
	 * public Portlet getExpensesWidget() { Portlet expensesPortlet = new
	 * Portlet(); expensesPortlet.setTitle("Expenses");
	 * expensesPortlet.setName("EXPENSES"); ListGrid expensesGrid = new
	 * ListGrid(); expensesGrid.setHeight("100%");
	 * expensesGrid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("bill",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("status",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * expensesGrid.setFields(billField, statusField, owedField);
	 * expensesGrid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getExpensesData();
	 * 
	 * expensesPortlet.addItem(expensesGrid);
	 * 
	 * return expensesPortlet; }
	 * 
	 * private void getExpensesData() {
	 * 
	 * // final IAccounterHomeViewServiceAsync getService = //
	 * (IAccounterHomeViewServiceAsync) GWT //
	 * .create(IAccounterHomeViewService.class); // ((ServiceDefTarget) //
	 * getService).setServiceEntryPoint(FinanceApplication //
	 * .HOME_SERVICE_ENTRY_POINT); // // getService.get(new //
	 * AccounterAsyncCallback<ArrayList<Invoice>>() { // public void
	 * onFailure(Throwable caught) { // Window.alert("Get Failed:"+caught); // }
	 * // // public void onSuccess(ArrayList<Invoice> result) { //
	 * fillExpensesGrid(result); // expensesGrid.show(); // } // // }); }
	 * 
	 * // protected void fillExpensesGrid(List<Invoice> result) { //
	 * ListGridRecord[] records = new ListGridRecord[result.size()]; // Estimate
	 * estimate; // for (int recordIndex = 0; recordIndex < records.length;
	 * ++recordIndex) { // estimate = result.get(recordIndex); //
	 * records[recordIndex] = new ListGridRecord(); //
	 * records[recordIndex].setAttribute("estimate_id", estimate.getID() //
	 * .toString()); // records[recordIndex].setAttribute("customer", estimate
	 * // .getCustomer().getName()); //
	 * records[recordIndex].setAttribute("status", estimate.getStatus()); //
	 * records[recordIndex].setAttribute("Value", estimate.getTotal()); // // }
	 * // // latestQuoteGrid.setRecords(records); //
	 * latestQuoteGrid.fetchData(); // }
	 */
	// public Portlet getNewCustomerWidget() {
	// Portlet newCustomerPortlet = new Portlet();
	// newCustomerPortlet.setTitle("New Customer");
	// newCustomerPortlet.setName("NEW_CUSTOMER");
	// ListGrid newCustomerGrid = new ListGrid();
	// newCustomerGrid.setHeight("100%");
	// newCustomerGrid.setSelectionType(SelectionStyle.SINGLE);
	//
	// ListGridField customerField = new ListGridField("customer",
	// "<Font size= \"2\" color= \"Blue\">Customer</font>");
	// ListGridField dateField = new ListGridField("date",
	// "<Font size= \"2\" color= \"Blue\">Date Of Creation</font>");
	// // ListGridField owedField = new ListGridField("owed",
	// // "<Font size= \"2\" color= \"Blue\">Owed</font>", 75);
	//
	// newCustomerGrid.setFields(customerField, dateField);
	// newCustomerGrid.setAutoFitData(Autofit.HORIZONTAL);
	//
	// getNewCustomerData(newCustomerGrid);
	// newCustomerPortlet.addItem(newCustomerGrid);
	//
	// return newCustomerPortlet;
	// }
	/*
	 * private void getNewCustomerData(final ListGrid grid) {
	 * 
	 * final IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestCustomers(new
	 * AccounterAsyncCallback<ArrayList<ClientCustomer>>() {
	 * 
	 * public void onException(AccounterException caught) { //
	 * 
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientCustomer> result) {
	 * fillNewCustomerGrid(result, grid); grid.show(); } });
	 * 
	 * }
	 * 
	 * protected void fillNewCustomerGrid(List<ClientCustomer> result, final
	 * ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientCustomer customer; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) { customer
	 * = result.get(recordIndex); records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("customer_id", customer.getID());
	 * records[recordIndex].setAttribute("customer", customer.getName());
	 * records[recordIndex].setAttribute("date", customer.getDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); // grid.fetchData(); }
	 * 
	 * public Portlet getPurchaseItemWidget() { Portlet purchaseItemPortlet =
	 * new Portlet(); purchaseItemPortlet.setTitle("Item Purchased");
	 * purchaseItemPortlet.setName("ITEM_PURCHASE"); ListGrid grid = new
	 * ListGrid(); grid.setHeight("100%");
	 * grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("item",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("date",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); // ListGridField
	 * owedField = new ListGridField("owed", //
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getPurchaseItemData(grid);
	 * 
	 * purchaseItemPortlet.addItem(grid);
	 * 
	 * return purchaseItemPortlet; }
	 * 
	 * private void getPurchaseItemData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestPurchaseItems(new
	 * AccounterAsyncCallback<ArrayList<ClientItem>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientItem> result) {
	 * fillPurchaseItemGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillPurchaseItemGrid(List<ClientItem> result, final
	 * ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientItem item; for (int recordIndex = 0;
	 * recordIndex < records.length; ++recordIndex) { item =
	 * result.get(recordIndex); records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("item_id", item.getID());
	 * records[recordIndex].setAttribute("item", item.getName()); //
	 * records[recordIndex].setAttribute("date", item.getDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getSalesItemWidget() { Portlet salesItemPortlet = new
	 * Portlet(); salesItemPortlet.setTitle("Sales Item");
	 * salesItemPortlet.setName("SALES_ITEM"); ListGrid grid = new ListGrid();
	 * grid.setHeight("100%"); grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("item",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("date",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); // ListGridField
	 * owedField = new ListGridField("owed", //
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getSalesItemData(grid);
	 * 
	 * salesItemPortlet.addItem(grid);
	 * 
	 * return salesItemPortlet; }
	 * 
	 * private void getSalesItemData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService.getSalesItems(new
	 * AccounterAsyncCallback<ArrayList<ClientItem>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientItem> result) {
	 * fillSalesItemGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillSalesItemGrid(List<ClientItem> result, final ListGrid
	 * grid) { ListGridRecord[] records = new ListGridRecord[result.size()];
	 * ClientItem item; for (int recordIndex = 0; recordIndex < records.length;
	 * ++recordIndex) { item = result.get(recordIndex); records[recordIndex] =
	 * new ListGridRecord(); records[recordIndex].setAttribute("item_id",
	 * item.getID()); records[recordIndex].setAttribute("item", item.getName());
	 * // records[recordIndex].setAttribute("date", item.getCreatedDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getPaymentRecievedWidget() { Portlet
	 * paymentReceivedPortlet = new Portlet();
	 * paymentReceivedPortlet.setTitle("Payment Recieved");
	 * paymentReceivedPortlet.setName("PAYMENT_RECEIVED"); ListGrid grid = new
	 * ListGrid(); grid.setHeight("100%");
	 * grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("date",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); // ListGridField
	 * statusField = new ListGridField("status", //
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); // ListGridField
	 * owedField = new ListGridField("owed", //
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField); grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getPaymentRecievedData(grid);
	 * 
	 * paymentReceivedPortlet.addItem(grid);
	 * 
	 * return paymentReceivedPortlet; }
	 * 
	 * private void getPaymentRecievedData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestReceivePayments(new
	 * AccounterAsyncCallback<ArrayList<ClientReceivePayment>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientReceivePayment> result) {
	 * fillPaymentReceivedGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillPaymentReceivedGrid(List<ClientReceivePayment> result,
	 * final ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientReceivePayment receivePayment; for
	 * (int recordIndex = 0; recordIndex < records.length; ++recordIndex) {
	 * receivePayment = result.get(recordIndex); records[recordIndex] = new
	 * ListGridRecord(); records[recordIndex].setAttribute("payment_id",
	 * receivePayment .getID()); //
	 * records[recordIndex].setAttribute("customer", //
	 * receivePayment.getName()); // records[recordIndex].setAttribute("date",
	 * receivePayment // .getCreatedDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getCashSaleWidget() { Portlet cashSalePortlet = new
	 * Portlet(); cashSalePortlet.setTitle("Cash Sale");
	 * cashSalePortlet.setName("CASH_SALES"); ListGrid grid = new ListGrid();
	 * grid.setHeight("100%"); grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("date",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); // ListGridField
	 * statusField = new ListGridField("status", //
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); // ListGridField
	 * owedField = new ListGridField("owed", //
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField); grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getCashSaleData(grid);
	 * 
	 * cashSalePortlet.addItem(grid);
	 * 
	 * return cashSalePortlet; }
	 * 
	 * private void getCashSaleData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestCashSales(new
	 * AccounterAsyncCallback<ArrayList<ClientCashSales>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientCashSales> result) {
	 * fillCashSalesGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillCashSalesGrid(List<ClientCashSales> result, final
	 * ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientCashSales cashSales; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) { cashSales
	 * = result.get(recordIndex); records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("cashSale_id", cashSales.getID()); //
	 * records[recordIndex].setAttribute("customer", // cashSales.getName());
	 * records[recordIndex].setAttribute("date", cashSales.getDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getCreditAndRefundWidget() { Portlet
	 * creditAndRefundPortlet = new Portlet();
	 * creditAndRefundPortlet.setTitle("Credit And Refund");
	 * creditAndRefundPortlet.setName("CREDIT_AND_REFUNDS"); ListGrid grid = new
	 * ListGrid(); grid.setHeight("100%");
	 * grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("date",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); // ListGridField
	 * statusField = new ListGridField("status", //
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); // ListGridField
	 * owedField = new ListGridField("owed", //
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField); grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getCreditAndRefundData(grid);
	 * 
	 * creditAndRefundPortlet.addItem(grid);
	 * 
	 * return creditAndRefundPortlet; }
	 * 
	 * private void getCreditAndRefundData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestCustomerRefunds(new
	 * AccounterAsyncCallback<ArrayList<ClientCustomerRefund>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientCustomerRefund> result) {
	 * fillCreditAndRefundGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillCreditAndRefundGrid(List<ClientCustomerRefund> result,
	 * final ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientCustomerRefund customerRefund; for
	 * (int recordIndex = 0; recordIndex < records.length; ++recordIndex) {
	 * customerRefund = result.get(recordIndex); records[recordIndex] = new
	 * ListGridRecord(); records[recordIndex].setAttribute("refund_id",
	 * customerRefund .getID()); //
	 * records[recordIndex].setAttribute("customer", //
	 * customerRefund.getName()); // records[recordIndex].setAttribute("date",
	 * customerRefund // .getCreatedDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getNewVendorWidget() { Portlet newVendorPortlet = new
	 * Portlet(); newVendorPortlet.setTitle("New Vendor");
	 * newVendorPortlet.setName("NEW_VENDOR"); ListGrid grid = new ListGrid();
	 * grid.setHeight("100%"); grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("vendor",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("status",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField, owedField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getNewVendorData(grid);
	 * 
	 * newVendorPortlet.addItem(grid);
	 * 
	 * return newVendorPortlet; }
	 * 
	 * private void getNewVendorData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService.getLatestVendors(new
	 * AccounterAsyncCallback<ArrayList<ClientVendor>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientVendor> result) {
	 * fillNewVendorGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillNewVendorGrid(List<ClientVendor> result, final
	 * ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientVendor vendor; for (int recordIndex
	 * = 0; recordIndex < records.length; ++recordIndex) { vendor =
	 * result.get(recordIndex); records[recordIndex] = new ListGridRecord();
	 * records[recordIndex].setAttribute("vendor_id", vendor.getID());
	 * records[recordIndex].setAttribute("vendor", vendor.getName()); //
	 * records[recordIndex] // .setAttribute("date", vendor.getCreatedDate());
	 * // records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getBillsPaidWidget() { Portlet billsPaidPortlet = new
	 * Portlet(); billsPaidPortlet.setTitle("Bills Paid");
	 * billsPaidPortlet.setName("BILL_PAID"); ListGrid grid = new ListGrid();
	 * grid.setHeight("100%"); grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("customer",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("status",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField, owedField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getBillsPaidData(grid); billsPaidPortlet.addItem(grid);
	 * 
	 * return billsPaidPortlet; }
	 * 
	 * private void getBillsPaidData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestVendorPayments(new
	 * AccounterAsyncCallback<ArrayList<PaymentsList>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<PaymentsList> result) {
	 * fillBillsPaidGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillBillsPaidGrid(List<PaymentsList> result, final
	 * ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; PaymentsList paymentsList; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) {
	 * paymentsList = result.get(recordIndex); records[recordIndex] = new
	 * ListGridRecord(); records[recordIndex].setAttribute("payment_id",
	 * paymentsList .getTransactionId().toString());
	 * records[recordIndex].setAttribute("customer", paymentsList .getName());
	 * // records[recordIndex] // .setAttribute("date",
	 * paymentsList.getCreatedDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getCashPurchaseWidget() { Portlet cashPurchasePortlet =
	 * new Portlet(); cashPurchasePortlet.setTitle("Cash Purchase");
	 * cashPurchasePortlet.setName("CASH_PURCHASE"); ListGrid grid = new
	 * ListGrid(); grid.setHeight("100%");
	 * grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("bill",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("date",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField, owedField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getCashPurchaseData(grid);
	 * 
	 * cashPurchasePortlet.addItem(grid);
	 * 
	 * return cashPurchasePortlet; }
	 * 
	 * private void getCashPurchaseData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestCashPurchases(new
	 * AccounterAsyncCallback<ArrayList<ClientCashPurchase>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientCashPurchase> result) {
	 * fillCashPurchaseGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillCashPurchaseGrid(List<ClientCashPurchase> result,
	 * final ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientCashPurchase cashPurchase; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) {
	 * cashPurchase = result.get(recordIndex); records[recordIndex] = new
	 * ListGridRecord(); records[recordIndex].setAttribute("customer_id",
	 * cashPurchase .getID()); // records[recordIndex].setAttribute("customer",
	 * // cashPurchase.getName()); // records[recordIndex].setAttribute("date",
	 * cashPurchase // .getCreatedDate()); customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getCheckIssuedWidget() { Portlet checkIssuedPortlet = new
	 * Portlet(); checkIssuedPortlet.setTitle("Check Issued");
	 * checkIssuedPortlet.setName("CHECK_ISSUED"); ListGrid grid = new
	 * ListGrid(); grid.setHeight("100%");
	 * grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("bill",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("date",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField, owedField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getCheckIssuedData(grid);
	 * 
	 * checkIssuedPortlet.addItem(grid);
	 * 
	 * return checkIssuedPortlet; }
	 * 
	 * private void getCheckIssuedData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService.getLatestChecks(new
	 * AccounterAsyncCallback<ArrayList<ClientWriteCheck>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientWriteCheck> result) {
	 * fillCheckIssuedGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillCheckIssuedGrid(List<ClientWriteCheck> result, final
	 * ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientWriteCheck writeCheck; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) {
	 * writeCheck = result.get(recordIndex); records[recordIndex] = new
	 * ListGridRecord(); records[recordIndex].setAttribute("check_id",
	 * writeCheck.getID()); // records[recordIndex].setAttribute("customer", //
	 * writeCheck.getName()); // records[recordIndex].setAttribute("date",
	 * writeCheck // .getCreatedDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getDepositeWidget() { Portlet depositePortlet = new
	 * Portlet(); depositePortlet.setTitle("Deposite");
	 * depositePortlet.setName("DEPOSITE"); ListGrid grid = new ListGrid();
	 * grid.setHeight("100%"); grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("bill",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("status",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField, owedField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * // getDepositeData(grid);
	 * 
	 * depositePortlet.addItem(grid);
	 * 
	 * return depositePortlet; }
	 * 
	 * private void getDepositeData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestDeposits(new
	 * AccounterAsyncCallback<ArrayList<ClientMakeDeposit>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientMakeDeposit> result) {
	 * fillDepositeGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillDepositeGrid(List<ClientMakeDeposit> result, final
	 * ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientMakeDeposit makeDeposit; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) {
	 * makeDeposit = result.get(recordIndex); records[recordIndex] = new
	 * ListGridRecord(); records[recordIndex].setAttribute("deposite_id",
	 * makeDeposit .getID()); // records[recordIndex].setAttribute("customer",
	 * // makeDeposit.getName()); // records[recordIndex].setAttribute("date",
	 * makeDeposit // .getCreatedDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getFundTransferedWidget() { Portlet fundTransferedPortlet
	 * = new Portlet(); fundTransferedPortlet.setTitle("Fund Transfer");
	 * fundTransferedPortlet.setName("FUND_TRANSFERED"); ListGrid grid = new
	 * ListGrid(); grid.setHeight("100%");
	 * grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("bill",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("date",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField, owedField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * getFundTransferedData(grid);
	 * 
	 * fundTransferedPortlet.addItem(grid);
	 * 
	 * return fundTransferedPortlet; }
	 * 
	 * private void getFundTransferedData(final ListGrid grid) { final
	 * IAccounterHomeViewServiceAsync getService =
	 * (IAccounterHomeViewServiceAsync) GWT
	 * .create(IAccounterHomeViewService.class); ((ServiceDefTarget) getService)
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT);
	 * 
	 * getService .getLatestFundsTransfer(new
	 * AccounterAsyncCallback<ArrayList<ClientTransferFund>>() {
	 * 
	 * public void onException(AccounterException caught) { // Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onSuccess(ArrayList<ClientTransferFund> result) {
	 * fillFundTransferGrid(result, grid); grid.show(); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * protected void fillFundTransferGrid(List<ClientTransferFund> result,
	 * final ListGrid grid) { ListGridRecord[] records = new
	 * ListGridRecord[result.size()]; ClientTransferFund transferFund; for (int
	 * recordIndex = 0; recordIndex < records.length; ++recordIndex) {
	 * transferFund = result.get(recordIndex); records[recordIndex] = new
	 * ListGridRecord(); records[recordIndex].setAttribute("transfer_id",
	 * transferFund .getID()); // records[recordIndex].setAttribute("customer",
	 * // transferFund.getName()); // records[recordIndex].setAttribute("date",
	 * transferFund // .getCreatedDate()); //
	 * records[recordIndex].setAttribute("Value", customer.getE);
	 * 
	 * }
	 * 
	 * grid.setRecords(records); }
	 * 
	 * public Portlet getCreditCardChargesWidget() { Portlet
	 * creditCardChargesPortlet = new Portlet();
	 * creditCardChargesPortlet.setTitle("Credit Card Charges");
	 * creditCardChargesPortlet.setName("CREDIT_CARD_CHARGES"); ListGrid grid =
	 * new ListGrid(); grid.setHeight("100%");
	 * grid.setSelectionType(SelectionStyle.SINGLE);
	 * 
	 * ListGridField billField = new ListGridField("bill",
	 * "<Font size= \"2\" color= \"Blue\">Bill</font>", 75); ListGridField
	 * statusField = new ListGridField("status",
	 * "<Font size= \"2\" color= \"Blue\">Status</font>"); ListGridField
	 * owedField = new ListGridField("owed",
	 * "<Font size= \"2\" color= \"Blue\">Owed</font>");
	 * 
	 * grid.setFields(billField, statusField, owedField);
	 * grid.setAutoFitData(Autofit.HORIZONTAL);
	 * 
	 * // getCreditCardChargesData(grid);
	 * 
	 * creditCardChargesPortlet.addItem(grid);
	 * 
	 * return creditCardChargesPortlet; }
	 * 
	 * // private void getCreditCardChargesData(final ListGrid grid) { // final
	 * IAccounterHomeViewServiceAsync getService = //
	 * (IAccounterHomeViewServiceAsync) GWT //
	 * .create(IAccounterHomeViewService.class); // ((ServiceDefTarget)
	 * getService) //
	 * .setServiceEntryPoint(FinanceApplication.HOME_SERVICE_ENTRY_POINT); // //
	 * getService.getLAtestC // // }
	 */
}
