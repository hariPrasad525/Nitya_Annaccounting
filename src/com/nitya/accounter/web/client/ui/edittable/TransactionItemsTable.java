package com.nitya.accounter.web.client.ui.edittable;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionItemsTable extends EditTable<ClientTransactionItem> {

	private ICurrencyProvider currencyProvider;

	public TransactionItemsTable() {
		addStyleName("transactionitemsTable");
	}

	public TransactionItemsTable(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected void initColumns() {
		TextEditColumn<ClientTransactionItem> textEditColumn = new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {
				String name = "";
				ClientItem item = Accounter.getCompany().getItem(row.getItem());
				if (item == null) {
					ClientAccount account = Accounter.getCompany().getAccount(
							row.getAccount());
					name = account.getName();
				} else {
					name = item.getName();
				}
				return name;
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {

			}

			@Override
			protected String getColumnName() {
				return messages.name();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.name() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(textEditColumn);

		DescriptionEditColumn textEditColumn2 = new DescriptionEditColumn() {
			@Override
			protected String getValue(ClientTransactionItem row) {
				if (row.getDescription() == null
						|| row.getDescription().isEmpty()) {
					return messages.nodescription();
				}
				return row.getDescription();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};
		this.addColumn(textEditColumn2);

		this.addColumn(new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {
				String name = "";
				ClientTAXCode taxCode = Accounter.getCompany().getTAXCode(
						row.getTaxCode());
				if (taxCode != null) {
					name = taxCode.getName() + "  " + taxCode.getSalesTaxRate();
				}
				return name;
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {

			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.taxCode();
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.taxCode() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		TextEditColumn<ClientTransactionItem> textEditColumn3 = new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {

				Double lineTotal = row.getLineTotal()
						+ (row.isTaxable() ? row.getVATfraction() : 0.0);
				if (lineTotal != null && currencyProvider != null) {
					ClientTransaction estimate = row.getTransaction();
					if (currencyProvider.getTransactionCurrency().getID() != estimate
							.getCurrency()) {
						lineTotal = row.getLineTotal()
								/ currencyProvider.getCurrencyFactor();
					}
				}
				return DataUtils.getAmountAsStrings(lineTotal);
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {
			}

			@Override
			protected String getColumnName() {
				return messages.lineTotal();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.lineTotal() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(textEditColumn3);
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}
}
