package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EstimateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class CreateSalesOrderCommand extends AbstractTransactionCommand {

	public static final String STATUS = "status";
	public static final String CUSTOMER_ORDER_NO = "customerOrderNumber";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(getMessages().salesOrder());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().salesOrder());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(STATUS).setDefaultValue(getMessages().open());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		get(DISCOUNT).setDefaultValue(0.0);

		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().salesOrder());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseSelect(
				Global.get().Customer()), Global.get().Customer(), false, true,
				new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						CreateSalesOrderCommand.this.get(CONTACT)
								.setValue(null);
					}
				}) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});

		list.add(new AmountRequirement(DISCOUNT, getMessages().pleaseEnter(
				getMessages().discount()), getMessages().discount(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackDiscounts()
						&& !getPreferences().isDiscountPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

		});

		list.add(new TransactionItemTableRequirement(ITEMS, getMessages()
				.pleaseEnter(getMessages().itemName()), getMessages().items(),
				false, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.getType() == Item.TYPE_SERVICE) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			protected double getCurrencyFactor() {
				return CreateSalesOrderCommand.this.getCurrencyFactor();
			}

			@Override
			protected Currency getCurrency() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateSalesOrderCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, true));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getMessages().paymentTerm()), getMessages()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contact(), true,
				true, null) {

			@Override
			protected Payee getPayee() {
				return get(CUSTOMER).getValue();
			}
		});
		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getMessages().orderNo()), getMessages().orderNo(), true, true));

		list.add(new NumberRequirement(CUSTOMER_ORDER_NO, getMessages()
				.pleaseEnter(
						Global.get().Customer() + getMessages().orderNumber()),
				Global.get().Customer() + getMessages().orderNumber(), true,
				true));
		list.add(new EstimateRequirement(ESTIMATE, getMessages().pleaseSelect(
				getMessages().quote()), getMessages().quote(), true, true, null) {

			@Override
			protected List<Estimate> getLists(Context context) {
				try {
					return new FinanceTool().getCustomerManager().getEstimates(
							((Customer) get(CUSTOMER).getValue()).getID(),
							context.getCompany().getID());
				} catch (DAOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected boolean filter(Estimate e, String name) {
				return e.getCustomer().getName().contains(name);
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getMessages().dueDate()), getMessages().dueDate(), true, true));

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
		list.add(new StringListRequirement(STATUS, getMessages().pleaseSelect(
				getMessages().status()), getMessages().status(), true, true,
				null) {

			@Override
			protected String getSetMessage() {

				return getMessages().hasSelected(getMessages().status());
			}

			@Override
			protected String getSelectString() {

				return getMessages().pleaseSelect(getMessages().status());
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().open());
				list.add(getMessages().completed());
				list.add(getMessages().cancelled());
				return list;
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(getMessages().status());
			}
		});

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackTax()
						&& !context.getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().includeVATwithAmountenabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().includeVATwithAmountDisabled();
			}
		});

	}

	private ClientEstimate createSalesOrderObjetc(Context context) {
		ClientEstimate newSalesOrder = new ClientEstimate();

		Customer customer = get(CUSTOMER).getValue();
		newSalesOrder.setCustomer(customer.getID());
		newSalesOrder.setType(ClientTransaction.TYPE_ESTIMATE);
		newSalesOrder.setPhone((String) get(PHONE).getValue());
		int statusNumber = 0;
		if (get(STATUS).getValue().equals(getMessages().open())) {
			statusNumber = ClientTransaction.STATUS_OPEN;
		} else if (get(STATUS).getValue().equals(getMessages().completed())) {
			statusNumber = ClientTransaction.STATUS_COMPLETED;
		} else if (get(STATUS).getValue().equals(getMessages().cancelled())) {
			statusNumber = ClientTransaction.STATUS_CANCELLED;
		}

		ClientAddress billTo = get(BILL_TO).getValue();
		// newSalesOrder.setBillingAddress(billTo);
		newSalesOrder.setStatus(statusNumber);
		ClientFinanceDate value = get(DATE).getValue();
		newSalesOrder.setDate(value.getDate());
		newSalesOrder.setNumber((String) get(NUMBER).getValue());

		// newSalesOrder.setCustomerOrderNumber((String)
		// get(ORDER_NO).getValue());

		PaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		if (newPaymentTerms != null)
			newSalesOrder.setPaymentTerm(newPaymentTerms.getID());

		ClientCompanyPreferences preferences = context.getPreferences();

		ClientFinanceDate date = get(DUE_DATE).getValue();
		newSalesOrder.setDate(date.getDate());

		Estimate e = get(ESTIMATE).getValue();

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		if (e != null) {
			ClientConvertUtil c = new ClientConvertUtil();
			ClientEstimate cEstimate = null;
			try {
				cEstimate = c.toClientObject(e, ClientEstimate.class);
			} catch (AccounterException e1) {
				e1.printStackTrace();
			}

			// newSalesOrder.setEstimate(cEstimate.getID());
			addEstimate(cEstimate, items);
		}
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		newSalesOrder.setTransactionItems(items);
		double taxTotal = updateTotals(context, newSalesOrder, true);
		newSalesOrder.setTaxTotal(taxTotal);
		String memo = get(MEMO).getValue();
		newSalesOrder.setMemo(memo);
		return newSalesOrder;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		create(createSalesOrderObjetc(context), context);
		return null;
	}

	private void addEstimate(ClientEstimate cct,
			List<ClientTransactionItem> items) {
		for (ClientTransactionItem cst : cct.getTransactionItems()) {
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (cst.getLineTotal() != 0.0) {
				clientItem.setDescription(cst.getDescription());
				clientItem.setType(cst.getType());
				clientItem.setAccount(cst.getAccount());
				clientItem.setItem(cst.getItem());
				clientItem.setVATfraction(cst.getVATfraction());
				clientItem.setTaxCode(cst.getTaxCode());
				clientItem.setDescription(cst.getDescription());
				clientItem.setQuantity(cst.getQuantity());
				clientItem.setUnitPrice(cst.getUnitPrice());
				clientItem.setDiscount(cst.getDiscount());
				clientItem.setLineTotal(cst.getLineTotal() - cst.getInvoiced());
				clientItem.setTaxable(cst.isTaxable());
				clientItem.setReferringTransactionItem(cst.getID());

				items.add(clientItem);
			}
		}
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		updateTotals(context, createSalesOrderObjetc(context), true);
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Currency getCurrency() {
		return null;
	}

}
