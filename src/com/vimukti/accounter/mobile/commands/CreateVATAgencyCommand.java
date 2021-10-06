package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CustomerContactRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.util.CountryPreferenceFactory;

public class CreateVATAgencyCommand extends AbstractCommand {

	private static final String PAYMENT_TERM = "paymentTerm";
	private static final String SALES_ACCOUNT = "salesLiabilityAccount";
	private static final String PURCHASE_ACCOUNT = "purchaseLiabilityAccount";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EMAIL = "email";
	private static final String WEBSITE = "webPageAddress";
	protected static final String CONTACT_NAME = "contactName";
	protected static final String TITLE = "title";
	protected static final String BUSINESS_PHONE = "businessPhone";
	protected static final String CONTACT_EMAIL = "contactEmail";
	private static final String IS_ACTIVE = "isActive";

	private static final String TAX_AGENCY_NAME = "Tax Agency Name";
	private static final String VAT_RETURN = "Vat Return";
	private static final String ADREESS = "address";
	private static final String CONTACTS = "contact";
	private static final String TAX_TYPE = "taxType";
	ClientTAXAgency taxAgency;

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringRequirement(TAX_AGENCY_NAME, getMessages()
				.pleaseEnter(getMessages().taxAgency()), "Tax Agency", false,
				true));

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inActive();
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERM, getMessages()
				.pleaseSelect(getMessages().paymentTerm()), getMessages()
				.paymentTerm(), false, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new StringListRequirement(TAX_TYPE, getMessages()
				.pleaseSelect(getMessages().taxType()),
				getMessages().taxType(), false, true,
				new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						taxTypeSelected(value);
					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().taxType());
			}

			@Override
			protected List<String> getLists(Context context) {
				String[] types = new String[] { getMessages().salesTax(),
						getMessages().vat(), getMessages().serviceTax(),
						getMessages().tds(), getMessages().other() };
				return Arrays.asList(types);
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(VAT_RETURN, getMessages()
				.pleaseSelect(getMessages().taxReturn()), "Vat Return", false,
				true, null) {
			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().taxReturn());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getVatReturns();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isTrackPaidTax()) {
					if (context.getCompany().getCountry()
							.equals(CountryPreferenceFactory.UNITED_KINGDOM)) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

		});

		list.add(new AccountRequirement(SALES_ACCOUNT, getMessages()
				.pleaseSelect(
						getMessages().sales() + getMessages().liability()
								+ getMessages().account()), getMessages()
				.sales() + getMessages().account(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return getSalesLiabilityAccounts(context,
						new ListFilter<Account>() {

							@Override
							public boolean filter(Account account) {
								return account.getIsActive()
										&& Arrays
												.asList(Account.TYPE_INCOME,
														Account.TYPE_EXPENSE,
														Account.TYPE_OTHER_CURRENT_LIABILITY,
														Account.TYPE_OTHER_CURRENT_ASSET,
														Account.TYPE_FIXED_ASSET)
												.contains(account.getType());
							}
						});
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AccountRequirement(PURCHASE_ACCOUNT, getMessages()
				.pleaseSelect(
						getMessages().purchase() + getMessages().liability()
								+ getMessages().account()), getMessages()
				.purchase() + getMessages().account(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return getSalesLiabilityAccounts(context,
						new ListFilter<Account>() {

							@Override
							public boolean filter(Account account) {
								return account.getIsActive()
										&& Arrays
												.asList(Account.TYPE_INCOME,
														Account.TYPE_EXPENSE,
														Account.TYPE_OTHER_CURRENT_LIABILITY,
														Account.TYPE_OTHER_CURRENT_ASSET,
														Account.TYPE_FIXED_ASSET)
												.contains(account.getType());
							}
						});
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AddressRequirement(ADREESS, getMessages().pleaseEnter(
				getMessages().address()), getMessages().address(), true, true));

		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

		list.add(new StringRequirement(FAX, getMessages().pleaseEnter(
				getMessages().faxNumber()), getMessages().faxNumber(), true,
				true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new URLRequirement(WEBSITE, getMessages().pleaseEnter(
				getMessages().webSite()), getMessages().webSite(), true, true));

		list.add(new CustomerContactRequirement(CONTACTS, getMessages()
				.pleaseSelect(getMessages().contact()), CONTACTS, true, true) {

			@Override
			protected List<ClientContact> getList() {
				List<ClientContact> contacts = getAgencyContacts();
				return new ArrayList<ClientContact>(contacts);
			}

		});

	}

	protected List<ClientContact> getAgencyContacts() {
		if (taxAgency.getID() != 0) {
			new ArrayList<ClientContact>(taxAgency.getContacts());
		}
		return null;
	}

	protected List<Account> getSalesLiabilityAccounts(Context context,
			ListFilter<Account> filter) {
		List<Account> filteredList = new ArrayList<Account>();
		for (Account obj : context.getCompany().getAccounts()) {
			if (filter.filter(obj)) {
				filteredList.add(obj);
			}
		}
		return filteredList;
	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = taxAgency.getID();
		return id != 0 ? "deleteTaxagency " + id : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
		}

		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().taxAgency()));
				return "vendors";
			}
			ClientPayee vendorByName = CommandUtils.getPayeeByName(
					context.getCompany(), string);
			if (vendorByName == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().taxAgency()));
				return "vendors " + string;
			}
			taxAgency = (ClientTAXAgency) vendorByName;
			setValues();
		} else {
			taxAgency = new ClientTAXAgency();
			if (string.isEmpty()) {
				get(TAX_AGENCY_NAME).setValue(string);
			}
		}
		return null;
	}

	private void setValues() {
		get(TAX_AGENCY_NAME).setValue(taxAgency.getName());
		get(PAYMENT_TERM).setValue(
				CommandUtils.getServerObjectById(taxAgency.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(SALES_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(
						taxAgency.getSalesLiabilityAccount(),
						AccounterCoreType.ACCOUNT));
		get(PHONE).setValue(taxAgency.getPhoneNo());
		get(FAX).setValue(taxAgency.getFaxNo());
		get(EMAIL).setValue(taxAgency.getEmail());
		get(WEBSITE).setValue(taxAgency.getWebPageAddress());
		List<ClientAddress> address = new ArrayList<ClientAddress>(
				taxAgency.getAddress());
		if (!address.isEmpty()) {
			get(ADREESS).setValue(address.get(0));
		}
		get(CONTACTS).setValue(
				new ArrayList<ClientContact>(taxAgency.getContacts()));
		get(PURCHASE_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(
						taxAgency.getPurchaseLiabilityAccount(),
						AccounterCoreType.ACCOUNT));
		int vatReturn = taxAgency.getVATReturn();
		if (vatReturn == ClientTAXAgency.RETURN_TYPE_NONE) {
			get(VAT_RETURN).setValue("");
		} else if (vatReturn == ClientTAXAgency.RETURN_TYPE_UK_VAT) {
			get(VAT_RETURN).setValue("UK VAT");
		} else if (vatReturn == ClientTAXAgency.RETURN_TYPE_IRELAND_VAT) {
			get(VAT_RETURN).setValue(null);
		}
		String taxTypeString = getTaxTypeString(taxAgency.getTaxType());
		get(TAX_TYPE).setValue(taxTypeString);
		taxTypeSelected(taxTypeString);
	}

	@Override
	protected String getWelcomeMessage() {
		return taxAgency.getID() == 0 ? getMessages().create(
				getMessages().taxAgency()) : getMessages().updating(
				getMessages().taxAgency());
	}

	@Override
	protected String getDetailsMessage() {
		return taxAgency.getID() == 0 ? getMessages().readyToCreate(
				getMessages().taxAgency()) : getMessages().readyToUpdate(
				getMessages().taxAgency());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		return taxAgency.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().taxAgency()) : getMessages().updateSuccessfully(
				getMessages().taxAgency());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(TAX_AGENCY_NAME).getValue();
		PaymentTerms paymentTerm = get(PAYMENT_TERM).getValue();
		Account salesAccount = get(SALES_ACCOUNT).getValue();

		String phone = (String) get(PHONE).getValue();
		String fax = (String) get(FAX).getValue();
		String email = (String) get(EMAIL).getValue();
		String website = (String) get(WEBSITE).getValue();

		taxAgency.setName(name);
		taxAgency.setPaymentTerm(paymentTerm.getID());
		taxAgency.setSalesLiabilityAccount(salesAccount == null ? 0
				: salesAccount.getID());
		taxAgency.setPhoneNo(phone);
		taxAgency.setFaxNo(fax);
		taxAgency.setEmail(email);
		taxAgency.setWebPageAddress(website);
		ClientAddress address = get(ADREESS).getValue();
		Set<ClientAddress> listAddress = new HashSet<ClientAddress>();
		if (address != null) {
			listAddress.add(address);
		}
		taxAgency.setAddress(listAddress);
		List<ClientContact> contactList = get(CONTACTS).getValue();
		taxAgency.setContacts(new HashSet<ClientContact>(contactList));
		if (context.getPreferences().isTrackPaidTax()) {
			Account purchaseAccount = get(PURCHASE_ACCOUNT).getValue();
			String vatReturn = get(VAT_RETURN).getValue();
			taxAgency.setPurchaseLiabilityAccount(purchaseAccount == null ? 0
					: purchaseAccount.getID());
			if (vatReturn == "") {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_NONE);
			} else if (vatReturn == "UK VAT") {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_UK_VAT);
			} else {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_IRELAND_VAT);
			}
		}
		String taxType = get(TAX_TYPE).getValue();
		taxAgency.setTaxType(getTaxType(taxType));
		create(taxAgency, context);

		markDone();
		return null;
	}

	private List<String> getVatReturns() {
		ArrayList<String> vatReturnList = new ArrayList<String>();
		vatReturnList.add(getMessages().ukVAT());
		vatReturnList.add(getMessages().vat3Ireland());
		return vatReturnList;
	}

	private void taxTypeSelected(String selectedType) {
		int type = getTaxType(selectedType);
		if (type == ClientTAXAgency.TAX_TYPE_SERVICETAX
				|| type == ClientTAXAgency.TAX_TYPE_VAT) {
			get(SALES_ACCOUNT).setOptional(false);
			get(PURCHASE_ACCOUNT).setOptional(false);
			get(PURCHASE_ACCOUNT).setEditable(true);
		} else if (type == ClientTAXAgency.TAX_TYPE_SALESTAX) {
			get(SALES_ACCOUNT).setOptional(false);
			get(PURCHASE_ACCOUNT).setOptional(true);
			get(PURCHASE_ACCOUNT).setEditable(false);
			get(PURCHASE_ACCOUNT).setValue(null);
		} else if (type == ClientTAXAgency.TAX_TYPE_TDS) {
			get(SALES_ACCOUNT).setOptional(false);
			get(PURCHASE_ACCOUNT).setOptional(true);
			get(PURCHASE_ACCOUNT).setEditable(false);
			get(PURCHASE_ACCOUNT).setValue(null);
		} else {
			get(SALES_ACCOUNT).setOptional(false);
			get(PURCHASE_ACCOUNT).setOptional(false);
			get(PURCHASE_ACCOUNT).setEditable(true);
		}

		if (type != ClientTAXAgency.TAX_TYPE_VAT) {
			get(VAT_RETURN).setOptional(true);
			get(VAT_RETURN).setEditable(false);
			get(VAT_RETURN).setValue("");
		} else {
			if (getCompany().getCountry().equals(
					CountryPreferenceFactory.UNITED_KINGDOM)) {
				get(VAT_RETURN).setOptional(false);
				get(VAT_RETURN).setEditable(true);
			}
		}
	}

	private int getTaxType(String taxType) {
		if (taxType == null) {
			return 0;
		}
		if (taxType.equals(getMessages().salesTax())) {
			return 1;
		} else if (taxType.equals(getMessages().vat())) {
			return 2;
		} else if (taxType.equals(getMessages().serviceTax())) {
			return 3;
		} else if (taxType.equals(getMessages().tds())) {
			return 4;
		} else if (taxType.equals(getMessages().other())) {
			return 5;
		} else {
			return 0;
		}
	}

	private String getTaxTypeString(int taxType) {
		if (taxType == ClientTAXAgency.TAX_TYPE_SERVICETAX) {
			return getMessages().serviceTax();
		}
		if (taxType == ClientTAXAgency.TAX_TYPE_VAT) {
			return getMessages().vat();
		}

		if (taxType == ClientTAXAgency.TAX_TYPE_SALESTAX) {
			return getMessages().salesTax();
		}
		if (taxType == ClientTAXAgency.TAX_TYPE_SALESTAX) {
			return getMessages().tds();
		}
		if (taxType == ClientTAXAgency.TAX_TYPE_OTHER) {
			getMessages().other();
		}

		return null;
	}
}
