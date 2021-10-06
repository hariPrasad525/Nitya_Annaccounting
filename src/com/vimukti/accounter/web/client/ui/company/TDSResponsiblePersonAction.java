package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.vat.TDSResponsiblePersonDetailsView;

public class TDSResponsiblePersonAction extends Action {


	public TDSResponsiblePersonAction() {
		super();
		this.catagory = messages.deducatorMasters();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				TDSResponsiblePersonDetailsView view = GWT
						.create(TDSResponsiblePersonDetailsView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, TDSResponsiblePersonAction.this);
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

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "personDetails";
	}

	@Override
	public String getHelpToken() {
		return "personDetails";
	}

	@Override
	public String getText() {
		return messages.particularForPersonResponsibleForTaxDeduction();
	}
}
