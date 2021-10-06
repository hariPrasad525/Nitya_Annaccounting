package com.nitya.accounter.web.client.ui.payroll;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public class TaxItemTable extends EditTable<ClientTAXItem> {
	
	AccounterMessages2 messages2 = Global.get().messages2();
	
	AccounterMessages messages = Global.get().messages();
	
	ClientTAXAgency parent = null;

	public TaxItemTable(ClientTAXAgency parent) {
		super();
		this.parent = parent;
	}
	
	public TaxItemTable() {
		super();
	}

	@Override
	protected ClientTAXItem getEmptyRow() {
		return new ClientTAXItem();
	}
	
	@Override
	public void setAllRows(List<ClientTAXItem> rows) {
		if(this.parent == null) {
			return;
		}
		
		List<ClientTAXItem> items = Accounter.getCompany().getTaxItems();
		Iterator<ClientTAXItem> iterator = items.iterator();
		while(iterator.hasNext()) {
			ClientTAXItem item = iterator.next();
			if(item.getTaxAgency() != this.parent.getID()) {
				iterator.remove();
			}
		}
		super.setAllRows(items);
	}

	@Override
	protected void initColumns() {
		
		this.addColumn(new CheckboxEditColumn<ClientTAXItem>() {

			@Override
			protected void onChangeValue(boolean value, ClientTAXItem row) {
				row.setActive(value);
			}

			@Override
			public String getValueAsString(ClientTAXItem row) {
				return row.isActive()+"";
			}
			
			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(messages.active());
				return columnHeader;
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		
		this.addColumn(new TextEditColumn<ClientTAXItem>() {

			@Override
			protected String getValue(ClientTAXItem row) {
				return row.getName();
			}

			@Override
			protected void setValue(ClientTAXItem row, String value) {
				row.setName(value);
			}

			@Override
			public String getValueAsString(ClientTAXItem row) {
				return row.getName();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
			
			@Override
			protected String getColumnName() {
				return messages.taxItemName();
			}
		});
		
		this.addColumn(new TextEditColumn<ClientTAXItem>() {

			@Override
			protected String getValue(ClientTAXItem row) {
				return row.getDescription();
			}

			@Override
			protected void setValue(ClientTAXItem row, String value) {
				row.setDescription(value);
			}

			@Override
			public String getValueAsString(ClientTAXItem row) {
				return row.getDescription();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
			
			@Override
			protected String getColumnName() {
				return messages.description();
			}
		});
		
		this.addColumn(new TaxAgencyColumn<ClientTAXItem>() {
			@Override
			protected void setValue(ClientTAXItem row, ClientTAXAgency newValue) {
				super.setValue(row, newValue);
				row.setTaxAgency(newValue.getID());
			}
		});
		
		this.addColumn(new AmountColumn<ClientTAXItem>(null, false) {

			@Override
			protected Double getAmount(ClientTAXItem row) {
				return row.getTaxRate();
			}

			@Override
			protected void setAmount(ClientTAXItem row, Double value) {
				row.setTaxRate(value);
			}

			@Override
			protected String getColumnName() {
				return messages.rate();
			}

			@Override
			public int getWidth() {
				return 50;
			}

			@Override
			public String getValueAsString(ClientTAXItem row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		
		this.addColumn(new DeleteColumn<ClientTAXItem>());

	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		for (ClientTAXItem row : getAllRows()) {
			if (row.getTaxAgency() == 0) {
				result.addError(row, "Tax Agency should not be empty");
				return result;
			}
		}
		return result;
	}

}

