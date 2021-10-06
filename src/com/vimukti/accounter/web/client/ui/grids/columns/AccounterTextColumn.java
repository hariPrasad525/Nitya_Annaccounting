/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.TextCell;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class AccounterTextColumn<R> extends CustomColumn<R, String> {
	/**
	 * Creates new Instance
	 */
	public AccounterTextColumn() {
		super(new TextCell());
	}

}
