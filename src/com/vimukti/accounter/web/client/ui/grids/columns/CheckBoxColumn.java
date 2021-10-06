/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class CheckBoxColumn<T> extends Column<T, Boolean> implements
		FieldUpdater<T, Boolean> {

	/**
	 * Creates new Instance
	 */
	public CheckBoxColumn() {
		super(new CheckboxCell(true, true));
		setFieldUpdater(this);
	}

}
