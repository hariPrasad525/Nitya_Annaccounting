package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.server.FinanceTool;

public class InvoiceListCommand extends AbstractTransactionListCommand {

	private boolean isPrinting;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		isPrinting = context.getString().contains("print");
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
		get(VIEW_BY).setDefaultValue(getMessages().open());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new ShowListRequirement<InvoicesList>(
				getMessages().invoices(), getMessages().pleaseSelect(
						getMessages().invoice()), 20) {
			@Override
			protected String onSelection(InvoicesList value) {
				if (isPrinting) {
					return "printAndSendEmail " + value.getTransactionId();
				}
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().invoices();
			}

			@Override
			protected String getEmptyString() {
				return getMessages()
						.youDontHaveAny(getMessages().invoiceList());
			}

			@Override
			protected Record createRecord(InvoicesList value) {
				Record record = new Record(value);
				record.add(getMessages().transactionName(),
						Utility.getTransactionName(value.getType()));
				record.add(getMessages().name(), value.getCustomerName());
				record.add(getMessages().date(),
						getDateByCompanyType(value.getDate()));

				Currency currency = (Currency) HibernateUtil
						.getCurrentSession().get(Currency.class,
								value.getCurrency());
				record.add(
						getMessages().total(),
						getAmountWithCurrency(value.getTotalPrice(),
								currency.getSymbol()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createInvoice");
			}

			@Override
			protected boolean filter(InvoicesList e, String name) {
				return e.getCustomerName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<InvoicesList> getLists(Context context) {
				return getInvoices(context);
			}
		});
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().open());
		list.add(getMessages().overDue());
		list.add(getMessages().voided());
		list.add(getMessages().all());
		return list;
	}

	private int checkViewType(String view) {
		if (view.equalsIgnoreCase(getMessages().open())) {
			return (VIEW_OPEN);
		} else if (view.equalsIgnoreCase(getMessages().voided())) {
			return (VIEW_VOIDED);
		} else if (view.equalsIgnoreCase(getMessages().overDue())) {
			return (VIEW_OVERDUE);
		}
		return VIEW_ALL;
	}

	private List<InvoicesList> getInvoices(Context context) {
		String viewType = get(VIEW_BY).getValue();
		try {
			return new FinanceTool().getInventoryManager().getInvoiceList(
					context.getCompany().getId(), getStartDate().getDate(),
					getEndDate().getDate(), 0, checkViewType(viewType), 0, -1);
		} catch (Exception e) {
		}
		return new ArrayList<InvoicesList>();
	}
}