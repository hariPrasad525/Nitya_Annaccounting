package com.vimukti.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class EditColumn<R> {

	private EditTable<R> table;
	private Label columnHeader;

	protected AccounterMessages messages = Global.get().messages();

	protected AccounterMessages2 messages2 = Global.get().messages2();
	
	public boolean onChange(R row) {
		return false;

	}

	/**
	 * return width of this cell, or -1 to take available space
	 * 
	 * @return
	 */
	public abstract int getWidth();

	public abstract String getValueAsString(R row);

	public abstract int insertNewLineNumber();

	public IsWidget getHeader() {
		if (columnHeader == null) {
			columnHeader = new Label(getColumnName());
		}
		return columnHeader;
	}

	protected String getColumnName() {
		return "";
	}

	public abstract void render(IsWidget widget, RenderContext<R> context);

	public abstract IsWidget getWidget(RenderContext<R> context);

	public EditTable<R> getTable() {
		return table;
	}

	public void setTable(EditTable<R> editTableImpl) {
		this.table = editTableImpl;
	}

	public ClientCompanyPreferences getPreferences() {
		return Accounter.getCompany().getPreferences();
	}

	protected void updateFromGUI(IsWidget widget, R row) {

	}

	public void updateHeader() {
		if (getColumnName().length() > 0) {
			IsWidget header = getHeader();
			columnHeader = (Label) header;
			columnHeader.setText(getColumnName());
		}
	}

	public int getColumnSpan() {
		return 1;
	}

	public boolean isPrimaryColumn() {
		return false;
	}

	public void onValueChange(R row) {
		if (isPrimaryColumn()) {
			List<R> allRows = table.getAllRows();
			// If this is last row
			if (allRows.indexOf(row) == allRows.size() - 1) {
				table.addEmptyRowAtLast();
			}
		}
	}
}
