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
import com.nitya.accounter.core.ShippingMethod;
import com.nitya.accounter.core.ShippingTerms;
import com.nitya.accounter.core.TAXCode;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.AddressRequirement;
import com.nitya.accounter.mobile.requirements.AmountRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.ChangeListner;
import com.nitya.accounter.mobile.requirements.ContactRequirement;
import com.nitya.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.nitya.accounter.mobile.requirements.CustomerRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.PaymentTermRequirement;
import com.nitya.accounter.mobile.requirements.PhoneRequirement;
import com.nitya.accounter.mobile.requirements.ShippingMethodRequirement;
import com.nitya.accounter.mobile.requirements.ShippingTermRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.requirements.TaxCodeRequirement;
import com.nitya.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author SaiPrasad N
 * 
 */
public class CreateQuoteCommand extends AbstractTransactionCommand {

	private static final String PHONE = "phone";
	private static final String DELIVERY_DATE = "deliveryDate";
	private static final String EXPIRATION_DATE = "expirationDate";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String STATUS = "status";
	private static final String CUSTOMER_ORDER_NO = "customerOrderNo";

	private ClientEstimate estimate;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CustomerRequirement(CUSTOMER, getMessages()
				.pleaseEnterNameOrNumber(Global.get().Customer()),
				getMessages().payeeNumber(Global.get().Customer()), false,
				true, new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
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

						get(PHONE).setValue(value.getPhoneNo());

						get(CONTACT).setValue(null);
						for (Contact contact : value.getContacts()) {
							if (contact.isPrimary()) {
								get(CONTACT).setValue(contact);
								break;
							}
						}

						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateQuoteCommand.this
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

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), CURRENCY_FACTOR) {
			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateQuoteCommand.this.get(
						CUSTOMER).getValue();
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
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isISellThisItem()) {
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
				return CreateQuoteCommand.this.getCurrencyFactor();
			}

			@Override
			protected Currency getCurrency() {
				return ((Customer) CreateQuoteCommand.this.get(CUSTOMER)
						.getValue()).getCurrency();
			}

			@Override
			protected double getDiscount() {
				Double value2 = CreateQuoteCommand.this.get(DISCOUNT)
						.getValue();
				return value2;
			}

		});

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(CUSTOMER_ORDER_NO, getMessages()
				.pleaseEnter(getMessages().number()), getMessages().number(),
				true, false));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseEnterName(getMessages().paymentTerm()), getMessages()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES
						|| estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contacts(), true,
				true, null) {

			@Override
			protected Payee getPayee() {
				return get(CUSTOMER).getValue();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES
						|| estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
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
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES
						|| estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phone(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new StringListRequirement(STATUS, getMessages().pleaseSelect(
				getMessages().status()), getMessages().status(), true, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES
						|| estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().status());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseEnter(getMessages().status());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getStatusTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new DateRequirement(EXPIRATION_DATE, getMessages()
				.pleaseEnter(getMessages().expirationDate()), getMessages()
				.expirationDate(), true, false) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
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
				if (getPreferences().isDoProductShipMents()
						&& estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
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
				if (getPreferences().isDoProductShipMents()
						&& estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseEnterName(
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

	protected List<String> getStatusTypes() {
		ArrayList<String> statuses = new ArrayList<String>();
		statuses.add(getMessages().open());
		if (estimate.getEstimateType() != ClientEstimate.SALES_ORDER) {
			statuses.add(getMessages().accepted());
			statuses.add(getMessages().closed());
			if (estimate == null
					|| estimate.getStatus() == ClientEstimate.STATUS_REJECTED
					|| estimate.getSaveStatus() != ClientTransaction.STATUS_DRAFT) {
				statuses.add(getMessages().rejected());
			}
		} else {
			statuses.add(getMessages().completed());
			statuses.add(getMessages().cancelled());
		}

		return statuses;
	}

	private int getSaveStatus(String status) {
		if (status.equals(getMessages().open())) {
			return ClientEstimate.STATUS_OPEN;
		} else if (status.equals(getMessages().accepted())) {
			return ClientEstimate.STATUS_ACCECPTED;
		} else if (status.equals(getMessages().closed())) {
			return ClientEstimate.STATUS_CLOSE;
		} else if (status.equals(getMessages().rejected())) {
			return ClientEstimate.STATUS_REJECTED;
		} else if (status.equals(getMessages().completed())) {
			return ClientTransaction.STATUS_COMPLETED;
		} else if (status.equals(getMessages().cancelled())) {
			return ClientTransaction.STATUS_CANCELLED;
		}
		return -1;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Customer customer = get(CUSTOMER).getValue();
		estimate.setCustomer(customer.getID());
		estimate.setType(ClientTransaction.TYPE_ESTIMATE);
		ClientFinanceDate date = get(DATE).getValue();
		estimate.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		estimate.setNumber(number);

		Contact contact = get(CONTACT).getValue();
		if (contact != null)
			estimate.setContact(toClientContact(contact));

		ClientAddress billTo = get(BILL_TO).getValue();
		estimate.setAddress(billTo);

		String phone = get(PHONE).getValue();
		estimate.setPhone(phone);

		String saveStatus = get(STATUS).getValue();
		estimate.setStatus(getSaveStatus(saveStatus));

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		ShippingMethod method = get(SHIPPING_METHOD).getValue();
		if (method != null) {
			estimate.setShippingMethod(method.getID());
		}

		ShippingTerms shippingTerms = get(SHIPPING_TERM).getValue();
		if (shippingTerms != null) {
			estimate.setShippingTerm(shippingTerms.getID());
		}

		String customerOrderNo = get(CUSTOMER_ORDER_NO).getValue();
		estimate.setCustomerOrderNumber(customerOrderNo);

		estimate.setTransactionItems(items);

		PaymentTerms paymentTerm = get(PAYMENT_TERMS).getValue();
		estimate.setPaymentTerm(paymentTerm.getID());

		ClientFinanceDate d = get(DATE).getValue();
		estimate.setDate(d.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		estimate.setDeliveryDate(deliveryDate.getDate());
		ClientFinanceDate expiryDdate = get(EXPIRATION_DATE).getValue();
		estimate.setExpirationDate(expiryDdate.getDate());
		estimate.setCurrencyFactor(1);
		String memo = get(MEMO).getValue();
		estimate.setMemo(memo);
		double taxTotal = updateTotals(context, estimate, true);
		estimate.setTaxTotal(taxTotal);
		estimate.setCurrency(customer.getCurrency().getID());
		estimate.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		create(estimate, context);

		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
			return estimate.getID() == 0 ? getMessages().creating(
					getMessages().quote()) : getMessages().updating(
					getMessages().quote());
		} else if (estimate.getEstimateType() == ClientEstimate.CREDITS) {
			return estimate.getID() == 0 ? getMessages().creating(
					getMessages().credit()) : getMessages().updating(
					getMessages().credit());
		} else if (estimate.getEstimateType() == ClientEstimate.CHARGES) {
			return estimate.getID() == 0 ? getMessages().creating(
					getMessages().charge()) : getMessages().updating(
					getMessages().charge());
		} else if (estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
			return estimate.getID() == 0 ? getMessages().creating(
					getMessages().salesOrder()) : getMessages().updating(
					getMessages().salesOrder());
		}

		return "";
	}

	@Override
	protected String getDetailsMessage() {
		if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
			return estimate.getID() == 0 ? getMessages().readyToCreate(
					getMessages().quote()) : getMessages().readyToUpdate(
					getMessages().estimate());
		} else if (estimate.getEstimateType() == ClientEstimate.CREDITS) {
			return estimate.getID() == 0 ? getMessages().readyToCreate(
					getMessages().credit()) : getMessages().readyToUpdate(
					getMessages().credits());
		} else if (estimate.getEstimateType() == ClientEstimate.CHARGES) {
			return estimate.getID() == 0 ? getMessages().readyToCreate(
					getMessages().charge()) : getMessages().readyToUpdate(
					getMessages().Charges());
		} else if (estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
			return estimate.getID() == 0 ? getMessages().readyToCreate(
					getMessages().salesOrder()) : getMessages().readyToUpdate(
					getMessages().salesOrder());
		}

		return "";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}
		get(CUSTOMER_ORDER_NO).setDefaultValue(" ");
		get(STATUS).setDefaultValue(getMessages().open());
		get(DELIVERY_DATE).setDefaultValue(new ClientFinanceDate());
		get(EXPIRATION_DATE).setDefaultValue(new ClientFinanceDate());

		get(MEMO).setDefaultValue(" ");
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);

	}

	@Override
	public String getSuccessMessage() {
		if (estimate.getEstimateType() == ClientEstimate.QUOTES) {
			return estimate.getID() == 0 ? getMessages().createSuccessfully(
					getMessages().quote()) : getMessages().updateSuccessfully(
					getMessages().quote());
		} else if (estimate.getEstimateType() == ClientEstimate.CREDITS) {
			return estimate.getID() == 0 ? getMessages().createSuccessfully(
					getMessages().credit()) : getMessages().updateSuccessfully(
					getMessages().credit());
		} else if (estimate.getEstimateType() == ClientEstimate.CHARGES) {
			return estimate.getID() == 0 ? getMessages().createSuccessfully(
					getMessages().charge()) : getMessages().updateSuccessfully(
					getMessages().charge());
		} else if (estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
			return estimate.getID() == 0 ? getMessages().createSuccessfully(
					getMessages().salesOrder()) : getMessages()
					.updateSuccessfully(getMessages().salesOrder());
		}
		return "";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		int estimateType = ClientEstimate.QUOTES;
		String commandString = context.getCommandString().toLowerCase();
		if (commandString.contains("charge")) {
			estimateType = ClientEstimate.CHARGES;
		} else if (commandString.contains("credit")) {
			estimateType = ClientEstimate.CREDITS;
		} else if (commandString.contains("sales")) {
			estimateType = ClientEstimate.SALES_ORDER;
		}
		if (commandString.contains("quote")) {
			if (!context.getPreferences().isDoyouwantEstimates()) {
				addFirstMessage(context, getMessages()
						.youDntHavePermissionToDoThis());
				return "cancel";
			}
		} else {
			if (!context.getPreferences().isDelayedchargesEnabled()) {
				addFirstMessage(context, getMessages()
						.youDntHavePermissionToDoThis());
				return "cancel";
			}
		}

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				if (estimateType == ClientEstimate.QUOTES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().estimate()));
					return "quotesList";
				} else if (estimateType == ClientEstimate.CREDITS) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().credits()));
					return "creditsList";
				} else if (estimateType == ClientEstimate.CHARGES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().Charges()));
					return "chargesList";
				} else if (estimateType == ClientEstimate.SALES_ORDER) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().Charges()));
					return "salesorderlist";
				}
			}

			estimate = getTransaction(string, AccounterCoreType.ESTIMATE,
					context);

			// if(isUpdate)
			// {
			// if(estimate.getEstimateType() == ClientEstimate.CREDITS)
			// {
			// return "UpdateCredit";
			// }else if(estimate.getEstimateType() == ClientEstimate.CHARGES){
			// return "UpdateCharges";
			// }else if(estimate.getEstimateType() == ClientEstimate.QUOTES)
			// {
			// return "UpdateQuote";
			// }else if(estimate.getEstimateType() ==
			// ClientEstimate.SALES_ORDER){
			// return "UpdateSalesOrder";
			// }
			// }
			if (estimate == null) {
				if (estimateType == ClientEstimate.QUOTES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().estimate()));
					return "quotesList " + string;
				} else if (estimateType == ClientEstimate.CREDITS) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().credits()));
					return "creditsList " + string;
				} else if (estimateType == ClientEstimate.CHARGES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().Charges()));
					return "chargesList " + string;
				} else if (estimateType == ClientEstimate.CHARGES) {
					addFirstMessage(
							context,
							getMessages().selectATransactionToUpdate(
									getMessages().Charges()));
					return "chargesList " + string;
				}
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			estimate = new ClientEstimate();
			estimate.setEstimateType(estimateType);
		}
		setTransaction(estimate);

		return null;
	}

	private void setValues(Context context) {
		get(CURRENCY_FACTOR).setValue(estimate.getCurrencyFactor());
		get(DATE).setValue(estimate.getDate());
		get(NUMBER).setValue(estimate.getNumber());
		get(ITEMS).setValue(estimate.getTransactionItems());
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(estimate.getCustomer(),
						AccounterCoreType.CUSTOMER));
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			get(TAXCODE).setValue(
					getTaxCodeForTransactionItems(
							estimate.getTransactionItems(), context));

		}
		get(CUSTOMER_ORDER_NO).setValue(estimate.getCustomerOrderNumber());
		get(SHIPPING_METHOD).setValue(
				CommandUtils.getServerObjectById(estimate.getShippingMethod(),
						AccounterCoreType.SHIPPING_METHOD));
		get(SHIPPING_TERM).setValue(
				CommandUtils.getServerObjectById(estimate.getShippingTerm(),
						AccounterCoreType.SHIPPING_TERM));
		get(STATUS).setValue(getSaveStatusAsString(estimate.getSaveStatus()));
		get(DELIVERY_DATE).setValue(
				new ClientFinanceDate(estimate.getDeliveryDate()));
		get(EXPIRATION_DATE).setValue(
				new ClientFinanceDate(estimate.getExpirationDate()));
		get(CONTACT).setValue(toServerContact(estimate.getContact()));
		get(BILL_TO).setValue(estimate.getAddress());
		get(PAYMENT_TERMS).setValue(
				CommandUtils.getServerObjectById(estimate.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(MEMO).setValue(estimate.getMemo());
		get(IS_VAT_INCLUSIVE).setValue(isAmountIncludeTAX(estimate));
		get(PHONE).setValue(estimate.getPhone());
		if (getPreferences().isTrackDiscounts()
				&& !getPreferences().isDiscountPerDetailLine()) {
			get(DISCOUNT).setValue(
					getDiscountFromTransactionItems(estimate
							.getTransactionItems()));
		}
	}

	private String getSaveStatusAsString(int saveStatus) {
		switch (saveStatus) {
		case ClientEstimate.STATUS_OPEN:
			return getMessages().open();
		case ClientEstimate.STATUS_ACCECPTED:
			if (estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
				return getMessages().completed();
			}
			return getMessages().accepted();
		case ClientEstimate.STATUS_CLOSE:
			return getMessages().closed();
		case ClientEstimate.STATUS_REJECTED:
			return getMessages().rejected();
		case ClientEstimate.STATUS_COMPLETED:
			if (estimate.getEstimateType() == ClientEstimate.SALES_ORDER) {
				return getMessages().completed();
			}
			return getMessages().closed();
		case ClientTransaction.STATUS_CANCELLED:
			return getMessages().cancelled();
		default:
			break;
		}
		return "";
	}

	@Override
	protected Currency getCurrency() {
		return ((Customer) CreateQuoteCommand.this.get(CUSTOMER).getValue())
				.getCurrency();
	}

}
