package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;

public abstract class PaybillTableRequirement extends
		AbstractTableRequirement<ClientTransactionPayBill> {
	private static final String BILL_NO = "BillNo";
	private static final String ORIGINAL_AMOUNT = "OriginalAmount";
	private static final String AMOUNT = "Amount";
	private static final String DUE_DATE = "BillDueDate";
	private static final String DISCOUNT_DATE = "billdiscountDate";
	private static final String CASH_DISCOUNT = "cashDiscount";
	private static final String APPLIED_CREDITS = "appliedcredits";
	private static final String PAYMENT = "payment";

	public PaybillTableRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {

		DateRequirement billDueDate = new DateRequirement(DUE_DATE,
				getMessages().pleaseEnter(getMessages().dueDate()),
				getMessages().dueDate(), true, true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement(BILL_NO, "",
				getMessages().billNo(), true, true);
		billNo.setEditable(false);
		list.add(billNo);

		CurrencyAmountRequirement originalAmount = new CurrencyAmountRequirement(
				ORIGINAL_AMOUNT, "", getMessages().originalAmount(), true, true) {
			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}
		};
		originalAmount.setEditable(false);
		list.add(originalAmount);

		CurrencyAmountRequirement amountDue = new CurrencyAmountRequirement(
				AMOUNT, getMessages().pleaseEnter(getMessages().amountDue()),
				getMessages().amountDue(), true, true) {
			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}
		};
		amountDue.setEditable(false);
		list.add(amountDue);

		DateRequirement discountDate = new DateRequirement(DISCOUNT_DATE,
				getMessages().pleaseEnter(getMessages().discountDate()),
				getMessages().discountDate(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackDiscounts()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		};
		discountDate.setEditable(false);
		list.add(discountDate);

		CashDiscountWriteOffRequirement cashDiscountReq = new CashDiscountWriteOffRequirement(
				CASH_DISCOUNT, getMessages().pleaseSelect(
						getMessages().cashDiscount()), getMessages()
						.cashDiscount(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackDiscounts()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				if (value != null) {
					updateTransactionPayBill();
				}
			}
		};
		list.add(cashDiscountReq);

		list.add(new ApplyCreditsRequirement(APPLIED_CREDITS, getMessages()
				.pleaseEnter(getMessages().appliedCredits()), getMessages()
				.appliedCredits()) {

			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}

			@Override
			protected double getAmountDue() {
				return PaybillTableRequirement.this.get(AMOUNT).getValue();
			}

			@Override
			public List<ClientCreditsAndPayments> getTotalCredits() {
				return PaybillTableRequirement.this.getCreditsPayments();
			}

			public java.util.List<ClientTransactionPayBill> getPayBillPayments() {
				return PaybillTableRequirement.this.getValue();
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				if (value != null) {
					updateTransactionPayBill();
				}
			}

		});

		CurrencyAmountRequirement amount = new CurrencyAmountRequirement(
				PAYMENT, getMessages().pleaseEnter(getMessages().payment()),
				getMessages().payment(), true, true) {
			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}
		};
		list.add(amount);

	}

	protected void updateTransactionPayBill() {
		ClientTransactionPayBill obj = PaybillTableRequirement.this.currentValue;
		if (obj != null) {
			CashDiscountWriteOffRequirement offRequirement = (CashDiscountWriteOffRequirement) get(CASH_DISCOUNT);
			if (offRequirement.getAccount() != null
					&& offRequirement.getValue() != null) {
				obj.setCashDiscount((Double) offRequirement.getValue());
				obj.setDiscountAccount(offRequirement.getAccount().getID());
			} else {
				obj.setCashDiscount(0.0);
				obj.setDiscountAccount(0);
			}

			Double appliedCredits = get(APPLIED_CREDITS).getValue();
			if (appliedCredits != null) {
				obj.setAppliedCredits(appliedCredits, true);
			}

			obj.updatePayment();
			// obj.setDummyDue(obj.getAmountDue() - obj.getPayment());
			updatePayment(obj);
			PaybillTableRequirement.this.get(PAYMENT)
					.setValue(obj.getPayment());
		}
	}

	public void vendorSelected() {
		((ApplyCreditsRequirement) get(APPLIED_CREDITS))
				.addCreditsAndPayments();
	}

	protected abstract List<ClientCreditsAndPayments> getCreditsPayments();

	public abstract long getTransactionId();

	protected void updatePayment(ClientTransactionPayBill rec) {
		Double amountDue = rec.getAmountDue();
		Double cashDiscount = rec.getCashDiscount();
		Double credit = rec.getAppliedCredits();
		Double payments = amountDue - (cashDiscount + credit);
		if (rec.getPayment() == 0
				&& !getPreferences().isCreditsApplyAutomaticEnable()) {
			rec.setPayment(payments);
		}

		rec.setCashDiscount(cashDiscount);
	}

	@Override
	protected void getRequirementsValues(ClientTransactionPayBill obj) {
		Double amount = get(PAYMENT).getValue();
		obj.setPayment(amount);
		Double appliedCredits = get(APPLIED_CREDITS).getValue();
		obj.setAppliedCredits(appliedCredits, true);
		CashDiscountWriteOffRequirement offRequirement = (CashDiscountWriteOffRequirement) get(CASH_DISCOUNT);
		if (offRequirement.getAccount() != null) {
			obj.setCashDiscount((Double) offRequirement.getValue());
			obj.setDiscountAccount(offRequirement.getAccount().getID());
		}
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionPayBill obj) {
		get(DUE_DATE).setValue(new ClientFinanceDate(obj.getDueDate()));
		get(BILL_NO).setValue(obj.getBillNumber());
		get(ORIGINAL_AMOUNT).setValue(obj.getOriginalAmount());
		get(AMOUNT).setValue(obj.getAmountDue());
		get(PAYMENT).setDefaultValue(obj.getAmountDue());
		get(DISCOUNT_DATE).setValue(
				new ClientFinanceDate(obj.getDiscountDate()));
		CashDiscountWriteOffRequirement offRequirement = (CashDiscountWriteOffRequirement) get(CASH_DISCOUNT);
		offRequirement.setAccount((Account) CommandUtils.getServerObjectById(
				obj.getDiscountAccount(), AccounterCoreType.ACCOUNT));
		offRequirement.setValue(obj.getCashDiscount());
		ApplyCreditsRequirement applyCreditsRequirement = (ApplyCreditsRequirement) get(APPLIED_CREDITS);
		applyCreditsRequirement.setTransactionCreditsAndPayments(obj
				.getTransactionCreditsAndPayments());
		get(APPLIED_CREDITS).setValue(obj.getAppliedCredits());
	}

	@Override
	protected ClientTransactionPayBill getNewObject() {
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionPayBill t) {
		String formalName;
		if (getPreferences().isEnableMultiCurrency()) {
			formalName = getCurrency().getFormalName();
		} else {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
		}

		Record record = new Record(t);
		record.add(getMessages().dueDate(), t.getDueDate());
		record.add(getMessages().billNo(), t.getBillNumber());
		record.add(getMessages().originalAmount() + "(" + formalName + ")",
				t.getOriginalAmount());
		record.add(getMessages().amountDue() + "(" + formalName + ")",
				t.getAmountDue());
		if (getPreferences().isTrackDiscounts()) {
			record.add(getMessages().discountDate(), t.getDiscountDate());
			record.add(getMessages().cashDiscount(), t.getCashDiscount());
		}
		record.add(getMessages().appliedCredits(), t.getAppliedCredits());
		record.add(getMessages().payment() + "(" + formalName + ")",
				t.getPayment());
		return record;
	}

	@Override
	protected Record createRecord(ClientTransactionPayBill t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().billsToPay());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().billsToPay());
	}

	@Override
	protected boolean contains(List<ClientTransactionPayBill> oldValues,
			ClientTransactionPayBill t) {
		for (ClientTransactionPayBill payBillTransactionList : oldValues) {
			if (payBillTransactionList.getEnterBill() == t.getEnterBill()) {
				return true;
			}
		}
		return false;
	}

	protected abstract Payee getPayee();

	protected Currency getCurrency() {
		return getPayee().getCurrency();
	}
}
