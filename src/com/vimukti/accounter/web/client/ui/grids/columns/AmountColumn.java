package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

public abstract class AmountColumn<T> extends Column<T, Double> implements FieldUpdater<T, Double> {

	public AmountColumn() {
		super(new AmountCell());
		setSortable(true);
		setFieldUpdater(this);
	}


}
