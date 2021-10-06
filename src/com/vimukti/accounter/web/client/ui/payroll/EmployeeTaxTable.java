package com.vimukti.accounter.web.client.ui.payroll;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEmployeeTax;
import com.vimukti.accounter.web.client.core.ClientListBox;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;

public class EmployeeTaxTable extends EditTable<ClientEmployeeTax> {
	
	AccounterMessages2 messages2 = Global.get().messages2();

	public EmployeeTaxTable() {
		super();
		addEmptyRecords();
	}

	@Override
	protected ClientEmployeeTax getEmptyRow() {
		return new ClientEmployeeTax();
	}
	
	private Map<Integer, String> getFilingStatusMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(ClientEmployeeTax.FILING_STATUS_SINGLE, messages2.taxFilingStatusSingle());
		map.put(ClientEmployeeTax.FILING_STATUS_MARRIED, messages2.taxFilingStatusMarried());
		map.put(ClientEmployeeTax.FILING_STATUS_MARRIED_JOINTLY, messages2.taxFilingStatusMarriedJointly());
		map.put(ClientEmployeeTax.FILING_STATUS_MARRIED_SEPARATE, messages2.taxFilingStatusMarriedSeparate());
		return map;
	}
	
	private Map<Integer, String> getTaxResidencyType() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(ClientEmployeeTax.TAX_RESIDENCY, messages2.taxResidencyTypeResident());
		map.put(ClientEmployeeTax.TAX_RESIDENCY_NON, messages2.taxResidencyTypeNonResident());
		return map;
	}

	@Override
	protected void initColumns() {
		
		this.addColumn(new TaxAgencyColumn<ClientEmployeeTax>() {
			@Override
			protected void setValue(ClientEmployeeTax row, ClientTAXAgency newValue) {
				super.setValue(row, newValue);
				//row.setTaxAgency(newValue.getID());
			}
		});
		
		this.addColumn(new CustomComboColumn<ClientEmployeeTax>(getFilingStatusMap()) {
			@Override
			protected String getColumnName() {
				return messages2.taxFilingStatus();
			}
			
			@Override
			protected void setValue(ClientEmployeeTax row, ClientListBox newValue) {
				super.setValue(row, newValue);
				row.setTaxFilingStatus((int) newValue.getID());
			}
		});

		this.addColumn(new CustomComboColumn<ClientEmployeeTax>(getTaxResidencyType()) {
			@Override
			protected String getColumnName() {
				return messages2.taxResidencyType();
			}
			
			@Override
			protected void setValue(ClientEmployeeTax row, ClientListBox newValue) {
				super.setValue(row, newValue);
				row.setTaxResidencyType((int) newValue.getID());
			}
		});
		
		this.addColumn(new CheckboxEditColumn<ClientEmployeeTax>() {

			@Override
			protected void onChangeValue(boolean value, ClientEmployeeTax row) {
				row.setIsSSNTaxable(value);
			}

			@Override
			public String getValueAsString(ClientEmployeeTax row) {
				return row.isSSNTaxable()+"";
			}
			
			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(messages2.isSSNTaxable());
				return columnHeader;
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		

		this.addColumn(new CheckboxEditColumn<ClientEmployeeTax>() {

			@Override
			protected void onChangeValue(boolean value, ClientEmployeeTax row) {
				row.setMedicareTaxable(value);
			}

			@Override
			public String getValueAsString(ClientEmployeeTax row) {
				return row.isMedicareTaxable()+"";
			}
			
			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(messages2.medicareTaxable());
				return columnHeader;
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		

		this.addColumn(new CheckboxEditColumn<ClientEmployeeTax>() {

			@Override
			protected void onChangeValue(boolean value, ClientEmployeeTax row) {
				row.setTaxUnemployement(value);
			}
			
			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(messages2.taxUnemployement());
				return columnHeader;
			}

			@Override
			public String getValueAsString(ClientEmployeeTax row) {
				return row.isTaxUnemployement()+"";
			}
			

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new AmountColumn<ClientEmployeeTax>(null, false) {

			@Override
			protected Double getAmount(ClientEmployeeTax row) {
				return (double) row.getTaxallowences();
			}

			@Override
			protected void setAmount(ClientEmployeeTax row, Double value) {
				row.setTaxallowences(0);
			}

			@Override
			protected String getColumnName() {
				return messages2.taxAllowances();
			}

			@Override
			public int getWidth() {
				return 50;
			}

			@Override
			public String getValueAsString(ClientEmployeeTax row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		
		this.addColumn(new AmountColumn<ClientEmployeeTax>(null, false) {

			@Override
			protected Double getAmount(ClientEmployeeTax row) {
				return row.getAdditionalAmount();
			}

			@Override
			protected void setAmount(ClientEmployeeTax row, Double value) {
				row.setAdditionalAmount(value);
			}

			@Override
			protected String getColumnName() {
				return messages2.additionalAmount();
			}

			@Override
			public int getWidth() {
				return 50;
			}

			@Override
			public String getValueAsString(ClientEmployeeTax row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		
		this.addColumn(new DeleteColumn<ClientEmployeeTax>());

	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		for (ClientEmployeeTax row : getAllRows()) {
			if (row.getTaxFilingStatus() == 0) {
				result.addError(row, "Filing Status should not be selected");
				return result;
			}
		}
		return result;
	}

}
