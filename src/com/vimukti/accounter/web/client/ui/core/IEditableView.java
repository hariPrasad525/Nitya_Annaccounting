package com.vimukti.accounter.web.client.ui.core;

public interface IEditableView {
	/**
	 * Returns true if current user can edit data displayed in this view. Used
	 * to enable Edit button
	 * 
	 * @return
	 */
	public boolean canEdit();
	
	public void onSave(boolean reopen);
	
	public void onEdit();
	
	public boolean isDirty();
}
