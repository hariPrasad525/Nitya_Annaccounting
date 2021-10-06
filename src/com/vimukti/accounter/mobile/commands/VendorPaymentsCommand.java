package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class VendorPaymentsCommand extends AbstractTransactionListCommand {
	private String commandString = null;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		commandString = context.getCommandString();
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().notIssued());
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new ShowListRequirement<PaymentsList>(getMessages()
				.payeePaymentList(Global.get().Vendor()), "", 20) {
			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// PaymentsList value) {
			// commandList.add(new UserCommand("update Payment", String
			// .valueOf(value.getTransactionId())));
			// }

			@Override
			protected String onSelection(PaymentsList value) {
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {
				if (commandString.contains("Vendor")) {
					return getMessages()
							.payeePaymentList(Global.get().Vendor());
				} else {
					return getMessages().paymentsList();
				}

			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(PaymentsList p) {
				Record payment = new Record(p);
				payment.add(getMessages().paymentDate(),
						getDateByCompanyType(p.getPaymentDate()));
				payment.add(getMessages().paymentNo(), p.getPaymentNumber());
				payment.add(getMessages().status(),
						Utility.getStatus(p.getType(), p.getStatus()));
				payment.add(getMessages().issueDate(),
						getDateByCompanyType(p.getIssuedDate()));
				payment.add(getMessages().name(), p.getName());
				payment.add(getMessages().transactionName(),
						Utility.getTransactionName(p.getType()));
				payment.add(getMessages().paymentMethod(),
						p.getPaymentMethodName());
				payment.add(getMessages().checkNo(), p.getCheckNumber());

				Currency currency = (Currency) HibernateUtil
						.getCurrentSession().get(Currency.class,
								p.getCurrency());
				payment.add(
						getMessages().amountPaid(),
						getAmountWithCurrency(p.getAmountPaid(),
								currency.getSymbol()));
				return payment;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				if (commandString.contains("Vendor")) {
					list.add("addANewVendorPayment");
				} else {
					list.add("addaNewPayment");
				}

			}

			@Override
			protected boolean filter(PaymentsList e, String name) {
				return e.getName().startsWith(name)
						|| e.getPaymentNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<PaymentsList> getLists(Context context) {
				return getListData(context);
			}
		});
	}

	/**
	 * get payments
	 * 
	 * @param context
	 * 
	 * @return {@link List<paymentsList>}
	 */
	protected List<PaymentsList> getListData(Context context) {
		String currentView = get(VIEW_BY).getValue();
		FinanceTool tool = new FinanceTool();
		List<PaymentsList> result = new ArrayList<PaymentsList>();
		List<PaymentsList> paymentsLists = null;
		try {
			if (commandString.contains("vendor")
					|| commandString.contains("Vendor")) {
				paymentsLists = tool.getVendorManager().getVendorPaymentsList(
						context.getCompany().getId(), getStartDate().getDate(),
						getEndDate().getDate(), 0, -1,
						checkViewType(currentView));
			} else {
				paymentsLists = tool.getCustomerManager().getPaymentsList(
						context.getCompany().getId(), getStartDate().getDate(),
						getEndDate().getDate(), 0, -1,
						checkViewType(currentView));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (paymentsLists != null) {
			for (PaymentsList list : paymentsLists) {
				if (currentView.equals(getMessages().notIssued())) {
					if (((Utility.getTransactionStatus(list.getType(),
							list.getStatus()).equalsIgnoreCase(getMessages()
							.notIssued())) && !(list.isVoided()))) {
						result.add(list);
					}
				} else if (currentView.equals(getMessages().issued())) {
					if (Utility.getTransactionStatus(list.getType(),
							list.getStatus()).equalsIgnoreCase(
							getMessages().issued())
							&& !list.isVoided()) {
						result.add(list);
					}
				} else if (currentView.equalsIgnoreCase(getMessages().voided())) {
					if (list.isVoided()
							&& list.getStatus() != ClientTransaction.STATUS_DELETED) {
						result.add(list);
					}
				} else if (currentView.equalsIgnoreCase(getMessages().all())) {
					result.add(list);
				}
			}
			return result;
		}
		return null;

	}

	private int checkViewType(String viewSelect) {
		if (viewSelect.equalsIgnoreCase(getMessages().notIssued())) {
			return Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		} else if (viewSelect.equalsIgnoreCase(getMessages().issued())) {
			return Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (viewSelect.equalsIgnoreCase(getMessages().voided())) {
			return Transaction.VIEW_VOIDED;
		} else if (viewSelect.equalsIgnoreCase(getMessages().all())) {
			return TYPE_ALL;
		}
		return TYPE_ALL;
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().notIssued());
		list.add(getMessages().issued());
		list.add(getMessages().voided());
		list.add(getMessages().all());
		return list;
	}
}
