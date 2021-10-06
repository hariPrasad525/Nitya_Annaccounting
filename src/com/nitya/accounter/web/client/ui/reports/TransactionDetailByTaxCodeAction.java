package com.nitya.accounter.web.client.ui.reports;
//package com.nitya.accounter.web.client.ui.reports;
//
//import com.google.gwt.resources.client.ImageResource;
//import com.nitya.accounter.web.client.ui.Accounter;
//import com.nitya.accounter.web.client.ui.core.AccounterAsync;
//import com.nitya.accounter.web.client.ui.core.Action;
//import com.nitya.accounter.web.client.ui.core.CreateViewAsyncCallback;
//
///**
// * 
// * @author Mandeep Singh
// */
//
//public class TransactionDetailByTaxCodeAction extends Action {
//
//	public TransactionDetailByTaxCodeAction(String text) {
//		super(text);
//		this.catagory = messages.report();
//	}
//
//	public void runAsync(final Object data, final Boolean isDependent) {
//
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				Accounter.showInformation(messages
//						.thisReportNotYetImplemented());
//
//			}
//
//		});
//	}
//
//	// @Override
//	// public ParentCanvas getView() {
//	// // not required for this class
//	// return null;
//	// }
//
//	@Override
//	public void run() {
//		runAsync(data, isDependent);
//	}
//
//	public ImageResource getBigImage() {
//		return null;
//	}
//
//	public ImageResource getSmallImage() {
//		return Accounter.getFinanceMenuImages().reports();
//	}
//
//	// @Override
//	// public String getImageUrl() {
//	// return "/images/reports.png";
//	// }
//
//	@Override
//	public String getHistoryToken() {
//
//		return "transactionDetailByTaxCode";
//	}
//
//	@Override
//	public String getHelpToken() {
//		return "transaction-by-item";
//	}
//
//}
