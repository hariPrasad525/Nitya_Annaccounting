package com.vimukti.accounter.admin.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.Action;

public abstract class AdminAbstractView<T> extends FlowPanel {

	abstract public void init();

	private Action action;
	protected AccounterMessages messages = Global.get().messages();
	/**
	 * Flag, to Determine, whether in Edit Mode or Create mode.
	 */

	protected Object data;

	private AdminViewManger manager;

	public Object getData() {
		return data;
	}

	public void setData(Object input) {
		this.data = input;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	@Override
	public String toString() {
		if (action == null)
			return super.toString();
		else
			return action.getText();
	}

	public boolean shouldSaveInHistory() {
		return false;
	}

	public boolean isAListView() {
		return false;
	}

	public void disableUserEntry() {

	}

	/**
	 * return String values for Select box in Grid,
	 * 
	 * @param colIndex
	 *            is selectBox index in row
	 */
	public String[] getGridSelectBoxValues(int colIndex) {
		return null;
	}

	/**
	 * called when values has been changed of Selectbox in grid
	 * 
	 * @param colIndex
	 *            is selectBox index in row
	 */
	public void onSelectBoxValueChange(int colIndex, Object value) {
	}

	/**
	 * call this method to set focus in View
	 */
	public abstract void setFocus();

	public abstract void fitToSize(int height, int width);

	public abstract void onEdit();

	public abstract void printPreview();

	public abstract void print();

	public void exportToCsv() {
	}

	public void initData() {
		// TODO Auto-generated method stub

	}

	public AdminViewManger getManager() {
		return manager;
	}

	public void setManager(AdminViewManger manager) {
		this.manager = manager;
	}

	public ClientCompanyPreferences getPreferences() {
		// if (preferences != null)
		// return preferences;
		// else
		// return Accounter.getCompany().getPreferences();
		return null;
	}

	public void cancel() {
		// TODO Auto-generated method stub

	}

	public abstract Widget getGrid();

}
