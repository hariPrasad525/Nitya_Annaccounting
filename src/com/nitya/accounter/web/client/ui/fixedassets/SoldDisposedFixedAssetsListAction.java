/**
 * 
 */
package com.nitya.accounter.web.client.ui.fixedassets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * @author Murali.A
 * 
 */
public class SoldDisposedFixedAssetsListAction extends Action {


	/**
	 * @param text
	 */
	public SoldDisposedFixedAssetsListAction() {
		super();
		this.catagory = messages.fixedAssets();
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.core.Action#getBigImage()
	 */
	@Override
	public ImageResource getBigImage() {
		return null;
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.core.Action#getSmallImage()
	 */
	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().soldDisposedItemsList();
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.core.Action#getView()
	 */

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.core.Action#run(java.lang.Object,
	 * java.lang.Boolean)
	 */
	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				SoldAndDisposedItemsListView view = new SoldAndDisposedItemsListView();
				MainFinanceWindow.getViewManager().showView(view, null, false,
						SoldDisposedFixedAssetsListAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//			}
//
//		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Sold_Disposed_Items_List.png";
	// }

	@Override
	public String getHistoryToken() {
		return "soldDisposedFixedAssets";
	}

	@Override
	public String getHelpToken() {
		return "sold-disposed-fixed-assets";
	}

	@Override
	public String getText() {
		return messages.soldDisposedItems();
	}

}
