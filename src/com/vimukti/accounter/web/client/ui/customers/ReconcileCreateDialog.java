package com.vimukti.accounter.web.client.ui.customers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientStatementRecord;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ReconcileCreateDialog extends BaseDialog {

	private ClientStatementRecord statementRecord;

	public ReconcileCreateDialog(ClientStatementRecord statementRecord) {
		this.statementRecord = statementRecord;
		setText("Create");
		this.getElement().setId("ReconcileCreateDialog");
		createControls();
		// setWidth("500px");
	}

	/**
	 * Creating the controls
	 */
	private void createControls() {
		FlexTable linksTable = new FlexTable();
		final Map<String, Action> map;
		// Checking statementRecord amount is Spent or received amount.
		if (statementRecord.getSpentAmount() > 0) {
			map = getSpentTransactionNames();
		} else {
			map = getReceivedTransactionNames();
		}
		int j = 0, i = 0;
		for (String linkName : map.keySet()) {
			final Anchor anchor = new Anchor(linkName);
			anchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					map.get(anchor.getText()).run();
					removeFromParent();
				}
			});
			linksTable.setWidget(i, j, anchor);
			i++;
		}
		add(linksTable);
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, Action> getReceivedTransactionNames() {
		Map<String, Action> map = new HashMap<String, Action>();
		map.put(messages.invoice(), new NewInvoiceAction());
		map.put(messages.cashSale(), new NewCashSaleAction());
		map.put(messages.payeePrePayment(Global.get().customer()),
				new CustomerPaymentsAction());
		map.put(messages.receivePayment(), new ReceivePaymentAction());
		return map;
	}

	/**
	 * 
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	private Map<String, Action> getSpentTransactionNames() {
		Map<String, Action> map = new HashMap<String, Action>();
		map.put(messages.enterBill(), new EnterBillsAction());
		map.put(messages.cashPurchase(), new NewCashPurchaseAction());
		map.put(messages.payeePrePayment(Global.get().Vendor()),
				new VendorPaymentsAction());
		map.put(messages.cashExpense(), new CashExpenseAction());
		map.put(messages.creditCardExpense(), new CreditCardExpenseAction());
		map.put(messages.writeCheck(), new WriteChecksAction());
		map.put(messages.payBill(), new PayBillsAction());
		return map;
	}

	@Override
	protected boolean onOK() {
		return false;
	}

	@Override
	public void setFocus() {
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
