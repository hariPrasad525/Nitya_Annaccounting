package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.images.FinanceImages;
import com.nitya.accounter.web.client.ui.Accounter;

public class IndicatorColumn extends
		Column<ClientTransactionItem, ImageResource> {

	private final FinanceImages images;

	public IndicatorColumn() {
		super(new ImageResourceCell());
		images = Accounter.getFinanceImages();
		setSortable(true);
	}

	@Override
	public ImageResource getValue(ClientTransactionItem object) {
		switch (object.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			return images.Accounts();
		case ClientTransactionItem.TYPE_ITEM:
			return images.itemsIcon();
		default:
			return null;
		}

	}

}
