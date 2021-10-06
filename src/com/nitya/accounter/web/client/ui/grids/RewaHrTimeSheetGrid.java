package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.core.ClientRewaHrTimeSheet;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public class RewaHrTimeSheetGrid extends EditTable<ClientRewaHrTimeSheet> {
	
	
	public RewaHrTimeSheetGrid(boolean isMultiSelectionEnable) {
		super();
	}

	@Override
	protected void initColumns() {

		TextEditColumn<ClientRewaHrTimeSheet> dayColumn = new TextEditColumn<ClientRewaHrTimeSheet>() {
			
			
			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages.date();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

			
			@Override
			protected String getValue(ClientRewaHrTimeSheet row) {
				
				return row.getReportedDate();
			}

			@Override
			protected void setValue(ClientRewaHrTimeSheet row, String value) {
//				value = row.getReportedDate().toString();
				
			}

			@Override
			public String getValueAsString(ClientRewaHrTimeSheet row) {
				// TODO Auto-generated method stub
				return null;
			}
	
		};
		this.addColumn(dayColumn);
		TextEditColumn<ClientRewaHrTimeSheet> descriptionColumn = new TextEditColumn<ClientRewaHrTimeSheet>() {
			
			
			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages.description();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

			
			@Override
			protected String getValue(ClientRewaHrTimeSheet row) {
				// TODO Auto-generated method stub
				return row.getDescription();
			}

			@Override
			protected void setValue(ClientRewaHrTimeSheet row, String value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getValueAsString(ClientRewaHrTimeSheet row) {
				// TODO Auto-generated method stub
				return null;
			}
	
		};
		this.addColumn(descriptionColumn);
		
		TextEditColumn<ClientRewaHrTimeSheet> hoursColumn = new TextEditColumn<ClientRewaHrTimeSheet>() {
			
			
			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages2.hours();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

			
			@Override
			protected String getValue(ClientRewaHrTimeSheet row) {
				
				return row.getDuration()+"";
			}

			@Override
			protected void setValue(ClientRewaHrTimeSheet row, String value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getValueAsString(ClientRewaHrTimeSheet row) {
				// TODO Auto-generated method stub
				return row.getDuration()+"";
			}
	
		};
		this.addColumn(hoursColumn);
		
	TextEditColumn<ClientRewaHrTimeSheet> extraHoursColumn = new TextEditColumn<ClientRewaHrTimeSheet>() {
			
			
			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return "extra "+ messages2.hours();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

			
			@Override
			protected String getValue(ClientRewaHrTimeSheet row) {
				// TODO Auto-generated method stub
				return row.getExtraDuration()+"";
			}

			@Override
			protected void setValue(ClientRewaHrTimeSheet row, String value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getValueAsString(ClientRewaHrTimeSheet row) {
				// TODO Auto-generated method stub
				return null;
			}
	
		};
		this.addColumn(extraHoursColumn);
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
