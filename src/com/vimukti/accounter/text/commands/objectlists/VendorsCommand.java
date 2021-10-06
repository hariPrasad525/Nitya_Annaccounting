package com.vimukti.accounter.text.commands.objectlists;

import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.server.AccounterExportCSVImpl;

/**
 * 
 * @author vimukti10
 * 
 */
public class VendorsCommand extends AbstractObjectListCommand {

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		AccounterExportCSVImpl accounterExportCSVImpl = new AccounterExportCSVImpl();
		String payeeListExportCsv = accounterExportCSVImpl
				.getPayeeListExportCsv(ClientPayee.TYPE_VENDOR, true);
		String renameFile = getRenameFilePath(payeeListExportCsv, "Vendors.csv");
		respnse.addFile(renameFile);
	}
}
