package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar kasimala
 */

public class NewQuoteAction extends Action {

	private int type;
	private String title;

	public NewQuoteAction(int type) {
		super();
		this.type = type;

		if (type == ClientEstimate.QUOTES) {
			title = messages.quote();
		} else if (type == ClientEstimate.CHARGES) {
			title = messages.charge();
		} else if (type == ClientEstimate.CREDITS) {
			title = messages.credit();
		} else if (type == ClientEstimate.SALES_ORDER) {
			title = messages.salesOrder();
		}

	}

	public NewQuoteAction(ClientEstimate quote,
			AccounterAsyncCallback<Object> callback, int type) {
		super();
		this.type = type;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				if (data != null) {
					type = ((ClientEstimate) data).getEstimateType();
				}
				switch (type) {
				case ClientEstimate.QUOTES:
					title = messages.quote();
					break;
				case ClientEstimate.CHARGES:
					title = messages.charge();
					break;
				case ClientEstimate.CREDITS:
					title = messages.credit();
					break;
				case ClientEstimate.SALES_ORDER:
					title = messages.salesOrder();
				default:
					break;
				}
				QuoteView view = GWT.create(QuoteView.class);
				view.setTitle(title);
				view.setType(type);

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewQuoteAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		//
		// }
		//
		// });
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newQuote();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/new_quote.png";
	// }

	@Override
	public String getHistoryToken() {
		if (type == ClientEstimate.QUOTES) {
			return "newQuote";
		} else if (type == ClientEstimate.CHARGES) {
			return "newCharge";
		} else if (type == ClientEstimate.CREDITS) {
			return "newCredit";
		} else if (type == ClientEstimate.SALES_ORDER) {
			return "salesOrder";
		}
		return "";
	}

	@Override
	public String getHelpToken() {
		return "customer-quote";
	}

	@Override
	public String getText() {
		if (type == ClientEstimate.QUOTES) {
			title = messages.newQuote();
		} else if (type == ClientEstimate.CHARGES) {
			title = messages.newCharge();
		} else if (type == ClientEstimate.CREDITS) {
			title = messages.newCredit();
		} else if (type == ClientEstimate.SALES_ORDER) {
			title = messages.newSalesOrder();
		}
		return title;
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	@Override
	public String getViewName() {
		String viewName = "";
		if (type == ClientEstimate.QUOTES) {
			viewName = messages.quote();
		} else if (type == ClientEstimate.CHARGES) {
			viewName = messages.charge();
		} else if (type == ClientEstimate.CREDITS) {
			viewName = messages.credit();
		} else if (type == ClientEstimate.SALES_ORDER) {
			viewName = messages.salesOrder();
		}
		return viewName;
	}
}
