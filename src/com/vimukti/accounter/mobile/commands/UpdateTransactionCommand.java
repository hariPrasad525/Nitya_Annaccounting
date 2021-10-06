package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ForwardRequirement;
import com.vimukti.accounter.mobile.requirements.ListRequirement;

public class UpdateTransactionCommand extends AbstractCommand {

	private static final String TRANSACTION = "transaction";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long transactionId = Long.valueOf(context.getString());
		Session session = context.getHibernateSession();
		Transaction transaction = (Transaction) session.get(Transaction.class,
				transactionId);
		get(TRANSACTION).setValue(transaction);
		context.setString("");
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ListRequirement<Transaction>(TRANSACTION, getMessages()
				.pleaseEnter("transaction name"), "Transaction", false, true,
				null) {

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected Record createRecord(Transaction value) {
				Record record = new Record(value);

				record.add(getMessages().type(), value.getType());
				record.add(getMessages().date(), value.getDate());
				record.add(getMessages().netAmount(), value.getNetAmount());
				record.add(getMessages().total(), value.getTotal());

				return null;
			}

			@Override
			protected String getDisplayValue(Transaction value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getSelectString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected boolean filter(Transaction e, String name) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			protected List<Transaction> getLists(Context context) {

				return null;
			}
		});
		list.add(new ForwardRequirement() {

			@Override
			public String getNextCommand() {
				Transaction transaction = get(TRANSACTION).getValue();
				int type = transaction.getType();
				if (transaction.getType() == Transaction.TYPE_ESTIMATE) {
					Estimate estimate = (Estimate) transaction;
					if (estimate.getEstimateType() == Estimate.QUOTES) {
						return "updateQuote " + transaction.getID();
					} else if (estimate.getEstimateType() == Estimate.CHARGES) {
						return "updateCharge " + transaction.getID();
					} else if (estimate.getEstimateType() == Estimate.CREDITS) {
						return "updateCredit " + transaction.getID();
					} else if (estimate.getEstimateType() == Estimate.SALES_ORDER) {
						return "updateSalesOrder " + transaction.getID();
					}

				}
				String transactionName = Utility.getTransactionName(type);
				String replace = transactionName.replace(" ", "");
				return "update" + replace + " " + transaction.getID();
			}
		});
	}
}
