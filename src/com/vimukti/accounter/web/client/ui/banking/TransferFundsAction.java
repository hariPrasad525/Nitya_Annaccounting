//package com.vimukti.accounter.web.client.ui.banking;
//
//import com.google.gwt.resources.client.ImageResource;
//import com.vimukti.accounter.web.client.AccounterAsyncCallback;
//import com.vimukti.accounter.web.client.ui.Accounter;
//import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
//import com.vimukti.accounter.web.client.ui.core.Action;
//import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
//
//public class TransferFundsAction extends Action {
//
//	public TransferFundsAction() {
//		super();
//		this.catagory = messages.banking();
//	}
//
//	public TransferFundsAction(ClientTransferFund transferFund,
//			AccounterAsyncCallback<Object> callback) {
//		super();
//		this.catagory = messages.banking();
//	}
//
//	@Override
//	public void run() {
//		runAsync(data, isDependent);
//	}
//
//	public void runAsync(final Object data, final Boolean isDependent) {
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				new TransferFundsDialog(data).show();
//
//			}
//
//		});
//	}
//
//	public ImageResource getBigImage() {
//		return null;
//	}
//
//	public ImageResource getSmallImage() {
//		return Accounter.getFinanceMenuImages().transerFunds();
//	}
//
//	@Override
//	public String getHistoryToken() {
//		return "transferFunds";
//	}
//
//	@Override
//	public String getHelpToken() {
//		return "transfer-funds";
//	}
//
//	@Override
//	public String getText() {
//		return messages.transferFunds();
//	}
// }
