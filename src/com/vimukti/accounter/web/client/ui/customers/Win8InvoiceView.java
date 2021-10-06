package com.vimukti.accounter.web.client.ui.customers;

import com.vimukti.accounter.web.client.ui.StyledPanel;

public class Win8InvoiceView extends InvoiceView {
	@Override
	protected StyledPanel getTopLay() {
		return null;
	}

	@Override
	protected boolean canAddAttachmentPanel() {
		return false;
	}
}
