package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientLocation;
import com.nitya.accounter.web.client.core.ClientRecurringTransaction;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class CreateRecurringTemplateDialog extends
		BaseDialog<ClientRecurringTransaction> {

	public static String ALL = messages.all();
	public static String BILL = messages.bill();
	public static String CASH_EXPENSE = messages.cashExpense();
	public static String CASH_SALES = messages.cashSale();
	public static String CASH_PURCHASE = messages.cashPurchase();
	public static String CREDIT_CARD_EXPENSE = messages.creditCardExpense();
	public static String CUSTOMER_CREDIT_MEMO = messages
			.customerCreditNote(Global.get().Customer());
	public static String DEPOSIT_TRANSFER_FUNDS = messages
			.depositTransferFunds();
	public static String INVOICE = messages.invoice();
	public static String QUOTE = messages.quote();
	public static String VENDOR_CREDIT_MEMO = messages.vendorCreditMemo();
	public static String WRITE_CHECK = messages.writeCheck();

	Map<String, Integer> typesMap = new HashMap<String, Integer>();

	private DynamicForm form;
	private SelectCombo transactionTypeCombo;
	ClientLocation clientLocation;

	public CreateRecurringTemplateDialog() {
		super(messages.selectTransactionType(), "");
		this.getElement().setId("CreateRecurringTemplateDialog");
		createControls();
	}

	private void createControls() {

		initTypesMap();

		form = new DynamicForm("form");
		// form.setWidth("100%");
		transactionTypeCombo = new SelectCombo(messages.transactionType());
		transactionTypeCombo
				.initCombo(new ArrayList<String>(typesMap.keySet()));
		transactionTypeCombo.setRequired(true);

		StyledPanel layout = new StyledPanel("layout");
		form.add(transactionTypeCombo);
		layout.add(form);

		setBodyLayout(layout);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		return result;
	}

	@Override
	protected boolean onOK() {
		int type = typesMap.get(transactionTypeCombo.getSelectedValue());

		ClientTransaction transaction = UIUtils.getTransactionObject(type);
		transaction.setSaveStatus(ClientTransaction.STATUS_TEMPLATE);
		transaction.setType(type);

		ReportsRPC.openTransactionView(transaction);
		return true;
	}

	@Override
	public void setFocus() {
		transactionTypeCombo.setFocus();
	}

	private void initTypesMap() {
		typesMap.put(BILL, ClientTransaction.TYPE_ENTER_BILL);
		typesMap.put(CASH_EXPENSE, ClientTransaction.TYPE_CASH_EXPENSE);
		typesMap.put(CASH_SALES, ClientTransaction.TYPE_CASH_SALES);
		typesMap.put(CASH_PURCHASE, ClientTransaction.TYPE_CASH_PURCHASE);
		typesMap.put(CREDIT_CARD_EXPENSE,
				ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
		typesMap.put(CUSTOMER_CREDIT_MEMO,
				ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
		typesMap.put(DEPOSIT_TRANSFER_FUNDS,
				ClientTransaction.TYPE_TRANSFER_FUND);
		typesMap.put(INVOICE, ClientTransaction.TYPE_INVOICE);
		typesMap.put(QUOTE, ClientTransaction.TYPE_ESTIMATE);
		typesMap.put(VENDOR_CREDIT_MEMO,
				ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
		typesMap.put(WRITE_CHECK, ClientTransaction.TYPE_WRITE_CHECK);
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
