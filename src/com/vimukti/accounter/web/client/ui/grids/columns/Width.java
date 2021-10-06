/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.dom.client.Style.Unit;

/**
 * @author Prasanna Kumar G
 * 
 */
public class Width {
	double width;
	Unit unit;

	/**
	 * Creates new Instance
	 */
	public Width(double width, Unit unit) {
		this.width = width;
		this.unit = unit;
	}

	/**
	 * Creates new Instance
	 */
	public Width(double width) {
		this.width = width;
		this.unit = Unit.PX;
	}

}
