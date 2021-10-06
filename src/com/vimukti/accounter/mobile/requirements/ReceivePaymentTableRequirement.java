package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CreditsAndPayments;
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
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class ReceivePaymentTableRequirement extends
		AbstractTableRequirement<ClientTransactionReceivePayment> {

	private static final String INVOICE_NO = "invoiceNo";
	private static final String INVOICE_AMOUNT = "invoiceAmount";
	private static final String PAYMENT = "receivePayment";
	private static final String AMOUNT_DUE = "amountDue";
	private static final String DUE_DATE = "BillDueDate";
	private static final String WRITE_OFF = "writeOff";
	private static final String CASH_DISCOUNT = "cashDiscount"; // IF enabled
																// discounts
	private static final String APPLIED_CREDITS = "appliedcredits";

	public ReceivePaymentTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		DateRequirement billDueDate = new DateRequirement(DUE_DATE,
				getMessages().pleaseEnter(getMessages().dueDate()),
				getMessages().dueDate(), true, true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement(INVOICE_NO, "",
				getMessages().billNo(), true, true);
		billNo.setEditable(false);
		list.add(billNo);

		CurrencyAmountRequirement originalAmount = new CurrencyAmountRequirement(
				INVOICE_AMOUNT, "", getMessages().originalAmount(), true, true) {

			@Override
			protected Currency getCurrency() {
				return ReceivePaymentTableRequirement.this.getCurrency();
			}

		};
		originalAmount.setEditable(false);
		list.add(originalAmount);

		CurrencyAmountRequirement amountDue = new CurrencyAmountRequirement(
				AMOUNT_DUE, getMessages()
						.pleaseEnter(getMessages().amountDue()), getMessages()
						.amountDue(), true, true) {

			@Override
			protected Currency getCurrency() {
				return ReceivePaymentTableRequirement.this.getCurrency();
			}

		};
		amountDue.setEditable(false);
		list.add(amountDue);

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
				ClientTransactionReceivePayment obj = ReceivePaymentTableRequirement.this.currentValue;
				if (getAccount() != null && obj != null && value != null) {
					obj.setPayment(0.0);
					obj.setCashDiscount((Double) getValue());
					obj.setDiscountAccount(getAccount().getID());
					String validatePaymentValue = validatePaymentValue(obj);
					if (validatePaymentValue == null) {
						updateTransactionReceivePayment();
					} else {
						addFirstMessage(validatePaymentValue);
						super.setValue(0.0);
					}
				}
			}
		};
		list.add(cashDiscountReq);

		CashDiscountWriteOffRequirement writeOffReq = new CashDiscountWriteOffRequirement(
				WRITE_OFF,
				getMessages().pleaseSelect(getMessages().writeOff()),
				getMessages().writeOff(), true, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				ClientTransactionReceivePayment obj = ReceivePaymentTableRequirement.this.currentValue;
				if (getAccount() != null && obj != null && value != null) {
					obj.setPayment(0.0);
					obj.setWriteOff((Double) getValue());
					obj.setWriteOffAccount(getAccount().getID());
					String validatePaymentValue = validatePaymentValue(obj);
					if (validatePaymentValue == null) {
						updateTransactionReceivePayment();
					} else {
						addFirstMessage(validatePaymentValue);
						super.setValue(0.0);
					}
				}
			}
		};

		list.add(writeOffReq);

		list.add(new ApplyCreditsRequirement(APPLIED_CREDITS, getMessages()
				.pleaseEnter(getMessages().appliedCredits()), getMessages()
				.appliedCredits()) {

			@Override
			protected Currency getCurrency() {
				return ReceivePaymentTableRequirement.this.getCurrency();
			}

			@Override
			protected double getAmountDue() {
				return ReceivePaymentTableRequirement.this.get(AMOUNT_DUE)
						.getValue();
			}

			@Override
			public List<ClientCreditsAndPayments> getTotalCredits() {
				return ReceivePaymentTableRequirement.this.getCreditsPayments();
			}

			@Override
			public List<ClientTransactionReceivePayment> getTransactionPayments() {
				return ReceivePaymentTableRequirement.this.getValue();
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				ClientTransactionReceivePayment obj = ReceivePaymentTableRequirement.this.currentValue;
				if (obj != null && value != null) {
					obj.updatePayment();
					obj.setDummyDue(obj.getAmountDue() - obj.getPayment());
					updateTransactionReceivePayment();
				}
			}

		});

		CurrencyAmountRequirement paymentReq = new CurrencyAmountRequirement(
				PAYMENT, getMessages().pleaseEnter(getMessages().payment()),
				getMessages().payment(), true, true) {

			@Override
			protected Currency getCurrency() {
				return ReceivePaymentTableRequirement.this.getCurrency();
			}

		};

		list.add(paymentReq);
	}

	protected List<ClientCreditsAndPayments> getCreditsPayments() {
		List<ClientCreditsAndPayments> clientCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		if (getPayee() == null) {
			return clientCreditsAndPayments;
		}
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = new FinanceTool()
					.getCustomerManager()
					.getCreditsAndPayments(getPayee().getID(),
							getTransactionId(), getPayee().getCompany().getId());
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				ClientCreditsAndPayments clientObject = new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class);
				clientObject.setTransactionDate(creditsAndPayments
						.getTransaction().getDate().toClientFinanceDate());
				clientCreditsAndPayments.add(clientObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCreditsAndPayments>(clientCreditsAndPayments);
	}

	protected String getFormalName() {
		return getPayee().getCurrency().getFormalName();
	}

	@Override
	protected void getRequirementsValues(ClientTransactionReceivePayment obj) {
		Double amount = get(PAYMENT).getValue();
		obj.setPayment(amount);
		Double appliedCredits = get(APPLIED_CREDITS).getValue();
		obj.setAppliedCredits(appliedCredits, true);
		CashDiscountWriteOffRequirement offRequirement = (CashDiscountWriteOffRequirement) get(CASH_DISCOUNT);
		if (offRequirement.getAccount() != null) {
			obj.setCashDiscount((Double) offRequirement.getValue());
			obj.setDiscountAccount(offRequirement.getAccount().getID());
		}

		CashDiscountWriteOffRequirement writeOffRequirement = (CashDiscountWriteOffRequirement) get(WRITE_OFF);
		if (writeOffRequirement.getAccount() != null) {
			obj.setWriteOff((Double) writeOffRequirement.getValue());
			obj.setWriteOffAccount(writeOffRequirement.getAccount().getID());
		}
	}

	public void updateAmountDue(ClientTransactionReceivePayment item) {
		double totalValue = item.getCashDiscount() + item.getWriteOff()
				+ item.getAppliedCredits() + item.getPayment();
		double amount = item.getAmountDue();
		if (!DecimalUtil.isGreaterThan(totalValue, amount)) {
			if (!DecimalUtil.isLessThan(item.getPayment(), 0.00))
				item.setDummyDue(amount - totalValue);
			else
				item.setDummyDue(amount + item.getPayment() - totalValue);

		}
	}

	private double getTotalValue(ClientTransactionReceivePayment payment) {
		double totalValue = payment.getWriteOff() + payment.getAppliedCredits()
				+ payment.getPayment();
		if (getPreferences().isTrackDiscounts()) {
			totalValue += payment.getCashDiscount();
		}
		return totalValue;
	}

	public void updatePayment(ClientTransactionReceivePayment payment) {
		payment.setPayment(0);
		double paymentValue = payment.getAmountDue() - getTotalValue(payment);
		payment.setPayment(paymentValue);
		updateAmountDue(payment);
	}

	@Override
	protected void setRequirementsDefaultValues(
			ClientTransactionReceivePayment obj) {
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate(obj.getDueDate()));
		get(INVOICE_NO).setDefaultValue(obj.getNumber());
		get(INVOICE_AMOUNT).setDefaultValue(obj.getInvoiceAmount());
		get(AMOUNT_DUE).setDefaultValue(obj.getAmountDue());
		get(PAYMENT).setDefaultValue(obj.getAmountDue());
		CashDiscountWriteOffRequirement offRequirement = (CashDiscountWriteOffRequirement) get(CASH_DISCOUNT);
		offRequirement.setAccount((Account) CommandUtils.getServerObjectById(
				obj.getDiscountAccount(), AccounterCoreType.ACCOUNT));
		offRequirement.setValue(obj.getCashDiscount());

		CashDiscountWriteOffRequirement writeOffRequirement = (CashDiscountWriteOffRequirement) get(WRITE_OFF);
		writeOffRequirement.setAccount((Account) CommandUtils
				.getServerObjectById(obj.getWriteOffAccount(),
						AccounterCoreType.ACCOUNT));
		writeOffRequirement.setValue(obj.getWriteOff());

		ApplyCreditsRequirement applyCreditsRequirement = (ApplyCreditsRequirement) get(APPLIED_CREDITS);
		applyCreditsRequirement.setTransactionCreditsAndPayments(obj
				.getTransactionCreditsAndPayments());

		get(APPLIED_CREDITS).setValue(obj.getAppliedCredits());
	}

	@Override
	protected ClientTransactionReceivePayment getNewObject() {
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionReceivePayment t) {
		String formalName;
		if (getPreferences().isEnableMultiCurrency()) {
			formalName = getCurrency().getFormalName();
		} else {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
		}
		Record record = new Record(t);
		record.add(getMessages().dueDate(), t.getDueDate());
		record.add(getMessages().invoiceNo(), t.getNumber());
		record.add(getMessages().invoiceAmount() + "(" + formalName + ")",
				t.getInvoiceAmount());
		record.add(getMessages().amountDue() + "(" + formalName + ")",
				t.getAmountDue());
		if (getPreferences().isTrackDiscounts()) {
			record.add(getMessages().cashDiscount(), t.getCashDiscount());
		}
		record.add(getMessages().writeOff(), t.getWriteOff());

		record.add(getMessages().appliedCredits(), t.getAppliedCredits());
		record.add(getMessages().payment() + "(" + formalName + ")",
				t.getPayment());
		return record;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().dueForPayment());
	}

	@Override
	protected Record createRecord(ClientTransactionReceivePayment t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().dueForPayment());
	}

	@Override
	protected boolean contains(List<ClientTransactionReceivePayment> oldValues,
			ClientTransactionReceivePayment t) {
		for (ClientTransactionReceivePayment receivePaymentTransactionList : oldValues) {
			if (t.getInvoice() == receivePaymentTransactionList.getInvoice()) {
				return true;
			}
		}
		return false;
	}

	protected abstract Payee getPayee();

	protected abstract long getTransactionId();

	protected Currency getCurrency() {
		return getPayee().getCurrency();
	}

	public void customerSelected() {
		((ApplyCreditsRequirement) get(APPLIED_CREDITS))
				.addCreditsAndPayments();
	}

	public List<ClientCreditsAndPayments> getCreditsAndPayments() {
		return ((ApplyCreditsRequirement) get(APPLIED_CREDITS))
				.getCreditsAndPayments();
	}

	protected void updateTransactionReceivePayment() {
		ClientTransactionReceivePayment obj = ReceivePaymentTableRequirement.this.currentValue;
		if (obj != null) {
			CashDiscountWriteOffRequirement offRequirement = (CashDiscountWriteOffRequirement) get(CASH_DISCOUNT);
			if (offRequirement.getAccount() != null
					&& offRequirement.getValue() != null) {
				obj.setPayment(0.0);
				obj.setCashDiscount((Double) offRequirement.getValue());
				obj.setDiscountAccount(offRequirement.getAccount().getID());
				if (validatePaymentValue(obj) == null) {
					updatePayment(obj);
				} else {
					obj.setCashDiscount(0.0D);
					updatePayment(obj);
				}
			} else {
				obj.setCashDiscount(0.0);
				obj.setDiscountAccount(0);
			}

			CashDiscountWriteOffRequirement writeoffRequirement = (CashDiscountWriteOffRequirement) get(WRITE_OFF);
			if (writeoffRequirement.getAccount() != null
					&& writeoffRequirement.getValue() != null) {
				obj.setPayment(0.0);
				obj.setWriteOff((Double) writeoffRequirement.getValue());
				obj.setWriteOffAccount(writeoffRequirement.getAccount().getID());
				if (validatePaymentValue(obj) == null) {
					updatePayment(obj);
				} else {
					obj.setWriteOff(0.0D);
					updatePayment(obj);
				}
			} else {
				obj.setWriteOff(0.0);
				obj.setWriteOffAccount(0);
			}

			Double appliedCredits = get(APPLIED_CREDITS).getValue();
			if (appliedCredits != null) {
				obj.setAppliedCredits(appliedCredits, true);
			}

			obj.updatePayment();
			// obj.setDummyDue(obj.getAmountDue() - obj.getPayment());
			updatePayment(obj);
			ReceivePaymentTableRequirement.this.get(PAYMENT).setValue(
					obj.getPayment());
		}
	}

	protected String validatePaymentValue(
			ClientTransactionReceivePayment selectedObject) {
		double totalValue = getTotalValue(selectedObject);
		return isValidReceive_Payment(selectedObject.getAmountDue(), totalValue);

	}

	public String isValidReceive_Payment(double amountDue, double totalValue) {
		if (DecimalUtil.isLessThan(totalValue, 0.00)) {
			return getMessages().valueCannotBe0orlessthan0(
					getMessages().amount());
		} else if (DecimalUtil.isGreaterThan(totalValue, amountDue)) {
			return getMessages().receiveAmountPayDue();
		}
		return null;
	}
}
