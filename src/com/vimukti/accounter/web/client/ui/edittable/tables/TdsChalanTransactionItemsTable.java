package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class TdsChalanTransactionItemsTable extends
		EditTable<ClientTDSTransactionItem> {

	private ICurrencyProvider currencyProvider;
	private int formType;

	public TdsChalanTransactionItemsTable(int formType) {
		super();
		this.formType = formType;
	}

	@Override
	protected void initColumns() {

		this.addColumn(new CheckboxEditColumn<ClientTDSTransactionItem>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTDSTransactionItem row) {
				updateNonEditableFields();
			}

			@Override
			public IsWidget getWidget(
					RenderContext<ClientTDSTransactionItem> context) {
				CheckBox box = (CheckBox) super.getWidget(context);
				box.setValue(isInViewMode());
				return box;
			}

			@Override
			public String getValueAsString(ClientTDSTransactionItem row) {
				// TODO Auto-generated method stub
				return "TODO check later";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		TextEditColumn<ClientTDSTransactionItem> vendorNameColumn = new TextEditColumn<ClientTDSTransactionItem>() {

			@Override
			protected String getValue(ClientTDSTransactionItem row) {
				if (formType != ClientTDSChalanDetail.Form27EQ) {
					return Accounter.getCompany().getVendor(row.getVendor())
							.getName();
				} else {
					return Accounter.getCompany().getCustomer(row.getVendor())
							.getName();
				}

			}

			@Override
			protected void setValue(ClientTDSTransactionItem row, String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			protected String getColumnName() {
				return messages.deducteeName();
			}

			@Override
			public String getValueAsString(ClientTDSTransactionItem row) {
				return messages.deducteeName() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(vendorNameColumn);

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.amountPaidOrCredited();
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getTotalAmount();
			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getValueAsString(ClientTDSTransactionItem row) {
				return messages.amountPaidOrCredited() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}

		});

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.tdsAmount();
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getTdsAmount();

			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getValueAsString(ClientTDSTransactionItem row) {
				return messages.tdsAmount() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}

		});

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return true;
			}

			@Override
			protected String getColumnName() {
				return messages.surchageAmount();
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getSurchargeAmount();
			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				row.setSurchargeAmount(value);

				row.setTotalTax(row.getTdsAmount() + value + row.getEduCess());
				getTable().update(row);
				updateNonEditableFields();
			}

			@Override
			public String getValueAsString(ClientTDSTransactionItem row) {
				return messages.surchageAmount() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		});

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return true;
			}

			@Override
			protected String getColumnName() {
				return messages.educationCess();
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getEduCess();
			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {
				row.setEduCess(value);
				row.setTotalTax(row.getTdsAmount() + value
						+ row.getSurchargeAmount());
				getTable().update(row);
				updateNonEditableFields();
			}

			@Override
			public String getValueAsString(ClientTDSTransactionItem row) {
				return row.getEduCess() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		});

		this.addColumn(new AmountColumn<ClientTDSTransactionItem>(
				currencyProvider, false) {

			@Override
			public int getWidth() {
				return 133;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.totalTax();
			}

			@Override
			protected Double getAmount(ClientTDSTransactionItem row) {
				return row.getTotalTax();
			}

			@Override
			protected void setAmount(ClientTDSTransactionItem row, Double value) {

			}

			@Override
			public String getValueAsString(ClientTDSTransactionItem row) {
				return messages.totalTax() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

		});
		TextEditColumn<ClientTDSTransactionItem> dateColumn = new TextEditColumn<ClientTDSTransactionItem>() {

			@Override
			protected String getValue(ClientTDSTransactionItem row) {
				ClientFinanceDate date = new ClientFinanceDate(
						row.getTransactionDate());
				return date.toString();
			}

			@Override
			protected void setValue(ClientTDSTransactionItem row, String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			protected String getColumnName() {
				return messages.dateofPayment();
			}

			@Override
			public String getValueAsString(ClientTDSTransactionItem row) {
				return messages.dateofPayment() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		};
		this.addColumn(dateColumn);

	}

	public abstract void updateNonEditableFields();

	public List<ClientTDSTransactionItem> getSelectedRecords() {
		return getSelectedRecords(0);
	}

	public void removeAllRows() {
		List<ClientTDSTransactionItem> allRows = getAllRows();
		for (int index = 0; index < allRows.size(); index++) {
			remove(index + 1);
		}
	}
}
