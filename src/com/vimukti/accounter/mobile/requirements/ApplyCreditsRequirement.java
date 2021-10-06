package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;

public abstract class ApplyCreditsRequirement extends MultiRequirement<Double> {

	private static final String CREDITS_PAYMENTS = "creditsandpayments";
	private static final String AMOUNT_TO_USE = "creditspaymentsamounttouse";
	private static final String AMOUNT_DUE = "creditspaymentsamountDue";
	private List<ClientTransactionCreditsAndPayments> initialCredits = new ArrayList<ClientTransactionCreditsAndPayments>();
	private List<ClientCreditsAndPayments> totalCredits = new ArrayList<ClientCreditsAndPayments>();
	private boolean fullyApplied;

	public ApplyCreditsRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, true, true);
		setValue(0.0);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		List<ClientCreditsAndPayments> totalCredits2 = getTotalCredits();
		if (!totalCredits2.isEmpty()) {
			return super.run(context, makeResult, list, actions);
		}
		return null;
	}

	@Override
	protected void setDefaultValues() {
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		CurrencyAmountRequirement amountDue = new CurrencyAmountRequirement(
				AMOUNT_DUE, getMessages()
						.pleaseEnter(getMessages().amountDue()), getMessages()
						.amountDue(), false, true) {
			@Override
			protected Currency getCurrency() {
				return ApplyCreditsRequirement.this.getCurrency();
			}
		};
		amountDue.setEditable(false);
		list.add(amountDue);

		list.add(new ShowListRequirement<ClientCreditsAndPayments>(
				CREDITS_PAYMENTS, getMessages().pleaseSelect(
						getMessages().creditsPayments()), 40) {

			@Override
			protected String onSelection(ClientCreditsAndPayments value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().creditsPayments());
			}

			@Override
			protected Record createRecord(ClientCreditsAndPayments value) {
				Record record = new Record(value);
				record.add(getMessages().memo(), value.getMemo());
				record.add(getMessages().creditAmount(),
						value.getCreditAmount());
				record.add(getMessages().balance(), value.getRemaoningBalance());
				record.add(getMessages().amountToUse(), value.getAmtTouse());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected boolean filter(ClientCreditsAndPayments e, String name) {
				return false;
			}

			@Override
			protected List<ClientCreditsAndPayments> getLists(Context context) {
				return totalCredits;
			}
		});

		list.add(new CurrencyAmountRequirement(AMOUNT_TO_USE, getMessages()
				.pleaseEnter(getMessages().amountToUse()), getMessages()
				.amountToUse(), true, true) {

			@Override
			protected Currency getCurrency() {
				return ApplyCreditsRequirement.this.getCurrency();
			}

			@Override
			public void setValue(Object value) {
				if (value != null) {
					checkTotalAmount((Double) value, totalCredits);
					String onOK = onOK((Double) value, totalCredits);
					if (onOK != null) {
						addFirstMessage(onOK);
						return;
					}
				}
				super.setValue(value);
			}
		});
	}

	protected abstract Currency getCurrency();

	private double getTotalUnuseCreditAmount(
			List<ClientCreditsAndPayments> records) {
		double totalUnusedCreditAmount = 0.0;
		for (ClientCreditsAndPayments crd : records) {
			totalUnusedCreditAmount += crd.getRemaoningBalance()
					+ crd.getAmtTouse();
		}
		return totalUnusedCreditAmount;
	}

	private void initialAmountUse(List<ClientCreditsAndPayments> list) {
		double amount = 0.0d;
		double totalCreditAmount = getTotalUnuseCreditAmount(list);
		double appliedCredits = 0.0d;

		if (totalCreditAmount > getAmountDue()) {
			amount = getAmountDue();
		} else {
			amount = totalCreditAmount;
		}
		if (appliedCredits == 0) {
			getRequirement(AMOUNT_TO_USE).setValue(amount);
		} else {
			getRequirement(AMOUNT_TO_USE).setValue(appliedCredits);
		}
		getRequirement(AMOUNT_DUE).setValue(getAmountDue());
	}

	protected abstract double getAmountDue();

	private void checkTotalAmount(Double totalAmount,
			List<ClientCreditsAndPayments> credits) {
		// if (totalAmount > amountDue) {
		// return getMessages().amountToUseMustLessthanTotal();
		// }
		for (ClientCreditsAndPayments credit : credits) {
			credit.setRemaoningBalance(credit.getRemaoningBalance()
					+ credit.getAmtTouse());
		}
		double amountNeeded = totalAmount;
		for (ClientCreditsAndPayments credit : credits) {
			double balance = credit.getRemaoningBalance();
			double amountToUse = Math.min(amountNeeded, balance);
			credit.setAmtTouse(amountToUse);
			credit.setRemaoningBalance(credit.getRemaoningBalance()
					- amountToUse);
			amountNeeded -= amountToUse;
		}
		fullyApplied = amountNeeded == 0;
	}

	@Override
	protected Result onFinish(Context context) {
		Double amount = getRequirement(AMOUNT_TO_USE).getValue();
		this.initialCredits.clear();
		for (ClientCreditsAndPayments ccap : totalCredits) {
			if (ccap.getAmtTouse() > 0) {
				ClientTransactionCreditsAndPayments ctcap = new ClientTransactionCreditsAndPayments();
				ctcap.setAmountToUse(ccap.getAmtTouse());
				ctcap.setCreditsAndPayments(ccap.getID());
				initialCredits.add(ctcap);
			}
			ccap.setBalance(ccap.getRemaoningBalance());
		}
		setValue(amount);
		return null;
	}

	@Override
	protected String getDisplayValue() {
		return String.valueOf((Double) getValue());
	}

	protected String onOK(Double totalAmount,
			List<ClientCreditsAndPayments> credits) {
		if (credits.isEmpty() && totalAmount > 0) {
			return (getMessages().noCreditsToApply());
		} else if (totalAmount > getAmountDue()) {
			return (getMessages().amountToUseMustLessthanTotal());
		} else if (!fullyApplied) {
			return getMessages().amountMoreThanCredits();
		}
		return null;
	}

	public void setTransactionCreditsAndPayments(
			List<ClientTransactionCreditsAndPayments> creditsAndPayments) {
		for (ClientCreditsAndPayments ccap : this.totalCredits) {
			double balance = ccap.getBalance();
			double usedAmount = 0.0;
			for (ClientTransactionCreditsAndPayments ctcap : creditsAndPayments) {
				if (ctcap.getCreditsAndPayments() == ccap.getID()) {
					usedAmount += ctcap.getAmountToUse();
				}
			}
			ccap.setRemaoningBalance(balance);
			ccap.setAmtTouse(usedAmount);
		}
		this.initialCredits = creditsAndPayments;
		initialAmountUse(totalCredits);
	}

	public List<ClientCreditsAndPayments> getCreditsAndPayments() {
		return totalCredits;
	}

	public void addCreditsAndPayments() {
		List<ClientCreditsAndPayments> list = getTotalCredits();
		this.totalCredits = list;
		if (getTransactionPayments() != null) {
			for (ClientTransactionReceivePayment ctrp : getTransactionPayments()) {
				for (ClientCreditsAndPayments ccap : totalCredits) {
					double balance = ccap.getBalance();
					double usedAmount = 0.0;
					for (ClientTransactionCreditsAndPayments ctcap : ctrp
							.getTransactionCreditsAndPayments()) {
						if (ctcap.getCreditsAndPayments() == ccap.getID()) {
							usedAmount += ctcap.getAmountToUse();
						}
					}
					ccap.setBalance(balance + usedAmount);
					ccap.setRemaoningBalance(balance);
					ccap.setAmtTouse(usedAmount);
				}
				ctrp.getTransactionCreditsAndPayments().clear();
			}
		}

		if (getPayBillPayments() != null) {
			for (ClientTransactionPayBill ctpb : getPayBillPayments()) {
				for (ClientCreditsAndPayments ccap : totalCredits) {
					double balance = ccap.getBalance();
					double usedAmount = 0.0;
					for (ClientTransactionCreditsAndPayments ctcap : ctpb
							.getTransactionCreditsAndPayments()) {
						if (ctcap.getCreditsAndPayments() == ccap.getID()) {
							usedAmount += ctcap.getAmountToUse();
						}
					}
					ccap.setBalance(balance + usedAmount);
				}
				ctpb.getTransactionCreditsAndPayments().clear();
			}

		}
	}

	public List<ClientTransactionReceivePayment> getTransactionPayments() {
		return null;
	}

	public List<ClientTransactionPayBill> getPayBillPayments() {
		return null;
	}

	public List<ClientTransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return initialCredits;
	}

	public abstract List<ClientCreditsAndPayments> getTotalCredits();
}
