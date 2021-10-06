package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.IssuePaymentTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class CreateIssuePaymentCommand extends AbstractTransactionCommand {
	private static final String PAYMENTS_TO_ISSUED = "paymentstoissued";
	private static final String CHEQUE_NO = "checknum";
	private static final String PAYMENT_METHOD = "paymentMethod";
	private static final String ACCOUNT = "account";
	ClientIssuePayment issuePayment;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), false, true, null) {

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
				String payVatMethodArray[] = new String[] { getMessages()
						.check() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AccountRequirement(ACCOUNT, getMessages()
				.pleaseSelectPayFromAccount(), getMessages().bankAccount(),
				false, false, null) {

			@Override
			protected String getSetMessage() {
				return "";
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getIsActive()
									&& (e.getType() == Account.TYPE_BANK || e
											.getType() == Account.TYPE_OTHER_CURRENT_ASSET)) {
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
				return getMessages().noRecordsToShow();
			}

			@Override
			protected boolean filter(Account e, String name) {
				return false;
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getMessages().checkNo()), getMessages().checkNo(), true, true));
		list.add(new IssuePaymentTableRequirement(PAYMENTS_TO_ISSUED,
				getMessages().selectTypeOfThis(getMessages().payment()),
				"payments list", false, true) {

			@Override
			protected Account getAccount() {
				return (Account) CreateIssuePaymentCommand.this.get(ACCOUNT)
						.getValue();
			}

		});
	}

	private String getNextCheckNumber(Context context) {
		Account account = get(ACCOUNT).getValue();
		if (account == null) {
			return "";
		}
		String checknumber = "";
		try {
			String nextIssuePaymentCheckNumber = new FinanceTool()
					.getNextIssuePaymentCheckNumber(account.getID(), context
							.getCompany().getID());
			if (nextIssuePaymentCheckNumber != null) {
				checknumber = nextIssuePaymentCheckNumber;
			}
		} catch (Exception e) {
			checknumber = "";
		}
		return checknumber;
	}

	private String getNextTransactionNumber(Context context) {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(ClientTransaction.TYPE_ISSUE_PAYMENT,
						context.getCompany().getID());
		return nextTransactionNumber;
	}

	private void completeProcess(Context context) {
		issuePayment.setType(ClientTransaction.TYPE_ISSUE_PAYMENT);
		issuePayment.setNumber(getNextTransactionNumber(context));
		issuePayment.setDate(new ClientFinanceDate().getDate());
		String paymentmethod = get(PAYMENT_METHOD).getValue();
		issuePayment.setPaymentMethod(paymentmethod);
		Account account = get(ACCOUNT).getValue();
		issuePayment.setAccount(account.getID());

		String chequenum = get(CHEQUE_NO).getValue();
		if (chequenum.isEmpty()) {
			chequenum = "1";
		}
		issuePayment.setCheckNumber(chequenum);
		issuePayment.setTransactionIssuePayment(getTransactionIssuePayments());
		issuePayment.setTotal(getTransactionTotal());
		new FinanceTool().doCreateIssuePaymentEffect(getCompanyId(),
				issuePayment);
	}

	/**
	 * 
	 * @param issuePayment
	 * @return
	 */
	private List<ClientTransactionIssuePayment> getTransactionIssuePayments() {
		List<ClientTransactionIssuePayment> transactionIssuePaymentsList = new ArrayList<ClientTransactionIssuePayment>();
		ArrayList<IssuePaymentTransactionsList> issuepayments = get(
				PAYMENTS_TO_ISSUED).getValue();
		for (IssuePaymentTransactionsList entry : issuepayments) {
			ClientTransactionIssuePayment record = new ClientTransactionIssuePayment();
			if (entry.getDate() != null)
				record.setDate(entry.getDate().getDate());
			if (entry.getNumber() != null)
				record.setNumber(entry.getNumber());
			record.setName(entry.getName() != null ? entry.getName() : "");
			record.setMemo(entry.getMemo() != null ? entry.getMemo() : "");
			if (entry.getAmount() != null)
				record.setAmount(entry.getAmount());
			if (entry.getPaymentMethod() != null)
				record.setPaymentMethod(entry.getPaymentMethod());
			record.setRecordType(entry.getType());

			switch (entry.getType()) {
			case ClientTransaction.TYPE_WRITE_CHECK:
				record.setWriteCheck(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
				break;
			case ClientTransaction.TYPE_CASH_PURCHASE:
			case ClientTransaction.TYPE_CASH_EXPENSE:
			case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
				record.setCashPurchase(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
				break;
			case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
				record.setCustomerRefund(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
				break;
			case ClientTransaction.TYPE_PAY_TAX:
				record.setPaySalesTax(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_PAY_TAX);
				break;
			case ClientTransaction.TYPE_PAY_BILL:
				record.setPayBill(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_PAY_BILL);
				break;
			case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
				record.setCreditCardCharge(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
				break;
			case ClientTransaction.TYPE_RECEIVE_TAX:
				record.setReceiveVAT(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_RECEIVE_TAX);
				break;
			case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
				record.setCustomerPrepayment(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
				break;

			}
			record.setTransaction(issuePayment);
			transactionIssuePaymentsList.add(record);
		}

		return transactionIssuePaymentsList;

	}

	private double getTransactionTotal() {
		double total = 0.0;
		for (ClientTransactionIssuePayment rec : getTransactionIssuePayments()) {
			total += rec.getAmount();
			rec.setTransaction(issuePayment);
		}
		return total;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().issuePayment()));
				return "vendorPayments";
			}
			issuePayment = getTransaction(string,
					AccounterCoreType.ISSUEPAYMENT, context);

			if (issuePayment == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().issuePayment()));
				return "vendorPayments " + string;
			}
			setValues(context);
		} else {
			issuePayment = new ClientIssuePayment();
		}
		setTransaction(issuePayment);
		return null;
	}

	private void setValues(Context context) {
		get(PAYMENT_METHOD).setValue(
				CommandUtils.getPaymentMethod(
						issuePayment.getPaymentMethodForCommands(),
						getMessages()));
		get(ACCOUNT).setValue(
				CommandUtils.getServerObjectById(issuePayment.getAccount(),
						AccounterCoreType.ACCOUNT));
		get(CHEQUE_NO).setValue(issuePayment.getCheckNumber());
		get(PAYMENTS_TO_ISSUED).setValue(getIssuePaymentTransactionsList());

	}

	private List<IssuePaymentTransactionsList> getIssuePaymentTransactionsList() {
		List<IssuePaymentTransactionsList> issuePaymentList = new ArrayList<IssuePaymentTransactionsList>();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : issuePayment
				.getTransactionIssuePayment()) {
			IssuePaymentTransactionsList issuedPayment = new IssuePaymentTransactionsList();
			issuedPayment.setTransactionId(clientTransactionIssuePayment
					.getTransaction().getID());
			issuedPayment.setAmount(clientTransactionIssuePayment.getAmount());
			issuedPayment.setDate(new ClientFinanceDate(
					clientTransactionIssuePayment.getDate()));
			issuedPayment.setMemo(clientTransactionIssuePayment.getMemo());
			issuedPayment.setName(clientTransactionIssuePayment.getName());
			issuedPayment.setNumber(clientTransactionIssuePayment.getNumber());
			issuedPayment.setPaymentMethod(clientTransactionIssuePayment
					.getPaymentMethod());
			issuedPayment
					.setType(clientTransactionIssuePayment.getRecordType());
			issuePaymentList.add(issuedPayment);
		}
		return issuePaymentList;
	}

	@Override
	protected String getWelcomeMessage() {
		return issuePayment.getID() == 0 ? "Creating Issue Payment... "
				: "Updating issue payment";
	}

	@Override
	protected String getDetailsMessage() {
		return issuePayment.getID() == 0 ? getMessages().readyToCreate(
				getMessages().issuePayment()) : getMessages().readyToUpdate(
				getMessages().issuePayment());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(CHEQUE_NO).setValue(getNextCheckNumber(context));
	}

	@Override
	public String getSuccessMessage() {
		return issuePayment.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().issuePayment()) : getMessages()
				.updateSuccessfully(getMessages().issuePayment());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		completeProcess(context);
		return null;
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		makeResult.add(getMessages().total() + ":" + getTransactionTotal());
	}

	@Override
	protected Currency getCurrency() {
		return null;
	}
}
