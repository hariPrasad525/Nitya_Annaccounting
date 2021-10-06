package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.ui.Anchor;
//import com.google.gwt.user.client.ui.HTML;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.nitya.accounter.web.client.Global;
//import com.nitya.accounter.web.client.core.ClientAccount;
//import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
//import com.nitya.accounter.web.client.ui.core.ActionFactory;
//
//public class GettingStartedPortlet extends Portlet {
//	private StyledPanel mainPanel;
//	private HTML minHtml, allHtml;
//	private Label moreLabel;
//
//	Anchor accountReceivable, accountPayable, banking, expences, customer,
//			vendor, inviteUser, createBankAcc, accounts;
//
//	public GettingStartedPortlet(ClientPortletConfiguration configuration) {
//		super(configuration, messages.gettingStartedusingAccounter(), "");
//	}
//
//	@Override
//	public void createBody() {
//		mainPanel = new StyledPanel();
//		// <li> <a href=''><font color='green'>Watch the Getting Started
//		// tour.</font></a>
//		// <li>Set up repeating invoices for those invoices you regularly send
//		// or receive.
//		// <li><a href=''><font color='green'>Create a budget</font></a> for
//		// your organisation so that you can compare with actual expenditure
//		// throughout the year.
//		accountReceivable = getAnchor(messages.accountReceivable());
//		accountPayable = getAnchor(messages.accountPayable());
//		banking = getAnchor(messages.bankingTransactions());
//		expences = getAnchor(messages.expenseClaims());
//		customer = getAnchor(messages.payees(Global.get().customers()));
//		vendor = getAnchor(messages.payees(Global.get().vendors()));
//		inviteUser = getAnchor(messages.inviteOtherUser());
//		createBankAcc = getAnchor(messages.createanyadditionalbankaccounts());
//		accounts = getAnchor(messages.Accounts());
//		// minHtml = new HTML(
//		// "<p>Now you are ready to start using Accounter on a regular basis to record and report on normal business transcations. There is <a href='http://help.accounter.com'><font color='green'>full online help</font></a> and tips on each screen in Accounter if you need it. It's really up to you what you do next.</p><ul><li>Add "
//		// + accountReceivable
//		// + " and "
//		// + accountPayable
//		// + " invoices, "
//		// + banking
//		// + " and "
//		// + expences
//		// +
//		// ".<li>Add to <a href=''><font color='green'>customers</font></a> or <a href=''><font color='green'>vendors</font></a> the people you regularly transact with.</ul>");
//		// StyledPanel vPanel = new StyledPanel();
//		// StyledPanel hPanel1 = new StyledPanel();
//		// StyledPanel hPanel2 = new StyledPanel();
//		// StyledPanel hPanel3 = new StyledPanel();
//		// StyledPanel hPanel4 = new StyledPanel();
//		// StyledPanel hPanel5 = new StyledPanel();
//		allHtml = new HTML(messages.allHTML());
//		// hPanel1.add(new HTML("<li>Add "));
//		// if (FinanceApplication.getUser().canDoInvoiceTransactions()) {
//		// hPanel1.add(accountReceivable);
//		// hPanel1.add(new HTML(" and "));
//		// hPanel1.add(accountPayable);
//		// hPanel1.add(new HTML(" invoices, "));
//		// }
//		// hPanel1.add(banking);
//		// hPanel1.add(new HTML(" and "));
//		// hPanel1.add(expences);
//		// hPanel1.setSpacing(5);
//		//
//		// hPanel2.add(new HTML("<li>Add to "));
//		// hPanel2.add(customer);
//		// hPanel2.add(new HTML(" or "));
//		// hPanel2.add(vendor);
//		// hPanel2.add(new HTML(" the people you regularly transact with."));
//		// hPanel2.setSpacing(5);
//		//
//		// hPanel3.add(new HTML("<li>You can"));
//		// hPanel3.add(inviteUser);
//		// hPanel3
//		// .add(new HTML(
//		// "such as your accountant or financial adviser to access your organisation."));
//		// hPanel3.setSpacing(5);
//		//
//		// hPanel4.add(new HTML("<li>You can"));
//		// hPanel4.add(createBankAcc);
//		// hPanel4.add(new HTML("to use in Accounter."));
//		// hPanel4.setSpacing(5);
//		//
//		// hPanel5.add(new HTML("<li>You can see "));
//		// hPanel5.add(financeCategories);
//		// hPanel5.add(new HTML(
//		// "to check which accounts are effected by your transactions."));
//		// hPanel5.setSpacing(5);
//
//		// moreLabel = new Label("More");
//		// moreLabel.addStyleName("lesslabel");
//		// moreLabel.addClickHandler(new ClickHandler() {
//		//
//		// @Override
//		// public void onClick(ClickEvent event) {
//		// if (moreLabel.getText().equals("Less")) {
//		// moreLabel.setText("More");
//		// if (allHtml.isAttached()) {
//		// mainPanel.remove(allHtml);
//		// }
//		// mainPanel.remove(moreLabel);
//		// mainPanel.add(minHtml);
//		// mainPanel.add(moreLabel);
//		//
//		// } else {
//		// moreLabel.setText("Less");
//		// if (minHtml.isAttached()) {
//		// mainPanel.remove(minHtml);
//		// }
//		// mainPanel.remove(moreLabel);
//		// mainPanel.add(allHtml);
//		// mainPanel.add(moreLabel);
//		// }
//		//
//		// }
//		// });
//		// vPanel.add(allHtml);
//		// if (FinanceApplication.getUser().canSeeInvoiceTransactions())
//		// vPanel.add(hPanel1);
//		// vPanel.add(hPanel2);
//		// if (FinanceApplication.getUser().isCanDoUserManagement())
//		// vPanel.add(hPanel3);
//		// if (FinanceApplication.getUser().canDoBanking())
//		// vPanel.add(hPanel4);
//		// if (FinanceApplication.getUser().canSeeInvoiceTransactions())
//		// vPanel.add(hPanel5);
//		// mainPanel.add(paraHtml);
//		// mainPanel.add(minHtml);
//		mainPanel.add(allHtml);
//		// mainPanel.add(accountReceivable);
//		// mainPanel.add(moreLabel);
//		body.add(mainPanel);
//
//	}
//
//	@Override
//	public void goToClicked() {
//		// TODO Auto-generated method stub
//		this.setVisible(false);
//		// Header.changeHelpBarContent("Show Getting Started");
//	}
//
//	@Override
//	public void helpClicked() {
//
//	}
//
//	public Anchor getAnchor(final String title) {
//		Anchor link = new Anchor(title);
//		// link.getElement().getStyle().setCursor(Cursor.POINTER);
//		link.getElement().getStyle().setColor("green");
//		link.addClickHandler(new ClickHandler() {
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public void onClick(ClickEvent event) {
//
//				if (title.equals(messages.accountReceivable())) {
//					if (Accounter.getUser().canDoInvoiceTransactions())
//						ActionFactory.getNewInvoiceAction().run(null, true);
//				} else if (title.equals(messages.accountPayable())) {
//					if (Accounter.getUser().canDoInvoiceTransactions())
//						ActionFactory.getEnterBillsAction().run(null, true);
//				} else if (title.equals(messages.bankingTransactions()))
//					ActionFactory.getChartOfAccountsAction(
//							ClientAccount.TYPE_BANK).run(null, true);
//				else if (title.equals(messages.expenseClaims())) {
//					if (Accounter.getUser().canDoInvoiceTransactions())
//						ActionFactory.getExpensesAction(null).run(null, true);
//				} else if (title.equals(messages
//						.payees(Global.get().customers())))
//					ActionFactory.getNewCustomerAction().run(null, true);
//				else if (title.equals(messages.payees(Global.get().vendors())))
//					ActionFactory.getNewVendorAction().run(null, true);
//				else if (title.equals(messages.inviteOtherUser())) {
//					if (Accounter.getUser().isCanDoUserManagement())
//						ActionFactory.getInviteUserAction().run(null, true);
//				} else if (title.equals(messages
//						.createanyadditionalbankaccounts())) {
//					if (Accounter.getUser().canDoBanking())
//						ActionFactory.getNewBankAccountAction().run(null, true);
//				} else if (title.equals(messages.Accounts())) {
//					if (Accounter.getUser().canSeeInvoiceTransactions())
//						ActionFactory.getChartOfAccountsAction()
//								.run(null, true);
//				}
//
//			}
//		});
//		return link;
//	}
//
//	@Override
//	public void refreshClicked() {
//		// TODO Auto-generated method stub
//
//	}
//
// }
