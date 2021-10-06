package com.nitya.accounter.text.commands.objectlists;

import com.nitya.accounter.text.ITextData;
import com.nitya.accounter.text.ITextResponse;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.server.AccounterExportCSVImpl;

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
