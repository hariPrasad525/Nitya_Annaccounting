package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.nitya.accounter.web.client.core.ClientQuantity;

public abstract class QuantityColumn<T> extends Column<T, ClientQuantity> implements FieldUpdater<T, ClientQuantity>{

	public QuantityColumn() {
		super(new QuantityCell());
		this.setSortable(true);
	}


}
