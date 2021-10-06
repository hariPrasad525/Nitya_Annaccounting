package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AdjustTAXAction extends Action {
	private ClientTAXAgency vatAgency;

	int type;
	public AdjustTAXAction(int type) {
		super();
		type = type;
		this.catagory = messages.tax();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vatAdjustment();
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AdjustTAXView view;
				if (isDependent) {
					view = new AdjustTAXView(vatAgency);
					MainFinanceWindow.getViewManager().showView(view, null,
							isDependent, AdjustTAXAction.this);
				} else {
					view = new AdjustTAXView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, AdjustTAXAction.this);
				}

				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//
//				
//			}
//		});

	}

	public void setVatAgency(ClientTAXAgency selectedVatAgency) {
		this.vatAgency = selectedVatAgency;
	}

	@Override
	public String getHistoryToken() {
		return "taxAdjustment";
	}

	@Override
	public String getHelpToken() {
		return "adjust-tax";
	}

	@Override
	public String getText() {
		if(type == 1){
			return messages.vatAdjustment();
		}else{
		    return messages.taxAdjustment();
		}
	}

}
