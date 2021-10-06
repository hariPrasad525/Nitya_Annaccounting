package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.AccounterAsync;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class VendorTransactionHistoryListAction  extends Action{

		private VendorTransactionHistoryListView view;
		private ClientVendor selected;

		public VendorTransactionHistoryListAction(ClientVendor selectedVendor) {
			super();
			selected = selectedVendor;
		}


		public void run() {
			runAsync(data, isDependent);

		}

		private void runAsync(final Object data, final Boolean isDependent) {
			AccounterAsync.createAsync(new CreateViewAsyncCallback() {

				@Override
				public void onCreated() {
					view = new VendorTransactionHistoryListView();
					view.setSelectedVendor(selected);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, VendorTransactionHistoryListAction.this);

				}

			});
		}

		@Override
		public ImageResource getBigImage() {
			return null;
		}

		@Override
		public ImageResource getSmallImage() {
			return null;
		}

		@Override
		public String getHistoryToken() {
			return "vendorTransactionHistoryListAction";
		}

		@Override
		public String getHelpToken() {
			return null;
		}

		@Override
		public String getText() {
			return null;
		}



}
