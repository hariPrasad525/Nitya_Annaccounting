package com.vimukti.accounter.web.client.ui.core;

public interface IPrintableView {
	/**
	 * Returns true if the data is printable, used to enable print method on the
	 * view
	 * 
	 * @return
	 */
	public boolean canPrint();

	public boolean canExportToCsv();
}
