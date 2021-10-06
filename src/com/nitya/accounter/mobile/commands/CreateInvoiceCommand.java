package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Contact;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Customer;
import com.nitya.accounter.core.Item;
import com.nitya.accounter.core.NumberUtils;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.core.PaymentTerms;
import com.nitya.accounter.core.SalesPerson;
import com.nitya.accounter.core.ShippingMethod;
import com.nitya.accounter.core.ShippingTerms;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.AddressRequirement;
import com.nitya.accounter.mobile.requirements.AmountRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.ContactRequirement;
import com.nitya.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.nitya.accounter.mobile.requirements.CustomerRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.EstimatesAndSalesOrderTableRequirement;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.PaymentTermRequirement;
import com.nitya.accounter.mobile.requirements.SalesPersonRequirement;
import com.nitya.accounter.mobile.requirements.ShippingMethodRequirement;
import com.nitya.accounter.mobile.requirements.ShippingTermRequirement;
import com.nitya.accounter.mobile.requirements.TaxCodeRequirement;
import com.nitya.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientInvoice;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

/**
 * 
 * @author SaiPrasad N
 * 
 */

public class CreateInvoiceCommand extends AbstractTransactionCommand {

	private static final String ESTIMATEANDSALESORDER = "estimateAndSalesOrder";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String DUE_DATE = "duedate";
	private static final String ORDER_NO = "orderNo";

	private ClientInvoice invoice;
	List<EstimatesAndSalesOrdersList> estimatesAndSalesOrdersLists = new ArrayList<EstimatesAndSalesOrdersList>();

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getWelcomeMessage() {

		return invoice.getID() == 0 ? "Creating new invoice... "
				: "Updating invoice";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseSelect(
				Global.get().customer()), Global.get().Customer(), false, true,
				new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						CreateInvoiceCommand.this.initList(value);
						get(BILL_TO).setValue(null);
						Set<Address> addresses = value.getAddress();
						for (Address address : addresses) {
							if (address.getType() == Address.TYPE_BILL_TO) {
								try {
									ClientAddress addr = new ClientConvertUtil()
											.toClientObject(address,
													ClientAddress.class);
									get(BILL_TO).setValue(addr);
								} catch (AccounterException e) {
									e.printStackTrace();
								}
								break;
							}
						}

						CreateInvoiceCommand.this.get(CONTACT).setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								CreateInvoiceCommand.this.get(CONTACT)
										.setValue(contact);
								break;
							}
						}
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateInvoiceCommand.this
									.get(CURRENCY_FACTOR)
									.setValue(
											mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				}) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});

		list.add(new EstimatesAndSalesOrderTableRequirement(
				ESTIMATEANDSALESORDER, getMessages().selectTypeOfThis(
						getMessages().quote()), getMessages()
						.quoteAndSalesOrderList()) {
			@Override
			protected List<EstimatesAndSalesOrdersList> getList() {
				return estimatesAndSalesOrdersLists;
			}

		});

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) get(CUSTOMER).getValue();
				return customer.getCurrency();
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
			protected double getCurrencyFactor() {
				return CreateInvoiceCommand.this.getCurrencyFactor();
			}

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				ArrayList<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isISellThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateInvoiceCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			public boolean isDone() {
				List<EstimatesAndSalesOrdersList> list = CreateInvoiceCommand.this
						.get(ESTIMATEANDSALESORDER).getValue();
				return ((estimatesAndSalesOrdersLists == null || estimatesAndSalesOrdersLists
						.isEmpty()) && (list == null || list.isEmpty())) ? super
						.isDone() : true;
			}

			@Override
			protected Currency getCurrency() {
				return ((Customer) CreateInvoiceCommand.this.get(CUSTOMER)
						.getValue()).getCurrency();
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
			public List<PaymentTerms> getLists(Context context) {
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

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getMessages().dueDate()), getMessages().dueDate(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getMessages().orderNo()), getMessages().orderNo(), true, true));

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new SalesPersonRequirement(SALES_PERSON, getMessages()
				.pleaseSelect(getMessages().salesPerson()), getMessages()
				.salesPerson(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect(getMessages().salesPerson());
			}

			@Override
			protected List<SalesPerson> getLists(Context context) {
				List<SalesPerson> salesPersonList = new ArrayList<SalesPerson>();
				Set<SalesPerson> salesPersons = context.getCompany()
						.getSalesPersons();
				for (SalesPerson salesPerson : salesPersons) {
					if (salesPerson.isActive()) {
						salesPersonList.add(salesPerson);
					}
				}
				return salesPersonList;
			}

			@Override
			protected boolean filter(SalesPerson e, String name) {
				return e.getFirstName().equalsIgnoreCase(name);
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isSalesPersonEnabled()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new ShippingTermRequirement(SHIPPING_TERM, getMessages()
				.pleaseSelect(getMessages().shippingTerm()), getMessages()
				.shippingTerm(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect(getMessages().shippingTerm());
			}

			@Override
			protected List<ShippingTerms> getLists(Context context) {
				return new ArrayList<ShippingTerms>(context.getCompany()
						.getShippingTerms());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().shippingTerms());
			}

			@Override
			protected boolean filter(ShippingTerms e, String name) {
				return e.getName().equalsIgnoreCase(name);
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new ShippingMethodRequirement(SHIPPING_METHOD, getMessages()
				.pleaseSelect(getMessages().shippingMethod()), getMessages()
				.shippingMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect(
						getMessages().shippingMethod());
			}

			@Override
			protected List<ShippingMethod> getLists(Context context) {
				return new ArrayList<ShippingMethod>(context.getCompany()
						.getShippingMethods());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().shippingMethodList());
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isDoProductShipMents()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

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
				return e.getName().contains(name);
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
		list.add(new CommandsRequirement("InvoiceCommand") {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (invoice.getID() != 0) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			public String onSelection(String value) {
				return "sendInvoiceToMail " + invoice.getID();
			}

			@Override
			protected boolean canAddToResult() {
				return false;
			}

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().printAndSendEmail());
				return list;
			}
		});
	}

	private ClientEstimate getEstimate(long transactionId, Context context) {
		ClientEstimate cEstimate = null;
		try {
			cEstimate = new FinanceTool().getManager().getObjectById(
					AccounterCoreType.ESTIMATE, transactionId,
					context.getCompany().getID());
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		return cEstimate;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientFinanceDate date = get(DATE).getValue();
		invoice.setDate(date.getDate());

		invoice.setType(Transaction.TYPE_INVOICE);

		String number = get(NUMBER).getValue();
		invoice.setNumber(number);

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<EstimatesAndSalesOrdersList> e = get(ESTIMATEANDSALESORDER)
				.getValue();
		if (items.isEmpty() && e.isEmpty()) {
			return new Result();
		}

		Customer customer = get(CUSTOMER).getValue();
		ClientAddress billTo = get(BILL_TO).getValue();
		// ClientAddress shippingAddress = null;
		// List<Address> serverShiptAddrs = new ArrayList<Address>();
		// List<ClientAddress> addr = get("ship_to").getValue();
		// for (ClientAddress clientAddress : addr) {
		// serverShiptAddrs.add(toServerAddress(clientAddress));
		// }
		// if (billTo != null) {
		// for (ClientAddress clientAddress : addr) {
		// if (clientAddress.getType() == ClientAddress.TYPE_BILL_TO) {
		// addr.remove(clientAddress);
		// }
		// }
		// addr.add(billTo);
		// }
		// if (!addr.isEmpty()) {
		// Set<Address> addresses = new HashSet<Address>();
		// for (ClientAddress clientAddress : addr) {
		// addresses.add(toServerAddress(clientAddress));
		// }
		// customer.setAddress(addresses);
		// // Accounter.createOrUpdate(this, getCustomer());
		//
		// for (ClientAddress clientAddress : addr) {
		// if (clientAddress.getType() == ClientAddress.TYPE_SHIP_TO)
		// shippingAddress = clientAddress;
		// }
		// }
		SalesPerson salesPerson = get(SALES_PERSON).getValue();
		if (salesPerson == null) {
			salesPerson = customer.getSalesPerson();
		}

		if (salesPerson != null) {
			invoice.setSalesPerson(salesPerson.getID());
		}

		invoice.setCustomer(customer.getID());

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		invoice.setDueDate(dueDate.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		invoice.setDeliverydate(deliveryDate.getDate());

		Contact contact = get(CONTACT).getValue();
		invoice.setContact(toClientContact(contact));

		if (billTo != null) {
			invoice.setBillingAddress(billTo);
		}

		// if (shippingAddress != null) {
		// invoice.setShippingAdress(shippingAddress);
		// }

		PaymentTerms paymentTerm = get(PAYMENT_TERMS).getValue();
		invoice.setPaymentTerm(paymentTerm.getID());

		String orderNo = get(ORDER_NO).getValue();
		invoice.setOrderNum(orderNo);

		ShippingMethod method = get(SHIPPING_METHOD).getValue();
		if (method != null) {
			invoice.setShippingMethod(method.getID());
		}

		ShippingTerms shippingTerms = get(SHIPPING_TERM).getValue();
		if (shippingTerms != null) {
			invoice.setShippingTerm(shippingTerms.getID());
		}

		String memo = get(MEMO).getValue();
		invoice.setMemo(memo);
		// Adding selecting estimate or salesOrder to Invoice
		invoice.setCurrencyFactor(1);
		ClientCompanyPreferences preferences = context.getPreferences();

		List<ClientEstimate> estimates = new ArrayList<ClientEstimate>();
		for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : e) {
			if (e != null) {
				ClientEstimate cct = getEstimate(
						estimatesAndSalesOrdersList.getTransactionId(), context);
				estimates.add(cct);
			}
		}
		invoice.setSaveStatus(ClientTransaction.STATUS_APPROVE);
		invoice.setEstimates(estimates);
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		invoice.setTransactionItems(items);
		double taxTotal = updateTotals(context, invoice, true);
		double totalAmount = 0.0;
		double totalNetAmount = 0.0;
		for (ClientEstimate clientEstimate : estimates) {
			if (clientEstimate.getEstimateType() == ClientEstimate.CREDITS) {
				totalAmount -= clientEstimate.getTotal();
				totalNetAmount -= clientEstimate.getNetAmount();
				taxTotal -= clientEstimate.getTaxTotal();
			} else {
				totalAmount += clientEstimate.getTotal();
				totalNetAmount += clientEstimate.getNetAmount();
				taxTotal += clientEstimate.getTaxTotal();
			}
		}
		invoice.setDiscountDate(invoice.getTransactionDate());
		invoice.setNetAmount(invoice.getNetAmount() + totalNetAmount);
		invoice.setTotal(invoice.getTotal() + totalAmount);
		invoice.setTaxTotal(taxTotal);
		invoice.setCurrency(customer.getCurrency().getID());
		if (customer.getPriceLevel() != null) {
			invoice.setPriceLevel(customer.getPriceLevel().getID());
		}
		invoice.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		create(invoice, context);
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return invoice.getID() == 0 ? getMessages().readyToCreate(
				getMessages().invoice()) : getMessages().readyToUpdate(
				getMessages().invoice());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(DELIVERY_DATE).setDefaultValue(new ClientFinanceDate());
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_INVOICE, context.getCompany()));
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);
		get(DISCOUNT).setDefaultValue(0.0);
	}

	@Override
	public String getSuccessMessage() {
		return invoice.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().invoice()) : getMessages().updateSuccessfully(
				getMessages().invoice());
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		// TODO
		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		List<ClientTransactionItem> allrecords = get(ITEMS).getValue();
		List<EstimatesAndSalesOrdersList> e = get(ESTIMATEANDSALESORDER)
				.getValue();
		if (allrecords.isEmpty() && e.isEmpty()) {
			addFirstMessage(context,
					getMessages().pleaseSelect(getMessages().transactionItem()));
		}
		ClientCompanyPreferences preferences = context.getPreferences();
		for (ClientTransactionItem item : allrecords) {
			if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
				TAXCode taxCode = get(TAXCODE).getValue();
				if (taxCode != null) {
					item.setTaxCode(taxCode.getID());
				}
			}
			item.setAmountIncludeTAX(isVatInclusive);
		}

		double[] result = getTransactionTotal(isVatInclusive, allrecords, true);

		for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : e) {
			if (e != null) {
				ClientEstimate clientEstimate = getEstimate(
						estimatesAndSalesOrdersList.getTransactionId(), context);
				if (clientEstimate.getEstimateType() == ClientEstimate.CREDITS) {
					result[0] -= clientEstimate.getNetAmount();
					result[1] -= clientEstimate.getTaxTotal();
				} else {
					result[0] += clientEstimate.getNetAmount();
					result[1] += clientEstimate.getTaxTotal();
				}
			}
		}
		if (context.getPreferences().isTrackTax()) {
			makeResult.add(getMessages().totalTax() + result[1]);
		}
		Customer customer = get(CUSTOMER).getValue();
		Currency currency = customer.getCurrency();
		String formalName = getPreferences().getPrimaryCurrency()
				.getFormalName();
		if (!currency.getFormalName().equalsIgnoreCase(formalName))
			makeResult.add(getMessages().total()
					+ "("
					+ formalName
					+ ")"
					+ ": "
					+ (result[0] * getCurrencyFactor() + result[1]
							* getCurrencyFactor()));
		makeResult.add(getMessages().total() + "(" + currency.getFormalName()
				+ ")" + ": " + (result[0] + result[1]));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "invoices";
			}
			invoice = getTransaction(string, AccounterCoreType.INVOICE, context);

			if (invoice == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "invoices" + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			invoice = new ClientInvoice();
		}
		setTransaction(invoice);
		return null;
	}

	/**
	 * update the requirements
	 * 
	 * @param context
	 */

	private void setValues(Context context) {
		get(DATE).setValue(invoice.getDate());
		get(NUMBER).setValue(invoice.getNumber());
		ArrayList<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		if (invoice.getTransactionItems() != null
				&& !invoice.getTransactionItems().isEmpty()) {
			for (ClientTransactionItem item : invoice.getTransactionItems()) {
				// We should exclude those which come from quote/charge/credit
				if (item.getReferringTransactionItem() == 0) {
					list.add(item);
				}
			}
		}
		get(CURRENCY_FACTOR).setValue(invoice.getCurrencyFactor());
		get(ITEMS).setValue(list);
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(invoice.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(DUE_DATE).setValue(new ClientFinanceDate(invoice.getDueDate()));
		get(CONTACT).setValue(toServerContact(invoice.getContact()));
		get(BILL_TO).setValue(invoice.getBillingAddress());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(invoice.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							invoice.getTransactionItems(), context));
		}
		if (preferences.isTrackDiscounts()
				&& !preferences.isDiscountPerDetailLine()) {
			get(DISCOUNT).setValue(
					getDiscountFromTransactionItems(invoice
							.getTransactionItems()));
		}
		get(ORDER_NO).setValue(invoice.getOrderNum());
		get(MEMO).setValue(invoice.getMemo());
		/* get(CURRENCY_FACTOR).setValue(invoice.getCurrencyFactor()); */

		List<EstimatesAndSalesOrdersList> e = getEstimatesSalesOrderList();
		get(ESTIMATEANDSALESORDER).setValue(e);
		initList((Customer) get(CUSTOMER).getValue());
		get(IS_VAT_INCLUSIVE).setValue(isAmountIncludeTAX(invoice));
	}

	/**
	 * 
	 * @return
	 */

	private List<EstimatesAndSalesOrdersList> getEstimatesSalesOrderList() {
		List<EstimatesAndSalesOrdersList> list = new ArrayList<EstimatesAndSalesOrdersList>();
		List<ClientEstimate> estimates = invoice.getEstimates();
		if (estimates == null) {
			return list;
		}
		for (ClientEstimate clientEstimate : estimates) {
			EstimatesAndSalesOrdersList el = new EstimatesAndSalesOrdersList();
			el.setTransactionId(clientEstimate.getID());
			el.setType(clientEstimate.getType());
			el.setTransactionNumber(clientEstimate.getNumber());
			el.setTotal(clientEstimate.getTotal());
			el.setDate(clientEstimate.getDate());
			ClientCustomer clientObjectById = (ClientCustomer) CommandUtils
					.getClientObjectById(clientEstimate.getCustomer(),
							AccounterCoreType.CUSTOMER, getCompanyId());
			el.setCustomerName(clientObjectById != null ? clientObjectById
					.getName() : "");
			el.setEstimateType(clientEstimate.getEstimateType());
			list.add(el);
		}

		return list;
	}

	@Override
	protected Currency getCurrency() {
		return ((Customer) CreateInvoiceCommand.this.get(CUSTOMER).getValue())
				.getCurrency();
	}

	protected void initList(Customer value) {
		estimatesAndSalesOrdersLists = invoice.getCustomer() == value.getID() ? getEstimatesSalesOrderList()
				: new ArrayList<EstimatesAndSalesOrdersList>();
		try {
			estimatesAndSalesOrdersLists.addAll(new FinanceTool()
					.getCustomerManager().getEstimatesAndSalesOrdersList(
							value.getID(), value.getCompany().getID()));

		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
}