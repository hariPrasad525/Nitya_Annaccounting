package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class IssuePaymentTableRequirement extends
		AbstractTableRequirement<IssuePaymentTransactionsList> {

	private static final String DATE = "date";
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	private static final String MEMO = "memo";
	private static final String AMOUNT = "amount";
	private static final String PAYMENT_METHOD = "paymentMethod";

	public IssuePaymentTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, false, isOptional,
				isAllowFromContext);
		setDefaultValue(new ArrayList<IssuePaymentTransactionsList>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		DateRequirement billDueDate = new DateRequirement(DATE, getMessages()
				.pleaseEnter(getMessages().date()), getMessages().date(), true,
				true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement(NUMBER, "",
				getMessages().billNo(), true, true);
		billNo.setEditable(false);
		list.add(billNo);

		NameRequirement name = new NameRequirement(NAME, "", getMessages()
				.name(), true, true);
		name.setEditable(false);
		list.add(name);

		NameRequirement memo = new NameRequirement(MEMO, "", getMessages()
				.memo(), true, true);
		name.setEditable(false);
		list.add(memo);

		AmountRequirement amount = new AmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getMessages().amount()), getMessages().amount(),
				true, true);
		amount.setEditable(false);
		list.add(amount);

		NameRequirement paymentMethod = new NameRequirement(PAYMENT_METHOD,
				getMessages().pleaseEnter(getMessages().paymentMethod()),
				getMessages().paymentMethod(), true, true);
		paymentMethod.setEditable(false);
		list.add(paymentMethod);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().paymentsToIssue());
	}

	@Override
	protected void getRequirementsValues(IssuePaymentTransactionsList obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setRequirementsDefaultValues(IssuePaymentTransactionsList obj) {

		get(DATE).setDefaultValue((obj.getDate()));
		get(NUMBER).setDefaultValue(obj.getNumber());
		get(NAME).setDefaultValue(obj.getName());
		get(MEMO).setDefaultValue(obj.getMemo());
		get(AMOUNT).setDefaultValue(obj.getAmount());
		get(PAYMENT_METHOD).setDefaultValue(obj.getPaymentMethod());
	}

	@Override
	protected IssuePaymentTransactionsList getNewObject() {
		return new IssuePaymentTransactionsList();
	}

	protected abstract Account getAccount();

	@Override
	protected Record createFullRecord(IssuePaymentTransactionsList t) {
		Record record = new Record(t);
		record.add(getMessages().date(), t.getDate());
		record.add(getMessages().number(), t.getNumber());
		record.add(getMessages().name(), t.getName());
		record.add(getMessages().memo(), t.getMemo());
		record.add(getMessages().amount(), t.getAmount());
		record.add(getMessages().paymentMethod(), t.getPaymentMethod());
		return record;
	}

	@Override
	protected List<IssuePaymentTransactionsList> getList() {
		try {
			return new FinanceTool().getVendorManager().getChecks(
					getAccount().getID(), getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected Record createRecord(IssuePaymentTransactionsList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<IssuePaymentTransactionsList> oldValues = getValue();
		return oldValues.isEmpty() ? getMessages().addOf(
				getMessages().issuePayments()) : getMessages().addMore(
				getMessages().issuePayments());
	}

	@Override
	protected boolean contains(List<IssuePaymentTransactionsList> oldValues,
			IssuePaymentTransactionsList t) {
		for (IssuePaymentTransactionsList issuePaymentTransactionsList : oldValues) {
			if (issuePaymentTransactionsList.getTransactionId() == t
					.getTransactionId()) {
				return true;
			}
		}
		return false;
	}
}
