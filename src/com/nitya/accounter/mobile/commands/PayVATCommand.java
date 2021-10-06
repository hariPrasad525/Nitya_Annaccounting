package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.NumberUtils;
import com.nitya.accounter.core.TAXAgency;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.DateRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.PayVatTableRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.requirements.TaxAgencyRequirement;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayTAX;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionPayTAX;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.server.FinanceTool;

public class PayVATCommand extends AbstractTransactionCommand {

	private static final String VAT_RETURN_END_DATE = "vatReturnEndDate";
	private static final String BILLS_TO_PAY = "billToPay";
	private static final String PAY_FROM = "payFrom";
	private final String TAX_AGENCY = "taxAgency";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new AccountRequirement(PAY_FROM, getMessages().pleaseSelect(
				getMessages().bankAccount()), getMessages().bankAccount(),
				false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getIsActive()
									&& (e.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET || e
											.getType() == ClientAccount.TYPE_CREDIT_CARD)) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages()
						.youDontHaveAny(getMessages().bankAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);

			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});

		list.add(new DateRequirement(VAT_RETURN_END_DATE, getMessages()
				.pleaseEnter(getMessages().filterBy()), getMessages()
				.filterBy(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnterName(getMessages().taxAgency()), getMessages()
				.taxAgency(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().taxAgency());
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(context.getCompany()
						.getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().taxAgency());
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				return e.getName().toLowerCase().startsWith(name);
			}
		});
		list.add(new PayVatTableRequirement(BILLS_TO_PAY, getMessages()
				.pleaseSelect(getMessages().billsToPay()), getMessages()
				.billsToPay()) {

			@Override
			protected List<ClientTransactionPayTAX> getList() {
				return getClientTransactionPayTAXes();
			}

		});
	}

	private List<ClientTransactionPayTAX> getClientTransactionPayTAXes() {
		TAXAgency selectedVATAgency = null;
		if (get(TAX_AGENCY) != null) {
			selectedVATAgency = get(TAX_AGENCY).getValue();
		}
		if (selectedVATAgency == null) {
			return getPayTAXEntries();
		}
		List<ClientTransactionPayTAX> filterRecords = new ArrayList<ClientTransactionPayTAX>();
		for (ClientTransactionPayTAX tpt : getPayTAXEntries()) {
			if (tpt.getTaxAgency() == selectedVATAgency.getID()) {
				filterRecords.add(tpt);
			}
		}
		return filterRecords;
	}

	private List<ClientTransactionPayTAX> getPayTAXEntries() {
		return new FinanceTool().getTaxManager().getPayTAXEntries(
				getCompanyId());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientPayTAX payVAT = new ClientPayTAX();
		Account payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<ClientTransactionPayTAX> billsToPay = get(BILLS_TO_PAY).getValue();
		for (ClientTransactionPayTAX clientTransactionPayTAX : billsToPay) {
			clientTransactionPayTAX.setID(0);
		}
		ClientFinanceDate vatReturnDate = get(VAT_RETURN_END_DATE).getValue();
		ClientFinanceDate transactionDate = get(DATE).getValue();
		TAXAgency taxAgency = get(TAX_AGENCY).getValue();
		String orderNo = get(ORDER_NO).getValue();
		payVAT.setNumber(orderNo);
		payVAT.setType(ClientTransaction.TYPE_PAY_TAX);
		payVAT.setPayFrom(payFrom.getID());
		payVAT.setPaymentMethod(paymentMethod);
		payVAT.setTransactionPayTax(billsToPay);
		payVAT.setBillsDueOnOrBefore(vatReturnDate.getDate());
		payVAT.setTaxAgency(taxAgency == null ? 0 : taxAgency.getID());
		payVAT.setDate(transactionDate.getDate());

		create(payVAT, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, getMessages()
					.youDoNotHavePermissionToDoThisAction());
			return "cancel";
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().payVAT());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().payVAT());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(VAT_RETURN_END_DATE).setDefaultValue(new ClientFinanceDate());
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(ORDER_NO).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_PAY_TAX, context.getCompany()));

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().payVAT());
	}

	@Override
	protected Currency getCurrency() {
		return null;
	}

}
